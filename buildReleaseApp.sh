#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building..."
./gradlew clean assembleIndirectRelease clean build

echo "Build finished!"
