@echo off
REM Build script for Spymaster with MongoDB integration

echo Compiling Spymaster with MongoDB dependencies...

REM Create classfiles directory if it doesn't exist
if not exist classfiles mkdir classfiles

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib

REM Check if MongoDB driver JARs exist
if not exist lib\mongodb-driver-sync-4.11.1.jar (
    echo MongoDB driver JARs not found in lib directory.
    echo Please download the following files and place them in the lib directory:
    echo - mongodb-driver-sync-4.11.1.jar
    echo - bson-4.11.1.jar
    echo - mongodb-driver-core-4.11.1.jar
    exit /b 1
)

REM Compile Java files with MongoDB Java Driver on classpath
javac -cp ".;lib\mongodb-driver-sync-4.11.1.jar;lib\bson-4.11.1.jar;lib\mongodb-driver-core-4.11.1.jar" -d classfiles *.java

echo Compilation complete.