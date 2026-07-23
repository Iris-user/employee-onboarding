---
name: sonar-scan
description: Run a SonarQube scan against the local Docker SonarQube instance for this Maven/Spring Boot project, read back the reported issues, and fix code smells/bugs/vulnerabilities before committing. Use this whenever the workflow calls for a "Sonar scan" step (e.g. step 8 of the Feature Development Workflow in CLAUDE.md), or when asked to check/fix Sonar/code-quality issues.
---

# Sonar Scan

## Prerequisites
- Local SonarQube must be running at `http://localhost:9000` (Docker).
- A valid Sonar token is required. Never hardcode it in a command, a file, or CLAUDE.md.
  Read it from an environment variable (e.g. `SONAR_TOKEN`), or ask the user for it if not
  set. If a token is ever found sitting in a repo file or shell history, flag it to the user
  as a secret to rotate — do not reuse or reprint it.

## Steps

1. Build first so the scan analyzes current code:
   ```
   mvn clean install
   ```

2. Run the scan:
   ```
   mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=$SONAR_TOKEN
   ```
   (On Windows Git Bash, `$SONAR_TOKEN` must already be exported in the shell env — don't
   inline a literal token.)

3. Read the scan output/report for the project. If an MCP Sonar server is connected in this
   session (tool names like `mcp__sonar__issues`), prefer calling it to fetch structured
   issues instead of parsing CLI output. Otherwise open the dashboard URL printed at the end
   of the CLI run.

4. Triage findings:
   - Bugs / Vulnerabilities: must fix before proceeding.
   - Code Smells: fix unless clearly a false positive — note the reasoning if skipped.
   - Coverage: `**/Main.java` is intentionally excluded via `sonar.coverage.exclusions` in
     `pom.xml` — don't try to "fix" that exclusion or add tests to satisfy it.

5. Apply fixes directly in the source (no suppression annotations or config-based
   exclusions as a shortcut — fix the underlying issue).

6. Re-run the scan (steps 1–2) to confirm the fixes cleared the reported issues before
   moving on to commit/PR steps.
