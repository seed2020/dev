package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블 VO
 */
public class WpPrjMpPlanLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2496554611309116928L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 인력ID - KEY */
	private String mpId;

	/** 월번호 - KEY */
	private String mNo;

	/** 버전 */
	private String ver;

	/** 계획멘먼스 */
	private String planMm;

	/** 계획년월 */
	private String planYm;


	// 추가컬럼
	/** 테이블타입 */
	private String tableType = null;

	/** 프로잭트번호 목록 */
	private List<String> prjNoList;

	/** 최소계획년월 */
	private String minPlanYm;

	/** 최대계획년월 */
	private String maxPlanYm;

	/** 최소계획멘먼스 */
	private String minPlanMm;

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

	/** 월번호 - KEY */
	public String getMNo() {
		return mNo;
	}

	/** 월번호 - KEY */
	public void setMNo(String mNo) {
		this.mNo = mNo;
	}

	/** 버전 */
	public String getVer() {
		return ver;
	}

	/** 버전 */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/** 계획멘먼스 */
	public String getPlanMm() {
		return planMm;
	}

	/** 계획멘먼스 */
	public void setPlanMm(String planMm) {
		this.planMm = planMm;
	}

	/** 계획년월 */
	public String getPlanYm() {
		return planYm;
	}

	/** 계획년월 */
	public void setPlanYm(String planYm) {
		this.planYm = planYm;
	}

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "L" : "L"+tableType;
	}

	/** 테이블 타입 제거 */
	public void removeTableType(){
		tableType = null;
	}

	/** 히스토리 테이블 설정 */
	public void setHistory(){
		tableType = "H";
	}

	/** 프로잭트번호 목록 */
	public List<String> getPrjNoList() {
		return prjNoList;
	}

	/** 프로잭트번호 목록 */
	public void setPrjNoList(List<String> prjNoList) {
		this.prjNoList = prjNoList;
	}

	/** 최소계획년월 */
	public String getMinPlanYm() {
		return minPlanYm;
	}

	/** 최소계획년월 */
	public void setMinPlanYm(String minPlanYm) {
		this.minPlanYm = minPlanYm;
	}

	/** 최대계획년월 */
	public String getMaxPlanYm() {
		return maxPlanYm;
	}

	/** 최대계획년월 */
	public void setMaxPlanYm(String maxPlanYm) {
		this.maxPlanYm = maxPlanYm;
	}

	/** 최소계획멘먼스 */
	public String getMinPlanMm() {
		return minPlanMm;
	}

	/** 최소계획멘먼스 */
	public void setMinPlanMm(String minPlanMm) {
		this.minPlanMm = minPlanMm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.selectWpPrjMpPlanL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.insertWpPrjMpPlanL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.updateWpPrjMpPlanL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.deleteWpPrjMpPlanL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.countWpPrjMpPlanL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트인력계획내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		if(mNo!=null) { if(tab!=null) builder.append(tab); builder.append("mNo(월번호-PK):").append(mNo).append('\n'); }
		if(ver!=null) { if(tab!=null) builder.append(tab); builder.append("ver(버전):").append(ver).append('\n'); }
		if(planMm!=null) { if(tab!=null) builder.append(tab); builder.append("planMm(계획멘먼스):").append(planMm).append('\n'); }
		if(planYm!=null) { if(tab!=null) builder.append(tab); builder.append("planYm(계획년월):").append(planYm).append('\n'); }
		if(prjNoList!=null) { if(tab!=null) builder.append(tab); builder.append("prjNoList(프로잭트번호 목록):"); appendStringListTo(builder, prjNoList, tab); builder.append('\n'); }
		if(minPlanYm!=null) { if(tab!=null) builder.append(tab); builder.append("minPlanYm(최소계획년월):").append(minPlanYm).append('\n'); }
		if(maxPlanYm!=null) { if(tab!=null) builder.append(tab); builder.append("maxPlanYm(최대계획년월):").append(maxPlanYm).append('\n'); }
		if(minPlanMm!=null) { if(tab!=null) builder.append(tab); builder.append("minPlanMm(최소계획멘먼스):").append(minPlanMm).append('\n'); }
		super.toString(builder, tab);
	}
}
