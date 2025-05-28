package pro.sky.recommendation.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation.system.entity.DynamicRule;

import java.util.UUID;

/**
 * Репозиторий для работы с динамическими правилами рекомендаций.
 */
@Repository
public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
}
