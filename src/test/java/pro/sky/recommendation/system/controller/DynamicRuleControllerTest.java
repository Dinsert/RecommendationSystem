package pro.sky.recommendation.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.service.DynamicRuleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the DynamicRuleController class.
 * Uses MockMvc to test the controller endpoints in isolation from the actual service layer.
 */
@SpringBootTest
public class DynamicRuleControllerTest {

    // MockMvc instance to simulate HTTP requests
    private MockMvc mockMvc;
    // ObjectMapper for JSON serialization/deserialization
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Mock service layer to isolate controller tests
    @Mock
    private DynamicRuleService dynamicRuleService;

    // Controller instance with mocked dependencies injected
    @InjectMocks
    private DynamicRuleController dynamicRuleController;

    // Test data
    private DynamicRule testRule;
    private final UUID testRuleId = UUID.randomUUID();

    /**
     * Set up test environment before each test method.
     * Initializes MockMvc and creates a test DynamicRule instance.
     */
    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller to test
        mockMvc = MockMvcBuilders.standaloneSetup(dynamicRuleController).build();

        // Create a test DynamicRule with sample data
        testRule = new DynamicRule();
        testRule.setId(testRuleId);
        testRule.setProductName("Test Product");
        testRule.setProductId(UUID.randomUUID());
        testRule.setProductText("Test Product Description");
        testRule.setRule(new ArrayList<>());
    }

    /**
     * Test case for creating a new rule through the REST API.
     * Verifies that the controller correctly processes a POST request to create a rule.
     */
    @Test
    void createRule_ShouldReturnCreatedRule() throws Exception {
        // Arrange: Set up mock behavior
        when(dynamicRuleService.createRule(any(DynamicRule.class))).thenReturn(testRule);

        // Act & Assert: Perform the request and verify the response
        mockMvc.perform(post("/rule/createRule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRuleId.toString()))
                .andExpect(jsonPath("$.name").value("Test Rule"));

        // Verify that the service method was called exactly once with any DynamicRule parameter
        verify(dynamicRuleService, times(1)).createRule(any(DynamicRule.class));
    }

    /**
     * Test case for retrieving all rules through the REST API.
     * Verifies that the controller correctly returns a list of all rules.
     */
    @Test
    void getAllRules_ShouldReturnAllRules() throws Exception {
        // Arrange: Set up mock to return a list containing our test rule
        List<DynamicRule> rules = Arrays.asList(testRule);
        when(dynamicRuleService.getAllRules()).thenReturn(rules);

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/rule/getAllRules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(testRuleId.toString()))
                .andExpect(jsonPath("$.data[0].name").value("Test Rule"));

        // Verify that the service method was called exactly once
        verify(dynamicRuleService, times(1)).getAllRules();
    }

    /**
     * Test case for deleting an existing rule through the REST API.
     * Verifies that the controller correctly processes a DELETE request.
     */
    @Test
    void deleteRule_ShouldReturnNoContent() throws Exception {
        // Act & Assert: Perform DELETE request and verify no content response
        mockMvc.perform(delete("/rule/deleteRule/" + testRuleId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify that the service method was called exactly once with the correct ID
        verify(dynamicRuleService, times(1)).deleteRule(testRuleId);
    }

    /**
     * Test case for attempting to delete a rule with an invalid UUID.
     * Verifies that the controller returns a 400 Bad Request status.
     */
    @Test
    void deleteRule_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert: Perform DELETE request with invalid UUID and verify error response
        mockMvc.perform(delete("/rule/deleteRule/invalid-uuid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Verify that the service method was never called with any parameter
        verify(dynamicRuleService, never()).deleteRule(any());
    }
}