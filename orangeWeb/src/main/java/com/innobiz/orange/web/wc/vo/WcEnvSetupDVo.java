package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 환경 설정 상세(WC_ENV_SETUP_D)테이블 VO
 */
public class WcEnvSetupDVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = -4522672546156960678L;

	/** 회사ID */
	private String compId;

	/** 사용자UID */
	private String userUid;

	/** 페이지/포틀릿 구분코드 */
	private String pagePltTypCd;

	/** 일정구분코드 */
	private String schdlTypCd;

	/** 일정종류코드 */
	private String schdlKndCd;

	/** 양력여부 */
	private String solaLunaYn;


	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getPagePltTypCd() {
		return pagePltTypCd;
	}

	public void setPagePltTypCd(String pagePltTypCd) {
		this.pagePltTypCd = pagePltTypCd;
	}

	public String getSchdlTypCd() {
		return schdlTypCd;
	}

	public void setSchdlTypCd(String schdlTypCd) {
		this.schdlTypCd = schdlTypCd;
	}

	public String getSchdlKndCd() {
		return schdlKndCd;
	}

	public void setSchdlKndCd(String schdlKndCd) {
		this.schdlKndCd = schdlKndCd;
	}

	public String getSolaLunaYn() {
		return solaLunaYn;
	}

	public void setSolaLunaYn(String solaLunaYn) {
		this.solaLunaYn = solaLunaYn;
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
		builder.append('[').append(this.getClass().getName()).append(":환경 설정 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(pagePltTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pagePltTypCd(페이지/포틀릿 구분코드):").append(pagePltTypCd).append('\n'); }
		if(schdlTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlTypCd(일정구분코드):").append(schdlTypCd).append('\n'); }
		if(schdlKndCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlKndCd(일정종류코드):").append(schdlKndCd).append('\n'); }
		if(solaLunaYn!=null) { if(tab!=null) builder.append(tab); builder.append("solaLunaYn(양력여부):").append(solaLunaYn).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}