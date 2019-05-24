package com.innobiz.orange.web.em.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 외부 솔루션(Navid 이메일) 기반 이메일 서비스 */
public class EmNavidEmailSvc implements EmailSvc{
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmNavidEmailSvc.class);
	
	/** 메신저 처리 */
	@Resource(name = "commonEmailSvc")
	private CommonEmailSvc commonEmailSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Resource(name = "ptSysSvc")
	private PtSysSvc ptSysSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 이메일 정보 저장 - JSONArray */
	@Override
	public Integer saveEmailInfo(CmEmailBVo cmEmailBVo, JSONArray recvIds,
			List<CommonFileVo> fileList, QueryQueue queryQueue, UserVo userVo)
			throws CmException, SQLException, IOException {
		String[] emailInfo = new String[2];
		
		//발송정보
		commonEmailSvc.setEmailSendInfo(cmEmailBVo, emailInfo, userVo);
		
		//수신자 정보가 있을경우
		if(recvIds != null && recvIds.size() > 0 ){
			OrUserBVo orUserBVo = null;
			OrUserPinfoDVo orUserPinfoDVo = null;
			String recvNm = "";
			emailInfo = new String[2];
			for (int i = 0; i < recvIds.size(); i++) {
				// 사용자기본(OR_USER_B) 테이블
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid((String)recvIds.get(i));
				orUserBVo = commonEmailSvc.getOrUserBVo(orUserBVo);
				
				if(orUserBVo != null ){
					//이메일등 부가정보
					orUserPinfoDVo = new OrUserPinfoDVo();
					orUserPinfoDVo.setOdurUid((String)orUserBVo.getOdurUid());
					orUserPinfoDVo = commonEmailSvc.getOrUserPinfoDVo(orUserPinfoDVo);
					if(orUserPinfoDVo.getEmail() == null ) continue;//이메일 정보가 없을경우 수신자 정보에 담지 않는다.
					emailInfo[0] = orUserBVo.getUserNm();
					if( orUserPinfoDVo != null)	emailInfo[1] = orUserPinfoDVo.getEmail();
					if(emailInfo[1] != null) recvNm += "".equals(recvNm) ? commonEmailSvc.setEmailNm(emailInfo) : " "+ commonEmailSvc.setEmailNm(emailInfo);
				}
			}
			if(!"".equals(recvNm)){
				cmEmailBVo.setRecvNm(recvNm);
			}
		}
			
		/** 이메일 ID 생성 */
		Integer emailId = commonSvc.nextVal("CM_EMAIL_B").intValue();
		
		// 신규 ID 세팅
		cmEmailBVo.setEmailId(emailId);
				
		// 첨부파일
		commonEmailSvc.setEmailFileInfo(cmEmailBVo, queryQueue, fileList);
		
		//이메일 초기 기본정보 저장
		queryQueue.insert(cmEmailBVo);
		
		return emailId;
	}
	
	/** 이메일 정보 저장 - String[][] */
	@Override
	public Integer saveEmailInfo(HttpServletRequest request, CmEmailBVo cmEmailBVo, String[][] recvIds,
			List<CommonFileVo> fileList, QueryQueue queryQueue, UserVo userVo)
			throws CmException, SQLException, IOException {
		String[] emailInfo = new String[2];
		
		//발송정보
		commonEmailSvc.setEmailSendInfo(cmEmailBVo, emailInfo, userVo);
		
		//수신자 정보가 있을경우
		if(recvIds != null && recvIds.length > 0 ){
			String recvNm = "";
			emailInfo = new String[2];
			for (int i = 0; i < recvIds.length; i++) {
				if(recvIds[i][1] == null ) continue;
				emailInfo[0] = recvIds[i][0];
				emailInfo[1] = recvIds[i][1];
				recvNm += "".equals(recvNm) ? commonEmailSvc.setEmailNm(emailInfo) : " "+ commonEmailSvc.setEmailNm(emailInfo);
			}
			if(!"".equals(recvNm)){
				cmEmailBVo.setRecvNm(recvNm);
			}
			
			if(cmEmailBVo.getRecvNm()==null || cmEmailBVo.getRecvNm().isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException(messageProperties.getMessage("cm.msg.notValidCall", request));
			}
			// 수신자 데이터 체크
			int recvLen = cmEmailBVo.getRecvNm().getBytes("UTF-8").length;
			if(recvLen>EmConstant.MAX_RECIPIENTS){
				//em.msg.mail.manyRecipients=수신자가 너무 많습니다.
				throw new CmException(messageProperties.getMessage("em.msg.mail.manyRecipients", request));
			}
		}
		
		/** 이메일 ID 생성 */
		Integer emailId = commonSvc.nextVal("CM_EMAIL_B").intValue();
		// 신규 ID 세팅
		cmEmailBVo.setEmailId(emailId);
				
		// 첨부파일
		commonEmailSvc.setEmailFileInfo(cmEmailBVo, queryQueue, fileList);
		//이메일 초기 기본정보 저장
		queryQueue.insert(cmEmailBVo);
		
		return emailId;
	}
	
	/** 사용자 UID로 이름,이메일 정보 조회 String[]*/
	public String[] getUserInfo(String userUid, String langTypCd) throws SQLException, CmException, IOException {
		String[] userInfo = null;
		//발송정보
		OrUserBVo orUserBVo = new OrUserBVo();
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = commonEmailSvc.getOrUserBVo(orUserBVo);
		if(orUserBVo != null ){
			//이메일등 부가정보
			orUserPinfoDVo.setOdurUid((String)orUserBVo.getOdurUid());
			orUserPinfoDVo = commonEmailSvc.getOrUserPinfoDVo(orUserPinfoDVo);
			//수신자의 이메일 정보가 있을경우에만 해당 정보를 세팅한다.
			if( orUserPinfoDVo != null && orUserPinfoDVo.getEmail() != null && !orUserPinfoDVo.getEmail().isEmpty()) {
				userInfo = new String[2];
				userInfo[0] = orUserBVo.getUserNm();
				userInfo[1] = orUserPinfoDVo.getEmail();
			}else return null;
		}else{
			return null;
		}
		return userInfo;
	}
	
	/** 이메일 발송 공통[포탈내에 송수신자의 UID가 있을경우에만 사용 ]*/
	@Override
	public String sendMailSvc(String fromUid, String[] toUids, String subject,
			String content, String[][] file_list, boolean save_send_mail,
			boolean save_attach_file, String langTypCd) throws SQLException, CmException, IOException {
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(!(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")))){
			LOGGER.error("[not email service] - mailEnable : "+sysPlocMap.get("mailEnable"));
			return null;
		}
		
		//발송자정보
		String[] fromInfo = getUserInfo(fromUid, langTypCd);
		if(fromInfo==null) return null;
		String from_name = fromInfo[0];	//이름
		String from_email = fromInfo[1]; //이메일

		//수신자 정보 임시 배열
		List<String> tempList = new ArrayList<String>();
		//수신자정보
		//String[] to_list= 	new String[toUids.length];
		if(toUids.length > 0){
			for(int i=0;i<toUids.length;i++){
				if(toUids[i] == null || toUids[i].isEmpty()) continue;
				String[] toInfo = getUserInfo(toUids[i], langTypCd);
				if(toInfo == null) {
					LOGGER.error("[email is null] - toUids[i] : "+toUids[i]);
					continue;
				}
				tempList.add(toInfo[1]);//이메일
				//to_list[i] = toInfo[1];
			}
		}
		
		if(tempList.size() == 0 ){
			LOGGER.error("[receive email count : "+tempList.size()+"]");
			return null;
		}
		// 이메일이 있는 수신자의 정보만 세팅한다.
		String[] to_list = tempList.toArray(new String[tempList.size()]);
		
		String tar = "html";
		try {//메일 발송
			commonEmailSvc.sendMail(from_name, from_email, to_list, subject, content, tar, file_list, save_send_mail, save_attach_file);
		} catch (Exception e) {
			LOGGER.error("[ERROR]commonEmailSvc.sendMail : "+e.getMessage());
			//e.printStackTrace();
		}
		//메세지 처리는 필요한 경우 삽입한다.(현재 업무들은 이메일여부를 선택해서 보내기 때문에 처리 안함)
		return null;
	}
	
	/** 이메일 발송 공통[이름 메일]*/
	@Override
	public String sendMailSvc2(String fromUid, String[] to_list,
			String subject, String content, String[][] file_list,
			boolean save_send_mail, boolean save_attach_file, String langTypCd) throws CmException, SQLException, IOException {
		
		//발송자정보
		String[] fromInfo = getUserInfo(fromUid, langTypCd);
		String fromName = fromInfo[0];	//이름
		String fromEmail = fromInfo[1]; //이메일
		
		return sendMailSvc3(fromName, fromEmail, to_list, subject, 
				content, file_list, save_send_mail, save_attach_file);
	}

	/** 이메일 발송 공통[이름 메일]*/
	@Override
	public String sendMailSvc3(String fromName, String fromEmail,
			String[] to_list, String subject, String content,
			String[][] file_list, boolean save_send_mail,
			boolean save_attach_file) throws SQLException,
			CmException, IOException {
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(!(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")))){
			LOGGER.error("[not email service] - mailEnable : "+sysPlocMap.get("mailEnable"));
			return null;
		}
		
		String tar=	"html";
		
		try {//메일 발송
			commonEmailSvc.sendMail(fromName, fromEmail, to_list, subject, content, tar, file_list, save_send_mail, save_attach_file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 이메일 정보 삭제 */
	@Override
	public int deleteEmailInfo(CmEmailBVo cmEmailBVo , QueryQueue queryQueue ) throws SQLException{
		return commonEmailSvc.deleteEmailInfo(cmEmailBVo, queryQueue);
	}

	/** 메세지 처리용 */
	public void setEmailMessage(ModelMap model , HttpServletRequest request , Integer emailId){
		model.put("emailId", emailId);
		model.put("result", "ok");
	}
	
	/** 이메일 건수 - 신규메일 */
	@Override
	public String getEmailCnt(String userUid, String mode) throws SQLException, IOException {
		return commonEmailSvc.getEmailCnt(userUid, mode, null);
	}
	
	/** 이메일 건수 - 승인메일 */
	@Override
	public String getEmailApvCnt(String userUid, String mode) throws SQLException, IOException {
		return commonEmailSvc.getEmailCnt(userUid, mode, "aprv");
	}
	
	/** 서비스 중인지 */
	public boolean isInService(){
		return true;
	}
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName(){
		return "NAVID";
	}
	
	/** 메일 보내기 형식에 맞게 파일 목록 변환 */
	@Override
	public String[][] getFileToArrayList(List<CommonFileVo> fileList){
		if(fileList==null || fileList.size()==0) return null;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		String savePath=null;
		List<String[]> returnFileList=new ArrayList<String[]>();
		for (CommonFileVo fileVo : fileList) {
			if(fileVo==null || fileVo.getSavePath()==null) continue;
			savePath = wasCopyBaseDir+fileVo.getSavePath();
			returnFileList.add(new String[]{fileVo.getDispNm(), savePath});
		}
		
		if(returnFileList.size()>0){
			return ArrayUtil.to2Array(returnFileList);
		}
		return null;
		
	}
}
