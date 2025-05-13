package pro.sky.recommendation.system.service.DynamicRuleSet;

import org.springframework.stereotype.Component;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.entity.RuleQuery;
import pro.sky.recommendation.system.entity.RuleStats;
import pro.sky.recommendation.system.repository.DynamicRuleRepository;
import pro.sky.recommendation.system.repository.RecommendationsRepository;
import pro.sky.recommendation.system.repository.RuleStatsRepository;
import pro.sky.recommendation.system.service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DynamicRuleSet implements RecommendationRuleSet {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final RecommendationsRepository recommendationsRepository;
    private final RuleStatsRepository ruleStatsRepository;

    public DynamicRuleSet(DynamicRuleRepository dynamicRuleRepository,
                          RecommendationsRepository recommendationsRepository,
                          RuleStatsRepository ruleStatsRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.recommendationsRepository = recommendationsRepository;
        this.ruleStatsRepository = ruleStatsRepository;
    }

    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID userId) {
        return dynamicRuleRepository.findAll().stream()
                .map(rule -> checkRuleForUser(userId, rule))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    Optional<RecommendationDTO> checkRuleForUser(UUID userId, DynamicRule rule) {
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

