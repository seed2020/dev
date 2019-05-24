package com.innobiz.orange.web.bb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 코드그룹(BA_CD_GRP_B) 테이블 VO */
@SuppressWarnings("serial")
public class BaCdGrpBVo extends CommonVoImpl {
	/** 코드그룹ID */ 
	private String cdGrpId;

 	/** 회사ID */ 
	private String compId;

 	/** 코드그룹명 */ 
	private String cdGrpNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 사용여부 */ 
	private String grpUseYn;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 코드 상세 목록 */
	private List<BaCdDVo> baCdDVoList;
	
	/** 리소스명 */
	private String rescNm;
	
	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}
	public void setBaCdDVoList(List<BaCdDVo> baCdDVoList) {
		this.baCdDVoList = baCdDVoList;
	}
	/** 코드 상세 목록 */
	public List<BaCdDVo> getBaCdDVoList() {
		return baCdDVoList;
	}
	
	public void setCdGrpId(String cdGrpId) { 
		this.cdGrpId = cdGrpId;
	}
	/** 코드그룹ID */ 
	public String getCdGrpId() { 
		return cdGrpId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCdGrpNm(String cdGrpNm) { 
		this.cdGrpNm = cdGrpNm;
	}
	/** 코드그룹명 */ 
	public String getCdGrpNm() { 
		return cdGrpNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setGrpUseYn(String grpUseYn) { 
		this.grpUseYn = grpUseYn;
	}
	/** 사용여부 */ 
	public String getGrpUseYn() { 
		return grpUseYn;
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
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdGrpBDao.selectBaCdGrpB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdGrpBDao.insertBaCdGrpB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdGrpBDao.updateBaCdGrpB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdGrpBDao.deleteBaCdGrpB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdGrpBDao.countBaCdGrpB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":코드그룹기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(cdGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("cdGrpId(코드그룹ID):").append(cdGrpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(cdGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("cdGrpNm(코드그룹명):").append(cdGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(grpUseYn!=null) { if(tab!=null) builder.append(tab); builder.append("grpUseYn(사용여부):").append(grpUseYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
