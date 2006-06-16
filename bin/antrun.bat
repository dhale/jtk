@echo off
setlocal

rem Customize these locations as necessary.
set GCC_HOME=C:\pro\mingw
set ANT_HOME=C:\pro\ant
set JDK_HOME=C:\pro\jdk

rem These are needed by VC++ compiler and linker.
set LIB=%JDK_HOME%\lib;%GCC_HOME%\lib
set PATH=%JDK_HOME%\bin;%ANT_HOME%\bin;%GCC_HOME%\bin;%PATH%

ant -e %*

endlocal
