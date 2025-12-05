@echo off
setlocal
set PROJ=%~dp0
set CLASSES=%PROJ%target\classes
set OUTDIR=%PROJ%src\main\java\withaknoe\noel\lang

echo Project: %PROJ%
echo Output : %OUTDIR%

REM Ensure output dir exists
if not exist "%OUTDIR%" mkdir "%OUTDIR%"

REM Compile
javac -d "%CLASSES%" "%PROJ%src\main\java\withaknoe\noel\lang\tool\GenerateAst.java"
if errorlevel 1 (
  echo Compile failed.
  exit /b 1
)

REM Run
java -cp "%CLASSES%" withaknoe.ci_noel.tool.GenerateAst "%OUTDIR%"
if errorlevel 1 (
  echo Generator failed.
  exit /b 1
)

REM Verify
if exist "%OUTDIR%\Expr.java" (
  echo Generated: "%OUTDIR%\Expr.java"
) else (
  echo FAILED: Expr.java was not created.
)
endlocal
