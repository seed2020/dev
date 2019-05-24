package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 회사기본(PT_COMP_B) 테이블 VO
 */
public class PtCompBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5850905428169839651L;

	/** 회사ID - KEY */
	private String compId;

	/** 참조코드 */
	private String rcd;

	/** 회사명 */
	private String compNm;

	/** 리소스ID */
	private String rescId;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;

	/** 삭제여부 */
	private String delYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 메일도메인 */
	private String mailDomain;

	/** 라이선스수 */
	private String licCnt;


	// 추가컬럼
	/** 계열사 ID 목록 */
	private List<String> affiliateIds;

	/** 계열사 명 목록 */
	private List<String> affiliateNms;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 참조코드 */
	public String getRcd() {
		return rcd;
	}

	/** 참조코드 */
	public void setRcd(String rcd) {
		this.rcd = rcd;
	}

	/** 회사명 */
	public String getCompNm() {
		return compNm;
	}

	/** 회사명 */
	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
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

	/** 삭제여부 */
	public String getDelYn() {
		return delYn;
	}

	/** 삭제여부 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
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

	/** 메일도메인 */
	public String getMailDomain() {
		return mailDomain;
	}

	/** 메일도메인 */
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	/** 라이선스수 */
	public String getLicCnt() {
		return licCnt;
	}

	/** 라이선스수 */
	public void setLicCnt(String licCnt) {
		this.licCnt = licCnt;
	}

	/** 계열사 ID 목록 */
	public List<String> getAffiliateIds() {
		return affiliateIds;
	}

	/** 계열사 ID 목록 */
	public void setAffiliateIds(List<String> affiliateIds) {
		this.affiliateIds = affiliateIds;
	}

	/** 계열사 명 목록 */
	public List<String> getAffiliateNms() {
		return affiliateNms;
	}

	/** 계열사 명 목록 */
	public void setAffiliateNms(List<String> affiliateNms) {
		this.affiliateNms = affiliateNms;
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
			return "com.innobiz.orange.web.pt.dao.PtCompBDao.selectPtCompB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCompBDao.insertPtCompB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCompBDao.updatePtCompB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCompBDao.deletePtCompB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCompBDao.countPtCompB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":회사기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(rcd!=null) { if(tab!=null) builder.append(tab); builder.append("rcd(참조코드):").append(rcd).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(delYn!=null) { if(tab!=null) builder.append(tab); builder.append("delYn(삭제여부):").append(delYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(mailDomain!=null) { if(tab!=null) builder.append(tab); builder.append("mailDomain(메일도메인):").append(mailDomain).append('\n'); }
		if(licCnt!=null) { if(tab!=null) builder.append(tab); builder.append("licCnt(라이선스수):").append(licCnt).append('\n'); }
		if(affiliateIds!=null) { if(tab!=null) builder.append(tab); builder.append("affiliateIds(계열사 ID 목록):"); appendStringListTo(builder, affiliateIds, tab); builder.append('\n'); }
		if(affiliateNms!=null) { if(tab!=null) builder.append(tab); builder.append("affiliateNms(계열사 명 목록):"); appendStringListTo(builder, affiliateNms, tab); builder.append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
