package pro.sky.recommendation.system.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки источников данных и JPA.
 * Настраивает два отдельных источника данных (H2 и PostgreSQL) с соответствующими
 * бинами для работы с JPA и JDBC.
 *
 * <p>Активируется для всех профилей, кроме 'test'.</p>
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "pro.sky.recommendation.system.repository",
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager"
)
@Profile("!test")
public class DataSourceConfig {

    /**
     * Создает основной источник данных для H2 (используется для транзакций).
     * Настроен как primary для разрешения неоднозначностей при инъекции.
     *
     * @return настроенный DataSource для H2 базы данных
     * @see HikariDataSource
     */
    @Primary
    @Bean(name = "recommendationsDataSource")
    public DataSource recommendationsDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:./db/transaction");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    /**
     * Создает дополнительный источник данных для PostgreSQL (используется для хранения правил).
     *
     * @return настроенный DataSource для PostgreSQL базы данных
     * @see HikariDataSource
     */
    @Bean(name = "secondDataSource")
    public DataSource secondDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/rules_db");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        return dataSource;
    }

    /**
     * Создает JdbcTemplate для работы с основным источником данных (H2).
     *
     * @param dataSource основной источник данных (должен быть квалифицирован как "recommendationsDataSource")
     * @return настроенный JdbcTemplate
     * @see JdbcTemplate
     */
    @Bean(name = "recommendationsJdbcTemplate")
    public JdbcTemplate recommendationsJdbcTemplate(@Qualifier("recommendationsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Создает EntityManager для PostgreSQL источника данных.
     *
     * @param dataSource источник данных PostgreSQL (должен быть квалифицирован как "secondDataSource")
     * @return настроенная фабрика EntityManager
     * @see LocalContainerEntityManagerFactoryBean
     */
    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("secondDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("pro.sky.recommendation.system.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Set JPA properties for PostgreSQL
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * Создает менеджер транзакций для PostgreSQL EntityManagerFactory.
     *
     * @param entityManagerFactory фабрика EntityManager (должна быть квалифицирована как "secondEntityManagerFactory")
     * @return настроенный менеджер транзакций
     * @see JpaTransactionManager
     */
    @Bean(name = "secondTransactionManager")
    public JpaTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}