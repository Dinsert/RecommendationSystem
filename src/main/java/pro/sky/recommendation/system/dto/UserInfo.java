package pro.sky.recommendation.system.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO-класс, предназначенный для представления информации о пользователе и его персональных рекомендациях.
 * Может использоваться в ответах веб-сервисов или для внутренней передачи данных между слоями приложения.
 */
public class UserInfo {

    /**
     * Уникальный идентификатор пользователя.
     */
    private final UUID userId;
    /**
     * имя пользователя.
     */
    private final String firstName;
    /**
     * фамилия пользователя.
     */
    private final String lastName;
    /**
     * лист с рекомендациями
     */
    private final List<RecommendationDTO> recommendations;

    /**
     * Конструктор для создания экземпляра {@code UserInfo}.
     *
     * @param userId        уникальный идентификатор пользователя
     * @param firstName     имя пользователя
     * @param lastName      фамилия пользователя
     * @param recommendations список персональных рекомендаций для пользователя
     */
    public UserInfo(UUID userId, String firstName, String lastName, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.recommendations = recommendations;
    }

    /**
     * Возвращает уникальный идентификатор пользователя.
     *
     * @return идентификатор пользователя
     */
    public UUID getUserId() { return userId; }
    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getFirstName() { return firstName; }
    /**
     * Возвращает фамилию пользователя.
     *
     * @return фамилия пользователя
     */
    public String getLastName() { return lastName; }
    /**
     * Возвращает список рекомендаций для пользователя.
     *
     * @return список рекомендаций
     */
    public List<RecommendationDTO> getRecommendations() { return recommendations; }
}
