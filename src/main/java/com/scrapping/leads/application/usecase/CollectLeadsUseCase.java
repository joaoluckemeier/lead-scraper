package com.scrapping.leads.application.usecase;

import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.port.LeadCollector;
import com.scrapping.leads.domain.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectLeadsUseCase {
    private final LeadCollector leadCollector;
    private final LeadRepository leadRepository;

    public CollectLeadsUseCase(LeadCollector leadCollector, LeadRepository leadRepository) {
        this.leadCollector = leadCollector;
        this.leadRepository = leadRepository;
    }

    public int execute(CollectorConfig config) {
        List<Lead> leads = leadCollector.collect(config);

        int saved = 0;
        for (Lead lead : leads) {
            if (!leadRepository.findByLinkedInUrl(lead.getLinkedInUrl())) {
                leadRepository.save(lead);
                saved++;
            }
        }
        return saved;
    }
}
