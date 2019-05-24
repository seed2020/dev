package com.innobiz.orange.web.or.sync;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class SimpleIterator implements Closeable {

	private int objInstanceNo = 0;
	private long currIndex = 0;
	private long lastIndex = -1;
	
	private Statement stmt = null;
	private ResultSet rs = null;
	
	private String[] headers = null;
	private int[] types = null;
	
	private boolean nullToEmpty = false;
	
	public SimpleIterator(Statement stmt, ResultSet rs){
		this.stmt = stmt;
		this.rs = rs;
	}
	
	public int getObjInstanceNo(){
		return objInstanceNo;
	}
	public void setObjInstanceNo(int no){
		objInstanceNo = no;
	}

	public void setNullToEmpty(boolean nullToEmpty){
		this.nullToEmpty = nullToEmpty;
	}
	
	private static String iso8859 = "ISO-8859-1";
	public Map<String, String> next() throws SQLException, IOException {
		
		if(rs==null) return null;
		if(!rs.next()){
			close();
			return null;
		}
		currIndex++;
//		if(currIndex < Long.MAX_VALUE){
//			currIndex++;
//		} else {
//			lastIndex = 0;
//			currIndex = 1;
//		}
		
		if(headers==null) createHeader(rs.getMetaData());
		
		Map<String, String> returnMap = new HashMap<String, String>();
		int i, size = headers.length;
		String value;
		byte[] bytes;
		for(i=0;i<size;i++){
			if(types[i] == Types.VARCHAR){
				value = rs.getString(i+1);
			} else if(types[i] == Types.CLOB){
				value = readReader(rs.getCharacterStream(i+1));
			} else if(types[i] == Types.BLOB){
				bytes = readStream(rs.getBinaryStream(i+1));
				value = new String(bytes, iso8859);
			} else {
				value = rs.getString(i+1);
			}
			if(value!=null) returnMap.put(headers[i], value);
			else if(nullToEmpty) returnMap.put(headers[i], "");
		}
		
		return returnMap;
	}

	@Override
	public void close() throws IOException {
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException ignore) {
			} finally {
				rs = null;
			}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException ignore) {
			} finally {
				stmt = null;
			}
		}
		
		headers = null;
		types = null;
		SimpleQuery.releaseIterator(this);
	}
	
	public boolean isExpired(){
		if(currIndex>lastIndex){
			lastIndex = currIndex;
			return false;
		} else if(currIndex==lastIndex){
			lastIndex++;
			return false;
		} else {
			return true;
		}
	}
	
	private void createHeader(ResultSetMetaData meta) throws SQLException{
		int i, size = meta.getColumnCount();
		headers = new String[size];
		types = new int[size];
		for(i=0;i<size;i++) headers[i] = toCamelNotation(meta.getColumnName(i+1), false);
		
		int columnType = 0;
		for(i=0;i<size;i++){
			columnType = meta.getColumnType(i+1);
			if(columnType==Types.LONGVARCHAR || columnType==Types.LONGNVARCHAR
					|| columnType==Types.CLOB || columnType==Types.NCLOB){
				types[i] = Types.CLOB;
			} else if(columnType==Types.BINARY || columnType==Types.BLOB || columnType==Types.LONGVARBINARY){
				types[i] = Types.BLOB;
			} else {
				types[i] = Types.VARCHAR;
			}
		}
	}

	private byte[] readStream(InputStream in) throws SQLException {
		try {
			if(in==null) return null;
			byte[] bytes = new byte[1024];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while( (len=in.read(bytes,0,1024))>0 ) out.write(bytes, 0, len);
			in.close();
			return out.toByteArray();
		} catch(IOException e){
			throw new SQLException(e);
		}
	}
	
	private String readReader(Reader reader)throws SQLException {
		try {
			if(reader==null) return null;
			char[] chs = new char[1024];
			int len;
			StringBuffer buffer = new StringBuffer();
			while( (len=reader.read(chs,0,1024))>0 ) buffer.append(chs,0,len);
			reader.close();
			return buffer.toString();
		} catch(IOException e){
			throw new SQLException(e);
		}
	}
	
	private static char DIFF = 'a' - 'A';
	private String toCamelNotation(String txt, boolean isInitCap){
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

}
