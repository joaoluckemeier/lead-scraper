ALTER TABLE leads ADD COLUMN company_id  UUID REFERENCES companies(id);
ALTER TABLE leads ADD COLUMN location_id UUID REFERENCES locations(id);
ALTER TABLE leads ADD COLUMN source_id   UUID REFERENCES sources(id);