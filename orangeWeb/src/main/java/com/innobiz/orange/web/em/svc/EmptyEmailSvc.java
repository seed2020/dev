package com.innobiz.orange.web.em.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 기능없는 이메일 - 스프링 삽입용 - 오류방지 */
public class EmptyEmailSvc implements EmailSvc{
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 이메일 정보 저장 - JSONArray */
	@Override
	public Integer saveEmailInfo(CmEmailBVo cmEmailBVo, JSONArray recvIds,
			List<CommonFileVo> fileList, QueryQueue queryQueue, UserVo userVo) {
		return null;
	}
	
	/** 이메일 정보 저장 - String[][] */
	@Override
	public Integer saveEmailInfo(HttpServletRequest request, CmEmailBVo cmEmailBVo, String[][] recvIds,
			List<CommonFileVo> fileList, QueryQueue queryQueue, UserVo userVo) {
		return null;
	}

	/** 이메일 발송 공통[포탈내에 송수신자의 UID가 있을경우에만 사용 ]*/
	@Override
	public String sendMailSvc(String fromUid, String[] toUids, String subject,
			String content, String[][] file_list, boolean save_send_mail,
			boolean save_attach_file, String langTypCd) {
		return null;
	}
	
	/** 이메일 발송 공통[이름 메일]*/
	@Override
	public String sendMailSvc2(String fromUid, String[] to_list, String subject,
			String content, String[][] file_list, boolean save_send_mail,
			boolean save_attach_file, String langTypCd) {
		return null;
	}

	@Override
	public String sendMailSvc3(String fromName, String fromEmail,
			String[] to_list, String subject, String content,
			String[][] file_list, boolean save_send_mail,
			boolean save_attach_file) throws SQLException,
			CmException, IOException {
		return null;
	}
	
	/** 이메일 정보 삭제 */
	@Override
	public int deleteEmailInfo(CmEmailBVo cmEmailBVo, QueryQueue queryQueue) {
		return 0;
	}
	
	/** 메세지 처리용 */
	public void setEmailMessage(ModelMap model , HttpServletRequest request , Integer emailId){
		model.put("result", "fail");
		// 이메일 서비스를 제공하지 않습니다.[메시지 창을 띄우지 않을거면 주석처리]
		model.put("message", messageProperties.getMessage("em.msg.not.email.service", request));
	}
	
	/** 이메일 건수 */
	@Override
	public String getEmailCnt(String userUid, String mode) {
		return "0";
	}
	
	/** 이메일 건수 */
	@Override
	public String getEmailApvCnt(String userUid, String mode) {
		return "0";
	}
	
	/** 서비스 중인지 */
	public boolean isInService(){
		return false;
	}
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName(){
		return null;
	}
	
	/** 메일 보내기 형식에 맞게 파일 목록 변환 */
	@Override
	public String[][] getFileToArrayList(List<CommonFileVo> fileList){
		return null;
	}
	
}
