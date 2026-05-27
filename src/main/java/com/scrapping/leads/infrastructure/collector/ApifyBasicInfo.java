package com.scrapping.leads.infrastructure.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApifyBasicInfo(@JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, @JsonProperty("email") String email, @JsonProperty("profile_url") String profileUrl, @JsonProperty("headline") String headline, @JsonProperty("current_company") String currentCompany, @JsonProperty("location") ApifyLocation location) {
}
