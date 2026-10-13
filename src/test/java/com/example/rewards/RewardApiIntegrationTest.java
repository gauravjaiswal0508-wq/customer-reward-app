package com.example.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsRewardsForKnownCustomer() throws Exception {
        mockMvc.perform(get("/api/rewards/C001").param("months", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C001"))
                .andExpect(jsonPath("$.timeframeMonths").value(3))
                .andExpect(jsonPath("$.monthlyRewards").isArray())
                .andExpect(jsonPath("$.totalRewardPoints").isNumber());
    }

    @Test
    void defaultsTimeframeToThreeMonthsAndIncludesTransactionDetails() throws Exception {
        mockMvc.perform(get("/api/rewards/C001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeframeMonths").value(3))
                .andExpect(jsonPath("$.customerName").value("Aarav Sharma"))
                .andExpect(jsonPath("$.transactionCount").isNumber())
                .andExpect(jsonPath("$.totalSpend").isString())
                .andExpect(jsonPath("$.transactions[0].transactionId").exists())
                .andExpect(jsonPath("$.transactions[0].rewardPoints").isNumber());
    }

    @Test
    void rejectsTimeframeAboveMaximum() throws Exception {
        mockMvc.perform(get("/api/rewards/C001").param("months", "37"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsNotFoundForUnknownCustomer() throws Exception {
        mockMvc.perform(get("/api/rewards/UNKNOWN").param("months", "3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found: UNKNOWN"));
    }

    @Test
    void rejectsInvalidTimeframe() throws Exception {
        mockMvc.perform(get("/api/rewards/C001").param("months", "0"))
                .andExpect(status().isBadRequest());
    }
}
