package pl.rolex.bricksbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.model.Set;
import pl.rolex.bricksbox.repository.SetsRepository;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetSetsService {
    private final SetsRepository setsRepository;

    public Flux<Set> getSets() {
        return this.setsRepository.findAll();
    }
}
