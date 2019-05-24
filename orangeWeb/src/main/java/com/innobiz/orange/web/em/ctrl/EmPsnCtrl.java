package com.innobiz.orange.web.em.ctrl;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.wc.utils.WcConstant;

/** 개인설정  */
@Controller
public class EmPsnCtrl {		
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmPsnCtrl.class);
	
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
	@RequestMapping(value = "/wc/transUserSetup")
	public String transUserSetup(HttpServletRequest request,
			@RequestParam(value = "setupClsId", required = false) String setupClsId,
			@RequestParam(value = "cacheYn", required = true) String cacheYn,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {

		try{
			if(setupClsId==null || setupClsId.isEmpty()){
				if(request.getRequestURI().startsWith("/wc/")) setupClsId = WcConstant.USER_CONFIG;
			}
			
			if(setupClsId==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			ptPsnSvc.storeUserSetupToQueue(request, setupClsId, cacheYn, queryQueue);
			
			if(!queryQueue.isEmpty()){
				// 일괄실행
				commonSvc.execute(queryQueue);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				if(dialog!=null && !dialog.isEmpty())
					model.addAttribute("todo", "dialog.close('"+dialog+"')");
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
		
		return LayoutUtil.getResultJsp();
	}
	
}
