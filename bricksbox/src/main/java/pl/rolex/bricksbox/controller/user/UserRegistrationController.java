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
import pl.rolex.bricksbox.service.user.UserRegistrationService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("api/user/register")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("")
    public Mono<ResponseEntity<?>> register(@RequestBody @Valid AuthRequest authRequest) {
        return this.userRegistrationService.register(authRequest).map(this::registrationResponse);
    }

    private ResponseEntity<?> registrationResponse(AuthResponse response) {
        if (Objects.isNull(response.getToken())) {
            return userAlreadyExistsResponse();
        }
        return userAuthenticatedResponse(response);
    }

    private ResponseEntity<ErrorResponse> userAlreadyExistsResponse() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message("User already exists!")
                        .build());
    }

    private ResponseEntity<AuthResponse> userAuthenticatedResponse(AuthResponse response) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                AuthResponse.builder()
                        .token(response.getToken())
                        .build());
    }
}
