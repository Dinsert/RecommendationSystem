package pro.sky.recommendation.system.service;

import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.entity.RuleStats;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RuleStatsRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для сбора статистики по правилам системы рекомендаций.
 */
@Service
public class RuleStatsService {

    /**
     * Репозиторий для доступа к статистике правил.
     */
    private final RuleStatsRepository ruleStatsRepository;
    /**
     * Репозиторий для доступа к динамическим правилам.
     */
    private final DynamicRuleRepository dynamicRuleRepository;

    /**
     * Конструктор для инжектинга зависимых репозиториев.
     *
     * @param ruleStatsRepository репозиторий статистики правил
     * @param dynamicRuleRepository репозиторий динамических правил
     */
    public RuleStatsService(RuleStatsRepository ruleStatsRepository, DynamicRuleRepository dynamicRuleRepository) {
        this.ruleStatsRepository = ruleStatsRepository;
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

    /**
     * Получает статистику использования правил.
     *
     * @return Карта с результатами статистики, где ключ — это строка `"stats"`,
     * значение — список карт вида {"rule_id": ..., "count": ...}, содержащий ID правила и количество использований.
     */
    public Map<String, List<Map<String, String>>> getRuleStats() {
        List<DynamicRule> allRules = dynamicRuleRepository.findAll();
        List<RuleStats> stats = ruleStatsRepository.findAll();
        Map<UUID, Long> statsMap = stats.stream()
                .collect(Collectors.toMap(RuleStats::getRuleId, RuleStats::getCount));

        List<Map<String, String>> result = allRules.stream()
                .map(rule -> Map.of(
                        "rule_id", rule.getId().toString(),
                        "count", String.valueOf(statsMap.getOrDefault(rule.getId(), 0L))
                ))
                .toList();

        return Map.of("stats", result);
    }
}
