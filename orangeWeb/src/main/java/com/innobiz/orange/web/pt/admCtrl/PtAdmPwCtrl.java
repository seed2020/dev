package com.innobiz.orange.web.pt.admCtrl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;

/** 관리자 비밀번호 설정 컨트롤러 */
@Controller
public class PtAdmPwCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtAdmPwCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 비밀번호 설정 저장 */
	@RequestMapping(value = "/cm/admin/transPw")
	public String transPw(HttpServletRequest request,
			@RequestParam(value = "secu", required = true) String secu,
			HttpSession session, ModelMap model, Locale locale) throws Exception {
		try {
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Change password fail(by user) : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				throw new CmException(messageProperties.getMessage("pt.login.fail.decrypt", locale));
			}
			
			Map<String, String> adminPwMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ADMIN_PW, false);
			String decTxt, encTxt = adminPwMap.get("value");
			
			if(encTxt!=null){
				
				// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
				throw new CmException(messageProperties.getMessage("cm.msg.errors.403", locale));
				
			} else {
				
				String newPw = (String)jsonObject.get("newPw1");
				
				// 저장 데이터
				Map<String, String> userData = new HashMap<String, String>();
				userData.put("userUid", "U0000001");// U0000001 : 관리자 userUid
				userData.put("pw", newPw);
				
				// json 변경 및 암호화
				decTxt = JsonUtil.toJson(userData);
				encTxt = License.ins.encryptPersanal(decTxt);
				
				PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(PtConstant.PT_ADMIN_PW);
				ptSysSetupDVo.setSetupId("value");
				ptSysSetupDVo.setSetupVa(encTxt);
				
				if(commonSvc.update(ptSysSetupDVo) == 0){
					commonSvc.insert(ptSysSetupDVo);
				}
				
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
				model.put("todo", "parent.location.replace(parent.location.href);");
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
}
