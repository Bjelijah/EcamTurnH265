package com.howell.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint({ "NewApi", "Override" })
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback{

	private  FingerprintManager mFingerprintManager;
	private CancellationSignal mCancellationSignal;
	private Callback mCallback;
	private boolean mSelfCancelled;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.i("123", "get msg msg.what="+msg.what);
			super.handleMessage(msg);
		}
	};
	
	public FingerprintUiHelper(FingerprintManager fingerprintManager,Callback callback) {
		this.mFingerprintManager = fingerprintManager;
		this.mCallback = callback;
	}
	
	public static boolean isFingerAvailable(Context context){
		FingerprintManager fm = context.getSystemService(FingerprintManager.class);
		return fm.isHardwareDetected()&&fm.hasEnrolledFingerprints();
	}
	
	public boolean isFingerprintAuthAvailable(){
		return mFingerprintManager.isHardwareDetected()&&mFingerprintManager.hasEnrolledFingerprints();
	}
	
	public void startListening(FingerprintManager.CryptoObject cryptoObject){
		if (!isFingerprintAuthAvailable()) {
			Log.e("123", "finger print not available");
			return;
		}
		mCancellationSignal = new CancellationSignal();
		mSelfCancelled = false;
		mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, this, mHandler);
	}
	
	public void stopListening(){
		if (mCancellationSignal!=null ) {
			mSelfCancelled = true;
			try{
				mCancellationSignal.cancel();
			}catch(Exception e){
				e.printStackTrace();
			}
			mCancellationSignal = null;
		}
	}
	
	

	@Override
	public void onAuthenticationError(int errorCode, CharSequence errString) {
		Log.i("123", "onAuthenticationError:"+"errorCode="+errorCode+"   errString="+errString);
		
		switch (errorCode) {
		case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
			mSelfCancelled = true;
			break;
		case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
			try{
				mCallback.onError(errorCode,errString);
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onAuthenticationFailed() {
		Log.e("123", "onAuthenticationFailed");
		mCallback.onFailed();
	}
	
	@Override
	public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
		Log.e("123", "onAuthenticationError:"+"helpcode="+helpCode+"   helpString="+helpString);
		mCallback.onHelp(helpCode, helpString);
	}
	
	@Override
	public void onAuthenticationSucceeded(AuthenticationResult result) {
	
		Class<AuthenticationResult> c = AuthenticationResult.class;
		Method method = null;
		try {
			method = c.getMethod("getFingerprint");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		Log.i("123", "method="+method);
		method.setAccessible(true);
		Object o = null;
		String className = null;
		try {
			 o = method.invoke(result, null);
			 Log.e("123", "11111111111111111         o="+o+"       result="+result);
			 className = o.getClass().getName();
			 Log.i("123", "o="+o.toString()+" class="+o.getClass().getName());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		int fingerID = 0;
		
		try {
			Class fingerprint = Class.forName(className);
			Method method2 = fingerprint.getMethod("getFingerId");
			method2.setAccessible(true);
			Object name = method2.invoke(o, null);
			Log.i("123", "name="+name.toString());
			fingerID = Integer.valueOf(name.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("123", "onAuthenticationSucceeded   fingerID="+fingerID);
		
		mCallback.onAuthenticated(fingerID);
	}
	
	
	public interface Callback{
		void onAuthenticated(int id);
		void onFailed();
		void onHelp(int helpCode,CharSequence str);
		void onError(int code,CharSequence s);
	}
	
}
