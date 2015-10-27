#!/bin/sh

SCRIPT=$(readlink -f "$0")
SCRIPTPATH=$(dirname "$SCRIPT")

# If Mac OS X, which version of Java should we use?
export JAVA_VERSION=1.7.0

# Where will Java look for classes?
# Add other jars to this list as necessary.
export CLASSPATH=\
"$SCRIPTPATH/*":\
"$SCRIPTPATH/lib/*"

# Run a server 64-bit VM with assertions enabled and a 1GB max Java heap.
# Modify these flags and properties as necessary for your system.
java -server -d64 -ea -Xmx1g \
-Djava.util.logging.config.file=$HOME/.java_logging_config \
$*
