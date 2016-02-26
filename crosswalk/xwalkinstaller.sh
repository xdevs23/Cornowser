#/bin/bash

if [ -z $1 ]; then
  echo -e "Version not specified.\nPlease specify a version.\n"
  echo "Example: $0 18.46.460.0"
  exit
fi

if [ ! -e "crosswalk-$1.aar" ]; then
  echo -e "\e[1mDownloading Crosswalk $1\e[0m\n"
  wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/$1/crosswalk-$1.aar
else echo -e "\e[1;92mCrosswalk $1 is available locally. Download skipped.\e[0m\n"
fi

if [ ! -e "crosswalk-webview-$1-arm.zip" ]; then
  echo -e "\e[1mDownloading Crosswalk $1 (additional zip)\e[0m\n"
  wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/$1/arm/crosswalk-webview-$1-arm.zip
else echo -e "\e[1;92mCrosswalk $1 (additional zip) is available locally. Download skipped.\e[0m\n"
fi

if [ ! "$3" == "--nominify" ]; then
echo -e "\e[1mMinifying aar..."

mkdir minify
mkdir minify/aar
mkdir minify/zip

unzip "crosswalk-$1.aar" -d "minify/aar/"
unzip "crosswalk-webview-$1-arm.zip" -d "minify/zip/"

rm "minify/aar/classes.jar"
cp "minify/zip/crosswalk-webview-$1-arm/libs/xwalk_core_library_java.jar" "minify/aar/classes.jar"
rm -rf "minify/aar/jni/armeabi-v7a/"
rm -rf "minify/aar/jni/x86/"
cp -R "minify/zip/crosswalk-webview-$1-arm/libs/armeabi-v7a" "minify/aar/jni/armeabi-v7a"

cd "minify/aar"
zip -r crosswalk-$1-arm.aar ./

cd "../../"

cp "minify/aar/crosswalk-$1-arm.aar" "./crosswalk-$1-arm.aar"

rm -rf minify/
rm crosswalk-webview-$1-arm.zip
rm crosswalk-$1.aar
else mv crosswalk-$1.aar crosswalk-$1-arm.zip
fi

echo -e "\n\e[1mInstalling library into local maven repo...\e[0m\n"

mvn install:install-file -DgroupId=org.xwalk -DartifactId=xwalk_core_library_canary -Dversion=$1 -Dpackaging=aar -Dfile=crosswalk-$1-arm.aar -DgeneratePom=true

echo -e "\n\e[7mInstallation finished! You can now use it in gradle.\e[0m\n"
echo -e "\n\nTo use it in gradle insert following code into your repositories section:"
echo -e "\n\nrepositories { \n    mavenLocal()\n    \e[92m// Your other repos\e[0m\n}"
echo -e "\n\nAnd following code in your dependencies:"
echo -e "\n\ndependencies { \n    compile \e[36m'org.xwalk:xwalk_core_library_canary:$1'\e[0m\n    \e[92m// Your other dependencies can go here\e[0m\n}"
echo -e "\n\n\n\e[1mThank you for using this script.\e[0m\n"
exit
