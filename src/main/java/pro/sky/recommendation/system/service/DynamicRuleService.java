package pro.sky.recommendation.system.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.entity.RuleQuery;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RecommendationsRepository;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;


    public DynamicRuleService(DynamicRuleRepository repository, RecommendationsRepository recommendationsRepository, @Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.repository = repository;
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
        repository.deleteById(id);
    }


}

