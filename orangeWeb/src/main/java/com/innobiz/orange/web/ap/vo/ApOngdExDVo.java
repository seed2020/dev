package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 진행문서확장상세(AP_ONGD_EX_D) 테이블 VO
 */
public class ApOngdExDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2263192465055923539L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 확장ID - KEY */
	private String exId;

	/** 확장값 */
	private String exVa;


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

	/** 확장ID - KEY */
	public String getExId() {
		return exId;
	}

	/** 확장ID - KEY */
	public void setExId(String exId) {
		this.exId = exId;
	}

	/** 확장값 */
	public String getExVa() {
		return exVa;
	}

	/** 확장값 */
	public void setExVa(String exVa) {
		this.exVa = exVa;
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
			return "com.innobiz.orange.web.ap.dao.ApOngdExDDao.selectApOngdExD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExDDao.insertApOngdExD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExDDao.updateApOngdExD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExDDao.deleteApOngdExD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExDDao.countApOngdExD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서확장상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_EX_D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(exId!=null) { if(tab!=null) builder.append(tab); builder.append("exId(확장ID-PK):").append(exId).append('\n'); }
		if(exVa!=null) { if(tab!=null) builder.append(tab); builder.append("exVa(확장값):").append(exVa).append('\n'); }
		if(apvNoList!=null) { if(tab!=null) builder.append(tab); builder.append("apvNoList(결재번호 목록):"); appendStringListTo(builder, apvNoList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
