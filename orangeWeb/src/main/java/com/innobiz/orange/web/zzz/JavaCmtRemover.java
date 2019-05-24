package com.innobiz.orange.web.zzz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class JavaCmtRemover {

	public static void main(String[] arg){
		//String path = "D:\\dev\\Workspace\\orangeWeb\\src\\main\\java";
		String path = "D:\\dev\\Workspace\\orangeMobile\\src\\main\\java";
		removeCmtByFile(new File(path));
	}
	
	public static void removeCmtByFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if(files!=null){
				for(File sub : files){
					removeCmtByFile(sub);
				}
			}
		} else if(file.isFile() && file.getName().endsWith(".java")) {
			String src = readFile(file);
			if(src != null){
				src = removeCmt(src);
				storeFile(file, src);
			}
		}
	}
	
	private static String readFile(File file){
		byte[] bytes = new byte[1024];
		int len;
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(file);
			while((len = in.read(bytes, 0, 1024)) > 0) out.write(bytes, 0, len);
			return out.toString("UTF-8");
		} catch (IOException e) {
		} finally {
			try {
				if(in!=null) in.close();
			} catch (IOException ignore) {}
		}
		return null;
	}
	
	private static void storeFile(File file, String text){
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(text.getBytes("UTF-8"));
		} catch (IOException ignore) {
		} finally {
			try {
				if(out!=null) out.close();
			} catch (IOException ignore) {}
		}
	}
	
	public static String removeCmt(String code){
		
		JavaSrcBuilder builder = new JavaSrcBuilder(code.length());
		
		boolean quotOpen = false;	// ["]
		boolean prevEscape = false;	// [\]
		boolean lnCmtOpen = false;	// [//]
		boolean cmtOpen = false;	// [/*]
		
		boolean justCmtClosed = false;
		
		char[] chars = code.toCharArray();
		int len = chars.length;
		char c;
		
		for(int i=0;i<len;i++){
			c = chars[i];
			
			if(quotOpen){
				if(prevEscape){
					prevEscape = false;
				} else if(c=='\\'){
					prevEscape = true;
				} else if(c=='\"'){
					quotOpen = false;
				}
				builder.append(c);
			} else if(prevEscape){
				prevEscape = false;
				builder.append(c);
			} else if(lnCmtOpen){
				if(c=='\r'){
					
					if(i<len-1 && chars[i+1]=='\n'){
						builder.appendIfHasChar(c, chars[i+1]);
						i++;
					} else {
						builder.appendIfHasChar(c, JavaSrcBuilder.ZERO);
					}
					
					lnCmtOpen = false;
					
				} else if(c=='\n'){
					
					builder.appendIfHasChar(c, JavaSrcBuilder.ZERO);
					
					lnCmtOpen = false;
				}
			} else if(cmtOpen) {
				if(c=='*'){
					if(i<len-1 && chars[i+1]=='/'){
						cmtOpen = false;
						i++;
						
						justCmtClosed = true;
					}
				}
			} else {
				
				if(justCmtClosed){
					
					if(c=='\r'){
						
						if(i<len-1 && chars[i+1]=='\n'){
							builder.appendIfHasChar(c, chars[i+1]);
							i++;
						} else {
							builder.appendIfHasChar(c, JavaSrcBuilder.ZERO);
						}
						
						lnCmtOpen = false;
						
					} else if(c=='\n'){
						
						builder.appendIfHasChar(c, JavaSrcBuilder.ZERO);
						
						lnCmtOpen = false;
					}
					
					justCmtClosed = false;
					if(c=='\r' || c=='\n'){
						continue;
					}
				}
				
				if(c=='\"'){
					quotOpen = true;
					builder.append(c);
				} else if(c=='\\'){
					prevEscape = true;
					builder.append(c);
				} else if(c=='/'){
					if(i<len-1 && chars[i+1]=='/'){
						lnCmtOpen = true;
						i++;
						builder.rtrimLn();
					} else if(i<len-1 && chars[i+1]=='*'){
						cmtOpen = true;
						i++;
					} else {
						builder.append(c);
					}
				} else {
					builder.append(c);
				}
			}
			
		}
		
		
		return builder.toString();
	}
}
