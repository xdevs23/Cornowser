#!/bin/bash

APPRELPATHORIG="app/build/outputs/apk/app-release.apk"
APPRELPATH="app/build/outputs/apk/app-indirect-release.apk"
APPVER="$1-$2$3"
APPVER1="$1"
APPVER2="$2"
APPVER3="$3"
APPVER4="$4"

if [ -z $1 ]; then echo "Usage: source prepareupdate.sh vername vercode vertype"
else
  if [ ! "$4" == "--updateOnly" ]; then source make.sh; fi
  # If CB_USE_CUSTOM_PPUPD is set, use a custom script
  # defined in that variable to do the preparation of
  # the update. This is useful if you plan to use
  # another server for the update files, which is
  # recommended now.
  if [ ! -z "$CB_USE_CUSTOM_PPUPD" ]; then
    source $CB_USE_CUSTOM_PPUPD
  else
    cp "$APPRELPATH" "update/Cornowser.apk"
    cp "$APPRELPATH" "versions/Cornowser-$1-$2$3.apk"
  fi

  mv $APPRELPATH $APPRELPATHORIG

  echo -en $1>update/version.txt
  echo -en $2>update/rel.txt

  nano update/changelog.txt
  git add -A
  git commit -m "update: $APPVER"
  git push
fi
