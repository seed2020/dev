package com.innobiz.orange.web.cm.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.util.FileCopyUtils;

/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
public class LobHandler {

	private DataSource datasource = null;
	
	private String sql = null;
	private String[] params = null;
	
	public LobHandler create(String sql, String[] params) throws SQLException{
		LobHandler ins = new LobHandler();
		ins.datasource = this.datasource;
		ins.sql = sql;
		ins.params = params;
		return ins;
	}
	
	public void setDataSource(DataSource datasource){
		this.datasource = datasource;
	}
	
	public void writeFile(OutputStream out) throws SQLException, IOException{
		if(sql==null) return;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InputStream in = null;
		try {
			conn = datasource.getConnection();
			pstmt = conn.prepareStatement(sql);
			if(params != null){
				for(int i=0;i<params.length; i++){
					pstmt.setString(i+1, params[i]);
				}
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				in = rs.getBinaryStream(1);
				FileCopyUtils.copy(in, out);
			}
		} finally {
			try { if(in   !=null) in   .close(); } catch(Exception ignore){}
			try { if(rs   !=null) rs   .close(); } catch(Exception ignore){}
			try { if(pstmt!=null) pstmt.close(); } catch(Exception ignore){}
			try { if(conn !=null) conn .close(); } catch(Exception ignore){}
		}
	}

	public void writeText(Writer writer, boolean escapeScript) throws SQLException, IOException{
		if(sql==null) return;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Reader reader = null;
		try {
			conn = datasource.getConnection();
			pstmt = conn.prepareStatement(sql);
			if(params != null){
				for(int i=0;i<params.length; i++){
					pstmt.setString(i+1, params[i]);
				}
			}
			rs = pstmt.executeQuery();
			if(rs.next()){
				reader = rs.getCharacterStream(1);
				if(!escapeScript){
					FileCopyUtils.copy(reader, writer);
				} else {
					int len, p, q, textLen;
					String keep = null, text, lower;
					char[] chrs = new char[1024];
					
					while((len = reader.read(chrs, 0, 1024)) > 0){
						text = (keep==null) ? new String(chrs, 0, len) : keep + new String(chrs, 0, len);
						keep = null;
						lower = text.toLowerCase();
						q = 0;
						while((p=lower.indexOf("javascript", q))>0){
							writer.write(text, q, p-q);
							writer.write("java-script");
							q = p+10;
						}
						textLen = text.length();
						if(textLen-9 - q > 0){
							writer.write(text, q, textLen -9 -q);
							keep = text.substring(textLen-9);
						} else {
							writer.write(text, q, textLen - q);
							keep = null;
						}
					}
					if(keep!=null) writer.write(keep);
				}
			}
		} finally {
			try { if(reader!=null) reader.close(); } catch(Exception ignore){}
			try { if(rs    !=null) rs    .close(); } catch(Exception ignore){}
			try { if(pstmt !=null) pstmt .close(); } catch(Exception ignore){}
			try { if(conn  !=null) conn  .close(); } catch(Exception ignore){}
		}
	}
}
