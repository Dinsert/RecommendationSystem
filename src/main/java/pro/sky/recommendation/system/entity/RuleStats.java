package pro.sky.recommendation.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Сущность для хранения статистики использования правил рекомендаций.
 * Каждое правило имеет уникальный идентификатор и счётчик количества обращений.
 */
@Entity
@Table(name = "rule_stats")
public class RuleStats {

    /**
     * Уникальный идентификатор правила, по которому собирается статистика.
     * Данный идентификатор совпадает с идентификатором самого правила (связан с ним внешним ключом).
     */
    @Id
    @Column(name = "rule_id", nullable = false)
    private UUID ruleId;

    /**
     * Число обращений к этому правилу (количество использований).
     */
    @Column(name = "count", nullable = false)
    private Long count;

    public RuleStats() {
    }

    /**
     * Основной конструктор для инициализации объекта статистики правил.
     *
     * @param ruleId уникальный идентификатор правила
     * @param count количество обращений к правилу
     */
    public RuleStats(UUID ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    /**
     * получение id правила
     * @return
     */
    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }
    /**
     * получение количества обращений к правилу
     * @return
     */
    public Long getCount() {
        return count;
    }

    /**
     * изменение количества обращений к правилу
     * @return
     */
    public void setCount(Long count) {
        this.count = count;
    }
}