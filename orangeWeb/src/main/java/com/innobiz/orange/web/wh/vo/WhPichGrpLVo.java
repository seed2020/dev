package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/07/10 11:26 ******/
/**
* 담당자그룹목록(WH_PICH_GRP_L) 테이블 VO 
*/
public class WhPichGrpLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7918173517752865127L;

	/** 담당자그룹ID */ 
	private String pichGrpId;

 	/** 사용자UID */ 
	private String userUid;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

	/** 추가 */
	/** 사용자명 */ 
	private String userNm;
	
	
 	public void setPichGrpId(String pichGrpId) { 
		this.pichGrpId = pichGrpId;
	}
	/** 담당자그룹ID */ 
	public String getPichGrpId() { 
		return pichGrpId;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
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
			return "com.innobiz.orange.web.wh.dao.WhPichGrpLDao.selectWhPichGrpL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhPichGrpLDao.insertWhPichGrpL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhPichGrpLDao.updateWhPichGrpL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhPichGrpLDao.deleteWhPichGrpL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhPichGrpLDao.countWhPichGrpL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":담당자그룹목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(pichGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("pichGrpId(담당자그룹ID):").append(pichGrpId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
