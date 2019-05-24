package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원카드권한상세(WO_ONEC_AUTH_D) 테이블 VO
 */
public class WoOnecAuthDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2336814092386624271L;

	/** 원카드번호 - KEY */
	private String onecNo;

	/** 사용자UID - KEY */
	private String userUid;

	/** 권한구분코드 */
	private String authTypCd;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 원카드번호 - KEY */
	public String getOnecNo() {
		return onecNo;
	}

	/** 원카드번호 - KEY */
	public void setOnecNo(String onecNo) {
		this.onecNo = onecNo;
	}

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 권한구분코드 */
	public String getAuthTypCd() {
		return authTypCd;
	}

	/** 권한구분코드 */
	public void setAuthTypCd(String authTypCd) {
		this.authTypCd = authTypCd;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecAuthDDao.selectWoOnecAuthD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecAuthDDao.insertWoOnecAuthD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecAuthDDao.updateWoOnecAuthD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecAuthDDao.deleteWoOnecAuthD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecAuthDDao.countWoOnecAuthD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드권한상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(onecNo!=null) { if(tab!=null) builder.append(tab); builder.append("onecNo(원카드번호-PK):").append(onecNo).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(authTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authTypCd(권한구분코드):").append(authTypCd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
