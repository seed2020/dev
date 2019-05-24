package com.innobiz.orange.mobile.ct.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.vo.CtCatBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.ct.vo.CtScrnSetupDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 커뮤니티 */
@Controller
public class MoCtCmntCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 

	/** 나의 커뮤니티 */
	@RequestMapping(value= "/ct/listMyCm")
	public String listMyCm(HttpServletRequest request, ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//조회조건 매핑
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setSignal("my");
		
		/* ======================================
		 * ctActStat 커뮤니티 활동 상태
		 * S : 신청중 / A : 활동중 / C : 폐쇄
		   ======================================*/
		List<String> ctActList = new ArrayList<String>();
		ctActList.add("A");
		if(ctEstbBVo.getSchClose() != null && ctEstbBVo.getSchClose().equals("Y")){
			ctActList.add("C");
		}
		ctEstbBVo.setCtActStatList(ctActList);
		List<String> joinStateList = new ArrayList<String>();
		joinStateList.add("1");
		joinStateList.add("3");		
		ctEstbBVo.setJoinStatList(joinStateList);
		Map<String, Object> rsltMap = ctCmntSvc.getMyCtMapList(request, ctEstbBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> myCtMapList = (List<Map<String, Object>>)rsltMap.get("myCtMapList");
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		//ctScrnSetupDVo.setCompId(userVo.getCompId());
		ctScrnSetupDVo.setLangTyp(langTypCd);
		for(Map<String, Object> storedRsltMap : myCtMapList){
			ctScrnSetupDVo.setCtId((String)storedRsltMap.get("ctId"));
			@SuppressWarnings("unchecked")
			List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
			if(ctScrnSetupList.size() > 0 && ctScrnSetupList.get(0).getImgFileId()!=null && !ctScrnSetupList.get(0).getImgFileId().equals("")){
				CtFileDVo conditionFileDvo=new CtFileDVo();
				conditionFileDvo.setFileId(Integer.parseInt(ctScrnSetupList.get(0).getImgFileId()));
				conditionFileDvo=(CtFileDVo) commonDao.queryVo(conditionFileDvo);
				model.put("imgFilePath_"+storedRsltMap.get("ctId"), conditionFileDvo.getSavePath());
			}
		}
		model.put("myCtMapList", myCtMapList);
		
		 
		model.put("logUserUid", ctEstbBVo.getLogUserUid());
		
		return MoLayoutUtil.getJspPath("/ct/listMyCm");
		
	}
	
	/** 전체 커뮤니티 */
	@RequestMapping(value = "/ct/listAllCm")
	public String listCm(HttpServletRequest request, ModelMap model) throws Exception{
		//조회조건 매핑
		CtCatBVo ctCatBVo = new CtCatBVo();
		ctCatBVo.setExtnOpenYn("Y");
		VoUtil.bind(request, ctCatBVo);
		Map<String, Object> rsltMap = ctCmntSvc.getCatCmntList(request, ctCatBVo);
		//ctCatBVo.setCatId(request.getParameter("catId"));
		
		model.put("catCmntList", rsltMap.get("catCmntList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> ctCatCtList = (List<CtEstbBVo>) rsltMap.get("catCmntList");
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		ctScrnSetupDVo.setLangTyp(langTypCd);
		for(CtEstbBVo storedCtEstbBVo : ctCatCtList){
			ctScrnSetupDVo.setCtId(storedCtEstbBVo.getCtId());
			@SuppressWarnings("unchecked")
			List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
			if(ctScrnSetupList.size() > 0 && ctScrnSetupList.get(0).getImgFileId()!=null && !ctScrnSetupList.get(0).getImgFileId().equals("")){
				CtFileDVo conditionFileDvo=new CtFileDVo();
				conditionFileDvo.setFileId(Integer.parseInt(ctScrnSetupList.get(0).getImgFileId()));
				conditionFileDvo=(CtFileDVo) commonDao.queryVo(conditionFileDvo);
				model.put("imgFilePath_"+storedCtEstbBVo.getCtId(), conditionFileDvo.getSavePath());
			}
		}
		//model.put("catId", request.getParameter("catId"));
		
		return MoLayoutUtil.getJspPath("/ct/listAllCm");
	}
	
	/** 커뮤니티 메인  */
	@RequestMapping(value = "/ct/viewCm")
	public String viewCm(HttpServletRequest request, ModelMap model) throws Exception{

		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");
		
		//================================================================================================
		//권한 체크 (강제로 주소 입력시 접근 권한)
		//================================================================================================
		CtMbshDVo ctMbshD = new CtMbshDVo();
		//ctMbshD.setCompId(userVo.getCompId());
		ctMbshD.setCtId(ctId);
		ctMbshD.setUserUid(userVo.getUserUid());
		
		ctMbshD = (CtMbshDVo) commonDao.queryVo(ctMbshD);
		
		if((ctMbshD == null?true:!ctMbshD.getJoinStat().equalsIgnoreCase("3")) ){
			if(ctMbshD != null){
				if(ctMbshD.getJoinStat().equalsIgnoreCase("1")){
					//ct.msg.access.joinWait = 가입 승인 중입니다. 가입 승인 후 사용하시기 바랍니다.
					model.put("message", messageProperties.getMessage("ct.msg.access.joinWait", request));
				}else{
					//ct.msg.access.close = 권한이 없거나, 잘못된 접근 입니다. \n 다시 확인 후 재시도 하십시오.
					model.put("message", messageProperties.getMessage("ct.msg.access.close", request));
				}
			}else{
				//ct.msg.access.close = 권한이 없거나, 잘못된 접근 입니다. \n 다시 확인 후 재시도 하십시오.
				model.put("message", messageProperties.getMessage("ct.msg.access.close", request));
			}
			//공통 처리 페이지
			return MoLayoutUtil.getResultJsp();
		}
		
		//커뮤니티 가입 현황 전체/오늘
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);

		//방문기록
		ctCmntSvc.setVistHst(userVo, ctId, "H" , null);
		
		model.put("fileSum", ctCmntSvc.getFileAttCalcSum(ctId));

		return MoLayoutUtil.getJspPath("/ct/viewCm", "ct");
	}
	
	
	/** 회원 정보 검색 페이지 */
	@RequestMapping(value = "/ct/listMbsh")
	public String listMbsh(HttpServletRequest request, ModelMap model)throws Exception{
		// 세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		VoUtil.bind(request, ctMbshDVo);
		ctMbshDVo.setQueryLang(langTypCd);
		ctMbshDVo.setLangTyp(langTypCd);
		ctMbshDVo.setJoinStat("3");
		ctMbshDVo.setCtId(ctId);
		
		//================================================================================================
		//커뮤니티 가입 현황 전체/오늘 (model에 allPeople, todayPeople를 put한다.)
		//================================================================================================
		ctCmntSvc.putJoinPeopleStat(request, model, ctId);
		//================================================================================================
		
		Integer recodeCount = commonDao.count(ctMbshDVo);
		PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctMbshList", ctMbshList);
		model.put("ctId", ctId);
		
		return MoLayoutUtil.getJspPath("/ct/listMbsh", "ct");
	}
	
	/** 커뮤니티 회원 정보 POP */
	@RequestMapping(value = "/ct/dropOut")
	public String dropOut(HttpServletRequest request, ModelMap model)throws Exception{
		String menuId = request.getParameter("menuId");
		//String ctId = request.getParameter("ctId");

		model.put("menuId", menuId);
		return MoLayoutUtil.getJspPath("/ct/dropOutPop");
	}
	
	
	/** 커뮤니티 탈퇴 */
	@RequestMapping(value = "/ct/transMbshDropOut")
	public String transMbshDropOut(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			
			if (ctId == null || ctId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
			
			if(seculCd.equalsIgnoreCase("M")){
				//ct.msg.mast.dropOut.fail = 마스터는 해당 커뮤니티에 탈퇴 할 수 없습니다. 
				model.put("message", messageProperties.getMessage("ct.msg.mast.dropOut.fail", request));
				model.put("result", "fail");
			}else{
				CtEstbBVo ctEstbBVo = new CtEstbBVo();
				ctEstbBVo.setCtId(ctId);
				
				ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
				if(ctEstbBVo==null){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				//사용자 권한에 따른 url에 대한 menuId를 가져온다.
				String menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/ct/listMyCm.do");
				
				CtMbshDVo ctMbshDVo = new CtMbshDVo();
				ctMbshDVo.setCtId(ctId);
				ctMbshDVo.setCompId(ctEstbBVo.getCompId());
				ctMbshDVo.setUserUid(userVo.getUserUid());
				
				queryQueue.delete(ctMbshDVo);			
				commonDao.execute(queryQueue);
				
				//ct.msg.dropOut.sucess = 커뮤니티 탈퇴 되었습니다.
				model.put("message", messageProperties.getMessage("ct.msg.dropOut.sucess", request));
				model.put("result", "ok");
				model.put("menuUrl", menuUrl);
			}
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 [관리] 커뮤니티 회원관리 */
	@RequestMapping(value = "/ct/mng/listMngJoinReqs")
	public String listMngJoinReqs(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		ctMbshDVo.setCtId(ctId);
		ctMbshDVo.setCompId(ctEstbBVo.getCompId());
		ctMbshDVo.setJoinStat("1");
		ctMbshDVo.setLangTyp(langTypCd);
		
		VoUtil.bind(request, ctMbshDVo);
		
		Map<String, Object> rsltMap = ctCmntSvc.getMngJoinReqsList(request, ctMbshDVo);
		
		model.put("ctMngJoinReqsList", rsltMap.get("ctMngJoinReqsList"));
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctId", ctId);

		return MoLayoutUtil.getJspPath("/ct/listMngJoinReqs", "ct");
	}
	
	/** 커뮤니티 가입 페이지*/
	@RequestMapping(value = "/ct/setCmJoinPop")
	public String setCmJoinPop(HttpServletRequest request, ModelMap model) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtEstbBVo joinCtEstbBVo = new CtEstbBVo();
		joinCtEstbBVo.setCompId(userVo.getCompId());
		joinCtEstbBVo.setCtId(request.getParameter("ctId"));
		joinCtEstbBVo.setLangTyp(langTypCd);
		joinCtEstbBVo = (CtEstbBVo) commonDao.queryVo(joinCtEstbBVo);

		model.put("joinCtEstbBVo", joinCtEstbBVo);
		model.put("signal", request.getParameter("signal"));
		model.put("catId", request.getParameter("catId"));
		return MoLayoutUtil.getJspPath("/ct/setCmJoinPop");
	}
	
	/** 커뮤니티 가입 */
	@RequestMapping(value = "/ct/setCmntJoin")
	public String setCmntJoin(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String ctId  = (String) object.get("ctId");
			if (ctId == null || ctId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			UserVo userVo = LoginSession.getUser(request);
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setCtId(ctId);
			ctMbshDVo.setUserUid(userVo.getUserUid());
			ctCmntSvc.setCtMbshJoin(request, ctMbshDVo);
			
			// "ct.msg.join.success" 가입 신청이 완료되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.join.success", request));
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 가입승인 */
	@RequestMapping(value = "/ct/mng/transMbshApvd")
	public String transMbshApvd(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		try{

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId  = (String) object.get("mbshCtId");
			String mbshId  = (String) object.get("mbshId");
			if (mbshCtId == null || mbshCtId.isEmpty() && mbshId == null || mbshId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setUserUid(mbshId);
			ctMbshDVo.setJoinStat("3");
			
			queryQueue.update(ctMbshDVo);
						
			commonDao.execute(queryQueue);
					
			//ct.msg.mbshApvd = 커뮤니티 가입이 승인 처리 되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.mbshApvd", request));
			model.put("result", "ok");
				
			model.put("mbshCtId", mbshCtId);	
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 가입미승인 */
	@RequestMapping(value = "/ct/mng/transMbshNapvd")
	public String transMbshNapvd(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mbshCtId  = (String) object.get("mbshCtId");
			String mbshId  = (String) object.get("mbshId");
			if (mbshCtId == null || mbshCtId.isEmpty() && mbshId == null || mbshId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCtId(mbshCtId);
			ctMbshDVo.setCompId(userVo.getCompId());
			ctMbshDVo.setUserUid(mbshId);
			ctMbshDVo.setJoinStat("2");
			
			queryQueue.update(ctMbshDVo);
						
			commonDao.execute(queryQueue);
					
			//ct.msg.mbshNapvd = 커뮤니티 가입이 미승인 처리 되었습니다.
			model.put("message", messageProperties.getMessage("ct.msg.mbshNapvd", request));
			model.put("result", "ok");
				
			model.put("mbshCtId", mbshCtId);	
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
}
