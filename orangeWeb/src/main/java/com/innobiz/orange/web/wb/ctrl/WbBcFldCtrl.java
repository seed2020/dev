package com.innobiz.orange.web.wb.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.wb.svc.WbBcFldSvc;
import com.innobiz.orange.web.wb.svc.WbBcRescSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.svc.WbConstant;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;
import com.innobiz.orange.web.wb.vo.WmRescBVo;

/** 명함관리 */
@Controller
public class WbBcFldCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcFldCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcRescSvc wbBcRescSvc;
	
	/** 폴더 서비스 */
	@Autowired
	private WbBcFldSvc wbBcFldSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 폴더 목록 조회 팝업 */
	@RequestMapping(value = {"/wb/findBcFldPop", "/wb/adm/findBcFldPop", "/wb/pub/findBcFldPop"})
	public String findBcFldPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wb/findBcFldPop");
	}
	
	/** 폴더 목록 조회 */
	@RequestMapping(value = {"/wb/findBcFldFrm", "/wb/adm/findBcFldFrm", "/wb/pub/findBcFldFrm"})
	public String findBcFldFrm(HttpServletRequest request,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		if(isPub) wbBcFldBVo.setOrderBy("FLD_ID, SORT_ORDR");
		else wbBcFldBVo.setOrderBy("BC_FLD_ID, SORT_ORDR");
		wbBcFldBVo.setQueryLang(langTypCd);
		
		wbBcFldBVo.setPub(isPub); // 공유명함여부
		
		if(schBcRegrUid != null && !"".equals(schBcRegrUid)){
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(schBcRegrUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
			wbBcFldBVo.setCompId(orUserBVo.getCompId());
			wbBcFldBVo.setRegrUid(schBcRegrUid);
			
		}else{
			//wbBcFldBVo.setCompId(userVo.getCompId());
			wbBcFldBVo.setRegrUid(userVo.getUserUid());
		}
		//wbBcFldBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<WbBcFldBVo> wbBcFldBVoList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);		
		model.put("wbBcFldBVoList", wbBcFldBVoList);
		
		return LayoutUtil.getJspPath("/wb/findBcFldFrm");
	}
	/** 폴더관리 */
	@RequestMapping(value = "/wb/setBcFld")
	public String setBcFld(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wb/setBcFld");
	}
	
	/** 폴더 조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = {"/wb/setFldPop", "/wb/pub/setFldPop"})
	public String setFldPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		String bcFldId = ParamUtil.getRequestParam(request, "bcFldId", false);
		
		// 대리자UID
		String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
					
		if(!"ROOT".equals(bcFldId) && !"NONE".equals(bcFldId)){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WbBcFldBVo tempWbBcFldBVo = new WbBcFldBVo();
			tempWbBcFldBVo.setCompId(userVo.getCompId());
			tempWbBcFldBVo.setBcFldId(bcFldId);
			tempWbBcFldBVo.setPub(isPub); // 공유명함여부
			
			// 작성자UID
			String userUid = userVo.getUserUid();
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){ // 대리명함
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty()) userUid = schBcRegrUid;
			}
			
			if(!isPub) {
				tempWbBcFldBVo.setRegrUid(userUid);
				tempWbBcFldBVo.setCompId(null);
			}
			tempWbBcFldBVo = (WbBcFldBVo)commonSvc.queryVo(tempWbBcFldBVo);
			String mode = ParamUtil.getRequestParam(request, "mode", false);
			if("mod".equals(mode)){
				model.put("wbBcFldBVo", tempWbBcFldBVo);
				
				if (isPub && tempWbBcFldBVo != null && tempWbBcFldBVo.getRescId() != null) {
					// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
					wbBcRescSvc.queryRescBVo(tempWbBcFldBVo.getRescId(), model);
				}
				
			}else{
				WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
				wbBcFldBVo.setAbvFldId(tempWbBcFldBVo.getBcFldId());
				wbBcFldBVo.setAbvFldNm(tempWbBcFldBVo.getFldNm());
				model.put("wbBcFldBVo", wbBcFldBVo);
			}
		}else{
			WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
			wbBcFldBVo.setAbvFldId("ROOT");
			model.put("wbBcFldBVo", wbBcFldBVo);
		}
		model.put("isPub", isPub);
		return LayoutUtil.getJspPath("/wb/setFldPop");
	}

	/** 폴더 저장 */
	@RequestMapping(value = {"/wb/transFld", "/wb/pub/transFld"})
	public String transFld(HttpServletRequest request,
			@Parameter(name="abvFldId", required=false) String abvFldId,
			@Parameter(name="bcFldId", required=false) String bcFldId,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(abvFldId==null || abvFldId.isEmpty() ){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - abvFldId:"+abvFldId+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 폴더 기본 테이블
			WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
			VoUtil.bind(request, wbBcFldBVo);
			wbBcFldBVo.setCompId(userVo.getCompId());
			wbBcFldBVo.setPub(isPub); // 공유명함여부
			//공유명함일 경우 다국어 처리
			
			if(isPub){
				WmRescBVo wmRescBVo = wbBcRescSvc.collectBaRescBVo(request, "", queryQueue);
				if (wmRescBVo == null) {
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				wbBcFldBVo.setFldNm(wmRescBVo.getRescVa());
				wbBcFldBVo.setRescId(wmRescBVo.getRescId());
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 대리자UID
			String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
			
			// 작성자UID
			String userUid = userVo.getUserUid();
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){ // 대리명함
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty()) userUid = schBcRegrUid;
			}
			
			if("reg".equals(mode)){
				// ID 생성
				wbBcFldBVo.setBcFldId(wbCmSvc.createId(isPub ? "WB_PUB_FLD_B" : "WB_BC_FLD_B"));
				wbBcFldBVo.setRegDt("sysdate");
				wbBcFldBVo.setRegrUid(userUid);
				 // 순서 구하기
				WbBcFldBVo sortVo = new WbBcFldBVo();
				sortVo.setInstanceQueryId("com.innobiz.orange.web.wb.dao.WbBcFldBDao.maxWbBcFldB");
				sortVo.setPub(isPub);
				if(!isPub) sortVo.setRegrUid(userUid);
				
				if(abvFldId!=null && !abvFldId.isEmpty()){
					sortVo.setAbvFldId(abvFldId);
				}
				Integer sortOrdr = commonSvc.queryInt(sortVo);
				if(sortOrdr==null) sortOrdr = 1;
				wbBcFldBVo.setSortOrdr(String.valueOf(sortOrdr));
				// 코드기본(PT_CD_B) 테이블 - INSERT
				queryQueue.insert(wbBcFldBVo);
			} else {
				wbBcFldBVo.setModDt("sysdate");
				wbBcFldBVo.setModrUid(userUid);
				// 코드기본(PT_CD_B) 테이블 - UPDATE
				queryQueue.update(wbBcFldBVo);
			}
			
			String cacheKey=isPub ? WbConstant.PUB : WbConstant.FLD; 
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, cacheKey);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(cacheKey);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadTree('"+wbBcFldBVo.getBcFldId()+"','');");
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 폴더 복사 , 이동  */
	@RequestMapping(value = {"/wb/transBcFldCopyAjx", "/wb/pub/transBcFldCopyAjx"})
	public String transBcFldCopyAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String bcFldId = (String)jsonObject.get("bcFldId");// 복사 또는 이동 대상 아이디
			String abvFldId = (String)jsonObject.get("abvFldId");// 복사 또는 이동시 상위 아이디
			
			String mode = (String)jsonObject.get("mode");// 복사 또는 이동 모드
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 대리자UID
			String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
						
			// 작성자UID
			String userUid = userVo.getUserUid();
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){ // 대리명함
				// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
				schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty()) userUid = schBcRegrUid;
			}
			
			 // 순서 구하기
			WbBcFldBVo sortVo = new WbBcFldBVo();
			sortVo.setInstanceQueryId("com.innobiz.orange.web.wb.dao.WbBcFldBDao.maxWbBcFldB");
			sortVo.setPub(isPub);
			if(!isPub) sortVo.setRegrUid(userUid);
			
			if(abvFldId!=null && !abvFldId.isEmpty()){
				sortVo.setAbvFldId(abvFldId);
			}
			Integer sortOrdr = commonSvc.queryInt(sortVo);
			if(sortOrdr==null) sortOrdr = 1;
			
			// 폴더 기본 테이블
			WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
			wbBcFldBVo.setCompId(userVo.getCompId());
			wbBcFldBVo.setBcFldId(bcFldId);
			wbBcFldBVo.setPub(isPub); // 공유명함여부
			if(!isPub) wbBcFldBVo.setRegrUid(userUid);
			wbBcFldBVo = (WbBcFldBVo)commonSvc.queryVo(wbBcFldBVo);
			if(wbBcFldBVo != null) wbBcFldBVo.setPub(isPub); // 공유명함여부
			if("copy".equals(mode)){// 복사 [폴더를 하나 생성하고 상위아이디를 저장] 
				wbBcFldBVo.setAbvFldId(abvFldId);
				// ID 생성
				wbBcFldBVo.setBcFldId(wbCmSvc.createId(isPub ? "WB_PUB_FLD_B" : "WB_BC_FLD_B"));
				wbBcFldBVo.setRegDt("sysdate");
				wbBcFldBVo.setRegrUid(userUid);
				wbBcFldBVo.setSortOrdr(String.valueOf(sortOrdr));
				queryQueue.insert(wbBcFldBVo);
			}else if("move".equals(mode)){// 이동 [상위 아이디를 변경]
				wbBcFldBVo.setAbvFldId(abvFldId);
				wbBcFldBVo.setModDt("sysdate");
				wbBcFldBVo.setModrUid(userUid);
				wbBcFldBVo.setSortOrdr(String.valueOf(sortOrdr));
				queryQueue.update(wbBcFldBVo);
			}
			
			String cacheKey=isPub ? WbConstant.PUB : WbConstant.FLD; 
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, cacheKey);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(cacheKey);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			model.put("bcFldId", wbBcFldBVo.getBcFldId());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 폴더 삭제 : 폴더 포함 명함도 삭제 */
	@RequestMapping(value = {"/wb/transBcFldDelAjx", "/wb/pub/transBcFldDelAjx"})
	public String transBcFldDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String bcFldId = (String)jsonObject.get("bcFldId");
			
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			
			String langTypCd = LoginSession.getLangTypCd(request);
			List<WbBcFldBVo> wbBcFldBList = wbBcFldSvc.getDownTreeList(userVo.getCompId(), bcFldId, langTypCd, true, isPub, null);
			
			if(wbBcFldBList.size() > 0 ){
				// 대리자UID
				String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
							
				// 작성자UID
				String userUid = userVo.getUserUid();
				if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){ // 대리명함
					// 대리명함 조회UID가 있을경우 명함 등록자UID로 세팅한다.
					schBcRegrUid = wbCmSvc.getSchBcRegrUid(schBcRegrUid, userVo, langTypCd, model);
					if(schBcRegrUid != null && !schBcRegrUid.isEmpty()) userUid = schBcRegrUid;
				}
				
				// 폴더 안의 명함 삭제
				WbBcBVo wbBcBVo = new WbBcBVo();
				wbBcBVo.setSchFldTypYn("A");
				wbBcBVo.setCompId(userVo.getCompId());
				wbBcBVo.setPub(isPub); // 공유명함여부
				if(!isPub) wbBcBVo.setModrUid(userUid);
				wbBcBVo.setWbBcFldBVo(wbBcFldBList);
				queryQueue.delete(wbBcBVo);
				
				// 폴더 삭제
				for(WbBcFldBVo storedWbBcFldBVo : wbBcFldBList){
					storedWbBcFldBVo.setCompId(userVo.getCompId());
					storedWbBcFldBVo.setPub(isPub); // 공유명함여부
					if(!isPub) storedWbBcFldBVo.setRegrUid(userUid);
					queryQueue.delete(storedWbBcFldBVo);
				}
			}
			
			String cacheKey=isPub ? WbConstant.PUB : WbConstant.FLD; 
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, cacheKey);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(cacheKey);

			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 폴더 트리 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wb/listBcFldFrm","/wb/listAgntBcFldFrm", "/wb/pub/listPubBcFldFrm"})
	public String listFldFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WbBcFldBVo wbBcFldBVo = new WbBcFldBVo();
		if(isPub) wbBcFldBVo.setOrderBy("FLD_ID, SORT_ORDR");
		else wbBcFldBVo.setOrderBy("BC_FLD_ID, SORT_ORDR");
		wbBcFldBVo.setQueryLang(langTypCd);
		wbBcFldBVo.setCompId(userVo.getCompId());
		wbBcFldBVo.setPub(isPub); // 공유명함여부
		
		boolean isSrch = false;
		// 목록
		List<WbBcFldBVo> wbBcFldBVoList = new ArrayList<WbBcFldBVo>();
		// 미분류 등록
		WbBcFldBVo noneVo = new WbBcFldBVo();
		noneVo.setBcFldId("NONE");
		noneVo.setAbvFldId("ROOT");
		noneVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request)); // 미분류
		noneVo.setSortOrdr("1");
		wbBcFldBVoList.add(noneVo);
				
		// 요청경로 세팅
		String path = wbCmSvc.getRequestPath(request, model , null);
		if(path.startsWith("listAgnt")){			
			String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
			if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){
				wbBcFldBVo.setRegrUid(schBcRegrUid);
				isSrch=true;
			}
		}else{
			isSrch = true;
			wbBcFldBVo.setRegrUid(userVo.getUserUid());
			wbBcFldBVo.setCompId(null);
		}
		if(isSrch){
			List<WbBcFldBVo> tmpList = (List<WbBcFldBVo>)commonSvc.queryList(wbBcFldBVo);
			if(tmpList!=null) wbBcFldBVoList.addAll(tmpList);
		}
		model.put("wbBcFldBVoList", wbBcFldBVoList);
		return LayoutUtil.getJspPath("/wb/bc/listFldFrm");
	}
}
