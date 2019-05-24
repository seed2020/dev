package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 프로잭트인력결과내역(WP_PRJ_MP_RSLT_L) 테이블 VO
 */
public class WpPrjMpRsltLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1188647451224555383L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 인력ID - KEY */
	private String mpId;

	/** 결과년월일 - KEY */
	private String rsltYmd;

	/** 결과맨데이 */
	private String rsltMd;

	/** 비고 */
	private String note;


	// 추가컬럼
	/** 프로잭트번호 목록 */
	private List<String> prjNoList;

	/** 최소투입일 */
	private String minRsltYmd;

	/** 최대투입일 */
	private String maxRsltYmd;

	/** 프로잭트명 */
	private String prjNm;

	/** 색상 */
	private String color;

	/** 프로잭트번호 - KEY */
	public String getPrjNo() {
		return prjNo;
	}

	/** 프로잭트번호 - KEY */
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	/** 인력ID - KEY */
	public String getMpId() {
		return mpId;
	}

	/** 인력ID - KEY */
	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** 결과년월일 - KEY */
	public String getRsltYmd() {
		return rsltYmd;
	}

	/** 결과년월일 - KEY */
	public void setRsltYmd(String rsltYmd) {
		this.rsltYmd = rsltYmd;
	}

	/** 결과맨데이 */
	public String getRsltMd() {
		return rsltMd;
	}

	/** 결과맨데이 */
	public void setRsltMd(String rsltMd) {
		this.rsltMd = rsltMd;
	}

	/** 비고 */
	public String getNote() {
		return note;
	}

	/** 비고 */
	public void setNote(String note) {
		this.note = note;
	}

	/** 프로잭트번호 목록 */
	public List<String> getPrjNoList() {
		return prjNoList;
	}

	/** 프로잭트번호 목록 */
	public void setPrjNoList(List<String> prjNoList) {
		this.prjNoList = prjNoList;
	}

	/** 최소투입일 */
	public String getMinRsltYmd() {
		return minRsltYmd;
	}

	/** 최소투입일 */
	public void setMinRsltYmd(String minRsltYmd) {
		this.minRsltYmd = minRsltYmd;
	}

	/** 최대투입일 */
	public String getMaxRsltYmd() {
		return maxRsltYmd;
	}

	/** 최대투입일 */
	public void setMaxRsltYmd(String maxRsltYmd) {
		this.maxRsltYmd = maxRsltYmd;
	}

	/** 프로잭트명 */
	public String getPrjNm() {
		return prjNm;
	}

	/** 프로잭트명 */
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
	}

	/** 색상 */
	public String getColor() {
		return color;
	}

	/** 색상 */
	public void setColor(String color) {
		this.color = color;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltLDao.selectWpPrjMpRsltL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltLDao.insertWpPrjMpRsltL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltLDao.updateWpPrjMpRsltL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltLDao.deleteWpPrjMpRsltL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltLDao.countWpPrjMpRsltL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트인력결과내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		if(rsltYmd!=null) { if(tab!=null) builder.append(tab); builder.append("rsltYmd(결과년월일-PK):").append(rsltYmd).append('\n'); }
		if(rsltMd!=null) { if(tab!=null) builder.append(tab); builder.append("rsltMd(결과맨데이):").append(rsltMd).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		if(prjNoList!=null) { if(tab!=null) builder.append(tab); builder.append("prjNoList(프로잭트번호 목록):"); appendStringListTo(builder, prjNoList, tab); builder.append('\n'); }
		if(minRsltYmd!=null) { if(tab!=null) builder.append(tab); builder.append("minRsltYmd(최소투입일):").append(minRsltYmd).append('\n'); }
		if(maxRsltYmd!=null) { if(tab!=null) builder.append(tab); builder.append("maxRsltYmd(최대투입일):").append(maxRsltYmd).append('\n'); }
		if(prjNm!=null) { if(tab!=null) builder.append(tab); builder.append("prjNm(프로잭트명):").append(prjNm).append('\n'); }
		if(color!=null) { if(tab!=null) builder.append(tab); builder.append("color(색상):").append(color).append('\n'); }
		super.toString(builder, tab);
	}
}
