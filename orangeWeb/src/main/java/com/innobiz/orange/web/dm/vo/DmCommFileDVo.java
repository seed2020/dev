package com.innobiz.orange.web.dm.vo;


/****** Object:  Vo - Date: 2015/06/17 11:56 ******/
/**
* 첨부파일상세(DM_FILE_D) 테이블 VO 
*/
public class DmCommFileDVo {
 	/** 원본저장경로 */ 
	private String originSavePath;
	
	/** 모듈별 기본 경로*/
	private String mdPath;
	
 	/** 신규저장경로 */ 
	private String newSavePath;
	
	/** 원본저장경로 */ 
	public String getOriginSavePath() {
		return originSavePath;
	}

	public void setOriginSavePath(String originSavePath) {
		this.originSavePath = originSavePath;
	}
	
	/** 모듈별 기본 경로*/
	public String getMdPath() {
		return mdPath;
	}

	public void setMdPath(String mdPath) {
		this.mdPath = mdPath;
	}
	
	/** 신규저장경로 */ 
	public String getNewSavePath() {
		return newSavePath;
	}

	public void setNewSavePath(String newSavePath) {
		this.newSavePath = newSavePath;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":공통첨부파일상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(originSavePath!=null) { if(tab!=null) builder.append(tab); builder.append("originSavePath(원본저장경로):").append(originSavePath).append('\n'); }
		if(mdPath!=null) { if(tab!=null) builder.append(tab); builder.append("mdPath(모듈기본경로):").append(mdPath).append('\n'); }
		if(newSavePath!=null) { if(tab!=null) builder.append(tab); builder.append("newSavePath(신규저장경로):").append(newSavePath).append('\n'); }
	}

}
