package com.innobiz.orange.web.ap.down;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.pt.secu.CRC32;

public class HtmlResource {

	private String rootDir = null;
	private String baseDir = null;
	private String storeDir = null;
	private String domain  = null;
	
	private File imageDir = null;
	private File attachDir = null;
	
	private HashMap<Integer, String> imageMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> attachMap = new HashMap<Integer, String>();
	
	public HtmlResource(Map<String, String> prop) {
		rootDir = prop.get("rootDir");
		baseDir = prop.get("baseDir");
		storeDir = prop.get("storeDir");
		domain  = prop.get("domain");
		
		String fileType = prop.get("fileType");
		boolean withFile  = "htmlImageFile".equals(fileType);
		boolean withImage = withFile || "htmlImage".equals(fileType);

		if(withFile){
			attachDir = new File(baseDir+storeDir+"/"+"attaches");
			if(!attachDir.isDirectory()) attachDir.mkdirs();
		}
		if(withImage){
			imageDir = new File(baseDir+storeDir+"/"+"images");
			if(!imageDir.isDirectory()) imageDir.mkdirs();
			
			getImageResource("/images/blue/dot_title.png");
			getImageResource("/images/blue/dot_title_s.png");
			getImageResource("/images/blue/dot_search.png");
		}
	}
	
	public String getImageResource(String path){
		int hash = CRC32.hash(path.getBytes());
		
		String imgPath = imageMap.get(hash);
		if(imgPath!=null) return imgPath;
		
		String name = path.substring(path.lastIndexOf('/')+1);
		
		FileOutputStream out = null;
		try {
			HttpClient client = new HttpClient();
			byte[] bytes = client.sendGetBytes("http://"+domain+path, null);
			out = new FileOutputStream(new File(imageDir, name));
			out.write(bytes);
		} catch(Exception ignore){
		} finally {
			if(out!=null){
				try{ out.close(); } catch(Exception ignore2){}
			}
		}
		
		imgPath = "./images/"+name;
		imageMap.put(hash, imgPath);
		return imgPath;
	}
	
	public String getAttachResource(String path){
		int hash = CRC32.hash(path.getBytes());
		
		String attachPath = attachMap.get(hash);
		if(attachPath!=null) return attachPath;
		
		String name = path.substring(path.lastIndexOf('/')+1);
		
		FileInputStream in = null;
		FileOutputStream out = null;
		
		File inFile = new File(rootDir+path);
		if(inFile.isFile()){
			try{
				out = new FileOutputStream(new File(attachDir, name));
				out.write(readStream(new FileInputStream(inFile)));
				out.close();
			} catch(Exception ignore){
			} finally {
				if(in!=null){
					try{ in.close(); } catch(Exception ignore2){}
				}
				if(out!=null){
					try{ out.close(); } catch(Exception ignore2){}
				}
			}
		}
		
		attachPath = "./attaches/"+name;
		attachMap.put(hash, attachPath);
		return attachPath;
	}
	
	private byte[] readStream(InputStream in) throws IOException {
		int len;
		byte[] bytes = new byte[2048];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while((len = in.read(bytes, 0, 2048))>0) out.write(bytes, 0, len);
		return out.toByteArray();
	}
}
