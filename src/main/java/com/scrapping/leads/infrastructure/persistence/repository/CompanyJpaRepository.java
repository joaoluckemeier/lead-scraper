package com.scrapping.leads.infrastructure.persistence.repository;

import com.scrapping.leads.infrastructure.persistence.entity.CompanyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, UUID> {
    Optional<CompanyJpaEntity> findByName(String name);
}
