package com.innobiz.orange.web.em.svc;

import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.xml.sax.InputSource;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.SyncConstant;

/** 외부 솔루션(UC 메신저) 기반 메신저 서비스 */
public class EmUcMessengerSvc implements MessengerSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmUcMessengerSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 사용자 기본정보 조회 */
	private OrUserBVo getOrUserBVo(OrUserBVo orUserBVo) throws SQLException{
		return (OrUserBVo)commonDao.queryVo(orUserBVo);
	}
	
	/** msgKey Naming Rule
	 *  포탈에서 해당 메시지를 읽었을때 메신저 알림에서도 동일하게 처리하기 위한 키
	 *  엔터티 상세그룹약어_업무별PK조합
	 * */
	
	/** 알림발송[멀티] 
	 * param => recvUids : 수신자 userUid(String 배열) , 이하 동일  
	 * */
	@Override
	@Async
	public String sendNotice(String[] recvUids, String sendUid, String catNm,
			String subj, String contents, String url, String msgKey)
			throws SQLException, IOException, GeneralSecurityException {
		String returnMsg = null;
		for(String recvUid : recvUids){
			returnMsg = sendNotice(recvUid, sendUid, catNm, subj, contents, url, msgKey);
			if( returnMsg == null) break;
		}
		
		return returnMsg;
	}
	
	/** 알림발송[한명]
	 *  param => recvUid : 수신자 userUid , sendId : 발신자 userUid , catNm : 업무명 , subj : 제목 , contents : 내용 , url : 링크 url , msgKey : 업무별 유니크키  
	 * @throws SQLException 
	 * @throws GeneralSecurityException 
	 * @throws IOException 
	 * */
	@Override
	@Async
	public String sendNotice(String recvUid, String sendUid, String catNm,
			String subj, String contents, String url, String msgKey) 
			throws SQLException, IOException, GeneralSecurityException {
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(!"Y".equals(sysPlocMap.get("messengerEnable"))) return null;
		
		// 엔터프라이즈 버전 여부
		boolean isEnterprise=sysPlocMap!=null && sysPlocMap.get("messengerUcEnable")!=null && "Y".equals(sysPlocMap.get("messengerUcEnable"));
				
		//발신자정보
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(sendUid);
		orUserBVo = getOrUserBVo(orUserBVo);
		String sendUserNm = orUserBVo.getUserNm();
		if(ServerConfig.IS_LINUX || isEnterprise){
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
			orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
			if(orOdurBVo==null){
				LOGGER.error("Messenger SendNotice-Fail : no user(OR_ODUR_B) : "+orUserBVo.getOdurUid());
				return null;
			}
			sendUid = orOdurBVo.getLginId();
		}
		//수신자정보
		orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(recvUid);
		orUserBVo = getOrUserBVo(orUserBVo);
		if(orUserBVo == null ){
			LOGGER.error("Messenger SendNotice-Fail : no user(OR_USER_B) : "+recvUid);
			return null;
		}
		// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		if(orOdurBVo==null){
			LOGGER.error("Messenger SendNotice-Fail : no user(OR_ODUR_B) : "+orUserBVo.getOdurUid());
			return null;
		}
		
		String lginId = orOdurBVo.getLginId();
		if(url != null && !"".equals(url)){
			StringBuilder builder = new StringBuilder(128);
			char concat = url.indexOf('?') > -1 ? '&' : '?';
			
			if(isEnterprise){
				builder.append(url)
					.append(concat).append("ssoOnetime=(%USER_FIELD2%)");
				builder.append("&userUid=")
					.append(RC4.getEncrypt(orUserBVo.getUserUid()));
				//builder.append("&ver=e");
			}else if(ServerConfig.IS_LINUX){ // 리눅스일 경우
				//BASE64Encoder base64Encoder = new BASE64Encoder();
				builder.append(url)
					.append(concat).append("ssoOnetime=")
					.append(RC4.getEncrypt(orUserBVo.getUserUid())); // RC4 로 인코딩		
				builder.append("&lang=")
					.append("(%LANG%)");
			}else{
				builder.append(url)
					.append(concat).append("ssoOnetime=(%UCWTOKEN%)");
				builder.append("&userUid=")
					.append(RC4.getEncrypt(orUserBVo.getUserUid()));
			}
			url = builder.toString();
		}
		String textEncoding = "UTF-8";
		String[][] params = new String[][]{
				{"Action","ALERT"},
				{"SystemName",catNm},
				{"SystemName_Encode",textEncoding},
				{"SendID",sendUid},
				{"SendName",sendUserNm},
				{"SendName_Encode",textEncoding},
				{"RecvID",lginId},
				{"Subject",subj},
				{"Subject_Encode",textEncoding},
				{"Contents",contents},
				{"Contents_Encode",textEncoding},
				{"URL",url},
				{"URL_Encode",textEncoding}
				};
		
		Map<String, String> param = new HashMap<String, String>();
		for(String[] storedParam : params){
			param.put(storedParam[0], storedParam[1]);
		}
		
		// 리눅스일 경우
		if(ServerConfig.IS_LINUX){
			param.put("CMD", "ALERT");
			param.put("Dest_domain", "");
			param.put("Dest_gubun", "US");
			param.put("Option", "WB=NEW,UE=DLL,UM=GET,IT=Y,IR=Y,IM=Y,IA=Y,IS=Y");
		}else{// WINDOW
			param.put("MsgKey", "");
			param.put("URL_Option", "WB=NEW,UE=DLL,UM=GET,IT=Y,IR=Y,IM=Y,IA=Y,IS=Y");
		}

		return sendMessage(param);
	}
	
	/** 알림 정보 저장 */
	private String sendMessage(Map<String, String> param) {
		String returnMsg = null;
		try{
			HttpClient http = new HttpClient();
			Map<String, String> header = new HashMap<String, String>();
			header.put("User-Agent", SyncConstant.USER_AGENT+"/"+SyncConstant.VERSION);
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			
			/*Entry<String, String> entry;
			Iterator<Entry<String, String>> iterator = param.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				if(ServerConfig.IS_LINUX){
					System.out.println("key : "+entry.getKey()+"\t value : "+entry.getValue());
				}
			}*/
			String url = "http://"+svrEnvMap.get("messengerCall");
			String rsltStr = http.sendPost(url, param, header, "UTF-8");
			if(ServerConfig.IS_LINUX){
				if(rsltStr.indexOf("ok")>-1) returnMsg = "200";
				else returnMsg = null;
			}else{
				SAXBuilder builder = new SAXBuilder();
	            Document doc = builder.build(new InputSource(new StringReader(rsltStr)));
	            
	            XMLOutputter xo = new XMLOutputter();
	    		xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
	    		
	            Element root = doc.getRootElement();
	            
	            List<Element> list = root.getChildren();
	            Iterator<Element> iter = list.iterator();
	            
	            while(iter.hasNext()){
	            	Element el = iter.next();
	            	if("code".equals(el.getName())){
	            		returnMsg = el.getText();
	            		break;
	            	}
	            }
			}
            
            
		}catch(Exception exception){
			StringBuilder builder = new StringBuilder(256);
			builder.append("\n");
			tracePackage(exception, builder);
			LOGGER.error(builder.toString());
		}
		
		return returnMsg;
	}
	
	private void tracePackage(Throwable throwable, StringBuilder builder){
		if(throwable==null) return;
		String className;
		builder.append(throwable.getClass().getCanonicalName());
		if(throwable.getMessage() != null){
			builder.append(" - ").append(throwable.getMessage());
		}
		builder.append("\n");
		for(StackTraceElement element : throwable.getStackTrace()){
			className = element.getClassName();
			if(className.startsWith("com.innobiz.") && className.indexOf("$$")<0){
				builder.append("	at ").append(className).append('.')
				.append(element.getMethodName()).append('(')
				.append(className.substring(className.lastIndexOf('.')+1)).append(".java:")
				.append(element.getLineNumber()).append(")\n");
			}
		}
	}
	
	/** 서비스 중인지 */
	public boolean isInService(){
		return true;
	}
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName(){
		return "UC";
	}
	
}
