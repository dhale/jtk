@echo off
setlocal

set JVES_HOME=c:\dev\jves
set JAVA_HOME=c:\tools\Java\j2sdk1.4.2_06


rem Set the Windows platform.
rem At this point the only valid value is windows\x86

set PLATFORM=windows\x86


rem Set CLASSPATH to include required jars.

set CLASSPATH=
set CLASSPATH=%CLASSPATH%;%JVES_HOME%\build\jar\edu_mines_jves.jar
set CLASSPATH=%CLASSPATH%;%JVES_HOME%\jar\%PLATFORM%\swt.jar
set CLASSPATH=%CLASSPATH%;%JVES_HOME%\jar\junit.jar


rem Set PATH to include required libraries.

set JLIB=
set JLIB=%JLIB%;%JVES_HOME%\lib\%PLATFORM%
set JLIB=%JLIB%;%JVES_HOME%\build\jni
set PATH=%JAVA_HOME%\bin;%JLIB%


rem Invoke the jvm.

java -server -ea -Xmx1000m %*

endlocal
