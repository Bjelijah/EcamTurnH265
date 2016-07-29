package com.howell.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.howell.activity.PlayerActivity;
import com.howell.entityclass.VODRecord;
import com.howell.jni.JniUtil;
import com.howell.utils.IConst;
import com.howell.utils.JsonUtil;
import com.howell.utils.SDCardUtils;
import com.howell.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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
	
	private Timer timer = null;
	private MyTimerTask myTimerTask = null;
	
	ArrayList<VODRecord> mList = null;
	
	public ArrayList<VODRecord> getMList(){
		return mList;
	}
	

	
	public void setContext(Context context){
		this.context = context;
	}
	
	public void onConnect(String sessionId){
		Log.i("123", "session id = "+sessionId);
		sessionID = sessionId;
		handler.sendEmptyMessage(MSG_LOGIN_CAM_OK);
	}
	
	public void onDisConnect(){
		Log.i("123", "onDisConnect ");
		handler.sendEmptyMessage(MSG_DISCONNECT);
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
		turnServiceIP = PlatformAction.getInstance().getTurnServerIP();
		turnServicePort = PlatformAction.getInstance().getTurnServerPort();
		
		turnServiceIP = TEST_IP;
		turnServicePort = TEST_TURN_SERCICE_PORT;
		
		Log.i("123", "login cam");

		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				Log.i("123", "doinback");
				
			
				
				JniUtil.netInit();
				
				JniUtil.transInit(turnServiceIP, TEST_TURN_SERCICE_PORT);//FIXME 8812 test
				JniUtil.transSetCallBackObj(PlayerManager.this, 0);
				JniUtil.transSetCallbackMethodName("onConnect", 0);
				JniUtil.transSetCallbackMethodName("onDisConnect", 1);
				JniUtil.transSetCallbackMethodName("onRecordFileList", 2);
				
				
				
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
				
				Log.i("123", "transConnect ok");
				
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
	}
	
	
	
	
	
	
	
	public void playViewCam(int is_sub){
		
		
		if(JniUtil.readyPlayLive()){
			Log.i("123", "play view cam");
			Subscribe s = new Subscribe(sessionID, (int)getDialogId(), PlatformAction.getInstance().getDeviceId(), "live",is_sub);
			s.setStartTime(null);
			s.setEndTime(null);
			String jsonStr = JsonUtil.subScribeJson(s);
			Log.i("123", "jsonStr="+jsonStr);
			JniUtil.transSubscribe(jsonStr, jsonStr.length());
			
			JniUtil.playView();
			startTimerTask();
		}else{
			Log.e("123", "ready play live error");
		}	
	}
	
	public void playBackCam(int is_sub,String startTime,String endTime){
		if(JniUtil.readyPlayPlayback()){
			Log.i("123", "play back cam");
			Subscribe s = new Subscribe(sessionID, (int)getDialogId(), PlatformAction.getInstance().getDeviceId(), "playback",is_sub);
			s.setStartTime(startTime);
			s.setEndTime(endTime);
			String jsonStr = JsonUtil.subScribeJson(s);
			Log.i("123", "jsonStr = "+jsonStr);
			JniUtil.playView();
			startTimerTask();
		}else{
			Log.e("123", "read play playback error");
		}
		
		
	}
	
	public void stopPlaybackCam(){
		stopViewCam();
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
		JniUtil.transInit(ip, port);
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
	
	public void onRecordFileList(String jsonStr){
		try {
			mList = JsonUtil.parseRecordFileList(new JSONObject(jsonStr));
			handler.sendEmptyMessage(MSG_RECORD_LIST_GET);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			int streamLen = JniUtil.transGetStreamLenSomeTime();
			Log.i("123", streamLen+"");
			PlayerActivity.showStreamLen(streamLen/1024*8);
		}
	}
	
}
