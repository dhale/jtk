#The Mines Java Toolkit

The Mines Java Toolkit (Mines JTK) is a set of Java packages and native (non-Java) software libraries for science and engineering. Applications currently include digital signal processing, linear algebra, optimization, meshing, interpolation, and 2D and 3D graphics.

The Mines JTK is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).


###Getting the source code

To build and use the Mines JTK, you must first [download its source code from GitHub](https://github.com/dhale/jtk). If you clone this source code repository using git, then you will be able to easily update your copy as others make changes. Alternatively, you may use the Downloads link provided by GitHub to obtain a current snapshot of the code.

If you are using Linux or Mac OS X (10.7+), then you already have a git command-line client. Various git clients with graphical user interfaces are also available for Linux, Mac OS X, and Windows, and git is also available within popular integrated development environments. Note that git will be necessary if you wish to propose changes (submit pull requests) for the master branch of the source code repository. 

To determine if you have a git command-line client, in a terminal window type ```git```. If that command is found, then 

1. cd to the directory that will contain your directory jtk and
2. type the command:

```
git clone https://github.com/dhale/jtk.git
```

This command will create a directory jtk in your current working directory. The subdirectory .git contains a complete copy of the repository for the Mines Java Toolkit. If instead you simply use the GitHub Downloads link, then this subdirectory will be absent.

The directory jtk/ includes the following subdirectories:

| Directory          | Description                                                |
|-------------------:|------------------------------------------------------------|
| [core/](core/)     | everything needed to build and test the Mines Java Toolkit |
| [demo/](demo/)     | demonstration programs written in Java and Jython          |
| [docs/](docs/)     | extra (not API) documentation for some packages            |
| [gradle/](gradle/) | used by the Gradle wrapper to build the Mines JTK          |
| [misc/](misc/)     | miscellaneous tools for development and maintenance        |


###Installing the Java Development Kit (JDK)

Before building the Mines JTK, you must first [install Java SE JDK 7 (or later)](http://www.oracle.com/technetwork/java/javase/downloads) On Windows, we like to put tools such as the JDK in a folder named ```C:\pro\```. This folder name is shorter than "C:\Program Files" and contains no spaces, which makes it easy to specify in scripts and environment variables.

###Building the Mines JTK

The Mines JTK can be built most easily using the included Gradle wrapper. Gradle is a tool for automatic software builds. You can download and install Gradle on your system, but you need not do so if you only want to build the Mines JTK. First cd into the directory jtk/, which contains files build.gradle, gradlew (for Mac OS and Linux) and gradlew.bat (for Windows). Then type the command ```gradlew``` (or ```sh gradlew```) to build the Mines JTK. Look for the file ```core/build/libs/edu-mines-jtk-x.x.x.jar```, for some version number ```x.x.x```. You can use this JAR file like any other. However, depending on which packages you use, you may need other JAR files, called "dependencies." For example, the Mines JTK depends on JOGL for 3D graphics via OpenGL.

Gradle will automatically be downloaded the first time that you use the gradlew command. So you should first execute this command only when you have an internet connection.

The layout of directories and files for the Mines JTK was designed to conform to that expected by common build tools such as [Gradle](http://gradle.org/gradle-download/) (and Maven). You may also use an integrated development environment (IDE), such as [Eclipse](https://www.eclipse.org/downloads/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/) to build the Mines JTK. However, we strongly recommend that you first build the JTK from the command line, as described above.

###Testing the Mines JTK

The full name of the default Gradle task performed by the command ```gradlew``` is ```:core:jar```. We can perform other tasks, such as

```gradlew test```

which will build and run non-interactive unit tests, or

```gradlew deps```

to copy all external dependencies (JAR files used by some packages in the Mines JTK) to a folder named ```core/build/deps/```, or

```gradlew distZip```

to make a ZIP archive containing JAR files for the Mines JTK and all dependencies. To learn what tasks are available, we can use

```gradlew tasks```

###Running demonstration programs

The Mines Java Toolkit is a set of classes intended for use in other programs. The demo subproject provides examples, Java classes with a method ```main```, and Jython scripts. We can learn a lot about classes in the Mines JTK by running the demos and studying their source code. Try this:

```gradlew run -P demo=mosaic.PlotFrameDemo```

and this:

```gradlew run -P demo=mosaic/PlotFrameDemo.py```

The former command runs a Java class with a method main, and the latter runs a Jython script. In the property ```demo``` defined with the flag ```-P```, the ```/``` instead of ```.``` and the suffix ```.py``` distinguish between the two cases. Despite their similar names, these demos are entirely different programs with different results.

Currently, the best way to learn about what demos are available is to browse the directories ```demo/src/main/[java, jython]/jtkdemo/```.

###3D graphics in the Mines JTK

Our packages for 3D graphics are built on JOGL, a Java binding for the OpenGL API. As a first demo that 3D graphics is working, type the command

```gradlew run -P demo=ogl.HelloDemo```

You should see a white square, painted via OpenGL. This demo program also prints the OpenGL vendor and version number. That number should not be less than 1.2.

###Development using the Mines JTK

When developing your own software, you should not use package names that begin with "edu.mines.jtk", unless you are making modifications or additions to the Mines JTK that you wish to contribute back to us. (See the file [license.txt](license.txt].) The prefix edu.mines.jtk makes our class names unique.

Therefore, most classes that you write will have a different prefix, and your build process will create a JAR file with a different name. You might start by copying and modifying our demo directory layout and build.gradle.

In fact, this is how many of us work. We have our own private projects in which we implement new ideas with Java packages, which may or may not someday be included in the Mines JTK. (For convenience, the prefix for the package names in our private projects may be much shorter than "edu.mines.jtk".) Only those Java packages that are both well written and useful to others are eventually moved to edu.mines.jtk.
