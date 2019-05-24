package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/12/21 13:41 ******/
/**
* 국가설정(WC_NAT_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WcNatBVo  extends CommonVoImpl {	
	/** 회사ID */ 
	private String compId;

 	/** 코드 */ 
	private String cd;

 	/** 리소스ID */ 
	private String rescId;

 	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 리소스명 */ 
	private String rescNm;
	
	/** 사용자UID */ 
	private String userUid;
	
	/** 기본여부 */ 
	private String dftYn;
	
 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCd(String cd) { 
		this.cd = cd;
	}
	/** 코드 */ 
	public String getCd() { 
		return cd;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 리소스명 */ 
	public String getRescNm() {
		return rescNm;
	}
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}
	
	/** 사용자UID */ 
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 기본여부 */ 
	public String getDftYn() {
		return dftYn;
	}
	public void setDftYn(String dftYn) {
		this.dftYn = dftYn;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcNatBDao.selectWcNatB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcNatBDao.insertWcNatB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcNatBDao.updateWcNatB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcNatBDao.deleteWcNatB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcNatBDao.countWcNatB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":국가설정]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(cd!=null) { if(tab!=null) builder.append(tab); builder.append("cd(코드):").append(cd).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
