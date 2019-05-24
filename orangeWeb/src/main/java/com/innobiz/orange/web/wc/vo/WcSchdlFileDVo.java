package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 일정 파일 상세(WC_SCHDL_FILE_D)테이블 VO
 */
public class WcSchdlFileDVo extends CommonVoImpl implements CommonFileVo {

	/** serialVersionUID. */
	private static final long serialVersionUID = 468549371314805606L;

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
	
	/** 파일ID */
	public Integer getFileId() {
		return fileId;
	}

	/** 파일ID */
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	/** 참조ID */
	public String getRefId() {
		return refId;
	}

	/** 참조ID */
	public void setRefId(String refId) {
		this.refId = refId;
	}

	/** 표시이름 */
	public String getDispNm() {
		return dispNm;
	}

	/** 표시이름 */
	public void setDispNm(String dispNm) {
		this.dispNm = dispNm;
	}

	/** 표시순서 */
	public Integer getDispOrdr() {
		return dispOrdr;
	}

	/** 표시순서 */
	public void setDispOrdr(Integer dispOrdr) {
		this.dispOrdr = dispOrdr;
	}

	/** 파일확장자 */
	public String getFileExt() {
		return fileExt;
	}

	/** 파일확장자 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 파일크기 */
	public Long getFileSize() {
		return fileSize;
	}

	/** 파일크기 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/** 저장경로 */
	public String getSavePath() {
		return savePath;
	}

	/** 저장경로 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	
	// /** 등록자 */
	// public String getRegrUid() {
	// return regrUid;
	// }
	//
	// /** 등록자 */
	// public void setRegrUid(String regrUid) {
	// this.regrUid = regrUid;
	// }

	// /** 등록일시 */
	// public String getRegDt() {
	// return regDt;
	// }
	//
	// /** 등록일시 */
	// public void setRegDt(String regDt) {
	// this.regDt = regDt;
	// }

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null)
			return getInstanceQueryId();
		String classNameDomain = getClass().getName()
				.substring(0, getClass().getName().length() - 2)
				.replaceAll("\\.vo\\.", ".dao.");
		if (QueryType.SELECT == queryType) {
			return classNameDomain
					+ "Dao.select"
					+ classNameDomain.split("\\.")[classNameDomain.split("\\.").length - 1];
		} else if (QueryType.INSERT == queryType) {
			return classNameDomain
					+ "Dao.insert"
					+ classNameDomain.split("\\.")[classNameDomain.split("\\.").length - 1];
		} else if (QueryType.UPDATE == queryType) {
			return classNameDomain
					+ "Dao.update"
					+ classNameDomain.split("\\.")[classNameDomain.split("\\.").length - 1];
		} else if (QueryType.DELETE == queryType) {
			return classNameDomain
					+ "Dao.delete"
					+ classNameDomain.split("\\.")[classNameDomain.split("\\.").length - 1];
		} else if (QueryType.COUNT == queryType) {
			return classNameDomain
					+ "Dao.count"
					+ classNameDomain.split("\\.")[classNameDomain.split("\\.").length - 1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 파일 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(fileId!=null) { if(tab!=null) builder.append(tab); builder.append("fileId(파일ID):").append(fileId).append('\n'); }
		if(refId!=null) { if(tab!=null) builder.append(tab); builder.append("refId(참조ID):").append(refId).append('\n'); }
		if(dispNm!=null) { if(tab!=null) builder.append(tab); builder.append("dispNm(표시이름):").append(dispNm).append('\n'); }
		if(dispOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("dispOrdr(표시순서):").append(dispOrdr).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileSize!=null) { if(tab!=null) builder.append(tab); builder.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if(savePath!=null) { if(tab!=null) builder.append(tab); builder.append("savePath(저장경로):").append(savePath).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		super.toString(builder, tab);
	}

}