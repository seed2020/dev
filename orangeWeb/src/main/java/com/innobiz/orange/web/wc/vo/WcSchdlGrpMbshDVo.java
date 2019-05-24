package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 일정 그룹 회원 상세(WC_SCHDL_GRP_MBSH_D)테이블 VO
 */
public class WcSchdlGrpMbshDVo  extends CommonVoImpl {	
	
	/** serialVersionUID. */
	private static final long serialVersionUID = -719174028691090384L;

	/** 사용자 UID */
	private String userUid;

	/** 일정 그룹ID */
	private String schdlGrpId;

	/** 회사ID */
	private String compId;
	
	/** 사용자명 */
	private String userNm;


	/** 회원구분코드 */
	private String mbshTypCd;

	/** 권한등급코드 */
	private String authGradCd;

	/** 이메일발송여부 */
	private String emailSendYn;
	
	
	// 추가 컬럼
	/** 그룹 멤버 목록*/
	private String[] grpMbshList;
	
	/** 부서리소스명 */
	private String deptRescNm;
	
	/** 직위명 */
	private String gradeNm;
	
	/** 전화번호*/
	private String mbno;
	
	/** 원직자UID */
	private String odurUid;

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

	/** 리소스ID */
	private String rescId;

	/** 겸직구분코드 - 01:원직, 02:겸직, 03:파견직 */
	private String aduTypCd;

	/** 시작년월일 */
	private String strtYmd;

	/** 종료년월일 */
	private String endYmd;

	/** 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제 */
	private String userStatCd;

	/** 종료사유 */
	private String endRson;

	/** 담당업무내용 */
	private String tichCont;

	/** 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자) */
	private String signMthdCd;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;


	/** 사용자 UID */
	public String getUserUid() {
		return userUid;
	}
	
	/** 사용자 UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 일정 그룹ID */
	public String getSchdlGrpId() {
		return schdlGrpId;
	}

	/** 일정 그룹ID */
	public void setSchdlGrpId(String schdlGrpId) {
		this.schdlGrpId = schdlGrpId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 회원구분코드 */
	public String getMbshTypCd() {
		return mbshTypCd;
	}

	/** 회원구분코드 */
	public void setMbshTypCd(String mbshTypCd) {
		this.mbshTypCd = mbshTypCd;
	}

	/** 권한등급코드 */
	public String getAuthGradCd() {
		return authGradCd;
	}

	/** 권한등급코드 */
	public void setAuthGradCd(String authGradCd) {
		this.authGradCd = authGradCd;
	}
	
	/** 이메일발송여부 */
	public String getEmailSendYn() {
		return emailSendYn;
	}

	/** 이메일발송여부 */
	public void setEmailSendYn(String emailSendYn) {
		this.emailSendYn = emailSendYn;
	}
	
	// 추가 컬럼 Getter, Setter
	public String getOdurUid() {
		return odurUid;
	}

	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgRescId() {
		return orgRescId;
	}

	public void setOrgRescId(String orgRescId) {
		this.orgRescId = orgRescId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptRescId() {
		return deptRescId;
	}

	public void setDeptRescId(String deptRescId) {
		this.deptRescId = deptRescId;
	}

	public String getSortOrdr() {
		return sortOrdr;
	}

	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	public String getSeculCd() {
		return seculCd;
	}

	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}

	public String getGradeCd() {
		return gradeCd;
	}

	public void setGradeCd(String gradeCd) {
		this.gradeCd = gradeCd;
	}

	public String getTitleCd() {
		return titleCd;
	}

	public void setTitleCd(String titleCd) {
		this.titleCd = titleCd;
	}

	public String getPositCd() {
		return positCd;
	}

	public void setPositCd(String positCd) {
		this.positCd = positCd;
	}

	public String getDutyCd() {
		return dutyCd;
	}

	public void setDutyCd(String dutyCd) {
		this.dutyCd = dutyCd;
	}

	public String getRescId() {
		return rescId;
	}

	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	public String getAduTypCd() {
		return aduTypCd;
	}

	public void setAduTypCd(String aduTypCd) {
		this.aduTypCd = aduTypCd;
	}

	public String getStrtYmd() {
		return strtYmd;
	}

	public void setStrtYmd(String strtYmd) {
		this.strtYmd = strtYmd;
	}

	public String getEndYmd() {
		return endYmd;
	}

	public void setEndYmd(String endYmd) {
		this.endYmd = endYmd;
	}

	public String getUserStatCd() {
		return userStatCd;
	}

	public void setUserStatCd(String userStatCd) {
		this.userStatCd = userStatCd;
	}

	public String getEndRson() {
		return endRson;
	}

	public void setEndRson(String endRson) {
		this.endRson = endRson;
	}

	public String getTichCont() {
		return tichCont;
	}

	public void setTichCont(String tichCont) {
		this.tichCont = tichCont;
	}

	public String getSignMthdCd() {
		return signMthdCd;
	}

	public void setSignMthdCd(String signMthdCd) {
		this.signMthdCd = signMthdCd;
	}

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	public String getLangTypCd() {
		return langTypCd;
	}

	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	public String getRescVa() {
		return rescVa;
	}

	public void setRescVa(String rescVa) {
		this.rescVa = rescVa;
	}

	public String getDeptRescNm() {
		return deptRescNm;
	}
	
	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}
	
	public String getGradeNm() {
		return gradeNm;
	}
	
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}
	
	public String getMbno() {
		return mbno;
	}
	
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}
	
	public String[] getGrpMbshList() {
		return grpMbshList;
	}
	
	public void setGrpMbshList(String[] grpMbshList) {
		this.grpMbshList = grpMbshList;
	}
	
	

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 그룹 회원 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자 UID):").append(userUid).append('\n'); }
		if(schdlGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlGrpId(일정 그룹ID):").append(schdlGrpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(mbshTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mbshTypCd(회원구분코드):").append(mbshTypCd).append('\n'); }
		if(authGradCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGradCd(권한등급코드):").append(authGradCd).append('\n'); }
		if(emailSendYn!=null) { if(tab!=null) builder.append(tab); builder.append("emailSendYn(이메일발송여부):").append(emailSendYn).append('\n'); }
		if(grpMbshList!=null) { if(tab!=null) builder.append(tab); builder.append("grpMbshList(그룹 멤버 목록):"); appendArrayTo(builder, grpMbshList); builder.append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(gradeNm!=null) { if(tab!=null) builder.append(tab); builder.append("gradeNm(직위명):").append(gradeNm).append('\n'); }
		if(mbno!=null) { if(tab!=null) builder.append(tab); builder.append("mbno(전화번호):").append(mbno).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID):").append(odurUid).append('\n'); }
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
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(aduTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("aduTypCd(겸직구분코드):").append(aduTypCd).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(endYmd!=null) { if(tab!=null) builder.append(tab); builder.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if(userStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("userStatCd(사용자상태코드):").append(userStatCd).append('\n'); }
		if(endRson!=null) { if(tab!=null) builder.append(tab); builder.append("endRson(종료사유):").append(endRson).append('\n'); }
		if(tichCont!=null) { if(tab!=null) builder.append(tab); builder.append("tichCont(담당업무내용):").append(tichCont).append('\n'); }
		if(signMthdCd!=null) { if(tab!=null) builder.append(tab); builder.append("signMthdCd(서명방법코드):").append(signMthdCd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}