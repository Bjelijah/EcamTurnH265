package com.howell.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import bean.UserLoginDBBean;

import java.util.Map;

import com.android.howell.webcamH265.R;
import com.howell.action.PlatformAction;
import com.howell.broadcastreceiver.HomeKeyEventBroadCastReceiver;
import com.howell.db.UserLoginDao;
import com.howell.utils.DecodeUtils;
import com.howell.utils.MessageUtiles;
import com.howell.utils.PhoneConfig;
import com.howell.protocol.GetNATServerReq;
import com.howell.protocol.GetNATServerRes;
import com.howell.protocol.LoginRequest;
import com.howell.protocol.LoginResponse;
import com.howell.protocol.SoapManager;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText mUserName;
    private EditText mPassWord;
    private Button mButton;
    private SoapManager mSoapManager;
    private TextView mtvIEMI;
    public ProgressDialog mLoadingDialog;
    
    private static final int POSTPASSWORDERROR = 1;
    private static final int POSTNULLINFO = 2;
    private static final int POSTTOAST = 3;
    private static final int POSTLINKERROR = 4;
    private static final int POSTACCOUNTERROR = 5;
    
    private MessageHandler handler;
    
    private static MainActivity mActivity;
    
    private int intentFlag;
    
    private Activities mActivities;
    private HomeKeyEventBroadCastReceiver receiver;
    
