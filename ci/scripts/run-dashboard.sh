#!/usr/bin/env bash
# One command: run the full UI regression suite, build the report, and open the dashboard.
#
# Usage: ci/scripts/run-dashboard.sh [env] [headless]
#   env      dev|staging|prod   (default: prod)
#   headless true|false         (default: false)
set -uo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
TEST_ENV="${1:-prod}"
HEADLESS="${2:-false}"

cd "${ROOT_DIR}"

echo ">> Building all modules (tests skipped)..."
mvn -B -ntp clean install -DskipTests

echo ">> Running full ui-tests suite (env=${TEST_ENV}, headless=${HEADLESS})..."
mvn -B -ntp -pl ui-tests test \
  -Dmaven.test.failure.ignore=true \
  -Denv="${TEST_ENV}" \
  -Dselenium.grid.enabled=false \
  -Dbrowser.headless="${HEADLESS}"
# Test failures must not abort the script: failed/broken tests are exactly what
# the dashboard exists to surface, so we always continue to the report step.

cd "${ROOT_DIR}/dashboard"
if [ ! -d node_modules ]; then
  echo ">> Installing dashboard dependencies (first run only)..."
  npm install
fi

echo ">> Building report.json from ui-tests/target/allure-results and starting the dashboard..."
npm run dev
