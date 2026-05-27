package com.scrapping.leads.infrastructure.collector;

import java.time.LocalDate;
import java.util.List;

public class ExperienceCalculator {
    public static int calculate(List<ApifyExperience> experiences){
        if (experiences == null) return 0;

        int soma = 0;
        int currentYear = LocalDate.now().getYear();

        for (ApifyExperience experience : experiences) {
            if (experience.startDate() == null || experience.startDate().year() == null) continue;

            int startYear = experience.startDate().year();
            int endYear = experience.isCurrent() || experience.endDate() == null || experience.endDate().year() == null
                    ? currentYear
                    : experience.endDate().year();

            soma += endYear - startYear;

        }
        return soma;
    }
}
