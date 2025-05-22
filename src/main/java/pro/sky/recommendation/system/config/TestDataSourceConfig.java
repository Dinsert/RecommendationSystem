package pro.sky.recommendation.system.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Конфигурационный класс для настройки тестового источника данных.
 * Использует H2 in-memory базу данных для тестирования.
 *
 * <p>Активируется только для профиля "test".</p>
 *
 * <p>Основные особенности конфигурации:</p>
 * <ul>
 *   <li>In-memory база данных H2</li>
 *   <li>Автоматическое создание схемы при запуске</li>
 *   <li>Отключение закрытия базы при выходе</li>
 *   <li>Primary DataSource для разрешения неоднозначностей</li>
 * </ul>
 */
@Configuration
@Profile("test")
public class TestDataSourceConfig {

    /**
     * Создает основной тестовый источник данных (H2 in-memory).
     *
     * <p>Настройки подключения:</p>
     * <ul>
     *   <li>URL: jdbc:h2:mem:testdb</li>
     *   <li>Драйвер: org.h2.Driver</li>
     *   <li>Пользователь: sa</li>
     *   <li>Пароль: пустой</li>
     * </ul>
     *
     * <p>Особые параметры H2:</p>
     * <ul>
     *   <li>DB_CLOSE_DELAY=-1 - не закрывать базу при неактивности</li>
     *   <li>DB_CLOSE_ON_EXIT=FALSE - не закрывать при завершении приложения</li>
     * </ul>
     *
     * @return настроенный DataSource для тестовой среды
     */
    @Primary
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    /**
     * Создает JdbcTemplate для работы с тестовым источником данных.
     *
     * @param dataSource тестовый источник данных (должен быть квалифицирован как "recommendationsDataSource")
     * @return настроенный JdbcTemplate для тестовой среды
     */
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(@Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}