INSERT INTO companies (name)
SELECT DISTINCT current_company FROM leads
WHERE current_company IS NOT NULL AND current_company != ''
ON CONFLICT DO NOTHING;

INSERT INTO locations (city, state, country)
SELECT DISTINCT city, state, country FROM leads
    ON CONFLICT DO NOTHING;

INSERT INTO sources (name)
SELECT DISTINCT source FROM leads
WHERE source IS NOT NULL
    ON CONFLICT DO NOTHING;

UPDATE leads l SET company_id  = (SELECT id FROM companies WHERE name = l.current_company);
UPDATE leads l SET location_id = (SELECT id FROM locations WHERE city = l.city AND country = l.country);
UPDATE leads l SET source_id   = (SELECT id FROM sources   WHERE name = l.source);