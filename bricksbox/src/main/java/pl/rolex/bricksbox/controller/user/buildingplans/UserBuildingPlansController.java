package pl.rolex.bricksbox.controller.user.buildingplans;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rolex.bricksbox.controller.user.userwithtoken.UserTokenRequest;
import pl.rolex.bricksbox.service.buildingplans.UserBuildingPlansService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/user/building-plans")
@RequiredArgsConstructor
public class UserBuildingPlansController {

    private final UserBuildingPlansService userBuildingPlansService;

    @PostMapping("/{modelId}")
    public Mono<ResponseEntity<?>> addBuildingPlan(@RequestBody UserTokenRequest tokenRequest, @PathVariable String modelId) {
        return this.userBuildingPlansService.addBuildingPlan(tokenRequest, modelId);
    }

    @DeleteMapping("/{modelId}")
    public Mono<ResponseEntity<?>> deleteBuildingPlan(@RequestBody UserTokenRequest tokenRequest, @PathVariable String modelId) {
        return this.userBuildingPlansService.deleteBuildingPlan(tokenRequest, modelId);
    }

    @PostMapping("/{modelId}/status")
    public Mono<ResponseEntity<?>> changeStatus(@RequestBody UserTokenRequest tokenRequest, @PathVariable String modelId) {
        return this.userBuildingPlansService.changeBuildingPlanStatus(tokenRequest, modelId);
    }

    @GetMapping("")
    public Mono<ResponseEntity<?>> getBuildingPlans(@RequestBody UserTokenRequest tokenRequest) {
        return this.userBuildingPlansService.getBuildingPlans(tokenRequest);
    }
}
