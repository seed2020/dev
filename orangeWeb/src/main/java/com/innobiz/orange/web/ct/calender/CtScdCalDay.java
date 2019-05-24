package com.innobiz.orange.web.ct.calender;

import java.util.ArrayList;
import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;


public class CtScdCalDay  extends CommonVoImpl {	
	
	
	private static final long serialVersionUID = -4147118666912857337L;
	
	private int year;
	private int month;
	private int day;
	private int scdMaxIndex=-1;
	
	private String days;
	
	/** 일요일표시 */
	private String holiFlag;
	/** 주말표시 */
	private String dayOfTheWeek;
	/** 당일표시 */
	private String toDayFlag;

	private String solaYN;
	private List<CtSchdlBVo> scds=new ArrayList<CtSchdlBVo>();
	
	
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
	public String getSolaYN() {
		return solaYN;
	}
	public void setSolaYN(String solaYN) {
		this.solaYN = solaYN;
	}	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}	
	public List<CtSchdlBVo> getScds() {
		return scds;
	}
	public void setScds(List<CtSchdlBVo> scds) {
		this.scds = scds;
	}
	public String getHoliFlag() {
		return holiFlag;
	}
	public void setHoliFlag(String dayColor) {
		this.holiFlag = dayColor;
	}
	public String getDayOfTheWeek() {
		return dayOfTheWeek;
	}
	public void setDayOfTheWeek(String dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}
	public String getToDayFlag() {
		return toDayFlag;
	}
	public void setToDayFlag(String toDayFlag) {
		this.toDayFlag = toDayFlag;
	}
	public int getScdMaxIndex() {
		return scdMaxIndex;
	}
	public void setScdMaxIndex(int scdMaxIndex) {
		this.scdMaxIndex = scdMaxIndex;
	}
	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}
	
}
