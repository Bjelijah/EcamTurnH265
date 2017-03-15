package com.howell.protocol;

public class GetVideoBasicReq {
	String account;
	String loginSession;
	String devID;
	int channelNO;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getLoginSession() {
		return loginSession;
	}
	public void setLoginSession(String loginSession) {
		this.loginSession = loginSession;
	}
	public String getDevID() {
		return devID;
	}
	public void setDevID(String devID) {
		this.devID = devID;
	}
	public int getChannelNO() {
		return channelNO;
	}
	public void setChannelNO(int channelNO) {
		this.channelNO = channelNO;
	}
	@Override
	public String toString() {
		return "GetVideoBasicReq [account=" + account + ", loginSession=" + loginSession + ", devID=" + devID
				+ ", channelNO=" + channelNO + "]";
	}
	public GetVideoBasicReq(String account, String loginSession, String devID, int channelNO) {
		super();
		this.account = account;
		this.loginSession = loginSession;
		this.devID = devID;
		this.channelNO = channelNO;
	}
	public GetVideoBasicReq() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
