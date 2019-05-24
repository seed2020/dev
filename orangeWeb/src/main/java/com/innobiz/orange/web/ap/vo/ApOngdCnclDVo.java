package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서취소상세(AP_ONGD_CNCL_D) 테이블 VO
 */
public class ApOngdCnclDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4374922049240419958L;

	/** 기안자UID - KEY */
	private String makrUid;

	/** XML구분ID - KEY */
	private String xmlTypId;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 기안자UID - KEY */
	public String getMakrUid() {
		return makrUid;
	}

	/** 기안자UID - KEY */
	public void setMakrUid(String makrUid) {
		this.makrUid = makrUid;
	}

	/** XML구분ID - KEY */
	public String getXmlTypId() {
		return xmlTypId;
	}

	/** XML구분ID - KEY */
	public void setXmlTypId(String xmlTypId) {
		this.xmlTypId = xmlTypId;
	}

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdCnclDDao.selectApOngdCnclD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdCnclDDao.insertApOngdCnclD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdCnclDDao.updateApOngdCnclD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdCnclDDao.deleteApOngdCnclD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdCnclDDao.countApOngdCnclD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서취소상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(makrUid!=null) { if(tab!=null) builder.append(tab); builder.append("makrUid(기안자UID-PK):").append(makrUid).append('\n'); }
		if(xmlTypId!=null) { if(tab!=null) builder.append(tab); builder.append("xmlTypId(XML구분ID-PK):").append(xmlTypId).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		super.toString(builder, tab);
	}
}
