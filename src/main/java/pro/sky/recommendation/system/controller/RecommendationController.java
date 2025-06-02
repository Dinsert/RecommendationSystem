package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.dto.RecommendationResponse;
import pro.sky.recommendation.system.service.RecommendationService;

import java.util.UUID;

/**
 * Контроллер для работы с рекомендациями.
 * Предоставляет API для получения персонализированных рекомендаций.
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;


    /**
     * Конструктор контроллера.
     *
     * @param recommendationService сервис для работы с рекомендациями
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    /**
     * Получает рекомендации для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return ответ с рекомендациями (может быть пустым)
     * @response 200 Успешный запрос, возвращает список рекомендаций
     * @response 404 Пользователь не найден
     */
    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
        return recommendationService.getRecommendationsForUser(userId);
    }
}