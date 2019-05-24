package com.innobiz.orange.web.ap.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApFormJspDVo;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 결재 옵션 관리 Ctrl (관리자용) */
@Controller
public class ApAdmOptCtrl {

//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(ApAdmOptCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 옵션관리 */
	@RequestMapping(value = "/ap/adm/opt/setOpt")
	public String setOpt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		String langTypCd = LoginSession.getLangTypCd(request);
		//서명방법코드
		List<PtCdBVo> signMthdCdList = ptCmSvc.getCdList("SIGN_MTHD_CD", langTypCd, "Y");
		model.put("signMthdCdList", signMthdCdList);
		
		model.put("months", ApConstant.MONTHS);
		model.put("days", ApConstant.DAYS);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("dmEnable"))){
			model.put("dmEnable", Boolean.TRUE);
		}
		
		if(userVo.getUserUid().equals("U0000001")) {
			// 대외공문
			ApFormJspDVo apFormJspDVo = new ApFormJspDVo();
			apFormJspDVo.setJspId("exDoc");
			@SuppressWarnings("unchecked")
			List<ApFormJspDVo> apFormJspDVoList = (List<ApFormJspDVo>)commonSvc.queryList(apFormJspDVo);
			if(apFormJspDVoList!=null && !apFormJspDVoList.isEmpty()) {
				model.put("apFormJspDVoList", apFormJspDVoList);
			}
		}
		
		return LayoutUtil.getJspPath("/ap/adm/opt/setOpt");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/ap/adm/opt/transOpt")
	public String transOpt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{

			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(ApConstant.AP_SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [대외공문:팝업] 양식별 대외공문용 JSP 매핑 테이블 */
	@RequestMapping(value = "/ap/adm/opt/setExtDocPop")
	public String setExtDocPop(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		ApFormJspDVo apFormJspDVo = new ApFormJspDVo();
		apFormJspDVo.setJspId("exDoc");
		@SuppressWarnings("unchecked")
		List<ApFormJspDVo> apFormJspDVoList = (List<ApFormJspDVo>)commonSvc.queryList(apFormJspDVo);
		if(apFormJspDVoList==null || apFormJspDVoList.isEmpty()){
			apFormJspDVoList = new ArrayList<ApFormJspDVo>();
		}
		
		apFormJspDVoList.add(new ApFormJspDVo());
		apFormJspDVoList.add(new ApFormJspDVo());
		apFormJspDVoList.add(new ApFormJspDVo());
		model.put("apFormJspDVoList", apFormJspDVoList);
		
		return LayoutUtil.getJspPath("/ap/adm/opt/setExtDocPop");
	}
	
	/** 대외공문 - 저장 */
	@RequestMapping(value = "/ap/adm/opt/transExtDoc")
	public String transExtDoc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String[] formIds = request.getParameterValues("formId");
		String[] jspPaths = request.getParameterValues("jspPath");
		
		QueryQueue queryQueue = new QueryQueue();
		
		ApFormJspDVo apFormJspDVo = new ApFormJspDVo();
		apFormJspDVo.setJspId("exDoc");
		queryQueue.delete(apFormJspDVo);
		
		if(formIds!=null && jspPaths!=null) {
			String formId, jspPath;
			for(int i=0; i<formIds.length && i<jspPaths.length; i++) {
				
				formId = formIds[i]==null ? null : formIds[i].trim();
				jspPath = jspPaths[i]==null ? null : jspPaths[i].trim();
				
				if(formId!=null && !formId.isEmpty() && jspPath!=null && !jspPath.isEmpty()) {
					
					apFormJspDVo = new ApFormJspDVo();
					apFormJspDVo.setJspId("exDoc");
					apFormJspDVo.setFormId(formId);
					apFormJspDVo.setJspPath(jspPath);
					queryQueue.insert(apFormJspDVo);
				}
			}
		}
		
		try {
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e) {
			model.put("message", e.getMessage());
		}
		
		
		
		return LayoutUtil.getResultJsp();
	}
}
