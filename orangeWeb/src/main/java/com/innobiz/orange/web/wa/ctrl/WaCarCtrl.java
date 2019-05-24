package com.innobiz.orange.web.wa.ctrl;

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
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.st.svc.StRescSvc;
import com.innobiz.orange.web.wa.svc.WaImgSvc;
import com.innobiz.orange.web.wa.vo.WaCarImgDVo;
import com.innobiz.orange.web.wa.vo.WaCarKndLVo;

@Controller
public class WaCarCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WaCarCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private StCmSvc stCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "stRescSvc")
	private StRescSvc stRescSvc;
	
	/** 이미지 저장 서비스 */
	@Resource(name = "waImgSvc")
	private WaImgSvc waImgSvc;
	
	/** 사이트 관리 - 관리자 */
	@RequestMapping(value = "/wa/adm/car/setCar")
	public String setCar(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wa/car/setCar");
	}
	
	/** 등록 수정 */
	@RequestMapping(value = "/wa/adm/car/setCarPop")
	public String setCarPop(HttpServletRequest request,
			@Parameter(name="carKndNo", required=false) String carKndNo,
			ModelMap model) throws Exception {
		
		if(carKndNo!=null && !carKndNo.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			WaCarKndLVo waCarKndLVo = new WaCarKndLVo();
			waCarKndLVo.setQueryLang(langTypCd);
			waCarKndLVo.setCompId(userVo.getCompId());
			waCarKndLVo.setCarKndNo(carKndNo);
			waCarKndLVo = (WaCarKndLVo)commonSvc.queryVo(waCarKndLVo);
			if(waCarKndLVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("waCarKndLVo", waCarKndLVo);
		}
		return LayoutUtil.getJspPath("/wa/car/setCarPop");
	}
	
	/** 조회 */
	@RequestMapping(value = {"/wa/adm/car/listCarFrm"})
	public String listCarFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WaCarKndLVo waCarKndLVo = new WaCarKndLVo();
		VoUtil.bind(request, waCarKndLVo);
		waCarKndLVo.setQueryLang(langTypCd);
		waCarKndLVo.setCompId(userVo.getCompId());
		
		@SuppressWarnings("unchecked")
		List<WaCarKndLVo> waCarKndLVoList = (List<WaCarKndLVo>)commonSvc.queryList(waCarKndLVo);
		model.put("waCarKndLVoList", waCarKndLVoList);
		
		// 화면 구성용 2개의 빈 vo 넣음
		waCarKndLVoList.add(new WaCarKndLVo());
		waCarKndLVoList.add(new WaCarKndLVo());
		
		model.put("mode", "list");
		return LayoutUtil.getJspPath("/wa/car/listCar","Frm");
	}
	
	/** [히든프레임] 사이트 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wa/adm/car/transCarList")
	public String transCarList(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 카테고리기본(ST_CAT_B) 테이블 VO
			WaCarKndLVo waCarKndLVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String carKndNo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			WaCarImgDVo waCarImgDVo = null;
			for(i=0;i<delCds.length;i++){
				carKndNo = delCds[i];
				
				// 이미지 삭제
				waCarImgDVo = new WaCarImgDVo();
				waCarImgDVo.setCarKndNo(carKndNo);
				queryQueue.delete(waCarImgDVo);
				
				// 기본 테이블 - 삭제
				waCarKndLVo = new WaCarKndLVo();
				waCarKndLVo.setCarKndNo(carKndNo);
				waCarKndLVo.setCompId(userVo.getCompId());
				queryQueue.delete(waCarKndLVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 기본(ST_SITE_B) 테이블
			List<WaCarKndLVo> waCarKndLVoList = (List<WaCarKndLVo>)VoUtil.bindList(request, WaCarKndLVo.class, new String[]{"valid"});
			size = waCarKndLVoList==null ? 0 : waCarKndLVoList.size();
			
			String corpNo = ParamUtil.getRequestParam(request, "corpNo", false);
			
			for(i=0;i<size;i++){
				waCarKndLVo = waCarKndLVoList.get(i);
				waCarKndLVo.setCompId(userVo.getCompId());
				
				if(waCarKndLVo.getCarKndNo() == null || waCarKndLVo.getCarKndNo().isEmpty()){
					waCarKndLVo.setCarKndNo(commonSvc.nextVal("WA_CAR_KND_L").toString());
					if(corpNo!=null && !corpNo.isEmpty()) waCarKndLVo.setCorpNo(corpNo);
				}
				
				// 이미지 저장
				if(waCarKndLVo.getTmpImgId() != null && !waCarKndLVo.getTmpImgId().isEmpty()) waImgSvc.setCarImgDVo(request, queryQueue, waCarKndLVo.getCarKndNo(), waCarKndLVo.getTmpImgId());
				
				queryQueue.store(waCarKndLVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans();");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 이미지 선택 */
	@RequestMapping(value = {"/wa/driv/viewImagePop", "/wa/adm/car/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="carKndNo", required=false) String carKndNo,
			ModelMap model) throws Exception {
		
		if(carKndNo!=null && !carKndNo.isEmpty()){
			WaCarImgDVo waCarImgDVo = new WaCarImgDVo();
			waCarImgDVo.setCarKndNo(carKndNo);
			waCarImgDVo = (WaCarImgDVo)commonSvc.queryVo(waCarImgDVo);
			model.put("waCarImgDVo", waCarImgDVo);
		}
		if(request.getRequestURI().startsWith("/wa/adm/")) model.put("isView", Boolean.FALSE);
		return LayoutUtil.getJspPath("/wa/car/setImagePop");
	}
	
	/** [AJAX] 사이트 이미지 개별 삭제 */
	@RequestMapping(value = "/wa/adm/car/transCarImgDelAjx")
	public String transCarImgDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String carKndNo = (String) object.get("carKndNo");
			if ( carKndNo == null || carKndNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 이미지 삭제
			WaCarImgDVo waCarImgDVo = new WaCarImgDVo();
			waCarImgDVo.setCarKndNo(carKndNo);
			commonSvc.delete(waCarImgDVo);
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
	
	/** [AJAX] 차종 목록 조회 */
	@RequestMapping(value = {"/wa/driv/getCarKndListAjx", "/wa/adm/car/getCarKndListAjx"})
	public String getCarKndListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String corpNo = (String) object.get("corpNo");
			if (corpNo == null || corpNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			WaCarKndLVo waCarKndLVo = new WaCarKndLVo();
			waCarKndLVo.setQueryLang(langTypCd);
			waCarKndLVo.setCompId(userVo.getCompId());
			waCarKndLVo.setCorpNo(corpNo);
			
			@SuppressWarnings("unchecked")
			List<WaCarKndLVo> waCarKndLVoList = (List<WaCarKndLVo>)commonSvc.queryList(waCarKndLVo);
			model.put("waCarKndLVoList", waCarKndLVoList);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
