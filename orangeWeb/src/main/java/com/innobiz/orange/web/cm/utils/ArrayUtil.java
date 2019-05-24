package com.innobiz.orange.web.cm.utils;

import java.util.ArrayList;
import java.util.List;

/** 배열 또는 배열 형태의 데이터 처리용 UTIL */
public class ArrayUtil {

	/** List<String> 을 String[]로 변환 */
	public static String[] toArray(List<String> list){
		if(list==null) return null;
		int i, size = list.size();
		String[] arr = new String[size];
		for(i=0;i<size;i++) arr[i] = list.get(i);
		return arr;
	}
	
	/** List<String> 을 String[] 역순으로 변환 */
	public static String[] toReversedArray(List<String> list){
		if(list==null) return null;
		int i, size = list.size();
		String[] arr = new String[size];
		for(i=0;i<size;i++){
			arr[i] = list.get(size - 1 -i);
		}
		return arr;
	}
	
	/** List<String[]> 을 String[][]로 변환 */
	public static String[][] to2Array(List<String[]> list){
		if(list==null) return null;
		int i, size = list.size();
		String[][] arr = new String[size][];
		for(i=0;i<size;i++) arr[i] = list.get(i);
		return arr;
	}
	
	/** 배열을 역순으로 */
	public static void reverse(char[] arr){
		char[] tmpArr = arr.clone();
		int i, len = arr.length;
		for(i=0;i<len;i++) arr[i] = tmpArr[len-i-1];
	}
	
	/** 배열을 역순으로 */
	public static void reverse(String[] arr){
		int i, len = arr.length;
		String[] tmpArr = arr.clone();
		for(i=0;i<len;i++) arr[i] = tmpArr[len-i-1];
	}
	
	/** 배열에 해당 값이 있는지 여부 리턴 */
	public static boolean isInArray(String[] arr, String finding){
		if(finding==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(finding.equals(arr[i])) return true;
		}
		return false;
	}
	
	/** 배열에 해당 값이 있는지 여부 리턴 */
	public static boolean isIn2Array(String[][] arr, int coefficient, String finding){
		if(finding==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(arr[i].length<coefficient) continue;
			if(finding.equals(arr[i][coefficient])) return true;
		}
		return false;
	}
	
	/** 배열에 해당 값이 있는지 여부 리턴 - startsWith */
	public static boolean isStartsWithArray(String[] arr, String finding){
		if(finding==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(finding.startsWith(arr[i])) return true;
		}
		return false;
	}
	
	/** 배열에 해당 값이 있는지 여부 리턴 - endsWith */
	public static boolean isEndsWithArray(String[] arr, String finding){
		if(finding==null || arr==null) return false;
		for(int i=0;i<arr.length;i++){
			if(finding.endsWith(arr[i])) return true;
		}
		return false;
	}
	
	/** 파라미터 text 를 seperator 로 잘라서 List에 담아 리턴함 */
	public static List<String> toList(String text, String seperator, boolean widthEmpty){
		ArrayList<String> list = new ArrayList<String>();
		if(text==null) return list;
		int p=0, q=0, len = seperator.length();
		while((p=text.indexOf(seperator, q))>=0){
			if(widthEmpty || p>q){
				list.add(text.substring(q, p));
			}
			q = p+len;
		}
		if(widthEmpty || q<len){
			list.add(text.substring(q));
		}
		return list;
	}
	
	/** 파라미터 array 를 List<String> 로 변환 리턴함 */
	public static List<String> toList(String[] array, boolean removeEmpty){
		ArrayList<String> list = new ArrayList<String>();
		if(array==null || array.length==0) return null;
		for(String txt : array){
			if(removeEmpty){
				if(txt!=null && !txt.isEmpty()){
					list.add(txt);
				}
			} else {
				list.add(txt);
			}
		}
		return list.isEmpty() ? null : list;
	}

	/** StringBuilder 에 String[] 정보를 더함 - 출력용 */
	public static void appendArrayTo(StringBuilder builder, String[] arr) {
		int i, size = arr==null ? 0 : arr.length;
		if(size>0){
			builder.append('[');
			for(i=0;i<size;i++){
				if(i>0) builder.append(',').append(' ');
				builder.append(arr[i]);
			}
			builder.append(']');
		}
	}

	/** StringBuilder 에 String[][] 정보를 더함 - 출력용 */
	public static void appendArrayTo(StringBuilder builder, String[][] arr) {
		int i, j, size = arr==null ? 0 : arr.length, jsize;
		String[] inArr;
		if(size>0){
			builder.append('[');
			for(i=0;i<size;i++){
				inArr = arr[i];
				jsize = inArr==null ? 0 : inArr.length;
				if(jsize>0){
					builder.append('[');
					for(j=0;j<jsize;j++){
						if(j>0) builder.append(',').append(' ');
						builder.append(inArr[j]);
					}
					builder.append(']');
				}
			}
			builder.append(']');
		}
	}
	
	/** 배열을 String 으로 변환 - 로그, 출력 용 */
	public static String toString(String[] arr){
		StringBuilder builder = new StringBuilder(128);
		appendArrayTo(builder, arr);
		return builder.toString();
	}
	/** 배열을 String 으로 변환 - 로그, 출력 용 */
	public static String toString(String[][] arr){
		StringBuilder builder = new StringBuilder(128);
		appendArrayTo(builder, arr);
		return builder.toString();
	}
}
