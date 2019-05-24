package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원직자보안상세(OR_ODUR_SECU_D) 테이블 VO
 */
public class OrOdurSecuDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7053123447737508185L;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 보안ID - KEY - lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부, useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부 */
	private String secuId;

	/** 보안값 */
	private String secuVa;

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 보안ID - KEY - lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부, useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부 */
	public String getSecuId() {
		return secuId;
	}

	/** 보안ID - KEY - lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부, useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부 */
	public void setSecuId(String secuId) {
		this.secuId = secuId;
	}

	/** 보안값 */
	public String getSecuVa() {
		return secuVa;
	}

	/** 보안값 */
	public void setSecuVa(String secuVa) {
		this.secuVa = secuVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurSecuDDao.selectOrOdurSecuD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurSecuDDao.insertOrOdurSecuD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurSecuDDao.updateOrOdurSecuD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurSecuDDao.deleteOrOdurSecuD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOdurSecuDDao.countOrOdurSecuD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원직자보안상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(secuId!=null) { if(tab!=null) builder.append(tab); builder.append("secuId(보안ID-PK):").append(secuId).append('\n'); }
		if(secuVa!=null) { if(tab!=null) builder.append(tab); builder.append("secuVa(보안값):").append(secuVa).append('\n'); }
		super.toString(builder, tab);
	}
}
