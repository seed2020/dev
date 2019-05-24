package com.innobiz.orange.web.cm.utils;

/** ID 체번용 유틸 */
public class IdUtil {
	
	/** ID 체번용 케렉터 */
	private static char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	/** 0~9A~Z 의 스트링으로 변환 */
	public static String createId(long seq, int length){
		return createId((char)0, seq, length);
	}
	
	/** 0~9A~Z 의 스트링으로 prefix 캐렉터를 붙여서 변환 */
	public static String createId(char prefix, long seq, int length){
		
		char[] returnChars = new char[length];
		long no = seq;
		int size = CHARS.length;
		int index = length - 1;
		
		// 0~9A~Z 변환
		while(no>0){
			returnChars[index--] = CHARS[(int)(no % size)];
			no = no / size;
		}
		
		while(index>-1) returnChars[index--] = '0';
		if(prefix!=0) returnChars[0] = prefix;
		
		return String.valueOf(returnChars);
	}
	
	/** Id 를 Integer 로 변환 */
	public static int toInt(String id){
		char[] arr = id.toCharArray();
		int i, no, sum=0, digitNumber=1;
		for(i=arr.length-1;i>0;i--){
			no = (arr[i]<'A') ? arr[i] - '0' : (arr[i] - 'A') + 10;
			sum += no * digitNumber;
			digitNumber *= 36;
		}
		return sum;
	}
}
