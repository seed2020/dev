package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 목록설정상세(PT_LST_SETUP_D) 테이블 VO
 */
public class PtLstSetupDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6934394296781302340L;

	/** 목록설정메타ID - KEY */
	private String lstSetupMetaId;

	/** 기본값여부 - KEY */
	private String dftVaYn;

	/** 속성ID - KEY */
	private String atrbId;

	/** 메시지ID */
	private String msgId;

	/** 넓이퍼센트 */
	private String wdthPerc;

	/** 줄맞춤값 */
	private String alnVa;

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	private String sortOptVa;

	/** 데이터정렬값 - asc:내림차순, desc:올림차순 */
	private String dataSortVa;

	/** 표시여부 */
	private String dispYn;

	/** 정렬순서 */
	private String sortOrdr;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 수정자명 */
	private String modrNm;

	/** 목록설정메타ID - KEY */
	public String getLstSetupMetaId() {
		return lstSetupMetaId;
	}

	/** 목록설정메타ID - KEY */
	public void setLstSetupMetaId(String lstSetupMetaId) {
		this.lstSetupMetaId = lstSetupMetaId;
	}

	/** 기본값여부 - KEY */
	public String getDftVaYn() {
		return dftVaYn;
	}

	/** 기본값여부 - KEY */
	public void setDftVaYn(String dftVaYn) {
		this.dftVaYn = dftVaYn;
	}

	/** 속성ID - KEY */
	public String getAtrbId() {
		return atrbId;
	}

	/** 속성ID - KEY */
	public void setAtrbId(String atrbId) {
		this.atrbId = atrbId;
	}

	/** 메시지ID */
	public String getMsgId() {
		return msgId;
	}

	/** 메시지ID */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/** 넓이퍼센트 */
	public String getWdthPerc() {
		return wdthPerc;
	}

	/** 넓이퍼센트 */
	public void setWdthPerc(String wdthPerc) {
		this.wdthPerc = wdthPerc;
	}

	/** 줄맞춤값 */
	public String getAlnVa() {
		return alnVa;
	}

	/** 줄맞춤값 */
	public void setAlnVa(String alnVa) {
		this.alnVa = alnVa;
	}

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	public String getSortOptVa() {
		return sortOptVa;
	}

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	public void setSortOptVa(String sortOptVa) {
		this.sortOptVa = sortOptVa;
	}

	/** 데이터정렬값 - asc:내림차순, desc:올림차순 */
	public String getDataSortVa() {
		return dataSortVa;
	}

	/** 데이터정렬값 - asc:내림차순, desc:올림차순 */
	public void setDataSortVa(String dataSortVa) {
		this.dataSortVa = dataSortVa;
	}

	/** 표시여부 */
	public String getDispYn() {
		return dispYn;
	}

	/** 표시여부 */
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
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
			return "com.innobiz.orange.web.pt.dao.PtLstSetupDDao.selectPtLstSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupDDao.insertPtLstSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupDDao.updatePtLstSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupDDao.deletePtLstSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupDDao.countPtLstSetupD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":목록설정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(lstSetupMetaId!=null) { if(tab!=null) builder.append(tab); builder.append("lstSetupMetaId(목록설정메타ID-PK):").append(lstSetupMetaId).append('\n'); }
		if(dftVaYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftVaYn(기본값여부-PK):").append(dftVaYn).append('\n'); }
		if(atrbId!=null) { if(tab!=null) builder.append(tab); builder.append("atrbId(속성ID-PK):").append(atrbId).append('\n'); }
		if(msgId!=null) { if(tab!=null) builder.append(tab); builder.append("msgId(메시지ID):").append(msgId).append('\n'); }
		if(wdthPerc!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPerc(넓이퍼센트):").append(wdthPerc).append('\n'); }
		if(alnVa!=null) { if(tab!=null) builder.append(tab); builder.append("alnVa(줄맞춤값):").append(alnVa).append('\n'); }
		if(sortOptVa!=null) { if(tab!=null) builder.append(tab); builder.append("sortOptVa(정렬옵션값):").append(sortOptVa).append('\n'); }
		if(dataSortVa!=null) { if(tab!=null) builder.append(tab); builder.append("dataSortVa(데이터정렬값):").append(dataSortVa).append('\n'); }
		if(dispYn!=null) { if(tab!=null) builder.append(tab); builder.append("dispYn(표시여부):").append(dispYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
