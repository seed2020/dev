package com.innobiz.orange.web.pt.sso;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** UC 메신저 SSO 처리 클라이언트 */
public class SsoParamClientUc extends SsoParamClientImpl implements SsoParamClient {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(SsoParamClientUc.class);
	
//	/** SSO 체크 파라미터 */
//	private String paramName;
	
//	/** 컨텍스트 프로퍼티 */
//	@Resource(name="contextProperties")
//	private Properties contextProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 로그인 관련 서비스  */
//	@Autowired
//	private PtLoginSvc ptLoginSvc;
	
//	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
//	@Autowired
//	private IpChecker ipChecker;
	
	/** SSO를 체크함 - 사용자가 확인되면 true 리턴 */
	@Override
	public boolean checkSso(HttpServletRequest request, HttpServletResponse response) {
		
		// SSO 체크
		String paramName = getParamName();
		if(paramName == null) return false;
		String tokenId = request.getParameter(paramName);
		if(tokenId == null || tokenId.isEmpty()) {
        	LOGGER.error("UC Messenger SSO fail - no TokenID");
        	return false;
        }
		/*if(ServerConfig.IS_LINUX){
			System.out.println("tokenId : "+tokenId);
		}*/
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = null;
		try{
			sysPlocMap=ptSysSvc.getSysPlocMap();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		boolean useSSL = sysPlocMap!=null && "Y".equals(sysPlocMap.get("useSSL"));
		if(useSSL && !request.isSecure() && request.getServerPort() == 80){
			String name, value;
			StringBuilder builder = new StringBuilder(64);
			builder.append("https://").append(request.getServerName()).append(request.getRequestURI());
			
			boolean first = true;
			Enumeration<String> enums = request.getParameterNames();
			while(enums.hasMoreElements()){
				name = enums.nextElement();
				if(first){
					first = false;
					builder.append('?');
				} else {
					builder.append('&');
				}
				value = "";
				try {
					value = URLEncoder.encode(request.getParameter(name), "UTF-8");
				} catch (UnsupportedEncodingException ignore) {}
				builder.append(name).append('=').append(value);
			}
			request.setAttribute("SSO_REDIRECT", builder.toString());
			return true;
		}
		
		//String ver = (String)request.getParameter("ver");
		boolean isLinux = ServerConfig.IS_LINUX || (sysPlocMap!=null && sysPlocMap.get("messengerUcEnable")!=null && "Y".equals(sysPlocMap.get("messengerUcEnable")));
		
		//세션 생성시 메신저에서 넘어온 언어타입으로 세팅한다.
        String langTypCd = request.getParameter("lang") == null ? "ko" : isLinux ? ((String)request.getParameter("lang")).substring(0, 2).toLowerCase(): request.getParameter("lang");
        //if(ServerConfig.IS_LINUX){System.out.println("langTypCd : "+langTypCd);}
		try{
			//사용자UID
            String userUid = null, tokenUid = null;
            
            //리눅스 일 경우
			if(isLinux){
				// 사용자 UID 복호화
	            try {
	            	tokenUid = RC4.getDecrypt(tokenId); // tokenId를 복호화한다.
	            	//tokenUid = tokenId;
					if(tokenUid.indexOf("@") > -1){
						tokenUid = tokenUid.substring(0, tokenUid.indexOf("@")).toUpperCase();
					}
	            } catch(Exception ignore){
	            	LOGGER.error("Decrypt Fail - tokenId:"+tokenId);
	            	return false;
	            }
			}else{
				HttpClient http = new HttpClient();
	            
	            // header
	            Map<String, String> header = null;
				
				// param
				Map<String, String> param = new HashMap<String, String>();
				param.put("CMD","FETCH_UCWTOKEN");
				param.put("TokenID", tokenId);
				
				// 서버 설정 목록 조회
				Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
				String url = "http://"+svrEnvMap.get("messengerCall");
				
				// url 호출 결과
				String rsltStr = http.sendPost(url, param, header, "UTF-8");
				
				// url 호출 결과 - xml 읽기 >> userUid
	            SAXBuilder builder = new SAXBuilder();
	            Document doc = builder.build(new InputSource(new StringReader(rsltStr)));
	            Element root = doc.getRootElement();
	            
	            List<Element> list = root.getChildren();
	            Iterator<Element> iter = list.iterator();
	            
	            String encTokenUid = null;
	            
	            //오류코드
	            String tokenerr = null;
	            while(iter.hasNext()){
	            	Element el = iter.next();
	            	if("tokenerr".equals(el.getName())){
	            		tokenerr = el.getText();
	            		break;
	            	}
	            	if("tokendata".equals(el.getName())){
	            		Element uidEl = el.getChild("userUid");
	            		encTokenUid = uidEl.getText();
	            		break;
	            	}
	            }
	            
	            if(encTokenUid == null) {
	            	LOGGER.error("TokenID : "+tokenId + "[error message : "+tokenerr+"]");
	            	return false;
	            }
	            
	            // 사용자 UID 복호화
	            try {
	            	tokenUid = RC4.getDecrypt(encTokenUid);
					if(tokenUid.indexOf("@") > -1){
						tokenUid = tokenUid.substring(0, tokenUid.indexOf("@")).toUpperCase();
					}
	            } catch(Exception ignore){
	            	LOGGER.error("Decrypt Fail - encTokenUid:"+encTokenUid);
	            	return false;
	            }
			}
            
			if(tokenUid!=null && !tokenUid.isEmpty()) tokenUid = tokenUid.toUpperCase();
			
			String paramUid = request.getParameter("userUid");
			 
			/////////////////////////////////////////////
			//
			//  원칙적으로 토큰의 userUid 와 파라미터의 userUid 가 같아야 하나
			//  - 다를 경우는
			//      - 토큰의 userUid:원직자UID, 파라미터:사용자UID로 가정해서 두개의 UID가 DB 조회값과
			// 			- 같으면 - 파라미터인 사용자UID로 로그인 하고
            //			- 다르면 - 토큰의 UID로 로그인 함
            
            
			// 토큰 사용자 조회
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(tokenUid);
			orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
			if(orUserBVo == null){
				LOGGER.error("No user by tokenUid:"+tokenUid);
				return false;
			}
			if(paramUid != null && !paramUid.isEmpty()) paramUid = RC4.getDecrypt(paramUid).toUpperCase();
			
			if(paramUid==null || paramUid.isEmpty()){
				userUid = tokenUid;
			} else if(paramUid.equals(tokenUid)){
				userUid = tokenUid;
			} else if(paramUid.equals(orUserBVo.getUserUid()) || paramUid.equals(orUserBVo.getOdurUid())){
				userUid = paramUid;
				
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(paramUid);
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo == null){
					LOGGER.error("paramUid.equals(orUserBVo.getUserUid()) || paramUid.equals(orUserBVo.getOdurUid()) No user by paramUid:"+paramUid);
					return false;
				}
			} else {
				
				// 토큰의 원직자UID
				String tokenOdurUid = orUserBVo.getOdurUid();
				
				// 파라미터의 원직자 UID 조회
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(paramUid);
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo == null){
					LOGGER.error("No user by paramUid:"+paramUid);
					return false;
				}
				
				// 토큰사용자의 원직자UID 와 파라미터의 원직자UID 비교
				if(tokenOdurUid.equals(orUserBVo.getOdurUid())){
					userUid = paramUid;
				} else {
					LOGGER.error("Not matched user  tokenUid:"+tokenUid+"  tokenOdurUid:"+tokenOdurUid
							+"  paramUid:"+paramUid+"  paramOdurUid:"+orUserBVo.getOdurUid());
					return false;
				}
			}
//            
//            if(paramUid == null || paramUid.isEmpty() || tokenUid.equals(paramUid)){
//            	userUid = tokenUid;
//            } else {
//            	orUserBVo = new OrUserBVo();
//        		orUserBVo.setUserUid(paramUid);
//        		orUserBVo.setQueryLang(langTypCd);
//        		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
//        		
//        		if(orUserBVo != null && paramUid.equals(orUserBVo.getUserUid())) {//&& tokenUid.equals(orUserBVo.getOdurUid())){
//        			userUid = paramUid;
//        		} else {
//        			userUid = tokenUid;
//        		}
//            }
//            
//            // userUid @주소 제거
//            if(userUid.indexOf("@") > -1)
//            	userUid = userUid.substring(0, userUid.indexOf("@"));
			request.setAttribute("BY_SSO_TYPE", "UC-MESSANGER");
            return super.createSession(request, response, userUid, langTypCd, orUserBVo);
            
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

//	/** SSO 파라미터 명 세팅 */
//	@Override
//	public void setParamName(String paramName) {
//		this.paramName = paramName;
//	}
//
//	/** SSO 파라미터 명 리턴 */
//	@Override
//	public String getParamName() {
//		return paramName;
//	}

	/** xml 메세지(테스트) */
	@SuppressWarnings("unused")
	private String getXmlStr(String uid){
		//최상위 element 생성
		Element root = new Element("ucwtoken");
		Element tokenid = new Element("tokenid");
		tokenid.setText("46546DDF3078804AFB1E2C3817CB69CA");
		root.addContent(tokenid);
		if("nodata".equals(uid) || "expired".equals(uid)){
			Element tokenerr = new Element("tokenerr");
			tokenerr.setText(uid);
			root.addContent(tokenerr);
		}else{
			Element tokendata = new Element("tokendata");
			Element userUid = new Element("userUid");
			userUid.setText(uid);
			tokendata.addContent(userUid);
			root.addContent(tokendata);
		}
		Document doc = new Document().setRootElement(root);
		XMLOutputter xo = new XMLOutputter();
		xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		return xo.outputString(doc);
	}
}
