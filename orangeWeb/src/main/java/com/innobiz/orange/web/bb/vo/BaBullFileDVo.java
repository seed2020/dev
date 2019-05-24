package com.innobiz.orange.web.bb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시물첨부파일(BA_BULL_FILE_D) 테이블 VO
 */
public class BaBullFileDVo extends CommonVoImpl implements CommonFileVo {

	/** serialVersionUID */
	private static final long serialVersionUID = 5163619713145315136L;

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
	
	// 추가 컬럼

	/** 새참조ID */
	private String newRefId;

	/** 첨부파일수 */
	private Integer fileCnt;

	/** 게시물ID 목록 */
	private List<Integer> bullIdList;

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

	/** 첨부파일수 */
	public Integer getFileCnt() {
		return fileCnt;
	}

	/** 첨부파일수 */
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}

	/** 게시물ID 목록 */
	public List<Integer> getBullIdList() {
		return bullIdList;
	}

	/** 게시물ID 목록 */
	public void setBullIdList(List<Integer> bullIdList) {
		this.bullIdList = bullIdList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullFileDDao.selectBaBullFileD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullFileDDao.insertBaBullFileD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullFileDDao.updateBaBullFileD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullFileDDao.deleteBaBullFileD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullFileDDao.countBaBullFileD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시옵션]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (fileId != null) { if (tab != null) sb.append(tab); sb.append("fileId(파일ID):").append(fileId).append('\n'); }
		if (refId != null) { if (tab != null) sb.append(tab); sb.append("refId(참조ID):").append(refId).append('\n'); }
		if (dispNm != null) { if (tab != null) sb.append(tab); sb.append("dispNm(표시이름):").append(dispNm).append('\n'); }
		if (dispOrdr != null) { if (tab != null) sb.append(tab); sb.append("dispOrdr(표시순서):").append(dispOrdr).append('\n'); }
		if (fileExt != null) { if (tab != null) sb.append(tab); sb.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if (fileSize != null) { if (tab != null) sb.append(tab); sb.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if (savePath != null) { if (tab != null) sb.append(tab); sb.append("savePath(저장경로):").append(savePath).append('\n'); }
		if (useYn != null) { if (tab != null) sb.append(tab); sb.append("useYn(사용여부):").append(useYn).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (newRefId != null) { if (tab != null) sb.append(tab); sb.append("newRefId(새참조ID):").append(newRefId).append('\n'); }
		
		if (fileCnt != null) { if (tab != null) sb.append(tab); sb.append("fileCnt(첨부파일수):").append(fileCnt).append('\n'); }
		if (bullIdList!=null) { if(tab!=null) sb.append(tab); sb.append("bullIdList(게시물ID 목록):"); appendStringListTo(sb, bullIdList, tab); sb.append('\n'); }
		super.toString(sb, tab);
	}
}