#!/bin/sh

# Where is the Mines Java Toolkit?
export MINES_JTK_HOME=~/box/jtk/trunk

# Where are the Mines JTK jars and JNI libraries?
export CLASSPATH=\
$MINES_JTK_HOME/build/jar/edu_mines_jtk.jar:\
$MINES_JTK_HOME/jar/junit.jar
export LD_LIBRARY_PATH=\
$MINES_JTK_HOME/lib/linux/x86

# Run java with the server VM, assertions enabled, and a 1GB Java heap.
java -server -ea -Xmx1000m $*
