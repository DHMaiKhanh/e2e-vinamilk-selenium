---
name: git-commit-push
description: Stage the relevant changes, create a well-formed git commit, and push the current branch to GitHub. Use when the user asks to "commit", "commit và push", "push lên github", "lưu code lên git", or otherwise wants local changes saved and published to the remote.
---

# Git Commit & Push

Commits the current working-tree changes and pushes the branch to GitHub (origin).

## Steps

1. **Inspect state (run in parallel).**
   - `git status` — see tracked/untracked files.
   - `git diff` — see unstaged changes; `git diff --cached` for anything already staged.
   - `git log --oneline -10` — match this repo's commit message style.
   - `git branch --show-current` and `git status -sb` — confirm current branch and whether it
     tracks a remote / is ahead-behind.
2. **Decide what to stage.**
   - Default to staging everything relevant to the user's current work (`git add <files>` by name).
   - Never blindly `git add -A`/`git add .` without reviewing `git status` first — check for stray
     files (temp files, `.env`, credentials, large binaries) and exclude them.
   - If a file looks like it may contain secrets, open it and confirm before staging.
   - If the user named specific files, stage only those.
3. **Write the commit message.**
   - 1-2 sentences, focused on *why*, not a line-by-line restatement of the diff.
   - Match the repo's existing style (see `git log` output) — this repo uses short imperative
     summaries (e.g. "Add HomePage test suite, dashboard viewer, and docs").
   - Use a heredoc so formatting/quoting is safe:
     ```
     git commit -m "$(cat <<'EOF'
     <summary line>

     Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
     EOF
     )"
     ```
4. **Push.**
   - If the branch already tracks a remote: `git push`.
   - If it's a new local branch with no upstream: `git push -u origin <branch>`.
   - Never force-push (`--push -f`/`--force`) unless the user explicitly asks for it, and warn them
     first if the target is `main`/`master`.
5. **Verify.** Run `git status` after the push and report: files committed, commit message used,
   branch pushed to, and confirmation the push succeeded (or the error if it didn't).

## Rules

- Never run `--no-verify`, `--no-gpg-sign`, or otherwise skip hooks/signing.
- Never amend an existing commit unless the user explicitly asks for `git commit --amend`.
- Only commit/push when the user has asked for it in this turn — this skill is invoked on demand,
  not proactively.
- If there is nothing staged and nothing to commit, say so rather than creating an empty commit.
- If a pre-commit hook fails, fix the underlying issue, re-stage, and create a **new** commit rather
  than bypassing the hook.
- If the push is rejected (e.g. remote has new commits), stop and tell the user rather than force
  pushing — ask whether they want to pull/rebase first.
</content>
