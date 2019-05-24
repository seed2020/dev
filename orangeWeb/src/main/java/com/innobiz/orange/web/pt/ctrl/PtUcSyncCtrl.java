package com.innobiz.orange.web.pt.ctrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.svc.PtUcSyncSvc;

/** 사용자 조직도 동기화 처리용 (메신저)*/
@Controller
public class PtUcSyncCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtUcSyncCtrl.class);
	
	/** 동기화 처리용 서비스 */
	@Autowired
	private PtUcSyncSvc ptUcSyncSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 동기화 */
	@RequestMapping(value = "/cm/sync/uc/getSecuData")
	public String getSecuData( HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		String result = null;
		try{
			Document doc = null;
			String func = ParamUtil.getRequestParam(request, "func", true);
			String lang = ParamUtil.getRequestParam(request, "lang", false);
			String encYn = ParamUtil.getRequestParam(request, "encYn", false);
			String fileYn = ParamUtil.getRequestParam(request, "fileYn", false);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			boolean messengerUcEnable=sysPlocMap!=null && sysPlocMap.get("messengerUcEnable")!=null && "Y".equals(sysPlocMap.get("messengerUcEnable"));
			// 메신저 버전 : 신규 버전부터(e:enterprise)
			//String ver = ParamUtil.getRequestParam(request, "ver", false);			
			boolean isLinux = ServerConfig.IS_LINUX || messengerUcEnable;
						
			//회사ID 가 있을경우 해당 회사의 조직정보만 동기화 한다
			String compId = ParamUtil.getRequestParam(request, "compId", false);
			
			if(encYn == null || encYn.isEmpty()) encYn = "N";
			
			String fileNm = "";
			if("getOrgList".equals(func)){//조직정보
				doc = messengerUcEnable ? ptUcSyncSvc.getSortOrgDeptList(isLinux, lang, compId, encYn) : isLinux ? ptUcSyncSvc.getOrgDeptList(isLinux, lang, compId, encYn) : ptUcSyncSvc.getOrgDeptList(isLinux, encYn , lang , "orgDept" , compId);
				fileNm = "org_dept";
			} else if("getAllUserList".equals(func)){//사용자정보
				doc = isLinux ? ptUcSyncSvc.getAllUserList(isLinux, lang, compId, encYn) : ptUcSyncSvc.getUserList(isLinux, encYn, lang, "allUser", compId);
				fileNm = "all_user";
			} else if("getOrgUserList".equals(func)){//조직 사용자정보(겹업등)
				doc = isLinux ? ptUcSyncSvc.getOrgUserList(isLinux, compId, encYn) : ptUcSyncSvc.getUserList(isLinux, encYn , lang , "orgUser", compId);
				fileNm = "org_user";
			}
			if(doc==null) return null;
			XMLOutputter xo = new XMLOutputter();
			xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
			
			//파일생성여부
			if(fileYn != null && "Y".equals(fileYn)){
				fileNm+= "_innobiz"+("Y".equals(encYn) ? "_enc" : "")+(lang != null && !"".equals(lang) ? "_"+lang : "")+".xml";
				String fileDir = ServerConfig.IS_LINUX ? "/app/gwOrange/xml" : "d:/temp";
				File xmlDir = new File(fileDir);
				if(!xmlDir.isDirectory()){
					xmlDir.mkdirs();
				}
				
				FileOutputStream out = new FileOutputStream(new File(fileDir,fileNm));
				xo.output(doc, out);
				out.flush();
				out.close();
			}
			result = xo.outputString(doc);
			//System.out.println("xml : "+xo.outputString(doc));
			
			/*StringWriter writer = new StringWriter();
			out.output(document, writer); // Document를 파일에 기록
			StringBuffer buf = writer.getBuffer();
			String returnValue = buf.toString();
			writer.close();*/
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		
		if(result!=null && !result.isEmpty()){
			PrintWriter write = response.getWriter();
			write.print(result);
			//response.getOutputStream().write(result.getBytes());
			write.flush();
			write.close();
			return null;
		}
		
		response.sendError(403);
		return null;
	}
	
	/** 메신저에서 로그인 체크 */
	@RequestMapping(value = "/cm/login/processLoginCheck")
	public String processLogin(
			@RequestParam(value = "lginId", required = false) String lginId,
			@RequestParam(value = "pw", required = false) String pw,
			HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		/*if(ServerConfig.IS_LINUX){
			System.out.println("=========processLogin==========");
			System.out.println("lginId : "+lginId);
			System.out.println("pw : "+pw);
		}*/
		PrintWriter write = null;
		try{
			write = response.getWriter();
			if(lginId == null || lginId.isEmpty() || pw == null || pw.isEmpty()) {
				LOGGER.error("Login-Fail : lginId == null || lginId.isEmpty() || pw == null || pw.isEmpty()");
				write.print(getMessage("false"));
				return null;
			}	
//			String pwTmp = pw;
			// lginId , pw Decrypt 
			
			lginId = RC4.getDecrypt(lginId);
			//System.out.println("RC4.getDecrypt(lginId) : "+lginId);
			pw = RC4.getDecrypt(pw);
			
			
			//공백제거
			if(lginId != null) lginId = lginId.trim();
			if(pw != null) pw = pw.trim();
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			OrOdurBVo orOdurBVo = null;
			// 사번 로그인
			if("Y".equals(sysPlocMap.get("einLginEnable"))){
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setEin(lginId);
				@SuppressWarnings("unchecked")
				List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
				
				if(orOdurBVoList==null || orOdurBVoList.size()==0){
					orOdurBVo = null;
				} else if(orOdurBVoList.size()==1){
					orOdurBVo = orOdurBVoList.get(0);
				} else {
					orOdurBVo = null;
					LOGGER.error("Login-Fail : Dup Ein : "+lginId+ "  count:"+orOdurBVoList.size());
				}
			// ID 로그인
			} else {
				// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setLginId(lginId);
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			}
						
			if(orOdurBVo==null){
				LOGGER.error("Messanger Login-Fail : no user(OR_ODUR_B) : "+lginId);
				//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				write.print(getMessage("false"));
				return null;
			}
			// 원직자UID
			String odurUid = orOdurBVo.getOdurUid();
			// 로그인 패스워드 암호화
			String encPw = License.ins.encryptPw(pw, odurUid);
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
			OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(odurUid);
			orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
			orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
			if(orUserPwDVo==null){
				LOGGER.error("Messanger Login-Fail : no password(OR_USER_PW_D) - loginId:"+lginId+"  odurUid:"+odurUid);
				//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				write.print(getMessage("false"));
				return null;
			}
			// 패스워드 비교
			if(encPw==null || !encPw.equals(orUserPwDVo.getPwEnc())){
				//LOGGER.error("Messanger Login-Fail : password not matched - loginId:"+lginId+"  odurUid:"+odurUid+"\tpwTmp : "+pwTmp+"\tpw :"+pw+"\tencPw:"+encPw+"\torUserPwDVo.getPwEnc():"+orUserPwDVo.getPwEnc());
				// 평문 pw 로그 금지
				LOGGER.error("Messanger Login-Fail : password not matched - loginId:"+lginId+"  odurUid:"+odurUid+"\tencPw:"+encPw+"\torUserPwDVo.getPwEnc():"+orUserPwDVo.getPwEnc());
				//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				write.print(getMessage("false"));
				return null;
			}
			
			// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
			if(!"02".equals(orOdurBVo.getUserStatCd())){
				//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
				LOGGER.error("Messanger Login-Fail : user stat(not 02) - loginId:"+lginId+"  odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
				write.print(getMessage("false"));
				return null;
			}
			String result = getMessage("true");
			//System.out.println("result : "+result);
			write.print(result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(write != null){
				write.flush();
				write.close();
			}
		}
		return null;
	}
	
	/** xml 메세지 */
	private String getMessage(String msg){
		//if(ServerConfig.IS_LINUX) return msg;
		//최상위 element 생성
		Element root = new Element("result");
		root.addContent(msg);
		Document doc = new Document().setRootElement(root);
		XMLOutputter xo = new XMLOutputter();
		xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		return xo.outputString(doc);
	}
}

