@echo off
setlocal

rem Where is the Mines Java Toolkit?
set MINES_JTK_HOME=c:\dhale\box\jtk\trunk\dist

rem Where is the Java Runtime Environment (JRE)?
set JRE_HOME=c:\pro\jdk\jre

set CLASSPATH=^
%MINES_JTK_HOME%\jar\edu_mines_jtk.jar;^
%MINES_JTK_HOME%\jar\swt.jar;^
%MINES_JTK_HOME%\jar\junit.jar

set PATH=^
%MINES_JTK_HOME%\lib\;^
%JRE_HOME%\bin

java -server -ea -Xmx1000m %*

endlocal
