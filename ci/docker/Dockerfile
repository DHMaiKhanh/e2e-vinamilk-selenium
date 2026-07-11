# Runner image for executing the automation suite in CI without depending on
# whatever JDK/Maven happen to be preinstalled on a build agent.
FROM maven:3.9.11-eclipse-temurin-17

WORKDIR /workspace

COPY pom.xml .
COPY automation-core/pom.xml automation-core/pom.xml
COPY ui-tests/pom.xml ui-tests/pom.xml
COPY api-tests/pom.xml api-tests/pom.xml
COPY messaging-tests/pom.xml messaging-tests/pom.xml
COPY mock-services/pom.xml mock-services/pom.xml

# Warm the dependency cache as its own layer so source-only changes don't
# force a full re-download.
RUN mvn -B -ntp -q dependency:go-offline || true

COPY . .

ENTRYPOINT ["mvn", "-B", "-ntp"]
CMD ["test"]
