package com.scrapping.leads.interfaces.scheduler;

import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.application.usecase.CollectLeadsUseCase;
import com.scrapping.leads.application.usecase.ExportLeadsUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LeadCollectScheduler {
    private final CollectLeadsUseCase collectLeadsUseCase;
    private final ExportLeadsUseCase exportLeadsUseCase;

    @Value("${scraper.trigger}")
    private String trigger;

    public LeadCollectScheduler(CollectLeadsUseCase collectLeadsUseCase, ExportLeadsUseCase exportLeadsUseCase) {
        this.collectLeadsUseCase = collectLeadsUseCase;
        this.exportLeadsUseCase = exportLeadsUseCase;
    }

    @Scheduled(cron = "${scraper.schedule}")
    public void run() {
        if (!"SCHEDULED".equals(trigger)) return;
        CollectorConfig config = new CollectorConfig("Software Engineer", "Brazil", 4, 50);
        collectLeadsUseCase.execute(config);
        exportLeadsUseCase.execute();
    }
}
