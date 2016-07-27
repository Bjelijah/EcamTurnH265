package com.howell.utils;

public interface IConst {

	final int MSG_LOGIN_CAM_OK       = 0xf0;
	final int MSG_DISCONNECT		 = 0xf1;
	
	final int MSG_VIDEO_LIST_GET_OK  = 0xf2;
	
	
	
	final String TEST_ACCOUNT   	= "bxm555";//no use
	final String TEST_PASSWORD		= "bxm555";//no use
	
	final String WSDL_URL = "https://192.168.18.245:8850/HomeService/HomeMCUService.svc?wsdl"; //soap service  wsdl url
	
	
	final String TEST_IP = "192.168.18.245";//turn service ip
	final int TEST_TURN_SERCICE_PORT = 8862;//turn service port
	
	
	
	final static int MSG_LOGIN_OK 	=  0xa0;
	final static int MSG_LOGIN_FAIL = 0xa1;
	final static int MSG_CONNECT_OK = 0xa2;
	
}
