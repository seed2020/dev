package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이메일첨부파일상세(CM_EMAIL_FILE_D) 테이블 VO
 */
public class CmEmailFileDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 56959345291273138L;

	/** 이메일ID */ 
	private Integer emailId;

 	/** 표시이름 */ 
	private String dispNm;

 	/** 표시순서 */ 
	private Integer dispOrdr;

 	/** 파일확장자 */ 
	private String fileExt;

 	/** 파일크기 */ 
	private Long fileSize;

 	/** 파일내용 */ 
	private byte[] fileCont;

	public void setDispNm(String dispNm) { 
		this.dispNm = dispNm;
	}
	/** 표시이름 */ 
	public String getDispNm() { 
		return dispNm;
	}

	public void setDispOrdr(Integer dispOrdr) { 
		this.dispOrdr = dispOrdr;
	}
	/** 표시순서 */ 
	public Integer getDispOrdr() { 
		return dispOrdr;
	}

	public void setFileExt(String fileExt) { 
		this.fileExt = fileExt;
	}
	/** 파일확장자 */ 
	public String getFileExt() { 
		return fileExt;
	}

	public void setFileSize(Long fileSize) { 
		this.fileSize = fileSize;
	}
	/** 파일크기 */ 
	public Long getFileSize() { 
		return fileSize;
	}

	/** 이메일ID */ 
	public Integer getEmailId() {
		return emailId;
	}
	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}
	public byte[] getFileCont() {
		return fileCont;
	}
	public void setFileCont(byte[] fileCont) {
		this.fileCont = fileCont;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailFileDDao.selectCmEmailFileD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailFileDDao.insertCmEmailFileD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.CmEmailFileDDao.deleteCmEmailFileD";
		} 
		return null;
	}

}
