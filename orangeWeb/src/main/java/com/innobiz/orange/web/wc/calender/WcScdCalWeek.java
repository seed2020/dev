package com.innobiz.orange.web.wc.calender;

import java.util.ArrayList;
import java.util.List;


public class WcScdCalWeek  {	
	private int year;
	private int month;
	private int week;
	private String solaYN;
	
	private List<WcScdCalDay> days=new ArrayList<WcScdCalDay>();
	
	
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
	public List<WcScdCalDay> getDays() {
		return days;
	}
	public void setDays(List<WcScdCalDay> days) {
		this.days = days;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	
}
