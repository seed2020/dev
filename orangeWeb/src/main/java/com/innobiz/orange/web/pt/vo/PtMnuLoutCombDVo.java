package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 메뉴레이아웃조합상세(PT_MNU_LOUT_COMB_D) 테이블 VO
 */
public class PtMnuLoutCombDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8333369734199894067L;

	/** 회사ID - KEY */
	private String compId;

	/** 메뉴레이아웃조합ID - KEY */
	private String mnuLoutCombId;

	/** 메뉴레이아웃ID */
	private String mnuLoutId;

	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃 */
	private String loutCatId;

	/** 메뉴레이아웃조합부모ID */
	private String mnuLoutCombPid;

	/** 폴더여부 */
	private String fldYn;

	/** 폴더명 */
	private String fldNm;

	/** 리소스ID */
	private String rescId;

	/** 메뉴ID */
	private String mnuId;

	/** 메뉴리소스ID */
	private String mnuRescId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 자식(메뉴레이아웃조합상세) VO 목록 */
	private List<PtMnuLoutCombDVo> childList;

	/** 메뉴상세 VO */
	private PtMnuDVo ptMnuDVo;

	/** 메뉴URL */
	private String mnuUrl;

	/** 메뉴 함수 */
	private String mnuFnc;

	/** 리소스명 */
	private String rescNm;

	/** 메뉴리소스명 */
	private String mnuRescNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 메뉴레이아웃조합ID - KEY */
	public String getMnuLoutCombId() {
		return mnuLoutCombId;
	}

	/** 메뉴레이아웃조합ID - KEY */
	public void setMnuLoutCombId(String mnuLoutCombId) {
		this.mnuLoutCombId = mnuLoutCombId;
	}

	/** 메뉴레이아웃ID */
	public String getMnuLoutId() {
		return mnuLoutId;
	}

	/** 메뉴레이아웃ID */
	public void setMnuLoutId(String mnuLoutId) {
		this.mnuLoutId = mnuLoutId;
	}

	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃 */
	public String getLoutCatId() {
		return loutCatId;
	}

	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃 */
	public void setLoutCatId(String loutCatId) {
		this.loutCatId = loutCatId;
	}

	/** 메뉴레이아웃조합부모ID */
	public String getMnuLoutCombPid() {
		return mnuLoutCombPid;
	}

	/** 메뉴레이아웃조합부모ID */
	public void setMnuLoutCombPid(String mnuLoutCombPid) {
		this.mnuLoutCombPid = mnuLoutCombPid;
	}

	/** 폴더여부 */
	public String getFldYn() {
		return fldYn;
	}

	/** 폴더여부 */
	public void setFldYn(String fldYn) {
		this.fldYn = fldYn;
	}

	/** 폴더명 */
	public String getFldNm() {
		return fldNm;
	}

	/** 폴더명 */
	public void setFldNm(String fldNm) {
		this.fldNm = fldNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 메뉴ID */
	public String getMnuId() {
		return mnuId;
	}

	/** 메뉴ID */
	public void setMnuId(String mnuId) {
		this.mnuId = mnuId;
	}

	/** 메뉴리소스ID */
	public String getMnuRescId() {
		return mnuRescId;
	}

	/** 메뉴리소스ID */
	public void setMnuRescId(String mnuRescId) {
		this.mnuRescId = mnuRescId;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 자식(메뉴레이아웃조합상세) VO 목록 */
	public List<PtMnuLoutCombDVo> getChildList() {
		return childList;
	}

	/** 자식(메뉴레이아웃조합상세) VO 목록 */
	public void setChildList(List<PtMnuLoutCombDVo> childList) {
		this.childList = childList;
	}

	/** 메뉴상세 VO */
	public PtMnuDVo getPtMnuDVo() {
		return ptMnuDVo;
	}

	/** 메뉴상세 VO */
	public void setPtMnuDVo(PtMnuDVo ptMnuDVo) {
		this.ptMnuDVo = ptMnuDVo;
	}

	/** 메뉴URL */
	public String getMnuUrl() {
		return mnuUrl;
	}

	/** 메뉴URL */
	public void setMnuUrl(String mnuUrl) {
		this.mnuUrl = mnuUrl;
	}

	/** 메뉴 함수 */
	public String getMnuFnc() {
		return mnuFnc;
	}

	/** 메뉴 함수 */
	public void setMnuFnc(String mnuFnc) {
		this.mnuFnc = mnuFnc;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 메뉴리소스명 */
	public String getMnuRescNm() {
		return mnuRescNm;
	}

	/** 메뉴리소스명 */
	public void setMnuRescNm(String mnuRescNm) {
		this.mnuRescNm = mnuRescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutCombDDao.selectPtMnuLoutCombD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutCombDDao.insertPtMnuLoutCombD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutCombDDao.updatePtMnuLoutCombD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutCombDDao.deletePtMnuLoutCombD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutCombDDao.countPtMnuLoutCombD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메뉴레이아웃조합상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(mnuLoutCombId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutCombId(메뉴레이아웃조합ID-PK):").append(mnuLoutCombId).append('\n'); }
		if(mnuLoutId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutId(메뉴레이아웃ID):").append(mnuLoutId).append('\n'); }
		if(loutCatId!=null) { if(tab!=null) builder.append(tab); builder.append("loutCatId(레이아웃유형ID):").append(loutCatId).append('\n'); }
		if(mnuLoutCombPid!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutCombPid(메뉴레이아웃조합부모ID):").append(mnuLoutCombPid).append('\n'); }
		if(fldYn!=null) { if(tab!=null) builder.append(tab); builder.append("fldYn(폴더여부):").append(fldYn).append('\n'); }
		if(fldNm!=null) { if(tab!=null) builder.append(tab); builder.append("fldNm(폴더명):").append(fldNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(mnuId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuId(메뉴ID):").append(mnuId).append('\n'); }
		if(mnuRescId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuRescId(메뉴리소스ID):").append(mnuRescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(childList!=null) { if(tab!=null) builder.append(tab); builder.append("childList(자식(메뉴레이아웃조합상세) VO 목록):"); appendVoListTo(builder, childList, tab); builder.append('\n'); }
		if(ptMnuDVo!=null) { if(tab!=null) builder.append(tab); builder.append("ptMnuDVo(메뉴상세 VO):\n"); ptMnuDVo.toString(builder, tab==null ? "\t" : tab+"\t"); }
		if(mnuUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mnuUrl(메뉴URL):").append(mnuUrl).append('\n'); }
		if(mnuFnc!=null) { if(tab!=null) builder.append(tab); builder.append("mnuFnc(메뉴 함수):").append(mnuFnc).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(mnuRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuRescNm(메뉴리소스명):").append(mnuRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
