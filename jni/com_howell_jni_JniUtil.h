/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_howell_jni_JniUtil */

#ifndef _Included_com_howell_jni_JniUtil
#define _Included_com_howell_jni_JniUtil
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVInit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVDeinit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVDeinit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVSetCallbackObject
 * Signature: (Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVSetCallbackObject
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVSetCallbackMethodName
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVSetCallbackMethodName
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVLock
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVLock
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVUnlock
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVUnlock
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVSetEnable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVSetEnable
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVRenderY
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVRenderY
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVRenderU
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVRenderU
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    YUVRenderV
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_YUVRenderV
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioInit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioDeinit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioDeinit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioSetCallbackObject
 * Signature: (Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioSetCallbackObject
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioSetCallbackMethodName
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioSetCallbackMethodName
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioBPlayable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioBPlayable
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    nativeAudioStop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_nativeAudioStop
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    netInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_netInit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    netDeinit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_netDeinit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    login
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_howell_jni_JniUtil_login
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    loginOut
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_howell_jni_JniUtil_loginOut
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    setCallBackObj
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_setCallBackObj
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    readyPlayLive
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_howell_jni_JniUtil_readyPlayLive
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    readyPlayPlayback
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_howell_jni_JniUtil_readyPlayPlayback
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    playView
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_playView
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    stopView
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_stopView
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    getHI265Version
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_getHI265Version
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transInit
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transInit
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transSetCallBackObj
 * Signature: (Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transSetCallBackObj
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transSetCallbackMethodName
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transSetCallbackMethodName
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transDeinit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transDeinit
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transConnect
 * Signature: (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transConnect
  (JNIEnv *, jclass, jint, jstring, jstring, jstring);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transDisconnect
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transDisconnect
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transSubscribe
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transSubscribe
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transUnsubscribe
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transUnsubscribe
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    catchPic
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_catchPic
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transGetStreamLenSomeTime
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_howell_jni_JniUtil_transGetStreamLenSomeTime
  (JNIEnv *, jclass);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transGetCam
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transGetCam
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transGetRecordFiles
 * Signature: (Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transGetRecordFiles
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transSetCrt
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transSetCrt
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    transSetCrtPaht
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_transSetCrtPaht
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     com_howell_jni_JniUtil
 * Method:    turnInputViewData
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_com_howell_jni_JniUtil_turnInputViewData
  (JNIEnv *, jclass, jbyteArray, jint);

#ifdef __cplusplus
}
#endif
#endif
