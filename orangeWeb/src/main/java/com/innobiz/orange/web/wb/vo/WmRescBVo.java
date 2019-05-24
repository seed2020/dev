package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 리소스기본
 */
@SuppressWarnings("serial")
public class WmRescBVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;
	
	/** 리소스ID */ 
	private String rescId;

 	/** 언어구분코드 */ 
	private String langTypCd;

 	/** 리소스값 */ 
	private String rescVa;
	
 	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setLangTypCd(String langTypCd) { 
		this.langTypCd = langTypCd;
	}
	/** 언어구분코드 */ 
	public String getLangTypCd() { 
		return langTypCd;
	}

	public void setRescVa(String rescVa) { 
		this.rescVa = rescVa;
	}
	/** 리소스값 */ 
	public String getRescVa() { 
		return rescVa;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WmRescBDao.selectWmRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WmRescBDao.insertWmRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WmRescBDao.updateWmRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WmRescBDao.deleteWmRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WmRescBDao.countWmRescB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":리소스기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		super.toString(builder, tab);
	}
}