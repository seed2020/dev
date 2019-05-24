package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서변환상세(AP_ONGD_TRX_D) 테이블 VO
 */
public class ApOngdTrxDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7247151619295713855L;

	/** 변환결재번호 - KEY */
	private String trxApvNo;

	/** 원본결재번호 */
	private String orgnApvNo;

	/** 양식ID */
	private String formId;

	/** 양식일련번호 */
	private String formSeq;

	/** 본문높이픽셀 */
	private String bodyHghtPx;

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	private String formWdthTypCd;

	/** 바닥글값 */
	private String footerVa;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 변환결재번호 - KEY */
	public String getTrxApvNo() {
		return trxApvNo;
	}

	/** 변환결재번호 - KEY */
	public void setTrxApvNo(String trxApvNo) {
		this.trxApvNo = trxApvNo;
	}

	/** 원본결재번호 */
	public String getOrgnApvNo() {
		return orgnApvNo;
	}

	/** 원본결재번호 */
	public void setOrgnApvNo(String orgnApvNo) {
		this.orgnApvNo = orgnApvNo;
	}

	/** 양식ID */
	public String getFormId() {
		return formId;
	}

	/** 양식ID */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식일련번호 */
	public String getFormSeq() {
		return formSeq;
	}

	/** 양식일련번호 */
	public void setFormSeq(String formSeq) {
		this.formSeq = formSeq;
	}

	/** 본문높이픽셀 */
	public String getBodyHghtPx() {
		return bodyHghtPx;
	}

	/** 본문높이픽셀 */
	public void setBodyHghtPx(String bodyHghtPx) {
		this.bodyHghtPx = bodyHghtPx;
	}

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	public String getFormWdthTypCd() {
		return formWdthTypCd;
	}

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	public void setFormWdthTypCd(String formWdthTypCd) {
		this.formWdthTypCd = formWdthTypCd;
	}

	/** 바닥글값 */
	public String getFooterVa() {
		return footerVa;
	}

	/** 바닥글값 */
	public void setFooterVa(String footerVa) {
		this.footerVa = footerVa;
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
			return "com.innobiz.orange.web.ap.dao.ApOngdTrxDDao.selectApOngdTrxD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdTrxDDao.insertApOngdTrxD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdTrxDDao.updateApOngdTrxD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdTrxDDao.deleteApOngdTrxD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdTrxDDao.countApOngdTrxD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서변환상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_TRX_D").append('\n'); }
		if(trxApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("trxApvNo(변환결재번호-PK):").append(trxApvNo).append('\n'); }
		if(orgnApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("orgnApvNo(원본결재번호):").append(orgnApvNo).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호):").append(formSeq).append('\n'); }
		if(bodyHghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHghtPx(본문높이픽셀):").append(bodyHghtPx).append('\n'); }
		if(formWdthTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formWdthTypCd(양식넓이구분코드):").append(formWdthTypCd).append('\n'); }
		if(footerVa!=null) { if(tab!=null) builder.append(tab); builder.append("footerVa(바닥글값):").append(footerVa).append('\n'); }
		super.toString(builder, tab);
	}
}
