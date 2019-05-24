package com.innobiz.orange.web.bb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 카테고리그룹(BA_CAT_GRP_B) 테이블 VO */
public class BaCatGrpBVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -7381944551048804547L;

	/** 카테고리그룹ID */
	private String catGrpId;

	/** 카테고리그룹명 */
	private String catGrpNm;

	/** 리소스ID */
	private String rescId;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;
	
	/** 회사ID */ 
	private String compId;
	
	// 추가 컬럼

	/** 리소스명 */
	private String rescNm;

	/** 카테고리 목록 */
	private List<BaCatDVo> catVoList;

	/** 카테고리 사용 게시판 수 */
	private Integer brdCnt;

	/** 카테고리그룹ID */
	public String getCatGrpId() {
		return catGrpId;
	}

	/** 카테고리그룹ID */
	public void setCatGrpId(String catGrpId) {
		this.catGrpId = catGrpId;
	}

	/** 카테고리그룹명 */
	public String getCatGrpNm() {
		return catGrpNm;
	}

	/** 카테고리그룹명 */
	public void setCatGrpNm(String catGrpNm) {
		this.catGrpNm = catGrpNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
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

	/** 카테고리 목록 */
	public List<BaCatDVo> getCatVoList() {
		return catVoList;
	}

	/** 카테고리 목록 */
	public void setCatVoList(List<BaCatDVo> catVoList) {
		this.catVoList = catVoList;
	}

	/** 카테고리 사용 게시판 수 */
	public Integer getBrdCnt() {
		return brdCnt;
	}

	/** 카테고리 사용 게시판 수 */
	public void setBrdCnt(Integer brdCnt) {
		this.brdCnt = brdCnt;
	}
	
	/** 회사ID */
	public String getCompId() {
		return compId;
	}
	
	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatGrpBDao.selectBaCatGrpB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatGrpBDao.insertBaCatGrpB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatGrpBDao.updateBaCatGrpB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatGrpBDao.deleteBaCatGrpB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCatGrpBDao.countBaCatGrpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":카테고리그룹]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (catGrpId != null) { if (tab != null) sb.append(tab); sb.append("catGrpId(카테고리그룹ID):").append(catGrpId).append('\n'); }
		if (catGrpNm != null) { if (tab != null) sb.append(tab); sb.append("catGrpNm(카테고리그룹명):").append(catGrpNm).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if (brdCnt != null) { if (tab != null) sb.append(tab); sb.append("brdCnt(카테고리 사용 게시판 수):").append(brdCnt).append('\n'); }
		if (compId != null) { if (tab != null) sb.append(tab); sb.append("compId(회사ID):").append(compId).append('\n'); }
		super.toString(sb, tab);
	}
}