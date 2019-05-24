package com.innobiz.orange.web.cu.vo;

import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/10/11 16:17 ******/
/**
* 첨부파일상세(CU_TASK_STAT_FILE_D) 테이블 VO 
*/
public class CuTaskStatFileDVo extends CommonVoImpl implements CommonFileVo {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6397206563096688058L;

	/** 파일ID */ 
	private Integer fileId;

 	/** 참조ID */ 
	private String refId;

 	/** 표시이름 */ 
	private String dispNm;

 	/** 표시순서 */ 
	private Integer dispOrdr;

 	/** 파일확장자 */ 
	private String fileExt;

 	/** 파일크기 */ 
	private Long fileSize;

 	/** 저장경로 */ 
	private String savePath;

 	/** 사용여부 */ 
	private String useYn;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 참조ID - new */ 
	private String newRefId;
	
	/** 구분코드[ 업무현황 : null , 주간보고 : REPRT] */ 
	private String typCd;

 	public void setFileId(Integer fileId) { 
		this.fileId = fileId;
	}
	/** 파일ID */ 
	public Integer getFileId() { 
		return fileId;
	}

	public void setRefId(String refId) { 
		this.refId = refId;
	}
	/** 참조ID */ 
	public String getRefId() { 
		return refId;
	}

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

	public void setSavePath(String savePath) { 
		this.savePath = savePath;
	}
	/** 저장경로 */ 
	public String getSavePath() { 
		return savePath;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자 */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}
	
	/** 참조ID - new */ 
	public String getNewRefId() {
		return newRefId;
	}
	public void setNewRefId(String newRefId) {
		this.newRefId = newRefId;
	}
	
	/** 구분코드[ 업무현황 : null , 주간보고 : REPRT] */ 
	public String getTypCd() {
		return typCd;
	}
	public void setTypCd(String typCd) {
		this.typCd = typCd;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.selectCuTaskStatFileD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.insertCuTaskStatFileD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.updateCuTaskStatFileD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.deleteCuTaskStatFileD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.countCuTaskStatFileD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":첨부파일상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(fileId!=null) { if(tab!=null) builder.append(tab); builder.append("fileId(파일ID):").append(fileId).append('\n'); }
		if(refId!=null) { if(tab!=null) builder.append(tab); builder.append("refId(참조ID):").append(refId).append('\n'); }
		if(dispNm!=null) { if(tab!=null) builder.append(tab); builder.append("dispNm(표시이름):").append(dispNm).append('\n'); }
		if(dispOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("dispOrdr(표시순서):").append(dispOrdr).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileSize!=null) { if(tab!=null) builder.append(tab); builder.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if(savePath!=null) { if(tab!=null) builder.append(tab); builder.append("savePath(저장경로):").append(savePath).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
