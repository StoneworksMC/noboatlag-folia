# Builds noboatlag with JDK 25 (required by Folia 26.2) regardless of the
# machine's global JAVA_HOME. Usage:  .\build.ps1   (add args, e.g. .\build.ps1 -o)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot"
Write-Host "Using JDK: $env:JAVA_HOME" -ForegroundColor Cyan
& mvn clean package @args
