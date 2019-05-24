package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서본문내역(AP_ONGD_BODY_L) 테이블 VO
 */
public class ApOngdBodyLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1118237157564403160L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 본문이력번호 - KEY */
	private String bodyHstNo;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 본문HTML */
	private String bodyHtml;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 수정자명 */
	private String modrNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 본문이력번호 - KEY */
	public String getBodyHstNo() {
		return bodyHstNo;
	}

	/** 본문이력번호 - KEY */
	public void setBodyHstNo(String bodyHstNo) {
		this.bodyHstNo = bodyHstNo;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 본문HTML */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/** 본문HTML */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/** 스토리지 리턴 */
	public String getStorage(){
		return storage == null ? "ONGD" : storage;
	}

	/** 스토리지 세팅 */
	public void setStorage(String storage) {
		if(storage==null || storage.isEmpty()) this.storage = null;
		else if(storage.equals("ONGD") || storage.matches("[0-9A-Z]{4}")) this.storage = storage;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBodyLDao.selectApOngdBodyL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBodyLDao.insertApOngdBodyL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBodyLDao.updateApOngdBodyL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBodyLDao.deleteApOngdBodyL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBodyLDao.countApOngdBodyL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서본문내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_BODY_L").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(bodyHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHstNo(본문이력번호-PK):").append(bodyHstNo).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(bodyHtml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHtml(본문HTML):").append(bodyHtml).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
