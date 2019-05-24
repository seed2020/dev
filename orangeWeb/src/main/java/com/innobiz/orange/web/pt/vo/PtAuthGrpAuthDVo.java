package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 VO
 */
public class PtAuthGrpAuthDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3061338728472344912L;

	/** 회사ID - KEY */
	private String compId;

	/** 권한그룹ID - KEY */
	private String authGrpId;

	/** 제외대상여부 - KEY */
	private String excliYn;

	/** 일련번호 - KEY */
	private String seq;

	/** 그룹종류코드 - KEY */
	private String grpKndCd;

	/** 그룹ID - KEY */
	private String grpId;

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	private String authGrpTypCd;

	/** 리소스ID */
	private String rescId;

	/** 하위포함여부 */
	private String subInclYn;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
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

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 그룹종류코드 - KEY */
	public String getGrpKndCd() {
		return grpKndCd;
	}

	/** 그룹종류코드 - KEY */
	public void setGrpKndCd(String grpKndCd) {
		this.grpKndCd = grpKndCd;
	}

	/** 그룹ID - KEY */
	public String getGrpId() {
		return grpId;
	}

	/** 그룹ID - KEY */
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public String getAuthGrpTypCd() {
		return authGrpTypCd;
	}

	/** 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹 */
	public void setAuthGrpTypCd(String authGrpTypCd) {
		this.authGrpTypCd = authGrpTypCd;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 하위포함여부 */
	public String getSubInclYn() {
		return subInclYn;
	}

	/** 하위포함여부 */
	public void setSubInclYn(String subInclYn) {
		this.subInclYn = subInclYn;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
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
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpAuthDDao.selectPtAuthGrpAuthD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpAuthDDao.insertPtAuthGrpAuthD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpAuthDDao.updatePtAuthGrpAuthD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpAuthDDao.deletePtAuthGrpAuthD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtAuthGrpAuthDDao.countPtAuthGrpAuthD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":권한그룹권한상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(authGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpId(권한그룹ID-PK):").append(authGrpId).append('\n'); }
		if(excliYn!=null) { if(tab!=null) builder.append(tab); builder.append("excliYn(제외대상여부-PK):").append(excliYn).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(grpKndCd!=null) { if(tab!=null) builder.append(tab); builder.append("grpKndCd(그룹종류코드-PK):").append(grpKndCd).append('\n'); }
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID-PK):").append(grpId).append('\n'); }
		if(authGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGrpTypCd(권한그룹구분코드):").append(authGrpTypCd).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(subInclYn!=null) { if(tab!=null) builder.append(tab); builder.append("subInclYn(하위포함여부):").append(subInclYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
