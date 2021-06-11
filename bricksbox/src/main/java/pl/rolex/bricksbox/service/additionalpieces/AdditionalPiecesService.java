package pl.rolex.bricksbox.service.additionalpieces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rolex.bricksbox.controller.user.additionalpieces.DeletePiece;
import pl.rolex.bricksbox.controller.user.additionalpieces.UpdatePiece;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionalPiecesService {

    private final JSONWebToken jsonWebToken;
    private final UserRepository userRepository;

    @Transactional
    public Mono<ResponseEntity<?>> update(final UpdatePiece updatePiece) {
        try {
            if (jsonWebToken.isTokenValid(updatePiece.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(updatePiece.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> updateUserAdditionalPieces(user, updatePiece));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> updateUserAdditionalPieces(User user, UpdatePiece updatePiece) {
        final Optional<Piece> filteredPiece = user.getAdditionalPieces().stream()
                .filter(piece -> isPieceEqual(updatePiece, piece))
                .collect(Collectors.toList()).stream().findFirst();
        if (filteredPiece.isPresent()) {
            filteredPiece.ifPresent(piece -> piece.setCount(updatePiece.getCount()));
        } else {
            user.getAdditionalPieces().add(Piece.builder()
                    .element(updatePiece.getElement())
                    .design(updatePiece.getDesign())
                    .colour(updatePiece.getColour())
                    .count(updatePiece.getCount())
                    .img_url(updatePiece.getImgUrl())
                    .build()
            );
        }
        return this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build()));
    }

    private boolean isPieceEqual(UpdatePiece updatePiece, Piece piece) {
        return piece.getElement().equals(updatePiece.getElement()) && piece.getDesign().equals(updatePiece.getDesign());
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public Mono<ResponseEntity<?>> getAdditionalPieces(UserTokenRequest tokenRequest) {
        try {
            if (jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(this::getUserAdditionalPieces);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> getUserAdditionalPieces(User user) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user.getAdditionalPieces()));
    }

    public Mono<ResponseEntity<?>> delete(final DeletePiece deletePiece) {
        try {
            if (jsonWebToken.isTokenValid(deletePiece.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(deletePiece.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> deleteUserAdditionalPiece(user, deletePiece));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> deleteUserAdditionalPiece(final User user, final DeletePiece deletePiece) {
        user.getAdditionalPieces().removeIf(piece -> isRemovable(deletePiece, piece));
        return this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build()));
    }

    private boolean isRemovable(DeletePiece deletePiece, Piece piece) {
        return piece.getDesign().equals(deletePiece.getDesign()) &&
                piece.getElement().equals(deletePiece.getElement()) &&
                piece.getColour().equals(deletePiece.getColour());
    }
}
