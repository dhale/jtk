@echo off
setlocal

set TOOLS=c:\tools
set VC_HOME=%TOOLS%\vc\vctk2003
set SDK_HOME=%TOOLS%\mssdk
set JAVA_HOME=%TOOLS%\Java\j2sdk1.4.2_06
set ANT_HOME=%TOOLS%\ant

set JAVA_INCLUDE=%JAVA_HOME%\include

set TPATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%VC_HOME%\bin
set PATH=%TPATH%;%PATH%
set LIB=%VC_HOME%\lib;%SDK_HOME%\lib

ant -e %*

endlocal
