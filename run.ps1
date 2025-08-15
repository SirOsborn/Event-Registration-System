# Event Management System Run Script
param(
    [switch]$help
)

if ($help) {
    Write-Host "Event Management System Run Script" -ForegroundColor Green
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\run.ps1           - Run GUI version" -ForegroundColor White
    Write-Host "  .\run.ps1 -help     - Show this help message" -ForegroundColor White
    exit 0
}

Write-Host "Running Event Management System..." -ForegroundColor Green

# Load local environment overrides if present (set DB_URL/DB_USER/DB_PASSWORD/DB_ON here)
$envFile = Join-Path $PSScriptRoot "env.ps1"
if (Test-Path $envFile) {
    Write-Host "Loading environment from env.ps1" -ForegroundColor Yellow
    . $envFile
}

# Check if build exists
if (!(Test-Path "build\classes\com\eventms\Launcher.class")) {
    Write-Host "Build not found. Running build script first..." -ForegroundColor Yellow
    & .\build.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Build failed. Cannot run application." -ForegroundColor Red
        exit 1
    }
}

# Run the application (GUI only)
Write-Host "Starting GUI application..." -ForegroundColor Cyan
java -cp "build/classes;lib/*" com.eventms.Launcher
