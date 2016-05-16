LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, app/src/main/java)

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, app/src/main/res) \
	$(TOP)/frameworks/support/v7/cardview/res \
	$(TOP)/frameworks/support/v7/appcompat/res

LOCAL_STATIC_JAVA_LIBRARIES := \
	android-support-v8-renderscript \
	android-support-v7-cardview \
	android-common \
	android-support-v4

LOCAL_AAPT_FLAGS := \
	--auto-add-overlay \
	--extra-packages android.support.v7.cardview

LOCAL_PACKAGE_NAME := Cornowser
LOCAL_OVERRIDES_PACKAGES := Browser

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

include $(BUILD_MULTI_PREBUILT)
