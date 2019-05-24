package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 푸쉬앱상세(PT_PUSH_APP_D) 테이블 VO
 */
public class PtPushAppDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4860720114891372344L;

	/** 고객코드 - KEY */
	private String custCd;

	/** 일련번호 - KEY */
	private String seq;

	/** 고객명 */
	private String custNm;

	/** 시작년월일 */
	private String strtYmd;

	/** 종료년월일 */
	private String endYmd;

	/** IPA경로 */
	private String ipaPath;

	/** APK경로 */
	private String apkPath;

	/** 표시명 */
	private String dispNm;

	/** 설명내용 */
	private String descCont;

	/** 등록일시 */
	private String regDt;

	/** 사용여부 */
	private String useYn;

	/** 고객코드 - KEY */
	public String getCustCd() {
		return custCd;
	}

	/** 고객코드 - KEY */
	public void setCustCd(String custCd) {
		this.custCd = custCd;
	}

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 고객명 */
	public String getCustNm() {
		return custNm;
	}

	/** 고객명 */
	public void setCustNm(String custNm) {
		this.custNm = custNm;
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

	/** IPA경로 */
	public String getIpaPath() {
		return ipaPath;
	}

	/** IPA경로 */
	public void setIpaPath(String ipaPath) {
		this.ipaPath = ipaPath;
	}

	/** APK경로 */
	public String getApkPath() {
		return apkPath;
	}

	/** APK경로 */
	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

	/** 표시명 */
	public String getDispNm() {
		return dispNm;
	}

	/** 표시명 */
	public void setDispNm(String dispNm) {
		this.dispNm = dispNm;
	}

	/** 설명내용 */
	public String getDescCont() {
		return descCont;
	}

	/** 설명내용 */
	public void setDescCont(String descCont) {
		this.descCont = descCont;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushAppDDao.selectPtPushAppD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushAppDDao.insertPtPushAppD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushAppDDao.updatePtPushAppD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushAppDDao.deletePtPushAppD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushAppDDao.countPtPushAppD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":푸쉬앱상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(custCd!=null) { if(tab!=null) builder.append(tab); builder.append("custCd(고객코드-PK):").append(custCd).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(custNm!=null) { if(tab!=null) builder.append(tab); builder.append("custNm(고객명):").append(custNm).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(endYmd!=null) { if(tab!=null) builder.append(tab); builder.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if(ipaPath!=null) { if(tab!=null) builder.append(tab); builder.append("ipaPath(IPA경로):").append(ipaPath).append('\n'); }
		if(apkPath!=null) { if(tab!=null) builder.append(tab); builder.append("apkPath(APK경로):").append(apkPath).append('\n'); }
		if(dispNm!=null) { if(tab!=null) builder.append(tab); builder.append("dispNm(표시명):").append(dispNm).append('\n'); }
		if(descCont!=null) { if(tab!=null) builder.append(tab); builder.append("descCont(설명내용):").append(descCont).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		super.toString(builder, tab);
	}
}
