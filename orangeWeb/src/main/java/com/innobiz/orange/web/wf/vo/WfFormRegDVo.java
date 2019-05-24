package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/28 16:57 ******/
/**
* 양식등록상세(WF_FORM_REG_D) 테이블 VO 
*/
public class WfFormRegDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 942324503854628017L;

	/** 생성ID */ 
	private String genId;
	
	/** 양식번호 */ 
	private String formNo;

	/** 레이아웃값  */ 
	private String loutVa;

 	/** 속성값 */ 
	private String attrVa;

 	/** 탭값 */ 
	private String tabVa;
	
	/** 모바일등록구분코드 */ 
	private String mobRegTypCd;
	
	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 모바일여부 */ 
	private boolean isMob=false;
	
	/** 이력여부 */ 
	private boolean isHst=false;
	
	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}
	
 	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
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

	public void setTabVa(String tabVa) { 
		this.tabVa = tabVa;
	}
	/** 탭값 */ 
	public String getTabVa() { 
		return tabVa;
	}
	
	public void setMobRegTypCd(String mobRegTypCd) { 
		this.mobRegTypCd = mobRegTypCd;
	}
	/** 모바일등록구분코드 */ 
	public String getMobRegTypCd() { 
		return mobRegTypCd;
	}
	
	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}
	
	/** 모바일여부 */ 
	public boolean isMob() {
		return isMob;
	}
	public void setMob(boolean isMob) {
		this.isMob = isMob;
	}
	
	/** 이력여부 */ 
	public boolean isHst() {
		return isHst;
	}
	public void setHst(boolean isHst) {
		this.isHst = isHst;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormRegDDao.selectWfFormRegD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormRegDDao.insertWfFormRegD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormRegDDao.updateWfFormRegD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormRegDDao.deleteWfFormRegD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormRegDDao.countWfFormRegD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식등록상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(loutVa!=null) { if(tab!=null) builder.append(tab); builder.append("loutVa(레이아웃값 ):").append(loutVa).append('\n'); }
		if(attrVa!=null) { if(tab!=null) builder.append(tab); builder.append("attrVa(속성값):").append(attrVa).append('\n'); }
		if(tabVa!=null) { if(tab!=null) builder.append(tab); builder.append("tabVa(탭값):").append(tabVa).append('\n'); }
		if(mobRegTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mobRegTypCd(모바일등록구분코드):").append(mobRegTypCd).append('\n'); }		
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
