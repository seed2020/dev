package com.innobiz.orange.mobile.ct.ctrl;

import java.util.ArrayList;
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
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtPrSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.ct.vo.CtPrBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 커뮤니티 홍보마당 */
@Controller
public class MoCtPrCtrl {
	
	/** 커뮤니티 홍보마당 서비스 */
	@Autowired
	private CtPrSvc ctPrSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoCtPrCtrl.class);
	
	/** 커뮤니티 홍보마당 목록 */
	@RequestMapping(value = "/ct/pr/listPr")
	public String listPr(HttpServletRequest request,
			ModelMap model) throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtPrBVo ctPrBVo = new CtPrBVo();
		ctPrBVo.setCompId(userVo.getCompId());
		VoUtil.bind(request, ctPrBVo);
		
		//커뮤니티 홍보마당 목록 - COUNT
		Integer recodeCount = commonDao.count(ctPrBVo);
		PersonalUtil.setPaging(request, ctPrBVo, recodeCount);
		
		//커뮤니티 홍보마당 목록
		List<CtPrBVo> ctPrList = ctPrSvc.getCtPrVoList(ctPrBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctPrList", ctPrList);
		
		return MoLayoutUtil.getJspPath("/ct/notcpr/listPr");
		
	}
	
	/** 게시물 조회*/
	@RequestMapping(value = "/ct/pr/viewPr")
	public String viewPr(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 테이블
		CtPrBVo ctPrBVo = ctPrSvc.getCtPrBVo(Integer.parseInt(bullId));
		if (ctPrBVo == null) {
			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("ct.msg.bullNotExists", request);
		}
		model.put("ctPrBVo", ctPrBVo);
		if(ctPrBVo.getRegrUid().equalsIgnoreCase(userVo.getUserUid())){
			model.put("prRegr", "prRegr");
		}
		
		if(userVo.isAdmin() || userVo.isSysAdmin()){
			model.put("admin", "admin");
		}
		
		// 커뮤니티 명
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCompId(ctPrBVo.getCompId());
		ctEstbBVo.setCtId(ctPrBVo.getCtId());
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		model.put("ctEstbBVo", ctEstbBVo);
				
		//조회이력 저장
		if(ctPrSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
			ctPrSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		// 게시물첨부파일 리스트 model에 추가
		ctPrSvc.putFileListToModel(bullId, model, userVo.getCompId());

		return MoLayoutUtil.getJspPath("/ct/notcpr/viewPr");
		
	}
	
	/** 커뮤니티 홍보마당 게시물 수정용 조회 및 등록화면 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ct/pr/setPr")
	public String setPr(HttpServletRequest request,
			@RequestParam(value = "bullId", required = false) String bullId,	
			ModelMap model) throws Exception {
		
		CtPrBVo ctPrBVo = null;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//수정인 경우
		if(bullId != null && !bullId.isEmpty()){
			ctPrBVo = ctPrSvc.getCtPrBVo(Integer.parseInt(bullId));
			if(ctPrBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			model.put("ctPrBVo", ctPrBVo);
			
			// 날짜 초기화
			ctPrSvc.initBullRezvDt(ctPrBVo);
			ctPrSvc.initBullExprDt(ctPrBVo);
		}else{
			ctPrBVo = new CtPrBVo();
			// 날짜 초기화
			ctPrSvc.initBullRezvDt(ctPrBVo);
			ctPrSvc.initBullExprDt(ctPrBVo);
			
		}
		
		// 사용자가 회원으로 등록되어 있는 커뮤니티 목록 (마스터 & 회원)
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		ctMbshDVo.setUserUid(userVo.getUserUid());
		ctMbshDVo.setJoinStat("3");
		ctMbshDVo.setCompId(userVo.getCompId());
		List<CtMbshDVo> ctMbshList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		
		// 사용자가 회원으로 등록되어 있는 커뮤니티 목록 (마스터 & 회원) 
		// 승인 상태이면서, 활동중인 커뮤니티 목록( 폐쇄된 커뮤니티는 표시되지 않도록 해야함. )
		List<CtEstbBVo> ctUserMastList = new ArrayList<CtEstbBVo>();
		for(CtMbshDVo storeCtMbshVo : ctMbshList){
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCompId(storeCtMbshVo.getCompId());
			ctEstbBVo.setCtId(storeCtMbshVo.getCtId());
			ctEstbBVo.setQueryLang(langTypCd);
			ctEstbBVo.setLangTyp(langTypCd);
			ctEstbBVo.setCtActStat("A");
			ctEstbBVo.setCtStat("A");
			ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo != null){
				ctUserMastList.add(ctEstbBVo);
			}
		}
		
		model.put("ctUserMastList", ctUserMastList);
		
		// 게시물첨부파일 리스트 model에 추가
		ctFileSvc.putFileListToModel(bullId, model, userVo.getCompId());

		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		
		return MoLayoutUtil.getJspPath("/ct/notcpr/setPr");
		
	}
	
	/** 커뮤니티 홍보마당 저장 */
	@RequestMapping(value = "ct/pr/transSetCtPrSavePost")
	public String transSetCtPrSave(HttpServletRequest request, ModelMap model){
		UploadHandler uploadHandler = null;
		
		try{
			
			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			
			if (bullStatCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			if ("B".equals(bullStatCd) && bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer storedBullId = ctPrSvc.saveCtBullLVo(request, bullId, bullStatCd, queryQueue);
			
			// 게시파일 저장
			String ctFncUid = request.getParameter("menuId");
			ctFileSvc.saveNotcFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid);
			commonSvc.execute(queryQueue);
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
			}	
			if (goView) {
				model.put("todo", "$m.nav.prev(event, '/ct/pr/viewPr.do?menuId="+request.getParameter("menuId")+"&bullId="+bullId+"');");
			} else {
				model.put("todo", "$m.nav.prev(event, '/ct/pr/listPr.do?menuId="+request.getParameter("menuId")+"');");
			}
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		} 
		
		return MoLayoutUtil.getResultJsp();
	}
	
	/** 게시물 삭제 */
	@RequestMapping(value = "/ct/pr/transPrDel")
	public String transPrDel(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			if(bullId == null){
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 테이블
			CtPrBVo ctPrBVo = ctPrSvc.getCtPrBVo(Integer.parseInt(bullId));
			if (ctPrBVo == null) {
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			
			//게시물 삭제
			QueryQueue queryQueue = new QueryQueue();
			ctPrSvc.deletePr(Integer.parseInt(bullId), queryQueue);
			
			commonDao.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
}
