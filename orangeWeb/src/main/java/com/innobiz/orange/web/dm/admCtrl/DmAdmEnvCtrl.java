package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmDocNoSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmTaskSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocNoDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Controller
public class DmAdmEnvCtrl {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(DmAdmEnvCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private DmCmSvc dmCmSvc;
	
	/** 관리 서비스 */
	@Autowired
	private DmAdmSvc dmAdmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 작업이력 서비스 */
	@Resource(name = "dmTaskSvc")
	private DmTaskSvc dmTaskSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "dmDocNoSvc")
	private DmDocNoSvc dmDocNoSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 환경설정 */
	@RequestMapping(value = "/dm/adm/env/setEnv")
	public String setEnv(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 관리자 여부
		boolean isSysAdmin = false;//SecuUtil.hasAuth(request, "SYS", null);
		
		if(isSysAdmin){//시스템관리자가 회사별 설정이 필요할 경우 기능 구현
			// 회사목록
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
			model.put("ptCompBVoList", ptCompBVoList);
			if(compId == null || compId.isEmpty()) compId = ptCompBVoList.get(0).getCompId();
			else{
				// 관리자가 주소표시줄에 회사ID를 임의로 넣을 경우를 대비해서 회사ID를 체크한다.
				boolean chkCompId = false;
				for(PtCompBVo storedPtCompBVo : ptCompBVoList){
					if(storedPtCompBVo.getCompId().equals(compId)){
						chkCompId = true;
						break;
					}
				}
				if(!chkCompId){
					// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
					model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
					return LayoutUtil.getResultJsp();
				}
			}
		}
		
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		if(!isSysAdmin) compId = userVo.getCompId();
				
		// 환경설정 세팅
		dmAdmSvc.getEnvConfigMap(model, compId);
		
		// 이력대상 목록
		model.put("taskList", dmTaskSvc.getDmTaskCdList(request));
		
		model.put("months", DmConstant.MONTHS);
		model.put("days", DmConstant.DAYS);
		
		// 기본 저장소가 없으면 초기화 버튼을 활성화 한다.
		model.put("resetYn", dmStorSvc.getDmStorBVo(request, model, null, null, null, null) == null ? "Y" : "N");
		
		return LayoutUtil.getJspPath("/dm/adm/env/setEnv");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/dm/adm/env/transEnv")
	public String transOpt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(DmConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 문서번호 초기화 */
	@RequestMapping(value = "/dm/adm/env/setDocNoResetPop")
	public String setDocNoResetPop(HttpServletRequest request,
			@RequestParam(value = "docNoDftOrg", required = false) String docNoDftOrg,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			//return LayoutUtil.getResultJsp();
			throw new CmException("dm.msg.nodata.stor", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 회사ID
		String compId = userVo.getCompId();
		
		// 환경설정
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
		
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		// 문서번호VO
		DmDocNoDVo dmDocNoDVo = new DmDocNoDVo();
		dmDocNoDVo.setStorId(storId);
		dmDocNoDVo.setOrgId(compId);
		
		// 회사 문서번호 조회
		dmDocNoDVo = dmDocNoSvc.getDmDocNoDVo(envConfigMap, dmDocNoDVo);
		if(dmDocNoDVo != null) model.put("compDocSeq", dmDocNoDVo.getDocSeq()+1);
		else model.put("compDocSeq", 1);
		
		// 문서채번 기준(조직) - 조직이 설정되지 않을 경우 조직문서번호는 초기화 할수 없다.
		//String docNoDftOrg = envConfigMap.get("docNoDftOrg");
		if(docNoDftOrg == null || docNoDftOrg.isEmpty() || "N".equals(docNoDftOrg)) model.put("isOrgChk", Boolean.FALSE);
		else {
			List<DmDocNoDVo> dmDocNoDVoList = new ArrayList<DmDocNoDVo>();
			dmDocNoDVoList.add(new DmDocNoDVo());
			model.put("dmDocNoDVoList", dmDocNoDVoList);
			model.put("isOrgChk", Boolean.TRUE);
		}
					
		return LayoutUtil.getJspPath("/dm/adm/env/setDocNoResetPop");
	}
	
	/** 문서번호 초기화 */
	@RequestMapping(value = "/dm/adm/env/transDocNoReset")
	public String transDocNoReset(HttpServletRequest request,
			@RequestParam(value = "compDocNo", required = false) String compDocNo,
			@RequestParam(value = "orgDocNo", required = false) String orgDocNo,
			ModelMap model) throws Exception {
		
		try{
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
			// 저장소ID
			String storId = dmStorBVo.getStorId();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 환경설정
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			String yy = null;
			// 문서채번 기준(연도)
			String docNoDftYear = envConfigMap.get("docNoDftYear");
			if(docNoDftYear != null && "Y".equals(docNoDftYear)) {
				String date = commonSvc.querySysdate(null);
				// 회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
				String year = dmDocNoSvc.getRecordYear(date, envConfigMap);
				yy = year;
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 문서번호상세(DM_DOC_NO_D) 테이블 VO
			DmDocNoDVo dmDocNoDVo;
			
			// 회사 문서일련번호 저장
			if(compDocNo != null && !compDocNo.isEmpty() && Integer.parseInt(compDocNo)>0){
				int compStrtSeq = Integer.parseInt(compDocNo)-1;
				dmDocNoDVo = new DmDocNoDVo();
				dmDocNoDVo.setStorId(storId);
				dmDocNoDVo.setYy(yy);
				dmDocNoDVo.setOrgId(userVo.getCompId());
				dmDocNoDVo.setDocSeq(compStrtSeq);
				queryQueue.store(dmDocNoDVo);
			}
			
			// 조직 문서일련번호 저장
			if(orgDocNo != null && !orgDocNo.isEmpty() && Integer.parseInt(orgDocNo)>0){
				int orgStrtSeq = Integer.parseInt(orgDocNo)-1;
				@SuppressWarnings("unchecked")
				List<DmDocNoDVo> dmDocNoDVoList = (List<DmDocNoDVo>)VoUtil.bindList(request, DmDocNoDVo.class, new String[]{"orgId"});
				if(dmDocNoDVoList.size()>0){// 선택된 조직의 일련번호만 변경
					for(DmDocNoDVo storedDmDocNoDVo : dmDocNoDVoList){
						storedDmDocNoDVo.setStorId(storId);
						storedDmDocNoDVo.setYy(yy);
						storedDmDocNoDVo.setDocSeq(orgStrtSeq);
						queryQueue.store(storedDmDocNoDVo);
					}
				}else{// 전체 조직의 일련번호 변경
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					
					// 조직도 트리 조회
					List<OrOrgBVo> orOrgBVoList = orCmSvc.getOrgTreeList(userVo.getCompId(), null, langTypCd);
					for(OrOrgBVo storedOrOrgBVo : orOrgBVoList){
						dmDocNoDVo = new DmDocNoDVo();
						dmDocNoDVo.setStorId(storId);
						dmDocNoDVo.setYy(yy);
						dmDocNoDVo.setOrgId(storedOrOrgBVo.getOrgId());
						dmDocNoDVo.setDocSeq(orgStrtSeq);
						queryQueue.store(dmDocNoDVo);
					}
				}
			}
			
			if(queryQueue != null && !queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 문서번호 초기화 */
	@RequestMapping(value = "/dm/adm/env/transDocNoResetAjx")
	public String transDocNoResetAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			//JSONObject object = (JSONObject) JSONValue.parse(data);
			
			//String orgId = (String)object.get("orgId");// 조직ID
			
			
			// 추석 끝나고 여기서부터...
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 회사ID
			String compId = userVo.getCompId();
			
			QueryQueue queryQueue = new QueryQueue();
			// 환경설정
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
			
			// 현재 날짜
			String date = commonSvc.querySysdate(null);
			
			// 회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
			String year = dmDocNoSvc.getRecordYear(date, envConfigMap);
			
			// 문서채번 기준(연도)
			String docNoDftYear = envConfigMap.get("docNoDftYear");
			if(docNoDftYear == null || "N".equals(docNoDftYear)) year = null;
			
			// 문서채번 기준(조직)
			//String docNoDftOrg = envConfigMap.get("docNoDftOrg");
			//if(docNoDftOrg == null || "N".equals(docNoDftOrg)) orgId = compId;
			
			// 연도 설정이 없을경우 기본연도(0000)으로 채번한다.
			if(year == null) year = DmConstant.DOC_NO_YEAR;
			
			commonSvc.execute(queryQueue);
						
		} /*catch (CmException e) {
			model.put("message", e.getMessage());
		} */catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
