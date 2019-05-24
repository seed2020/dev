package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 진행문서ERP양식상세(AP_ONGD_ERP_FORM_D) 테이블 VO
 */
public class ApOngdErpFormDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2933791924733555149L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** ERP값구분코드 - KEY */
	private String erpVaTypCd;

	/** ERP값 */
	private String erpVa;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 결재번호 목록 */
	private List<String> apvNoList;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** ERP값구분코드 - KEY */
	public String getErpVaTypCd() {
		return erpVaTypCd;
	}

	/** ERP값구분코드 - KEY */
	public void setErpVaTypCd(String erpVaTypCd) {
		this.erpVaTypCd = erpVaTypCd;
	}

	/** ERP값 */
	public String getErpVa() {
		return erpVa;
	}

	/** ERP값 */
	public void setErpVa(String erpVa) {
		this.erpVa = erpVa;
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

	/** 결재번호 목록 */
	public List<String> getApvNoList() {
		return apvNoList;
	}

	/** 결재번호 목록 */
	public void setApvNoList(List<String> apvNoList) {
		this.apvNoList = apvNoList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdErpFormDDao.selectApOngdErpFormD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdErpFormDDao.insertApOngdErpFormD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdErpFormDDao.updateApOngdErpFormD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdErpFormDDao.deleteApOngdErpFormD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdErpFormDDao.countApOngdErpFormD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서ERP양식상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_ERP_FORM_D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(erpVaTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("erpVaTypCd(ERP값구분코드-PK):").append(erpVaTypCd).append('\n'); }
		if(erpVa!=null) { if(tab!=null) builder.append(tab); builder.append("erpVa(ERP값):").append(erpVa).append('\n'); }
		if(apvNoList!=null) { if(tab!=null) builder.append(tab); builder.append("apvNoList(결재번호 목록):"); appendStringListTo(builder, apvNoList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
