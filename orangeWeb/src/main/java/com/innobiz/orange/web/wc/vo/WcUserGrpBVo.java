package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/02/23 14:55 ******/
/**
* 사용자그룹(WC_USER_GRP_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WcUserGrpBVo extends CommonVoImpl {
	/** 사용자그룹ID */ 
	private String userGrpId;

 	/** 회사ID */ 
	private String compId;

 	/** 사용자그룹명 */ 
	private String userGrpNm;

 	/** 리소스ID */ 
	private String rescId;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;
	
 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 리소스명 */ 
	private String rescNm;

 	public void setUserGrpId(String userGrpId) { 
		this.userGrpId = userGrpId;
	}
	/** 사용자그룹ID */ 
	public String getUserGrpId() { 
		return userGrpId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setUserGrpNm(String userGrpNm) { 
		this.userGrpNm = userGrpNm;
	}
	/** 사용자그룹명 */ 
	public String getUserGrpNm() { 
		return userGrpNm;
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
	
	public void setModrUid(String modrUid) { 
		this.modrUid = modrUid;
	}
	/** 수정자 */ 
	public String getModrUid() { 
		return modrUid;
	}

	public void setModDt(String modDt) { 
		this.modDt = modDt;
	}
	/** 수정일시 */ 
	public String getModDt() { 
		return modDt;
	}
	
	/** 리소스명 */ 
	public String getRescNm() {
		return rescNm;
	}
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpBDao.selectWcUserGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpBDao.insertWcUserGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpBDao.updateWcUserGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpBDao.deleteWcUserGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpBDao.countWcUserGrpB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자그룹]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("userGrpId(사용자그룹ID):").append(userGrpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("userGrpNm(사용자그룹명):").append(userGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
