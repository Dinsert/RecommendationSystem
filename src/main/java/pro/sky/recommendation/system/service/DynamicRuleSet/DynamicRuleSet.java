package pro.sky.recommendation.system.service.DynamicRuleSet;

import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.service.DynamicRuleService;
import pro.sky.recommendation.system.service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DynamicRuleSet implements RecommendationRuleSet {
    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleSet(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    //переопределение метода checkRecommendation для проверки динамических правил
    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID userId) {
        // Получаем все динамические правила из базы
        List<DynamicRule> rules = dynamicRuleService.getAllRules();

        // Проверяем каждое правило для пользователя
        for (DynamicRule rule : rules) {
            Optional<RecommendationDTO> recommendation = dynamicRuleService.checkDynamicRule(userId, rule);
            if (recommendation.isPresent()) {
                return recommendation;
            }
        }

        return Optional.empty();
    }
}

