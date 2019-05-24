package com.innobiz.orange.web.wc.calender;

import java.util.ArrayList;
import java.util.List;


public class WcScdCalMonth {	
	private int year;
	private int month;
	private String solaYN;
	private List<WcScdCalWeek> weeks=new ArrayList<WcScdCalWeek>();
	
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
	public List<WcScdCalWeek> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<WcScdCalWeek> weeks) {
		this.weeks = weeks;
	}
}
