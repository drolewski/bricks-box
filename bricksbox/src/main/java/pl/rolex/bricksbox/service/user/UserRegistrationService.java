package pl.rolex.bricksbox.service.user;

import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import pl.rolex.bricksbox.security.Pbkdf2PasswordEncoderAdapter;
import pl.rolex.bricksbox.security.model.AuthRequest;
import pl.rolex.bricksbox.security.model.AuthResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final Pbkdf2PasswordEncoderAdapter encoderAdapter;
    private final JSONWebToken JSONWebToken;
    private final UserRepository userRepository;

    @Transactional
    public Mono<AuthResponse> register(final AuthRequest authRequest) {
        return this.userRepository.findByUserName(authRequest.getUserName())
                .defaultIfEmpty(EmptyUserSingleton.getInstance())
                .flatMap(u -> getAuthResponseMono(authRequest, u));
    }

    private Mono<AuthResponse> getAuthResponseMono(AuthRequest authRequest, User u) {
        if (!authRequest.getUserName().equals(u.getUsername())) {
            return saveUser(authRequest);
        }
        return notAuthenticated();
    }

    private Mono<AuthResponse> saveUser(AuthRequest authRequest) {
        final User user = new User(
                authRequest.getUserName(),
                this.encoderAdapter.encode(authRequest.getPassword())
        );
        return this.userRepository.save(user).map(__ -> new AuthResponse(JSONWebToken.generateToken(user)));
    }

    private Mono<AuthResponse> notAuthenticated() {
        return Mono.just(new AuthResponse(null));
    }
}
