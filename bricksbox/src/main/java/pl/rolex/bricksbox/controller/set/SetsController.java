package pl.rolex.bricksbox.controller.set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.service.set.GetSetsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/sets")
@RequiredArgsConstructor
public class SetsController {
    private final GetSetsService getSetsService;

    @GetMapping("all")
    public Flux<Set> getSets() {
        return this.getSetsService.getAllSets();
    }

    @GetMapping("user/set/{modelId}")
    public Mono<ResponseEntity<?>> getUserSet(@PathVariable String modelId, @RequestBody UserTokenRequest userTokenRequest) {
        return this.getSetsService.getUserSet(userTokenRequest, modelId);
    }

}
