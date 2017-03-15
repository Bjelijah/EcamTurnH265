package com.howell.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.android.howell.webcamH265.R.id;
import com.howell.activity.DeviceSetActivity;
import com.howell.activity.PlayerActivity;
import com.howell.entityclass.VMDGrid;
import com.howell.jni.JniUtil;
import com.howell.protocol.GetVideoBasicReq;
import com.howell.protocol.GetVideoBasicRes;
import com.howell.protocol.SetVideoBasicReq;
import com.howell.protocol.SetVideoBasicRes;
import com.howell.protocol.SoapManager;
import com.howell.protocol.SubscribeAndroidPushReq;
import com.howell.protocol.SubscribeAndroidPushRes;
import com.howell.protocol.VMDParamReq;
import com.howell.protocol.VMDParamRes;
import com.howell.utils.IConst;
import com.howell.utils.JsonUtil;
import com.howell.utils.SDCardUtils;
import com.howell.utils.SharedPreferencesUtil;
import com.howell.utils.TurnJsonUtil;
import com.howell.utils.Utils;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import bean.GetRecordedFilesBean;
import bean.Subscribe;

public class PlayerManager implements IConst{
	Handler handler;
	private PlayerManager(){}
	private static PlayerManager mInstance = null;
	public static PlayerManager getInstance(){
		if(mInstance==null){
			mInstance = new PlayerManager();
		}
		return mInstance;
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	private long dialogId = 0;
	private String turnServiceIP = null;
	private int turnServicePort = -1;
	private String sessionID = null;
	private Context context;
	private static final int F_TIME = 1;
	private Timer timer = null;
	private MyTimerTask myTimerTask = null;
	private int mUnexpectNoFrame = 0;
	
	private String deviceID;
	private int channelNo ;
	private VMDParamRes vmdRes;
	public void setDeviceID(String deviceID){
		this.deviceID = deviceID;
	}
	public String getDeviceID(){
		return this.deviceID;
	}
	public void setChannelNO(int channelNO){
		this.channelNo = channelNO;
	}
	public int getChannelNO(){
		return channelNo;
	}
	
	
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public void onConnect(String sessionId){
		Log.i("123", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!session id = "+sessionId);
		sessionID = sessionId;
		handler.sendEmptyMessage(MSG_LOGIN_CAM_OK);
	}
	
	public void onDisConnect(){
		Log.i("123", "onDisConnect ");
		handler.sendEmptyMessage(MSG_DISCONNECT);
	}
			
	public void onSubscribe(String jsonStr){
		Log.i("123","!!!!!!!!!!!!!!!!!!!!!!onASubscribe   jsonStr="+jsonStr);

		if(JniUtil.readyPlayTurnLive(TurnJsonUtil.getTurnSubscribeAckAllFromJsonStr(jsonStr))){
			JniUtil.playView();
			startTimerTask();
		}else{
			Log.e("123", "ready play live error");
		}
	}
	
	
	public long getDialogId(){
		this.dialogId++;
		return dialogId;
	}
	
	private boolean doOnce = false;
	

	private void startTimerTask(){
		timer = new Timer();
		myTimerTask = new MyTimerTask();
		timer.schedule(myTimerTask, 0,200);
	}

	private void stopTimerTask(){
		if (timer!=null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		if (myTimerTask!=null) {
			myTimerTask.cancel();
			myTimerTask = null;
		}
	}
	
	
	
	
	public void	loginCam(){
//		if(doOnce){
//			handler.sendEmptyMessage(MSG_LOGIN_CAM_OK);
//			return ;
//		}
//		turnServiceIP = PlatformAction.getInstance().getTurnServerIP();
//		turnServicePort = PlatformAction.getInstance().getTurnServerPort();
		
		//每次都读文件

		
		turnServiceIP  = SharedPreferencesUtil.getTurnServerIP(context);
		turnServicePort = SharedPreferencesUtil.getTurnServerPort(context);
		
		
		
		
		
		//文件中没有  使用默认 并 保存到 文件 保存到SharedPreferences
	

//		turnServiceIP = TEST_IP;
//		turnServicePort = TEST_TURN_SERCICE_PORT;
		
		Log.e("123", "!!!!!!!!!!!!!!!login cam    turnIp="+turnServiceIP+"    trunPort="+turnServicePort);


		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				Log.i("123", "doinback");
				
			
				
				JniUtil.netInit();
				
				JniUtil.transInit(turnServiceIP, turnServicePort,USING_TURN_ENCRYPTION);//FIXME 8812 test
				JniUtil.transSetCallBackObj(PlayerManager.this, 0);
				JniUtil.transSetCallbackMethodName("onConnect", 0);
				JniUtil.transSetCallbackMethodName("onDisConnect", 1);
				JniUtil.transSetCallbackMethodName("onRecordFileList", 2);
				JniUtil.transSetCallbackMethodName("onDisconnectUnexpect", 3);
				JniUtil.transSetCallbackMethodName("onSubscribe", 4);
				InputStream ca = getClass().getResourceAsStream("/assets/ca.crt");
				InputStream client = getClass().getResourceAsStream("/assets/client.crt");
				InputStream key = getClass().getResourceAsStream("/assets/client.key");
				
				String castr = new String(SDCardUtils.saveCreateCertificate(ca, "ca.crt"));
				String clstr = new String(SDCardUtils.saveCreateCertificate(client, "client.crt"));
				String keystr = new String(SDCardUtils.saveCreateCertificate(key, "client.key"));
//				JniUtil.transSetCrt(castr, clstr, keystr);
				JniUtil.transSetCrtPaht(castr, clstr, keystr);	
				
				try {
					ca.close();
					client.close();
					key.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int type = 101;
				String id = Utils.getPhoneUid(context);
				
				JniUtil.transConnect(type, id, PlatformAction.getInstance().getAccount(), PlatformAction.getInstance().getPassword());
				
				Log.e("123", "transConnect ok");
				
				AudioAction.getInstance().initAudio();
				AudioAction.getInstance().playAudio();
				
				return null;
			}
			
		}.execute();
		doOnce = true;	
		Log.i("123", "login task exe over");
	}
	
	
	public void logoutCam(){
		//JniUtil.loginOut();
		JniUtil.transDisconnect();
		
		AudioAction.getInstance().stopAudio();
		AudioAction.getInstance().deInitAudio();
		JniUtil.transDeinit();
	}
	
	
	public void onRecordFileList(String jsonStr){
//		try {
//			mList = JsonUtil.parseRecordFileList(new JSONObject(jsonStr));
//			handler.sendEmptyMessage(MSG_RECORD_LIST_GET);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
	
	public void onDisconnectUnexpect(){
		Log.i("123", "on disConnectUnexpect  we need reLink");
		stopTimerTask();
//		PlayerActivity.showStreamLen(0);
		PlayerActivity.ShowStreamSpeed(0);
		handler.sendEmptyMessageDelayed(PlayerActivity.MSG_DISCONNECT_UNEXPECT, 5000);
	}
	
	
	public void playViewCam(int is_sub){
		
		Subscribe s = new Subscribe(sessionID, (int)getDialogId(), deviceID, "live",is_sub);
		String jsonStr = JsonUtil.subScribeJson(s);
		Log.i("123", "kaishi play view cam jsonStr="+jsonStr);
		JniUtil.transSubscribe(jsonStr, jsonStr.length());
		
		
//		if(JniUtil.readyPlayLive()){
//			Log.i("123", "play view cam");
//			Subscribe s = new Subscribe(sessionID, (int)getDialogId(), PlatformAction.getInstance().getDeviceId(), "live",is_sub);
//			String jsonStr = JsonUtil.subScribeJson(s);
//			Log.i("123", "jsonStr="+jsonStr);
//			JniUtil.transSubscribe(jsonStr, jsonStr.length());
//			
//			JniUtil.playView();
//			startTimerTask();
//		}else{
//			Log.e("123", "ready play live error");
//		}	
	}
	
	public void stopViewCam(){
		
		
		 new Runnable() {
			public void run() {
				Log.d("123", "stop View cam");
				JniUtil.stopView();
				JniUtil.transUnsubscribe();
			}
		}.run();
		stopTimerTask();
	}
	
	public void transDeInit(){
		JniUtil.transDeinit();
	}
	
	public void transInit(String ip,int port){
		JniUtil.transInit(ip, port,USING_TURN_ENCRYPTION);
	}
	
	public void transConnect(int type,String id,String name,String pwd){
		JniUtil.transConnect(type, id, name, pwd);
	}
	
	public void stransSubscribe(String jsonStr,int jsonLen){
		JniUtil.transSubscribe(jsonStr, jsonLen);
	}
	
	public void getRecordFiles(String startTime,String endTime){
		String [] str = new String[2];
		str[0] = startTime;
		str[1] = endTime;
		new AsyncTask<String, Void, Void>(){
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				String deviceId = PlatformAction.getInstance().getDevice_id();
				GetRecordedFilesBean bean = new GetRecordedFilesBean(deviceId, 0, params[0], params[1]);
				String jsonStr = JsonUtil.getRecordFilesJson(bean);
				JniUtil.transGetRecordFiles(jsonStr, jsonStr.length());
				return null;
			}
		}.execute(str);
	}
	
	public void rePlay(final int is_sub){
		if (handler!=null) {
			handler.sendEmptyMessage(MSG_DATA_MISSING);
		}
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				stopViewCam();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				playViewCam(is_sub);
				
				return null;
			}
			protected void onPostExecute(Void result) {
				
			};
		}.execute();
		
		
		
		
		
		
	}
	
	
	
