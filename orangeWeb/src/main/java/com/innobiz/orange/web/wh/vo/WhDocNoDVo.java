package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/29 11:33 ******/
/**
* 문서번호상세(WH_DOC_NO_D) 테이블 VO 
*/
public class WhDocNoDVo extends CommonVoImpl {
	
 	/** serialVersionUID */
	private static final long serialVersionUID = -547489783577231378L;

	/** 년 */ 
	private String yy;

 	/** 조직ID */ 
	private String orgId;

 	/** 문서ID */ 
	private Integer docSeq;
	
	/** 조직리소스명 */ 
	private String orgRescNm;

	public void setYy(String yy) { 
		this.yy = yy;
	}
	/** 년 */ 
	public String getYy() { 
		return yy;
	}

	public void setOrgId(String orgId) { 
		this.orgId = orgId;
	}
	/** 조직ID */ 
	public String getOrgId() { 
		return orgId;
	}

	public void setDocSeq(Integer docSeq) { 
		this.docSeq = docSeq;
	}
	/** 문서ID */ 
	public Integer getDocSeq() { 
		return docSeq;
	}

	/** 조직리소스명 */ 
	public String getOrgRescNm() {
		return orgRescNm;
	}
	public void setOrgRescNm(String orgRescNm) {
		this.orgRescNm = orgRescNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocNoDDao.selectDmDocNoD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocNoDDao.insertDmDocNoD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocNoDDao.updateDmDocNoD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocNoDDao.deleteDmDocNoD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocNoDDao.countDmDocNoD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서번호상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(yy!=null) { if(tab!=null) builder.append(tab); builder.append("yy(년):").append(yy).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(docSeq!=null) { if(tab!=null) builder.append(tab); builder.append("docSeq(문서일련번호):").append(docSeq).append('\n'); }
		if(orgRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgRescNm(조직리소스명):").append(orgRescNm).append('\n'); }
		super.toString(builder, tab);
	}

}
