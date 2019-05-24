package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서보류의견상세(AP_ONGD_HOLD_OPIN_D) 테이블 VO
 */
public class ApOngdHoldOpinDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7918654871633998397L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 결재자UID - KEY */
	private String apvrUid;

	/** 결재의견내용 */
	private String apvOpinCont;

	/** 결재의견표시여부 */
	private String apvOpinDispYn;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 결재자UID - KEY */
	public String getApvrUid() {
		return apvrUid;
	}

	/** 결재자UID - KEY */
	public void setApvrUid(String apvrUid) {
		this.apvrUid = apvrUid;
	}

	/** 결재의견내용 */
	public String getApvOpinCont() {
		return apvOpinCont;
	}

	/** 결재의견내용 */
	public void setApvOpinCont(String apvOpinCont) {
		this.apvOpinCont = apvOpinCont;
	}

	/** 결재의견표시여부 */
	public String getApvOpinDispYn() {
		return apvOpinDispYn;
	}

	/** 결재의견표시여부 */
	public void setApvOpinDispYn(String apvOpinDispYn) {
		this.apvOpinDispYn = apvOpinDispYn;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdHoldOpinDDao.selectApOngdHoldOpinD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdHoldOpinDDao.insertApOngdHoldOpinD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdHoldOpinDDao.updateApOngdHoldOpinD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdHoldOpinDDao.deleteApOngdHoldOpinD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdHoldOpinDDao.countApOngdHoldOpinD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서보류의견상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(apvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUid(결재자UID-PK):").append(apvrUid).append('\n'); }
		if(apvOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("apvOpinCont(결재의견내용):").append(apvOpinCont).append('\n'); }
		if(apvOpinDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("apvOpinDispYn(결재의견표시여부):").append(apvOpinDispYn).append('\n'); }
		super.toString(builder, tab);
	}
}
