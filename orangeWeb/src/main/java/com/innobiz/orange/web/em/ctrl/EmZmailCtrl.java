package com.innobiz.orange.web.em.ctrl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.em.vo.CmEmailFileDVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.Browser;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSsoSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 메일 */
@Controller
public class EmZmailCtrl {		
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmZmailCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** SSO 처리용 서비스 */
	@Autowired
	private PtSsoSvc ptSsoSvc;
	
	/** 컨텍스트 프로퍼티 */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** [SCHEDULED] 이메일 저장정보 삭제 스케줄링 */
	@Scheduled(cron=" 0 0 0 * * *")//매일 0시에 초기화
	public void deleteEmailInfoScheduled() {
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			if("oracle".equals(contextProperties.getProperty("dbms"))){
				cmEmailBVo.setWhereSqllet("AND REG_DT < SYSDATE");
			} else if("mysql".equals(contextProperties.getProperty("dbms"))){
				cmEmailBVo.setWhereSqllet("AND REG_DT < NOW()");
			} else {
				cmEmailBVo.setWhereSqllet("AND REG_DT < GETDATE()");
			}
			int cnt = emailSvc.deleteEmailInfo(cmEmailBVo , queryQueue);
			commonSvc.execute(queryQueue);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scheduled deleteEmailInfo completed at " + new Timestamp(System.currentTimeMillis()).toString() + ", delete count = " + cnt);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/** [AJAX] 이메일 발송 테스트 */
	@RequestMapping(value = "/cm/transSendEmailAjx")
	public String transSendEmailAjx(HttpServletRequest request,
			ModelMap model) {
		
		/*try {
			commonEmailSvc.sendMailTest();
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}*/
		
		return JsonUtil.returnJson(model);
	}
	
	/** admin 체크 */
	@RequestMapping(value = "/cm/getVirtualUserAjx")
	public String getVirtualUserAjx(HttpServletRequest request,
			ModelMap model) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String msg = "";
		if(userVo.getLginId().equals("admin"))
			msg =messageProperties.getMessage("cm.error.title.901", request); //가상의 사용자는 연계된 시스템을 사용할 수 없습니다.
		
		try{
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if(sysPlocMap != null){
				model.put("sysPlocMap", JsonUtil.toJson(sysPlocMap));
			}
		}catch(SQLException se){
			LOGGER.error("sysPlocMap error!!");
		}
		
		model.put("message", msg);
		return JsonUtil.returnJson(model);
	}
	
	
	/** [POPUP] 이메일 팝업 - 테스트[추후 이메일 어플리케이션으로 전환후 삭제] */
	@RequestMapping(value = "/cm/sendEmailPop")
	public String sendEmailPop(HttpServletRequest request,
			@Parameter(name="emailId", required=true) Integer emailId,
			ModelMap model) throws Exception {
		
		// 이메일 기본
		CmEmailBVo cmEmailBVo = new CmEmailBVo();
		cmEmailBVo.setEmailId(emailId);
		cmEmailBVo = (CmEmailBVo) commonSvc.queryVo(cmEmailBVo);
		
		if(cmEmailBVo == null ){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		//테스트용 [첨부파일이 있을경우 byte[] 로 저장되 있는 데이터를 물리적 파일로 변환]
		if("Y".equals(cmEmailBVo.getFileYn())){
			CmEmailFileDVo cmEmailFileDVo = new CmEmailFileDVo();
			cmEmailFileDVo.setEmailId(emailId);
			@SuppressWarnings("unchecked")
			List<CmEmailFileDVo> cmEmailFileDVoList = (List<CmEmailFileDVo>) commonSvc.queryList(cmEmailFileDVo);
			FileOutputStream fos = null;
			ByteArrayInputStream bais = null;
			
			if(cmEmailFileDVoList.size() > 0 ){
				for(CmEmailFileDVo storedCmEmailFileDVo : cmEmailFileDVoList){
					try{
						byte[] bytes = storedCmEmailFileDVo.getFileCont();
						fos = new FileOutputStream(new File("d:/"+storedCmEmailFileDVo.getDispNm()));
						bais = new ByteArrayInputStream(bytes);
						int character;
						while( (character = bais.read() ) != -1 ){
							fos.write(character);// 파일에 쓰기
						}
					}catch(IOException e){
						System.out.println("Error message: " + e.getMessage());
					}finally{
						if(bais != null){
							bais.close();
						}
						if(fos != null){
							fos.close();
						}
					}
				}
			}
			model.put("cmEmailFileDVoList", cmEmailFileDVoList);
		}
		model.put("cmEmailBVo", cmEmailBVo);
		
		return LayoutUtil.getJspPath("/em/sendEmailPop");
	}
	
	/** [AJAX] 이메일 초기정보 저장 (공통) */
	@RequestMapping(value = "/cm/transEmailAjx")
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			JSONArray jsonArray = (JSONArray)object.get("recvVal");//수신자 이메일
			
			//if(jsonArray == null || jsonArray.size() == 0 ){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				//throw new CmException("pt.msg.nodata.passed", request);
			//}
			
			String[][] recvList = null;
			if(jsonArray!=null && jsonArray.size()>0){
				// jsonArray size만큼 String[] 생성
				recvList = new String[jsonArray.size()][2];
							
				// jsonArray를 String[]에 담는다.
				for(int i=0;i<jsonArray.size();i++){
					recvList[i][0] ="";
					recvList[i][1] = (String)jsonArray.get(i);
				}
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			cmEmailBVo.setCont("");
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(request, cmEmailBVo , recvList , null , queryQueue , userVo);
			//Integer emailId = bbBullSvc.saveBullLEmail(bbBullLVo,queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [POPUP] 지메일 팝업*/
	@RequestMapping(value = "/cm/zmailPop")
	public String zmailPop(HttpServletRequest request,
			HttpServletResponse response,
			@Parameter(name="emailId", required=false) Integer emailId,
			@Parameter(name="mailSn", required=true) String mailSn,
			@Parameter(name="mailboxCode", required=true) String mailboxCode,
			@Parameter(name="cmd", required=true) String cmd,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 모바일 디바이스의 경우 - 모바일 웹서버로 redirect
		if(Browser.isMobile(request)){
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String mailDomain = svrEnvMap.get("mailCall");
			if(mailDomain!=null && !mailDomain.isEmpty() && userVo!=null){
				
				// 시스템 정책 조회
//				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				String url = "http://"+mailDomain+"/zmail/m_login_groupware.nvd";
//				boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
				boolean useSSL = request.isSecure();
				if(useSSL){
					url = ptSysSvc.toZMailSslUrl(url);
				}
				// SSO 정보 생성
				String encrypted = ptSsoSvc.createOnetime(userVo);
				// device=mobile : pc용 was를 통해서 sso 처리 필요함을 알리는 파라미터
				// byMsg=Y : [<-그룹웨어] 버튼
				// onetime=encrypted : sso 용 one time id
				response.sendRedirect(url+"?device=mobile&byMsg=Y&onetime="+encrypted+"&"+request.getQueryString());
				return null;
			} else {
				if(userVo!=null){
					LOGGER.error("Mobile domain is not set !");
				} else {
					LOGGER.error("No userVo !");
				}
			}
		}
		
		// 로그인 세션 체크
		if(userVo==null){
			// 로그인 페이지로 리다이렉트
			response.sendRedirect(PtConstant.URL_LOGIN);
			return null;
		}
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		//이메일 서비스가 없을경우 메인화면으로 이동
		if(!emailSvc.isInService() || !(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")))){
			//이메일 서비스를 제공하지 않습니다.
			model.put("message", messageProperties.getMessage("em.msg.not.email.service", request));
			model.put("togo", "/");
			return LayoutUtil.getResultJsp();
		}
		
		if(cmd == null || cmd.isEmpty()) cmd = "send_mail";
		//model.put("cmd", cmd);
		
		String langTypCd = LoginSession.getLangTypCd(request);
		String redirectUrl = null;
		
		if(!cmd.equals("send_mail")){
			PtMnuLoutDVo ptMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_MAIL, userVo.getLoutCatId(), userVo.getCompId(), langTypCd);
			if(ptMnuLoutDVo!=null){
				redirectUrl = ptMnuLoutDVo.getMnuUrl();
				if(redirectUrl!=null && !redirectUrl.isEmpty()){
					StringBuilder builder = new StringBuilder(256);
					builder.append(redirectUrl.replace("viewFrame.do",  "viewMail.do"));
					builder.append("&cmd=").append(cmd);
					if(mailSn!=null && !mailSn.isEmpty()){
						builder.append("&mailSn=").append(mailSn);
					}
					if(mailboxCode!=null && !mailboxCode.isEmpty()){
						builder.append("&mailboxCode=").append(mailboxCode);
					}
					redirectUrl = builder.toString();
				}
			}
		}
		
		if(redirectUrl==null){
			// SSO 정보 생성
			String encrypted = ptSsoSvc.createOnetime(userVo);
			String onetime = encrypted;
			model.put("onetime", onetime);
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			
			if(emailId==null){
				String strEmailId = (String)request.getAttribute("emailId");
				if(strEmailId!=null && !strEmailId.isEmpty()) emailId = Integer.valueOf(strEmailId);
			}
			
			if(emailId!=null){
				// 원직자 보안 정보
				Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(userVo.getOdurUid());
				if(odurSecuMap!=null && odurSecuMap.containsKey("useMailYn") && "N".equals(odurSecuMap.get("useMailYn"))){
					//이메일 서비스를 제공하지 않습니다.
					model.put("message", messageProperties.getMessage("em.msg.not.email.service", request));
					model.put("togo", "/");
					return LayoutUtil.getResultJsp();
				}
			}	
			
			StringBuilder builder = new StringBuilder(256);
			builder.append("http://").append(svrEnvMap.get("mailCall"))
				.append("/zmail/login.nvd?cmd=").append(cmd)
				.append("&etc=close_window&onetime=").append(onetime)
				.append("&email_id=").append(emailId);
			redirectUrl = builder.toString();
			
			boolean useSSL = request.isSecure();
			if(useSSL){
				redirectUrl = ptSysSvc.toZMailSslUrl(redirectUrl);
			}
		}
		
		model.put("UI_TITLE", "Mail Interface");
		
		model.put("todo", "location.replace('"+redirectUrl+"')");
		return LayoutUtil.getResultJsp();
		//return LayoutUtil.getJspPath("/em/zmailPop");
	}
	
	/** 메신저에서 메일 건수 조회 */
	@RequestMapping(value = "/cm/em/countMail")
	public String countMail(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(value = "mode", required = false) String mode,			
			ModelMap model) throws Exception {
		
		PrintWriter writer = null;
		try{
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
						
			boolean isLinux = ServerConfig.IS_LINUX || (sysPlocMap!=null && sysPlocMap.get("messengerUcEnable")!=null && "Y".equals(sysPlocMap.get("messengerUcEnable")));
			
			writer = response.getWriter();
			if(userUid == null || userUid.isEmpty() ) { 
				writer.print(getCnt(null, isLinux));
				return null;
			}	
			
			//사용자 UID 복호화
			userUid = RC4.getDecrypt(userUid);
			
			//사용자 정보 조회
			OrUserBVo orUserBVo = new OrUserBVo();
    		orUserBVo.setUserUid(userUid);
    		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
    		
    		if(orUserBVo == null){
    			writer.print(getCnt(null, isLinux));
				return null;
    		}
    		// 원직자가 아닌경우 원직자 UID로 대체
			if(!"01".equals(orUserBVo.getAduTypCd())){//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				// 원직자기본(OR_ODUR_B) 테이블 
				userUid = orUserBVo.getOdurUid();
			}
			
			String rsltStr = emailSvc.getEmailCnt(userUid, mode);
			//건수
			writer.print(getCnt(rsltStr, isLinux));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
		return null;
	}
	
	/** 건수 포맷 변경 */
	public String getCnt(String cnt, boolean isLinux){
		if(cnt==null) cnt = "0";
		if(isLinux){// 리눅스일 경우 XML 포맷으로 변경하여 리턴한다.
			XMLOutputter xo = new XMLOutputter();
			xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
			Element root = new Element("COUNTLIST");
			Element systemName = new Element("SYSTEMNAME");
			systemName.addContent("MAIL"); // 시스템명(메일)
			Element count = new Element("COUNT");
			count.addContent(cnt);
			root.addContent(systemName);
			root.addContent(count);
			Document doc = new Document().setRootElement(root);
			return xo.outputString(doc);
		}
		return cnt;
	}
	
	
	/** 포털 외부URL - 외부 연계 페이지 - 나비드 메일 (메뉴 삽입 후 호출)
	 *  - 메신저에서 메일클릭 했을때 - 포탈의 상단에 해당 메일조회 하단을 넣은 형태로 화면 조회 하기 위한 URL */
	@RequestMapping(value = "/pt/lout/viewMail")
	public String viewInterface(HttpServletRequest request,
			@Parameter(name="cmd", required=false) String cmd,
			@Parameter(name="emailId", required=false) String emailId,
			@Parameter(name="mailSn", required=false) String mailSn,
			@Parameter(name="mailboxCode", required=false) String mailboxCode,
			ModelMap model) throws Exception {

		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		
		StringBuilder urlBuilder = new StringBuilder(512);
		urlBuilder.append("http://")
			.append(svrEnvMap.get("mailCall"))
			.append("/zmail/login.nvd?cmd=").append(cmd);
		
		if(emailId!=null && !emailId.isEmpty()){
			urlBuilder.append("&email_id=").append(emailId).append("&etc=close_window");
		} else if(mailSn!=null && !mailSn.isEmpty()){
			urlBuilder.append("&mail_sn=").append(mailSn);
			if(mailboxCode!=null && !mailboxCode.isEmpty()){
				urlBuilder.append("&mailbox_code=").append(mailboxCode);
			}
		}
		
		UserVo userVo = LoginSession.getUser(request);
		// SSO 정보 생성
		String encrypted = ptSsoSvc.createOnetime(userVo);
		urlBuilder.append("&onetime=").append(encrypted);

		String frameUrl = urlBuilder.toString();
		
		//boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		boolean useSSL = request.isSecure();
		if(useSSL){
			frameUrl = ptSysSvc.toZMailSslUrl(frameUrl);
		}
		
		model.put("frameUrl", frameUrl);
		
		//왼쪽메뉴 표시안함 처리
		model.put("UI_FRAME", Boolean.TRUE);
		return LayoutUtil.getJspPath("/pt/lout/viewFrame");
	}
}
