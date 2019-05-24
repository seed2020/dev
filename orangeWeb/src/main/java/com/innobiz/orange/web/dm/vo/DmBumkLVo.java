package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/22 10:06 ******/
/**
* 즐겨찾기목록(DM_BUMK_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmBumkLVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;

 	/** 즐겨찾기ID */ 
	private String bumkId;

 	/** 등록유형 */ 
	private String regCat;

 	/** 유형값 */ 
	private String catVa;

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
	
 	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setBumkId(String bumkId) { 
		this.bumkId = bumkId;
	}
	/** 즐겨찾기ID */ 
	public String getBumkId() { 
		return bumkId;
	}

	public void setRegCat(String regCat) { 
		this.regCat = regCat;
	}
	/** 등록유형 */ 
	public String getRegCat() { 
		return regCat;
	}

	public void setCatVa(String catVa) { 
		this.catVa = catVa;
	}
	/** 유형값 */ 
	public String getCatVa() { 
		return catVa;
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
			return "com.innobiz.orange.web.dm.dao.DmBumkLDao.selectDmBumkL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkLDao.insertDmBumkL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkLDao.updateDmBumkL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkLDao.deleteDmBumkL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmBumkLDao.countDmBumkL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":즐겨찾기목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(bumkId!=null) { if(tab!=null) builder.append(tab); builder.append("bumkId(즐겨찾기ID):").append(bumkId).append('\n'); }
		if(regCat!=null) { if(tab!=null) builder.append(tab); builder.append("regCat(등록유형):").append(regCat).append('\n'); }
		if(catVa!=null) { if(tab!=null) builder.append(tab); builder.append("catVa(유형값):").append(catVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
