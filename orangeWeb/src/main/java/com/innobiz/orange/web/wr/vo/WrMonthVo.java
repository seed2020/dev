package com.innobiz.orange.web.wr.vo;

import java.util.List;

/** 월 */
public class WrMonthVo {	
	/** 년 */
	private int year;
	/** 월 */
	private int month;
	
	/** 월의 시작일 (yyyy-MM-dd) */
	private String strtDt;
	
	/** 월의 종료일 (yyyy-MM-dd) */
	private String endDt;
	
	/** 월의 시작일 : 달력에서 보여지는 전월포함 시작일 (yyyy-MM-dd) */
	private String beforeStrtDt;
	
	/** 월의 종료일  : 달력에서 보여지는 익월포함 종료일 (yyyy-MM-dd) */
	private String afterEndDt;
	
	/** 주간정보 */
	private List<WrWeekVo> wrWeekVo;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<WrWeekVo> getWrWeekVo() {
		return wrWeekVo;
	}

	public void setWrWeekVo(List<WrWeekVo> wrWeekVo) {
		this.wrWeekVo = wrWeekVo;
	}

	public String getStrtDt() {
		return strtDt;
	}

	public void setStrtDt(String strtDt) {
		this.strtDt = strtDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getBeforeStrtDt() {
		return beforeStrtDt;
	}

	public void setBeforeStrtDt(String beforeStrtDt) {
		this.beforeStrtDt = beforeStrtDt;
	}

	public String getAfterEndDt() {
		return afterEndDt;
	}

	public void setAfterEndDt(String afterEndDt) {
		this.afterEndDt = afterEndDt;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":월]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(year!=0) { if(tab!=null) builder.append(tab); builder.append("year(년):").append(year).append('\n'); }
		if(month!=0) { if(tab!=null) builder.append(tab); builder.append("month(월):").append(month).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(월의 시작일 (yyyy):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(월의 종료일 (yyyy):").append(endDt).append('\n'); }
		if(beforeStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("beforeStrtDt(월의 시작일 : 달력에서 보여지는 전월포함 시작일 (yyyy):").append(beforeStrtDt).append('\n'); }
		if(afterEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("afterEndDt(월의 종료일  : 달력에서 보여지는 익월포함 종료일 (yyyy):").append(afterEndDt).append('\n'); }
		//if(wrWeekVo!=null) { if(tab!=null) builder.append(tab); builder.append("wrWeekVo(주간정보):"); appendVoListTo(builder, wrWeekVo,tab); builder.append('\n'); }
		//super.toString(builder, tab);
	}
	
	
	
}