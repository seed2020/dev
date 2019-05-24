package com.innobiz.orange.web.pt.admCtrl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.CombAuthGrpDetl;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtAuthGrpAuthDVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpBVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpMnuPltRVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpUserRVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 권한관리 */
@Controller
public class PtAuthGrpCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtAuthGrpCtrl.class);
	
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
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 권한그룹설정(관리자) */
	@RequestMapping(value = "/pt/adm/auth/setAdminAuthGrp")
	public String setAdminAuthGrp(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		return setUserAuthGrp(request, compId, "A", model);
	}
	/** 권한그룹설정(관리자) */
	@RequestMapping(value = "/pt/adm/auth/setMobileAuthGrp")
	public String setMobileAuthGrp(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		return setUserAuthGrp(request, compId, "M", model);
	}
	/** 권한그룹설정 */
	@RequestMapping(value = "/pt/adm/auth/setUserAuthGrp")
	public String setUserAuthGrp(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="authGrpTypCd", required=false) String authGrpTypCd,
			ModelMap model) throws Exception {
		
		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 팝업 타이틀 설정
		if("A".equals(authGrpTypCd)){
			model.put("regAuthGrp", "regAdmAuthGrp");
			model.put("modAuthGrp", "modAdmAuthGrp");
			model.put("authMdCd", "Admin");
		} else if("M".equals(authGrpTypCd)){
			model.put("regAuthGrp", "regMobileAuthGrp");
			model.put("modAuthGrp", "modMobileAuthGrp");
			model.put("authMdCd", "Mobile");
		} else {
			model.put("regAuthGrp", "regUsrAuthGrp");
			model.put("modAuthGrp", "modUsrAuthGrp");
			model.put("authMdCd", "User");
			authGrpTypCd = "U";//사용자권한그룹
		}
		model.put("authGrpTypCd", authGrpTypCd);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		return LayoutUtil.getJspPath("/pt/adm/auth/setAuthGrp");
	}
	
	/** 권한그룹목록조회 - 왼편 (사용자그룹목록조회 포함), 사용자그룹 - 오른편 팝업 버튼 에서도 호출함:setAuthGrpDetlPop() */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pt/adm/auth/listAuthGrpFrm")
	public String listAuthGrpFrm(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="authGrpTypCd", required=false) String authGrpTypCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 권한그룹카테고리코드 - 조회
		List<PtCdBVo> authGrpCatCdList = ptCmSvc.getCdList("AUTH_GRP_CAT_CD", langTypCd, "Y");
		model.put("authGrpCatCdList", authGrpCatCdList);
		
		//권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
		model.put("authGrpTypCd", authGrpTypCd);
		
		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 목록조회
		PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo();
		ptAuthGrpBVo.setCompId(compId);//회사ID
		ptAuthGrpBVo.setAuthGrpTypCd(authGrpTypCd);
		ptAuthGrpBVo.setQueryLang(langTypCd);
		ptAuthGrpBVo.setOrderBy("AUTH_GRP_CAT_CD, SORT_ORDR");//AUTH_GRP_CAT_CD 권한그룹카테고리코드, AUTH_GRP_ID, 권한그룹ID
		
//		@SuppressWarnings("unchecked")
		List<PtAuthGrpBVo> ptAuthGrpBVoList = (List<PtAuthGrpBVo>)commonSvc.queryList(ptAuthGrpBVo);
		
		// 권한그룹카테고리코드 로 나누어 List에 담고 그 List 를 모델에 cat_ 붙여서 담음
		List<PtAuthGrpBVo> list = null;
		PtAuthGrpBVo storedPtAuthGrpBVo;
		String authGrpCatCd = null;
		int i, size = ptAuthGrpBVoList==null ? 0 : ptAuthGrpBVoList.size();
		for(i=0;i<size;i++){
			storedPtAuthGrpBVo = ptAuthGrpBVoList.get(i);
			if(storedPtAuthGrpBVo.getAuthGrpCatCd()==null) continue;
			if(!storedPtAuthGrpBVo.getAuthGrpCatCd().equals(authGrpCatCd)){
				authGrpCatCd = storedPtAuthGrpBVo.getAuthGrpCatCd();
				list = new ArrayList<PtAuthGrpBVo>();
				model.put("cat_"+authGrpCatCd, list);
			}
			list.add(storedPtAuthGrpBVo);
		}
		
		if(Boolean.TRUE.equals(model.get("forOneList"))){
			List<PtAuthGrpBVo> orderedPtAuthGrpBVoList = new ArrayList<PtAuthGrpBVo>();
			size = authGrpCatCdList==null ? 0 : authGrpCatCdList.size();
			for(i=0;i<size;i++){
				list = (List<PtAuthGrpBVo>)model.get("cat_"+authGrpCatCdList.get(i).getCd());
				if(list!=null) orderedPtAuthGrpBVoList.addAll(list);
			}
			model.put("ptAuthGrpBVoList", orderedPtAuthGrpBVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/auth/listAuthGrpFrm");
	}
	
	/** [팝업] 권한그룹 조회 - 왼편, 등록/수정용 */
	@RequestMapping(value = "/pt/adm/auth/setAuthGrpPop")
	public String setAuthGrpPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="authGrpId", required=false) String authGrpId,
