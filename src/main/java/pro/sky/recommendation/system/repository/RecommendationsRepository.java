package pro.sky.recommendation.system.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


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
