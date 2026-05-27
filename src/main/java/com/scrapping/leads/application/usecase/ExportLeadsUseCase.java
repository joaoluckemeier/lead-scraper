package com.scrapping.leads.application.usecase;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.port.LeadExporter;
import com.scrapping.leads.domain.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportLeadsUseCase {
    private final LeadRepository leadRepository;
    private final LeadExporter leadExporter;

    public ExportLeadsUseCase(LeadRepository leadRepository, LeadExporter leadExporter) {
        this.leadRepository = leadRepository;
        this.leadExporter = leadExporter;
    }

    public void execute() {
        List<Lead> leads = leadRepository.findAllLeads();
        leadExporter.leadExporter(leads);
    }
}
