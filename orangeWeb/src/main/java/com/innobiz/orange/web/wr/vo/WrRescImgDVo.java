package com.innobiz.orange.web.wr.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 자원이미지상세(WR_RESC_IMG_D) 테이블 VO
 */
@SuppressWarnings("serial")
public class WrRescImgDVo extends CommonVoImpl {
	/** 자원관리ID */ 
	private String rescMngId;

 	/** 이미지경로 */ 
	private String imgPath;

 	/** 이미지넓이 */ 
	private Integer imgWdth;

 	/** 이미지높이 */ 
	private Integer imgHght;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setRescMngId(String rescMngId) { 
		this.rescMngId = rescMngId;
	}
	/** 자원관리ID */ 
	public String getRescMngId() { 
		return rescMngId;
	}

	public void setImgPath(String imgPath) { 
		this.imgPath = imgPath;
	}
	/** 이미지경로 */ 
	public String getImgPath() { 
		return imgPath;
	}

	public Integer getImgWdth() {
		return imgWdth;
	}
	public void setImgWdth(Integer imgWdth) {
		this.imgWdth = imgWdth;
	}
	public Integer getImgHght() {
		return imgHght;
	}
	public void setImgHght(Integer imgHght) {
		this.imgHght = imgHght;
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
			return "com.innobiz.orange.web.wr.dao.WrRescImgDDao.selectWrRescImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescImgDDao.insertWrRescImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescImgDDao.updateWrRescImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescImgDDao.deleteWrRescImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescImgDDao.countWrRescImgD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":자원이미지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(rescMngId!=null) { if(tab!=null) builder.append(tab); builder.append("rescMngId(자원관리ID):").append(rescMngId).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(이미지넓이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}
	
}