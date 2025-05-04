package pro.sky.recommendation.system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.DTO.RecommendationResponse;
import pro.sky.recommendation.system.service.ProductCheckerService;
import pro.sky.recommendation.system.service.RecommendationService;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final ProductCheckerService productCheckerService;
    private final RecommendationService recommendationService;

    public RecommendationController(ProductCheckerService productCheckerService, RecommendationService recommendationService) {
        this.productCheckerService = productCheckerService;
        this.recommendationService = recommendationService;
    }


    //проверка наличия продукта у пользователя
    @GetMapping("/checkUserHasProductType/{userId}/{productType}")
    public ResponseEntity<Object> hasProductType(@PathVariable UUID userId, @PathVariable String productType) {
        boolean result = productCheckerService.hasProductType(userId, productType);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product type found");
        }
        return ResponseEntity.ok(result);
    }

    //Получить сумму пополнения по типу продукта
    @GetMapping("/getTotalDepositsByProductType/{userId}/{productType}")
    public ResponseEntity<Object> getTotalDepositsByProductType(@PathVariable UUID userId, @PathVariable String productType) {
        if (userId == null || userId.toString().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID cannot be null");
        }
        if (productType == null || productType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product type");
        }
        Double result = productCheckerService.getTotalDepositsByProductType(userId, productType);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No deposits found");
        }
        return ResponseEntity.ok(result);
    }

    //Получить сумму снятия по типу продукта
    @GetMapping("/getTotalWithdrawalsByProductType/{userId}/{productType}")
    public ResponseEntity<Object> getTotalWithdrawalsByProductType(@PathVariable UUID userId, @PathVariable String productType) {
        if (userId == null || userId.toString().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID cannot be null");
        }
        if (productType == null || productType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product type");
        }
        Double result = productCheckerService.getTotalWithdrawalsByProductType(userId, productType);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No withdrawals found");
        }
        return ResponseEntity.ok(result);
    }

    //Получить рекомендации на основе условий
    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable UUID userId) {
        RecommendationResponse response = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(response);
    }
}

