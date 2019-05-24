package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/26 16:39 ******/
/**
* 리소스기본(WF_RESC_B) 테이블 VO 
*/
public class WfRescBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8434618676488727684L;

	/** 리소스ID */ 
	private String rescId;

 	/** 언어구분코드 */ 
	private String langTypCd;

 	/** 리소스값 */ 
	private String rescVa;

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
			return "com.innobiz.orange.web.wf.dao.WfRescBDao.selectWfRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfRescBDao.insertWfRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfRescBDao.updateWfRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfRescBDao.deleteWfRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfRescBDao.countWfRescB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":리소스기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		super.toString(builder, tab);
	}

}
