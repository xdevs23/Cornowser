#!/bin/bash

if [ "$1" == "--clearPw" ]; then
  export ENV_CORNOWSER_KEYPWD=""
  export ENV_CORNOWSER_STOREPWD=""
fi

CORNOWSER_STOREPWD=""
CORNOWSER_KEYPWD=""

if [ $1 == "--gpw" ]; then
  export ENV_CORNOWSER_KEYPWD="$2"
  export ENV_CORNOWSER_STOREPWD="$3"
fi

if [ -z $ENV_CORNOWSER_KEYPWD ]; then
  echo -en "\nKeystore password: "
  read -s -p "" CORNOWSER_STOREPWD
  echo -en "\nKey      password: "
  read -s -p "" CORNOWSER_KEYPWD
  export ENV_CORNOWSER_KEYPWD="$CORNOWSER_KEYPWD"
  export ENV_CORNOWSER_STOREPWD="$CORNOWSER_STOREPWD"
fi

