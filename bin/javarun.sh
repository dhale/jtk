#!/bin/sh

# Where is the Mines Java Toolkit?
export MINES_JTK_HOME=~/box/jtk/trunk/dist

export CLASSPATH=\
$MINES_JTK_HOME/jar/edu_mines_jtk.jar:\
$MINES_JTK_HOME/jar/junit.jar

export LD_LIBRARY_PATH=\
$MINES_JTK_HOME/lib

java -server -ea -Xmx1000m $*
