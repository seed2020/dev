package com.innobiz.orange.web.wc.util;

import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.ibm.icu.util.ChineseCalendar;

/**
 * <pre>
 * * com.tistory.petulantman.calendar
 * |_ LunarCalendar.java
 * 
 * </pre>
 * 
 * @date : 2012. 4. 25. 오전 9:52:35
 * @version :
 * @author : 조완희
 */


@Component
public class LunalCalenderUtil {
	
	/** 공통 DAO */
	/*@Resource(name = "commonDao")
	private CommonDao commonDao;*/
	
	private Calendar cal;
	private ChineseCalendar cc;
	
	/** 음력일수 (2000~2050) */
	private static int[][] solarDays = new int[][]{{2000, 29}, {2001, 30}, {2002, 29}, {2003, 30}, {2004, 30}, {2005, 29}, {2006, 30}, {2007, 30}, {2008, 30}, {2009, 30}, {2010, 30}, 
		{2011, 29}, {2012, 29}, {2013, 30}, {2014, 30}, {2015, 29}, {2016, 30}, {2017, 30}, {2018, 30}, {2019, 30}, {2020, 30}, {2021, 29}, 
		{2022, 30}, {2023, 30}, {2024, 29}, {2025, 29}, {2026, 30}, {2027, 30}, {2028, 29}, {2029, 30}, {2030, 29}, {2031, 29}, {2032, 30}, 
		{2033, 30}, {2034, 29}, {2035, 30}, {2036, 30}, {2037, 30}, {2038, 29}, {2039, 29}, {2040, 29}, {2041, 30}, {2042, 30}, {2043, 30},
		{2044, 30}, {2045, 30}, {2046, 30}, {2047, 30}, {2048, 29}, {2049, 29}, {2050, 30}};
	
	public LunalCalenderUtil() {
		// default TimeZone, Locale 을 사용..
		cal = Calendar.getInstance();
		cc = new ChineseCalendar();
	}

	/**
	 * <pre>
	 * 1. 개요 : 양력(yyyyMMdd) -> 음력(yyyyMMdd)
	 * 2. 처리내용 : 양력을 음력으로 변환처리한다.
	 * </pre>
	 * 
	 * @Method Name : toLunar
	 * @date : 2012. 4. 25.
	 * @author : 조완희
	 * @history :
	
	 * 
	 * @param yyyy-mm-dd
	 * @return
	 */
	public synchronized String toLunar(String yyyymmdd) {
		if (yyyymmdd == null)
			return "";
		yyyymmdd=yyyymmdd.replaceAll("-", "");
		String date = yyyymmdd.trim();
		if (date.length() != 8) {
			if (date.length() == 4)
				date = date + "0101";
			else if (date.length() == 6)
				date = date + "01";
			else if (date.length() > 8)
				date = date.substring(0, 8);
			else
				return "";
		}

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

		cc.setTimeInMillis(cal.getTimeInMillis());

		// ChinessCalendar.YEAR 는 1~60 까지의 값만 가지고 ,

		// ChinessCalendar.EXTENDED_YEAR 는 Calendar.YEAR 값과 2637 만큼의 차이를 가집니다.
		int y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int m = cc.get(ChineseCalendar.MONTH) + 1;
		int d = cc.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		if (y < 1000)
			ret.append("0");
		else if (y < 100)
			ret.append("00");
		else if (y < 10)
			ret.append("000");
		ret.append(y);
		ret.append("-");
		if (m < 10)
			ret.append("0");
		ret.append(m);
		ret.append("-");
		if (d < 10)
			ret.append("0");
		ret.append(d);
		
		return ret.toString();
	}

	/**
	 * <pre>
	 * 1. 개요 : 음력(yyyyMMdd) -> 양력(yyyyMMdd)
	 * 2. 처리내용 : 음력을 양력으로 변환처리한다.
	 * </pre>
	 * 
	 * @Method Name : fromLunar
	 * @date : 2012. 4. 25.
	 * @author : 조완희
	 * @history :
	
	 * @param yyyymmdd
	 * @return
	 */
	public synchronized String fromLunar(String yyyymmdd) {
		if (yyyymmdd == null)
			return "";
		yyyymmdd=yyyymmdd.replaceAll("-", "");
		String date = yyyymmdd.trim();

		if (date.length() != 8) {
			if (date.length() == 4)
				date = date + "0101";
			else if (date.length() == 6)
				date = date + "01";
			else if (date.length() > 8)
				date = date.substring(0, 8);
			else
				return "";
		}

		cc.set(ChineseCalendar.EXTENDED_YEAR,
				Integer.parseInt(date.substring(0, 4)) + 2637);
		cc.set(ChineseCalendar.MONTH,
				Integer.parseInt(date.substring(4, 6)) - 1);
		cc.set(ChineseCalendar.DAY_OF_MONTH,
				Integer.parseInt(date.substring(6)));

		cal.setTimeInMillis(cc.getTimeInMillis());

		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);

