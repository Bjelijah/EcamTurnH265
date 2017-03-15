package com.howell.protocol;

public class SetVideoBasicReq {
	String account=null;
	String loginSession=null;
	String devID=null;
	int channelNO=0;
	String dnMode = null;
	int dnSensitivityVal =0;
	String agcMode = null;
	int agcVal = 0;
	String eShutterMode = null;
	String eShutter = null;
	String infraredMode = null;
	public String getAccount() {
		return account;
	}
	public SetVideoBasicReq setAccount(String account) {
		this.account = account;
		return this;
	}
	public String getLoginSession() {
		return loginSession;
	}
	public SetVideoBasicReq setLoginSession(String loginSession) {
		this.loginSession = loginSession;
		return this;
	}
	public String getDevID() {
		return devID;
	}
	public SetVideoBasicReq setDevID(String devID) {
		this.devID = devID;
		return this;
	}
	public int getChannelNO() {
		return channelNO;
	}
	public SetVideoBasicReq setChannelNO(int channelNO) {
		this.channelNO = channelNO;
		return this;
	}
	public String getDnMode() {
		return dnMode;
	}
	public SetVideoBasicReq setDnMode(String dnMode) {
		this.dnMode = dnMode;
		return this;
	}
	public int getDnSensitivityVal() {
		return dnSensitivityVal;
	}
	public SetVideoBasicReq setDnSensitivityVal(int dnSensitivityVal) {
		this.dnSensitivityVal = dnSensitivityVal;
		return this;
	}
	public String getAgcMode() {
		return agcMode;
	}
	public SetVideoBasicReq setAgcMode(String agcMode) {
		this.agcMode = agcMode;
		return this;
	}
	public int getAgcVal() {
		return agcVal;
	}
	public SetVideoBasicReq setAgcVal(int agcVal) {
		this.agcVal = agcVal;
		return this;
	}
	public String geteShutterMode() {
		return eShutterMode;
	}
	public SetVideoBasicReq seteShutterMode(String eShutterMode) {
		this.eShutterMode = eShutterMode;
		return this;
	}
	public String geteShutter() {
		return eShutter;
	}
	public SetVideoBasicReq seteShutter(String eShutter) {
		this.eShutter = eShutter;
		return this;
	}
	public String getInfraredMode() {
		return infraredMode;
	}
	public SetVideoBasicReq setInfraredMode(String infraredMode) {
		this.infraredMode = infraredMode;
		return this;
	}
	@Override
	public String toString() {
		return "SetVideoBasic [account=" + account + ", loginSession=" + loginSession + ", devID=" + devID
				+ ", channelNO=" + channelNO + ", dnMode=" + dnMode + ", dnSensitivityVal=" + dnSensitivityVal
				+ ", agcMode=" + agcMode + ", agcVal=" + agcVal + ", eShutterMode=" + eShutterMode + ", eShutter="
				+ eShutter + ", infraredMode=" + infraredMode + "]";
	}
	public SetVideoBasicReq(String account, String loginSession, String devID, int channelNO, String dnMode,
			int dnSensitivityVal, String agcMode, int agcVal, String eShutterMode, String eShutter,
			String infraredMode) {
		super();
		this.account = account;
		this.loginSession = loginSession;
		this.devID = devID;
		this.channelNO = channelNO;
		this.dnMode = dnMode;
		this.dnSensitivityVal = dnSensitivityVal;
		this.agcMode = agcMode;
		this.agcVal = agcVal;
		this.eShutterMode = eShutterMode;
		this.eShutter = eShutter;
		this.infraredMode = infraredMode;
	}
	public SetVideoBasicReq(String account, String loginSession, String devID, int channelNO) {
		super();
		this.account = account;
		this.loginSession = loginSession;
		this.devID = devID;
		this.channelNO = channelNO;
	}
	public SetVideoBasicReq() {
		super();
	}
}
