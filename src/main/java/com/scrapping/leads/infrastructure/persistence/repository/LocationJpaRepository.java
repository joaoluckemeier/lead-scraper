package com.scrapping.leads.infrastructure.persistence.repository;

import com.scrapping.leads.infrastructure.persistence.entity.LocationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationJpaRepository extends JpaRepository<LocationJpaEntity, UUID> {
    Optional<LocationJpaEntity> findByCityAndCountry(String city, String country);
}
