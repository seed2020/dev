package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시옵션(BA_BULL_OPT_D) 테이블 VO
 */
public class BaBullOptDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 4156367950704219306L;

	/** 게시물ID */
	private Integer bullId;

	/** 보안여부 */
	private String secuYn;

	/** 긴급여부 */
	private String ugntYn;

	/** 공지여부 */
	private String notcYn;

	// 추가 컬럼

	/** 새게시물ID */
	private Integer newBullId;

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	/** 보안여부 */
	public String getSecuYn() {
		return secuYn;
	}

	/** 보안여부 */
	public void setSecuYn(String secuYn) {
		this.secuYn = secuYn;
	}

	/** 긴급여부 */
	public String getUgntYn() {
		return ugntYn;
	}

	/** 긴급여부 */
	public void setUgntYn(String ugntYn) {
		this.ugntYn = ugntYn;
	}

	/** 공지여부 */
	public String getNotcYn() {
		return notcYn;
	}

	/** 공지여부 */
	public void setNotcYn(String notcYn) {
		this.notcYn = notcYn;
	}

	/** 새게시물ID */
	public Integer getNewBullId() {
		return newBullId;
	}

	/** 새게시물ID */
	public void setNewBullId(Integer newBullId) {
		this.newBullId = newBullId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullOptDDao.selectBaBullOptD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullOptDDao.insertBaBullOptD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullOptDDao.updateBaBullOptD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullOptDDao.deleteBaBullOptD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullOptDDao.countBaBullOptD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시물첨부파일]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (secuYn != null) { if (tab != null) sb.append(tab); sb.append("secuYn(보안여부):").append(secuYn).append('\n'); }
		if (ugntYn != null) { if (tab != null) sb.append(tab); sb.append("ugntYn(긴급여부):").append(ugntYn).append('\n'); }
		if (notcYn != null) { if (tab != null) sb.append(tab); sb.append("notcYn(공지여부):").append(notcYn).append('\n'); }
		if (newBullId != null) { if (tab != null) sb.append(tab); sb.append("newBullId(새게시물ID):").append(newBullId).append('\n'); }
		super.toString(sb, tab);
	}
}