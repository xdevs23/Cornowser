#!/bin/bash

if [ -z $1 ]; then echo "Usage: $0 vername vercode vertype"
else
source make.sh

cp app/app-release.apk update/Cornowser.apk
cp "app/app-release.apk" "versions/Cornowser-$1-$2$3.apk"

echo -en $1>update/version.txt
echo -en $2>update/rel.txt

nano update/changelog.txt
fi
