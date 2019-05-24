package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/10/01 15:03 ******/
/**
* 저장소문서관계(DM_STOR_DOC_R) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmStorDocRVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;

 	/** 문서그룹ID */ 
	private String docGrpId;

 	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorDocRDao.selectDmStorDocR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorDocRDao.insertDmStorDocR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorDocRDao.updateDmStorDocR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorDocRDao.deleteDmStorDocR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorDocRDao.countDmStorDocR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":저장소문서관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		super.toString(builder, tab);
	}

}
