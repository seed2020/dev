package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * SSO임시(PT_SSO_T) 테이블 VO
 */
public class PtSsoTVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 746674473085859713L;

	/** SSO ID - KEY */
	private String ssoId;

	/** 사용자UID */
	private String userUid;

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 대상URL */
	private String tgtUrl;

	/** 권한URL */
	private String authUrl;

	/** 등록일시 */
	private String regDt;

	/** SSO ID - KEY */
	public String getSsoId() {
		return ssoId;
	}

	/** SSO ID - KEY */
	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 대상URL */
	public String getTgtUrl() {
		return tgtUrl;
	}

	/** 대상URL */
	public void setTgtUrl(String tgtUrl) {
		this.tgtUrl = tgtUrl;
	}

	/** 권한URL */
	public String getAuthUrl() {
		return authUrl;
	}

	/** 권한URL */
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoTDao.selectPtSsoT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoTDao.insertPtSsoT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoTDao.updatePtSsoT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoTDao.deletePtSsoT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoTDao.countPtSsoT";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":SSO임시]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(ssoId!=null) { if(tab!=null) builder.append(tab); builder.append("ssoId(SSO ID-PK):").append(ssoId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(tgtUrl!=null) { if(tab!=null) builder.append(tab); builder.append("tgtUrl(대상URL):").append(tgtUrl).append('\n'); }
		if(authUrl!=null) { if(tab!=null) builder.append(tab); builder.append("authUrl(권한URL):").append(authUrl).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}
}
