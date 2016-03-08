#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Starting build..."
./gradlew clean build connectedCheck assembleAppDebug clean

echo "Build finished"
