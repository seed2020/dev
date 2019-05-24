package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자비밀번호이력(OR_USER_PW_H) 테이블 VO
 */
public class OrUserPwHVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7119784867987371728L;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 비밀번호구분코드 - KEY - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	private String pwTypCd;

	/** 수정일시 - KEY */
	private String modDt;

	/** 수정자UID */
	private String modrUid;

	/** 비밀번호암호값 */
	private String pwEnc;


	// 추가컬럼
	/** 수정자명 */
	private String modrNm;

	/** 비밀번호 */
	private String pw;

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 비밀번호구분코드 - KEY - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	public String getPwTypCd() {
		return pwTypCd;
	}

	/** 비밀번호구분코드 - KEY - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	public void setPwTypCd(String pwTypCd) {
		this.pwTypCd = pwTypCd;
	}

	/** 수정일시 - KEY */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 - KEY */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 비밀번호암호값 */
	public String getPwEnc() {
		return pwEnc;
	}

	/** 비밀번호암호값 */
	public void setPwEnc(String pwEnc) {
		this.pwEnc = pwEnc;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** 비밀번호 */
	public String getPw() {
		return pw;
	}

	/** 비밀번호 */
	public void setPw(String pw) {
		this.pw = pw;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwHDao.selectOrUserPwH";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwHDao.insertOrUserPwH";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwHDao.updateOrUserPwH";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwHDao.deleteOrUserPwH";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwHDao.countOrUserPwH";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자비밀번호이력]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(pwTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pwTypCd(비밀번호구분코드-PK):").append(pwTypCd).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시-PK):").append(modDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(pwEnc!=null) { if(tab!=null) builder.append(tab); builder.append("pwEnc(비밀번호암호값):").append(pwEnc).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if(pw!=null) { if(tab!=null) builder.append(tab); builder.append("pw(비밀번호):").append(pw).append('\n'); }
		super.toString(builder, tab);
	}
}