	public void reLink(){
		//stop
		Log.d("123", "relink........");
		handler.sendEmptyMessage(PlayerActivity.SHOWPROGRESSBAR);
		new Thread(){
			public void run() {
				stopViewCam();
				logoutCam();
				transDeInit();
				
				try {
					Thread.sleep(1000);//
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loginCam();
			};
		}.start();
	}
	
	
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			int streamLen = JniUtil.transGetStreamLenSomeTime();
			Log.i("123", streamLen+"");
			PlayerActivity.showStreamLen(streamLen/1024*8/F_TIME);
			if (streamLen == 0) {
				mUnexpectNoFrame++;
			}else{
				handler.sendEmptyMessage(PlayerActivity.HIDEPROGRESSBAR);
				mUnexpectNoFrame = 0;
			}
			
			if (mUnexpectNoFrame == 10) {// 10s / 1000ms 
				handler.sendEmptyMessage(PlayerActivity.MSG_DISCONNECT_UNEXPECT);
			}
		}
	}
	
	//播放功能
	public void getVideoBaseParam(){
		Log.i("123", "get video base param");
		if (IS_DEBUG) {
			return;
		}
		
		
		new AsyncTask<Void, Void, Boolean>(){
			GetVideoBasicRes res;
			
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				SoapManager mgr = SoapManager.getInstance();
				res = mgr.GetVideoBasicRes(new GetVideoBasicReq(PlatformAction.getInstance().getAccount()
						, PlatformAction.getInstance().getLoginSession(), deviceID, channelNo));
				vmdRes = mgr.getVMDParam(new VMDParamReq(PlatformAction.getInstance().getAccount()
						, PlatformAction.getInstance().getLoginSession(), deviceID, channelNo));
				return res.getResult().equalsIgnoreCase("OK");
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				if (handler==null) return;
				if (result) {
					Message msg = new Message();
					msg.what = MSG_GET_VIDEO_BASIC_OK;
					Bundle data = new Bundle();
					data.putSerializable("res", res);
					data.putBoolean("vmd", vmdRes.getEnabled());
					msg.setData(data);
					handler.sendMessage(msg);
				}else{
					handler.sendEmptyMessage(MSG_GET_VIDEO_BASIC_ERROR);
				}
				super.onPostExecute(result);
			}
			
		}.execute();
		
		
		
		
	
		
		
	}
	
	public void setVideoBaseParam(SetVideoBasicReq req){
		if (IS_DEBUG) {
			return;
		}
		req.setAccount(PlatformAction.getInstance().getAccount())
		.setLoginSession(PlatformAction.getInstance().getLoginSession())
		.setDevID(deviceID).setChannelNO(channelNo);
		
		new AsyncTask<SetVideoBasicReq, Void, Boolean>(){
			SetVideoBasicRes res;
			@Override
			protected Boolean doInBackground(SetVideoBasicReq... arg0) {
				// TODO Auto-generated method stub
				res = SoapManager.getInstance().SetVideoBasicRes(arg0[0]);
				return res.getResult().equalsIgnoreCase("OK");
			}
			
			protected void onPostExecute(Boolean result) {
				if (handler==null)return;
				if (result) {
					handler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_OK);
				}else{
					handler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_ERROR);
				}
				
			};
			
		}.execute(req);
		
		
		
	}
	
	public void setVMD(final boolean b){
		if (vmdRes==null) {
			if (handler!=null) {
				handler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_ERROR);
			}
			return;
		}
		
		new AsyncTask<Void, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				vmdRes.setEnabled(b);
				vmdRes.setSensitivity(40);
			
				VMDGrid grids = new VMDGrid(b?DeviceSetActivity.VMD_DEFAULT_GRIDS:DeviceSetActivity.VMD_ZERO_GRIDS);
				SubscribeAndroidPushReq req=new SubscribeAndroidPushReq(PlatformAction.getInstance().getAccount(),PlatformAction.getInstance().getLoginSession(),
						b?0x01:0x00,deviceID, channelNo);
				vmdRes.setGrids(grids);
				String res1 = SoapManager.getInstance().setVMDParam(vmdRes);
				SubscribeAndroidPushRes res2 =SoapManager.getInstance().getSubscribeAndroidPushRes(req);	
				return res1.equalsIgnoreCase("OK")&&res2.getResult().equalsIgnoreCase("OK");
			}
			protected void onPostExecute(Boolean result) {
				if (handler==null) 	return;
				if (result) {
					handler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_OK);
				}else{
					handler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_ERROR);
				}
			};
		}.execute();
		
		
		vmdRes.setEnabled(b);
		
		
		
		
		
	}
	
	
	
	
	
	
}
