package pl.rolex.bricksbox.controller.user.set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.set.OwnSetService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/sets")
@RequiredArgsConstructor
public class UserSetsController {

    private final OwnSetService ownSetService;

    @PostMapping("own")
    public Mono<ResponseEntity<?>> ownSet(@RequestBody OwnSetRequest ownSetRequest) {
        return this.ownSetService.ownSet(ownSetRequest);
    }

    @GetMapping("")
    public Mono<ResponseEntity<?>> getUserSets(@RequestHeader("Authorization") String token){
        return this.ownSetService.getSets(token);
    }
}
