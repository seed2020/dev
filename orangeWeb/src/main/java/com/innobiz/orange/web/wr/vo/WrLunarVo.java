package com.innobiz.orange.web.wr.vo;


/** 음력 */
public class WrLunarVo  {	
	/** 휴일배열 */
	private String[][] holidays;

	public String[][] getHolidays() {
		return holidays;
	}

	public void setHolidays(String[][] holidays) {
		this.holidays = holidays;
	}

	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":음력]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		//super.toString(builder, tab);
	}
	
}