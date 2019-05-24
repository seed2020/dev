package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이관기본(AP_TRAN_B) 테이블 VO
 */
public class ApTranBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2960708293907611366L;

	/** 이관ID - KEY */
	private String tranId;

	/** 회사ID */
	private String compId;

	/** 저장소ID */
	private String storId;

	/** 저장소리소스ID */
	private String storRescId;

	/** 이관대상시작년월일 */
	private String tranTgtStrtYmd;

	/** 이관대상종료년월일 */
	private String tranTgtEndYmd;

	/** 문서포함여부 */
	private String docInclYn;

	/** 종이문서포함여부 */
	private String papDocInclYn;

	/** 문서대상건수 */
	private String docTgtCnt;

	/** 변환대상건수 */
	private String trxTgtCnt;

	/** 종이대상건수 */
	private String papTgtCnt;

	/** 이관시작일시 */
	private String tranStrtDt;

	/** 이관종료일시 */
	private String tranEndDt;

	/** 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 */
	private String tranProcStatCd;

	/** 조각모음여부 */
	private String defragYn;

	/** 사용자UID */
	private String userUid;


	// 추가컬럼
	/** 전체대상건수 */
	private String allTgtCnt;

	/** 저장소리소스명 */
	private String storRescNm;

	/** 사용자명 */
	private String userNm;

	/** 이관ID - KEY */
	public String getTranId() {
		return tranId;
	}

	/** 이관ID - KEY */
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 저장소ID */
	public String getStorId() {
		return storId;
	}

	/** 저장소ID */
	public void setStorId(String storId) {
		this.storId = storId;
	}

	/** 저장소리소스ID */
	public String getStorRescId() {
		return storRescId;
	}

	/** 저장소리소스ID */
	public void setStorRescId(String storRescId) {
		this.storRescId = storRescId;
	}

	/** 이관대상시작년월일 */
	public String getTranTgtStrtYmd() {
		return tranTgtStrtYmd;
	}

	/** 이관대상시작년월일 */
	public void setTranTgtStrtYmd(String tranTgtStrtYmd) {
		this.tranTgtStrtYmd = tranTgtStrtYmd;
	}

	/** 이관대상종료년월일 */
	public String getTranTgtEndYmd() {
		return tranTgtEndYmd;
	}

	/** 이관대상종료년월일 */
	public void setTranTgtEndYmd(String tranTgtEndYmd) {
		this.tranTgtEndYmd = tranTgtEndYmd;
	}

	/** 문서포함여부 */
	public String getDocInclYn() {
		return docInclYn;
	}

	/** 문서포함여부 */
	public void setDocInclYn(String docInclYn) {
		this.docInclYn = docInclYn;
	}

	/** 종이문서포함여부 */
	public String getPapDocInclYn() {
		return papDocInclYn;
	}

	/** 종이문서포함여부 */
	public void setPapDocInclYn(String papDocInclYn) {
		this.papDocInclYn = papDocInclYn;
	}

	/** 문서대상건수 */
	public String getDocTgtCnt() {
		return docTgtCnt;
	}

	/** 문서대상건수 */
	public void setDocTgtCnt(String docTgtCnt) {
		this.docTgtCnt = docTgtCnt;
	}

	/** 변환대상건수 */
	public String getTrxTgtCnt() {
		return trxTgtCnt;
	}

	/** 변환대상건수 */
	public void setTrxTgtCnt(String trxTgtCnt) {
		this.trxTgtCnt = trxTgtCnt;
	}

	/** 종이대상건수 */
	public String getPapTgtCnt() {
		return papTgtCnt;
	}

	/** 종이대상건수 */
	public void setPapTgtCnt(String papTgtCnt) {
		this.papTgtCnt = papTgtCnt;
	}

	/** 이관시작일시 */
	public String getTranStrtDt() {
		return tranStrtDt;
	}

	/** 이관시작일시 */
	public void setTranStrtDt(String tranStrtDt) {
		this.tranStrtDt = tranStrtDt;
	}

	/** 이관종료일시 */
	public String getTranEndDt() {
		return tranEndDt;
	}

	/** 이관종료일시 */
	public void setTranEndDt(String tranEndDt) {
		this.tranEndDt = tranEndDt;
	}

	/** 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 */
	public String getTranProcStatCd() {
		return tranProcStatCd;
	}

	/** 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 */
	public void setTranProcStatCd(String tranProcStatCd) {
		this.tranProcStatCd = tranProcStatCd;
	}

	/** 조각모음여부 */
	public String getDefragYn() {
		return defragYn;
	}

	/** 조각모음여부 */
	public void setDefragYn(String defragYn) {
		this.defragYn = defragYn;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 전체대상건수 */
	public String getAllTgtCnt() {
		return allTgtCnt;
	}

	/** 전체대상건수 */
	public void setAllTgtCnt(String allTgtCnt) {
		this.allTgtCnt = allTgtCnt;
	}

	/** 저장소리소스명 */
	public String getStorRescNm() {
		return storRescNm;
	}

	/** 저장소리소스명 */
	public void setStorRescNm(String storRescNm) {
		this.storRescNm = storRescNm;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranBDao.selectApTranB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranBDao.insertApTranB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranBDao.updateApTranB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranBDao.deleteApTranB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranBDao.countApTranB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID-PK):").append(tranId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(storRescId!=null) { if(tab!=null) builder.append(tab); builder.append("storRescId(저장소리소스ID):").append(storRescId).append('\n'); }
		if(tranTgtStrtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("tranTgtStrtYmd(이관대상시작년월일):").append(tranTgtStrtYmd).append('\n'); }
		if(tranTgtEndYmd!=null) { if(tab!=null) builder.append(tab); builder.append("tranTgtEndYmd(이관대상종료년월일):").append(tranTgtEndYmd).append('\n'); }
		if(docInclYn!=null) { if(tab!=null) builder.append(tab); builder.append("docInclYn(문서포함여부):").append(docInclYn).append('\n'); }
		if(papDocInclYn!=null) { if(tab!=null) builder.append(tab); builder.append("papDocInclYn(종이문서포함여부):").append(papDocInclYn).append('\n'); }
		if(docTgtCnt!=null) { if(tab!=null) builder.append(tab); builder.append("docTgtCnt(문서대상건수):").append(docTgtCnt).append('\n'); }
		if(trxTgtCnt!=null) { if(tab!=null) builder.append(tab); builder.append("trxTgtCnt(변환대상건수):").append(trxTgtCnt).append('\n'); }
		if(papTgtCnt!=null) { if(tab!=null) builder.append(tab); builder.append("papTgtCnt(종이대상건수):").append(papTgtCnt).append('\n'); }
		if(tranStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("tranStrtDt(이관시작일시):").append(tranStrtDt).append('\n'); }
		if(tranEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("tranEndDt(이관종료일시):").append(tranEndDt).append('\n'); }
		if(tranProcStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("tranProcStatCd(이관진행상태코드):").append(tranProcStatCd).append('\n'); }
		if(defragYn!=null) { if(tab!=null) builder.append(tab); builder.append("defragYn(조각모음여부):").append(defragYn).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(allTgtCnt!=null) { if(tab!=null) builder.append(tab); builder.append("allTgtCnt(전체대상건수):").append(allTgtCnt).append('\n'); }
		if(storRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("storRescNm(저장소리소스명):").append(storRescNm).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
