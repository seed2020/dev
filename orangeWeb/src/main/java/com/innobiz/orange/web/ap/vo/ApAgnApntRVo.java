package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 대리지정관계(AP_AGN_APNT_R) 테이블 VO
 */
public class ApAgnApntRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6910972519840093230L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 대리인UID - KEY */
	private String agntUid;

	/** 대리인리소스ID */
	private String agntRescId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 대리인명 */
	private String agntNm;

	/** 대리인리소스명 */
	private String agntRescNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 대리인UID - KEY */
	public String getAgntUid() {
		return agntUid;
	}

	/** 대리인UID - KEY */
	public void setAgntUid(String agntUid) {
		this.agntUid = agntUid;
	}

	/** 대리인리소스ID */
	public String getAgntRescId() {
		return agntRescId;
	}

	/** 대리인리소스ID */
	public void setAgntRescId(String agntRescId) {
		this.agntRescId = agntRescId;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 대리인명 */
	public String getAgntNm() {
		return agntNm;
	}

	/** 대리인명 */
	public void setAgntNm(String agntNm) {
		this.agntNm = agntNm;
	}

	/** 대리인리소스명 */
	public String getAgntRescNm() {
		return agntRescNm;
	}

	/** 대리인리소스명 */
	public void setAgntRescNm(String agntRescNm) {
		this.agntRescNm = agntRescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntRDao.selectApAgnApntR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntRDao.insertApAgnApntR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntRDao.updateApAgnApntR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntRDao.deleteApAgnApntR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntRDao.countApAgnApntR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":대리지정관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(agntUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntUid(대리인UID-PK):").append(agntUid).append('\n'); }
		if(agntRescId!=null) { if(tab!=null) builder.append(tab); builder.append("agntRescId(대리인리소스ID):").append(agntRescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(agntNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntNm(대리인명):").append(agntNm).append('\n'); }
		if(agntRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntRescNm(대리인리소스명):").append(agntRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
