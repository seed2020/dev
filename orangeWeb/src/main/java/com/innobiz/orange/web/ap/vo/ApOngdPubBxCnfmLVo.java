package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서공람게시확인내역(AP_ONGD_PUB_BX_CNFM_L) 테이블 VO
 */
public class ApOngdPubBxCnfmLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7407762788384762899L;

	/** 공람게시부서ID - KEY */
	private String pubBxDeptId;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 사용자UID - KEY */
	private String userUid;

	/** 사용자명 */
	private String userNm;

	/** 부서ID */
	private String deptId;

	/** 부서명 */
	private String deptNm;

	/** 직위코드 */
	private String positCd;

	/** 직위명 */
	private String positNm;

	/** 조회일시 */
	private String vwDt;


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

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** 부서명 */
	public String getDeptNm() {
		return deptNm;
	}

	/** 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	/** 직위코드 */
	public String getPositCd() {
		return positCd;
	}

	/** 직위코드 */
	public void setPositCd(String positCd) {
		this.positCd = positCd;
	}

	/** 직위명 */
	public String getPositNm() {
		return positNm;
	}

	/** 직위명 */
	public void setPositNm(String positNm) {
		this.positNm = positNm;
	}

	/** 조회일시 */
	public String getVwDt() {
		return vwDt;
	}

	/** 조회일시 */
	public void setVwDt(String vwDt) {
		this.vwDt = vwDt;
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
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxCnfmLDao.selectApOngdPubBxCnfmL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxCnfmLDao.insertApOngdPubBxCnfmL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxCnfmLDao.updateApOngdPubBxCnfmL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxCnfmLDao.deleteApOngdPubBxCnfmL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPubBxCnfmLDao.countApOngdPubBxCnfmL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서공람게시확인내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_PUB_BX_CNFM_L").append('\n'); }
		if(pubBxDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxDeptId(공람게시부서ID-PK):").append(pubBxDeptId).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(positCd!=null) { if(tab!=null) builder.append(tab); builder.append("positCd(직위코드):").append(positCd).append('\n'); }
		if(positNm!=null) { if(tab!=null) builder.append(tab); builder.append("positNm(직위명):").append(positNm).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		super.toString(builder, tab);
	}
}
