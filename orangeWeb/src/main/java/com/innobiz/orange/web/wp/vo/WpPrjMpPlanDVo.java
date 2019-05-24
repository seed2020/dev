package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블 VO
 */
public class WpPrjMpPlanDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 800510071027443825L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 인력ID - KEY */
	private String mpId;

	/** 버전 */
	private String ver;

	/** 인력구분코드 */
	private String mpTypCd;

	/** 프로잭트역할1코드 */
	private String prjRole1Cd;

	/** 프로잭트역할2코드 */
	private String prjRole2Cd;

	/** 정렬순서 */
	private String sortOrdr;

	/** 인력멘먼스합계 */
	private String mpMmSum;


	// 추가컬럼
	/** 테이블타입 */
	private String tableType = null;

	/** 프로잭트번호 목록 */
	private List<String> prjNoList;

	/** 투입인력명 */
	private String mpNm;

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

	/** 버전 */
	public String getVer() {
		return ver;
	}

	/** 버전 */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/** 인력구분코드 */
	public String getMpTypCd() {
		return mpTypCd;
	}

	/** 인력구분코드 */
	public void setMpTypCd(String mpTypCd) {
		this.mpTypCd = mpTypCd;
	}

	/** 프로잭트역할1코드 */
	public String getPrjRole1Cd() {
		return prjRole1Cd;
	}

	/** 프로잭트역할1코드 */
	public void setPrjRole1Cd(String prjRole1Cd) {
		this.prjRole1Cd = prjRole1Cd;
	}

	/** 프로잭트역할2코드 */
	public String getPrjRole2Cd() {
		return prjRole2Cd;
	}

	/** 프로잭트역할2코드 */
	public void setPrjRole2Cd(String prjRole2Cd) {
		this.prjRole2Cd = prjRole2Cd;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 인력멘먼스합계 */
	public String getMpMmSum() {
		return mpMmSum;
	}

	/** 인력멘먼스합계 */
	public void setMpMmSum(String mpMmSum) {
		this.mpMmSum = mpMmSum;
	}

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "D" : "D"+tableType;
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

	/** 투입인력명 */
	public String getMpNm() {
		return mpNm;
	}

	/** 투입인력명 */
	public void setMpNm(String mpNm) {
		this.mpNm = mpNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanDDao.selectWpPrjMpPlanD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanDDao.insertWpPrjMpPlanD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanDDao.updateWpPrjMpPlanD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanDDao.deleteWpPrjMpPlanD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjMpPlanDDao.countWpPrjMpPlanD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트인력계획상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		if(ver!=null) { if(tab!=null) builder.append(tab); builder.append("ver(버전):").append(ver).append('\n'); }
		if(mpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mpTypCd(인력구분코드):").append(mpTypCd).append('\n'); }
		if(prjRole1Cd!=null) { if(tab!=null) builder.append(tab); builder.append("prjRole1Cd(프로잭트역할1코드):").append(prjRole1Cd).append('\n'); }
		if(prjRole2Cd!=null) { if(tab!=null) builder.append(tab); builder.append("prjRole2Cd(프로잭트역할2코드):").append(prjRole2Cd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(mpMmSum!=null) { if(tab!=null) builder.append(tab); builder.append("mpMmSum(인력멘먼스합계):").append(mpMmSum).append('\n'); }
		if(prjNoList!=null) { if(tab!=null) builder.append(tab); builder.append("prjNoList(프로잭트번호 목록):"); appendStringListTo(builder, prjNoList, tab); builder.append('\n'); }
		if(mpNm!=null) { if(tab!=null) builder.append(tab); builder.append("mpNm(투입인력명):").append(mpNm).append('\n'); }
		super.toString(builder, tab);
	}
}
