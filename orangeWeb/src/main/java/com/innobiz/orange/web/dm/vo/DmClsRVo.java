package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/15 14:13 ******/
/**
* 분류체계그룹관계(DM_CLS_R) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmClsRVo extends DmCmmBVo {
 	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 분류체계ID */ 
	private String clsId;
	
	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setClsId(String clsId) { 
		this.clsId = clsId;
	}
	/** 분류체계ID */ 
	public String getClsId() { 
		return clsId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsRDao.selectDmClsR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsRDao.insertDmClsR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsRDao.updateDmClsR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsRDao.deleteDmClsR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsRDao.countDmClsR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":분류체계그룹관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(clsId!=null) { if(tab!=null) builder.append(tab); builder.append("clsId(분류체계ID):").append(clsId).append('\n'); }
		super.toString(builder, tab);
	}

}
