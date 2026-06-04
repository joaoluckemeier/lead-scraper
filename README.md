# Lead Scraper para Meta Ads

Sistema de coleta automática de leads via LinkedIn, com persistência em PostgreSQL e exportação em CSV para campanhas de anúncios no Meta Ads.

---

## O que o sistema faz

1. Recebe uma configuração de busca (cargo, localização, experiência mínima)
2. Chama o actor `apimaestro/linkedin-profile-search-scraper` via Apify API
3. Normaliza os dados retornados
4. Deduplica por `linkedin_url` antes de persistir
5. Salva os leads no PostgreSQL em modelo normalizado
6. Exporta um CSV no formato exato exigido pelo Meta Ads Custom Audience

---

## Stack

- **Java 21** + **Spring Boot 3.5**
- **PostgreSQL 16** via Docker
- **Flyway** para migrations versionadas
- **Apify** como plataforma de scraping (actor externo, sem cookies)
- **RestClient** (Spring 6) para chamadas HTTP

---

## Arquitetura

O projeto segue **Clean Architecture** com dependências apontando sempre para dentro. O domínio não conhece Spring, JPA nem Apify.

```
interfaces/          ← CLI, Scheduler, REST Controller
application/         ← Use Cases (CollectLeadsUseCase, ExportLeadsUseCase)
domain/              ← Entidades, Value Objects, Ports (interfaces)
infrastructure/      ← Adapters: Apify, PostgreSQL, CSV
```

### Fluxo completo

```
CliRunner / Scheduler / LeadController
              ↓
    CollectLeadsUseCase
              ↓
    ApifyLinkedInCollector  ──►  Apify API (LinkedIn)
              ↓
    LeadNormalizer + Deduplicator
              ↓
    LeadJpaAdapter  ──►  PostgreSQL
              ↓
    ExportLeadsUseCase
              ↓
    CsvLeadExporter  ──►  leads_YYYY-MM-DD.csv
              ↓
    Upload manual no Meta Ads
```

### Ports e Adapters

| Port (domínio) | Adapter (infraestrutura) |
|---|---|
| `LeadCollector` | `ApifyLinkedInCollector` |
| `LeadExporter` | `CsvLeadExporter` |
| `LeadRepository` | `LeadJpaAdapter` |

Adicionar uma nova fonte (ex: Indeed) = criar `IndeedCollector implements LeadCollector`. Zero mudança no domínio ou use cases.

### Triggers configuráveis

O modo de acionamento é controlado por `application.yml`:

```yaml
scraper:
  trigger: MANUAL     # MANUAL | SCHEDULED | API
  schedule: "0 0 8 * * MON"
```

- `MANUAL` → `CommandLineRunner` executa ao subir a aplicação
- `SCHEDULED` → `@Scheduled` com cron configurável
- `API` → `POST /leads/collect` com body `CollectorConfig`

---

## Modelo de banco

Banco normalizado em 3FN com 9 migrations Flyway versionadas.

```
leads ────────── companies ── locations
  │                              ▲
  ├────────────── locations      │
  ├────────────── sources        │
  │                              │
lead_skills ───── skills

experiences ───── leads
            ───── companies
```

### Histórico de migrations

| Versão | O que faz |
|---|---|
| V1 | Cria tabela `leads` com todos os campos em uma única tabela |
| V2 | Cria tabelas normalizadas: `companies`, `locations`, `sources`, `skills`, `lead_skills` |
| V3 | Adiciona FKs nullable em `leads` (`company_id`, `location_id`, `source_id`) |
| V4 | Migra dados existentes para as novas tabelas de referência |
| V5 | Remove colunas antigas de `leads` (`current_company`, `city`, `state`, `country`, `source`, `skills`) |
| V6 | Adiciona colunas de auditoria (`created_at`, `updated_at`, `deleted_at`) em todas as tabelas |
| V7 | Adiciona `location_id` em `companies` |
| V8 | Cria tabela `experiences` com FK para `leads` e `companies` |
| V9 | Adiciona `is_active boolean DEFAULT true` em `leads` para exclusão lógica |

### Decisões de design

**Exclusão lógica** via `is_active boolean DEFAULT true`. Registros deletados são filtrados automaticamente nas queries (`findAllByIsActiveTrue`), preservando histórico.

**Auditoria** com `created_at`, `updated_at` e `deleted_at` em todas as tabelas relevantes.

**Resolvers** (`CompanyResolver`, `LocationResolver`, `SourceResolver`, `SkillResolver`) encapsulam a lógica de _upsert_ — buscam a entidade existente ou criam uma nova, evitando duplicatas nas tabelas de referência.

---

## Exemplo de output

CSV gerado no formato exato do Meta Ads Custom Audience:

