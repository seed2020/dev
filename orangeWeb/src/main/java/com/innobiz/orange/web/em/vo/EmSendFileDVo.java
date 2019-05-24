package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/05/25 14:16 ******/
/**
* 보내기파일(EM_SEND_FILE_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class EmSendFileDVo extends EmTmpFileTVo {
	/** 보내기번호 */ 
	private String sendNo;

 	/** 파일일련번호 */ 
	private Integer fileSeq;
	
	/** 메일여부 */
	private boolean isMail=false;
	
	/** 이메일번호  */ 
	private String emailNo;

 	/** 파일내용 */ 
	private byte[] fileCont;
	
 	public void setSendNo(String sendNo) { 
		this.sendNo = sendNo;
	}
	/** 보내기번호 */ 
	public String getSendNo() { 
		return sendNo;
	}

	public void setFileSeq(Integer fileSeq) { 
		this.fileSeq = fileSeq;
	}
	/** 파일일련번호 */ 
	public Integer getFileSeq() { 
		return fileSeq;
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

	public void setFileCont(byte[] fileCont) { 
		this.fileCont = fileCont;
	}
	/** 파일내용 */ 
	public byte[] getFileCont() { 
		return fileCont;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendFileDDao.selectEmSendFileD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendFileDDao.insertEmSendFileD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendFileDDao.updateEmSendFileD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendFileDDao.deleteEmSendFileD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmSendFileDDao.countEmSendFileD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":보내기파일]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(sendNo!=null) { if(tab!=null) builder.append(tab); builder.append("sendNo(보내기번호):").append(sendNo).append('\n'); }
		if(fileSeq!=null) { if(tab!=null) builder.append(tab); builder.append("fileSeq(파일일련번호):").append(fileSeq).append('\n'); }
		if(emailNo!=null) { if(tab!=null) builder.append(tab); builder.append("emailNo(이메일번호 ):").append(emailNo).append('\n'); }
		if(fileCont!=null) { if(tab!=null) builder.append(tab); builder.append("fileCont(파일내용):").append(fileCont).append('\n'); }
		super.toString(builder, tab);
	}

}
