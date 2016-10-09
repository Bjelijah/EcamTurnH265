package com.howell.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.howell.webcamH265.R;
import com.howell.action.FingerprintUiHelper;
import com.howell.action.PlatformAction;
import com.howell.db.UserLoginDao;
import com.howell.protocol.GetNATServerReq;
import com.howell.protocol.GetNATServerRes;
import com.howell.protocol.LoginRequest;
import com.howell.protocol.LoginResponse;
import com.howell.protocol.SoapManager;
import com.howell.utils.DecodeUtils;
import com.howell.utils.PhoneConfig;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import bean.UserLoginDBBean;

public class FingerPrintFragment extends DialogFragment implements FingerprintUiHelper.Callback,OnTouchListener{
	private static final int MSG_SIGN_IN_FAIL 	= 0xa0;
	private static final int MSG_SIGN_IN_OK 	= 0xa1;
	private static final int MSG_ERROR_WAIT_OK		= 0xa2;
	TextView mTvCancel,mTvPassword,mTvFingerState,mTvFingerWait;
	FingerprintUiHelper m;
	Context mContext;
	FingerprintUiHelper mFinger;
	ImageView mIvFingerState;
	Timer mWaitTimer = null;
	MyWaitTimerTask mWaitTimeTask = null;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_SIGN_IN_OK:
				dismiss();
				Activities.getInstance().getmActivityList().get("RegisterOrLogin").finish();
				break;
			case MSG_SIGN_IN_FAIL:
				showAuthenticationInfo(MyState.SIGN_ERROR);
				break;
			case MSG_ERROR_WAIT_OK:
				stopTimeTask();
				showAuthenticationInfo(MyState.WAIT);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getContext();
		getDialog().setTitle(getString(R.string.finger_title));
		View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
		mTvCancel = (TextView) v.findViewById(R.id.tv_finger_cancel);
		mTvCancel.setOnTouchListener(this);
		mTvPassword = (TextView) v.findViewById(R.id.tv_finger_password);
		mTvPassword.setOnTouchListener(this);
		mFinger = new FingerprintUiHelper(mContext.getSystemService(FingerprintManager.class), this);
		mFinger.startListening(null);
		mTvFingerState = (TextView) v.findViewById(R.id.fingerprint_status);
		mIvFingerState = (ImageView) v.findViewById(R.id.fingerprint_icon);
		
		mTvFingerWait = (TextView) v.findViewById(R.id.tv_finger_wait);
		
	
		
		return v;
	}

	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	
		super.onStart();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.e("123", "on destroy view");
		
		mFinger.stopListening();
		stopTimeTask();
		super.onDestroyView();
	}
	
	
	@Override
	public void onAuthenticated() {
		// TODO Auto-generated method stub
		Log.i("123", "识别到了");
		showAuthenticationInfo(MyState.OK);
		//开始登入
		signIn();
	}

	@Override
	public void onFailed() {
		// TODO Auto-generated method stub
		Log.i("123", "识别失败");
		showAuthenticationInfo(MyState.FAIL);
	}

	@Override
	public void onHelp(int helpCode, CharSequence str) {
		// TODO Auto-generated method stub
		Log.i("123", "helpCode="+helpCode+" str="+str);
		switch (helpCode) {
		case 1001:
			showAuthenticationInfo(MyState.WAIT);
			break;
		default:
			break;
		}	
	}

	@Override
	public void onError(int code,CharSequence s) {
		// TODO Auto-generated method stub
		Log.i("123", "识别error") ;
	
		if (code==7 || code == 5) {//7：连续验证失败后继续 验证     5：上次验证error后 还剩时间 秒
			int waitSec = Integer.valueOf((String) s);
			Log.i("123", "waitSec="+waitSec);
			if (mWaitTimeTask==null) {
				startTimeTask(waitSec);
			}
		}else{
			Log.e("123", "code="+code+"  s="+s);
		}
		
		
		showAuthenticationInfo(MyState.ERROR);
	}
	
	@SuppressWarnings("deprecation")
	private void showAuthenticationInfo(MyState state){
		switch (state) {
		case WAIT:
			mIvFingerState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fp));
			mTvFingerState.setText(mContext.getString(R.string.fingerprint_hint));
			mTvFingerState.setTextColor(mContext.getResources().getColor(R.color.hint_color));
			break;
		case FAIL:
//			mIvFingerState.setImageDrawable(getResources().getDrawable(R.drawable.common_no_default));
			mIvFingerState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fp_fail));
			mTvFingerState.setText(mContext.getString(R.string.fingerprint_failed));
			mTvFingerState.setTextColor(mContext.getResources().getColor(R.color.finger_fail));
			break;
		case OK:
