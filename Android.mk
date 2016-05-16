# AOSP Android Makefile to build Cornowser
 
# 
# Copyright (C) 2016 Simao Gomes Viana (xdevs23)
#
# This file is licensed under the MIT License
# Visit the git repository on http://github.com/xdevs23/Cornowser
# to learn more.

#
# WARNING: This file hasn't been finished yet.
# It only contains some set of necessary stuff.
# If you can, please complete the file and make a pull request
# THANKS!!

LOCAL_PATH := $(call my-dir)
# Might or might not be necessary
include $(CLEAR_VARS)

# Might or might not be necessary
LOCAL_MODULE_TAGS := optional

# Please symlink $(LOCAL_PATH)/app/src/main/AndroidManifest.xml
# to $(LOCAL_PATH)/AndroidManifest.xml before building!
LOCAL_SRC_FILES := $(call all-java-files-under, app/src/main/)

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, app/src/main/res) \
    $(TOP)/frameworks/support/v7/cardview/res \
    $(TOP)/frameworks/support/v7/appcompat/res \

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-v8-renderscript \
    android-support-v7-cardview \
    android-common \
    android-support-v4

# Clone https://github.com/xdevs23/CommonsIO
LOCAL_PREBUILT_JAVA_LIBRARIES := \
    $(TOP)/external/apache-commons-io/commons-io-2.4.jar

# Do we need --auto-add-overlay?
LOCAL_AAPT_FLAGS := \
    --auto-add-overlay \
    --extra-packages android.support.v7.cardview

LOCAL_PACKAGE_NAME := Cornowser
LOCAL_OVERRIDES_PACKAGES := Browser

# I don't know if we need these:
include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

include $(BUILD_MULTI_PREBUILT)
