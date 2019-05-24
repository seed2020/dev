package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * SSO메세지임시(PT_SSO_MSG_T) 테이블 VO
 */
public class PtSsoMsgTVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 9211704437906597536L;

	/** 메세지ID - KEY */
	private String msgId;

	/** 메세지값 */
	private String msgVa;

	/** 등록일시 */
	private String regDt;

	/** 메세지ID - KEY */
	public String getMsgId() {
		return msgId;
	}

	/** 메세지ID - KEY */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/** 메세지값 */
	public String getMsgVa() {
		return msgVa;
	}

	/** 메세지값 */
	public void setMsgVa(String msgVa) {
		this.msgVa = msgVa;
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
			return "com.innobiz.orange.web.pt.dao.PtSsoMsgTDao.selectPtSsoMsgT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoMsgTDao.insertPtSsoMsgT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoMsgTDao.updatePtSsoMsgT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoMsgTDao.deletePtSsoMsgT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSsoMsgTDao.countPtSsoMsgT";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":SSO메세지임시]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(msgId!=null) { if(tab!=null) builder.append(tab); builder.append("msgId(메세지ID-PK):").append(msgId).append('\n'); }
		if(msgVa!=null) { if(tab!=null) builder.append(tab); builder.append("msgVa(메세지값):").append(msgVa).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}
}
