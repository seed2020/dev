package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 분류정보상세(AP_CLS_INFO_D) 테이블 VO
 */
public class ApClsInfoDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 994767949126512516L;

	/** 조직ID - KEY */
	private String orgId;

	/** 분류정보ID - KEY */
	private String clsInfoId;

	/** 분류정보부모ID */
	private String clsInfoPid;

	/** 리소스ID */
	private String rescId;

	/** 분류정보명 */
	private String clsInfoNm;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;

	/** 시스템분류정보여부 */
	private String sysClsInfoYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 조직ID - KEY */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - KEY */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 분류정보ID - KEY */
	public String getClsInfoId() {
		return clsInfoId;
	}

	/** 분류정보ID - KEY */
	public void setClsInfoId(String clsInfoId) {
		this.clsInfoId = clsInfoId;
	}

	/** 분류정보부모ID */
	public String getClsInfoPid() {
		return clsInfoPid;
	}

	/** 분류정보부모ID */
	public void setClsInfoPid(String clsInfoPid) {
		this.clsInfoPid = clsInfoPid;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 분류정보명 */
	public String getClsInfoNm() {
		return clsInfoNm;
	}

	/** 분류정보명 */
	public void setClsInfoNm(String clsInfoNm) {
		this.clsInfoNm = clsInfoNm;
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

	/** 시스템분류정보여부 */
	public String getSysClsInfoYn() {
		return sysClsInfoYn;
	}

	/** 시스템분류정보여부 */
	public void setSysClsInfoYn(String sysClsInfoYn) {
		this.sysClsInfoYn = sysClsInfoYn;
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
			return "com.innobiz.orange.web.ap.dao.ApClsInfoDDao.selectApClsInfoD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApClsInfoDDao.insertApClsInfoD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApClsInfoDDao.updateApClsInfoD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApClsInfoDDao.deleteApClsInfoD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApClsInfoDDao.countApClsInfoD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":분류정보상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(clsInfoId!=null) { if(tab!=null) builder.append(tab); builder.append("clsInfoId(분류정보ID-PK):").append(clsInfoId).append('\n'); }
		if(clsInfoPid!=null) { if(tab!=null) builder.append(tab); builder.append("clsInfoPid(분류정보부모ID):").append(clsInfoPid).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(clsInfoNm!=null) { if(tab!=null) builder.append(tab); builder.append("clsInfoNm(분류정보명):").append(clsInfoNm).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sysClsInfoYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysClsInfoYn(시스템분류정보여부):").append(sysClsInfoYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
