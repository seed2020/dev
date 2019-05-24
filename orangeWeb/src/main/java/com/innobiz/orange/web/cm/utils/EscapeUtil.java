package com.innobiz.orange.web.cm.utils;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

/** escape 처리용 util */
public class EscapeUtil {

	/** input 테그의 value-attribute 에 대한 escape 문자 */
	private static final CharSequenceTranslator ESCAPE_VALUE
		= new AggregateTranslator(new LookupTranslator(new String[][] {
				{"'", "&apos;"},
				{"\"", "&quot;"},
				{"\r", "&#13;"},
				{"\n", "&#10;"}
			}));
	
	/** input 테그의 value-attribute 에 대한 escape 문자 */
	private static final CharSequenceTranslator ESCAPE_UNVALUE
		= new AggregateTranslator(new LookupTranslator(new String[][] {
				{"&apos;", "'"},
				{"&quot;", "\""},
				{"&#13;", "\r"},
				{"&#10;", "\n"}
			}));
	
	/** javascript 에서 쌍따옴표 내에 출력 될 경우에 대한 escape 문자 */
	private static final CharSequenceTranslator ESCAPE_SCRIPT
		= new AggregateTranslator(new LookupTranslator(new String[][] {
				{"'", "\\\'"},
				{"\"", "\\\""},
				{"\r", "\\r"},
				{"\n", "\\n"},
				//{"script", "scr-ipt"},
			}));
	
	/** html 에 대한 escape 문자 */
	private static final CharSequenceTranslator ESCAPE_HTML
		= new AggregateTranslator(
				new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
	            new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
	            new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()),
	            new LookupTranslator(new String[][] {
						{"\r", ""},
						{"\n", "<br/>"}
					})
				);

	/** html 에 대한 unescape 문자 */
	private static final CharSequenceTranslator ESCAPE_UNHTML
		= new AggregateTranslator(
				new LookupTranslator(EntityArrays.BASIC_UNESCAPE()),
	            new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()),
	            new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()),
	            new LookupTranslator(new String[][] {
						{"<br/>", "\r\n"}
					})
				);
	
