package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 반복설정 상세(WC_REPET_SETUP_D)테이블 VO
 */
public class WcRepetSetupDVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = -1167048694496513254L;

	

	/** 일정ID */
	private String schdlId;
	
	/** 반복설정 */
	private String repetSetup;

	/** 반복주기코드 */
	private String repetPerdCd;

	/** 지정월 */
	private String apntMm;

	/** 반복월 */
	private String repetMm;

	/** 지정주 */
	private String apntWk;
	
	/** 반복주 */
	private String repetWk;

	/** 지정요일 */
	private String apntDy;
	
	/** 반복일 */
	private String repetDd;

	/** 지정일 */
	private String apntDd;
	
	/** 반복시작일시 */
	private String repetStartDt;
	
	/** 반복종료일시*/
	private String repetEndDt;
	
	

	
	public String getSchdlId() {
		return schdlId;
	}

	public void setSchdlId(String schdlId) {
		this.schdlId = schdlId;
	}
	
	public String getRepetSetup() {
		return repetSetup;
	}

	public void setRepetSetup(String repetSetup) {
		this.repetSetup = repetSetup;
	}

	public String getRepetPerdCd() {
		return repetPerdCd;
	}

	public void setRepetPerdCd(String repetPerdCd) {
		this.repetPerdCd = repetPerdCd;
	}

	public String getApntMm() {
		return apntMm;
	}

	public void setApntMm(String apntMm) {
		this.apntMm = apntMm;
	}

	

	public String getRepetMm() {
		return repetMm;
	}

	public void setRepetMm(String repetMm) {
		this.repetMm = repetMm;
	}

	public String getApntWk() {
		return apntWk;
	}

	public void setApntWk(String apntWk) {
		this.apntWk = apntWk;
	}

	public String getRepetWk() {
		return repetWk;
	}

	public void setRepetWk(String repetWk) {
		this.repetWk = repetWk;
	}

	public String getApntDy() {
		return apntDy;
	}

	public void setApntDy(String apntDy) {
		this.apntDy = apntDy;
	}

	public String getRepetDd() {
		return repetDd;
	}

	public void setRepetDd(String repetDd) {
		this.repetDd = repetDd;
	}

	public String getApntDd() {
		return apntDd;
	}

	public void setApntDd(String apntDd) {
		this.apntDd = apntDd;
	}

	public String getRepetStartDt() {
		return repetStartDt;
	}

	public void setRepetStartDt(String repetStartDt) {
		this.repetStartDt = repetStartDt;
	}

	public String getRepetEndDt() {
		return repetEndDt;
	}

	public void setRepetEndDt(String repetEndDt) {
		this.repetEndDt = repetEndDt;
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
		builder.append('[').append(this.getClass().getName()).append(":반복설정 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(repetSetup!=null) { if(tab!=null) builder.append(tab); builder.append("repetSetup(반복설정):").append(repetSetup).append('\n'); }
		if(repetPerdCd!=null) { if(tab!=null) builder.append(tab); builder.append("repetPerdCd(반복주기코드):").append(repetPerdCd).append('\n'); }
		if(apntMm!=null) { if(tab!=null) builder.append(tab); builder.append("apntMm(지정월):").append(apntMm).append('\n'); }
		if(repetMm!=null) { if(tab!=null) builder.append(tab); builder.append("repetMm(반복월):").append(repetMm).append('\n'); }
		if(apntWk!=null) { if(tab!=null) builder.append(tab); builder.append("apntWk(지정주):").append(apntWk).append('\n'); }
		if(repetWk!=null) { if(tab!=null) builder.append(tab); builder.append("repetWk(반복주):").append(repetWk).append('\n'); }
		if(apntDy!=null) { if(tab!=null) builder.append(tab); builder.append("apntDy(지정요일):").append(apntDy).append('\n'); }
		if(repetDd!=null) { if(tab!=null) builder.append(tab); builder.append("repetDd(반복일):").append(repetDd).append('\n'); }
		if(apntDd!=null) { if(tab!=null) builder.append(tab); builder.append("apntDd(지정일):").append(apntDd).append('\n'); }
		if(repetStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("repetStartDt(반복시작일시):").append(repetStartDt).append('\n'); }
		if(repetEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("repetEndDt(반복종료일시):").append(repetEndDt).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}