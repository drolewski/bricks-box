package pl.rolex.bricksbox.service.user;

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

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final Pbkdf2PasswordEncoderAdapter encoderAdapter;
    private final JSONWebToken JSONWebToken;
    private final UserRepository userRepository;

    @Transactional
    public Mono<AuthResponse> register(final AuthRequest authRequest) {
        return this.userRepository.findByUserName(authRequest.getUserName())
                .defaultIfEmpty(EmptyUserSingleton.getInstance())
                .flatMap(u -> getRegistrationResponse(authRequest, u));
    }

    private Mono<AuthResponse> getRegistrationResponse(AuthRequest authRequest, User u) {
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

    @Transactional
    public Mono<AuthResponse> login(final AuthRequest authRequest) {
        return this.userRepository.findByUserName(authRequest.getUserName())
                .defaultIfEmpty(EmptyUserSingleton.getInstance())
                .flatMap(u -> getLoginResponse(authRequest, u));
    }

    private Mono<AuthResponse> getLoginResponse(AuthRequest authRequest, User user) {
        if (authRequest.getUserName().equals(user.getUsername()) && encoderAdapter.matches(authRequest.getPassword(), user.getPassword())) {
            return loginUser(authRequest);
        }
        return notAuthenticated();
    }

    private Mono<AuthResponse> loginUser(AuthRequest authRequest) {
        final User user = new User(
                authRequest.getUserName(),
                this.encoderAdapter.encode(authRequest.getPassword())
        );
        return Mono.just(new AuthResponse(JSONWebToken.generateToken(user)));
    }

    private Mono<AuthResponse> notAuthenticated() {
        return Mono.just(new AuthResponse(null));
    }
}
