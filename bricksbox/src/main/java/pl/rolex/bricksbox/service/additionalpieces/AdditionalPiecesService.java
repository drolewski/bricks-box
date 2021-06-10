package pl.rolex.bricksbox.service.additionalpieces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rolex.bricksbox.controller.user.additionalpieces.UpdateAdditionalPieces;
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
    public Mono<ResponseEntity<?>> update(final UpdateAdditionalPieces updateAdditionalPieces) {
        try {
            if (jsonWebToken.isTokenValid(updateAdditionalPieces.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(updateAdditionalPieces.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> updateUserAdditionalPieces(user, updateAdditionalPieces));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> updateUserAdditionalPieces(User user, UpdateAdditionalPieces updateAdditionalPieces) {
        final Optional<Piece> filteredPiece = user.getAdditionalPieces().stream()
                .filter(piece -> isPieceEqual(updateAdditionalPieces, piece))
                .collect(Collectors.toList()).stream().findFirst();
        if (filteredPiece.isPresent()) {
            filteredPiece.ifPresent(piece -> piece.setCount(piece.getCount() + updateAdditionalPieces.getCount()));
        } else {
            user.getAdditionalPieces().add(Piece.builder()
                    .element(updateAdditionalPieces.getElement())
                    .design(updateAdditionalPieces.getDesign())
                    .colour(updateAdditionalPieces.getColour())
                    .count(updateAdditionalPieces.getCount())
                    .img_url(updateAdditionalPieces.getImgUrl())
                    .build()
            );
        }
        return this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build()));
    }

    private boolean isPieceEqual(UpdateAdditionalPieces updateAdditionalPieces, Piece piece) {
        return piece.getElement().equals(updateAdditionalPieces.getElement()) && piece.getDesign().equals(updateAdditionalPieces.getDesign());
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
