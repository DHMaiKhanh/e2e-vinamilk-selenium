# One command: run the full UI regression suite, build the report, and open the dashboard.
#
# Usage: .\ci\scripts\run-dashboard.ps1 [-TestEnv prod] [-Headless false]
param(
    [string]$TestEnv = "prod",
    [string]$Headless = "false"
)

$ErrorActionPreference = "Continue"
$RootDir = Resolve-Path (Join-Path $PSScriptRoot "..\..")

Set-Location $RootDir

Write-Host ">> Building all modules (tests skipped)..."
mvn -B -ntp clean install -DskipTests

Write-Host ">> Running full ui-tests suite (env=$TestEnv, headless=$Headless)..."
mvn -B -ntp -pl ui-tests test `
    "-Dmaven.test.failure.ignore=true" `
    "-Denv=$TestEnv" `
    "-Dselenium.grid.enabled=false" `
    "-Dbrowser.headless=$Headless"
# Test failures must not abort the script: failed/broken tests are exactly what
# the dashboard exists to surface, so we always continue to the report step.

Set-Location (Join-Path $RootDir "dashboard")
if (-not (Test-Path "node_modules")) {
    Write-Host ">> Installing dashboard dependencies (first run only)..."
    npm install
}

Write-Host ">> Building report.json from ui-tests/target/allure-results and starting the dashboard..."
npm run dev
