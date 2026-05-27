package com.scrapping.leads.infrastructure.persistence.repository;

import com.scrapping.leads.infrastructure.persistence.entity.SourceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceJpaRepository extends JpaRepository<SourceJpaEntity, UUID> {
    Optional<SourceJpaEntity> findByName(String name);
}
