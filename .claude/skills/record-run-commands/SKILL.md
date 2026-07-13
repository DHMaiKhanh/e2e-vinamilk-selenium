---
name: record-run-commands
description: Keep a single Markdown file (docs/run-commands.md) listing the Maven command to run each test feature/class in ui-tests, and regenerate it whenever test classes are added, renamed, or removed. Use when the user gives a run command to save, asks "lưu lệnh chạy test này", "cập nhật file lệnh chạy", or wants a reference file of all `mvn -pl ui-tests test -Dtest=...` commands.
---

# Record Run Commands

Maintains `docs/run-commands.md` as the single source of truth for how to run each test
feature/class in this project via Maven/TestNG.

## Inputs

- Either: a specific run command the user just gave you (e.g.
  `mvn -pl ui-tests test "-Dtest=HomePageTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"`),
  or: a request to (re)generate the full file for every test class.

## Steps

1. **Discover test classes.** List `ui-tests/src/test/java/com/vinamilk/automation/ui/tests/*Test.java`.
   For each, read the class-level `@Feature("...")` annotation (Allure) to get its feature name —
   fall back to the class name (without `Test` suffix, space-split on case) if no `@Feature` is
   present.
2. **Build/update the command table.** For each test class, the canonical command is:
   ```
   mvn -pl ui-tests test "-Dtest=<ClassName>" "-Denv=<env>" "-Dselenium.grid.enabled=<bool>" "-Dbrowser.headless=<bool>"
   ```
   - Default `-Denv=prod`, `-Dselenium.grid.enabled=false`, `-Dbrowser.headless=false` unless the
     user's given command specifies different values — in that case use the user's values for that
     class and note them as the recorded default for it.
   - If the user gives a new/updated command for a class already in the file, replace that class's
     row/command in place; don't duplicate it.
3. **Write `docs/run-commands.md`** (create `docs/` if missing — it already exists here). Structure:
   - `# Run Commands` header, one-line intro that this is generated/maintained by this skill.
   - One section per feature: `## <Feature Name> (<ClassName>)`, with the fenced `bash` command
     block, plus a one-line note of what flags mean if non-default (e.g. `env=prod` targets the
     production site, `browser.headless=false` opens a visible browser window).
   - Keep sections ordered the same as the test classes appear on disk (alphabetical is fine) so
     re-runs produce a stable diff.
   - End with a `## Common flags` section documenting the reusable flags once (`-Dtest`, `-Denv`,
     `-Dselenium.grid.enabled`, `-Dbrowser.headless`) instead of repeating explanations per class.
4. **Report back** the file path and which class(es) were added/updated in this run.

## Conventions to preserve

- This skill only ever touches `docs/run-commands.md` — it does not run the commands or modify test
  code.
- One file for the whole project; never create per-class or dated copies.
- If a test class has multiple logically distinct suites worth running separately (e.g. smoke vs.
  full), list them as separate commands under the same feature section rather than separate files.
