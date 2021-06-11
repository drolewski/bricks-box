package pl.rolex.bricksbox.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rolex.bricksbox.controller.response.ErrorResponse;
import pl.rolex.bricksbox.security.model.AuthRequest;
import pl.rolex.bricksbox.security.model.AuthResponse;
import pl.rolex.bricksbox.service.user.UserAuthenticationService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;


@RestController
@RequestMapping("/api/user/login")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("")
    public Mono<ResponseEntity<?>> login(@RequestBody @Valid AuthRequest authRequest) {
        return this.userAuthenticationService.login(authRequest).map(this::loginResponse);
    }

    private ResponseEntity<?> loginResponse(AuthResponse response) {
        if (Objects.isNull(response.getToken())) {
            return userNotExist();
        }
        return userLoggedInResponse(response);
    }

    private ResponseEntity<?> userNotExist() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message("Wrong login or passwords")
                        .build());
    }

    private ResponseEntity<?> userLoggedInResponse(AuthResponse response) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                AuthResponse.builder()
                        .token(response.getToken())
                        .build()
        );
    }
}
