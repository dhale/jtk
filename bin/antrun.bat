@echo off
setlocal

rem Customize these locations as necessary.
set VC_HOME=c:\pro\msvc
set SDK_HOME=c:\pro\mssdk
set ANT_HOME=c:\pro\ant
set JDK_HOME=c:\pro\jdk

rem These are needed by VC++ compiler and linker.
set LIB=%JDK_HOME%\lib;%VC_HOME%\lib;%SDK_HOME%\lib
set PATH=%JDK_HOME%\bin;%ANT_HOME%\bin;%VC_HOME%\bin;%PATH%

ant -e %*

endlocal
