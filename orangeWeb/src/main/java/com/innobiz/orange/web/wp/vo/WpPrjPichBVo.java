package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 프로잭트담당자상세(WP_PRJ_PICH_B) 테이블 VO
 */
public class WpPrjPichBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3820647055334317538L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 일련번호 - KEY */
	private String seq;

	/** 담당자명 */
	private String pichNm;

	/** 담당자직급 */
	private String pichGrade;

	/** 담당자전화번호 */
	private String pichPhon;

	/** 담당자이메일 */
	private String pichEmail;

	/** 비고 */
	private String note;

	/** 프로잭트번호 - KEY */
	public String getPrjNo() {
		return prjNo;
	}

	/** 프로잭트번호 - KEY */
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 담당자명 */
	public String getPichNm() {
		return pichNm;
	}

	/** 담당자명 */
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}

	/** 담당자직급 */
	public String getPichGrade() {
		return pichGrade;
	}

	/** 담당자직급 */
	public void setPichGrade(String pichGrade) {
		this.pichGrade = pichGrade;
	}

	/** 담당자전화번호 */
	public String getPichPhon() {
		return pichPhon;
	}

	/** 담당자전화번호 */
	public void setPichPhon(String pichPhon) {
		this.pichPhon = pichPhon;
	}

	/** 담당자이메일 */
	public String getPichEmail() {
		return pichEmail;
	}

	/** 담당자이메일 */
	public void setPichEmail(String pichEmail) {
		this.pichEmail = pichEmail;
	}

	/** 비고 */
	public String getNote() {
		return note;
	}

	/** 비고 */
	public void setNote(String note) {
		this.note = note;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjPichBDao.selectWpPrjPichB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjPichBDao.insertWpPrjPichB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjPichBDao.updateWpPrjPichB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjPichBDao.deleteWpPrjPichB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjPichBDao.countWpPrjPichB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트담당자상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(pichNm!=null) { if(tab!=null) builder.append(tab); builder.append("pichNm(담당자명):").append(pichNm).append('\n'); }
		if(pichGrade!=null) { if(tab!=null) builder.append(tab); builder.append("pichGrade(담당자직급):").append(pichGrade).append('\n'); }
		if(pichPhon!=null) { if(tab!=null) builder.append(tab); builder.append("pichPhon(담당자전화번호):").append(pichPhon).append('\n'); }
		if(pichEmail!=null) { if(tab!=null) builder.append(tab); builder.append("pichEmail(담당자이메일):").append(pichEmail).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		super.toString(builder, tab);
	}
}
