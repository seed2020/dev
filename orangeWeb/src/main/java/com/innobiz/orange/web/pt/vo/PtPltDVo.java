package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 포틀릿상세(PT_PLT_D) 테이블 VO
 */
public class PtPltDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 9028374455483003175L;

	/** 포틀릿ID - KEY */
	private String pltId;

	/** 포틀릿카테고리코드 - pt:포털, ap:결재, em:메일, bb:게시, ct:커뮤니티, wc:일정, wb:명함, wv:설문, wr:자원, dm:문서 */
	private String pltCatCd;

	/** 공개회사ID */
	private String openCompId;

	/** 포틀릿명 */
	private String pltNm;

	/** 리소스ID */
	private String rescId;

	/** 포틀릿URL */
	private String pltUrl;

	/** 모어URL */
	private String moreUrl;

	/** 설정URL */
	private String setupUrl;

	/** 넓이픽셀 */
	private String wdthPx;

	/** 높이픽셀 */
	private String hghtPx;

	/** 포틀릿설명 */
	private String pltDesc;

	/** 포틀릿타이틀여부 */
	private String pltTitlYn;

	/** 항목타이틀여부 */
	private String itemTitlYn;

	/** 테두리선여부 */
	private String bordYn;

	/** 사용여부 */
	private String useYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 공개회사명 */
	private String openCompNm;

	/** 공개회사ID목록 */
	private List<String> openCompIdList;

	/** 포틀릿카테고리명 */
	private String pltCatNm;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 포틀릿ID - KEY */
	public String getPltId() {
		return pltId;
	}

	/** 포틀릿ID - KEY */
	public void setPltId(String pltId) {
		this.pltId = pltId;
	}

	/** 포틀릿카테고리코드 - pt:포털, ap:결재, em:메일, bb:게시, ct:커뮤니티, wc:일정, wb:명함, wv:설문, wr:자원, dm:문서 */
	public String getPltCatCd() {
		return pltCatCd;
	}

	/** 포틀릿카테고리코드 - pt:포털, ap:결재, em:메일, bb:게시, ct:커뮤니티, wc:일정, wb:명함, wv:설문, wr:자원, dm:문서 */
	public void setPltCatCd(String pltCatCd) {
		this.pltCatCd = pltCatCd;
	}

	/** 공개회사ID */
	public String getOpenCompId() {
		return openCompId;
	}

	/** 공개회사ID */
	public void setOpenCompId(String openCompId) {
		this.openCompId = openCompId;
	}

	/** 포틀릿명 */
	public String getPltNm() {
		return pltNm;
	}

	/** 포틀릿명 */
	public void setPltNm(String pltNm) {
		this.pltNm = pltNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 포틀릿URL */
	public String getPltUrl() {
		return pltUrl;
	}

	/** 포틀릿URL */
	public void setPltUrl(String pltUrl) {
		this.pltUrl = pltUrl;
	}

	/** 모어URL */
	public String getMoreUrl() {
		return moreUrl;
	}

	/** 모어URL */
	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}

	/** 설정URL */
	public String getSetupUrl() {
		return setupUrl;
	}

	/** 설정URL */
	public void setSetupUrl(String setupUrl) {
		this.setupUrl = setupUrl;
	}

	/** 넓이픽셀 */
	public String getWdthPx() {
		return wdthPx;
	}

	/** 넓이픽셀 */
	public void setWdthPx(String wdthPx) {
		this.wdthPx = wdthPx;
	}

	/** 높이픽셀 */
	public String getHghtPx() {
		return hghtPx;
	}

	/** 높이픽셀 */
	public void setHghtPx(String hghtPx) {
		this.hghtPx = hghtPx;
	}

	/** 포틀릿설명 */
	public String getPltDesc() {
		return pltDesc;
	}

	/** 포틀릿설명 */
	public void setPltDesc(String pltDesc) {
		this.pltDesc = pltDesc;
	}

	/** 포틀릿타이틀여부 */
	public String getPltTitlYn() {
		return pltTitlYn;
	}

	/** 포틀릿타이틀여부 */
	public void setPltTitlYn(String pltTitlYn) {
		this.pltTitlYn = pltTitlYn;
	}

	/** 항목타이틀여부 */
	public String getItemTitlYn() {
		return itemTitlYn;
	}

	/** 항목타이틀여부 */
	public void setItemTitlYn(String itemTitlYn) {
		this.itemTitlYn = itemTitlYn;
	}

	/** 테두리선여부 */
	public String getBordYn() {
		return bordYn;
	}

	/** 테두리선여부 */
	public void setBordYn(String bordYn) {
		this.bordYn = bordYn;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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

	/** 공개회사명 */
	public String getOpenCompNm() {
		return openCompNm;
	}

	/** 공개회사명 */
	public void setOpenCompNm(String openCompNm) {
		this.openCompNm = openCompNm;
	}

	/** 공개회사ID목록 */
	public List<String> getOpenCompIdList() {
		return openCompIdList;
	}

	/** 공개회사ID목록 */
	public void setOpenCompIdList(List<String> openCompIdList) {
		this.openCompIdList = openCompIdList;
	}

	/** 포틀릿카테고리명 */
	public String getPltCatNm() {
		return pltCatNm;
	}

	/** 포틀릿카테고리명 */
	public void setPltCatNm(String pltCatNm) {
		this.pltCatNm = pltCatNm;
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
			return "com.innobiz.orange.web.pt.dao.PtPltDDao.selectPtPltD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltDDao.insertPtPltD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltDDao.updatePtPltD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltDDao.deletePtPltD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltDDao.countPtPltD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":포틀릿상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID-PK):").append(pltId).append('\n'); }
		if(pltCatCd!=null) { if(tab!=null) builder.append(tab); builder.append("pltCatCd(포틀릿카테고리코드):").append(pltCatCd).append('\n'); }
		if(openCompId!=null) { if(tab!=null) builder.append(tab); builder.append("openCompId(공개회사ID):").append(openCompId).append('\n'); }
		if(pltNm!=null) { if(tab!=null) builder.append(tab); builder.append("pltNm(포틀릿명):").append(pltNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(pltUrl!=null) { if(tab!=null) builder.append(tab); builder.append("pltUrl(포틀릿URL):").append(pltUrl).append('\n'); }
		if(moreUrl!=null) { if(tab!=null) builder.append(tab); builder.append("moreUrl(모어URL):").append(moreUrl).append('\n'); }
		if(setupUrl!=null) { if(tab!=null) builder.append(tab); builder.append("setupUrl(설정URL):").append(setupUrl).append('\n'); }
		if(wdthPx!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPx(넓이픽셀):").append(wdthPx).append('\n'); }
		if(hghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("hghtPx(높이픽셀):").append(hghtPx).append('\n'); }
		if(pltDesc!=null) { if(tab!=null) builder.append(tab); builder.append("pltDesc(포틀릿설명):").append(pltDesc).append('\n'); }
		if(pltTitlYn!=null) { if(tab!=null) builder.append(tab); builder.append("pltTitlYn(포틀릿타이틀여부):").append(pltTitlYn).append('\n'); }
		if(itemTitlYn!=null) { if(tab!=null) builder.append(tab); builder.append("itemTitlYn(항목타이틀여부):").append(itemTitlYn).append('\n'); }
		if(bordYn!=null) { if(tab!=null) builder.append(tab); builder.append("bordYn(테두리선여부):").append(bordYn).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(openCompNm!=null) { if(tab!=null) builder.append(tab); builder.append("openCompNm(공개회사명):").append(openCompNm).append('\n'); }
		if(openCompIdList!=null) { if(tab!=null) builder.append(tab); builder.append("openCompIdList(공개회사ID목록):"); appendStringListTo(builder, openCompIdList, tab); builder.append('\n'); }
		if(pltCatNm!=null) { if(tab!=null) builder.append(tab); builder.append("pltCatNm(포틀릿카테고리명):").append(pltCatNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
