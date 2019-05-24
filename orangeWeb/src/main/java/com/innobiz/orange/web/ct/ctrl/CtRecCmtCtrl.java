//package com.innobiz.orange.web.ct.ctrl;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.innobiz.orange.web.cm.exception.CmException;
//import com.innobiz.orange.web.cm.svc.CommonSvc;
//import com.innobiz.orange.web.cm.utils.JsonUtil;
//import com.innobiz.orange.web.cm.utils.LayoutUtil;
//import com.innobiz.orange.web.cm.utils.MessageProperties;
//import com.innobiz.orange.web.cm.utils.VoUtil;
//import com.innobiz.orange.web.cm.vo.QueryQueue;
//import com.innobiz.orange.web.ct.svc.CtCmSvc;
//import com.innobiz.orange.web.ct.vo.CtRecMastCmdDVo;
//import com.innobiz.orange.web.pt.secu.LoginSession;
//import com.innobiz.orange.web.pt.secu.UserVo;
//import com.innobiz.orange.web.pt.utils.PersonalUtil;
//
///* 한줄답변 */
//@Controller
//public class CtRecCmtCtrl {
//	
//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(CtRecMastCtrl.class);
//	
//	/** 메세지 */
//	@Autowired
//	private MessageProperties messageProperties;
//
//	/** 공통 DB 처리용 서비스 */
//	@Resource(name = "commonSvc")
//	private CommonSvc commonSvc;
//
//	/** 게시판 공통 서비스 */
//	@Autowired
//	private CtCmSvc ctCmSvc;
//
//	/** 한줄답변 목록 (사용자) */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/ct/pds/listCmtFrm")
//	public String listCmtFrm(HttpServletRequest request,
//			@RequestParam(value = "bullId", required = false) String bullId,
//			ModelMap model) throws Exception {
//		
//		// 파라미터 검사
//		if (bullId == null) {
//			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//			throw new CmException("pt.msg.nodata.passed", request);
//		}
//		String ctId = request.getParameter("ctId");
//		model.put("ctId", ctId);
//		
//		// 한줄답변(CT_REC_MAST_CMD_D) 테이블 - BIND
//		CtRecMastCmdDVo ctRecMastCmdDVo = new CtRecMastCmdDVo();
//		VoUtil.bind(request, ctRecMastCmdDVo);
//		
//		// 한줄답변(CT_REC_MAST_CMD_D) 테이블 - COUNT
//		Integer recodeCount = commonSvc.count(ctRecMastCmdDVo);
//		model.addAttribute("recodeCount", recodeCount);
//		PersonalUtil.setPaging(request, ctRecMastCmdDVo, recodeCount);
//		
//		// 한줄답변(CT_REC_MAST_CMD_D) 테이블 - SELECT
//		List<CtRecMastCmdDVo> ctCmtDVoList = (List<CtRecMastCmdDVo>) commonSvc.queryList(ctRecMastCmdDVo);
//		model.addAttribute("ctCmtDVoList", ctCmtDVoList);
//		
//		return LayoutUtil.getJspPath("/ct/pds/listCmtFrm");
//	}
//
//	/** 게시물 저장 (사용자) */
//	@RequestMapping(value = "/ct/pds/transCmtAjx")
//	public String transCmtAjx(HttpServletRequest request,
//			@RequestParam(value = "data", required = true) String data,
//			ModelMap model) {
//		
//		try {
//			// 파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			String bullId = (String) object.get("bullId");
//			String cmt = (String) object.get("cmt");
//			if (bullId == null || cmt == null) {
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
//			
//			// 세션의 사용자 정보
//			UserVo userVo = LoginSession.getUser(request);
//			
//			QueryQueue queryQueue = new QueryQueue();
//			CtRecMastCmdDVo ctRecMastCmdDVo = new CtRecMastCmdDVo();
//			ctRecMastCmdDVo.setCmtId(ctCmSvc.createBullCmtId());
//			ctRecMastCmdDVo.setBullId(Integer.valueOf(bullId));
//			ctRecMastCmdDVo.setCmt(cmt);
//			ctRecMastCmdDVo.setRegrUid(userVo.getUserUid());
//			ctRecMastCmdDVo.setRegDt("sysdate");
//			queryQueue.insert(ctRecMastCmdDVo);
//			
//			commonSvc.execute(queryQueue);
//			
//			// cm.msg.save.success=저장 되었습니다.
//			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
//			model.put("result", "ok");
//		
//		} catch (CmException e) {
//			model.put("message", e.getMessage());
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("message", e.getMessage());
//		}
//
//		return JsonUtil.returnJson(model);
//	}
//
//	/** 게시물 삭제 (사용자) */
//	@RequestMapping(value = "/ct/pds/transCmtDelAjx")
//	public String transCmtDelAjx(HttpServletRequest request,
//	                          @RequestParam(value = "data", required = true) String data,
//	                          ModelMap model) {
//		
//		try {
//			// 파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			String bullId = (String) object.get("bullId");
//			String cmtId = (String) object.get("cmtId");
//			if (bullId == null || cmtId == null) {
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
//			
//			// 세션의 사용자 정보
//			UserVo userVo = LoginSession.getUser(request);
//			
//			// 한줄답변(CT_REC_MAST_CMD_D) 테이블 - SELECT
//			CtRecMastCmdDVo ctRecMastCmdDVo = new CtRecMastCmdDVo();
//			ctRecMastCmdDVo.setCmtId(Integer.valueOf(cmtId));
//			ctRecMastCmdDVo.setBullId(Integer.valueOf(bullId));
//			ctRecMastCmdDVo.setRegrUid(userVo.getUserUid());
//			CtRecMastCmdDVo ctCmtDVo = (CtRecMastCmdDVo) commonSvc.queryVo(ctRecMastCmdDVo);
//			
//			if (ctCmtDVo == null) {
//				// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
//				throw new CmException("cm.msg.errors.403", request);
//			}
//			
//			// 한줄답변(CT_REC_MAST_CMD_D) 테이블 - DELETE
//			QueryQueue queryQueue = new QueryQueue();
//			queryQueue.delete(ctRecMastCmdDVo);
//			
//			commonSvc.execute(queryQueue);
//			
//			// cm.msg.del.success=삭제 되었습니다.
//			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
//			model.put("result", "ok");
//			
//		} catch (CmException e) {
//			model.put("message", e.getMessage());
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("message", e.getMessage());
//		}
//		
//		return JsonUtil.returnJson(model);
//	}
//
//}
