package pro.sky.recommendation.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для администрирования системы: очистки кеша и получения базовой информации о сервисе.
 */
@Service
public class ManagementService {

    /**
     * Менеджер кэш-памяти для взаимодействия с кешем.
     */
    private final CacheManager cacheManager;

    /**
     * Свойства билда для извлечения информации о версии приложения.
     */
    private final BuildProperties buildProperties;

    /**
     * Имя текущего приложения, извлекаемое из конфигурации.
     */
    private final String serviceName;

    /**
     * Конструктор для инициализации полей сервиса необходимыми компонентами.
     *
     * @param cacheManager      менеджер кэш-памяти
     * @param buildProperties   свойства билда
     * @param serviceName       имя текущего приложения
     */
    public ManagementService(CacheManager cacheManager,
                             BuildProperties buildProperties,
                             @Value("${spring.application.name}") String serviceName) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties;
        this.serviceName = serviceName;
    }

    /**
     * Полностью очищает все кэши, управляемые менеджером.
     */
    public void clearCaches() {
        cacheManager.getCacheNames().forEach(cacheName ->
                cacheManager.getCache(cacheName).clear()
        );
    }

    /**
     * Возвращает базовую информацию о сервисе: название и версию.
     *
     * @return карта с именем и версией сервиса
     */
    public Map<String, String> getInfo() {
        return Map.of(
                "name", serviceName,
                "version", buildProperties.getVersion()
        );
    }
}
