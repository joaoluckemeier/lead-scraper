package com.scrapping.leads.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "leads")
public class LeadJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;

    @Column(name = "linkedin_url")
    private String linkedInUrl;

    private String headline;
    private int experienceYears;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyJpaEntity company;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationJpaEntity location;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private SourceJpaEntity source;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "lead_skills",
            joinColumns = @JoinColumn(name = "lead_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<SkillJpaEntity> skills;

    private LocalDateTime collectedAt;

    public LeadJPAEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public CompanyJpaEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyJpaEntity company) {
        this.company = company;
    }

    public LocationJpaEntity getLocation() {
        return location;
    }

    public void setLocation(LocationJpaEntity location) {
        this.location = location;
    }

    public SourceJpaEntity getSource() {
        return source;
    }

    public void setSource(SourceJpaEntity source) {
        this.source = source;
    }

    public List<SkillJpaEntity> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillJpaEntity> skills) {
        this.skills = skills;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(LocalDateTime collectedAt) {
        this.collectedAt = collectedAt;
    }
}
