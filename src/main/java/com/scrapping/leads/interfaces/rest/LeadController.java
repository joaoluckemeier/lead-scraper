package com.scrapping.leads.interfaces.rest;

import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.application.usecase.CollectLeadsUseCase;
import com.scrapping.leads.application.usecase.ExportLeadsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leads")
public class LeadController {
    private final CollectLeadsUseCase collectLeadsUseCase;
    private final ExportLeadsUseCase exportLeadsUseCase;

    public LeadController(CollectLeadsUseCase collectLeadsUseCase, ExportLeadsUseCase exportLeadsUseCase) {
        this.collectLeadsUseCase = collectLeadsUseCase;
        this.exportLeadsUseCase = exportLeadsUseCase;
    }

    @PostMapping("/collect")
    public ResponseEntity<String> collect(@RequestBody CollectorConfig config) {
        int saved = collectLeadsUseCase.execute(config);
        return ResponseEntity.ok("Leads salvos: " + saved);
    }

    @PostMapping("/export")
    public ResponseEntity<String> export() {
        exportLeadsUseCase.execute();
        return ResponseEntity.ok("CSV gerado com sucesso");
    }
}
