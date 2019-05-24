package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 나의게시물설정(BA_MY_BULL_M) 테이블 VO
 */
public class BaMyBullMVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -2577189067419089600L;

	/** 사용자UID */
	private String userUid;

	/** 게시판ID */
	private String brdId;

	/** 리소스ID */
	private String rescId;

	// 추가 컬럼

	/** 리소스명 */
	private String rescNm;

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 게시판ID */
	public String getBrdId() {
		return brdId;
	}

	/** 게시판ID */
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaMyBullMDao.selectBaMyBullM";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaMyBullMDao.insertBaMyBullM";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaMyBullMDao.updateBaMyBullM";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaMyBullMDao.deleteBaMyBullM";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaMyBullMDao.countBaMyBullM";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":나의게시물설정]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (userUid != null) { if (tab != null) sb.append(tab); sb.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		super.toString(sb, tab);
	}
}