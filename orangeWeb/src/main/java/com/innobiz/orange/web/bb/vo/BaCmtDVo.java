package com.innobiz.orange.web.bb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 한줄답변(BA_CMT_D) 테이블 VO */
public class BaCmtDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 5423942592037230092L;

	/** 한줄답변ID */
	private Integer cmtId;

	/** 게시물ID */
	private Integer bullId;

	/** 한줄답변 */
	private String cmt;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	// 추가 컬럼

	/** 등록자명 */
	private String regrNm;

	/** 한줄답변수 */
	private Integer cmtCnt;

	/** 게시물ID 목록 */
	private List<Integer> bullIdList;

	/** 한줄답변ID */
	public Integer getCmtId() {
		return cmtId;
	}

	/** 한줄답변ID */
	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	/** 한줄답변 */
	public String getCmt() {
		return cmt;
	}

	/** 한줄답변 */
	public void setCmt(String cmt) {
		this.cmt = cmt;
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

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 한줄답변수 */
	public Integer getCmtCnt() {
		return cmtCnt;
	}

	/** 한줄답변수 */
	public void setCmtCnt(Integer cmtCnt) {
		this.cmtCnt = cmtCnt;
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
			return "com.innobiz.orange.web.bb.dao.BaCmtDDao.selectBaCmtD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCmtDDao.insertBaCmtD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCmtDDao.updateBaCmtD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCmtDDao.deleteBaCmtD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCmtDDao.countBaCmtD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":한줄답변]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (cmtId != null) { if (tab != null) sb.append(tab); sb.append("cmtId(한줄답변ID):").append(cmtId).append('\n'); }
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (cmt != null) { if (tab != null) sb.append(tab); sb.append("cmt(한줄답변):").append(cmt).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regrNm != null) { if (tab != null) sb.append(tab); sb.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		
		if (cmtCnt != null) { if (tab != null) sb.append(tab); sb.append("cmtCnt(한줄답변수):").append(cmtCnt).append('\n'); }
		if (bullIdList!=null) { if(tab!=null) sb.append(tab); sb.append("bullIdList(게시물ID 목록):"); appendStringListTo(sb, bullIdList, tab); sb.append('\n'); }
		super.toString(sb, tab);
	}
}