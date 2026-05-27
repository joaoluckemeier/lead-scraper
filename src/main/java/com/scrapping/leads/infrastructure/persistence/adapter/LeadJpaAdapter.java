package com.scrapping.leads.infrastructure.persistence.adapter;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.repository.LeadRepository;
import com.scrapping.leads.domain.valueobject.LeadSource;
import com.scrapping.leads.domain.valueobject.LinkedInUrl;
import com.scrapping.leads.infrastructure.persistence.entity.LeadJPAEntity;
import com.scrapping.leads.infrastructure.persistence.mapper.LeadMapper;
import com.scrapping.leads.infrastructure.persistence.repository.LeadJpaRepository;
import com.scrapping.leads.infrastructure.persistence.resolver.CompanyResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.LocationResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.SkillResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.SourceResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeadJpaAdapter implements LeadRepository {
    private final LeadJpaRepository leadJpaRepository;
    private final CompanyResolver companyResolver;
    private final LocationResolver locationResolver;
    private final SourceResolver sourceResolver;
    private final SkillResolver skillResolver;

    public LeadJpaAdapter(LeadJpaRepository leadJpaRepository,
                          CompanyResolver companyResolver,
                          LocationResolver locationResolver,
                          SourceResolver sourceResolver,
                          SkillResolver skillResolver) {
        this.leadJpaRepository = leadJpaRepository;
        this.companyResolver = companyResolver;
        this.locationResolver = locationResolver;
        this.sourceResolver = sourceResolver;
        this.skillResolver = skillResolver;
    }

    @Override
    public Lead save(Lead lead) {
        LeadJPAEntity entity = LeadMapper.toEntity(lead, companyResolver, locationResolver, sourceResolver, skillResolver);
        LeadJPAEntity saved = leadJpaRepository.save(entity);
        return LeadMapper.toDomain(saved);
    }

    @Override
    public boolean findByLinkedInUrl(LinkedInUrl linkedInUrl) {
        return leadJpaRepository.existsByLinkedInUrl(linkedInUrl.value());
    }

    @Override
    public List<Lead> findAllLeads() {
        return leadJpaRepository.findAll()
                .stream()
                .map(LeadMapper::toDomain)
                .toList();
    }

    @Override
    public List<Lead> findByLeadSource(LeadSource leadSource) {
        return leadJpaRepository.findAll()
                .stream()
                .filter(e -> e.getSource() != null && e.getSource().getName().equals(leadSource.name()))
                .map(LeadMapper::toDomain)
                .toList();
    }
}
