package com.howell.protocol;

import java.io.Serializable;

public class SetVideoBasicRes implements Serializable{
	String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "SetVideoBasicRes [result=" + result + "]";
	}

	public SetVideoBasicRes(String result) {
		super();
		this.result = result;
	}

	public SetVideoBasicRes() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
