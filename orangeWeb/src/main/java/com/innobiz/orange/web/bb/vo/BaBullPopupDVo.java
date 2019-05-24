package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시옵션(BA_BULL_POPUP_D) 테이블 VO
 */
public class BaBullPopupDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 6536652216710616390L;

	/** 게시물ID */
	private Integer bullId;

	/** 게시판ID */
	private String brdId;
	
	/** 시작일 */
	private String strtDt;
	
	/** 종료일 */
	private String endDt;
	
	/** 팝업넓이 */
	private String width;
	
	/** 표시안함여부 */
	private String dispYn;

	/** 사용여부 */
	private String useYn;
	
	/** 오늘날짜 */
	private String todayYmd;
	
	/** 메뉴ID  */
	private String compId;
	
	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}
	
	/** 게시판ID */
	public String getBrdId() {
		return brdId;
	}

	/** 게시판ID */
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}
	
	/** 시작일 */
	public String getStrtDt() {
		return strtDt;
	}

	/** 시작일 */
	public void setStrtDt(String strtDt) {
		this.strtDt = strtDt;
	}
	
	/** 종료일 */
	public String getEndDt() {
		return endDt;
	}

	/** 종료일 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	
	/** 팝업넓이 */
	public String getWidth() {
		return width;
	}

	/** 팝업넓이 */
	public void setWidth(String width) {
		this.width = width;
	}

	/** 표시안함여부 */
	public String getDispYn() {
		return dispYn;
	}

	/** 표시안함여부 */
	public void setDispYn(String dispYn) {
		this.dispYn = dispYn;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	/** 오늘날짜 */
	public String getTodayYmd() {
		return todayYmd;
	}

	/** 오늘날짜 */
	public void setTodayYmd(String todayYmd) {
		this.todayYmd = todayYmd;
	}
	
	/** 메뉴ID */
	public String getCompId() {
		return compId;
	}
	
	/** 메뉴ID */
	public void setCompId(String compId) {
		this.compId = compId;
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


	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDDao.selectBaBullPopupD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDDao.insertBaBullPopupD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDDao.updateBaBullPopupD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDDao.deleteBaBullPopupD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBullPopupDDao.countBaBullPopupD";
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
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (strtDt != null) { if (tab != null) sb.append(tab); sb.append("startDt(시작일):").append(strtDt).append('\n'); }
		if (endDt != null) { if (tab != null) sb.append(tab); sb.append("endDt(종료일):").append(endDt).append('\n'); }
		if (width != null) { if (tab != null) sb.append(tab); sb.append("width(팝업넓이):").append(width).append('\n'); }
		if (dispYn != null) { if (tab != null) sb.append(tab); sb.append("dispYn(표시안함여부):").append(dispYn).append('\n'); }
		if (useYn != null) { if (tab != null) sb.append(tab); sb.append("useYn(사용여부):").append(useYn).append('\n'); }
		if (todayYmd != null) { if (tab != null) sb.append(tab); sb.append("todayYmd(오늘날짜):").append(todayYmd).append('\n'); }
		if(compId!=null) { if(tab!=null) sb.append(tab); sb.append("compId(회사ID):").append(compId).append('\n'); }
		if(regrUid!=null) { if(tab!=null) sb.append(tab); sb.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) sb.append(tab); sb.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(sb, tab);
	}
}