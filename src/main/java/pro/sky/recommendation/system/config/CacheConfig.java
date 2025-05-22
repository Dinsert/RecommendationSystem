package pro.sky.recommendation.system.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация кэширования для системы рекомендаций.
 * Создает и настраивает менеджер кэша для хранения промежуточных результатов запросов.
 */
@Configuration
public class CacheConfig {

    /**
     * Создает менеджер кэша с тремя отдельными кэшами.
     *
     * @return CacheManager с настроенными кэшами:
     *         - userOfCache: для хранения информации о наличии продуктов у пользователей
     *         - activeUserCache: для хранения данных об активных пользователях
     *         - sumCache: для хранения сумм транзакций
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("userOfCache", "activeUserCache", "sumCache", "userInfoCache");
    }
}
