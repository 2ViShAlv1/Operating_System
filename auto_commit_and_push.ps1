param(
    [string]$RepoPath = (Split-Path -Parent $MyInvocation.MyCommand.Path),
    [int]$DebounceSeconds = 2
)

Set-Location -Path $RepoPath

function Log {
    param($msg)
    $time = (Get-Date).ToString('o')
    Write-Output "[$time] $msg"
}

if (-not (Test-Path (Join-Path $RepoPath ".git"))) {
    Log "No .git found at $RepoPath - exiting."
    exit 1
}

$watcher = New-Object System.IO.FileSystemWatcher $RepoPath -Property @{ IncludeSubdirectories = $true; NotifyFilter = [System.IO.NotifyFilters]'FileName, LastWrite, DirectoryName' }

$pending = $false
$lastEventTime = Get-Date

Register-ObjectEvent $watcher Changed -SourceIdentifier FileChanged -Action {
    $script:pending = $true
    $script:lastEventTime = Get-Date
}
Register-ObjectEvent $watcher Created -SourceIdentifier FileCreated -Action {
    $script:pending = $true
    $script:lastEventTime = Get-Date
}
Register-ObjectEvent $watcher Deleted -SourceIdentifier FileDeleted -Action {
    $script:pending = $true
    $script:lastEventTime = Get-Date
}
Register-ObjectEvent $watcher Renamed -SourceIdentifier FileRenamed -Action {
    $script:pending = $true
    $script:lastEventTime = Get-Date
}

$watcher.EnableRaisingEvents = $true

Log "Auto-sync watcher started for $RepoPath. Press Ctrl+C to stop."

try {
    while ($true) {
        Start-Sleep -Seconds 1
        if ($pending) {
            $elapsed = (Get-Date) - $lastEventTime
            if ($elapsed.TotalSeconds -ge $DebounceSeconds) {
                $pending = $false

                # Stage all changes
                Log "Detected changes — staging all files."
                & git -C $RepoPath add -A

                # Check if there's anything to commit
                $status = & git -C $RepoPath status --porcelain
                if ([string]::IsNullOrWhiteSpace($status)) {
                    Log "No changes to commit."
                    continue
                }

                $commitMsg = "Auto-sync: $(Get-Date -Format o)"
                Log "Committing: $commitMsg"
                & git -C $RepoPath commit -m "$commitMsg" | Out-Null

                Log "Pushing to origin"
                try {
                    & git -C $RepoPath push origin HEAD
                    Log "Push succeeded."
                } catch {
                    Log "Push failed: $($_.Exception.Message)"
                }
            }
        }
    }
} finally {
    Unregister-Event -SourceIdentifier FileChanged -ErrorAction SilentlyContinue
    Unregister-Event -SourceIdentifier FileCreated -ErrorAction SilentlyContinue
    Unregister-Event -SourceIdentifier FileDeleted -ErrorAction SilentlyContinue
    Unregister-Event -SourceIdentifier FileRenamed -ErrorAction SilentlyContinue
    $watcher.Dispose()
}
