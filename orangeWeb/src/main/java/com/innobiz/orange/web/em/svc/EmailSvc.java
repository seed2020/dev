package com.innobiz.orange.web.em.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.pt.secu.UserVo;

public interface EmailSvc {
	
	/** 이메일 정보 저장 */
	public Integer saveEmailInfo(CmEmailBVo cmEmailBVo , JSONArray recvIds , List<CommonFileVo> fileList , QueryQueue queryQueue , UserVo userVo) throws SQLException, CmException, IOException;
	
	/** 이메일 정보 저장 */
	public Integer saveEmailInfo(HttpServletRequest request, CmEmailBVo cmEmailBVo , String[][] recvIds , List<CommonFileVo> fileList , QueryQueue queryQueue , UserVo userVo) throws SQLException, CmException, IOException;
	
	/** 이메일 발송 공통[포탈내에 송수신자의 UID가 있을경우에만 사용 ]*/
	public String sendMailSvc(String fromUid , String[] toUids , String subject , String content , String[][] file_list , boolean save_send_mail , boolean save_attach_file, String langTypCd) throws SQLException, CmException, IOException;
	
	/** 이메일 발송 공통[이름 메일]*/
	public String sendMailSvc2(String fromUid , String[] to_list , String subject , String content , String[][] file_list , boolean save_send_mail , boolean save_attach_file, String langTypCd) throws SQLException, CmException, IOException;

	/** 이메일 발송 공통[이름 메일]*/
	public String sendMailSvc3(String fromName, String fromEmail, String[] to_list , String subject , String content , String[][] file_list , boolean save_send_mail , boolean save_attach_file) throws SQLException, CmException, IOException;
	
	/** 이메일 정보 삭제 */
	public int deleteEmailInfo(CmEmailBVo cmEmailBVo , QueryQueue queryQueue ) throws SQLException, CmException, IOException;
	
	/** 메세지 처리용 */
	public void setEmailMessage(ModelMap model, HttpServletRequest request , Integer emailId);
	
	/** 이메일 건수 - 신규메일 */
	public String getEmailCnt(String userUid, String mode) throws SQLException, CmException, IOException;
	
	/** 이메일 건수 - 승인메일 */
	public String getEmailApvCnt(String userUid, String mode) throws SQLException, CmException, IOException;

	/** 서비스 중인지 */
	public boolean isInService();
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName();
	
	/** 제품명 or 회사명 리턴 */
	public String[][] getFileToArrayList(List<CommonFileVo> fileList);
}
