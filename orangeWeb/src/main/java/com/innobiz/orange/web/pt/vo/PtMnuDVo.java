package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 메뉴상세(PT_MNU_D) 테이블 VO
 */
public class PtMnuDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7110912247218708780L;

	/** 메뉴ID - KEY */
	private String mnuId;

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	private String mnuGrpMdCd;

	/** 메뉴그룹ID */
	private String mnuGrpId;

	/** 메뉴명 */
	private String mnuNm;

	/** 리소스ID */
	private String rescId;

	/** 메뉴부모ID */
	private String mnuPid;

	/** 정렬순서 */
	private String sortOrdr;

	/** 메뉴URL */
	private String mnuUrl;

	/** 메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업 */
	private String mnuTypCd;

	/** 팝업설정내용 */
	private String popSetupCont;

	/** 메뉴설명 */
	private String mnuDesc;

	/** 폴더여부 */
	private String fldYn;

	/** 시스템메뉴여부 */
	private String sysMnuYn;

	/** 모듈참조ID */
	private String mdRid;

	/** 모듈ID */
	private String mdId;

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
	/** 메뉴레벨 */
	private String mnuLvl;

	/** 메뉴경로 */
	private String mnuPath;

	/** 자식 VO 목록 */
	private List<PtMnuDVo> childList;

	/** 메뉴그룹모듈명 */
	private String mnuGrpMdNm;

	/** 리소스명 */
	private String rescNm;

	/** 메뉴구분명 */
	private String mnuTypNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 메뉴ID - KEY */
	public String getMnuId() {
		return mnuId;
	}

	/** 메뉴ID - KEY */
	public void setMnuId(String mnuId) {
		this.mnuId = mnuId;
	}

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	public String getMnuGrpMdCd() {
		return mnuGrpMdCd;
	}

	/** 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일 */
	public void setMnuGrpMdCd(String mnuGrpMdCd) {
		this.mnuGrpMdCd = mnuGrpMdCd;
	}

	/** 메뉴그룹ID */
	public String getMnuGrpId() {
		return mnuGrpId;
	}

	/** 메뉴그룹ID */
	public void setMnuGrpId(String mnuGrpId) {
		this.mnuGrpId = mnuGrpId;
	}

	/** 메뉴명 */
	public String getMnuNm() {
		return mnuNm;
	}

	/** 메뉴명 */
	public void setMnuNm(String mnuNm) {
		this.mnuNm = mnuNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 메뉴부모ID */
	public String getMnuPid() {
		return mnuPid;
	}

	/** 메뉴부모ID */
	public void setMnuPid(String mnuPid) {
		this.mnuPid = mnuPid;
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

	/** 메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업 */
	public String getMnuTypCd() {
		return mnuTypCd;
	}

	/** 메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업 */
	public void setMnuTypCd(String mnuTypCd) {
		this.mnuTypCd = mnuTypCd;
	}

	/** 팝업설정내용 */
	public String getPopSetupCont() {
		return popSetupCont;
	}

	/** 팝업설정내용 */
	public void setPopSetupCont(String popSetupCont) {
		this.popSetupCont = popSetupCont;
	}

	/** 메뉴설명 */
	public String getMnuDesc() {
		return mnuDesc;
	}

	/** 메뉴설명 */
	public void setMnuDesc(String mnuDesc) {
		this.mnuDesc = mnuDesc;
	}

	/** 폴더여부 */
	public String getFldYn() {
		return fldYn;
	}

	/** 폴더여부 */
	public void setFldYn(String fldYn) {
		this.fldYn = fldYn;
	}

	/** 시스템메뉴여부 */
	public String getSysMnuYn() {
		return sysMnuYn;
	}

	/** 시스템메뉴여부 */
	public void setSysMnuYn(String sysMnuYn) {
		this.sysMnuYn = sysMnuYn;
	}

	/** 모듈참조ID */
	public String getMdRid() {
		return mdRid;
	}

	/** 모듈참조ID */
	public void setMdRid(String mdRid) {
		this.mdRid = mdRid;
	}

	/** 모듈ID */
	public String getMdId() {
		return mdId;
	}

	/** 모듈ID */
	public void setMdId(String mdId) {
		this.mdId = mdId;
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

	/** 메뉴레벨 */
	public String getMnuLvl() {
		return mnuLvl;
	}

	/** 메뉴레벨 */
	public void setMnuLvl(String mnuLvl) {
		this.mnuLvl = mnuLvl;
	}

	/** 메뉴경로 */
	public String getMnuPath() {
		return mnuPath;
	}

	/** 메뉴경로 */
	public void setMnuPath(String mnuPath) {
		this.mnuPath = mnuPath;
	}

	/** 자식 VO 목록 */
	public List<PtMnuDVo> getChildList() {
		return childList;
	}

	/** 자식 VO 목록 */
	public void setChildList(List<PtMnuDVo> childList) {
		this.childList = childList;
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

	/** 메뉴구분명 */
	public String getMnuTypNm() {
		return mnuTypNm;
	}

	/** 메뉴구분명 */
	public void setMnuTypNm(String mnuTypNm) {
		this.mnuTypNm = mnuTypNm;
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
			return "com.innobiz.orange.web.pt.dao.PtMnuDDao.selectPtMnuD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuDDao.insertPtMnuD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuDDao.updatePtMnuD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuDDao.deletePtMnuD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMnuDDao.countPtMnuD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메뉴상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(mnuId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuId(메뉴ID-PK):").append(mnuId).append('\n'); }
		if(mnuGrpMdCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpMdCd(메뉴그룹모듈코드):").append(mnuGrpMdCd).append('\n'); }
		if(mnuGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpId(메뉴그룹ID):").append(mnuGrpId).append('\n'); }
		if(mnuNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuNm(메뉴명):").append(mnuNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(mnuPid!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPid(메뉴부모ID):").append(mnuPid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(mnuUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mnuUrl(메뉴URL):").append(mnuUrl).append('\n'); }
		if(mnuTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuTypCd(메뉴구분코드):").append(mnuTypCd).append('\n'); }
		if(popSetupCont!=null) { if(tab!=null) builder.append(tab); builder.append("popSetupCont(팝업설정내용):").append(popSetupCont).append('\n'); }
		if(mnuDesc!=null) { if(tab!=null) builder.append(tab); builder.append("mnuDesc(메뉴설명):").append(mnuDesc).append('\n'); }
		if(fldYn!=null) { if(tab!=null) builder.append(tab); builder.append("fldYn(폴더여부):").append(fldYn).append('\n'); }
		if(sysMnuYn!=null) { if(tab!=null) builder.append(tab); builder.append("sysMnuYn(시스템메뉴여부):").append(sysMnuYn).append('\n'); }
		if(mdRid!=null) { if(tab!=null) builder.append(tab); builder.append("mdRid(모듈참조ID):").append(mdRid).append('\n'); }
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(mnuLvl!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLvl(메뉴레벨):").append(mnuLvl).append('\n'); }
		if(mnuPath!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPath(메뉴경로):").append(mnuPath).append('\n'); }
		if(childList!=null) { if(tab!=null) builder.append(tab); builder.append("childList(자식 VO 목록):"); appendVoListTo(builder, childList, tab); builder.append('\n'); }
		if(mnuGrpMdNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpMdNm(메뉴그룹모듈명):").append(mnuGrpMdNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(mnuTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuTypNm(메뉴구분명):").append(mnuTypNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
