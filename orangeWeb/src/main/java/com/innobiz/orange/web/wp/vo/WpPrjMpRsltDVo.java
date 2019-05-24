package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 프로잭트인력결과상세(WP_PRJ_MP_RSLT_D) 테이블 VO
 */
public class WpPrjMpRsltDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6564269664857952268L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 인력ID - KEY */
	private String mpId;

	/** 결과년월 - KEY */
	private String rsltYm;

	/** 결과맨먼스 */
	private String rsltMm;

	/** 월멘데이 */
	private String mMd;


	// 추가컬럼
	/** 프로잭트번호 목록 */
	private List<String> prjNoList;

	/** 최소결과년월 */
	private String minRsltYm;

	/** 최대결과년월 */
	private String maxRsltYm;

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

	/** 결과년월 - KEY */
	public String getRsltYm() {
		return rsltYm;
	}

	/** 결과년월 - KEY */
	public void setRsltYm(String rsltYm) {
		this.rsltYm = rsltYm;
	}

	/** 결과맨먼스 */
	public String getRsltMm() {
		return rsltMm;
	}

	/** 결과맨먼스 */
	public void setRsltMm(String rsltMm) {
		this.rsltMm = rsltMm;
	}

	/** 월멘데이 */
	public String getMMd() {
		return mMd;
	}

	/** 월멘데이 */
	public void setMMd(String mMd) {
		this.mMd = mMd;
	}

	/** 프로잭트번호 목록 */
	public List<String> getPrjNoList() {
		return prjNoList;
	}

	/** 프로잭트번호 목록 */
	public void setPrjNoList(List<String> prjNoList) {
		this.prjNoList = prjNoList;
	}

	/** 최소결과년월 */
	public String getMinRsltYm() {
		return minRsltYm;
	}

	/** 최소결과년월 */
	public void setMinRsltYm(String minRsltYm) {
		this.minRsltYm = minRsltYm;
	}

	/** 최대결과년월 */
	public String getMaxRsltYm() {
		return maxRsltYm;
	}

	/** 최대결과년월 */
	public void setMaxRsltYm(String maxRsltYm) {
		this.maxRsltYm = maxRsltYm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.selectWpPrjMpRsltD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.insertWpPrjMpRsltD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.updateWpPrjMpRsltD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.deleteWpPrjMpRsltD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.countWpPrjMpRsltD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트인력결과상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		if(rsltYm!=null) { if(tab!=null) builder.append(tab); builder.append("rsltYm(결과년월-PK):").append(rsltYm).append('\n'); }
		if(rsltMm!=null) { if(tab!=null) builder.append(tab); builder.append("rsltMm(결과맨먼스):").append(rsltMm).append('\n'); }
		if(mMd!=null) { if(tab!=null) builder.append(tab); builder.append("mMd(월멘데이):").append(mMd).append('\n'); }
		if(prjNoList!=null) { if(tab!=null) builder.append(tab); builder.append("prjNoList(프로잭트번호 목록):"); appendStringListTo(builder, prjNoList, tab); builder.append('\n'); }
		if(minRsltYm!=null) { if(tab!=null) builder.append(tab); builder.append("minRsltYm(최소결과년월):").append(minRsltYm).append('\n'); }
		if(maxRsltYm!=null) { if(tab!=null) builder.append(tab); builder.append("maxRsltYm(최대결과년월):").append(maxRsltYm).append('\n'); }
		super.toString(builder, tab);
	}
}
