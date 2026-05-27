package com.scrapping.leads.infrastructure.persistence.resolver;

import com.scrapping.leads.infrastructure.persistence.entity.SkillJpaEntity;
import com.scrapping.leads.infrastructure.persistence.repository.SkillJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class SkillResolver {
    private final SkillJpaRepository repository;

    public SkillResolver(SkillJpaRepository repository) {
        this.repository = repository;
    }

    public SkillJpaEntity resolve(String name) {
        if (name == null || name.isBlank()) return null;
        return repository.findByName(name).orElseGet(() -> {
            SkillJpaEntity entity = new SkillJpaEntity();
            entity.setName(name);
            return repository.save(entity);
        });
    }
}
