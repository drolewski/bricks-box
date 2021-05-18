package pl.rolex.bricksbox.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.rolex.bricksbox.model.Set;

public interface SetRepository extends ReactiveCrudRepository<Set, String> {
}
