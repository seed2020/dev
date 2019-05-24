package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 진행문서첨부파일내역(AP_ONGD_ATT_FILE_L) 테이블 VO
 */
public class ApOngdAttFileLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1950045795128432267L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 첨부이력번호 - KEY */
	private String attHstNo;

	/** 첨부일련번호 - KEY */
	private String attSeq;

	/** 첨부표시명 */
	private String attDispNm;

	/** 파일확장자 */
	private String fileExt;

	/** 파일K바이트 */
	private String fileKb;

	/** 파일경로 */
	private String filePath;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 첨부일련번호 목록 */
	private List<String> attSeqList;

	/** 수정자명 */
	private String modrNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 첨부이력번호 - KEY */
	public String getAttHstNo() {
		return attHstNo;
	}

	/** 첨부이력번호 - KEY */
	public void setAttHstNo(String attHstNo) {
		this.attHstNo = attHstNo;
	}

	/** 첨부일련번호 - KEY */
	public String getAttSeq() {
		return attSeq;
	}

	/** 첨부일련번호 - KEY */
	public void setAttSeq(String attSeq) {
		this.attSeq = attSeq;
	}

	/** 첨부표시명 */
	public String getAttDispNm() {
		return attDispNm;
	}

	/** 첨부표시명 */
	public void setAttDispNm(String attDispNm) {
		this.attDispNm = attDispNm;
	}

	/** 파일확장자 */
	public String getFileExt() {
		return fileExt;
	}

	/** 파일확장자 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	/** 파일K바이트 */
	public String getFileKb() {
		return fileKb;
	}

	/** 파일K바이트 */
	public void setFileKb(String fileKb) {
		this.fileKb = fileKb;
	}

	/** 파일경로 */
	public String getFilePath() {
		return filePath;
	}

	/** 파일경로 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 스토리지 리턴 */
	public String getStorage(){
		return storage == null ? "ONGD" : storage;
	}

	/** 스토리지 세팅 - ONGD, 년도4자리 */
	public void setStorage(String storage) {
		if(storage==null || storage.isEmpty()) this.storage = null;
		else if(storage.equals("ONGD") || storage.matches("[0-9A-Z]{4}")) this.storage = storage;
	}

	/** 첨부일련번호 목록 */
	public List<String> getAttSeqList() {
		return attSeqList;
	}

	/** 첨부일련번호 목록 */
	public void setAttSeqList(List<String> attSeqList) {
		this.attSeqList = attSeqList;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdAttFileLDao.selectApOngdAttFileL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdAttFileLDao.insertApOngdAttFileL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdAttFileLDao.updateApOngdAttFileL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdAttFileLDao.deleteApOngdAttFileL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdAttFileLDao.countApOngdAttFileL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서첨부파일내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_ATT_FILE_L").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(attHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("attHstNo(첨부이력번호-PK):").append(attHstNo).append('\n'); }
		if(attSeq!=null) { if(tab!=null) builder.append(tab); builder.append("attSeq(첨부일련번호-PK):").append(attSeq).append('\n'); }
		if(attDispNm!=null) { if(tab!=null) builder.append(tab); builder.append("attDispNm(첨부표시명):").append(attDispNm).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileKb!=null) { if(tab!=null) builder.append(tab); builder.append("fileKb(파일K바이트):").append(fileKb).append('\n'); }
		if(filePath!=null) { if(tab!=null) builder.append(tab); builder.append("filePath(파일경로):").append(filePath).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(attSeqList!=null) { if(tab!=null) builder.append(tab); builder.append("attSeqList(첨부일련번호 목록):"); appendStringListTo(builder, attSeqList, tab); builder.append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
