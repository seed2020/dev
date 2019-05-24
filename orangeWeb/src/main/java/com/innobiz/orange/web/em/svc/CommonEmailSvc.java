package com.innobiz.orange.web.em.svc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.em.vo.CmEmailFileDVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.SyncConstant;

/** 공통 DB 처리용 서비스 */
@Service
public class CommonEmailSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CommonEmailSvc.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
//	/** 메세지 처리용 프라퍼티 - 다국어 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	/** 이메일 정보 조합 */
	public String setEmailNm(String[] emailInfo ){
		if(emailInfo == null) return null;
		String emailNm = !"".equals(emailInfo[0]) ? "\""+emailInfo[0] + "\"" : "";
		//String emailNm = "&quot;"+emailInfo[0] + "&quot;";
		if(emailInfo[1] != null ){
			emailNm+=" <"+emailInfo[1]+">";
		}
		return emailNm;
	}
	
	/** 사용자 기본정보 조회 */
	public OrUserBVo getOrUserBVo(OrUserBVo orUserBVo) throws SQLException{
		return (OrUserBVo)commonDao.queryVo(orUserBVo);
	}
	
	/** 사용자개인정보상세(OR_USER_PINFO_D) 조회 */
	public OrUserPinfoDVo getOrUserPinfoDVo(OrUserPinfoDVo orUserPinfoDVo) throws SQLException, CmException, IOException {
		orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
		if(orUserPinfoDVo!=null){
			orCmSvc.decryptUserPinfo(orUserPinfoDVo);// 복호화
		}
		
		return orUserPinfoDVo;
	}
	
	/** 발송정보 세팅 */
	public void setEmailSendInfo(CmEmailBVo cmEmailBVo , String[] emailInfo , UserVo userVo) throws CmException, SQLException, IOException  {
		//발송정보
		/*OrUserBVo orUserBVo = new OrUserBVo();*/
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		/*orUserBVo.setUserUid((String)userVo.getUserUid());
		orUserBVo = getOrUserBVo(orUserBVo);*/
		//if(orUserBVo != null ){
			emailInfo[0] = userVo.getUserNm();
			//이메일등 부가정보
			//orUserPinfoDVo.setOdurUid((String)orUserBVo.getOdurUid());
			orUserPinfoDVo.setOdurUid((String)userVo.getOdurUid());
			orUserPinfoDVo = getOrUserPinfoDVo(orUserPinfoDVo);
			if( orUserPinfoDVo != null)	emailInfo[1] = orUserPinfoDVo.getEmail();
			cmEmailBVo.setSendNm(setEmailNm(emailInfo));
		//}
	}
	
	/** 파일 정보 세팅 */
	public void setEmailFileInfo(CmEmailBVo cmEmailBVo , QueryQueue queryQueue , List<CommonFileVo> fileList) throws IOException  {
		
		// 첨부파일 리스트
		if(fileList != null && fileList.size() > 0 ){
			// baseDir
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			cmEmailBVo.setFileYn("Y");//파일 여부 'Y'
			CmEmailFileDVo cmEmailFileDVo = null;
			FileInputStream fis = null;
			ByteArrayOutputStream baos  = null;
			
			File file = null;
			int dispOrdr = 0;//파일순서
			for(CommonFileVo commonFileVo : fileList){
				try{
					file = new File(wasCopyBaseDir + commonFileVo.getSavePath());
					if(!file.isFile()) continue;
					// 파일 정보 세팅
					cmEmailFileDVo = new CmEmailFileDVo();
					cmEmailFileDVo.setEmailId(cmEmailBVo.getEmailId());
					cmEmailFileDVo.setDispNm(commonFileVo.getDispNm());
					cmEmailFileDVo.setDispOrdr(dispOrdr);
					cmEmailFileDVo.setFileExt(commonFileVo.getFileExt());
					cmEmailFileDVo.setFileSize(commonFileVo.getFileSize());
					
					fis = new FileInputStream(file);
					baos = new ByteArrayOutputStream();
					int len = 0;
					byte[] buf = new byte[1024];
					while ((len = fis.read(buf)) != -1) {
						baos.write(buf, 0, len);
					}
					byte[] fileArray = baos.toByteArray();
					cmEmailFileDVo.setFileCont(fileArray);//byte[] 로 파일을 저장
					dispOrdr++;
				}catch(IOException e){
					System.out.println("Error message: " + e.getMessage());
				}finally{
					if(fis != null){
						fis.close();
					}
					if(baos != null){
						baos.close();
					}
				}
				//파일정보 저장
				queryQueue.insert(cmEmailFileDVo);
			}
		}else{
			cmEmailBVo.setFileYn("N");
		}
	}
	
	
	/** 이메일 정보 삭제 */
	public int deleteEmailInfo(CmEmailBVo cmEmailBVo , QueryQueue queryQueue ) throws SQLException {
		@SuppressWarnings("unchecked")
		List<CmEmailBVo> cmEmailBVoList = (List<CmEmailBVo>) commonDao.queryList(cmEmailBVo);
		if(cmEmailBVoList.size() > 0){
			CmEmailFileDVo cmEmailFileDVo = null;
			for(CmEmailBVo storedCmEmailBVo : cmEmailBVoList){
				if("Y".equals(storedCmEmailBVo.getFileYn())){
					cmEmailFileDVo = new CmEmailFileDVo();
					cmEmailFileDVo.setEmailId(storedCmEmailBVo.getEmailId());
					//파일정보 삭제
					queryQueue.delete(cmEmailFileDVo);
				}
			}
		}
		
		int cnt = cmEmailBVoList.size();
		//기본정보 삭제
		queryQueue.delete(cmEmailBVo);
		
		return cnt;
	}
	
