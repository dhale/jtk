*****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This software and accompanying materials are made available under the terms 
of the Common Public License - v1.0, which accompanies this distribution, 
and is available at http://www.eclipse.org/legal/cpl-v10.html
*****************************************************************************

The Colorado School of Mines Java Toolkit
-----------------------------------------

The Mines Java Toolkit (JTK) is a set of Java packages and native (non-Java) 
software libraries for science and engineering. Applications currently 
include digital signal processing, linear algebra, optimization, meshing,
interpolation, and 2D and 3D graphics.

Note: if you modify (or port, or translate, or ...) our source code, then
you have created a "derived work", and should review carefully the terms
of the license that accompanies this software.


Getting the source code
-----------------------

To build and use the Mines JTK, you must first download its source
code from GitHub at https://github.com/dhale/jtk. If you clone this
source code repository using git, then you will be able to easily
update your copy as others make changes. Alternatively, you may use
the Downloads link provided by GitHub to obtain a current snapshot
of the code; but this copy cannot be conveniently updated with git.

If you are using Linux or Mac OS X (10.7+), then you already have 
a git command-line client. Various git clients with graphical user
interfaces are also available for Linux, Mac OS X, and Windows, 
and git is also available within popular integrated development 
environments, such as Eclipse, Netbeans, and IntelliJ IDEA. Note 
that git will be necessary if you wish to propose changes (submit 
pull requests) for the master branch of the source code repository. 

To determine if you have a git command-line client, in a terminal 
window type "git". If that command is found, then cd to the 
directory that will contain your directory jtk and (2) type the 
command
git clone https://github.com/dhale/jtk.git
This command will create a directory jtk in your current working
directory. The subdirectory .git contains a complete copy of the
repository for the Mines Java Toolkit. If instead you simply use
the GitHub Downloads link, then this subdirectory will be absent.

The directory jtk/ has the following subdirectories:
bin/  - platform-dependent scripts (e.g., antrun.sh and javarun.sh)
data/ - data used for demos and testing
doc/  - documentation for tools we use but do not build (e.g., JUnit)
jar/  - JAR files (e.g., junit.jar)
lib/  - platform-specific JNI libraries (e.g., jogl.dll)
src/  - source code files (e.g., main/java/edu/mines/jtk/util/Stopwatch.java)


Tools for building
------------------

To build the Mines JTK, you need these freely available tools:
* Java SE JDK 6.0 (or later):
  http://www.oracle.com/technetwork/java/javase/downloads
* Apache Ant 1.7 (or later): 
  http://ant.apache.org

On Windows, we like to put tools such as the JDK and Ant in a folder named 
"C:\pro\". This folder name is shorter than "C:\Program Files" and contains 
no spaces, which makes it easy to specify in scripts.

These tools may be used with or without an integrated development
environment (IDE), such as NetBeans, Eclipse, or IntelliJ IDEA.


Building the Mines JTK
----------------------

In your jtk/bin/ directory are scripts for running Ant. Choose an
appropriate script for your platform, and copy it to some directory that is 
in your PATH. From any directory, you will want to type the command antrun 
and have this script be found. You may of course rename this script to 
anything you like. For example, you might rename the script antrun.sh
to be simply "a" (or "a.bat"), which is easier to type.

In your copy of the antrun script, edit the environment variables so that 
the script will find your JDK and Ant. (For Linux and Mac OS X systems, 
these are in installed in standard locations, so no environment variables
are necessary.)

Then cd to your jtk/ directory (the one that contains build.xml) and type 
"antrun" (or whatever you called your ant script). This command should 
build the Mines Java Toolkit.

Type "antrun -p" to learn more about other build targets, such as clean 
and doc.


Using the Mines JTK
-------------------

After you have built the Mines JTK, you should have the JAR file
jtk/build/jar/edu_mines_jtk.jar.
You may use this file as you would any other JAR file.

To use the Mines JTK, we must launch a Java virtual machine, specifying 
all of these JAR files and the locations of our JNI libraries.

In jtk/bin/ are scripts (e.g., javarun.sh) that illustrate how
we do this for different platforms. To use the Mines JTK from the command
line, you should (1) copy the appropriate script to some directory in
your PATH, (2) edit the script to specify the correct directories, and
(3) run a Mines JTK program by typing "javarun name_of_a_java_class". 
(Again, you may want to use a shorter name, such as "j" for this script.)

For example, you might run JUnit tests for edu.mines.jtk.util by typing
javarun edu.mines.jtk.util.AllTest
If any of these tests fail, then you may need to edit your javarun script.
These tests depend on only pure Java code; they do not depend on our JNI
libraries. Many of our Java packages contain AllTest suites like this one.

The Mines JTK also comes with demos in jtk/src/demo. There are demos in
multiple languages, but the majority are in Jython. Scripting in Jython
is a powerful and robust way to utilize the Java classes in the Mines JTK,
and is the way that most of us work. We include Jython in a jar file as
well as scripts in the jtk/bin/ directory, so that these demos can be run
without any additional installations.
 
To execute the Jython demos you should (1) copy the appropriate script
(jythonrun.sh or jythonrun.bat) to some directory in your PATH, (2) edit
the script to specify the correct directories, and (3) run a Jython
program by typing "jythonrun program_name.py". (You may want to use a
shorter name, such as "jy" for this script.)


3D graphics in the Mines JTK
-----------------------------

Our packages for 3D graphics are built on JOGL, a Java binding for the 
OpenGL API. JAR files for JOGL on 64-bit operating systems are provided 
with the Mines JTK. To test 3D graphics on Linux or Mac OS X, type
javarun.sh edu.mines.jtk.ogl.HelloTest
You should see a white square, painted via OpenGL. This program also
prints the OpenGL vendor and version number. That number should not 
be less than 1.2.

If instead you see a "java.lang.UnsatisfiedLinkError", then perhaps
the JOGL JAR files required for your operating system are not provided 
with the Mines JTK. You can check the JOGL web site for appropriate 
JAR files. Put them with the other platform-specific JAR files in
the directory jtk/jar/.


Development using the Mines JTK
-------------------------------

The layout of directories and files for the Mines JTK was designed to
conform to that expected by common development tools and IDEs. However,
before using these more advanced tools, we recommend that you first 
build and use the JTK with only Ant and the JDK, as described above.
(By not providing pre-built JAR files, we gently force you to do so.) 

When developing your own software, you should not use package names
that begin with "edu.mines.jtk", unless you are making modifications
or additions to the Mines JTK that you wish to contribute back to us. 
The prefix edu.mines.jtk makes our class names unique.

Therefore, most classes that you write will have a different prefix,
and your build process will create a JAR file different from our
edu_mines_jtk.jar. To do this easily, you may copy and modify our 
directory layout and the file build.xml to create your own JAR file. 
By adding that JAR file to the CLASSPATH list in your javarun script, 
you can easily use your Java packages with those in the Mines JTK.

In fact, this is what most of us do. We have our own private projects
in which we implement new ideas with Java packages, which may or may 
not be someday included in the Mines JTK. (For convenience, the prefix
for the package names in our private projects may be much shorter than 
"edu.mines.jtk".) Only those Java packages that are both well written 
and useful to others are eventually moved to edu.mines.jtk.


Development using Scala
-----------------------

Scala is a relatively new programming language that supports both
object-oriented and functional programming and the use of existing
Java packages. The Mines JTK does not currently require Scala. 
However, if you define the environment variable SCALA_HOME, the
Ant build script build.xml will compile any Scala source files
and include Scala classes in the JAR file edu_mines_jtk.jar.
