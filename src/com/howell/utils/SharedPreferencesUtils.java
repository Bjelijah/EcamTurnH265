package com.howell.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author 霍之昊 
 *
 * 类说明
 */
public class SharedPreferencesUtils {
	private static final String spName = "turn_server_info";

	public static void saveTurnServerInfo(Context mContext,String trunServerIp,String trunServerPort){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("ip", trunServerIp);
        editor.putString("port", trunServerPort);
        editor.commit();
	}
	
	public static String getTurnServerIp(Context mContext){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
		return sharedPreferences.getString("ip", null);
	}
	
	public static String getTurnServerPort(Context mContext){
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
		return sharedPreferences.getString("port", null);
	}
	
	
	
	
	
}
