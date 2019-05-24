package com.innobiz.orange.web.wf.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/30 14:29 ******/
/**
* 생성테이블코드목록(WF_0000_WORKS_CODE_L) 테이블 VO 
*/
public class WfWorksCodeLVo extends WfCmWorksVo {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -5219922419111647093L;

 	/** 구분코드 */ 
	private String typCd;

 	/** 컬럼명 */ 
	private String colmNm;

 	/** 코드값 */ 
	private String cdVa;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 추가 */
	/** 코드명 */ 
	private String cdNm;
	
	/** 코드값 목록 */ 
	private List<String> cdVaList;
	
	/** 생성자 */
 	public WfWorksCodeLVo() {
		super();
	}
	
	/** 생성자 */
 	public WfWorksCodeLVo(String formNo) {
		super(formNo);
	}

	public void setTypCd(String typCd) { 
		this.typCd = typCd;
	}
	/** 구분코드 */ 
	public String getTypCd() { 
		return typCd;
	}

	public void setColmNm(String colmNm) { 
		this.colmNm = colmNm;
	}
	/** 컬럼명 */ 
	public String getColmNm() { 
		return colmNm;
	}

	public void setCdVa(String cdVa) { 
		this.cdVa = cdVa;
	}
	/** 코드값 */ 
	public String getCdVa() { 
		return cdVa;
	}
	
	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 코드명 */ 
	public String getCdNm() {
		return cdNm;
	}
	public void setCdNm(String cdNm) {
		this.cdNm = cdNm;
	}
	
	/** 코드값 목록 */ 
	public List<String> getCdVaList() {
		return cdVaList;
	}

	public void setCdVaList(List<String> cdVaList) {
		this.cdVaList = cdVaList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksCodeLDao.selectWfWorksCodeL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksCodeLDao.insertWfWorksCodeL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksCodeLDao.updateWfWorksCodeL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksCodeLDao.deleteWfWorksCodeL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksCodeLDao.countWfWorksCodeL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":생성테이블코드목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(typCd!=null) { if(tab!=null) builder.append(tab); builder.append("typCd(구분코드):").append(typCd).append('\n'); }
		if(colmNm!=null) { if(tab!=null) builder.append(tab); builder.append("colmNm(컬럼명):").append(colmNm).append('\n'); }
		if(cdVa!=null) { if(tab!=null) builder.append(tab); builder.append("cdVa(코드값):").append(cdVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }		
		super.toString(builder, tab);
	}

}
