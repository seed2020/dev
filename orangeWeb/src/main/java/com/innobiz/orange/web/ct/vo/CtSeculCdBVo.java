package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_SECUL_CD_B [회원 등급 코드 기본] */
public class CtSeculCdBVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1313749459204656813L;
	
	/** 회사 ID */
	
	private String compId;


	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}


	/** 사용자 보안 등급 코드 */
	
	private String userSeculCd;
	
	public String getUserSeculCd() {
		return userSeculCd;
	}

	public void setUserSeculCd(String userSeculCd) {
		this.userSeculCd = userSeculCd;
	}

	
	/** 사용자 보안 등급 코드 제목 리소스 ID */
	
	private String userSeculCdSubjRescId;
	
	public String getUserSeculCdSubjRescId() {
		return userSeculCdSubjRescId;
	}

	public void setUserSeculCdSubjRescId(String userSeculCdSubjRescId) {
		this.userSeculCdSubjRescId = userSeculCdSubjRescId;
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
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사 ID):").append(compId).append('\n'); }
		if(userSeculCd!=null) { if(tab!=null) builder.append(tab); builder.append("userSeculCd(사용자 보안 등급 코드):").append(userSeculCd).append('\n'); }
		if(userSeculCdSubjRescId!=null) { if(tab!=null) builder.append(tab); builder.append("userSeculCdSubjRescId(사용자 보안 등급 코드 제목 리소스 ID):").append(userSeculCdSubjRescId).append('\n'); }
		super.toString(builder, tab);
	}

}