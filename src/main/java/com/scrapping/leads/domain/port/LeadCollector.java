package com.scrapping.leads.domain.port;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.application.dto.CollectorConfig;
import com.scrapping.leads.domain.valueobject.LeadSource;

import java.util.List;

public interface LeadCollector {
    List<Lead> collect(CollectorConfig config);
    LeadSource source();
}
