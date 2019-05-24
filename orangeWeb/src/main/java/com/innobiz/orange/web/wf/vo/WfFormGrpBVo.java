package com.innobiz.orange.web.wf.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/26 16:00 ******/
/**
* 양식그룹(WF_FORM_GRP_B) 테이블 VO 
*/
public class WfFormGrpBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1122641514736079261L;

	/** 그룹ID */ 
	private String grpId;

 	/** 회사ID */ 
	private String compId;

 	/** 그룹명 */ 
	private String grpNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 상위그룹ID */ 
	private String grpPid;

 	/** 사용여부 */ 
	private String useYn;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가 */
	/** 그룹ID 목록 */ 
	private List<String> grpIdList;
	
	/** 회사ID목록 */
	private List<String> compIdList;
	
	/** 변경할 회사ID */ 
	private String paramCompId;
	
	/** 그룹구분 */ 
	private String grpTyp="F"; // 기본은 폴더로 지정
	
	/** 생성ID[양식에서 사용] */ 
	private String genId;

 	public void setGrpId(String grpId) { 
		this.grpId = grpId;
	}
	/** 그룹ID */ 
	public String getGrpId() { 
		return grpId;
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

	public void setGrpPid(String grpPid) { 
		this.grpPid = grpPid;
	}
	/** 상위그룹ID */ 
	public String getGrpPid() { 
		return grpPid;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
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
	
	/** 그룹ID 목록 */ 
	public List<String> getGrpIdList() {
		return grpIdList;
	}
	public void setGrpIdList(List<String> grpIdList) {
		this.grpIdList = grpIdList;
	}
	
	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}
	
	/** 변경할 회사ID */ 
	public String getParamCompId() {
		return paramCompId;
	}
	public void setParamCompId(String paramCompId) {
		this.paramCompId = paramCompId;
	}
	
	/** 생성ID[양식에서 사용] */ 
	public String getGenId() {
		return genId;
	}
	public void setGenId(String genId) {
		this.genId = genId;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGrpBDao.selectWfFormGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGrpBDao.insertWfFormGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGrpBDao.updateWfFormGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGrpBDao.deleteWfFormGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGrpBDao.countWfFormGrpB";
		}
		return null;
	}
	
	/** 그룹구분 */ 
	public String getGrpTyp() {
		return grpTyp;
	}
	public void setGrpTyp(String grpTyp) {
		this.grpTyp = grpTyp;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식그룹]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID):").append(grpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(grpNm!=null) { if(tab!=null) builder.append(tab); builder.append("grpNm(그룹명):").append(grpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(grpPid!=null) { if(tab!=null) builder.append(tab); builder.append("grpPid(상위그룹ID):").append(grpPid).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
