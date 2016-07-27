#!/bin/bash

echo "Configuring..."
source configure.sh $1

echo "Starting build..."
./gradlew assembleAppDebug

echo "Build finished"
