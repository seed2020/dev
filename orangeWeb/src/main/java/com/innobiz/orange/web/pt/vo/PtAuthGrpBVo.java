package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 권한그룹기본(PT_AUTH_GRP_B) 테이블 VO
 */
public class PtAuthGrpBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6713829273529821288L;

	/** 회사ID - KEY */
	private String compId;

	/** 권한그룹ID - KEY */
	private String authGrpId;

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	private String authGrpTypCd;

	/** 권한그룹카테고리코드 */
	private String authGrpCatCd;

	/** 권한그룹명 */
	private String authGrpNm;

	/** 리소스ID */
	private String rescId;

	/** 권한그룹설명 */
	private String authGrpDesc;

	/** 시스템권한그룹여부 */
	private String sysAuthGrpYn;

	/** 사용여부 */
	private String useYn;

	/** 정렬순서 */
	private String sortOrdr;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 권한그룹카테고리명 */
	private String authGrpCatNm;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 권한그룹ID - KEY */
	public String getAuthGrpId() {
		return authGrpId;
	}

	/** 권한그룹ID - KEY */
	public void setAuthGrpId(String authGrpId) {
		this.authGrpId = authGrpId;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public String getAuthGrpTypCd() {
		return authGrpTypCd;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public void setAuthGrpTypCd(String authGrpTypCd) {
		this.authGrpTypCd = authGrpTypCd;
	}

	/** 권한그룹카테고리코드 */
	public String getAuthGrpCatCd() {
		return authGrpCatCd;
	}

	/** 권한그룹카테고리코드 */
	public void setAuthGrpCatCd(String authGrpCatCd) {
		this.authGrpCatCd = authGrpCatCd;
	}

	/** 권한그룹명 */
	public String getAuthGrpNm() {
		return authGrpNm;
	}

	/** 권한그룹명 */
	public void setAuthGrpNm(String authGrpNm) {
		this.authGrpNm = authGrpNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 권한그룹설명 */
	public String getAuthGrpDesc() {
		return authGrpDesc;
	}

	/** 권한그룹설명 */
	public void setAuthGrpDesc(String authGrpDesc) {
		this.authGrpDesc = authGrpDesc;
	}

	/** 시스템권한그룹여부 */
	public String getSysAuthGrpYn() {
		return sysAuthGrpYn;
	}

	/** 시스템권한그룹여부 */
	public void setSysAuthGrpYn(String sysAuthGrpYn) {
		this.sysAuthGrpYn = sysAuthGrpYn;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
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

	/** 권한그룹카테고리명 */
	public String getAuthGrpCatNm() {
		return authGrpCatNm;
	}

	/** 권한그룹카테고리명 */
	public void setAuthGrpCatNm(String authGrpCatNm) {
		this.authGrpCatNm = authGrpCatNm;
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
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpBDao.selectPtAuthGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpBDao.insertPtAuthGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpBDao.updatePtAuthGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpBDao.deletePtAuthGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpBDao.countPtAuthGrpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":권한그룹기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(authGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpId(권한그룹ID-PK):").append(authGrpId).append('\n'); }
		if(authGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpTypCd(권한그룹구분코드):").append(authGrpTypCd).append('\n'); }
		if(authGrpCatCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpCatCd(권한그룹카테고리코드):").append(authGrpCatCd).append('\n'); }
		if(authGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpNm(권한그룹명):").append(authGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(authGrpDesc!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpDesc(권한그룹설명):").append(authGrpDesc).append('\n'); }
		if(sysAuthGrpYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysAuthGrpYn(시스템권한그룹여부):").append(sysAuthGrpYn).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(authGrpCatNm!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpCatNm(권한그룹카테고리명):").append(authGrpCatNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
