package com.innobiz.orange.web.st.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/03/14 16:00 ******/
/**
* 사이트(ST_SITE_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class StSiteBVo extends CommonVoImpl {
	/** 사이트ID */ 
	private String siteId;

 	/** 회사ID */ 
	private String compId;

 	/** 카테고리ID */ 
	private String catId;

 	/** 사이트명  */ 
	private String siteNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 내용 */ 
	private String cont;

 	/** 사이트URL */ 
	private String siteUrl;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 이미지ID */ 
	private String tmpImgId;
	
	/** 이미지 Vo */
	private StSiteImgDVo stSiteImgDVo;

 	public void setSiteId(String siteId) { 
		this.siteId = siteId;
	}
	/** 사이트ID */ 
	public String getSiteId() { 
		return siteId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCatId(String catId) { 
		this.catId = catId;
	}
	/** 카테고리ID */ 
	public String getCatId() { 
		return catId;
	}

	public void setSiteNm(String siteNm) { 
		this.siteNm = siteNm;
	}
	/** 사이트명  */ 
	public String getSiteNm() { 
		return siteNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}

	public void setSiteUrl(String siteUrl) { 
		this.siteUrl = siteUrl;
	}
	/** 사이트URL */ 
	public String getSiteUrl() { 
		return siteUrl;
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
	/** 등록자UID */ 
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
	/** 수정자UID */ 
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
	
	/** 이미지ID */ 
	public String getTmpImgId() {
		return tmpImgId;
	}
	public void setTmpImgId(String tmpImgId) {
		this.tmpImgId = tmpImgId;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteBDao.selectStSiteB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteBDao.insertStSiteB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteBDao.updateStSiteB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteBDao.deleteStSiteB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteBDao.countStSiteB";
		}
		return null;
	}
	
	/** 이미지 Vo */
	public StSiteImgDVo getStSiteImgDVo() {
		return stSiteImgDVo;
	}
	public void setStSiteImgDVo(StSiteImgDVo stSiteImgDVo) {
		this.stSiteImgDVo = stSiteImgDVo;
	}
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사이트]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(siteId!=null) { if(tab!=null) builder.append(tab); builder.append("siteId(사이트ID):").append(siteId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(카테고리ID):").append(catId).append('\n'); }
		if(siteNm!=null) { if(tab!=null) builder.append(tab); builder.append("siteNm(사이트명 ):").append(siteNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(siteUrl!=null) { if(tab!=null) builder.append(tab); builder.append("siteUrl(사이트URL):").append(siteUrl).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
