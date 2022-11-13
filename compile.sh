#!/bin/bash
#
#   File:       compile.sh
#   Author:     Matteo Loporchio
#

SOURCEDIR="src"
TARGETDIR="bin"
CLASSPATH="${SOURCEDIR}:lib/gson-2.10.jar"

javac -cp $CLASSPATH $SOURCEDIR/*.java -d $TARGETDIR