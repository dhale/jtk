#!/bin/bash

export JVES_HOME=~/box/jves/trunk

export CLASSPATH=\
$JVES_HOME/build/jar/edu_mines_jves.jar:\
$JVES_HOME/jar/linux/x86/swt.jar:\
$JVES_HOME/jar/linux/x86/swt-pi.jar:\
$JVES_HOME/jar/junit.jar

export LD_LIBRARY_PATH=\
$JVES_HOME/lib/linux/x86:\
$JVES_HOME/build/jni

java -server -ea -Xmx1000m $*
