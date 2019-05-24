package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 사용자기본(OR_USER_B) 테이블 VO
 */
public class OrUserBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8933040367691190832L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 원직자UID */
	private String odurUid;

	/** 회사ID */
	private String compId;

	/** 조직ID */
	private String orgId;

	/** 조직리소스ID */
	private String orgRescId;

	/** 부서ID */
	private String deptId;

	/** 부서리소스ID */
	private String deptRescId;

	/** 정렬순서 */
	private String sortOrdr;

	/** 보안등급코드 */
	private String seculCd;

	/** 직급코드 */
	private String gradeCd;

	/** 직책코드 */
	private String titleCd;

	/** 직위코드 */
	private String positCd;

	/** 직무코드 */
	private String dutyCd;

	/** 사용자명 */
	private String userNm;

	/** 리소스ID */
	private String rescId;

	/** 겸직구분코드 - 01:원직, 02:겸직, 03:파견직 */
	private String aduTypCd;

	/** 시작년월일 */
	private String strtYmd;

	/** 종료년월일 */
	private String endYmd;

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	private String userStatCd;

	/** 종료사유 */
	private String endRson;

	/** 담당업무내용 */
	private String tichCont;

	/** 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자) */
	private String signMthdCd;

	/** 참조ID */
	private String rid;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 동기화단계코드 */
	private String syncStepCd;

	/** 참조값1 */
	private String refVa1;

	/** 참조값2 */
	private String refVa2;

	/** 참조값3 */
	private String refVa3;

	/** 자동결재선코드 */
	private String autoApvLnCd;

	/** 시스템사용자여부 */
	private String sysUserYn;


	// 추가컬럼
	/** 정렬순서옵션 */
	private String sortOrdrOpt;

	/** 역할코드 - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	private String roleCd;

	/** 사용자UID 목록 */
	private List<String> userUidList;

	/** 조직ID 목록 */
	private List<String> orgIdList;

	/** 회사ID 목록 */
	private List<String> compIdList;

	/** 사용자상태코드목록 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	private List<String> userStatCdList;

	/** 원직자 VO */
	private OrOdurBVo orOdurBVo;

	/** 역할명 */
	private String roleNms;

	/** 사원번호 */
	private String ein;

	/** 로그인ID */
	private String lginId;

	/** 입사년월일 */
	private String entraYmd;

	/** 최소입사년월일 */
	private String minEntraYmd;

	/** 입사년월일 */
	private String maxEntraYmd;

	/** 조직리소스명 */
	private String orgRescNm;

	/** 부서리소스명 */
	private String deptRescNm;

	/** 보안등급명 */
	private String seculNm;

	/** 직급명 */
	private String gradeNm;

	/** 직책명 */
	private String titleNm;

	/** 직위명 */
	private String positNm;

	/** 직무명 */
	private String dutyNm;

	/** 리소스명 */
	private String rescNm;

	/** 겸직구분명 */
	private String aduTypNm;

	/** 사용자상태명 */
	private String userStatNm;

	/** 서명방법명 */
	private String signMthdNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 자동결재선명 */
	private String autoApvLnNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
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

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 조직ID */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 조직리소스ID */
	public String getOrgRescId() {
		return orgRescId;
	}

	/** 조직리소스ID */
	public void setOrgRescId(String orgRescId) {
		this.orgRescId = orgRescId;
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

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 보안등급코드 */
	public String getSeculCd() {
		return seculCd;
	}

	/** 보안등급코드 */
	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}

	/** 직급코드 */
	public String getGradeCd() {
		return gradeCd;
	}

	/** 직급코드 */
	public void setGradeCd(String gradeCd) {
		this.gradeCd = gradeCd;
	}

	/** 직책코드 */
	public String getTitleCd() {
		return titleCd;
	}

	/** 직책코드 */
	public void setTitleCd(String titleCd) {
		this.titleCd = titleCd;
	}

	/** 직위코드 */
	public String getPositCd() {
		return positCd;
	}

	/** 직위코드 */
	public void setPositCd(String positCd) {
		this.positCd = positCd;
	}

	/** 직무코드 */
	public String getDutyCd() {
		return dutyCd;
	}

	/** 직무코드 */
	public void setDutyCd(String dutyCd) {
		this.dutyCd = dutyCd;
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

	/** 겸직구분코드 - 01:원직, 02:겸직, 03:파견직 */
	public String getAduTypCd() {
		return aduTypCd;
	}

	/** 겸직구분코드 - 01:원직, 02:겸직, 03:파견직 */
	public void setAduTypCd(String aduTypCd) {
		this.aduTypCd = aduTypCd;
	}

	/** 시작년월일 */
	public String getStrtYmd() {
		return strtYmd;
	}

	/** 시작년월일 */
	public void setStrtYmd(String strtYmd) {
		this.strtYmd = strtYmd;
	}

	/** 종료년월일 */
	public String getEndYmd() {
		return endYmd;
	}

	/** 종료년월일 */
	public void setEndYmd(String endYmd) {
		this.endYmd = endYmd;
	}

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	public String getUserStatCd() {
		return userStatCd;
	}

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	public void setUserStatCd(String userStatCd) {
		this.userStatCd = userStatCd;
	}

	/** 종료사유 */
	public String getEndRson() {
		return endRson;
	}

	/** 종료사유 */
	public void setEndRson(String endRson) {
		this.endRson = endRson;
	}

	/** 담당업무내용 */
	public String getTichCont() {
		return tichCont;
	}

	/** 담당업무내용 */
	public void setTichCont(String tichCont) {
		this.tichCont = tichCont;
	}

	/** 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자) */
	public String getSignMthdCd() {
		return signMthdCd;
	}

	/** 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자) */
	public void setSignMthdCd(String signMthdCd) {
		this.signMthdCd = signMthdCd;
	}

	/** 참조ID */
	public String getRid() {
		return rid;
	}

	/** 참조ID */
	public void setRid(String rid) {
		this.rid = rid;
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

	/** 자동결재선코드 */
	public String getAutoApvLnCd() {
		return autoApvLnCd;
	}

	/** 자동결재선코드 */
	public void setAutoApvLnCd(String autoApvLnCd) {
		this.autoApvLnCd = autoApvLnCd;
	}

	/** 시스템사용자여부 */
	public String getSysUserYn() {
		return sysUserYn;
	}

	/** 시스템사용자여부 */
	public void setSysUserYn(String sysUserYn) {
		this.sysUserYn = sysUserYn;
	}

	/** 정렬순서옵션 */
	public String getSortOrdrOpt() {
		return sortOrdrOpt;
	}

	/** 정렬순서옵션 */
	public void setSortOrdrOpt(String sortOrdrOpt) {
		this.sortOrdrOpt = sortOrdrOpt;
	}

	/** 역할코드 - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	public String getRoleCd() {
		return roleCd;
	}

	/** 역할코드 - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	public void setRoleCd(String roleCd) {
		this.roleCd = roleCd;
	}

	/** 사용자UID 목록 */
	public List<String> getUserUidList() {
		return userUidList;
	}

	/** 사용자UID 목록 */
	public void setUserUidList(List<String> userUidList) {
		this.userUidList = userUidList;
	}

	/** 조직ID 목록 */
	public List<String> getOrgIdList() {
		return orgIdList;
	}

	/** 조직ID 목록 */
	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	/** 회사ID 목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	/** 회사ID 목록 */
	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}

	/** 사용자상태코드목록 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	public List<String> getUserStatCdList() {
		return userStatCdList;
	}

	/** 사용자상태코드목록 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 96:SSO, 97:읽기전용, 98:숨김, 99:삭제 */
	public void setUserStatCdList(List<String> userStatCdList) {
		this.userStatCdList = userStatCdList;
	}

	/** 원직자 VO */
	public OrOdurBVo getOrOdurBVo() {
		return orOdurBVo;
	}

	/** 원직자 VO */
	public void setOrOdurBVo(OrOdurBVo orOdurBVo) {
		this.orOdurBVo = orOdurBVo;
	}

	/** 역할명 */
	public String getRoleNms() {
		return roleNms;
	}

	/** 역할명 */
	public void setRoleNms(String roleNms) {
		this.roleNms = roleNms;
	}

	/** 사원번호 */
	public String getEin() {
		return ein;
	}

	/** 사원번호 */
	public void setEin(String ein) {
		this.ein = ein;
	}

	/** 로그인ID */
	public String getLginId() {
		return lginId;
	}

	/** 로그인ID */
	public void setLginId(String lginId) {
		this.lginId = lginId;
	}

	/** 입사년월일 */
	public String getEntraYmd() {
		return entraYmd;
	}

	/** 입사년월일 */
	public void setEntraYmd(String entraYmd) {
		this.entraYmd = entraYmd;
	}

	/** 최소입사년월일 */
	public String getMinEntraYmd() {
		return minEntraYmd;
	}

	/** 최소입사년월일 */
	public void setMinEntraYmd(String minEntraYmd) {
		this.minEntraYmd = minEntraYmd;
	}

	/** 입사년월일 */
	public String getMaxEntraYmd() {
		return maxEntraYmd;
	}

	/** 입사년월일 */
	public void setMaxEntraYmd(String maxEntraYmd) {
		this.maxEntraYmd = maxEntraYmd;
	}

	/** 조직리소스명 */
	public String getOrgRescNm() {
		return orgRescNm;
	}

	/** 조직리소스명 */
	public void setOrgRescNm(String orgRescNm) {
		this.orgRescNm = orgRescNm;
	}

	/** 부서리소스명 */
	public String getDeptRescNm() {
		return deptRescNm;
	}

	/** 부서리소스명 */
	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}

	/** 보안등급명 */
	public String getSeculNm() {
		return seculNm;
	}

	/** 보안등급명 */
	public void setSeculNm(String seculNm) {
		this.seculNm = seculNm;
	}

	/** 직급명 */
	public String getGradeNm() {
		return gradeNm;
	}

	/** 직급명 */
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}

	/** 직책명 */
	public String getTitleNm() {
		return titleNm;
	}

	/** 직책명 */
	public void setTitleNm(String titleNm) {
		this.titleNm = titleNm;
	}

	/** 직위명 */
	public String getPositNm() {
		return positNm;
	}

	/** 직위명 */
	public void setPositNm(String positNm) {
		this.positNm = positNm;
	}

	/** 직무명 */
	public String getDutyNm() {
		return dutyNm;
	}

	/** 직무명 */
	public void setDutyNm(String dutyNm) {
		this.dutyNm = dutyNm;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 겸직구분명 */
	public String getAduTypNm() {
		return aduTypNm;
	}

	/** 겸직구분명 */
	public void setAduTypNm(String aduTypNm) {
		this.aduTypNm = aduTypNm;
	}

	/** 사용자상태명 */
	public String getUserStatNm() {
		return userStatNm;
	}

	/** 사용자상태명 */
	public void setUserStatNm(String userStatNm) {
		this.userStatNm = userStatNm;
	}

	/** 서명방법명 */
	public String getSignMthdNm() {
		return signMthdNm;
	}

	/** 서명방법명 */
	public void setSignMthdNm(String signMthdNm) {
		this.signMthdNm = signMthdNm;
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

	/** 자동결재선명 */
	public String getAutoApvLnNm() {
		return autoApvLnNm;
	}

	/** 자동결재선명 */
	public void setAutoApvLnNm(String autoApvLnNm) {
		this.autoApvLnNm = autoApvLnNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserBDao.insertOrUserB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserBDao.updateOrUserB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserBDao.deleteOrUserB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserBDao.countOrUserB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID):").append(odurUid).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(orgRescId!=null) { if(tab!=null) builder.append(tab); builder.append("orgRescId(조직리소스ID):").append(orgRescId).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(deptRescId!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescId(부서리소스ID):").append(deptRescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안등급코드):").append(seculCd).append('\n'); }
		if(gradeCd!=null) { if(tab!=null) builder.append(tab); builder.append("gradeCd(직급코드):").append(gradeCd).append('\n'); }
		if(titleCd!=null) { if(tab!=null) builder.append(tab); builder.append("titleCd(직책코드):").append(titleCd).append('\n'); }
		if(positCd!=null) { if(tab!=null) builder.append(tab); builder.append("positCd(직위코드):").append(positCd).append('\n'); }
		if(dutyCd!=null) { if(tab!=null) builder.append(tab); builder.append("dutyCd(직무코드):").append(dutyCd).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(aduTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("aduTypCd(겸직구분코드):").append(aduTypCd).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(endYmd!=null) { if(tab!=null) builder.append(tab); builder.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if(userStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("userStatCd(사용자상태코드):").append(userStatCd).append('\n'); }
		if(endRson!=null) { if(tab!=null) builder.append(tab); builder.append("endRson(종료사유):").append(endRson).append('\n'); }
		if(tichCont!=null) { if(tab!=null) builder.append(tab); builder.append("tichCont(담당업무내용):").append(tichCont).append('\n'); }
		if(signMthdCd!=null) { if(tab!=null) builder.append(tab); builder.append("signMthdCd(서명방법코드):").append(signMthdCd).append('\n'); }
		if(rid!=null) { if(tab!=null) builder.append(tab); builder.append("rid(참조ID):").append(rid).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(syncStepCd!=null) { if(tab!=null) builder.append(tab); builder.append("syncStepCd(동기화단계코드):").append(syncStepCd).append('\n'); }
		if(refVa1!=null) { if(tab!=null) builder.append(tab); builder.append("refVa1(참조값1):").append(refVa1).append('\n'); }
		if(refVa2!=null) { if(tab!=null) builder.append(tab); builder.append("refVa2(참조값2):").append(refVa2).append('\n'); }
		if(refVa3!=null) { if(tab!=null) builder.append(tab); builder.append("refVa3(참조값3):").append(refVa3).append('\n'); }
		if(autoApvLnCd!=null) { if(tab!=null) builder.append(tab); builder.append("autoApvLnCd(자동결재선코드):").append(autoApvLnCd).append('\n'); }
		if(sysUserYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysUserYn(시스템사용자여부):").append(sysUserYn).append('\n'); }
		if(sortOrdrOpt!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdrOpt(정렬순서옵션):").append(sortOrdrOpt).append('\n'); }
		if(roleCd!=null) { if(tab!=null) builder.append(tab); builder.append("roleCd(역할코드):").append(roleCd).append('\n'); }
		if(userUidList!=null) { if(tab!=null) builder.append(tab); builder.append("userUidList(사용자UID 목록):"); appendStringListTo(builder, userUidList, tab); builder.append('\n'); }
		if(orgIdList!=null) { if(tab!=null) builder.append(tab); builder.append("orgIdList(조직ID 목록):"); appendStringListTo(builder, orgIdList, tab); builder.append('\n'); }
		if(compIdList!=null) { if(tab!=null) builder.append(tab); builder.append("compIdList(회사ID 목록):"); appendStringListTo(builder, compIdList, tab); builder.append('\n'); }
		if(userStatCdList!=null) { if(tab!=null) builder.append(tab); builder.append("userStatCdList(사용자상태코드목록):"); appendStringListTo(builder, userStatCdList, tab); builder.append('\n'); }
		if(orOdurBVo!=null) { if(tab!=null) builder.append(tab); builder.append("orOdurBVo(원직자 VO):\n"); orOdurBVo.toString(builder, tab==null ? "\t" : tab+"\t"); }
		if(roleNms!=null) { if(tab!=null) builder.append(tab); builder.append("roleNms(역할명):").append(roleNms).append('\n'); }
		if(ein!=null) { if(tab!=null) builder.append(tab); builder.append("ein(사원번호):").append(ein).append('\n'); }
		if(lginId!=null) { if(tab!=null) builder.append(tab); builder.append("lginId(로그인ID):").append(lginId).append('\n'); }
		if(entraYmd!=null) { if(tab!=null) builder.append(tab); builder.append("entraYmd(입사년월일):").append(entraYmd).append('\n'); }
		if(minEntraYmd!=null) { if(tab!=null) builder.append(tab); builder.append("minEntraYmd(최소입사년월일):").append(minEntraYmd).append('\n'); }
		if(maxEntraYmd!=null) { if(tab!=null) builder.append(tab); builder.append("maxEntraYmd(입사년월일):").append(maxEntraYmd).append('\n'); }
		if(orgRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgRescNm(조직리소스명):").append(orgRescNm).append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(seculNm!=null) { if(tab!=null) builder.append(tab); builder.append("seculNm(보안등급명):").append(seculNm).append('\n'); }
		if(gradeNm!=null) { if(tab!=null) builder.append(tab); builder.append("gradeNm(직급명):").append(gradeNm).append('\n'); }
		if(titleNm!=null) { if(tab!=null) builder.append(tab); builder.append("titleNm(직책명):").append(titleNm).append('\n'); }
		if(positNm!=null) { if(tab!=null) builder.append(tab); builder.append("positNm(직위명):").append(positNm).append('\n'); }
		if(dutyNm!=null) { if(tab!=null) builder.append(tab); builder.append("dutyNm(직무명):").append(dutyNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(aduTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("aduTypNm(겸직구분명):").append(aduTypNm).append('\n'); }
		if(userStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("userStatNm(사용자상태명):").append(userStatNm).append('\n'); }
		if(signMthdNm!=null) { if(tab!=null) builder.append(tab); builder.append("signMthdNm(서명방법명):").append(signMthdNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if(autoApvLnNm!=null) { if(tab!=null) builder.append(tab); builder.append("autoApvLnNm(자동결재선명):").append(autoApvLnNm).append('\n'); }
		super.toString(builder, tab);
	}
}
