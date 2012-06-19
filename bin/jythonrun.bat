@echo off
setlocal

rem Where is the Mines JTK? (Where is your build.xml?)
set MINES_JTK_HOME=c:\dhale\git\jtk

rem Where will Java look for classes? 
rem Add other jars to this list as necessary.
set CLASSPATH=^
%MINES_JTK_HOME%\build\jar\edu_mines_jtk.jar;^
%MINES_JTK_HOME%\jar\arpack-java.jar;^
%MINES_JTK_HOME%\jar\netlib-java.jar;^
%MINES_JTK_HOME%\jar\gluegen-rt.jar;^
%MINES_JTK_HOME%\jar\jogl.jar;^
%MINES_JTK_HOME%\jar\junit.jar;^
%MINES_JTK_HOME%\jar\jythonlib.jar;^
.

rem Where are the relevant native (non-Java) code libraries?
rem For 32-bit Windows, change x64 to x86.
set JAVA_LIBRARY_PATH=^
%MINES_JTK_HOME%\lib\windows\x64

rem Specifiy a directory to use for jython package caching
set CACHEDIR=%tmp%\cachedir

rem Run a server VM with assertions enabled and a 1GB max Java heap.
rem Modify these flags and properties as necessary for your system.
java -server -ea -Xmx1000m ^
-Djava.library.path=%JAVA_LIBRARY_PATH% ^
-Djava.util.logging.config.file=c:\dhale\etc\java_logging_config ^
-Dpython.cachedir.skip=false ^
-Dpython.cachedir=%CACHEDIR% ^
org.python.util.jython %*

endlocal
