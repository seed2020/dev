package com.innobiz.orange.web.wf.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.log4j.Logger;

import com.innobiz.orange.web.cm.utils.StringUtil;

public class ReturnDataMap extends ListOrderedMap{
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ReturnDataMap.class);
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7965162668850309953L;
	
	/** yyyy-MM-dd HH:mm:ss 형 */
	private final static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
     * key 에 대하여 소문자로 변환하여 super.put
     * (ListOrderedMap) 을 호출한다.
     * @param key
     *        - '_' 가 포함된 변수명
     * @param value
     *        - 명시된 key 에 대한 값 (변경 없음)
     */
	public Object put(Object keyObj, Object valueObj) {
		String key=StringUtil.toCamelNotation((String)keyObj, false);
		String value=null;
		if(valueObj!=null){
			if (valueObj instanceof String) {
				value=String.valueOf(valueObj);
			} else if (valueObj instanceof BigDecimal) {
				value=((BigDecimal)valueObj).toString();
			} else if (valueObj instanceof Integer) {
				value=Integer.toString((Integer)valueObj);
			} else if (valueObj instanceof Long) {
				value=Long.toString((Long)valueObj);
			} else if (valueObj instanceof Double) {
				value=Double.toString((Double)valueObj);
			} else if (valueObj instanceof Float) {
				value=Float.toString((Float)valueObj);
			} else if (valueObj instanceof Boolean) {
				value=Boolean.toString((Boolean)valueObj);
			} else if(valueObj instanceof java.sql.Clob){
				try{
					value=StringUtil.clobToString((java.sql.Clob)valueObj);
				}catch(SQLException sqle){
					LOGGER.error("[ERROR] - CLOBToString ==> SQLException:key["+key+"]");
				}catch(IOException ioe){
					LOGGER.error("[ERROR] - CLOBToString ==> IOException:key["+key+"]");
				}
			} else if(valueObj instanceof java.sql.Blob){
				Blob blob = (Blob)valueObj;
				try{
					byte[] bdata = blob.getBytes(1, (int) blob.length());
					value = new String(bdata);
				}catch(SQLException sqle){
					LOGGER.error("[ERROR] - BLOBToString ==> SQLException:key["+key+"]");
				}	
			} else if(valueObj instanceof Date){
				value=DATE_TIME_FORMAT.format(valueObj);
			}
		}
		if(value==null) value=valueObj+"";
		return super.put(key, value);
	}
}
