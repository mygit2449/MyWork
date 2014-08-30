package com.daleelo.Qiblah.Activites;

import java.io.Serializable;

public class TimeZoneModel implements Serializable{
	private String offset;
	private String suffix;
	private String localtime;
	private String isotime;
	private String utctime;
	private String dst;
	
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getLocaltime() {
		return localtime;
	}
	public void setLocaltime(String localtime) {
		this.localtime = localtime;
	}
	public String getIsotime() {
		return isotime;
	}
	public void setIsotime(String isotime) {
		this.isotime = isotime;
	}
	public String getUtctime() {
		return utctime;
	}
	public void setUtctime(String utctime) {
		this.utctime = utctime;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}

}
