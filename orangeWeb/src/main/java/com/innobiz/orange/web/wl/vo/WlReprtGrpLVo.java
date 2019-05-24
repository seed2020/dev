package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/22 17:04 ******/
/**
* 보고그룹목록(WL_REPRT_GRP_L) 테이블 VO 
*/
public class WlReprtGrpLVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = 4871145738254202054L;

	/** 그룹번호 */ 
	private String grpNo;

 	/** 사용자UID */ 
	private String userUid;

 	/** 대상구분코드 */ 
	private String tgtTypCd;

 	/** 보고구분코드 */ 
	private String reprtTypCd;

 	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 사용자명 */ 
	private String userNm;
	
 	public void setGrpNo(String grpNo) { 
		this.grpNo = grpNo;
	}
	/** 그룹번호 */ 
	public String getGrpNo() { 
		return grpNo;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setTgtTypCd(String tgtTypCd) { 
		this.tgtTypCd = tgtTypCd;
	}
	/** 대상구분코드 */ 
	public String getTgtTypCd() { 
		return tgtTypCd;
	}

	public void setReprtTypCd(String reprtTypCd) { 
		this.reprtTypCd = reprtTypCd;
	}
	/** 보고구분코드 */ 
	public String getReprtTypCd() { 
		return reprtTypCd;
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
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpLDao.selectWlReprtGrpL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpLDao.insertWlReprtGrpL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpLDao.updateWlReprtGrpL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpLDao.deleteWlReprtGrpL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlReprtGrpLDao.countWlReprtGrpL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":보고그룹목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(grpNo!=null) { if(tab!=null) builder.append(tab); builder.append("grpNo(그룹번호):").append(grpNo).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(tgtTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("tgtTypCd(대상구분코드):").append(tgtTypCd).append('\n'); }
		if(reprtTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("reprtTypCd(보고구분코드):").append(reprtTypCd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
