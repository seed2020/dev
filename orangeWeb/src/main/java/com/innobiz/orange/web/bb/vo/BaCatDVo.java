package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 카테고리(BA_CAT_D) 테이블 VO */
public class BaCatDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -1588525614605916038L;

	/** 카테고리ID */
	private String catId;

	/** 카테고리그룹ID */
	private String catGrpId;

	/** 카테고리명 */
	private String catNm;

	/** 리소스ID */
	private String rescId;

	/** 표시순서 */
	private Integer dispOrdr;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	// 추가 컬럼

	/** 리소스명 */
	private String rescNm;

	/** 유효성 체크용 속성 */
	private String valid;

	/** 삭제여부 확인용 속성 */
	private String deleted;

	/** 카테고리ID */
	public String getCatId() {
		return catId;
	}

	/** 카테고리ID */
	public void setCatId(String catId) {
		this.catId = catId;
	}

	/** 카테고리그룹ID */
	public String getCatGrpId() {
		return catGrpId;
	}

	/** 카테고리그룹ID */
	public void setCatGrpId(String catGrpId) {
		this.catGrpId = catGrpId;
	}

	/** 카테고리명 */
	public String getCatNm() {
		return catNm;
	}

	/** 카테고리명 */
	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 표시순서 */
	public Integer getDispOrdr() {
		return dispOrdr;
	}

	/** 표시순서 */
	public void setDispOrdr(Integer dispOrdr) {
		this.dispOrdr = dispOrdr;
	}

	/** 등록자 */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자 */
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

	/** 수정자 */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자 */
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

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 유효성 체크용 속성 */
	public String getValid() {
		return valid;
	}

	/** 유효성 체크용 속성 */
	public void setValid(String valid) {
		this.valid = valid;
	}

	/** 삭제여부 확인용 속성 */
	public String getDeleted() {
		return deleted;
	}

	/** 삭제여부 확인용 속성 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatDDao.selectBaCatD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatDDao.insertBaCatD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatDDao.updateBaCatD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatDDao.deleteBaCatD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatDDao.countBaCatD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":카테고리]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (catId != null) { if (tab != null) sb.append(tab); sb.append("catId(카테고리ID):").append(catId).append('\n'); }
		if (catGrpId != null) { if (tab != null) sb.append(tab); sb.append("catGrpId(카테고리그룹ID):").append(catGrpId).append('\n'); }
		if (catNm != null) { if (tab != null) sb.append(tab); sb.append("catNm(카테고리명):").append(catNm).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (dispOrdr != null) { if (tab != null) sb.append(tab); sb.append("dispOrdr(표시순서):").append(dispOrdr).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if (valid != null) { if (tab != null) sb.append(tab); sb.append("valid(유효성체크):").append(valid).append('\n'); }
		if (deleted != null) { if (tab != null) sb.append(tab); sb.append("deleted(삭제여부):").append(deleted).append('\n'); }
		super.toString(sb, tab);
	}
}