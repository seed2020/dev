package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 메뉴레이아웃상세(PT_MNU_LOUT_D) 테이블 VO
 */
public class PtMnuLoutDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1388220147181446357L;

	/** 회사ID - KEY */
	private String compId;

	/** 메뉴레이아웃ID - KEY */
	private String mnuLoutId;

	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃 */
	private String loutCatId;

	/** 메뉴레이아웃부모ID */
	private String mnuLoutPid;

	/** 레이아웃위치코드 - 아이콘레이아웃(home:홈,icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(home:홈,top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자) */
	private String loutLocCd;

	/** 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합 */
	private String mnuLoutKndCd;

	/** 메뉴그룹ID */
	private String mnuGrpId;

	/** 메뉴그룹리소스ID */
	private String mnuGrpRescId;

	/** 이미지종류값 */
	private String imgKndVa;

	/** 메뉴레이아웃명 */
	private String mnuLoutNm;

	/** 리소스ID */
	private String rescId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 최상위메뉴레이아웃ID */
	private String topMnuLoutId;

	/** 자식(메뉴레이아웃상세) VO 목록 */
	private List<PtMnuLoutDVo> childList;

	/** 콤보(메뉴레이아웃조합상세) VO 목록 */
	private List<PtMnuLoutCombDVo> combList;

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	private String mnuGrpTypCd;

	/** 메뉴URL */
	private String mnuUrl;

	/** 메뉴 함수 */
	private String mnuFnc;

	/** 마이 메뉴 */
	private boolean myMnu;

	/** 레이아웃위치명 */
	private String loutLocNm;

	/** 레이아웃종류명 */
	private String mnuLoutKndNm;

	/** 메뉴그룹리소스명 */
	private String mnuGrpRescNm;

	/** 리소스명 */
	private String rescNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 메뉴레이아웃ID - KEY */
	public String getMnuLoutId() {
		return mnuLoutId;
	}

	/** 메뉴레이아웃ID - KEY */
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

	/** 메뉴레이아웃부모ID */
	public String getMnuLoutPid() {
		return mnuLoutPid;
	}

	/** 메뉴레이아웃부모ID */
	public void setMnuLoutPid(String mnuLoutPid) {
		this.mnuLoutPid = mnuLoutPid;
	}

	/** 레이아웃위치코드 - 아이콘레이아웃(home:홈,icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(home:홈,top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자) */
	public String getLoutLocCd() {
		return loutLocCd;
	}

	/** 레이아웃위치코드 - 아이콘레이아웃(home:홈,icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(home:홈,top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자) */
	public void setLoutLocCd(String loutLocCd) {
		this.loutLocCd = loutLocCd;
	}

	/** 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합 */
	public String getMnuLoutKndCd() {
		return mnuLoutKndCd;
	}

	/** 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합 */
	public void setMnuLoutKndCd(String mnuLoutKndCd) {
		this.mnuLoutKndCd = mnuLoutKndCd;
	}

	/** 메뉴그룹ID */
	public String getMnuGrpId() {
		return mnuGrpId;
	}

	/** 메뉴그룹ID */
	public void setMnuGrpId(String mnuGrpId) {
		this.mnuGrpId = mnuGrpId;
	}

	/** 메뉴그룹리소스ID */
	public String getMnuGrpRescId() {
		return mnuGrpRescId;
	}

	/** 메뉴그룹리소스ID */
	public void setMnuGrpRescId(String mnuGrpRescId) {
		this.mnuGrpRescId = mnuGrpRescId;
	}

	/** 이미지종류값 */
	public String getImgKndVa() {
		return imgKndVa;
	}

	/** 이미지종류값 */
	public void setImgKndVa(String imgKndVa) {
		this.imgKndVa = imgKndVa;
	}

	/** 메뉴레이아웃명 */
	public String getMnuLoutNm() {
		return mnuLoutNm;
	}

	/** 메뉴레이아웃명 */
	public void setMnuLoutNm(String mnuLoutNm) {
		this.mnuLoutNm = mnuLoutNm;
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

	/** 최상위메뉴레이아웃ID */
	public String getTopMnuLoutId() {
		return topMnuLoutId;
	}

	/** 최상위메뉴레이아웃ID */
	public void setTopMnuLoutId(String topMnuLoutId) {
		this.topMnuLoutId = topMnuLoutId;
	}

	/** 자식(메뉴레이아웃상세) VO 목록 */
	public List<PtMnuLoutDVo> getChildList() {
		return childList;
	}

	/** 자식(메뉴레이아웃상세) VO 목록 */
	public void setChildList(List<PtMnuLoutDVo> childList) {
		this.childList = childList;
	}

	/** 콤보(메뉴레이아웃조합상세) VO 목록 */
	public List<PtMnuLoutCombDVo> getCombList() {
		return combList;
	}

	/** 콤보(메뉴레이아웃조합상세) VO 목록 */
	public void setCombList(List<PtMnuLoutCombDVo> combList) {
		this.combList = combList;
	}

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	public String getMnuGrpTypCd() {
		return mnuGrpTypCd;
	}

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	public void setMnuGrpTypCd(String mnuGrpTypCd) {
		this.mnuGrpTypCd = mnuGrpTypCd;
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

	/** 마이 메뉴 */
	public boolean isMyMnu() {
		return myMnu;
	}

	/** 마이 메뉴 */
	public void setMyMnu(boolean myMnu) {
		this.myMnu = myMnu;
	}

	/** 레이아웃위치명 */
	public String getLoutLocNm() {
		return loutLocNm;
	}

	/** 레이아웃위치명 */
	public void setLoutLocNm(String loutLocNm) {
		this.loutLocNm = loutLocNm;
	}

	/** 레이아웃종류명 */
	public String getMnuLoutKndNm() {
		return mnuLoutKndNm;
	}

	/** 레이아웃종류명 */
	public void setMnuLoutKndNm(String mnuLoutKndNm) {
		this.mnuLoutKndNm = mnuLoutKndNm;
	}

	/** 메뉴그룹리소스명 */
	public String getMnuGrpRescNm() {
		return mnuGrpRescNm;
	}

	/** 메뉴그룹리소스명 */
	public void setMnuGrpRescNm(String mnuGrpRescNm) {
		this.mnuGrpRescNm = mnuGrpRescNm;
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
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutDDao.selectPtMnuLoutD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutDDao.insertPtMnuLoutD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutDDao.updatePtMnuLoutD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutDDao.deletePtMnuLoutD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuLoutDDao.countPtMnuLoutD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메뉴레이아웃상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(mnuLoutId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutId(메뉴레이아웃ID-PK):").append(mnuLoutId).append('\n'); }
		if(loutCatId!=null) { if(tab!=null) builder.append(tab); builder.append("loutCatId(레이아웃유형ID):").append(loutCatId).append('\n'); }
		if(mnuLoutPid!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutPid(메뉴레이아웃부모ID):").append(mnuLoutPid).append('\n'); }
		if(loutLocCd!=null) { if(tab!=null) builder.append(tab); builder.append("loutLocCd(레이아웃위치코드):").append(loutLocCd).append('\n'); }
		if(mnuLoutKndCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutKndCd(레이아웃종류코드):").append(mnuLoutKndCd).append('\n'); }
		if(mnuGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpId(메뉴그룹ID):").append(mnuGrpId).append('\n'); }
		if(mnuGrpRescId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpRescId(메뉴그룹리소스ID):").append(mnuGrpRescId).append('\n'); }
		if(imgKndVa!=null) { if(tab!=null) builder.append(tab); builder.append("imgKndVa(이미지종류값):").append(imgKndVa).append('\n'); }
		if(mnuLoutNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutNm(메뉴레이아웃명):").append(mnuLoutNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(topMnuLoutId!=null) { if(tab!=null) builder.append(tab); builder.append("topMnuLoutId(최상위메뉴레이아웃ID):").append(topMnuLoutId).append('\n'); }
		if(childList!=null) { if(tab!=null) builder.append(tab); builder.append("childList(자식(메뉴레이아웃상세) VO 목록):"); appendVoListTo(builder, childList, tab); builder.append('\n'); }
		if(combList!=null) { if(tab!=null) builder.append(tab); builder.append("combList(콤보(메뉴레이아웃조합상세) VO 목록):"); appendVoListTo(builder, combList, tab); builder.append('\n'); }
		if(mnuGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpTypCd(메뉴그룹구분코드):").append(mnuGrpTypCd).append('\n'); }
		if(mnuUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mnuUrl(메뉴URL):").append(mnuUrl).append('\n'); }
		if(mnuFnc!=null) { if(tab!=null) builder.append(tab); builder.append("mnuFnc(메뉴 함수):").append(mnuFnc).append('\n'); }
		if(myMnu) { if(tab!=null) builder.append(tab); builder.append("myMnu(마이 메뉴):").append(myMnu).append('\n'); }
		if(loutLocNm!=null) { if(tab!=null) builder.append(tab); builder.append("loutLocNm(레이아웃위치명):").append(loutLocNm).append('\n'); }
		if(mnuLoutKndNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutKndNm(레이아웃종류명):").append(mnuLoutKndNm).append('\n'); }
		if(mnuGrpRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpRescNm(메뉴그룹리소스명):").append(mnuGrpRescNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
