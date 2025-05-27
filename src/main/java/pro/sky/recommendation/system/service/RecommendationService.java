package pro.sky.recommendation.system.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.recommendation.system.DTO.RecommendationDTO;
import pro.sky.recommendation.system.DTO.RecommendationResponse;
import pro.sky.recommendation.system.DTO.UserInfo;
import pro.sky.recommendation.system.exception.UserNotFoundException;
import pro.sky.recommendation.system.repository.RecommendationsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с системой рекомендаций.
 * Предоставляет функциональность для получения рекомендаций для пользователей на основании набора бизнес-правил.
 */
@Service
public class RecommendationService {

    /**
     * Набор бизнес-правил для определения рекомендаций.
     */
    private final List<RecommendationRuleSet> recommendationRules;
    /**
     * Репозиторий для доступа к данным рекомендаций.
     */
    private final RecommendationsRepository recommendationsRepository;

    /**
     * Конструктор для установки экземпляров репозитория и набора правил.
     *
     * @param recommendationRules       коллекция правил для вычисления рекомендаций
     * @param recommendationsRepository репозиторий для работы с базой данных рекомендаций
     */
    public RecommendationService(List<RecommendationRuleSet> recommendationRules, RecommendationsRepository recommendationsRepository) {
        this.recommendationRules = recommendationRules;
        this.recommendationsRepository = recommendationsRepository;
    }

    /**
     * Получить список рекомендаций для указанного пользователя.
     * <p>
     * Применяются все доступные правила, и собираются соответствующие рекомендации.
     *
     * @param userId уникальный идентификатор пользователя
     * @return объект с результатом проверки правил и списком рекомендаций
     */
    public RecommendationResponse getRecommendationsForUser(UUID userId) {
        List<RecommendationDTO> recommendations = recommendationRules.stream()
                .map(rule -> rule.checkRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new RecommendationResponse(userId, recommendations);
    }

    /**
     * Получить расширённую информацию о пользователе вместе с рекомендациями.
     * <p>
     * Производится проверка наличия пользователя по его имени, выполняется запрос в базу данных,
     * а затем формируются рекомендации с использованием метода {@link #getRecommendationsForUser(UUID)}.
     *
     * @param username имя пользователя
     * @return объект с полными сведениями о пользователе и его рекомендациях
     * @throws UserNotFoundException если пользователь не найден или найдено больше одного пользователя с указанным именем
     */
    @Transactional
    public UserInfo getRecommendationsByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new UserNotFoundException("Username cannot be empty");
        }
        /** Получаем данные из репозитория
         */
        List<Object[]> userData = recommendationsRepository.getRecommendationsByUsername(username.trim());

        /** Проверяем, что данные были получены
         */
        if (userData == null || userData.isEmpty()) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

        /** Проверяем количество пользователей с таким именем
         */
        if (userData.size() > 1) {
            throw new UserNotFoundException("Multiple users found with username " + username);
        }
        /** Извлекаем данные из массива
         */
        Object[] row = userData.get(0);
        UUID userId = UUID.fromString((String) row[0]);
        String firstName = (String) row[1];
        String lastName = (String) row[2];

        /** Получаем рекомендации для пользователя
         */
        List<RecommendationDTO> recommendations = getRecommendationsForUser(userId).getRecommendations();

        /** Создаём объект с полной информацией о пользователе
         */
        return new UserInfo(userId, firstName, lastName, recommendations);
    }
}




