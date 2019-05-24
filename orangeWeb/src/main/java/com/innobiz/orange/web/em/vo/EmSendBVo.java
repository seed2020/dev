package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/05/25 14:16 ******/
/**
* 보내기(EM_SEND_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class EmSendBVo extends CommonVoImpl {
	/** 보내기번호 */ 
	private String sendNo;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;
	
	/** 메일여부 */
	private boolean isMail=false;
	
	/** 이메일번호  */ 
	private String emailNo;

 	/** 파일여부 */ 
	private String fileYn;
	
 	public void setSendNo(String sendNo) { 
		this.sendNo = sendNo;
	}
	/** 보내기번호 */ 
	public String getSendNo() { 
		return sendNo;
	}

	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}
	
	/** 메일여부 */
	public boolean isMail() {
		return isMail;
	}
	public void setMail(boolean isMail) {
		this.isMail = isMail;
	}
	
	public void setEmailNo(String emailNo) { 
		this.emailNo = emailNo;
	}
	/** 이메일번호  */ 
	public String getEmailNo() { 
		return emailNo;
	}

	public void setFileYn(String fileYn) { 
		this.fileYn = fileYn;
	}
	/** 파일여부 */ 
	public String getFileYn() { 
		return fileYn;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendBDao.selectEmSendB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendBDao.insertEmSendB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendBDao.updateEmSendB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendBDao.deleteEmSendB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendBDao.countEmSendB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":보내기]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(sendNo!=null) { if(tab!=null) builder.append(tab); builder.append("sendNo(보내기번호):").append(sendNo).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(emailNo!=null) { if(tab!=null) builder.append(tab); builder.append("emailNo(이메일번호 ):").append(emailNo).append('\n'); }
		if(fileYn!=null) { if(tab!=null) builder.append(tab); builder.append("fileYn(파일여부):").append(fileYn).append('\n'); }
		super.toString(builder, tab);
	}

}
