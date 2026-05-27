package com.scrapping.leads.interfaces.cli;

import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.application.usecase.CollectLeadsUseCase;
import com.scrapping.leads.application.usecase.ExportLeadsUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {
    private final CollectLeadsUseCase collectLeadsUseCase;
    private final ExportLeadsUseCase exportLeadsUseCase;

    @Value("${scraper.trigger}")
    private String trigger;

    public CliRunner(CollectLeadsUseCase collectLeadsUseCase, ExportLeadsUseCase exportLeadsUseCase) {
        this.collectLeadsUseCase = collectLeadsUseCase;
        this.exportLeadsUseCase = exportLeadsUseCase;
    }

    @Override
    public void run(String... args) {
        if (!"MANUAL".equals(trigger)) return;
        CollectorConfig config = new CollectorConfig("Software Engineer", "Brazil", 4, 3);
        int saved = collectLeadsUseCase.execute(config);
        System.out.println("Leads coletados e salvos: " + saved);
        exportLeadsUseCase.execute();
        System.out.println("CSV exportado com sucesso.");
    }
}
