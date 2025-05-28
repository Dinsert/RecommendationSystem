package pro.sky.recommendation.system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Сущность представляющая собой динамическое правило для рекомендации продуктов.
 * Содержит основную информацию о продукте и набор запросов для формирования рекомендаций.
 */
@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {

    /**
     * Уникальный идентификатор правила.
     * Генерируется автоматически при создании объекта.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Название продукта, связанного с данным правилом.
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * Идентификатор продукта, связанный с данным правилом.
     */
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    /**
     * Описание или текстовая характеристика продукта.
     */
    @Column(name = "product_text", nullable = false)
    private String productText;

    /**
     * Список запросов, ассоциированных с данным правилом.
     * Позволяет формировать рекомендации на основе указанных условий.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "rule_id", nullable = false)
    private List<RuleQuery> rule;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleQuery> getRule() {
        return rule;
    }

    public void setRule(List<RuleQuery> rule) {
        this.rule = rule;
    }
}
