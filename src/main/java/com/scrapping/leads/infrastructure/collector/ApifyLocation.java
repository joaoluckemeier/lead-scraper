package com.scrapping.leads.infrastructure.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApifyLocation(@JsonProperty("city") String city, @JsonProperty("country") String country) {
}
