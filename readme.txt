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

The directory jtk/ includes the following subdirectories:
bin/    - platform-dependent scripts (e.g., javarun.sh)
data/   - data used for demos and testing
docs/   - documentation for tools we use but do not build (e.g., JUnit)
gradle/ - used by the Gradle wrapper to build the Mines JTK
libs/   - code libraries (e.g., junit.jar) that we do not build
src/    - source code (e.g., main/java/edu/mines/jtk/util/Stopwatch.java)


Installing the Java Development Kit (JDK)
-----------------------------------------

Before building the Mines JTK, you must first install Java SE JDK 7,
which is available from
http://www.oracle.com/technetwork/java/javase/downloads

On Windows, we like to put tools such as the JDK in a folder named 
"C:\pro\". This folder name is shorter than "C:\Program Files" and 
contains no spaces, which makes it easy to specify in scripts and
environment variables.


Building the Mines JTK
----------------------

The Mines JTK can be built most easily using the included Gradle
wrapper. Gradle is a tool for automatic software builds. You can
download and install Gradle on your system, but you need not do so if
you only want to build the Mines JTK. First cd into the directory
jtk/, which contains files build.gradle, gradlew (for Mac OS and
Linux) and gradlew.bat (for Windows). Then type the command "gradlew"
(or "sh gradlew") to build the Mines JTK.

Gradle will automatically be downloaded the first time that you use
the gradlew command. So you should first execute this command only
when you have an internet connection. Also, when first building the
Mines JTK, JAR files for the Scala programming language (see below)
will be downloaded automatically.

The layout of directories and files for the Mines JTK was designed to
conform to that expected by common build tools such as Gradle (and
Maven). You may also use an integrated development environment (IDE),
such as Eclipse or IntelliJ IDEA to build the Mines JTK. However, we
strongly recommend that you first build the JTK from the command line,
as described above.

The file build.xml is now deprecated, but is currently provided so
that you can build the Mines JTK using Apache Ant. However, we
encourage you to use Gradle instead of Ant. To begin to see why we
prefer Gradle, compare build.gradle with build.xml.


Using the Mines JTK
-------------------

After you have built the Mines JTK, you should have the JAR file
jtk/build/libs/edu_mines_jtk.jar.
You may use this file as you would any other JAR file. To use the
Mines JTK, we must launch a Java virtual machine (JVM), and specify
any required JAR files, including edu_mines_jtk.jar.

In jtk/bin/ are scripts (e.g., javarun.sh) that illustrate how to do
this for different platforms. To use the Mines JTK from the command
line, you should (1) copy the appropriate script to some directory in
your PATH, (2) edit the script to specify the correct directories, and
(3) run a Mines JTK program by typing "javarun name_of_a_java_class".
For convenience, we recommend that you give this script a shorter
name, like "j" (or "j.bat").

You should first attempt to run some unit tests. For example, you
might run all tests for the package edu.mines.jtk.util by typing 
javarun edu.mines.jtk.util.AllTest 
If any of these tests fail, then you may need to edit your javarun
script. Many of our Java packages contain AllTest suites like this
one.

The Mines JTK also comes with demo programs in jtk/src/demo. These
demos are written in multiple languages, including Jython. Scripting
in Jython is an efficient way to utilize the Java classes in the Mines
JTK. With the Mines JTK we include a Jython JAR file (in jtk/libs/),
and scripts to run Jython programs (in jtk/bin/), so that these demos
can be run without any additional software.
 
To execute the Jython demos you should (1) copy the appropriate script
(jythonrun.sh or jythonrun.bat) to some directory in your PATH, (2)
edit the script to specify the correct directories, and (3) run a
Jython program by typing "jythonrun program_name.py". Again, you may
want to use a shorter name (such as "jy"), for this script.


3D graphics in the Mines JTK
-----------------------------

Our packages for 3D graphics are built on JOGL, a Java binding for the
OpenGL API. JAR files for JOGL on 64-bit operating systems are
provided with the Mines JTK. To test 3D graphics on Linux or Mac OS X,
type javarun.sh edu.mines.jtk.ogl.HelloTest You should see a white
square, painted via OpenGL. This program also prints the OpenGL vendor
and version number. That number should not be less than 1.2.

If instead you see a "java.lang.UnsatisfiedLinkError", then perhaps
the JOGL JAR files required for your operating system were not
provided with the Mines JTK. You can check the JOGL web site for
appropriate JAR files. Put them with the other platform-specific JAR
files in the directory jtk/libs/.


Development using the Mines JTK
-------------------------------

When developing your own software, you should not use package names
that begin with "edu.mines.jtk", unless you are making modifications
or additions to the Mines JTK that you wish to contribute back to us.
(See the file license.txt.) The prefix edu.mines.jtk makes our class
names unique.

Therefore, most classes that you write will have a different prefix,
and your build process will create a JAR file different from our
edu_mines_jtk.jar. To do this easily, you may copy and modify our
directory layout and our build.gradle to create your own JAR file. By
adding that JAR file to the CLASSPATH list in your javarun script, you
can easily use your own Java packages with those in the Mines JTK.

In fact, this is how many of us work. We have our own private projects
in which we implement new ideas with Java packages, which may or may
not someday be included in the Mines JTK. (For convenience, the prefix
for the package names in our private projects may be much shorter than
"edu.mines.jtk".) Only those Java packages that are both well written
and useful to others are eventually moved to edu.mines.jtk.


Development using Scala
-----------------------

Scala is a relatively new programming language that supports both
object-oriented and functional programming and the use of existing
Java packages. Currently, Scala is used only in demo programs, and
these demos will be built when you build the Mines JTK. However, the
use of Scala remains optional.

If you want to use Scala, then you must install it on your system so
that you have commands scala and scalac, which work much like java and
javac.
