package com.scrapping.leads.domain.valueobject;

public record ExperienceYears(int value) {
    public ExperienceYears {
        if (value < 0){
            throw new IllegalArgumentException("Experiência deve ser maior que 0.");
        }
    }

    public boolean isSenior(){
        return value >= 4;
    }
}
