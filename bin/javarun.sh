#!/bin/sh

export CSM_JTK_HOME=~/box/jtk/trunk

export CLASSPATH=\
$CSM_JTK_HOME/build/jar/edu_mines_jtk.jar:\
$CSM_JTK_HOME/jar/linux/x86/swt.jar:\
$CSM_JTK_HOME/jar/linux/x86/swt-pi.jar:\
$CSM_JTK_HOME/jar/junit.jar

export LD_LIBRARY_PATH=\
$CSM_JTK_HOME/lib/linux/x86:\
$CSM_JTK_HOME/build/jni

java -server -ea -Xmx1000m $*
