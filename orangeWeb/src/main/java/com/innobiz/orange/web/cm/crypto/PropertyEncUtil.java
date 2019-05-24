package com.innobiz.orange.web.cm.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.innobiz.orange.web.cm.exception.CmException;

/** 암호화 프로퍼티 - 암호화 하는 유틸 */
public class PropertyEncUtil {
	
	/** 파일 읽기 */
	protected static String readFile(File file) throws IOException {
		if(file==null) return null;
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		byte[] bytes = new byte[2048];
		try{
			while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
		} finally {
			try{ in.close(); } catch(Exception ignore){}
		}
		return out.toString("UTF-8");
	}

	/** 암호화 파일에 반영 */
	protected static void encodeProperties(String applyCustCode) throws IOException, CmException {
		
		String path = "D:/dev/Workspace/orangeWeb/src/main/resources/";
		File dir = new File(path+"settings/");
		String[] fileNames = dir.list();
		String propTxt;
		License license;
		File licenseFile, propertyFile;
		
		int i;
		String[] propFileNms = {"/context-plain.properties", "/context-plain-dev.properties", "/context-plain-loc.properties", "/context-plain-run.properties"};
		String[] encFileNms = {"/context.properties", "/context-dev.properties", "/context-loc.properties", "/context-run.properties"};
		
		if(fileNames != null){
			for(String custCode : fileNames){
				
				if(custCode.indexOf('.')>=0) continue;
				if(applyCustCode!=null && !applyCustCode.equals(custCode)) continue;
				
				licenseFile = new File(path+"settings/"+custCode+"/gwOrange.license");
				if(!licenseFile.isFile()) continue;
				
				System.setProperty("CustomerCode", custCode);
				license = LicenseLoader.loadLicense(false);
				
				for(i=0; i<propFileNms.length; i++){
					propertyFile = new File(path+"settings/"+custCode+propFileNms[i]);
					if(!propertyFile.isFile()) continue;
					
					propTxt = readFile(propertyFile);
					if(propTxt!=null){
						
						try {
							//프로퍼티에서 주석 제거
							//propTxt = removeComment(propTxt);
							propTxt = license.encryptProperty(propTxt);
							storeFile(path+"settings/"+custCode+encFileNms[i], propTxt.getBytes());
							if(custCode.equals("AD8227")){
								storeFile(path+"context.properties", propTxt.getBytes());
							}
						} catch(Exception e) {
							System.out.println("License expired : "+custCode);
						}
					}
				}
				
			}
		}
	}
	
	/** 주석 제거 */
	protected static String removeComment(String propTxt){
		StringBuilder builder = new StringBuilder(512);
		BufferedReader reader = new BufferedReader(new StringReader(propTxt));
		String line;
		
		try {
			while((line = reader.readLine()) != null){
				line = line.trim();
				if(line.isEmpty() || line.charAt(0)=='#') continue;
				builder.append(line).append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	/** 암호화된 파일 저장 */
	private static void storeFile(String path, byte[] bytes) throws IOException {
		FileOutputStream out = new FileOutputStream(path);
		try{
			out.write(bytes);
		} finally {
			try{ out.close(); } catch(Exception ignore){}
		}
	}
	
	// 프로퍼티의 내용이 돌리면 메인함수 실행해서 암호화 파일에 반영해야함
	public static void main(String[] arg) throws IOException, CmException {
		encodeProperties(null);
		System.out.println("Done !");
	}
	
}
