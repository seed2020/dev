package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 결재라인그룹기본(AP_APV_LN_GRP_B) 테이블 VO
 */
public class ApApvLnGrpBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2650333302515861201L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 결재라인그룹ID - KEY */
	private String apvLnGrpId;

	/** 결재라인그룹명 */
	private String apvLnGrpNm;

	/** 리소스ID */
	private String rescId;

	/** 정렬순서 */
	private String sortOrdr;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 결재라인그룹구분코드 */
	private String apvLnGrpTypCd;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 리소스명 */
	private String rescNm;

	/** 수정자명 */
	private String modrNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 결재라인그룹ID - KEY */
	public String getApvLnGrpId() {
		return apvLnGrpId;
	}

	/** 결재라인그룹ID - KEY */
	public void setApvLnGrpId(String apvLnGrpId) {
		this.apvLnGrpId = apvLnGrpId;
	}

	/** 결재라인그룹명 */
	public String getApvLnGrpNm() {
		return apvLnGrpNm;
	}

	/** 결재라인그룹명 */
	public void setApvLnGrpNm(String apvLnGrpNm) {
		this.apvLnGrpNm = apvLnGrpNm;
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

	/** 결재라인그룹구분코드 */
	public String getApvLnGrpTypCd() {
		return apvLnGrpTypCd;
	}

	/** 결재라인그룹구분코드 */
	public void setApvLnGrpTypCd(String apvLnGrpTypCd) {
		this.apvLnGrpTypCd = apvLnGrpTypCd;
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
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpBDao.selectApApvLnGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpBDao.insertApApvLnGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpBDao.updateApApvLnGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpBDao.deleteApApvLnGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpBDao.countApApvLnGrpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":결재라인그룹기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(apvLnGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpId(결재라인그룹ID-PK):").append(apvLnGrpId).append('\n'); }
		if(apvLnGrpNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpNm(결재라인그룹명):").append(apvLnGrpNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(apvLnGrpTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpTypCd(결재라인그룹구분코드):").append(apvLnGrpTypCd).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
