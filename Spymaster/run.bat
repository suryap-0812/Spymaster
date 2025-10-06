@echo off
REM Run script for Spymaster with MongoDB integration

echo Running Spymaster...

REM Check if MongoDB driver JARs exist
if not exist lib\mongodb-driver-sync-4.11.1.jar (
    echo MongoDB driver JARs not found in lib directory.
    echo Please download the following files and place them in the lib directory:
    echo - mongodb-driver-sync-4.11.1.jar
    echo - bson-4.11.1.jar
    echo - mongodb-driver-core-4.11.1.jar
    exit /b 1
)

REM Run the game with MongoDB Java Driver on classpath
java -cp "classfiles;lib\mongodb-driver-sync-4.11.1.jar;lib\bson-4.11.1.jar;lib\mongodb-driver-core-4.11.1.jar" GameEngine

echo Game terminated.