package pl.rolex.bricksbox.service.piece;

import lombok.extern.log4j.Log4j2;
import pl.rolex.bricksbox.model.Piece;
import pl.rolex.bricksbox.model.User;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class GetUserPieces {

    public Mono<Map<String, Integer>> getUserPieces(User user) {
        final Map<String, Integer> userPieces = user.getAdditionalPieces().stream().collect(Collectors.groupingBy(Piece::getDesign, Collectors.summingInt(Piece::getCount)));
        user.getSets().forEach(set -> set.getPieces().stream().collect(Collectors.groupingBy(Piece::getDesign, Collectors.summingInt(Piece::getCount))).forEach((key, value) -> userPieces.merge(key, value, Integer::sum)));
        log.info("{}", userPieces);
        return Mono.just(userPieces);
    }
}
