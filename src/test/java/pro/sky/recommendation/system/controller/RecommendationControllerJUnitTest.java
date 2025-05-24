package pro.sky.recommendation.system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.DTO.RecommendationResponse;
import pro.sky.recommendation.system.service.RecommendationService;
import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RecommendationControllerJUnitTest {


    private MockMvc mockMvc;

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private static final UUID TEST_USER_ID = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
    private static final UUID TOP_SAVING_RULE_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final UUID USUAL_CREDIT_RULE_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    void shouldReturnRecommendationsWhenValidUserIdProvided() throws Exception {
        // Arrange
        RecommendationDTO topSavingRecommendation = createRecommendationDTO(TOP_SAVING_RULE_ID, "Top Saving", "Откройте свою собственную «Копилку» с нашим банком!");
        RecommendationDTO usualCreditRecommendation = createRecommendationDTO(USUAL_CREDIT_RULE_ID, "Usual Credit", "Откройте мир выгодных кредитов с нами!");

        RecommendationResponse mockResponse = new RecommendationResponse(TEST_USER_ID,
            Arrays.asList(topSavingRecommendation, usualCreditRecommendation));

        when(recommendationService.getRecommendationsForUser(TEST_USER_ID))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/recommendation/" + TEST_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(TEST_USER_ID.toString())))
                .andExpect(content().string(containsString(TOP_SAVING_RULE_ID.toString())))
                .andExpect(content().string(containsString("Top Saving")))
                .andExpect(content().string(containsString("Откройте свою собственную «Копилку»")))
                .andExpect(content().string(containsString(USUAL_CREDIT_RULE_ID.toString())))
                .andExpect(content().string(containsString("Usual Credit")))
                .andExpect(content().string(containsString("Откройте мир выгодных кредитов")));

        verify(recommendationService, times(1)).getRecommendationsForUser(TEST_USER_ID);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidUUIDProvided() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/recommendation/invalid-uuid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(recommendationService, never()).getRecommendationsForUser(any());
    }

    private RecommendationDTO createRecommendationDTO(UUID id, String name, String text) {
        return new RecommendationDTO(id, name, text);
    }

}
