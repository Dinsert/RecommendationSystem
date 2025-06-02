package pro.sky.recommendation.system.dto;

import java.util.Objects;
import java.util.UUID;

/**
 * DTO для передачи информации о рекомендации.
 *
 * @author vkozhevatov
 * Содержит основные данные о рекомендуемом продукте.
 */
public class RecommendationDTO {

    private UUID id;
    private String name;
    private String text;

    public RecommendationDTO() {
    }

    /**
     * Конструктор DTO.
     *
     * @param id   идентификатор продукта
     * @param name название продукта
     * @param text описание рекомендации
     */
    public RecommendationDTO(UUID id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    /**
     * возвращает id продукта
     *
     * @return id
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * возвращает текст рекомендации
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * возвращает имя продукта
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationDTO that = (RecommendationDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text);
    }

    @Override
    public String toString() {
        return "RecommendationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
