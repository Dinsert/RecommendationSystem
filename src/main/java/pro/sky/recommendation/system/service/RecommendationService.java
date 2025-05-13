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

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> recommendationRules;
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(List<RecommendationRuleSet> recommendationRules, RecommendationsRepository recommendationsRepository) {
        this.recommendationRules = recommendationRules;
        this.recommendationsRepository = recommendationsRepository;
    }

    public RecommendationResponse getRecommendationsForUser(UUID userId) {
        List<RecommendationDTO> recommendations = recommendationRules.stream()
                .map(rule -> rule.checkRecommendation(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new RecommendationResponse(userId, recommendations);
    }

    @Transactional
    public UserInfo getRecommendationsByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new UserNotFoundException("Username cannot be empty");
        }
        // Получаем данные из репозитория
        List<Object[]> userData = recommendationsRepository.getRecommendationsByUsername(username.trim());

        // Проверяем, что данные найдены
        if (userData == null || userData.isEmpty()) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

        if (userData.size() > 1) {
            throw new UserNotFoundException("Multiple users found with username " + username);
        }
        // Извлекаем данные из результата
        Object[] row = userData.get(0);
        UUID userId = UUID.fromString((String) row[0]);
        String firstName = (String) row[1];
        String lastName = (String) row[2];

        // Получаем рекомендации для пользователя
        List<RecommendationDTO> recommendations = getRecommendationsForUser(userId).getRecommendations();

        // Создаём и возвращаем объект UserInfo
        return new UserInfo(userId, firstName, lastName, recommendations);
    }
}




