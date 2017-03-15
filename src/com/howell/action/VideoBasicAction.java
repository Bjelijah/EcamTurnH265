package com.howell.action;

import com.howell.protocol.GetVideoBasicReq;
import com.howell.protocol.GetVideoBasicRes;
import com.howell.protocol.SetVideoBasicReq;
import com.howell.protocol.SetVideoBasicRes;
import com.howell.protocol.SoapManager;
import com.howell.utils.IConst;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class VideoBasicAction implements IConst {
	private static VideoBasicAction mInstance = null;
	public static VideoBasicAction getInstance(){
		if (mInstance==null) {
			mInstance = new VideoBasicAction();
		}
		return mInstance;
	}
	private VideoBasicAction(){}
	
	private String mAccount;
	private String mLoginSession;
	private String mDevID;
	private int mChannelNo;
	private Handler mHandler=null;
	public VideoBasicAction init(String account,String session,String devID,int channelNo,Handler h){
		this.mAccount = account;
		this.mLoginSession = session;
		this.mDevID = devID;
		this.mChannelNo = channelNo;
		this.mHandler = h;
		return this;
	}
	
	public void getVideoBasic(){
		new AsyncTask<Void, Void, Void>() {
			GetVideoBasicRes mRes=null;
			@Override
			protected Void doInBackground(Void... arg0) {
				mRes = SoapManager.getInstance().GetVideoBasicRes(new GetVideoBasicReq(mAccount, mLoginSession, mDevID, mChannelNo));
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				if (mHandler!=null) {
					Message msg = new Message();
					msg.what = MSG_GET_VIDEO_BASIC;
					Bundle bundle = new Bundle();
					bundle.putSerializable("res", mRes);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
				super.onPostExecute(result);
			}
			
		}.execute();	
	}
	
	private void setVideoBasic(final SetVideoBasicReq req){
		new AsyncTask<Void, Void, Void>(){
			SetVideoBasicRes res = null;
			@Override
			protected Void doInBackground(Void... arg0) {
				res = SoapManager.getInstance().SetVideoBasicRes(req);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				if (mHandler!=null) {
					Message msg = new Message();
					msg.what = MSG_SET_VIDEO_BASIC;
					Bundle bundle = new Bundle();
					bundle.putSerializable("res", res);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
				super.onPostExecute(result);
			}
			
		}.execute();
	}
	
	public void setDN(String mode,int val){
		SetVideoBasicReq req = new SetVideoBasicReq(mAccount, mLoginSession, mDevID, mChannelNo);
		req.setDnMode(mode).setDnSensitivityVal(val);
		setVideoBasic(req);
	}
	
	public void setAGC(String mode,int val){
		SetVideoBasicReq req = new SetVideoBasicReq(mAccount, mLoginSession, mDevID, mChannelNo);
		req.setAgcMode(mode).setAgcVal(val);
		setVideoBasic(req);
	}
	
	public void setEShutter(String mode,String val){
		SetVideoBasicReq req = new SetVideoBasicReq(mAccount, mLoginSession, mDevID, mChannelNo);
		req.seteShutterMode(mode).seteShutter(val);
		setVideoBasic(req);
	}
	
	public void setInfrared(String mode){
		SetVideoBasicReq req = new SetVideoBasicReq(mAccount, mLoginSession, mDevID, mChannelNo);
		req.setInfraredMode(mode);
		setVideoBasic(req);
	}
}
