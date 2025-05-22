package pro.sky.recommendation.system.service;


import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RuleStatsRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с динамическими правилами рекомендаций.
 * Обеспечивает CRUD-операции с правилами и их проверку.
 */
@Service
public class DynamicRuleService {

    private final DynamicRuleRepository repository;
    private final RuleStatsRepository ruleStatsRepository;

    /**
     * Конструктор сервиса.
     *
     * @param repository репозиторий для работы с динамическими правилами
     */
    public DynamicRuleService(DynamicRuleRepository repository, RuleStatsRepository ruleStatsRepository) {
        this.repository = repository;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    /**
     * Создает новое динамическое правило.
     *
     * @param rule правило для создания
     * @return созданное правило с заполненным идентификатором
     * @throws IllegalArgumentException если правило не прошло валидацию
     */
    public DynamicRule createRule(DynamicRule rule) {
        return repository.save(rule);
    }

    /**
     * Получает все динамические правила.
     *
     * @return список всех правил (может быть пустым)
     */
    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    /**
     * Удаляет правило по идентификатору.
     *
     * @param id идентификатор правила для удаления
     */
    public void deleteRule(UUID id) {
        ruleStatsRepository.deleteById(id);
        repository.deleteById(id);
    }
}