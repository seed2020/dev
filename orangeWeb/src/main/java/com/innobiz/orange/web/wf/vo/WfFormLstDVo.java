package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/07 15:14 ******/
/**
* 양식목록상세(WF_FORM_LST_D) 테이블 VO 
*/
public class WfFormLstDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4798671007877152392L;
	
	/** 생성ID */ 
	private String genId;
	
	/** 컬럼ID */ 
	private String colmId;
	
	/** 양식번호 */ 
	private String formNo;
	
	/** 모듈코드 */ 
	private String mdCd;

 	/** 넓이퍼센트 */ 
	private String wdthPerc;

 	/** 줄맞춤값 */ 
	private String alnVa;

 	/** 정렬옵션값 */ 
	private String sortOptVa;

 	/** 데이터정렬값 */ 
	private String dataSortVa;

 	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 조회여부 */ 
	private String srchYn;
	
	/** 추가 */
	/** 컬럼명 */ 
	private String colmNm;

 	/** 항목명 */ 
	private String itemNm;
	
	/** 컬럼구분코드 */ 
	private String colmTypCd;
	
	/** 컬럼구분 */ 
	private String colmTyp;
	
	/** 코드컬럼 여부 */ 
	private boolean isCdColm=false;
	
	/** 이력여부 */ 
	private boolean isHst=false;

	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}
	
	public void setColmId(String colmId) { 
		this.colmId = colmId;
	}
	/** 컬럼ID */ 
	public String getColmId() { 
		return colmId;
	}
	
 	public void setFormNo(String formNo) { 
		this.formNo = formNo;
	}
	/** 양식번호 */ 
	public String getFormNo() { 
		return formNo;
	}
	
	public void setMdCd(String mdCd) { 
		this.mdCd = mdCd;
	}
	/** 모듈코드 */ 
	public String getMdCd() { 
		return mdCd;
	}

	public void setWdthPerc(String wdthPerc) { 
		this.wdthPerc = wdthPerc;
	}
	/** 넓이퍼센트 */ 
	public String getWdthPerc() { 
		return wdthPerc;
	}

	public void setAlnVa(String alnVa) { 
		this.alnVa = alnVa;
	}
	/** 줄맞춤값 */ 
	public String getAlnVa() { 
		return alnVa;
	}

	public void setSortOptVa(String sortOptVa) { 
		this.sortOptVa = sortOptVa;
	}
	/** 정렬옵션값 */ 
	public String getSortOptVa() { 
		return sortOptVa;
	}

	public void setDataSortVa(String dataSortVa) { 
		this.dataSortVa = dataSortVa;
	}
	/** 데이터정렬값 */ 
	public String getDataSortVa() { 
		return dataSortVa;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	public void setSrchYn(String srchYn) { 
		this.srchYn = srchYn;
	}
	/** 조회여부 */ 
	public String getSrchYn() { 
		return srchYn;
	}
	
	public void setColmNm(String colmNm) { 
		this.colmNm = colmNm;
	}
	/** 컬럼명 */ 
	public String getColmNm() { 
		return colmNm;
	}

	public void setItemNm(String itemNm) { 
		this.itemNm = itemNm;
	}
	/** 항목명 */ 
	public String getItemNm() { 
		return itemNm;
	}
	
	/** 컬럼구분코드 */ 
	public String getColmTypCd() {
		return colmTypCd;
	}
	public void setColmTypCd(String colmTypCd) {
		this.colmTypCd = colmTypCd;
	}
	
	/** 컬럼구분 */ 
	public String getColmTyp() {
		return colmTyp;
	}
	public void setColmTyp(String colmTyp) {
		this.colmTyp = colmTyp;
	}
	
	/** 코드컬럼 여부 */ 
	public boolean isCdColm() {
		return isCdColm;
	}
	public void setCdColm(boolean isCdColm) {
		this.isCdColm = isCdColm;
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
			return "com.innobiz.orange.web.wf.dao.WfFormLstDDao.selectWfFormLstD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormLstDDao.insertWfFormLstD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormLstDDao.updateWfFormLstD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormLstDDao.deleteWfFormLstD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormLstDDao.countWfFormLstD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식목록상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) {
		if(colmId!=null) { if(tab!=null) builder.append(tab); builder.append("colmId(컬럼ID):").append(colmId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }
		if(mdCd!=null) { if(tab!=null) builder.append(tab); builder.append("mdCd(모듈코드):").append(mdCd).append('\n'); }		
		if(wdthPerc!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPerc(넓이퍼센트):").append(wdthPerc).append('\n'); }
		if(alnVa!=null) { if(tab!=null) builder.append(tab); builder.append("alnVa(줄맞춤값):").append(alnVa).append('\n'); }
		if(sortOptVa!=null) { if(tab!=null) builder.append(tab); builder.append("sortOptVa(정렬옵션값):").append(sortOptVa).append('\n'); }
		if(dataSortVa!=null) { if(tab!=null) builder.append(tab); builder.append("dataSortVa(데이터정렬값):").append(dataSortVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(srchYn!=null) { if(tab!=null) builder.append(tab); builder.append("srchYn(조회여부):").append(srchYn).append('\n'); }
		super.toString(builder, tab);
	}

}
