# -------------------
# Android_4.2.1.mk
# [from 'jni/' directory]
# ndk-build clean
# ndk-build
# -------------------
LOCAL_PATH := $(call my-dir)
JSQLITE_PATH := javasqlite-20120209
SPATIALITE_PATH := libspatialite-4.2.1
GEOS_PATH := geos-3.4.2
PROJ4_PATH := proj-4.8.0
SQLITE_PATH := sqlite-amalgamation-3080801
ICONV_PATH := libiconv-1.13.1
XML2_PATH := libxml2-2.9.1
LZMA_PATH := xz-5.1.3alpha

include $(LOCAL_PATH)/iconv-1.13.1.mk
include $(LOCAL_PATH)/sqlite-3080801.mk
include $(LOCAL_PATH)/proj4-4.8.0.mk
include $(LOCAL_PATH)/geos-3.4.2.mk
include $(LOCAL_PATH)/libxml2-2.9.1.mk
include $(LOCAL_PATH)/lzma-xz-5.1.3a.mk
include $(LOCAL_PATH)/spatialite-4.2.1.mk
include $(LOCAL_PATH)/jsqlite-20120209.mk
$(call import-module,android/cpufeatures)
