package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 이관대상임시(AP_TRAN_TGT_T) 테이블 VO
 */
public class ApTranTgtTVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 9075968304969849523L;

	/** 이관ID - KEY */
	private String tranId;

	/** 이관데이터구분코드 - KEY - doc:문서, trx:시행변환, paper:종이문서 */
	private String tranDataTypCd;

	/** 이관일련번호 - KEY */
	private String tranSeq;

	/** 결재번호 */
	private String apvNo;

	/** 이관ID - KEY */
	public String getTranId() {
		return tranId;
	}

	/** 이관ID - KEY */
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/** 이관데이터구분코드 - KEY - doc:문서, trx:시행변환, paper:종이문서 */
	public String getTranDataTypCd() {
		return tranDataTypCd;
	}

	/** 이관데이터구분코드 - KEY - doc:문서, trx:시행변환, paper:종이문서 */
	public void setTranDataTypCd(String tranDataTypCd) {
		this.tranDataTypCd = tranDataTypCd;
	}

	/** 이관일련번호 - KEY */
	public String getTranSeq() {
		return tranSeq;
	}

	/** 이관일련번호 - KEY */
	public void setTranSeq(String tranSeq) {
		this.tranSeq = tranSeq;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranTgtTDao.selectApTranTgtT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranTgtTDao.insertApTranTgtT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranTgtTDao.updateApTranTgtT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranTgtTDao.deleteApTranTgtT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApTranTgtTDao.countApTranTgtT";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관대상임시]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID-PK):").append(tranId).append('\n'); }
		if(tranDataTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("tranDataTypCd(이관데이터구분코드-PK):").append(tranDataTypCd).append('\n'); }
		if(tranSeq!=null) { if(tab!=null) builder.append(tab); builder.append("tranSeq(이관일련번호-PK):").append(tranSeq).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		super.toString(builder, tab);
	}
}
