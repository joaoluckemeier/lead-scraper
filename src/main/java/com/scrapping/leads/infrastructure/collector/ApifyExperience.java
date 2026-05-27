package com.scrapping.leads.infrastructure.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApifyExperience(@JsonProperty("title") String title, @JsonProperty("company") String company, @JsonProperty("start_date") ApifyDate startDate, @JsonProperty("end_date") ApifyDate endDate, @JsonProperty("is_current") boolean isCurrent) {
}
