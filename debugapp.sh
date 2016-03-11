#/bin/sh

DBGAPKPATH="app/build/outputs/apk/app-appdebug.apk"

if [ -z "$1" ]; then source buildDebugApp.sh --noX; fi

if [ "$1" == "-l" ]; then
  adb logcat -v tag -s Cornowser:*
elif [ "$1" == "--cleardata" ]; then
  adb shell pm clear io.xdevs23.cornowser.browser
elif [ "$1" == "-i" ]; then
  adb push $DBGAPKPATH /sdcard/CBCustom.apk
  adb shell pm set-install-location 1
  adb shell pm install -rdtf /sdcard/CBCustom.apk
elif [ "$1" == "--start" ]; then
  adb shell am start -n io.xdevs23.cornowser.browser/.CornBrowser
elif [ "$1" == "--uninstall" ]; then
  adb shell pm uninstall io.xdevs23.cornowser.browser
elif [ "$1" == "--reinstall" ]; then
  adb shell pm uninstall io.xdevs23.cornowser.browser
  adb push $DBGAPKPATH /sdcard/CBCustom.apk
  adb shell pm set-install-location 1
  adb shell pm install -rdtf /sdcard/CBCustom.apk
else
  adb push $DBGAPKPATH /sdcard/CBCustom.apk
  adb root>/dev/null
  adb wait-for-device
  adb shell pm set-install-location 1
  adb shell pm install -rdtf /sdcard/CBCustom.apk
  adb shell am start -n io.xdevs23.cornowser.browser/.CornBrowser
  if [ "$1" == "--grp" ]; then adb logcat -v tag -s Cornowser | grep $2
  else adb logcat -v tag -s Cornowser:*
  fi
fi
