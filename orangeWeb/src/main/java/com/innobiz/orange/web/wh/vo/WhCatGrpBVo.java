package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/18 14:50 ******/
/**
* 유형그룹기본(WH_CAT_GRP_B) 테이블 VO 
*/
public class WhCatGrpBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8041820978501272129L;

	/** 유형그룹ID */ 
	private String catGrpId;

 	/** 회사ID */ 
	private String compId;

 	/** 유형그룹명 */ 
	private String catGrpNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 사용여부 */ 
	private String useYn;

 	/** 기본여부 */ 
	private String dftYn;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setCatGrpId(String catGrpId) { 
		this.catGrpId = catGrpId;
	}
	/** 유형그룹ID */ 
	public String getCatGrpId() { 
		return catGrpId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCatGrpNm(String catGrpNm) { 
		this.catGrpNm = catGrpNm;
	}
	/** 유형그룹명 */ 
	public String getCatGrpNm() { 
		return catGrpNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setDftYn(String dftYn) { 
		this.dftYn = dftYn;
	}
	/** 기본여부 */ 
	public String getDftYn() { 
		return dftYn;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpBDao.selectWhCatGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpBDao.insertWhCatGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpBDao.updateWhCatGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpBDao.deleteWhCatGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpBDao.countWhCatGrpB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":유형그룹기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(catGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("catGrpId(유형그룹ID):").append(catGrpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(catGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("catGrpNm(유형그룹명):").append(catGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(dftYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftYn(기본여부):").append(dftYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
