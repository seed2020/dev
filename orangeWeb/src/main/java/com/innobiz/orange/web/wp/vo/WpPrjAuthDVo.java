package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 프로잭트권한상세(WP_PRJ_AUTH_D) 테이블 VO
 */
public class WpPrjAuthDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6240701940613153013L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 권한코드 - KEY */
	private String authCd;

	/** 인력ID - KEY */
	private String mpId;

	/** 프로잭트번호 - KEY */
	public String getPrjNo() {
		return prjNo;
	}

	/** 프로잭트번호 - KEY */
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	/** 권한코드 - KEY */
	public String getAuthCd() {
		return authCd;
	}

	/** 권한코드 - KEY */
	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	/** 인력ID - KEY */
	public String getMpId() {
		return mpId;
	}

	/** 인력ID - KEY */
	public void setMpId(String mpId) {
		this.mpId = mpId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAuthDDao.selectWpPrjAuthD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAuthDDao.insertWpPrjAuthD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAuthDDao.updateWpPrjAuthD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAuthDDao.deleteWpPrjAuthD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAuthDDao.countWpPrjAuthD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트권한상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한코드-PK):").append(authCd).append('\n'); }
		if(mpId!=null) { if(tab!=null) builder.append(tab); builder.append("mpId(인력ID-PK):").append(mpId).append('\n'); }
		super.toString(builder, tab);
	}
}
