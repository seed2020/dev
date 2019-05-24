package com.innobiz.orange.web.dm.ctrl;

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
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmPltSetupDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;

/** 문서 포틀릿 컨트롤러 */
@Controller
public class DmPltCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmPltCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 포틀릿에서 보여줄 함 목록 - 문서조회,최신문서,소유문서,보존연한문서,개인문서 */
	private static final String[] PLT_LIST = {"listDoc", "listOpenReqDoc", "listNewDoc", "listOwnDoc", "listKprdDoc", "listPsnDoc"};
	
	/** 문서 포틀릿 */
	@RequestMapping(value = "/dm/plt/listDocPlt")
	public String listDocPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {

		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		List<DmPltSetupDVo> storedDmPltSetupDVoList = queryDmPltSetupDVoList(userVo, pltId, langTypCd);
		
		// 디폴트 설정 - 문서조회,개인문서
		if(storedDmPltSetupDVoList==null || storedDmPltSetupDVoList.isEmpty()){
			storedDmPltSetupDVoList = new ArrayList<DmPltSetupDVo>();
			DmPltSetupDVo dmPltSetupDVo = new DmPltSetupDVo();
			dmPltSetupDVo.setBxId("listDoc");//문서조회
			storedDmPltSetupDVoList.add(dmPltSetupDVo);
			dmPltSetupDVo = new DmPltSetupDVo();
			dmPltSetupDVo.setBxId("listPsnDoc");//개인문서
			storedDmPltSetupDVoList.add(dmPltSetupDVo);
			dmPltSetupDVo = new DmPltSetupDVo();
			dmPltSetupDVo.setBxId("listOpenReqDoc");//열람요청문서
			storedDmPltSetupDVoList.add(dmPltSetupDVo);
		}
		
		// 권한 있는 문서함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		List<DmPltSetupDVo> dmPltSetupDVoList = new ArrayList<DmPltSetupDVo>();
		for(DmPltSetupDVo dmPltSetupDVo : storedDmPltSetupDVoList){
			url = dmDocSvc.getBxUrlByBxId(dmPltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				dmPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				dmPltSetupDVo.setMenuId(menuId);
				dmPltSetupDVoList.add(dmPltSetupDVo);
			}
		}
		
		model.put("dmPltSetupDVoList", dmPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/dm/plt/listDocPlt");
	}
	
	/** 문서 포틀릿 */
	@RequestMapping(value = "/dm/plt/setDocPltSetupPop")
	public String setDocPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		List<DmPltSetupDVo> storedDmPltSetupDVoList = queryDmPltSetupDVoList(userVo, pltId, langTypCd);
		
		List<DmPltSetupDVo> dmPltSetupDVoList = new ArrayList<DmPltSetupDVo>();
		List<String> storedBxIdList = new ArrayList<String>();
		
		String url;
		PtMnuDVo ptMnuDVo;
		if(storedDmPltSetupDVoList != null){
			// 설정되어 있는 함 정보 저장 - 권한 체크 후
			for(DmPltSetupDVo dmPltSetupDVo : storedDmPltSetupDVoList){
				
				storedBxIdList.add(dmPltSetupDVo.getBxId());
				dmPltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
				url = dmDocSvc.getBxUrlByBxId(dmPltSetupDVo.getBxId());
				ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
				if(ptMnuDVo != null){
					dmPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					dmPltSetupDVoList.add(dmPltSetupDVo);
				}
			}
		}
		
		for(String bxId : PLT_LIST){
			
			// 이미 설정되어 있는것 제외
			if(storedBxIdList.contains(bxId)) continue;
			
			DmPltSetupDVo dmPltSetupDVo;
			url = dmDocSvc.getBxUrlByBxId(bxId);
			ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
			if(ptMnuDVo != null){
				// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
				dmPltSetupDVo = new DmPltSetupDVo();
				dmPltSetupDVo.setBxId(bxId);
				dmPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				dmPltSetupDVoList.add(dmPltSetupDVo);
			}
		}
		
		model.put("dmPltSetupDVoList", dmPltSetupDVoList);
		
		return LayoutUtil.getJspPath("/dm/plt/setDocPltSetupPop");
	}
	
	/** [AJAX] 문서 포틀릿 설정 저장 */
	@RequestMapping(value = "/dm/plt/transDocPltSetupAjx")
	public String transDocPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		DmPltSetupDVo dmPltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			dmPltSetupDVo = new DmPltSetupDVo();
			dmPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(dmPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				dmPltSetupDVo = new DmPltSetupDVo();
				dmPltSetupDVo.setUserUid(userVo.getUserUid());
				dmPltSetupDVo.setPltId(pltId);
				dmPltSetupDVo.setBxId(bxId);
				dmPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(dmPltSetupDVo);
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
	
	/** 사용자별 문서 포틀릿 설정 조회 
	 * @param langTypCd */
	private List<DmPltSetupDVo> queryDmPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		DmPltSetupDVo dmPltSetupDVo = new DmPltSetupDVo();
		dmPltSetupDVo.setUserUid(userVo.getUserUid());
		dmPltSetupDVo.setPltId(pltId);
		dmPltSetupDVo.setQueryLang(langTypCd);
		dmPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<DmPltSetupDVo> dmPltSetupDVoList = (List<DmPltSetupDVo>)commonSvc.queryList(dmPltSetupDVo);
		return dmPltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
}
