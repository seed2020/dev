package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함목록설정기본(WB_BC_LST_SETUP_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcLstSetupBVo extends WbBcUserLstSetupRVo {

 	/** 기본설정여부 */ 
	private String dftSetupYn;
	
	/** value length(Byte) */ 
	private int vaLen;

	public void setDftSetupYn(String dftSetupYn) { 
		this.dftSetupYn = dftSetupYn;
	}
	/** 기본설정여부 */ 
	public String getDftSetupYn() { 
		return dftSetupYn;
	}
	
	public int getVaLen() {
		return vaLen;
	}
	public void setVaLen(int vaLen) {
		this.vaLen = vaLen;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcLstSetupBDao.selectWbBcLstSetupB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcLstSetupBDao.insertWbBcLstSetupB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcLstSetupBDao.updateWbBcLstSetupB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcLstSetupBDao.deleteWbBcLstSetupB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcLstSetupBDao.countWbBcLstSetupB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함목록설정기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(dftSetupYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftSetupYn(기본설정여부):").append(dftSetupYn).append('\n'); }
		if(vaLen!=0) { if(tab!=null) builder.append(tab); builder.append("vaLen(value length(Byte)):").append(vaLen).append('\n'); }
		super.toString(builder, tab);
	}
	
}