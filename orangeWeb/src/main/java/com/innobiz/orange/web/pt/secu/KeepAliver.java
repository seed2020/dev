package com.innobiz.orange.web.pt.secu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.innobiz.orange.web.cm.utils.Hash;

public class KeepAliver {

	private static HashMap<Integer, String> NEW_KEEP_ALIVE_MAP = new HashMap<Integer, String>();
	
	private static HashMap<Integer, String> OLD_KEEP_ALIVE_MAP = null;
	
	private static boolean CHECK_KEEP_ALIVE = false;
	
	private static long START_TIME = 0;
	
	public static void initializKeepAlive() {
		
		File keepAliveFile = new File(".keepalive");
		
		if(!keepAliveFile.isFile()){
			CHECK_KEEP_ALIVE = false;
			OLD_KEEP_ALIVE_MAP = null;
			return;
		} else {
			START_TIME = System.currentTimeMillis();
			CHECK_KEEP_ALIVE = true;
			OLD_KEEP_ALIVE_MAP = new HashMap<Integer, String>();
		}
		
		boolean hasContents = false;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(keepAliveFile)));
			String line, userUid;
			Integer sessionIdHash;
			int p;
			
			while((line = reader.readLine()) != null){
				p = line.indexOf(',');
				if(p>0){
					sessionIdHash = Integer.valueOf(line.substring(0, p));
					userUid       = line.substring(p+1);
					OLD_KEEP_ALIVE_MAP.put(sessionIdHash, userUid);
					hasContents = true;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(!hasContents){
				CHECK_KEEP_ALIVE = false;
				OLD_KEEP_ALIVE_MAP = null;
			}
			try {
				if(reader!=null) reader.close();
			} catch (IOException igonre) {}
			keepAliveFile.delete();
		}
	}
	
	public static void storeKeepAlive() {
		
		File keepAliveFile = new File(".keepalive");
		
		boolean hasContents = false;
		StringBuilder builder = new StringBuilder();
		synchronized (NEW_KEEP_ALIVE_MAP) {
			Iterator<Entry<Integer, String>> iterator = NEW_KEEP_ALIVE_MAP.entrySet().iterator();
			Entry<Integer, String> entry;
			while(iterator.hasNext()){
				entry = iterator.next();
				builder.append(entry.getKey()).append(',').append(entry.getValue()).append('\n');
				hasContents = true;
			}
		}
		
		FileOutputStream out = null;
		try {
			if(hasContents){
				out = new FileOutputStream(keepAliveFile);
				out.write(builder.toString().getBytes("UTF-8"));
			} else {
				if(keepAliveFile.isFile()) keepAliveFile.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(out!=null) out.close();
			} catch (IOException igonre) {}
		}
	}
	
	public static void createKeepAlive(String sessionId, String userUid){
		synchronized (NEW_KEEP_ALIVE_MAP) {
			NEW_KEEP_ALIVE_MAP.put(Hash.hashId(sessionId), userUid);
		}
		storeKeepAlive();
	}
	
	public static void destroyKeepAlive(String sessionId){
		synchronized (NEW_KEEP_ALIVE_MAP) {
			NEW_KEEP_ALIVE_MAP.remove(Hash.hashId(sessionId));
		}
		storeKeepAlive();
	}
	
	private static String getKeepAliveUserUid(String sessionId){
		return OLD_KEEP_ALIVE_MAP.get(Hash.hashId(sessionId));
	}
	
	public static void processKeepAlive(HttpServletRequest request, HttpServletResponse response){
		
		if(!CHECK_KEEP_ALIVE) return;
		if(request.getSession().getAttribute("userVo")!=null) return;
		
		String cookieValue = getCookie(request, "ORANGE_WEB_ID");
		if(cookieValue == null) return;
		
		if(System.currentTimeMillis() - START_TIME > 60 * 60 * 1000){
			CHECK_KEEP_ALIVE = false;
			OLD_KEEP_ALIVE_MAP = null;
			return;
		}
		
		if(!cookieValue.equals(request.getSession().getId())) {
			
			String userUid = getKeepAliveUserUid(cookieValue);
			if(userUid!=null){
				try {
					SecuBridge.ins.ptLoginSvc.createUserSessionByUserUid(request, response, userUid);
//					UserVo userVo = (UserVo)request.getSession().getAttribute("userVo");
//					if(userVo!=null){
//						String ip = IpChecker.ins.getIp(request);
//						userVo.setLginIp(ip);//로그인IP
//						boolean internalIp = IpChecker.ins.isInternalIpRange(ip);// 내부망 IP 여부
//						userVo.setInternalIp(internalIp);
//						
//						// 권한 있는 모듈 참조 ID
//						userVo.setMnuGrpMdRids(SecuBridge.ins.ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
//					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String getCookie(HttpServletRequest request, String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if(cookieName.equals(cookie.getName())) return cookie.getValue();
			}
		}
		return null;
	}
}
