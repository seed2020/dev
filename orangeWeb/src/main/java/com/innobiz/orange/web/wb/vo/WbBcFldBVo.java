package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/** 명함폴더기본 */
@SuppressWarnings("serial")
public class WbBcFldBVo extends WbPubFldBVo {
	/** 등록자UID */ 
	private String regrUid;

 	/** 명함폴더ID */ 
	private String bcFldId;

 	/** 폴더명 */ 
	private String fldNm;

 	/** 상위폴더ID */ 
	private String abvFldId;

 	/** 정렬순서 */ 
	private String sortOrdr;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 회사ID */
	private String compId;
	
	/** 추가 */
	/** 상위폴더명 */ 
	private String abvFldNm;
	
	/** 공용명함여부 */ 
	private boolean isPub = false;
	
 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setBcFldId(String bcFldId) { 
		this.bcFldId = bcFldId;
	}
	/** 명함폴더ID */ 
	public String getBcFldId() { 
		return bcFldId;
	}

	public void setFldNm(String fldNm) { 
		this.fldNm = fldNm;
	}
	/** 폴더명 */ 
	public String getFldNm() { 
		return fldNm;
	}

	public void setAbvFldId(String abvFldId) { 
		this.abvFldId = abvFldId;
	}
	/** 상위폴더ID */ 
	public String getAbvFldId() { 
		return abvFldId;
	}

	public void setSortOrdr(String sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public String getSortOrdr() { 
		return sortOrdr;
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
	
	public String getAbvFldNm() {
		return abvFldNm;
	}
	public void setAbvFldNm(String abvFldNm) {
		this.abvFldNm = abvFldNm;
	}
	
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 공용명함여부 */ 
	public boolean isPub() {
		return isPub;
	}
	public void setPub(boolean isPub) {
		this.isPub = isPub;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcFldBDao.selectWbBcFldB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcFldBDao.insertWbBcFldB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcFldBDao.updateWbBcFldB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcFldBDao.deleteWbBcFldB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcFldBDao.countWbBcFldB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함폴더기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(bcFldId!=null) { if(tab!=null) builder.append(tab); builder.append("bcFldId(명함폴더ID):").append(bcFldId).append('\n'); }
		if(fldNm!=null) { if(tab!=null) builder.append(tab); builder.append("fldNm(폴더명):").append(fldNm).append('\n'); }
		if(abvFldId!=null) { if(tab!=null) builder.append(tab); builder.append("abvFldId(상위폴더ID):").append(abvFldId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(abvFldNm!=null) { if(tab!=null) builder.append(tab); builder.append("abvFldNm(상위폴더명):").append(abvFldNm).append('\n'); }
		super.toString(builder, tab);
	}
	
}