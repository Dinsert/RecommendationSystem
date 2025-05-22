package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.service.RuleStatsService;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для API запросов, связанных со статистикой правил системы рекомендаций.
 */
@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    /**
     * Сервис для доступа к статистике правил.
     */
    private final RuleStatsService ruleStatsService;

    /**
     * Конструктор для инжекта сервиса статистики правил.
     * @param ruleStatsService сервис для работы со статистикой правил
     */
    public RuleStatsController(RuleStatsService ruleStatsService) {
        this.ruleStatsService = ruleStatsService;
    }

    /**
     * Запрашивает статистику использования правил.
     * @return Статистика использования правил в виде списка карт, каждая из которых содержит правило и число раз его использования.
     */
    @GetMapping("/stats")
    public Map<String, List<Map<String, String>>> getRuleStats() {
        return ruleStatsService.getRuleStats();
    }
}