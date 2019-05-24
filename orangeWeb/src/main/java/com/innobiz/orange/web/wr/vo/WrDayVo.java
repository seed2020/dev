package com.innobiz.orange.web.wr.vo;

import java.util.List;

/** 일 */
public class WrDayVo {		
	
	private static final long serialVersionUID = -4147118666912857337L;
	
	/** 년 */
	private int year;
	/** 월 */
	private int month;
	/** 일 */
	private int day;

	/** 요일 */
	private int dayOfWeek;
	
	/**음력일자 */
	private String lunarDays;

	/** 양력일자 */
	private String days;
	
	/** 공휴일,주말여부 */
	private String isHoliDay;
	
	/** 기념일 목록 - 공휴일명(어린이날)등 */
	private List<String[]> spclDtList;

	/** 오늘여부 */
	private String todayYn;

	/** 음력,양력여부*/
	private String solaYn;
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getIsHoliDay() {
		return isHoliDay;
	}

	public void setIsHoliDay(String isHoliDay) {
		this.isHoliDay = isHoliDay;
	}
	
	public String getTodayYn() {
		return todayYn;
	}

	public void setTodayYn(String todayYn) {
		this.todayYn = todayYn;
	}

	public String getSolaYn() {
		return solaYn;
	}

	public void setSolaYn(String solaYn) {
		this.solaYn = solaYn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLunarDays() {
		return lunarDays;
	}

	public void setLunarDays(String lunarDays) {
		this.lunarDays = lunarDays;
	}

	public List<String[]> getSpclDtList() {
		return spclDtList;
	}

	public void setSpclDtList(List<String[]> spclDtList) {
		this.spclDtList = spclDtList;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(year!=0) { if(tab!=null) builder.append(tab); builder.append("year(년):").append(year).append('\n'); }
		if(month!=0) { if(tab!=null) builder.append(tab); builder.append("month(월):").append(month).append('\n'); }
		if(day!=0) { if(tab!=null) builder.append(tab); builder.append("day(일):").append(day).append('\n'); }
		if(dayOfWeek!=0) { if(tab!=null) builder.append(tab); builder.append("dayOfWeek(요일):").append(dayOfWeek).append('\n'); }
		if(lunarDays!=null) { if(tab!=null) builder.append(tab); builder.append("lunarDays(음력일자):").append(lunarDays).append('\n'); }
		if(days!=null) { if(tab!=null) builder.append(tab); builder.append("days(양력일자):").append(days).append('\n'); }
		if(isHoliDay!=null) { if(tab!=null) builder.append(tab); builder.append("isHoliDay(공휴일,주말여부):").append(isHoliDay).append('\n'); }
		if(todayYn!=null) { if(tab!=null) builder.append(tab); builder.append("todayYn(오늘여부):").append(todayYn).append('\n'); }
		if(solaYn!=null) { if(tab!=null) builder.append(tab); builder.append("solaYn(음력,양력여부):").append(solaYn).append('\n'); }
		//super.toString(builder, tab);
	}
	
	
}