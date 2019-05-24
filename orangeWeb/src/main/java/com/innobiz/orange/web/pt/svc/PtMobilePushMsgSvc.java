package com.innobiz.orange.web.pt.svc;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.icu.text.SimpleDateFormat;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;

/** 포털 모바일 메세지 서비스 */
@Service
public class PtMobilePushMsgSvc {

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
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;

	/** 재시도용 */
	@SuppressWarnings("unchecked")
	private LinkedList<PtPushMsgDVo>[] tryListArray = new LinkedList[3];
	
	/** 모바일 시간 표시 형태 */
	private static SimpleDateFormat MOBILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	/** DB 또는 JAVA 시간 표시 형태 */
	private static SimpleDateFormat DB_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** 2차 마지막 실행 시간  */
	private long lastTryTime2 = 0;
	/** 3차 마지막 실행 시간  */
	private long lastTryTime3 = 0;
	
	public PtMobilePushMsgSvc(){
		tryListArray[0] = new LinkedList<PtPushMsgDVo>();
		tryListArray[1] = new LinkedList<PtPushMsgDVo>();
		tryListArray[2] = new LinkedList<PtPushMsgDVo>();
	}
	
	/** 모바일 푸쉬 메세지 발송 - 비동기 방식 */
	public void sendMobilePush(List<PtPushMsgDVo> ptPushMsgDVoList) throws SQLException {
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 푸쉬 메세지 로그 남기기
		boolean pushMsgLog = "Y".equals(sysPlocMap.get("pushMsgLog"));
		
		boolean pushEnabled = "Y".equals(sysPlocMap.get("mobileEnable")) && "Y".equals(sysPlocMap.get("mobilePushEnable"));
		if(!pushEnabled){
			if(pushMsgLog){
				LOGGER.warn("Mobile or Push disabled !");
			}
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
		
		if(ptPushMsgDVoList != null){
			tryListArray[0].addAll(ptPushMsgDVoList);
		}
	}
	
	/** 2초 간격으로 모바일 푸쉬 메세지 보낼 것 체크하여 발송함 - 푸쉬 서버의 url 호출 방식 */
	@Scheduled(fixedDelay=2000)
	public void sendPush(){
		
		// 모바일 도메인
		String mobileDomain = null;
		Map<String, String> svrEnvMap = null;
		Map<String, String> sysPlocMap = null;
		
		// 푸쉬 메세지 로그 남기기
		boolean pushMsgLog = false;
		
		try {
			// 서버 설정 목록 조회
			svrEnvMap = ptSysSvc.getSvrEnvMap();
			sysPlocMap = ptSysSvc.getSysPlocMap();
			
			mobileDomain = svrEnvMap.get("mobileDomain");
			
			// 푸쉬 메세지 로그 남기기
			pushMsgLog = "Y".equals(sysPlocMap.get("pushMsgLog"));
			
			// 바스원(모바일 푸쉬 개발업체) 테스트 서버
			//mobileDomain = "221.149.87.44:7080";
			
			// 도메인이 설정되지 않으면 모바일 푸쉬 보내지 않음
			if(mobileDomain==null || mobileDomain.isEmpty()){
				if(pushMsgLog) LOGGER.error("Empty mobile domain !");
				return;
			}
		} catch (SQLException e) {
			LOGGER.error("send mobile push message - domain error : "+e.getMessage());
			return;
		}
		
		long currTime = System.currentTimeMillis();
		if(lastTryTime2==0){
			lastTryTime2 = currTime;
			lastTryTime3 = currTime;
		}
		
		String pushPage = "/Apps/ProcRequest.action";
		String pushServerUrl = "http://"+mobileDomain
				+ ("Y".equals(sysPlocMap.get("portForwarding")) ? ":60080" : "")
				+ pushPage;
		
		int tryCnt, i, size;
		LinkedList<PtPushMsgDVo> list;
		PtPushMsgDVo ptPushMsgDVo;
		
		boolean result, hasException;
		String resultXml = null;
		Map<String, String> param;
		HttpClient httpClient = new HttpClient();
		
		for(tryCnt=0;tryCnt<3;tryCnt++){
			
			if(tryCnt==0){
				
			} else if(tryCnt==1){
				// 3분마다 2차시도 발송할 때 되었는지 체크
				if(currTime > lastTryTime2+(1000*60*3)){
					lastTryTime2 = currTime;
				} else {
					continue;
				}
			} else if(tryCnt==2){
				// 10분마다 3차시도 발송할 때 되었는지 체크
				if(currTime > lastTryTime3+(1000*60*10)){
					lastTryTime3 = currTime;
				} else {
					continue;
				}
			}
			
			list = tryListArray[tryCnt];
			size = (list==null) ? 0 : list.size();
			
			for(i=0;i<size;i++){
				
				ptPushMsgDVo = null;
				synchronized(list){
					if(!list.isEmpty()){
						try {
							ptPushMsgDVo = list.removeFirst();
						} catch(Exception ignore){}
					}
				}
				
				if(ptPushMsgDVo==null) continue;
				
				if(tryCnt==1){
					// 2차 시도일때 10분 경과되지 않은 경우 - 패스
					if(currTime < ptPushMsgDVo.getLastTryTime() + (1000 * 60 * 10)){
						continue;
					}
				} else if(tryCnt==2){
					// 3차 시도일때 30분 경과되지 않은 경우 - 패스
					if(currTime < ptPushMsgDVo.getLastTryTime() + (1000 * 60 * 30)){
						continue;
					}
				}
				
				hasException = false;
				param = null;
				result = false;
				
				try {
					
					// 푸쉬 서버에 전송할 파라미터 xml로 변환
					param = makePushParam(ptPushMsgDVo, mobileDomain, currTime);
					
					if(pushMsgLog){
						LOGGER.info("Push request : "+pushServerUrl
								+(param==null ? "" : "\r\nreqxml:"+param.get("reqxml")) );
					}
					
					// 페이지 호출 후 결과 받기
					resultXml = httpClient.sendPost(pushServerUrl, param, "UTF-8");
					result = checkResult(resultXml);
					
					if(pushMsgLog){
						LOGGER.info("Push response : "+resultXml);
					}
					
				} catch (Exception e) {
					hasException = true;
					LOGGER.error("send mobile push message error\r\n"+e.getMessage()
							+"\r\nURL:"+pushServerUrl+"\r\n"
							+(param==null ? "" : "\r\nreqxml:"+param.get("reqxml")) );
				}
				
				if(!result){
					if(tryCnt<2){
						ptPushMsgDVo.setLastTryTime(currTime);
						tryListArray[tryCnt+1].add(ptPushMsgDVo);
					}
					if(!hasException){
						LOGGER.error("send mobile push message error : fail response\r\n"+resultXml);
					}
				}
				if(!result && tryCnt<2){
					ptPushMsgDVo.setLastTryTime(currTime);
					tryListArray[tryCnt+1].add(ptPushMsgDVo);
				}
			}
		}
	}
	
	/** 모바일 푸쉬 메세지용 XML을 생성하여 post 전송하기 위한 파라미터 처리 */
	private Map<String, String> makePushParam(PtPushMsgDVo ptPushMsgDVo, String domain, long currTime) throws ParseException{
		StringBuilder builder = new StringBuilder();
		
		if(ptPushMsgDVo.getIsuDt()==null || ptPushMsgDVo.getIsuDt().isEmpty()){
			ptPushMsgDVo.setIsuDt(DB_FORMAT.format(new Date(currTime)));
		} else if("sysdate".equals(ptPushMsgDVo.getIsuDt())){
			ptPushMsgDVo.setIsuDt(DB_FORMAT.format(new Date(currTime)));
		}
		
		String moduleName = "AP".equals(ptPushMsgDVo.getMdRid()) ? "Approval" : "Mail";
		String compId = ptPushMsgDVo.getCompId();
		String url = "http://"+domain+"/index.do?msgId="+ptPushMsgDVo.getPushMsgId();
		
//		String moduleName = "AP".equals(ptPushMsgDVo.getMdRid()) ? "Approval" : "Mail";
//		String compId = ptPushMsgDVo.getCompId();
//		if(compId==null || compId.isEmpty()) compId = "NO_COMP";
//		
//		String url = ptPushMsgDVo.getUrl();
//		if(!url.startsWith("http://")){
//			url = "http://"+domain+url;
//		}
		
		// 메일의 경우 시스템 메세지 쪽에 표시될때 "[메일]" 붙이기
		String title = ptPushMsgDVo.getPushSubj();
		if("MAIL".equals(ptPushMsgDVo.getMdRid())){
			title = "["+messageProperties.getMessage("pt.topicon.11", SessionUtil.toLocale(ptPushMsgDVo.getLangTypCd()))+"] "
					+title;
		}
		
		String titleSub = title;
		if(titleSub.length()>35) titleSub = titleSub.substring(0, 35);
		
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		builder.append("<root>\r\n");
		builder.append("<request method=\"orange_push_request\"><data>\r\n");
		
		builder.append("<telno>").append(ptPushMsgDVo.getMbno().replace("-", "")).append("</telno>\r\n");
		builder.append("<title>").append(escapeXmlValue(titleSub)).append("</title>\r\n");
		builder.append("<content><![CDATA[<OrangeNoti><").append(moduleName).append(" title='")
			.append(escapeXmlValue(titleSub))
			.append("' regdt='")
			.append(toGlobalDateTime(ptPushMsgDVo.getIsuDt(), currTime))
			.append("' url='")
			.append(escapeXmlValue(url))
			.append("' /></OrangeNoti>]]></content>\r\n");
		builder.append("<outsystemkey>").append(compId).append("_").append(ptPushMsgDVo.getMdRid()).append("</outsystemkey>\r\n");
		builder.append("<maxretrycnt>4</maxretrycnt>\r\n");
		builder.append("<scheduledatetime></scheduledatetime>\r\n");
		
		builder.append("</data></request>\r\n");
		builder.append("</root>");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("reqxml", builder.toString());
		
		return param;
/* - 전송 메세지 샘플
<root>
  <request method="orange_push_request">
    <data>
      <telno>01034871599</telno>
      <title>제목2</title>
      <content><![CDATA[<OrangeNoti><Approval title='이노비즈그룹웨어 전자결제 사용을 환영합니다.' regdt='2015-04-12T21:42:00+0900' url='https://m.search.naver.com/search.naver?query=총선' /><Approval title='이노비즈그룹웨어 사용을 환영합니다.' regdt='2015-04-27T22:20:15+0900' url='https://m.search.naver.com/search.naver?query=총선' /><Mail title='이노비즈그룹웨어 전자메일 사용을 환영합니다.' regdt='2015-04-12T21:42:00+0900' url='https://m.search.naver.com/search.naver?query=총선' /><Mail title='이노비즈그룹웨어 전자메일 사용을 환영합니다.' regdt='2015-04-27T22:20:15+0900' url='https://m.search.naver.com/search.naver?query=총선' /></OrangNoti>]]></content>
      <outsystemkey>34634634563456</outsystemkey>
      <scheduledatetime>2015-04-25 13:00:12:00</scheduledatetime>
      <maxretrycnt>3</maxretrycnt>
     </data>
    <data>
      <telno>01094230390</telno>
      <title>제목2</title>
      <content><![CDATA[<OrangeNoti>
      <Approval
		title='이노비즈그룹웨어 전자결제 사용을 환영합니다.'
		regdt='2015-04-12T21:42:00+0900'
		url='https://m.search.naver.com/search.naver?query=총선' /><Approval title='이노비즈그룹웨어 사용을 환영합니다.' regdt='2015-04-27T22:20:15+0900' url='https://m.search.naver.com/search.naver?query=총선' /><Mail title='이노비즈그룹웨어 전자메일 사용을 환영합니다.' regdt='2015-04-12T21:42:00+0900' url='https://m.search.naver.com/search.naver?query=총선' /><Mail title='이노비즈그룹웨어 전자메일 사용을 환영합니다.' regdt='2015-04-27T22:20:15+0900' url='https://m.search.naver.com/search.naver?query=총선' /></OrangNoti>]]></content>
      <outsystemkey>34634634563456</outsystemkey>
      <scheduledatetime>2015-04-25 13:00:12:00</scheduledatetime>
      <maxretrycnt>3</maxretrycnt>
     </data>
  </request>
</root>
*/
	}
	
	/** xml value escape 처리 */
	private String escapeXmlValue(String value){
		if(value==null) return value;
		
		StringBuilder builder = new StringBuilder(value.length()+64);
		for(char c : value.toCharArray()){
			if(c=='&'){
				builder.append("&amp;");
			} else if(c=='<'){
				builder.append("&lt;");
			} else if(c=='>'){
				builder.append("&gt;");
			} else if(c=='\''){
				builder.append("&apos;");
			} else if(c=='\"'){
				builder.append("&quot;");
			} else if((c>=0 && c<32) || c==127){
				// 제거
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
//		value = value.replace("&", "&amp;");
//		value = value.replace("<", "&lt;");
//		value = value.replace(">", "&gt;");
//		value = value.replace("'", "&apos;");
//		value = value.replace("\"", "&quot;");
//		return value;
	}
	
	/** 모바일 표현용 날짜형식 변환 */
	private String toGlobalDateTime(String dateTime, long currTime) throws ParseException{
		if(dateTime==null || dateTime.isEmpty() || "sysdate".equals(dateTime)){
			return MOBILE_FORMAT.format(new Date(currTime));
		} else {
			Date date = DB_FORMAT.parse(dateTime);
			return MOBILE_FORMAT.format(date);
		}
	}
	
//	public static void main(String[] arg){
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><response><data code=\"-1\" msg=\"deviceid를 찾을수 없습니다.\" telno=\"01025892525\"></data></response></root>";
//		new PtMobilePushMsgSvc().checkResult(xml);
//	}
	
	/** 푸쉬서버 응답 처리 */
	private boolean checkResult(String xml){
		try {
			if(xml==null || xml.isEmpty()){
				LOGGER.error("no return message of Mobile push server");
				return false;
			}
			
			xml = xml.trim();
			if(xml.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><response/></root>")){
				return true;
			}
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			
			XPath xPath =  XPathFactory.newInstance().newXPath();
			String expression = "/root/response/data";
			
			Node node, attr;
			NamedNodeMap namedNodeMap;
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
			
			int size = nodeList==null ? 0 : nodeList.getLength();
			if(size>0){
				node = nodeList.item(0);
				namedNodeMap = node.getAttributes();
				attr = namedNodeMap.getNamedItem("code");
				
				//deviceid를 찾을수 없습니다
				if(attr!=null){
					// 오류가 있는 경우
					// - 대부분 해당번호에 앱이 깔리지 않은 경우로 로그만 남기고 메세지 다시 보내지 않음
					if(!"0".equals(attr.getNodeValue())){
						
						Node msg = namedNodeMap.getNamedItem("msg");
						Node telno = namedNodeMap.getNamedItem("telno");
						
						// 해당 번호에 앱이 깔리지 않은 경우 - 로그만 출력 / 다시 안보냄
						if(msg!=null && "deviceid를 찾을수 없습니다.".equals(msg.getNodeValue())){
							if(LOGGER.isInfoEnabled()){
								LOGGER.info("telno:"+telno.getNodeValue()+"  "+msg.getNodeValue());
							}
							return true;
						}
						return false;
					} else {
						return true;// 2018-04-18 발견
					}
				}
			}
			
/* - 결과 메세지 샘플
<?xml version="1.0" encoding="UTF-8"?>
<root>
<response>
<data telno="01034871599" msg="" code="0"/>
<data telno="01094230390" msg="" code="0"/>
</response>
</root>
*/
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return false;
	}
	
	/** 오래된 메세지 삭제 - 초 분 시 일 월 주 */
	@Scheduled(cron="0 15 0 * * ?")
	public void deleteOld(){
		if(!ServerConfig.IS_PRIME) return;
		String maxIsuDt = StringUtil.getDiffYmd(-30) + " 00:00:00";
		PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
		ptPushMsgDVo.setMaxIsuDt(maxIsuDt);
		try {
			commonDao.delete(ptPushMsgDVo);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
