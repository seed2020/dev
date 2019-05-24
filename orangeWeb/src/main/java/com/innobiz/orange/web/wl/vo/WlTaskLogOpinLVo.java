package com.innobiz.orange.web.wl.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/07 11:10 ******/
/**
* 업무일지의견목록(WL_TASK_LOG_OPIN_L) 테이블 VO 
*/
public class WlTaskLogOpinLVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = 2695312867795612206L;

	/** 일지번호 */ 
	private Integer logNo;

 	/** 사용자UID */ 
	private String userUid;

 	/** 회사ID */ 
	private String compId;

 	/** 의견내용 */ 
	private String opinCont;

 	/** 등록일시 */ 
	private String regDt;

 	public void setLogNo(Integer logNo) { 
		this.logNo = logNo;
	}
	/** 일지번호 */ 
	public Integer getLogNo() { 
		return logNo;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setOpinCont(String opinCont) { 
		this.opinCont = opinCont;
	}
	/** 의견내용 */ 
	public String getOpinCont() { 
		return opinCont;
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
			return "com.innobiz.orange.web.wl.dao.WlTaskLogOpinLDao.selectWlTaskLogOpinL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogOpinLDao.insertWlTaskLogOpinL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogOpinLDao.updateWlTaskLogOpinL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogOpinLDao.deleteWlTaskLogOpinL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogOpinLDao.countWlTaskLogOpinL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무일지의견목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(logNo!=null) { if(tab!=null) builder.append(tab); builder.append("logNo(일지번호):").append(logNo).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(opinCont!=null) { if(tab!=null) builder.append(tab); builder.append("opinCont(의견내용):").append(opinCont).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
