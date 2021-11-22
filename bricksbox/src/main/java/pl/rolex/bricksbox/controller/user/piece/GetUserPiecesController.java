package pl.rolex.bricksbox.controller.user.piece;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.piece.GetUserPiecesService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/piece")
@RequiredArgsConstructor
public class GetUserPiecesController {

    private final GetUserPiecesService getUserPiecesService;

    @GetMapping("/{withShoppingList}")
    public Mono<ResponseEntity<?>> getPieces(@PathVariable boolean withShoppingList,
                                              @RequestHeader("Authorization") String token) {
        return this.getUserPiecesService.getPieces(token, withShoppingList);
    }
}
