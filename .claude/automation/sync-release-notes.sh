#!/usr/bin/env bash
# Runs the "sync newly-Done TC tickets into Release Notes Confluence page" job
# headlessly, invoked on a schedule by the scheduler
# (task name: EmployeeOnboarding-SyncReleaseNotes). See scheduled-tasks.json
# in this directory for the job definition this script implements.
set -uo pipefail

# Task Scheduler launches bash.exe directly, bypassing Git's usual
# interactive/login wrappers - so PATH starts out missing not just node/npm
# but Git's OWN coreutils (dirname, date, cat live in usr/bin next to
# bash.exe). Without this, `dirname` below fails, SCRIPT_DIR ends up empty,
# LOG_FILE resolves to a bogus path, and the whole script dies before
# writing a single log line - which is exactly what was happening. PATH
# must be fixed before using any external command, including SCRIPT_DIR
# resolution just below.
export PATH="/c/Users/astha.jain/AppData/Local/Programs/Git/usr/bin:/c/Program Files/nodejs:/c/Users/astha.jain/AppData/Roaming/npm:$PATH"

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
