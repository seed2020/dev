package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/07 15:14 ******/
/**
* 양식컬럼목록(WF_FORM_COLM_L) 테이블 VO 
*/
public class WfFormColmLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7334528556507927151L;
	
	/** 생성ID */ 
	private String genId;
	
	/** 컬럼ID */ 
	private String colmId;
	
	/** 양식번호 */ 
	private String formNo;

	/** 컬럼구분코드 */ 
	private String colmTypCd;

 	/** 컬럼명 */ 
	private String colmNm;

 	/** 항목명 */ 
	private String itemNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 사용여부 */ 
	private String useYn;
	
	/** 목록여부 */ 
	private String lstYn;
	
	/** 배포여부 */ 
	private String deployYn;
	
 	/** JSON_PATH */ 
	private String jsonPath;
	
	/** 추가 */
	/** 데이터 타입[varchar,text ...] */
	private String dataTyp;
	
	/** 데이터 길이[byte] */
	private Integer dataLen;
	
	/** 컬럼DB명 */ 
	private String colmDbNm;
	
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
	
	public void setColmTypCd(String colmTypCd) { 
		this.colmTypCd = colmTypCd;
	}
	/** 컬럼구분코드 */ 
	public String getColmTypCd() { 
		return colmTypCd;
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

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}
	
	public void setLstYn(String lstYn) { 
		this.lstYn = lstYn;
	}
	/** 목록여부 */ 
	public String getLstYn() { 
		return lstYn;
	}
	
	public void setDeployYn(String deployYn) { 
		this.deployYn = deployYn;
	}
	/** 배포여부 */ 
	public String getDeployYn() { 
		return deployYn;
	}
	
	public void setJsonPath(String jsonPath) { 
		this.jsonPath = jsonPath;
	}
	/** JSON_PATH */ 
	public String getJsonPath() { 
		return jsonPath;
	}
	
	/** 데이터 타입[varchar,text ...] */
	public String getDataTyp() {
		return dataTyp;
	}
	public void setDataTyp(String dataTyp) {
		this.dataTyp = dataTyp;
	}
	
	/** 데이터 길이[byte] */
	public Integer getDataLen() {
		return dataLen;
	}
	public void setDataLen(Integer dataLen) {
		this.dataLen = dataLen;
	}
	
	/** 컬럼DB명 */ 
	public String getColmDbNm() {
		return colmDbNm;
	}
	public void setColmDbNm(String colmDbNm) {
		this.colmDbNm = colmDbNm;
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
			return "com.innobiz.orange.web.wf.dao.WfFormColmLDao.selectWfFormColmL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormColmLDao.insertWfFormColmL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormColmLDao.updateWfFormColmL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormColmLDao.deleteWfFormColmL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormColmLDao.countWfFormColmL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":양식컬럼목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(colmId!=null) { if(tab!=null) builder.append(tab); builder.append("colmId(컬럼ID):").append(colmId).append('\n'); }
		if(formNo!=null) { if(tab!=null) builder.append(tab); builder.append("formNo(양식번호):").append(formNo).append('\n'); }		
		if(colmTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("colmTypCd(컬럼구분코드):").append(colmTypCd).append('\n'); }		
		if(colmNm!=null) { if(tab!=null) builder.append(tab); builder.append("colmNm(컬럼명):").append(colmNm).append('\n'); }
		if(itemNm!=null) { if(tab!=null) builder.append(tab); builder.append("itemNm(항목명):").append(itemNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(lstYn!=null) { if(tab!=null) builder.append(tab); builder.append("lstYn(목록여부):").append(lstYn).append('\n'); }
		if(deployYn!=null) { if(tab!=null) builder.append(tab); builder.append("deployYn(배포여부):").append(deployYn).append('\n'); }
		if(jsonPath!=null) { if(tab!=null) builder.append(tab); builder.append("jsonPath(JSON_PATH):").append(jsonPath).append('\n'); }
		super.toString(builder, tab);
	}

}
