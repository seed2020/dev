package com.innobiz.orange.web.ct.calender;

import java.util.ArrayList;
import java.util.List;


public class CtScdCalWeek  {	
	private int year;
	private int month;
	private int week;
	private String solaYN;
	
	private List<CtScdCalDay> days=new ArrayList<CtScdCalDay>();
	
	
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
	public List<CtScdCalDay> getDays() {
		return days;
	}
	public void setDays(List<CtScdCalDay> days) {
		this.days = days;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	
}
