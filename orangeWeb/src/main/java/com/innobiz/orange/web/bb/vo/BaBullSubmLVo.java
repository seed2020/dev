package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시상신함(BA_BULL_SUBM_L) 테이블 VO
 */
public class BaBullSubmLVo extends BbBullLVo {

	/** serialVersionUID */
	private static final long serialVersionUID = -994528507290789589L;

	/** 게시판ID목록 */
	private String brdIdList;

	/** 반려의견 */
	private String rjtOpin;

	/** 심의자UID */
	private String discrUid;

	/** 심의일시 */
	private String discDt;

	// 추가 컬럼

	/** 게시판ID목록 */
	public String getBrdIdList() {
		return brdIdList;
	}

	/** 게시판ID목록 */
	public void setBrdIdList(String brdIdList) {
		this.brdIdList = brdIdList;
	}

	/** 반려의견 */
	public String getRjtOpin() {
		return rjtOpin;
	}

	/** 반려의견 */
	public void setRjtOpin(String rjtOpin) {
		this.rjtOpin = rjtOpin;
	}

	/** 심의자UID */
	public String getDiscrUid() {
		return discrUid;
	}

	/** 심의자UID */
	public void setDiscrUid(String discrUid) {
		this.discrUid = discrUid;
	}

	/** 심의일시 */
	public String getDiscDt() {
		return discDt;
	}

	/** 심의일시 */
	public void setDiscDt(String discDt) {
		this.discDt = discDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullSubmLDao.selectBaBullSubmL";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullSubmLDao.insertBaBullSubmL";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullSubmLDao.updateBaBullSubmL";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullSubmLDao.deleteBaBullSubmL";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullSubmLDao.countBaBullSubmL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시상신함]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (brdIdList != null) { if (tab != null) sb.append(tab); sb.append("brdIdList(게시판ID목록):").append(brdIdList).append('\n'); }
		if (rjtOpin != null) { if (tab != null) sb.append(tab); sb.append("rjtOpin(반려의견):").append(rjtOpin).append('\n'); }
		if (discrUid != null) { if (tab != null) sb.append(tab); sb.append("discrUid(심의자UID):").append(discrUid).append('\n'); }
		if (discDt != null) { if (tab != null) sb.append(tab); sb.append("discDt(심의일시):").append(discDt).append('\n'); }
		super.toString(sb, tab);
	}
}