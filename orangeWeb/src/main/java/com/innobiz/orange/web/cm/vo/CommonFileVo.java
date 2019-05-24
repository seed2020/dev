package com.innobiz.orange.web.cm.vo;

import java.io.Serializable;

/** 공통 파일VO 인터페이스 */
public interface CommonFileVo extends Serializable {

	/** 파일ID */
	public Integer getFileId();

	/** 파일ID */
	public void setFileId(Integer fileId);

	/** 표시이름 */
	public String getDispNm();

	/** 표시이름 */
	public void setDispNm(String dispNm);

	/** 파일확장자 */
	public String getFileExt();

	/** 파일확장자 */
	public void setFileExt(String fileExt);

	/** 저장경로 */
	public String getSavePath();

	/** 저장경로 */
	public void setSavePath(String savePath);

	/** 사용여부 */
	public String getUseYn();

	/** 사용여부 */
	public void setUseYn(String useYn);

	/** 참조ID */
	public String getRefId();

	/** 참조ID */
	public void setRefId(String refId);
	
	/** 표시순서 */
	public Integer getDispOrdr();
	
	/** 표시순서 */
	public void setDispOrdr(Integer dispOrdr);
	
	/** 파일크기 */
	public Long getFileSize();
	
	/** 파일크기 */
	public void setFileSize(Long fileSize);
	
	/** 등록일시 */
	public String getRegDt();
}
