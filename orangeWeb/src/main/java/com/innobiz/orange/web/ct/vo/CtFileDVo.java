package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtFileDVo extends CommonVoImpl implements CommonFileVo{

	/** serialVersionUID */
	private static final long serialVersionUID = 8292841890520898833L;

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
	
	/** 커뮤니티 ID */
	private String ctId;
	
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	
	// 추가 컬럼

	/** 새참조ID */
	private String newRefId;

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}

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
	
	/** 등록자 */
	public String getRegrUid() {
		return regrUid;
	}
	
	/** 등록자 */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}
	
	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	/** 새참조ID */
	public String getNewRefId() {
		return newRefId;
	}

	/** 새참조ID */
	public void setNewRefId(String newRefId) {
		this.newRefId = newRefId;
	}
	
	//추가 컬럼
	/** 파일 합계 */
	private String fileSum;
	
	public String getFileSum() {
		return fileSum;
	}

	public void setFileSum(String fileSum) {
		this.fileSum = fileSum;
	}

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
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
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(newRefId!=null) { if(tab!=null) builder.append(tab); builder.append("newRefId(새참조ID):").append(newRefId).append('\n'); }
		if(fileSum!=null) { if(tab!=null) builder.append(tab); builder.append("fileSum(파일 합계):").append(fileSum).append('\n'); }
		super.toString(builder, tab);
	}

}