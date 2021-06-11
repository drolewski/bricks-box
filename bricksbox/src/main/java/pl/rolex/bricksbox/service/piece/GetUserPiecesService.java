package pl.rolex.bricksbox.service.piece;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.DesignPiece;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GetUserPiecesService {

    private final UserRepository userRepository;
    private final JSONWebToken jsonWebToken;

    public Mono<ResponseEntity<?>> getPieces(UserTokenRequest request) {
        try {
            if (jsonWebToken.isTokenValid(request.getToken())) {
                final var username = jsonWebToken.getUsernameFromToken(request.getToken());
                return this.userRepository.findByUserName(username)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(this::getUserPieces)
                        .flatMap(this::getResponseEntity);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    public Mono<List<DesignPiece>> getUserPieces(User user) {
        Map<Piece, Integer> piecesWithCounter = getPiecesMap(user);
        List<DesignPiece> designPieces = new ArrayList<>();
        piecesWithCounter.forEach((key, value) -> designPieces.add(new DesignPiece(key.getDesign(), value, key.getImg_url())));
        log.info("{}", designPieces);
        return Mono.just(designPieces);
    }

    private Map<Piece, Integer> getPiecesMap(User user) {
        Map<Piece, Integer> piecesWithCounter = new HashMap<>();
        user.getAdditionalPieces().stream().collect(Collectors.groupingBy(Piece::getDesign, Collectors.summingInt(Piece::getCount)))
                .forEach((key, value) -> user.getAdditionalPieces().stream()
                        .filter(piece -> piece.getDesign().equals(key))
                        .findFirst().ifPresent(piece -> piecesWithCounter.put(piece, value))
                );
        user.getSets().forEach(set -> set.getPieces().stream().collect(Collectors.groupingBy(Piece::getDesign, Collectors.summingInt(Piece::getCount)))
                .forEach((key, value) -> user.getSets().forEach(s -> set.getPieces().stream().filter(piece -> piece.getDesign().equals(key))
                        .findFirst().ifPresent(piece -> piecesWithCounter.merge(piece, value, Integer::sum))))
        );
        return piecesWithCounter;
    }

    private Mono<ResponseEntity<?>> getResponseEntity(List<DesignPiece> userPieces) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userPieces));
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}
