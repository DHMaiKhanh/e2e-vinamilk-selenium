#!/usr/bin/env bash
# Convenience wrapper mirroring the CI stages, for local use before pushing.
#
# Usage: ci/scripts/run-tests.sh [dev|staging|prod] [ui|api|messaging|all]
set -euo pipefail

TEST_ENV="${1:-dev}"
SCOPE="${2:-all}"

run_api() {
  mvn -B -ntp -pl api-tests -am test -Denv="${TEST_ENV}" \
    -DsuiteXmlFile=src/test/resources/testng-api.xml
}

run_ui() {
  mvn -B -ntp -pl ui-tests -am test -Denv="${TEST_ENV}" \
    -DsuiteXmlFile=src/test/resources/testng-ui.xml
}

run_messaging() {
  mvn -B -ntp -pl messaging-tests -am test -Denv="${TEST_ENV}" \
    -DsuiteXmlFile=src/test/resources/testng-messaging.xml
}

mvn -B -ntp clean install -DskipTests

case "${SCOPE}" in
  ui) run_ui ;;
  api) run_api ;;
  messaging) run_messaging ;;
  all)
    run_api
    run_ui
    run_messaging
    ;;
  *)
    echo "Unknown scope: ${SCOPE} (expected ui|api|messaging|all)" >&2
    exit 1
    ;;
esac
