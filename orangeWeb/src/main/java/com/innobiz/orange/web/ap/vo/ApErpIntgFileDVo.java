package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * ERP연계파일상세(AP_ERP_INTG_FILE_D) 테이블 VO
 */
public class ApErpIntgFileDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4459877569569487549L;

	/** 연계번호 - KEY */
	private String intgNo;

	/** 일련번호 - KEY */
	private String seq;

	/** 첨부표시명 */
	private String attDispNm;

	/** 파일확장자 */
	private String fileExt;

	/** 파일K바이트 */
	private String fileKb;

	/** 파일내용 */
	private byte[] fileCont;


	// 추가컬럼
	/** LOB 데이터 조회 여부 */
	private boolean withLob;

	/** 연계번호 - KEY */
	public String getIntgNo() {
		return intgNo;
	}

	/** 연계번호 - KEY */
	public void setIntgNo(String intgNo) {
		this.intgNo = intgNo;
	}

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
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

	/** 파일내용 */
	public byte[] getFileCont() {
		return fileCont;
	}

	/** 파일내용 */
	public void setFileCont(byte[] fileCont) {
		this.fileCont = fileCont;
	}

	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}

	/** LOB 데이터 조회 여부 */
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgFileDDao.selectApErpIntgFileD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgFileDDao.insertApErpIntgFileD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgFileDDao.updateApErpIntgFileD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgFileDDao.deleteApErpIntgFileD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApErpIntgFileDDao.countApErpIntgFileD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":ERP연계파일상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(intgNo!=null) { if(tab!=null) builder.append(tab); builder.append("intgNo(연계번호-PK):").append(intgNo).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(attDispNm!=null) { if(tab!=null) builder.append(tab); builder.append("attDispNm(첨부표시명):").append(attDispNm).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileKb!=null) { if(tab!=null) builder.append(tab); builder.append("fileKb(파일K바이트):").append(fileKb).append('\n'); }
		if(fileCont!=null) { if(tab!=null) builder.append(tab); builder.append("fileCont(파일내용):").append("- BLOB -").append('\n'); }
		if(withLob) { if(tab!=null) builder.append(tab); builder.append("withLob(LOB 데이터 조회 여부):").append(withLob).append('\n'); }
		super.toString(builder, tab);
	}
}
