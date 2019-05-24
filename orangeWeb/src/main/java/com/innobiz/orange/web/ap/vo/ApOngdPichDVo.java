package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서담당자상세(AP_ONGD_PICH_D) 테이블 VO
 */
public class ApOngdPichDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 977576049432858252L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	private String pichTypCd;

	/** 담당자UID */
	private String pichUid;

	/** 담당자부서ID */
	private String pichDeptId;

	/** 담당자명 */
	private String pichNm;

	/** 담당자부서명 */
	private String pichDeptNm;

	/** 담당자의견내용 */
	private String pichOpinCont;

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	private String hdlStatCd;

	/** 처리일시 */
	private String hdlDt;

	/** 조회일시 */
	private String vwDt;

	/** 이전결재자결재일시 */
	private String prevApvrApvDt;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 처리상태명 */
	private String hdlStatNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	public String getPichTypCd() {
		return pichTypCd;
	}

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	public void setPichTypCd(String pichTypCd) {
		this.pichTypCd = pichTypCd;
	}

	/** 담당자UID */
	public String getPichUid() {
		return pichUid;
	}

	/** 담당자UID */
	public void setPichUid(String pichUid) {
		this.pichUid = pichUid;
	}

	/** 담당자부서ID */
	public String getPichDeptId() {
		return pichDeptId;
	}

	/** 담당자부서ID */
	public void setPichDeptId(String pichDeptId) {
		this.pichDeptId = pichDeptId;
	}

	/** 담당자명 */
	public String getPichNm() {
		return pichNm;
	}

	/** 담당자명 */
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}

	/** 담당자부서명 */
	public String getPichDeptNm() {
		return pichDeptNm;
	}

	/** 담당자부서명 */
	public void setPichDeptNm(String pichDeptNm) {
		this.pichDeptNm = pichDeptNm;
	}

	/** 담당자의견내용 */
	public String getPichOpinCont() {
		return pichOpinCont;
	}

	/** 담당자의견내용 */
	public void setPichOpinCont(String pichOpinCont) {
		this.pichOpinCont = pichOpinCont;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public String getHdlStatCd() {
		return hdlStatCd;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public void setHdlStatCd(String hdlStatCd) {
		this.hdlStatCd = hdlStatCd;
	}

	/** 처리일시 */
	public String getHdlDt() {
		return hdlDt;
	}

	/** 처리일시 */
	public void setHdlDt(String hdlDt) {
		this.hdlDt = hdlDt;
	}

	/** 조회일시 */
	public String getVwDt() {
		return vwDt;
	}

	/** 조회일시 */
	public void setVwDt(String vwDt) {
		this.vwDt = vwDt;
	}

	/** 이전결재자결재일시 */
	public String getPrevApvrApvDt() {
		return prevApvrApvDt;
	}

	/** 이전결재자결재일시 */
	public void setPrevApvrApvDt(String prevApvrApvDt) {
		this.prevApvrApvDt = prevApvrApvDt;
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

	/** 처리상태명 */
	public String getHdlStatNm() {
		return hdlStatNm;
	}

	/** 처리상태명 */
	public void setHdlStatNm(String hdlStatNm) {
		this.hdlStatNm = hdlStatNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPichDDao.selectApOngdPichD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPichDDao.insertApOngdPichD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPichDDao.updateApOngdPichD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPichDDao.deleteApOngdPichD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdPichDDao.countApOngdPichD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서담당자상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_PICH_D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(pichTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pichTypCd(담당자구분코드-PK):").append(pichTypCd).append('\n'); }
		if(pichUid!=null) { if(tab!=null) builder.append(tab); builder.append("pichUid(담당자UID):").append(pichUid).append('\n'); }
		if(pichDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("pichDeptId(담당자부서ID):").append(pichDeptId).append('\n'); }
		if(pichNm!=null) { if(tab!=null) builder.append(tab); builder.append("pichNm(담당자명):").append(pichNm).append('\n'); }
		if(pichDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("pichDeptNm(담당자부서명):").append(pichDeptNm).append('\n'); }
		if(pichOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("pichOpinCont(담당자의견내용):").append(pichOpinCont).append('\n'); }
		if(hdlStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("hdlStatCd(처리상태코드):").append(hdlStatCd).append('\n'); }
		if(hdlDt!=null) { if(tab!=null) builder.append(tab); builder.append("hdlDt(처리일시):").append(hdlDt).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		if(prevApvrApvDt!=null) { if(tab!=null) builder.append(tab); builder.append("prevApvrApvDt(이전결재자결재일시):").append(prevApvrApvDt).append('\n'); }
		if(hdlStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("hdlStatNm(처리상태명):").append(hdlStatNm).append('\n'); }
		super.toString(builder, tab);
	}
}
