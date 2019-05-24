package com.innobiz.orange.web.wa.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/12/05 10:27 ******/
/**
* 법인기본(WA_CORP_B) 테이블 VO 
*/
public class WaCorpBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -5312804559134271233L;

	/** 법인번호 */ 
	private String corpNo;

 	/** 회사ID */ 
	private String compId;

 	/** 법인명 */ 
	private String corpNm;

 	/** 사업자등록번호 */ 
	private String corpRegNo;

 	public void setCorpNo(String corpNo) { 
		this.corpNo = corpNo;
	}
	/** 법인번호 */ 
	public String getCorpNo() { 
		return corpNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCorpNm(String corpNm) { 
		this.corpNm = corpNm;
	}
	/** 법인명 */ 
	public String getCorpNm() { 
		return corpNm;
	}

	public void setCorpRegNo(String corpRegNo) { 
		this.corpRegNo = corpRegNo;
	}
	/** 사업자등록번호 */ 
	public String getCorpRegNo() { 
		return corpRegNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCorpBDao.selectWaCorpB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCorpBDao.insertWaCorpB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCorpBDao.updateWaCorpB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCorpBDao.deleteWaCorpB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCorpBDao.countWaCorpB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":법인기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(corpNo!=null) { if(tab!=null) builder.append(tab); builder.append("corpNo(법인번호):").append(corpNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(corpNm!=null) { if(tab!=null) builder.append(tab); builder.append("corpNm(법인명):").append(corpNm).append('\n'); }
		if(corpRegNo!=null) { if(tab!=null) builder.append(tab); builder.append("corpRegNo(사업자등록번호):").append(corpRegNo).append('\n'); }
		super.toString(builder, tab);
	}

}
