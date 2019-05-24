package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시물사진(BA_BULL_PHOTO_D) 테이블 VO
 */
public class BaBullPhotoDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 4282710890353616264L;

	/** 게시물ID */
	private Integer bullId;

	/** 파일확장자 */
	private String fileExt;

	/** 파일크기 */
	private Long fileSize;

	/** 저장경로 */
	private String savePath;

	/** 이미지폭 */
	private Integer imgWdth;

	/** 이미지높이 */
	private Integer imgHght;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	// 추가 컬럼

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
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

	/** 이미지폭 */
	public Integer getImgWdth() {
		return imgWdth;
	}

	/** 이미지폭 */
	public void setImgWdth(Integer imgWdth) {
		this.imgWdth = imgWdth;
	}

	/** 이미지높이 */
	public Integer getImgHght() {
		return imgHght;
	}

	/** 이미지높이 */
	public void setImgHght(Integer imgHght) {
		this.imgHght = imgHght;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPhotoDDao.selectBaBullPhotoD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPhotoDDao.insertBaBullPhotoD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPhotoDDao.updateBaBullPhotoD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPhotoDDao.deleteBaBullPhotoD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPhotoDDao.countBaBullPhotoD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시물첨부파일]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (fileExt != null) { if (tab != null) sb.append(tab); sb.append("fileExt(파일확장자):").append(fileExt).append('\n'); }
		if (fileSize != null) { if (tab != null) sb.append(tab); sb.append("fileSize(파일크기):").append(fileSize).append('\n'); }
		if (savePath != null) { if (tab != null) sb.append(tab); sb.append("savePath(저장경로):").append(savePath).append('\n'); }
		if (imgWdth != null) { if (tab != null) sb.append(tab); sb.append("imgWdth(이미지폭):").append(imgWdth).append('\n'); }
		if (imgHght != null) { if (tab != null) sb.append(tab); sb.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(sb, tab);
	}
}