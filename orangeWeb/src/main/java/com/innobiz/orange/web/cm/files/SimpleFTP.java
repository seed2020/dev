package com.innobiz.orange.web.cm.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.net.ftp.FTPClient;

import com.innobiz.orange.web.cm.exception.CmException;

/** apache FTPClient 래퍼 클래스, 파일을 서버에 올리는 역할을 수행 */
public class SimpleFTP {
	
	/** apache FTPClient */
	private FTPClient ftpClient;
	
	/** 서버 FTP 기준 디렉토리 */
	private String serverBaseDir;
	
	/** 클라이언트 FTP 기준 디렉토리 */
	private String clientBaseDir;
	
	/** 로케일 - 언어어권 */
	private Locale locale;
	
	/** 생성자 */
	public SimpleFTP(String ip, String user, String password, Locale locale, boolean passiveMode) throws IOException, CmException{
		try{
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.connect(ip);
			this.locale = locale;
		} catch(IOException e) {
			//cm.msg.ftp.fail.connect=FTP 연결에 실패 하였습니다.({0})
			throw new CmException("cm.msg.ftp.fail.connect", new String[]{ip}, locale);
		}
		boolean success = ftpClient.login(user, password);
		if(!success){
			throw new CmException("cm.msg.ftp.fail.login", new String[]{ip, user}, locale);
		}
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		if(passiveMode){
			ftpClient.enterLocalPassiveMode();
		}
	}
	
	/** FTP 연결을 닫음 */
	public void close() throws IOException{
		ftpClient.quit();
	}
	
	/** 서버 및 클라이언트의 베이스 디렉토리를 설정함 */
	public void setBaseDir(String serverBaseDir, String clientBaseDir){
		this.serverBaseDir = serverBaseDir;
		this.clientBaseDir = clientBaseDir;
	}
	
	/** 파일을 전송함, 서버 및 클라이언트의 베이스 디렉토리를 기준으로 해당하는 파일을 보냄 */
	public boolean send(String path) throws IOException, CmException {
		mkServerdirs(path);
		FileInputStream in = new FileInputStream(clientBaseDir+"/"+path);
		try {
			return ftpClient.storeFile(toServerPath(path), in);
		} catch (IOException e){
			if(in!=null) {
				try { in.close(); }
				catch(IOException ignore){}
			}
			throw e;
		}
	}
	
	/** 파일을 전송함, 서버 및 클라이언트의 베이스 디렉토리를 기준으로 해당하는 파일을 보냄 */
	public boolean send(String clientPath, String serverPath) throws IOException, CmException {
		mkServerdirs(serverPath);
		FileInputStream in = new FileInputStream(clientBaseDir+"/"+clientPath);
		try {
			return ftpClient.storeFile(toServerPath(serverPath), in);
		} catch (IOException e){
			if(in!=null) {
				try { in.close(); }
				catch(IOException ignore){}
			}
			throw e;
		}
	}
	
	/** 파일을 전송함, 서버의 베이스 디렉토리를 기준으로 해당하는 파일을 보냄 */
	public boolean sendFromAbsolutePath(String clientAbsolutePath, String serverPath) throws IOException, CmException {
		mkServerdirs(serverPath);
		FileInputStream in = new FileInputStream(clientAbsolutePath);
		try {
			return ftpClient.storeFile(toServerPath(serverPath), in);
		} catch (IOException e){
			if(in!=null) {
				try { in.close(); }
				catch(IOException ignore){}
			}
			throw e;
		}
	}
	
	/** 서버의 경로를 리턴 */
	private String toServerPath(String path){
		if(serverBaseDir.endsWith("/")){
			if(path.startsWith("/")) return serverBaseDir+path.substring(1);
			return serverBaseDir+path;
		} else {
			if(path.startsWith("/")) return serverBaseDir+path;
			return serverBaseDir+"/"+path;
		}
	}
	
	/** 서버에 폴더가 없으면 만듬 */
	private void mkServerdirs(String path) throws IOException, CmException {
		String[] arr = path.split("/");
		int i, size = arr.length - 1;
		String cwd;
		boolean success;
		
		success = ftpClient.changeWorkingDirectory(serverBaseDir);
		if(!success){
			//cm.msg.ftp.fail.noSeverDir=FTP 서버에 해당 경로를 찾을 수 없습니다.({0})
			throw new CmException("cm.msg.ftp.fail.noSeverDir", new String[]{serverBaseDir}, locale);
		}
		
		ftpClient.changeWorkingDirectory(serverBaseDir);
		for(i=0; i<size;i++){
			cwd = arr[i];
			if(cwd.isEmpty()) continue;
			if(!ftpClient.changeWorkingDirectory(cwd)){
				success = ftpClient.makeDirectory(cwd);
				if(!success){
					//cm.msg.ftp.fail.createDir=FTP 서버에 해당 경로를 만들 수 없습니다.({0})
					throw new CmException("cm.msg.ftp.fail.createDir", new String[]{serverBaseDir}, locale);
				}
				ftpClient.changeWorkingDirectory(cwd);
			}
		}
	}
	
	/** 서버의 응답 값 리턴 */
	public String getReplyMessage() {
		return ftpClient.getReplyString();
	}
}
