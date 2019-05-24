package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/26 10:32 ******/
/**
* 문서버전목록(DM_DOC_VER_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmDocVerLVo extends DmCmmBVo {
 	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 문서ID */ 
	private String docId;

 	/** 버전값 */ 
	private String verVa;

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setDocId(String docId) { 
		this.docId = docId;
	}
	/** 문서ID */ 
	public String getDocId() { 
		return docId;
	}

	public void setVerVa(String verVa) { 
		this.verVa = verVa;
	}
	/** 버전값 */ 
	public String getVerVa() { 
		return verVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocVerLDao.selectDmDocVerL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocVerLDao.insertDmDocVerL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocVerLDao.updateDmDocVerL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocVerLDao.deleteDmDocVerL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocVerLDao.countDmDocVerL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서버전목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(docId!=null) { if(tab!=null) builder.append(tab); builder.append("docId(문서ID):").append(docId).append('\n'); }
		if(verVa!=null) { if(tab!=null) builder.append(tab); builder.append("verVa(버전값):").append(verVa).append('\n'); }
		super.toString(builder, tab);
	}

}
