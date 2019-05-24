package com.innobiz.orange.web.sh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 권한 VO
 */
public class ShAuthVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7406778884081256063L;

	/** 권한구분코드 */
	private String authTypCd;

	/** 권한코드 */
	private String authCd;

	/** 권한코드 목록 */
	private List<String> authCdList;

	/** 권한구분코드 */
	public String getAuthTypCd() {
		return authTypCd;
	}

	/** 권한구분코드 */
	public void setAuthTypCd(String authTypCd) {
		this.authTypCd = authTypCd;
	}

	/** 권한코드 */
	public String getAuthCd() {
		return authCd;
	}

	/** 권한코드 */
	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	/** 권한코드 목록 */
	public List<String> getAuthCdList() {
		return authCdList;
	}

	/** 권한코드 목록 */
	public void setAuthCdList(List<String> authCdList) {
		this.authCdList = authCdList;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":권한 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(authTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authTypCd(권한구분코드):").append(authTypCd).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한코드):").append(authCd).append('\n'); }
		if(authCdList!=null) { if(tab!=null) builder.append(tab); builder.append("authCdList(권한코드 목록):"); appendStringListTo(builder, authCdList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
