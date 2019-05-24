package com.innobiz.orange.web.ap.ctrl;

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

import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.vo.ApPltSetupDVo;
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

/** 결재 포틀릿 컨트롤러 */
@Controller
public class ApPltCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApPltCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
//	/** 결재함 컨트롤러 */
//	@Autowired
//	private ApBxCtrl apBxCtrl;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 포틀릿에서 보여줄 함 목록 - 대기함, 기안함, 통보함(후열함), 부서대기함, 발송함, 접수함, 참조열람 */
	private static final String[] PLT_LIST1 = {"waitBx", "myBx", "postApvdBx", "deptBx", "toSendBx", "recvBx"};
	private static final String[] PLT_LIST2 = {"waitBx", "myBx", "postApvdBx", "deptBx", "toSendBx", "recvBx", "refVwBx"};
	
	/** 결재 포틀릿 */
	@RequestMapping(value = "/ap/plt/listApvBxPlt")
	public String listApvBxPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		List<ApPltSetupDVo> storedApPltSetupDVoList = queryApPltSetupDVoList(userVo, pltId, langTypCd);
		
		// 디폴트 설정 - 대기함, 기안함
		if(storedApPltSetupDVoList==null || storedApPltSetupDVoList.isEmpty()){
			storedApPltSetupDVoList = new ArrayList<ApPltSetupDVo>();
			ApPltSetupDVo apPltSetupDVo = new ApPltSetupDVo();
			apPltSetupDVo.setBxId("waitBx");
			storedApPltSetupDVoList.add(apPltSetupDVo);
			apPltSetupDVo = new ApPltSetupDVo();
			apPltSetupDVo.setBxId("myBx");
			storedApPltSetupDVoList.add(apPltSetupDVo);
		}
		
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<ApPltSetupDVo> apPltSetupDVoList = new ArrayList<ApPltSetupDVo>();
		for(ApPltSetupDVo apPltSetupDVo : storedApPltSetupDVoList){
			url = apBxSvc.getBxUrlByBxId(apPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				apPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				apPltSetupDVo.setMenuId(menuId);
				apPltSetupDVoList.add(apPltSetupDVo);
			}
		}
		
		model.put("apPltSetupDVoList", apPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/ap/plt/listApvBxPlt");
	}
	
	/** 결재 포틀릿 */
	@RequestMapping(value = "/ap/plt/setApvBxPltSetupPop")
	public String setApvBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		List<ApPltSetupDVo> storedApPltSetupDVoList = queryApPltSetupDVoList(userVo, pltId, langTypCd);
		
		List<ApPltSetupDVo> apPltSetupDVoList = new ArrayList<ApPltSetupDVo>();
		List<String> storedBxIdList = new ArrayList<String>();
		
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		String[] pltList = "Y".equals(optConfigMap.get("refVwEnable")) ? PLT_LIST2 : PLT_LIST1;
		
		String url;
		PtMnuDVo ptMnuDVo;
		if(storedApPltSetupDVoList != null){
			// 설정되어 있는 함 정보 저장 - 권한 체크 후
			for(ApPltSetupDVo apPltSetupDVo : storedApPltSetupDVoList){
				
				storedBxIdList.add(apPltSetupDVo.getBxId());
				apPltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
				url = apBxSvc.getBxUrlByBxId(apPltSetupDVo.getBxId());
				ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
				if(ptMnuDVo != null){
					apPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					apPltSetupDVoList.add(apPltSetupDVo);
				}
			}
		}
		
		for(String bxId : pltList){
			
			// 이미 설정되어 있는것 제외
			if(storedBxIdList.contains(bxId)) continue;
			
			ApPltSetupDVo apPltSetupDVo;
			url = apBxSvc.getBxUrlByBxId(bxId);
			ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
			if(ptMnuDVo != null){
				// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
				apPltSetupDVo = new ApPltSetupDVo();
				apPltSetupDVo.setBxId(bxId);
				apPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				apPltSetupDVoList.add(apPltSetupDVo);
			}
		}
		
		model.put("apPltSetupDVoList", apPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/ap/plt/setApvBxPltSetupPop");
	}
	
	/** [AJAX] 결재 포틀릿 설정 저장 */
	@RequestMapping(value = "/ap/plt/transApvBxPltSetupAjx")
	public String transApvBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		ApPltSetupDVo apPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			apPltSetupDVo = new ApPltSetupDVo();
			apPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(apPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				apPltSetupDVo = new ApPltSetupDVo();
				apPltSetupDVo.setUserUid(userVo.getUserUid());
				apPltSetupDVo.setPltId(pltId);
				apPltSetupDVo.setBxId(bxId);
				apPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(apPltSetupDVo);
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
	
	/** 사용자별 결재 포틀릿 설정 조회 
	 * @param langTypCd */
	private List<ApPltSetupDVo> queryApPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		ApPltSetupDVo apPltSetupDVo = new ApPltSetupDVo();
		apPltSetupDVo.setUserUid(userVo.getUserUid());
		apPltSetupDVo.setPltId(pltId);
		apPltSetupDVo.setQueryLang(langTypCd);
		apPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApPltSetupDVo> apPltSetupDVoList = (List<ApPltSetupDVo>)commonSvc.queryList(apPltSetupDVo);
		return apPltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
}
