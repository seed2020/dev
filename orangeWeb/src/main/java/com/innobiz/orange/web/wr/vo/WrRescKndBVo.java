package com.innobiz.orange.web.wr.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 자원종류기본(WR_RESC_KND_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WrRescKndBVo extends CommonVoImpl {
	/** 자원분류ID */ 
	private String rescKndId;

 	/** 회사ID */ 
	private String compId;

 	/** 리소스ID */ 
	private String rescId;

 	/** 분류명 */ 
	private String kndNm;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 배경색코드 */ 
	private String bgcolCd;
	
	/** 사용여부 */ 
	private String useYn;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;

/** 추가 */	
	/** 등록자명*/
	private String regrNm;
	
 	public void setRescKndId(String rescKndId) { 
		this.rescKndId = rescKndId;
	}
	/** 자원분류ID */ 
	public String getRescKndId() { 
		return rescKndId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setKndNm(String kndNm) { 
		this.kndNm = kndNm;
	}
	/** 분류명 */ 
	public String getKndNm() { 
		return kndNm;
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
	
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	public String getBgcolCd() {
		return bgcolCd;
	}
	public void setBgcolCd(String bgcolCd) {
		this.bgcolCd = bgcolCd;
	}
	
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	/** 정렬순서 */ 
	public Integer getSortOrdr() {
		return sortOrdr;
	}
	public void setSortOrdr(Integer sortOrdr) {
		this.sortOrdr = sortOrdr;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescKndBDao.selectWrRescKndB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescKndBDao.insertWrRescKndB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescKndBDao.updateWrRescKndB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescKndBDao.deleteWrRescKndB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRescKndBDao.countWrRescKndB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":자원종류기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(rescKndId!=null) { if(tab!=null) builder.append(tab); builder.append("rescKndId(자원분류ID):").append(rescKndId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(kndNm!=null) { if(tab!=null) builder.append(tab); builder.append("kndNm(분류명):").append(kndNm).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(bgcolCd!=null) { if(tab!=null) builder.append(tab); builder.append("bgcolCd(배경색코드):").append(bgcolCd).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		super.toString(builder, tab);
	}
	
}