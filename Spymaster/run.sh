#!/bin/bash
# Run script for Spymaster with MongoDB integration

echo "Running Spymaster..."

# Run the game with MongoDB Java Driver on classpath
# Note: Replace 'path/to/mongodb-driver-sync.jar' with the actual path to your downloaded MongoDB driver
java -cp "classfiles:path/to/mongodb-driver-sync.jar" GameEngine

echo "Game terminated."