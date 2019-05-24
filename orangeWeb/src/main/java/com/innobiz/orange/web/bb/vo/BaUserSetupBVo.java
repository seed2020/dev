package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자설정(BA_USER_SETUP_B) 테이블 VO
 */
public class BaUserSetupBVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -5437020721437979988L;

	/** 사용자UID */
	private String userUid;

	/** 기한 */
	private Integer ddln;

	// 추가 컬럼

	/**
	 * 사용자UID
	 */
	public String getUserUid() {
		return userUid;
	}

	/**
	 * 사용자UID
	 */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/**
	 * 기한
	 */
	public Integer getDdln() {
		return ddln;
	}

	/**
	 * 기한
	 */
	public void setDdln(Integer ddln) {
		this.ddln = ddln;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaUserSetupBDao.selectBaUserSetupB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaUserSetupBDao.insertBaUserSetupB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaUserSetupBDao.updateBaUserSetupB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaUserSetupBDao.deleteBaUserSetupB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaUserSetupBDao.countBaUserSetupB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":사용자설정]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (userUid != null) { if (tab != null) sb.append(tab); sb.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if (ddln != null) { if (tab != null) sb.append(tab); sb.append("ddln(기한):").append(ddln).append('\n'); }
		super.toString(sb, tab);
	}
}