package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.CRC32;

/** PC 알림 발송 서비스 */
@Service
public class PtWebPushMsgSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMobilePushMsgSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 메세지 프로퍼티 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	@SuppressWarnings("unchecked")
	private ArrayList<Map<String, String>>[] tryListArray = new ArrayList[3];
	
	
	private int tryCount = -1;
	
	private int[] errorIndex = { 0,0,0 };
	
	public PtWebPushMsgSvc(){
		tryListArray[0] = new ArrayList<Map<String, String>>();
		tryListArray[1] = new ArrayList<Map<String, String>>();
		tryListArray[2] = new ArrayList<Map<String, String>>();
	}
	
	/** 회사별 메세지 발송 */
	public void setWebPushByCompId(String compId, String title, String body, String url){
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setCompId(compId);
		orUserBVo.setPageRowCnt(1000);
		orUserBVo.setUserStatCd("02");
		
		boolean hasNext = true;
		try {
			
			List<Map<String, String>> pushList;
			Map<String, String> pcNotiData;
			
			for(int i=1; hasNext; i++){
				
				pushList = new ArrayList<Map<String, String>>();
				
				orUserBVo.setPageNo(i);
				@SuppressWarnings("unchecked")
				List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
				if(orUserBVoList != null){
					for(OrUserBVo storedOrUserBVo : orUserBVoList){
						
						pcNotiData = new HashMap<String, String>();
						pcNotiData.put("uid", storedOrUserBVo.getUserUid());
						pcNotiData.put("title", title);
						pcNotiData.put("body", body);
						pcNotiData.put("url", url);
						
						pushList.add(pcNotiData);
					}
					if(!pushList.isEmpty()){
						sendAsyncWebPush(pushList);
					}
					
					hasNext = orUserBVoList.size() == 1000;
				} else {
					hasNext = false;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/** PC 알림 메세지 발송 - 비동기 방식 - map-key: (uid or oid), title(결재/게시), body(제목), url */
	public void sendAsyncWebPush(List<Map<String, String>> pcNotiList) throws SQLException {
		
		if(pcNotiList==null || pcNotiList.isEmpty()) return;
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		boolean pushEnabled = "Y".equals(sysPlocMap.get("pcNotiEnable"));
		if(!pushEnabled){
			LOGGER.warn("PC Noti disabled !");
			return;
		}
		
		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		String mobileDomain = svrEnvMap.get("mobileDomain");

		// 도메인이 설정되지 않으면 모바일 푸쉬 보내지 않음
		if(mobileDomain==null || mobileDomain.isEmpty()){
			LOGGER.warn("send mobile push message - no mobile domain");
			return;
		}
		
		synchronized (tryListArray[0]) {
			for(Map<String, String> pcNotiData : pcNotiList){
				
				if(pcNotiData.get("uid")!=null){
					pcNotiData.put("uid", Integer.toString(CRC32.hash(pcNotiData.get("uid").getBytes())));
				} else if(pcNotiData.get("oid")!=null){
					pcNotiData.put("oid", Integer.toString(CRC32.hash(pcNotiData.get("oid").getBytes())));
				} else {
					LOGGER.error("key(uid or oid) missing - "+pcNotiData);
					continue;
				}
				tryListArray[0].add(pcNotiData);
			}
		}
	}
	
	/** 2초 간격으로 모바일 푸쉬 메세지 보낼 것 체크하여 발송함 - 푸쉬 서버의 url 호출 방식 */
	@Scheduled(fixedDelay=2000)
	public void sendPush(){
		
		tryCount++;
		if(tryCount==150) tryCount = 0;
		
		if(tryListArray[0].isEmpty() && tryListArray[1].isEmpty() && tryListArray[2].isEmpty()){
			return;
		}
		
		// 모바일 도메인
		boolean mobileEnabled = false;
		String notiUrl = null;
		
		boolean hasConfig = false;
		ArrayList<Map<String, String>> tryList;
		for(int tryIndex=0; tryIndex<3; tryIndex++){
			
			if(tryIndex==0
					|| (tryIndex==1 && tryCount % 30 == errorIndex[1])
					|| (tryIndex==2 && tryCount % 150 == errorIndex[2])
				){
				
				tryList = tryListArray[tryIndex];
				
				if(tryList.isEmpty()) continue;
				
				// 설정 세팅이 안되어 있으면
				if(!hasConfig){
					
					// 설정 세팅
					String domain = null;
					Map<String, String> svrEnvMap  = null;
					Map<String, String> sysPlocMap = null;
					
					try {
						svrEnvMap = ptSysSvc.getSvrEnvMap();
						sysPlocMap = ptSysSvc.getSysPlocMap();
					} catch(Exception ignore){
						LOGGER.error("PcNoti - check db connection.");
						return;
					}
					
					mobileEnabled = "Y".equals(sysPlocMap.get("mobileEnable"));
					if(mobileEnabled){
						domain = svrEnvMap.get("mobileDomain");
						if(domain==null || domain.isEmpty()){
							LOGGER.error("PcNoti - Empty mobile domain !");
							return;
						}
					} else {
						domain = svrEnvMap.get("webDomain");
						if(domain==null || domain.isEmpty()){
							LOGGER.error("PcNoti - Empty web domain !");
							return;
						}
					}
					
					String pushPort = svrEnvMap.get("pushPort");
					
					notiUrl = "http://"+domain
							+ (pushPort==null || pushPort.isEmpty() ? ":7080" : ":"+pushPort)
							+ "/Apps/webPush/sendWebPush.do";
					
					hasConfig = true;
				}
				
				synchronized (tryList) {
					// 호출 인자 만들기
					StringBuilder builder = new StringBuilder(1024);
					builder.append('[');
					boolean first = true;
					
					for(Map<String, String> map : tryList){
						if(first) first = false;
						else builder.append(',');
						appendAsJson(map, builder);
					}
					builder.append(']');
					
					Map<String, String> param = new HashMap<String, String>();
					param.put("list", builder.toString());

					// 발송
					try {
						HttpClient http = new HttpClient();
						String result = http.sendPost(notiUrl, param, "UTF-8");
						if(result!=null){
							result = result.trim();
							if(!"{\"result\":\"ok\"}".equals(result)){
								LOGGER.error("PcNoti - server result:"+result);
							}
						}
						
					} catch(Exception ignore){
						// 1차 발송 오류
						if(tryIndex==0){
							// 1분 뒤 발송 되도록 발송 인덱스 바꿈
							errorIndex[1] = tryCount % 30;
							tryListArray[1].addAll(tryList);
						// 2차 발송 오류
						} else if(tryIndex==1){
							// 5분 뒤 발송 되도록 발송 인덱스 바꿈
							errorIndex[2] = tryCount % 150;
							tryListArray[2].addAll(tryList);
						}
						// 3차 발송 오류는 다시 보내지 않음
						
						LOGGER.error(ignore.getMessage());
						
					} finally {
						// 현재 작업중 목록 모두 제거 - 발송 또는 오류
						for(int i=tryList.size()-1; i>=0; i--){
							tryList.remove(i);
						}
					}
				}
				
			}
		}
		
	}
	
	/** AJAX 데이터로 변환 */
	private void appendAsJson(Map<String, String> map, StringBuilder builder){
		builder.append('{');
		String key;
		String value;
		boolean isFirst = true;
		Iterator<String> it =  map.keySet().iterator();
		while(it.hasNext()){
			key = it.next().toString();
			value = map.get(key);
			
			if(isFirst) isFirst = false;
			else builder.append(',');
			
			builder
				.append('"')
				.append(JSONObject.escape(key))
				.append('"').append(':')
				.append('"')
				.append(JSONObject.escape(value))
				.append('"');
		}
		builder.append('}');
	}
	
}
