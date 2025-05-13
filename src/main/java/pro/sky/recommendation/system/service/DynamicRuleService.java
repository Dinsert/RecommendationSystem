package pro.sky.recommendation.system.service;


import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RuleStatsRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository repository;
    private final RuleStatsRepository ruleStatsRepository;

    public DynamicRuleService(DynamicRuleRepository repository, RuleStatsRepository ruleStatsRepository) {
        this.repository = repository;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    //создание динамического правила
    public DynamicRule createRule(DynamicRule rule) {
        return repository.save(rule);
    }

    //получение всех динамических правил
    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    //удаление динамического правила
    public void deleteRule(UUID id) {
        ruleStatsRepository.deleteById(id);
        repository.deleteById(id);
    }
}