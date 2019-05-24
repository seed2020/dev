package com.innobiz.orange.web.st.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/03/14 16:00 ******/
/**
* 이미지(ST_SITE_IMG_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class StSiteImgDVo extends CommonVoImpl {
	/** 사이트ID */ 
	private String siteId;

 	/** 이미지경로 */ 
	private String imgPath;

 	/** 가로길이 */ 
	private Integer imgWdth;

 	/** 세로길이 */ 
	private Integer imgHght;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setSiteId(String siteId) { 
		this.siteId = siteId;
	}
	/** 사이트ID */ 
	public String getSiteId() { 
		return siteId;
	}

	public void setImgPath(String imgPath) { 
		this.imgPath = imgPath;
	}
	/** 이미지경로 */ 
	public String getImgPath() { 
		return imgPath;
	}

	public void setImgWdth(Integer imgWdth) { 
		this.imgWdth = imgWdth;
	}
	/** 가로길이 */ 
	public Integer getImgWdth() { 
		return imgWdth;
	}

	public void setImgHght(Integer imgHght) { 
		this.imgHght = imgHght;
	}
	/** 세로길이 */ 
	public Integer getImgHght() { 
		return imgHght;
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
			return "com.innobiz.orange.web.st.dao.StSiteImgDDao.selectStSiteImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteImgDDao.insertStSiteImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteImgDDao.updateStSiteImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteImgDDao.deleteStSiteImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.st.dao.StSiteImgDDao.countStSiteImgD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이미지]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(siteId!=null) { if(tab!=null) builder.append(tab); builder.append("siteId(사이트ID):").append(siteId).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(가로길이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(세로길이):").append(imgHght).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
