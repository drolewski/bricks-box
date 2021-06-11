package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.model.DesignPiece;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.piece.GetUserPiecesService;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BuildPossibilitiesService {

    private final UserRepository userRepository;
    private final SetRepository setRepository;
    private final JSONWebToken jsonWebToken;
    private final GetUserPiecesService getUserPiecesService;

    public Mono<List<Set>> get(final String token, final boolean withShoppingList) {
        try {
            if (jsonWebToken.isTokenValid(token)) {
                final var username = jsonWebToken.getUsernameFromToken(token);
                return this.userRepository.findByUserName(username)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(user -> this.getUserPiecesService.getUserPieces(user, withShoppingList))
                        .flatMap(this::getUserBuildPossibilities);
            }
            return Mono.just(Collections.emptyList());
        } catch (Exception ex) {
            return Mono.just(Collections.emptyList());
        }
    }

    private Mono<List<Set>> getUserBuildPossibilities(List<DesignPiece> userPieces) {
        return this.setRepository.findAll().buffer().flatMap(sets -> getBuildPossibilitiesWithPieces(sets, userPieces)).single();
    }

    private Mono<List<Set>> getBuildPossibilitiesWithPieces(List<Set> sets, List<DesignPiece> userPieces) {
        return Mono.just(sets.stream()
                .filter(set -> hasUserPiece(userPieces, set))
                .collect(Collectors.toList()));
    }

    private boolean hasUserPiece(List<DesignPiece> userPieces, Set set) {
        for (Piece pieceFromSet : set.getPieces()) {
            if (!isPieceInUserCollection(userPieces, pieceFromSet)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPieceInUserCollection(List<DesignPiece> userPieces, Piece setPiece) {
        for (DesignPiece userPiece : userPieces) {
            if (hasUserEnoughPieces(setPiece, userPiece, userPiece.getCount())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasUserEnoughPieces(Piece setPiece, DesignPiece userPiece, Integer userPieceCount) {
        return userPiece.getDesign().equals(setPiece.getDesign()) && userPieceCount >= setPiece.getCount();
    }

}
