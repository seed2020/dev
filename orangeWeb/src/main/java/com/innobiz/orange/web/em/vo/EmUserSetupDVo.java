package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/07 15:18 ******/
/**
* 사용자설정상세(EM_USER_SETUP_D) 테이블 VO 
*/
public class EmUserSetupDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 501084682675219275L;

	/** 사용자UID */ 
	private String userUid;

 	/** 설정분류ID */ 
	private String setupClsId;

 	/** 설정ID */ 
	private String setupId;

 	/** 설정값 */ 
	private String setupVa;

 	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setSetupClsId(String setupClsId) { 
		this.setupClsId = setupClsId;
	}
	/** 설정분류ID */ 
	public String getSetupClsId() { 
		return setupClsId;
	}

	public void setSetupId(String setupId) { 
		this.setupId = setupId;
	}
	/** 설정ID */ 
	public String getSetupId() { 
		return setupId;
	}

	public void setSetupVa(String setupVa) { 
		this.setupVa = setupVa;
	}
	/** 설정값 */ 
	public String getSetupVa() { 
		return setupVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmUserSetupDDao.selectEmUserSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmUserSetupDDao.insertEmUserSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmUserSetupDDao.updateEmUserSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmUserSetupDDao.deleteEmUserSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmUserSetupDDao.countEmUserSetupD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자설정상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(setupClsId!=null) { if(tab!=null) builder.append(tab); builder.append("setupClsId(설정분류ID):").append(setupClsId).append('\n'); }
		if(setupId!=null) { if(tab!=null) builder.append(tab); builder.append("setupId(설정ID):").append(setupId).append('\n'); }
		if(setupVa!=null) { if(tab!=null) builder.append(tab); builder.append("setupVa(설정값):").append(setupVa).append('\n'); }
		super.toString(builder, tab);
	}

}
