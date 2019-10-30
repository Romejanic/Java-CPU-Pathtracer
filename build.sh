#!/bin/sh
mkdir bin 2>/dev/null
set -x
javac -d bin -sourcepath src src/com/jack/pathtracer/Pathtracer.java 
