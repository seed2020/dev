package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 일정 그룹 기본(WC_SCHDL_GRP_B)테이블 VO
 */
public class WcSchdlGrpBVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = 1977245403963899913L;
	
	/** 일정 그룹ID */
	private String schdlGrpId;

	/** 회사ID */
	private String compId;

	/** 그룹명 */
	private String grpNm;

	/** 등록자UID */
	private String regrUid;
	
	/** 등록자명 */
	private String regrNm;

	/** 마스터UID */
	private String mastrUid;

	/** 등록일자 */
	private String regDt;
	
	/** 사용자 UID */
	private String userUid;
	
	/** 사용자명 */
	private String userNm;

	/** 회원구분코드 */
	private String mbshTypCd;

	/** 권한등급코드 */
	private String authGradCd;

	/** 이메일발송여부 */
	private String emailSendYn;
	
	/** 그룹구분 */
	private String fncMy;
	
	/** 권한 */
	private String auth;
	

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getSchdlGrpId() {
		return schdlGrpId;
	}

	public void setSchdlGrpId(String schdlGrpId) {
		this.schdlGrpId = schdlGrpId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getGrpNm() {
		return grpNm;
	}

	public void setGrpNm(String grpNm) {
		this.grpNm = grpNm;
	}

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}
	
	
	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	public String getMastrUid() {
		return mastrUid;
	}

	public void setMastrUid(String mastrUid) {
		this.mastrUid = mastrUid;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	
	/** 사용자 UID */
	public String getUserUid() {
		return userUid;
	}
	
	/** 사용자 UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
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
	
	
	/** 그룹구분 */
	public String getFncMy() {
		return fncMy;
	}

	public void setFncMy(String fncMy) {
		this.fncMy = fncMy;
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		builder.append('[').append(this.getClass().getName()).append(":일정 그룹 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(schdlGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlGrpId(일정 그룹ID):").append(schdlGrpId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(grpNm!=null) { if(tab!=null) builder.append(tab); builder.append("grpNm(그룹명):").append(grpNm).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(mastrUid!=null) { if(tab!=null) builder.append(tab); builder.append("mastrUid(마스터UID):").append(mastrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일자):").append(regDt).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자 UID):").append(userUid).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(mbshTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mbshTypCd(회원구분코드):").append(mbshTypCd).append('\n'); }
		if(authGradCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGradCd(권한등급코드):").append(authGradCd).append('\n'); }
		if(emailSendYn!=null) { if(tab!=null) builder.append(tab); builder.append("emailSendYn(이메일발송여부):").append(emailSendYn).append('\n'); }
		if(fncMy!=null) { if(tab!=null) builder.append(tab); builder.append("fncMy(그룹구분):").append(fncMy).append('\n'); }
		if(auth!=null) { if(tab!=null) builder.append(tab); builder.append("auth(권한):").append(auth).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}