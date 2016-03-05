#!/bin/bash

source configure.sh

./gradlew clean build connectedCheck assembleDebug clean

