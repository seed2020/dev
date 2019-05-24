package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_PROM_GUEST_D[커뮤니티 약속 참석자 상세] */
public class CtPromGuestDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1399899384472049038L;

	/** 일정ID */
	private String schdlId;

	/** 참석자UID */
	private String guestUid;

	/** 참석자 성명 */
	private String guestNm;

	/** 참석자 직위명 */
	private String guestPositNm;

	/** 참석자 부서명 */
	private String guestDeptNm;

	/** 참석자 회사명 */
	private String guestCompNm;

	/** 상태 코드 */
	private String statCd;

	/** 의견내용 */
	private String opinCont;

	/** 참석자_임직원 여부 */
	private String guestEmplYn;

	/** 이메일 */
	private String email;

	/** 이메일발송여부 */
	private String emailSendYn;

	public String getSchdlId() {
		return schdlId;
	}

	public void setSchdlId(String schdlId) {
		this.schdlId = schdlId;
	}

	public String getGuestUid() {
		return guestUid;
	}

	public void setGuestUid(String guestUid) {
		this.guestUid = guestUid;
	}

	public String getGuestNm() {
		return guestNm;
	}

	public void setGuestNm(String guestNm) {
		this.guestNm = guestNm;
	}

	public String getGuestPositNm() {
		return guestPositNm;
	}

	public void setGuestPositNm(String guestPositNm) {
		this.guestPositNm = guestPositNm;
	}

	public String getGuestDeptNm() {
		return guestDeptNm;
	}

	public void setGuestDeptNm(String guestDeptNm) {
		this.guestDeptNm = guestDeptNm;
	}

	public String getGuestCompNm() {
		return guestCompNm;
	}

	public void setGuestCompNm(String guestCompNm) {
		this.guestCompNm = guestCompNm;
	}

	public String getStatCd() {
		return statCd;
	}

	public void setStatCd(String statCd) {
		this.statCd = statCd;
	}

	public String getOpinCont() {
		return opinCont;
	}

	public void setOpinCont(String opinCont) {
		this.opinCont = opinCont;
	}

	public String getGuestEmplYn() {
		return guestEmplYn;
	}

	public void setGuestEmplYn(String guestEmplYn) {
		this.guestEmplYn = guestEmplYn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailSendYn() {
		return emailSendYn;
	}

	public void setEmailSendYn(String emailSendYn) {
		this.emailSendYn = emailSendYn;
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
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(guestUid!=null) { if(tab!=null) builder.append(tab); builder.append("guestUid(참석자UID):").append(guestUid).append('\n'); }
		if(guestNm!=null) { if(tab!=null) builder.append(tab); builder.append("guestNm(참석자 성명):").append(guestNm).append('\n'); }
		if(guestPositNm!=null) { if(tab!=null) builder.append(tab); builder.append("guestPositNm(참석자 직위명):").append(guestPositNm).append('\n'); }
		if(guestDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("guestDeptNm(참석자 부서명):").append(guestDeptNm).append('\n'); }
		if(guestCompNm!=null) { if(tab!=null) builder.append(tab); builder.append("guestCompNm(참석자 회사명):").append(guestCompNm).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태 코드):").append(statCd).append('\n'); }
		if(opinCont!=null) { if(tab!=null) builder.append(tab); builder.append("opinCont(의견내용):").append(opinCont).append('\n'); }
		if(guestEmplYn!=null) { if(tab!=null) builder.append(tab); builder.append("guestEmplYn(참석자_임직원 여부):").append(guestEmplYn).append('\n'); }
		if(email!=null) { if(tab!=null) builder.append(tab); builder.append("email(이메일):").append(email).append('\n'); }
		if(emailSendYn!=null) { if(tab!=null) builder.append(tab); builder.append("emailSendYn(이메일발송여부):").append(emailSendYn).append('\n'); }
		super.toString(builder, tab);
	}
	



}