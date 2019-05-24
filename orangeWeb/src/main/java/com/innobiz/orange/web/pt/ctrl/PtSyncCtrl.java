package com.innobiz.orange.web.pt.ctrl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.LicenseException;
import com.innobiz.orange.web.pt.svc.PtSyncSvc;
import com.innobiz.orange.web.pt.sync.SyncConstant;

/** 사용자 조직도 동기화 처리용 */
@Controller
public class PtSyncCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtSyncCtrl.class);
	
	/** 동기화 처리용 서비스 */
	@Autowired
	private PtSyncSvc ptSyncSvc;
	
	/** 동기화 */
	@RequestMapping(value = "/cm/sync/getSecuData")
	public String getSecuData( HttpServletRequest request,
			@RequestParam(value = "secuData", required = false) String secuData,
			@RequestParam(value = "noAgnt", required = false) String noAgnt,
			HttpServletResponse response,
			ModelMap model) throws LicenseException, IOException {
		
		if(!"Y".equals(noAgnt)){
			String userAgent = request.getHeader("User-Agent");
			if(userAgent==null || !userAgent.startsWith(SyncConstant.USER_AGENT)){
				response.sendError(403);
				LOGGER.error("[SYNC] USER AGENT FAIL");
				return null;
			}
		}
		
		JSONObject jsonObject = null;
		try{
			String decrypted = License.ins.decryptCookie(secuData);
			if(decrypted!=null && decrypted.startsWith("{") && decrypted.endsWith("}")){
				jsonObject = (JSONObject)JSONValue.parse(decrypted);
			} else {
				LOGGER.error("secuData : "+secuData);
				LOGGER.error("decrypted : "+decrypted);
			}
		} catch(Exception ignore){
			ignore.printStackTrace();
		}
		
		if(jsonObject==null){
			response.sendError(403);
			return null;
		}
		
		String result = null;
		try{
			result = ptSyncSvc.processSync(jsonObject);
//			if("getCompList".equals(func)){
//				result = ptSyncSvc.getCompList(lang);
//			} else if("getOrgList".equals(func)){
//				result = ptSyncSvc.getOrgList(compId, lang);
//			} else if("getCodeListMap".equals(func)){
//				result = ptSyncSvc.getCodeListMap(compId, lang);
//			} else if("getUserList".equals(func)){
//				String pageString = (String)jsonObject.get("page");
//				if(pageString!=null){
//					Integer page = Integer.valueOf(pageString);
//					result = ptSyncSvc.getUserList(compId, lang, page);
//				}
//				if(result==null){
//					return null;
//				}
//			} else if("getUser".equals(func)){
//				String userUid = (String)jsonObject.get("userUid");
//				result = ptSyncSvc.getUser(userUid, lang);
//			} else if("getUserListByUserUid".equals(func)){
//				String userUids = (String)jsonObject.get("userUids");
//				result = ptSyncSvc.getUserListByUserUid(userUids, lang);
//			} else if("getLanguageList".equals(func)){
//				result = ptSyncSvc.getLanguageList(compId);
//			}
		} catch(Exception e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		
		if(result!=null && !result.isEmpty()){
			response.getOutputStream().write(result.getBytes());
			return null;
		}
		
		response.sendError(403);
		return null;
	}
}
