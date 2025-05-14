package pro.sky.recommendation.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "rule_stats")
public class RuleStats {

    @Id
    @Column(name = "rule_id", nullable = false)
    private UUID ruleId;

    @Column(name = "count", nullable = false)
    private Long count;

    public RuleStats() {
    }

    public RuleStats(UUID ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    // Геттеры и сеттеры
    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}