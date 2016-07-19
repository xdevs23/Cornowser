#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building..."
./gradlew clean build connectedCheck assembleRelease clean build

echo "Build finished!"
