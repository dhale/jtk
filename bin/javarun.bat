@echo off
setlocal

set CSM_JTK_HOME=c:\dev\jtk
set JAVA_HOME=c:\tools\Java\j2sdk1.4.2_06\jre


rem Set the Windows platform.
rem At this point the only valid value is windows\x86

set PLATFORM=windows\x86


rem Set CLASSPATH to include required jars.

set CLASSPATH=
set CLASSPATH=%CLASSPATH%;%CSM_JTK_HOME%\build\jar\edu_mines_jtk.jar
set CLASSPATH=%CLASSPATH%;%CSM_JTK_HOME%\jar\%PLATFORM%\swt.jar
set CLASSPATH=%CLASSPATH%;%CSM_JTK_HOME%\jar\junit.jar


rem Set PATH to include required libraries.

set JLIB=
set JLIB=%JLIB%;%CSM_JTK_HOME%\lib\%PLATFORM%
set JLIB=%JLIB%;%CSM_JTK_HOME%\build\jni
set PATH=%JAVA_HOME%\bin;%JLIB%


rem Invoke the jvm.

java -server -ea -Xmx1000m %*

endlocal
