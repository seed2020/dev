package com.innobiz.orange.web.cm.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

	private int responseCode = 0;
	private String contentType = null;
	
	public String sendGet(String url, String charset) throws IOException{
		return sendGet(url, null, charset);
	}
	
	public String sendGet(String url, Map<String, String> header, String charset) throws IOException{
		
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		InputStream in = null;
		
		con.setRequestMethod("GET");
		
		// set header
		Entry<String, String> entry;
		if(header!=null){
			Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		responseCode = con.getResponseCode();
		
		try {
			in = con.getInputStream();
			return readStreamAsString(in, charset);
		} finally {
			try{
				if(in!=null) in.close();
			} catch(IOException ignore){}
		}
	}
	
	public byte[] sendGetBytes(String url, Map<String, String> header) throws IOException{
		
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		InputStream in = null;
		
		con.setRequestMethod("GET");
		
		// set header
		Entry<String, String> entry;
		if(header!=null){
			Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		con.setDoOutput(true);
		con.setUseCaches(false);
		
		responseCode = con.getResponseCode();
		contentType = con.getHeaderField("Content-Type");
		
		try {
			in = con.getInputStream();
			return readStream(in);
		} finally {
			try{
				if(in!=null) in.close();
			} catch(IOException ignore){}
		}
	}
	
	public String sendPost(String url, Map<String, String> param, String charset) throws IOException{
		return sendPost(url, param, null, charset);
	}
	
	public String sendPost(String url, Map<String, String> param, Map<String, String> header, String charset) throws IOException{
		
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		InputStream in = null;
		
		con.setRequestMethod("POST");
 
		// set header
		Entry<String, String> entry;
		if(header!=null){
			Iterator<Entry<String, String>> iterator = header.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		StringBuilder paramBuilder = new StringBuilder(256);
		if(param!=null){
			Iterator<Entry<String, String>> iterator = param.entrySet().iterator();
			boolean first = true;
			while(iterator.hasNext()){
				entry = iterator.next();
				if(first) first = false;
				else paramBuilder.append('&');
				paramBuilder.append(entry.getKey());
				paramBuilder.append('=');
				paramBuilder.append(URLEncoder.encode(entry.getValue()==null ? "" : entry.getValue(), charset));
			}
		}
		
		// Send post request
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);
		
		DataOutputStream dataWriter = new DataOutputStream(con.getOutputStream());
		dataWriter.writeBytes(paramBuilder.toString());
		dataWriter.flush();
		dataWriter.close();
		
		responseCode = con.getResponseCode();
		
		try {
			in = con.getInputStream();
			return readStreamAsString(in, charset);
		} finally {
			try{
				if(in!=null) in.close();
			} catch(IOException ignore){}
		}
	}
	
	public int getResponseCode(){
		return responseCode;
	}
	public String getContentType(){
		return contentType;
	}
	
	private String readStreamAsString(InputStream in, String charset) throws IOException{
		int len;
		byte[] bytes = new byte[2048];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
		return out.toString(charset);
	}
	
	private byte[] readStream(InputStream in) throws IOException{
		int len;
		byte[] bytes = new byte[2048];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
		return out.toByteArray();
	}
}