//	/** 발송 테스트 */
//	public void sendMailTest() throws SQLException {
//		String from_name=	"관리자";
//		String from_email= 	"user01@innogw.com";
//		
//		//String[] to_list= 	{"yeo382@hosting1.zettamail.com","yeo382@naver.com"};
//		String[] to_list= 	{"user01@innogw.com"};
//		
//		String subject= 	"테스트로발송하는메일11입니다.";
//		String content= 	"테스트로발송하는메일11입니다.<br>끝";
//		String tar=			"html";
//		//String[] file_list=	{"E:\\하드웨어,소프트웨어구성도.gif","E:\\one.gif"};
//		String[][] file_list=	null;
//		
//		//보낸메일함에 저장여부
//		boolean save_send_mail= true;
//		
//		//보낸메일함에 메일을 저장할때 첨부파일도 저장할지 여부 
//		// (false 이면 용량이 없는 가비지 파일이 저장됨)
//		boolean save_attach_file= true;
//		
//		try {//메일 발송
//			sendMail(from_name, from_email, to_list, subject, content, tar, file_list, save_send_mail, save_attach_file);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	/** 이메일 발송 [최종] */
	@Async
	public void sendMail(String from_name, String from_email, String[] to_list, String subject, String content, String tar, String[][] file_list, boolean save_send_mail, boolean save_attach_file) throws IOException, MessagingException, SQLException, CmException {
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		String domain = svrEnvMap.get("smtpCall");
		if(domain==null || domain.isEmpty()){
			LOGGER.error("[smtpCall] - SMTP Domain is null !");
			return;
		}
		
		String smptPort = "Y".equals(sysPlocMap.get("portForwarding")) ? "60025" : "25";
		
		int p = domain.indexOf(':');
		if(p>0) {
			smptPort = domain.substring(p+1);
			domain = domain.substring(0, p);
		}
		Properties msgProperties = new Properties();
		msgProperties.put("mail.smtp.host", domain);
		if(smptPort!=null && !smptPort.isEmpty()) msgProperties.put("mail.smtp.port", smptPort);
		
		//add header 관련
		//msgProperties.put("mail.smtp.dsn.notify", "NEVER");
		//msgProperties.put("mail.smtp.auth", "true"); 
		
		/*final String id=null;
		final String pass=null;
		
		Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() { 
                return new PasswordAuthentication(id, pass); 
            }
        };*/
	
		// 보낼 서버와 연결한 세션을 얻어온다.
        Session session = Session.getDefaultInstance(msgProperties, null);
		//Session session = Session.getDefaultInstance(msgProperties, auth);
        
		// 보낼 메세지 객체 생성
		MimeMessage msg = new MimeMessage(session);

		// 보낼 사람의이름과 이메일 주소
		InternetAddress fromAddress= null;
		if(from_name!=null && !from_name.equals("")){
			fromAddress= new InternetAddress(from_name + "<" + from_email + ">");
		}else{
			fromAddress= new InternetAddress(from_email);			
		}
		String personal = fromAddress.getPersonal();
		if(personal != null) {
			fromAddress.setPersonal(personal, "UTF-8");
		}
		
		msg.setFrom(fromAddress);
		
		// 받을 이메일주소 ( 받을사람 <받을이메일주소>  or 받을이메일주소 )
		if(to_list==null){
			LOGGER.error("[sendMail] - to_list is null");
			return;
		}
		
		InternetAddress[] toAddressList = new InternetAddress[to_list.length];
		int idx=0;
		for(String to: to_list){
			toAddressList[idx] = new InternetAddress(to);
			idx++;
			
		}
		
		msg.setRecipients(Message.RecipientType.TO, toAddressList);
		//msg.setRecipients(Message.RecipientType.CC, address); // 참조
		//msg.setRecipients(Message.RecipientType.BCC, address); // 숨은 참조
		
		//제목
		msg.setSubject(subject, "UTF-8");
		
		//보내는 날짜
		msg.setSentDate(new java.util.Date());

		// 내용
		MimeBodyPart mbp1 = new MimeBodyPart();
		if (tar.equals("html")) {
			mbp1.setContent(content, "text/html;charset=UTF-8");//"text/html; charset=UTF-8"
			//mbp1.setContent(content.replaceAll(" ", "&nbsp;"), "text/html;charset=euc-kr");
		} else {
			mbp1.setText(content.replaceAll(" ", "&nbsp;"), "UTF-8");
		}
		
		// Multipart - 몸의 각부분(= MimeBodyPart)를 하나로 합친다. part부분의 대소문자 주의!!..
		// MimeMultipart(part - 소문자),MimeBodyPart(Part - 대문자)
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);

		// 파일
		if(file_list != null && file_list.length > 0){
			for(String[] files: file_list){//[0] : 파일명 , [1] : 파일경로
				// 파일사이즈를 체크해서 용량이 크면 스킵한다
				if(isOverFileSize(files[1])) continue; 	
				
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(files[1]); // J2EE API 중
																	// activation패키지 참고
				mbp2.setDataHandler(new DataHandler(fds));
				//mbp2.addHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
				// 다음에서 MimeUtility.encodeText는 한글이 포함된 파일명일 경우 글자깨짐을 막기 위함.
				mbp2.setFileName(MimeUtility.encodeText(files[0], "UTF-8", "B"));
				mp.addBodyPart(mbp2);
			}
		}
		
		msg.setContent(mp);
		
		//보낸편지함에 저장, 첨부파일도 저장
		if(save_send_mail){
			if(save_attach_file){
				msg.addHeader("x-zmail-savemail", from_email+";no_save_attach=0");
			}else{
				msg.addHeader("x-zmail-savemail", from_email+";no_save_attach=1");
			}
		}
		
		//Transport.send(msg);
		try{
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}catch(MessagingException e){
			LOGGER.error("[MessagingException] - send mail error : "+e.getMessage());
			throw e;
		}
		
		if("Y".equals(sysPlocMap.get("mailSendLog"))){
			LOGGER.info("mail sending complete!!");
		}
	}
	
	private boolean isOverFileSize(String file_path) {
		File file = new File(file_path);
		if (file.length() > (1024 * 1024 * 2.5)) {
			return true;
		}
		return false;
	}
	
	//이메일 건수
	public String getEmailCnt(String userUid, String mode, String mailTyp) throws SQLException, IOException {
		HttpClient http = new HttpClient();
		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", SyncConstant.USER_AGENT+"/"+SyncConstant.VERSION);
		
		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		
		// 요청 URL
		String domain=svrEnvMap.get("mailCall");
		
		int p = domain.indexOf(':');
		if(p>0) {
			domain = domain.substring(0, p)+":4040";
		}else{
			domain+=":4040";
		}
		String url = "http://"+domain+"/zmail/api/";
		
		if(mailTyp!=null && "aprv".equals(mailTyp)) url+="approve_wait_mail_count"; // 승인메일
		else url+="new_mail_count";// 신규메일
		url+=".nvd";
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("userUid",userUid);
		if(mode != null && !mode.isEmpty()){
			param.put("mode", mode);
		}
		
		try {
			String result = http.sendPost(url, param, header, "UTF-8");
			return result;
		} catch(IOException e){
			LOGGER.error("MAIL COUNT ERROR - url:"+url);
			LOGGER.error("MAIL COUNT ERROR - userUid:"+userUid+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
			return "0";
		}
	}

}
