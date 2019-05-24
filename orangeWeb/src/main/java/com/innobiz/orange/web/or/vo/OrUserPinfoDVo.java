package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 사용자개인정보상세(OR_USER_PINFO_D) 테이블 VO
 */
public class OrUserPinfoDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5709012867700889667L;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 휴대전화번호암호값 */
	private String mbnoEnc;

	/** 이메일암호값 */
	private String emailEnc;

	/** 외부이메일암호값 */
	private String extnEmailEnc;

	/** 자택전화번호암호값 */
	private String homePhonEnc;

	/** 자택팩스번호암호값 */
	private String homeFnoEnc;

	/** 자택우편번호암호값 */
	private String homeZipNoEnc;

	/** 자택주소암호값 */
	private String homeAdrEnc;

	/** 자택상세주소암호값 */
	private String homeDetlAdrEnc;

	/** 회사전화번호암호값 */
	private String compPhonEnc;

	/** 회사팩스번호암호값 */
	private String compFnoEnc;

	/** 회사우편번호암호값 */
	private String compZipNoEnc;

	/** 회사주소암호값 */
	private String compAdrEnc;

	/** 회사상세주소암호값 */
	private String compDetlAdrEnc;

	/** 주민등록번호암호값 */
	private String ssnEnc;

	/** 홈페이지URL암호값 */
	private String hpageUrlEnc;


	// 추가컬럼
	/** 사용자UID */
	private String userUid;

	/** 원직자UID 목록 */
	private List<String> odurUidList;

	/** 회사ID */
	private String compId;

	/** 휴대전화번호 */
	private String mbno;

	/** 이메일 */
	private String email;

	/** 외부이메일 */
	private String extnEmail;

	/** 자택전화번호 */
	private String homePhon;

	/** 자택팩스번호 */
	private String homeFno;

	/** 자택우편번호 */
	private String homeZipNo;

	/** 자택주소 */
	private String homeAdr;

	/** 자택상세주소 */
	private String homeDetlAdr;

	/** 회사전화번호 */
	private String compPhon;

	/** 회사팩스번호 */
	private String compFno;

	/** 회사우편번호 */
	private String compZipNo;

	/** 회사주소 */
	private String compAdr;

	/** 회사상세주소 */
	private String compDetlAdr;

	/** 주민등록번호 */
	private String ssn;

	/** 홈페이지URL */
	private String hpageUrl;

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 휴대전화번호암호값 */
	public String getMbnoEnc() {
		return mbnoEnc;
	}

	/** 휴대전화번호암호값 */
	public void setMbnoEnc(String mbnoEnc) {
		this.mbnoEnc = mbnoEnc;
	}

	/** 이메일암호값 */
	public String getEmailEnc() {
		return emailEnc;
	}

	/** 이메일암호값 */
	public void setEmailEnc(String emailEnc) {
		this.emailEnc = emailEnc;
	}

	/** 외부이메일암호값 */
	public String getExtnEmailEnc() {
		return extnEmailEnc;
	}

	/** 외부이메일암호값 */
	public void setExtnEmailEnc(String extnEmailEnc) {
		this.extnEmailEnc = extnEmailEnc;
	}

	/** 자택전화번호암호값 */
	public String getHomePhonEnc() {
		return homePhonEnc;
	}

	/** 자택전화번호암호값 */
	public void setHomePhonEnc(String homePhonEnc) {
		this.homePhonEnc = homePhonEnc;
	}

	/** 자택팩스번호암호값 */
	public String getHomeFnoEnc() {
		return homeFnoEnc;
	}

	/** 자택팩스번호암호값 */
	public void setHomeFnoEnc(String homeFnoEnc) {
		this.homeFnoEnc = homeFnoEnc;
	}

	/** 자택우편번호암호값 */
	public String getHomeZipNoEnc() {
		return homeZipNoEnc;
	}

	/** 자택우편번호암호값 */
	public void setHomeZipNoEnc(String homeZipNoEnc) {
		this.homeZipNoEnc = homeZipNoEnc;
	}

	/** 자택주소암호값 */
	public String getHomeAdrEnc() {
		return homeAdrEnc;
	}

	/** 자택주소암호값 */
	public void setHomeAdrEnc(String homeAdrEnc) {
		this.homeAdrEnc = homeAdrEnc;
	}

	/** 자택상세주소암호값 */
	public String getHomeDetlAdrEnc() {
		return homeDetlAdrEnc;
	}

	/** 자택상세주소암호값 */
	public void setHomeDetlAdrEnc(String homeDetlAdrEnc) {
		this.homeDetlAdrEnc = homeDetlAdrEnc;
	}

	/** 회사전화번호암호값 */
	public String getCompPhonEnc() {
		return compPhonEnc;
	}

	/** 회사전화번호암호값 */
	public void setCompPhonEnc(String compPhonEnc) {
		this.compPhonEnc = compPhonEnc;
	}

	/** 회사팩스번호암호값 */
	public String getCompFnoEnc() {
		return compFnoEnc;
	}

	/** 회사팩스번호암호값 */
	public void setCompFnoEnc(String compFnoEnc) {
		this.compFnoEnc = compFnoEnc;
	}

	/** 회사우편번호암호값 */
	public String getCompZipNoEnc() {
		return compZipNoEnc;
	}

	/** 회사우편번호암호값 */
	public void setCompZipNoEnc(String compZipNoEnc) {
		this.compZipNoEnc = compZipNoEnc;
	}

	/** 회사주소암호값 */
	public String getCompAdrEnc() {
		return compAdrEnc;
	}

	/** 회사주소암호값 */
	public void setCompAdrEnc(String compAdrEnc) {
		this.compAdrEnc = compAdrEnc;
	}

	/** 회사상세주소암호값 */
	public String getCompDetlAdrEnc() {
		return compDetlAdrEnc;
	}

	/** 회사상세주소암호값 */
	public void setCompDetlAdrEnc(String compDetlAdrEnc) {
		this.compDetlAdrEnc = compDetlAdrEnc;
	}

	/** 주민등록번호암호값 */
	public String getSsnEnc() {
		return ssnEnc;
	}

	/** 주민등록번호암호값 */
	public void setSsnEnc(String ssnEnc) {
		this.ssnEnc = ssnEnc;
	}

	/** 홈페이지URL암호값 */
	public String getHpageUrlEnc() {
		return hpageUrlEnc;
	}

	/** 홈페이지URL암호값 */
	public void setHpageUrlEnc(String hpageUrlEnc) {
		this.hpageUrlEnc = hpageUrlEnc;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 원직자UID 목록 */
	public List<String> getOdurUidList() {
		return odurUidList;
	}

	/** 원직자UID 목록 */
	public void setOdurUidList(List<String> odurUidList) {
		this.odurUidList = odurUidList;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 휴대전화번호 */
	public String getMbno() {
		return mbno;
	}

	/** 휴대전화번호 */
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}

	/** 이메일 */
	public String getEmail() {
		return email;
	}

	/** 이메일 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** 외부이메일 */
	public String getExtnEmail() {
		return extnEmail;
	}

	/** 외부이메일 */
	public void setExtnEmail(String extnEmail) {
		this.extnEmail = extnEmail;
	}

	/** 자택전화번호 */
	public String getHomePhon() {
		return homePhon;
	}

	/** 자택전화번호 */
	public void setHomePhon(String homePhon) {
		this.homePhon = homePhon;
	}

	/** 자택팩스번호 */
	public String getHomeFno() {
		return homeFno;
	}

	/** 자택팩스번호 */
	public void setHomeFno(String homeFno) {
		this.homeFno = homeFno;
	}

	/** 자택우편번호 */
	public String getHomeZipNo() {
		return homeZipNo;
	}

	/** 자택우편번호 */
	public void setHomeZipNo(String homeZipNo) {
		this.homeZipNo = homeZipNo;
	}

	/** 자택주소 */
	public String getHomeAdr() {
		return homeAdr;
	}

	/** 자택주소 */
	public void setHomeAdr(String homeAdr) {
		this.homeAdr = homeAdr;
	}

	/** 자택상세주소 */
	public String getHomeDetlAdr() {
		return homeDetlAdr;
	}

	/** 자택상세주소 */
	public void setHomeDetlAdr(String homeDetlAdr) {
		this.homeDetlAdr = homeDetlAdr;
	}

	/** 회사전화번호 */
	public String getCompPhon() {
		return compPhon;
	}

	/** 회사전화번호 */
	public void setCompPhon(String compPhon) {
		this.compPhon = compPhon;
	}

	/** 회사팩스번호 */
	public String getCompFno() {
		return compFno;
	}

	/** 회사팩스번호 */
	public void setCompFno(String compFno) {
		this.compFno = compFno;
	}

	/** 회사우편번호 */
	public String getCompZipNo() {
		return compZipNo;
	}

	/** 회사우편번호 */
	public void setCompZipNo(String compZipNo) {
		this.compZipNo = compZipNo;
	}

	/** 회사주소 */
	public String getCompAdr() {
		return compAdr;
	}

	/** 회사주소 */
	public void setCompAdr(String compAdr) {
		this.compAdr = compAdr;
	}

	/** 회사상세주소 */
	public String getCompDetlAdr() {
		return compDetlAdr;
	}

	/** 회사상세주소 */
	public void setCompDetlAdr(String compDetlAdr) {
		this.compDetlAdr = compDetlAdr;
	}

	/** 주민등록번호 */
	public String getSsn() {
		return ssn;
	}

	/** 주민등록번호 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/** 홈페이지URL */
	public String getHpageUrl() {
		return hpageUrl;
	}

	/** 홈페이지URL */
	public void setHpageUrl(String hpageUrl) {
		this.hpageUrl = hpageUrl;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPinfoDDao.selectOrUserPinfoD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPinfoDDao.insertOrUserPinfoD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPinfoDDao.updateOrUserPinfoD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPinfoDDao.deleteOrUserPinfoD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserPinfoDDao.countOrUserPinfoD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자개인정보상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(mbnoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("mbnoEnc(휴대전화번호암호값):").append(mbnoEnc).append('\n'); }
		if(emailEnc!=null) { if(tab!=null) builder.append(tab); builder.append("emailEnc(이메일암호값):").append(emailEnc).append('\n'); }
		if(extnEmailEnc!=null) { if(tab!=null) builder.append(tab); builder.append("extnEmailEnc(외부이메일암호값):").append(extnEmailEnc).append('\n'); }
		if(homePhonEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homePhonEnc(자택전화번호암호값):").append(homePhonEnc).append('\n'); }
		if(homeFnoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeFnoEnc(자택팩스번호암호값):").append(homeFnoEnc).append('\n'); }
		if(homeZipNoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeZipNoEnc(자택우편번호암호값):").append(homeZipNoEnc).append('\n'); }
		if(homeAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeAdrEnc(자택주소암호값):").append(homeAdrEnc).append('\n'); }
		if(homeDetlAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeDetlAdrEnc(자택상세주소암호값):").append(homeDetlAdrEnc).append('\n'); }
		if(compPhonEnc!=null) { if(tab!=null) builder.append(tab); builder.append("compPhonEnc(회사전화번호암호값):").append(compPhonEnc).append('\n'); }
		if(compFnoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("compFnoEnc(회사팩스번호암호값):").append(compFnoEnc).append('\n'); }
		if(compZipNoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("compZipNoEnc(회사우편번호암호값):").append(compZipNoEnc).append('\n'); }
		if(compAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("compAdrEnc(회사주소암호값):").append(compAdrEnc).append('\n'); }
		if(compDetlAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("compDetlAdrEnc(회사상세주소암호값):").append(compDetlAdrEnc).append('\n'); }
		if(ssnEnc!=null) { if(tab!=null) builder.append(tab); builder.append("ssnEnc(주민등록번호암호값):").append(ssnEnc).append('\n'); }
		if(hpageUrlEnc!=null) { if(tab!=null) builder.append(tab); builder.append("hpageUrlEnc(홈페이지URL암호값):").append(hpageUrlEnc).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(odurUidList!=null) { if(tab!=null) builder.append(tab); builder.append("odurUidList(원직자UID 목록):"); appendStringListTo(builder, odurUidList, tab); builder.append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(mbno!=null) { if(tab!=null) builder.append(tab); builder.append("mbno(휴대전화번호):").append(mbno).append('\n'); }
		if(email!=null) { if(tab!=null) builder.append(tab); builder.append("email(이메일):").append(email).append('\n'); }
		if(extnEmail!=null) { if(tab!=null) builder.append(tab); builder.append("extnEmail(외부이메일):").append(extnEmail).append('\n'); }
		if(homePhon!=null) { if(tab!=null) builder.append(tab); builder.append("homePhon(자택전화번호):").append(homePhon).append('\n'); }
		if(homeFno!=null) { if(tab!=null) builder.append(tab); builder.append("homeFno(자택팩스번호):").append(homeFno).append('\n'); }
		if(homeZipNo!=null) { if(tab!=null) builder.append(tab); builder.append("homeZipNo(자택우편번호):").append(homeZipNo).append('\n'); }
		if(homeAdr!=null) { if(tab!=null) builder.append(tab); builder.append("homeAdr(자택주소):").append(homeAdr).append('\n'); }
		if(homeDetlAdr!=null) { if(tab!=null) builder.append(tab); builder.append("homeDetlAdr(자택상세주소):").append(homeDetlAdr).append('\n'); }
		if(compPhon!=null) { if(tab!=null) builder.append(tab); builder.append("compPhon(회사전화번호):").append(compPhon).append('\n'); }
		if(compFno!=null) { if(tab!=null) builder.append(tab); builder.append("compFno(회사팩스번호):").append(compFno).append('\n'); }
		if(compZipNo!=null) { if(tab!=null) builder.append(tab); builder.append("compZipNo(회사우편번호):").append(compZipNo).append('\n'); }
		if(compAdr!=null) { if(tab!=null) builder.append(tab); builder.append("compAdr(회사주소):").append(compAdr).append('\n'); }
		if(compDetlAdr!=null) { if(tab!=null) builder.append(tab); builder.append("compDetlAdr(회사상세주소):").append(compDetlAdr).append('\n'); }
		if(ssn!=null) { if(tab!=null) builder.append(tab); builder.append("ssn(주민등록번호):").append(ssn).append('\n'); }
		if(hpageUrl!=null) { if(tab!=null) builder.append(tab); builder.append("hpageUrl(홈페이지URL):").append(hpageUrl).append('\n'); }
		super.toString(builder, tab);
	}
}
