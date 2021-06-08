package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.response.ErrorResponse;
import pl.rolex.bricksbox.controller.user.set.OwnSetRequest;
import pl.rolex.bricksbox.controller.user.set.OwnSetResponse;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OwnSetService {

    private final UserRepository userRepository;
    private final JSONWebToken jsonWebToken;

    public Mono<ResponseEntity<?>> ownSet(final OwnSetRequest ownSetRequest) {
        try {
            if (this.jsonWebToken.isTokenValid(ownSetRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(ownSetRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .defaultIfEmpty(EmptyUserSingleton.getInstance())
                        .flatMap(u -> ownSet(u, ownSetRequest.getId()));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> ownSet(User user, final String id) {
        if (user.getSets().contains(id)) {
            return Mono.just(removeUserSet(user, id));
        }
        return Mono.just(addUserSet(user, id));
    }

    private ResponseEntity<OwnSetResponse> removeUserSet(User user, String id) {
        user.getSets().remove(id);
        this.userRepository.save(user).subscribe();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(OwnSetResponse.builder()
                        .message("You no longer own set!")
                        .build());
    }

    private ResponseEntity<OwnSetResponse> addUserSet(User user, String id) {
        user.getSets().add(id);
        this.userRepository.save(user).subscribe();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(OwnSetResponse.builder()
                        .message("You own new set!")
                        .build());
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ErrorResponse.builder()
                                .message("Login or register")
                                .build()
                        )
        );
    }
}
