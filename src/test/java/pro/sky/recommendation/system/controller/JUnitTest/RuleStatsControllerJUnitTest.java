package pro.sky.recommendation.system.controller.JUnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.recommendation.system.controller.RuleStatsController;
import pro.sky.recommendation.system.service.RuleStatsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the RuleStatsController class.
 * Uses MockMvc to test the rule statistics endpoints in isolation from the actual service layer.
 * These tests verify the behavior of rule statistics retrieval functionality.
 */
@SpringBootTest
public class RuleStatsControllerJUnitTest {

    // MockMvc instance to simulate HTTP requests
    private MockMvc mockMvc;

    // Mock service layer to isolate controller tests
    @Mock
    private RuleStatsService ruleStatsService;

    // Controller instance with mocked dependencies injected
    @InjectMocks
    private RuleStatsController ruleStatsController;

    /**
     * Set up test environment before each test method.
     * Initializes MockMvc with the controller to be tested.
     */
    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller to test
        mockMvc = MockMvcBuilders.standaloneSetup(ruleStatsController).build();
    }

    /**
     * Test case for retrieving rule statistics through the REST API.
     * Verifies that the controller correctly returns the rule statistics.
     *
     * Test Steps:
     * 1. Set up mock to return expected rule statistics
     * 2. Send a GET request to /rule/stats
     * 3. Verify the response status is 200 OK
     * 4. Verify the response body contains the expected rule statistics
     * 5. Verify that the ruleStatsService.getRuleStats() method was called exactly once
     */
    @Test
    void getRuleStats_ShouldReturnStats() throws Exception {
        // Arrange: Set up test data
        Map<String, String> stat1 = new HashMap<>();
        stat1.put("ruleId", "550e8400-e29b-41d4-a716-446655440000");
        stat1.put("ruleName", "High Balance Rule");
        stat1.put("executionCount", "42");

        Map<String, String> stat2 = new HashMap<>();
        stat2.put("ruleId", "550e8400-e29b-41d4-a716-446655440001");
        stat2.put("ruleName", "Frequent Transactions Rule");
        stat2.put("executionCount", "15");

        // Create expected response structure
        Map<String, List<Map<String, String>>> expectedResponse = Map.of(
            "statistics", Arrays.asList(stat1, stat2)
        );

        // Configure mock service to return the expected response
        when(ruleStatsService.getRuleStats()).thenReturn(expectedResponse);

        // Act & Assert: Perform the GET request and verify the response
        mockMvc.perform(get("/rule/stats")
                .contentType(MediaType.APPLICATION_JSON))
                // Verify HTTP status is 200 OK
                .andExpect(status().isOk())
                // Verify the response contains the expected rule statistics
                .andExpect(jsonPath("$.statistics[0].ruleName").value("High Balance Rule"))
                .andExpect(jsonPath("$.statistics[0].executionCount").value("42"))
                .andExpect(jsonPath("$.statistics[1].ruleName").value("Frequent Transactions Rule"))
                .andExpect(jsonPath("$.statistics[1].executionCount").value("15"));

        // Verify that the service method was called exactly once
        verify(ruleStatsService, times(1)).getRuleStats();
    }
}