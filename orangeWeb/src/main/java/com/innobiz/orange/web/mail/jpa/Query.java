package com.innobiz.orange.web.mail.jpa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Query {
	
	public final static SimpleDateFormat DEFAULT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public boolean sqlLog = true;
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private String lastSql = null;
	private String dbms = null;
	
	private String logPrefix = null;
	private boolean headerCamelNotation = true;
	
	private SqlBuilder mSqlBuilder = null;
	
	private SimpleDateFormat dateFormatter = null;
	
	public Query(Connection conn, String dbms){
		this.conn = conn;
		this.dbms = dbms;
		try {
			if(conn!=null) conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String getDatabase(){
		return dbms;
	}
	
	public void setTimezone(TimeZone timezone){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(timezone);
		dateFormatter = formatter;
	}
	public SimpleDateFormat getDateFormatter(){
		return dateFormatter==null ? DEFAULT_FORMATTER : dateFormatter;
	}
	
	public List<Map<String, String>> queryList(String sql, Object[] params) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		ResultHeader header = getHeader(rs.getMetaData(), headerCamelNotation);
		Map<String, String> map;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while(rs.next()){
			map = toMap(rs, header);
			if(map!=null) list.add(map);
		}
		return list;
	}
	
	public List<String> queryStringList(String sql, Object[] params) throws SQLException, IOException{
		List<String> list = new ArrayList<String>();
		
		ResultSet rs = execute(sql, params);
		ResultHeader header = getHeader(rs.getMetaData(), headerCamelNotation);
		boolean isDate = "DATE".equals(header.types[0]) || "DATETIME".equals(header.types[0]);
		Timestamp ts;
		while(rs.next()){
			if(isDate){
				ts = rs.getTimestamp(1);
				list.add(ts==null ? null : getDateFormatter().format(ts));
			} else {
				list.add(rs.getString(1));
			}
		}
		return list;
	}
	
	public String queryString(String sql, Object[] params) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);ResultHeader header = getHeader(rs.getMetaData(), headerCamelNotation);
		boolean isDate = "DATE".equals(header.types[0]) || "DATETIME".equals(header.types[0]);
		Timestamp ts;
		if(rs.next()){
			if(isDate){
				ts = rs.getTimestamp(1);
				return ts==null ? null : getDateFormatter().format(ts);
			} else {
				return rs.getString(1);
			}
		}
		return null;
	}
	
	public Integer queryInt(String sql, Object[] params) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		if(rs.next()){
			int result = rs.getInt(1);
			return rs.wasNull() ? null : result;
		}
		return null;
	}
	
	public Long queryLong(String sql, Object[] params) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		if(rs.next()){
			long result = rs.getLong(1);
			return rs.wasNull() ? null : result;
		}
		return null;
	}

	public Map<String, String> queryMap(String sql, Object[] params) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		ResultHeader header = getHeader(rs.getMetaData(), headerCamelNotation);
		if(rs.next()){
			return toMap(rs, header);
		}
		return null;
	}
	
	public Map<String, String> queryRowMap(String sql, Object[] params) throws SQLException{
		ResultSet rs = execute(sql, params);
		Map<String, String> map = new HashMap<String, String>();
		while(rs.next()){
			map.put(rs.getString(1), rs.getString(2));
		}
		return map;
	}
	
	private ResultSet execute(String sql, Object[] params) throws SQLException {
		if(lastSql==null || !lastSql.equals(sql)){
			closePstmt();
			pstmt = conn.prepareStatement(sql);
			lastSql = sql;
		}
		if(sqlLog) logSql(sql, params);
		prepareParam(params);
		return pstmt.executeQuery();
	}
	
	public int executeUpdate(String sql) throws SQLException, IOException{
		return executeUpdate(sql, (Object[])null);
	}
	public int executeUpdate(String sql, Map<String, Object> param) throws SQLException, IOException{
		if(param==null) return executeUpdate(sql, (Object[])null);
		
		StringBuilder builder = new StringBuilder(sql.length()+1);
		List<Object> paramList = new ArrayList<Object>();
		paramToPreparedSQL(sql, param, builder, paramList);
		return executeUpdate(builder.toString(), paramList.toArray());
	}
	public int executeUpdate(String sql, Object[] params) throws SQLException {
		try{
			if(lastSql==null || !lastSql.equals(sql)){
				closePstmt();
				pstmt = conn.prepareStatement(sql);
				lastSql = sql;
			}
			if(sqlLog) logSql(sql, params);
			prepareParam(params);
			return pstmt.executeUpdate();
		} catch(SQLException e){
			logSql(sql, params);
			throw e;
		}
	}
	
	private void prepareParam(Object[] params) throws SQLException {
		int i, size = params==null ? 0 : params.length;
		Object object;
		Class<? extends Object> clazz;
		for(i=0; i<size;i++){
			object = params[i];
			clazz = object==null ? null : object.getClass();
			if(clazz == null || clazz == String.class){
				pstmt.setString(i+1, (String)object);
			} else if(clazz == byte[].class){
				pstmt.setBinaryStream(i+1, new ByteArrayInputStream((byte[])object));
			} else if(object instanceof InputStream){
				pstmt.setBinaryStream(i+1, (InputStream)object);
			} else if(object instanceof Reader){
				pstmt.setCharacterStream(i+1, (Reader)object);
			} else if(object instanceof Integer || clazz == int.class){
				pstmt.setInt(i+1, (Integer)object);
			} else if(object instanceof Long || clazz == long.class){
				pstmt.setLong(i+1, (Long)object);
			} else if(object instanceof Float || clazz == float.class){
				pstmt.setFloat(i+1, (Float)object);
			} else if(object instanceof Double || clazz == double.class){
				pstmt.setDouble(i+1, (Double)object);
			} else {
				pstmt.setString(i+1, object.toString());
			}
		}
	}
	
	private ResultHeader getHeader(ResultSetMetaData meta, boolean camelNotation) throws SQLException{
		int i, size = meta.getColumnCount();
		String[] headers = new String[size];
		String[] types = new String[size];
		for(i=0;i<size;i++){
			if(camelNotation){
				headers[i] = toCamelNotation(meta.getColumnName(i+1), false);
			} else {
				headers[i] = meta.getColumnName(i+1);
			}
			types[i] = meta.getColumnTypeName(i+1).toUpperCase();
		}
		return new ResultHeader(headers, types);
	}
	
	private Map<String, String> toMap(ResultSet rs, ResultHeader header) throws SQLException, IOException{
		Map<String, String> map = new HashMap<String, String>();
		int i, size = header.headers.length;
		String value;
		String[] types = header.types;
		String[] headers = header.headers;
		for(i=0; i<size; i++){
			if(types[i].indexOf("CHAR")>-1 || types[i].indexOf("NUMBER")>-1 || types[i].indexOf("INT")>-1){
				value = rs.getString(i+1);
				if(value!=null) map.put(headers[i], value);
			} else if(types[i].indexOf("BLOB")>-1 || "IMAGE".equals(types[i])){
				InputStream in = rs.getAsciiStream(i+1);
				value = readToString(in, "ISO8859-1");
				if(value != null) map.put(headers[i], value);
			} else if(types[i].indexOf("CLOB")>-1 || types[i].indexOf("TEXT")>-1){
				Reader reader = rs.getCharacterStream(i+1);
				value = readToString(reader);
				if(value != null) map.put(headers[i], value);
			} else if("DATE".equals(types[i]) || "DATETIME".equals(types[i])){
				Timestamp ts = rs.getTimestamp(i+1);
				value = ts==null ? null : getDateFormatter().format(ts);
				if(value!=null) map.put(headers[i], value);
			} else {
				value = rs.getString(i+1);
				if(value!=null) map.put(headers[i], value);
			}
		}
		return map;
	}
	
	
	
	public Connection getConnection(){
		return conn;
	}
	
	public void commit(){
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void rollback(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(pstmt!=null){
			try{ pstmt.close(); }
			catch(Exception ignore){}
			pstmt = null;
			lastSql = null;
		}
		if(conn!=null){
			try{ conn.close(); }
			catch(Exception ignore){}
			conn = null;
		}
	}
	
	public void closePstmt(){
		if(pstmt!=null){
			try{ pstmt.close(); }
			catch(Exception ignore){}
			pstmt = null;
			lastSql = null;
		}
	}
	
	public static byte[] readToBytes(InputStream in) throws IOException {
		try {
			if(in==null) return null;
			byte[] bytes = new byte[1024];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while( (len=in.read(bytes,0,1024))>0 ) out.write(bytes, 0, len);
			in.close();
			in = null;
			return out.toByteArray();
		} finally {
			if(in!=null){
				try{ in.close(); } catch(Exception ignore){}
			}
		}
	}
	
	public static String readToString(InputStream in, String charset) throws IOException {
		try {
			if(in==null) return null;
			byte[] bytes = new byte[1024];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while( (len=in.read(bytes,0,1024))>0 ) out.write(bytes, 0, len);
			in.close();
			in = null;
			return out.toString(charset);
		} finally {
			if(in!=null){
				try{ in.close(); } catch(Exception ignore){}
			}
		}
	}
	
	public static String readToString(Reader reader) throws IOException{
		try {
			if(reader==null) return null;
			char[] chs = new char[1024];
			int len;
			StringBuilder builder = new StringBuilder(2048);
			while( (len=reader.read(chs,0,1024))>0 ) builder.append(chs,0,len);
			reader.close();
			reader = null;
			return builder.toString();
		} finally {
			if(reader!=null){
				try{ reader.close(); } catch(Exception ignore){}
			}
		}
	}
	
	private class ResultHeader {
		public ResultHeader(String[] headers, String[] types){
			this.headers = headers;
			this.types = types;
		}
		public String[] headers;
		public String[] types;
		public Field[] attrs;
	}
	
//	public static List<Object> toParam(Object ... objects){
//		if(objects==null) return null;
//		
//		List<Object> param = new ArrayList<Object>();
//		for(Object object : objects){
//			param.add(object);
//		}
//		return param;
//	}
	
	private static char DIFF = 'a' - 'A';
	public static String toCamelNotation(String txt, boolean isInitCap){
		boolean wasSpace = isInitCap;
		StringBuilder builder = new StringBuilder(txt.length());
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
				builder.append(c);
			}
		}
		return builder.toString();
	}
	public static String deCamelNotation(String txt){
		StringBuilder builder = new StringBuilder(txt.length()+10);
		for(char c: txt.toCharArray()){
			if(c>='a' && c<='z'){
				c -= DIFF;
			} else if(c>='A' && c<='Z'){
				builder.append('_');
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	private void logSql(String sql, Object[] params){
		
		StringBuilder builder = new StringBuilder(512);
		if(logPrefix!=null) builder.append(logPrefix).append(" >> ");
		else builder.append(">> ");
		
		int s, e=0, idx=0;
		Object object;
		Class<? extends Object> clazz;
		String strValue;
		
		while((s = sql.indexOf('?', e)) > 0){
			builder.append(sql, e, s);
			
			object = params[idx++];
			
			if(object == null){
				builder.append("null");
				e = s+1;
				continue;
			}
			clazz = object.getClass();
			
			if(clazz == String.class){
				strValue = (String)object;
			} else if(clazz == byte[].class){
				strValue = "[byte array]";
			} else if(object instanceof InputStream){
				strValue = "[byte stream]";
			} else if(object instanceof Reader){
				strValue = "[char stream]";
			} else {
				strValue = object.toString();
			}
			
			if(clazz == Integer.class || clazz == Long.class || clazz == Double.class || clazz == Float.class){
				builder.append(strValue);
				builder.append("/*p*/");
			} else {
				builder.append('\'');
				builder.append(strValue.replace("'", "''"));
				builder.append("\'/*p*/");
			}
			
			e = s+1;
		}
		builder.append(sql, e, sql.length());
		
		System.out.println(builder);
	}
	
	private void paramToPreparedSQL(String sql, Map<String, Object> param, StringBuilder builder, List<Object> paramList){
		
		char[] chars = sql.toCharArray();
		char c;
		
		String paramSplitChars = " \t!<>=()\r\n+-*/";
		int s, e;
		
		for(int i=0; i<chars.length; i++){
			c = chars[i];
			if(c == ':'){
				s = i;
				e = 0;
				for(i++; i<chars.length;i++){
					if(paramSplitChars.indexOf(chars[i])>=0){
						e = i;
						i--;
						break;
					}
				}
				if(e == 0) e = chars.length;
				
				builder.append('?');
				paramList.add(param.get(sql.substring(s+1, e)));
			} else {
				builder.append(c);
			}
		}
	}
	
	public void setLogPrefix(String logPrefix){
		this.logPrefix = logPrefix;
	}
	public Query setHeaderCamelNotation(boolean headerCamelNotation){
		this.headerCamelNotation = headerCamelNotation;
		return this;
	}
	
	public SqlBuilder sqlBuilder(){
		if(mSqlBuilder==null){
			mSqlBuilder = SqlBuilder.create(this, dbms);
		}
		return mSqlBuilder;
	}
	
	public <T> T queryObject(String sql, Object[] params, Class<T> resultClass) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		ResultHeader header = getHeader(rs.getMetaData(), true);
		setHeaderAttrs(resultClass, header);
		if(rs.next()){
			return toVo(rs, header, resultClass);
		}
		return null;
	}
	
	public <T> List<T> queryObjectList(String sql, Object[] params, Class<T> resultClass) throws SQLException, IOException{
		ResultSet rs = execute(sql, params);
		ResultHeader header = getHeader(rs.getMetaData(), headerCamelNotation);
		setHeaderAttrs(resultClass, header);
		
		List<T> list = new ArrayList<T>();
		while(rs.next()){
			list.add(toVo(rs, header, resultClass));
		}
		return list;
	}
	
	private static final Class<?> BYTE_ARRAY = (new byte[]{}).getClass();
	private <T> T toVo(ResultSet rs, ResultHeader header, Class<T> resultClass) throws SQLException, IOException{
		
		T obj = null;
		try {
			obj = resultClass.newInstance();
		} catch (Exception e) {
			throw new SQLException("newInstance fail : "+resultClass.getCanonicalName());
		}
		
		int i, size = header.headers.length;
		String[] types = header.types;
		Field[] attrs = header.attrs;
		
		String value;
		Long longValue;
		Integer intValue;
		byte[] bytes;
		
		Field attr;
		Class<?> attrClass;
		
		for(i=0; i<size; i++){
			
			if(attrs[i] == null) continue;
			
			try {
				attr = attrs[i];
				attrClass = attr.getType();
				
				if(attrClass == String.class){
					value = null;
					if(types[i].indexOf("CHAR")>-1 || types[i].indexOf("NUMBER")>-1 || types[i].indexOf("INT")>-1){
						value = rs.getString(i+1);
					} else if(types[i].indexOf("BLOB")>-1 || "IMAGE".equals(types[i])){
						InputStream inputStream = rs.getAsciiStream(i+1);
						value = readToString(inputStream, "ISO8859-1");
					} else if(types[i].indexOf("CLOB")>-1 || types[i].indexOf("TEXT")>-1){
						Reader reader = rs.getCharacterStream(i+1);
						value = readToString(reader);
					} else if("DATE".equals(types[i])){
						Timestamp ts = rs.getTimestamp(i+1);
						value = ts==null ? null : getDateFormatter().format(ts);
					} else {
						value = rs.getString(i+1);
					}
					if(value!=null){
						attr.setAccessible(true);
						attr.set(obj, value);
					}
				} else if(attrClass == Long.class || attrClass == long.class){
					longValue = rs.getLong(i+1);
					if(longValue!=null){
						attr.setAccessible(true);
						attr.set(obj, longValue);
					}
				} else if(attrClass == Integer.class || attrClass == int.class){
					intValue = rs.getInt(i+1);
					if(intValue!=null){
						attr.setAccessible(true);
						attr.set(obj, intValue);
					}
				} else if(attrClass == Double.class || attrClass == double.class){
					Double doubleValue = rs.getDouble(i+1);
					if(doubleValue!=null){
						attr.setAccessible(true);
						attr.set(obj, doubleValue);
					}
				} else if(attrClass == Float.class || attrClass == float.class){
					Float floatValue = rs.getFloat(i+1);
					if(floatValue!=null){
						attr.setAccessible(true);
						attr.set(obj, floatValue);
					}
				} else if(attrClass == BYTE_ARRAY){
					bytes = readToBytes(rs.getAsciiStream(i+1));
					if(bytes!=null){
						attr.setAccessible(true);
						attr.set(obj, bytes);
					}
				} else if(attrClass == Character.class || attrClass == char.class){
					value = rs.getString(i+1);
					if(value!=null && value.length()>0){
						Character c = value.charAt(0);
						attr.setAccessible(true);
						attr.set(obj, c);
					}
				} else if(InputStream.class.isAssignableFrom(attrClass)){
					InputStream inputStream = rs.getAsciiStream(i+1);
					if(inputStream!=null){
						attr.setAccessible(true);
						attr.set(obj, inputStream);
					}
				} else if(Reader.class.isAssignableFrom(attrClass)){
					Reader reader = rs.getCharacterStream(i+1);
					if(reader!=null){
						attr.setAccessible(true);
						attr.set(obj, reader);
					}
				} else if(Timestamp.class.isAssignableFrom(attrClass)){
					Timestamp timestamp = rs.getTimestamp(i+1);
					if(timestamp!=null){
						attr.setAccessible(true);
						attr.set(obj, timestamp);
					}
				} else if(Date.class.isAssignableFrom(attrClass)){
					Date date = rs.getDate(i+1);
					if(date!=null){
						attr.setAccessible(true);
						attr.set(obj, date);
					}
				}
			} catch(Exception ignore) {}
			
		}
		return obj;
	}
	private void setHeaderAttrs(Class<?> objClass, ResultHeader header){
		int i, size = header.headers.length;
		header.attrs = new Field[size];
		for(i=0; i<size; i++){
			header.attrs[i] = getClassAttribute(objClass, header.headers[i]);
		}
	}
	
	private Field getClassAttribute(Class<?> objClass, String attrName){
		for(Field attr : objClass.getDeclaredFields()){
			if(attrName.equals(attr.getName())){
				return attr;
			}
		}
		Class<?> superClass = objClass.getSuperclass();
		if(superClass == Object.class) return null;
		return getClassAttribute(superClass, attrName);
	}
}
