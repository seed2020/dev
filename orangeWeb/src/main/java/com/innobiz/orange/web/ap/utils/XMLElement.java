package com.innobiz.orange.web.ap.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.innobiz.orange.web.cm.utils.StringUtil;

public class XMLElement {
	
	private String tagName = null;
	
	private StringBuilder valueBuilder = null;
	
	private List<XMLElement> childList = null;
	
	private Map<String, String> attributes = null;
	
	public XMLElement(String tagName){
		this.tagName = tagName;
	}
	
	public void addAttr(String attrName, String attrValue){
		if(attributes==null) attributes = new LinkedHashMap<String, String>();
		attributes.put(attrName, attrValue);
	}
	
	public void addChild(XMLElement element){
		if(childList==null) childList = new ArrayList<XMLElement>();
		childList.add(element);
	}
	
	public boolean isEmptyValue(){
		return valueBuilder==null;
	}
	
	public void appendValue(char ch[], int start, int length){
		if(valueBuilder==null) valueBuilder = new StringBuilder();
		valueBuilder.append(ch, start, length);
	}
	
	public void setValue(String value){
		valueBuilder = new StringBuilder().append(value);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(256);
		appendToString(builder, 0);
		return builder.toString();
	}
	
	private void appendToString(StringBuilder builder, int tabCnt){
		if(tabCnt>0) builder.append("\r\n");
		for(int i=0;i<tabCnt;i++) builder.append('\t');
		String tagNmStr = tagName!=null ? tagName : "empty-xml";
		builder.append('<').append(tagNmStr);
		
		if(attributes!=null){
			Iterator<Entry<String, String>> it = attributes.entrySet().iterator();
			Entry<String, String> entry;
			while(it.hasNext()){
				entry = it.next();
				builder.append(' ');
				appendEscapeChars(builder, entry.getKey());
				builder.append('=').append('"');
				appendEscapeChars(builder, entry.getValue());
				builder.append('"');
			}
		}
		
		if(childList!=null){
			builder.append('>');
			
			for(XMLElement child : childList){
				child.appendToString(builder, tabCnt+1);
			}
			builder.append("\r\n");
			for(int i=0;i<tabCnt;i++) builder.append('\t');
			builder.append('<').append('/').append(tagNmStr).append('>');
		} else if(valueBuilder!=null){
			builder.append('>');
			appendEscapeChars(builder, valueBuilder.toString());
			builder.append('<').append('/').append(tagNmStr).append('>');
		} else {
			builder.append('/').append('>');
		}
	}
	
	private void appendEscapeChars(StringBuilder builder, String text){
		if(text==null || text.isEmpty()) return;
		char[] chs = text.toCharArray();
		for(char c : chs){
			if(c=='<'){
				builder.append("&lt;");
			} else if(c=='>'){
				builder.append("&gt;");
			} else if(c=='&'){
				builder.append("&amp;");
			} else if(c=='\''){
				builder.append("&apos;");
			} else if(c=='\"'){
				builder.append("&quot;");
			} else {
				builder.append(c);
			}
		}
	}
	
	public XMLElement getElem(String path){
		if(path==null) return null;
		path = path.trim();
		if(path.isEmpty()) return null;
		
		String[] arr = path.split("\\/");
		int i, size = arr.length, p, q, index;
		XMLElement element = this;
		for(i=0;i<size;i++){
			p = arr[i].indexOf('[');
			q = arr[i].indexOf(']',p+1);
			if(p>-1 && q>p){
				index = 0;
				try {
					index = Integer.parseInt(arr[i].substring(p+1, q).trim());
					arr[i] = arr[i].substring(0, p);
				} catch(Exception ignore){}
				
				element = element.find(arr[i], index);
			} else {
				element = element.find(arr[i], 0);
			}
			if(element==null) return null;
		}
		
		return element;
	}
	
