#!/bin/sh

# Customize this location as necessary.
# Note that the JDK (Java Development Kit) is not the same as the JRE (Java 
# Runtime Environment). The JDK has a javac compiler; the JRE does not.
export JDK_HOME=/usr/java/jdk

# Run ant without adornments.
ant -e $*
