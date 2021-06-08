package pl.rolex.bricksbox.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rolex.bricksbox.controller.user.set.OwnSetRequest;
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
}
