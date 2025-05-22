package pro.sky.recommendation.system.service;

import pro.sky.recommendation.system.DTO.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для наборов правил рекомендаций.
 * Определяет контракт для проверки правил и формирования рекомендаций.
 */
public interface RecommendationRuleSet {
    /**
     * Проверяет правила и возвращает рекомендацию, если условия выполнены.
     *
     * @param userId идентификатор пользователя
     * @return Optional с рекомендацией, если условия выполнены,
     *         иначе Optional.empty()
     */
    Optional<RecommendationDTO> checkRecommendation (UUID userId);
}
