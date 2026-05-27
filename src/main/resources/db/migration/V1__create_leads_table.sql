CREATE TABLE leads (
                       id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       first_name      VARCHAR(255) NOT NULL,
                       last_name       VARCHAR(255),
                       email           VARCHAR(255),
                       linkedin_url    VARCHAR(500) UNIQUE NOT NULL,
                       headline        VARCHAR(500),
                       current_company VARCHAR(255),
                       city            VARCHAR(100),
                       state           VARCHAR(100),
                       country         VARCHAR(100),
                       experience_years INT,
                       skills          TEXT[],
                       source          VARCHAR(50) NOT NULL,
                       collected_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);