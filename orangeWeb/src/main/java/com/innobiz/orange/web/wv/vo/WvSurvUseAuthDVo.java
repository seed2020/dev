package com.innobiz.orange.web.wv.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** WV_SURV_USE_AUTH_D[설문 사용 권한 상세] */
public class WvSurvUseAuthDVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = -1669145217856261832L;
	
	/** 회사ID */
	private String compId;

	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 설문ID */
	private String survId;


	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
	}
	
	/** 권한대상구분코드 */
	private String authTgtTypCd;


	public String getAuthTgtTypCd() {
		return authTgtTypCd;
	}

	public void setAuthTgtTypCd(String authTgtTypCd) {
		this.authTgtTypCd = authTgtTypCd;
	}
	
	/** 권한대상UID */
	private String authTgtUid;


	public String getAuthTgtUid() {
		return authTgtUid;
	}

	public void setAuthTgtUid(String authTgtUid) {
		this.authTgtUid = authTgtUid;
	}
	
	/** 권한 포함여부 */
	private String authInclYn;


	public String getAuthInclYn() {
		return authInclYn;
	}

	public void setAuthInclYn(String authInclYn) {
		this.authInclYn = authInclYn;
	}
	
	/** 권한등급코드 */
	private String authGradCd;


	public String getAuthGradCd() {
		return authGradCd;
	}

	public void setAuthGradCd(String authGradCd) {
		this.authGradCd = authGradCd;
	}

	
	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":설문 사용 권한 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(authTgtTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("authTgtTypCd(권한대상구분코드):").append(authTgtTypCd).append('\n'); }
		if(authTgtUid!=null) { if(tab!=null) builder.append(tab); builder.append("authTgtUid(권한대상UID):").append(authTgtUid).append('\n'); }
		if(authInclYn!=null) { if(tab!=null) builder.append(tab); builder.append("authInclYn(권한 포함여부):").append(authInclYn).append('\n'); }
		if(authGradCd!=null) { if(tab!=null) builder.append(tab); builder.append("authGradCd(권한등급코드):").append(authGradCd).append('\n'); }
		super.toString(builder, tab);
	}

}