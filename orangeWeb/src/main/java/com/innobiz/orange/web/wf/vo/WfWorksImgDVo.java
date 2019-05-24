package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/04/17 17:20 ******/
/**
* 생성테이블이미지(WF_0000_WORKS_IMG_D) 테이블 VO 
*/
public class WfWorksImgDVo extends WfCmWorksVo {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 4244917537255290220L;

	/** 이미지번호 */ 
	private String imgNo;
	
 	/** 컬럼명 */ 
	private String colmNm;

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
	
	/** 생성자 */
 	public WfWorksImgDVo() {
	}
 	
	public WfWorksImgDVo(String formNo) {
		super(formNo);
	}
	
	public void setImgNo(String imgNo) { 
		this.imgNo = imgNo;
	}
	/** 이미지번호 */ 
	public String getImgNo() { 
		return imgNo;
	}
	
	public void setColmNm(String colmNm) { 
		this.colmNm = colmNm;
	}
	/** 컬럼명 */ 
	public String getColmNm() { 
		return colmNm;
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
			return "com.innobiz.orange.web.wf.dao.WfWorksImgDDao.selectWfWorksImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksImgDDao.insertWfWorksImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksImgDDao.updateWfWorksImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksImgDDao.deleteWfWorksImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksImgDDao.countWfWorksImgD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":생성테이블이미지]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(imgNo!=null) { if(tab!=null) builder.append(tab); builder.append("imgNo(이미지번호):").append(imgNo).append('\n'); }		
		if(colmNm!=null) { if(tab!=null) builder.append(tab); builder.append("colmNm(컬럼명):").append(colmNm).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(가로길이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(세로길이):").append(imgHght).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
