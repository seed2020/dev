package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 프로잭트기본(WP_PRJ_B) 테이블 VO
 */
public class WpPrjBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 841390192092347182L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 버전 */
	private String ver;

	/** 프로잭트코드 */
	private String prjCd;

	/** 프로잭트명 */
	private String prjNm;

	/** 그룹ID */
	private String grpId;

	/** 시작년월일 */
	private String strtYmd;

	/** 종료년월일 */
	private String endYmd;

	/** 완료년월일 */
	private String cmplYmd;

	/** 프로잭트맨먼스 */
	private String prjMm;

	/** 프로잭트금액 */
	private String prjAmt;

	/** 피엠ID */
	private String pmId;

	/** 피엠인력구분코드 */
	private String pmMpTypCd;

	/** 개요 */
	private String smry;

	/** 고객명 */
	private String custNm;

	/** 고객우편번호 */
	private String custZipNo;

	/** 고객주소 */
	private String custAdr;

	/** 프로잭트상태코드 */
	private String prjStatCd;

	/** 변경상태코드 */
	private String modStatCd;

	/** 수정내용 */
	private String modCont;

	/** 반려사유 */
	private String rejtRson;

	/** 등록일 */
	private String regDt;

	/** 등록자UID */
	private String regUid;


	// 추가컬럼
	/** 테이블타입 */
	private String tableType = null;

	/** 프로잭트번호 목록 */
	private List<String> prjNoList;

	/** 피엠명 */
	private String pmNm;

	/** 투입인력ID */
	private String mpId;

	/** 변경상태코드 목록 */
	private List<String> modStatCdList;

	/** 프로잭트역할2코드 */
	private String prjRole2Cd;

	/** 프로잭트번호 - KEY */
	public String getPrjNo() {
		return prjNo;
	}

	/** 프로잭트번호 - KEY */
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	/** 버전 */
	public String getVer() {
		return ver;
	}

	/** 버전 */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/** 프로잭트코드 */
	public String getPrjCd() {
		return prjCd;
	}

	/** 프로잭트코드 */
	public void setPrjCd(String prjCd) {
		this.prjCd = prjCd;
	}

	/** 프로잭트명 */
	public String getPrjNm() {
		return prjNm;
	}

	/** 프로잭트명 */
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
	}

	/** 그룹ID */
	public String getGrpId() {
		return grpId;
	}

	/** 그룹ID */
	public void setGrpId(String grpId) {
		this.grpId = grpId;
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

	/** 완료년월일 */
	public String getCmplYmd() {
		return cmplYmd;
	}

	/** 완료년월일 */
	public void setCmplYmd(String cmplYmd) {
		this.cmplYmd = cmplYmd;
	}

	/** 프로잭트맨먼스 */
	public String getPrjMm() {
		return prjMm;
	}

	/** 프로잭트맨먼스 */
	public void setPrjMm(String prjMm) {
		this.prjMm = prjMm;
	}

	/** 프로잭트금액 */
	public String getPrjAmt() {
		return prjAmt;
	}

	/** 프로잭트금액 */
	public void setPrjAmt(String prjAmt) {
		this.prjAmt = prjAmt;
	}

	/** 피엠ID */
	public String getPmId() {
		return pmId;
	}

	/** 피엠ID */
	public void setPmId(String pmId) {
		this.pmId = pmId;
	}

	/** 피엠인력구분코드 */
	public String getPmMpTypCd() {
		return pmMpTypCd;
	}

	/** 피엠인력구분코드 */
	public void setPmMpTypCd(String pmMpTypCd) {
		this.pmMpTypCd = pmMpTypCd;
	}

	/** 개요 */
	public String getSmry() {
		return smry;
	}

	/** 개요 */
	public void setSmry(String smry) {
		this.smry = smry;
	}

	/** 고객명 */
	public String getCustNm() {
		return custNm;
	}

	/** 고객명 */
	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	/** 고객우편번호 */
	public String getCustZipNo() {
		return custZipNo;
	}

	/** 고객우편번호 */
	public void setCustZipNo(String custZipNo) {
		this.custZipNo = custZipNo;
	}

	/** 고객주소 */
	public String getCustAdr() {
		return custAdr;
	}

	/** 고객주소 */
	public void setCustAdr(String custAdr) {
		this.custAdr = custAdr;
	}

	/** 프로잭트상태코드 */
	public String getPrjStatCd() {
		return prjStatCd;
	}

	/** 프로잭트상태코드 */
	public void setPrjStatCd(String prjStatCd) {
		this.prjStatCd = prjStatCd;
	}

	/** 변경상태코드 */
	public String getModStatCd() {
		return modStatCd;
	}

	/** 변경상태코드 */
	public void setModStatCd(String modStatCd) {
		this.modStatCd = modStatCd;
	}

	/** 수정내용 */
	public String getModCont() {
		return modCont;
	}

	/** 수정내용 */
	public void setModCont(String modCont) {
		this.modCont = modCont;
	}

	/** 반려사유 */
	public String getRejtRson() {
		return rejtRson;
	}

	/** 반려사유 */
	public void setRejtRson(String rejtRson) {
		this.rejtRson = rejtRson;
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

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "B" : "B"+tableType;
	}

	/** 테이블 타입 리턴 */
	public String getTableDType(){
		return tableType==null ? "D" : "D"+tableType;
	}

	/** 테이블 타입 제거 */
	public void removeTableType(){
		tableType = null;
	}

	/** 히스토리 테이블 설정 */
	public void setHistory(){
		tableType = "H";
	}

	/** 프로잭트번호 목록 */
	public List<String> getPrjNoList() {
		return prjNoList;
	}

	/** 프로잭트번호 목록 */
	public void setPrjNoList(List<String> prjNoList) {
		this.prjNoList = prjNoList;
	}

	/** 피엠명 */
	public String getPmNm() {
		return pmNm;
	}

	/** 피엠명 */
	public void setPmNm(String pmNm) {
		this.pmNm = pmNm;
	}

	/** 투입인력ID */
	public String getMpId() {
		return mpId;
	}

	/** 투입인력ID */
	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** 변경상태코드 목록 */
	public List<String> getModStatCdList() {
		return modStatCdList;
	}

	/** 변경상태코드 목록 */
	public void setModStatCdList(List<String> modStatCdList) {
		this.modStatCdList = modStatCdList;
	}

	/** 프로잭트역할2코드 */
	public String getPrjRole2Cd() {
		return prjRole2Cd;
	}

	/** 프로잭트역할2코드 */
	public void setPrjRole2Cd(String prjRole2Cd) {
		this.prjRole2Cd = prjRole2Cd;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjBDao.selectWpPrjB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjBDao.insertWpPrjB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjBDao.updateWpPrjB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjBDao.deleteWpPrjB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjBDao.countWpPrjB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(ver!=null) { if(tab!=null) builder.append(tab); builder.append("ver(버전):").append(ver).append('\n'); }
		if(prjCd!=null) { if(tab!=null) builder.append(tab); builder.append("prjCd(프로잭트코드):").append(prjCd).append('\n'); }
		if(prjNm!=null) { if(tab!=null) builder.append(tab); builder.append("prjNm(프로잭트명):").append(prjNm).append('\n'); }
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID):").append(grpId).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(endYmd!=null) { if(tab!=null) builder.append(tab); builder.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if(cmplYmd!=null) { if(tab!=null) builder.append(tab); builder.append("cmplYmd(완료년월일):").append(cmplYmd).append('\n'); }
		if(prjMm!=null) { if(tab!=null) builder.append(tab); builder.append("prjMm(프로잭트맨먼스):").append(prjMm).append('\n'); }
		if(prjAmt!=null) { if(tab!=null) builder.append(tab); builder.append("prjAmt(프로잭트금액):").append(prjAmt).append('\n'); }
		if(pmId!=null) { if(tab!=null) builder.append(tab); builder.append("pmId(피엠ID):").append(pmId).append('\n'); }
		if(pmMpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pmMpTypCd(피엠인력구분코드):").append(pmMpTypCd).append('\n'); }
		if(smry!=null) { if(tab!=null) builder.append(tab); builder.append("smry(개요):").append(smry).append('\n'); }
		if(custNm!=null) { if(tab!=null) builder.append(tab); builder.append("custNm(고객명):").append(custNm).append('\n'); }
		if(custZipNo!=null) { if(tab!=null) builder.append(tab); builder.append("custZipNo(고객우편번호):").append(custZipNo).append('\n'); }
		if(custAdr!=null) { if(tab!=null) builder.append(tab); builder.append("custAdr(고객주소):").append(custAdr).append('\n'); }
		if(prjStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("prjStatCd(프로잭트상태코드):").append(prjStatCd).append('\n'); }
		if(modStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("modStatCd(변경상태코드):").append(modStatCd).append('\n'); }
		if(modCont!=null) { if(tab!=null) builder.append(tab); builder.append("modCont(수정내용):").append(modCont).append('\n'); }
		if(rejtRson!=null) { if(tab!=null) builder.append(tab); builder.append("rejtRson(반려사유):").append(rejtRson).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일):").append(regDt).append('\n'); }
		if(regUid!=null) { if(tab!=null) builder.append(tab); builder.append("regUid(등록자UID):").append(regUid).append('\n'); }
		if(prjNoList!=null) { if(tab!=null) builder.append(tab); builder.append("prjNoList(프로잭트번호 목록):"); appendStringListTo(builder, prjNoList, tab); builder.append('\n'); }
		if(pmNm!=null) { if(tab!=null) builder.append(tab); builder.append("pmNm(피엠명):").append(pmNm).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(투입인력ID):").append(mpId).append('\n'); }
		if(modStatCdList!=null) { if(tab!=null) builder.append(tab); builder.append("modStatCdList(변경상태코드 목록):"); appendStringListTo(builder, modStatCdList, tab); builder.append('\n'); }
		if(prjRole2Cd!=null) { if(tab!=null) builder.append(tab); builder.append("prjRole2Cd(프로잭트역할2코드):").append(prjRole2Cd).append('\n'); }
		super.toString(builder, tab);
	}
}
