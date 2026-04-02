package com.example.diploma.loyalty.repository;

import com.example.diploma.loyalty.model.LoyaltyRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoyaltyRuleRepository extends CrudRepository<LoyaltyRule, UUID> {
    @Query("""
            select l from LoyaltyRule l
            where l.active = true
            and l.minHours <= :hours
            and (l.maxHours is null or l.maxHours >= :hours)
            order by l.minHours desc
            """)
    List<LoyaltyRule> findMatchingRules(@Param("hours") Long hours);
}
