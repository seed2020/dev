package com.innobiz.orange.web.wf.ctrl;

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
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfPltSetupDVo;


/** 포틀릿 */
@Controller
public class WfWorksPortletCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfWorksPortletCtrl.class);
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
//	/** 포틀릿 서비스 */
//	@Autowired
//	private PtPltSvc ptPltSvc;
	
	/** 포틀릿 */
	@RequestMapping(value = "/wf/plt/listWorksPlt")
	public String listWorksPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿 설정 조회
		List<WfPltSetupDVo> storedWfPltSetupDVoList = queryWfPltSetupDVoList(userVo, pltId, langTypCd);
				
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<WfPltSetupDVo> wfPltSetupDVoList = new ArrayList<WfPltSetupDVo>();
		for(WfPltSetupDVo wfPltSetupDVo : storedWfPltSetupDVoList){
			url = getBxUrlByBxId(wfPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				wfPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				wfPltSetupDVo.setMenuId(menuId);
				wfPltSetupDVoList.add(wfPltSetupDVo);
			}
		}
		
		model.put("wfPltSetupDVoList", wfPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/wf/plt/listWorksPlt");
	}
	
	/** 포틀릿 */
	@RequestMapping(value = "/wf/plt/setWorksPltSetupPop")
	public String setWorksPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 포틀릿 설정 조회
		List<WfPltSetupDVo> storedWfPltSetupDVoList = queryWfPltSetupDVoList(userVo, pltId, langTypCd);
		
		List<WfPltSetupDVo> wfPltSetupDVoList = new ArrayList<WfPltSetupDVo>();
		
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "WF", "R");
		
		// 양식 전체 목록 조회
		List<WfFormBVo> wfFormBVoList = wfFormSvc.getWfFormBVoList(null, langTypCd, "Y", null, true);
		
		// 양식번호 목록
		List<String> formNoList=new ArrayList<String>();
		if(mdIds!=null && mdIds.size()>0){
			for(WfFormBVo storedWfFormBVo : wfFormBVoList){
				if(mdIds.contains(storedWfFormBVo.getFormNo()))
					formNoList.add(storedWfFormBVo.getFormNo());
			}
		}
		// 모듈 별 권한 있는 모듈참조ID 목록
		//List<PtMnuDVo> ptMnuDVoList = ptSecuSvc.getAuthedMnuVoListByMdRid(userVo, "WF");
		
		String url;
		PtMnuDVo ptMnuDVo;
		if(formNoList.size()>0){
			if(storedWfPltSetupDVoList != null){
				// 설정되어 있는 함 정보 저장 - 권한 체크 후
				for(WfPltSetupDVo wfPltSetupDVo : storedWfPltSetupDVoList){
					if(!formNoList.contains(wfPltSetupDVo.getBxId())) continue;
					storedBxIdList.add(wfPltSetupDVo.getBxId());
					wfPltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
					url = getBxUrlByBxId(wfPltSetupDVo.getBxId());
					ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
					if(ptMnuDVo != null){
						wfPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
						wfPltSetupDVoList.add(wfPltSetupDVo);
					}
				}
			}
			
			for(String bxId : formNoList){
				
				// 이미 설정되어 있는것 제외
				if(storedBxIdList.contains(bxId)) continue;
				
				WfPltSetupDVo wfPltSetupDVo;
				url = getBxUrlByBxId(bxId);
				ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
				if(ptMnuDVo != null){
					// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
					wfPltSetupDVo = new WfPltSetupDVo();
					wfPltSetupDVo.setBxId(bxId);
					wfPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					wfPltSetupDVoList.add(wfPltSetupDVo);
				}
			}
		}
		
		
		model.put("wfPltSetupDVoList", wfPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/wf/plt/setWorksPltSetupPop");
	}
	
	/** [AJAX] 포틀릿 설정 저장 */
	@RequestMapping(value = "/wf/plt/transWorksPltSetupAjx")
	public String transWorksPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		WfPltSetupDVo wfPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			wfPltSetupDVo = new WfPltSetupDVo();
			wfPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(wfPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				wfPltSetupDVo = new WfPltSetupDVo();
				wfPltSetupDVo.setUserUid(userVo.getUserUid());
				wfPltSetupDVo.setPltId(pltId);
				wfPltSetupDVo.setBxId(bxId);
				wfPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(wfPltSetupDVo);
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
	private List<WfPltSetupDVo> queryWfPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		WfPltSetupDVo wfPltSetupDVo = new WfPltSetupDVo();
		wfPltSetupDVo.setUserUid(userVo.getUserUid());
		wfPltSetupDVo.setPltId(pltId);
		wfPltSetupDVo.setQueryLang(langTypCd);
		wfPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WfPltSetupDVo> wfPltSetupDVoList = (List<WfPltSetupDVo>)commonSvc.queryList(wfPltSetupDVo);
		return wfPltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		return "/wf/works/listWorks.do?formNo="+bxId;
	}
}
