package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/**
 * 조회이력(BA_READ_HST_L) 테이블 VO
 */
public class BaReadHstLVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -4844932361597020958L;

	/** 게시물ID */
	private Integer bullId;

	/** 사용자UID */
	private String userUid;

	/** 조회일시 */
	private String readDt;

	// 추가 컬럼

	/** 사용자명 */
	private String userNm;

	/** 사용자기본(OR_USER_B) 테이블 VO */
	private OrUserBVo orUserBVo;

	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	private OrOdurBVo orOdurBVo;

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 조회일시 */
	public String getReadDt() {
		return readDt;
	}

	/** 조회일시 */
	public void setReadDt(String readDt) {
		this.readDt = readDt;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 사용자기본(OR_USER_B) 테이블 VO */
	public OrUserBVo getOrUserBVo() {
		return orUserBVo;
	}

	/** 사용자기본(OR_USER_B) 테이블 VO */
	public void setOrUserBVo(OrUserBVo orUserBVo) {
		this.orUserBVo = orUserBVo;
	}

	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	public OrOdurBVo getOrOdurBVo() {
		return orOdurBVo;
	}

	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	public void setOrOdurBVo(OrOdurBVo orOdurBVo) {
		this.orOdurBVo = orOdurBVo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaReadHstLDao.selectBaReadHstL";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaReadHstLDao.insertBaReadHstL";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaReadHstLDao.updateBaReadHstL";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaReadHstLDao.deleteBaReadHstL";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaReadHstLDao.countBaReadHstL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":조회이력]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (userUid != null) { if (tab != null) sb.append(tab); sb.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if (readDt != null) { if (tab != null) sb.append(tab); sb.append("readDt(조회일시):").append(readDt).append('\n'); }
		super.toString(sb, tab);
	}
}