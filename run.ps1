# Event Management System Run Script
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
Write-Host "Starting application..." -ForegroundColor Cyan
java -cp "build/classes" com.eventms.Main
