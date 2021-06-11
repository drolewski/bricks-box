package pl.rolex.bricksbox.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.rolex.bricksbox.model.Set;
import reactor.core.publisher.Mono;

public interface SetRepository extends ReactiveCrudRepository<Set, String> {
    Mono<Set> findByModelId(String modelId);
}
