package com.innobiz.orange.web.ct.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtRescSvc;
import com.innobiz.orange.web.ct.svc.CtScdManagerSvc;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlCatClsBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;


/** 자원관리 */
@Controller
public class CtSchdlAdmCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtSchdlAdmCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 일정관리 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private CtRescSvc ctRescSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private CtScdManagerSvc ctScdManagerService;
	
	/** 카테고리 목록 조회 */
	@RequestMapping(value = "/ct/schdl/adm/listCatCls")
	public String listCatCls(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		CtSchdlCatClsBVo searchVO = new CtSchdlCatClsBVo();
		VoUtil.bind(request, searchVO);
		searchVO.setQueryLang(langTypCd);
		searchVO.setOrderBy("SORT_ORDR");
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		//if(!isSysAdmin){
			searchVO.setCompId(userVo.getCompId());
		//}
		
		@SuppressWarnings("unchecked")
		List<CtSchdlCatClsBVo> CtSchdlCatClsBVoList = (List<CtSchdlCatClsBVo>)commonSvc.queryList(searchVO);
		
		// UI 구성용 - 빈 VO 하나 더함
		if(CtSchdlCatClsBVoList == null) CtSchdlCatClsBVoList = new ArrayList<CtSchdlCatClsBVo>();
		CtSchdlCatClsBVoList.add(new CtSchdlCatClsBVo());
		CtSchdlCatClsBVoList.add(new CtSchdlCatClsBVo());
		model.put("CtSchdlCatClsBVoList", CtSchdlCatClsBVoList);
		
		// 리소스 조회
		CtRescBVo ctRescBVo = new CtRescBVo();
		
		@SuppressWarnings("unchecked")
		List<CtRescBVo> ctRescBVoList = (List<CtRescBVo>)commonSvc.queryList(ctRescBVo);
		
		// 코드 리소스 model에 저장
		if(ctRescBVoList!=null){
			for(CtRescBVo storedCtRescBVo : ctRescBVoList){
				model.put(storedCtRescBVo.getRescId()+"_"+storedCtRescBVo.getLangTypCd(), storedCtRescBVo.getRescVa());
			}
		}
		return LayoutUtil.getJspPath("/ct/schdl/adm/listCatCls");
	}
	
	/** 카테고리 등록 수정 (저장) */
	@RequestMapping(value = "/ct/schdl/adm/transCatCls")
	public String transCatCls(HttpServletRequest request,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//  삭제 목록 처리
			int i, size;
			String[] delIds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			if(delIds.length > 0 ){
				for(String catId : delIds){
					CtSchdlCatClsBVo delCtSchdlCatClsBVo = new CtSchdlCatClsBVo();
					delCtSchdlCatClsBVo.setCompId(userVo.getCompId());
					delCtSchdlCatClsBVo.setCatId(catId);
					ctScdManagerService.deleteCatCls(queryQueue, delCtSchdlCatClsBVo);
					
				}
			}
			
			// 카테고리관리(WC_CAT_CLS_B) 테이블
			@SuppressWarnings("unchecked")
			List<CtSchdlCatClsBVo> CtSchdlCatClsBVoList = (List<CtSchdlCatClsBVo>)VoUtil.bindList(request, CtSchdlCatClsBVo.class, new String[]{"valid"});
			size = CtSchdlCatClsBVoList==null ? 0 : CtSchdlCatClsBVoList.size();
			CtSchdlCatClsBVo CtSchdlCatClsBVo = new CtSchdlCatClsBVo();
			// 리소스 정보 queryQueue에 담음
			ctRescSvc.collectCtRescBVoList(request, queryQueue, CtSchdlCatClsBVoList, "valid", "rescId", "catNm");
			
			for(i=0;i<size;i++){
				CtSchdlCatClsBVo = CtSchdlCatClsBVoList.get(i);
				if(CtSchdlCatClsBVo.getCatId()==null || CtSchdlCatClsBVo.getCatId().isEmpty()){
					CtSchdlCatClsBVo.setCatId(ctCmSvc.createId("CT_SCHDL_CAT_CLS_B"));
					CtSchdlCatClsBVo.setCompId(userVo.getCompId());
					CtSchdlCatClsBVo.setRegDt("sysdate");
					CtSchdlCatClsBVo.setRegrUid(userVo.getUserUid());
//					queryQueue.insert(CtSchdlCatClsBVo);
				} else {
					CtSchdlCatClsBVo.setModDt("sysdate");
					CtSchdlCatClsBVo.setModrUid(userVo.getUserUid());
					queryQueue.update(CtSchdlCatClsBVo);
				}
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}

}
