@echo off
REM Lightweight Gradle runner for Windows: downloads a Gradle distribution if not present and runs it.
SETLOCAL ENABLEDELAYEDEXPANSION

SET GRADLE_VERSION=8.4.1
SET WRAPPER_DIR=%USERPROFILE%\.gradle\wrapper\gradle-%GRADLE_VERSION%
SET GRADLE_BIN=%WRAPPER_DIR%\bin\gradle.bat

:check
IF EXIST "%GRADLE_BIN%" GOTO run

necho Gradle not found locally. Downloading Gradle %GRADLE_VERSION% to %WRAPPER_DIR% ...
SET ZIPFILE=%TEMP%\gradle-%GRADLE_VERSION%-bin.zip

powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%ZIPFILE%'"
IF ERRORLEVEL 1 (
  echo Failed to download Gradle. Please install Gradle manually or ensure internet access.
  EXIT /B 1
)

n
powershell -Command "Expand-Archive -Path '%ZIPFILE%' -DestinationPath '%TEMP%\gradle_unpack' -Force"
IF ERRORLEVEL 1 (
  echo Failed to extract Gradle. Ensure Expand-Archive is available (PowerShell 5+).
  EXIT /B 1
)

nmd "%WRAPPER_DIR%" >nul 2>&1
robocopy "%TEMP%\gradle_unpack\gradle-%GRADLE_VERSION%" "%WRAPPER_DIR%" /E >nul
IF ERRORLEVEL 8 (
  echo Failed to copy Gradle files.
  EXIT /B 1
)

ndel /q "%ZIPFILE%" >nul 2>&1
rmdir /s /q "%TEMP%\gradle_unpack" >nul 2>&1

:run
n"%GRADLE_BIN%" %*
ENDLOCAL
