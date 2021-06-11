package pl.rolex.bricksbox.controller.user.additionalpieces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.additionalpieces.AdditionalPiecesService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/additional-pieces")
@RequiredArgsConstructor
public class AdditionalPiecesController {

    private final AdditionalPiecesService additionalPiecesService;

    @PostMapping("update")
    public Mono<ResponseEntity<?>> update(@RequestBody UpdateAdditionalPieces updateAdditionalPieces) {
        return this.additionalPiecesService.update(updateAdditionalPieces);
    }

    @GetMapping("")
    public Mono<ResponseEntity<?>> getAdditionalPieces(@RequestBody UserTokenRequest tokenRequest) {
        return additionalPiecesService.getAdditionalPieces(tokenRequest);
    }
}
