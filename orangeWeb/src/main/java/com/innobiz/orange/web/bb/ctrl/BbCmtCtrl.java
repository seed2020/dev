package com.innobiz.orange.web.bb.ctrl;

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

import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaCmtDVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/* 한줄답변 */
@Controller
public class BbCmtCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 한줄답변 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/bb/listCmtFrm", "/bb/adm/listCmtFrm"})
	public String listCmtFrm(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			ModelMap model) throws Exception {
		
		// 파라미터 검사
		if (bullId == null) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 한줄답변(BA_CMT_D) 테이블 - BIND
		BaCmtDVo baCmtDVo = new BaCmtDVo();
		VoUtil.bind(request, baCmtDVo);
		
		// 한줄답변(BA_CMT_D) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCmtDVo);
		model.addAttribute("recodeCount", recodeCount);
		PersonalUtil.setPaging(request, baCmtDVo, recodeCount);
		
		// 한줄답변(BA_CMT_D) 테이블 - SELECT
		List<BaCmtDVo> baCmtDVoList = (List<BaCmtDVo>) commonSvc.queryList(baCmtDVo);
		model.addAttribute("baCmtDVoList", baCmtDVoList);
		
		return LayoutUtil.getJspPath("/bb/listCmtFrm");
	}

	/** 게시물 저장 (사용자) */
	@RequestMapping(value = {"/bb/transCmtAjx", "/bb/adm/transCmtAjx"})
	public String transCmtAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			String cmt = (String) object.get("cmt");
			if (bullId == null || cmt == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			BaCmtDVo baCmtDVo = new BaCmtDVo();
			baCmtDVo.setCmtId(bbCmSvc.createCmtId());
			baCmtDVo.setBullId(Integer.valueOf(bullId));
			baCmtDVo.setCmt(cmt);
			baCmtDVo.setRegrUid(userVo.getUserUid());
			baCmtDVo.setRegDt("sysdate");
			queryQueue.insert(baCmtDVo);
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
		
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** 게시물 삭제 (사용자) */
	@RequestMapping(value = {"/bb/transCmtDelAjx","/bb/adm/transCmtDelAjx"})
	public String transCmtDelAjx(HttpServletRequest request,
	                          @RequestParam(value = "data", required = true) String data,
	                          ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			String cmtId = (String) object.get("cmtId");
			if (bullId == null || cmtId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			//UserVo userVo = LoginSession.getUser(request);
			
			// 한줄답변(BA_CMT_D) 테이블 - SELECT
			BaCmtDVo paramCmtVo = new BaCmtDVo();
			paramCmtVo.setCmtId(Integer.valueOf(cmtId));
			paramCmtVo.setBullId(Integer.valueOf(bullId));
			//paramCmtVo.setRegrUid(userVo.getUserUid());
			BaCmtDVo baCmtDVo = (BaCmtDVo) commonSvc.queryVo(paramCmtVo);
			
			if (baCmtDVo == null) {
				// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
				throw new CmException("cm.msg.errors.403", request);
			}
			
			// 한줄답변(BA_CMT_D) 테이블 - DELETE
			QueryQueue queryQueue = new QueryQueue();
			queryQueue.delete(paramCmtVo);
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
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
