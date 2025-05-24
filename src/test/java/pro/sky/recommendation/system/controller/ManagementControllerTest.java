package pro.sky.recommendation.system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.recommendation.system.service.ManagementService;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the ManagementController class.
 * Uses MockMvc to test the management endpoints in isolation from the actual service layer.
 * These tests verify the behavior of administrative and monitoring endpoints.
 */
@SpringBootTest
public class ManagementControllerTest {

    // MockMvc instance to simulate HTTP requests
    private MockMvc mockMvc;

    // Mock service layer to isolate controller tests
    @Mock
    private ManagementService managementService;

    // Controller instance with mocked dependencies injected
    @InjectMocks
    private ManagementController managementController;

    /**
     * Set up test environment before each test method.
     * Initializes MockMvc with the controller to be tested.
     */
    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller to test
        mockMvc = MockMvcBuilders.standaloneSetup(managementController).build();
    }

    /**
     * Test case for clearing caches through the management endpoint.
     * Verifies that the controller correctly processes a POST request to clear caches.
     *
     * Test Steps:
     * 1. Send a POST request to /management/clear-caches
     * 2. Verify that the response status is 200 OK
     * 3. Verify that the managementService.clearCaches() method was called exactly once
     */
    @Test
    void clearCaches_ShouldReturnOk() throws Exception {
        // Act: Perform the POST request to clear caches
        mockMvc.perform(post("/management/clear-caches")
                .contentType(MediaType.APPLICATION_JSON))
                // Assert: Verify the response status is 200 OK
                .andExpect(status().isOk());

        // Verify that the service method was called exactly once
        verify(managementService, times(1)).clearCaches();
    }

    /**
     * Test case for retrieving service information through the management endpoint.
     * Verifies that the controller correctly returns the service information.
     *
     * Test Steps:
     * 1. Set up mock to return expected service information
     * 2. Send a GET request to /management/info
     * 3. Verify the response status is 200 OK
     * 4. Verify the response body contains the expected name and version
     * 5. Verify that the managementService.getInfo() method was called exactly once
     */
    @Test
    void getInfo_ShouldReturnServiceInfo() throws Exception {
        // Arrange: Set up mock to return expected service information
        Map<String, String> expectedInfo = Map.of(
            "name", "recommendation-system",
            "version", "1.0.0"
        );

        when(managementService.getInfo()).thenReturn(expectedInfo);

        // Act & Assert: Perform the GET request and verify the response
        mockMvc.perform(get("/management/info")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verify the response body contains the expected fields and values
                .andExpect(jsonPath("$.name").value("recommendation-system"))
                .andExpect(jsonPath("$.version").value("1.0.0"));

        // Verify that the service method was called exactly once
        verify(managementService, times(1)).getInfo();
    }
}