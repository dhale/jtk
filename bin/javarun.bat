@echo off
setlocal

set SCRIPT_DIR=%~dp0

rem Where will Java look for classes? 
rem Add other jars to this list as necessary.
set CLASSPATH=^
%SCRIPT_DIR%\*;^
%SCRIPT_DIR%\lib\*

rem Run a server VM with assertions enabled and a 1GB max Java heap.
rem Modify these flags and properties as necessary for your system.
java -server -ea -Xmx1g ^
-Djava.util.logging.config.file="%userprofile%\java_logging_config" ^
%*

endlocal
