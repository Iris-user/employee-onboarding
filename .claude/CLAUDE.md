# Project Workflow

## Stack


## Tech Stack
- Language: Java
- Build Tool: Maven (or Gradle — update as appropriate)
- Framework: Spring Boot (update if different)
- Testing: JUnit 5
- Code Quality: SonarQube (local Docker, http://localhost:9000)
- Version Control: Git + GitHub
- Java/Spring Boot (or your stack)
- GitHub (public repo)
- Jira Cloud for issue tracking


## Explicitly NOT Used
- Python
- Node.js
- pip
- Any scripting language other than bash

## Build Commands
- Build: `mvn clean install`
- Test: `mvn test`
- Sonar scan: `mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=<token>`

## Git Configuration
- Remote: https://github.com/Iris-user/employee-onboarding.git
- Repo owner: Iris-user
- Contributor with write access: subodhmalik
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
1. Fetch the Jira ticket details via MCP
2. Pull latest from `main` branch
3. Create feature branch: `feature/PROJ-123-<short-description>`
4. Mark JIRA Status to IN PROGRESS
5. Follow TDD - Write failing Test Case using JUNIT 5, add imports to pom.xml if needed
6. run tests and ensure they are executing but failing
7. Implement the changes described in the ticket
8. Run SonarQube scan — fix any issues including code smells
9. Commit with message: `PROJ-123: <ticket summary>`
10. Push branch to GitHub
11. Raise a Pull Request with the Jira ticket summary as the PR description
12. Update README.md
13. Update JIRA with a detailed comment of the change, PR link and change status to IN REVIEW

## Coding Standards
- Follow existing code style in the repo
- Always follow TDD, test driven approach
- Do not merge — only raise the PR