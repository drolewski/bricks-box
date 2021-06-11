package pl.rolex.bricksbox.service.set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.response.ErrorResponse;
import pl.rolex.bricksbox.controller.user.set.OwnSetRequest;
import pl.rolex.bricksbox.controller.user.set.OwnSetResponse;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.service.user.EmptyUserSingleton;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OwnSetService {

    private final UserRepository userRepository;
    private final SetRepository setRepository;
    private final JSONWebToken jsonWebToken;

    public Mono<ResponseEntity<?>> ownSet(final OwnSetRequest ownSetRequest) {
        try {
            if (this.jsonWebToken.isTokenValid(ownSetRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(ownSetRequest.getToken());
                return this.setRepository.findByModelId(ownSetRequest.getId()).flatMap(s ->
                        this.userRepository.findByUserName(userName)
                                .defaultIfEmpty(EmptyUserSingleton.getInstance())
                                .flatMap(u -> ownSet(u, s))
                );
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> ownSet(User user, final Set set) {
        if (user.getSets().contains(set)) {
            return Mono.just(removeUserSet(user, set));
        }
        return Mono.just(addUserSet(user, set));
    }

    private ResponseEntity<OwnSetResponse> removeUserSet(User user, Set set) {
        user.getSets().remove(set);
        this.userRepository.save(user).subscribe();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(OwnSetResponse.builder()
                        .message("You no longer own set!")
                        .build());
    }

    private ResponseEntity<OwnSetResponse> addUserSet(User user, Set set) {
        user.getSets().add(set);
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

    public Mono<ResponseEntity<?>> getSets(UserTokenRequest tokenRequest) {
        try {
            if (this.jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(this::getSets);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> getSets(User user) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user.getSets()));
    }
}
