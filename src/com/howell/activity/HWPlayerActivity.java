package com.howell.activity;

import com.android.howell.webcamH265.R;
import com.howell.action.AudioAction;
import com.howell.action.PlayerManager;
import com.howell.action.YV12Renderer;
import com.howell.protocol.GetVideoBasicRes;
import com.howell.protocol.SetVideoBasicReq;
import com.howell.protocol.SoapManager;
import com.howell.utils.IConst;
import com.howell.utils.PhoneConfig;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class HWPlayerActivity extends Activity implements Callback, OnTouchListener, OnGestureListener, OnClickListener,IConst{
	
	private GLSurfaceView mGlView;
	GestureDetector mGestureDetector;
	
	private boolean showSurfaceSet = false;
	private ScrollView mSurfaceSet;
    private LinearLayout mLayoutChangePower;
    private CheckBox mColorChange,mScale,mLaserSet,mLasetPower,mMoveDetect,cbShutter,cbGain,cbStream;
    private Spinner spShutter;
    private  PlayerManager mPlayMgr = PlayerManager.getInstance();
    private ProgressBar mProgressBar;
    
    private boolean mIsScale = false;
    private String [] mShutterOptions;
    private boolean mIsOptionUpdata = false;
    
    boolean mIsStreamSub = true;
    
    
    
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_LOGIN_CAM_OK:
				mPlayMgr.playViewCam(0);//默认主码流
				AudioAction.getInstance().playAudio();
				break;
			case MSG_DISCONNECT:
				mPlayMgr.transDeInit();
				break;
			case MSG_DISCONNECT_UNEXPECT:
				mProgressBar.setVisibility(View.VISIBLE);
				mPlayMgr.reLink();
				break;
			case MSG_DATA_MISSING:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case MSG_DATA_COMING:
				mProgressBar.setVisibility(View.GONE);
				break;
			case MSG_GET_VIDEO_BASIC_ERROR:
				Toast.makeText(HWPlayerActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
				mProgressBar.setVisibility(View.GONE);
				break;
			case MSG_GET_VIDEO_BASIC_OK:
				Bundle data = msg.getData();
				GetVideoBasicRes res = (GetVideoBasicRes) data.getSerializable("res");
				boolean isVmd = data.getBoolean("vmd");
				updataVideoBaseState(res,isVmd);
				mProgressBar.setVisibility(View.GONE);
				break;
			case MSG_SET_VIDEO_BASIC_ERROR:
				Toast.makeText(HWPlayerActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
				mProgressBar.setVisibility(View.GONE);
				break;
			case MSG_SET_VIDEO_BASIC_OK:
				mProgressBar.setVisibility(View.GONE);
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hw_player);
		
		//FIXME test fun
		testFun();
        initUI();
        initFun();
	}

	private void testFun(){
		if (!IS_DEBUG)return;
		SoapManager.context = this;
		SoapManager.initUrl(this);	
		
	}
	
	
	private void initUI(){
		mGlView = (GLSurfaceView) findViewById(R.id.glsurface_view);
		mGlView.setEGLContextClientVersion(2);
		mGlView.setRenderer(new YV12Renderer(this, mGlView, mHandler));
		mGlView.getHolder().addCallback( this);
		mGlView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mGlView.setOnTouchListener(this);   
		mGlView.setFocusable(true);   
		mGlView.setClickable(true);   
		mGlView.setLongClickable(true);   
		mGestureDetector = new GestureDetector(this,this);   
        mGestureDetector.setIsLongpressEnabled(true);  
        
        mSurfaceSet = (ScrollView) findViewById(R.id.scrollView);
        mSurfaceSet.setVisibility(View.GONE);
        showSurfaceSet = false;
        mGlView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (showSurfaceSet) {
					mSurfaceSet.setVisibility(View.GONE);
					showSurfaceSet = false;
				}else{
					mSurfaceSet.setVisibility(View.VISIBLE);
					showSurfaceSet = true;
					mPlayMgr.getVideoBaseParam();
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}
		});
        mLayoutChangePower = (LinearLayout) findViewById(R.id.ll_change_power);
        mColorChange = (CheckBox)findViewById(R.id.cb_color_change);
        mColorChange.setOnClickListener(this);
        
        mScale = (CheckBox)findViewById(R.id.cb_scale);
        mScale.setOnClickListener(this);
        
        
        
    	mLaserSet = (CheckBox)findViewById(R.id.cb_laser_set);
        mLaserSet.setOnClickListener(this);
        
        mLasetPower = (CheckBox)findViewById(R.id.cb_laser_power);
        mLasetPower.setOnClickListener(this);
        
    	mMoveDetect = (CheckBox)findViewById(R.id.cb_move_detect);
        mMoveDetect.setOnClickListener(this);
        
        
        cbShutter = (CheckBox)findViewById(R.id.cb_shutter);
        cbShutter.setOnClickListener(this);
        
        
        cbGain = (CheckBox)findViewById(R.id.cb_gain);
        cbGain.setOnClickListener(this);
        
        
    	spShutter = (Spinner)findViewById(R.id.spinner_shutter);
    	spShutter.setVisibility(View.GONE);
    	mProgressBar = (ProgressBar) findViewById(R.id.my_progressBar);
    	
    	
    	String [] shutterArray = getResources().getStringArray(R.array.shutter_arry);
    	String [] tmp = new String[shutterArray.length];
    	for(int i=0;i<shutterArray.length;i++){
    		tmp[i] = shutterArray[i]+(i>0?"秒":"");
    	}
		ArrayAdapter<String> shutterAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tmp);
		shutterAdapter.setDropDownViewResource(R.layout.spinner_item); 
		spShutter.setAdapter(shutterAdapter);  
        spShutter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Log.i("123", "onItemSelected   arg2="+arg2);
				if (arg2==0) {
					return;
				}
				shutterSPFun(arg2,mIsOptionUpdata?mShutterOptions:getResources().getStringArray(R.array.shutter_arry));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Log.i("123", "onNothingSelected");
			}
		});
        
        cbStream = (CheckBox) findViewById(R.id.cb_stream);
        cbStream.setOnClickListener(this);
        cbStream.setChecked(true);
        mIsScale = false;
        
        
        
        
	}

	private void initFun(){
		mPlayMgr.setHandler(mHandler);
		mPlayMgr.setContext(this);
		mPlayMgr.loginCam();
	}
	
	private boolean getShutterOptions(String option){//"A,B,C,D"
		if (option==null) {
			return false;
		}
		mShutterOptions = option.split(",");
		Log.i("123", "mShutterOption len="+mShutterOptions.length);
		String [] tmp = new String[mShutterOptions.length+1];
		tmp[0]="请选择：";
		for(int i=0;i<mShutterOptions.length;i++){
			Log.i("123","option="+mShutterOptions[i]);
			tmp[i+1] = mShutterOptions[i]+"秒";
		}
		
		ArrayAdapter<String> shutterAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tmp);
		shutterAdapter.setDropDownViewResource(R.layout.spinner_item); 
		spShutter.setAdapter(shutterAdapter);
		return true;
	}
	
	
	private void updataVideoBaseState(GetVideoBasicRes res,boolean isVmd){
		if (res.getDnMode()!=null) {
			mColorChange.setChecked(res.getDnMode().equalsIgnoreCase("BlackAndWhite")?false:true);
		}else{
			mColorChange.setChecked(false);
		}
		mScale.setChecked(mIsScale);
		if (res.getInfraredMode()!=null) {
			mLaserSet.setChecked(res.getInfraredMode().equalsIgnoreCase("Off")?false:true);
			mLasetPower.setChecked(res.getInfraredMode().equalsIgnoreCase("High")?true:false);
			mLasetPower.setEnabled(res.getInfraredMode().equalsIgnoreCase("Off")?false:true);
			mLayoutChangePower.setVisibility(res.getInfraredMode().equalsIgnoreCase("Off")?View.INVISIBLE:View.VISIBLE);
		}else{
			mLaserSet.setChecked(false);
			mLasetPower.setEnabled(false);
			mLayoutChangePower.setVisibility(View.INVISIBLE);
		}
		
		mMoveDetect.setChecked(isVmd);
		
		if (res.getAgcMode()!=null) {
			cbGain.setChecked(res.getAgcMode().equalsIgnoreCase("High")?true:false);//
			if (res.getAgcMode().equalsIgnoreCase("Manual")) {
				cbGain.setChecked(res.getAgcVal()>((res.getAgcMax()-res.getAgcMin())/2)?true:false);
			}
			
			
		}else{
			cbGain.setChecked(false);
		}
		
		mIsOptionUpdata = getShutterOptions(res.geteShutterOptions());
		
		
		if (res.geteShutterMode()!=null) {
			if(res.geteShutterMode().equalsIgnoreCase("Auto")){
				cbShutter.setChecked(false);
				spShutter.setVisibility(View.INVISIBLE);
			}else{
				cbShutter.setChecked(true);
				spShutter.setVisibility(View.VISIBLE);
				Log.i("123", " shutter value="+res.geteShutterVal()+"   option="+res.geteShutterOptions());
				if (mIsOptionUpdata) {
					int index = 0;
					for(int i=0;i<mShutterOptions.length;i++){
						if (res.geteShutterVal().equalsIgnoreCase(mShutterOptions[i])) {
							index = i;
							break;
						}
					}
					Log.i("123", "spShutter setSelection   Line 264");
					spShutter.setSelection(index,true);
				}else{
					String str = res.geteShutterVal();
					if (str.equalsIgnoreCase("1/2")) {
						Log.i("123", "spShutter setSelection   0");
						spShutter.setSelection(0,true);
					}else if (str.equalsIgnoreCase("1/4")) {
						Log.i("123", "spShutter setSelection   1");
						spShutter.setSelection(1,true);
					}else if (str.equalsIgnoreCase("1/7")) {
						Log.i("123", "spShutter setSelection   2");
						spShutter.setSelection(2,true);
					}else if (str.equalsIgnoreCase("1/12.5")) {
						Log.i("123", "spShutter setSelection   3");
						spShutter.setSelection(3,true);
					}else{
						Toast.makeText(this, "获取快门信息时报", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}else{
			cbShutter.setChecked(false);
			spShutter.setVisibility(View.INVISIBLE);
		}
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mGlView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mGlView.onResume();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mPlayMgr.stopViewCam();
		mPlayMgr.logoutCam();
		mPlayMgr.setHandler(null);
		super.onDestroy();
	}
	
	
	
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(arg1);   
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	private void colorModeFun(boolean b){
		SetVideoBasicReq req = new SetVideoBasicReq();
		req.setDnMode(b?"Auto":"BlackAndWhite");
		mPlayMgr.setVideoBaseParam(req);
	}
	
	private void laserModeFun(boolean b){
		SetVideoBasicReq req = new SetVideoBasicReq();
		if (b) {
			req.setDnMode("BlackAndWhite");
			req.setInfraredMode("Low");
			
			mColorChange.setChecked(false);
			mLasetPower.setEnabled(true);
			mLasetPower.setChecked(false);
			
			mLayoutChangePower.setVisibility(View.VISIBLE);
			
		}else{
			req.setDnMode("Auto");
			req.setInfraredMode("Off");
			
			mColorChange.setChecked(true);
			mLasetPower.setChecked(false);
			mLasetPower.setEnabled(false);
			mLayoutChangePower.setVisibility(View.INVISIBLE);
		}
		mPlayMgr.setVideoBaseParam(req);
		
	}
	
	private void laserPowerFun(boolean b){
		SetVideoBasicReq req = new SetVideoBasicReq();
		if (b) {
			req.setInfraredMode("High");
		}else{
			req.setInfraredMode("Low");
		}
		mPlayMgr.setVideoBaseParam(req);
		
	}
	
	private void gainFun(boolean b){
		SetVideoBasicReq req = new SetVideoBasicReq();
		if (b) {
			req.setAgcMode("High");
		}else{
			req.setAgcMode("Auto");
		}
		mPlayMgr.setVideoBaseParam(req);
		
		
	}
	
	private void shutterFun(boolean b){
		if (b) {
			spShutter.setVisibility(View.VISIBLE);
			cbGain.setChecked(true);
			spShutter.performClick();
			
		}else{
			SetVideoBasicReq req = new SetVideoBasicReq();
			req.seteShutterMode("Auto");
			req.setAgcMode("Auto");
			mPlayMgr.setVideoBaseParam(req);
			
			
			cbShutter.setChecked(false);
			cbGain.setChecked(false);
			spShutter.setVisibility(View.INVISIBLE);
			
		}
	}
	
	private void shutterSPFun(int index,String [] data){
		if (index<0||index>data.length) return;
		//Log.i("123", "data="+data[index]);
		mPlayMgr.setVideoBaseParam(new SetVideoBasicReq().setAgcMode(cbGain.isChecked()?"High":"Auto")
														 .seteShutterMode("Manual")
														 .seteShutter(data[index]));	
	}
	
	private void scaleFun(boolean b){
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mGlView.getLayoutParams();
		if (b) {
			lp.height = PhoneConfig.getPhoneHeight(HWPlayerActivity.this) * 2;
			lp.width = PhoneConfig.getPhoneWidth(HWPlayerActivity.this) * 2;
			mIsScale = true;
		}else{
			lp.height = PhoneConfig.getPhoneHeight(HWPlayerActivity.this);
			lp.width = PhoneConfig.getPhoneWidth(HWPlayerActivity.this);
			mIsScale = false;
		}
		mGlView.setLayoutParams(lp);
		mHandler.sendEmptyMessage(MSG_SET_VIDEO_BASIC_OK);
	}
	
	private void VMDFun(boolean b){
		mPlayMgr.setVMD(b);
	}
	
	private void streamFun(boolean b){
		mIsStreamSub = !b;
		mPlayMgr.rePlay(mIsStreamSub?1:0);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		mProgressBar.setVisibility(View.VISIBLE);
		
		switch (arg0.getId()) {
		case R.id.cb_color_change:
			colorModeFun(mColorChange.isChecked());
			break;
		case R.id.cb_scale:
			scaleFun(mScale.isChecked());
			break;
		case R.id.cb_laser_set:
			laserModeFun(mLaserSet.isChecked());
			break;
		case R.id.cb_laser_power:
			laserPowerFun(mLasetPower.isChecked());
			break;
		case R.id.cb_move_detect:
			VMDFun(mMoveDetect.isChecked());
			break;
		case R.id.cb_shutter:
			shutterFun(cbShutter.isChecked());
			break;
		case R.id.cb_gain:
			gainFun(cbGain.isChecked());
			break;
		case R.id.cb_stream:
			streamFun(cbStream.isChecked());
			break;
		
		default:
			break;
		}
	
	}
	
}
