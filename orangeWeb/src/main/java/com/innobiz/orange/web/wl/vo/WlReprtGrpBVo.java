package com.innobiz.orange.web.wl.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/22 17:04 ******/
/**
* 보고그룹기본(WL_REPRT_GRP_B) 테이블 VO 
*/
public class WlReprtGrpBVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = 812855524374814147L;

	/** 그룹번호 */ 
	private String grpNo;

 	/** 회사ID */ 
	private String compId;

 	/** 그룹명 */ 
	private String grpNm;

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
	
	/** 그룹번호 리스트 */ 
	private List<String> grpNoList;
	
	/** 추가 */
	/** 등록자명 */ 
	private String regrNm;

 	public void setGrpNo(String grpNo) { 
		this.grpNo = grpNo;
	}
	/** 그룹번호 */ 
	public String getGrpNo() { 
		return grpNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setGrpNm(String grpNm) { 
		this.grpNm = grpNm;
	}
	/** 그룹명 */ 
	public String getGrpNm() { 
		return grpNm;
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
	
	/** 그룹번호 리스트 */ 
	public List<String> getGrpNoList() {
		return grpNoList;
	}
	public void setGrpNoList(List<String> grpNoList) {
		this.grpNoList = grpNoList;
	}
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpBDao.selectWlReprtGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpBDao.insertWlReprtGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpBDao.updateWlReprtGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpBDao.deleteWlReprtGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpBDao.countWlReprtGrpB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":보고그룹기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(grpNo!=null) { if(tab!=null) builder.append(tab); builder.append("grpNo(그룹번호):").append(grpNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(grpNm!=null) { if(tab!=null) builder.append(tab); builder.append("grpNm(그룹명):").append(grpNm).append('\n'); }
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
