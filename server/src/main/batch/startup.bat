@echo off
title "flyingsocks Server"

if "%1%" == "-t" (
    java -server -Dflyingsocks.config.location=../config -Xbootclasspath/a:../config:../ -cp ../lib/* com.lzf.flyingsocks.server.ServerBoot
) else (
    start /b java -Dflyingsocks.config.location=../config -Xbootclasspath/a:../config:../ -cp ../lib/* com.lzf.flyingsocks.server.ServerBoot
)
echo "Complete."
pause