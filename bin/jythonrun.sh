#!/bin/sh

# Where is the Mines JTK? (Where is your build.xml?)
export MINES_JTK_HOME=$HOME/git/jtk

# If Mac OS X, which version of Java should we use?
export JAVA_VERSION=1.6.0

# Where will Java look for classes? 
# Add other jars to this list as necessary.
export CLASSPATH=\
$MINES_JTK_HOME/build/jar/edu_mines_jtk.jar:\
$MINES_JTK_HOME/jar/arpack-java.jar:\
$MINES_JTK_HOME/jar/netlib-java.jar:\
$MINES_JTK_HOME/jar/gluegen-rt.jar:\
$MINES_JTK_HOME/jar/jogl-all.jar:\
$MINES_JTK_HOME/jar/junit.jar:\
$MINES_JTK_HOME/jar/jythonlib.jar:\
.

# Run a server 64-bit VM with assertions enabled and a 1GB max Java heap.
# Modify these flags and properties as necessary for your system.
java -server -d64 -ea -Xmx1g \
-Djava.library.path=$JAVA_LIBRARY_PATH \
-Djava.util.logging.config.file=$HOME/.java_logging_config \
-Dapple.awt.graphicsUseQuartz=true \
-Dpython.cachedir.skip=false \
-Dpython.cachedir=/tmp/cachedir \
org.python.util.jython $*

