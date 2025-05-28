package pro.sky.recommendation.system.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.entity.RuleQuery;
import pro.sky.recommendation.system.entity.RuleStats;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RecommendationsRepository;
import pro.sky.recommendation.system.repository.RuleStatsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с динамическими правилами рекомендаций.
 * Обеспечивает CRUD-операции с правилами и их проверку.
 * Реализация набора правил для динамических рекомендаций.
 * Проверяет условия динамических правил и формирует рекомендации.
 */
@Service
public class DynamicRuleService {

    private final DynamicRuleRepository repository;
    private final RuleStatsRepository ruleStatsRepository;
    private final RecommendationsRepository recommendationsRepository;


    /**
     * Конструктор набора правил.
     *
     * @param repository репозиторий динамических правил
     * @param recommendationsRepository репозиторий рекомендаций
     * @param ruleStatsRepository репозиторий статистики правил
     */
    public DynamicRuleService(DynamicRuleRepository repository, RuleStatsRepository ruleStatsRepository,
                              RecommendationsRepository recommendationsRepository) {
        this.repository = repository;
        this.ruleStatsRepository = ruleStatsRepository;
        this.recommendationsRepository = recommendationsRepository;
    }

    /**
     * Создает новое динамическое правило.
     *
     * @param rule правило для создания
     * @return созданное правило с заполненным идентификатором
     * @throws IllegalArgumentException если правило не прошло валидацию
     */
    @Transactional
    public DynamicRule createRule(DynamicRule rule) {
        return repository.save(rule);
    }

    /**
     * Получает все динамические правила.
     *
     * @return список всех правил (может быть пустым)
     */
    @Transactional(readOnly = true)
    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    /**
     * Удаляет правило по идентификатору.
     *
     * @param id идентификатор правила для удаления
     */
    @Transactional
    public void deleteRule(UUID id) {
        ruleStatsRepository.deleteById(id);
        repository.deleteById(id);
    }

    /**
     * Возвращает все динамические рекомендации для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список динамических рекомендаций или пустой список.
     */
    @Transactional
    public List<RecommendationDTO> findAllDynamicRecommendationForUser(UUID userId) {
        return repository.findAll().stream()
                .map(rule -> checkRuleForUser(userId, rule))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * Проверяет правило для конкретного пользователя.
     *
     * @param userId идентификатор пользователя
     * @param rule правило для проверки
     * @return Optional с рекомендацией, если правило применимо,
     *         иначе Optional.empty()
     */
    private Optional<RecommendationDTO> checkRuleForUser(UUID userId, DynamicRule rule) {
        boolean allConditionsMet = rule.getRule().stream()
                .allMatch(query -> checkCondition(userId, query));

        if (allConditionsMet) {
            RuleStats stats = ruleStatsRepository.findById(rule.getId())
                    .orElse(new RuleStats(rule.getId(), 0L));
            stats.setCount(stats.getCount() + 1);
            ruleStatsRepository.save(stats);
            return Optional.of(new RecommendationDTO(
                    rule.getProductId(),
                    rule.getProductName(),
                    rule.getProductText()));
        }
        return Optional.empty();
    }

    /**
     * Проверяет условие для конкретного пользователя.
     * @param userId идентификатор пользователя
     * @param query динамическое правило
     * @return true если пользователь удовлетворяет динамическому правилу иначе false
     */
    private boolean checkCondition(UUID userId, RuleQuery query) {
        boolean result = switch (query.getQuery()) {
            case "USER_OF" -> recommendationsRepository.hasProductType(userId, query.getArguments().get(0));
            case "ACTIVE_USER_OF" ->
                    recommendationsRepository.getTransactionCount(userId, query.getArguments().get(0)) >= 5;
            case "TRANSACTION_SUM_COMPARE" -> checkTransactionSum(userId, query);
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> checkDepositWithdrawCompare(userId, query);
            default -> throw new IllegalArgumentException("Unknown query type: " + query.getQuery());
        };
        return result ^ query.isNegate();
    }

    /**
     * Проверяет сумму транзакций для конкретного пользователя.
     * @param userId идентификатор пользователя
     * @param query динамическое правило
     * @return true если пользователь удовлетворяет динамическому правилу иначе false
     */
    private boolean checkTransactionSum(UUID userId, RuleQuery query) {
        List<String> args = query.getArguments();
        double sum = args.get(1).equals("DEPOSIT")
                ? recommendationsRepository.getTotalDepositsByProductType(userId, args.get(0))
                : recommendationsRepository.getTotalWithdrawalsByProductType(userId, args.get(0));

        double compareValue = Double.parseDouble(args.get(3));

        return switch (args.get(2)) {
            case ">" -> sum > compareValue;
            case "<" -> sum < compareValue;
            case "=" -> Math.abs(sum - compareValue) < 0.001;
            case ">=" -> sum >= compareValue;
            case "<=" -> sum <= compareValue;
            default -> throw new IllegalArgumentException("Unknown operator: " + args.get(2));
        };
    }

    /**
     * Проверяет сравнение сумм депозитов и снятий для конкретного пользователя.
     * @param userId идентификатор пользователя
     * @param query динамическое правило
     * @return true если пользователь удовлетворяет динамическому правилу иначе false
     */
    private boolean checkDepositWithdrawCompare(UUID userId, RuleQuery query) {
        List<String> args = query.getArguments();
        double depositSum = recommendationsRepository.getTotalDepositsByProductType(userId, args.get(0));
        double withdrawSum = recommendationsRepository.getTotalWithdrawalsByProductType(userId, args.get(0));

        return switch (args.get(1)) {
            case ">" -> depositSum > withdrawSum;
            case "<" -> depositSum < withdrawSum;
            case "=" -> Math.abs(depositSum - withdrawSum) < 0.001;
            case ">=" -> depositSum >= withdrawSum;
            case "<=" -> depositSum <= withdrawSum;
            default -> throw new IllegalArgumentException("Unknown operator: " + args.get(1));
        };
    }
}