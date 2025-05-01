package pro.sky.recommendation.system.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pro.sky.recommendation.system.DTO.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

@Service
@Component
public interface RecommendationRuleSet {
    Optional<RecommendationDTO> checkRecommendation (UUID userId);
}
