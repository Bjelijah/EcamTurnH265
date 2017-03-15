package com.howell.utils;

public interface IConst {

	final boolean IS_DEBUG				= false;
	
	final boolean USING_WSDL_ENCRYPTION = true;
	final boolean USING_TURN_ENCRYPTION = true;
	
	final int MSG_LOGIN_CAM_OK       = 0xf0;
	final int MSG_DISCONNECT		 = 0xf1;
	
	final int MSG_VIDEO_LIST_GET_OK  = 0xf2;

	
	final int MSG_DISCONNECT_UNEXPECT = 0x0012;
	final int MSG_DATA_MISSING		  = 0x0003;
	final int MSG_DATA_COMING         = 0x0004;
	

	
	
	final String TEST_ACCOUNT   	= "bxm555";//no use
	final String TEST_PASSWORD		= "bxm555";//no use
	
	final String WSDL_URL = "https://180.166.7.214:8850/HomeService/HomeMCUService.svc?wsdl"; //soap service  wsdl url FIXME 
	//should not be a local area network addresses 
	//180.166.7.214
	
	final String TEST_IP = "180.166.7.214";//turn service ip FIXME 
	final int TEST_TURN_SERCICE_PORT_SSL = 8862;//turn service port 
	final int TEST_TURN_SERVICE_PORT_NOSSL = 8812;//turn service port   8862
	
	
	final static int MSG_LOGIN_OK 	=  0xa0;
	final static int MSG_LOGIN_FAIL = 0xa1;
	final static int MSG_CONNECT_OK = 0xa2;
	
	
	final static int MSG_GET_VIDEO_BASIC = 0xb0;
	final static int MSG_SET_VIDEO_BASIC = 0xb1;
	final int MSG_GET_VIDEO_BASIC_ERROR  = 0xb2;
	final int MSG_GET_VIDEO_BASIC_OK	 = 0xb3;
	final int MSG_SET_VIDEO_BASIC_ERROR  = 0xb4;
	final int MSG_SET_VIDEO_BASIC_OK	 = 0xb5;
}
