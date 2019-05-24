package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 명함친밀도코드 */
@SuppressWarnings("serial")
public class WbBcClnsCVo extends CommonVoImpl {
	/** 등록자UID */ 
	private String regrUid;

 	/** 친밀도명 */ 
	private String clnsNm;

 	/** 친밀도ID */ 
	private String clnsId;

 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setClnsNm(String clnsNm) { 
		this.clnsNm = clnsNm;
	}
	/** 친밀도명 */ 
	public String getClnsNm() { 
		return clnsNm;
	}

	public void setClnsId(String clnsId) { 
		this.clnsId = clnsId;
	}
	/** 친밀도ID */ 
	public String getClnsId() { 
		return clnsId;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcClnsCDao.selectWbBcClnsC";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcClnsCDao.insertWbBcClnsC";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcClnsCDao.updateWbBcClnsC";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcClnsCDao.deleteWbBcClnsC";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcClnsCDao.countWbBcClnsC";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함친밀도코드]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(clnsNm!=null) { if(tab!=null) builder.append(tab); builder.append("clnsNm(친밀도명):").append(clnsNm).append('\n'); }
		if(clnsId!=null) { if(tab!=null) builder.append(tab); builder.append("clnsId(친밀도ID):").append(clnsId).append('\n'); }
		super.toString(builder, tab);
	}
}