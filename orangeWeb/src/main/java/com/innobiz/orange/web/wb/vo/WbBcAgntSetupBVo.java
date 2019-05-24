package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함대리설정기본(WB_BC_AGNT_SETUP_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcAgntSetupBVo extends CommonVoImpl {
	/** 등록자UID */ 
	private String regrUid;

 	/** 명함등록자UID */ 
	private String bcRegrUid;
	
/** 추가 */
	/** 사용자명 */
	private String userNm;
	
 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setBcRegrUid(String bcRegrUid) { 
		this.bcRegrUid = bcRegrUid;
	}
	/** 명함등록자UID */ 
	public String getBcRegrUid() { 
		return bcRegrUid;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntSetupBDao.selectWbBcAgntSetupB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntSetupBDao.insertWbBcAgntSetupB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntSetupBDao.updateWbBcAgntSetupB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntSetupBDao.deleteWbBcAgntSetupB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcAgntSetupBDao.countWbBcAgntSetupB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함대리설정기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(bcRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("bcRegrUid(명함등록자UID):").append(bcRegrUid).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
	
}