//	private static final String[][] SCRIPT_STRINGS = {
//		{"<javascript","<java-script"},
//		{"</javascript","</java-script"},
//		{"=javascript","=java-script"},
//		{"\"javascript","\"java-script"},
//		{"\'javascript","\'java-script"},
//		{"&quot;javascript","&quot;java-script"},
//		{"&apos;javascript","&apos;java-script"},
//		{"javascript:","java-script:"},
//		
//		{"<script","<scr-ipt"},
//		{"</script","</scr-ipt"},
//		{"=script","=scr-ipt"},
//		{"\"script","\"scr-ipt"},
//		{"\'script","\'scr-ipt"},
//		{"&quot;script","&quot;scr-ipt"},
//		{"&apos;script","&apos;scr-ipt"},
//	};
//	/** HTML 에서 script 제거 */
//	public static String replaceScript(String value){
//		if(value==null || value.isEmpty()) return value;
//		String srcValue = value;
//		StringBuilder builder = new StringBuilder();
//		
//		String lower = srcValue.toLowerCase();
//		int i, p=0, q=0;
//		
//		for(i=0;i<SCRIPT_STRINGS.length;i++){
//			while((p=lower.indexOf(SCRIPT_STRINGS[i][0], q))>=0){
//				builder.append(srcValue, q, p).append(SCRIPT_STRINGS[i][1]);
//				q = p+SCRIPT_STRINGS[i][0].length();
//			}
//			if(q>=0){
//				builder.append(srcValue, q, lower.length());
//				srcValue = builder.toString();
//				builder.delete(0, builder.length());
//				lower = srcValue.toLowerCase();
//				q=0;
//			}
//		}
//		
//		return srcValue;
//	}
	/** HTML 에서 script 제거 */
	public static String replaceScript(String value){
		if(value==null || value.isEmpty()) return value;
		StringBuilder builder = new StringBuilder();
		
		String lower = value.toLowerCase();
		int p=0, q=0, len=lower.length(), prefixLen;
		
		while((p=lower.indexOf("script", q))>=0){
			
			if(p>=2 && lower.charAt(p-2)=='v' && lower.charAt(p-1)=='b'){
				prefixLen = 2;
			} else if(p>=4 && lower.charAt(p-4)=='j' && lower.charAt(p-3)=='a' && lower.charAt(p-2)=='v' && lower.charAt(p-1)=='a'){
				prefixLen = 4;
			} else {
				prefixLen = 0;
			}
			
			if(		hasNextChar(lower, p+6, len)
				||	hasScriptPrevChar(lower, p-prefixLen-1)
				||	hasScriptPrevString(lower, p-prefixLen-1)){
				
				if(prefixLen > 0){
					builder.append(value, q, p).append('-').append(value, p, p+6);
					q = p+6;
				} else {
					builder.append(value, q, p+3).append('-').append(value, p+3, p+6);
					q = p+6;
				}
			} else {
				builder.append(value, q, p+6);
				q = p+6;
			}
		}
		if(q==0) return value;
		builder.append(value, q, lower.length());
		return builder.toString();
	}
	
	private static boolean hasNextChar(String lower, int start, int len){
		for(;start<len;start++){
			if(!Character.isWhitespace(lower.charAt(start))){
				return ':' == lower.charAt(start);
			}
		}
		return false;
	}
	
	private static char[] SCRIPT_PREV_CHARS = {'<','\"','\'','='};
	private static boolean hasScriptPrevChar(String lower, int start){
		char c;
		for(;start>=0;start--){
			c = lower.charAt(start);
			if(!Character.isWhitespace(c)){
				for(char finding : SCRIPT_PREV_CHARS){
					if(c == finding) return true;
				}
				return false;
			}
		}
		return false;
	}
	

	private static char[][] SCRIPT_PREV_STRINGS = {
		{'<','/'},
		{'&','q','u','o','t',';'},//&quot;
		{'&','a','p','o','s',';'}//&apos;
	};
	private static boolean hasScriptPrevString(String lower, int start){
		int i;
		boolean matched;
		for(;start>=0;start--){
			if(!Character.isWhitespace(lower.charAt(start))){
				for(char[] prevChars : SCRIPT_PREV_STRINGS){
					if(start - prevChars.length + 1 >= 0){
						matched = true;
						for(i=0;i<prevChars.length;i++){
							if(prevChars[i] != lower.charAt(start - prevChars.length + 1 + i)){
								matched = false;
								break;
							}
						}
						if(matched) return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
	
	/** HTML 에 대한 escape */
	public static String escapeHTML(String value){
		value = replaceScript(value);
		return escapeHTML(value, "&nbsp;");
	}
	/** HTML 에 대한 escape */
	public static String escapeHTML(String value, String nullValue){
		if(value==null || value.isEmpty()) return nullValue;
		return ESCAPE_HTML.translate(value);
	}
	
	/** HTML 에 대한 unescape */
	public static String unescapeHTML(String value){
		return unescapeHTML(value, "");
	}
	
	/** HTML 에 대한 unescape */
	public static String unescapeHTML(String value, String nullValue){
		if(value==null || value.isEmpty()) return nullValue;
		return ESCAPE_UNHTML.translate(value);
	}
	
	/** input 테그의 value-attribute 에 대한 escape */
	public static String escapeValue(String value){
		if(value==null || value.isEmpty()) return "";
		return ESCAPE_VALUE.translate(value);
	}
	
	/** input 테그의 value-attribute 에 대한 unescape */
	public static String unescapeValue(String value){
		if(value==null || value.isEmpty()) return "";
		return ESCAPE_UNVALUE.translate(value);
	}
	
	/** script 내 인용할 경우 처리 escape */
	public static String escapeScript(String value){
		if(value==null || value.isEmpty()) return "";
		value = replaceScript(value);
		return ESCAPE_SCRIPT.translate(value);
	}
	
	/** TEXTAREA 에 대한 escape */
	private static final CharSequenceTranslator ESCAPE_TEXTAREA
	= new AggregateTranslator(
            new LookupTranslator(new String[][] {
            		{"&", "&amp;"},   // & - ampersand
                    {"<", "&lt;"},    // < - less-than
                    {">", "&gt;"},    // > - greater-than
				}));
	/** TEXTAREA 에 대한 escape */
	public static String escapeTextarea(String value){
		if(value==null || value.isEmpty()) return "";
		return ESCAPE_TEXTAREA.translate(value);
	}
	/** HTML 내의 특수문자 Entity가 editor 커스텀 테그에 넘어갈때 자동 치환 되어서 역치환 한번 해서 올바른 html이 전달 되도록 하는 함수 */
	public static String escapeWriteableHtml(String value){
		if(value==null || value.isEmpty()) return "";
		value = value.replace("&amp;", "&amp;amp;");
		value = value.replace("&lt;", "&amp;lt;");
		value = value.replace("&gt;", "&amp;gt;");
		return value;
	}
	/*
	private static final String TEXTAREA = "textarea";
	public static String escapeTextarea(String value){
		if(value==null || value.isEmpty()) return "";
		String lower = value.toLowerCase();
		int s=0, e=0, len = value.length();
		StringBuffer buffer = new StringBuffer(len+10);
		while((e=lower.indexOf(TEXTAREA, s))>=0){
			if(e>0) buffer.append(value.substring(s, e));
			buffer.append(value.substring(e, e+4)).append('-').append(value.substring(e+4, e+8));
			s = e+8;
		}
		if(s<len) buffer.append(value.substring(s));
		return buffer.toString();
	}*/
}
