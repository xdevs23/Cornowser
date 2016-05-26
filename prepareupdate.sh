#!/bin/bash

APPRELPATH="app/build/outputs/apk/app-release.apk"

if [ -z $1 ]; then echo "Usage: $0 vername vercode vertype"
else
  if [ ! "$4" == "--updateOnly" ]; then source make.sh; fi
  # If CB_USE_CUSTOM_PPUPD is set, use a custom script
  # defined in that variable to do the preparation of
  # the update. This is useful if you plan to use
  # another server for the update files, which is
  # recommended now.
  if [ -z "$CB_USE_CUSTOM_PPUPD" ]; then
    bash $CB_USE_CUSTOM_PPUPD
  else
    cp "$APPRELPATH" "update/Cornowser.apk"
    cp "$APPRELPATH" "versions/Cornowser-$1-$2$3.apk"
  fi

  echo -en $1>update/version.txt
  echo -en $2>update/rel.txt

  nano update/changelog.txt
fi
