Auto-sync watcher for this repository

Overview
- This repository already has a Git repo and `origin` remote configured.
- `auto_commit_and_push.ps1` watches the working tree, auto-stages changes, commits with a timestamped message, and pushes to `origin`.

Quick start
1. Ensure Git is installed and you can push to the remote from this machine.
   - Recommended (Windows): install Git for Windows which includes Git Credential Manager (GCM).
   - Or configure SSH keys and switch the remote to SSH: `git remote set-url origin git@github.com:username/repo.git`
2. Run the watcher (from the repo root):

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\auto_commit_and_push.ps1
```

Behavior
- Debounce waiting: 2 seconds (configurable via the script parameter `$DebounceSeconds`).
- Commit message format: `Auto-sync: <ISO-timestamp>`.
- Stages all files (`git add -A`) before committing.

Security notes
- Do NOT store tokens or secrets in the repo. Add them to `.gitignore` if present.
- If you choose HTTPS+token, do not embed tokens in source files — use Git credential manager or environment-based secrets.

Optional: run at logon
- To run this automatically at user logon, create a Scheduled Task. Example command (run in an elevated prompt to create for current user):

```powershell
$script = Join-Path (Resolve-Path .) 'auto_commit_and_push.ps1'
schtasks /Create /TN "AutoSyncRepo" /TR "powershell -NoProfile -ExecutionPolicy Bypass -File \"$script\"" /SC ONLOGON /RL HIGHEST
```

Notes
- The watcher performs pushes directly; ensure your Git credentials are configured for non-interactive pushes (GCM or SSH agent).
- If you want, I can add a Scheduled Task now or convert remote to SSH for you.
