package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 VO
 */
public class PtPltSetupRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3952423524250233951L;

	/** 메뉴그룹ID - KEY */
	private String mnuGrpId;

	/** 포틀릿ID - KEY */
	private String pltId;

	/** 포틀릿지역코드 - D1:1단, D2:2단, D3:3단, D4:4단, 05:5단 */
	private String pltZoneCd;

	/** 리소스ID */
	private String rescId;

	/** 정렬순서 */
	private String sortOrdr;

	/** TOP픽셀 */
	private String topPx;

	/** LEFT픽셀 */
	private String leftPx;

	/** 넓이픽셀 */
	private String wdthPx;

	/** 높이픽셀 */
	private String hghtPx;

	/** Z-INDEX */
	private String zidx;


	// 추가컬럼
	/** 포틀릿상세 VO */
	private PtPltDVo ptPltDVo;

	/** 포틀릿지역명 */
	private String pltZoneNm;

	/** 리소스명 */
	private String rescNm;

	/** 메뉴그룹ID - KEY */
	public String getMnuGrpId() {
		return mnuGrpId;
	}

	/** 메뉴그룹ID - KEY */
	public void setMnuGrpId(String mnuGrpId) {
		this.mnuGrpId = mnuGrpId;
	}

	/** 포틀릿ID - KEY */
	public String getPltId() {
		return pltId;
	}

	/** 포틀릿ID - KEY */
	public void setPltId(String pltId) {
		this.pltId = pltId;
	}

	/** 포틀릿지역코드 - D1:1단, D2:2단, D3:3단, D4:4단, 05:5단 */
	public String getPltZoneCd() {
		return pltZoneCd;
	}

	/** 포틀릿지역코드 - D1:1단, D2:2단, D3:3단, D4:4단, 05:5단 */
	public void setPltZoneCd(String pltZoneCd) {
		this.pltZoneCd = pltZoneCd;
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

	/** TOP픽셀 */
	public String getTopPx() {
		return topPx;
	}

	/** TOP픽셀 */
	public void setTopPx(String topPx) {
		this.topPx = topPx;
	}

	/** LEFT픽셀 */
	public String getLeftPx() {
		return leftPx;
	}

	/** LEFT픽셀 */
	public void setLeftPx(String leftPx) {
		this.leftPx = leftPx;
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

	/** Z-INDEX */
	public String getZidx() {
		return zidx;
	}

	/** Z-INDEX */
	public void setZidx(String zidx) {
		this.zidx = zidx;
	}

	/** 포틀릿상세 VO */
	public PtPltDVo getPtPltDVo() {
		return ptPltDVo;
	}

	/** 포틀릿상세 VO */
	public void setPtPltDVo(PtPltDVo ptPltDVo) {
		this.ptPltDVo = ptPltDVo;
	}

	/** 포틀릿지역명 */
	public String getPltZoneNm() {
		return pltZoneNm;
	}

	/** 포틀릿지역명 */
	public void setPltZoneNm(String pltZoneNm) {
		this.pltZoneNm = pltZoneNm;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltSetupRDao.selectPtPltSetupR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltSetupRDao.insertPtPltSetupR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltSetupRDao.updatePtPltSetupR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltSetupRDao.deletePtPltSetupR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPltSetupRDao.countPtPltSetupR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":포틀릿설정관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(mnuGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpId(메뉴그룹ID-PK):").append(mnuGrpId).append('\n'); }
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID-PK):").append(pltId).append('\n'); }
		if(pltZoneCd!=null) { if(tab!=null) builder.append(tab); builder.append("pltZoneCd(포틀릿지역코드):").append(pltZoneCd).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(topPx!=null) { if(tab!=null) builder.append(tab); builder.append("topPx(TOP픽셀):").append(topPx).append('\n'); }
		if(leftPx!=null) { if(tab!=null) builder.append(tab); builder.append("leftPx(LEFT픽셀):").append(leftPx).append('\n'); }
		if(wdthPx!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPx(넓이픽셀):").append(wdthPx).append('\n'); }
		if(hghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("hghtPx(높이픽셀):").append(hghtPx).append('\n'); }
		if(zidx!=null) { if(tab!=null) builder.append(tab); builder.append("zidx(Z-INDEX):").append(zidx).append('\n'); }
		if(ptPltDVo!=null) { if(tab!=null) builder.append(tab); builder.append("ptPltDVo(포틀릿상세 VO):\n"); ptPltDVo.toString(builder, tab==null ? "\t" : tab+"\t"); }
		if(pltZoneNm!=null) { if(tab!=null) builder.append(tab); builder.append("pltZoneNm(포틀릿지역명):").append(pltZoneNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
