package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 명함즐겨찾기관계(WB_BC_BUMK_R) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcBumkRVo extends CommonVoImpl {
	/** 등록자UID */ 
	private String regrUid;

 	/** 명함ID */ 
	private String bcId;

 	/** 정렬순서 */ 
	private String sortOrdr;
	
	/** 즐겨찾기여부 */ 
	private String schBumkYn;
	
 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public void setSortOrdr(String sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public String getSortOrdr() { 
		return sortOrdr;
	}
	
	public String getSchBumkYn() {
		return schBumkYn;
	}
	public void setSchBumkYn(String schBumkYn) {
		this.schBumkYn = schBumkYn;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBumkRDao.selectWbBcBumkR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBumkRDao.insertWbBcBumkR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBumkRDao.updateWbBcBumkR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBumkRDao.deleteWbBcBumkR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBumkRDao.countWbBcBumkR";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함즐겨찾기관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(schBumkYn!=null) { if(tab!=null) builder.append(tab); builder.append("schBumkYn(즐겨찾기여부):").append(schBumkYn).append('\n'); }
		super.toString(builder, tab);
	}
	
}