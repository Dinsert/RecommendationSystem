package pro.sky.recommendation.system.dto;

import java.util.List;
import java.util.UUID;

/**
 * Класс для представления результатов рекомендательной системы.
 * Включает идентификатор пользователя и список рекомендуемых позиций.
 */
public class RecommendationResponse {
    /**
     * Уникальный идентификатор пользователя, для которого сформированы рекомендации.
     */
    private UUID userId;
    /**
     * Список рекомендаций для пользователя.
     */
    private List<RecommendationDTO> recommendations;

    /**
     * Конструктор для инициализации объекта с указанием пользователя и списка рекомендаций.
     *
     * @param userId          идентификатор пользователя
     * @param recommendations список рекомендуемых позиций
     */
    public RecommendationResponse(UUID userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    /**
     * Получает идентификатор пользователя.
     *
     * @return идентификатор пользователя
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Устанавливает идентификатор пользователя.
     *
     * @param userId идентификатор пользователя
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Получает список рекомендаций.
     *
     * @return список рекомендаций
     */
    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

}
