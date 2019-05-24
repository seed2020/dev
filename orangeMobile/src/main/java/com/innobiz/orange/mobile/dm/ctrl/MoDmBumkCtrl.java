package com.innobiz.orange.mobile.dm.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmBumkBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

@Controller
public class MoDmBumkCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoDmBumkCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 즐겨찾기 */
	
	/** 즐겨찾기 조회 */
	@RequestMapping(value = {"/dm/doc/listBumkFrm","/dm/doc/setBumkPop","/dm/doc/selectBumkPop"})
	public String selectBumkPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Bumk");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 즐겨찾기[개인] 조회
		List<DmBumkBVo> dmBumkBVoList = dmDocSvc.getDmBumkBVoList(langTypCd, "P", userVo, null);
		model.put("dmBumkBVoList", dmBumkBVoList);
		if(path.endsWith("Pop")){
			if(path.startsWith("set")){
				// 화면 구성용 2개의 빈 vo 넣음
				dmBumkBVoList.add(new DmBumkBVo());
				dmBumkBVoList.add(new DmBumkBVo());
				for (DmBumkBVo storedDmBumkBVo : dmBumkBVoList) {
					if (storedDmBumkBVo.getRescId() != null) {
						// 리소스기본(DM_RESC_B) 테이블 - 조회
						dmRescSvc.queryRescBVo(null, storedDmBumkBVo.getRescId(), model);
					}
				}
				model.put("isSelect", Boolean.FALSE);
			}else{
				model.put("isSelect", Boolean.TRUE);
			}
			return MoLayoutUtil.getJspPath("/dm/bumk/setBumkPop");
		}
		
		return MoLayoutUtil.getJspPath("/dm/bumk/listBumkFrm");
	}
	
	/** [AJAX] 즐겨찾기 */
	@RequestMapping(value = "/dm/doc/transBumkDocAjx")
	public String transBumkDocAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bumkId = (String) object.get("bumkId"); // 즐겨찾기ID
			String regCat = (String) object.get("regCat");
			String catVa = (String) object.get("catVa");
			String mode = (String) object.get("mode");
			if ((bumkId == null || bumkId.isEmpty()) || ((regCat == null || regCat.isEmpty()) || (catVa == null || catVa.isEmpty()))) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return MoLayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			QueryQueue queryQueue = new QueryQueue();

			String msgCode = "cm.msg.del.success";
			
			if("del".equals(mode)){
				dmDocSvc.saveBumkDoc(request, queryQueue, storId, bumkId, regCat, catVa, true);
			}else{
				msgCode = "cm.msg.save.success"; // 저장되었습니다.
				String[] bumkIds = bumkId.split(",");
				for(String id : bumkIds){
					dmDocSvc.saveBumkDoc(request, queryQueue, storId, id, regCat, catVa, false);
				}
			}
			
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage(msgCode, request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
