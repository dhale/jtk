@echo off
setlocal

@rem Customize these locations as necessary.
set TOOLS=c:\tools
set VC_HOME=%TOOLS%\vc\vctk2003
set SDK_HOME=%TOOLS%\mssdk
set ANT_HOME=%TOOLS%\ant
set JDK_HOME=%TOOLS%\Java\j2sdk1.4.2_06

set TPATH=%JDK_HOME%\bin;%ANT_HOME%\bin;%VC_HOME%\bin
set PATH=%TPATH%;%PATH%
set LIB=%VC_HOME%\lib;%SDK_HOME%\lib

ant -e %*

endlocal
