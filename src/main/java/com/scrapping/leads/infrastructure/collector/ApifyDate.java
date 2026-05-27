package com.scrapping.leads.infrastructure.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApifyDate(@JsonProperty("year") Integer year, @JsonProperty("month") String month) {
}
