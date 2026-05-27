package com.scrapping.leads.domain.port;

import com.scrapping.leads.domain.entity.Lead;

import java.util.List;

public interface LeadExporter {
    void leadExporter(List<Lead> leads);
    String exporterName();
}
