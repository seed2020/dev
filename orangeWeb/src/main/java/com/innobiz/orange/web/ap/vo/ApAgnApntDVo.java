package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 대리지정상세(AP_AGN_APNT_D) 테이블 VO
 */
public class ApAgnApntDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4565546409665941985L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 대리인UID - KEY */
	private String agntUid;

	/** 일련번호 - KEY */
	private String seq;

	/** 종료일시 */
	private String endDt;

	/** 시작일시 */
	private String strtDt;

	/** 대리인리소스ID */
	private String agntRescId;

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	private String absRsonCd;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 대리인명 */
	private String agntNm;

	/** 대리인리소스명 */
	private String agntRescNm;

	/** 부재사유명 */
	private String absRsonNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 대리인UID - KEY */
	public String getAgntUid() {
		return agntUid;
	}

	/** 대리인UID - KEY */
	public void setAgntUid(String agntUid) {
		this.agntUid = agntUid;
	}

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 종료일시 */
	public String getEndDt() {
		return endDt;
	}

	/** 종료일시 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	/** 시작일시 */
	public String getStrtDt() {
		return strtDt;
	}

	/** 시작일시 */
	public void setStrtDt(String strtDt) {
		this.strtDt = strtDt;
	}

	/** 대리인리소스ID */
	public String getAgntRescId() {
		return agntRescId;
	}

	/** 대리인리소스ID */
	public void setAgntRescId(String agntRescId) {
		this.agntRescId = agntRescId;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public String getAbsRsonCd() {
		return absRsonCd;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public void setAbsRsonCd(String absRsonCd) {
		this.absRsonCd = absRsonCd;
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

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 대리인명 */
	public String getAgntNm() {
		return agntNm;
	}

	/** 대리인명 */
	public void setAgntNm(String agntNm) {
		this.agntNm = agntNm;
	}

	/** 대리인리소스명 */
	public String getAgntRescNm() {
		return agntRescNm;
	}

	/** 대리인리소스명 */
	public void setAgntRescNm(String agntRescNm) {
		this.agntRescNm = agntRescNm;
	}

	/** 부재사유명 */
	public String getAbsRsonNm() {
		return absRsonNm;
	}

	/** 부재사유명 */
	public void setAbsRsonNm(String absRsonNm) {
		this.absRsonNm = absRsonNm;
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
			return "com.innobiz.orange.web.ap.dao.ApAgnApntDDao.selectApAgnApntD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntDDao.insertApAgnApntD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntDDao.updateApAgnApntD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntDDao.deleteApAgnApntD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApAgnApntDDao.countApAgnApntD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":대리지정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(agntUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntUid(대리인UID-PK):").append(agntUid).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일시):").append(endDt).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일시):").append(strtDt).append('\n'); }
		if(agntRescId!=null) { if(tab!=null) builder.append(tab); builder.append("agntRescId(대리인리소스ID):").append(agntRescId).append('\n'); }
		if(absRsonCd!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonCd(부재사유코드):").append(absRsonCd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(agntNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntNm(대리인명):").append(agntNm).append('\n'); }
		if(agntRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntRescNm(대리인리소스명):").append(agntRescNm).append('\n'); }
		if(absRsonNm!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonNm(부재사유명):").append(absRsonNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
