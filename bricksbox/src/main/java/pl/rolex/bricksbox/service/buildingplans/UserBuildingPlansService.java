package pl.rolex.bricksbox.service.buildingplans;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.model.BuildingSet;
import pl.rolex.bricksbox.model.User;
import pl.rolex.bricksbox.repository.SetRepository;
import pl.rolex.bricksbox.repository.UserRepository;
import pl.rolex.bricksbox.security.JSONWebToken;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserBuildingPlansService {

    private final JSONWebToken jsonWebToken;
    private final UserRepository userRepository;
    private final SetRepository setRepository;

    public Mono<ResponseEntity<?>> getBuildingPlans(UserTokenRequest tokenRequest) {
        try {
            if (jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(this::getUserBuildingPlans);
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> getUserBuildingPlans(User user) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user.getBuildPlans()));
    }

    private Mono<ResponseEntity<?>> unauthorized() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public Mono<ResponseEntity<?>> addBuildingPlan(UserTokenRequest tokenRequest, String modelId) {
        try {
            if (jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> addUserBuildingPlan(user, modelId));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> addUserBuildingPlan(User user, String modelId) {
        return this.setRepository.findByModelId(modelId)
                .doOnNext(set -> user.getBuildPlans().add(BuildingSet.builder()
                        .modelId(set.getModelId())
                        .name(set.getName())
                        .theme(set.getTheme())
                        .imgLink(set.getImg_link())
                        .year(set.getYear())
                        .numberOfPieces(set.getPieces_num())
                        .areInstructions(set.getInstructions().size() > 0)
                        .isBuilt(false)
                        .build()))
                .then(this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build())));
    }

    public Mono<ResponseEntity<?>> deleteBuildingPlan(UserTokenRequest tokenRequest, String modelId) {
        try {
            if (jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> deleteUserBuildingPlan(user, modelId));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> deleteUserBuildingPlan(User user, String modelId) {
        return this.setRepository.findByModelId(modelId)
                .doOnNext(set -> user.getBuildPlans().removeIf(s -> s.getModelId().equals(modelId)))
                .then(this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build())));
    }

    public Mono<ResponseEntity<?>> changeBuildingPlanStatus(UserTokenRequest tokenRequest, String modelId) {
        try {
            if (jsonWebToken.isTokenValid(tokenRequest.getToken())) {
                var userName = jsonWebToken.getUsernameFromToken(tokenRequest.getToken());
                return this.userRepository.findByUserName(userName)
                        .flatMap(user -> changeUserBuildingStatus(user, modelId));
            }
            return unauthorized();
        } catch (Exception ex) {
            return unauthorized();
        }
    }

    private Mono<ResponseEntity<?>> changeUserBuildingStatus(User user, String modelId) {
        user.getBuildPlans().stream().filter(set -> set.getModelId().equals(modelId))
                .findFirst().ifPresent(s -> s.setBuilt(changeState(s)));
        return this.userRepository.save(user).then(Mono.just(ResponseEntity.ok().build()));
    }

    private boolean changeState(BuildingSet buildingSet) {
        return !buildingSet.isBuilt();
    }
}
