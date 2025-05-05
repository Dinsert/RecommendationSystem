package pro.sky.recommendation.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.DTO.RecommendationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendationRules;

    @Autowired
    public RecommendationService(List<RecommendationRuleSet> recommendationRules) {
        this.recommendationRules = recommendationRules;
    }

    public RecommendationResponse getRecommendationsForUser(UUID userId) {
        List<RecommendationDTO> recommendations = recommendationRules.stream()
                .map(rule -> rule.checkRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new RecommendationResponse(userId, recommendations);
    }
}


