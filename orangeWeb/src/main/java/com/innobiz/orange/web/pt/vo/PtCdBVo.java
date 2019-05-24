package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 코드기본(PT_CD_B) 테이블 VO
 */
public class PtCdBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8284260954519243203L;

	/** 분류코드 - KEY */
	private String clsCd;

	/** 코드 - KEY */
	private String cd;

	/** 코드값 */
	private String cdVa;

	/** 리소스ID */
	private String rescId;

	/** 회사ID목록 */
	private String compIds;

	/** 참조값1 */
	private String refVa1;

	/** 참조값2 */
	private String refVa2;

	/** 코드설명 */
	private String cdDesc;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;

	/** 폴더여부 */
	private String fldYn;

	/** 시스템코드여부 */
	private String sysCdYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 코드레벨 */
	private String cdLvl;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 분류코드 - KEY */
	public String getClsCd() {
		return clsCd;
	}

	/** 분류코드 - KEY */
	public void setClsCd(String clsCd) {
		this.clsCd = clsCd;
	}

	/** 코드 - KEY */
	public String getCd() {
		return cd;
	}

	/** 코드 - KEY */
	public void setCd(String cd) {
		this.cd = cd;
	}

	/** 코드값 */
	public String getCdVa() {
		return cdVa;
	}

	/** 코드값 */
	public void setCdVa(String cdVa) {
		this.cdVa = cdVa;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 회사ID목록 */
	public String getCompIds() {
		return compIds;
	}

	/** 회사ID목록 */
	public void setCompIds(String compIds) {
		this.compIds = compIds;
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

	/** 코드설명 */
	public String getCdDesc() {
		return cdDesc;
	}

	/** 코드설명 */
	public void setCdDesc(String cdDesc) {
		this.cdDesc = cdDesc;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 폴더여부 */
	public String getFldYn() {
		return fldYn;
	}

	/** 폴더여부 */
	public void setFldYn(String fldYn) {
		this.fldYn = fldYn;
	}

	/** 시스템코드여부 */
	public String getSysCdYn() {
		return sysCdYn;
	}

	/** 시스템코드여부 */
	public void setSysCdYn(String sysCdYn) {
		this.sysCdYn = sysCdYn;
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

	/** 코드레벨 */
	public String getCdLvl() {
		return cdLvl;
	}

	/** 코드레벨 */
	public void setCdLvl(String cdLvl) {
		this.cdLvl = cdLvl;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
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
			return "com.innobiz.orange.web.pt.dao.PtCdBDao.selectPtCdB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCdBDao.insertPtCdB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCdBDao.updatePtCdB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCdBDao.deletePtCdB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCdBDao.countPtCdB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":코드기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(clsCd!=null) { if(tab!=null) builder.append(tab); builder.append("clsCd(분류코드-PK):").append(clsCd).append('\n'); }
		if(cd!=null) { if(tab!=null) builder.append(tab); builder.append("cd(코드-PK):").append(cd).append('\n'); }
		if(cdVa!=null) { if(tab!=null) builder.append(tab); builder.append("cdVa(코드값):").append(cdVa).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(compIds!=null) { if(tab!=null) builder.append(tab); builder.append("compIds(회사ID목록):").append(compIds).append('\n'); }
		if(refVa1!=null) { if(tab!=null) builder.append(tab); builder.append("refVa1(참조값1):").append(refVa1).append('\n'); }
		if(refVa2!=null) { if(tab!=null) builder.append(tab); builder.append("refVa2(참조값2):").append(refVa2).append('\n'); }
		if(cdDesc!=null) { if(tab!=null) builder.append(tab); builder.append("cdDesc(코드설명):").append(cdDesc).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(fldYn!=null) { if(tab!=null) builder.append(tab); builder.append("fldYn(폴더여부):").append(fldYn).append('\n'); }
		if(sysCdYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysCdYn(시스템코드여부):").append(sysCdYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(cdLvl!=null) { if(tab!=null) builder.append(tab); builder.append("cdLvl(코드레벨):").append(cdLvl).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
