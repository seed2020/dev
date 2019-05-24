package com.innobiz.orange.web.zzz;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrjConverter {

	public static void convert(String path, String seperator, String[] removals) throws IOException {
		
		byte[] bytes = readStream(new FileInputStream(path));
		
		String text = new String(bytes, "UTF-8");
		for(String removal : removals){
			text = remove(text, seperator, removal);
		}
		
		FileOutputStream out = new FileOutputStream(path);
		out.write(text.getBytes("UTF-8"));
		out.close();
		
	}
	
	public static String remove(String text, String seperator, String removal){
		
		int p = text.indexOf(removal);
		if(p<0) return text;
		int start = text.lastIndexOf(seperator, p);
		int end  = text.indexOf(seperator, p);
		
		return text.substring(0, start) + text.substring(end);
	}
	

	public static byte[] readStream(InputStream in) throws IOException {
		if(in==null) return null;
		try {
			byte[] bytes = new byte[1024];
			int len;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while( (len=in.read(bytes,0,1024))>0 ) out.write(bytes, 0, len);
			in.close();
			return out.toByteArray();
		} finally {
			try { in.close(); }
			catch(Exception ignore){}
		}
	}
}
