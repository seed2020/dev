package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함지정인관계(WB_BC_APNTR_R) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcApntrRVo extends CommonVoImpl {
	/** 명함ID */ 
	private String bcId;

 	/** 명함지정인UID */ 
	private String bcApntrUid;
	
	/** 지정인명 */
	private String userNm;
	
	/** 신규여부 */
	private String updateYn;
	
	/** 삭제처리 배열 */
	private String[] deleteUids;
	
 	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public void setBcApntrUid(String bcApntrUid) { 
		this.bcApntrUid = bcApntrUid;
	}
	/** 명함지정인UID */ 
	public String getBcApntrUid() { 
		return bcApntrUid;
	}
	
	
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getUpdateYn() {
		return updateYn;
	}
	public void setUpdateYn(String updateYn) {
		this.updateYn = updateYn;
	}
	
	public String[] getDeleteUids() {
		return deleteUids;
	}
	public void setDeleteUids(String[] deleteUids) {
		this.deleteUids = deleteUids;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcApntrRDao.selectWbBcApntrR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcApntrRDao.insertWbBcApntrR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcApntrRDao.updateWbBcApntrR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcApntrRDao.deleteWbBcApntrR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcApntrRDao.countWbBcApntrR";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함지정인관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(bcApntrUid!=null) { if(tab!=null) builder.append(tab); builder.append("bcApntrUid(명함지정인UID):").append(bcApntrUid).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(지정인명):").append(userNm).append('\n'); }
		if(updateYn!=null) { if(tab!=null) builder.append(tab); builder.append("updateYn(신규여부):").append(updateYn).append('\n'); }
		if(deleteUids!=null) { if(tab!=null) builder.append(tab); builder.append("deleteUids(삭제처리 배열):"); appendArrayTo(builder, deleteUids); builder.append('\n'); }
		super.toString(builder, tab);
	}
}