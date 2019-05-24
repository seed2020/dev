package com.innobiz.orange.web.bb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 테이블관리(BA_TBL_B) 테이블 VO
 */
public class BaTblBVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -2622179287667060006L;

	/** 테이블ID */
	private String tblId;

	/** 테이블명 */
	private String tblNm;

	/** 테이블표시명 */
	private String tblDispNm;

	/** 리소스ID */
	private String rescId;

	/** 확장여부 */
	private String exYn;

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

	/** 테이블컬럼 VO 리스트 */
	private List<BaTblColmDVo> colmVoList;

	/** 테이블 사용 게시물수 */
	private Integer bullCnt;

	/** 테이블ID */
	public String getTblId() {
		return tblId;
	}

	/** 테이블ID */
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}

	/** 테이블명 */
	public String getTblNm() {
		return tblNm;
	}

	/** 테이블명 */
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}

	/** 테이블표시명 */
	public String getTblDispNm() {
		return tblDispNm;
	}

	/** 테이블표시명 */
	public void setTblDispNm(String tblDispNm) {
		this.tblDispNm = tblDispNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 확장여부 */
	public String getExYn() {
		return exYn;
	}

	/** 확장여부 */
	public void setExYn(String exYn) {
		this.exYn = exYn;
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

	/** 테이블컬럼 VO 리스트 */
	public List<BaTblColmDVo> getColmVoList() {
		return colmVoList;
	}

	/** 테이블컬럼 VO 리스트 */
	public void setColmVoList(List<BaTblColmDVo> colmVoList) {
		this.colmVoList = colmVoList;
	}

	/** 테이블 사용 게시물수 */
	public Integer getBullCnt() {
		return bullCnt;
	}

	/** 테이블 사용 게시물수 */
	public void setBullCnt(Integer bullCnt) {
		this.bullCnt = bullCnt;
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
			return "com.innobiz.orange.web.bb.dao.BaTblBDao.selectBaTblB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblBDao.insertBaTblB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblBDao.updateBaTblB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblBDao.deleteBaTblB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblBDao.countBaTblB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":테이블관리]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (tblId != null) { if (tab != null) sb.append(tab); sb.append("tblId(테이블ID):").append(tblId).append('\n'); }
		if (tblNm != null) { if (tab != null) sb.append(tab); sb.append("tblNm(테이블명):").append(tblNm).append('\n'); }
		if (tblDispNm != null) { if (tab != null) sb.append(tab); sb.append("tblDispNm(테이블표시명):").append(tblDispNm).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (exYn != null) { if (tab != null) sb.append(tab); sb.append("exYn(확장여부):").append(exYn).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if (bullCnt != null) { if (tab != null) sb.append(tab); sb.append("bullCnt(게시물수):").append(bullCnt).append('\n'); }
		if (compId != null) { if (tab != null) sb.append(tab); sb.append("compId(회사ID):").append(compId).append('\n'); }
		super.toString(sb, tab);
	}
}