//			@Parameter(name="authGrpCatCd", required=false) String authGrpCatCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 코드조회 : AUTH_GRP_CAT_CD (권한그룹카테고리코드)
		List<PtCdBVo> authGrpCatCdList = ptCmSvc.getCdList("AUTH_GRP_CAT_CD", langTypCd, "Y");
		model.put("authGrpCatCdList", authGrpCatCdList);
		
		if(authGrpId!=null && !authGrpId.isEmpty()){
			// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 조회
			PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo();
			VoUtil.bind(request, ptAuthGrpBVo);
			ptAuthGrpBVo.setCompId(compId);
			ptAuthGrpBVo.setQueryLang(langTypCd);
			ptAuthGrpBVo = (PtAuthGrpBVo)commonSvc.queryVo(ptAuthGrpBVo);
			
			// 리소스상세(PT_RESC_D) 테이블 조회
			ptRescSvc.queryRescDVo(ptAuthGrpBVo.getCompId(), ptAuthGrpBVo.getRescId(), model);
			
			model.put("ptAuthGrpBVo", ptAuthGrpBVo);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/auth/setAuthGrpPop");
	}
	
	/** [팝업] 권한그룹상세설정 - 오른편 팝업 - 직위,직급,직책,역할,보안등급 선택 팝업 */
	@RequestMapping(value = "/pt/adm/auth/setAuthGrpDetlPop")
	public String setAuthGrpDetlPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="grpKndCd", required=false) String grpKndCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 사용자 그룹 목록조회
		if("userGrp".equals(grpKndCd)){
			model.put("forOneList", Boolean.TRUE);
			listAuthGrpFrm(request, compId, "G", model);
			
		// 겸직 상태
		} else if("aduStat".equals(grpKndCd)){
			List<PtCdBVo> cdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
			
			if(cdList != null){
				List<PtCdBVo> grpKndCdList = new ArrayList<PtCdBVo>();
				for(PtCdBVo cdVo : cdList){
					if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, cdVo.getCd())){
						grpKndCdList.add(cdVo);
					}
				}
				model.put("grpKndCdList", grpKndCdList);
			}
			
		} else {
			
			String parsedGrpKndCd = grpKndCd==null ? null : grpKndCd.toUpperCase() + "_CD"; 
			if(parsedGrpKndCd!=null){
				// 직위,직급,직책,역할,보안등급 의 코드 목록
				List<PtCdBVo> grpKndCdList = ptCmSvc.getFilteredCdList(parsedGrpKndCd, langTypCd, compId, null, null, "Y");
				model.put("grpKndCdList", grpKndCdList);
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/auth/setAuthGrpDetlPop");
	}
	
	/** 권한그룹 저장 - 왼편 */
	@RequestMapping(value = "/pt/adm/auth/transAuthGrp")
	public String transAuthGrp(HttpServletRequest request,
			ModelMap model) throws Exception {
		try{
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스상세(PT_RESC_D) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescDVo ptRescDVo = ptRescSvc.collectPtRescDVo(request, null, null, queryQueue);
			if(ptRescDVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 권한그룹기본(PT_AUTH_GRP_B) 테이블
			PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo();
			VoUtil.bind(request, ptAuthGrpBVo);
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
			ptAuthGrpBVo.setRescId(ptRescDVo.getRescId());
			ptAuthGrpBVo.setAuthGrpNm(ptRescDVo.getRescVa());
			
			// 수정일,수정자
			ptAuthGrpBVo.setModDt("sysdate");
			ptAuthGrpBVo.setModrUid(userVo.getUserUid());
			
			PtAuthGrpBVo storedPtAuthGrpBVo = null;
			if(ptAuthGrpBVo.getAuthGrpId()!=null && !ptAuthGrpBVo.getAuthGrpId().isEmpty()){
				// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 조회
				storedPtAuthGrpBVo = new PtAuthGrpBVo();
				storedPtAuthGrpBVo.setCompId(ptAuthGrpBVo.getCompId());
				storedPtAuthGrpBVo.setAuthGrpTypCd(ptAuthGrpBVo.getAuthGrpTypCd());
				storedPtAuthGrpBVo.setAuthGrpId(ptAuthGrpBVo.getAuthGrpId());
				storedPtAuthGrpBVo = (PtAuthGrpBVo)commonSvc.queryVo(storedPtAuthGrpBVo);
			}
			
			// 신규 등록의 경우
			if(storedPtAuthGrpBVo==null){
				ptAuthGrpBVo.setAuthGrpId(ptCmSvc.createId("PT_AUTH_GRP_B"));
				
				// 사용여부 없으면
				if(ptAuthGrpBVo.getUseYn()==null || ptAuthGrpBVo.getUseYn().isEmpty()){
					ptAuthGrpBVo.setUseYn("Y");
				}
				// 시스템권한그룹여부 없으면
				if(ptAuthGrpBVo.getSysAuthGrpYn()==null || ptAuthGrpBVo.getSysAuthGrpYn().isEmpty()){
					ptAuthGrpBVo.setSysAuthGrpYn("N");
				}
				// 등록일시, 등록자
				ptAuthGrpBVo.setRegDt("sysdate");
				ptAuthGrpBVo.setRegrUid(userVo.getUserUid());
				
				queryQueue.insert(ptAuthGrpBVo);
			// 수정
			} else {
				
				// 카테고리가 바뀌면 - 정렬순서 조절 필요
				if(storedPtAuthGrpBVo.getAuthGrpCatCd()!=null
						&& !storedPtAuthGrpBVo.getAuthGrpCatCd().equals(ptAuthGrpBVo.getAuthGrpCatCd())){
					
					// 최대 정렬순서 쿼리
					PtxSortOrdrChnVo ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
					ptxSortOrdrChnVo.setTabId("PT_AUTH_GRP_B");
					ptxSortOrdrChnVo.setCompId(ptAuthGrpBVo.getCompId());
					ptxSortOrdrChnVo.setPkCol("AUTH_GRP_TYP_CD");
					ptxSortOrdrChnVo.setPk(ptAuthGrpBVo.getAuthGrpTypCd());
					ptxSortOrdrChnVo.setPkCol2("AUTH_GRP_CAT_CD");
					ptxSortOrdrChnVo.setPk2(ptAuthGrpBVo.getAuthGrpCatCd());
					Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
					
					// 바뀐 카테고리의 최대값 + 1 세팅
					maxSortOrdr = maxSortOrdr==null ? 1 : maxSortOrdr+1;
					ptAuthGrpBVo.setSortOrdr(maxSortOrdr.toString());
					
					queryQueue.update(ptAuthGrpBVo);
					
					// 옮기기 전의 정렬순서를 하나씩 줄임
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					ptxSortOrdrChnVo.setTabId("PT_AUTH_GRP_B");
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setCompId(storedPtAuthGrpBVo.getCompId());
					ptxSortOrdrChnVo.setPkCol("AUTH_GRP_TYP_CD");
					ptxSortOrdrChnVo.setPk(storedPtAuthGrpBVo.getAuthGrpTypCd());
					ptxSortOrdrChnVo.setPkCol2("AUTH_GRP_CAT_CD");
					ptxSortOrdrChnVo.setPk2(storedPtAuthGrpBVo.getAuthGrpCatCd());
					ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedPtAuthGrpBVo.getSortOrdr()));
					queryQueue.update(ptxSortOrdrChnVo);
					
				} else {
					queryQueue.update(ptAuthGrpBVo);
				}
				
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.AUTH_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadAuthGrp('"+ptAuthGrpBVo.getAuthGrpTypCd()+"','"+ptAuthGrpBVo.getAuthGrpCatCd()+"','"+ptAuthGrpBVo.getAuthGrpId()+"');");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 권한그룹 삭제 - 왼편 */
	@RequestMapping(value = "/pt/adm/auth/transAuthGrpDelAjx")
	public String transAuthGrpDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		String compId=null, authGrpTypCd=null, authGrpId=null;
		try{
	//		// 세션의 언어코드
	//		String langTypCd = LoginSession.getLangTypCd(request);
			QueryQueue queryQueue = new QueryQueue();
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			// 회사ID - KEY
			compId = (String)jsonObject.get("compId");
			// 권한그룹구분코드 - KEY - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
			authGrpTypCd = (String)jsonObject.get("authGrpTypCd");
			// 권한그룹ID - KEY
			authGrpId = (String)jsonObject.get("authGrpId");
			
			// 리소스상세(PT_RESC_D) 테이블
			PtRescDVo ptRescDVo;
			
			// 권한그룹기본(PT_AUTH_GRP_B) 테이블
			PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo();
			ptAuthGrpBVo.setCompId(compId);
			ptAuthGrpBVo.setAuthGrpId(authGrpId);
			
			// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 조회 - 시스템 권한그룹인지 체크
			PtAuthGrpBVo storedPtAuthGrpBVo = (PtAuthGrpBVo)commonSvc.queryVo(ptAuthGrpBVo);
			if(storedPtAuthGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			} else if("Y".equals(storedPtAuthGrpBVo.getSysAuthGrpYn())){
				// pt.msg.not.use.authGrp.sys=시스템 권한 그룹은 삭제 할 수 없습니다.
				throw new CmException("pt.msg.not.use.authGrp.sys", request);
			}
			
//			// 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 - 조회 - 사용중 체크
//			PtAuthGrpAuthDVo ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
//			ptAuthGrpAuthDVo.setCompId(compId);
//			ptAuthGrpAuthDVo.setAuthGrpTypCd(authGrpTypCd);
//			ptAuthGrpAuthDVo.setAuthGrpId(authGrpId);
//			
//			// 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 - 에 사용여부 검사
//			int count = commonSvc.count(ptAuthGrpAuthDVo);
//			if(count > 0){
//				// pt.msg.not.del.authGrp.inUse=사용중인 권한그룹은 삭제 할 수 없습니다. '사용안함'으로 변경하시기 바랍니다.
//				throw new CmException("pt.msg.not.del.authGrp.inUse", request);
//			}
			
			// 리소스상세(PT_RESC_D) 테이블 - 삭제
			if(storedPtAuthGrpBVo.getRescId()!=null && !storedPtAuthGrpBVo.getRescId().isEmpty()){
				ptRescDVo = new PtRescDVo();
				ptRescDVo.setCompId(storedPtAuthGrpBVo.getCompId());
				ptRescDVo.setRescId(storedPtAuthGrpBVo.getRescId());
				queryQueue.delete(ptRescDVo);
			}
			
			// 권한그룹메뉴포틀릿관계(PT_AUTH_GRP_MNU_PLT_R) 테이블 - 삭제
			PtAuthGrpMnuPltRVo ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
			ptAuthGrpMnuPltRVo.setCompId(compId);
			ptAuthGrpMnuPltRVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpMnuPltRVo);
			
			// 권한그룹사용자관계(PT_AUTH_GRP_USER_R) 테이블 - 삭제
			PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
			ptAuthGrpUserRVo.setCompId(compId);
			ptAuthGrpUserRVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpUserRVo);
			
			// 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 - 삭제
			PtAuthGrpAuthDVo ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
			ptAuthGrpAuthDVo.setCompId(compId);
			ptAuthGrpAuthDVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpAuthDVo);
			
			// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 삭제
			ptAuthGrpBVo = new PtAuthGrpBVo();
			ptAuthGrpBVo.setCompId(compId);
			ptAuthGrpBVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpBVo);
			
			// 삭제된 것보다 큰 sortOrder 하나씩 줄임
			PtxSortOrdrChnVo ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			ptxSortOrdrChnVo.setTabId("PT_AUTH_GRP_B");
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setCompId(compId);
			ptxSortOrdrChnVo.setPkCol("AUTH_GRP_TYP_CD");
			ptxSortOrdrChnVo.setPk(authGrpTypCd);
			ptxSortOrdrChnVo.setPkCol2("AUTH_GRP_CAT_CD");
			ptxSortOrdrChnVo.setPk2(storedPtAuthGrpBVo.getAuthGrpCatCd());
			ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedPtAuthGrpBVo.getSortOrdr()));
			queryQueue.update(ptxSortOrdrChnVo);
			
			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.AUTH_GRP);
			}
			
			//cm.msg.del.success=삭제 되었습니다.
			String msg = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", msg);
			model.put("result", "ok");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage()+"  compId:"+compId+" authGrpTypCd:"+authGrpTypCd+" authGrpId:"+authGrpId);
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 권한그룹상세설정 - 오른편 */
	@RequestMapping(value = "/pt/adm/auth/setAuthGrpDetlFrm")
	public String setAuthGrpDetl(HttpServletRequest request,
			@Parameter(name="authGrpId", required=false)String authGrpId,
			ModelMap model) throws Exception {
		
		if(authGrpId!=null){
			
			// 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 - 조회
			PtAuthGrpAuthDVo ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
			VoUtil.bind(request, ptAuthGrpAuthDVo);
			ptAuthGrpAuthDVo.setOrderBy("SEQ, GRP_KND_CD, SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<PtAuthGrpAuthDVo> ptAuthGrpAuthDVoList = (List<PtAuthGrpAuthDVo>)commonSvc.queryList(ptAuthGrpAuthDVo);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			Map<String, String> orgTermMap = ptSysSvc.getTermMap("or.term", langTypCd);
			
			// UI 표시용으로 seq 기준으로 Map에 넣음
			String seq=null, grpKndCd=null, term;
			int i, size = ptAuthGrpAuthDVoList==null ? 0 : ptAuthGrpAuthDVoList.size();
			Integer seqCount = 0;
			Map<String, StringBuilder> grpKndCdMap = null;
			StringBuilder builder;
			//or.check.withSub.short=하위포함
			String withSubTxt = messageProperties.getMessage("or.check.withSub.short", request);
			boolean withSub;
			for(i=0;i<size;i++){
				
				ptAuthGrpAuthDVo = ptAuthGrpAuthDVoList.get(i);
				if(!ptAuthGrpAuthDVo.getSeq().equals(seq)){
					seq = ptAuthGrpAuthDVo.getSeq();
					grpKndCdMap = new HashMap<String, StringBuilder>();
					model.put("grpKndCdMap"+seq, grpKndCdMap);
					seqCount++;
				}
				grpKndCd = ptAuthGrpAuthDVo.getGrpKndCd();
				
				// grpId
				builder = grpKndCdMap.get(grpKndCd+"GrpIds");
				if(builder==null){
					builder = new StringBuilder();
					grpKndCdMap.put(grpKndCd+"GrpIds", builder);
					if(grpKndCd.equals("dept")){
						term = messageProperties.getMessage("pt.btn.dept", request);
					} else if(grpKndCd.equals("user")){
						term = messageProperties.getMessage("pt.btn.user", request);
					} else if(grpKndCd.equals("userGrp")){
						term = messageProperties.getMessage("pt.jsp.setAuthGrp.userGrp", request);
					} else if(grpKndCd.equals("aduStat")){
						term = messageProperties.getMessage("pt.sysopt.aduStat", request);
					} else {
						term = orgTermMap==null ? null : orgTermMap.get("or.term."+grpKndCd);
						if(term==null) term = messageProperties.getMessage("or.term."+grpKndCd, request);
					}
					grpKndCdMap.put(grpKndCd+"RescTerm", new StringBuilder(term));
				} else {
					builder.append(',');
				}
				
				// 하위포함여부
				withSub = "Y".equals(ptAuthGrpAuthDVo.getSubInclYn());
				if(withSub){//하위 포함 일 경우 Grpid 뒤에 :Y 를 붙임
					builder.append(ptAuthGrpAuthDVo.getGrpId()+":Y");
				} else {
					builder.append(ptAuthGrpAuthDVo.getGrpId());
				}
				
				// rescId
				builder = grpKndCdMap.get(grpKndCd+"RescIds");
				if(builder==null){
					builder = new StringBuilder();
					grpKndCdMap.put(grpKndCd+"RescIds", builder);
				} else {
					builder.append(',');
				}
				builder.append(ptAuthGrpAuthDVo.getRescId());
				
				// rescNm
				builder = grpKndCdMap.get(grpKndCd+"RescNms");
				if(builder==null){
					builder = new StringBuilder();
					grpKndCdMap.put(grpKndCd+"RescNms", builder);
				} else {
					builder.append(',').append(' ');
				}
				builder.append(ptAuthGrpAuthDVo.getRescNm());
				if(withSub){
					builder.append('(').append(withSubTxt).append(')');
				}
			}
			// 사용자의 경우 - Iframe 용 파라미터
			PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
			VoUtil.bind(request, ptAuthGrpUserRVo);
			ptAuthGrpUserRVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<PtAuthGrpUserRVo> ptAuthGrpUserRVoList = (List<PtAuthGrpUserRVo>)commonSvc.queryList(ptAuthGrpUserRVo);
			if(ptAuthGrpUserRVoList!=null){
				builder = new StringBuilder(64);
				boolean first = true;
				for(PtAuthGrpUserRVo storedPtAuthGrpUserRVo : ptAuthGrpUserRVoList){
					if(first) first = false;
					else builder.append(',');
					builder.append(storedPtAuthGrpUserRVo.getUserUid());
				}
				model.put("userGrpIds", builder);
			}
			
//			if(grpKndCdMap!=null){
//				builder = grpKndCdMap.get("userGrpIds");
//				if(builder!=null){
//					seqCount--;
//					model.put("userGrpIds", builder);
//				}
//			}
			
			model.put("seqCount", seqCount);
			model.put("grpKndCds", CombAuthGrpDetl.GRP_KND_CDS);
			
		} else {
			model.put("seqCount", "0");
		}
		
		return LayoutUtil.getJspPath("/pt/adm/auth/setAuthGrpDetlFrm");
	}
	
	/** [AJAX] 권한그룹 상세 저장 - 오른편 */
	@RequestMapping(value = "/pt/adm/auth/transAuthGrpDetlAjx")
	public String transAuthGrpDetlAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		String compId=null, authGrpTypCd=null, authGrpId=null, prefix=null;
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			// 회사ID - KEY
			compId = (String)jsonObject.get("compId");
			// 권한그룹구분코드 - KEY - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
			authGrpTypCd = (String)jsonObject.get("authGrpTypCd");
			// 권한그룹ID - KEY
			authGrpId = (String)jsonObject.get("authGrpId");
			
			if(compId==null || compId.isEmpty() || authGrpTypCd==null || authGrpTypCd.isEmpty() || authGrpId==null || authGrpId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			
			PtAuthGrpAuthDVo ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
			ptAuthGrpAuthDVo.setCompId(compId);
			ptAuthGrpAuthDVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpAuthDVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpAuthDVo);
			
			PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
			ptAuthGrpUserRVo.setCompId(compId);
			ptAuthGrpUserRVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpUserRVo.setAuthGrpId(authGrpId);
			queryQueue.delete(ptAuthGrpUserRVo);
			
			String[] grpIds, rescIds;
			int prefixLen = 0, p, q, i, size;
			Integer sortOrdr, userSortOrdr;
			String paramNm, excliYn, seq, grpKndCd, grpIdStr, rescIdStr;
			
			prefix = compId+"-"+authGrpTypCd+"-"+authGrpId+"-";
			prefixLen = prefix.length();
			
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keySet().iterator();
			while(iterator.hasNext()){
				
				paramNm = iterator.next();
				if(!paramNm.startsWith(prefix) || !paramNm.endsWith("grpIds")) continue;
				
				excliYn = paramNm.substring(prefixLen, prefixLen+1);
				
				p = prefixLen+2;
				q = paramNm.indexOf('-', p);
				if(q<0) continue;
				
				seq = paramNm.substring(p, q);
				p = q+1;
				q = paramNm.indexOf('-', p);
				if(q<0) continue;
				
				grpKndCd = paramNm.substring(p, q);
				
				grpIdStr = (String)jsonObject.get(paramNm);
				if(grpIdStr==null || grpIdStr.isEmpty()) continue;
				grpIds = grpIdStr.split(",");
				
				rescIdStr = (String)jsonObject.get(paramNm.substring(0, q+1)+"rescIds");
				if(rescIdStr==null || rescIdStr.isEmpty()) continue;
				rescIds = rescIdStr.split(",");
				
				userSortOrdr = 1;
				sortOrdr = 1;
				if("user".equals(grpKndCd)){
					size = grpIds.length;
				} else {
					size = Math.min(grpIds.length, rescIds.length);
				}
				
				for(i=0;i<size;i++){
					if("user".equals(grpKndCd)){//사용자 >> PT_AUTH_GRP_USER_R
						ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
						ptAuthGrpUserRVo.setCompId(compId);
						ptAuthGrpUserRVo.setAuthGrpTypCd(authGrpTypCd);
						ptAuthGrpUserRVo.setAuthGrpId(authGrpId);
						ptAuthGrpUserRVo.setExcliYn(excliYn);
						ptAuthGrpUserRVo.setUserUid(grpIds[i]);
						ptAuthGrpUserRVo.setSortOrdr(userSortOrdr.toString());
						userSortOrdr++;
						queryQueue.insert(ptAuthGrpUserRVo);
					} else {// 부서, 직위,직책,직무,직급,권한등급, 역할 >> PT_AUTH_GRP_AUTH_D
						ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
						ptAuthGrpAuthDVo.setCompId(compId);
						ptAuthGrpAuthDVo.setAuthGrpTypCd(authGrpTypCd);
						ptAuthGrpAuthDVo.setAuthGrpId(authGrpId);
						ptAuthGrpAuthDVo.setExcliYn(excliYn);
						ptAuthGrpAuthDVo.setSeq(seq);
						ptAuthGrpAuthDVo.setGrpKndCd(grpKndCd);
						if(grpIds[i].endsWith(":Y")){
							ptAuthGrpAuthDVo.setSubInclYn("Y");
							ptAuthGrpAuthDVo.setGrpId(grpIds[i].substring(0, grpIds[i].length()-2));
						} else {
							ptAuthGrpAuthDVo.setSubInclYn("N");
							if((p = grpIds[i].indexOf(':'))>0){
								ptAuthGrpAuthDVo.setGrpId(grpIds[i].substring(0, p));
							} else {
								ptAuthGrpAuthDVo.setGrpId(grpIds[i]);
							}
						}
						ptAuthGrpAuthDVo.setRescId(rescIds[i]);
						ptAuthGrpAuthDVo.setSortOrdr(sortOrdr.toString());
						sortOrdr++;
						queryQueue.insert(ptAuthGrpAuthDVo);
					}
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.AUTH_GRP);
			
			//cm.msg.save.success=저장 되었습니다.
			String msg = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", msg);
			model.put("result", "ok");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage()+"  compId:"+compId+" authGrpTypCd:"+authGrpTypCd+" authGrpId:"+authGrpId);
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 권한그룹 순서변경 */
	@RequestMapping(value = "/pt/adm/auth/transAuthGrpMoveAjx")
	public String transAuthGrpMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		//{compId:'${param.compId}', authGrpTypCd:typCd, authGrpId:grpId, direction:direction}
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		String authGrpTypCd = (String)jsonObject.get("authGrpTypCd");
		String authGrpId = (String)jsonObject.get("authGrpId");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(compId==null || direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(compId==null) LOGGER.error("Menu move(up/down) - compId==null : "+msg);
			if(authGrpTypCd==null) LOGGER.error("Menu move(up/down) - authGrpTypCd==null : "+msg);
			if(authGrpId==null) LOGGER.error("Menu move(up/down) - authGrpId==null : "+msg);
			if(direction==null) LOGGER.error("Menu move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
		
		PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo(), storedPtAuthGrpBVo;
		
		// 위로 이동
		if("up".equals(direction)){
			
			// curOrdr - 현재순번
			Integer curOrdr, switchOrdr;//, stdOrdr=1;
			ptAuthGrpBVo = new PtAuthGrpBVo();
			
			ptAuthGrpBVo.setCompId(compId);
			ptAuthGrpBVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpBVo.setAuthGrpId(authGrpId);
			storedPtAuthGrpBVo = (PtAuthGrpBVo)commonSvc.queryVo(ptAuthGrpBVo);
			if(storedPtAuthGrpBVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error(msg+" : CANNOT MOVE-UP - compId:"+compId+" authGrpTypCd:"+authGrpTypCd+" authGrpId:"+authGrpId);
				return JsonUtil.returnJson(model);
			}
			curOrdr = Integer.valueOf(storedPtAuthGrpBVo.getSortOrdr());
			String authGrpCatCd = storedPtAuthGrpBVo.getAuthGrpCatCd();
			
			switchOrdr = curOrdr-1;
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_AUTH_GRP_B");
			ptxSortOrdrChnVo.setCompId(compId);
			ptxSortOrdrChnVo.setPkCol("AUTH_GRP_TYP_CD");
			ptxSortOrdrChnVo.setPk(authGrpTypCd);
			ptxSortOrdrChnVo.setPkCol2("AUTH_GRP_CAT_CD");
			ptxSortOrdrChnVo.setPk2(authGrpCatCd);
			ptxSortOrdrChnVo.setMoreThen(switchOrdr);
			ptxSortOrdrChnVo.setLessThen(switchOrdr);
			ptxSortOrdrChnVo.setChnVa(1);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			queryQueue.update(ptxSortOrdrChnVo);
			
			storedPtAuthGrpBVo = new PtAuthGrpBVo();
			storedPtAuthGrpBVo.setCompId(compId);
			storedPtAuthGrpBVo.setAuthGrpTypCd(authGrpTypCd);
			storedPtAuthGrpBVo.setAuthGrpId(authGrpId);
			storedPtAuthGrpBVo.setSortOrdr(switchOrdr.toString());
			queryQueue.update(storedPtAuthGrpBVo);
			
			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.AUTH_GRP);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			return JsonUtil.returnJson(model);
			
			// 아래로 이동
		} else if("down".equals(direction)){
			
			ptAuthGrpBVo = new PtAuthGrpBVo();
			ptAuthGrpBVo.setCompId(compId);
			ptAuthGrpBVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpBVo.setAuthGrpId(authGrpId);
			storedPtAuthGrpBVo = (PtAuthGrpBVo)commonSvc.queryVo(ptAuthGrpBVo);
			if(storedPtAuthGrpBVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error(msg+" : CANNOT MOVE-DOWN - compId:"+compId+" authGrpTypCd:"+authGrpTypCd+" authGrpId:"+authGrpId);
				return JsonUtil.returnJson(model);
			}
			
			String authGrpCatCd = storedPtAuthGrpBVo.getAuthGrpCatCd();
			
			// curOrdr - 현재순번
			Integer curOrdr, switchOrdr;//, stdOrdr=maxSortOrdr;
			curOrdr = Integer.valueOf(storedPtAuthGrpBVo.getSortOrdr());
			
			switchOrdr = curOrdr+1;
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_AUTH_GRP_B");
			ptxSortOrdrChnVo.setCompId(compId);
			ptxSortOrdrChnVo.setPkCol("AUTH_GRP_TYP_CD");
			ptxSortOrdrChnVo.setPk(authGrpTypCd);
			ptxSortOrdrChnVo.setPkCol2("AUTH_GRP_CAT_CD");
			ptxSortOrdrChnVo.setPk2(authGrpCatCd);
			ptxSortOrdrChnVo.setMoreThen(switchOrdr);
			ptxSortOrdrChnVo.setLessThen(switchOrdr);
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			queryQueue.update(ptxSortOrdrChnVo);
			
			storedPtAuthGrpBVo = new PtAuthGrpBVo();
			storedPtAuthGrpBVo.setCompId(compId);
			storedPtAuthGrpBVo.setAuthGrpTypCd(authGrpTypCd);
			storedPtAuthGrpBVo.setAuthGrpId(authGrpId);
			storedPtAuthGrpBVo.setSortOrdr(switchOrdr.toString());
			queryQueue.update(storedPtAuthGrpBVo);
			
			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.AUTH_GRP);
				
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
	
//	/** 권한 그룹 별 권한 관리 - 조회 [팝업버전] */
//	@RequestMapping(value = "/pt/adm/auth/setAuthByAuthGrpPop")
//	public String setAuthByAuthGrpPop(HttpServletRequest request,
//			@Parameter(name="compId", required=false) String compId,
//			@Parameter(name="authGrpTypCd", required=false) String authGrpTypCd,
//			@Parameter(name="authGrpId", required=false) String authGrpId,
//			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
//			ModelMap model) throws Exception {
//		setAuthByAuthGrp(request, compId, authGrpTypCd, authGrpId, mnuGrpId, model);
//		return LayoutUtil.getJspPath("/pt/adm/auth/setAuthByAuthGrpPop");
//	}
	
	/** 권한 그룹 별 권한 관리 - 조회 */
	@RequestMapping(value = "/pt/adm/auth/setAdminAuth")
	public String setAdminAuthGrpAuth(HttpServletRequest request,
			@Parameter(name="authScopCd", required=false) String authScopCd,
			@Parameter(name="authGrpId", required=false) String authGrpId,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		return setAuth(request, authScopCd, "A", authGrpId, mnuGrpId, model);
	}
	/** 권한 그룹 별 권한 관리 - 조회 */
	@RequestMapping(value = "/pt/adm/auth/setMobileAuth")
	public String setMobileAuthGrpAuth(HttpServletRequest request,
			@Parameter(name="authScopCd", required=false) String authScopCd,
			@Parameter(name="authGrpId", required=false) String authGrpId,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		return setAuth(request, authScopCd, "M", authGrpId, mnuGrpId, model);
	}
	/** 권한 그룹 별 권한 관리 - 조회 */
	@RequestMapping(value = "/pt/adm/auth/setUserAuth")
	public String setUserAuth(HttpServletRequest request,
			@Parameter(name="authScopCd", required=false) String authScopCd,
			@Parameter(name="authGrpId", required=false) String authGrpId,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		return setAuth(request, authScopCd, "U", authGrpId, mnuGrpId, model);
	}
	/** 권한 그룹 별 권한 관리 - 조회 */
	private String setAuth(HttpServletRequest request,
			@Parameter(name="authScopCd", required=false) String authScopCd,
			@Parameter(name="authGrpTypCd", required=false) String authGrpTypCd,
			@Parameter(name="authGrpId", required=false) String authGrpId,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		
		if(!PtConstant.AUTH_IP_EX.equals(authScopCd)) authScopCd = PtConstant.AUTH_IP_IN;
		
		// IP 보안 정책 조회
		Map<String, String> secuPolc = ptSysSvc.getSecuPolc();
		model.put("secuPolc", secuPolc);
		
		// 세션 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
		model.put("compId", compId);
		
		String authMdCd = "A".equals(authGrpTypCd) ? "Admin" : "M".equals(authGrpTypCd) ? "Mobile" : "User";
		model.put("authMdCd", authMdCd);
		model.put("authGrpTypCd", authGrpTypCd);
		
		// 권한그룹 목록 조회 - 좌측 상단 콤보
		model.put("forOneList", Boolean.TRUE);//(하나의 리스트로 만들라는 것)
		listAuthGrpFrm(request, compId, authGrpTypCd, model);// << ptAuthGrpBVoList
		
		// 권한그룹ID 세팅
		if(authGrpId==null || authGrpId.isEmpty()){
			@SuppressWarnings("unchecked")
			List<PtAuthGrpBVo> ptAuthGrpBVoList = (List<PtAuthGrpBVo>)model.get("ptAuthGrpBVoList");
			if(ptAuthGrpBVoList!=null && ptAuthGrpBVoList.size()>0){
				authGrpId = ptAuthGrpBVoList.get(0).getAuthGrpId();
			}
		}
		model.put("authGrpId", authGrpId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 관리자메뉴여부
		boolean isAdmMnu = "A".equals(authGrpTypCd);//권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
		boolean isMobile = "M".equals(authGrpTypCd);
		String mnuGrpMdCd = isAdmMnu ? "A" : isMobile ? "M" : "U";
		// 우측 상단 콤보 - 1. 메뉴그룹 목록 조회 
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setMnuGrpMdCd(mnuGrpMdCd);
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		
		// 전체회사 + 사용자의 회사 - 로 메뉴그룹 목록 제한
		List<String> openCompIdList = new ArrayList<String>();
		openCompIdList.add(PtConstant.SYS_COMP_ID);
		openCompIdList.add(userVo.getCompId());
		ptMnuGrpBVo.setOpenCompIdList(openCompIdList);
		
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
		model.put("ptMnuGrpBVoList", ptMnuGrpBVoList);
		
		// 우측 상단 콤보 - 2. 레이아웃 목록
		PtMnuLoutDVo ptMnuLoutDVo = new PtMnuLoutDVo();
		ptMnuLoutDVo.setCompId(compId);
		ptMnuLoutDVo.setMnuLoutKndCd("C");//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
		if(isAdmMnu){
			ptMnuLoutDVo.setLoutCatId("A");//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
		} else if(isMobile){
			ptMnuLoutDVo.setLoutCatId("M");//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
		} else {
			ptMnuLoutDVo.setWhereSqllet("AND LOUT_CAT_ID IN ('B', 'I')");
		}
		ptMnuLoutDVo.setSortOrdr("LOUT_CAT_ID, LOUT_LOC_CD, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuLoutDVo> ptMnuLoutDVoList = (List<PtMnuLoutDVo>)commonSvc.queryList(ptMnuLoutDVo);
		if(isAdmMnu){
			if(ptMnuLoutDVoList!=null && !ptMnuLoutDVoList.isEmpty()){
				// 관리자용 메뉴조합 : opt-group 명 : 메뉴조합
				model.put("ptMnuLoutDVoListA", ptMnuLoutDVoList);
			}
		} else if(isAdmMnu){
			if(ptMnuLoutDVoList!=null && !ptMnuLoutDVoList.isEmpty()){
				// 관리자용 메뉴조합 : opt-group 명 : 메뉴조합
				model.put("ptMnuLoutDVoListM", ptMnuLoutDVoList);
			}
		} else {
			List<PtMnuLoutDVo> ptMnuLoutDVoListB = new ArrayList<PtMnuLoutDVo>();
			List<PtMnuLoutDVo> ptMnuLoutDVoListI = new ArrayList<PtMnuLoutDVo>();
			int i, size = ptMnuLoutDVoList==null ? 0 : ptMnuLoutDVoList.size();
			for(i=0;i<size;i++){
				ptMnuLoutDVo = ptMnuLoutDVoList.get(i);
				if("B".equals(ptMnuLoutDVo.getLoutCatId())){
					// 사용자용 메뉴조합(기본 레이아웃) : opt-group 명 : 메뉴조합(기본 레이아웃)
					ptMnuLoutDVoListB.add(ptMnuLoutDVo);
				} else if("I".equals(ptMnuLoutDVo.getLoutCatId())){
					// 사용자용 메뉴조합(아이콘 레이아웃) : opt-group 명 : 메뉴조합(아이콘 레이아웃)
					ptMnuLoutDVoListI.add(ptMnuLoutDVo);
				}
			}
			if(ptMnuLoutDVoListB!=null && !ptMnuLoutDVoListB.isEmpty()){
				model.put("ptMnuLoutDVoListB", ptMnuLoutDVoListB);
			}
			if(ptMnuLoutDVoListI!=null && !ptMnuLoutDVoListI.isEmpty()){
				model.put("ptMnuLoutDVoListI", ptMnuLoutDVoListI);
			}
		}
		
		// 메뉴포틀릿구분코드 - KEY - M:메뉴, P:포틀릿, MG:메뉴그룹, C:레이아웃조합
		String mnuPltTypCd = null, mnuGrpNm = null;
		String mnuLoutId=null, mnuLoutNm=null;
		
		// 메뉴 트릭 구성(하단)
		// 메뉴그룹이 없거나 파라미터-메뉴그룹이 포틀릿 인 경우 - 포틀릿으로 설정함
		if("PORTLET".equals(mnuGrpId)){
			mnuPltTypCd = "P";
		} else if(mnuGrpId==null || mnuGrpId.isEmpty()){
			mnuPltTypCd = "M";
		} else if(mnuGrpId.charAt(0)=='L') {
			mnuPltTypCd = "L";
		} else {
			mnuPltTypCd = "M";
		}
		model.put("mnuPltTypCd", mnuPltTypCd);
		
		boolean hasPortalMnu = true;
		
		// 포틀릿 목록 조회
		if("P".equals(mnuPltTypCd)) {
			model.put("mnuGrpId", "PORTLET");
			
			PtPltDVo ptPltDVo = new PtPltDVo();
			ptPltDVo.setUseYn("Y");
			ptPltDVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<PtPltDVo> ptPltDVoList = (List<PtPltDVo>)commonSvc.queryList(ptPltDVo);
			model.put("ptPltDVoList", ptPltDVoList);
		
		// 메뉴 트리 조회
		} else if("M".equals(mnuPltTypCd)) {
			
			// 메뉴그룹 ID/명 세팅
			if(ptMnuGrpBVoList!=null && !ptMnuGrpBVoList.isEmpty()){
				// 메뉴 그룹ID 가 없으면 첫번째거
				if(mnuGrpId==null){
					ptMnuGrpBVo = ptMnuGrpBVoList.get(0);
					mnuGrpId = ptMnuGrpBVo.getMnuGrpId();
					mnuGrpNm = ptMnuGrpBVo.getRescNm();
					
					String mnuGrpTypCd = ptMnuGrpBVo.getMnuGrpTypCd();
					// 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임
					hasPortalMnu = "01".equals(mnuGrpTypCd) || "03".equals(mnuGrpTypCd);
				} else {
					// 메뉴그룹 명 찾기
					int i, size = ptMnuGrpBVoList.size();
					for(i=0; i<size; i++){
						ptMnuGrpBVo = ptMnuGrpBVoList.get(i);
						if(mnuGrpId.equals(ptMnuGrpBVo.getMnuGrpId())){
							mnuGrpNm = ptMnuGrpBVo.getRescNm();
							
							String mnuGrpTypCd = ptMnuGrpBVo.getMnuGrpTypCd();
							// 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임
							hasPortalMnu = "01".equals(mnuGrpTypCd) || "03".equals(mnuGrpTypCd);
							
							break;
						}
					}
				}
			}
			
			// 메뉴그룹의 메뉴트리 조회하여 model에 세팅함
			if(mnuGrpId!=null){
				model.put("mnuGrpId", mnuGrpId);
				model.put("mnuGrpNm", mnuGrpNm);
				
				if(hasPortalMnu){
					//메뉴상세(PT_MNU_D) 테이블
					PtMnuDVo ptMnuDVo = new PtMnuDVo();
					ptMnuDVo.setMnuGrpId(mnuGrpId);
					ptMnuDVo.setUseYn("Y");
					ptMnuDVo.setQueryLang(langTypCd);
					
					ptMnuDVo.setOrderBy("T.MNU_PID, T.SORT_ORDR");
					@SuppressWarnings("unchecked")
					List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
					model.put("ptMnuDVoList", makePtMnuDVoTree(ptMnuDVoList, mnuGrpId));
				}
			}
			
		// 레이아웃 트리 조회
		} else if("L".equals(mnuPltTypCd)) {
			
			// 레이아웃의 메뉴조합 ID/명 세팅
			if(ptMnuLoutDVoList!=null && !ptMnuLoutDVoList.isEmpty()){
				// 메뉴 그룹ID 가 없으면 첫번째거
				if(mnuGrpId==null){
					ptMnuLoutDVo = ptMnuLoutDVoList.get(0);
					mnuLoutId = ptMnuLoutDVo.getMnuLoutId();
					mnuLoutNm = ptMnuLoutDVo.getRescNm();
				} else {
					// 메뉴그룹 명 찾기
					int i, size = ptMnuLoutDVoList.size();
					for(i=0; i<size; i++){
						ptMnuLoutDVo = ptMnuLoutDVoList.get(i);
						if(mnuGrpId.equals(ptMnuLoutDVo.getMnuLoutId())){
							mnuLoutId = mnuGrpId;
							mnuLoutNm = ptMnuLoutDVo.getRescNm();
							break;
						}
					}
				}
			}
			
			// 레이아웃조합의 메뉴트리 조회하여 model에 세팅함
			if(mnuLoutId!=null){
				model.put("mnuGrpId", mnuLoutId);
				model.put("mnuGrpNm", mnuLoutNm);
				
				// 레이아웃조합 메뉴트리 세팅
				Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = ptLoutSvc.getLoutCombTreeByLoutIdMap(compId, langTypCd);
				Map<Integer, PtMnuDVo> mnuByMnuIdMap = ptLoutSvc.getMnuByMnuIdMap(langTypCd);
				List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList = loutCombTreeByLoutIdMap.get(Hash.hashId(mnuLoutId));
				List<PtMnuDVo> ptMnuDVoList = makePtMnuDVoTreeByComb(ptMnuLoutCombDVoList, mnuByMnuIdMap);
				if(ptMnuDVoList!=null){
					model.put("ptMnuDVoList", ptMnuDVoList);
				}
				
				// 권한 코드 결정자
				AuthCdDecider authCdDecider = ptSecuSvc.getAuthCdDecider(compId, authScopCd);
				String[] authGrpIds = {authGrpId};
				String auth;
				
				// 권한맵
				Map<Integer, String> authCdMap = new HashMap<Integer, String>();
				// 조합그룹의 권한 세팅
				if(isAdmMnu){
					auth = authCdDecider.getAdmAuthCd(mnuLoutId, authGrpIds);
					if(auth!=null) authCdMap.put(Hash.hashId(mnuLoutId), auth);
				} else {
					auth = authCdDecider.getUsrAuthCd(mnuLoutId, authGrpIds);
					if(auth!=null) authCdMap.put(Hash.hashId(mnuLoutId), auth);
				}
				
				// 조합그룹의 상세 메뉴 권한 세팅
				int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
				PtMnuDVo ptMnuDVo;
				for(i=0;i<size;i++){
					ptMnuDVo = ptMnuDVoList.get(i);
					if(isAdmMnu){
						auth = authCdDecider.getAdmAuthCd(ptMnuDVo.getMnuId(), authGrpIds);
						if(auth!=null) authCdMap.put(Hash.hashId(ptMnuDVo.getMnuId()), auth);
					} else {
						auth = authCdDecider.getUsrAuthCd(ptMnuDVo.getMnuId(), authGrpIds);
						if(auth!=null) authCdMap.put(Hash.hashId(ptMnuDVo.getMnuId()), auth);
					}
				}
				model.put("authCdMap", authCdMap);
			}
			
		}
		
		// 레이아웃이 아닌경우 - 메뉴그룹 / 포틀릿 의 권한 세팅
		if(!"L".equals(mnuPltTypCd)) {
			// 권한그룹메뉴포틀릿관계(PT_AUTH_GRP_MNU_PLT_R) 테이블 - 세팅된 권한 정보 조회
			PtAuthGrpMnuPltRVo ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
			ptAuthGrpMnuPltRVo.setCompId(compId);
			ptAuthGrpMnuPltRVo.setAuthScopCd(authScopCd);
			ptAuthGrpMnuPltRVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpMnuPltRVo.setAuthGrpId(authGrpId);
			@SuppressWarnings("unchecked")
			List<PtAuthGrpMnuPltRVo> ptAuthGrpMnuPltRVoList = (List<PtAuthGrpMnuPltRVo>)commonSvc.queryList(ptAuthGrpMnuPltRVo);
			
			// mnuPltId(메뉴포틀릿ID) : authCd(권한코드) 맵으로 변환
			Map<Integer, String> authCdMap = new HashMap<Integer, String>();
			int i, size = ptAuthGrpMnuPltRVoList==null ? 0 : ptAuthGrpMnuPltRVoList.size();
			for(i=0;i<size;i++){
				ptAuthGrpMnuPltRVo = ptAuthGrpMnuPltRVoList.get(i);
				authCdMap.put(Hash.hashId(ptAuthGrpMnuPltRVo.getMnuPltId()), ptAuthGrpMnuPltRVo.getAuthCd());
			}
			model.put("authCdMap", authCdMap);
		}
		
		
		return LayoutUtil.getJspPath("/pt/adm/auth/setAuth");
	}
	
	/** Tree 형으로 mnuPath 등 세팅하여 구조화 */
	private List<PtMnuDVo> makePtMnuDVoTreeByComb(List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList, Map<Integer, PtMnuDVo> mnuByMnuIdMap) {
		if(ptMnuLoutCombDVoList==null || ptMnuLoutCombDVoList.isEmpty()) return null;
		List<PtMnuDVo> returnList = new ArrayList<PtMnuDVo>();
		
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		int i, size = ptMnuLoutCombDVoList==null ? 0 : ptMnuLoutCombDVoList.size();
		for(i=0;i<size;i++){
			ptMnuLoutCombDVo = ptMnuLoutCombDVoList.get(i);
			makePtMnuDVoTreeListByComb(ptMnuLoutCombDVo, "R_"+(i+1), 1, mnuByMnuIdMap, returnList);
		}
		return returnList;
	}
	/** 메뉴의 경로세팅 - 경로를 R_1_1, returnList 에 트리 구조로 만듬 */
	private void makePtMnuDVoTreeListByComb(PtMnuLoutCombDVo ptMnuLoutCombDVo, String path, Integer mnuLvl,
			Map<Integer, PtMnuDVo> mnuByMnuIdMap, List<PtMnuDVo> returnList){
		
		PtMnuDVo cachedPtMnuDVo = null;
		if(ptMnuLoutCombDVo.getMnuId() != null){
			cachedPtMnuDVo = mnuByMnuIdMap.get(Hash.hashId(ptMnuLoutCombDVo.getMnuId()));
		}
		
		// PtMnuLoutCombDVo > PtMnuDVo 로 데이터 전환
		PtMnuDVo ptMnuDVo = new PtMnuDVo();
		if(cachedPtMnuDVo==null){
			ptMnuDVo.setMnuId(ptMnuLoutCombDVo.getMnuLoutCombId());
		} else {
			ptMnuDVo.setMnuId(cachedPtMnuDVo.getMnuId());
			ptMnuDVo.setMnuGrpId(cachedPtMnuDVo.getMnuGrpId());
		}
		ptMnuDVo.setFldYn(ptMnuLoutCombDVo.getFldYn());
		ptMnuDVo.setRescNm(ptMnuLoutCombDVo.getRescNm());
		ptMnuDVo.setMnuLvl(mnuLvl.toString());
		ptMnuDVo.setMnuPath(path);
		returnList.add(ptMnuDVo);
		
		PtMnuLoutCombDVo childVo;
		List<PtMnuLoutCombDVo> childList = ptMnuLoutCombDVo.getChildList();
		int i, size = childList==null ? 0 : childList.size();
		for(i=0;i<size;i++){
			childVo = childList.get(i);
			makePtMnuDVoTreeListByComb(childVo, path+"_"+(i+1), mnuLvl+1, mnuByMnuIdMap, returnList);
		}
	}
	
	/** Tree 형으로 mnuPath 등 세팅하여 구조화 */
	private List<PtMnuDVo> makePtMnuDVoTree(List<PtMnuDVo> ptMnuDVoList, String mnuGrpId){
		List<PtMnuDVo> sorted, returnList = new ArrayList<PtMnuDVo>();
		Map<Integer, List<PtMnuDVo>> treeMap = new HashMap<Integer, List<PtMnuDVo>>();
		Integer hashKey;
		for(PtMnuDVo ptMnuDVo : ptMnuDVoList){
			hashKey = Hash.hashId(ptMnuDVo.getMnuPid());
			sorted = treeMap.get(hashKey);
			if(sorted==null){
				sorted = new ArrayList<PtMnuDVo>();
				treeMap.put(hashKey, sorted);
			}
			sorted.add(ptMnuDVo);
		}
		
		makePtMnuDVoTreeList(mnuGrpId, "R", 1, treeMap, returnList);
		return returnList;
	}
	/** 메뉴의 경로세팅 - 경로를 R_1_1, returnList 에 트리 구조로 만듬 */
	private void makePtMnuDVoTreeList(String pid, String path, Integer mnuLvl, Map<Integer, List<PtMnuDVo>> treeMap, List<PtMnuDVo> returnList) {
		List<PtMnuDVo> sorted = treeMap.get(Hash.hashId(pid));
		if(sorted!=null){
			int no = 0;
			for(PtMnuDVo ptMnuDVo : sorted){
				ptMnuDVo.setMnuPath(path+"_"+(++no));
				ptMnuDVo.setMnuLvl(mnuLvl.toString());
				returnList.add(ptMnuDVo);
				makePtMnuDVoTreeList(ptMnuDVo.getMnuId(), ptMnuDVo.getMnuPath(), mnuLvl+1, treeMap, returnList);
			}
		}
	}
	
	/** 권한 그룹 별 권한 관리 - 저장 */
	@RequestMapping(value = "/pt/adm/auth/transAuth")
	public String transAuth(HttpServletRequest request,
			@Parameter(name="authScopCd", required=false) String authScopCd,
			@Parameter(name="authGrpTypCd", required=false)String authGrpTypCd,
			@Parameter(name="authGrpId", required=false)String authGrpId,
			@Parameter(name="mnuPltTypCd", required=false)String mnuPltTypCd,//메뉴포틀릿구분코드 - KEY - M:메뉴, P:포틀릿
			@Parameter(name="mnuGrpId", required=false)String mnuGrpId,
			ModelMap model) throws Exception {

		if(!PtConstant.AUTH_IP_EX.equals(authScopCd)) authScopCd = PtConstant.AUTH_IP_IN;
		
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			
			if(	authGrpTypCd==null || authGrpTypCd.isEmpty()
				|| authGrpId==null || authGrpId.isEmpty()
				|| mnuPltTypCd==null || mnuPltTypCd.isEmpty() ){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			
			// 권한그룹메뉴포틀릿관계(PT_AUTH_GRP_MNU_PLT_R) 테이블 - 기존 설정 삭제
			PtAuthGrpMnuPltRVo ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
			ptAuthGrpMnuPltRVo.setCompId(compId);
			ptAuthGrpMnuPltRVo.setAuthScopCd(authScopCd);
			ptAuthGrpMnuPltRVo.setAuthGrpTypCd(authGrpTypCd);
			ptAuthGrpMnuPltRVo.setAuthGrpId(authGrpId);
			ptAuthGrpMnuPltRVo.setMnuGrpId(mnuGrpId);
			queryQueue.delete(ptAuthGrpMnuPltRVo);
			
			boolean isLout = "L".equals(mnuPltTypCd);// 레이아웃 여부
			int prefixLen = 0;
			String name, value, mnuPltId, grpId, prefix=null;
			
			//메뉴포틀릿구분코드 - KEY - M:메뉴, P:포틀릿, MG:메뉴그룹, L:레이아웃
			if("M".equals(mnuPltTypCd) || isLout){
				prefix = "mnu_";
				prefixLen = 4;
				
				value = request.getParameter("grp_"+mnuGrpId);
				if(value!=null && !value.isEmpty() && !"N".equals(value)){
					ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
					ptAuthGrpMnuPltRVo.setCompId(compId);
					ptAuthGrpMnuPltRVo.setAuthScopCd(authScopCd);
					ptAuthGrpMnuPltRVo.setAuthGrpTypCd(authGrpTypCd);
					ptAuthGrpMnuPltRVo.setAuthGrpId(authGrpId);
					ptAuthGrpMnuPltRVo.setMnuGrpId(mnuGrpId);
					//메뉴포틀릿구분코드 - KEY - M:메뉴, P:포틀릿, MG:메뉴그룹, L:레이아웃
					if("M".equals(mnuPltTypCd)){
						ptAuthGrpMnuPltRVo.setMnuPltTypCd("MG");
					} else {
						ptAuthGrpMnuPltRVo.setMnuPltTypCd("L");
					}
					ptAuthGrpMnuPltRVo.setMnuPltId(mnuGrpId);
					ptAuthGrpMnuPltRVo.setAuthCd(value);
					queryQueue.insert(ptAuthGrpMnuPltRVo);
				}
				
			} else if("P".equals(mnuPltTypCd)){
				prefix = "plt_";
				prefixLen = 4;
			}
			
			Enumeration<String> enums = request.getParameterNames();
			
			// 루프 돌면서 queryQueue 권한정보 세팅
			while(enums.hasMoreElements()){
				name = enums.nextElement();
				value = request.getParameter(name);
				
				// 권한값이 없거나, 권한없음이면 생략
				if(name.isEmpty() || value.isEmpty()) continue;
				if(!name.startsWith(prefix)) continue;
				if(!isLout && "N".equals(value)) continue;
				
				ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
				// key - start
				ptAuthGrpMnuPltRVo.setCompId(compId);
				ptAuthGrpMnuPltRVo.setAuthScopCd(authScopCd);
				ptAuthGrpMnuPltRVo.setAuthGrpId(authGrpId);
				mnuPltId = name.substring(prefixLen);
				ptAuthGrpMnuPltRVo.setMnuPltId(mnuPltId);
				// key - end
				
				if(isLout && "N".equals(value)){// 레이아웃의 경우 한건 한건 지워야함
					queryQueue.delete(ptAuthGrpMnuPltRVo);
				} else {
					
					ptAuthGrpMnuPltRVo.setAuthGrpTypCd(authGrpTypCd);
					if(isLout){//레이아웃의 경우 - 각각의 메뉴그룹을 히든테그에 넣어둠
						// 생성한 폴더의 경우는 메뉴그룹ID가 넘겨오지 않으며 이 경우 레이아웃조합ID를 넣어서
						// 차후 레이아웃조합을 삭제 할 때 같이 삭제 되도록 함
						grpId = request.getParameter("mnug_"+mnuPltId);
						if(grpId==null || grpId.isEmpty()) grpId = mnuGrpId;
						ptAuthGrpMnuPltRVo.setMnuGrpId(grpId);
					} else {
						ptAuthGrpMnuPltRVo.setMnuGrpId(mnuGrpId);
					}
					ptAuthGrpMnuPltRVo.setMnuPltTypCd(mnuPltTypCd);
					ptAuthGrpMnuPltRVo.setAuthCd(value);
					if(isLout){
						queryQueue.store(ptAuthGrpMnuPltRVo);
					} else {
						queryQueue.insert(ptAuthGrpMnuPltRVo);
					}
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.AUTH);
			
			//cm.msg.save.success=저장 되었습니다.
			String msg = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", msg);
			
		} catch(CmException e){
			LOGGER.error(e.getMessage()+"  compId:"+compId+" authGrpTypCd:"+authGrpTypCd+" authGrpId:"+authGrpId);
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
}
