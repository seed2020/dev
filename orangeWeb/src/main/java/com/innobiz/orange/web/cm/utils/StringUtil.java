package com.innobiz.orange.web.cm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/** String 관련 처리 유틸 */
public class StringUtil {
	
	/** 대문자 */
	public final static char[] UPPERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	/** 소문자 */
	public final static char[] LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	/** 숫자 */
	public final static char[] NUMBERS = "0123456789".toCharArray();
	/** 대소문자 차이 */
	private final static char DIFF = 'a' - 'A';
	
	/** 업로드 디렉토리 생성용 SimpleDateFormat */
	private final static SimpleDateFormat UPLOAD_DATE_FORMAT = new SimpleDateFormat("/yyyy/MMdd");
	
	/** 업로드 디렉토리 생성용 SimpleDateFormat */
	private final static SimpleDateFormat UPLOAD_YEAR_FORMAT = new SimpleDateFormat("/yyyy");

	/** yyyy-MM-dd 형 */
	private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/** yyyy-MM-dd HH:mm:ss 형 */
	private final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** Random */
	private final static Random RANDOM = new Random();

	/** DecimalFormat */
	private final static DecimalFormat DF = new DecimalFormat("#,###");
	
	/** Random long 리턴 */
	public static long getNextLong(){
		return RANDOM.nextLong();
	}
	
	/** Random int 리턴 */
	public static int getNextInt(){
		return RANDOM.nextInt();
	}
	
	/** Random Hexa String 리턴 */
	public static String getNextHexa(){
		return Long.toHexString(RANDOM.nextLong());
	}
	
	/** Random Hexa String 리턴 */
	public static String getNextHexa(int length){
		StringBuilder builder = new StringBuilder(length+2);
		String rand;
		int sum = 0, len;
		while(true){
			rand = Long.toHexString(RANDOM.nextLong());
			len = rand.length();
			if(sum + len >= length){
				builder.append(rand);
				return builder.substring(0, length);
			} else {
				sum += len;
				builder.append(rand);
			}
		}
	}
	
	/** Random Hexa String 리턴 */
	public static String getNextIntHexa(){
		return Integer.toHexString(RANDOM.nextInt());
	}
	
	
	
	/** Random Number 리턴 */
	public static String getRandomNumber(int pLength) {
		String result = "";
		while(result.length()<pLength){
			result+=(int)(Math.random()*10);
		}
		return result;
	}
		
	/** 3자리 콤마 형태로 변경 */
	public static String toNumber(String number){
		return toNumber(number, "");
	}
	
	/** 3자리 콤마 형태로 변경 */
	public static String toNumber(String number, String nullValue){
		if(number==null || number.isEmpty()) return nullValue;
		double no = 0;
		try {
			no = Double.parseDouble(number);
		} catch(NumberFormatException e){
			return nullValue; 
		}
		return DF.format(no);
	}
	
	/** YYYY-MM-DD 형태로 변경 */
	public static String toShortDate(String date){
		return toShortDate(date, "");
	}
	
	/** YYYY-MM-DD 형태로 변경 */
	public static String toShortDate(String date, String nullValue){
		if(date==null || date.isEmpty()) return nullValue;
		if(date.length()>10) return date.substring(0, 10);
		return date;
	}
	
	/** YYYY-MM-DD HH:MI:SS 형태로 변경 */
	public static String toLongDate(String date){
		return toLongDate(date, "");
	}

	/** YYYY-MM-DD 형태로 변경 */
	public static String toLongDate(String date, String nullValue){
		if(date==null || date.isEmpty()) return nullValue;
		if(date.length()>19) return date.substring(0, 19);
		return date;
	}

	/** HH:MI 형태로 변경 */
	public static String toHourMinute(String date){
		return toHourMinute(date, "");
	}

	/** HH:MI 형태로 변경 */
	public static String toHourMinute(String date, String nullValue){
		if(date==null || date.isEmpty()) return nullValue;
		if(date.length()>=16) return date.substring(11, 16);
		return date;
	}
	
	/** 현재 년월일(yyyy-MM-dd) 리턴 */
	public static String getCurrYmd(){
		return SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
	}
	
	/** 현재 년월일시분초(yyyy-MM-dd HH:mm:ss) 리턴 */
	public static String getCurrDateTime(){
		return DATE_TIME_FORMAT.format(new Date(System.currentTimeMillis()));
	}
	
	/** 년월일시분초(yyyy-MM-dd HH:mm:ss) 리턴 */
	public static String getDateTime(long date){
		return DATE_TIME_FORMAT.format(new Date(date));
	}
	
