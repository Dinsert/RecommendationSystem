package pro.sky.recommendation.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.recommendation.system.DTO.RecommendationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RuleStatsControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getRuleStats() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(url() + "/recommendation/{userId}", RecommendationResponse.class, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
        assertThat(response.getBody().getRecommendations()).isNotEmpty();
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("Top Saving"));
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("Usual Credit"));
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("INVEST500"));
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("Test product"));


        ResponseEntity<Map<String, List<Map<String, String>>>> responseStats = restTemplate.exchange(url() + "/rule/stats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(responseStats.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseStats.getBody()).isNotNull();
        assertThat(responseStats.getBody().get("stats")).anyMatch(stats -> stats.get("rule_id").equals("d8328cd4-cab8-4ed6-908b-4844023d46b3"));
        assertThat(responseStats.getBody().get("stats")).anyMatch(stats -> stats.get("count").equals("1"));
    }

    private String url() {
        return "http://localhost:" + port;
    }
}