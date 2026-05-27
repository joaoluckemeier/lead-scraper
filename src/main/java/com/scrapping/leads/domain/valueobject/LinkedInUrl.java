package com.scrapping.leads.domain.valueobject;

public record LinkedInUrl(String value) {
    public LinkedInUrl {
        if (value == null || value.isBlank() || !value.contains("linkedin.com/in/")){
            throw new IllegalArgumentException("URL inválida.");
        }
    }
}
