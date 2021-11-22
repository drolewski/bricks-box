package pl.rolex.bricksbox.controller.user.shopping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.additionalpieces.DeletePiece;
import pl.rolex.bricksbox.controller.user.additionalpieces.UpdatePiece;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.shopping.UserShoppingService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/shopping")
@RequiredArgsConstructor
public class UserShoppingController {

    private final UserShoppingService userShoppingService;

    @GetMapping("")
    public Mono<ResponseEntity<?>> get(@RequestHeader("Authorization") String token) {
        return userShoppingService.getShoppingList(token);
    }

    @PostMapping("update")
    public Mono<ResponseEntity<?>> update(@RequestBody UpdatePiece updatePiece) {
        return userShoppingService.updateShoppingList(updatePiece);
    }

    @DeleteMapping("")
    public Mono<ResponseEntity<?>> delete(@RequestBody DeletePiece deletePiece) {
        return userShoppingService.deletePieceFromShoppingList(deletePiece);
    }
}
