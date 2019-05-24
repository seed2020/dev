package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/18 15:00 ******/
/**
* 유형그룹목록(WH_CAT_GRP_L) 테이블 VO 
*/
public class WhCatGrpLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 2902495502544641117L;

	/** 유형번호 */ 
	private String catNo;

 	/** 유형그룹ID */ 
	private String catGrpId;

 	/** 유형명 */ 
	private String catNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	public void setCatNo(String catNo) { 
		this.catNo = catNo;
	}
	/** 유형번호 */ 
	public String getCatNo() { 
		return catNo;
	}

	public void setCatGrpId(String catGrpId) { 
		this.catGrpId = catGrpId;
	}
	/** 유형그룹ID */ 
	public String getCatGrpId() { 
		return catGrpId;
	}

	public void setCatNm(String catNm) { 
		this.catNm = catNm;
	}
	/** 유형명 */ 
	public String getCatNm() { 
		return catNm;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpLDao.selectWhCatGrpL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpLDao.insertWhCatGrpL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpLDao.updateWhCatGrpL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpLDao.deleteWhCatGrpL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhCatGrpLDao.countWhCatGrpL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":유형그룹목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(catNo!=null) { if(tab!=null) builder.append(tab); builder.append("catNo(유형번호):").append(catNo).append('\n'); }
		if(catGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("catGrpId(유형그룹ID):").append(catGrpId).append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(유형명):").append(catNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
