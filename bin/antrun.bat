@echo off
setlocal

rem Customize these locations as necessary.
set ANT_HOME=C:\pro\ant
set JDK_HOME=C:\pro\jdk

rem Include the JDK and Ant bin folders in our PATH.
set PATH=%JDK_HOME%\bin;%ANT_HOME%\bin;%PATH%

rem Run ant without adornments.
ant -e %*

endlocal
