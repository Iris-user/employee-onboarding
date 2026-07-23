# Project Workflow

## Stack


## Tech Stack
- Language: Java
- Build Tool: Maven (or Gradle — update as appropriate)
- Framework: Spring Boot (update if different)
- Testing: JUnit 5
- Code Quality: SonarQube (local Docker, http://localhost:9000)
- Version Control: Git + GitHub
- Jira Cloud for issue tracking
- Confluence Cloud for documentation (https://nitinmvs-v2.atlassian.net/wiki, via the `atlassian` MCP server alongside Jira)


## Explicitly NOT Used
- Python
- Node.js
- pip
- Any scripting language other than bash

## Git Configuration
- Remote: https://github.com/Iris-user/employee-onboarding.git
- Repo owner: Iris-user
- Contributor with write access: astha-jain-mcp
- Git credential helper: Windows Credential Manager (credentials pre-stored)
- Always push via `git push` — never embed tokens in remote URLs
- Never push directly to main
- Branch naming: feature/JIRA-ID-short-description

## Rules
- Never use Python or Python-based tools
- Never use pip or virtual environments
- Use Maven for all build, test, and dependency tasks

## Feature Development Workflow
When given a Jira ID (e.g. PROJ-123), follow these steps:
1. Checkout to main branch and fetch the Jira ticket details via MCP
2. Pull latest from `main` branch
3. Create feature branch: `feature/PROJ-123-<short-description>`
4. Mark JIRA Status to IN PROGRESS and assign JIRA to astha.jain63@gmail.com
5. Follow TDD - Write failing Test Case using JUNIT 5, add imports to pom.xml if needed
6. run tests and ensure they are executing but failing
7. Implement the changes described in the ticket
8. Run SonarQube scan — fix any issues including code smells
9. Commit with message: `PROJ-123: <ticket summary>`
10. Push branch to GitHub
11. Raise a Pull Request with the Jira ticket summary as the PR description
12. Update README.md
13. Update JIRA with a detailed comment of the change, PR link and change status to IN REVIEW
14. Once the ticket reaches IN REVIEW, log a Jira worklog entry for the time actually spent implementing: pull the issue's changelog, use the "To Do → In Progress" transition timestamp as `started`, and the "In Progress → In Review" transition timestamp to compute `time_spent` (round to the nearest minute). Add a short comment summarizing what was done in that span.
    - If the ticket never passed through both of those transitions (e.g. went straight To Do → Done, or skipped In Review), skip the worklog — there's no valid span to log.
    - If the gap between the two transitions is implausibly long (multi-day/week — a stale ticket picked up long after creation, not continuous work), skip the worklog rather than logging misleading elapsed time.
    - If a worklog was previously logged for this ticket using a different span (e.g. In Progress → Done) and needs correcting, there's no delete-worklog tool available — add a new entry with the corrected duration and a comment noting it supersedes the earlier one, rather than trying to remove the old entry.
15. Perform the code review on the open PRs and do not perform the changes

## Coding Standards
- Follow existing code style in the repo
- Always follow TDD, test driven approach
- Do not merge — only raise the PR