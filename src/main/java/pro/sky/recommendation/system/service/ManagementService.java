package pro.sky.recommendation.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManagementService {

    private final CacheManager cacheManager;
    private final BuildProperties buildProperties;
    private final String serviceName;

    public ManagementService(CacheManager cacheManager,
                             BuildProperties buildProperties,
                             @Value("${spring.application.name}") String serviceName) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties;
        this.serviceName = serviceName;
    }

    public void clearCaches() {
        cacheManager.getCacheNames().forEach(cacheName ->
                cacheManager.getCache(cacheName).clear()
        );
    }

    public Map<String, String> getInfo() {
        return Map.of(
                "name", serviceName,
                "version", buildProperties.getVersion()
        );
    }
}