	/** 현재일부터 계산된 년월일 (yyyy-MM-dd) 리턴 */
	public static String getDiffYmd(int days){
		long diff = (1000L * 60 * 60 * 24) * days;
		return SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis() + diff));
	}
	
	/** 일반 URL을 권한 관리용 URL로 전환 */
	public static String toAuthUrl(String url){
		if(url.isEmpty()) return "";
		int p = url.indexOf('/', 2);
		int q = url.lastIndexOf('/');
		if(p<0 || q<0 || p>q) return null;
		return url.substring(p, q);
	}
	
	/** 파라미터 base 문자열에 파라미터 append로 들어온 문자열 중의 char 가 없으면 더해서 리턴함 */
	public static void appendNotHaveChars(StringBuilder builder, String appendString){
		if(appendString==null || appendString.isEmpty()) return;
		String base = builder.toString();
		for(char c: appendString.toCharArray()){
			if(base.indexOf(c)<0) builder.append(c);
		}
	}
	
	/** base 에서 removeString 에 속한 문자를 제거함 */
	public static String removeChars(String base, String removeString){
		if(removeString==null) return base;
		
		int i;
		char[] chars = base.toCharArray();
		for(char c: removeString.toCharArray()){
			for(i=0;i<chars.length;i++){
				if(chars[i]==c) chars[i] = 0;
			}
		}
		StringBuilder builder = new StringBuilder(chars.length+1);
		for(i=0;i<chars.length;i++){
			if(chars[i]!=0) builder.append(chars[i]);
		}
		return builder.toString();
	}
	
	/** base 에서 removeString 에 속한 문자를 제거함 */
	public static String removeChar(String base, char removeChar){
		char[] chars = base.toCharArray();
		StringBuilder builder = new StringBuilder(chars.length+1);
		for(int i=0;i<chars.length;i++){
			if(chars[i]!=removeChar) builder.append(chars[i]);
		}
		return builder.toString();
	}
	
	/** &nbsp; 리턴 */
	public static String getWebEmpty(){
		return "&nbsp;";
	}
	
	/** 업로드 디렉토리 생성용 날짜 생성 */
	public static String toUploadDirDate(){
		return UPLOAD_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
	}
	
	/** 업로드 디렉토리 생성용 날짜 생성 */
	public static String toUploadDirDate(int add){
		return UPLOAD_DATE_FORMAT.format(new Date(System.currentTimeMillis() + add));
	}
	
	/** 업로드 디렉토리 생성용 날짜 생성 */
	public static String toUploadDirYear(){
		return UPLOAD_YEAR_FORMAT.format(new Date(System.currentTimeMillis()));
	}
	
	/** 전화번호 쪼개기 */
	public static String[] splitPhone(String txt){
		if(txt==null || txt.isEmpty() || txt.equals("--") || txt.equals("-")){
			return new String[]{"","",""};
		}
		int p = txt.indexOf('-');
		int q = txt.lastIndexOf('-');
		if(p<0 || q<=p){
			txt = removeChar(txt, '-');
			txt = removeChar(txt, '+');
			txt = removeChar(txt, ' ');
			if(txt.startsWith("02")){
				p = txt.length();
				if(p<8) return new String[]{"","",""};
				return new String[]{
						txt.substring(0, 2).trim(),
						txt.substring(2,p-4).trim(),
						txt.substring(p-4).trim()
					};
			} else {
				p = txt.length();
				if(p<9) return new String[]{"","",""};
				return new String[]{
						txt.substring(0, 3).trim(),
						txt.substring(3,p-4).trim(),
						txt.substring(p-4).trim()
					};
			}
		} else {
			return new String[]{
				txt.substring(0, p).trim(),
				txt.substring(p+1,q).trim(),
				txt.substring(q+1).trim()
			};
		}
	}
	
	/** 전화번호 쪼개기 */
	public static String[] splitSsn(String txt){
		if(txt==null || txt.length()!=13) return new String[]{"", ""};
		return new String[]{txt.substring(0,6), txt.substring(6)};
	}
	
	/** 인자(str)가 NULL인 경우 빈문자열(""), 아닌 경우에는 trim()된 문자열을 구한다. */
	public static String trim(Object str) {
        if (str == null) return "";
        
		if (str instanceof Integer) {
			return Integer.toString((Integer)str).trim();
		} else if (str instanceof Long) {
			return Long.toString((Long)str).trim();
		} else if (str instanceof Double) {
			return Double.toString((Double)str).trim();
		} else if (str instanceof Float) {
			return Float.toString((Float)str).trim();
		} else if (str instanceof Boolean) {
			return Boolean.toString((Boolean)str).trim();
		}	
		
		return ((String)str).trim();
	}
	
	/** CamelNotation 으로 변환 - 언더바(_)뒤의 문자는 대문자로 나머지는 소문자로, 언더바는 제거 */
	public static String toCamelNotation(String txt, boolean isInitCap){
		boolean wasSpace = isInitCap;
		StringBuffer buffer = new StringBuffer(txt.length());
		for(char c: txt.toCharArray()){
			if(c==' ') continue;
			else if(c=='_') wasSpace = true;
			else {
				if(wasSpace){
					if(c>='a' && c<='z') c -= DIFF;
					wasSpace = false;
				} else {
					if(c>='A' && c<='Z') c += DIFF;
				}
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	
	/** CamelNotation 에서 대문자로 변환 - 언더바(_) 포함 */
	public static String fromCamelNotation(String txt){
		StringBuffer buffer = new StringBuffer(txt.length());
		for(char c: txt.toCharArray()){
			if(c>='A' && c<='Z'){
				buffer.append('_');
				buffer.append(c);
			} else if(c>='a' && c<='z'){
				c -= DIFF;
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	
	/** 날짜 더하기 */
	public static String addDate(String orginDate, int number){
		Date date = Date.valueOf(orginDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, number);
		return SIMPLE_DATE_FORMAT.format(c.getTime());
	}

	/** months 개월 이후의 일시 리턴 */
	public static String afterMonths(Integer months) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, months);
		return new Timestamp(cal.getTimeInMillis()).toString();
	}

	/** days 일 이전의 일시 리턴 */
	public static String afterDays(Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		return new Timestamp(cal.getTimeInMillis()).toString();
	}

	/** String 타입의 날짜를 받아 미래의 시간인지 리턴 */
	public static boolean isAfterNow(String strDt) {
		return new java.util.Date().before(Timestamp.valueOf(strDt));
	}

	/** 문자열을 maxLength 길이로 자른 후 리턴 */
	public static String cutString(String str, int maxLength, boolean ellipsis) {
		if (str == null) return str;
		int count = 0;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			count += (str.charAt(i) > 127 ? 2 : 1);
			if (count > maxLength) return buffer.toString() + (ellipsis ? " ..." : "");
			buffer.append(str.charAt(i));
		}
		return str;
	}
	
	/** 공백 라인 제거 */
	public static String removeEmptyLine(String text){
    	StringBuilder builder = new StringBuilder(text.length()+1);
    	boolean first = true;
    	text = text.replace("\r\n", "\n");
    	text = text.replace("\r", "\n");
    	for(String line : text.split("\n")){
    		if(!line.trim().isEmpty()){
    			if(first) first = false;
    			else builder.append("\r\n");
    			builder.append(line);
    		}
    	}
    	return builder.toString();
    }
	
	/** 날짜의 차이 계산 */
    public static Integer getDateDiff(String date1 , String date2 , String type) {
    	try{
    		String fmt = "date".equals(type) ? "yyyyMMdd" : "yyyyMMddHHmm";
    		java.util.Date dToday = new SimpleDateFormat(fmt).parse(date1);
        	Calendar todayCal = new GregorianCalendar();
        	todayCal.setTime(dToday);

        	java.util.Date dSaleStrDm = new SimpleDateFormat(fmt).parse(date2);
        	Calendar saleStrDmCal = new GregorianCalendar();
        	saleStrDmCal.setTime(dSaleStrDm);
        	     
        	long diffMillis = todayCal.getTimeInMillis() - saleStrDmCal.getTimeInMillis();
        	long diff = 0;
        	if("min".equals(type)){
	        	// 분
	        	diff = diffMillis / (60 * 1000);
        		return (int)diff;
        	}
        	if("hour".equals(type)){
	        	// 시
        		diff = diffMillis / (60 * 60 * 1000);
        		return (int)diff;
        	}
        	if("date".equals(type)){
            	// 일
            	diff = diffMillis/ (24 * 60 * 60 * 1000);
            	return (int)diff;
        	}
    	}catch(ParseException pe){
    		pe.printStackTrace();
    	}
    	
    	return null;
    }
    
    /** 문자열을 바이트로 변환 */
	public static int getBytes(String str) {
		if (str == null) return 0;
		int len = 0;
		try{
			len = str.getBytes("UTF-8").length;
		}catch (UnsupportedEncodingException e) {}
		return len;
	}
	
	/** 데이터 길이 체크 */
	public static boolean chkMaxByte(String maxByte, int bytes){
		return Integer.parseInt(maxByte) < bytes;
	}
	
	/** 날짜 long 값으로 파싱 */
	public static long parsetDateToLong(String date) throws ParseException{
		if(date==null || date.isEmpty()) return 0;
		if(date.length()==10){
			return SIMPLE_DATE_FORMAT.parse(date).getTime();
		} else if(date.length()==19){
			return DATE_TIME_FORMAT.parse(date).getTime();
		}
		return 0;
	}
	
	/** 현재 시간 입력된 시간 이후인지 체크 */
	public static boolean isAfter(String compareDate, int date, int hour, int minute) throws ParseException {
		long now = System.currentTimeMillis();
		long longDt = parsetDateToLong(compareDate);
		if(date != 0){
			longDt += hour * 24 * 60 * 60 * 1000;
		}
		if(hour!=0){
			longDt += hour * 60 * 60 * 1000;
		}
		if(minute!=0){
			longDt += minute * 60 * 1000;
		}
		return now > longDt;
	}
	
	/** Clob 를 String 으로 변경 */
	  public static String clobToString(Clob clob) throws SQLException, IOException {
	  if (clob == null) {
		  return null;
	   }

	  StringBuilder sb = new StringBuilder();
	  String str = "";
	  BufferedReader br = new BufferedReader(clob.getCharacterStream());

	  while ((str = br.readLine()) != null) {
		  sb.append(str);
	   }
	   return sb.toString();
	  }
}


