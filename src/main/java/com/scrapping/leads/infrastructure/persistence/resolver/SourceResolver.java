package com.scrapping.leads.infrastructure.persistence.resolver;

import com.scrapping.leads.infrastructure.persistence.entity.SourceJpaEntity;
import com.scrapping.leads.infrastructure.persistence.repository.SourceJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class SourceResolver {
    private final SourceJpaRepository repository;

    public SourceResolver(SourceJpaRepository repository) {
        this.repository = repository;
    }

    public SourceJpaEntity resolve(String name) {
        if (name == null || name.isBlank()) return null;
        return repository.findByName(name).orElseGet(() -> {
            SourceJpaEntity entity = new SourceJpaEntity();
            entity.setName(name);
            return repository.save(entity);
        });
    }
}
