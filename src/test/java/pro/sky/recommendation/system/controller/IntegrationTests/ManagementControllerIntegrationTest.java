package pro.sky.recommendation.system.controller.IntegrationTests;

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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ManagementControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void clearCaches() {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                url() + "/clear-caches",
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getInfo() {
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url() + "/info",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Map<String, String> body = response.getBody();
        assertThat(body).containsKey("name");
        assertThat(body.get("name")).isEqualTo("recommendation-system");
        assertThat(body).containsKey("version");
        assertThat(body.get("version")).isNotEmpty();
    }

    private String url() {
        return "http://localhost:" + port + "/management";
    }
}