#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building..."
./gradlew clean assembleGplayPlayrelease

echo "Build finished!"