		StringBuffer ret = new StringBuffer();
		if (y < 1000)
			ret.append("0");
		else if (y < 100)
			ret.append("00");
		else if (y < 10)
			ret.append("000");
		ret.append(y);
		ret.append("-");
		if (m < 10)
			ret.append("0");
		ret.append(m);
		ret.append("-");
		if (d < 10)
			ret.append("0");
		ret.append(d);

		return ret.toString();
	}
	
	/** 음력일자 체크 */
	public static boolean isSolarChk(int year){
		for(int[] days : solarDays){
			if(year==days[0]){
				return days[1]==30;
			}
		}
		return true;
	}
	
	/** 음력->양력[DB조회]*/
	  /*public synchronized String toSolarTmp(String sDate) throws SQLException{
	        String dateStr = sDate.replaceAll("-", "");

	        int solarYear = Integer.parseInt(dateStr.substring(0,4));
	        int solarMonth = Integer.parseInt(dateStr.substring(4,6));
	        int solarDay = Integer.parseInt(dateStr.substring(6,8));
	        if(solarMonth==12 && solarDay == 30 && !isSolarChk(solarYear)){
	        	solarDay=solarDay-1;
	        }
	        
	        WcLunarSolarLVo wcLunarSolarLVo = new WcLunarSolarLVo();
	        wcLunarSolarLVo.setLunarYmd(solarYear+"-"+(solarMonth<10 ? "0"+solarMonth : solarMonth)+"-"+(solarDay<10 ? "0"+solarDay : solarDay));
	        wcLunarSolarLVo.setLeapMonthYn("N"); // 윤달여부
			wcLunarSolarLVo = (WcLunarSolarLVo)commonDao.queryVo(wcLunarSolarLVo);
			if(wcLunarSolarLVo==null) {
				return toSolarTmp(sDate);
			}
	        return wcLunarSolarLVo.getSolarYmd();
	    }*/

	  
	  public synchronized String toSolar(String sDate) {
	        String dateStr = sDate.replaceAll("-", "");

	        Calendar cal ;
	        ChineseCalendar lcal ;
	        
	        cal = Calendar.getInstance() ;
	        lcal = new ChineseCalendar();

	        int solarYear = Integer.parseInt(dateStr.substring(0,4));
	        int solarMonth = Integer.parseInt(dateStr.substring(4,6));
	        int solarDay = Integer.parseInt(dateStr.substring(6,8));
	        if(solarMonth==12 && solarDay == 30 && !isSolarChk(solarYear)){
	        	solarDay=solarDay-1;
	        }
	        lcal.set(ChineseCalendar.EXTENDED_YEAR, solarYear + 2637);
	        lcal.set(ChineseCalendar.MONTH        , solarMonth - 1);
	        lcal.set(ChineseCalendar.DAY_OF_MONTH , solarDay);
	        
	        int leap_yn = 0;
	        //int leap_year = cal.get(Calendar.YEAR);
	        //if((leap_year % 4 == 0 && leap_year % 100 != 0) || leap_year % 400 == 0)
	        //	leap_yn = 1;
	        
	        lcal.set(ChineseCalendar.IS_LEAP_MONTH, leap_yn);

	        cal.setTimeInMillis(lcal.getTimeInMillis());

	        String year  = String.valueOf(cal.get(Calendar.YEAR        )    );
	        String month = String.valueOf(cal.get(Calendar.MONTH       ) + 1);
	        String day   = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)    );

	        String pad4Str = "0000";
	        String pad2Str = "00";

	        String retYear  = (pad4Str + year ).substring(year .length()) + "-";
	        String retMonth = (pad2Str + month).substring(month.length()) + "-";
	        String retDay   = (pad2Str + day  ).substring(day  .length());

	        return retYear+retMonth+retDay;
	    }

		
		
//	public static void main(String args[]) {
//		LunalCalenderUtil lc = new LunalCalenderUtil();
//
//		System.out.println("2012년 4월 25일에 대한 음력날짜는 아래와 같습니다.");
//		System.out.println(lc.toLunar("2014-11-06")); // 양력을 음력으로 바꾸기
//
//		System.out.println("2012년 4월 05일에 대한 양력날짜는 아래와 같습니다.");
//		System.out.println(lc.fromLunar("2014-04-05")); // 2012년 4월 25일에 대한 음력날짜를
//														// 집어넣는다.
//	} // end class
}