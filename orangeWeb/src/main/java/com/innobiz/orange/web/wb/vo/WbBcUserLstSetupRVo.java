package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함사용자목록설정관계(WB_BC_USER_LST_SETUP_R) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcUserLstSetupRVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;
	
	/** 등록자UID */ 
	private String regrUid;

 	/** 속성ID */ 
	private String atrbId;

 	/** 정렬순서 */ 
	private String sortOrdr;
	
	/** 메시지ID */ 
	private String msgId;
	
	/** 줄마춤값 */ 
	private String alnVa;
	
	/** 넓이퍼센트 */ 
	private String wdthPerc;
	
	/** 표시여부 */ 
	private String dispYn;
	
 	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setAtrbId(String atrbId) { 
		this.atrbId = atrbId;
	}
	/** 속성ID */ 
	public String getAtrbId() { 
		return atrbId;
	}

	public void setSortOrdr(String sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public String getSortOrdr() { 
		return sortOrdr;
	}
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	public String getAlnVa() {
		return alnVa;
	}
	public void setAlnVa(String alnVa) {
		this.alnVa = alnVa;
	}
	public String getWdthPerc() {
		return wdthPerc;
	}
	public void setWdthPerc(String wdthPerc) {
		this.wdthPerc = wdthPerc;
	}
	public String getDispYn() {
		return dispYn;
	}
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserLstSetupRDao.selectWbBcUserLstSetupR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserLstSetupRDao.insertWbBcUserLstSetupR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserLstSetupRDao.updateWbBcUserLstSetupR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserLstSetupRDao.deleteWbBcUserLstSetupR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserLstSetupRDao.countWbBcUserLstSetupR";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함사용자화면설정관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(atrbId!=null) { if(tab!=null) builder.append(tab); builder.append("atrbId(속성ID):").append(atrbId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(msgId!=null) { if(tab!=null) builder.append(tab); builder.append("msgId(메시지ID):").append(msgId).append('\n'); }
		if(alnVa!=null) { if(tab!=null) builder.append(tab); builder.append("alnVa(줄마춤값):").append(alnVa).append('\n'); }
		if(wdthPerc!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPerc(넓이퍼센트):").append(wdthPerc).append('\n'); }
		if(dispYn!=null) { if(tab!=null) builder.append(tab); builder.append("dispYn(표시여부):").append(dispYn).append('\n'); }
		super.toString(builder, tab);
	}
}