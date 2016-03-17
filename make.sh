#!/bin/bash

if [ "$1" == "--gpw" ]; then ./configure.sh --gpw $2 $3; fi

source buildReleaseApp.sh
