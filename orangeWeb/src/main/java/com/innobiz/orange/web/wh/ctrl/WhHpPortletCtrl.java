package com.innobiz.orange.web.wh.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.wh.vo.WhPltSetupDVo;


/** 포틀릿 */
@Controller
public class WhHpPortletCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WhHpPortletCtrl.class);
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
//	/** 포틀릿 서비스 */
//	@Autowired
//	private PtPltSvc ptPltSvc;
	
	/** 포틀릿에서 보여줄 함 목록 - 요청, 접수, 처리, 처리현황 */
	private static final String[] PLT_LIST = {"Req", "Recv", "Hdl", "DashBrd"};
	
	/** 포틀릿 */
	@RequestMapping(value = "/wh/plt/listHelpPlt")
	public String listHelpPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿 설정 조회
		List<WhPltSetupDVo> storedWhPltSetupDVoList = queryWhPltSetupDVoList(userVo, pltId, langTypCd);
		
		// 디폴트 설정 - 요청, 처리현황
		if(storedWhPltSetupDVoList==null || storedWhPltSetupDVoList.isEmpty()){
			storedWhPltSetupDVoList = new ArrayList<WhPltSetupDVo>();
			WhPltSetupDVo whPltSetupDVo = new WhPltSetupDVo();
			whPltSetupDVo.setBxId("Req");
			storedWhPltSetupDVoList.add(whPltSetupDVo);
			/*whPltSetupDVo = new WhPltSetupDVo();
			whPltSetupDVo.setBxId("DashBrd");
			storedWhPltSetupDVoList.add(whPltSetupDVo);*/
		}
				
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<WhPltSetupDVo> whPltSetupDVoList = new ArrayList<WhPltSetupDVo>();
		for(WhPltSetupDVo whPltSetupDVo : storedWhPltSetupDVoList){
			url = getBxUrlByBxId(whPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				whPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				whPltSetupDVo.setMenuId(menuId);
				whPltSetupDVoList.add(whPltSetupDVo);
			}
		}
		
		model.put("whPltSetupDVoList", whPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/wh/plt/listHelpPlt");
	}
	
	/** 포틀릿 */
	@RequestMapping(value = "/wh/plt/setHelpPltSetupPop")
	public String setHelpPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 포틀릿 설정 조회
		List<WhPltSetupDVo> storedWhPltSetupDVoList = queryWhPltSetupDVoList(userVo, pltId, langTypCd);
		
		List<WhPltSetupDVo> whPltSetupDVoList = new ArrayList<WhPltSetupDVo>();
		
		List<String> storedBxIdList = new ArrayList<String>();
		
		String[] pltList = PLT_LIST;
		
		String url;
		PtMnuDVo ptMnuDVo;
		if(storedWhPltSetupDVoList != null){
			// 설정되어 있는 함 정보 저장 - 권한 체크 후
			for(WhPltSetupDVo whPltSetupDVo : storedWhPltSetupDVoList){
				
				storedBxIdList.add(whPltSetupDVo.getBxId());
				whPltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
				url = getBxUrlByBxId(whPltSetupDVo.getBxId());
				ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
				if(ptMnuDVo != null){
					whPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					whPltSetupDVoList.add(whPltSetupDVo);
				}
			}
		}
		
		for(String bxId : pltList){
			
			// 이미 설정되어 있는것 제외
			if(storedBxIdList.contains(bxId)) continue;
			
			WhPltSetupDVo whPltSetupDVo;
			url = getBxUrlByBxId(bxId);
			ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
			if(ptMnuDVo != null){
				// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
				whPltSetupDVo = new WhPltSetupDVo();
				whPltSetupDVo.setBxId(bxId);
				whPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				whPltSetupDVoList.add(whPltSetupDVo);
			}
		}
		
		model.put("whPltSetupDVoList", whPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/wh/plt/setHelpPltSetupPop");
	}
	
	/** [AJAX] 포틀릿 설정 저장 */
	@RequestMapping(value = "/wh/plt/transHelpPltSetupAjx")
	public String transHelpPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		WhPltSetupDVo whPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			whPltSetupDVo = new WhPltSetupDVo();
			whPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(whPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				whPltSetupDVo = new WhPltSetupDVo();
				whPltSetupDVo.setUserUid(userVo.getUserUid());
				whPltSetupDVo.setPltId(pltId);
				whPltSetupDVo.setBxId(bxId);
				whPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(whPltSetupDVo);
			}
		}
		
		try {
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("result", "ok");
			
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+e.getStackTrace()[0].toString()
					+(message==null || message.isEmpty() ? "" : "\n"+message));
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 사용자별 포틀릿 설정 조회 
	 * @param langTypCd */
	private List<WhPltSetupDVo> queryWhPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		WhPltSetupDVo whPltSetupDVo = new WhPltSetupDVo();
		whPltSetupDVo.setUserUid(userVo.getUserUid());
		whPltSetupDVo.setPltId(pltId);
		whPltSetupDVo.setQueryLang(langTypCd);
		whPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WhPltSetupDVo> whPltSetupDVoList = (List<WhPltSetupDVo>)commonSvc.queryList(whPltSetupDVo);
		return whPltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("Dashbrd")){
			return "/wh/help/listDashBrd.do";
		} else {
			return "/wh/help/list"+bxId+".do";
		}
	}
}
