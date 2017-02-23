package com.howell.activity;


import com.android.howell.webcamH265.R;
import com.howell.utils.IConst;
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

public class LoginSettingActivity extends Activity implements OnClickListener,IConst {
	
	ImageButton mBack;
	Button mSave;
	EditText mSevriceIP,mServicePort;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		int port = 0;
		if (ip.equals("")) {
			ip = getString(R.string.login_setting_default_IP);
		}else{
			result = Utils.checkIsIP(ip);
			Log.i("123","result = "+ result);

			result = true;//FIXME ip maybe like "www.haoweis.com"  so we foces to true

		}
		
		if (portStr.equals("")) {
			port = Integer.valueOf(getString(R.string.login_setting_default_port));
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
		//FIXME final 8862 still used maybe fix in next version
		SharedPreferencesUtil.saveTurnServerInfo(this, ip, USING_TURN_ENCRYPTION?TEST_TURN_SERCICE_PORT_SSL:TEST_TURN_SERVICE_PORT_NOSSL);
		return true;
	}
	

	
}
