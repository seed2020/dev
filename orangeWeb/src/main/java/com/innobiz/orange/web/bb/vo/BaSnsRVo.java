package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/06/23 14:41 ******/
/**
* SNS관계(BA_SNS_R) 테이블 VO 
*/
public class BaSnsRVo extends CommonVoImpl {
	
	private static final long serialVersionUID = 3840899987579376712L;

	/** 게시물ID */ 
	private Integer bullId;

 	/** 게시판ID */ 
	private String brdId;

 	/** 속성ID */ 
	private String atrbId;

 	/** 사용여부 */ 
	private String useYn;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;

 	public void setBullId(Integer bullId) { 
		this.bullId = bullId;
	}
	/** 게시물ID */ 
	public Integer getBullId() { 
		return bullId;
	}

	public void setBrdId(String brdId) { 
		this.brdId = brdId;
	}
	/** 게시판ID */ 
	public String getBrdId() { 
		return brdId;
	}

	public void setAtrbId(String atrbId) { 
		this.atrbId = atrbId;
	}
	/** SNS구분 */ 
	public String getAtrbId() { 
		return atrbId;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
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
			return "com.innobiz.orange.web.bb.dao.BaSnsRDao.selectBaSnsR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.bb.dao.BaSnsRDao.insertBaSnsR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.bb.dao.BaSnsRDao.updateBaSnsR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.bb.dao.BaSnsRDao.deleteBaSnsR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.bb.dao.BaSnsRDao.countBaSnsR";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":SNS관계]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(bullId!=null) { if(tab!=null) builder.append(tab); builder.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if(brdId!=null) { if(tab!=null) builder.append(tab); builder.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if(atrbId!=null) { if(tab!=null) builder.append(tab); builder.append("atrbId(속성ID):").append(atrbId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		super.toString(builder, tab);
	}

}
