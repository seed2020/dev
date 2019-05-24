package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/03/07 16:32 ******/
/**
* 임시파일(EM_FILE_T) 테이블 VO 
*/
@SuppressWarnings("serial")
public class EmTmpFileTVo extends CommonVoImpl{ 
	/** 임시파일ID */ 
	private Integer tmpFileId;

 	/** 표시이름 */ 
	private String dispNm;

 	/** 파일확장자 */ 
	private String fileExt;

 	/** 파일크기 */ 
	private Long fileSize;

 	/** 저장경로 */ 
	private String savePath;

 	public void setTmpFileId(Integer tmpFileId) { 
		this.tmpFileId = tmpFileId;
	}
	/** 임시파일ID */ 
	public Integer getTmpFileId() { 
		return tmpFileId;
	}

	public void setDispNm(String dispNm) { 
		this.dispNm = dispNm;
	}
	/** 표시이름 */ 
	public String getDispNm() { 
		return dispNm;
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

	public void setSavePath(String savePath) { 
		this.savePath = savePath;
	}
	/** 저장경로 */ 
	public String getSavePath() { 
		return savePath;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmTmpFileTDao.selectEmTmpFileT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmTmpFileTDao.insertEmTmpFileT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmTmpFileTDao.updateEmTmpFileT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmTmpFileTDao.deleteEmTmpFileT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmTmpFileTDao.countEmTmpFileT";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":임시파일]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(tmpFileId!=null) { if(tab!=null) builder.append(tab); builder.append("tmpFileId(임시파일ID):").append(tmpFileId).append('\n'); }
		if(dispNm!=null) { if(tab!=null) builder.append(tab); builder.append("dispNm(표시이름):").append(dispNm).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileSize!=null) { if(tab!=null) builder.append(tab); builder.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if(savePath!=null) { if(tab!=null) builder.append(tab); builder.append("savePath(저장경로):").append(savePath).append('\n'); }
		super.toString(builder, tab);
	}

}
