package com.scrapping.leads.infrastructure.persistence.mapper;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.valueobject.ExperienceYears;
import com.scrapping.leads.domain.valueobject.LeadSource;
import com.scrapping.leads.domain.valueobject.LinkedInUrl;
import com.scrapping.leads.infrastructure.persistence.entity.LeadJPAEntity;
import com.scrapping.leads.infrastructure.persistence.entity.SkillJpaEntity;
import com.scrapping.leads.infrastructure.persistence.resolver.CompanyResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.LocationResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.SkillResolver;
import com.scrapping.leads.infrastructure.persistence.resolver.SourceResolver;

import java.util.List;
import java.util.stream.Collectors;

public class LeadMapper {
    public static LeadJPAEntity toEntity(Lead domain,
                                         CompanyResolver companyResolver,
                                         LocationResolver locationResolver,
                                         SourceResolver sourceResolver,
                                         SkillResolver skillResolver) {
        LeadJPAEntity entity = new LeadJPAEntity();
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setLinkedInUrl(domain.getLinkedInUrl().value());
        entity.setHeadline(domain.getHeadline());
        entity.setExperienceYears(domain.getExperienceYears().value());
        entity.setCollectedAt(domain.getCollectedAt());
        entity.setCompany(companyResolver.resolve(domain.getCurrentCompany()));
        entity.setLocation(locationResolver.resolve(domain.getCity(), domain.getState(), domain.getCountry()));
        entity.setSource(sourceResolver.resolve(domain.getSource().name()));
        entity.setSkills(domain.getSkills() == null ? List.of() :
                domain.getSkills().stream()
                .map(skillResolver::resolve)
                .filter(s -> s != null)
                .collect(Collectors.toList()));
        return entity;
    }

    public static Lead toDomain(LeadJPAEntity jpa) {
        return Lead.of(
                jpa.getFirstName(),
                jpa.getLastName(),
                jpa.getEmail(),
                new LinkedInUrl(jpa.getLinkedInUrl()),
                jpa.getHeadline(),
                jpa.getCompany() != null ? jpa.getCompany().getName() : null,
                jpa.getLocation() != null ? jpa.getLocation().getCity() : null,
                jpa.getLocation() != null ? jpa.getLocation().getState() : null,
                jpa.getLocation() != null ? jpa.getLocation().getCountry() : null,
                new ExperienceYears(jpa.getExperienceYears()),
                jpa.getSource() != null ? LeadSource.valueOf(jpa.getSource().getName()) : LeadSource.LINKEDIN,
                jpa.getSkills() != null ?
                        jpa.getSkills().stream().map(SkillJpaEntity::getName).collect(Collectors.toList()) :
                        List.of()
        );
    }
}
