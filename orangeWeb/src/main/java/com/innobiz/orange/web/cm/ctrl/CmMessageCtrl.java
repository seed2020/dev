package com.innobiz.orange.web.cm.ctrl;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** AJAX를 통한 메세지 조회용 컨트롤러 */
@Controller
public class CmMessageCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CmMessageCtrl.class);

	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
    private MessageProperties messageProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 메세지 조회 */
	@RequestMapping(value = "/cm/msg/getMessageAjx", method = RequestMethod.GET)
	public String getMessageAjx(@RequestParam(value = "data", required = false) String data, HttpServletRequest request, ModelMap model){
		
		JSONObject object = (JSONObject)JSONValue.parse(data);
		String msgId = (String)object.get("msgId");
		String lang = (String)object.get("lang");
		String arg;
		
		try {
			Locale locale;
			if(lang==null || lang.isEmpty() || lang.equals("null")){
				locale = SessionUtil.getLocale(request);
			} else {
				locale = SessionUtil.toLocale(lang);
			}
			
			int p;
			Map<String, String> termMap;
			String setupClsId, setupId, termVa;
			
			String[] argArray = null;
			Object args = object.get("args");
			if(args!=null){
				if(args instanceof JSONArray){
					JSONArray jarray = (JSONArray)args;
					argArray = new String[jarray.size()];
					for(int i=0;i<argArray.length;i++){
						arg = jarray.get(i).toString();
						if(!arg.isEmpty() && arg.charAt(0)=='#') {
							// 용어설정된 것 치환
							if(arg.indexOf(".term.")>0){
								p = arg.lastIndexOf(".");
								setupClsId = arg.substring(1, p);
								setupId = arg.substring(p+1);
								termMap = ptSysSvc.getTermMap(setupClsId, locale.getLanguage());
								termVa = termMap==null ? null : termMap.get(setupId);
								
								if(termVa!=null){
									arg = termVa;
								} else {
									arg = messageProperties.getMessage(arg.substring(1), null, locale);
								}
							// 일반 메세지 치환
							} else {
								arg = messageProperties.getMessage(arg.substring(1), null, locale);
							}
						}
						argArray[i] = arg;
					}
				} else {
					arg = args.toString();
					if(!arg.isEmpty() && arg.charAt(0)=='#') {
						arg = messageProperties.getMessage(arg.substring(1), null, locale);
					}
					argArray = new String[] {arg};
				}
			}
			
			
			String message = messageProperties.getMessage(msgId, argArray, locale);
			if(message==null) message = "";
			model.put("message", message);
			
		} catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 용어 조회 */
	@RequestMapping(value = "/cm/msg/getTermAjx", method = RequestMethod.GET)
	public String getTermAjx(@RequestParam(value = "data", required = false) String data, HttpServletRequest request, ModelMap model){
		
		JSONObject object = (JSONObject)JSONValue.parse(data);
		String msgId = (String)object.get("msgId");
		
		try {
			int p = msgId.lastIndexOf('.');
			if(p>0){
				String langTypCd = SessionUtil.getLangTypCd(request);
				Map<String, String> termMap = ptSysSvc.getTermMap(msgId.substring(0, p), langTypCd);
				if(termMap!=null){
					String message = termMap.get(msgId.substring(p+1));
					if(message!=null){
						model.put("message", message);
						return JsonUtil.returnJson(model);
					}
				}
			}
		} catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		
		// 용어가 없으면 메세지 조회해서 리턴함
		return getMessageAjx(data, request, model);
	}
	
}
