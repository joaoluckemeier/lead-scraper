package com.scrapping.leads.infrastructure.persistence.resolver;

import com.scrapping.leads.infrastructure.persistence.entity.CompanyJpaEntity;
import com.scrapping.leads.infrastructure.persistence.repository.CompanyJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CompanyResolver {
    private final CompanyJpaRepository repository;

    public CompanyResolver(CompanyJpaRepository repository) {
        this.repository = repository;
    }

    public CompanyJpaEntity resolve(String name) {
        if (name == null || name.isBlank()) return null;
        return repository.findByName(name).orElseGet(() -> {
            CompanyJpaEntity entity = new CompanyJpaEntity();
            entity.setName(name);
            return repository.save(entity);
        });
    }
}
