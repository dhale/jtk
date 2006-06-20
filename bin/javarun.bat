rem A Windows script to run java programs that use the Mines JTK. 
rem 
rem This script sets environment variables each time it is run, but does 
rem not set them externally. The variables you want depend on whether you 
rem are using a binary distribution of the Mines JTK or you build the 
rem Mines JTK yourself from source code. In any case, you should modify 
rem these settings to be consistent with your system.
rem -------------------------------------------------------------------------

@echo off
setlocal

rem Where is your Java Runtime Environment (JRE)?
rem ---------------------------------------------
set JRE_HOME=c:\pro\jdk\jre


rem Use something like these with a binary distribution of the Mines JTK.
rem ---------------------------------------------------------------------
set MINES_JTK_HOME=c:\dhale\box\jtk\trunk\dist
set CLASSPATH=^
%MINES_JTK_HOME%\jar\edu_mines_jtk.jar;^
%MINES_JTK_HOME%\jar\junit.jar
set PATH=^
%MINES_JTK_HOME%\lib\;^
%JRE_HOME%\bin

rem Use something like these if you build the Mines JTK from source code.
rem ---------------------------------------------------------------------
rem set MINES_JTK_HOME=c:\dhale\box\jtk\trunk
rem set CLASSPATH=^
rem %MINES_JTK_HOME%\build\jar\edu_mines_jtk.jar;^
rem %MINES_JTK_HOME%\jar\junit.jar
rem set PATH=^
rem %MINES_JTK_HOME%\build\lib\;^
rem %MINES_JTK_HOME%\lib\window\x86;^
rem %JRE_HOME%\bin

rem Run java with the server VM and assertions enabled and 1GB or memory.
rem ---------------------------------------------------------------------
java -server -ea -Xmx1000m %*

endlocal
