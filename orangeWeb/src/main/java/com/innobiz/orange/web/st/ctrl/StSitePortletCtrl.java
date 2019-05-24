package com.innobiz.orange.web.st.ctrl;

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

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtPltSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.st.vo.StCatBVo;
import com.innobiz.orange.web.st.vo.StSitePltSetupDVo;


/** 사이트 포틀릿 */
@Controller
public class StSitePortletCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(StSitePortletCtrl.class);
	
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
	
	/** 포틀릿 서비스 */
	@Autowired
	private PtPltSvc ptPltSvc;
	
	/** 사이트포틀릿 */
	@RequestMapping(value = {"/st/plt/listSitePlt", "/cm/plt/listSitePlt"})
	public String listSitePlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿 맵 조회(캐쉬)
		Map<Integer, PtPltDVo> pltMap = ptPltSvc.getPltByPltIdMap(langTypCd);
		
		if(pltId==null || pltId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		PtPltDVo ptPltDVo = pltMap.get(Hash.hashId(pltId));
		
		if(ptPltDVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 포틀릿 설정 조회
		List<StSitePltSetupDVo> storedStSitePltSetupDVoList = queryStSitePltSetupDVoList(userVo, pltId, langTypCd);
		
		// 전체 카테고리 기준 목록 초기화
		List<StSitePltSetupDVo> targetStSitePltSetupDVoList = new ArrayList<StSitePltSetupDVo>(); 
		
		// 카테고리조회
		StCatBVo stCatBVo = new StCatBVo();
		stCatBVo.setQueryLang(langTypCd);
		stCatBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<StCatBVo> stCatBVoList = (List<StCatBVo>)commonSvc.queryList(stCatBVo);
		for(StCatBVo storedStCatBVo : stCatBVoList){
			StSitePltSetupDVo stSitePltSetupDVo = new StSitePltSetupDVo();	
			stSitePltSetupDVo.setBxId(storedStCatBVo.getCatId());
			stSitePltSetupDVo.setBxNm(storedStCatBVo.getCatNm());
			targetStSitePltSetupDVoList.add(stSitePltSetupDVo);
		}
		List<StSitePltSetupDVo> stSitePltSetupDVoList = new ArrayList<StSitePltSetupDVo>();
		
		String urlPrefix = request.getRequestURI().startsWith("/cm") ? "cm" : "st";
		// 권한 체크 여부
		boolean isAuthChk = true;
		String url = "/site/listSite.do";
		if(ptPltDVo.getPltUrl()!=null && ptPltDVo.getPltUrl().startsWith("/cm") ) {
			isAuthChk = false;
			url = "/site/listSiteFrm.do";
		}
		url="/"+urlPrefix+url;
		String menuId = ptSecuSvc.getSecuMenuId(userVo, url);
		// 조회여부
		boolean isListView = true;
		if(isAuthChk){
			// 권한 있는 문서함 체크
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
			PtMnuDVo ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			isListView = ptMnuDVo != null;
		}
		model.put("urlPrefix", urlPrefix);
		if(isListView){
			for(StSitePltSetupDVo stSitePltSetupDVo : storedStSitePltSetupDVoList){
				for(StSitePltSetupDVo targetStSitePltSetupDVo  : targetStSitePltSetupDVoList){
					if(targetStSitePltSetupDVo.getBxId().equals(stSitePltSetupDVo.getBxId())){
						targetStSitePltSetupDVo.setMenuId(menuId);
						stSitePltSetupDVoList.add(targetStSitePltSetupDVo);
						break;
					}
				}
			}
			// 디폴트 설정
			if(stSitePltSetupDVoList==null || stSitePltSetupDVoList.isEmpty() || stSitePltSetupDVoList.size()==0){
				/*if(targetStSitePltSetupDVoList.size()==0){
					StSitePltSetupDVo stSitePltSetupDVo = new StSitePltSetupDVo();	
					stSitePltSetupDVo.setBxId("ALL");
					// cm.option.all=전체
					stSitePltSetupDVo.setBxNm(messageProperties.getMessage("cm.option.all", request));
					targetStSitePltSetupDVoList.add(stSitePltSetupDVo);
				}*/
				
				int cnt=0;
				for(StSitePltSetupDVo targetStSitePltSetupDVo  : targetStSitePltSetupDVoList){
					if(cnt>2) break;
					targetStSitePltSetupDVo.setMenuId(menuId);
					stSitePltSetupDVoList.add(targetStSitePltSetupDVo);
					cnt++;
				}
			}
		}
		
		model.put("stSitePltSetupDVoList", stSitePltSetupDVoList);
		
		return LayoutUtil.getJspPath("/st/plt/listSitePlt");
	}
	
	/** 사이트포틀릿 */
	@RequestMapping(value = {"/st/plt/setSitePltSetupPop", "/cm/plt/setSitePltSetupPop"})
	public String setSitePltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿 맵 조회(캐쉬)
		Map<Integer, PtPltDVo> pltMap = ptPltSvc.getPltByPltIdMap(langTypCd);
		
		if(pltId==null || pltId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		PtPltDVo ptPltDVo = pltMap.get(Hash.hashId(pltId));
		
		if(ptPltDVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
				
		// 포틀릿 설정 조회
		List<StSitePltSetupDVo> storedStSitePltSetupDVoList = queryStSitePltSetupDVoList(userVo, pltId, langTypCd);
		
		List<StSitePltSetupDVo> stSitePltSetupDVoList = new ArrayList<StSitePltSetupDVo>();
		
		// 권한 체크 여부
		String urlPrefix = request.getRequestURI().startsWith("/cm") ? "cm" : "st";
		boolean isAuthChk = true;
		String url = "/site/listSite.do";
		if(ptPltDVo.getPltUrl()!=null && ptPltDVo.getPltUrl().startsWith("/cm") ) {
			isAuthChk = false;
			url = "/site/listSiteFrm.do";
		}
		url="/"+urlPrefix+url;
		String menuId = ptSecuSvc.getSecuMenuId(userVo, url);
		
		// 조회여부
		boolean isListView = true;
		if(isAuthChk){
			// 권한 있는 문서함 체크
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
			PtMnuDVo ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			isListView = ptMnuDVo != null;
			model.put("urlPrefix", "st");
		}
		model.put("urlPrefix", urlPrefix);
		if(isListView){
			// 카테고리조회
			StCatBVo stCatBVo = new StCatBVo();
			stCatBVo.setQueryLang(langTypCd);
			stCatBVo.setCompId(userVo.getCompId());
			@SuppressWarnings("unchecked")
			List<StCatBVo> stCatBVoList = (List<StCatBVo>)commonSvc.queryList(stCatBVo);
			if(stCatBVoList != null && stCatBVoList.size()>0){
				// 포틀릿 설정 ID목록
				List<String> storedBxIdList = new ArrayList<String>();
				
				// 설정되어 있는 함 정보 저장 - 권한 체크 후
				for(StSitePltSetupDVo stSitePltSetupDVo : storedStSitePltSetupDVoList){
					for(StCatBVo storedStCatBVo : stCatBVoList){
						if(storedStCatBVo.getCatId().equals(stSitePltSetupDVo.getBxId())){
							storedBxIdList.add(stSitePltSetupDVo.getBxId());
							stSitePltSetupDVo = new StSitePltSetupDVo();
							stSitePltSetupDVo.setBxId(storedStCatBVo.getCatId());
							stSitePltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
							stSitePltSetupDVo.setBxNm(storedStCatBVo.getCatNm());
							stSitePltSetupDVoList.add(stSitePltSetupDVo);
							break;
						}
					}
				}
				StSitePltSetupDVo stSitePltSetupDVo;
				for(StCatBVo storedStCatBVo : stCatBVoList){
					if(storedBxIdList.size()>0 && storedBxIdList.contains(storedStCatBVo.getCatId())) continue;
					// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
					stSitePltSetupDVo = new StSitePltSetupDVo();
					stSitePltSetupDVo.setBxId(storedStCatBVo.getCatId());
					stSitePltSetupDVo.setUseYn("N");//설정 되어 있음 표시
					stSitePltSetupDVo.setBxNm(storedStCatBVo.getCatNm());
					stSitePltSetupDVoList.add(stSitePltSetupDVo);
				}
			}
		}
		
		model.put("stSitePltSetupDVoList", stSitePltSetupDVoList);
		
		return LayoutUtil.getJspPath("/st/plt/setSitePltSetupPop");
	}
	
	/** [AJAX] 사이트포틀릿 설정 저장 */
	@RequestMapping(value = {"/st/plt/transSitePltSetupAjx", "/cm/plt/transSitePltSetupAjx"})
	public String transSitePltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		StSitePltSetupDVo stSitePltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			stSitePltSetupDVo = new StSitePltSetupDVo();
			stSitePltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(stSitePltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				stSitePltSetupDVo = new StSitePltSetupDVo();
				stSitePltSetupDVo.setUserUid(userVo.getUserUid());
				stSitePltSetupDVo.setPltId(pltId);
				stSitePltSetupDVo.setBxId(bxId);
				stSitePltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(stSitePltSetupDVo);
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
	
	/** 사용자별 사이트포틀릿 설정 조회 
	 * @param langTypCd */
	private List<StSitePltSetupDVo> queryStSitePltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		StSitePltSetupDVo stSitePltSetupDVo = new StSitePltSetupDVo();
		stSitePltSetupDVo.setUserUid(userVo.getUserUid());
		stSitePltSetupDVo.setPltId(pltId);
		stSitePltSetupDVo.setQueryLang(langTypCd);
		stSitePltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<StSitePltSetupDVo> stSitePltSetupDVoList = (List<StSitePltSetupDVo>)commonSvc.queryList(stSitePltSetupDVo);
		return stSitePltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
}
