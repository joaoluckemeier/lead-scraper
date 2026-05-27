package com.scrapping.leads.infrastructure.collector;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApifyProfileResult(@JsonProperty("basic_info") ApifyBasicInfo basicInfo, @JsonProperty("experience") List<ApifyExperience> experience){
}
