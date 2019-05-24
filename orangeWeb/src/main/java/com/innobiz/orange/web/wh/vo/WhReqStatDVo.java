package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/06/26 17:40 ******/
/**
* 요청상태상세(WH_REQ_STAT_D) 테이블 VO 
*/
public class WhReqStatDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 2435187679239332089L;

	/** 요청번호 */ 
	private String reqNo;

 	/** 상태코드 */ 
	private String statCd;

 	/** HTML여부 */ 
	private String htmlYn;

 	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}

	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}

	public void setHtmlYn(String htmlYn) { 
		this.htmlYn = htmlYn;
	}
	/** HTML여부 */ 
	public String getHtmlYn() { 
		return htmlYn;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqStatDDao.selectWhReqStatD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqStatDDao.insertWhReqStatD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqStatDDao.updateWhReqStatD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqStatDDao.deleteWhReqStatD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqStatDDao.countWhReqStatD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청상태상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(htmlYn!=null) { if(tab!=null) builder.append(tab); builder.append("htmlYn(HTML여부):").append(htmlYn).append('\n'); }
		super.toString(builder, tab);
	}

}
