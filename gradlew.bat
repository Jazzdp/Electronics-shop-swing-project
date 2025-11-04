@echo off
REM Lightweight Gradle runner for Windows: downloads a Gradle distribution if not present and runs it.
SETLOCAL ENABLEDELAYEDEXPANSION


:check
IF EXIST "%GRADLE_BIN%" GOTO run

echo Gradle not found locally. Downloading Gradle %GRADLE_VERSION% to %WRAPPER_DIR% ...
SET ZIPFILE=%TEMP%\gradle-%GRADLE_VERSION%-bin.zip

powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri \"https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip\" -OutFile '%ZIPFILE%'"
IF ERRORLEVEL 1 (
  echo Failed to download Gradle. Please install Gradle manually or ensure internet access.
  EXIT /B 1
)

powershell -Command "Expand-Archive -Path '%ZIPFILE%' -DestinationPath '%TEMP%\gradle_unpack' -Force"
IF ERRORLEVEL 1 (
  echo Failed to extract Gradle. Ensure Expand-Archive is available (PowerShell 5+).
  EXIT /B 1
)

md "%WRAPPER_DIR%" >nul 2>&1
robocopy "%TEMP%\gradle_unpack\gradle-%GRADLE_VERSION%" "%WRAPPER_DIR%" /E >nul
IF ERRORLEVEL 8 (
  echo Failed to copy Gradle files.
  EXIT /B 1
)

del /q "%ZIPFILE%" >nul 2>&1
rmdir /s /q "%TEMP%\gradle_unpack" >nul 2>&1

:run
"%GRADLE_BIN%" %*
ENDLOCAL

@echo off
:: Official-ish Gradle wrapper launcher (requires gradle/wrapper/gradle-wrapper.jar in the repo)
setlocal
set DIRNAME=%~dp0
set WRAPPER_JAR=%DIRNAME%gradle\wrapper\gradle-wrapper.jar
if exist "%WRAPPER_JAR%" (
  java -jar "%WRAPPER_JAR%" %*
) else (
  echo Gradle wrapper JAR not found at "%WRAPPER_JAR%".
  echo If you have Gradle installed locally, generate the wrapper by running:
  echo    gradle wrapper
  echo This will create the missing files (including gradle-wrapper.jar). Then re-run .\gradlew.bat
  echo Alternatively, download the official gradle-wrapper.jar from a trusted source and place it into gradle\wrapper\
  exit /b 1
)
