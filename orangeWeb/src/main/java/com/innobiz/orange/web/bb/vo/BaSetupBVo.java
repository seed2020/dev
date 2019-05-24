package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 설정(BA_SETUP_B) 테이블 VO
 */
public class BaSetupBVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -4494724236540626516L;

	/** 기한 */
	private Integer ddln;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 기한 */
	public Integer getDdln() {
		return ddln;
	}

	/** 기한 */
	public void setDdln(Integer ddln) {
		this.ddln = ddln;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaSetupBDao.selectBaSetupB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaSetupBDao.updateBaSetupB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaSetupBDao.insertBaSetupB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":설정]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (ddln != null) { if (tab != null) sb.append(tab); sb.append("ddln(기한):").append(ddln).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(sb, tab);
	}
}