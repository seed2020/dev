package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmAuthDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtAuthGrpBVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

@Controller
public class DmAdmAuthCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmAuthCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	
	/** [팝업] 권한관리 (관리자) */
	@RequestMapping(value = "/dm/adm/auth/setAuth")
	public String setAuth(HttpServletRequest request,
			@RequestParam(value = "clsCd", required = false) String clsCd,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Auth");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		
		/** 분류코드 조회 */
		List<PtCdBVo> clsCdList = ptCmSvc.getCdList(DmConstant.AUTH_GRP_CD, langTypCd, "Y");
		model.put("clsCdList", clsCdList);
		
		if(clsCd == null || clsCd.isEmpty()) clsCd = DmConstant.SECUL_CD;
		
		/** 코드 조회 */
		List<PtCdBVo> cdList = new ArrayList<PtCdBVo>(); 
		
		if(clsCd.startsWith(DmConstant.AUTH_GRP_PREFIX)){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String[] clsChars = clsCd.split("_");
			if(clsChars.length>0){
				// 권한그룹기본(PT_AUTH_GRP_B) 테이블 - 목록조회
				PtAuthGrpBVo ptAuthGrpBVo = new PtAuthGrpBVo();
				ptAuthGrpBVo.setCompId(userVo.getCompId());//회사ID
				ptAuthGrpBVo.setAuthGrpTypCd(clsChars[clsChars.length-1]);
				ptAuthGrpBVo.setQueryLang(langTypCd);
				ptAuthGrpBVo.setOrderBy("AUTH_GRP_CAT_CD, SORT_ORDR");//AUTH_GRP_CAT_CD 권한그룹카테고리코드, AUTH_GRP_ID, 권한그룹ID
				
				@SuppressWarnings("unchecked")
				List<PtAuthGrpBVo> ptAuthGrpBVoList = (List<PtAuthGrpBVo>)commonSvc.queryList(ptAuthGrpBVo);
				PtAuthGrpBVo storedPtAuthGrpBVo;
				PtCdBVo ptCdBVo = null;
				int i, size = ptAuthGrpBVoList==null ? 0 : ptAuthGrpBVoList.size();
				for(i=0;i<size;i++){
					storedPtAuthGrpBVo = ptAuthGrpBVoList.get(i);
					if(storedPtAuthGrpBVo.getAuthGrpCatCd()==null) continue;
					ptCdBVo = new PtCdBVo();
					ptCdBVo.setCd(storedPtAuthGrpBVo.getAuthGrpId());
					ptCdBVo.setRescNm(storedPtAuthGrpBVo.getAuthGrpNm()+"-"+storedPtAuthGrpBVo.getAuthGrpCatNm());
					cdList.add(ptCdBVo);
				}
			}
		}else{
			cdList.addAll(ptCmSvc.getCdList(clsCd, langTypCd, "Y"));
		}
		
		model.put("cdList", cdList);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 조회조건 매핑
		DmAuthDVo dmAuthDVo = new DmAuthDVo();
		VoUtil.bind(request, dmAuthDVo);
		dmAuthDVo.setQueryLang(langTypCd);
		dmAuthDVo.setCompId(userVo.getCompId());
		dmAuthDVo.setClsCd(clsCd);
		@SuppressWarnings("unchecked")
		List<DmAuthDVo> dmAuthDVoList = (List<DmAuthDVo>)commonSvc.queryList(dmAuthDVo);
		
		// 권한 맵 세팅
		Map<String, String> listMap = new HashMap<String,String>();
		for(DmAuthDVo storedDmAuthDVo : dmAuthDVoList){
			listMap.put(storedDmAuthDVo.getClsCd()+"_"+storedDmAuthDVo.getCd()+"_"+storedDmAuthDVo.getAuthVa(), storedDmAuthDVo.getUseYn());
		}
		model.put("clsCd", clsCd);
		model.put("listMap", listMap);
		model.put("params", ParamUtil.getQueryString(request));
		
		// 권한대상 목록
		model.put("authList", dmAdmSvc.getDmAuthCdList(request));
		
		return LayoutUtil.getJspPath("/dm/adm/auth/setAuth");
	}
	
	/** 권한 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/auth/transAuth")
	public String transAuth(HttpServletRequest request,
			@RequestParam(value = "clsCd", required = false) String clsCd,
			ModelMap model) {

		try {
			if (clsCd == null || clsCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			//String storId = dmStorBVo.getStorId();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			//권한 전체 삭제(분류코드)
			DmAuthDVo dmAuthDVo = new DmAuthDVo();
			dmAuthDVo.setCompId(userVo.getCompId());
			dmAuthDVo.setClsCd(clsCd);
			queryQueue.delete(dmAuthDVo);
			
			// 컬럼표시여부(DM_AUTH_D) 테이블 - BIND
			List<DmAuthDVo> boundDmAuthDVoList = (List<DmAuthDVo>) VoUtil.bindList(request, DmAuthDVo.class, new String[]{"useYn"});
			for(DmAuthDVo storedDmAuthDVo : boundDmAuthDVoList){
				storedDmAuthDVo.setCompId(userVo.getCompId());
				storedDmAuthDVo.setClsCd(clsCd);
				queryQueue.insert(storedDmAuthDVo);
			}
			
			commonSvc.execute(queryQueue);
			/*// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.BRD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.BRD);*/

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
}
