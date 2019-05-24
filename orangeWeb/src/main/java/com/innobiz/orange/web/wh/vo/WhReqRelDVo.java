package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/07/03 13:19 ******/
/**
* 요청관련상세(WH_REQ_REL_D) 테이블 VO 
*/
public class WhReqRelDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 3668677939018793928L;

	/** 요청번호 */ 
	private String reqNo;

 	/** 관련번호 */ 
	private String relNo;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}

	public void setRelNo(String relNo) { 
		this.relNo = relNo;
	}
	/** 관련번호 */ 
	public String getRelNo() { 
		return relNo;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqRelDDao.selectWhReqRelD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqRelDDao.insertWhReqRelD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqRelDDao.updateWhReqRelD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqRelDDao.deleteWhReqRelD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqRelDDao.countWhReqRelD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청관련상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(relNo!=null) { if(tab!=null) builder.append(tab); builder.append("relNo(관련번호):").append(relNo).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
