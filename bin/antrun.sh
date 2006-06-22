#!/bin/sh

# Customize these locations as necessary.
export JDK_HOME=/usr/java/jdk

# Run ant without adornments.
ant -e $*
