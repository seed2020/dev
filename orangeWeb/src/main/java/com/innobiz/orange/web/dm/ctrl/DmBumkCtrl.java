package com.innobiz.orange.web.dm.ctrl;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
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
public class DmBumkCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmBumkCtrl.class);
	
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
	/*@RequestMapping(value = "/dm/bumk/listBumkDoc")
	public String setFld(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/bumk/listBumkDoc");
	}*/
	
	/** 즐겨찾기 조회 */
	@RequestMapping(value = {"/dm/doc/listBumkFrm","/dm/doc/setBumkPop","/dm/doc/selectBumkPop"})
	public String treeFldFrm(HttpServletRequest request,
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
			return LayoutUtil.getJspPath("/dm/bumk/setBumkPop");
		}
		
		return LayoutUtil.getJspPath("/dm/bumk/listBumkFrm");
	}
	
	/** [히든프레임] 즐겨찾기 일괄 저장(왼쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/doc/transBumkList")
	public String transFldList(HttpServletRequest request,			
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 즐겨찾기기본(DM_BUMK_B) 테이블 VO
			DmBumkBVo dmBumkBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String bumkId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				bumkId = delCds[i];
				
				// 폴더기본(DM_FLD_B) 테이블 - 삭제
				dmBumkBVo = new DmBumkBVo();
				dmBumkBVo.setBumkId(bumkId);
				queryQueue.delete(dmBumkBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 즐겨찾기기본(DM_BUMK_B) 테이블
			List<DmBumkBVo> dmBumkBVoList = (List<DmBumkBVo>)VoUtil.bindList(request, DmBumkBVo.class, new String[]{"valid"});
			size = dmBumkBVoList==null ? 0 : dmBumkBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				dmRescSvc.collectDmRescBVoList(request, queryQueue, dmBumkBVoList, "valid", "rescId", "bumkNm", null);
			}
			
			for(i=0;i<size;i++){
				dmBumkBVo = dmBumkBVoList.get(i);
				// 등록자, 등록일시
				dmBumkBVo.setRegrUid(userVo.getUserUid());
				dmBumkBVo.setRegDt("sysdate");
				// 수정자, 수정일시
				dmBumkBVo.setModrUid(userVo.getUserUid());
				dmBumkBVo.setModDt("sysdate");
				
				if(dmBumkBVo.getBumkId() == null || dmBumkBVo.getBumkId().isEmpty()){
					dmBumkBVo.setBumkId(dmCmSvc.createId("DM_BUMK_B"));
					dmBumkBVo.setBumkCat("P");
				}
				queryQueue.store(dmBumkBVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			String menuId = ParamUtil.getRequestParam(request, "menuId", true);
			model.put("todo", "parent.reloadFrm('leftPage','./listBumkFrm.do?menuId="+menuId+"','setBumkPop');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
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
				return LayoutUtil.getResultJsp();
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
	
	/** [AJAX] 즐겨찾기삭제[전체] */
	@RequestMapping(value = "/dm/adm/doc/transDelDocBumkAjx")
	public String transDelDocBumkAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String regrUid = (String) object.get("regrUid");
			if ( regrUid == null || regrUid.isEmpty()) {
				LOGGER.error("[ERROR] fldId == null || fldId.isEmpty() || regrUid == null || regrUid.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 즐겨찾기VO
			DmBumkBVo dmBumkBVo = new DmBumkBVo();
			dmBumkBVo.setRegrUid(regrUid);
			
			// 즐겨찾기기본(DM_BUMK_B) 테이블
			@SuppressWarnings("unchecked")
			List<DmBumkBVo> dmBumkBVoList = (List<DmBumkBVo>)commonSvc.queryList(dmBumkBVo);
			for(DmBumkBVo storedDmBumkBVo : dmBumkBVoList){
				// 즐겨찾기목록 삭제
				dmDocSvc.saveBumkDoc(request, queryQueue, storId, storedDmBumkBVo.getBumkId(), null, null, true);
			}
			
			// 즐겨찾기기본 삭제
			queryQueue.delete(dmBumkBVo);
			
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
