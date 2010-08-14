*****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This software and accompanying materials are made available under the terms 
of the Common Public License - v1.0, which accompanies this distribution, 
and is available at http://www.eclipse.org/legal/cpl-v10.html
*****************************************************************************

The Colorado School of Mines Java Toolkit
-----------------------------------------

The Mines Java Toolkit (JTK) is a set of Java packages and native (non-Java) 
code libraries for scientific and engineering computing. Applications
currently include digital signal processing and 2-D and 3-D graphics.

To take advantage of useful existing software not written in Java, we wrap 
such software using the Java Native Interface (JNI). Where needed, we load
JNI libraries dynamically via Java's System.loadLibrary method. An example
is our JNI library that wraps parts of the LAPACK library for dense linear 
algebra. 

To simplify development, we include pre-compiled binaries for our JNI 
libraries with the source code for the Mines JTK. Currently, we provide
JNI libraries for 32-bit (x86) systems running Linux or Windows and 64-bit
(x64) systems running Linux or Mac OS X. For these platforms, you need only 
a Java compiler to use everything in the Mines JTK.

Note: if you modify (or port, or translate, or ...) our source code, then
you have created a "derived work", and should review carefully the terms
of the license that accompanies this software.


Getting the source code
-----------------------

To build the Mines JTK from source, you must first use Subversion (SVN)
client software to checkout (co) the source code from our SVN repository:
http://boole.mines.edu/jtk.

If you are using Linux, an SVN client is likely available with your Linux 
distribution. If not yet installed, perhaps you simply need to install it. 
In a terminal window, type "svn". If that command is found, then (1) cd to 
the directory that will contain your directory jtk and (2) type the command
svn co http://boole.mines.edu/jtk
This command will create a directory jtk in your current working
directory.

For Windows, we recommand that you download and install TortoiseSVN from 
http://tortoisesvn.tigris.org. TortoiseSVN integrates nicely with the
Windows Explorer. After installing TortoiseSVN, (1) make a folder jtk 
(in the same folder as your jtk folder), (2) right-click on your jtk 
folder and select "SVN checkout ..." from the menu, (3) enter the URL
http://boole.mines.edu/jtk, and (4) enter the output path, including
the jtk directory.

After SVN checkout, you should have a directory jtk/trunk/ with
the following subdirectories:
bin/  - platform-dependent scripts (e.g., antrun.bat and javarun.bat)
data/ - data used for demos and testing
doc/  - documentation for tools we use but do not build (e.g., JUnit)
jar/  - JAR files (e.g., junit.jar)
lib/  - platform-specific JNI libraries (e.g., edu_mines_jtk_lapack.dll)
src/  - source code files (e.g., main/java/edu/mines/jtk/util/Stopwatch.java)


Tools for building
------------------

To build the Mines JTK, you need:
* J2SE JDK 5.0 (or later): http://java.sun.com/j2se
* Apache Ant 1.6.2 (or later): http://ant.apache.org

These tools are freely available on the web. 

The Mines JTK exploits significant new language features and classes that 
became available only with Java 5.0. Therefore, this software cannot be 
used or built with Java 1.4.2 (or earlier).

On Windows, we like to put tools such as the JDK and Ant in a folder named 
"C:\pro\". This folder name is shorter than "C:\Program Files" and contains 
no spaces, which makes it easy to specify in scripts.

These tools may be used with or without an IDE, such as NetBeans or Eclipse.
Furthermore, you need not use Ant, though that is what we use and support.


Building the Mines JTK
----------------------

In your jtk/trunk/bin/ directory are scripts for running Ant. Choose an
appropriate script for your platform, and copy it to some directory that is 
in your PATH. From any directory, you will want to type the command antrun 
and have this script be found. You may of course rename this script to 
anything you like. For example, on my systems, the script antrun.sh is
named simply "a".

In your copy of the antrun script, edit the environment variables so that 
the script will find your JDK and Ant. (For Linux and Mac OS X systems, 
these are in installed in standard locations, so no environment variables
are necessary.)

Then cd to your jtk/trunk/ directory (the one that contains build.xml) 
and type antrun (or whatever you called your ant script). This command 
should build the Mines Java Toolkit. 

Type antrun -p to learn more about other build targets, such as clean 
and doc.


Using the Mines JTK
-------------------

After you have built the Mines JTK, you should have the JAR file
jtk/trunk/build/jar/edu_mines_jtk.jar.
You may use this file as you would any other JAR file.
To run JUnit tests, you will also need jtk/trunk/jar/junit.jar.

Some packages (e.g., edu.mines.jtk.lapack) require JNI libraries of
native (non-Java) code. These platform-specific libraries should be
in a subdirectory of jtk/trunk/lib/, such as jtk/trunk/lib/linux/x86/.

To use the Mines JTK, we must launch a Java virtual machine, specifying 
all of these JAR files and the locations of our JNI libraries.

In jtk/trunk/bin/ are scripts (e.g., javarun.bat) that illustrate how
we do this for different platforms. To use the Mines JTK from the command
line, you should (1) copy the appropriate script to some directory in
your PATH, (2) edit the script to specify the correct directories, and
(3) run a Mines JTK program by typing javarun. (Again, you may want to
use a short name for the name of this script; I use simply "j".)

For example, you might run JUnit tests for edu.mines.jtk.util by typing
javarun edu.mines.jtk.util.AllTest
If this test fails, then you may need to edit your javarun script.
This test depends on only pure Java code; it does not depend on our JNI
libraries. Many of our Java packages contain test suites like this one.

To test something that requires one of our JNI libraries, type
javarun edu.mines.jtk.lapack.AllTest
Again, if this test fails, then you may need to edit your javarun script.
Or the JNI library for the package edu.mines.jtk.lapack is perhaps not
provided for your platform.

If your javarun script looks ok, then perhaps you have an old version of 
the standard C++ library. We compile and link our JNI libraries with a 
recent version of that library, but your system may have an older and
incompatible version. In such cases, if you cannot upgrade your standard
C++ library, then you must build the JNI libraries yourself.


3-D graphics in the Mines JTK
-----------------------------

Our packages for 3-D graphics are built on JOGL, a Java binding for the 
OpenGL API. Like the Mines JTK, JOGL provides both JAR files and JNI
libraries; and the JNI libraries are platform-specific. In the future, 
JOGL is likely to become part of standard Java Runtime Environment (JRE). 

Until then, the JAR files and JNI libraries for JOGL are provided with 
the JTK for most platforms. For other platforms, you may download these
libraries from the JOGL website.

To ensure that you have the JOGL JAR files and JNI libraries installed
correctly, type
javarun edu.mines.jtk.ogl.HelloTest
You should see a white square, painted via OpenGL.

If you get a "java.lang.UnsatisfiedLinkError", the JOGL JAR files or JNI 
libraries are perhaps not provided for your platform, or they may be in 
directories (e.g., jtk/trunk/lib/linux/x86/) that have not been specified 
correctly in your javarun script.

Note the OpenGL version and vendor printed by HelloTest. The version 
number should not be less than 1.2.
