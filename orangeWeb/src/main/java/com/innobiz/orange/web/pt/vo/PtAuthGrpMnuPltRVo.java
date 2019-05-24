package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 권한그룹메뉴포틀릿관계(PT_AUTH_GRP_MNU_PLT_R) 테이블 VO
 */
public class PtAuthGrpMnuPltRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5799276127767881226L;

	/** 회사ID - KEY */
	private String compId;

	/** 권한범위코드 - KEY */
	private String authScopCd;

	/** 권한그룹ID - KEY */
	private String authGrpId;

	/** 메뉴포틀릿ID - KEY */
	private String mnuPltId;

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	private String authGrpTypCd;

	/** 메뉴그룹ID */
	private String mnuGrpId;

	/** 메뉴포틀릿구분코드 - M:메뉴, P:포틀릿, MG:메뉴그룹, L:레이아웃 */
	private String mnuPltTypCd;

	/** 권한코드 */
	private String authCd;


	// 추가컬럼
	/** 메뉴포틀릿구분명 */
	private String mnuPltTypNm;

	/** 권한명 */
	private String authNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 권한범위코드 - KEY */
	public String getAuthScopCd() {
		return authScopCd;
	}

	/** 권한범위코드 - KEY */
	public void setAuthScopCd(String authScopCd) {
		this.authScopCd = authScopCd;
	}

	/** 권한그룹ID - KEY */
	public String getAuthGrpId() {
		return authGrpId;
	}

	/** 권한그룹ID - KEY */
	public void setAuthGrpId(String authGrpId) {
		this.authGrpId = authGrpId;
	}

	/** 메뉴포틀릿ID - KEY */
	public String getMnuPltId() {
		return mnuPltId;
	}

	/** 메뉴포틀릿ID - KEY */
	public void setMnuPltId(String mnuPltId) {
		this.mnuPltId = mnuPltId;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public String getAuthGrpTypCd() {
		return authGrpTypCd;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public void setAuthGrpTypCd(String authGrpTypCd) {
		this.authGrpTypCd = authGrpTypCd;
	}

	/** 메뉴그룹ID */
	public String getMnuGrpId() {
		return mnuGrpId;
	}

	/** 메뉴그룹ID */
	public void setMnuGrpId(String mnuGrpId) {
		this.mnuGrpId = mnuGrpId;
	}

	/** 메뉴포틀릿구분코드 - M:메뉴, P:포틀릿, MG:메뉴그룹, L:레이아웃 */
	public String getMnuPltTypCd() {
		return mnuPltTypCd;
	}

	/** 메뉴포틀릿구분코드 - M:메뉴, P:포틀릿, MG:메뉴그룹, L:레이아웃 */
	public void setMnuPltTypCd(String mnuPltTypCd) {
		this.mnuPltTypCd = mnuPltTypCd;
	}

	/** 권한코드 */
	public String getAuthCd() {
		return authCd;
	}

	/** 권한코드 */
	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	/** 메뉴포틀릿구분명 */
	public String getMnuPltTypNm() {
		return mnuPltTypNm;
	}

	/** 메뉴포틀릿구분명 */
	public void setMnuPltTypNm(String mnuPltTypNm) {
		this.mnuPltTypNm = mnuPltTypNm;
	}

	/** 권한명 */
	public String getAuthNm() {
		return authNm;
	}

	/** 권한명 */
	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpMnuPltRDao.selectPtAuthGrpMnuPltR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpMnuPltRDao.insertPtAuthGrpMnuPltR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpMnuPltRDao.updatePtAuthGrpMnuPltR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpMnuPltRDao.deletePtAuthGrpMnuPltR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpMnuPltRDao.countPtAuthGrpMnuPltR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":권한그룹메뉴포틀릿관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(authScopCd!=null) { if(tab!=null) builder.append(tab); builder.append("authScopCd(권한범위코드-PK):").append(authScopCd).append('\n'); }
		if(authGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpId(권한그룹ID-PK):").append(authGrpId).append('\n'); }
		if(mnuPltId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPltId(메뉴포틀릿ID-PK):").append(mnuPltId).append('\n'); }
		if(authGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpTypCd(권한그룹구분코드):").append(authGrpTypCd).append('\n'); }
		if(mnuGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("mnuGrpId(메뉴그룹ID):").append(mnuGrpId).append('\n'); }
		if(mnuPltTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPltTypCd(메뉴포틀릿구분코드):").append(mnuPltTypCd).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한코드):").append(authCd).append('\n'); }
		if(mnuPltTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("mnuPltTypNm(메뉴포틀릿구분명):").append(mnuPltTypNm).append('\n'); }
		if(authNm!=null) { if(tab!=null) builder.append(tab); builder.append("authNm(권한명):").append(authNm).append('\n'); }
		super.toString(builder, tab);
	}
}
