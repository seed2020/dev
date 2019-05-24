package com.innobiz.orange.web.pt.admCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtPltSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.pt.vo.PtPltSetupRVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;

/** 포틀릿 설정 */
@Controller
public class PtPltCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtPltCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포틀릿 관련 서비스 */
	@Autowired
	private PtPltSvc ptPltSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 포틀릿목록 */
	@RequestMapping(value = "/pt/adm/plt/listPltMng")
	public String listPlt(HttpServletRequest request,
			@Parameter(name="popYn", required=false) String popYn,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 코드조회 : PLT_CAT_CD (포틀릿카테고리코드)
		List<PtCdBVo> pltCatCdList = ptCmSvc.getCdList("PLT_CAT_CD", langTypCd, "Y");
		model.put("pltCatCdList", pltCatCdList);
		
		// 포틀릿상세(PT_PLT_D) 테이블 - 변수 바인딩
		PtPltDVo ptPltDVo = new PtPltDVo();
		VoUtil.bind(request, ptPltDVo);
		ptPltDVo.setQueryLang(langTypCd);
		
		// 팝업용 - 사용자/관리자 메뉴그룹 관리 > 포틀릿 구성 > [다음] > 포틀릿 추가
		// listPltPop() 에서 호출
		if("Y".equals(popYn)){
			ptPltDVo.setUseYn("Y");
			List<String> openCompIdList = new ArrayList<String>();
			openCompIdList.add(PtConstant.SYS_COMP_ID);
			openCompIdList.add(userVo.getCompId());
			ptPltDVo.setOpenCompIdList(openCompIdList);
		} else {
			String setupId = "portlet";
			model.put("setupId", setupId);
			if(!SecuUtil.hasAuth(request, "SYS", null)){
				Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
				String setupVa = map==null ? null : map.get(setupId);
				if(setupVa == null || "myComp".equals(setupVa)){
					ptPltDVo.setOpenCompId(userVo.getCompId());
				}
			}
		}
		// 카테고리 명 순, 포틀릿 명 순
		ptPltDVo.setOrderBy("PLT_CAT_NM, RESC_NM");
		
		// 레코드수 조회 - 패지징
		Integer recodeCount = commonSvc.count(ptPltDVo);
		// 페이지 정보 세팅
		PersonalUtil.setPaging(request, ptPltDVo, recodeCount);
		
		// 목록조회
		@SuppressWarnings("unchecked")
		List<PtPltDVo> ptPltDVoList = (List<PtPltDVo>)commonSvc.queryList(ptPltDVo);
		
		// 공개범위 - 회사명 세팅
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		String openCompId, allCompNm = messageProperties.getMessage("cm.option.allComp", request);//전체회사
		
		// 공개범위 - 회사명 세팅
		if(ptPltDVoList!=null){
			for(PtPltDVo storedPtPltDVo : ptPltDVoList){
				openCompId = storedPtPltDVo.getOpenCompId();
				if(openCompId==null || openCompId.equals(PtConstant.SYS_COMP_ID) || openCompId.isEmpty()){
					storedPtPltDVo.setOpenCompNm(allCompNm);
				} else {
					ptCompBVo = ptCompMap.get(openCompId);
					storedPtPltDVo.setOpenCompNm(ptCompBVo==null ? null : ptCompBVo.getRescNm());
				}
			}
		}
		
		model.put("recodeCount", recodeCount);
		model.put("ptPltDVoList", ptPltDVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/plt/listPltMng");
	}
	
	/** [팝업] 관리범위 설정 */
	@RequestMapping(value = "/pt/adm/plt/setMngCompPop")
	public String setMngCompPop(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			ModelMap model) throws Exception {
		
		Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
		String setupVa = map==null ? null : map.get(setupId);
		model.put("setupVa", setupVa==null ? "allComp" : setupVa);
		
		return LayoutUtil.getJspPath("/pt/adm/plt/setMngCompPop");
	}
	
	/** [히든프레임] 관리범위 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/plt/transMngComp")
	public String transMngComp(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			@Parameter(name="setupVa", required=false) String setupVa,
			ModelMap model) throws Exception {
		
		try{
			QueryQueue queryQueue = new QueryQueue();
			
//			// 현재 년월일
//			String hstYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			
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
	
	/** [팝업] 포틀릿조회 - 수정,등록용 */
	@RequestMapping(value = "/pt/adm/plt/setPltPop")
	public String setPltPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 코드조회 : PLT_CAT_CD (포틀릿카테고리코드)
		List<PtCdBVo> pltCatCdList = ptCmSvc.getCdList("PLT_CAT_CD", langTypCd, "Y");
		model.put("pltCatCdList", pltCatCdList);
		
		// 포틀릿상세(PT_PLT_D) 테이블 - 변수 바인딩
		PtPltDVo ptPltDVo = new PtPltDVo();
		VoUtil.bind(request, ptPltDVo);
		
		// 포틀릿상세(PT_PLT_D) 테이블 - 단건조회
		PtPltDVo storedPtPltDVo = null;
		if(ptPltDVo.getPltId()!=null && !ptPltDVo.getPltId().isEmpty()){
			storedPtPltDVo = (PtPltDVo)commonSvc.queryVo(ptPltDVo);
		}
		
		if(storedPtPltDVo!=null){
			model.put("ptPltDVo", storedPtPltDVo);
			// 리소스기본(PT_RESC_B) 테이블 조회
			ptRescSvc.queryRescBVo(storedPtPltDVo.getRescId(), model);
		} else {
			storedPtPltDVo = new PtPltDVo();
			// 포틀릿 설정 조회
			Map<String, String> pltPolc = ptSysSvc.getPltPolc();
			storedPtPltDVo.setWdthPx(pltPolc.get("defaultWidth"));
			storedPtPltDVo.setHghtPx(pltPolc.get("defaultHeight"));
			model.put("ptPltDVo", storedPtPltDVo);
		}

		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
		model.put("sysCompId", PtConstant.SYS_COMP_ID);
		
		return LayoutUtil.getJspPath("/pt/adm/plt/setPltPop");
	}
	
	/** [팝업] 포틀릿저장 */
	@RequestMapping(value = "/pt/adm/plt/transPltMng")
	public String transPlt(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			
			// 포틀릿상세(PT_PLT_D) 테이블 - 변수 바인딩
			PtPltDVo ptPltDVo = new PtPltDVo();
			VoUtil.bind(request, ptPltDVo);
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 리소스ID, 포틀릿명
			ptPltDVo.setRescId(ptRescBVo.getRescId());
			ptPltDVo.setPltNm(ptRescBVo.getRescVa());
			
			// 수정일, 수정자
			ptPltDVo.setModDt("sysdate");
			ptPltDVo.setModrUid(userVo.getUserUid());
			
			// 신규 포틀릿 등록
			boolean isNew = false;
			if(pltId==null || pltId.isEmpty()){
				isNew = true;
				
				// 포틀릿 ID 생성
				ptPltDVo.setPltId(ptCmSvc.createId("PT_PLT_D"));
				// 등록일, 등록자
				ptPltDVo.setModDt("sysdate");
				ptPltDVo.setModrUid(userVo.getUserUid());
				if(ptPltDVo.getUseYn()==null || ptPltDVo.getUseYn().isEmpty()){
					ptPltDVo.setUseYn("Y");
				}
				
				if(ptPltDVo.getOpenCompId()==null) {
					ptPltDVo.setOpenCompId(userVo.getCompId());
				}
				
				queryQueue.insert(ptPltDVo);
			// 수정
			} else {
				queryQueue.update(ptPltDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(isNew){
				model.put("todo", "parent.location.replace('./listPltMng.do?menuId="+menuId+"');");
			} else {
				model.put("todo", "parent.location.replace(parent.location.href);");
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
	
	/** [AJAX] 포틀릿삭제 */
	@RequestMapping(value = "/pt/adm/plt/transPltDelAjx")
	public String transPltDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)jsonObject.get("pltIds");
			String pltId;
			String langTypCd = LoginSession.getLangTypCd(request);
			
			int i, size = jsonArray==null ? 0 : jsonArray.size(), count;
			if(size==0){
				//cm.msg.noSelect=선택한 항목이 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.noSelect", request));
				return JsonUtil.returnJson(model);
			}
			
			// 포틀릿상세(PT_PLT_D) 테이블
			PtPltDVo ptPltDVo, storedPtPltDVo;
			
			// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블
			PtPltSetupRVo storedPtPltSetupRVo = null, ptPltSetupRVo = new PtPltSetupRVo();
			PtMnuGrpBVo storedPtMnuGrpBVo;
			
			// 사용여부 체크
			for(i=0;i<size;i++){
				pltId = (String)jsonArray.get(i);
				ptPltSetupRVo.setPltId(pltId);
				@SuppressWarnings("unchecked")
				List<PtPltSetupRVo> storedPtPltSetupRVoList = (List<PtPltSetupRVo>)commonSvc.queryList(ptPltSetupRVo);
				
				// 사용중인 포틀릿이 있을 경우
				if(storedPtPltSetupRVoList != null && !storedPtPltSetupRVoList.isEmpty()){
					
					storedPtPltSetupRVo = storedPtPltSetupRVoList.get(0);
					
					// 메뉴그룹 조회
					storedPtMnuGrpBVo = new PtMnuGrpBVo();
					storedPtMnuGrpBVo.setMnuGrpId(storedPtPltSetupRVo.getMnuGrpId());
					storedPtMnuGrpBVo.setQueryLang(langTypCd);
					storedPtMnuGrpBVo = (PtMnuGrpBVo)commonSvc.queryVo(storedPtMnuGrpBVo);
					
					// 포틀릿 조회
					ptPltDVo = new PtPltDVo();
					ptPltDVo.setPltId(pltId);
					storedPtPltDVo = (PtPltDVo)commonSvc.queryVo(ptPltDVo);
					
					if(storedPtMnuGrpBVo != null && storedPtPltDVo != null){
						// pt.msg.not.del.plt.inUse=사용중인 포틀릿은 삭제 할 수 없습니다.
						// cols.mnuGrpNm=메뉴그룹명
						// cols.pltNm=포틀릿명
						String msg = messageProperties.getMessage("pt.msg.not.del.plt.inUse", locale) + "\n\n"
							+ messageProperties.getMessage("cols.pltNm", locale) + " : " + storedPtPltDVo.getRescNm() + "\n"
							+ messageProperties.getMessage("cols.mnuGrpNm", locale) + " : " + storedPtMnuGrpBVo.getRescNm();
						throw new CmException(msg);
					// 사용되는 메뉴그룹, 포틀릿 정보가 없으면 - 해당 사용정보 삭제 - 원칙적으로 발생 되지 않는 케이스
					} else {
						queryQueue.delete(storedPtPltSetupRVo);
					}
				}
				
				count = commonSvc.count(ptPltSetupRVo);
				
				ptPltDVo = new PtPltDVo();
				ptPltDVo.setPltId(pltId);
				
				// 사용중
				if(count>0){
					storedPtPltDVo = (PtPltDVo)commonSvc.queryVo(ptPltDVo);
					
					//pt.msg.not.del.plt.inUse=사용중인 포틀릿은 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.del.plt.inUse", request)
							+(	storedPtPltDVo==null ? "" : 
								("ko".equals(langTypCd)) ? " - 포틀릿명:"+storedPtPltDVo.getRescNm() :
									" : "+storedPtPltDVo.getRescNm());
					throw new CmException(msg);
				}
				
				queryQueue.delete(ptPltDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", locale));
			model.put("result", "ok");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage().replace("\n\n", "\n"));
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [팝업] 포틀릿목록 */
	@RequestMapping(value = "/pt/adm/mnu/listPltPop")
	public String listPltPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		listPlt(request, "Y", model);
		return LayoutUtil.getJspPath("/pt/adm/plt/listPltPop");
	}
	
	/** 포틀릿 설정 1단계 */
	@RequestMapping(value = "/pt/adm/mnu/setPltStep1")
	public String setPltStep1(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		PtMnuGrpBVo ptMnuGrpBVo = ptLoutSvc.getMnuGrpByGrpId(mnuGrpId, langTypCd);
		if(ptMnuGrpBVo!=null){
			model.put("pltLoutCd", ptMnuGrpBVo.getPltLoutCd());
		}
		
		return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep1");
	}
	
	/** 포틀릿 설정 2단계 */
	@RequestMapping(value = "/pt/adm/mnu/setPltStep2")
	public String setPltStep2(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			@Parameter(name="pltLoutCd", required=false) String pltLoutCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		PtMnuGrpBVo ptMnuGrpBVo = ptLoutSvc.getMnuGrpByGrpId(mnuGrpId, langTypCd);
		if(ptMnuGrpBVo!=null){
			model.put("ptMnuGrpBVo", ptMnuGrpBVo);
			ptPltSvc.setPltZoneList(mnuGrpId, pltLoutCd, langTypCd, model, null, null, null);
		}
		
		// 포틀릿 정책 읽기
		model.put("pltPolc", ptSysSvc.getPltPolc());
		
		// FREE:1단자유구성
		if("FREE".equals(pltLoutCd)){
			// pt.pltSetup.js 추가 include
			model.put("JS_OPTS", new String[]{"pt.pltSetup"});
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Free");
		} else if(pltLoutCd!=null && pltLoutCd.startsWith("D2")){
			// pt.jsp.setPltStep2.left=좌 측
			// pt.jsp.setPltStep2.right=우 측
			model.put("subTitle1", messageProperties.getMessage("pt.jsp.setPltStep2.left", request));
			model.put("subTitle2", messageProperties.getMessage("pt.jsp.setPltStep2.right", request));
			model.put("divCount", Integer.valueOf(2));
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Div");
		} else if(pltLoutCd!=null && pltLoutCd.startsWith("D3")){
			// pt.jsp.setPltStep2.left=좌 측
			// pt.jsp.setPltStep2.middle=중 앙
			// pt.jsp.setPltStep2.right=우 측
			model.put("subTitle1", messageProperties.getMessage("pt.jsp.setPltStep2.left", request));
			model.put("subTitle2", messageProperties.getMessage("pt.jsp.setPltStep2.middle", request));
			model.put("subTitle3", messageProperties.getMessage("pt.jsp.setPltStep2.right", request));
			model.put("divCount", Integer.valueOf(3));
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Div");
		}
		
		//cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
		throw new CmException("cm.msg.notValidPage", request);
	}
	/** 포틀릿 설정 저장(3단계) */
	@RequestMapping(value = "/pt/adm/mnu/transPltSetup")
	public String transPltSetup(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			@Parameter(name="pltLoutCd", required=false) String pltLoutCd,
			@Parameter(name="zoneData1", required=false) String zoneData1,
			@Parameter(name="zoneData2", required=false) String zoneData2,
			@Parameter(name="zoneData3", required=false) String zoneData3,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - 포틀릿레이아웃코드 업데이트
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
		ptMnuGrpBVo.setPltLoutCd(pltLoutCd);
		queryQueue.update(ptMnuGrpBVo);
		
		// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 - 기존데이터 삭제
		PtPltSetupRVo ptPltSetupRVo = new PtPltSetupRVo();
		ptPltSetupRVo.setMnuGrpId(mnuGrpId);
		queryQueue.delete(ptPltSetupRVo);
		
		//FREE
		if("FREE".equals(pltLoutCd)){
			// 포틀릿지역코드 설정된 포틀릿 데이터 모으기
			collectPtPltSetupRVo(mnuGrpId, "1", zoneData1, queryQueue, true);
		} else {
			// 포틀릿지역코드 설정된 포틀릿 데이터 모으기
			collectPtPltSetupRVo(mnuGrpId, "1", zoneData1, queryQueue, false);
			collectPtPltSetupRVo(mnuGrpId, "2", zoneData2, queryQueue, false);
			collectPtPltSetupRVo(mnuGrpId, "3", zoneData3, queryQueue, false);
		}
		
		try {

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.MENU_GRP, CacheConfig.PLT_LAYOUT);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU_GRP, CacheConfig.PLT_LAYOUT);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.goBack();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 포틀릿 설정 데이터 모으기 */
	@SuppressWarnings("unchecked")
	private void collectPtPltSetupRVo(String mnuGrpId, String pltZoneCd, String data, QueryQueue queryQueue, boolean withPosition){
		if(data==null || data.isEmpty()) return;
		
		JSONArray jsonArray = (JSONArray)JSONValue.parse(data);
		
		Object obj;
		Map<String, String> jsonMap;
		PtPltSetupRVo ptPltSetupRVo;
		int i, size = jsonArray==null ? 0 : jsonArray.size();
		for(i=0;i<size;i++){
			jsonMap = (Map<String, String>)jsonArray.get(i);
			
			// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블
			ptPltSetupRVo = new PtPltSetupRVo();
			ptPltSetupRVo.setMnuGrpId(mnuGrpId);
			ptPltSetupRVo.setPltId((String)jsonMap.get("pltId"));
			ptPltSetupRVo.setRescId((String)jsonMap.get("rescId"));
			ptPltSetupRVo.setPltZoneCd(pltZoneCd);
			ptPltSetupRVo.setSortOrdr(Integer.toString(i+1));
			if(withPosition){// 포틀릿레이아웃 이 FREE 인 경우
				obj = jsonMap.get("topPx");
				ptPltSetupRVo.setTopPx(obj==null ? null : obj.toString());//TOP픽셀
				obj = jsonMap.get("leftPx");
				ptPltSetupRVo.setLeftPx(obj==null ? null : obj.toString());//LEFT픽셀
				obj = jsonMap.get("wdthPx");
				ptPltSetupRVo.setWdthPx(obj==null ? null : obj.toString());//넓이픽셀
				obj = jsonMap.get("hghtPx");
				ptPltSetupRVo.setHghtPx(obj==null ? null : obj.toString());//높이픽셀
				obj = jsonMap.get("zidx");
				ptPltSetupRVo.setZidx(obj==null ? null : obj.toString());//Z-INDEX
			}
			// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 - INSERT
			queryQueue.insert(ptPltSetupRVo);
		}
	}
	/** [팝업] 가로/세로선 추가 */
	@RequestMapping(value = "/pt/adm/mnu/setLinePop")
	public String setLinePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// Ctrl 로직 없음 - 단순 팝업
		return LayoutUtil.getJspPath("/pt/adm/plt/setLinePop");
	}
}