//			mIvFingerState.setImageDrawable(getResources().getDrawable(R.drawable.ok_default_green));
			mIvFingerState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fp_ok));
			mTvFingerState.setText(mContext.getString(R.string.fingerprint_ok));
			mTvFingerState.setTextColor(mContext.getResources().getColor(R.color.finger_green));
			break;
		case ERROR:
			mIvFingerState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.common_no_highlighted));
			mTvFingerState.setText(mContext.getString(R.string.fingerprint_error));
			mTvFingerState.setTextColor(mContext.getResources().getColor(R.color.red));
			break;
		case SIGN_ERROR:
			mIvFingerState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.common_no_default));
			mTvFingerState.setText(mContext.getString(R.string.fingerprint_sign_error));
			mTvFingerState.setTextColor(mContext.getResources().getColor(R.color.finger_orgeen));
			break;
		default:
			break;
		}
	}
	
	
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.i("123", "on touch");
		switch (v.getId()) {
		case R.id.tv_finger_cancel:
			Log.i("123", "tv_finger_cancel"+" act="+event.getAction());
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				mTvCancel.postDelayed(new Runnable() {
					public void run() {
						dismiss();
					}
				}, 300);
			}
			break;

		case R.id.tv_finger_password:
			Log.i("123", "tv_finger_password"+"act="+event.getAction());
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				mTvPassword.postDelayed(new Runnable() {
					public void run() {
						dismiss();
						Intent intent = new Intent(mContext,MainActivity.class);
						startActivity(intent);
					}
				}, 300);	
			}
			
			
			break;
		default:
			break;
		}
		
		
		return false;
	}

	private enum MyState{
		WAIT,
		FAIL,
		OK,
		ERROR,
		SIGN_ERROR;
	}
	
	private void signIn(){
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
			
				
				int sn = (int)PhoneConfig.showUserSerialNum(mContext);
				UserLoginDao dao = new UserLoginDao(mContext, "user.db", 1);
				
				List<UserLoginDBBean> l = dao.queryByNum(sn);
				if (l.size()!=1) {
					Log.e("123", "l.size  !=1  size="+l.size());
					return false;
				}
				String userName = l.get(0).getUserName();
				String userPassword = l.get(0).getUserPassword();
				
				Log.i("123", "finger start login");
				//start login
				String encodedPassword = DecodeUtils.getEncodedPassword(userPassword);
				String imei = PhoneConfig.getPhoneDeveceID(mContext);
				LoginRequest loginReq = new LoginRequest(userName, "Common",encodedPassword, "1.0.0.1",imei);
				LoginResponse loginRes = null;
				try{
					loginRes=SoapManager.getInstance().getUserLoginRes(loginReq);
				}catch(Exception e){
					e.printStackTrace();
					Log.e("123", "finger start error  we return false");
					return false;
				}
			
				
				Log.i("123", "login get result");
				if (loginRes.getResult().toString().equals("OK")) {
//                    SharedPreferences sharedPreferences = getSharedPreferences(
//                            "set", Context.MODE_PRIVATE);
//                    Editor editor = sharedPreferences.edit();
//                    editor.putString("account", account);
//                    editor.putString("password", password);
//                    editor.commit();
                    PlatformAction.getInstance().setAccount(userName);
                    PlatformAction.getInstance().setPassword(userPassword);	                     
                	 PlatformAction.getInstance().setDeviceList(loginRes.getNodeList());
                    GetNATServerRes res = SoapManager.getInstance().getGetNATServerRes(new GetNATServerReq(userName, loginRes.getLoginSession()));
                    Log.e("MainActivity", res.toString());
                    PlatformAction.getInstance().setTurnServerIP(res.getTURNServerAddress());
                    PlatformAction.getInstance().setTurnServerPort(res.getTURNServerPort());
                    
                    Intent intent = new Intent(mContext,CamTabActivity.class);
                    startActivity(intent);
                   
                  
	            }else{
	            	return false;
	            }
				return true;	
			}
			protected void onPostExecute(Boolean result) {
				if (result) {
					mHandler.sendEmptyMessage(MSG_SIGN_IN_OK);
				}else{
					mHandler.sendEmptyMessage(MSG_SIGN_IN_FAIL);
				}
				
			};
		
		}.execute();
		
		
		
	}
	
	private void startTimeTask(int sec){
		mWaitTimer = new Timer();
		mWaitTimeTask = new MyWaitTimerTask(sec);
		mWaitTimer.schedule(mWaitTimeTask, 0,1000);
		//show
		mTvFingerWait.setVisibility(View.VISIBLE);
	}
	
	private void stopTimeTask(){
		if (mWaitTimer!=null) {
			mWaitTimer.cancel();
			mWaitTimer.purge();
			mWaitTimer = null;
		}
		if (mWaitTimeTask!=null) {
			mWaitTimeTask.cancel();
			mWaitTimeTask = null;
		}
		//gone
		mTvFingerWait.setVisibility(View.GONE);
	}
	
	
	class MyWaitTimerTask extends TimerTask{
		int waitSec;
		public MyWaitTimerTask(int s) {
			waitSec = s;
		}
		
		@Override
		public void run() {
		
			if (waitSec<0) {
				mHandler.sendEmptyMessage(MSG_ERROR_WAIT_OK);
			}else {
				//绘制
				mTvFingerWait.post( new Runnable() {
					public void run() {
						if (waitSec>=0) {
							mTvFingerWait.setText(waitSec+"");
						}
					}
				});
			}
			//
			waitSec--;
		}
	}
	
	
}
