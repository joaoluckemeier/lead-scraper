package com.scrapping.leads.infrastructure.collector;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.port.LeadCollector;
import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.domain.valueobject.ExperienceYears;
import com.scrapping.leads.domain.valueobject.LeadSource;
import com.scrapping.leads.domain.valueobject.LinkedInUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApifyLinkedInCollector implements LeadCollector {
    private final RestClient restClient;

    @Value("${apify.actor-id}")
    private String actorId;

    public ApifyLinkedInCollector(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<Lead> collect(CollectorConfig config) {
        System.out.println("Chamando Apify actor: " + actorId);

        Map<String, Object> body = Map.of(
                "current_job_title", config.jobTitle(),
                "location", config.location(),
                "max_profiles", config.maxProfiles(),
                "include_email", true
        );

        String url = "https://api.apify.com/v2/acts/apimaestro~linkedin-profile-search-scraper/run-sync-get-dataset-items?timeout=300";

        ApifyProfileResult[] results = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(ApifyProfileResult[].class);

        if (results == null) return List.of();
        System.out.println("Resultados brutos do Apify: " + results.length);

        return Arrays.stream(results)
                .filter(r -> r.basicInfo() != null)
                .filter(r -> r.basicInfo().profileUrl() != null)
                .map(r -> {
                    int years = ExperienceCalculator.calculate(r.experience());
                    return Lead.of(
                            r.basicInfo().firstName(),
                            r.basicInfo().lastName(),
                            r.basicInfo().email(),
                            new LinkedInUrl(r.basicInfo().profileUrl()),
                            r.basicInfo().headline(),
                            r.basicInfo().currentCompany(),
                            r.basicInfo().location() != null ? r.basicInfo().location().city() : null,
                            null,
                            r.basicInfo().location() != null ? r.basicInfo().location().country() : null,
                            new ExperienceYears(years),
                            LeadSource.LINKEDIN,
                            List.of()
                    );
                })
                .filter(lead -> lead.getExperienceYears().value() >= config.minExperienceYears())
                .collect(Collectors.toList());
    }

    @Override
    public LeadSource source() {
        return LeadSource.LINKEDIN;
    }
}
