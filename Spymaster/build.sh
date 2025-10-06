#!/bin/bash
# Build script for Spymaster with MongoDB integration

echo "Compiling Spymaster with MongoDB dependencies..."

# Create classfiles directory if it doesn't exist
mkdir -p classfiles

# Compile Java files with MongoDB Java Driver on classpath
# Note: Replace 'path/to/mongodb-driver-sync.jar' with the actual path to your downloaded MongoDB driver
javac -cp ".:path/to/mongodb-driver-sync.jar" -d classfiles *.java

echo "Compilation complete."