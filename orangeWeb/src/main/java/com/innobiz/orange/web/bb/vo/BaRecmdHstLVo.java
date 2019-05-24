package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/**
 * 추천이력(BA_RECMD_HST_L) 테이블 VO
 */
public class BaRecmdHstLVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 7702797183401043785L;

	/** 게시물ID */
	private Integer bullId;

	/** 사용자UID */
	private String userUid;

	/** 등록일시 */
	private String regDt;

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

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
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
			return "com.innobiz.orange.web.bb.dao.BaRecmdHstLDao.selectBaRecmdHstL";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRecmdHstLDao.insertBaRecmdHstL";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRecmdHstLDao.updateBaRecmdHstL";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRecmdHstLDao.deleteBaRecmdHstL";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaRecmdHstLDao.countBaRecmdHstL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":추천이력]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (userUid != null) { if (tab != null) sb.append(tab); sb.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(sb, tab);
	}
}