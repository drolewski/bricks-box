package pl.rolex.bricksbox.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.rolex.bricksbox.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUserName(String userName);
}
