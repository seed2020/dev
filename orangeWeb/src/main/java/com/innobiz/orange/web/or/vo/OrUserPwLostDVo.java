package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자비밀번호분실상세(OR_USER_PW_LOST_D) 테이블 VO
 */
public class OrUserPwLostDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7293100130889963292L;

	/** 분실ID - KEY */
	private String lostId;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 등록일시 */
	private String regDt;

	/** 분실ID - KEY */
	public String getLostId() {
		return lostId;
	}

	/** 분실ID - KEY */
	public void setLostId(String lostId) {
		this.lostId = lostId;
	}

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
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
			return "com.innobiz.orange.web.or.dao.OrUserPwLostDDao.selectOrUserPwLostD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwLostDDao.insertOrUserPwLostD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwLostDDao.updateOrUserPwLostD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwLostDDao.deleteOrUserPwLostD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPwLostDDao.countOrUserPwLostD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자비밀번호분실상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(lostId!=null) { if(tab!=null) builder.append(tab); builder.append("lostId(분실ID-PK):").append(lostId).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}
}
