
package com.shivora.shipping.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shivora.shipping.entity.DeliveryRule;

@Repository
public interface DeliveryRuleRepository
        extends JpaRepository<DeliveryRule, Long> {

    @Query("""
        SELECT r
        FROM DeliveryRule r
        WHERE r.zone.id = :zoneId
          AND r.status = 'ACTIVE'
          AND r.validFrom <= :now
          AND (r.validTo IS NULL OR r.validTo > :now)
        ORDER BY r.id
        """)
    List<DeliveryRule> findActiveRulesByZone(
            @Param("zoneId") Long zoneId,
            @Param("now") LocalDateTime now);
}
