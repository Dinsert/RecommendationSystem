package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.service.ManagementService;

import java.util.Map;
/**
 * Контроллер для административных операций: очистка кэшей и получение информации о сервисе.
 */
@RestController
@RequestMapping("/management")
public class ManagementController {

    /**
     * Сервис для административной функциональности.
     */
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    /**
     * Обработчик POST-запросов для полной очистки всех кешей.
     */
    @PostMapping("/clear-caches")
    public void clearCaches() {
        managementService.clearCaches();
    }

    /**
     * Обработчик GET-запросов для получения базовых сведений о сервисе.
     *
     * @return информация о сервисе в виде карты
     */
    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return managementService.getInfo();
    }
}