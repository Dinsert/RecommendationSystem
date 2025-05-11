package pro.sky.recommendation.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.DTO.RecommendationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendationRules;
    private final DynamicRuleService dynamicRuleService;

    @Autowired
    public RecommendationService(List<RecommendationRuleSet> recommendationRules, DynamicRuleService dynamicRuleService) {
        this.recommendationRules = recommendationRules;
        this.dynamicRuleService = dynamicRuleService;
    }

    public RecommendationResponse getRecommendationsForUser(UUID userId) {
        //1.получаем список статических рекомендаций (из RecommendationRuleSet)
        List<RecommendationDTO> recommendations = recommendationRules.stream()
                .map(rule -> rule.checkRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        // 2. Получаем динамические рекомендации (из базы данных)
        List<RecommendationDTO> dynamicRecommendations = dynamicRuleService.getAllRules().stream()
                .map(rule -> dynamicRuleService.checkDynamicRule(userId, rule))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // 3. Объединяем оба списка
        List<RecommendationDTO> allRecommendations = new ArrayList<>();
        allRecommendations.addAll(recommendations);
        allRecommendations.addAll(dynamicRecommendations);

        return new RecommendationResponse(userId, allRecommendations);
    }
}




