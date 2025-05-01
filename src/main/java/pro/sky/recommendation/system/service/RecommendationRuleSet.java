package pro.sky.recommendation.system.service;

import pro.sky.recommendation.system.DTO.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;


public interface RecommendationRuleSet {
    Optional<RecommendationDTO> checkRecommendation (UUID userId);
}
