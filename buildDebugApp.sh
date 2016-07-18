#!/bin/bash

echo "Configuring..."
source configure.sh $1

echo "Starting build..."
./gradlew clean assembleIndirectAppDebug build

echo "Build finished"
