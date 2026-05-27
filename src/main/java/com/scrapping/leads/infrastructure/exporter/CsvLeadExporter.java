package com.scrapping.leads.infrastructure.exporter;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.port.LeadExporter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class CsvLeadExporter implements LeadExporter {

    @Override
    public void leadExporter(List<Lead> leads) {
        String fileName = "leads_" + LocalDate.now() + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("fn,ln,em,ph,ct,st,country,zip,dob,gen");
            writer.newLine();
            for (Lead lead : leads) {
                String line = String.join(",",
                        nullSafe(lead.getFirstName()),
                        nullSafe(lead.getLastName()),
                        nullSafe(lead.getEmail()),
                        "",
                        nullSafe(lead.getCity()),
                        "",
                        nullSafe(lead.getCountry()),
                        "",
                        "",
                        ""
                );
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public String exporterName() {
        return "CSV";
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }
}
