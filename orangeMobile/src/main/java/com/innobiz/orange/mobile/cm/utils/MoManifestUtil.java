package com.innobiz.orange.mobile.cm.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/** - 사용안함 */
public class MoManifestUtil {
	
	private static List<String> PATH_LIST = new ArrayList<String>();
	private static Map<String, List<String>> RESOURCE_MAP = new HashMap<String, List<String>>();
	
	public static void log(HttpServletRequest request){
		
		String uri = request.getRequestURI();
		String referer = request.getHeader("Referer");
		
		if(uri.indexOf(".do")>0){
//System.out.println("do : "+uri);
			return;
		}
		if(referer==null || uri.equals("/")) return;
		
		String refererPath = referer;
		int p = refererPath==null ? 0 : refererPath.indexOf('/', 8);
		if(p>0){
			refererPath = referer.substring(p);
		}
		p = refererPath.indexOf('?');
		if(p>0){
			refererPath = refererPath.substring(0, p);
		}
		
		String manifestPath = refererPath.startsWith("/cm/login") ? refererPath : toManifestPath(refererPath);
		List<String> resourceList = RESOURCE_MAP.get(manifestPath);
		if(resourceList==null){
			if(PATH_LIST.contains(manifestPath)){
//				System.out.println("add path twice : "+manifestPath);
			} else {
				PATH_LIST.add(manifestPath);
			}
			resourceList = new ArrayList<String>();
			RESOURCE_MAP.put(manifestPath, resourceList);
		}
		
		if(!resourceList.contains(uri)){
			resourceList.add(uri);
		}
		if(!uri.endsWith(".css") && !uri.endsWith(".js") && uri.indexOf("images/upload")<0){
//			System.out.println("resource: "+refererPath+"\n"+uri+"\n");
		}
	}
	
	public static String toManifestPath(String uri){
		if(uri==null) return null;
		if(uri.endsWith(".css")) return uri;
		int p = uri.indexOf('?');
		if(p>0){
			uri = uri.substring(0, p);
		}
		if(uri.equals("/") || uri.equals("/index.do")){
			return "/cm/manifest/index.do";
		}
		int size = uri.length();
		if(size<4 || !uri.endsWith(".do")) return null;
		
		p = uri.lastIndexOf('/');
		if(p<0) return null;
		StringBuilder builder = new StringBuilder(128);
		builder.append("/cm/manifest").append('/');
		
		boolean appended = false;
		char c;
		for(int i=p+1;i<size;i++){
			c = uri.charAt(i);
			if(c=='.' || c=='_') break;
			if(c>='A' && c<='Z') break;
			builder.append(c);
			appended = true;
		}
		if(appended){
			builder.append(".do");
			return builder.toString();
		}
		return null;
	}
	
	public static void printManifest(){
		StringBuilder builder = new StringBuilder(1024 * 8);
		List<String> resourceList;
		for(String path : PATH_LIST){
			resourceList = RESOURCE_MAP.get(path);
			if(resourceList==null){
				System.out.println("null - path : "+path);
			} else {
				Collections.sort(resourceList);
				builder.append("\r\n\r\n").append(path).append("\r\n");
				for(String resource : resourceList){
					builder.append(resource).append("\r\n");
				}
			}
		}
		
		System.out.println(builder.toString());
	}
}
