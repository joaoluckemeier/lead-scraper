CREATE TABLE experiences (
                             id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             lead_id    UUID NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
                             company_id UUID REFERENCES companies(id),
                             title      VARCHAR(255),
                             start_date DATE,
                             end_date   DATE,
                             is_current BOOLEAN NOT NULL DEFAULT false,
                             created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);