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

if [ ! -e "crosswalk-$1-64bit.aar" ]; then
  echo -e "\e[1mDownloading Crosswalk $1 64 bit\e[0m\n"
  wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/$1/crosswalk-$1-64bit.aar
else echo -e "\e[1;92mCrosswalk $1 64 bit is available locally. Download skipped.\e[0m\n"
fi

echo -e "\n\e[1mInstalling library into local maven repo...\e[0m\n"

mvn install:install-file -DgroupId=org.xwalk -DartifactId=xwalk_core_library_canary -Dversion=$1 -Dpackaging=aar -Dfile=crosswalk-$1.aar -DgeneratePom=true
mvn install:install-file -DgroupId=org.xwalk -DartifactId=xwalk_core_library_canary_sixtyfour -Dversion=$1 -Dpackaging=aar -Dfile=crosswalk-$1-64bit.aar -DgeneratePom=true

echo -e "\n\e[7mInstallation finished! You can now use it in gradle.\e[0m\n"
echo -e "\n\nTo use it in gradle insert following code into your repositories section:"
echo -e "\n\nrepositories { \n    mavenLocal()\n    // Your other repos\n}"
echo -e "\n\nAnd following code in your dependencies:"
echo -e "\n\ndependencies { \n    compile 'org.xwalk:xwalk_core_library_canary:$1'\n    compile 'org.xwalk:xwalk_core_library_canary_sixtyfour:$1'\n    // Your other dependencies can go here\n}"
echo -e "\n\n\n\e[1mThank you for using this script.\e[0m\n"
exit
