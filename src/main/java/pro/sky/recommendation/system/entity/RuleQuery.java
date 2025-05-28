package pro.sky.recommendation.system.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Представляет отдельную запись условия ("запрос"), используемую в правиле рекомендаций.
 * Хранится в таблице "rule_queries" и описывает конкретный критерий отбора рекомендаций, включая его тип, аргументы и признак отрицания.
 */
@Entity
@Table(name = "rule_queries")
public class RuleQuery {

    /**
     * Уникальный идентификатор запроса, генерируемый автоматически при сохранении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Тип или выражение запроса, определяющее условие фильтрации продукта.
     * Является обязательным полем.
     */
    @Column(name = "query_type", nullable = false)
    private String query;

    /**
     * Аргументы, используемые в данном запросе.
     * Могут содержать дополнительные данные, необходимые для интерпретации запроса.
     */
    @ElementCollection
    @CollectionTable(name = "rule_query_arguments", joinColumns = @JoinColumn(name = "query_id"))
    @Column(name = "argument")
    private List<String> arguments;

    /**
     * Признак, указывающий на необходимость инвертировать результат данного запроса.
     * По умолчанию равен false.
     */
    @Column(name = "negate", nullable = false)
    private boolean negate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

}
