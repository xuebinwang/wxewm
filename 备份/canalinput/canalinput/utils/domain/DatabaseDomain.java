package com.primeton.di.trans.steps.canalinput.utils.domain;


public class DatabaseDomain {
    private String driver = "com.mysql.jdbc.Driver";
    private String port;
    private String user;
    private String pwd;
    private String server;
    private String access = "mysql";
    
    
	@Override
	public String toString() {
		return "DatabaseDomain [driver=" + driver + ", port=" + port + ", user=" + user + ", pwd=" + pwd + ", server="
				+ server + ", access=" + access + "]";
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getport() {
		return port;
	}
	public void setport(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
    
    
}
