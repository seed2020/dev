package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 그룹기본(WP_PRJ_GRP_B) 테이블 VO
 */
public class WpPrjGrpBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4790836175669882301L;

	/** 그룹ID - KEY */
	private String grpId;

	/** 그룹명 */
	private String grpNm;

	/** 그룹ID - KEY */
	public String getGrpId() {
		return grpId;
	}

	/** 그룹ID - KEY */
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	/** 그룹명 */
	public String getGrpNm() {
		return grpNm;
	}

	/** 그룹명 */
	public void setGrpNm(String grpNm) {
		this.grpNm = grpNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjGrpBDao.selectWpPrjGrpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjGrpBDao.insertWpPrjGrpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjGrpBDao.updateWpPrjGrpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjGrpBDao.deleteWpPrjGrpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjGrpBDao.countWpPrjGrpB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":그룹기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID-PK):").append(grpId).append('\n'); }
		if(grpNm!=null) { if(tab!=null) builder.append(tab); builder.append("grpNm(그룹명):").append(grpNm).append('\n'); }
		super.toString(builder, tab);
	}
}
