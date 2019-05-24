package com.innobiz.orange.web.st.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/03/14 16:00 ******/
/**
* 카테고리(ST_CAT_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class StCatBVo extends CommonVoImpl {
	/** 카테고리ID */ 
	private String catId;

 	/** 회사ID */ 
	private String compId;

 	/** 카테고리명 */ 
	private String catNm;

 	/** 리소스ID */ 
	private String rescId;

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

 	public void setCatId(String catId) { 
		this.catId = catId;
	}
	/** 카테고리ID */ 
	public String getCatId() { 
		return catId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCatNm(String catNm) { 
		this.catNm = catNm;
	}
	/** 카테고리명 */ 
	public String getCatNm() { 
		return catNm;
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
			return "com.innobiz.orange.web.st.dao.StCatBDao.selectStCatB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.st.dao.StCatBDao.insertStCatB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.st.dao.StCatBDao.updateStCatB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.st.dao.StCatBDao.deleteStCatB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.st.dao.StCatBDao.countStCatB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":카테고리]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(카테고리ID):").append(catId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(카테고리명):").append(catNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
