The Colorado School of Mines Java Toolkit

*****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This software and accompanying materials are made available under the terms 
of the Common Public License - v1.0, which accompanies this distribution, 
and is available at http://www.eclipse.org/legal/cpl-v10.html
*****************************************************************************


Binary distributions
--------------------

Binary distributions include the following directories:
bin/ - platform-dependent scripts for running Mines JTK programs
doc/ - documentation files
jar/ - Java archive (JAR) files (*.jar)
lib/ - platform-specific runtime libraries (*.so or *.dll)
src/ - source code files (*.java, *.cpp, etc.)

The Mines JTK requires Java Runtime Environment (JRE) 5.0. It will not 
work with JRE 1.4.2 or earlier.

If you obtained this file by downloading a platform-specific (binary)
distribution, then you have all of the source code for the Mines JTK.
We provide source files in a binary distribution for reference, only.
You should not attempt to build a binary distribution from those files. 


Building from source
--------------------

If you want to build a binary distribution yourself, perhaps because you
need to modify our source code, then you should SVN checkout our source 
code and other essential files from our Subversion (SVN) repository: 
http://boole.mines.edu/jtk. For that, you need an SVN client. These are
available for many operating systems from http://subversion.tigris.org/.

Note: if you modify (or port, or translate, or ...) our source code, then
you have created a "derived work", and should review carefully the terms
of the license that accompanies this software.

We designed the Mines JTK to be portable software. However, because of
dependencies on some external packages and libraries, we build today for
only Linux and Windows platforms.
  
To build the Mines JTK (after SVN checkout), you need the following:
* J2SE JDK 5.0 (or later): http://java.sun.com/j2se
* Apache Ant 1.6.2 (or later): http://ant.apache.org
* g++ (GCC) 3.2.3 (or later): http://www.mingw.org (for Windows only)

The Mines JTK exploits significant new language features and classes that 
became available only with Java 5.0. Therefore, this software cannot be 
used or built with Java 1.4.2 (or earlier).


C++ compilers
-------------

The Mines JTK depends on some external libraries, which can be accessed only 
via native (non-Java) code. For example, the package edu.mines.jtk.opengl 
wraps OpenGL 3-D graphics libraries via glue that we wrote in C++. For such 
packages, we require a C++ compiler.

For both Linux and Windows, we use g++, the GNU C++ compiler. For Linux,
we simply use the g++ compiler provided with our Linux distribution. 

For Windows, we use g++ as provided with the MinGW (Minimalist GNU for 
Windows) system, which is free software. As its name implies, MinGW is 
relatively small and easy to download and install. 

Libraries built with MinGW depend on the file mingwm10.dll provided here 
(lib\windows\x86) and with the MinGW distribution. At runtime, this file 
must be in your PATH.

To build our C++ code on any platform, you will need appropriate header 
files. In particular, to build the native code glue for edu.mines.jtk.opengl, 
you will need the header files gl.h, glext.h, and so on. These files are 
freely available, but with licenses that do not permit us to redistribute 
them. 

Alternatively, you may simply build only the Java source code, and then use 
the native code libraries from one of our binary distributions.

