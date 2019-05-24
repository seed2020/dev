package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 개인분류정보상세(AP_PSN_CLS_INFO_D) 테이블 VO
 */
public class ApPsnClsInfoDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8151225046355807709L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 개인분류정보ID - KEY */
	private String psnClsInfoId;

	/** 개인분류정보부모ID */
	private String psnClsInfoPid;

	/** 개인분류정보명 */
	private String psnClsInfoNm;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 개인분류정보ID - KEY */
	public String getPsnClsInfoId() {
		return psnClsInfoId;
	}

	/** 개인분류정보ID - KEY */
	public void setPsnClsInfoId(String psnClsInfoId) {
		this.psnClsInfoId = psnClsInfoId;
	}

	/** 개인분류정보부모ID */
	public String getPsnClsInfoPid() {
		return psnClsInfoPid;
	}

	/** 개인분류정보부모ID */
	public void setPsnClsInfoPid(String psnClsInfoPid) {
		this.psnClsInfoPid = psnClsInfoPid;
	}

	/** 개인분류정보명 */
	public String getPsnClsInfoNm() {
		return psnClsInfoNm;
	}

	/** 개인분류정보명 */
	public void setPsnClsInfoNm(String psnClsInfoNm) {
		this.psnClsInfoNm = psnClsInfoNm;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
			return "com.innobiz.orange.web.ap.dao.ApPsnClsInfoDDao.selectApPsnClsInfoD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApPsnClsInfoDDao.insertApPsnClsInfoD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApPsnClsInfoDDao.updateApPsnClsInfoD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApPsnClsInfoDDao.deleteApPsnClsInfoD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApPsnClsInfoDDao.countApPsnClsInfoD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":개인분류정보상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(psnClsInfoId!=null) { if(tab!=null) builder.append(tab); builder.append("psnClsInfoId(개인분류정보ID-PK):").append(psnClsInfoId).append('\n'); }
		if(psnClsInfoPid!=null) { if(tab!=null) builder.append(tab); builder.append("psnClsInfoPid(개인분류정보부모ID):").append(psnClsInfoPid).append('\n'); }
		if(psnClsInfoNm!=null) { if(tab!=null) builder.append(tab); builder.append("psnClsInfoNm(개인분류정보명):").append(psnClsInfoNm).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
