package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.service.RuleStatsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatsService ruleStatsService;

    public RuleStatsController(RuleStatsService ruleStatsService) {
        this.ruleStatsService = ruleStatsService;
    }

    @GetMapping("/stats")
    public Map<String, List<Map<String, String>>> getRuleStats() {
        return ruleStatsService.getRuleStats();
    }
}