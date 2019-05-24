package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시옵션(BA_BULL_POPUP_DISP_D) 테이블 VO
 */
public class BaBullPopupDispDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -3834099481367271367L;

	/** 게시물ID */
	private Integer bullId;

	/** 게시판ID */
	private String brdId;
	
	/** 사용자UID */
	private String userUid;
	
	/** 처리일 */
	private String updateDt;
	
	/** 시작일 */
	private String strtDt;
	
	/** 종료일 */
	private String endDt;
	

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}
	
	/** 게시판ID */
	public String getBrdId() {
		return brdId;
	}

	/** 게시판ID */
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}
	
	/** 처리일 */
	public String getUpdateDt() {
		return updateDt;
	}

	/** 처리일 */
	public void setUpdateDt(String updateDt) {
		this.updateDt= updateDt;
	}
	
	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 시작일 */
	public String getStrtDt() {
		return strtDt;
	}

	/** 시작일 */
	public void setStrtDt(String strtDt) {
		this.strtDt = strtDt;
	}
	
	/** 종료일 */
	public String getEndDt() {
		return endDt;
	}

	/** 종료일 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDispDDao.selectBaBullPopupDispD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDispDDao.insertBaBullPopupDispD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDispDDao.updateBaBullPopupDispD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDispDDao.deleteBaBullPopupDispD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDispDDao.countBaBullPopupDispD";
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
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (userUid != null) { if (tab != null) sb.append(tab); sb.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if (updateDt != null) { if (tab != null) sb.append(tab); sb.append("updateDt(처리일):").append(updateDt).append('\n'); }

		super.toString(sb, tab);
	}
}