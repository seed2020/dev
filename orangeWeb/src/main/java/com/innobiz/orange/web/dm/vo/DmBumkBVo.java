package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/22 10:06 ******/
/**
* 즐겨찾기기본(DM_BUMK_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmBumkBVo extends CommonVoImpl {
 	/** 즐겨찾기ID */ 
	private String bumkId;

 	/** 즐겨찾기명 */ 
	private String bumkNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 즐겨찾기유형 - 개인,부서,회사[P,D,C] */ 
	private String bumkCat;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

	public void setBumkId(String bumkId) { 
		this.bumkId = bumkId;
	}
	/** 즐겨찾기ID */ 
	public String getBumkId() { 
		return bumkId;
	}

	public void setBumkNm(String bumkNm) { 
		this.bumkNm = bumkNm;
	}
	/** 즐겨찾기명 */ 
	public String getBumkNm() { 
		return bumkNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setBumkCat(String bumkCat) { 
		this.bumkCat = bumkCat;
	}
	/** 즐겨찾기유형 */ 
	public String getBumkCat() { 
		return bumkCat;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자 */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkBDao.selectDmBumkB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkBDao.insertDmBumkB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkBDao.updateDmBumkB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkBDao.deleteDmBumkB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkBDao.countDmBumkB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":즐겨찾기기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(bumkId!=null) { if(tab!=null) builder.append(tab); builder.append("bumkId(즐겨찾기ID):").append(bumkId).append('\n'); }
		if(bumkNm!=null) { if(tab!=null) builder.append(tab); builder.append("bumkNm(즐겨찾기명):").append(bumkNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(bumkCat!=null) { if(tab!=null) builder.append(tab); builder.append("bumkCat(즐겨찾기유형):").append(bumkCat).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
