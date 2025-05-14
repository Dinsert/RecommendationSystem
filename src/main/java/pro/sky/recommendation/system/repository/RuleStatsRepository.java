package pro.sky.recommendation.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.recommendation.system.entity.RuleStats;

import java.util.UUID;

@Repository
public interface RuleStatsRepository extends JpaRepository<RuleStats, UUID> {
}