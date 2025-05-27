package pro.sky.recommendation.system.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с рекомендациями и пользовательскими данными.
 * Обеспечивает доступ к данным транзакций и продуктов с поддержкой кэширования.
 */
@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор репозитория.
     *
     * @param jdbcTemplate настроенный JdbcTemplate для работы с БД транзакций
     */
    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет, есть ли у пользователя продукт указанного типа.
     *
     * @param userId идентификатор пользователя
     * @param productType тип продукта для проверки
     * @return true если у пользователя есть такой продукт, иначе false
     */
    @Cacheable(value = "userOfCache", key = "#userId + ':' + #productType")
    public boolean hasProductType(UUID userId, String productType) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*)>0 FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ?",
                Boolean.class,
                userId,
                productType
        );
    }
    /**
     * Возвращает общую сумму депозитов по типу продукта для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param productType тип продукта
     * @return сумма всех депозитов (пополнений) по указанному продукту
     */
    @Cacheable(value = "sumCache", key = "'deposit:' + #userId + ':' + #productType")
    public Double getTotalDepositsByProductType(UUID userId, String productType) {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'",
                Double.class,
                userId,
                productType
        );
    }

    /**
     * Подсчитывает количество транзакций пользователя по типу продукта.
     *
     * @param userId идентификатор пользователя
     * @param productType тип продукта
     * @return количество транзакций (>= 0)
     */
    @Cacheable(value = "sumCache", key = "'withdraw:' + #userId + ':' + #productType")
    public Double getTotalWithdrawalsByProductType(UUID userId, String productType) {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'",
                Double.class,
                userId,
                productType
        );
    }

    /**
     * Подсчитывает количество транзакций пользователя по типу продукта.
     * Результат кэшируется в "activeUserCache".
     *
     * @param userId идентификатор пользователя (не может быть null)
     * @param productType тип продукта (не может быть null или пустым)
     * @return количество транзакций (всегда >= 0)
     */
    @Cacheable(value = "activeUserCache", key = "#userId + ':' + #productType")
    public int getTransactionCount(UUID userId, String productType) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transactions t " +
                        "JOIN products p ON t.product_id = p.id " +
                        "WHERE t.user_id = ? AND p.type = ?",
                Integer.class,
                userId,
                productType
        );
    }

    /**
     * Получает рекомендации для пользователя по его имени.
     *
     * @param username имя пользователя для поиска (не может быть null или пустым)
     * @return информация о пользователе и список рекомендаций
     */
    @Cacheable(value = "userInfoCache", key = "#username")
    public List<Object[]> getRecommendationsByUsername(String username) {
        String sql = "SELECT id, first_name, last_name FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) -> new Object[]{
                rs.getString("id"),
                rs.getString("first_name"),
                rs.getString("last_name")
        });
    }
}
