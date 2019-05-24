package com.innobiz.orange.web.pt.admCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPltSetupRVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 메뉴 설정 관리 */
@Controller
public class PtMnuCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMnuCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 캐쉬 서비스 */
//	@Autowired
//	private PtCacheSvc ptCacheSvc;
	
	// 임시
	@RequestMapping(value = "/pt/index")
	public String admLv0(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/pt/index");
	}
	
	/** [메뉴그룹] 메뉴그룹목록조회 (관리자) */
	@RequestMapping(value = "/pt/adm/mnu/listAdmMnuGrp")
	public String listAdmMnuGrp(HttpServletRequest request,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		model.put("mnuGrpMd", "Adm");// 메뉴구성URL의 중간 - set${mnuGrpMd}Mnu.do
		String mnuGrpMdCd = "A";//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		model.put("mnuGrpMdCd", mnuGrpMdCd);
		model.put("mnuGrpMdCdParam", "&mnuGrpMdCd="+mnuGrpMdCd);
		return listMnuGrp(request, schWord, mnuGrpMdCd, model);
	}
	
	/** [메뉴그룹] 메뉴그룹목록조회 (모바일) */
	@RequestMapping(value = "/pt/adm/mnu/listMobileMnuGrp")
	public String listMobileMnuGrp(HttpServletRequest request,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		model.put("mnuGrpMd", "Mobile");// 메뉴구성URL의 중간 - set${mnuGrpMd}Mnu.do
		String mnuGrpMdCd = "M";//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		model.put("mnuGrpMdCd", mnuGrpMdCd);
		model.put("mnuGrpMdCdParam", "&mnuGrpMdCd="+mnuGrpMdCd);
		return listMnuGrp(request, schWord, mnuGrpMdCd, model);
	}
	
	/** [메뉴그룹] 메뉴그룹목록조회 */
	@RequestMapping(value = "/pt/adm/mnu/listMnuGrp")
	public String listMnuGrp(HttpServletRequest request,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,// 관리자메뉴에서 호출 - listAdmMnuGrp
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		if(mnuGrpMdCd==null){
			mnuGrpMdCd = "U";// U:사용자 메뉴그룹, A:관리자 메뉴그룹: M:모바일 메뉴그룹
			model.put("mnuGrpMdCd", mnuGrpMdCd);
		}
		
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setSysgYn("N");
		ptMnuGrpBVo.setMnuGrpMdCd(mnuGrpMdCd);
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		
		if(schWord!=null && !schWord.isEmpty()){
			ptMnuGrpBVo.setSchCat("mnuGrpNm");
			ptMnuGrpBVo.setSchWord(schWord);
		}
		
		// 공개 범위에 따른 조회 범위 설정
		String setupId = "A".equals(mnuGrpMdCd) ? "adminMnuGrp" : "M".equals(mnuGrpMdCd) ? "mobileMnuGrp" : "userMnuGrp";
		model.put("setupId", setupId);
		if(!SecuUtil.hasAuth(request, "SYS", null)){
			Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
			String setupVa = map==null ? null : map.get(setupId);
			if(setupVa == null || "myComp".equals(setupVa)){
				UserVo userVo = LoginSession.getUser(request);
				ptMnuGrpBVo.setOpenCompId(userVo.getCompId());
			}
		}
		
		Integer recodeCount = commonSvc.count(ptMnuGrpBVo);
		PersonalUtil.setPaging(request, ptMnuGrpBVo, recodeCount);
		
		// 공개범위 - 회사명 세팅
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		String openCompId, allCompNm = messageProperties.getMessage("cm.option.allComp", request);//전체회사
		
		// 공개범위 - 회사명 세팅
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
		if(ptMnuGrpBVoList!=null){
			for(PtMnuGrpBVo storedPtMnuGrpBVo : ptMnuGrpBVoList){
				openCompId = storedPtMnuGrpBVo.getOpenCompId();
				if(openCompId==null || openCompId.equals(PtConstant.SYS_COMP_ID) || openCompId.isEmpty()){
					storedPtMnuGrpBVo.setOpenCompNm(allCompNm);
				} else {
					ptCompBVo = ptCompMap.get(openCompId);
					storedPtMnuGrpBVo.setOpenCompNm(ptCompBVo==null ? null : ptCompBVo.getRescNm());
				}
			}
		}
		
		model.put("recodeCount", recodeCount);
		model.put("ptMnuGrpBVoList", ptMnuGrpBVoList);
		return LayoutUtil.getJspPath("/pt/adm/mnu/listMnuGrp");
	}
	
	/** [메뉴그룹] 메뉴그룹상세조회 (수정용 팝업) */
	@RequestMapping(value = "/pt/adm/mnu/setMnuGrpPop")
	public String setMnuGrpPop(HttpServletRequest request,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - 변수 바인딩
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		VoUtil.bind(request, ptMnuGrpBVo);
		ptMnuGrpBVo.setQueryLang(langTypCd);
		
		// 메뉴그룹기본(PT_MNU_GRP_B) - SELECT
		PtMnuGrpBVo storedPtMnuGrpBVo = null;
		if(ptMnuGrpBVo.getMnuGrpId()!=null){
			
			storedPtMnuGrpBVo = (PtMnuGrpBVo)commonSvc.queryVo(ptMnuGrpBVo);
			if(storedPtMnuGrpBVo!=null){
				
				model.put("ptMnuGrpBVo", storedPtMnuGrpBVo);
				if(storedPtMnuGrpBVo.getRescId()!=null){
					
					// 리소스기본(PT_RESC_B) 테이블 - 조회
					ptRescSvc.queryRescBVo(storedPtMnuGrpBVo.getRescId(), model);
				}
			}
		}
		
		// 시스템 회사코드 - 전체회사에 해당
		model.put("sysCompId", PtConstant.SYS_COMP_ID);
		
		// 메뉴그룹구분코드
		List<PtCdBVo> mnuGrpTypCdList = ptCmSvc.getCdList("MNU_GRP_TYP_CD", langTypCd, "Y");
		model.put("mnuGrpTypCdList", mnuGrpTypCdList);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
		
		// 모바일 메뉴의 경우
		if("M".equals(mnuGrpMdCd)){
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 모바일 사용 여부
			if("Y".equals(sysPlocMap.get("mobileEnable"))){
				model.put("mobileEnable", Boolean.TRUE);
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/mnu/setMnuGrpPop");
	}
	
	/** [팝업] 모바일 메뉴 아이콘 조회 */
	@RequestMapping(value = "/pt/adm/mnu/setMdRidPop")
	public String setMdRidPop(HttpServletRequest request,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/pt/adm/mnu/setMdRidPop");
	}
	
	/** [팝업] 관리범위 설정 */
	@RequestMapping(value = "/pt/adm/mnu/setMngCompPop")
	public String setMngCompPop(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			ModelMap model) throws Exception {
		
		Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
		String setupVa = map==null ? null : map.get(setupId);
		model.put("setupVa", setupVa==null ? "allComp" : setupVa);
		
		return LayoutUtil.getJspPath("/pt/adm/mnu/setMngCompPop");
	}
	
	/** [히든프레임] 관리범위 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/mnu/transMngComp")
	public String transMngComp(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			@Parameter(name="setupVa", required=false) String setupVa,
			ModelMap model) throws Exception {
		
		try{
			QueryQueue queryQueue = new QueryQueue();
			
			PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(PtConstant.PT_MNG_COMP);
			ptSysSetupDVo.setSetupId(setupId);
			ptSysSetupDVo.setSetupVa(setupVa);
			queryQueue.store(ptSysSetupDVo);
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setMngCompDialog');");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [메뉴그룹] 메뉴그룹저장 */
	@RequestMapping(value = "/pt/adm/mnu/transMnuGrp")
	public String transMnuGrp(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{

			QueryQueue queryQueue = new QueryQueue();
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
			VoUtil.bind(request, ptMnuGrpBVo);
			ptMnuGrpBVo.setSysgYn("N");
			ptMnuGrpBVo.setMnuGrpNm(ptRescBVo.getRescVa());
			ptMnuGrpBVo.setRescId(ptRescBVo.getRescId());
			ptMnuGrpBVo.setModDt("sysdate");
			ptMnuGrpBVo.setModrUid(userVo.getUserUid());
			if(ptMnuGrpBVo.getMnuGrpMdCd()==null) ptMnuGrpBVo.setMnuGrpMdCd("U");
			
			// 메뉴그룹ID - KEY가 없을 경우 : 신규
			if(ptMnuGrpBVo.getMnuGrpId()==null || ptMnuGrpBVo.getMnuGrpId().isEmpty()){
				ptMnuGrpBVo.setMnuGrpId(ptCmSvc.createId("PT_MNU_GRP_B"));//"PT_MNU_GRP_B", 'N', 5
				if(ptMnuGrpBVo.getOpenCompId()==null) ptMnuGrpBVo.setOpenCompId(userVo.getCompId());
				ptMnuGrpBVo.setRegDt("sysdate");
				ptMnuGrpBVo.setRegrUid(userVo.getUserUid());
				queryQueue.insert(ptMnuGrpBVo);
			} else {
				queryQueue.store(ptMnuGrpBVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 메뉴그룹 삭제 */
	@RequestMapping(value = "/pt/adm/mnu/transMnuGrpDelAjx")
	public String transMnuGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			String message = null;
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)jsonObject.get("mnuGrpIds");
			
			String mnuGrpId;
			QueryQueue queryQueue = new QueryQueue();
			
			PtMnuDVo ptMnuDVo;
			PtPltSetupRVo ptPltSetupRVo;
			PtMnuGrpBVo ptMnuGrpBVo;
			PtMnuLoutDVo ptMnuLoutDVo = new PtMnuLoutDVo();
			int i, size = jsonArray==null ? 0 : jsonArray.size(), count;
			for(i=0;i<size;i++){
				mnuGrpId = (String)jsonArray.get(i);
				
				// 사용여부 체크
				ptMnuLoutDVo.setMnuGrpId(mnuGrpId);
				count = commonSvc.count(ptMnuLoutDVo);
				
				if(count>0){
					ptMnuGrpBVo = new PtMnuGrpBVo();
					ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
					ptMnuGrpBVo.setQueryLang(langTypCd);
					ptMnuGrpBVo = (PtMnuGrpBVo)commonSvc.queryVo(ptMnuGrpBVo);
					
					//pt.msg.not.del.mnuGrp.inUse=사용중인 메뉴그룹은 삭제 할 수 없습니다.
					message = messageProperties.getMessage("pt.msg.not.del.mnuGrp.inUse", request);
					if(ptMnuGrpBVo!=null){
						LOGGER.error(message+" (tab:PT_MNU_LOUT_D) - mnuGrpId:"+mnuGrpId+" : "+ptMnuGrpBVo.getRescNm());
						throw new CmException(message+" : "+ptMnuGrpBVo.getRescNm());
					} else {
						LOGGER.error(message+" (tab:PT_MNU_LOUT_D) - mnuGrpId:"+mnuGrpId);
						throw new CmException(message+" - mnuGrpId:"+mnuGrpId);
					}
				}
				
				// 메뉴상세(PT_MNU_D) 테이블 - 삭제
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuGrpId(mnuGrpId);
				queryQueue.delete(ptMnuDVo);
				
				// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 - 삭제
				ptPltSetupRVo = new PtPltSetupRVo();
				ptPltSetupRVo.setMnuGrpId(mnuGrpId);
				queryQueue.delete(ptPltSetupRVo);
				
				// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - 삭제
				ptMnuGrpBVo = new PtMnuGrpBVo();
				ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
				queryQueue.delete(ptMnuGrpBVo);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			//cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 메뉴그룹 순서변경 */
	@RequestMapping(value = "/pt/adm/mnu/transMnuGrpMoveAjx")
	public String transMnuGrpMoveAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "mnuGrpMdCd", required = false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		JSONArray jsonArray = (JSONArray)jsonObject.get("mnuGrpIds");
		
		// 쿼리의 일괄 SQL 실행을 위한  Vo 보관 객체
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		String mnuGrpId, direction = (String)jsonObject.get("direction");
		PtMnuGrpBVo ptMnuGrpBVo, storedPtMnuGrpBVo;
		
		if(mnuGrpMdCd==null) mnuGrpMdCd = "U";
		
		// 위로 이동
		if("up".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			ptMnuGrpBVo = new PtMnuGrpBVo();
			for(int i=0;i<jsonArray.size();i++){
				mnuGrpId = (String)jsonArray.get(i);
				ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
				storedPtMnuGrpBVo = (PtMnuGrpBVo)commonSvc.queryVo(ptMnuGrpBVo);
				curOrdr = Integer.valueOf(storedPtMnuGrpBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				// 옮겨 갈곳의 순서를 내림
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_GRP_B");
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setPkCol("MNU_GRP_MD_CD");
				ptxSortOrdrChnVo.setPk(mnuGrpMdCd);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				// 선택된 것의 순서를 올림
				storedPtMnuGrpBVo = new PtMnuGrpBVo();
				storedPtMnuGrpBVo.setMnuGrpId(mnuGrpId);
				storedPtMnuGrpBVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuGrpBVo);
				
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				model.put("result", "ok");
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			
			return JsonUtil.returnJson(model);
			
		// 아래로 이동
		} else if("down".equals(direction)){
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_MNU_GRP_B");
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			ptMnuGrpBVo = new PtMnuGrpBVo();
			for(int i=jsonArray.size()-1;i>=0;i--){
				mnuGrpId = (String)jsonArray.get(i);
				ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
				storedPtMnuGrpBVo = (PtMnuGrpBVo)commonSvc.queryVo(ptMnuGrpBVo);
				curOrdr = Integer.valueOf(storedPtMnuGrpBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				// 옮겨갈 곳의 순서를 올림
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_GRP_B");
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setPkCol("MNU_GRP_MD_CD");
				ptxSortOrdrChnVo.setPk(mnuGrpMdCd);
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				// 선택된 것의 순서를 내림
				storedPtMnuGrpBVo = new PtMnuGrpBVo();
				storedPtMnuGrpBVo.setMnuGrpId(mnuGrpId);
				storedPtMnuGrpBVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuGrpBVo);
				
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				model.put("result", "ok");
			} else {
				//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
			}
			return JsonUtil.returnJson(model);
			
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("Comp move(up/down) - direction=="+direction+" : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
	}
	
	/** 메뉴구성 */
	@RequestMapping(value = "/pt/adm/mnu/setAdmMnu")
	public String setAdmMnu(HttpServletRequest request, ModelMap model) throws Exception {
		String mnuGrpMdCd = "A";//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		model.put("mnuGrpMdCd", mnuGrpMdCd);
		model.put("mnuGrpMdCdParam", "&mnuGrpMdCd="+mnuGrpMdCd);
		return setMnu(request, model);
	}
	
	/** 메뉴구성 */
	@RequestMapping(value = "/pt/adm/mnu/setMobileMnu")
	public String setMobileMnu(HttpServletRequest request, ModelMap model) throws Exception {
		String mnuGrpMdCd = "M";//메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
		model.put("mnuGrpMdCd", mnuGrpMdCd);
		model.put("mnuGrpMdCdParam", "&mnuGrpMdCd="+mnuGrpMdCd);
		return setMnu(request, model);
	}
	
	/** 메뉴구성 */
	@RequestMapping(value = "/pt/adm/mnu/setMnu")
	public String setMnu(HttpServletRequest request, ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/pt/adm/mnu/setMnu");// Ctrl 단 로직 없음
	}
	
	/** [프레임] 메뉴트리조회 */
	@RequestMapping(value = "/pt/adm/mnu/treeMnuFrm")
	public String treeMnuFrm(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			@Parameter(name="useYn", required=false) String useYn,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//메뉴상세(PT_MNU_D) 테이블
		PtMnuDVo ptMnuDVo = new PtMnuDVo();
		ptMnuDVo.setMnuGrpId(mnuGrpId);
		ptMnuDVo.setUseYn(useYn);
		ptMnuDVo.setQueryLang(langTypCd);
		
		ptMnuDVo.setOrderBy("T.MNU_PID, T.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
		model.put("ptMnuDVoList", ptMnuDVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/mnu/treeMnuFrm");
	}
	
	/** [팝업] 폴더상세조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/pt/adm/mnu/setFldPop")
	public String setFldPop(HttpServletRequest request,
			@Parameter(name="mnuId", required=false) String mnuId,
			@Parameter(name="mnuPid", required=false) String mnuPid,
			ModelMap model) throws Exception {
		
		PtMnuDVo ptMnuDVo = null;
		if(mnuId!=null && !mnuId.isEmpty()){
			// 메뉴상세(PT_MNU_D) 테이블 - 조회
			ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setMnuId(mnuId);
			ptMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
			if(ptMnuDVo!=null){
				// 리소스기본(PT_RESC_B) 테이블 조회
				ptRescSvc.queryRescBVo(ptMnuDVo.getRescId(), model);
			}
		}
		if(ptMnuDVo==null) {
			ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setMnuPid(mnuPid);
		}
		model.put("ptMnuDVo", ptMnuDVo);
		
		return LayoutUtil.getJspPath("/pt/adm/mnu/setFldPop");
	}
	
	/** 폴더저장 */
	@RequestMapping(value = "/pt/adm/mnu/transFld")
	public String transFld(HttpServletRequest request,
//			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="mnuId", required=false) String mnuId,
			@Parameter(name="mnuPid", required=false) String mnuPid,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,
			@Parameter(name="mode", required=false) String mode,
			@Parameter(name="callback", required=false) String callback,
			ModelMap model) throws Exception {
		
		boolean valid = true;
		boolean isNew = mnuId==null || mnuId.isEmpty();
		if(isNew){// reg
			if(mnuPid==null || mnuPid.isEmpty() || ("ROOT".equals(mnuPid) && (mnuGrpId==null || mnuGrpId.isEmpty()))){
				valid = false;
			}
		}
		if(!valid){
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save mnu[folder] - mnuPid:"+mnuPid+" mnuGrpId:"+mnuGrpId+" mnuId:"+mnuId+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 메뉴상세(PT_MNU_D) 테이블
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			VoUtil.bind(request, ptMnuDVo);
			// ROOT -> mnuGrpId
			if("ROOT".equals(mnuPid)) ptMnuDVo.setMnuPid(mnuGrpId);
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
			ptMnuDVo.setRescId(ptRescBVo.getRescId());
			ptMnuDVo.setMnuNm(ptRescBVo.getRescVa());
			
			ptMnuDVo.setModDt("sysdate");
			ptMnuDVo.setModrUid(userVo.getUserUid());
			
			if(isNew){
				if(mnuGrpMdCd==null) mnuGrpMdCd = "U";
				
				mnuId = ptCmSvc.createId("PT_MNU_D");//"PT_MNU_D", 'M', 8
				ptMnuDVo.setMnuId(mnuId);
				ptMnuDVo.setFldYn("Y");
				ptMnuDVo.setMnuTypCd("");
				ptMnuDVo.setUseYn("Y");
				ptMnuDVo.setSysMnuYn("N");
				ptMnuDVo.setRegDt("sysdate");
				ptMnuDVo.setMnuGrpMdCd(mnuGrpMdCd);
				ptMnuDVo.setRegrUid(userVo.getUserUid());
				// 메뉴상세(PT_MNU_D) 테이블 - INSERT
				queryQueue.insert(ptMnuDVo);
			} else {
				// 메뉴상세(PT_MNU_D) 테이블 - UPDATE
				queryQueue.update(ptMnuDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(callback==null || callback.isEmpty()) callback="reloadTree";
			if(isNew){
				model.put("todo", "parent."+callback+"('"+mnuPid+"','');");
			} else {
				model.put("todo", "parent."+callback+"('"+mnuPid+"','"+mnuId+"');");
			}
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [프레임] 메뉴목록조회 */
	@RequestMapping(value = "/pt/adm/mnu/listMnuFrm")
	public String listMnuFrm(HttpServletRequest request,
			@Parameter(name="mnuPid", required=false) String mnuPid,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		
		if(	(mnuPid!=null && !mnuPid.isEmpty()) || (mnuGrpId!=null && !mnuGrpId.isEmpty()) ){
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 메뉴구분코드
			List<PtCdBVo> mnuTypCdList = ptCmSvc.getCdList("MNU_TYP_CD", langTypCd, "Y");
			model.put("mnuTypCdList", mnuTypCdList);
			
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			if(mnuPid!=null && !mnuPid.isEmpty()){
				if("ROOT".equals(mnuPid)) mnuPid = mnuGrpId;
				ptMnuDVo.setMnuPid(mnuPid);
			}
			if(mnuGrpId!=null && !mnuGrpId.isEmpty()) ptMnuDVo.setMnuGrpId(mnuGrpId);
			ptMnuDVo.setQueryLang(langTypCd);
			ptMnuDVo.setOrderBy("SORT_ORDR");
			
			// 메뉴 목록 조회
			@SuppressWarnings("unchecked")
			List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
			// 화면 구성용 2개의 빈 vo 넣음
			ptMnuDVoList.add(new PtMnuDVo());
			ptMnuDVoList.add(new PtMnuDVo());
			model.put("ptMnuDVoList", ptMnuDVoList);
			
			// 리소스 조회
			PtRescBVo ptRescBVo = new PtRescBVo();
			ptRescBVo.setMnuPid(mnuPid);
			@SuppressWarnings("unchecked")
			List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);

			// 코드 리소스 model에 저장
			if(ptRescBVoList!=null){
				for(PtRescBVo storedPtRescBVo : ptRescBVoList){
					model.put(storedPtRescBVo.getRescId()+"_"+storedPtRescBVo.getLangTypCd(), storedPtRescBVo.getRescVa());
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/mnu/listMnuFrm");
	}
	
	/** [프레임] 메뉴상세조회 */
	@RequestMapping(value = "/pt/adm/mnu/setMnuFrm")
	public String setMnuFrm(HttpServletRequest request,
			@Parameter(name="mnuId", required=false) String mnuId,
			@Parameter(name="mnuPid", required=false) String mnuPid,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 메뉴구분코드
		List<PtCdBVo> mnuTypCdList = ptCmSvc.getCdList("MNU_TYP_CD", langTypCd, "Y");
		model.put("mnuTypCdList", mnuTypCdList);
		
		setFldPop(request, mnuId, mnuPid, model);
		return LayoutUtil.getJspPath("/pt/adm/mnu/setMnuFrm");
	}
	
	/** 메뉴저장 - 단건 메뉴 저장 (폴더가 아닌 메뉴 클릭 후 상세 화면에서 저장) */
	@RequestMapping(value = "/pt/adm/mnu/transMnu")
	public String transMnu(HttpServletRequest request,
			@Parameter(name="mnuId", required=false) String mnuId,
			ModelMap model) throws Exception {
		
		try{
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 메뉴상세(PT_MNU_D) 테이블
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			VoUtil.bind(request, ptMnuDVo);
			
			// mnuUrl trim
			if(ptMnuDVo.getMnuUrl()!=null && !ptMnuDVo.getMnuUrl().isEmpty()) {
				ptMnuDVo.setMnuUrl(ptMnuDVo.getMnuUrl().trim());
			}
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
			ptMnuDVo.setRescId(ptRescBVo.getRescId());
			ptMnuDVo.setMnuNm(ptRescBVo.getRescVa());
			
			ptMnuDVo.setModDt("sysdate");
			ptMnuDVo.setModrUid(userVo.getUserUid());
			
			//메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업
			String mnuTypCd = ptMnuDVo.getMnuTypCd();
			// 폴더면
			if("Y".equals(ptMnuDVo.getFldYn())){
				ptMnuDVo.setMnuUrl("");//메뉴URL
				ptMnuDVo.setMnuTypCd("");//메뉴구분코드
				ptMnuDVo.setPopSetupCont("");//팝업설정내용
			// 외부팝업 이 아니면
			} else if(!"OUT_POP".equals(mnuTypCd)){
				ptMnuDVo.setPopSetupCont("");//팝업설정내용
			}
			
			// 신규 등록의 경우
			if(ptMnuDVo.getMnuId()==null || ptMnuDVo.getMnuId().isEmpty()){
				
				// 사용여부 없으면
				if(ptMnuDVo.getUseYn()==null || ptMnuDVo.getUseYn().isEmpty()){
					ptMnuDVo.setUseYn("Y");
				}
				// 메뉴그룹모듈코드 - A:관리자, U:사용자, M:모바일
				if(ptMnuDVo.getMnuGrpMdCd()==null){
					ptMnuDVo.setMnuGrpMdCd("U");
				}
				// 시스템메뉴여부 없으면
				if(ptMnuDVo.getSysMnuYn()==null || ptMnuDVo.getSysMnuYn().isEmpty()){
					ptMnuDVo.setSysMnuYn("N");
				}
				// 등록일시, 등록자
				ptMnuDVo.setRegDt("sysdate");
				ptMnuDVo.setRegrUid(userVo.getUserUid());
				
				queryQueue.insert(ptMnuDVo);
			// 수정
			} else {
				queryQueue.update(ptMnuDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+mnuId+"');");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 메뉴저장 - 메뉴 일괄 저장(오른쪽 프레임) */
	@RequestMapping(value = "/pt/adm/mnu/transMnuList")
	public String transMnuList(HttpServletRequest request,
			@Parameter(name="mnuPid", required=false)String mnuPid,
			@Parameter(name="mnuGrpId", required=false)String mnuGrpId,
			@Parameter(name="delList", required=false)String delList,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			QueryQueue queryQueue = new QueryQueue();
			UserVo userVo = LoginSession.getUser(request);
			
			if(mnuGrpMdCd==null) mnuGrpMdCd = "U";
			
			// 리소스기본(PT_RESC_B) 테이블
			PtRescBVo ptRescBVo;
			
			// 삭제 목록의 것 삭제 SQL 처리
			// 메뉴상세(PT_MNU_D) 테이블
			PtMnuDVo ptMnuDVo, storedPtMnuDVo, checkPtMnuDVo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			for(int i=0;i<delCds.length;i++){
				
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuPid(delCds[i]);
				if(commonSvc.count(ptMnuDVo)>0){
					// pt.msg.not.del.mnu.withChild=자식 메뉴가 있는 폴더는 지울수가 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.del.mnu.withChild", request)
							+ " - mnuPid:"+delCds[i];
					LOGGER.error(msg);
					throw new CmException(msg);
				}
				
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuId(delCds[i]);
				ptMnuDVo.setQueryLang(langTypCd);
				
				storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
				if(storedPtMnuDVo!=null && "Y".equals(storedPtMnuDVo.getSysMnuYn())){
					// pt.msg.not.del.mnu.sys=시스템 메뉴는 삭제 할 수 업습니다.
					String msg = messageProperties.getMessage("pt.msg.not.del.mnu.sys", request)
							+ " - mnuId:"+storedPtMnuDVo.getMnuId()+"  name:"+storedPtMnuDVo.getRescNm();
					LOGGER.error(msg);
					throw new CmException(msg);
				}
				
				// 리소스기본(PT_RESC_B) 테이블 - 삭제
				if(storedPtMnuDVo.getRescId()!=null && !storedPtMnuDVo.getRescId().isEmpty()){
					ptRescBVo = new PtRescBVo();
					ptRescBVo.setRescId(storedPtMnuDVo.getRescId());
					queryQueue.delete(ptRescBVo);
				}
				
				// 메뉴상세(PT_MNU_D) 테이블 - 삭제
				queryQueue.delete(ptMnuDVo);
			}
			
			String mnuTypCd;
			
			//  메뉴상세(PT_MNU_D) 테이블 VO에 데이터 담음
			@SuppressWarnings("unchecked")
			List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)VoUtil.bindList(request, PtMnuDVo.class, new String[]{"valid"});
			for(int i=0;i<ptMnuDVoList.size();i++){
				ptMnuDVo = ptMnuDVoList.get(i);
				ptMnuDVo.setModDt("sysdate");
				ptMnuDVo.setModrUid(userVo.getUserUid());
				// ROOT -> mnuGrpId
				if("ROOT".equals(mnuPid)) ptMnuDVo.setMnuPid(mnuGrpId);
				
				// mnuUrl trim
				if(ptMnuDVo.getMnuUrl()!=null && !ptMnuDVo.getMnuUrl().isEmpty()) {
					ptMnuDVo.setMnuUrl(ptMnuDVo.getMnuUrl().trim());
				}
				
				// 메뉴ID 생성 및 디폴트 데이터 세팅
				if(ptMnuDVo.getMnuId()==null || ptMnuDVo.getMnuId().isEmpty()){
					ptMnuDVo.setMnuId(ptCmSvc.createId("PT_MNU_D"));//"PT_MNU_D", 'M', 8
					ptMnuDVo.setRegDt("sysdate");
					ptMnuDVo.setRegrUid(userVo.getUserUid());
					ptMnuDVo.setSysMnuYn("N");
					ptMnuDVo.setMnuGrpMdCd(mnuGrpMdCd);
				
				// 수정의 경우 - 폴더일 때 - 자식이 있는 폴더를 메뉴로 바꾸면 오류 메세지 나타냄
				} else if("N".equals(ptMnuDVo.getFldYn())){
					
					checkPtMnuDVo = new PtMnuDVo();
					checkPtMnuDVo.setMnuPid(ptMnuDVo.getMnuId());
					if(commonSvc.count(checkPtMnuDVo)>0){
						// pt.msg.not.chg.intoMnu.withChild=자식이 있는 폴더는 메뉴로 변경 할 수 없습니다.
						String msg = messageProperties.getMessage("pt.msg.not.chg.intoMnu.withChild", request)
								+ " - mnuPid:"+ptMnuDVo.getMnuId();
						LOGGER.error(msg);
						throw new CmException(msg);
					}
				}
				
				//메뉴구분코드 - IN_URL:내부URL, IN_POP:내부팝업, OUT_URL:외부URL, OUT_POP:외부팝업
				mnuTypCd = ptMnuDVo.getMnuTypCd();
				// 폴더면
				if("Y".equals(ptMnuDVo.getFldYn())){
					ptMnuDVo.setMnuUrl("");//메뉴URL
					ptMnuDVo.setMnuTypCd("");//메뉴구분코드
					ptMnuDVo.setPopSetupCont("");//팝업설정내용
				// 외부팝업 이 아니면
				} else if(!"OUT_POP".equals(mnuTypCd)){
					ptMnuDVo.setPopSetupCont("");//팝업설정내용
				}
			}
			
			// 리소스 정보 queryQueue에 담음
			ptRescSvc.collectPtRescBVoList(request, queryQueue, ptMnuDVoList, "valid", "rescId", "mnuNm");

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+mnuPid+"');");
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 폴더삭제 */
	@RequestMapping(value = "/pt/adm/mnu/transFldDelAjx")
	public String transFldDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		JSONArray jsonArray = (JSONArray)jsonObject.get("mnuIds");
		String mnuId;
		
		// 리소스기본(PT_RESC_B) 테이블
		PtRescBVo ptRescBVo;
		
		// 메뉴상세(PT_MNU_D) 테이블 - SELECT
		PtMnuDVo ptMnuDVo = new PtMnuDVo(), storedPtMnuDVo;
		
		int i, count;
		String msg;
		for(i=0;i<jsonArray.size();i++){
			
			mnuId = (String)jsonArray.get(i);
			ptMnuDVo.setMnuPid(mnuId);
			count = commonSvc.count(ptMnuDVo);
			
			if(count>0){
				// pt.msg.not.del.mnu.withChild=자식 메뉴가 있는 폴더는 지울수가 없습니다.
				msg = messageProperties.getMessage("pt.msg.not.del.mnu.withChild", request);
				model.put("message", msg);
				LOGGER.error(msg+"  - mnuPid:"+mnuId);
				return JsonUtil.returnJson(model);
			}
			
			ptMnuDVo.setMnuPid(null);
			ptMnuDVo.setMnuId(mnuId);
			storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
			if("Y".equals(storedPtMnuDVo.getSysMnuYn())){
				// pt.msg.not.del.mnu.sys=시스템 메뉴는 삭제 할 수 업습니다.
				msg = messageProperties.getMessage("pt.msg.not.del.mnu.sys", request);
				model.put("message", msg);
				LOGGER.error(msg+"  - mnuId:"+mnuId);
				return JsonUtil.returnJson(model);
			}
			
			// 리소스기본(PT_RESC_B) 테이블 - 삭제
			if(storedPtMnuDVo.getRescId()!=null && !storedPtMnuDVo.getRescId().isEmpty()){
				ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(storedPtMnuDVo.getRescId());
				queryQueue.delete(ptRescBVo);
			}
			
			// 메뉴상세(PT_MNU_D) 테이블 삭제
			ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setMnuId(mnuId);
			queryQueue.delete(ptMnuDVo);
			
			// 삭제된 것보다 큰 sortOrder 하나씩 줄임
			PtxSortOrdrChnVo ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			ptxSortOrdrChnVo.setTabId("PT_MNU_D");
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setPkCol("MNU_PID");
			ptxSortOrdrChnVo.setPk(storedPtMnuDVo.getMnuPid());
			ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedPtMnuDVo.getSortOrdr()));
			queryQueue.update(ptxSortOrdrChnVo);
			
		}
		
		if(!queryQueue.isEmpty()){
			try {

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
			} catch(Exception e){
				msg = e.getMessage();
				LOGGER.error(msg);
				e.printStackTrace();
				model.put("message", msg);
				return JsonUtil.returnJson(model);
			}
		}
		
		//cm.msg.del.success=삭제 되었습니다.
		msg = messageProperties.getMessage("cm.msg.del.success", request);
		model.put("message", msg);
		model.put("result", "ok");
		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] - 폴더순서변경 */
	@RequestMapping(value = "/pt/adm/mnu/transFldMoveAjx")
	public String transFldMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		//
		// 폴더 및 메뉴의 순서 목적으로 만들었는데
		// 메뉴의 순서는 저장하면서 한번에 바뀌므로 실제로
		// mnuIds - 여기에 한개의 폴더 아이디만 들어옴
		//
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		JSONArray jsonArray = (JSONArray)jsonObject.get("mnuIds");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(direction==null) LOGGER.error("Menu move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
			
		PtMnuDVo ptMnuDVo = new PtMnuDVo(), storedPtMnuDVo;
		// 위로 이동
		if("up".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			ptMnuDVo = new PtMnuDVo();
			
			String mnuId, mnuPid=null;
			for(int i=0;i<jsonArray.size();i++){
				mnuId = (String)jsonArray.get(i);
				
				ptMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
				if(storedPtMnuDVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-UP - mnuId:"+mnuId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedPtMnuDVo.getSortOrdr());
				mnuPid = storedPtMnuDVo.getMnuPid();
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_D");
				ptxSortOrdrChnVo.setPkCol("MNU_PID");
				ptxSortOrdrChnVo.setPk(storedPtMnuDVo.getMnuPid());
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuDVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mnuIds 에 세팅함
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuPid(mnuPid);
				ptMnuDVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
				int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
				List<String> mnuIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mnuIdList.add(ptMnuDVoList.get(i).getMnuId());
				}
				model.put("mnuIds", mnuIdList);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			return JsonUtil.returnJson(model);
			
			// 아래로 이동
		} else if("down".equals(direction)){
			
			String mnuPid = (String)jsonObject.get("mnuPid");
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_MNU_D");
			ptxSortOrdrChnVo.setPkCol("MNU_PID");
			ptxSortOrdrChnVo.setPk(mnuPid);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			ptMnuDVo = new PtMnuDVo();
			
			String mnuId;
			for(int i=jsonArray.size()-1;i>=0;i--){
				mnuId = (String)jsonArray.get(i);
				
				ptMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
				if(storedPtMnuDVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-DOWN - mnuId:"+mnuId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedPtMnuDVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_D");
				ptxSortOrdrChnVo.setPkCol("MNU_PID");
				ptxSortOrdrChnVo.setPk(storedPtMnuDVo.getMnuPid());
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuDVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mnuIds 에 세팅함
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuPid(mnuPid);
				ptMnuDVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
				int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
				List<String> mnuIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mnuIdList.add(ptMnuDVoList.get(i).getMnuId());
				}
				
				model.put("mnuIds", mnuIdList);
				
			} else {
				//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
			}
			return JsonUtil.returnJson(model);
			
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("Code move(up/down) - direction=="+direction+" : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}

	}
	
	// 메뉴 순서 일괄 변경 - 정렬순서 꼬였을때
//	@RequestMapping(value = "/cm/mnu/modifyOrdr")
//	public String modifyOrdr(HttpServletRequest request,
//			@Parameter(name="data", required=false)String data,
//			ModelMap model) throws Exception {
//		
//		PtMnuDVo ptMnuDVo = new PtMnuDVo(), vo;
//		ptMnuDVo.setOrderBy("MNU_GRP_ID, MNU_PID, SORT_ORDR");
//		@SuppressWarnings("unchecked")
//		List<PtMnuDVo> list = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
//		
//		int i, size = list.size();
//		Integer order = 1;
//		String mnuGrpId=null, mnuPid=null;
//		
//		QueryQueue queryQueue = new QueryQueue();
//		
//		for(i=0;i<size;i++){
//			ptMnuDVo = list.get(i);
//			if(mnuGrpId==null || !mnuGrpId.equals(ptMnuDVo.getMnuGrpId())){
//				mnuPid=null;
//				mnuGrpId = ptMnuDVo.getMnuGrpId();
//			}
//			if(mnuPid==null || !mnuPid.equals(ptMnuDVo.getMnuPid())){
//				order = 1;
//				mnuPid = ptMnuDVo.getMnuPid();
//			}
//			
//			vo = null;
//			if(mnuPid.equals("ROOT")){
//				vo = new PtMnuDVo();
//				vo.setMnuId(ptMnuDVo.getMnuId());
//				vo.setMnuPid(mnuGrpId);
//			}
//			if(!order.toString().equals(ptMnuDVo.getSortOrdr())){
//				if(vo==null){
//					vo = new PtMnuDVo();
//					vo.setMnuId(ptMnuDVo.getMnuId());
//				}
//				vo.setSortOrdr(order.toString());
//			}
//			if(vo!=null){
//				queryQueue.update(vo);
//				//System.out.println(vo);
//			}
//			order++;
//		}
//		
//		commonSvc.execute(queryQueue);
//		
//		model.put("message", "ok");
//		return LayoutUtil.getResultJsp();
//	}
}
