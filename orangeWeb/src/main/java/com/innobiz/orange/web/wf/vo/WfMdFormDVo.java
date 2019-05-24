package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/07/23 14:23 ******/
/**
* 모듈양식상세(WF_MD_FORM_D) 테이블 VO 
*/
public class WfMdFormDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7720527527100498091L;

	/** 모듈코드 */ 
	private String mdCd;

 	/** 모듈참조ID */ 
	private String mdRefId;

 	/** 양식번호 */ 
	private String formNo;

 	/** 생성ID */ 
	private String genId;

 	/** JSON값 */ 
	private String jsonVa;

 	/** 레이아웃값  */ 
	private String loutVa;

 	/** 속성값 */ 
	private String attrVa;

	public void setMdCd(String mdCd) { 
		this.mdCd = mdCd;
	}
	/** 모듈코드 */ 
	public String getMdCd() { 
		return mdCd;
	}

	public void setMdRefId(String mdRefId) { 
		this.mdRefId = mdRefId;
	}
	/** 모듈참조ID */ 
	public String getMdRefId() { 
		return mdRefId;
	}

	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}

	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}

	public void setJsonVa(String jsonVa) { 
		this.jsonVa = jsonVa;
	}
	/** JSON값 */ 
	public String getJsonVa() { 
		return jsonVa;
	}

	public void setLoutVa(String loutVa) { 
		this.loutVa = loutVa;
	}
	/** 레이아웃값  */ 
	public String getLoutVa() { 
		return loutVa;
	}

	public void setAttrVa(String attrVa) { 
		this.attrVa = attrVa;
	}
	/** 속성값 */ 
	public String getAttrVa() { 
		return attrVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdFormDDao.selectWfMdFormD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdFormDDao.insertWfMdFormD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdFormDDao.updateWfMdFormD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdFormDDao.deleteWfMdFormD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfMdFormDDao.countWfMdFormD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":모듈양식상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(mdCd!=null) { if(tab!=null) builder.append(tab); builder.append("mdCd(모듈코드):").append(mdCd).append('\n'); }
		if(mdRefId!=null) { if(tab!=null) builder.append(tab); builder.append("mdRefId(모듈참조ID):").append(mdRefId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(jsonVa!=null) { if(tab!=null) builder.append(tab); builder.append("jsonVa(JSON값):").append(jsonVa).append('\n'); }
		if(loutVa!=null) { if(tab!=null) builder.append(tab); builder.append("loutVa(레이아웃값 ):").append(loutVa).append('\n'); }
		if(attrVa!=null) { if(tab!=null) builder.append(tab); builder.append("attrVa(속성값):").append(attrVa).append('\n'); }
		super.toString(builder, tab);
	}

}
