package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/04/29 13:36 ******/
/**
* 팝업사용자(BA_POP_USER_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class BaPopUserBVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;

 	/** 사용자UID */ 
	private String userUid;

 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ba.dao.BaPopUserBDao.selectBaPopUserB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ba.dao.BaPopUserBDao.insertBaPopUserB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ba.dao.BaPopUserBDao.updateBaPopUserB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ba.dao.BaPopUserBDao.deleteBaPopUserB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ba.dao.BaPopUserBDao.countBaPopUserB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":팝업사용자]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		super.toString(builder, tab);
	}

}
