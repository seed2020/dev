package com.innobiz.orange.web.dm.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/12 10:11 ******/
/**
* 폴더기본(DM_FLD_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmFldBVo extends DmCmmBVo {
 	/** 폴더ID */ 
	private String fldId;

 	/** 폴더그룹ID */ 
	private String fldGrpId;

 	/** 유형ID */ 
	private String catId;

 	/** 폴더명 */ 
	private String fldNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 상위폴더ID */ 
	private String fldPid;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 폴더구분코드 */ 
	private String fldTypCd;

 	/** 사용여부 */ 
	private String useYn;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 선택ID 목록 */ 
	private List<String> selIdList;
	
	/** 유형명 */ 
	private String catNm;
	
	/** 생성자 */ 
	public DmFldBVo() {
		super();
	}
	
	/** 생성자 */ 
	public DmFldBVo(String storId) {
		super(storId);
	}

	/** 폴더ID 목록 */ 
	public List<String> getSelIdList() {
		return selIdList;
	}
	public void setSelIdList(List<String> selIdList) {
		this.selIdList = selIdList;
	}
	public void setFldId(String fldId) { 
		this.fldId = fldId;
	}
	/** 폴더ID */ 
	public String getFldId() { 
		return fldId;
	}

	public void setFldGrpId(String fldGrpId) { 
		this.fldGrpId = fldGrpId;
	}
	/** 폴더그룹ID */ 
	public String getFldGrpId() { 
		return fldGrpId;
	}

	public void setCatId(String catId) { 
		this.catId = catId;
	}
	/** 유형ID */ 
	public String getCatId() { 
		return catId;
	}

	public void setFldNm(String fldNm) { 
		this.fldNm = fldNm;
	}
	/** 폴더명 */ 
	public String getFldNm() { 
		return fldNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setFldPid(String fldPid) { 
		this.fldPid = fldPid;
	}
	/** 상위폴더ID */ 
	public String getFldPid() { 
		return fldPid;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}

	public void setFldTypCd(String fldTypCd) { 
		this.fldTypCd = fldTypCd;
	}
	/** 폴더구분코드 */ 
	public String getFldTypCd() { 
		return fldTypCd;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
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
	
	/** 유형명 */ 
	public String getCatNm() {
		return catNm;
	}

	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmFldBDao.selectDmFldB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmFldBDao.insertDmFldB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmFldBDao.updateDmFldB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmFldBDao.deleteDmFldB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmFldBDao.countDmFldB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":폴더기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) {
		if(fldId!=null) { if(tab!=null) builder.append(tab); builder.append("fldId(폴더ID):").append(fldId).append('\n'); }
		if(fldGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("fldGrpId(폴더그룹ID):").append(fldGrpId).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(유형ID):").append(catId).append('\n'); }
		if(fldNm!=null) { if(tab!=null) builder.append(tab); builder.append("fldNm(폴더명):").append(fldNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(fldPid!=null) { if(tab!=null) builder.append(tab); builder.append("fldPid(상위폴더ID):").append(fldPid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(fldTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("fldTypCd(폴더구분코드):").append(fldTypCd).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
