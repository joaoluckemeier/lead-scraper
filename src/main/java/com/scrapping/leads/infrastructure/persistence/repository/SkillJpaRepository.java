package com.scrapping.leads.infrastructure.persistence.repository;

import com.scrapping.leads.infrastructure.persistence.entity.SkillJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SkillJpaRepository extends JpaRepository<SkillJpaEntity, UUID> {
    Optional<SkillJpaEntity> findByName(String name);
}
