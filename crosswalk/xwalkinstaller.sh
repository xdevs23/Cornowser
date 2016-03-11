#/bin/bash

if [ "${PWD##*/}" != "crosswalk" ]; then cd crosswalk; fi

xwalkver=$(cat version.txt)

if [ -z "$1" ]; then
  echo -e "Version not specified.\nUsing given version.\n"
  echo "Example: $xwalkver"
else xwalkver="$1"
fi

if [ "$xwalkver" == 'auto' ]; then
  xwalkver=$(cat version.txt)
fi

if [ ! -e "crosswalk-$xwalkver.aar" ] || [ ! -e "crosswalk-$xwalkver-arm.aar" ]; then
  echo -e "\e[1mDownloading Crosswalk $xwalkver\e[0m\n"
  wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/$xwalkver/crosswalk-$xwalkver.aar
else echo -e "\e[1;92mCrosswalk $xwalkver is available locally. Download skipped.\e[0m\n"
fi

if [ ! -e "crosswalk-webview-$xwalkver-arm.zip" ]; then
  echo -e "\e[1mDownloading Crosswalk $xwalkver (additional zip)\e[0m\n"
  wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/$xwalkver/arm/crosswalk-webview-$xwalkver-arm.zip
else echo -e "\e[1;92mCrosswalk $xwalkver (additional zip) is available locally. Download skipped.\e[0m\n"
fi

if [ "$2" == "--nominify" ]; then
  mv crosswalk-$xwalkver.aar crosswalk-$xwalkver-arm.aar
else
  echo -e "\e[1mMinifying aar..."

  mkdir minify
  mkdir minify/aar
  mkdir minify/zip

  unzip "crosswalk-$xwalkver.aar" -d "minify/aar/"
  unzip "crosswalk-webview-$xwalkver-arm.zip" -d "minify/zip/"

  rm "minify/aar/classes.jar"
  cp "minify/zip/crosswalk-webview-$xwalkver-arm/libs/xwalk_core_library_java.jar" "minify/aar/classes.jar"
  rm -rf "minify/aar/jni/armeabi-v7a/"
  rm -rf "minify/aar/jni/x86/"
  cp -R "minify/zip/crosswalk-webview-$xwalkver-arm/libs/armeabi-v7a" "minify/aar/jni/armeabi-v7a"

  cd "minify/aar"
  zip -r crosswalk-$xwalkver-arm.aar ./

  cd "../../"

  cp "minify/aar/crosswalk-$xwalkver-arm.aar" "./crosswalk-$xwalkver-arm.aar"

  rm -rf minify/
  rm crosswalk-webview-$xwalkver-arm.zip
  rm crosswalk-$xwalkver.aar
fi

echo -e "\n\e[1mInstalling library into local maven repo...\e[0m\n"

mvn install:install-file -DgroupId=org.xwalk -DartifactId=xwalk_core_library_canary -Dversion=$xwalkver -Dpackaging=aar -Dfile=crosswalk-$xwalkver-arm.aar -DgeneratePom=true

echo -e "\n\e[7mInstallation finished! You can now use it in gradle.\e[0m\n"
echo -e "\n\nTo use it in gradle insert following code into your repositories section:"
echo -e "\n\nrepositories { \n    mavenLocal()\n    \e[92m// Your other repos\e[0m\n}"
echo -e "\n\nAnd following code in your dependencies:"
echo -e "\n\ndependencies { \n    compile \e[36m'org.xwalk:xwalk_core_library_canary:$xwalkver'\e[0m\n    \e[92m// Your other dependencies can go here\e[0m\n}"
echo -e "\n\n\n\e[1mThank you for using this script.\e[0m\n"
exit
