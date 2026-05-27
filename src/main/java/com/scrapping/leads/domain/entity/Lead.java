package com.scrapping.leads.domain.entity;

import com.scrapping.leads.domain.valueobject.ExperienceYears;
import com.scrapping.leads.domain.valueobject.LeadSource;
import com.scrapping.leads.domain.valueobject.LinkedInUrl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Lead {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LinkedInUrl linkedInUrl;
    private String headline;
    private String currentCompany;
    private String city;
    private String state;
    private String country;
    private ExperienceYears experienceYears;
    private List<String> skills;
    private LeadSource source;
    private LocalDateTime collectedAt;

    public static Lead of(String firstName, String lastName, String email, LinkedInUrl linkedInUrl, String headline, String currentCompany, String city, String state, String country, ExperienceYears experienceYears, LeadSource leadSource, List<String> skills ) {
        Lead lead = new Lead();
        lead.id = UUID.randomUUID();
        lead.collectedAt = LocalDateTime.now();
        lead.firstName = firstName;
        lead.lastName = lastName;
        lead.email = email;
        lead.linkedInUrl = linkedInUrl;
        lead.headline = headline;
        lead.currentCompany = currentCompany;
        lead.city = city;
        lead.state = state;
        lead.country = country;
        lead.experienceYears = experienceYears;
        lead.skills = skills;
        lead.source = leadSource;
        return lead;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LinkedInUrl getLinkedInUrl() {
        return linkedInUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public ExperienceYears getExperienceYears() {
        return experienceYears;
    }

    public List<String> getSkills() {
        return skills;
    }

    public LeadSource getSource() {
        return source;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }
}
