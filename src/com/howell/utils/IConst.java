package com.howell.utils;

public interface IConst {

	final int MSG_LOGIN_CAM_OK       = 0xf0;
	final int MSG_DISCONNECT		 = 0xf1;
	
	final int MSG_VIDEO_LIST_GET_OK  = 0xf2;
	
	
	
	final String TEST_ACCOUNT   	= "bxm555";//no use
	final String TEST_PASSWORD		= "bxm555";//no use
	
	
	
//	final String WSDL_URL = "https://192.168.18.245:8850/HomeService/HomeMCUService.svc?wsdl"; //soap service  wsdl url
	final String WSDL_URL = "https://180.166.7.214:8850/HomeService/HomeMCUService.svc?wsdl"; //soap service  wsdl url
	
	
	//192.168.18.245

	final String TEST_IP = "180.166.7.214";//turn service ip
	final int TEST_TURN_SERCICE_PORT = 8862;//turn service port
	
	
	
	final static int MSG_LOGIN_OK 				= 0xa0;
	final static int MSG_LOGIN_FAIL 			= 0xa1;
	final static int MSG_CONNECT_OK 			= 0xa2;
	final static int MSG_SOCK_CONNECT 			= 0xa3;
	final static int MSG_TURN_CONNECT_OK 		= 0xa4;
	final static int MSG_TURN_CONNECT_FAIL 		= 0xa5;
	final static int MSG_TURN_DISCONNECT_OK		= 0xa6;
	final static int MSG_TURN_DISCONNECT_FAIL	= 0xa7;
	final static int MSG_TURN_SUBSCRIBE_OK		= 0xa8;
	final static int MSG_TURN_SUBSCRIBE_FAIL	= 0xa9;
	final static int MSG_TURN_DISSUBSCRIBE_OK	= 0xaa;
	final static int MSG_TURN_DISSUBSCRIBE_FAIL	= 0xab;
	final static int MSG_TURN_GETCAMERA_OK		= 0xac;
	final static int MSG_TURN_GETCAMERA_FAIL	= 0xad;
	final static int MSG_TURN_GETRECORD_OK		= 0xae;
	final static int MSG_TURN_GETRECORD_FAIL	= 0xaf;
	final static int MSG_TURN_PTZ_OK			= 0xb0;
	final static int MSG_TURN_PTZ_FAIL			= 0xb1;
	
	
	
	//videoList
	final static int MSG_RECORD_LIST_GET = 0xc0;
	
	
	
}
