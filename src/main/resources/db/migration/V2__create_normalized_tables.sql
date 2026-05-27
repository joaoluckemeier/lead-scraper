CREATE TABLE companies (
                           id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE locations (
                           id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           city    VARCHAR(100),
                           state   VARCHAR(100),
                           country VARCHAR(100),
                           UNIQUE (city, state, country)
);

CREATE TABLE sources (
                         id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE skills (
                        id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE lead_skills (
                             lead_id  UUID NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
                             skill_id UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
                             PRIMARY KEY (lead_id, skill_id)
);