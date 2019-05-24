package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 양식함상세(AP_FORM_BX_D) 테이블 VO
 */
public class ApFormBxDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 802800010882984969L;

	/** 양식함ID - KEY */
	private String formBxId;

	/** 회사ID */
	private String compId;

	/** 양식함명 */
	private String formBxNm;

	/** 리소스ID */
	private String rescId;

	/** 양식함부모ID */
	private String formBxPid;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 양식부서ID목록 */
	private List<String> formBxDeptIdList;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 양식함ID - KEY */
	public String getFormBxId() {
		return formBxId;
	}

	/** 양식함ID - KEY */
	public void setFormBxId(String formBxId) {
		this.formBxId = formBxId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 양식함명 */
	public String getFormBxNm() {
		return formBxNm;
	}

	/** 양식함명 */
	public void setFormBxNm(String formBxNm) {
		this.formBxNm = formBxNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 양식함부모ID */
	public String getFormBxPid() {
		return formBxPid;
	}

	/** 양식함부모ID */
	public void setFormBxPid(String formBxPid) {
		this.formBxPid = formBxPid;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 양식부서ID목록 */
	public List<String> getFormBxDeptIdList() {
		return formBxDeptIdList;
	}

	/** 양식부서ID목록 */
	public void setFormBxDeptIdList(List<String> formBxDeptIdList) {
		this.formBxDeptIdList = formBxDeptIdList;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDDao.selectApFormBxD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDDao.insertApFormBxD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDDao.updateApFormBxD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDDao.deleteApFormBxD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApFormBxDDao.countApFormBxD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식함상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formBxId!=null) { if(tab!=null) builder.append(tab); builder.append("formBxId(양식함ID-PK):").append(formBxId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(formBxNm!=null) { if(tab!=null) builder.append(tab); builder.append("formBxNm(양식함명):").append(formBxNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(formBxPid!=null) { if(tab!=null) builder.append(tab); builder.append("formBxPid(양식함부모ID):").append(formBxPid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(formBxDeptIdList!=null) { if(tab!=null) builder.append(tab); builder.append("formBxDeptIdList(양식부서ID목록):"); appendStringListTo(builder, formBxDeptIdList, tab); builder.append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
