package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서공람게시상세(AP_ONGD_PUB_BX_D) 테이블 VO
 */
public class ApOngdPubBxDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4230364453042353540L;

	/** 공람게시부서ID - KEY */
	private String pubBxDeptId;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 공람게시일시 */
	private String pubBxDt;

	/** 공람게시종료년월일 */
	private String pubBxEndYmd;

	/** 등록자UID */
	private String regrUid;

	/** 등록자명 */
	private String regrNm;

	/** 등록부서ID */
	private String regDeptId;

	/** 등록부서명 */
	private String regDeptNm;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 공람게시부서ID - KEY */
	public String getPubBxDeptId() {
		return pubBxDeptId;
	}

	/** 공람게시부서ID - KEY */
	public void setPubBxDeptId(String pubBxDeptId) {
		this.pubBxDeptId = pubBxDeptId;
	}

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 공람게시일시 */
	public String getPubBxDt() {
		return pubBxDt;
	}

	/** 공람게시일시 */
	public void setPubBxDt(String pubBxDt) {
		this.pubBxDt = pubBxDt;
	}

	/** 공람게시종료년월일 */
	public String getPubBxEndYmd() {
		return pubBxEndYmd;
	}

	/** 공람게시종료년월일 */
	public void setPubBxEndYmd(String pubBxEndYmd) {
		this.pubBxEndYmd = pubBxEndYmd;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 등록부서ID */
	public String getRegDeptId() {
		return regDeptId;
	}

	/** 등록부서ID */
	public void setRegDeptId(String regDeptId) {
		this.regDeptId = regDeptId;
	}

	/** 등록부서명 */
	public String getRegDeptNm() {
		return regDeptNm;
	}

	/** 등록부서명 */
	public void setRegDeptNm(String regDeptNm) {
		this.regDeptNm = regDeptNm;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxDDao.selectApOngdPubBxD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxDDao.insertApOngdPubBxD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxDDao.updateApOngdPubBxD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxDDao.deleteApOngdPubBxD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxDDao.countApOngdPubBxD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서공람게시상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_PUB_BX_D").append('\n'); }
		if(pubBxDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxDeptId(공람게시부서ID-PK):").append(pubBxDeptId).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(pubBxDt!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxDt(공람게시일시):").append(pubBxDt).append('\n'); }
		if(pubBxEndYmd!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxEndYmd(공람게시종료년월일):").append(pubBxEndYmd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(regDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("regDeptId(등록부서ID):").append(regDeptId).append('\n'); }
		if(regDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("regDeptNm(등록부서명):").append(regDeptNm).append('\n'); }
		super.toString(builder, tab);
	}
}
