package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 목록설정메타상세(PT_LST_SETUP_META_D) 테이블 VO
 */
public class PtLstSetupMetaDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6953417001974532735L;

	/** 목록설정메타ID - KEY */
	private String lstSetupMetaId;

	/** 속성ID - KEY */
	private String atrbId;

	/** 메시지ID */
	private String msgId;

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	private String sortOptVa;

	/** 정렬순서 */
	private String sortOrdr;

	/** 목록설정메타ID - KEY */
	public String getLstSetupMetaId() {
		return lstSetupMetaId;
	}

	/** 목록설정메타ID - KEY */
	public void setLstSetupMetaId(String lstSetupMetaId) {
		this.lstSetupMetaId = lstSetupMetaId;
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

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	public String getSortOptVa() {
		return sortOptVa;
	}

	/** 정렬옵션값 - code:코드 테이블의 정렬순서, value:본 테이블의 순서, none:정렬하지 않음 */
	public void setSortOptVa(String sortOptVa) {
		this.sortOptVa = sortOptVa;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupMetaDDao.selectPtLstSetupMetaD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupMetaDDao.insertPtLstSetupMetaD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupMetaDDao.updatePtLstSetupMetaD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupMetaDDao.deletePtLstSetupMetaD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLstSetupMetaDDao.countPtLstSetupMetaD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":목록설정메타상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(lstSetupMetaId!=null) { if(tab!=null) builder.append(tab); builder.append("lstSetupMetaId(목록설정메타ID-PK):").append(lstSetupMetaId).append('\n'); }
		if(atrbId!=null) { if(tab!=null) builder.append(tab); builder.append("atrbId(속성ID-PK):").append(atrbId).append('\n'); }
		if(msgId!=null) { if(tab!=null) builder.append(tab); builder.append("msgId(메시지ID):").append(msgId).append('\n'); }
		if(sortOptVa!=null) { if(tab!=null) builder.append(tab); builder.append("sortOptVa(정렬옵션값):").append(sortOptVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}
}
