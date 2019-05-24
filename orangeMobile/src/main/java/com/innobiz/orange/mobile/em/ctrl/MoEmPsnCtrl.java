package com.innobiz.orange.mobile.em.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.wc.utils.WcConstant;

/** SnS */
@Controller
public class MoEmPsnCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoEmPsnCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 개인설정 저장 */
	@RequestMapping(value = "/wc/transUserSetupAjx")
	public String transUserSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String setupClsId = (String)jsonObject.get("setupClsId");
			if(setupClsId==null || setupClsId.isEmpty()){
				if(request.getRequestURI().startsWith("/wc/")) setupClsId = WcConstant.USER_CONFIG;
			}
			if(setupClsId==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 파라미터 JSON String
			String paramList = (String)jsonObject.get("paramList");
			if(paramList == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String cacheYn = (String)jsonObject.get("cacheYn");
			if(cacheYn==null || cacheYn.isEmpty()) cacheYn="N";
			// JSON String 을 Map 으로 변환
			Map<String, String> paramMap = new ObjectMapper().readValue(paramList.toString(), new TypeReference<Map<String, String>>(){}) ;
			
			if(paramMap==null || paramMap.size()==0){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			Entry<String, String> entry;
			Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
			List<String[]> list = new ArrayList<String[]>();
			
			while(iterator.hasNext()){
				entry = iterator.next();
				list.add(new String[]{ entry.getKey(), entry.getValue() });
			}
			
			if(list.size()>0){
				UserVo userVo = LoginSession.getUser(request);
				
				QueryQueue queryQueue = new QueryQueue();
				ptPsnSvc.storeUserSetupToQueue(list, userVo.getUserUid(), setupClsId, cacheYn, queryQueue);
				
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
		} catch(SQLException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 개인설정 조회 */
	@RequestMapping(value = "/wc/getUserSetupAjx")
	public String getUserSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String setupClsId = (String)jsonObject.get("setupClsId");
			if(setupClsId==null || setupClsId.isEmpty()){
				if(request.getRequestURI().startsWith("/wc/")) setupClsId = WcConstant.USER_CONFIG;
			}
			if(setupClsId==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// Setup Id
			String setupId = (String)jsonObject.get("setupId");
			
			UserVo userVo = LoginSession.getUser(request);
			
			// 사용자 설정 정보 조회
			Map<String, String> userSetupMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), WcConstant.USER_CONFIG, false);
			
			if(userSetupMap!=null){
				if(setupId!=null && !setupId.isEmpty()) model.put("value", userSetupMap.get(setupId));
				else model.put("userSetupMap", userSetupMap);
			}
		} catch(SQLException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
