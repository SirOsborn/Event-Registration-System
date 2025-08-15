# Event Management System Build Script
Write-Host "Building Event Management System..." -ForegroundColor Green

# Create build directory if it doesn't exist
if (!(Test-Path "build\classes")) {
    New-Item -ItemType Directory -Path "build\classes" -Force
    Write-Host "Created build/classes directory" -ForegroundColor Yellow
}

# Clean previous build
Remove-Item -Recurse -Force "build\classes\*" -ErrorAction SilentlyContinue
Write-Host "Cleaned previous build" -ForegroundColor Yellow

# Compile model classes
Write-Host "Compiling model classes..." -ForegroundColor Cyan
javac -d "build/classes" -cp "src/main/java" src/main/java/com/eventms/model/*.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Model classes compiled successfully" -ForegroundColor Green
} else {
    Write-Host "Model compilation failed" -ForegroundColor Red
    exit 1
}

# Compile service classes
Write-Host "Compiling service classes..." -ForegroundColor Cyan
javac -d "build/classes" -cp "build/classes;src/main/java" src/main/java/com/eventms/service/*.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Service classes compiled successfully" -ForegroundColor Green
} else {
    Write-Host "Service compilation failed" -ForegroundColor Red
    exit 1
}

# Compile view classes (GUI components)
Write-Host "Compiling view classes..." -ForegroundColor Cyan
javac -d "build/classes" -cp "build/classes;src/main/java" src/main/java/com/eventms/view/*.java
# Compile database classes (optional JDBC helpers)
Write-Host "Compiling database classes..." -ForegroundColor Cyan
javac -d "build/classes" -cp "build/classes;src/main/java;lib/*" src/main/java/com/eventms/database/*.java 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "Database classes compiled successfully" -ForegroundColor Green
} else {
    Write-Host "Warning: Database classes compilation had issues (JDBC driver may be missing). Continuing..." -ForegroundColor Yellow
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "View classes compiled successfully" -ForegroundColor Green
} else {
    Write-Host "View compilation failed" -ForegroundColor Red
    exit 1
}

# Compile launcher class
Write-Host "Compiling launcher..." -ForegroundColor Cyan
javac -d "build/classes" -cp "build/classes" src/main/java/com/eventms/Launcher.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Launcher compiled successfully" -ForegroundColor Green
} else {
    Write-Host "Launcher compilation failed" -ForegroundColor Red
    exit 1
}

Write-Host "Build completed successfully!" -ForegroundColor Green
Write-Host "Run with: java -cp 'build/classes;lib/*' com.eventms.Launcher" -ForegroundColor Yellow
