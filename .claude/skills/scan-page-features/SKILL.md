---
name: scan-page-features
description: Given the name of a Vinamilk website page (e.g. "Home Page", "Product Listing", "Cart"), scan it live with the Playwright MCP, list its real features, and write a detailed feature + test case Markdown spec for review. Does NOT generate any Java code — that's a separate follow-up skill once the spec is approved. Use when the user names a screen/page and asks to inventory its features and/or draft test cases for it.
---

# Scan Page Features

Turns "quét màn hình X, liệt kê tính năng, viết test case" into a repeatable pipeline:
live browser scan → feature inventory → detailed Markdown spec (feature description + test cases)
for the user to review. Code generation (Page Object + TestNG test class) is intentionally out of
scope for this skill — it happens later, in a separate skill, once the user approves the spec.

## Inputs

- A page name or path (Vietnamese or English name is fine — e.g. "Trang chủ" / "Home Page",
  "Giỏ hàng" / "Cart", "/store-list"). Map it to the site's base URL from
  `automation-core/src/main/resources/environments/dev.properties` (fallback `prod.properties`,
  key `ui.baseUrl`) plus the page's path.

## Steps

1. **Navigate and snapshot.** Use `mcp__playwright__browser_navigate` to the page URL, then
   `mcp__playwright__browser_snapshot` to get the accessibility tree. Don't rely on WebFetch/static
   HTML — this site is JS-rendered, and hidden UI (search flyouts, dropdown menus, modals) only
   appears after interaction.
2. **Reveal hidden/dynamic UI.** Click icon-only buttons, nav dropdowns, "load more"/pagination
   controls, and modal triggers with `mcp__playwright__browser_click`, then re-snapshot each time.
   Look especially for: search dialogs, account/login menus, cart drawers, filter/sort dropdowns,
   accordions, carousels with prev/next controls. Note every distinct URL a click lands on.
3. **Compile the feature list.** For each real, confirmed feature capture: what it is, the visible
   Vietnamese label/text, and any element you can key a Selenium locator off (role, text, href
   pattern, class fragment). Skip cosmetic-only elements (decorative images) unless they gate a
   user action.
4. **Update the feature inventory doc** at `docs/vinamilk-features.md`. Find (or add) the matching
   numbered section for this page. Replace placeholder/unconfirmed bullets with confirmed ones
   (`[x]`), keep genuinely still-unverified items as `[ ]`, and add a one-line note with the scan
   date and method (`MCP Playwright browser_navigate + browser_snapshot`). Link to the spec file
   from step 5 next to the section heading (e.g. `[docs/features/home-page.md](features/home-page.md)`).
5. **Write a single detailed spec file for the whole page** at `docs/features/<page-slug>.md`
   (slug = kebab-case page name, e.g. `docs/features/home-page.md`, `docs/features/cart.md`). Do
   NOT create a per-page folder or one file per feature — every feature for this page lives as its
   own section inside this one file. Create the file if missing, otherwise update it in place;
   append a new numbered section for any newly discovered feature. File structure:
   - Header: `# <Page Name> — Tổng hợp tính năng & Test Cases`, page URL, scan date, scan method.
   - A `## Mục lục` (table of contents) linking to each feature section by anchor.
   - One `## <N>. <Feature Name>` section per confirmed feature, each containing:
     - `### Mô tả tính năng` — what it is, the visible Vietnamese label/text, where it lives on the
       page (header/section/modal/etc.), how a user triggers it, and any element detail useful for
       a future locator (role, text, href pattern, class fragment) — but no Java/code, this is a
       plain description.
     - `### Test Cases` — a table with one row per test case: ID (e.g. `TC-HOME-01`), title,
       precondition, steps, expected result, priority/severity (Critical/High/Medium/Low). Cover
       the happy path plus realistic edge cases (empty state, invalid input, disabled/hidden
       state) — but only for behavior actually observed or clearly implied by the UI, not
       speculative ones. Keep ID numbering unique and sequential across the whole file (don't
       restart from 01 per section, e.g. `TC-HOME-01`..`TC-HOME-23`).
   - Write everything in Vietnamese, consistent with `docs/vinamilk-features.md` wording.
6. **Report back** a short summary: feature count found, which were already covered vs newly
   discovered, and confirmation that `docs/features/<page-slug>.md` was written/updated. Note this
   is a spec for review only — no Page Object or test code has been generated. Ask the user to
   review that file before triggering the follow-up code-generation skill.

## Conventions to preserve

- This skill never touches `ui-tests/src/**` — no Page Object, no TestNG class, no Java at all.
  Code generation is a separate, later skill that consumes the approved `.md` spec.
- Don't invent features that weren't actually observed in the live snapshot — mark genuinely
  unverifiable items as `[ ]` (in `docs/vinamilk-features.md`) or omit them from the spec rather
  than guessing.
- Keep bullet/spec wording in Vietnamese, consistent with existing rows in `docs/vinamilk-features.md`.
- One page = one file at `docs/features/<page-slug>.md` (e.g. `docs/features/home-page.md`),
  containing every feature of that page as its own numbered section. Never create a per-page
  folder or per-feature files (e.g. `docs/features/home-page/01-top-utility-bar.md`) — that older
  convention is deprecated.
