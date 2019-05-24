package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/07/08 11:53 ******/
/**
* 상신목록(DM_SUBM_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmSubmLVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;

 	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 상태코드 */ 
	private String statCd;

 	/** 반려의견 */ 
	private String rjtOpin;

 	/** 심의자UID */ 
	private String discrUid;

 	/** 심의일시 */ 
	private String discDt;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 심의자명 */ 
	private String discrNm;
	
	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 저장소ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}

	public void setRjtOpin(String rjtOpin) { 
		this.rjtOpin = rjtOpin;
	}
	/** 반려의견 */ 
	public String getRjtOpin() { 
		return rjtOpin;
	}

	public void setDiscrUid(String discrUid) { 
		this.discrUid = discrUid;
	}
	/** 심의자UID */ 
	public String getDiscrUid() { 
		return discrUid;
	}

	public void setDiscDt(String discDt) { 
		this.discDt = discDt;
	}
	/** 심의일시 */ 
	public String getDiscDt() { 
		return discDt;
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
	
	/** 등록자명 */ 
 	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 심의자명 */ 
	public String getDiscrNm() {
		return discrNm;
	}
	public void setDiscrNm(String discrNm) {
		this.discrNm = discrNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmSubmLDao.selectDmSubmL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmSubmLDao.insertDmSubmL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmSubmLDao.updateDmSubmL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmSubmLDao.deleteDmSubmL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmSubmLDao.countDmSubmL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":상신목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(rjtOpin!=null) { if(tab!=null) builder.append(tab); builder.append("rjtOpin(반려의견):").append(rjtOpin).append('\n'); }
		if(discrUid!=null) { if(tab!=null) builder.append(tab); builder.append("discrUid(심의자UID):").append(discrUid).append('\n'); }
		if(discDt!=null) { if(tab!=null) builder.append(tab); builder.append("discDt(심의일시):").append(discDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
