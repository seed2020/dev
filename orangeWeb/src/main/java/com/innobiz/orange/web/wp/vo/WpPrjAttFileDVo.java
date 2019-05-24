package com.innobiz.orange.web.wp.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 프로잭트첨부파일상세(WP_PRJ_ATT_FILE_D) 테이블 VO
 */
public class WpPrjAttFileDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2461601793371578334L;

	/** 프로잭트번호 - KEY */
	private String prjNo;

	/** 첨부일련번호 - KEY */
	private String attSeq;

	/** 첨부표시명 */
	private String dispNm;

	/** 파일확장자 */
	private String fileExt;

	/** 파일크기 */
	private String fileSize;

	/** 저장경로 */
	private String savePath;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 프로잭트번호 - KEY */
	public String getPrjNo() {
		return prjNo;
	}

	/** 프로잭트번호 - KEY */
	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
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
	public String getDispNm() {
		return dispNm;
	}

	/** 첨부표시명 */
	public void setDispNm(String dispNm) {
		this.dispNm = dispNm;
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
	public String getFileSize() {
		return fileSize;
	}

	/** 파일크기 */
	public void setFileSize(String fileSize) {
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

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAttFileDDao.selectWpPrjAttFileD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAttFileDDao.insertWpPrjAttFileD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAttFileDDao.updateWpPrjAttFileD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAttFileDDao.deleteWpPrjAttFileD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wp.dao.WpPrjAttFileDDao.countWpPrjAttFileD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":프로잭트첨부파일상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(prjNo!=null) { if(tab!=null) builder.append(tab); builder.append("prjNo(프로잭트번호-PK):").append(prjNo).append('\n'); }
		if(attSeq!=null) { if(tab!=null) builder.append(tab); builder.append("attSeq(첨부일련번호-PK):").append(attSeq).append('\n'); }
		if(dispNm!=null) { if(tab!=null) builder.append(tab); builder.append("dispNm(첨부표시명):").append(dispNm).append('\n'); }
		if(fileExt!=null) { if(tab!=null) builder.append(tab); builder.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if(fileSize!=null) { if(tab!=null) builder.append(tab); builder.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if(savePath!=null) { if(tab!=null) builder.append(tab); builder.append("savePath(저장경로):").append(savePath).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}
}
