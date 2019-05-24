package com.innobiz.orange.web.or.sync;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;

import com.innobiz.orange.web.cm.utils.StringUtil;

public class SimpleQuery implements Closeable {
	
	private Connection conn;
	
	public Map<String, String> queryMap(String sql, List<String> param) throws SQLException, IOException{
		if(sql==null || sql.isEmpty()) return null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleIterator iterator = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			int i, size = param==null ? 0 : param.size();
			for(i=0; i<size;i++){
				pstmt.setString(i+1, param.get(i));
			}
			rs = pstmt.executeQuery();
			
			iterator = new SimpleIterator(pstmt, rs);
			return iterator.next();
			
		} finally {
			if(iterator!=null){
				iterator.close();
			} else if(pstmt!=null){
				try { pstmt.close(); } catch (SQLException ignore) {}
			}
		}
	}
	
	public SimpleIterator queryIterator(String sql, List<String> param) throws SQLException  {
		if(sql==null || sql.isEmpty()) return new SimpleIterator(null, null);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			int i, size = param==null ? 0 : param.size();
			for(i=0; i<size;i++){
				pstmt.setString(i+1, param.get(i));
			}
			rs = pstmt.executeQuery();
			
			return issueIterator(pstmt, rs);
			
		} catch (SQLException e) {
			if(pstmt!=null){
				try { pstmt.close(); } catch (SQLException ignore) {}
			}
			throw e;
		}
	}
	
	public int executeUpdate(String sql, List<String> param) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			int i, size = param==null ? 0 : param.size();
			for(i=0; i<size;i++){
				pstmt.setString(i+1, param.get(i));
			}
			return pstmt.executeUpdate();
		} finally {
			if(pstmt!=null){
				try { pstmt.close(); } catch (SQLException ignore) {}
			}
		}
	}
	
	public void commit() throws SQLException {
		conn.commit();
	}
	
	public void rollback() throws SQLException {
		conn.rollback();
	}
	
	@Override
	public void close() throws IOException {
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				throw new IOException(e.getMessage());
			} finally {
				conn = null;
			}
		}
	}
	
	
	private static HashMap<String, String> propMap = new HashMap<String, String>();
	private static HashMap<String, BasicDataSource> dsMap = new HashMap<String, BasicDataSource>();
	
	private static List<SimpleIterator> iteratorList = new ArrayList<SimpleIterator>();
	
	private static SimpleIterator issueIterator(Statement stmt, ResultSet rs){
		SimpleIterator simpleIterator = new SimpleIterator(stmt, rs);
		simpleIterator.setObjInstanceNo(StringUtil.getNextInt());
		
		synchronized (iteratorList) {
			iteratorList.add(simpleIterator);
		}
		
		return simpleIterator;
	}
	
	public static void releaseIterator(SimpleIterator simpleIterator) {
		int objInstanceNo = simpleIterator.getObjInstanceNo();
		
		int i, size = iteratorList.size();
		for(i=0;i<size;i++){
			if(objInstanceNo == iteratorList.get(i).getObjInstanceNo()){
				iteratorList.remove(i);
				break;
			}
		}
	}
	
	public static void checkExpired() throws IOException {
		List<SimpleIterator> expiredList = null;
		synchronized (iteratorList) {
			int i, size = iteratorList.size();
			if(size>0){
				for(i=0;i<size;i++){
					if(iteratorList.get(i).isExpired()){
						if(expiredList==null) expiredList = new ArrayList<SimpleIterator>();
						expiredList.add(iteratorList.remove(i));
						i--;
						size--;
					}
				}
			}
		}
		if(expiredList!=null){
			int i, size = expiredList.size();
			for(i=0;i<size;i++){
				expiredList.get(i).close();
			}
		}
	}
	
	public static SimpleQuery create(String datasourceName, Map<String, String> configMap, boolean noCfgChange) throws SQLException {
		
		BasicDataSource dataSource = dsMap.get(datasourceName);
		
		boolean matched = (dataSource!=null);
		
		String propValue, newValue;
		String[] props = new String[]{"drv", "url", "usr", "pwd", "validationSql"};
		
		if(matched && !noCfgChange){
			for(String attr : props){
				propValue = propMap.get(datasourceName+"_"+attr);
				if(propValue==null){
					matched = false;
					break;
				}
				newValue = configMap.get(attr);
				if(newValue==null){
					throw new SQLException("Configuration must have Attribute \""+attr+"\" !");
				}
				if(!propValue.equals(newValue)){
					matched = false;
					break;
				}
			}
		}
		
		if(!matched){
			if(dataSource!=null) dataSource.close();
			
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(configMap.get("drv"));
			dataSource.setUsername(configMap.get("usr"));
			dataSource.setPassword(configMap.get("pwd"));
			dataSource.setUrl(configMap.get("url"));
			
			String va;
			int no;
			
			no = 5;
			try{
				va = configMap.get("maxActive");
				if(va!=null && !va.isEmpty()) no = Integer.parseInt(va);
			} catch(Exception ignore){}
			dataSource.setMaxActive(no);
			
			no = 1;
			try{
				va = configMap.get("maxIdle");
				if(va!=null && !va.isEmpty()) no = Integer.parseInt(va);
			} catch(Exception ignore){}
			dataSource.setMaxIdle(no);
			
			no = 1;
			try{
				va = configMap.get("initialSize");
				if(va!=null && !va.isEmpty()) no = Integer.parseInt(va);
			} catch(Exception ignore){}
			dataSource.setInitialSize(no);
			
			dataSource.setTestWhileIdle(true);
			dataSource.setTimeBetweenEvictionRunsMillis(600000);
			dataSource.setValidationQuery(configMap.get("validationSql"));
			
			dsMap.put(datasourceName, dataSource);
			
			for(String attr : props){
				propMap.put(datasourceName+"_"+attr, configMap.get(attr));
			}
		}
		
		SimpleQuery simple = new SimpleQuery();
		simple.conn = dataSource.getConnection();
		return simple;
	}
	
	public Integer queryCount(String sql, List<String> param) throws SQLException  {
		if(sql==null || sql.isEmpty()) return null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			int i, size = param==null ? 0 : param.size();
			for(i=0; i<size;i++){
				pstmt.setString(i+1, param.get(i));
			}
			rs = pstmt.executeQuery();
			
			Integer count=0;
			if(rs.next())
				count=rs.getInt(1);
			
			return count;
			
		} catch (SQLException e) {
			if(pstmt!=null){
				try { pstmt.close(); } catch (SQLException ignore) {}
			}
			throw e;
		}
	}
}

