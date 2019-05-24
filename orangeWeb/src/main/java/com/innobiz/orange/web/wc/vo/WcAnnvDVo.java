package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/12/21 15:34 ******/
/**
* 기념일상세(WC_ANNV_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WcAnnvDVo  extends CommonVoImpl {	
	/** 회사ID */ 
	private String compId;

 	/** 일정ID */ 
	private String schdlId;

 	/** 리소스ID */ 
	private String rescId;

 	/** 국가코드 */ 
	private String natCd;
	
	/** 리소스명 */ 
	private String rescNm;

 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setSchdlId(String schdlId) { 
		this.schdlId = schdlId;
	}
	/** 일정ID */ 
	public String getSchdlId() { 
		return schdlId;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setNatCd(String natCd) { 
		this.natCd = natCd;
	}
	/** 국가코드 */ 
	public String getNatCd() { 
		return natCd;
	}
	
	/** 리소스명 */ 
	public String getRescNm() {
		return rescNm;
	}
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcAnnvDDao.selectWcAnnvD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcAnnvDDao.insertWcAnnvD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcAnnvDDao.updateWcAnnvD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcAnnvDDao.deleteWcAnnvD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcAnnvDDao.countWcAnnvD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":기념일상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(natCd!=null) { if(tab!=null) builder.append(tab); builder.append("natCd(국가코드):").append(natCd).append('\n'); }
		super.toString(builder, tab);
	}

}
