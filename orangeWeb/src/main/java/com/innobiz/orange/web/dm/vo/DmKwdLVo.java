package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/17 11:05 ******/
/**
* 문서키워드목록(DM_KWD_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmKwdLVo extends DmCmmBVo {
 	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 키워드명 */ 
	private String kwdNm;

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setKwdNm(String kwdNm) { 
		this.kwdNm = kwdNm;
	}
	/** 키워드명 */ 
	public String getKwdNm() { 
		return kwdNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmKwdLDao.selectDmKwdL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmKwdLDao.insertDmKwdL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmKwdLDao.updateDmKwdL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmKwdLDao.deleteDmKwdL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmKwdLDao.countDmKwdL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서키워드목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(kwdNm!=null) { if(tab!=null) builder.append(tab); builder.append("kwdNm(키워드명):").append(kwdNm).append('\n'); }
		super.toString(builder, tab);
	}

}