```csv
fn,ln,em,ph,ct,st,country,zip,dob,gen
Gabriel,S.,contato.santosgabriel@gmail.com,,,,Brazil,,,
Antonio,Almeida,antonio.almeida@comun.app,,New York,,United States,,,
Silas,Brazil,,,Seattle,,United States,,,
```

Campos ausentes (`ph`, `st`, `zip`, `dob`, `gen`) são exportados como colunas vazias — o Meta Ads aceita upload parcial e faz match pelo que estiver disponível.

---

## Variáveis de ambiente e configuração

| Variável / Propriedade | Descrição | Exemplo |
|---|---|---|
| `APIFY_API_KEY` | Token de autenticação do Apify | `apify_api_xxxx` |
| `apify.actor-id` | Slug do actor no Apify | `apimaestro/linkedin-profile-search-scraper` |
| `scraper.trigger` | Modo de acionamento | `MANUAL`, `SCHEDULED`, `API` |
| `scraper.schedule` | Cron para trigger SCHEDULED | `0 0 8 * * MON` |
| `spring.datasource.url` | URL do PostgreSQL | `jdbc:postgresql://localhost:5433/leadgen` |

---

## Limitações conhecidas

- **Actor lento no plano gratuito do Apify** — o `run-sync-get-dataset-items` pode demorar 60–120 segundos para retornar dependendo da carga do servidor
- **Email nem sempre disponível** — o LinkedIn só expõe email quando o próprio usuário torna público; leads sem email são salvos normalmente mas têm match rate menor no Meta Ads
- **Skills não populadas** — o campo `skills` do actor Apify não está sendo mapeado ainda; a tabela `lead_skills` existe mas fica vazia
- **Experiences não populadas** — o histórico de experiências é usado apenas para calcular `experience_years`; a tabela `experiences` existe mas os dados não são persistidos ainda

---

## ADRs

**ADR-001 — Domínio sem anotações de framework**
`Lead.java` não importa Spring nem JPA. `@Entity` fica em `LeadJPAEntity` na camada de infraestrutura. Custo: mappers. Benefício: domínio 100% testável com Java puro.

**ADR-002 — RestClient em vez de WebFlux**
Chamadas ao Apify são síncronas por natureza. WebFlux adicionaria complexidade reativa sem ganho real nesse fluxo.

**ADR-003 — Ports como interfaces no domínio**
Seguindo Open/Closed: adicionar nova fonte de coleta ou novo formato de exportação = nova classe implementando o port. Zero mudança nos use cases.

**ADR-004 — Apify como plataforma de scraping**
Scraping direto do LinkedIn viola o ToS e gera risco de ban. O Apify absorve esse risco operando actors em infraestrutura própria, expondo os resultados via API REST.

**ADR-005 — CSV sem biblioteca externa**
`BufferedWriter` resolve o formato do Meta Ads sem dependência extra. Quando surgir necessidade de múltiplos formatos de exportação, `LeadExporter` já suporta extensão via novas implementações.

---

## Como rodar

### Pré-requisitos

- Java 21
- Docker
- Conta no [Apify](https://apify.com) com token gerado

### 1. Subir o banco

```bash
docker compose up -d
```

### 2. Configurar a API key

No IntelliJ: `Run > Edit Configurations > Environment Variables`

```
APIFY_API_KEY=apify_api_xxxxxxxxxxxx
```

### 3. Rodar

```bash
./mvnw spring-boot:run
```

Com `scraper.trigger: MANUAL` no `application.yml`, o sistema executa automaticamente ao subir, coleta os leads e gera o CSV na raiz do projeto.

### 4. Configurar a busca

Em `CliRunner.java` ou via `POST /leads/collect`:

```json
{
  "jobTitle": "Software Engineer",
  "location": "Brazil",
  "minExperienceYears": 4,
  "maxProfiles": 50
}
```

---

## Estrutura de pacotes

```
com.scrapping.leads/
├── domain/
│   ├── entity/          Lead.java
│   ├── valueobject/     ExperienceYears, LinkedInUrl, LeadSource
│   ├── port/            LeadCollector, LeadExporter
│   └── repository/      LeadRepository
├── application/
│   ├── usecase/         CollectLeadsUseCase, ExportLeadsUseCase
│   └── dto/             CollectorConfig
├── infrastructure/
│   ├── collector/       ApifyLinkedInCollector + records de deserialização
│   ├── exporter/        CsvLeadExporter
│   └── persistence/
│       ├── entity/      LeadJPAEntity + entidades normalizadas
│       ├── repository/  Spring Data repositories
│       ├── adapter/     LeadJpaAdapter
│       ├── mapper/      LeadMapper
│       └── resolver/    CompanyResolver, LocationResolver, SourceResolver, SkillResolver
└── interfaces/
    ├── cli/             CliRunner
    ├── scheduler/       LeadCollectScheduler
    └── rest/            LeadController
```
