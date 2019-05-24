package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서발송상세(AP_ONGD_SEND_D) 테이블 VO
 */
public class ApOngdSendDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3167881860480492534L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 발송일련번호 - KEY */
	private String sendSeq;

	/** 수신처ID */
	private String recvDeptId;

	/** 수신처명 */
	private String recvDeptNm;

	/** 참조처명 */
	private String refDeptNm;

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	private String recvDeptTypCd;

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	private String hdlStatCd;

	/** 처리자UID */
	private String hdlrUid;

	/** 처리자명 */
	private String hdlrNm;

	/** 처리자의견내용 */
	private String hdlrOpinCont;

	/** 처리일시 */
	private String hdlDt;

	/** 조회일시 */
	private String vwDt;

	/** 이전결재자결재일시 */
	private String prevApvrApvDt;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 원본결재번호 */
	private String orgnApvNo;

	/** 수신처구분명 */
	private String recvDeptTypNm;

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

	/** 발송일련번호 - KEY */
	public String getSendSeq() {
		return sendSeq;
	}

	/** 발송일련번호 - KEY */
	public void setSendSeq(String sendSeq) {
		this.sendSeq = sendSeq;
	}

	/** 수신처ID */
	public String getRecvDeptId() {
		return recvDeptId;
	}

	/** 수신처ID */
	public void setRecvDeptId(String recvDeptId) {
		this.recvDeptId = recvDeptId;
	}

	/** 수신처명 */
	public String getRecvDeptNm() {
		return recvDeptNm;
	}

	/** 수신처명 */
	public void setRecvDeptNm(String recvDeptNm) {
		this.recvDeptNm = recvDeptNm;
	}

	/** 참조처명 */
	public String getRefDeptNm() {
		return refDeptNm;
	}

	/** 참조처명 */
	public void setRefDeptNm(String refDeptNm) {
		this.refDeptNm = refDeptNm;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public String getRecvDeptTypCd() {
		return recvDeptTypCd;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public void setRecvDeptTypCd(String recvDeptTypCd) {
		this.recvDeptTypCd = recvDeptTypCd;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public String getHdlStatCd() {
		return hdlStatCd;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public void setHdlStatCd(String hdlStatCd) {
		this.hdlStatCd = hdlStatCd;
	}

	/** 처리자UID */
	public String getHdlrUid() {
		return hdlrUid;
	}

	/** 처리자UID */
	public void setHdlrUid(String hdlrUid) {
		this.hdlrUid = hdlrUid;
	}

	/** 처리자명 */
	public String getHdlrNm() {
		return hdlrNm;
	}

	/** 처리자명 */
	public void setHdlrNm(String hdlrNm) {
		this.hdlrNm = hdlrNm;
	}

	/** 처리자의견내용 */
	public String getHdlrOpinCont() {
		return hdlrOpinCont;
	}

	/** 처리자의견내용 */
	public void setHdlrOpinCont(String hdlrOpinCont) {
		this.hdlrOpinCont = hdlrOpinCont;
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

	/** 원본결재번호 */
	public String getOrgnApvNo() {
		return orgnApvNo;
	}

	/** 원본결재번호 */
	public void setOrgnApvNo(String orgnApvNo) {
		this.orgnApvNo = orgnApvNo;
	}

	/** 수신처구분명 */
	public String getRecvDeptTypNm() {
		return recvDeptTypNm;
	}

	/** 수신처구분명 */
	public void setRecvDeptTypNm(String recvDeptTypNm) {
		this.recvDeptTypNm = recvDeptTypNm;
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
			return "com.innobiz.orange.web.ap.dao.ApOngdSendDDao.selectApOngdSendD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdSendDDao.insertApOngdSendD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdSendDDao.updateApOngdSendD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdSendDDao.deleteApOngdSendD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdSendDDao.countApOngdSendD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서발송상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_SEND_D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(sendSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sendSeq(발송일련번호-PK):").append(sendSeq).append('\n'); }
		if(recvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptId(수신처ID):").append(recvDeptId).append('\n'); }
		if(recvDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptNm(수신처명):").append(recvDeptNm).append('\n'); }
		if(refDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("refDeptNm(참조처명):").append(refDeptNm).append('\n'); }
		if(recvDeptTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypCd(수신처구분코드):").append(recvDeptTypCd).append('\n'); }
		if(hdlStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("hdlStatCd(처리상태코드):").append(hdlStatCd).append('\n'); }
		if(hdlrUid!=null) { if(tab!=null) builder.append(tab); builder.append("hdlrUid(처리자UID):").append(hdlrUid).append('\n'); }
		if(hdlrNm!=null) { if(tab!=null) builder.append(tab); builder.append("hdlrNm(처리자명):").append(hdlrNm).append('\n'); }
		if(hdlrOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("hdlrOpinCont(처리자의견내용):").append(hdlrOpinCont).append('\n'); }
		if(hdlDt!=null) { if(tab!=null) builder.append(tab); builder.append("hdlDt(처리일시):").append(hdlDt).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		if(prevApvrApvDt!=null) { if(tab!=null) builder.append(tab); builder.append("prevApvrApvDt(이전결재자결재일시):").append(prevApvrApvDt).append('\n'); }
		if(orgnApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("orgnApvNo(원본결재번호):").append(orgnApvNo).append('\n'); }
		if(recvDeptTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypNm(수신처구분명):").append(recvDeptTypNm).append('\n'); }
		if(hdlStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("hdlStatNm(처리상태명):").append(hdlStatNm).append('\n'); }
		super.toString(builder, tab);
	}
}
