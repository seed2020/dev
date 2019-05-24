package com.innobiz.orange.web.pt.secu;

import java.util.List;

import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.pt.utils.PtConstant;

/**
 * 사용자 VO - 세션에 담을 사용자 로그인 정보 객체
 */
public class UserVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 341555562565790909L;

	/** 사용자UID */
	private String userUid;

	/** 원직자UID */
	private String odurUid;

	/** 사용자명 */
	private String userNm;

	/** 로그인ID */
	private String lginId;

	/** 조직ID */
	private String orgId;

	/** 회사ID */
	private String compId;

	/** 부서ID */
	private String deptId;

	/** 부서명 */
	private String deptNm;

	/** 보안등급코드 */
	private String seculCd;

	/** 역할코드목록 */
	private String[] roleCds;

	/** 겸직목록[부서명, 사용자UID] */
	private String[][] adurs;

	/** 상위조직목록 */
	private String[] orgPids;

	/** 상위부서목록 */
	private String[] deptPids;

	/** 레이아웃유형ID */
	private String loutCatId;

	/** 스킨경로 */
	private String skin;

	/** 언어구분코드 */
	private String langTypCd;

	/** 사용자권한그룹ID목록 */
	private String[] userAuthGrpIds;

	/** 관리자권한그룹ID목록 */
	private String[] adminAuthGrpIds;

	/** 로그인IP */
	private String lginIp;

	/** 로그인IP제외대상여부 */
	private boolean lginIpExcliYn;

	/** 세션IP제외대상여부 */
	private boolean sesnIpExcliYn;
	
	/** 제외대상로그인여부 */
	private boolean excliLginYn;

	/** 대리인UID목록 */
	private String[] agntUids;

	/** 대리인체크시간 */
	private Long agntChkTm;

	/** 권한체크시간 */
	private Long authChkTm;

	/** 보안쿠키 */
	private SecuCookie secuCookie;

	/** 겸직자VO목록 */
	private List<UserVo> additionalDutyVoList;

	/** 데모 로그인 여부 */
	private Boolean demoLgin;

	/** 내부망 IP 여부 */
	private Boolean internalIp;

	/** 관리자 세션 */
	private Boolean adminSesn;

	/** 변경가능한 IP 목록 */
	private List<String> changeableIpList;

	/** 자동결재선코드 */
	private String autoApvLnCd;

	/** 메뉴그룹 모듈 참조ID 목록 */
	private String[] mnuGrpMdRids;

	/** 사용자상태코드 */
	private String userStatCd;

	/** 이메일 */
	private String email;

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 원직자UID */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 로그인ID */
	public String getLginId() {
		return lginId;
	}

	/** 로그인ID */
	public void setLginId(String lginId) {
		this.lginId = lginId;
	}

	/** 조직ID */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** 조직명 */
	public String getDeptNm() {
		return deptNm;
	}

	/** 조직명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	/** 보안등급코드 */
	public String getSeculCd() {
		return seculCd;
	}

	/** 보안등급코드 */
	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}

	/** 역할코드목록 */
	public String[] getRoleCds() {
		return roleCds;
	}

	/** 역할코드목록 */
	public void setRoleCds(String[] roleCds) {
		this.roleCds = roleCds;
	}
	
	public boolean hasRole(String roleCd){
		if(roleCds==null) return false;
		return ArrayUtil.isInArray(roleCds, roleCd);
	}

	/** 겸직목록[부서명, 사용자UID] */
	public String[][] getAdurs() {
		return adurs;
	}

	/** 겸직목록[부서명, 사용자UID] */
	public void setAdurs(String[][] adurs) {
		this.adurs = adurs;
	}

	/** 상위조직목록 */
	public String[] getOrgPids() {
		return orgPids;
	}

	/** 상위조직목록 */
	public void setOrgPids(String[] orgPids) {
		this.orgPids = orgPids;
	}

	/** 상위부서목록 */
	public String[] getDeptPids() {
		return deptPids;
	}

	/** 상위부서목록 */
	public void setDeptPids(String[] deptPids) {
		this.deptPids = deptPids;
	}

	/** 레이아웃유형ID */
	public String getLoutCatId() {
		return loutCatId;
	}

	/** 레이아웃유형ID */
	public void setLoutCatId(String loutCatId) {
		this.loutCatId = loutCatId;
	}

	/** 스킨경로 */
	public String getSkin() {
		return skin;
	}

	/** 스킨경로 */
	public void setSkin(String skin) {
		this.skin = skin;
	}

	/** 언어구분코드 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 사용자권한그룹ID목록 */
	public String[] getUserAuthGrpIds() {
		return userAuthGrpIds;
	}

	/** 사용자권한그룹ID목록 */
	public void setUserAuthGrpIds(String[] userAuthGrpIds) {
		this.userAuthGrpIds = userAuthGrpIds;
	}

	/** 관리자권한그룹ID목록 */
	public String[] getAdminAuthGrpIds() {
		return adminAuthGrpIds;
	}

	/** 관리자권한그룹ID목록 */
	public void setAdminAuthGrpIds(String[] adminAuthGrpIds) {
		this.adminAuthGrpIds = adminAuthGrpIds;
	}

	/** 로그인IP */
	public String getLginIp() {
		return lginIp;
	}

	/** 로그인IP */
	public void setLginIp(String lginIp) {
		this.lginIp = lginIp;
	}

	/** 로그인IP제외대상여부 */
	public boolean getLginIpExcliYn() {
		return lginIpExcliYn;
	}

	/** 로그인IP제외대상여부 */
	public void setLginIpExcliYn(boolean lginIpExcliYn) {
		this.lginIpExcliYn = lginIpExcliYn;
	}

	/** 세션IP제외대상여부 */
	public boolean getSesnIpExcliYn() {
		return sesnIpExcliYn;
	}

	/** 세션IP제외대상여부 */
	public void setSesnIpExcliYn(boolean sesnIpExcliYn) {
		this.sesnIpExcliYn = sesnIpExcliYn;
	}

	/** 제외대상로그인여부 */
	public boolean getExcliLginYn() {
		return excliLginYn;
	}

	/** 제외대상로그인여부 */
	public void setExcliLginYn(boolean excliLginYn) {
		this.excliLginYn = excliLginYn;
	}

	/** 시스템 관리자 여부 */
	public boolean isSysAdmin(){
		return ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_SYS_ADMIN);
	}
	/** 관리자 여부 */
	public boolean isAdmin(){
		return isSysAdmin() || ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_ADMIN);
	}

	/** 대리인UID목록 */
	public String[] getAgntUids() {
		return agntUids;
	}

	/** 대리인UID목록 */
	public void setAgntUids(String[] agntUids) {
		this.agntUids = agntUids;
	}

	/** 대리인체크시간 */
	public Long getAgntChkTm() {
		return agntChkTm;
	}

	/** 대리인체크시간 */
	public void setAgntChkTm(Long agntChkTm) {
		this.agntChkTm = agntChkTm;
	}

	/** 권한체크시간 */
	public Long getAuthChkTm() {
		return authChkTm;
	}

	/** 권한체크시간 */
	public void setAuthChkTm(Long authChkTm) {
		this.authChkTm = authChkTm;
	}

	/** 보안쿠키 */
	public SecuCookie getSecuCookie() {
		return secuCookie;
	}

	/** 보안쿠키 */
	public void setSecuCookie(SecuCookie secuCookie) {
		this.secuCookie = secuCookie;
	}

	/** 겸직자VO목록 */
	public List<UserVo> getAdditionalDutyVoList() {
		return additionalDutyVoList;
	}

	/** 겸직자VO목록 */
	public void setAdditionalDutyVoList(List<UserVo> additionalDutyVoList) {
		this.additionalDutyVoList = additionalDutyVoList;
	}

	/** 데모 로그인 여부 */
	public Boolean isDemoLgin() {
		return demoLgin==null ? Boolean.FALSE : demoLgin;
	}

	/** 데모 로그인 여부 */
	public void setDemoLgin(Boolean demoLgin) {
		this.demoLgin = demoLgin;
	}

	/** 내부망 IP 여부 */
	public Boolean isInternalIp() {
		return internalIp==null ? Boolean.FALSE : internalIp;
	}

	/** 내부망 IP 여부 */
	public void setInternalIp(Boolean internalIp) {
		this.internalIp = internalIp;
	}

	/** 관리자 세션 */
	public Boolean isAdminSesn() {
		return adminSesn==null ? Boolean.FALSE : adminSesn;
	}

	/** 관리자 세션 */
	public void setAdminSesn(Boolean adminSesn) {
		this.adminSesn = adminSesn;
	}

	/** 변경가능한 IP 목록 */
	public List<String> getChangeableIpList() {
		return changeableIpList;
	}

	/** 변경가능한 IP 목록 */
	public void setChangeableIpList(List<String> changeableIpList) {
		this.changeableIpList = changeableIpList;
	}

	/** 자동결재선코드 */
	public String getAutoApvLnCd() {
		return autoApvLnCd;
	}

	/** 자동결재선코드 */
	public void setAutoApvLnCd(String autoApvLnCd) {
		this.autoApvLnCd = autoApvLnCd;
	}

	/** 메뉴그룹 모듈 참조ID 목록 */
	public void setMnuGrpMdRids(String[] mnuGrpMdRids) {
		this.mnuGrpMdRids = mnuGrpMdRids;
	}
	
	/** 메뉴그룹 모듈 참조ID 목록 */
	public boolean hasMnuGrpMdRidOf(String ... mdRids){
		if(this.mnuGrpMdRids == null || mdRids == null) return false;
		
		for(String mnuGrpMdRid : mnuGrpMdRids){
			for(String mdRid : mdRids){
				if(mdRid!=null && mdRid.equals(mnuGrpMdRid)){
					return true;
				}
			}
		}
		return false;
	}

	/** 사용자상태코드 */
	public String getUserStatCd() {
		return userStatCd;
	}

	/** 사용자상태코드 */
	public void setUserStatCd(String userStatCd) {
		this.userStatCd = userStatCd;
	}

	/** 이메일 */
	public String getEmail() {
		return email;
	}

	/** 이메일 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID):").append(odurUid).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(lginId!=null) { if(tab!=null) builder.append(tab); builder.append("lginId(로그인ID):").append(lginId).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안등급코드):").append(seculCd).append('\n'); }
		if(roleCds!=null) { if(tab!=null) builder.append(tab); builder.append("roleCds(역할코드목록):"); appendArrayTo(builder, roleCds); builder.append('\n'); }
		if(adurs!=null) { if(tab!=null) builder.append(tab); builder.append("adurs(겸직목록[부서명, 사용자UID]):"); appendArrayTo(builder, adurs); builder.append('\n'); }
		if(orgPids!=null) { if(tab!=null) builder.append(tab); builder.append("orgPids(상위조직목록):"); appendArrayTo(builder, orgPids); builder.append('\n'); }
		if(deptPids!=null) { if(tab!=null) builder.append(tab); builder.append("deptPids(상위부서목록):"); appendArrayTo(builder, deptPids); builder.append('\n'); }
		if(loutCatId!=null) { if(tab!=null) builder.append(tab); builder.append("loutCatId(레이아웃유형ID):").append(loutCatId).append('\n'); }
		if(skin!=null) { if(tab!=null) builder.append(tab); builder.append("skin(스킨경로):").append(skin).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(userAuthGrpIds!=null) { if(tab!=null) builder.append(tab); builder.append("userAuthGrpIds(사용자권한그룹ID목록):"); appendArrayTo(builder, userAuthGrpIds); builder.append('\n'); }
		if(adminAuthGrpIds!=null) { if(tab!=null) builder.append(tab); builder.append("adminAuthGrpIds(관리자권한그룹ID목록):"); appendArrayTo(builder, adminAuthGrpIds); builder.append('\n'); }
		if(lginIp!=null) { if(tab!=null) builder.append(tab); builder.append("lginIp(로그인IP):").append(lginIp).append('\n'); }
		if(lginIpExcliYn) { if(tab!=null) builder.append(tab); builder.append("lginIpExcliYn(로그인IP제외대상여부):").append(lginIpExcliYn).append('\n'); }
		if(sesnIpExcliYn) { if(tab!=null) builder.append(tab); builder.append("sesnIpExcliYn(세션IP제외대상여부):").append(sesnIpExcliYn).append('\n'); }
		if(excliLginYn) { if(tab!=null) builder.append(tab); builder.append("excliLginYn(제외대상로그인여부):").append(excliLginYn).append('\n'); }
		if(agntUids!=null) { if(tab!=null) builder.append(tab); builder.append("agntUids(대리인UID목록):"); appendArrayTo(builder, agntUids); builder.append('\n'); }
		if(agntChkTm!=null) { if(tab!=null) builder.append(tab); builder.append("agntChkTm(대리인체크시간):").append(agntChkTm).append('\n'); }
		if(authChkTm!=null) { if(tab!=null) builder.append(tab); builder.append("authChkTm(권한체크시간):").append(authChkTm).append('\n'); }
		if(demoLgin!=null) { if(tab!=null) builder.append(tab); builder.append("demoLgin(데모 로그인 여부):").append(demoLgin).append('\n'); }
		if(internalIp!=null) { if(tab!=null) builder.append(tab); builder.append("internalIp(내부망 IP 여부):").append(internalIp).append('\n'); }
		if(adminSesn!=null) { if(tab!=null) builder.append(tab); builder.append("adminSesn(관리자 세션):").append(adminSesn).append('\n'); }
		if(changeableIpList!=null) { if(tab!=null) builder.append(tab); builder.append("changeableIpList(변경가능한 IP 목록):"); appendStringListTo(builder, changeableIpList, tab); builder.append('\n'); }
		if(autoApvLnCd!=null) { if(tab!=null) builder.append(tab); builder.append("autoApvLnCd(자동결재선코드):").append(autoApvLnCd).append('\n'); }
		if(mnuGrpMdRids!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpMdRids(메뉴그룹 모듈 참조ID 목록):"); appendArrayTo(builder, mnuGrpMdRids); builder.append('\n'); }
		if(userStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("userStatCd(사용자상태코드):").append(userStatCd).append('\n'); }
		if(email!=null) { if(tab!=null) builder.append(tab); builder.append("email(이메일):").append(email).append('\n'); }
		super.toString(builder, tab);
	}
}
