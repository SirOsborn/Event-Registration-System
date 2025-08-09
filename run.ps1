# Event Management System Run Script
param(
    [switch]$gui,
    [switch]$help
)

if ($help) {
    Write-Host "Event Management System Run Script" -ForegroundColor Green
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\run.ps1           - Run console demo version" -ForegroundColor White
    Write-Host "  .\run.ps1 -gui      - Run GUI version" -ForegroundColor White
    Write-Host "  .\run.ps1 -help     - Show this help message" -ForegroundColor White
    exit 0
}

Write-Host "Running Event Management System..." -ForegroundColor Green

# Check if build exists
if (!(Test-Path "build\classes\com\eventms\Main.class")) {
    Write-Host "Build not found. Running build script first..." -ForegroundColor Yellow
    & .\build.ps1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Build failed. Cannot run application." -ForegroundColor Red
        exit 1
    }
}

# Run the application
if ($gui) {
    Write-Host "Starting GUI application..." -ForegroundColor Cyan
    java -cp "build/classes" com.eventms.Main --gui
} else {
    Write-Host "Starting console demo..." -ForegroundColor Cyan
    java -cp "build/classes" com.eventms.Main
}
