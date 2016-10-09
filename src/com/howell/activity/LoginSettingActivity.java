package com.howell.activity;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.howell.webcamH265.R;
import com.howell.utils.SharedPreferencesUtil;
import com.howell.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginSettingActivity extends Activity implements OnClickListener {
	
	ImageButton mBack;
	Button mSave;
	EditText mSevriceIP,mServicePort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login_setting_activity);
		
		mBack = (ImageButton) findViewById(R.id.ib_login_settting_back);
		mBack.setOnClickListener(this);
		mSave = (Button) findViewById(R.id.login_sttting_bt_save);
		mSave.setOnClickListener(this);
		mSevriceIP = (EditText) findViewById(R.id.login_settting_ip);
		mServicePort = (EditText) findViewById(R.id.login_settting_port);
		init();
	}

	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.ib_login_settting_back:
			finish();
			break;
		case R.id.login_sttting_bt_save:
			if (saveInfo()) {
				finish();
			}
			break;
			
			
		default:
			break;
		}
	}
	
	private void init(){
		String ip = SharedPreferencesUtil.getLoginServiceIP(this);
		if (!ip.equals("")) {
			mSevriceIP.setText(ip);
		}
		int port = SharedPreferencesUtil.getLoginServicePort(this);
		if (port!=-1) {
			mServicePort.setText(port+"");
		}
	}
	
	private boolean saveInfo(){
		boolean result = true;
		String ip = mSevriceIP.getText().toString();
		String portStr = mServicePort.getText().toString();
		int port = 8850;
		if (ip.equals("")) {
			ip = "180.166.7.214";
		}else{
			result = Utils.checkIsIP(ip);
			Log.i("123","result = "+ result);
		}
		
		if (portStr.equals("")) {
			port = 8850;
		}else{
			try{
				port = Integer.valueOf(mServicePort.getText().toString());
			}catch(Exception e){
				e.printStackTrace();
				result = false;
			}
		}
		Log.i("123", "ip="+ip+" port="+port);
		if (!result) {
			Toast.makeText(this, "设置失败", Toast.LENGTH_LONG).show();
			return false;
		}
		SharedPreferencesUtil.saveLoginInfo(this, ip, port);
		SharedPreferencesUtil.saveTurnServerInfo(this, ip, 8862);
		return true;
	}
	

	
}
