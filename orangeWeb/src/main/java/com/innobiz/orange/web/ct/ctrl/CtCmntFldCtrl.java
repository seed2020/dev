package com.innobiz.orange.web.ct.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtRescSvc;
import com.innobiz.orange.web.ct.vo.CtBlonCatDVo;
import com.innobiz.orange.web.ct.vo.CtCatBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtFncMbshAuthDVo;
import com.innobiz.orange.web.ct.vo.CtFncMngBVo;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 커뮤니티 */
@Controller
public class CtCmntFldCtrl {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
	/** 첨부파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private CtRescSvc ctRescSvc;
	

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtCmntFldCtrl.class);
	

	/** 카테고리관리  조회 */
	@RequestMapping(value = "/ct/adm/setCmCat")
	public String setCmCat(HttpServletRequest request,
			ModelMap model) throws Exception {
	
		return LayoutUtil.getJspPath("/ct/adm/setCmCat");
	}
		
	/** 폴더 트리 조회 */
	@RequestMapping(value = "/ct/adm/listCtFldFrm")
	public String listCtFldFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtCatBVo ctCatFldBVo = new CtCatBVo();
		ctCatFldBVo.setOrderBy("CAT_ID, CAT_ORDR");
		ctCatFldBVo.setQueryLang(langTypCd);
		ctCatFldBVo.setCompId(userVo.getCompId());
		ctCatFldBVo.setFldTypCd("F");
		ctCatFldBVo.setRegrUid(userVo.getUserUid());
		ctCatFldBVo.setLangTyp(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<CtCatBVo> ctCatFldBVoList = (List<CtCatBVo>)commonSvc.queryList(ctCatFldBVo);
		model.put("ctCatFldBVoList", ctCatFldBVoList);
		
		
		return LayoutUtil.getJspPath("/ct/adm/listCtFldFrm");
	}
	
