package com.howell.protocol;

import java.io.Serializable;

public class GetVideoBasicRes implements Serializable{
	String result = null;
	String dnMode = null;
	int dnSensitivityMin;
	int dnSensitivityMax;
	int dnSensitivityVal;
	String agcMode = null;
	int agcMin;
	int agcMax;
	int agcVal;
	String eShutterMode = null;
	String eShutterVal = null;
	String eShutterOptions = null;
	String infraredMode = null;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDnMode() {
		return dnMode;
	}
	public void setDnMode(String dnMode) {
		this.dnMode = dnMode;
	}
	public int getDnSensitivityVal() {
		return dnSensitivityVal;
	}
	public void setDnSensitivityVal(int dnSensitivityVal) {
		this.dnSensitivityVal = dnSensitivityVal;
	}
	public String getAgcMode() {
		return agcMode;
	}
	public void setAgcMode(String agcMode) {
		this.agcMode = agcMode;
	}
	public int getAgcVal() {
		return agcVal;
	}
	public void setAgcVal(int agcVal) {
		this.agcVal = agcVal;
	}
	public String geteShutterMode() {
		return eShutterMode;
	}
	public void seteShutterMode(String eShutterMode) {
		this.eShutterMode = eShutterMode;
	}
	public String geteShutterVal() {
		return eShutterVal;
	}
	public void seteShutterVal(String eShutterVal) {
		this.eShutterVal = eShutterVal;
	}
	public String geteShutterOptions() {
		return eShutterOptions;
	}
	public void seteShutterOptions(String eShutterOptions) {
		this.eShutterOptions = eShutterOptions;
	}
	public String getInfraredMode() {
		return infraredMode;
	}
	public void setInfraredMode(String infraredMode) {
		this.infraredMode = infraredMode;
	}
	public int getDnSensitivityMin() {
		return dnSensitivityMin;
	}
	public void setDnSensitivityMin(int dnSensitivityMin) {
		this.dnSensitivityMin = dnSensitivityMin;
	}
	public int getDnSensitivityMax() {
		return dnSensitivityMax;
	}
	public void setDnSensitivityMax(int dnSensitivityMax) {
		this.dnSensitivityMax = dnSensitivityMax;
	}
	public int getAgcMin() {
		return agcMin;
	}
	public void setAgcMin(int agcMin) {
		this.agcMin = agcMin;
	}
	public int getAgcMax() {
		return agcMax;
	}
	public void setAgcMax(int agcMax) {
		this.agcMax = agcMax;
	}
	@Override
	public String toString() {
		return "GetVideoBasicRes [result=" + result + ", dnMode=" + dnMode + ", dnSensitivityMin=" + dnSensitivityMin
				+ ", dnSensitivityMax=" + dnSensitivityMax + ", dnSensitivityVal=" + dnSensitivityVal + ", agcMode="
				+ agcMode + ", agcMin=" + agcMin + ", agcMax=" + agcMax + ", agcVal=" + agcVal + ", eShutterMode="
				+ eShutterMode + ", eShutterVal=" + eShutterVal + ", eShutterOptions=" + eShutterOptions
				+ ", infraredMode=" + infraredMode + "]";
	}
	public GetVideoBasicRes(String result, String dnMode, int dnSensitivityMin, int dnSensitivityMax,
			int dnSensitivityVal, String agcMode, int agcMin, int agcMax, int agcVal, String eShutterMode,
			String eShutterVal, String eShutterOptions, String infraredMode) {
		super();
		this.result = result;
		this.dnMode = dnMode;
		this.dnSensitivityMin = dnSensitivityMin;
		this.dnSensitivityMax = dnSensitivityMax;
		this.dnSensitivityVal = dnSensitivityVal;
		this.agcMode = agcMode;
		this.agcMin = agcMin;
		this.agcMax = agcMax;
		this.agcVal = agcVal;
		this.eShutterMode = eShutterMode;
		this.eShutterVal = eShutterVal;
		this.eShutterOptions = eShutterOptions;
		this.infraredMode = infraredMode;
	}
	public GetVideoBasicRes() {
		super();
	}
	
	
}
