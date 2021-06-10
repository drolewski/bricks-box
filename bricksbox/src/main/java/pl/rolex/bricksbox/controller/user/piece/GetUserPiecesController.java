package pl.rolex.bricksbox.controller.user.piece;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.piece.GetUserPiecesService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/piece")
@RequiredArgsConstructor
public class GetUserPiecesController {

    private final GetUserPiecesService getUserPiecesService;

    @GetMapping("")
    public Mono<ResponseEntity<?>> getPieces(@RequestBody UserTokenRequest request) {
        return this.getUserPiecesService.getPieces(request);
    }
}
