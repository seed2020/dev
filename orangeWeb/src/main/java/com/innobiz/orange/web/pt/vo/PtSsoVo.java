package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

public class PtSsoVo {
	
	private Integer onetime;
	
	private long createTime;
	
	private String odurUid;
	
	private String skin;
	
	private String lang;
	
	private boolean used;
	
	public PtSsoVo(UserVo userVo){
		this.onetime = Math.abs(StringUtil.getNextInt());
		this.createTime = System.currentTimeMillis();
		this.odurUid = userVo.getOdurUid();
		this.skin = userVo.getSkin();
		this.lang = userVo.getLangTypCd();
	}
	
	public PtSsoVo(String odurUid, String skin, String lang, Integer onetime){
		this.onetime = onetime;
		this.createTime = System.currentTimeMillis();
		this.odurUid = odurUid;
		this.skin = skin;
		this.lang = lang;
	}
	
	public Integer getOnetime() {
		return onetime;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	public String getOdurUid(){
		return odurUid;
	}
	
	public String getSkin() {
		return skin;
	}

	public String getLang() {
		return lang;
	}
	
	public void setUsed(){
		used = true;
	}
	
	public boolean isUsed(){
		return used;
	}

	public boolean isOver(int sec){
		return isOver(System.currentTimeMillis(), sec);
	}
	
	public boolean isOver(long systime, int sec){
		return systime - createTime - (sec * 1000) > 0;
	}
}
