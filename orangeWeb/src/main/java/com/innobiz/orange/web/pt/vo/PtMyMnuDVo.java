package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 나의메뉴상세(PT_MY_MNU_D) 테이블 VO
 */
public class PtMyMnuDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6791479746887191448L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 메뉴레이아웃조합ID - KEY */
	private String mnuLoutCombId;

	/** 메뉴레이아웃조합부모ID */
	private String mnuLoutCombPid;

	/** 회사ID */
	private String compId;

	/** 폴더여부 */
	private String fldYn;

	/** 메뉴명 */
	private String mnuNm;

	/** 리소스ID */
	private String rescId;

	/** 메뉴ID */
	private String mnuId;

	/** 메뉴리소스ID */
	private String mnuRescId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 리소스명 */
	private String rescNm;

	/** 메뉴리소스명 */
	private String mnuRescNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 메뉴레이아웃조합ID - KEY */
	public String getMnuLoutCombId() {
		return mnuLoutCombId;
	}

	/** 메뉴레이아웃조합ID - KEY */
	public void setMnuLoutCombId(String mnuLoutCombId) {
		this.mnuLoutCombId = mnuLoutCombId;
	}

	/** 메뉴레이아웃조합부모ID */
	public String getMnuLoutCombPid() {
		return mnuLoutCombPid;
	}

	/** 메뉴레이아웃조합부모ID */
	public void setMnuLoutCombPid(String mnuLoutCombPid) {
		this.mnuLoutCombPid = mnuLoutCombPid;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 폴더여부 */
	public String getFldYn() {
		return fldYn;
	}

	/** 폴더여부 */
	public void setFldYn(String fldYn) {
		this.fldYn = fldYn;
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

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
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
			return "com.innobiz.orange.web.pt.dao.PtMyMnuDDao.selectPtMyMnuD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMyMnuDDao.insertPtMyMnuD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMyMnuDDao.updatePtMyMnuD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMyMnuDDao.deletePtMyMnuD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtMyMnuDDao.countPtMyMnuD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":나의메뉴상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(mnuLoutCombId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutCombId(메뉴레이아웃조합ID-PK):").append(mnuLoutCombId).append('\n'); }
		if(mnuLoutCombPid!=null) { if(tab!=null) builder.append(tab); builder.append("mnuLoutCombPid(메뉴레이아웃조합부모ID):").append(mnuLoutCombPid).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(fldYn!=null) { if(tab!=null) builder.append(tab); builder.append("fldYn(폴더여부):").append(fldYn).append('\n'); }
		if(mnuNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuNm(메뉴명):").append(mnuNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(mnuId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuId(메뉴ID):").append(mnuId).append('\n'); }
		if(mnuRescId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuRescId(메뉴리소스ID):").append(mnuRescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(mnuRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuRescNm(메뉴리소스명):").append(mnuRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
