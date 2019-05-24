package com.innobiz.orange.web.zzz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlCmtRemover {
	
	public static void main(String[] arg){
		String path = "D:\\dev\\Workspace\\orangeWeb\\src\\main\\resources\\sql\\oracle";
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
		} else if(file.isFile() && file.getName().endsWith(".xml")) {
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
		
		StringBuilder builder = new StringBuilder(code.length()+1);
		
		int p=0, q=0;
		while((p = code.indexOf("<!--", q))>0){
			builder.append(code, q, p);
			q = code.indexOf("-->", p+4) + 3;
		}
		builder.append(code, q+1, code.length());
		return builder.toString();
	}
}
