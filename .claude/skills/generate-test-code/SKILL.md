---
name: generate-test-code
description: Given an approved feature/test-case Markdown spec (from the scan-page-features skill, e.g. docs/features/home-page.md), generate the Java Page Object + TestNG test class that implement those test cases in ui-tests/src. Use when the user says things like "gen code từ file .md này", "tạo automation test từ spec", or names a docs/features/*.md file and asks for code.
---

# Generate Test Code From Markdown Spec

Turns an approved `docs/features/<page-slug>.md` spec (feature descriptions + `TC-XXX-NN` test
case tables) into real, compiling Selenium/TestNG automation code in `ui-tests/src`.

## Inputs

- Path to a spec file, e.g. `docs/features/home-page.md`. If the user names a page instead of a
  file, resolve it to `docs/features/<page-slug>.md` (kebab-case of the page name). If that file
  doesn't exist, stop and tell the user to run the `scan-page-features` skill first — do not
  invent a spec from scratch.

## Steps

1. **Read the spec fully.** Read every feature section and its `Test Cases` table. Note the page
   URL, every element description with locator hints (role/text/href pattern/class fragment), and
   every `TC-<PAGE>-NN` row (title, precondition, steps, expected result, priority).
2. **Survey existing code first.** Read `ui-tests/src/main/java/com/vinamilk/automation/ui/pages/BasePage.java`
   and at least one existing Page Object (e.g. `HomePage.java`) and one existing TestNG test class
   (e.g. `HomePageTest.java`, `AddToCartTest.java`) to match conventions:
   - Page Objects extend `BasePage`, live in `ui-tests/src/main/java/com/vinamilk/automation/ui/pages/`,
     declare locators as `private static final By NAME = By...` constants at the top, expose
     action methods (`click`, `type`, navigation returning the resulting Page Object) and query
     methods (`isXDisplayed`, `xCount()`, etc.) built on `BasePage` helpers — never raw
     `driver.findElement` waits duplicated from `BasePage`.
   - Test classes extend `BaseUiTest`, live in `ui-tests/src/test/java/com/vinamilk/automation/ui/tests/`,
     use `@Epic`/`@Feature`/`@Test(description = ...)`/`@Severity`/`@Description` (Allure) matching
     the spec's priority (Critical→`SeverityLevel.CRITICAL`, High→`CRITICAL`/`NORMAL` per existing
     mapping in the codebase, Medium→`NORMAL`, Low→`MINOR`), and assert with TestNG `Assert`.
   - Reuse `ConfigManager.getInstance().get("ui.baseUrl")` for navigation, matching `@BeforeMethod`
     setup patterns already in use.
3. **Reuse or extend the Page Object.** If a Page Object for this page already exists, add only the
   missing locators/methods needed for the new test cases — never duplicate an existing locator or
   method under a new name. If it doesn't exist, create it following the BasePage pattern.
4. **Generate one `@Test` method per `TC-<PAGE>-NN` row** (or group tightly related rows sharing
   setup into one method only if the spec's steps clearly describe one continuous flow — default
   to one test per TC id). Method names in lowerCamelCase describing the behavior (not the ID).
   Carry the TC id into the Allure `@Description` so the generated test traces back to the spec
   row. Implement the exact steps and expected-result assertions from the table — don't invent
   assertions the spec didn't ask for, and don't skip a TC row silently.
5. **Compile-check.** After writing/editing files, run the module's build (e.g. `mvn -q -pl ui-tests -am compile test-compile`
   from the repo root, adjusting for the actual build tool/module layout found in the repo) to
   catch typos/missing imports. Fix errors before finishing; report to the user if a locator can't
   be verified without a live browser and should be double-checked against a Playwright snapshot.
6. **Report back**: which spec file was consumed, which Page Object/test class files were created
   vs. modified, how many TC rows were implemented vs. skipped (and why, if any), and the compile
   result.

## Conventions to preserve

- Never invent a test case that isn't backed by a row in the spec's `Test Cases` table — this
  skill implements the spec, it doesn't extend it.
- Never touch `docs/**` — writing the analysis of what was generated is a separate follow-up skill
  (`document-generated-code`), not this one.
- Match existing package structure and naming exactly (`com.vinamilk.automation.ui.pages`,
  `com.vinamilk.automation.ui.tests`) — don't introduce new packages or folders.
- Prefer extending an existing Page Object/test class over creating a parallel one for the same
  page.
