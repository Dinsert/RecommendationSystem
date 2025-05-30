package pro.sky.recommendation.system.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.recommendation.system.dto.RecommendationDTO;
import pro.sky.recommendation.system.dto.RecommendationResponse;
import pro.sky.recommendation.system.dto.UserInfo;
import pro.sky.recommendation.system.exception.UserNotFoundException;
import pro.sky.recommendation.system.repository.RecommendationsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * Сервис динамических правил.
     */
    private final DynamicRuleService dynamicRuleService;

    /**
     * Конструктор для установки экземпляров репозитория и набора правил.
     *
     * @param recommendationRules       коллекция правил для вычисления рекомендаций
     * @param recommendationsRepository репозиторий для работы с базой данных рекомендаций
     * @param dynamicRuleService сервис динамических правил
     */
    public RecommendationService(List<RecommendationRuleSet> recommendationRules,
                                 RecommendationsRepository recommendationsRepository,
                                 DynamicRuleService dynamicRuleService) {
        this.recommendationRules = recommendationRules;
        this.recommendationsRepository = recommendationsRepository;
        this.dynamicRuleService = dynamicRuleService;
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
        List<RecommendationDTO> staticRecommendations = recommendationRules.stream()
                .map(rule -> rule.checkRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<RecommendationDTO> dynamicRecommendations = dynamicRuleService.findAllDynamicRecommendationForUser(userId);

        List<RecommendationDTO> allRecommendations = new ArrayList<>();
        allRecommendations.addAll(dynamicRecommendations);
        allRecommendations.addAll(staticRecommendations);

        return new RecommendationResponse(userId, allRecommendations);
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
        List<Object[]> userData = recommendationsRepository.getRecommendationsByUsername(username.trim());

        if (userData == null || userData.isEmpty()) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

        if (userData.size() > 1) {
            throw new UserNotFoundException("Multiple users found with username " + username);
        }

        Object[] row = userData.get(0);
        UUID userId = UUID.fromString((String) row[0]);
        String firstName = (String) row[1];
        String lastName = (String) row[2];

        List<RecommendationDTO> recommendations = getRecommendationsForUser(userId).getRecommendations();

        return new UserInfo(userId, firstName, lastName, recommendations);
    }
}




