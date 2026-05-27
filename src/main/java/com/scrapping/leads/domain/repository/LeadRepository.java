package com.scrapping.leads.domain.repository;

import com.scrapping.leads.domain.entity.Lead;
import com.scrapping.leads.domain.valueobject.LeadSource;
import com.scrapping.leads.domain.valueobject.LinkedInUrl;

import java.util.List;

public interface LeadRepository {
    Lead save(Lead lead);
    boolean findByLinkedInUrl (LinkedInUrl linkedInUrl);
    List<Lead> findAllLeads ();
    List<Lead> findByLeadSource (LeadSource leadSource);
}
