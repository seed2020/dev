package com.innobiz.orange.web.pt.ip;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

// ip 대역대 참조 - http://www.nirsoft.net/countryip/

/** 중국, 해외 IP 차단 정책에 따른 요청에 차단 및 딜레이 적용 */
@Repository
public class ForeignIpBlocker {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ForeignIpBlocker.class);

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 국내 IP 체커 */
	@Autowired
	private DomesticIpChecker domesticIpChecker;
	
	/** 중국 IP 체커 */
	@Autowired
	private ForeignIpChecker foreignIpChecker;
	
	/** IP 별 로그인 페이지 딜레이 수 맵 */
	private HashMap<Integer, Integer> viewLoginDelayMap = new HashMap<Integer, Integer>();

	/** 시큐어세션 딜레이 수 맵 */
	private HashMap<Integer, Integer> secureSessionDelayMap = new HashMap<Integer, Integer>();
	
	/** 정책에 따른 로그인 화면 딜레이 적용 */
	public boolean delayViewLogin(HttpServletRequest request) throws SQLException{
		return delayRequest(request, viewLoginDelayMap, "viewLogin");
	}
	
	/** 정책에 따른 시큐어세션 딜레이 적용 */
	public boolean delaySecureSession(HttpServletRequest request) throws SQLException{
		return delayRequest(request, secureSessionDelayMap, "createSecuSessionAjx");
	}
	
	/** 정책에 따른 로그인 화면 딜레이 적용 */
	public boolean delayFailLogin(HttpServletRequest request) throws SQLException{
		return delayRequest(request, null, "processLogin");
	}
	
	/** 해외 IP 로그인 Fail 응답 딜레이 */
	public void delayFailResponse(HttpServletRequest request) throws SQLException{
		
		// 정책에 따라 막아야 할 IP 인지 조회
		
		// 중국 IP
		String country = "chinese";
		String blockingIp = getBlockingIp(request, country, true, true);
		if(blockingIp==null){
			// 해외 IP
			country = "foreign";
			blockingIp = getBlockingIp(request, country, true, true);
		}
		
		if(blockingIp==null) return;
		if(blockingIp.startsWith("192.168.")) return;
		if(blockingIp.equals("127.0.0.1")) return;
		
		if(blockingIp!=null){
			try {
				Thread.sleep(1000);
			} catch(Exception ignore){}
		}
	}
	
	/** 정책에 따른 중국 IP 인지 검사 - 중국 IP 면 IP 리턴 */
	private String getBlockingIp(HttpServletRequest request, String country, boolean isBlocking, boolean isDelay) throws SQLException{
		// 해외 IP 정책 조회
		Map<String, String> foreignIpPloc = ptSysSvc.getForeignIpBlockingPloc(ServerConfig.IS_MOBILE);
		
		String blockingIp = null, blockingOpt = foreignIpPloc.get(country);
		boolean needCheck = (isBlocking && "block".equals(blockingOpt))
				|| (isDelay && "delay".equals(blockingOpt));
		
		// IP에 속해 있는지
		if(needCheck){
			if("chinese".equals(country)){
				// 중국 IP에 속해 있는지
				blockingIp = foreignIpChecker.getInRangeIp(request);
			} else {
				// 국내 IP에 속해 있지 않은지
				blockingIp = domesticIpChecker.getOutOfRangeIp(request);
			}
		}
		return blockingIp;
	}
	
	/** 딜레이 적용 */
	private boolean delayRequest(HttpServletRequest request, HashMap<Integer, Integer> delayMap, String where) throws SQLException{
		

		// 중국 IP
		String country = "chinese";
		String blockingIp = getBlockingIp(request, country, true, true);
		if(blockingIp==null){
			// 해외 IP
			country = "foreign";
			blockingIp = getBlockingIp(request, country, true, true);
		}
		
		// 관련 없는 IP 리턴함
		if(blockingIp==null) return true;
		if(blockingIp.startsWith("192.168.")) return true;
		if(blockingIp.equals("127.0.0.1")) return true;
		
		boolean blockIp = false;
		Integer hashedIp = hashIp(blockingIp);
		Integer delayCount = 0;
		
		// 해당 IP의 딜레이 카운트 증가
		if(delayMap!=null){
			synchronized (delayMap) {
				delayCount = delayMap.get(hashedIp);
				delayCount = (delayCount==null) ? 1 : delayCount+1;
				delayMap.put(hashedIp, delayCount);
			}
		}
		
		if(delayCount<5){
			try{
				Thread.sleep(1000);
			} catch(Exception ignore){}
		} else if(delayCount<10){
			try{
				Thread.sleep(2000);
				LOGGER.warn("[FOREIGN IP] DELAY(2sec) - "+country+"/"+where+" - ip:"+blockingIp+"  queue count : "+delayCount);
			} catch(Exception ignore){}
		} else {
			try{
				Thread.sleep(2000);
				LOGGER.warn("[FOREIGN IP] RETRY - "+country+"/"+where+" - ip:"+blockingIp+"  queue count : "+delayCount);
			} catch(Exception ignore){}
			blockIp = true;
		}
		
		// 해당 IP의 딜레이 카운트 증가
		if(delayMap!=null){
			synchronized (delayMap) {
				delayCount = delayMap.get(hashedIp);
				if(delayCount != null){
					delayCount--;
					if(delayCount<=0){
						delayMap.remove(hashedIp);
					} else {
						delayMap.put(hashedIp, delayCount);
					}
				}
			}
		}
		
		if(blockIp) return false;
		
		return true;
	}
	
	/** IP를 해쉬함 */
	public static int hashIp(String ip){
		String[] ips = ip.split("\\.");
		if(ips.length==4){
			int p = (Integer.parseInt(ips[0])<<24)
					| (Integer.parseInt(ips[1])<<16)
					| (Integer.parseInt(ips[2])<<8)
					| Integer.parseInt(ips[3]);
			return p;
		} else {
			int hashValue = 0;
			for(char c : ip.toCharArray()){
				hashValue = c + (hashValue << 6) + (hashValue << 16) - hashValue;
			}
			return hashValue;
		}
	}

	/** 로그인을 막아야 하는지 여부 체크 */
	public boolean isLoginNoBlockByLginId(HttpServletRequest request, String lginId) throws SQLException {
		return isLoginBlockOk(request, lginId, null);
	}

	/** 로그인을 막아야 하는지 여부 체크 */
	public boolean isLoginNoBlockByMsgId(HttpServletRequest request, String msgId) throws SQLException {
		return isLoginBlockOk(request, null, msgId);
	}
	
	private boolean isLoginBlockOk(HttpServletRequest request, String lginId, String msgId) throws SQLException {
		
		// 정책에 따라 막아야 할 IP 인지 조회
		
		// 중국 IP
		String country = "chinese";
		String blockingIp = getBlockingIp(request, country, true, false);
		if(blockingIp==null){
			// 해외 IP
			country = "foreign";
			blockingIp = getBlockingIp(request, country, true, false);
		}
		
		// 관련 없는 IP 리턴함
		if(blockingIp==null) return true;
		if(blockingIp.startsWith("192.168.")) return true;
		if(blockingIp.equals("127.0.0.1")) return true;
		
		if(msgId!=null){
			LOGGER.warn("[FOREIGN IP] BLOCK - "+country+"/processLogin - msgId:"+msgId+"  ip:"+blockingIp);
		} else if(lginId != null){
			LOGGER.warn("[FOREIGN IP] BLOCK - "+country+"/processLogin - lginId:"+lginId+"  ip:"+blockingIp);
		}
		return false;
	}
	
}
