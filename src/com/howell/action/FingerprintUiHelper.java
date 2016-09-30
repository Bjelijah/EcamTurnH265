package com.howell.action;

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
			// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if (errorCode==7) {//连续验证失败导致的错误   在(int)errString 秒后可重新验证
			Log.e("123", "onAuthenticationError:"+"errorCode="+errorCode+"   errString="+errString);
		}
		
		try{
			mCallback.onError(errorCode,errString);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onAuthenticationFailed() {
		// TODO Auto-generated method stub
		Log.e("123", "onAuthenticationFailed");
		mCallback.onFailed();
	}
	
	@Override
	public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
		// TODO Auto-generated method stub
		Log.e("123", "onAuthenticationError:"+"helpcode="+helpCode+"   helpString="+helpString);
		mCallback.onHelp(helpCode, helpString);
	}
	
	@Override
	public void onAuthenticationSucceeded(AuthenticationResult result) {
		// TODO Auto-generated method stub
		Log.e("123", "onAuthenticationSucceeded");
		mCallback.onAuthenticated();
	}
	
	
	public interface Callback{
		void onAuthenticated();
		void onFailed();
		void onHelp(int helpCode,CharSequence str);
		void onError(int code,CharSequence s);
	}
	
}
