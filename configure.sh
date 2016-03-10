#!/bin/bash

function welcome() {
  echo "This is the configuration script to start building Cornowser."
  echo "Please make sure to have a private key created in order to proceed."
  echo "If you don't have one,  create one using i. e. Android Studio"
  echo "Then come back to this script and enter your credentials"
  echo "Note: The keystore must be saved as privatekey.jks"
}

function goodbye() {
  echo "Congratz! Seems that the configuration process was successful."
}

welcome

if [ "$1" == "--clearPw" ]; then
  export ENV_CORNOWSER_KEYPWD=""
  export ENV_CORNOWSER_STOREPWD=""
fi

CORNOWSER_STOREPWD=""
CORNOWSER_KEYPWD=""

if [ "$1" == "--gpw" ]; then
  export ENV_CORNOWSER_KEYPWD="$2"
  export ENV_CORNOWSER_STOREPWD="$3"
fi

if [ -z $ENV_CORNOWSER_KEYPWD ]; then
  echo "First of all the keystore needs to be configured."
  echo -en "\nKeystore password: "
  read -s -p "" CORNOWSER_STOREPWD
  echo -en "\nKey      password: "
  read -s -p "" CORNOWSER_KEYPWD
  export ENV_CORNOWSER_KEYPWD="$CORNOWSER_KEYPWD"
  export ENV_CORNOWSER_STOREPWD="$CORNOWSER_STOREPWD"
  echo -en "\n"
fi

echo -en "\n"

if [ "$1" != "--noX" ]; then
  echo "Configuring XWalk..."
  cd crosswalk
  bash xwalkinstaller.sh auto --nominify
  cd ../
fi

goodbye
