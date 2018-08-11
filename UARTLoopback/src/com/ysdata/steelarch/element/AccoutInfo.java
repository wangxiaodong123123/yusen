package com.ysdata.steelarch.element;

public class AccoutInfo {
	private String user = "";
	private String pwd = "";
	private String ticket = "";
	
	public AccoutInfo() {

	}
	
	public AccoutInfo(String user, String pwd, String ticket) {
		this.user = user;
		this.pwd = pwd;
		this.ticket = ticket;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getUser() {
    	return user;
    }
	
	public String getPwd() {
		return pwd;
	}
	
	public void setTicket(String ticket) {
    	this.ticket = ticket;
    }
	
	public String getTicket() {
		return ticket;
	}
}

