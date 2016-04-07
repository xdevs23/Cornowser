#/bin/sh

DBGAPKPATH="app/build/outputs/apk/app-appdebug.apk"
ADBARGS=""
CONTINUEXEC=true
COMPILEAPP=false

if [ "$1" == "--adbArgs" ]; then
  ADBARGS="$2 $3 $4 $5 $6"
  COMPILEAPP=true
fi

if [ -z "$1" ]; then source buildDebugApp.sh --noX
elif [ COMPILEAPP ]; then source buildDebugApp.sh --noX; fi

if [ "$1" == "-l" ]; then
  adb logcat -v tag -s Cornowser:*
  CONTINUEXEC=false
elif [ "$1" == "--cleardata" ]; then
  adb shell pm clear io.xdevs23.cornowser.browser
  CONTINUEXEC=false
elif [ "$1" == "-i" ]; then
  adb push $DBGAPKPATH /sdcard/CBCustom.apk
  adb shell pm set-install-location 1
  adb shell pm install -rdtf /sdcard/CBCustom.apk
  CONTINUEXEC=false
elif [ "$1" == "--start" ]; then
  adb shell am start -n io.xdevs23.cornowser.browser/.CornBrowser
  CONTINUEXEC=false
elif [ "$1" == "--uninstall" ]; then
  adb shell pm uninstall io.xdevs23.cornowser.browser
  CONTINUEXEC=false
elif [ "$1" == "--reinstall" ]; then
  adb shell pm uninstall io.xdevs23.cornowser.browser
  adb push $DBGAPKPATH /sdcard/CBCustom.apk
  adb shell pm set-install-location 1
  adb shell pm install -rdtf /sdcard/CBCustom.apk
  CONTINUEXEC=false
fi

if [ CONTINUEXEC ]; then
  adb $ADBARGS push $DBGAPKPATH /sdcard/CBCustom.apk
  adb $ADBARGS root>/dev/null
  adb $ADBARGS wait-for-device
  adb $ADBARGS shell pm set-install-location 1
  adb $ADBARGS shell pm install -rdtf /sdcard/CBCustom.apk
  adb $ADBARGS shell am start -n io.xdevs23.cornowser.browser/.CornBrowser
  if [ "$1" == "--grp" ]; then adb $ADBARGS logcat -v tag -s Cornowser | grep $2
  else adb $ADBARGS logcat -v tag -s Cornowser:* chromium:*
  fi
fi
