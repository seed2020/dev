package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함대리관리자기본(WB_BC_AGNT_ADM_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcAgntAdmBVo extends CommonVoImpl {
	/** 등록자UID */ 
	private String regrUid;

 	/** 대리관리자UID */ 
	private String agntAdmUid;

 	/** 권한코드 */ 
	private String authCd;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
/** 추가 */
	
	/** 사용자명 */
	private String userNm;
	
	/** 조직리소스명 */ 
	private String orgRescNm;
	
	/** 부서리소스명 */ 
	private String deptRescNm;
	
	/** 삭제 UID 배열 */ 
	private String[] delList;

 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setAgntAdmUid(String agntAdmUid) { 
		this.agntAdmUid = agntAdmUid;
	}
	/** 대리관리자UID */ 
	public String getAgntAdmUid() { 
		return agntAdmUid;
	}

	public void setAuthCd(String authCd) { 
		this.authCd = authCd;
	}
	/** 권한코드 */ 
	public String getAuthCd() { 
		return authCd;
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
	
	public String getOrgRescNm() {
		return orgRescNm;
	}
	public void setOrgRescNm(String orgRescNm) {
		this.orgRescNm = orgRescNm;
	}
	public String getDeptRescNm() {
		return deptRescNm;
	}
	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}
	
	public String[] getDelList() {
		return delList;
	}
	public void setDelList(String[] delList) {
		this.delList = delList;
	}
	
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntAdmBDao.selectWbBcAgntAdmB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntAdmBDao.insertWbBcAgntAdmB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntAdmBDao.updateWbBcAgntAdmB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntAdmBDao.deleteWbBcAgntAdmB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntAdmBDao.countWbBcAgntAdmB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함대리관리자기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(agntAdmUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntAdmUid(대리관리자UID):").append(agntAdmUid).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한코드):").append(authCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(orgRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgRescNm(조직리소스명):").append(orgRescNm).append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(delList!=null) { if(tab!=null) builder.append(tab); builder.append("delList(삭제 UID 배열):"); appendArrayTo(builder, delList); builder.append('\n'); }
		super.toString(builder, tab);
	}
}