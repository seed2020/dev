package com.innobiz.orange.web.wf.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/26 16:00 ******/
/**
* 양식기본(WF_FORM_B) 테이블 VO 
*/
public class WfFormBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8949413359569110980L;

	/** 양식번호 */ 
	private String formNo;

 	/** 회사ID */ 
	private String compId;
	
	/** 양식ID */ 
	private String formId;
	
	/** 그룹ID */ 
	private String grpId;
	
 	/** 양식명 */ 
	private String formNm;

 	/** 설명 */ 
	private String formDesc;

 	/** 리소스ID */ 
	private String rescId;

 	/** 사용여부 */ 
	private String useYn;
	
	/** 양식구분 */ 
	private String formTyp;
	
	/** 등록여부 */ 
	private String regYn;

 	/** 목록여부 */ 
	private String lstYn;
	
	/** 모듈구분코드 */ 
	private String mdTypCd;

 	/** 전사여부 */ 
	private String allCompYn;
	
	/** 목록구분코드 */ 
	private String lstTypCd;
	
	/** 생성ID */ 
	private String genId;
	
	/** 상태코드 */ 
	private String statCd;
	
	/** 배포일시 */ 
	private String deployDt;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가 */
	/** 등록자명 */ 
	private String regrNm;
	
	/** 그룹ID 목록 */ 
	private List<String> grpIdList;
	
	/** SQL일련번호 */
	private Integer sqlSeq;
	
	/** 컬럼목록 */
	private List<WfFormColmLVo> colmVoList;

 	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}
	
	public void setFormId(String formId) { 
		this.formId = formId;
	}
	/** 양식ID */ 
	public String getFormId() { 
		return formId;
	}
	
	public void setGrpId(String grpId) { 
		this.grpId = grpId;
	}
	/** 그룹ID */ 
	public String getGrpId() { 
		return grpId;
	}
	
	public void setFormNm(String formNm) { 
		this.formNm = formNm;
	}
	/** 양식명 */ 
	public String getFormNm() { 
		return formNm;
	}

	public void setFormDesc(String formDesc) { 
		this.formDesc = formDesc;
	}
	/** 설명 */ 
	public String getFormDesc() { 
		return formDesc;
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
	
	public void setFormTyp(String formTyp) { 
		this.formTyp = formTyp;
	}
	/** 양식구분 */ 
	public String getFormTyp() { 
		return formTyp;
	}
	
	public void setRegYn(String regYn) { 
		this.regYn = regYn;
	}
	/** 등록여부 */ 
	public String getRegYn() { 
		return regYn;
	}

	public void setLstYn(String lstYn) { 
		this.lstYn = lstYn;
	}
	/** 목록여부 */ 
	public String getLstYn() { 
		return lstYn;
	}
	
	public void setMdTypCd(String mdTypCd) { 
		this.mdTypCd = mdTypCd;
	}
	/** 모듈구분코드 */ 
	public String getMdTypCd() { 
		return mdTypCd;
	}

	public void setAllCompYn(String allCompYn) { 
		this.allCompYn = allCompYn;
	}
	/** 전사여부 */ 
	public String getAllCompYn() { 
		return allCompYn;
	}
	
	public void setLstTypCd(String lstTypCd) { 
		this.lstTypCd = lstTypCd;
	}
	/** 목록구분코드 */ 
	public String getLstTypCd() { 
		return lstTypCd;
	}
	
	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}
	
	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}
	
	public void setDeployDt(String deployDt) { 
		this.deployDt = deployDt;
	}
	/** 배포일시 */ 
	public String getDeployDt() { 
		return deployDt;
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
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 그룹ID 목록 */ 
	public List<String> getGrpIdList() {
		return grpIdList;
	}
	public void setGrpIdList(List<String> grpIdList) {
		this.grpIdList = grpIdList;
	}
	
	/** SQL일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	
	/** 컬럼목록 */
	public List<WfFormColmLVo> getColmLVoList() {
		return colmVoList;
	}
	public void setColmLVoList(List<WfFormColmLVo> colmVoList) {
		this.colmVoList = colmVoList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormBDao.selectWfFormB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormBDao.insertWfFormB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormBDao.updateWfFormB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormBDao.deleteWfFormB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormBDao.countWfFormB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID):").append(formId).append('\n'); }		
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID):").append(grpId).append('\n'); }		
		if(formNm!=null) { if(tab!=null) builder.append(tab); builder.append("formNm(양식명):").append(formNm).append('\n'); }
		if(formDesc!=null) { if(tab!=null) builder.append(tab); builder.append("formDesc(설명):").append(formDesc).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(formTyp!=null) { if(tab!=null) builder.append(tab); builder.append("formTyp(양식구분):").append(formTyp).append('\n'); }
		if(regYn!=null) { if(tab!=null) builder.append(tab); builder.append("regYn(등록여부):").append(regYn).append('\n'); }
		if(lstYn!=null) { if(tab!=null) builder.append(tab); builder.append("lstYn(목록여부):").append(lstYn).append('\n'); }
		if(mdTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mdTypCd(모듈구분코드):").append(mdTypCd).append('\n'); }
		if(allCompYn!=null) { if(tab!=null) builder.append(tab); builder.append("allCompYn(전사여부):").append(allCompYn).append('\n'); }
		if(lstTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("lstTypCd(목록구분코드):").append(lstTypCd).append('\n'); }		
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }		
		if(deployDt!=null) { if(tab!=null) builder.append(tab); builder.append("deployDt(배포일시):").append(deployDt).append('\n'); }		
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
