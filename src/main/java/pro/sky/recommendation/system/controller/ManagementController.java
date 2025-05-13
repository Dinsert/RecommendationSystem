package pro.sky.recommendation.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation.system.service.ManagementService;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @PostMapping("/clear-caches")
    public void clearCaches() {
        managementService.clearCaches();
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return managementService.getInfo();
    }
}