@echo off
setlocal

rem Where is the Java Runtime Environment (JRE)?
set JRE_HOME=c:\pro\jdk\jre

rem Where is the Mines JTK?
set MINES_JTK_HOME=c:\dhale\box\jtk\trunk

rem Where are the Mines JTK jars and JNI libraries?
set CLASSPATH=^
%MINES_JTK_HOME%\build\jar\edu_mines_jtk.jar;^
%MINES_JTK_HOME%\jar\junit.jar
set PATH=^
%MINES_JTK_HOME%\lib\windows\x86;^
%JRE_HOME%\bin

rem Run java with the server VM, assertions enabled, and a 1GB Java heap.
java -server -ea -Xmx1000m %*

endlocal
