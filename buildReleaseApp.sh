#!/bin/bash

. configure.sh

./gradlew clean build connectedCheck assembleRelease clean
