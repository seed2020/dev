package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시대상(BA_BULL_TGT_D) 테이블 VO
 */
public class BaBullTgtDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -2742275993684484179L;

	/** 게시물ID */
	private Integer bullId;

	/** 대상ID */
	private String tgtId;

	/** 대상구분 */
	private String tgtTyp;

	/** 하위포함여부 */
	private String withSubYn;

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

	/** 대상ID */
	public String getTgtId() {
		return tgtId;
	}

	/** 대상ID */
	public void setTgtId(String tgtId) {
		this.tgtId = tgtId;
	}

	/** 대상구분 */
	public String getTgtTyp() {
		return tgtTyp;
	}

	/** 대상구분 */
	public void setTgtTyp(String tgtTyp) {
		this.tgtTyp = tgtTyp;
	}

	/** 하위포함여부 */
	public String getWithSubYn() {
		return withSubYn;
	}

	/** 하위포함여부 */
	public void setWithSubYn(String withSubYn) {
		this.withSubYn = withSubYn;
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
			return "com.innobiz.orange.web.bb.dao.BaBullTgtDDao.selectBaBullTgtD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullTgtDDao.insertBaBullTgtD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullTgtDDao.updateBaBullTgtD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullTgtDDao.deleteBaBullTgtD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullTgtDDao.countBaBullTgtD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시대상]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (tgtId != null) { if (tab != null) sb.append(tab); sb.append("tgtId(대상ID):").append(tgtId).append('\n'); }
		if (tgtTyp != null) { if (tab != null) sb.append(tab); sb.append("tgtTyp(대상구분):").append(tgtTyp).append('\n'); }
		if (withSubYn != null) { if (tab != null) sb.append(tab); sb.append("withSubYn(하위포함여부):").append(withSubYn).append('\n'); }
		if (newBullId != null) { if (tab != null) sb.append(tab); sb.append("newBullId(새게시물ID):").append(newBullId).append('\n'); }
		super.toString(sb, tab);
	}

}