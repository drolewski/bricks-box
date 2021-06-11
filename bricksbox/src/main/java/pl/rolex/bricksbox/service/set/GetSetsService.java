package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.set.UserSetResponse;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.DesignPiece;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.piece.GetUserPiecesService;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GetSetsService {

    private final SetRepository setRepository;
    private final UserRepository userRepository;
    private final JSONWebToken jsonWebToken;
    private final GetUserPiecesService getUserPiecesService;

    public Flux<Set> getAllSets() {
        return this.setRepository.findAll();
    }

    public Mono<ResponseEntity<?>> getUserSet(UserTokenRequest userTokenRequest, String modelId, boolean withShoppingList) {
        try {
            if (jsonWebToken.isTokenValid(userTokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(userTokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(user -> this.getUserPiecesService.getUserPieces(user, withShoppingList))
                        .flatMap(userPieces -> getModelMissingPieces(userPieces, modelId))
                        .flatMap(this::getResponseEntity);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<UserSetResponse> getModelMissingPieces(List<DesignPiece> userPieces, String modelId) {
        return this.setRepository.findByModelId(modelId).flatMap(set -> getUserSetResponse(userPieces, set));
    }

    private Mono<UserSetResponse> getUserSetResponse(List<DesignPiece> userPieces, Set set) {
        return Mono.just(UserSetResponse.builder()
                .modelId(set.getModelId())
                .name(set.getName())
                .theme(set.getTheme())
                .imgLink(set.getImg_link())
                .year(set.getYear())
                .pieces(set.getPieces())
                .instructions(set.getInstructions())
                .piecesNum(set.getPieces_num())
                .piecesUrl(set.getPieces_url())
                .missingPieces(set.getPieces().stream()
                        .filter(p -> !hasEnoughPieces(userPieces, p))
                        .map(piece -> subtractPieces(piece, userPieces))
                        .collect(Collectors.toList()))
                .build());
    }

    private boolean hasEnoughPieces(List<DesignPiece> userPieces, Piece p) {
        return userPieces.stream().anyMatch(piece -> piece.getDesign().equals(p.getDesign()) && piece.getCount() >= p.getCount());
    }

    private Piece subtractPieces(Piece piece, List<DesignPiece> userPieces) {
        final Optional<DesignPiece> foundPiece = userPieces.stream().filter(p -> p.getDesign().equals(piece.getDesign())).findAny();
        if (foundPiece.isPresent()) {
            return Piece.builder()
                    .element(piece.getElement())
                    .design(piece.getDesign())
                    .colour(piece.getColour())
                    .count(piece.getCount() - foundPiece.get().getCount())
                    .img_url(piece.getImg_url())
                    .build();
        }
        return Piece.builder()
                .element(piece.getElement())
                .design(piece.getDesign())
                .colour(piece.getColour())
                .count(piece.getCount())
                .img_url(piece.getImg_url())
                .build();
    }


    private Mono<ResponseEntity<?>> getResponseEntity(UserSetResponse response) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response));
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
