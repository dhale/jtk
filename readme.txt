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
* A C++ compiler: g++ (for Linux) or Visual C++ (for Windows)

The Mines JTK exploits significant new language features and classes that 
became available only with Java 5.0. Therefore, this software cannot be 
used or built with Java 1.4.2 (or earlier).


C++ compilers
-------------

The Mines JTK depends on some external libraries, which can be accessed
only via native code. For example, the package edu.mines.jtk.opengl wraps
the OpenGL 3-D graphics libraries via glue that we wrote in C++. For such 
packages, we require a C++ compiler.

For Linux, we simply use g++, the GNU C++ compiler, which is free software. 
For Windows, we use Microsoft's Visual C++, which is freely available, but
requires some effort to install.

The Visual C++ Toolkit 2003 is freely available from Microsoft. However, 
to build the Mines JTK for Windows, you will also need Microsoft's Platform
SDK and their .NET Framework SDK, in addition to the VC++ Toolkit. The SDKs 
are also freely available, but they are huge downloads. This is unfortunate,
since you need only a few files from them. In particular, from the .NET 
Framework SDK, you need only the file MSVCRT.LIB.

Alternatively, you may simply buy Microsoft VC++ 2003. We have seen
academic prices as low as $60. If you do not qualify for that version,
then you might buy the standard version for about $90. That version does
not have an optimizing compiler, but you can simply replace the compiler 
in that version with the optimizing one in the free VC++ Toolkit 2003.

We chose VC++ for our Windows builds, because VC++ is a defacto standard
on Windows, and because everything we need is freely available, albeit
with some effort. We are considering switching to the free software MinGW 
(Minimalist GNU for Windows) for our Windows builds. As its name implies,
MinGW is relatively small and easy to download and install. Unlike software
built with VC++, software built with MinGW requires an extra, small, and 
freely-distributable library at runtime, in addition to the libraries that 
Microsoft provides with Windows. Aside from that small annoyance, we have 
had good experience with the g++ compiler and other tools in MinGW in the 
past. If you prefer MinGW to VC++, please let us know.
