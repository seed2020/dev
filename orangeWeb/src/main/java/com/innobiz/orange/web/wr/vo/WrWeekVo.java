package com.innobiz.orange.web.wr.vo;

import java.util.List;

/** 주 */
public class WrWeekVo  {
	/** 년 */
	private int year;
	/** 월 */
	private int month;
	
	/** 주차 */
	private int week;
	
	/** 주의 시작일 */
	private String strtDt;
	
	/** 주의 종료일 */
	private String endDt;
	
	/** 일간정보 */
	private List<WrDayVo> wrDayVo;

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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
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

	public List<WrDayVo> getWrDayVo() {
		return wrDayVo;
	}

	public void setWrDayVo(List<WrDayVo> wrDayVo) {
		this.wrDayVo = wrDayVo;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":주]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(year!=0) { if(tab!=null) builder.append(tab); builder.append("year(년):").append(year).append('\n'); }
		if(month!=0) { if(tab!=null) builder.append(tab); builder.append("month(월):").append(month).append('\n'); }
		if(week!=0) { if(tab!=null) builder.append(tab); builder.append("week(주차):").append(week).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(주의 시작일):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(주의 종료일):").append(endDt).append('\n'); }
		//if(wrDayVo!=null) { if(tab!=null) builder.append(tab); builder.append("wrDayVo(일간정보):"); appendVoListTo(builder, wrDayVo,tab); builder.append('\n'); }
		//super.toString(builder, tab);
	}

	
}