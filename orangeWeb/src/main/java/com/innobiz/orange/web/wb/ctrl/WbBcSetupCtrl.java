package com.innobiz.orange.web.wb.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcLstSetupBVo;
import com.innobiz.orange.web.wb.vo.WbBcUserLstSetupRVo;
import com.innobiz.orange.web.wb.vo.WbBcUserScrnSetupRVo;

/** 명함관리 환경설정*/
@Controller
public class WbBcSetupCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcSetupCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 명함 관리 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

/** 환경설정 START */
	
	/** 환경설정 등록 수정 화면 출력 */
	@RequestMapping(value = "/wb/setBcScrn")
	public String setBcScrn(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		//String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();	
		wbBcLstSetupBVo.setCompId(userVo.getCompId());
		
		//기본 목록설정항목 조회[회사별]
		//@SuppressWarnings("unchecked")
		//List<WbBcLstSetupBVo> wbBcLstSetupBVoList = (List<WbBcLstSetupBVo>)commonSvc.queryList(wbBcLstSetupBVo);
		List<WbBcLstSetupBVo> wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		
		//관리자 목록설정이 없을경우[기본항목으로 초기화한다.]
		//if(wbBcLstSetupBVoList.size() == 0 ){
		//	wbBcLstSetupBVoList = wbBcSvc.setWbBcLstSetupInit(wbBcLstSetupBVoList , null);
		//}
		
		model.put("wbBcLstSetupBVoList", wbBcSvc.setWbBcLstSetupInit(wbBcLstSetupBVoList , null));
		
		//화면설정 조회[입력창 위치 , 목록구분코드]
		WbBcUserScrnSetupRVo wbBcUserScrnSetupRVo = new WbBcUserScrnSetupRVo();
		wbBcUserScrnSetupRVo.setCompId(userVo.getCompId());//회사추가
		wbBcUserScrnSetupRVo.setRegrUid(userVo.getUserUid());
		wbBcUserScrnSetupRVo = (WbBcUserScrnSetupRVo)commonSvc.queryVo(wbBcUserScrnSetupRVo);
		model.put("wbBcUserScrnSetupRVo", wbBcUserScrnSetupRVo);
		
		//사용자 목록설정항목 조회
		WbBcUserLstSetupRVo wbBcUserLstSetupRVo = new WbBcUserLstSetupRVo();
		wbBcUserLstSetupRVo.setCompId(userVo.getCompId());//회사추가
		wbBcUserLstSetupRVo.setRegrUid(userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<WbBcUserLstSetupRVo> wbBcUserLstSetupRVoList = (List<WbBcUserLstSetupRVo>)commonSvc.queryList(wbBcUserLstSetupRVo);
		// UI 구성용 - 빈 VO 하나 더함
		if(wbBcUserLstSetupRVoList == null) wbBcUserLstSetupRVoList = new ArrayList<WbBcUserLstSetupRVo>();
		wbBcUserLstSetupRVoList.add(new WbBcUserLstSetupRVo());
		model.put("wbBcUserLstSetupRVoList", wbBcUserLstSetupRVoList);
				
		return LayoutUtil.getJspPath("/wb/setBcScrn");
	}
	
	/** 환경설정 등록 수정 (저장) */
	@RequestMapping(value = "/wb/transBcScrn")
	public String transBcScrn(HttpServletRequest request,
			@Parameter(name="menuId", required=false) String menuId,
			Locale locale,
			ModelMap model) throws Exception {
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//화면설정 조회[입력창 위치 , 목록구분코드]
			WbBcUserScrnSetupRVo wbBcUserScrnSetupRVo = new WbBcUserScrnSetupRVo();
			VoUtil.bind(request, wbBcUserScrnSetupRVo);
			wbBcUserScrnSetupRVo.setCompId(userVo.getCompId());//회사추가
			wbBcUserScrnSetupRVo.setRegrUid(userVo.getUserUid());
			//화면설정 저장
			queryQueue.store(wbBcUserScrnSetupRVo);
						
			//사용자 목록설정항목 조회
			WbBcUserLstSetupRVo wbBcUserLstSetupRVo = new WbBcUserLstSetupRVo();
			wbBcUserLstSetupRVo.setCompId(userVo.getCompId());//회사추가
			wbBcUserLstSetupRVo.setRegrUid(userVo.getUserUid());
			
			// 목록설정 초기화
			queryQueue.delete(wbBcUserLstSetupRVo);
			
			Integer sortOrdr = 1;
			// atrbId를 기준으로 배열 정보 VO에 세팅
			@SuppressWarnings("unchecked")
			List<WbBcUserLstSetupRVo> list = (List<WbBcUserLstSetupRVo>)VoUtil.bindList(request, WbBcUserLstSetupRVo.class, new String[]{"atrbId"});
			if(list != null){
				for(WbBcUserLstSetupRVo storedWbBcUserLstSetupRVo : list){
					storedWbBcUserLstSetupRVo.setCompId(userVo.getCompId());//회사추가
					storedWbBcUserLstSetupRVo.setRegrUid(userVo.getUserUid());
					storedWbBcUserLstSetupRVo.setSortOrdr(sortOrdr.toString());
					queryQueue.insert(storedWbBcUserLstSetupRVo);// 사용자 목록설정 저장
					sortOrdr++;
				}
			}
			
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("todo", "parent.location.replace('./setBcScrn.do?menuId=" + menuId+"');");
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
}
