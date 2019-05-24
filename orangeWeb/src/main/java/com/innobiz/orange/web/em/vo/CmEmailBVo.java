package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이메일기본(CM_EMAIL_B) 테이블 VO
 */
public class CmEmailBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8199953794998120486L;

	/** 이메일ID */ 
	private Integer emailId;

 	/** 제목 */ 
	private String subj;

 	/** 발송명 */ 
	private String sendNm;

 	/** 수신명 */ 
	private String recvNm;

 	/** 내용 */ 
	private String cont;

 	/** 파일여부 */ 
	private String fileYn;
	
	/** 등록일 */ 
	private String regDt;

 	public void setEmailId(Integer emailId) { 
		this.emailId = emailId;
	}
	/** 이메일ID */ 
	public Integer getEmailId() { 
		return emailId;
	}

	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setSendNm(String sendNm) { 
		this.sendNm = sendNm;
	}
	/** 발송명 */ 
	public String getSendNm() { 
		return sendNm;
	}

	public void setRecvNm(String recvNm) { 
		this.recvNm = recvNm;
	}
	/** 수신명 */ 
	public String getRecvNm() { 
		return recvNm;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}

	public void setFileYn(String fileYn) { 
		this.fileYn = fileYn;
	}
	/** 파일여부 */ 
	public String getFileYn() { 
		return fileYn;
	}
	
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailBDao.selectCmEmailB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailBDao.insertCmEmailB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailBDao.deleteCmEmailB";
		}
		return null;
	}
	

}
