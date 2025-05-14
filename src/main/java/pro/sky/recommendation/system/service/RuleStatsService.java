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

@Service
public class RuleStatsService {

    private final RuleStatsRepository ruleStatsRepository;
    private final DynamicRuleRepository dynamicRuleRepository;

    public RuleStatsService(RuleStatsRepository ruleStatsRepository, DynamicRuleRepository dynamicRuleRepository) {
        this.ruleStatsRepository = ruleStatsRepository;
        this.dynamicRuleRepository = dynamicRuleRepository;
    }

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
