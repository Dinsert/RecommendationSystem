package pro.sky.recommendation.system.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.recommendation.system.DTO.RecommendationResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecommendationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getRecommendationsIsPresent() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(url(), RecommendationResponse.class, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
        assertThat(response.getBody().getRecommendations()).isNotEmpty();
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("Top Saving"));
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("Usual Credit"));
        assertThat(response.getBody().getRecommendations()).anyMatch(dto -> dto.getName().equals("INVEST500"));
    }

    @Test
    void getRecommendationIsEmpty() {
        UUID userId = UUID.fromString("e809075f-1752-411a-8e0c-de3bae23e1b9");
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(url(), RecommendationResponse.class, userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
        assertThat(response.getBody().getRecommendations()).isEmpty();
    }

    @Test
    void getRecommendationInvalidUserId() {
        String invalidUserId = "invalid-uuid";
        ResponseEntity<String> response = restTemplate.getForEntity(url(), String.class, invalidUserId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Bad Request");
    }

    private String url() {
        return "http://localhost:" + port + "/recommendation/{userId}";
    }

    @Test
        //Valid UserID
    void getTotalWithdrawalsByProductTypeValid() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String productType = "DEBIT";
        ResponseEntity<Double> response = restTemplate.getForEntity("http://localhost:" + port + "/getTotalWithdrawalsByProductType/{userId}/{productType}", Double.class, userId, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
        //Invalid UserID BAD_REQUEST
    void getTotalWithdrawalsByProductTypeInvalidProduct() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String invalidProductType = "INVALID_PRODUCT";
        ResponseEntity<Double> response = restTemplate.getForEntity("http://localhost:" + port + "/getTotalWithdrawalsByProductType/{userId}/{productType}", Double.class, userId, invalidProductType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(0.0);
    }

    @Test
        //Empty Recommendation
    void getTotalDepositsByProductTypeValid() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String productType = "DEBIT";
        ResponseEntity<Double> response = restTemplate.getForEntity("http://localhost:" + port + "/getTotalDepositsByProductType/{userId}/{productType}", Double.class, userId, productType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
        //Total Withdrawals and Deposits
    void getTotalDepositsByProductTypeInvalidProduct() {
        UUID userId = UUID.fromString("9d2df4a9-0085-4838-b8af-d8b46659cb62");
        String invalidProductType = "INVALID_PRODUCT";
        ResponseEntity<Double> response = restTemplate.getForEntity("http://localhost:" + port + "/getTotalDepositsByProductType/{userId}/{productType}", Double.class, userId, invalidProductType);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(0.0);
    }

}