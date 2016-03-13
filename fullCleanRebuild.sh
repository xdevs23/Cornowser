#!/bin/bash

echo "Configuring..."
source configure.sh

echo "Building"
./gradlew clean build connectedCheck clean build

echo "Done!"
