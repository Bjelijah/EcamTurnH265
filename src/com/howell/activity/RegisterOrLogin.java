package com.howell.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.howell.webcamH265.R;
import com.howell.action.FingerprintUiHelper;
import com.howell.action.PlatformAction;
import com.howell.broadcastreceiver.HomeKeyEventBroadCastReceiver;
import com.howell.utils.DecodeUtils;
import com.howell.utils.MessageUtiles;
import com.howell.utils.PhoneConfig;
import com.howell.utils.Util;
import com.zys.brokenview.BrokenCallback;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;
import com.howell.protocol.GetNATServerReq;
import com.howell.protocol.GetNATServerRes;
import com.howell.protocol.LoginRequest;
import com.howell.protocol.LoginResponse;
import com.howell.protocol.SoapManager;

public class RegisterOrLogin extends Activity implements OnClickListener{
	private TextView mRegister,mLogin,mTest;
	private SoapManager mSoapManager;
	private Activities mActivities;
	private HomeKeyEventBroadCastReceiver receiver;
	private Dialog waitDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_or_login);
		mActivities = Activities.getInstance();
		mActivities.addActivity("RegisterOrLogin",RegisterOrLogin.this);
		mSoapManager = SoapManager.getInstance();
		SoapManager.context = this;
		receiver = new HomeKeyEventBroadCastReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		mRegister = (TextView)findViewById(R.id.btn_register);
		mLogin = (TextView)findViewById(R.id.btn_login);
		mTest = (TextView)findViewById(R.id.btn_test);

		TextPaint tp = mRegister.getPaint();
		tp.setFakeBoldText(true);

		tp = mLogin.getPaint();
		tp.setFakeBoldText(true);

		tp = mTest.getPaint();
		tp.setFakeBoldText(true);

		mRegister.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mTest.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		if (!Util.isNewApi()) {
			return;
		}

		if(FingerprintUiHelper.isFingerAvailable(this)){
			FingerPrintFragment fingerFragment = new FingerPrintFragment();
	
			fingerFragment.show(getFragmentManager(), "fingerLogin");
			
			//set brokeView
			
		
			
			
		}
	}



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_register:
			Intent intent = new Intent(RegisterOrLogin.this,Register.class);
			startActivity(intent);
			break;

		case R.id.btn_login:
			intent = new Intent(RegisterOrLogin.this,MainActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_test:
			waitDialog = MessageUtiles.postWaitingDialog(RegisterOrLogin.this);
			waitDialog.show();
			new AsyncTask<Void, Integer, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					PlatformAction.getInstance().setIsTest(true);
					SoapManager.initUrl(RegisterOrLogin.this);
					String encodedPassword = DecodeUtils.getEncodedPassword("100868");
					String imei = PhoneConfig.getPhoneDeveceID(RegisterOrLogin.this);
					LoginRequest loginReq = new LoginRequest("100868", "Common",encodedPassword, "1.0.0.1",imei);
					LoginResponse loginRes = null;
					try {
						 loginRes = mSoapManager.getUserLoginRes(loginReq);
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(RegisterOrLogin.this, "登入失败！", Toast.LENGTH_SHORT).show();
						return null;
					}
				

					if (loginRes.getResult().toString().equals("OK")) {
						GetNATServerRes res = mSoapManager.getGetNATServerRes(new GetNATServerReq("100868", loginRes.getLoginSession()));
						Log.e("Register ", res.toString());
						Intent intent = new Intent(RegisterOrLogin.this,CameraList.class);
						startActivity(intent);
					}else{
						Toast.makeText(RegisterOrLogin.this, "登入失败！", Toast.LENGTH_SHORT).show();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					waitDialog.dismiss();
				}
			}.execute();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mActivities.removeActivity("RegisterOrLogin");
		mActivities.toString();
		unregisterReceiver(receiver);
	}
	
	private class MyBrokenCallback extends BrokenCallback{

		@Override
		public void onStart(View v) {
			Log.i("123", "MyBrokenCallback :"+v.getId());
			super.onStart(v);
		}

		@Override
		public void onCancel(View v) {
			Log.i("123", "onCancel :"+v.getId());
			super.onCancel(v);
		}

		@Override
		public void onRestart(View v) {
			Log.i("123", "onRestart :"+v.getId());
			super.onRestart(v);
		}

		@Override
		public void onFalling(View v) {
			Log.i("123", "onFalling :"+v.getId());
			super.onFalling(v);
		}

		@Override
		public void onFallingEnd(View v) {
			Log.i("123", "onFallingEnd :"+v.getId());
			super.onFallingEnd(v);
		}

		@Override
		public void onCancelEnd(View v) {
			Log.i("123", "onCancelEnd :"+v.getId());
			super.onCancelEnd(v);
		}
		
	}

	
	
}
