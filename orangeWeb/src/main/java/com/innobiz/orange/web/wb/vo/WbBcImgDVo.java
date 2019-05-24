package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 명함이미지상세(WM_WB_IMG_D) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcImgDVo extends CommonVoImpl {
	/** 명함ID */ 
	private String bcId;

 	/** 이미지경로 */ 
	private String imgPath;

 	/** 이미지넓이 */ 
	private String imgWdth;

 	/** 이미지높이 */ 
	private String imgHght;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public void setImgPath(String imgPath) { 
		this.imgPath = imgPath;
	}
	/** 이미지경로 */ 
	public String getImgPath() { 
		return imgPath;
	}

	public void setImgWdth(String imgWdth) { 
		this.imgWdth = imgWdth;
	}
	/** 이미지넓이 */ 
	public String getImgWdth() { 
		return imgWdth;
	}

	public void setImgHght(String imgHght) { 
		this.imgHght = imgHght;
	}
	/** 이미지높이 */ 
	public String getImgHght() { 
		return imgHght;
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
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcImgDDao.selectWbBcImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcImgDDao.insertWbBcImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcImgDDao.updateWbBcImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcImgDDao.deleteWbBcImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcImgDDao.countWbBcImgD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함이미지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(이미지넓이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}
	
}