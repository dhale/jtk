@echo off
setlocal

rem Customize these locations as necessary.
rem Note that the JDK (Java Development Kit) is not the same as the JRE (Java 
rem Runtime Environment). The JDK has a javac compiler; the JRE does not.
set ANT_HOME=C:\pro\ant
set JDK_HOME=C:\pro\jdk

rem Include the JDK and Ant bin folders in our PATH.
set PATH=%JDK_HOME%\bin;%ANT_HOME%\bin;%PATH%

rem Run ant without adornments, quietly, and search for build.xml.
ant -e -q -s build.xml %*

endlocal
