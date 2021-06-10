package pl.rolex.bricksbox.controller.user.additionalpieces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
