package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/10/15 15:36 ******/
/**
* SSO임시(EM_SSO_T) 테이블 VO 
*/
public class EmSsoTVo extends CommonVoImpl { 
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1241283185816403295L;

	/** SSO ID */ 
	private String ssoId;

 	/** 사용자UID */ 
	private String userUid;

 	/** 등록일시 */ 
	private String regDt;

 	public void setSsoId(String ssoId) { 
		this.ssoId = ssoId;
	}
	/** SSO ID */ 
	public String getSsoId() { 
		return ssoId;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSsoTDao.selectEmSsoT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSsoTDao.insertEmSsoT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSsoTDao.updateEmSsoT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSsoTDao.deleteEmSsoT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSsoTDao.countEmSsoT";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":SSO임시]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(ssoId!=null) { if(tab!=null) builder.append(tab); builder.append("ssoId(SSO ID):").append(ssoId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
