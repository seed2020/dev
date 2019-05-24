package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 저장소기본(AP_STOR_B) 테이블 VO
 */
public class ApStorBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3682324096859140570L;

	/** 저장소ID - KEY */
	private String storId;

	/** 생성여부 */
	private String crtYn;

	/** 리소스ID */
	private String rescId;

	/** 전체문서건수 */
	private String allDocCnt;


	// 추가컬럼
	/** SQL일련번호 */
	private Integer sqlSeq;

	/** 리소스명 */
	private String rescNm;

	/** 저장소ID - KEY */
	public String getStorId() {
		return storId;
	}

	/** 저장소ID - KEY */
	public void setStorId(String storId) {
		this.storId = storId;
	}

	/** 생성여부 */
	public String getCrtYn() {
		return crtYn;
	}

	/** 생성여부 */
	public void setCrtYn(String crtYn) {
		this.crtYn = crtYn;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 전체문서건수 */
	public String getAllDocCnt() {
		return allDocCnt;
	}

	/** 전체문서건수 */
	public void setAllDocCnt(String allDocCnt) {
		this.allDocCnt = allDocCnt;
	}

	/** SQL일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}

	/** SQL일련번호 */
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorBDao.selectApStorB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorBDao.insertApStorB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorBDao.updateApStorB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorBDao.deleteApStorB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorBDao.countApStorB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":저장소기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID-PK):").append(storId).append('\n'); }
		if(crtYn!=null) { if(tab!=null) builder.append(tab); builder.append("crtYn(생성여부):").append(crtYn).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(allDocCnt!=null) { if(tab!=null) builder.append(tab); builder.append("allDocCnt(전체문서건수):").append(allDocCnt).append('\n'); }
		if(sqlSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sqlSeq(SQL일련번호):").append(sqlSeq).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
