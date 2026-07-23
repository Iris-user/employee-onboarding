---
name: pr-review
description: Review a GitHub pull request (or the current branch's diff before one is raised) against this project's engineering standards — correctness, JUnit 5 test coverage/TDD, SonarQube cleanliness, Jira ticket linkage, README updates, and the Maven/Spring Boot/Java-only stack rules. Use when asked to review a PR, review this branch before raising a PR, or check whether a PR is ready to merge.
---

# PR Review

## Scope

- If a PR number or URL is given, fetch it via the `github-official` MCP tools
  (`pull_request_read` for metadata/diff/comments) — repo is `Iris-user/employee-onboarding`.
- If no PR is given, review the current branch's diff against `main`:
  `git diff main...HEAD` (and `git log main..HEAD` for the commit list).
- Do not merge, approve, or push anything — this skill only produces a review.

## Gather context first

1. Get the Jira ticket ID from the branch name (`feature/TC-13-...`) or commit messages
   (`TC-13: ...`). Fetch the ticket with `jira_get_issue` to confirm the diff actually
   implements what the ticket describes — flag scope creep or missed requirements.
2. Read the changed files in full (not just the diff hunks) where behavior is non-trivial,
   so review comments account for surrounding context, not just added/removed lines.

## Review dimensions

- **Correctness** — logic errors, edge cases (nulls, empty lists, boundary values), error
  handling gaps, off-by-one issues.
- **Test coverage (TDD)** — this repo follows test-driven development with JUnit 5. Every
  new behavior in the diff should have a corresponding test. Flag production code changes
  with no accompanying test change.
- **Security** — OWASP-class issues: injection, missing input validation, secrets committed
  in code/config, overly permissive error messages.
- **Stack rules** — Maven only for build/deps; no Python, pip, virtualenvs, Node.js, or npm
  introduced anywhere in the diff (per CLAUDE.md).
- **Sonar cleanliness** — if SonarQube hasn't been run against this branch yet, say so and
  suggest running the `sonar-scan` skill before merging; don't re-run it yourself as part of
  review unless asked.
- **Documentation** — README.md updated when the diff adds/changes an endpoint or
  user-facing behavior (per this repo's workflow).
- **Simplification** — unnecessary abstractions, dead code, or scope beyond what the ticket
  asked for. Don't flag pre-existing style choices that aren't part of this diff.

## Reporting

Use the `ReportFindings` tool to report results: one entry per confirmed issue, most severe
first, with `file`, `summary`, and `failure_scenario` filled in concretely (real
input/state → wrong output, not a vague "could be improved"). If the diff is clean, call it
with an empty findings array rather than inventing minor nitpicks.

Do not fix issues found during this review — report them and let the user decide what to
apply, unless they explicitly ask you to fix as you go.
