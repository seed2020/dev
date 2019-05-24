package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/02/23 14:55 ******/
/**
* 사용자그룹목록(WC_USER_GRP_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WcUserGrpLVo extends CommonVoImpl {
	/** 사용자그룹ID */ 
	private String userGrpId;

 	/** 사용자UID */ 
	private String userUid;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 사용자명 */ 
	private String userNm;
	
	
	/** 사용자 정보 추가 */
	/** 부서리소스명 */
	private String deptRescNm;
	
	/** 직위명 */
	private String positNm;
	
 	public void setUserGrpId(String userGrpId) { 
		this.userGrpId = userGrpId;
	}
	/** 사용자그룹ID */ 
	public String getUserGrpId() { 
		return userGrpId;
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
	
	/** 부서리소스명 */
	public String getDeptRescNm() {
		return deptRescNm;
	}

	/** 부서리소스명 */
	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}
	
	/** 직위명 */
	public String getPositNm() {
		return positNm;
	}

	/** 직위명 */
	public void setPositNm(String positNm) {
		this.positNm = positNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpLDao.selectWcUserGrpL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpLDao.insertWcUserGrpL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpLDao.updateWcUserGrpL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpLDao.deleteWcUserGrpL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcUserGrpLDao.countWcUserGrpL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자그룹목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("userGrpId(사용자그룹ID):").append(userGrpId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		super.toString(builder, tab);
	}

}
