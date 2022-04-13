LOCAL_PATH:= $(call my-dir)

SINGLE_TARGET := MCS.apk

include $(CLEAR_VARS)
LOCAL_MODULE       := $(SINGLE_TARGET)
LOCAL_MODULE_TAGS  := optional
LOCAL_MODULE_CLASS := APPS
ifeq ($(TARGET_ARCH),arm64)
LOCAL_PREBUILT_JNI_LIBS := $(shell aapt l ${LOCAL_PATH}/$(SINGLE_TARGET) | grep lib/arm64.*/.*so | sort | sed 's/^/@/' | xargs)
LOCAL_PREBUILT_JNI_LIBS_arm := $(shell aapt l ${LOCAL_PATH}/$(SINGLE_TARGET) | grep lib/armeabi.*/.*so | sort | sed 's/^/@/' | xargs)
else
LOCAL_PREBUILT_JNI_LIBS := $(shell aapt l ${LOCAL_PATH}/$(SINGLE_TARGET) | grep lib/armeabi.*/.*so | sort | sed 's/^/@/' | xargs)
endif
#system/app
LOCAL_SYS_OPPOAPP := true
LOCAL_MODULE_PATH  :=$(TARGET_OUT)/app
ifneq (1,$(filter 1,$(shell echo "$$(( $(PLATFORM_SDK_VERSION) <= 22 ))" )))
$(warning "---------------6.0 or above")
LOCAL_CERTIFICATE  := platform
else
$(warning "---------------5.1 or below")
LOCAL_CERTIFICATE  := PRESIGNED
endif
LOCAL_SRC_FILES    := $(SINGLE_TARGET)
include $(BUILD_PREBUILT)
