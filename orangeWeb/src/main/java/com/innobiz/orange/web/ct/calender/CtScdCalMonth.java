package com.innobiz.orange.web.ct.calender;

import java.util.ArrayList;
import java.util.List;


public class CtScdCalMonth {	
	private int year;
	private int month;
	private String solaYN;
	private List<CtScdCalWeek> weeks=new ArrayList<CtScdCalWeek>();
	
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
	public List<CtScdCalWeek> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<CtScdCalWeek> weeks) {
		this.weeks = weeks;
	}
}
