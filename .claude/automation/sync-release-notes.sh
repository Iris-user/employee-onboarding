#!/usr/bin/env bash
# Runs the "sync newly-Done TC tickets into Release Notes Confluence page" job
# headlessly, invoked on a schedule by the scheduler
# (task name: EmployeeOnboarding-SyncReleaseNotes). See scheduled-tasks.json
# in this directory for the job definition this script implements.

# Task Scheduler launches bash.exe directly by absolute path, so the
# environment it inherits does NOT include Git's own usr/bin (dirname, sed,
# ls, uname, which, ...) the way an interactive Git Bash shell does, nor the
# node/npm global bin dirs. This must be set before ANY external command
# runs below — including dirname a few lines down, and the coreutils the
# `claude` npm shim itself calls internally to resolve its own install path.
export PATH="/c/Users/astha.jain/AppData/Local/Programs/Git/usr/bin:/c/Users/astha.jain/AppData/Local/Programs/Git/bin:/c/Program Files/nodejs:/c/Users/astha.jain/AppData/Roaming/npm:$PATH"

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_FILE="$SCRIPT_DIR/logs/sync-release-notes.log"

# Everything from here on is logged, including setup failures (e.g. cd or
# PATH problems) that used to die silently before the log line was reached.
{
  echo "===== $(date '+%Y-%m-%d %H:%M:%S') ====="
  set -e

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