package pro.sky.recommendation.system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_text", nullable = false)
    private String productText;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "rule_id", nullable = false)
    private List<RuleQuery> rule;

    public UUID getId() {
        return id; }
    public void setId(UUID id) {
        this.id = id; }
    public String getProductName() {
        return productName; }
    public void setProductName(String productName) {
        this.productName = productName; }
    public UUID getProductId() {
        return productId; }
    public void setProductId(UUID productId) {
        this.productId = productId; }
    public String getProductText() {
        return productText; }
    public void setProductText(String productText) {
        this.productText = productText; }
    public List<RuleQuery> getRule() {
        return rule; }
    public void setRule(List<RuleQuery> rule) {
        this.rule = rule; }
}

