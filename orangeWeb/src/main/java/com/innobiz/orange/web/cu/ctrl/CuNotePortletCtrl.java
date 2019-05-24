package com.innobiz.orange.web.cu.ctrl;

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
import com.innobiz.orange.web.cu.vo.CuNotePltSetupDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;


/** 포틀릿 */
@Controller
public class CuNotePortletCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuNotePortletCtrl.class);
	
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
	
	/** 포틀릿에서 보여줄 함 목록 - [ 읽지않은 쪽지, 최근쪽지 ] */
	private static final String[] PLT_LIST = {"notRead", "new", "recv", "send"};
	
	/** 포틀릿 */
	@RequestMapping(value = "/cu/note/plt/listNotePlt")
	public String listNotePlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 포틀릿 설정 조회
		//List<CuNotePltSetupDVo> storedCuNotePltSetupDVoList = queryCuNotePltSetupDVoList(userVo, pltId, langTypCd);
		List<CuNotePltSetupDVo> storedCuNotePltSetupDVoList = null;
		
		// 디폴트 설정 - 요청, 처리현황
		if(storedCuNotePltSetupDVoList==null || storedCuNotePltSetupDVoList.isEmpty()){
			storedCuNotePltSetupDVoList = new ArrayList<CuNotePltSetupDVo>();
			
			CuNotePltSetupDVo cuNotePltSetupDVo=new CuNotePltSetupDVo();
			cuNotePltSetupDVo.setBxId("notRead");
			storedCuNotePltSetupDVoList.add(cuNotePltSetupDVo);
			cuNotePltSetupDVo=new CuNotePltSetupDVo();
			cuNotePltSetupDVo.setBxId("new");
			storedCuNotePltSetupDVoList.add(cuNotePltSetupDVo);
		}
				
		// 권한 있는 결재함 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		
		String bxNm=null;
		List<CuNotePltSetupDVo> cuNotePltSetupDVoList = new ArrayList<CuNotePltSetupDVo>();
		for(CuNotePltSetupDVo cuNotePltSetupDVo : storedCuNotePltSetupDVoList){
			url = getBxUrlByBxId(cuNotePltSetupDVo.getBxId());
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				bxNm=getBxNmByBxId(request, cuNotePltSetupDVo.getBxId());
				if(bxNm==null) bxNm=ptMnuDVo.getRescNm();
				cuNotePltSetupDVo.setBxNm(bxNm);
				cuNotePltSetupDVo.setMenuId(menuId);
				cuNotePltSetupDVoList.add(cuNotePltSetupDVo);
			}
		}
		
		model.put("cuNotePltSetupDVoList", cuNotePltSetupDVoList);
		
		return LayoutUtil.getJspPath("/cu/note/plt/listNotePlt");
	}
	
	/** 포틀릿 */
	@RequestMapping(value = "/cu/note/plt/setNotePltSetupPop")
	public String setNotePltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 포틀릿 설정 조회
		List<CuNotePltSetupDVo> storedCuNotePltSetupDVoList = queryCuNotePltSetupDVoList(userVo, pltId, langTypCd);
		
		List<CuNotePltSetupDVo> cuNotePltSetupDVoList = new ArrayList<CuNotePltSetupDVo>();
		
		List<String> storedBxIdList = new ArrayList<String>();
		
		String[] pltList = PLT_LIST;
		
		String url, bxNm;
		PtMnuDVo ptMnuDVo;
		if(storedCuNotePltSetupDVoList != null){
			// 설정되어 있는 함 정보 저장 - 권한 체크 후
			for(CuNotePltSetupDVo cuNotePltSetupDVo : storedCuNotePltSetupDVoList){
				
				storedBxIdList.add(cuNotePltSetupDVo.getBxId());
				cuNotePltSetupDVo.setUseYn("Y");//설정 되어 있음 표시
				url = getBxUrlByBxId(cuNotePltSetupDVo.getBxId());
				ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
				if(ptMnuDVo != null){
					bxNm=getBxNmByBxId(request, cuNotePltSetupDVo.getBxId());
					if(bxNm==null) bxNm=ptMnuDVo.getRescNm();
					cuNotePltSetupDVo.setBxNm(bxNm);
					cuNotePltSetupDVoList.add(cuNotePltSetupDVo);
				}
			}
		}
		
		for(String bxId : pltList){
			
			// 이미 설정되어 있는것 제외
			if(storedBxIdList.contains(bxId)) continue;
			
			CuNotePltSetupDVo cuNotePltSetupDVo;
			url = getBxUrlByBxId(bxId);
			ptMnuDVo = ptSecuSvc.getMenuByUrl(url, userVo, langTypCd);
			if(ptMnuDVo != null){
				// 설정 안되어 있는 목록 - 데이터 생성해서 설정함
				cuNotePltSetupDVo = new CuNotePltSetupDVo();
				cuNotePltSetupDVo.setBxId(bxId);
				bxNm=getBxNmByBxId(request, bxId);
				if(bxNm==null) bxNm=ptMnuDVo.getRescNm();
				cuNotePltSetupDVo.setBxNm(bxNm);
				cuNotePltSetupDVoList.add(cuNotePltSetupDVo);
			}
		}
		
		model.put("cuNotePltSetupDVoList", cuNotePltSetupDVoList);
		
		return LayoutUtil.getJspPath("/cu/note/plt/setNotePltSetupPop");
	}
	
	/** [AJAX] 포틀릿 설정 저장 */
	@RequestMapping(value = "/cu/note/plt/transNotePltSetupAjx")
	public String transNotePltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String pltId = (String)jsonObject.get("pltId");
		String bxIds = (String)jsonObject.get("bxIds");
		
		QueryQueue queryQueue = new QueryQueue();
		CuNotePltSetupDVo cuNotePltSetupDVo;
		if(bxIds!=null && !bxIds.isEmpty()){
			
			cuNotePltSetupDVo = new CuNotePltSetupDVo();
			cuNotePltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(cuNotePltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				cuNotePltSetupDVo = new CuNotePltSetupDVo();
				cuNotePltSetupDVo.setUserUid(userVo.getUserUid());
				cuNotePltSetupDVo.setPltId(pltId);
				cuNotePltSetupDVo.setBxId(bxId);
				cuNotePltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(cuNotePltSetupDVo);
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
	private List<CuNotePltSetupDVo> queryCuNotePltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		CuNotePltSetupDVo cuNotePltSetupDVo = new CuNotePltSetupDVo();
		cuNotePltSetupDVo.setUserUid(userVo.getUserUid());
		cuNotePltSetupDVo.setPltId(pltId);
		cuNotePltSetupDVo.setQueryLang(langTypCd);
		cuNotePltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<CuNotePltSetupDVo> cuNotePltSetupDVoList = (List<CuNotePltSetupDVo>)commonSvc.queryList(cuNotePltSetupDVo);
		return cuNotePltSetupDVoList;
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("send")){
			return "/cu/send/listNote.do";
		} else {
			return "/cu/recv/listNote.do";
		}
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxNmByBxId(HttpServletRequest request, String bxId){
		if(bxId.equals("notRead")){
			return messageProperties.getMessage("cm.cols.recv.notRead", request);
		}else if(bxId.equals("new")){
			return messageProperties.getMessage("cm.cols.recv.new", request);
		}else {
			return null;
		}
	}
	
}
