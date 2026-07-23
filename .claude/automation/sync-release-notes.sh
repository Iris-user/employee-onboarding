#!/usr/bin/env bash
# Runs the "sync newly-Done TC tickets into Release Notes Confluence page" job
# headlessly, invoked on a schedule by the scheduler
# (task name: EmployeeOnboarding-SyncReleaseNotes). See scheduled-tasks.json
# in this directory for the job definition this script implements.
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_FILE="$SCRIPT_DIR/logs/sync-release-notes.log"

# Everything from here on is logged, including setup failures (e.g. cd or
# PATH problems) that used to die silently before the log line was reached.
{
  echo "===== $(date '+%Y-%m-%d %H:%M:%S') ====="
  set -e

  # Task Scheduler launches bash.exe non-interactively/non-login, so
  # ~/.bashrc / Git's /etc/profile are never sourced and PATH additions made
  # there (node, npm global bin) may be missing. Make PATH self-contained
  # here instead of relying on the invoking shell's environment.
  export PATH="/c/Program Files/nodejs:/c/Users/astha.jain/AppData/Roaming/npm:$PATH"

  PROJECT_DIR="/d/ClaudeCodePOC/employee-onboarding"
  PROMPT_FILE="$SCRIPT_DIR/sync-release-notes-prompt.txt"
  CLAUDE_BIN="/c/Users/astha.jain/AppData/Roaming/npm/claude"

  cd "$PROJECT_DIR"

  "$CLAUDE_BIN" -p "$(cat "$PROMPT_FILE")" \
    --allowedTools "mcp__MCP_DOCKER__atlassian__jira_search mcp__MCP_DOCKER__atlassian__confluence_get_page mcp__MCP_DOCKER__atlassian__confluence_update_page" \
    --output-format text \
    --no-session-persistence
  echo
} >> "$LOG_FILE" 2>&1
STATUS=$?
exit "$STATUS"
