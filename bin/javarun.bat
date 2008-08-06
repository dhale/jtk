@echo off
setlocal

rem Where is the Java Runtime Environment (JRE)?
rem We use the JRE *inside* the JDK, because it has the Hotspot server VM.
set JRE_HOME=c:\pro\jdk\jre

rem Where is the Mines JTK? (Where is your build.xml?)
set MINES_JTK_HOME=c:\dhale\box\jtk\trunk

rem Where are the Mines JTK jars and JNI libraries?
set CLASSPATH=^
%MINES_JTK_HOME%\build\jar\edu_mines_jtk.jar;^
%MINES_JTK_HOME%\jar\gluegen-rt.jar;^
%MINES_JTK_HOME%\jar\jogl.jar;^
%MINES_JTK_HOME%\jar\junit.jar
set PATH=^
%MINES_JTK_HOME%\lib\windows\x86;^
%JRE_HOME%\bin

rem Run java with the server VM, assertions enabled, and a 1GB Java heap.
rem If you use the java inside Sun's JDK, you have the Hotspot server VM;
rem Otherwise, you may need to remove this flag.
java -server -ea -Xmx1000m %*

endlocal
