package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.set.UserSetResponse;
import pl.rolex.bricksbox.controller.user.requests.UserTokenRequest;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.piece.GetUserPieces;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GetSetsService {

    private final SetRepository setRepository;
    private final UserRepository userRepository;
    private final JSONWebToken jsonWebToken;
    private final GetUserPieces getUserPieces;

    public Flux<Set> getAllSets() {
        return this.setRepository.findAll();
    }

    public Mono<ResponseEntity<?>> getUserSet(UserTokenRequest userTokenRequest, String modelId) {
        try {
            if (jsonWebToken.isTokenValid(userTokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(userTokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(this.getUserPieces::getUserPieces)
                        .flatMap(userPieces -> getModelMissingPieces(userPieces, modelId))
                        .flatMap(this::getResponseEntity);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<UserSetResponse> getModelMissingPieces(Map<String, Integer> userPieces, String modelId) {
        return this.setRepository.findByModelId(modelId).flatMap(set -> getUserSetResponse(userPieces, set));
    }

    private Mono<UserSetResponse> getUserSetResponse(Map<String, Integer> userPieces, Set set) {
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
                        .map(piece -> subtractPieces(piece, userPieces.get(piece.getDesign()) == null ? 0 : userPieces.get(piece.getDesign())))
                        .collect(Collectors.toList()))
                .build());
    }

    private boolean hasEnoughPieces(Map<String, Integer> userPieces, Piece p) {
        return userPieces.containsKey(p.getDesign()) && userPieces.get(p.getDesign()) >= p.getCount();
    }

    private Piece subtractPieces(Piece piece, Integer sub) {
        return Piece.builder()
                .element(piece.getElement())
                .design(piece.getDesign())
                .colour(piece.getColour())
                .count(piece.getCount() - sub)
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
