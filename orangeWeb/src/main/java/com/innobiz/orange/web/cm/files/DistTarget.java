package com.innobiz.orange.web.cm.files;

/** 배포 타겟(서버) 정보, context.properties 에서 정보 관리됨 */
public class DistTarget {
	
	/** IP */
	private String ip;
	
	/** ID (for FTP) */
	private String id;
	
	/** 암호 (for FTP) */
	private String pwd;
	
	/** 생성자 */
	public DistTarget(){}
	
	/** 생성자 */
	public DistTarget(String ip, String id, String pwd) {
		this.ip = ip;
		this.id = id;
		this.pwd = pwd;
	}

	/** IP */
	public String getIp() {
		return ip;
	}

	/** IP */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/** ID (for FTP) */
	public String getId() {
		return id;
	}

	/** ID (for FTP) */
	public void setId(String id) {
		this.id = id;
	}

	/** 암호 (for FTP) */
	public String getPwd() {
		return pwd;
	}

	/** 암호 (for FTP) */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
