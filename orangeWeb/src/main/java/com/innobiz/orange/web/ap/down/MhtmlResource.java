package com.innobiz.orange.web.ap.down;

import java.io.IOException;
import java.util.HashMap;

import com.innobiz.orange.web.ap.utils.Base64;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.pt.secu.CRC32;

public class MhtmlResource {

	private HashMap<Integer, String> resourceMap = new HashMap<Integer, String>();
	private HashMap<Integer, String> typeMap = new HashMap<Integer, String>();
	
	public String getResource(String url) throws IOException{
		
		try {
			Integer hash = CRC32.hash(url.getBytes("UTF-8"));
			String resource = resourceMap.get(hash);
			
			if(resource != null) return resource;
			
			HttpClient client = new HttpClient();
			byte[] bytes = client.sendGetBytes(url, null);
			
			if(bytes!=null && client.getResponseCode()==200){
				resource = Base64.encode(bytes, 19);
				if(url.indexOf("images/upload/editor")<0){
					resourceMap.put(hash, resource);
				}
				typeMap.put(hash, client.getContentType());
				return resource;
			}
		} catch(Exception ignore){}
		return null;
		
	}
	
	public String getContentType(String url) throws IOException{
		Integer hash = CRC32.hash(url.getBytes("UTF-8"));
		return typeMap.get(hash);
	}
	
	public void release(){
		resourceMap = null;
		typeMap = null;
	}
	
}
