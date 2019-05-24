package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 조직기본(OR_ORG_B) 테이블 VO
 */
public class OrOrgBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2429334283318091780L;

	/** 조직ID - KEY */
	private String orgId;

	/** 조직명 */
	private String orgNm;

	/** 리소스ID */
	private String rescId;

	/** 회사ID */
	private String compId;

	/** 조직부모ID */
	private String orgPid;

	/** 부서ID */
	private String deptId;

	/** 부서리소스ID */
	private String deptRescId;

	/** 참조ID */
	private String rid;

	/** 정렬순서 */
	private String sortOrdr;

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	private String orgTypCd;

	/** 동기화단계코드 */
	private String syncStepCd;

	/** 사용여부 */
	private String useYn;

	/** 시스템조직여부 */
	private String sysOrgYn;

	/** 조직약어리소스ID */
	private String orgAbbrRescId;

	/** 참조값1 */
	private String refVa1;

	/** 참조값2 */
	private String refVa2;

	/** 참조값3 */
	private String refVa3;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 회사ID 목록 */
	private List<String> compIdList;

	/** 조직ID 목록 */
	private List<String> orgIdList;

	/** 타사 회사ID */
	private String foreignCompId;

	/** 리소스명 */
	private String rescNm;

	/** 부서리소스명 */
	private String deptRescNm;

	/** 조직구분명 */
	private String orgTypNm;

	/** 조직약어리소스명 */
	private String orgAbbrRescNm;

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

	/** 조직명 */
	public String getOrgNm() {
		return orgNm;
	}

	/** 조직명 */
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 조직부모ID */
	public String getOrgPid() {
		return orgPid;
	}

	/** 조직부모ID */
	public void setOrgPid(String orgPid) {
		this.orgPid = orgPid;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** 부서리소스ID */
	public String getDeptRescId() {
		return deptRescId;
	}

	/** 부서리소스ID */
	public void setDeptRescId(String deptRescId) {
		this.deptRescId = deptRescId;
	}

	/** 참조ID */
	public String getRid() {
		return rid;
	}

	/** 참조ID */
	public void setRid(String rid) {
		this.rid = rid;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	public String getOrgTypCd() {
		return orgTypCd;
	}

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	public void setOrgTypCd(String orgTypCd) {
		this.orgTypCd = orgTypCd;
	}

	/** 동기화단계코드 */
	public String getSyncStepCd() {
		return syncStepCd;
	}

	/** 동기화단계코드 */
	public void setSyncStepCd(String syncStepCd) {
		this.syncStepCd = syncStepCd;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 시스템조직여부 */
	public String getSysOrgYn() {
		return sysOrgYn;
	}

	/** 시스템조직여부 */
	public void setSysOrgYn(String sysOrgYn) {
		this.sysOrgYn = sysOrgYn;
	}

	/** 조직약어리소스ID */
	public String getOrgAbbrRescId() {
		return orgAbbrRescId;
	}

	/** 조직약어리소스ID */
	public void setOrgAbbrRescId(String orgAbbrRescId) {
		this.orgAbbrRescId = orgAbbrRescId;
	}

	/** 참조값1 */
	public String getRefVa1() {
		return refVa1;
	}

	/** 참조값1 */
	public void setRefVa1(String refVa1) {
		this.refVa1 = refVa1;
	}

	/** 참조값2 */
	public String getRefVa2() {
		return refVa2;
	}

	/** 참조값2 */
	public void setRefVa2(String refVa2) {
		this.refVa2 = refVa2;
	}

	/** 참조값3 */
	public String getRefVa3() {
		return refVa3;
	}

	/** 참조값3 */
	public void setRefVa3(String refVa3) {
		this.refVa3 = refVa3;
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

	/** 회사ID 목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	/** 회사ID 목록 */
	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}

	/** 조직ID 목록 */
	public List<String> getOrgIdList() {
		return orgIdList;
	}

	/** 조직ID 목록 */
	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	/** 타사 회사ID */
	public String getForeignCompId() {
		return foreignCompId;
	}

	/** 타사 회사ID */
	public void setForeignCompId(String foreignCompId) {
		this.foreignCompId = foreignCompId;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 부서리소스명 */
	public String getDeptRescNm() {
		return deptRescNm;
	}

	/** 부서리소스명 */
	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}

	/** 조직구분명 */
	public String getOrgTypNm() {
		return orgTypNm;
	}

	/** 조직구분명 */
	public void setOrgTypNm(String orgTypNm) {
		this.orgTypNm = orgTypNm;
	}

	/** 조직약어리소스명 */
	public String getOrgAbbrRescNm() {
		return orgAbbrRescNm;
	}

	/** 조직약어리소스명 */
	public void setOrgAbbrRescNm(String orgAbbrRescNm) {
		this.orgAbbrRescNm = orgAbbrRescNm;
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
			return "com.innobiz.orange.web.or.dao.OrOrgBDao.selectOrOrgB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgBDao.insertOrOrgB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgBDao.updateOrOrgB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgBDao.deleteOrOrgB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgBDao.countOrOrgB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(orgNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgNm(조직명):").append(orgNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(orgPid!=null) { if(tab!=null) builder.append(tab); builder.append("orgPid(조직부모ID):").append(orgPid).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(deptRescId!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescId(부서리소스ID):").append(deptRescId).append('\n'); }
		if(rid!=null) { if(tab!=null) builder.append(tab); builder.append("rid(참조ID):").append(rid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(orgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("orgTypCd(조직구분코드):").append(orgTypCd).append('\n'); }
		if(syncStepCd!=null) { if(tab!=null) builder.append(tab); builder.append("syncStepCd(동기화단계코드):").append(syncStepCd).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sysOrgYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysOrgYn(시스템조직여부):").append(sysOrgYn).append('\n'); }
		if(orgAbbrRescId!=null) { if(tab!=null) builder.append(tab); builder.append("orgAbbrRescId(조직약어리소스ID):").append(orgAbbrRescId).append('\n'); }
		if(refVa1!=null) { if(tab!=null) builder.append(tab); builder.append("refVa1(참조값1):").append(refVa1).append('\n'); }
		if(refVa2!=null) { if(tab!=null) builder.append(tab); builder.append("refVa2(참조값2):").append(refVa2).append('\n'); }
		if(refVa3!=null) { if(tab!=null) builder.append(tab); builder.append("refVa3(참조값3):").append(refVa3).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(compIdList!=null) { if(tab!=null) builder.append(tab); builder.append("compIdList(회사ID 목록):"); appendStringListTo(builder, compIdList, tab); builder.append('\n'); }
		if(orgIdList!=null) { if(tab!=null) builder.append(tab); builder.append("orgIdList(조직ID 목록):"); appendStringListTo(builder, orgIdList, tab); builder.append('\n'); }
		if(foreignCompId!=null) { if(tab!=null) builder.append(tab); builder.append("foreignCompId(타사 회사ID):").append(foreignCompId).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(orgTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgTypNm(조직구분명):").append(orgTypNm).append('\n'); }
		if(orgAbbrRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgAbbrRescNm(조직약어리소스명):").append(orgAbbrRescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
