# Vinamilk QA Automation Framework

Multi-module Java test automation framework for the Vinamilk e-commerce
platform, built around its underlying microservices (`user-service`,
`product-service`, `order-service`, `payment-service`). It covers UI (Selenium),
API (REST Assured), event-driven (Kafka) and service-virtualization (WireMock)
testing behind one Maven reactor, one config model, and one CI/CD pipeline.

## Why a multi-module layout

A microservices application is not tested like a monolith: the same commerce
flow ("place an order") is verified at the UI layer, at each service's API
contract, and at the event that stitches services together. Splitting by
**test type** (not by page or by feature) lets each layer:

- run independently and in parallel in CI (`-pl api-tests`, `-pl ui-tests`, ...)
- scale its own test count without slowing down the others
- depend on `automation-core` only, never on each other's test code
  (`messaging-tests` reuses `api-tests`' service clients as a *library*, via a
  `test`-scoped dependency, to trigger the workflow it listens for - it does
  not duplicate HTTP logic)

```
vinamilk-automation-parent (pom)
├── automation-core      # framework: config, WebDriver lifecycle, listeners, utils
├── ui-tests              # Selenium 4 + Page Object Model, depends on core
├── api-tests              # REST Assured clients/models per microservice, depends on core
├── messaging-tests       # Kafka producer/consumer helpers + event-driven tests
└── mock-services         # Embedded WireMock for isolating a service under test
```

| Module | Tests | Depends on |
|---|---|---|
| `automation-core` | none (shared library) | Selenium, Jackson, Log4j2, Allure |
| `ui-tests` | Selenium/TestNG UI regression | `automation-core` |
| `api-tests` | REST Assured contract & cross-service integration | `automation-core` |
| `messaging-tests` | Kafka event-driven | `automation-core`, `api-tests` (test-scope) |
| `mock-services` | Isolated/stubbed service tests | `automation-core` |

## Configuration model

Environment values (base URLs, Kafka brokers, browser, grid) live in
`automation-core/src/main/resources/environments/{dev,staging,prod}.properties`
and are loaded by `ConfigManager` based on `-Denv=<name>` (default `dev`).
Any key can be overridden per-run with a matching `-D` system property, e.g.:

```bash
mvn -pl ui-tests -am test -Denv=staging -Dbrowser=firefox -Dbrowser.headless=true
```

## Running locally

```bash
# start Selenium Grid + Kafka (optional; only needed for grid/broker-backed runs)
docker-compose up -d

# API contract & cross-service integration tests
mvn -pl api-tests -am test -DsuiteXmlFile=src/test/resources/testng-api.xml

# UI regression suite
mvn -pl ui-tests -am test -DsuiteXmlFile=src/test/resources/testng-ui.xml

# Kafka event-driven tests (requires a broker, see docker-compose.yml)
mvn -pl messaging-tests -am test -DsuiteXmlFile=src/test/resources/testng-messaging.xml

# Service-virtualization tests (no external dependency - WireMock runs embedded)
mvn -pl mock-services -am test

# Allure HTML report from the accumulated */target/allure-results
mvn io.qameta.allure:allure-maven:report
```

`ci/scripts/run-tests.sh <env> <ui|api|messaging|all>` wraps the same calls
for local use.

## CI/CD

Two equivalent pipeline definitions are provided so the suite is portable
across orchestrators, matching either Jenkins or Harness:

- [`Jenkinsfile`](Jenkinsfile) - declarative pipeline, parameterized by
  environment and which suites to run, publishes Allure + JUnit + screenshots.
- [`ci/harness/pipeline.yml`](ci/harness/pipeline.yml) - the same stages
  expressed as a Harness CI pipeline.
- [`ci/docker/Dockerfile`](ci/docker/Dockerfile) - pins the Maven/JDK runner
  image so builds are reproducible off any agent.

Both pipelines call the exact same Maven commands documented above - the
orchestrator only schedules stages, it never encodes test logic.

## Framework building blocks

- **`ConfigManager`** - single source of truth for environment config,
  `-D` overridable.
- **`DriverFactory` / `DriverManager`** - `ThreadLocal<WebDriver>` so
  `testng.xml`'s `parallel="classes"` runs are isolated per thread; supports
  local drivers (via WebDriverManager) or a remote Selenium Grid.
- **`TestListener` / `RetryAnalyzer` / `RetryTransformer`** - screenshots on
  failure attached to Allure, automatic flaky-test retry applied to every
  `@Test` without annotating each one.
- **`RequestSpecFactory`** - one `RequestSpecification` per microservice
  (own base URI, shared auth/logging), so API clients stay one-liners.
- **JSON schema validation** (`api-tests/src/test/resources/schemas`) -
  contract tests fail on shape drift even when status codes still pass.
- **`WireMockServerManager`** - starts an embedded stub server from
  `mock-services/src/main/resources/wiremock`, so a service's consumer can be
  tested against a fixed contract without depending on a live upstream
  microservice being deployed and stable.

## Adding a new microservice

1. Add its base URL to each `environments/*.properties` file.
2. Add it to `ApiConfig.ServiceName` (api-tests).
3. Add a `<Service>Client` + request/response models under `api-tests`.
4. If it participates in the order flow, add its published event's schema to
   `messaging-tests`/`mock-services` as needed.
