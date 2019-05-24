package com.innobiz.orange.web.wh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/20 11:41 ******/
/**
* 모듈담당자목록(WH_MD_PICH_L) 테이블 VO 
*/
public class WhMdPichLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4085852010301747741L;

	/** 모듈ID */ 
	private String mdId;

 	/** 담당자구분코드 */ 
	private String pichTypCd;

 	/** ID값 */ 
	private String idVa;

 	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 추가 */
	/** 담당자명 */ 
	private String pichNm;
	
	/** 추가 */
	/** 모듈ID 목록 */ 
	private List<String> mdIdList;
	
 	public void setMdId(String mdId) { 
		this.mdId = mdId;
	}
	/** 모듈ID */ 
	public String getMdId() { 
		return mdId;
	}

	public void setPichTypCd(String pichTypCd) { 
		this.pichTypCd = pichTypCd;
	}
	/** 담당자구분코드 */ 
	public String getPichTypCd() { 
		return pichTypCd;
	}

	public void setIdVa(String idVa) { 
		this.idVa = idVa;
	}
	/** ID값 */ 
	public String getIdVa() { 
		return idVa;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 담당자명 */ 
	public String getPichNm() {
		return pichNm;
	}
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}
	
	/** 모듈ID 목록 */ 
	public List<String> getMdIdList() {
		return mdIdList;
	}
	public void setMdIdList(List<String> mdIdList) {
		this.mdIdList = mdIdList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdPichLDao.selectWhMdPichL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdPichLDao.insertWhMdPichL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdPichLDao.updateWhMdPichL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdPichLDao.deleteWhMdPichL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdPichLDao.countWhMdPichL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":모듈담당자목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(pichTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pichTypCd(담당자구분코드):").append(pichTypCd).append('\n'); }
		if(idVa!=null) { if(tab!=null) builder.append(tab); builder.append("idVa(ID값):").append(idVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
