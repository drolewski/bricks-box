package pl.rolex.bricksbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.service.GetSetsService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/sets")
@RequiredArgsConstructor
public class SetsController {
    private final GetSetsService getSetsService;

    @GetMapping("")
    public Flux<Set> getSets() {
        return this.getSetsService.getSets();
    }
}