	/** 폴더 조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/ct/adm/setFldPop")
	public String setFldPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String catId = request.getParameter("catId");
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtCatBVo tempCtFldBVo = new CtCatBVo();
		tempCtFldBVo.setCompId(userVo.getCompId());
		tempCtFldBVo.setCatId(catId);
		tempCtFldBVo.setRegrUid(userVo.getUserUid());
		tempCtFldBVo = (CtCatBVo)commonSvc.queryVo(tempCtFldBVo);
		
		String mode = request.getParameter("mode");
			if("mod".equals(mode)){
				if(tempCtFldBVo != null){
					ctRescSvc.queryRescBVo(tempCtFldBVo.getCatSubjRescId(), model);
					model.put("ctCtFldBVo", tempCtFldBVo);
				}
			}else{
				CtCatBVo ctCtFldBVo = new CtCatBVo();
				ctCtFldBVo.setCatPid("ROOT");
				model.put("ctCtFldBVo", ctCtFldBVo);
			}
			
		 	
		model.put("fnc", mode);
		return LayoutUtil.getJspPath("/ct/adm/setFldPop");
	}
	
	/** 카테고리 폴더 저장 */
	@RequestMapping(value = "/ct/adm/transFldSave")
	public String transFldSave(HttpServletRequest request,
			@Parameter(name="catPid", required=false) String catPid,
			@Parameter(name="catId", required=false) String catId,
			@Parameter(name="catSubjRescId", required=false) String catSubjRescId,
			@Parameter(name="extnOpenYn", required=false) String extnOpenYn,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(catPid==null || catPid.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - catPid:"+catPid+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 폴더 기본 테이블
			CtCatBVo ctcatFldBVo = new CtCatBVo();
//			VoUtil.bind(request, ctcatFldBVo);
			
			String catSubj = "catSubj";
			
			if("mod".equals(mode)) catSubj ="";
			
			
			QueryQueue queryQueue = new QueryQueue();
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			CtRescBVo ctRescBVo;
			
			// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, catSubj, langTypCdList);
			if(ctRescBVo != null){
				ctcatFldBVo.setCatSubjRescId(ctRescBVo.getRescId());
			}
			//======================================================================================================			
			
			ctcatFldBVo.setCompId(userVo.getCompId());
			if("reg".equals(mode)){
				// ID 생성
				ctcatFldBVo.setQueryLang(langTypCd);
				ctcatFldBVo.setCatId(ctCmSvc.createId("CT_CAT_B"));
				ctcatFldBVo.setRegDt(ctCmntSvc.currentDay());
				ctcatFldBVo.setRegrUid(userVo.getUserUid());
				ctcatFldBVo.setCompId(userVo.getCompId());
				ctcatFldBVo.setCatPid("ROOT");
				
				
				ctcatFldBVo.setFldTypCd("F");
				ctcatFldBVo.setChldYn("N");
				ctcatFldBVo.setExtnOpenYn(extnOpenYn);
				// 테이블 - INSERT
				
				
				queryQueue.insert(ctcatFldBVo);
			} else {
				ctcatFldBVo.setCatId(catId);
				
				ctcatFldBVo.setExtnOpenYn(extnOpenYn);
				ctcatFldBVo.setModDt(ctCmntSvc.currentDay());
				ctcatFldBVo.setModrUid(userVo.getUserUid());
				// 테이블 - UPDATE
				queryQueue.update(ctcatFldBVo);
			}
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadTree('"+ctcatFldBVo.getCatId()+"','');");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 폴더 삭제 : 분류가 있으면 미삭제 */
	@RequestMapping(value = "/ct/adm/transCtFldDelAjx")
	public String transCtFldDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String catId = (String)jsonObject.get("catId");
			
			if(catId==null || catId.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - catId:"+catId+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			
			CtCatBVo clsChkCtCatVo = new CtCatBVo();
			clsChkCtCatVo.setCompId(userVo.getCompId());
			clsChkCtCatVo.setFldTypCd("C");
			clsChkCtCatVo.setCatPid(catId);
			
			int clsChkCtCatCount = commonDao.count(clsChkCtCatVo);
			
			if(clsChkCtCatCount < 1){
				// 폴더 기본 테이블
				CtCatBVo ctCatFVo = new CtCatBVo();
				ctCatFVo.setCompId(userVo.getCompId());
				ctCatFVo.setCatId(catId);
				ctCatFVo.setFldTypCd("F");
				queryQueue.delete(ctCatFVo);
				
				ctCatFVo = (CtCatBVo) commonDao.queryVo(ctCatFVo);
				
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(ctCatFVo.getCatSubjRescId());
				queryQueue.delete(ctRescBVo);
				
				
				commonSvc.execute(queryQueue);
				// cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
			}else{
				//ct.msg.fld.fail.del = 하위 분류가 존재하므로 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("ct.msg.fld.fail.del", request));
				model.put("result", "");
			}
			
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** 분류 리스트 조회 */
	@RequestMapping(value = "/ct/adm/listCtClsFrm")
	public String listCtClsFrm(HttpServletRequest request,
			@Parameter(name="catId", required=false) String catId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtCatBVo ctCatFldBVo = new CtCatBVo();
		ctCatFldBVo.setOrderBy("CAT_ID, CAT_ORDR");
		ctCatFldBVo.setQueryLang(langTypCd);
		ctCatFldBVo.setCompId(userVo.getCompId());
		ctCatFldBVo.setFldTypCd("C");
		ctCatFldBVo.setRegrUid(userVo.getUserUid());
		ctCatFldBVo.setLangTyp(langTypCd);
		if(catId == null) catId = "ROOT";
		ctCatFldBVo.setCatPid(catId);
		@SuppressWarnings("unchecked")
		List<CtCatBVo> ctCatFldBVoList = (List<CtCatBVo>)commonSvc.queryList(ctCatFldBVo);
		model.put("ctCatFldBVoList", ctCatFldBVoList);
		
		
		return LayoutUtil.getJspPath("/ct/adm/listCtClsFrm");
	}
	
	/** 분류 조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/ct/adm/setClsPop")
	public String setClsPop(HttpServletRequest request,
			@Parameter(name="catPid", required=false) String catPid,
			@Parameter(name="clsId", required=false) String clsId,
			@Parameter(name="catSubjRescId", required=false) String catSubjRescId,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtCatBVo tempCtFldBVo = new CtCatBVo();
		tempCtFldBVo.setCompId(userVo.getCompId());
		tempCtFldBVo.setLangTyp(langTypCd);
		tempCtFldBVo.setCatId(clsId);
		tempCtFldBVo.setRegrUid(userVo.getUserUid());
		tempCtFldBVo = (CtCatBVo)commonSvc.queryVo(tempCtFldBVo);

		if("mod".equals(mode)){
			ctRescSvc.queryRescBVo(tempCtFldBVo.getCatSubjRescId(), model);
			tempCtFldBVo.setCatPidNm(tempCtFldBVo.getCatPidNm());
			model.put("ctCtFldBVo", tempCtFldBVo);
		}else{
			CtCatBVo ctCtFldBVo = new CtCatBVo();
			ctCtFldBVo.setCatPid(tempCtFldBVo.getCatId());
			ctCtFldBVo.setCatPidNm(tempCtFldBVo.getCatNm());
			model.put("ctCtFldBVo", ctCtFldBVo);
		}
		
		model.put("fnc", mode);
		return LayoutUtil.getJspPath("/ct/adm/setClsPop");
	}
	
	
	/** 분류 저장 */
	@RequestMapping(value = "/ct/adm/transClsSave")
	public String transClsSave(HttpServletRequest request,
			@Parameter(name="catId", required=false) String catId,
			@Parameter(name="catPid", required=false) String catPid,
			@Parameter(name="extnOpenYn", required=false) String extnOpenYn,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(catPid==null || catPid.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - catPid:"+catPid+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 폴더 기본 테이블
			CtCatBVo ctcatFldBVo = new CtCatBVo();
//			VoUtil.bind(request, ctcatFldBVo);
			String returnCatId = null;
			QueryQueue queryQueue = new QueryQueue();
			
			//등록일때는 catSubj
			String catSubj = "catSubj";
			//수정일때는 공백으로
			if("mod".equals(mode)) catSubj ="";
			
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			CtRescBVo ctRescBVo;
			
			// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, catSubj, langTypCdList);
			if(ctRescBVo != null){
				ctcatFldBVo.setCatSubjRescId(ctRescBVo.getRescId());
			}
			//======================================================================================================
			
			
			ctcatFldBVo.setCompId(userVo.getCompId());
			if("reg".equals(mode)){
				// ID 생성
				ctcatFldBVo.setQueryLang(langTypCd);
				ctcatFldBVo.setCatId(ctCmSvc.createId("CT_CAT_B"));
				ctcatFldBVo.setRegDt(ctCmntSvc.currentDay());
				ctcatFldBVo.setRegrUid(userVo.getUserUid());
				ctcatFldBVo.setCompId(userVo.getCompId());
				ctcatFldBVo.setCatPid(catPid);
				ctcatFldBVo.setFldTypCd("C");
				ctcatFldBVo.setChldYn("Y");
				ctcatFldBVo.setExtnOpenYn(extnOpenYn);
				// 코드기본(CT_CD_B) 테이블 - INSERT
				queryQueue.insert(ctcatFldBVo);
				returnCatId = ctcatFldBVo.getCatPid();
			} else {
				ctcatFldBVo.setCatId(catId);
				ctcatFldBVo.setExtnOpenYn(extnOpenYn);
				ctcatFldBVo.setModDt(ctCmntSvc.currentDay());
				ctcatFldBVo.setModrUid(userVo.getUserUid());
				// 코드기본(CT_CD_B) 테이블 - UPDATE
				queryQueue.update(ctcatFldBVo);
				
				returnCatId = catPid;
			}
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadClass('"+ returnCatId +"','');");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [AJAX] 분류 삭제 : 커뮤니티가 존재하면 미삭제 */
	@RequestMapping(value = "/ct/adm/transCtClsDelAjx")
	public String transCtClsDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			JSONArray catId = (JSONArray) jsonObject.get("catId");
			
			
			if(catId==null || catId.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - catId:"+catId+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			
			for(int i=0; i < catId.size(); i++){
				CtBlonCatDVo ctBlCatVo = new CtBlonCatDVo();
				ctBlCatVo.setCatId((String) catId.get(i));
				int ctBlCatCount = commonDao.count(ctBlCatVo);
				if(ctBlCatCount < 1){
					// 폴더 기본 테이블
					CtCatBVo ctCatFVo = new CtCatBVo();
					ctCatFVo.setCompId(userVo.getCompId());
					ctCatFVo.setCatId((String) catId.get(i));
					ctCatFVo.setFldTypCd("C");
					queryQueue.delete(ctCatFVo);
					
					ctCatFVo = (CtCatBVo) commonDao.queryVo(ctCatFVo);
					
					CtRescBVo ctRescBVo = new CtRescBVo();
					ctRescBVo.setRescId(ctCatFVo.getCatSubjRescId());
					
					queryQueue.delete(ctRescBVo);
				}
				
			}
			
			
			if(catId.size() == (queryQueue.size() / 2)){
				commonSvc.execute(queryQueue);
//				cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
				
			}else{
				//ct.msg.cls.fail.del = 하위 커뮤니티가 존재하므로 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("ct.msg.cls.fail.del", request));
				model.put("result", "");
				
			}
			
			
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티등록 및 수정 조회 화면*/
	@RequestMapping(value = "/ct/setCm")
	public String setCm(HttpServletRequest request, ModelMap model) throws Exception{
	
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//==========================================================================================
		//LEFT 카테고리 model.Put
		//==========================================================================================
		CtCatBVo ctCatFldBVo = new CtCatBVo();
		ctCatFldBVo.setOrderBy("CAT_ID, CAT_ORDR");
		ctCatFldBVo.setQueryLang(langTypCd);
		ctCatFldBVo.setCompId(userVo.getCompId());
		ctCatFldBVo.setRegrUid(userVo.getUserUid());
		ctCatFldBVo.setLangTyp(langTypCd);
		@SuppressWarnings("unchecked")
		List<CtCatBVo> ctCatFldBVoList = (List<CtCatBVo>)commonSvc.queryList(ctCatFldBVo);
		model.put("ctCatFldBVoList", ctCatFldBVoList);
		//==========================================================================================	
		String fnc = request.getParameter("fnc");
		
		String ctId = request.getParameter("ctId");
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		if(ctId == null){
			ctId = ctCmSvc.createId("CT_ESTB_B");
			
			ctEstbBVo.setCompId(userVo.getCompId());
			ctEstbBVo.setCtId(ctId);
			
		}else{
			
			//ctEstbBVo.setCompId(userVo.getCompId());
			ctEstbBVo.setCtId(ctId);
			ctEstbBVo.setLangTyp(langTypCd);
			
			ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			
			
			
			ctRescSvc.queryRescBVo(ctEstbBVo.getCtSubjResc(), model);
			
			CtBlonCatDVo ctBlonCatDVo = new CtBlonCatDVo();
			ctBlonCatDVo.setCompId(ctEstbBVo.getCompId());
			ctBlonCatDVo.setCtId(ctId);
			
			ctBlonCatDVo = (CtBlonCatDVo) commonDao.queryVo(ctBlonCatDVo);
			
			ctFileSvc.putFileListToModel(ctId, model, userVo.getCompId());
			
			model.put("catId", ctBlonCatDVo.getCatId());
		}
		model.put("ctEstbBVo", ctEstbBVo);
		model.put("ctIntro", ctEstbBVo.getCtItro());
		model.put("fnc", fnc);
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		String fncUid = request.getParameter("menuId");
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		if(fncUid!=null && fncUid.startsWith(PtConstant.MNU_GRP_REF_CT)){
			CtFncDVo ctFncDVo =  new CtFncDVo();
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtFncUid(fncUid);
			ctFncDVo.setLangTyp(langTypCd);
			CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
			if(CtFncDVo != null){
				model.put("menuTitle", CtFncDVo.getCtFncNm());
			}
		}
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
				
		return LayoutUtil.getJspPath("/ct/setCm");
	}
	
	/** 커뮤니티 기능등록 Fnc */
	@RequestMapping(value = "/ct/setCmFnc")
	public String setCmFnc(HttpServletRequest request, ModelMap model) throws Exception{
	
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
		String mng = request.getParameter("mng");
		String catId = request.getParameter("catId");
		
		CtFncMngBVo ctFncMngBVo = new CtFncMngBVo();
		ctFncMngBVo.setLangTyp(langTypCd);
		
		CtFncDVo ctFnc = new CtFncDVo();
		ctFnc.setCompId(ctEstbBVo.getCompId());
		ctFnc.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFnc);
		
		List<String> fncIdHideList = new ArrayList<String>();
		for(int i=0; i < ctFncList.size(); i++){
			
			fncIdHideList.add(ctFncList.get(i).getCtFncId());
		}
		
		if(fncIdHideList.size() != 0) ctFncMngBVo.setFncIdHideList(fncIdHideList);
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
		//	ctFncMngBVo.setCompId(userVo.getCompId());
		//}
		
		ctFncMngBVo.setCompId(ctEstbBVo.getCompId());
		
		@SuppressWarnings("unchecked")
		List<CtFncMngBVo> ctFncMngList = (List<CtFncMngBVo>) commonDao.queryList(ctFncMngBVo);
		
		model.put("ctFncMngList", ctFncMngList);
		
		model.put("catId", catId);
		model.put("ctId", ctId);
		model.put("mng", mng);
		//왼쪽 메뉴 
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		String fncUid = request.getParameter("menuId");
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		if(fncUid!=null && fncUid.startsWith(PtConstant.MNU_GRP_REF_CT)){
			CtFncDVo ctFncDVo =  new CtFncDVo();
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtFncUid(fncUid);
			ctFncDVo.setLangTyp(langTypCd);
			CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
			if(CtFncDVo != null){
				model.put("menuTitle", CtFncDVo.getCtFncNm());
			}
		}
		
		return LayoutUtil.getJspPath("/ct/setCmFnc");
	}
	
	/** 커뮤니티 기능 트리 조회 */
	@RequestMapping(value = "/ct/listCtFncFrm")
	public String listCtFncFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		QueryQueue queryQueue = new QueryQueue();
		CtFncDVo ctFncSystemFnd = new CtFncDVo();
		ctFncSystemFnd.setCompId(ctEstbBVo.getCompId());
		ctFncSystemFnd.setCtId(ctId);
		ctFncSystemFnd.setCtFncId("CTHOME");
		int ctFncCount = commonDao.count(ctFncSystemFnd);

		if(ctFncCount == 0){
			
			/**
			 * 최초 신규 개설 페이지 로딩시 커뮤니티홈, 회원정보검색, 커뮤니티관리, 탈퇴 강제 Insert
			 */
			
			List<String> ctFncSystemNm = new ArrayList<String>();
			List<String> ctFncSystemId = new ArrayList<String>();
		
			CtFncMngBVo ctFncMngBVo = new CtFncMngBVo();
			ctFncMngBVo.setLangTyp(langTypCd);
			ctFncMngBVo.setCtMngYn("N");
			ctFncMngBVo.setUseYn("Y");
			@SuppressWarnings("unchecked")
			List<CtFncMngBVo> ctFncMngList = (List<CtFncMngBVo>) commonDao.queryList(ctFncMngBVo);
			
			//커뮤니티 홈
			ctFncSystemId.add("CTHOME");
			ctFncSystemNm.add(null);
			for(CtFncMngBVo CtFncMngBVo : ctFncMngList)
			{
				if(CtFncMngBVo.getCtFncId().equals("CTHOME"))
				{
					ctFncSystemNm.set(0, CtFncMngBVo.getCtFncSubjRescId());
					break;
				}
			}
			
			//회원정보검색
			ctFncSystemId.add("CTGUESTSEARCH");
			ctFncSystemNm.add(null);
			for(CtFncMngBVo CtFncMngBVo : ctFncMngList)
			{
				if(CtFncMngBVo.getCtFncId().equals("CTGUESTSEARCH"))
				{
					ctFncSystemNm.set(1, CtFncMngBVo.getCtFncSubjRescId());
					break;
				}
			}
			
			//커뮤니티관리
			ctFncSystemId.add("CTMANAGEMENT");
			ctFncSystemNm.add(null);
			for(CtFncMngBVo CtFncMngBVo : ctFncMngList)
			{
				if(CtFncMngBVo.getCtFncId().equals("CTMANAGEMENT"))
				{
					ctFncSystemNm.set(2, CtFncMngBVo.getCtFncSubjRescId());
					break;
				}
			}
			
			//탈퇴
			ctFncSystemId.add("CTSECESSION");
			ctFncSystemNm.add(null);
			for(CtFncMngBVo CtFncMngBVo : ctFncMngList)
			{
				if(CtFncMngBVo.getCtFncId().equals("CTSECESSION"))
				{
					ctFncSystemNm.set(3, CtFncMngBVo.getCtFncSubjRescId());
					break;
				}
			}
			
			for(int i=1; i <= 4; i++){
				if(ctFncSystemNm.get(i-1) == null)
					continue;
				
				CtFncDVo ctFncSystemSave = new CtFncDVo();
				ctFncSystemSave.setCompId(ctEstbBVo.getCompId());
				ctFncSystemSave.setCtId(ctId);
				ctFncSystemSave.setCtFncId(ctFncSystemId.get(i-1));
				ctFncSystemSave.setCtFncOrdr(i);
				ctFncSystemSave.setCtFncTyp("2");
				//리소스명 복사, 생성해서 INSERT 
				ctFncSystemSave.setCtFncSubjRescId(ctRescSvc.collectCtRescCopy(request, queryQueue, ctFncSystemNm.get(i-1)));
				ctFncSystemSave.setOrdr(i);
				ctFncSystemSave.setCtFncPid("ROOT");
				ctFncSystemSave.setRegDt(ctCmntSvc.currentDay());
				ctFncSystemSave.setRegrUid(userVo.getUserUid());
				ctFncSystemSave.setCtFncUid("C" + ctCmSvc.createId("CT_FNC_D"));
				ctFncSystemSave.setCtFncLocStep("1");
				
				queryQueue.insert(ctFncSystemSave);
				
				//String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
				
				List<String> seculCdList = new ArrayList<String>();
				seculCdList.add("M");
				seculCdList.add("S");
				seculCdList.add("R");
				seculCdList.add("A");
				
				List<String> authCdList = new ArrayList<String>();
				if(i==1){
					authCdList.add("R/W/D");
					authCdList.add("R//");
					authCdList.add("R//");
					authCdList.add("R//");
				}else if(i==2){
					authCdList.clear();
					authCdList.add("R/W/D");
					authCdList.add("R/W/D");
					authCdList.add("//");
					authCdList.add("//");
				}else if(i==3){
					authCdList.clear();
					authCdList.add("R/W/D");
					authCdList.add("//");
					authCdList.add("//");
					authCdList.add("//");
				}else if(i==4){
					authCdList.clear();
					authCdList.add("R/W/D");
					authCdList.add("R/W/D");
					authCdList.add("R/W/D");
					authCdList.add("R/W/D");
				}
				
				
				
				for(int j=0; j < 4; j++){
					CtFncMbshAuthDVo ctFncMbshAuthMVo = new CtFncMbshAuthDVo();
					ctFncMbshAuthMVo.setCompId(ctEstbBVo.getCompId());
					ctFncMbshAuthMVo.setCtFncUid(ctFncSystemSave.getCtFncUid());
					
					ctFncMbshAuthMVo.setSeculCd(seculCdList.get(j));
					ctFncMbshAuthMVo.setAuthCd(authCdList.get(j));
					queryQueue.insert(ctFncMbshAuthMVo);
				}
				
				
			}
			commonDao.execute(queryQueue);
		}
		
		
		//=====================================================================
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setOrderBy("CT_FNC_ID, CT_FNC_ORDR, CT_FNC_LOC_STEP");
		ctFncDVo.setQueryLang(langTypCd);
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtId(ctId);
		ctFncDVo.setRegrUid(userVo.getUserUid());
		ctFncDVo.setLangTyp(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncDList = (List<CtFncDVo>)commonSvc.queryList(ctFncDVo);
		model.put("ctFncDList", ctFncDList);
//		model.put("ctFncUid", request.getParameter("ctFncUid")!=null?request.getParameter("ctFncUid"):"");
		
		return LayoutUtil.getJspPath("/ct/listCtFncFrm");
	}
	
	/** 폴더 조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/ct/setFncFldPop")
	public String setFncFldPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		String ctFncId = request.getParameter("ctFncId");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtFncDVo tempCtFncFldBVo = new CtFncDVo();
		tempCtFncFldBVo.setCompId(ctEstbBVo.getCompId());
		tempCtFncFldBVo.setCtId(ctId);
		tempCtFncFldBVo.setCtFncId(ctFncId);
		tempCtFncFldBVo.setRegrUid(userVo.getUserUid());
		tempCtFncFldBVo.setLangTyp(langTypCd);
		tempCtFncFldBVo = (CtFncDVo)commonSvc.queryVo(tempCtFncFldBVo);
		
		String mode = request.getParameter("mode");
			if("mod".equals(mode)){
				if(tempCtFncFldBVo != null){
					ctRescSvc.queryRescBVo(tempCtFncFldBVo.getCtFncSubjRescId(), model);
					model.put("ctCtFldBVo", tempCtFncFldBVo);
				}
			}else{
				CtFncDVo ctFncFldBVo = new CtFncDVo();
				ctFncFldBVo.setCtFncPid("ROOT");
				ctFncFldBVo.setCtId(ctId);
				ctFncFldBVo.setCtFncId(ctFncId);
				
				model.put("ctCtFldBVo", ctFncFldBVo);
			}
			
			
		model.put("fnc", mode);
		return LayoutUtil.getJspPath("/ct/setFncFldPop");
	}
	
	/** 기능 폴더 저장 */
	@RequestMapping(value = "/ct/transFncFldSave")
	public String transFncFldSave(HttpServletRequest request,
			@Parameter(name="ctId", required=false) String ctId,
			@Parameter(name="ctFncId", required=false) String ctFncId,
			@Parameter(name="catSubjRescId", required=false) String catSubjRescId,
			@Parameter(name="ctFncFldid", required=false) String ctFncFldid,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(ctId==null || ctId.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - ctId:"+ctId+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			//기능 폴더 기본 테이블
			CtFncDVo ctFncFldBVo = new CtFncDVo();
//			VoUtil.bind(request, ctcatFldBVo);
			
			String ctFncSubj = "ctFncSubj";
			
			if("mod".equals(mode)) ctFncSubj ="";
			
			
			QueryQueue queryQueue = new QueryQueue();
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			CtRescBVo ctRescBVo;
			
			// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			ctRescBVo = ctRescSvc.collectCtRescBVo(request, queryQueue, ctFncSubj, langTypCdList);
			if(ctRescBVo != null){
				ctFncFldBVo.setCtFncSubjRescId(ctRescBVo.getRescId());
			}
			//======================================================================================================			
			
			ctFncFldBVo.setCompId(ctEstbBVo.getCompId());
			if("reg".equals(mode)){
				// ID 생성
				ctFncFldBVo.setQueryLang(langTypCd);
				ctFncFldBVo.setCtId(ctId);
				ctFncFldBVo.setCompId(ctEstbBVo.getCompId());
				ctFncFldBVo.setCtFncId("CTFNCFOLDER");
				//부모 폴더 순서 
				CtFncDVo ctFncPidFldBVo = new CtFncDVo();
				ctFncPidFldBVo.setInstanceQueryId("countMaxCtFncOrdr");
				ctFncPidFldBVo.setCtId(ctId);
				ctFncPidFldBVo.setCompId(ctEstbBVo.getCompId());
				int ctPidOrdr =  commonDao.queryInt(ctFncPidFldBVo)!=null?commonDao.queryInt(ctFncPidFldBVo):4;
				//폴더 등록마다 ordr +1 
				ctFncFldBVo.setOrdr(ctPidOrdr + 1);
				//폴더는 3 기능은 2
				ctFncFldBVo.setCtFncTyp("3");
				ctFncFldBVo.setCtFncOrdr(ctPidOrdr + 1);
				ctFncFldBVo.setCtFncUid("C"+ctCmSvc.createId("CT_FNC_D"));
				ctFncFldBVo.setCtFncLocStep("1");
				ctFncFldBVo.setCtFncPid("ROOT");
				ctFncFldBVo.setRegDt(ctCmntSvc.currentDay());
				ctFncFldBVo.setRegrUid(userVo.getUserUid());
				
				// 테이블 - INSERT
				queryQueue.insert(ctFncFldBVo);
				
				List<String> seculCdList = new ArrayList<String>();
				seculCdList.add("M");
				seculCdList.add("S");
				seculCdList.add("R");
				seculCdList.add("A");
				
				List<String> authCdList = new ArrayList<String>();
				authCdList.add("R/W/D");
				authCdList.add("R//");
				authCdList.add("R//");
				authCdList.add("R//");
				
				for(int i=0; i < 4; i++){
					CtFncMbshAuthDVo ctFncMbshAuthMVo = new CtFncMbshAuthDVo();
					ctFncMbshAuthMVo.setCompId(ctEstbBVo.getCompId());
					ctFncMbshAuthMVo.setCtFncUid(ctFncFldBVo.getCtFncUid());
					
					ctFncMbshAuthMVo.setSeculCd(seculCdList.get(i));
					ctFncMbshAuthMVo.setAuthCd(authCdList.get(i));
					queryQueue.insert(ctFncMbshAuthMVo);
				}
				
			} else {
				ctFncFldBVo.setCtId(ctId);
				ctFncFldBVo.setCompId(ctEstbBVo.getCompId());
				ctFncFldBVo.setCtFncId(ctFncId);
				ctFncFldBVo.setCtFncUid(ctFncFldid);
				
				ctFncFldBVo.setModDt(ctCmntSvc.currentDay());
				ctFncFldBVo.setModrUid(userVo.getUserUid());
				// 테이블 - UPDATE
				queryQueue.update(ctFncFldBVo);
			}
			
			
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadTree('"+ctFncFldBVo.getCtFncUid()+"','');");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 기능 저장 */
	/** [AJAX] 기능 rightToLeft :  */
	@RequestMapping(value = "/ct/transFncRightToLeft")
	public String transFncRightToLeft(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {

		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			String ctId = ParamUtil.getRequestParam(request, "ctId", true);

			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			ctEstbBVo.setLangTyp(langTypCd);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String fncId = request.getParameter("fncId");
			String fncRescId = request.getParameter("fncRescId");
			String fncPid = request.getParameter("fncPid");
			String fncfldType = request.getParameter("fncfldType");
			
			//기능 폴더 기본 테이블
			CtFncDVo ctFncFldBVo = new CtFncDVo();
//			VoUtil.bind(request, ctcatFldBVo);
			
			QueryQueue queryQueue = new QueryQueue();
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 받아온 rescID를 새로운 rescID 생성하여 새로 만듬
			ctFncFldBVo.setCtFncSubjRescId(ctRescSvc.collectCtRescCopy(request, queryQueue, fncRescId));
			//======================================================================================================			
			
			ctFncFldBVo.setCompId(ctEstbBVo.getCompId());
			// ID 생성
			ctFncFldBVo.setQueryLang(langTypCd);
			ctFncFldBVo.setCtId(ctId);
			ctFncFldBVo.setCtFncId(fncId);
			//폴더 등록마다 ordr +1 
			//폴더는 3 기능은 2
			ctFncFldBVo.setCtFncTyp("2");
			ctFncFldBVo.setCtFncUid("C" + ctCmSvc.createId("CT_FNC_D"));
			int ctPidOrdr =0;
			
			//폴더 안으로 들어가는 경우
			if(fncfldType.equals("3")){
				ctFncFldBVo.setCtFncLocStep("2");
				//ctFncOrdr에 대한 순서 정하기
				CtFncDVo ctFncPidFldBVo = new CtFncDVo();
				ctFncPidFldBVo.setInstanceQueryId("countMaxCtFncOrdr");
				ctFncPidFldBVo.setCtId(ctId);
				ctFncPidFldBVo.setCompId(ctEstbBVo.getCompId());
				ctFncPidFldBVo.setCtFncPid(fncPid);
				ctPidOrdr =  commonDao.queryInt(ctFncPidFldBVo)!=null?commonDao.queryInt(ctFncPidFldBVo):0;
				
			}else{
				ctFncFldBVo.setCtFncLocStep("1");
				//ctFncOrdr에 대한 순서 정하기
				CtFncDVo ctFncPidFldBVo = new CtFncDVo();
				ctFncPidFldBVo.setInstanceQueryId("countMaxCtFncOrdr");
				ctFncPidFldBVo.setCtId(ctId);
				ctFncPidFldBVo.setCtFncPid("ROOT");
				ctFncPidFldBVo.setCompId(ctEstbBVo.getCompId());
				ctPidOrdr =  commonDao.queryInt(ctFncPidFldBVo)!=null?commonDao.queryInt(ctFncPidFldBVo):4;
			}
			
			ctFncFldBVo.setCtFncOrdr(ctPidOrdr +1);
			ctFncFldBVo.setCtFncPid(fncPid);
			ctFncFldBVo.setRegDt(ctCmntSvc.currentDay());
			ctFncFldBVo.setRegrUid(userVo.getUserUid());
			
			// 테이블 - INSERT
			queryQueue.insert(ctFncFldBVo);
			
			//String seculCd = ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
			
			List<String> seculCdList = new ArrayList<String>();
			seculCdList.add("M");
			seculCdList.add("S");
			seculCdList.add("R");
			seculCdList.add("A");
			
			List<String> authCdList = new ArrayList<String>();
			authCdList.add("R/W/D");
			authCdList.add("R/W/D");
			authCdList.add("R/W/");
			authCdList.add("R//");
			
			for(int i=0; i < 4; i++){
				CtFncMbshAuthDVo ctFncMbshAuthMVo = new CtFncMbshAuthDVo();
				ctFncMbshAuthMVo.setCompId(ctEstbBVo.getCompId());
				ctFncMbshAuthMVo.setCtFncUid(ctFncFldBVo.getCtFncUid());
				
				ctFncMbshAuthMVo.setSeculCd(seculCdList.get(i));
				ctFncMbshAuthMVo.setAuthCd(authCdList.get(i));
				queryQueue.insert(ctFncMbshAuthMVo);
			}

			commonSvc.execute(queryQueue);
			
			
			//cm.msg.save.success=저장 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadTree('"+ctFncFldBVo.getCtFncPid()+"','');");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 기능 삭제 */
	/** [AJAX] 기능 leftToRight :  */
	@RequestMapping(value = "/ct/transCtFncUidDelAjx")
	public String transCtFncUidDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String ctId  = (String) jsonObject.get("ctId");
			String ctFncUid  = (String) jsonObject.get("ctFncUid");
			
			
			if(ctId==null || ctId.isEmpty() && ctFncUid==null || ctFncUid.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - ctFncUid:"+ctFncUid+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
	
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtId(ctId);
			//ctFncDVo.setCompId(ctEstbBVo.getCompId());
			ctFncDVo.setCtFncUid(ctFncUid);
			
			queryQueue.delete(ctFncDVo);
			
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			
			if(ctFncDVo != null){
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(ctFncDVo.getCtFncSubjRescId());
				queryQueue.delete(ctRescBVo);
			}
			
			commonDao.execute(queryQueue);
			
			//cm.msg.del.success = 삭제 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 폴더,기능(이른변경) 조회 - 수정용 조회 */
	@RequestMapping(value = "/ct/setNmPop")
	public String setNmPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String fncRescId = request.getParameter("fncRescId");
		
		ctRescSvc.queryRescBVo(fncRescId, model);
		
//		ctRescBVo = (CtRescBVo) commonDao.queryVo(ctRescBVo);
		
		model.put("fncRescId", fncRescId);
		model.put("fnc", "mod");
		return LayoutUtil.getJspPath("/ct/setNmPop");
	}
	
	/** 기능 or 폴더 이름변경 저장*/
	@RequestMapping(value = "/ct/transReNameSave")
	public String transReNameSave(HttpServletRequest request,
			@Parameter(name="ctFncUid", required=false) String ctFncUid,
			@Parameter(name="ctId", required=false) String ctId,
			@Parameter(name="fncRescId", required=false) String fncRescId,
			ModelMap model) throws Exception {
		
		if(ctFncUid==null || ctFncUid.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - ctFncUid:"+ctFncUid+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 폴더 기본 테이블
			QueryQueue queryQueue = new QueryQueue();
			//======================================================================================================
			//다국어 처리
			//======================================================================================================
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			
			// 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			ctRescSvc.collectCtRescBVo(request, queryQueue, "", langTypCdList);
			//======================================================================================================			

			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadTree('"+ctFncUid+"','');");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 기능 이동 */
	/** [AJAX] 기능 up&Down :  */
	@RequestMapping(value = "/ct/transFncMove")
	public String transFncMove(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String ctId = ParamUtil.getRequestParam(request, "ctId", true);

			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String selFncUid  = (String) jsonObject.get("selFncUid");
			String selFncOrdr  = (String) jsonObject.get("selFncOrdr");
			String selChangeUid  = (String) jsonObject.get("selChangeUid");
			String selChangeOrdr  = (String) jsonObject.get("selChangeOrdr");
			
			
			if(ctId==null || ctId.isEmpty()
			&& selFncUid==null || selFncUid.isEmpty() 
			&& selFncOrdr==null || selFncOrdr.isEmpty()
			&& selChangeUid==null || selChangeUid.isEmpty()
			&& selChangeOrdr==null || selChangeOrdr.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - selFncUid:"+selFncUid+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
	
			CtFncDVo ctFncDVo = new CtFncDVo();
			
			ctFncDVo.setCtId(ctId);
			//ctFncDVo.setCompId(ctEstbBVo.getCompId());
			ctFncDVo.setCtFncUid(selFncUid);
			ctFncDVo.setCtFncOrdr(Integer.parseInt(selChangeOrdr));
			
			queryQueue.update(ctFncDVo);
			
			CtFncDVo ctFncChangeVo = new CtFncDVo();
			
			ctFncChangeVo.setCtId(ctId);
			ctFncChangeVo.setCompId(ctEstbBVo.getCompId());
			ctFncChangeVo.setCtFncUid(selChangeUid);
			ctFncChangeVo.setCtFncOrdr(Integer.parseInt(selFncOrdr));
			
			queryQueue.update(ctFncChangeVo);
			
			commonDao.execute(queryQueue);
			
			//cm.msg.del.success = 삭제 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
//			model.put("ctFncUid", selFncUid);
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 커뮤니티 생성 취소  */
	@RequestMapping(value = "/ct/ajaxCtCrtCancel")
	public String ajaxCtCrtCancel(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {

			//QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String menuId  = (String) jsonObject.get("menuId");
			String ctId  = (String) jsonObject.get("ctId");
			
			if(menuId==null || menuId.isEmpty() && ctId==null || ctId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - menuId:"+menuId+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			String menuUrl = null;
//			String ctHomeMenuId =  ctCmntSvc.getCtHomeMenuId(ctId);
			if(menuId.substring(0,2).equalsIgnoreCase(PtConstant.MNU_GRP_REF_CT)){
				
				menuUrl = "/ct/mng/listMngMain.do?menuId="+ menuId + "&ctId=" + ctId;
				model.put("result", "ct");
				model.put("menuUrl", menuUrl);
			}else{
				model.put("result", "ok");
				menuUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/ct/listCmReqs.do");
				model.put("menuUrl", menuUrl);
			}
			
			//ct.msg.dropOut.sucess = 커뮤니티 탈퇴 되었습니다.
//			model.put("message", messageProperties.getMessage("ct.msg.dropOut.sucess", request));
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 폴더 하위분류까지 삭제 :  */
	@RequestMapping(value = "/ct/transCtFncUidAllDelAjx")
	public String transCtFncUidAllDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String ctId = ParamUtil.getRequestParam(request, "ctId", true);

			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String ctFncUid  = (String) jsonObject.get("ctFncUid");
			
			
			if(ctId==null || ctId.isEmpty() && ctFncUid==null || ctFncUid.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("save cd[folder] - ctFncUid:"+ctFncUid+"  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			CtFncDVo ctFncChildDVo = new CtFncDVo();
			ctFncChildDVo.setCtId(ctId);
			ctFncChildDVo.setCompId(ctEstbBVo.getCompId());
			ctFncChildDVo.setCtFncPid(ctFncUid);
			@SuppressWarnings("unchecked")
			List<CtFncDVo> ctFncChildList = (List<CtFncDVo>) commonDao.queryList(ctFncChildDVo);
			
			for(CtFncDVo ctFncChild : ctFncChildList){
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(ctFncChild.getCtFncSubjRescId());
				queryQueue.delete(ctRescBVo);
			}
			
			queryQueue.delete(ctFncChildDVo);
	
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtId(ctId);
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtFncUid(ctFncUid);
			
			queryQueue.delete(ctFncDVo);
			
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			
			if(ctFncDVo != null){
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(ctFncDVo.getCtFncSubjRescId());
				queryQueue.delete(ctRescBVo);
			}
			
			commonDao.execute(queryQueue);
			
			//cm.msg.del.success = 삭제 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	
}
