package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 권한그룹사용자관계(PT_AUTH_GRP_USER_R) 테이블 VO
 */
public class PtAuthGrpUserRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1892501469078105817L;

	/** 회사ID - KEY */
	private String compId;

	/** 권한그룹ID - KEY */
	private String authGrpId;

	/** 제외대상여부 - KEY */
	private String excliYn;

	/** 사용자UID - KEY */
	private String userUid;

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	private String authGrpTypCd;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 회사ID목록 */
	private List<String> compIdList;

	/** 사용자명 */
	private String userNm;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 권한그룹ID - KEY */
	public String getAuthGrpId() {
		return authGrpId;
	}

	/** 권한그룹ID - KEY */
	public void setAuthGrpId(String authGrpId) {
		this.authGrpId = authGrpId;
	}

	/** 제외대상여부 - KEY */
	public String getExcliYn() {
		return excliYn;
	}

	/** 제외대상여부 - KEY */
	public void setExcliYn(String excliYn) {
		this.excliYn = excliYn;
	}

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public String getAuthGrpTypCd() {
		return authGrpTypCd;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public void setAuthGrpTypCd(String authGrpTypCd) {
		this.authGrpTypCd = authGrpTypCd;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	/** 회사ID목록 */
	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpUserRDao.selectPtAuthGrpUserR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpUserRDao.insertPtAuthGrpUserR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpUserRDao.updatePtAuthGrpUserR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpUserRDao.deletePtAuthGrpUserR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpUserRDao.countPtAuthGrpUserR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":권한그룹사용자관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(authGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpId(권한그룹ID-PK):").append(authGrpId).append('\n'); }
		if(excliYn!=null) { if(tab!=null) builder.append(tab); builder.append("excliYn(제외대상여부-PK):").append(excliYn).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(authGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpTypCd(권한그룹구분코드):").append(authGrpTypCd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(compIdList!=null) { if(tab!=null) builder.append(tab); builder.append("compIdList(회사ID목록):"); appendStringListTo(builder, compIdList, tab); builder.append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