	public String getValue(String path){
		if(path==null || path.isEmpty() || ".".equals(path)){
			return valueBuilder==null ? null : valueBuilder.toString();
		}
		XMLElement element = getElem(path);
		return element==null ? null : element.getValue(null);
	}
	public String getValue(String path, String type){
		String value = getValue(path);
		if(value==null || value.isEmpty()) return value;
		//TODO
		if("number".equals(type)){
			value=StringUtil.toNumber(value);
		} else if(type.endsWith("maildate")){
			if(value.length() >= 12 ){
				value=value.substring(0, 4)+"-"+value.substring(4, 6)+"-"+value.substring(6, 8)
						+" "+value.substring(8, 10)+":"+value.substring(10, 12);
			}
			else if(value.length() >= 8 ){
				value=value.substring(0, 4)+"-"+value.substring(4, 6)+"-"+value.substring(6, 8);
			} 
		} else if(type.endsWith("date")){
			String dateVal=value.replaceAll("\\D", "");
			if(dateVal.length()>=8){
				value=dateVal.substring(0, 4)+"-"+dateVal.substring(4, 6)+"-"+dateVal.substring(6, 8);
				if(type.startsWith("long") && dateVal.length()>=12){
					value+=" "+dateVal.substring(8, 10)+":"+dateVal.substring(10, 12);
				}
			}
		}
		return value;
	}
	
	public String getComma(String value){
		//숫자형태가 아닌 문자열일경우 디폴트 value 으로 반환 
        String numberExpr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$";
        if (!value.matches(numberExpr)) return value;
    
        Pattern p = Pattern.compile("(^[+-]?\\d+)(\\d{3})"); //정규표현식 
        Matcher regexMatcher = p.matcher(value); 
        
        while(regexMatcher.find()) {
        	value = regexMatcher.replaceAll("$1,$2"); //치환 : 그룹1 + "," + 그룹2
            regexMatcher.reset(value);
        }
        return value;
	} 
	
	public String getDecimalValue(String path, Integer maxFractionDigits){
		String value = getValue(path);
		if(value==null || value.isEmpty()) return value;
		
		if(maxFractionDigits!=null){
			String tmpVal=null;
			int p = value.indexOf(".");
			if(p>0 && value.substring(p+1).length()>=maxFractionDigits){
				tmpVal=value.substring(0, p);
				if(maxFractionDigits>0){
					tmpVal+=".";
					tmpVal+=value.substring(p+1, (p+maxFractionDigits)+1);
				}
				value=tmpVal;
			}
		}
		
		return getComma(value);
		
	}
	
	public String getAttr(String path){
		if(path==null) return null;
		path = path.trim();
		if(path.isEmpty()) return null;
		
		XMLElement element = this;
		String attrNm = null;
		
		int p = path.lastIndexOf('.');
		if(p<0){
			attrNm = path;
			element = this;
		} else if(p==0){
			attrNm = path.substring(1).trim();
			element = this;
		} else {
			attrNm = path.substring(p+1).trim();
			element = getElem(path.substring(0, p));
		}
		
		if(element==null || element.attributes==null) return null;
		return element.attributes.get(attrNm);
	}
	
	public List<XMLElement> getChildList(String path){
		if(path==null || path.isEmpty() || ".".equals(path)) return this.childList;
		XMLElement element = getElem(path);
		if(element==null) return null;
		return element.childList;
	}
	
	public List<XMLElement> getChildList(String path, int minCount){
		List<XMLElement> list = getChildList(path);
		if(list==null) list = new ArrayList<XMLElement>();
		for(int size = list.size(); size<minCount; size++){
			list.add(new XMLElement(null));
		}
		return list;
	}
	
	public List<XMLElement> getChildList(String path, int minCount, int extraElem){
		List<XMLElement> list = getChildList(path, minCount);
		for(int i=0;i<extraElem;i++){
			list.add(new XMLElement(null));
		}
		return list;
	}
	
	private XMLElement find(String tagName, int index){
		if(childList==null || tagName==null) return null;
		int matchIndex = 0;
		for(XMLElement child : childList){
			if(tagName.equals(child.tagName)){
				if(matchIndex==index) return child;
				else matchIndex++;
			}
		}
		return null;
	}

}
