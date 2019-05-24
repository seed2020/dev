package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시물 예약저장(BA_REZV_SAVE_L) 테이블 VO
 */
public class BaRezvSaveLVo extends BbBullLVo {

	/** serialVersionUID */
	private static final long serialVersionUID = -5021765539430657753L;

	/** 게시판ID목록 */
	private String brdIdList;

	// 추가 컬럼

	/**
	 * 게시판ID목록
	 */
	public String getBrdIdList() {
		return brdIdList;
	}

	/**
	 * 게시판ID목록
	 */
	public void setBrdIdList(String brdIdList) {
		this.brdIdList = brdIdList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRezvSaveLDao.selectBaRezvSaveL";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRezvSaveLDao.insertBaRezvSaveL";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRezvSaveLDao.updateBaRezvSaveL";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRezvSaveLDao.deleteBaRezvSaveL";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRezvSaveLDao.countBaRezvSaveL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시물 예약저장]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (brdIdList != null) { if (tab != null) sb.append(tab); sb.append("brdIdList(게시판ID목록):").append(brdIdList).append('\n'); }
		super.toString(sb, tab);
	}
}