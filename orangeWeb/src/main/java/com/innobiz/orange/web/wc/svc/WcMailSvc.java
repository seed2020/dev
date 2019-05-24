package com.innobiz.orange.web.wc.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlFileDVo;

/** 일정 메일 서비스 */
@Service
public class WcMailSvc {
	
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 관리 서비스 */
	@Autowired
	private WcAdmSvc wcAdmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 일정 공통 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	public void sendEmail(HttpServletRequest request, UserVo userVo, WcSchdlBVo wcsVo, List<WcPromGuestDVo> wcPromGuestDVoList, Map<String, String> paramMap) throws SQLException, IOException, CmException{
	   // 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// System.out.println("wcsVo.getEmailSendYn() : "+wcsVo.getEmailSendYn());
		// System.out.println("wcPromGuestDVoList.size() : "+wcPromGuestDVoList.size());
		//이메일 발송
		if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) &&
				wcsVo.getEmailSendYn() != null && "Y".equals(wcsVo.getEmailSendYn())){
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 환경설정
			Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			
			// 설정에서 참석자 수락여부가 'Y' 인 경우에만 수락여부 메일에 포함
			boolean acceptYn=envConfigMap.containsKey("acceptYn") && "Y".equals(envConfigMap.get("acceptYn"));
			// 수락여부를 보낼 사용자 맵
			//Map<String,String> acptUserMap = new HashMap<String,String>();
			List<String> toUserIds = null;
			List<String> acceptUserList = null;
			
			// 메일ID (수락메일 반복발송시 최신메일인지 구분할 시퀀스)
			String mailId=null;
			
			if(wcPromGuestDVoList.size() > 0 ){
				QueryQueue schdlQueryQueue = new QueryQueue();
				for(WcPromGuestDVo storedWcPromGuestDVo : wcPromGuestDVoList){
					if(storedWcPromGuestDVo.getEmail() != null && !"".equals(storedWcPromGuestDVo.getEmail())){
						if(storedWcPromGuestDVo.getEmailYn()==null || !"Y".equals(storedWcPromGuestDVo.getEmailYn())) // 개별메일발송 여부가 'Y'가 아니면 continue
							continue;
						// 조직에 속한 사용자 일 경우에만 메일일정 관계 등록
						if(acceptYn && storedWcPromGuestDVo.getGuestEmplYn() !=null && "Y".equals(storedWcPromGuestDVo.getGuestEmplYn())){
							// 메일ID 생성
							if(mailId==null) mailId=wcCmSvc.createId("WC_MAIL_SCHDL_R");
							wcScdManagerSvc.setMailSchdlVoList(schdlQueryQueue, mailId, wcsVo.getSchdlId(), null, storedWcPromGuestDVo.getGuestUid(), userVo.getUserUid(), null);
							if(acceptUserList==null) acceptUserList=new ArrayList<String>();
							acceptUserList.add(storedWcPromGuestDVo.getEmail());
							continue;
						}
						if(toUserIds==null) toUserIds=new ArrayList<String>();
						
						toUserIds.add(storedWcPromGuestDVo.getEmail());
					}
				}
				if(schdlQueryQueue.size()>0){
					commonDao.execute(schdlQueryQueue);
				}
			}
			if(toUserIds != null || acceptUserList!=null){
				String[] to_list = ArrayUtil.toArray(toUserIds);
				
				// 일정종류 조회
				WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
				wcCatClsBVo.setQueryLang(langTypCd);
				wcCatClsBVo.setCompId(userVo.getCompId());
				wcCatClsBVo.setCatId(wcsVo.getSchdlTypCd());
				wcCatClsBVo = (WcCatClsBVo)commonDao.queryVo(wcCatClsBVo);
				
				// 일정종류
				String catNm = wcCatClsBVo.getCatNm();
				
				// 제목
				String subj = "["+catNm + "]"+wcsVo.getSubj();
				
				// 파일 목록
				String[][] file_list = null;
				
				WcSchdlFileDVo wcFile = new WcSchdlFileDVo();
				wcFile.setRefId(wcsVo.getSchdlId());
				// 파일 건수 조회
				if(commonDao.count(wcFile)>0){
					// baseDir
					String wasCopyBaseDir = distManager.getWasCopyBaseDir();
					if (wasCopyBaseDir == null) {
						distManager.init();
						wasCopyBaseDir = distManager.getWasCopyBaseDir();
					}
					@SuppressWarnings("unchecked")
					List<WcSchdlFileDVo> wcFileList = (List<WcSchdlFileDVo>) commonDao.queryList(wcFile);
					file_list = new String[wcFileList.size()][2];//파일명,파일경로
					for(int i=0; i < wcFileList.size(); i++){
						file_list[i][0] = wcFileList.get(i).getDispNm();
						file_list[i][1] = wasCopyBaseDir + wcFileList.get(i).getSavePath();
					}
				}
				
				// 수락여부 포함 시킬 사용자 목록
				String[] acceptToList = ArrayUtil.toArray(acceptUserList);
				
				// 메일 내용
				String mailContent = getSchdlMailHTML(request, wcsVo, mailId, null, acceptToList!=null, paramMap);
				
				if(to_list!=null) {
					emailSvc.sendMailSvc2(userVo.getUserUid(), to_list, subj, mailContent, file_list, true,true, langTypCd);
				}
				
				if(acceptToList!=null) {
					emailSvc.sendMailSvc2(userVo.getUserUid(), acceptToList, subj, mailContent, file_list, true,true, langTypCd);
				}
				
			}
		}
   } 
	
	/** 일정 메일 내용 */
	public String getSchdlMailHTML(HttpServletRequest request, WcSchdlBVo wcSchdlBVo, String mailId, String msgUrl, boolean isAccept, Map<String, String> paramMap) throws SQLException, IOException, CmException{
		
		StringBuilder builder = new StringBuilder(1024);
		
		if(isAccept){ // 수락여부
			// 시스템 정책 조회
			Map<String, String> sysEnvMap = ptSysSvc.getSvrEnvMap();
			if(sysEnvMap!=null && sysEnvMap.containsKey("webDomain")){
				builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
				String webDomain = sysEnvMap.get("webDomain");
				String encryptSchdlId = cryptoSvc.encryptPersanal(wcSchdlBVo.getSchdlId()); // 일정ID 암호화
				String encryptMailId = null; // 메일ID 암호화
				if(mailId!=null && !mailId.isEmpty()){
					encryptMailId = cryptoSvc.encryptPersanal(mailId); // 메일ID 암호화
				}
				String returnUrl="http://"+webDomain+"/cm/wc/transGuestSchdl.do?acptId="+encryptSchdlId;
				if(encryptMailId!=null){
					returnUrl+="&mailId="+encryptMailId;
				}
				returnUrl+="&acptYn=";
				
				//builder.append("<div style=\"font-size:14px;margin-bottom:10px;\">");
				builder.append(messageProperties.getMessage("wc.cfrm.schdl.save", request)).append("&nbsp;&nbsp;");
				builder.append("<a href=\""+returnUrl+"Y\" target=\"_blank\">"+messageProperties.getMessage("wc.cols.guest.accept", request)+"</a>"); // 수락
				builder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				builder.append("<a href=\""+returnUrl+"N\" target=\"_blank\">"+messageProperties.getMessage("wc.cols.guest.reject", request)+"</a>"); // 거부
				//builder.append("</div>\n");
				builder.append("</p>\n<br/>\n");
			}
			
		}
		
		builder.append("<table style=\"border:0px\" border=\"0\">\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.subj", request)).append("</strong></td>\n"); // 제목
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if(msgUrl==null || msgUrl.isEmpty()){
			builder.append(wcSchdlBVo.getSubj());
		} else {
			builder.append("<a href=\"").append(msgUrl).append("\" target=\"_top\">").append(wcSchdlBVo.getSubj()).append("</a>");
		}
		builder.append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wc.cols.schdlPriod", request)).append("</strong></td>\n"); // 기간
		
		String startDt = wcSchdlBVo.getSchdlStartDt();
		String endDt = wcSchdlBVo.getSchdlEndDt();
		String priod = null;
		if(wcSchdlBVo.getAlldayYn() != null && "Y".equals(wcSchdlBVo.getAlldayYn())){
			priod = startDt.substring(0, 10)+"~"+endDt.substring(0, 10);
		}else{
			priod = startDt+"~"+endDt;
		}
		
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(priod).append("</td></tr>\n");
		
		// 모듈별 추가 항목
		if(paramMap!=null){
			String md=paramMap.get("md");
			if("WR".equals(md)){ // 자원예약
				if(paramMap.get("kndNm")!=null){
					builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
					.append(messageProperties.getMessage("cols.rescKnd", request)).append("</strong></td>\n"); //자원종류
					builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
					builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
					builder.append(paramMap.get("kndNm"));		
					builder.append("</td></tr>\n");
				}
				if(paramMap.get("rescNm")!=null){
					builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
						.append(messageProperties.getMessage("cols.rescNm", request)).append("</strong></td>\n"); // 자원명
					builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
					builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
					builder.append(paramMap.get("rescNm"));		
					builder.append("</td></tr>\n");
				}
			}
		}
		
		if(wcSchdlBVo.getCont()!=null){
			builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			builder.append("<td colspan=\"3\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(EscapeUtil.escapeValue(wcSchdlBVo.getCont())).append("</td>");
			builder.append("</tr>\n");
		}
		
		builder.append("</table>\n");
		
		return builder.toString();
	}
}
