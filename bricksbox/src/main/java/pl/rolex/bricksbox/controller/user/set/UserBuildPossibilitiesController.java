package pl.rolex.bricksbox.controller.user.set;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.service.set.BuildPossibilitiesService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/user/build-possibilities")
@RequiredArgsConstructor
public class UserBuildPossibilitiesController {

    private final BuildPossibilitiesService buildPossibilitiesService;

    @GetMapping("/{withShoppingList}")
    public Mono<ResponseEntity<?>> get(@RequestHeader("Authorization") String token, @PathVariable boolean withShoppingList) {
        return buildPossibilitiesService.get(token, withShoppingList).flatMap(sets -> {
            if (!Collections.isEmpty(sets)) {
                return userBuildPossibilities(sets);
            }
            return emptyBuildList();
        });
    }

    private Mono<ResponseEntity<?>> userBuildPossibilities(List<Set> sets) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(sets));
    }

    private Mono<ResponseEntity<?>> emptyBuildList() {
        return Mono.just(ResponseEntity.noContent().build());
    }
}
