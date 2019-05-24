package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/02/23 16:27 ******/
/**
* 보고자목록(WL_REPRT_USER_L) 테이블 VO 
*/
public class WlReprtUserLVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = -6342078303472265772L;

	/** 등록자UID */ 
	private String regrUid;

 	/** 사용자UID */ 
	private String userUid;

 	/** 구분코드 */ 
	private String typCd;

	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 사용자명 */ 
	private String userNm;

 	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setTypCd(String typCd) { 
		this.typCd = typCd;
	}
	/** 구분코드 */ 
	public String getTypCd() { 
		return typCd;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 사용자명 */ 
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtUserLDao.selectWlReprtUserL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtUserLDao.insertWlReprtUserL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtUserLDao.updateWlReprtUserL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtUserLDao.deleteWlReprtUserL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtUserLDao.countWlReprtUserL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":보고자목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(typCd!=null) { if(tab!=null) builder.append(tab); builder.append("typCd(구분코드):").append(typCd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
