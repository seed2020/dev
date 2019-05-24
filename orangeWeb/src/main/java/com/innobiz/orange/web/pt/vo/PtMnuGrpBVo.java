package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 메뉴그룹기본(PT_MNU_GRP_B) 테이블 VO
 */
public class PtMnuGrpBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1291586677376774564L;

	/** 메뉴그룹ID - KEY */
	private String mnuGrpId;

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	private String mnuGrpMdCd;

	/** 메뉴그룹명 */
	private String mnuGrpNm;

	/** 리소스ID */
	private String rescId;

	/** 시스템생성여부 */
	private String sysgYn;

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	private String mnuGrpTypCd;

	/** 정렬순서 */
	private String sortOrdr;

	/** 메뉴URL */
	private String mnuUrl;

	/** 팝업설정내용 */
	private String popSetupCont;

	/** 포틀릿레이아웃코드 - FREE:1단자유구성, D2R37:2단 (3:7), D2R46:2단 (4:6), D2R55:2단 (5:5), D2R64:2단 (6:4), D2R73:2단 (7:3), D3R111:3단 (1:1:1), D3R112:3단 (1:1:2), D3R121:3단 (1:2:1), D3R211:3단 (2:1:1), D3R221:3단 (2:2:1), D3R212:3단 (2:1:2) */
	private String pltLoutCd;

	/** 공개회사ID */
	private String openCompId;

	/** 모듈참조ID */
	private String mdRid;

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

	/** 메뉴그룹모듈명 */
	private String mnuGrpMdNm;

	/** 리소스명 */
	private String rescNm;

	/** 메뉴그룹구분명 */
	private String mnuGrpTypNm;

	/** 포틀릿레이아웃명 */
	private String pltLoutNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 메뉴그룹ID - KEY */
	public String getMnuGrpId() {
		return mnuGrpId;
	}

	/** 메뉴그룹ID - KEY */
	public void setMnuGrpId(String mnuGrpId) {
		this.mnuGrpId = mnuGrpId;
	}

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	public String getMnuGrpMdCd() {
		return mnuGrpMdCd;
	}

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	public void setMnuGrpMdCd(String mnuGrpMdCd) {
		this.mnuGrpMdCd = mnuGrpMdCd;
	}

	/** 메뉴그룹명 */
	public String getMnuGrpNm() {
		return mnuGrpNm;
	}

	/** 메뉴그룹명 */
	public void setMnuGrpNm(String mnuGrpNm) {
		this.mnuGrpNm = mnuGrpNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 시스템생성여부 */
	public String getSysgYn() {
		return sysgYn;
	}

	/** 시스템생성여부 */
	public void setSysgYn(String sysgYn) {
		this.sysgYn = sysgYn;
	}

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	public String getMnuGrpTypCd() {
		return mnuGrpTypCd;
	}

	/** 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임 */
	public void setMnuGrpTypCd(String mnuGrpTypCd) {
		this.mnuGrpTypCd = mnuGrpTypCd;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 메뉴URL */
	public String getMnuUrl() {
		return mnuUrl;
	}

	/** 메뉴URL */
	public void setMnuUrl(String mnuUrl) {
		this.mnuUrl = mnuUrl;
	}

	/** 팝업설정내용 */
	public String getPopSetupCont() {
		return popSetupCont;
	}

	/** 팝업설정내용 */
	public void setPopSetupCont(String popSetupCont) {
		this.popSetupCont = popSetupCont;
	}

	/** 포틀릿레이아웃코드 - FREE:1단자유구성, D2R37:2단 (3:7), D2R46:2단 (4:6), D2R55:2단 (5:5), D2R64:2단 (6:4), D2R73:2단 (7:3), D3R111:3단 (1:1:1), D3R112:3단 (1:1:2), D3R121:3단 (1:2:1), D3R211:3단 (2:1:1), D3R221:3단 (2:2:1), D3R212:3단 (2:1:2) */
	public String getPltLoutCd() {
		return pltLoutCd;
	}

	/** 포틀릿레이아웃코드 - FREE:1단자유구성, D2R37:2단 (3:7), D2R46:2단 (4:6), D2R55:2단 (5:5), D2R64:2단 (6:4), D2R73:2단 (7:3), D3R111:3단 (1:1:1), D3R112:3단 (1:1:2), D3R121:3단 (1:2:1), D3R211:3단 (2:1:1), D3R221:3단 (2:2:1), D3R212:3단 (2:1:2) */
	public void setPltLoutCd(String pltLoutCd) {
		this.pltLoutCd = pltLoutCd;
	}

	/** 공개회사ID */
	public String getOpenCompId() {
		return openCompId;
	}

	/** 공개회사ID */
	public void setOpenCompId(String openCompId) {
		this.openCompId = openCompId;
	}

	/** 모듈참조ID */
	public String getMdRid() {
		return mdRid;
	}

	/** 모듈참조ID */
	public void setMdRid(String mdRid) {
		this.mdRid = mdRid;
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

	/** 메뉴그룹모듈명 */
	public String getMnuGrpMdNm() {
		return mnuGrpMdNm;
	}

	/** 메뉴그룹모듈명 */
	public void setMnuGrpMdNm(String mnuGrpMdNm) {
		this.mnuGrpMdNm = mnuGrpMdNm;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 메뉴그룹구분명 */
	public String getMnuGrpTypNm() {
		return mnuGrpTypNm;
	}

	/** 메뉴그룹구분명 */
	public void setMnuGrpTypNm(String mnuGrpTypNm) {
		this.mnuGrpTypNm = mnuGrpTypNm;
	}

	/** 포틀릿레이아웃명 */
	public String getPltLoutNm() {
		return pltLoutNm;
	}

	/** 포틀릿레이아웃명 */
	public void setPltLoutNm(String pltLoutNm) {
		this.pltLoutNm = pltLoutNm;
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
			return "com.innobiz.orange.web.pt.dao.PtMnuGrpBDao.selectPtMnuGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuGrpBDao.insertPtMnuGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuGrpBDao.updatePtMnuGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuGrpBDao.deletePtMnuGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuGrpBDao.countPtMnuGrpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메뉴그룹기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(mnuGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpId(메뉴그룹ID-PK):").append(mnuGrpId).append('\n'); }
		if(mnuGrpMdCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpMdCd(메뉴그룹모듈코드):").append(mnuGrpMdCd).append('\n'); }
		if(mnuGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpNm(메뉴그룹명):").append(mnuGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sysgYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysgYn(시스템생성여부):").append(sysgYn).append('\n'); }
		if(mnuGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpTypCd(메뉴그룹구분코드):").append(mnuGrpTypCd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(mnuUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mnuUrl(메뉴URL):").append(mnuUrl).append('\n'); }
		if(popSetupCont!=null) { if(tab!=null) builder.append(tab); builder.append("popSetupCont(팝업설정내용):").append(popSetupCont).append('\n'); }
		if(pltLoutCd!=null) { if(tab!=null) builder.append(tab); builder.append("pltLoutCd(포틀릿레이아웃코드):").append(pltLoutCd).append('\n'); }
		if(openCompId!=null) { if(tab!=null) builder.append(tab); builder.append("openCompId(공개회사ID):").append(openCompId).append('\n'); }
		if(mdRid!=null) { if(tab!=null) builder.append(tab); builder.append("mdRid(모듈참조ID):").append(mdRid).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(openCompNm!=null) { if(tab!=null) builder.append(tab); builder.append("openCompNm(공개회사명):").append(openCompNm).append('\n'); }
		if(openCompIdList!=null) { if(tab!=null) builder.append(tab); builder.append("openCompIdList(공개회사ID목록):"); appendStringListTo(builder, openCompIdList, tab); builder.append('\n'); }
		if(mnuGrpMdNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpMdNm(메뉴그룹모듈명):").append(mnuGrpMdNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(mnuGrpTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpTypNm(메뉴그룹구분명):").append(mnuGrpTypNm).append('\n'); }
		if(pltLoutNm!=null) { if(tab!=null) builder.append(tab); builder.append("pltLoutNm(포틀릿레이아웃명):").append(pltLoutNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
