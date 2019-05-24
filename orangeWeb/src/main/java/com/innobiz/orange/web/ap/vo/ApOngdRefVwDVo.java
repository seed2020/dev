package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서참조열람상세(AP_ONGD_REF_VW_D) 테이블 VO
 */
public class ApOngdRefVwDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4177322398992895214L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 참조열람자UID - KEY */
	private String refVwrUid;

	/** 등록자UID */
	private String regrUid;

	/** 정렬순서 */
	private String sortOrdr;

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	private String refVwStatCd;

	/** 참조열람자명 */
	private String refVwrNm;

	/** 참조열람자직위코드 */
	private String refVwrPositCd;

	/** 참조열람자직위명 */
	private String refVwrPositNm;

	/** 참조열람자직책코드 */
	private String refVwrTitleCd;

	/** 참조열람자직책명 */
	private String refVwrTitleNm;

	/** 참조열람자부서ID */
	private String refVwrDeptId;

	/** 참조열람자부서명 */
	private String refVwrDeptNm;

	/** 참조열람일시 */
	private String refVwDt;

	/** 참조열람의견내용 */
	private String refVwOpinCont;

	/** 등록일시 */
	private String regDt;

	/** 조회일시 */
	private String vwDt;

	/** 참조열람고정결재자여부 */
	private String refVwFixdApvrYn;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 테이블타입 */
	private String tableType = null;

	/** 등록자명 */
	private String regrNm;

	/** 참조열람상태명 */
	private String refVwStatNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 참조열람자UID - KEY */
	public String getRefVwrUid() {
		return refVwrUid;
	}

	/** 참조열람자UID - KEY */
	public void setRefVwrUid(String refVwrUid) {
		this.refVwrUid = refVwrUid;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	public String getRefVwStatCd() {
		return refVwStatCd;
	}

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	public void setRefVwStatCd(String refVwStatCd) {
		this.refVwStatCd = refVwStatCd;
	}

	/** 참조열람자명 */
	public String getRefVwrNm() {
		return refVwrNm;
	}

	/** 참조열람자명 */
	public void setRefVwrNm(String refVwrNm) {
		this.refVwrNm = refVwrNm;
	}

	/** 참조열람자직위코드 */
	public String getRefVwrPositCd() {
		return refVwrPositCd;
	}

	/** 참조열람자직위코드 */
	public void setRefVwrPositCd(String refVwrPositCd) {
		this.refVwrPositCd = refVwrPositCd;
	}

	/** 참조열람자직위명 */
	public String getRefVwrPositNm() {
		return refVwrPositNm;
	}

	/** 참조열람자직위명 */
	public void setRefVwrPositNm(String refVwrPositNm) {
		this.refVwrPositNm = refVwrPositNm;
	}

	/** 참조열람자직책코드 */
	public String getRefVwrTitleCd() {
		return refVwrTitleCd;
	}

	/** 참조열람자직책코드 */
	public void setRefVwrTitleCd(String refVwrTitleCd) {
		this.refVwrTitleCd = refVwrTitleCd;
	}

	/** 참조열람자직책명 */
	public String getRefVwrTitleNm() {
		return refVwrTitleNm;
	}

	/** 참조열람자직책명 */
	public void setRefVwrTitleNm(String refVwrTitleNm) {
		this.refVwrTitleNm = refVwrTitleNm;
	}

	/** 참조열람자부서ID */
	public String getRefVwrDeptId() {
		return refVwrDeptId;
	}

	/** 참조열람자부서ID */
	public void setRefVwrDeptId(String refVwrDeptId) {
		this.refVwrDeptId = refVwrDeptId;
	}

	/** 참조열람자부서명 */
	public String getRefVwrDeptNm() {
		return refVwrDeptNm;
	}

	/** 참조열람자부서명 */
	public void setRefVwrDeptNm(String refVwrDeptNm) {
		this.refVwrDeptNm = refVwrDeptNm;
	}

	/** 참조열람일시 */
	public String getRefVwDt() {
		return refVwDt;
	}

	/** 참조열람일시 */
	public void setRefVwDt(String refVwDt) {
		this.refVwDt = refVwDt;
	}

	/** 참조열람의견내용 */
	public String getRefVwOpinCont() {
		return refVwOpinCont;
	}

	/** 참조열람의견내용 */
	public void setRefVwOpinCont(String refVwOpinCont) {
		this.refVwOpinCont = refVwOpinCont;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 조회일시 */
	public String getVwDt() {
		return vwDt;
	}

	/** 조회일시 */
	public void setVwDt(String vwDt) {
		this.vwDt = vwDt;
	}

	/** 참조열람고정결재자여부 */
	public String getRefVwFixdApvrYn() {
		return refVwFixdApvrYn;
	}

	/** 참조열람고정결재자여부 */
	public void setRefVwFixdApvrYn(String refVwFixdApvrYn) {
		this.refVwFixdApvrYn = refVwFixdApvrYn;
	}

	/** 스토리지 리턴 */
	public String getStorage(){
		return storage == null ? "ONGD" : storage;
	}

	/** 스토리지 세팅 - ONGD, 년도4자리 */
	public void setStorage(String storage) {
		if(storage==null || storage.isEmpty()) this.storage = null;
		else if(storage.equals("ONGD") || storage.matches("[0-9A-Z]{4}")) this.storage = storage;
	}

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "D" : tableType;
	}

	/** 테이블 타입 제거 */
	public void removeTableType(){
		tableType = null;
	}

	/** 히스토리 테이블 설정 */
	public void setHistory(){
		tableType = "H";
	}

	/** 이행 테이블 설정 */
	public void setExecution(){
		tableType = "E";
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 참조열람상태명 */
	public String getRefVwStatNm() {
		return refVwStatNm;
	}

	/** 참조열람상태명 */
	public void setRefVwStatNm(String refVwStatNm) {
		this.refVwStatNm = refVwStatNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRefVwDDao.selectApOngdRefVwD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRefVwDDao.insertApOngdRefVwD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRefVwDDao.updateApOngdRefVwD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRefVwDDao.deleteApOngdRefVwD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRefVwDDao.countApOngdRefVwD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서참조열람상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null || tableType!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_REF_VW_").append(tableType!=null ? tableType : "D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(refVwrUid!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrUid(참조열람자UID-PK):").append(refVwrUid).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(refVwStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("refVwStatCd(참조열람상태코드):").append(refVwStatCd).append('\n'); }
		if(refVwrNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrNm(참조열람자명):").append(refVwrNm).append('\n'); }
		if(refVwrPositCd!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrPositCd(참조열람자직위코드):").append(refVwrPositCd).append('\n'); }
		if(refVwrPositNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrPositNm(참조열람자직위명):").append(refVwrPositNm).append('\n'); }
		if(refVwrTitleCd!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrTitleCd(참조열람자직책코드):").append(refVwrTitleCd).append('\n'); }
		if(refVwrTitleNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrTitleNm(참조열람자직책명):").append(refVwrTitleNm).append('\n'); }
		if(refVwrDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrDeptId(참조열람자부서ID):").append(refVwrDeptId).append('\n'); }
		if(refVwrDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrDeptNm(참조열람자부서명):").append(refVwrDeptNm).append('\n'); }
		if(refVwDt!=null) { if(tab!=null) builder.append(tab); builder.append("refVwDt(참조열람일시):").append(refVwDt).append('\n'); }
		if(refVwOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("refVwOpinCont(참조열람의견내용):").append(refVwOpinCont).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		if(refVwFixdApvrYn!=null) { if(tab!=null) builder.append(tab); builder.append("refVwFixdApvrYn(참조열람고정결재자여부):").append(refVwFixdApvrYn).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(refVwStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwStatNm(참조열람상태명):").append(refVwStatNm).append('\n'); }
		super.toString(builder, tab);
	}
}
