package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
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

    public Mono<List<Set>> get(final String token) {
        try {
            if (jsonWebToken.isTokenValid(token)) {
                final var username = jsonWebToken.getUsernameFromToken(token);
                return this.userRepository.findByUserName(username)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(this::getUserPieces)
                        .flatMap(this::getUserBuildPossibilities);
            }
            return Mono.just(Collections.emptyList());
        } catch (Exception ex) {
            return Mono.just(Collections.emptyList());
        }
    }

    private Mono<List<Piece>> getUserPieces(User user) {
        final List<Piece> userPieces = user.getAdditionalPieces();
        user.getSets().forEach(set -> userPieces.addAll(set.getPieces()));
        log.info("{}", userPieces);
        return Mono.just(userPieces);
    }

    private Mono<List<Set>> getUserBuildPossibilities(List<Piece> pieces) {
        return this.setRepository.findAll().buffer().flatMap(sets -> getBuildPossibilitiesWithPieces(sets, pieces)).single();
    }

    private Mono<List<Set>> getBuildPossibilitiesWithPieces(List<Set> sets, List<Piece> pieces) {
        return Mono.just(sets.stream()
                .filter(s -> pieces.containsAll(s.getPieces()))
                .collect(Collectors.toList()));
    }

}