//    private ResizeLayout layout;
    
    private ImageButton mBack,mSetting;
	private Dialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mActivities = Activities.getInstance();
        mActivities.addActivity("MainActivity",MainActivity.this);
        receiver = new HomeKeyEventBroadCastReceiver();
		registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
		
        mActivity = this;
        mSoapManager = SoapManager.getInstance();
    	SoapManager.context = this;
        mUserName = (EditText) findViewById(R.id.username);
        mPassWord = (EditText) findViewById(R.id.password);
        mButton = (Button) findViewById(R.id.ok);
        
        mBack = (ImageButton)findViewById(R.id.ib_login_back);
        mSetting = (ImageButton)findViewById(R.id.ib_login_setting_service);
        SharedPreferences sharedPreferences = getSharedPreferences("set",Context.MODE_PRIVATE);
        String account = sharedPreferences.getString("account", "");
        String password = sharedPreferences.getString("password", "");

        mUserName.setText(account);
        mPassWord.setText(password);
        
        handler = new MessageHandler();
        mButton.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        Intent intent = getIntent();
        intentFlag = intent.getIntExtra("intentFlag", 0);
        if(intentFlag == 1){
        	MessageUtiles.postAlertDialog(this, getResources().getString(R.string.login_fail), getResources().getString(R.string.message), R.drawable.expander_ic_minimized
        			, null, getResources().getString(R.string.ok), null, null);
//        	MessageUtiles.postNewUIDialog(this, getResources().getString(R.string.message), getResources().getString(R.string.ok), 1);
        }else if(intentFlag == 2){
        	MessageUtiles.postAlertDialog(this, getResources().getString(R.string.login_fail), getResources().getString(R.string.login_error), R.drawable.expander_ic_minimized
        			, null, getResources().getString(R.string.ok), null, null);
//        	MessageUtiles.postNewUIDialog(this, getResources().getString(R.string.login_error), getResources().getString(R.string.ok), 1);
        }
        
        /*layout = (ResizeLayout) findViewById(R.id.layout);   
		layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {   
		       
			public void OnResize(int w, int h, int oldw, int oldh) { 
				if(oldh == 0){
					return;
				}
				if(oldh > h){
					if(oldh - h < 100)return;
					layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundclear1));
				}else if(oldh < h){
					if(h - oldh < 100)return;
					layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundclear));
				}else{
					return;
				}
			}   
		});   */
        
        mtvIEMI = (TextView) findViewById(R.id.main_tv_imei);
        mtvIEMI.setText(getResources().getString(R.string.device_id)+" : "+PhoneConfig.getPhoneDeveceID(this));
        
        
    }
    

    private static MainActivity getContext(){
    	return mActivity;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.ib_login_back:
			finish();
			break;
		case R.id.ib_login_setting_service:
			Intent intent = new Intent(this,LoginSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.ok:
			final String account = mUserName.getText().toString().trim();
	        final String password = mPassWord.getText().toString().trim();
			if (TextUtils.isEmpty(account) && TextUtils.isEmpty(password)) {
				MessageUtiles.postAlertDialog(this, getResources().getString(R.string.login_fail), getResources().getString(R.string.verification), R.drawable.expander_ic_minimized
						, null, getResources().getString(R.string.ok), null, null);
//				MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.verification), MainActivity.getContext().getString(R.string.ok), 1);
	        	return;
	        }
			PlatformAction.getInstance().setIsTest(false);
			SoapManager.initUrl(this);
	        waitDialog = MessageUtiles.postWaitingDialog(MainActivity.this);
			waitDialog.show();
			new AsyncTask<Void, Integer, Void>() {
				LoginResponse loginRes;
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try{
						String encodedPassword = DecodeUtils.getEncodedPassword(password);
						String imei = PhoneConfig.getPhoneDeveceID(MainActivity.this);
				        LoginRequest loginReq = new LoginRequest(account, "Common",encodedPassword, "1.0.0.1",imei);
				        loginRes = mSoapManager.getUserLoginRes(loginReq);
				        Log.e("loginRes",loginRes.getResult().toString());
			         }catch (Exception e) {
						// TODO: handle exception
			        	handler.sendEmptyMessage(POSTLINKERROR);
			         }
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					waitDialog.dismiss();
					if(loginRes == null){
						Log.e("123", "loginRes==null");
						return;
					}
					if (loginRes.getResult().toString().equals("OK")) {
	                     SharedPreferences sharedPreferences = getSharedPreferences(
	                             "set", Context.MODE_PRIVATE);
	                     Editor editor = sharedPreferences.edit();
	                     editor.putString("account", account);
	                     editor.putString("password", password);
	                     editor.commit();
	                     PlatformAction.getInstance().setAccount(account);
	                     PlatformAction.getInstance().setPassword(password);	                     
	                 	 PlatformAction.getInstance().setDeviceList(loginRes.getNodeList());
	                     GetNATServerRes res = mSoapManager.getGetNATServerRes(new GetNATServerReq(account, loginRes.getLoginSession()));
	                     Log.e("MainActivity", res.toString());
	                     PlatformAction.getInstance().setTurnServerIP(res.getTURNServerAddress());
	                     PlatformAction.getInstance().setTurnServerPort(res.getTURNServerPort());
	                     saveUserInfo2Db();
	                     Intent intent = new Intent(MainActivity.this,CamTabActivity.class);
	                     startActivity(intent);
	                     finish();
	                     
	                     if (mActivities.getmActivityList().get("RegisterOrLogin")!=null) {
	                    	   mActivities.getmActivityList().get("RegisterOrLogin").finish();
						}else{
							Log.e("123", "RegisterOrLogin == null");
						}
	                     
	                  
		            }else if(loginRes.getResult().toString().equals("AccountNotExist")){
		            	MessageUtiles.postAlertDialog(MainActivity.this, getResources().getString(R.string.login_fail), getResources().getString(R.string.account_error), R.drawable.expander_ic_minimized
								, null, getResources().getString(R.string.ok), null, null);
//		            	 MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.account_error), MainActivity.getContext().getString(R.string.ok), 1);
		            }else if(loginRes.getResult().toString().equals("Authencation")){
		            	MessageUtiles.postAlertDialog(MainActivity.this, getResources().getString(R.string.login_fail), getResources().getString(R.string.password_error), R.drawable.expander_ic_minimized
								, null, getResources().getString(R.string.ok), null, null);
//		            	 MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.password_error), MainActivity.getContext().getString(R.string.ok), 1);
		            }else{
		            	Log.e("123", "loging error result="+loginRes.getResult().toString());
		            	MessageUtiles.postAlertDialog(MainActivity.this, getResources().getString(R.string.login_fail), getResources().getString(R.string.login_error), R.drawable.expander_ic_minimized
								, null, getResources().getString(R.string.ok), null, null);
//		            	 MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_error), MainActivity.getContext().getString(R.string.ok), 1);
		            }
				}
				
			}.execute();
			break;
		default:
			break;
		}
	        
    }
    
	private void saveUserInfo2Db(){
		long sn = PhoneConfig.showUserSerialNum(this);
		if (sn<0) {
			Toast.makeText(this, getResources().getString(R.string.save_db_error), Toast.LENGTH_SHORT).show();
			return ;
		}
		int userNum = (int)sn;
		String userName = PlatformAction.getInstance().getAccount();
		String userPassword = PlatformAction.getInstance().getPassword();
		UserLoginDBBean info = new UserLoginDBBean(userNum, userName, userPassword);
		
		UserLoginDao dao = new UserLoginDao(this, "user.db", 1);
		if (dao.findByNum(userNum)) {
			dao.updataByNum(info);
		}else{
			dao.insert(info);
		}
		/*test show*/
//		List<UserLoginDBBean> list = dao.queryAll();
//		for(UserLoginDBBean o: list){
//			Log.i("123", o.toString());
//		}
		dao.close();
//		Toast.makeText(this, getResources().getString(R.string.save_db_ok), Toast.LENGTH_SHORT).show();
	
	}
    
    
    public static class MessageHandler extends Handler{
    	
 		@Override
 		public void handleMessage(Message msg) {
 			super.handleMessage(msg);
 			if (msg.what == POSTPASSWORDERROR) {
 				MessageUtiles.postAlertDialog(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_fail), MainActivity.getContext().getString(R.string.password_error), R.drawable.expander_ic_minimized
						, null, MainActivity.getContext().getString(R.string.ok), null, null);
// 				MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.password_error), MainActivity.getContext().getString(R.string.ok), 1);
 			}
 			if (msg.what == POSTACCOUNTERROR) {
 				MessageUtiles.postAlertDialog(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_fail), MainActivity.getContext().getString(R.string.account_error), R.drawable.expander_ic_minimized
						, null, MainActivity.getContext().getString(R.string.ok), null, null);
// 				MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.account_error), MainActivity.getContext().getString(R.string.ok), 1);
 			}
 			if (msg.what == POSTLINKERROR) {
 				MessageUtiles.postAlertDialog(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_fail), MainActivity.getContext().getString(R.string.login_error), R.drawable.expander_ic_minimized
						, null, MainActivity.getContext().getString(R.string.ok), null, null);
// 				MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_error), MainActivity.getContext().getString(R.string.ok), 1);
 			}
 			if (msg.what == POSTNULLINFO) {
 				MessageUtiles.postAlertDialog(MainActivity.getContext(), MainActivity.getContext().getString(R.string.login_fail), MainActivity.getContext().getString(R.string.verification), R.drawable.expander_ic_minimized
						, null, MainActivity.getContext().getString(R.string.ok), null, null);
// 				MessageUtiles.postNewUIDialog2(MainActivity.getContext(), MainActivity.getContext().getString(R.string.verification), MainActivity.getContext().getString(R.string.ok), 1);
 			}
 			if(msg.what == POSTTOAST){
 				MessageUtiles.postToast(MainActivity.getContext(), MainActivity.getContext().getString(R.string.loading), 1000);
 			}
 		}
 	}

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	mActivities.removeActivity("MainActivity");
    	unregisterReceiver(receiver);
    }
}
