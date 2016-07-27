package com.howell.utils;

import org.json.JSONException;
import org.json.JSONObject;

import bean.GetCamBean;
import bean.GetRecordedFilesBean;
import bean.Subscribe;

public class JsonUtil {
	
	public static String subScribeJson(Subscribe subscribe){
		
		JSONObject object = null;
		
		object = new JSONObject();
		try {
			object.put("session_id", subscribe.getSessionId());
			object.put("topic", "media");
			
			JSONObject childchild = null;
			childchild = new JSONObject();
			childchild.put("device_id", subscribe.getDeviceId());
			childchild.put("mode", subscribe.getMode());
			childchild.put("channel", 0);
			childchild.put("stream", subscribe.getIs_sub());
			
			
			JSONObject child = null;
			child = new JSONObject();
			child.put("dialog_id", subscribe.getDialogId());
			child.put("meta", childchild);
			
			object.put("media", child);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object.toString();
	}
	
	public static String getCamJson(GetCamBean bean){

		JSONObject object = null;
		
		object = new JSONObject();
		try {
			object.put("username", bean.getUserName());
			object.put("password", bean.getPassword());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object.toString();
	}
	
	public static String getRecordFilesJson(GetRecordedFilesBean bean){
		
		JSONObject object = null;
		object = new JSONObject();
		
		try {
			object.put("device_id", bean.getDeviceId());
			object.put("channel", bean.getChannel());
			object.put("begin", bean.getBegin());
			object.put("end", bean.getEnd());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object.toString();
	}
	
	
	
}
