package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 명함사용자화면설정관계(WB_BC_USER_SCRN_SETUP_R) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcUserScrnSetupRVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;
	
	/** 등록자UID */ 
	private String regrUid;

 	/** 입력창위치코드 */ 
	private String inputWndLocCd;

 	/** 목록구분코드 */ 
	private String lstTypCd;
	
 	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setInputWndLocCd(String inputWndLocCd) { 
		this.inputWndLocCd = inputWndLocCd;
	}
	/** 입력창위치코드 */ 
	public String getInputWndLocCd() { 
		return inputWndLocCd;
	}

	public void setLstTypCd(String lstTypCd) { 
		this.lstTypCd = lstTypCd;
	}
	/** 목록구분코드 */ 
	public String getLstTypCd() { 
		return lstTypCd;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserScrnSetupRDao.selectWbBcUserScrnSetupR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserScrnSetupRDao.insertWbBcUserScrnSetupR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserScrnSetupRDao.updateWbBcUserScrnSetupR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserScrnSetupRDao.deleteWbBcUserScrnSetupR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcUserScrnSetupRDao.countWbBcUserScrnSetupR";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함사용자화면설정관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(inputWndLocCd!=null) { if(tab!=null) builder.append(tab); builder.append("inputWndLocCd(입력창위치코드):").append(inputWndLocCd).append('\n'); }
		if(lstTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("lstTypCd(목록구분코드):").append(lstTypCd).append('\n'); }
		super.toString(builder, tab);
	}
	
}