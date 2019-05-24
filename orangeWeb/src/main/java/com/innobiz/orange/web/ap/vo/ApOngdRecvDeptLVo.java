package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서수신처내역(AP_ONGD_RECV_DEPT_L) 테이블 VO
 */
public class ApOngdRecvDeptLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4906708753739903064L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 수신처이력번호 - KEY */
	private String recvDeptHstNo;

	/** 수신처일련번호 - KEY */
	private String recvDeptSeq;

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	private String recvDeptTypCd;

	/** 수신처ID */
	private String recvDeptId;

	/** 수신처명 */
	private String recvDeptNm;

	/** 참조처ID */
	private String refDeptId;

	/** 참조처명 */
	private String refDeptNm;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 발송여부 */
	private String sendYn;

	/** 추가발송여부 */
	private String addSendYn;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 수신처구분명 */
	private String recvDeptTypNm;

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

	/** 수신처이력번호 - KEY */
	public String getRecvDeptHstNo() {
		return recvDeptHstNo;
	}

	/** 수신처이력번호 - KEY */
	public void setRecvDeptHstNo(String recvDeptHstNo) {
		this.recvDeptHstNo = recvDeptHstNo;
	}

	/** 수신처일련번호 - KEY */
	public String getRecvDeptSeq() {
		return recvDeptSeq;
	}

	/** 수신처일련번호 - KEY */
	public void setRecvDeptSeq(String recvDeptSeq) {
		this.recvDeptSeq = recvDeptSeq;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public String getRecvDeptTypCd() {
		return recvDeptTypCd;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public void setRecvDeptTypCd(String recvDeptTypCd) {
		this.recvDeptTypCd = recvDeptTypCd;
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

	/** 참조처ID */
	public String getRefDeptId() {
		return refDeptId;
	}

	/** 참조처ID */
	public void setRefDeptId(String refDeptId) {
		this.refDeptId = refDeptId;
	}

	/** 참조처명 */
	public String getRefDeptNm() {
		return refDeptNm;
	}

	/** 참조처명 */
	public void setRefDeptNm(String refDeptNm) {
		this.refDeptNm = refDeptNm;
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

	/** 발송여부 */
	public String getSendYn() {
		return sendYn;
	}

	/** 발송여부 */
	public void setSendYn(String sendYn) {
		this.sendYn = sendYn;
	}

	/** 추가발송여부 */
	public String getAddSendYn() {
		return addSendYn;
	}

	/** 추가발송여부 */
	public void setAddSendYn(String addSendYn) {
		this.addSendYn = addSendYn;
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

	/** 수신처구분명 */
	public String getRecvDeptTypNm() {
		return recvDeptTypNm;
	}

	/** 수신처구분명 */
	public void setRecvDeptTypNm(String recvDeptTypNm) {
		this.recvDeptTypNm = recvDeptTypNm;
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
			return "com.innobiz.orange.web.ap.dao.ApOngdRecvDeptLDao.selectApOngdRecvDeptL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRecvDeptLDao.insertApOngdRecvDeptL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRecvDeptLDao.updateApOngdRecvDeptL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRecvDeptLDao.deleteApOngdRecvDeptL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdRecvDeptLDao.countApOngdRecvDeptL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서수신처내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_RECV_DEPT_L").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(recvDeptHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptHstNo(수신처이력번호-PK):").append(recvDeptHstNo).append('\n'); }
		if(recvDeptSeq!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptSeq(수신처일련번호-PK):").append(recvDeptSeq).append('\n'); }
		if(recvDeptTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypCd(수신처구분코드):").append(recvDeptTypCd).append('\n'); }
		if(recvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptId(수신처ID):").append(recvDeptId).append('\n'); }
		if(recvDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptNm(수신처명):").append(recvDeptNm).append('\n'); }
		if(refDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("refDeptId(참조처ID):").append(refDeptId).append('\n'); }
		if(refDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("refDeptNm(참조처명):").append(refDeptNm).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(sendYn!=null) { if(tab!=null) builder.append(tab); builder.append("sendYn(발송여부):").append(sendYn).append('\n'); }
		if(addSendYn!=null) { if(tab!=null) builder.append(tab); builder.append("addSendYn(추가발송여부):").append(addSendYn).append('\n'); }
		if(recvDeptTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypNm(수신처구분명):").append(recvDeptTypNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
