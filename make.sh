#!/bin/bash

if [ "$1" == "--gpw" ]; then ./configure.sh --gpw $2 $3
else
  source configure.sh
fi

source buildReleaseApp.sh
