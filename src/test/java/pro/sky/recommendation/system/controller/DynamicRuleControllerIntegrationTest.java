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
import org.springframework.transaction.annotation.Transactional;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.entity.RuleQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DynamicRuleControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createRule() {
        UUID productId = UUID.randomUUID();
        String productName = "Visual product";
        String productText = "Description";

        String query = "USER_OF";
        List<String> arguments = new ArrayList<>(List.of("DEBIT"));
        boolean negate = false;
        RuleQuery ruleQuery = new RuleQuery();
        ruleQuery.setQuery(query);
        ruleQuery.setArguments(arguments);
        ruleQuery.setNegate(negate);
        List<RuleQuery> ruleQueries = new ArrayList<>(List.of(ruleQuery));

        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setProductId(productId);
        dynamicRule.setProductName(productName);
        dynamicRule.setProductText(productText);
        dynamicRule.setRule(ruleQueries);

        ResponseEntity<DynamicRule> response = restTemplate.postForEntity(url() + "/createRule", dynamicRule, DynamicRule.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getProductId()).isEqualTo(productId);
        assertThat(response.getBody().getProductName()).isEqualTo(productName);
        assertThat(response.getBody().getProductText()).isEqualTo(productText);
        assertThat(response.getBody().getRule()).hasSize(1);
        assertThat(response.getBody().getRule().get(0).getQuery()).isEqualTo(query);
    }

    @Test
    void getAllRules() {
        ResponseEntity<Map<String, List<DynamicRule>>> response = restTemplate.exchange(
                url() + "/getAllRules",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("data")).isNotNull();
        assertThat(response.getBody().get("data")).anyMatch(dynamicRule -> dynamicRule.getProductName().equals("Test product"));
        assertThat(response.getBody().get("data")).anyMatch(dynamicRule -> dynamicRule.getProductText().equals("Test description"));
    }

    @Test
    void getAllRulesIsEmpty() {
        ResponseEntity<Map<String, List<DynamicRule>>> initialResponse = restTemplate.exchange(
                url() + "/getAllRules",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        initialResponse.getBody().get("data").forEach(rule ->
                restTemplate.delete(url() + "/deleteRule/{id}", rule.getId())
        );

        ResponseEntity<Map<String, List<DynamicRule>>> response = restTemplate.exchange(
                url() + "/getAllRules",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("data")).isNotNull();
        assertThat(response.getBody().get("data")).isEmpty();
    }

    @Test
    void deleteRule() {
        UUID id = UUID.fromString("cd77bfb1-6180-4565-afd3-1f45762c5121");

        ResponseEntity<Void> response = restTemplate.exchange(
                url() + "/deleteRule/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Map<String, List<DynamicRule>>> getRulesResponse = restTemplate.exchange(
                url() + "/getAllRules",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(getRulesResponse.getBody()).isNotNull();
        assertThat(getRulesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRulesResponse.getBody().get("data")).anyMatch(dynamicRule -> !dynamicRule.getProductName().equals("Delete"));

    }


    private String url() {
        return "http://localhost:" + port + "/rule";
    }
}