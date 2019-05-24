package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원카드결재라인상세(WO_ONEC_APV_LN_D) 테이블 VO
 */
public class WoOnecApvLnDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4528279554229398713L;

	/** 원카드번호 - KEY */
	private String onecNo;

	/** 버전 - KEY */
	private String ver;

	/** 결재라인부모번호 - KEY */
	private String apvLnPno;

	/** 결재라인번호 - KEY */
	private String apvLnNo;

	/** 결재자역할코드 */
	private String apvrRoleCd;

	/** 결재상태코드 */
	private String apvStatCd;

	/** 부재사유코드 */
	private String absRsonCd;

	/** 부재사유명 */
	private String absRsonNm;

	/** 전결여부 */
	private String predYn;

	/** 대결자UID */
	private String agntUid;

	/** 결재자UID */
	private String apvrUid;

	/** 결재자명 */
	private String apvrNm;

	/** 직위표시값 */
	private String positDispVa;

	/** 서명표시값 */
	private String signDispVa;

	/** 일시표시값 */
	private String dtDispVa;

	/** 서명이미지경로 */
	private String signImgPath;

	/** 결재일시 */
	private String apvDt;


	// 추가컬럼
	/** 대결자명 */
	private String agntNm;

	/** 원카드번호 - KEY */
	public String getOnecNo() {
		return onecNo;
	}

	/** 원카드번호 - KEY */
	public void setOnecNo(String onecNo) {
		this.onecNo = onecNo;
	}

	/** 버전 - KEY */
	public String getVer() {
		return ver;
	}

	/** 버전 - KEY */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/** 결재라인부모번호 - KEY */
	public String getApvLnPno() {
		return apvLnPno;
	}

	/** 결재라인부모번호 - KEY */
	public void setApvLnPno(String apvLnPno) {
		this.apvLnPno = apvLnPno;
	}

	/** 결재라인번호 - KEY */
	public String getApvLnNo() {
		return apvLnNo;
	}

	/** 결재라인번호 - KEY */
	public void setApvLnNo(String apvLnNo) {
		this.apvLnNo = apvLnNo;
	}

	/** 결재자역할코드 */
	public String getApvrRoleCd() {
		return apvrRoleCd;
	}

	/** 결재자역할코드 */
	public void setApvrRoleCd(String apvrRoleCd) {
		this.apvrRoleCd = apvrRoleCd;
	}

	/** 결재상태코드 */
	public String getApvStatCd() {
		return apvStatCd;
	}

	/** 결재상태코드 */
	public void setApvStatCd(String apvStatCd) {
		this.apvStatCd = apvStatCd;
	}

	/** 부재사유코드 */
	public String getAbsRsonCd() {
		return absRsonCd;
	}

	/** 부재사유코드 */
	public void setAbsRsonCd(String absRsonCd) {
		this.absRsonCd = absRsonCd;
	}

	/** 부재사유명 */
	public String getAbsRsonNm() {
		return absRsonNm;
	}

	/** 부재사유명 */
	public void setAbsRsonNm(String absRsonNm) {
		this.absRsonNm = absRsonNm;
	}

	/** 전결여부 */
	public String getPredYn() {
		return predYn;
	}

	/** 전결여부 */
	public void setPredYn(String predYn) {
		this.predYn = predYn;
	}

	/** 대결자UID */
	public String getAgntUid() {
		return agntUid;
	}

	/** 대결자UID */
	public void setAgntUid(String agntUid) {
		this.agntUid = agntUid;
	}

	/** 결재자UID */
	public String getApvrUid() {
		return apvrUid;
	}

	/** 결재자UID */
	public void setApvrUid(String apvrUid) {
		this.apvrUid = apvrUid;
	}

	/** 결재자명 */
	public String getApvrNm() {
		return apvrNm;
	}

	/** 결재자명 */
	public void setApvrNm(String apvrNm) {
		this.apvrNm = apvrNm;
	}

	/** 직위표시값 */
	public String getPositDispVa() {
		return positDispVa;
	}

	/** 직위표시값 */
	public void setPositDispVa(String positDispVa) {
		this.positDispVa = positDispVa;
	}

	/** 서명표시값 */
	public String getSignDispVa() {
		return signDispVa;
	}

	/** 서명표시값 */
	public void setSignDispVa(String signDispVa) {
		this.signDispVa = signDispVa;
	}

	/** 일시표시값 */
	public String getDtDispVa() {
		return dtDispVa;
	}

	/** 일시표시값 */
	public void setDtDispVa(String dtDispVa) {
		this.dtDispVa = dtDispVa;
	}

	/** 서명이미지경로 */
	public String getSignImgPath() {
		return signImgPath;
	}

	/** 서명이미지경로 */
	public void setSignImgPath(String signImgPath) {
		this.signImgPath = signImgPath;
	}

	/** 결재일시 */
	public String getApvDt() {
		return apvDt;
	}

	/** 결재일시 */
	public void setApvDt(String apvDt) {
		this.apvDt = apvDt;
	}

	/** 대결자명 */
	public String getAgntNm() {
		return agntNm;
	}

	/** 대결자명 */
	public void setAgntNm(String agntNm) {
		this.agntNm = agntNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecApvLnDDao.selectWoOnecApvLnD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecApvLnDDao.insertWoOnecApvLnD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecApvLnDDao.updateWoOnecApvLnD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecApvLnDDao.deleteWoOnecApvLnD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecApvLnDDao.countWoOnecApvLnD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드결재라인상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(onecNo!=null) { if(tab!=null) builder.append(tab); builder.append("onecNo(원카드번호-PK):").append(onecNo).append('\n'); }
		if(ver!=null) { if(tab!=null) builder.append(tab); builder.append("ver(버전-PK):").append(ver).append('\n'); }
		if(apvLnPno!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnPno(결재라인부모번호-PK):").append(apvLnPno).append('\n'); }
		if(apvLnNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnNo(결재라인번호-PK):").append(apvLnNo).append('\n'); }
		if(apvrRoleCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCd(결재자역할코드):").append(apvrRoleCd).append('\n'); }
		if(apvStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatCd(결재상태코드):").append(apvStatCd).append('\n'); }
		if(absRsonCd!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonCd(부재사유코드):").append(absRsonCd).append('\n'); }
		if(absRsonNm!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonNm(부재사유명):").append(absRsonNm).append('\n'); }
		if(predYn!=null) { if(tab!=null) builder.append(tab); builder.append("predYn(전결여부):").append(predYn).append('\n'); }
		if(agntUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntUid(대결자UID):").append(agntUid).append('\n'); }
		if(apvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUid(결재자UID):").append(apvrUid).append('\n'); }
		if(apvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrNm(결재자명):").append(apvrNm).append('\n'); }
		if(positDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("positDispVa(직위표시값):").append(positDispVa).append('\n'); }
		if(signDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("signDispVa(서명표시값):").append(signDispVa).append('\n'); }
		if(dtDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("dtDispVa(일시표시값):").append(dtDispVa).append('\n'); }
		if(signImgPath!=null) { if(tab!=null) builder.append(tab); builder.append("signImgPath(서명이미지경로):").append(signImgPath).append('\n'); }
		if(apvDt!=null) { if(tab!=null) builder.append(tab); builder.append("apvDt(결재일시):").append(apvDt).append('\n'); }
		if(agntNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntNm(대결자명):").append(agntNm).append('\n'); }
		super.toString(builder, tab);
	}
}
