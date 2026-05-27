package com.scrapping.leads.infrastructure.persistence.repository;

import com.scrapping.leads.infrastructure.persistence.entity.LeadJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeadJpaRepository extends JpaRepository<LeadJPAEntity, UUID> {
    boolean existsByLinkedInUrl(String linkedInUrl);
}
