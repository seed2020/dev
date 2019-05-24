package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 인력기본(WP_MP_B) 테이블 VO
 */
public class WpMpBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1268028033318189838L;

	/** 인력ID - KEY */
	private String mpId;

	/** 인력명 */
	private String mpNm;

	/** 인력직급 */
	private String mpGrade;

	/** 인력전화번호 */
	private String mpPhone;

	/** 인력이메일 */
	private String mpEmail;

	/** 등록일 */
	private String regDt;

	/** 등록자UID */
	private String regUid;

	/** 인력ID - KEY */
	public String getMpId() {
		return mpId;
	}

	/** 인력ID - KEY */
	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** 인력명 */
	public String getMpNm() {
		return mpNm;
	}

	/** 인력명 */
	public void setMpNm(String mpNm) {
		this.mpNm = mpNm;
	}

	/** 인력직급 */
	public String getMpGrade() {
		return mpGrade;
	}

	/** 인력직급 */
	public void setMpGrade(String mpGrade) {
		this.mpGrade = mpGrade;
	}

	/** 인력전화번호 */
	public String getMpPhone() {
		return mpPhone;
	}

	/** 인력전화번호 */
	public void setMpPhone(String mpPhone) {
		this.mpPhone = mpPhone;
	}

	/** 인력이메일 */
	public String getMpEmail() {
		return mpEmail;
	}

	/** 인력이메일 */
	public void setMpEmail(String mpEmail) {
		this.mpEmail = mpEmail;
	}

	/** 등록일 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 등록자UID */
	public String getRegUid() {
		return regUid;
	}

	/** 등록자UID */
	public void setRegUid(String regUid) {
		this.regUid = regUid;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpMpBDao.selectWpMpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpMpBDao.insertWpMpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpMpBDao.updateWpMpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpMpBDao.deleteWpMpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpMpBDao.countWpMpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":인력기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		if(mpNm!=null) { if(tab!=null) builder.append(tab); builder.append("mpNm(인력명):").append(mpNm).append('\n'); }
		if(mpGrade!=null) { if(tab!=null) builder.append(tab); builder.append("mpGrade(인력직급):").append(mpGrade).append('\n'); }
		if(mpPhone!=null) { if(tab!=null) builder.append(tab); builder.append("mpPhone(인력전화번호):").append(mpPhone).append('\n'); }
		if(mpEmail!=null) { if(tab!=null) builder.append(tab); builder.append("mpEmail(인력이메일):").append(mpEmail).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일):").append(regDt).append('\n'); }
		if(regUid!=null) { if(tab!=null) builder.append(tab); builder.append("regUid(등록자UID):").append(regUid).append('\n'); }
		super.toString(builder, tab);
	}
}
