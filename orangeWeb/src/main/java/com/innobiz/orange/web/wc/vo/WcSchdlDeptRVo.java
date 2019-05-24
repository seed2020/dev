package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/06/05 11:34 ******/
/**
* 부서일정관계(WC_SCHDL_DEPT_R) 테이블 VO 
*/
public class WcSchdlDeptRVo extends CommonVoImpl { 
	
	/** serialVersionUID */
	private static final long serialVersionUID = 4572371907395877151L;

	/** 일정ID */ 
	private String schdlId;

 	/** 조직ID */ 
	private String orgId;
	
	/** 조직명 */ 
	private String orgNm;

 	public void setSchdlId(String schdlId) { 
		this.schdlId = schdlId;
	}
	/** 일정ID */ 
	public String getSchdlId() { 
		return schdlId;
	}

	public void setOrgId(String orgId) { 
		this.orgId = orgId;
	}
	/** 조직ID */ 
	public String getOrgId() { 
		return orgId;
	}
	
	/** 조직명 */ 
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcSchdlDeptRDao.selectWcSchdlDeptR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcSchdlDeptRDao.insertWcSchdlDeptR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcSchdlDeptRDao.updateWcSchdlDeptR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcSchdlDeptRDao.deleteWcSchdlDeptR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcSchdlDeptRDao.countWcSchdlDeptR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":부서일정관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		super.toString(builder, tab);
	}

}
