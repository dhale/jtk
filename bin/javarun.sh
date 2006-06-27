#!/bin/sh

# Where is the Mines JTK? (Where is your build.xml?)
export MINES_JTK_HOME=~/box/jtk/trunk

# Where are the Mines JTK jars and JNI libraries?
export CLASSPATH=\
$MINES_JTK_HOME/build/jar/edu_mines_jtk.jar:\
$MINES_JTK_HOME/jar/junit.jar
export LD_LIBRARY_PATH=\
$MINES_JTK_HOME/lib/linux/x86

# Run java with the server VM, assertions enabled, and a 1GB Java heap.
# If you use the java inside Sun's JDK, you have the Hotspot server VM;
# Otherwise, you may need to remove this flag.
java -server -ea -Xmx1000m $*
