package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 원직자기본(OR_ODUR_B) 테이블 VO
 */
public class OrOdurBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5619120264947734368L;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 로그인ID */
	private String lginId;

	/** 사용자명 */
	private String userNm;

	/** 리소스ID */
	private String rescId;

	/** 참조ID */
	private String rid;

	/** 사원번호 */
	private String ein;

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제 */
	private String userStatCd;

	/** 사용자설명 */
	private String userDesc;

	/** 입사년월일 */
	private String entraYmd;

	/** 퇴사년월일 */
	private String resigYmd;

	/** 성별코드 - M:남성, F:여성 */
	private String genCd;

	/** 결재비밀번호구분코드 - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	private String apvPwTypCd;

	/** 동기화단계코드 */
	private String syncStepCd;

	/** 참조값1 */
	private String refVa1;

	/** 참조값2 */
	private String refVa2;

	/** 참조값3 */
	private String refVa3;

	/** 시스템사용자여부 */
	private String sysUserYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 사용자UID */
	private String userUid;

	/** 조직ID */
	private String orgId;

	/** 원직자UID 목록 */
	private List<String> odurUidList;

	/** 로그인ID 목록 */
	private List<String> lginIdList;

	/** 회사ID */
	private String compId;

	/** 리소스명 */
	private String rescNm;

	/** 사용자상태명 */
	private String userStatNm;

	/** 성별명 */
	private String genNm;

	/** 결재비밀번호구분명 */
	private String apvPwTypNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 로그인ID */
	public String getLginId() {
		return lginId;
	}

	/** 로그인ID */
	public void setLginId(String lginId) {
		this.lginId = lginId;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 참조ID */
	public String getRid() {
		return rid;
	}

	/** 참조ID */
	public void setRid(String rid) {
		this.rid = rid;
	}

	/** 사원번호 */
	public String getEin() {
		return ein;
	}

	/** 사원번호 */
	public void setEin(String ein) {
		this.ein = ein;
	}

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제 */
	public String getUserStatCd() {
		return userStatCd;
	}

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제 */
	public void setUserStatCd(String userStatCd) {
		this.userStatCd = userStatCd;
	}

	/** 사용자설명 */
	public String getUserDesc() {
		return userDesc;
	}

	/** 사용자설명 */
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	/** 입사년월일 */
	public String getEntraYmd() {
		return entraYmd;
	}

	/** 입사년월일 */
	public void setEntraYmd(String entraYmd) {
		this.entraYmd = entraYmd;
	}

	/** 퇴사년월일 */
	public String getResigYmd() {
		return resigYmd;
	}

	/** 퇴사년월일 */
	public void setResigYmd(String resigYmd) {
		this.resigYmd = resigYmd;
	}

	/** 성별코드 - M:남성, F:여성 */
	public String getGenCd() {
		return genCd;
	}

	/** 성별코드 - M:남성, F:여성 */
	public void setGenCd(String genCd) {
		this.genCd = genCd;
	}

	/** 결재비밀번호구분코드 - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	public String getApvPwTypCd() {
		return apvPwTypCd;
	}

	/** 결재비밀번호구분코드 - SYS:시스템 비밀번호, APV:결재 비밀번호 */
	public void setApvPwTypCd(String apvPwTypCd) {
		this.apvPwTypCd = apvPwTypCd;
	}

	/** 동기화단계코드 */
	public String getSyncStepCd() {
		return syncStepCd;
	}

	/** 동기화단계코드 */
	public void setSyncStepCd(String syncStepCd) {
		this.syncStepCd = syncStepCd;
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

	/** 시스템사용자여부 */
	public String getSysUserYn() {
		return sysUserYn;
	}

	/** 시스템사용자여부 */
	public void setSysUserYn(String sysUserYn) {
		this.sysUserYn = sysUserYn;
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

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 조직ID */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 원직자UID 목록 */
	public List<String> getOdurUidList() {
		return odurUidList;
	}

	/** 원직자UID 목록 */
	public void setOdurUidList(List<String> odurUidList) {
		this.odurUidList = odurUidList;
	}

	/** 로그인ID 목록 */
	public List<String> getLginIdList() {
		return lginIdList;
	}

	/** 로그인ID 목록 */
	public void setLginIdList(List<String> lginIdList) {
		this.lginIdList = lginIdList;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 사용자상태명 */
	public String getUserStatNm() {
		return userStatNm;
	}

	/** 사용자상태명 */
	public void setUserStatNm(String userStatNm) {
		this.userStatNm = userStatNm;
	}

	/** 성별명 */
	public String getGenNm() {
		return genNm;
	}

	/** 성별명 */
	public void setGenNm(String genNm) {
		this.genNm = genNm;
	}

	/** 결재비밀번호구분명 */
	public String getApvPwTypNm() {
		return apvPwTypNm;
	}

	/** 결재비밀번호구분명 */
	public void setApvPwTypNm(String apvPwTypNm) {
		this.apvPwTypNm = apvPwTypNm;
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
			return "com.innobiz.orange.web.or.dao.OrOdurBDao.selectOrOdurB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurBDao.insertOrOdurB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurBDao.updateOrOdurB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurBDao.deleteOrOdurB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurBDao.countOrOdurB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원직자기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(lginId!=null) { if(tab!=null) builder.append(tab); builder.append("lginId(로그인ID):").append(lginId).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(rid!=null) { if(tab!=null) builder.append(tab); builder.append("rid(참조ID):").append(rid).append('\n'); }
		if(ein!=null) { if(tab!=null) builder.append(tab); builder.append("ein(사원번호):").append(ein).append('\n'); }
		if(userStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("userStatCd(사용자상태코드):").append(userStatCd).append('\n'); }
		if(userDesc!=null) { if(tab!=null) builder.append(tab); builder.append("userDesc(사용자설명):").append(userDesc).append('\n'); }
		if(entraYmd!=null) { if(tab!=null) builder.append(tab); builder.append("entraYmd(입사년월일):").append(entraYmd).append('\n'); }
		if(resigYmd!=null) { if(tab!=null) builder.append(tab); builder.append("resigYmd(퇴사년월일):").append(resigYmd).append('\n'); }
		if(genCd!=null) { if(tab!=null) builder.append(tab); builder.append("genCd(성별코드):").append(genCd).append('\n'); }
		if(apvPwTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvPwTypCd(결재비밀번호구분코드):").append(apvPwTypCd).append('\n'); }
		if(syncStepCd!=null) { if(tab!=null) builder.append(tab); builder.append("syncStepCd(동기화단계코드):").append(syncStepCd).append('\n'); }
		if(refVa1!=null) { if(tab!=null) builder.append(tab); builder.append("refVa1(참조값1):").append(refVa1).append('\n'); }
		if(refVa2!=null) { if(tab!=null) builder.append(tab); builder.append("refVa2(참조값2):").append(refVa2).append('\n'); }
		if(refVa3!=null) { if(tab!=null) builder.append(tab); builder.append("refVa3(참조값3):").append(refVa3).append('\n'); }
		if(sysUserYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysUserYn(시스템사용자여부):").append(sysUserYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(odurUidList!=null) { if(tab!=null) builder.append(tab); builder.append("odurUidList(원직자UID 목록):"); appendStringListTo(builder, odurUidList, tab); builder.append('\n'); }
		if(lginIdList!=null) { if(tab!=null) builder.append(tab); builder.append("lginIdList(로그인ID 목록):"); appendStringListTo(builder, lginIdList, tab); builder.append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(userStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("userStatNm(사용자상태명):").append(userStatNm).append('\n'); }
		if(genNm!=null) { if(tab!=null) builder.append(tab); builder.append("genNm(성별명):").append(genNm).append('\n'); }
		if(apvPwTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvPwTypNm(결재비밀번호구분명):").append(apvPwTypNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
