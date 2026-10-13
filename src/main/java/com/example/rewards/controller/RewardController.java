package com.example.rewards.controller;

import com.example.rewards.dto.RewardResponse;
import com.example.rewards.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {
    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Example: GET /api/rewards/C001?months=3
     */
    @GetMapping("/{customerId}")
    public RewardResponse getCustomerRewards(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "3") @Min(1) @Max(36) int months) {
        return rewardService.getRewards(customerId, months);
    }
}
