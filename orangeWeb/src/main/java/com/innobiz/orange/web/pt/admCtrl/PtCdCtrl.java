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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoHolder;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.PushSyncSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 코드관리 */
@Controller
public class PtCdCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtCdCtrl.class);
	
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
	
	/** 조직도 사용자 Push 방식 동기화 서비스 */
	@Autowired
	private PushSyncSvc pushSyncSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 코드 관리 */
	@RequestMapping(value = "/pt/adm/cd/setCd")
	public String setCd(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String setupId = "code";
		model.put("setupId", setupId);
		
		Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
		String setupVa = map==null ? null : map.get(setupId);
		if(setupVa == null || !"allow".equals(setupVa)){
			model.put("cdEditAuth", "SYS");
		} else {
			model.put("cdEditAuth", "A");
		}
		return LayoutUtil.getJspPath("/pt/adm/cd/setCd");
	}
	
	/** 카테고리 코드 관리 - 코드 이외의 곳에서 카테고리 코드관리로 링크 하는 경우 */
	@RequestMapping(value = "/{path1}/adm/{path2}/setCatCd")
	public String setCatCd(HttpServletRequest request,
			@Parameter(name="clsCd", required=false) String clsCd,
			@PathVariable("path1") String path1,
			@PathVariable("path2") String path2,
			ModelMap model) throws Exception {
		
		// 조직관리 - 용어설정 에서 올 경우
		String compId = null;
		if("pt".equals(path1) && "org".equals(path2)){
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
			model.addAttribute("forOneComp", Boolean.TRUE);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			PtCdBVo ptCdBVo = ptCmSvc.getCd("GW_OR", langTypCd, clsCd);
			if(ptCdBVo!=null){
				model.put("menuTitle", ptCdBVo.getRescNm()+" "+messageProperties.getMessage("cm.title.mng", request));
			}
		}
		
		listCdFrm(request, clsCd, compId, model);
		return LayoutUtil.getJspPath("/pt/adm/cd/setCatCd");
	}
	/** 카테고리 코드 관리 - 코드 이외의 곳에서 카테고리 코드관리로 링크 하는 경우 - 4단 경로 */
	@RequestMapping(value = "/{path1}/adm/{path2}/{path3}/setCatCd")
	public String setCatCd2(HttpServletRequest request,
			@Parameter(name="clsCd", required=false) String clsCd,
			ModelMap model) throws Exception {
		listCdFrm(request, clsCd, null, model);
		return LayoutUtil.getJspPath("/pt/adm/cd/setCatCd");
	}
	/** [프레임] 코드 목록 조회 */
	@RequestMapping(value = "/pt/adm/cd/listCdFrm")
	public String listCdFrm(HttpServletRequest request,
			@Parameter(name="clsCd", required=false) String clsCd,
			String compId,
			ModelMap model) throws Exception {
		
		if(clsCd!=null && !clsCd.isEmpty()){
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 코드 목록
			List<PtCdBVo> ptCdBVoList = null;
			
			// 코드 관리의 오른쪽 프레임 - 회사별 아닌 전체 데이터 보여줌
			if(compId==null){
				// 전체 코드 조회
				ptCdBVoList = ptCmSvc.getAllCdList(clsCd, langTypCd);
				// 화면 구성용 2개의 빈 vo 넣음
				ptCdBVoList.add(new PtCdBVo());
				ptCdBVoList.add(new PtCdBVo());
				
			// 코드 관리(메뉴)가 아닌, 카테고리 또는 회사별 직위,직급 관리(메뉴:조직 용어 설정)
			} else {
				
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
				// 직급, 직책, 직위, 직무
				if(ArrayUtil.isInArray(new String[]{"GRADE_CD","TITLE_CD","POSIT_CD","DUTY_CD"}, clsCd)){
					// 회사별 조직코드 사용
					if("Y".equals(sysPlocMap.get("codeByCompEnable"))){
						// 회사목록과 compId 가 같은 것 조회
						ptCdBVoList = ptCmSvc.getCdListEqCompId(clsCd, langTypCd, compId, "Y");
					} else {
						// 이 경우 - 이곳을 호출 안함
						// 회사목록에 compId 가 포함된 것 조회
						ptCdBVoList = ptCmSvc.getCdListByCompId(clsCd, langTypCd, compId, "Y");
					}
					
				// 보안등급
				} else if("SECUL_CD".equals(clsCd)){
					// 회사별 보안등급코드 사용
					if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
						// 회사목록과 compId 가 같은 것 조회
						ptCdBVoList = ptCmSvc.getCdListEqCompId(clsCd, langTypCd, compId, "Y");
					} else {
						// 이 경우 - 이곳을 호출 안함
						// 회사목록에 compId 가 포함된 것 조회
						ptCdBVoList = ptCmSvc.getCdListByCompId(clsCd, langTypCd, compId, "Y");
					}
				// 카테고리
				} else {
					// 회사목록에 compId 가 포함된 것 조회
					ptCdBVoList = ptCmSvc.getCdListByCompId(clsCd, langTypCd, compId, "Y");
				}
				
				// 화면 구성용 2개의 빈 vo 넣음
				PtCdBVo ptCdBVo = new PtCdBVo();
				ptCdBVo.setCompIds(compId);
				ptCdBVoList.add(ptCdBVo);
				
				ptCdBVo = new PtCdBVo();
				ptCdBVo.setCompIds(compId);
				ptCdBVoList.add(ptCdBVo);
			}
			model.put("ptCdBVoList", ptCdBVoList);
			
			// 코드 리소스 model에 저장
			PtCdBVo ptCdBVo = new PtCdBVo();
			ptCdBVo.setClsCd(clsCd);
			ptCdBVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtCdBDao.selectPtCdRescB");
			@SuppressWarnings("unchecked")
			List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptCdBVo);
			if(ptRescBVoList!=null){
				for(PtRescBVo ptRescBVo : ptRescBVoList){
					model.put(ptRescBVo.getRescId()+"_"+ptRescBVo.getLangTypCd(), ptRescBVo.getRescVa());
				}
			}
			
		}
		return LayoutUtil.getJspPath("/pt/adm/cd/listCdFrm");
	}
	
	/** [팝업] 폴더 조회 - 수정용 조회 및 등록 화면 */
	@RequestMapping(value = "/pt/adm/cd/setFldPop")
	public String setFldPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String clsCd = request.getParameter("clsCd");
		String cd = request.getParameter("cd");
		
		// clsCd:분류코드, 코드:cd 가 있으면 조회하여 모델에 담음
		if(clsCd!=null && !clsCd.isEmpty() && cd!=null && !cd.isEmpty()){
			PtCdBVo ptCdBVo = new PtCdBVo();
			ptCdBVo.setClsCd(clsCd);
			ptCdBVo.setCd(cd);
			
			PtCdBVo storedPtCdBVo = (PtCdBVo)commonSvc.queryVo(ptCdBVo);
			if(storedPtCdBVo!=null){
				
				model.put("ptCdBVo", storedPtCdBVo);
				
				// 리소스 조회
				if(storedPtCdBVo.getRescId()!=null && !storedPtCdBVo.getRescId().isEmpty()){
					
					// 코드 리소스 model에 저장
					ptCdBVo = new PtCdBVo();
					ptCdBVo.setClsCd(clsCd);
					ptCdBVo.setCd(cd);
					ptCdBVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtCdBDao.selectPtCdRescB");
					@SuppressWarnings("unchecked")
					List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptCdBVo);
					if(ptRescBVoList!=null){
						for(PtRescBVo ptRescBVo : ptRescBVoList){
							model.put(ptRescBVo.getRescId()+"_"+ptRescBVo.getLangTypCd(), ptRescBVo.getRescVa());
						}
					}
				}
				
				
			}
		} else {
			PtCdBVo ptCdBVo = new PtCdBVo();
			ptCdBVo.setClsCd(clsCd);
			model.put("ptCdBVo", ptCdBVo);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/cd/setFldPop");
	}
	
	/** [프레임] 트리조회 */
	@RequestMapping(value = "/pt/adm/cd/treeCdFrm")
	public String treeCdFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setQueryLang(langTypCd);
		ptCdBVo.setFldYn("Y");
		ptCdBVo.setOrderBy("CLS_CD, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonSvc.queryList(ptCdBVo);
		model.put("ptCdBVoList", ptCdBVoList);
		
//		ptCdBVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtCdBMngDao.treePtCdB");
//		@SuppressWarnings("unchecked")
//		List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonSvc.queryList(ptCdBVo);
//		model.put("ptCdBVoList", ptCdBVoList);
		return LayoutUtil.getJspPath("/pt/adm/cd/treeCdFrm");
	}
	
	/** 폴더 저장 */
	@RequestMapping(value = "/pt/adm/cd/transFld")
	public String transFld(HttpServletRequest request,
			@Parameter(name="clsCd", required=false) String clsCd,
			@Parameter(name="cd", required=false) String cd,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model) throws Exception {
		
		if(clsCd==null || clsCd.isEmpty() || cd==null || cd.isEmpty()){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("save cd[folder] - clsCd:"+clsCd+" cd:"+cd+"  msg:"+msg);
			model.put("message", msg);
			return LayoutUtil.getResultJsp();
		}
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			if("reg".equals(mode)){
				// 코드 조회 - 카테고리에 있는지
				PtCdBVo ptCdBVo = new PtCdBVo();
				ptCdBVo.setCd(cd);
				ptCdBVo.setFldYn("Y");
				
				ptCdBVo.setQueryLang(langTypCd);
				
				@SuppressWarnings("unchecked")
				List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonSvc.queryList(ptCdBVo);
				if(ptCdBVoList!=null && ptCdBVoList.size()>0){
					//pt.msg.not.reg.cd.inUse=이미 사용중인 코드는 등록 할 수 없습니다.
					CmException exp = new CmException("pt.msg.not.reg.cd.inUse", request);
					ptCdBVo = ptCdBVoList.get(0);
					LOGGER.error(exp.getMessage()+"  registed code - clsCd:"+ptCdBVo.getClsCd()+" cd:"+ptCdBVo.getCd()+" cdVa:"+ptCdBVo.getCdVa());
					throw exp;
				}
			}
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 코드기본(PT_CD_B) 테이블
			PtCdBVo ptCdBVo = new PtCdBVo();
			VoUtil.bind(request, ptCdBVo);
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
			ptCdBVo.setRescId(ptRescBVo.getRescId());
			ptCdBVo.setCdVa(ptRescBVo.getRescVa());
			
			ptCdBVo.setModDt("sysdate");
			ptCdBVo.setModrUid(userVo.getUserUid());
			
			if("reg".equals(mode)){
				ptCdBVo.setFldYn("Y");
				ptCdBVo.setUseYn("Y");
				ptCdBVo.setSysCdYn("N");
				ptCdBVo.setRegDt("sysdate");
				ptCdBVo.setRegrUid(userVo.getUserUid());
				// 코드기본(PT_CD_B) 테이블 - INSERT
				queryQueue.insert(ptCdBVo);
			} else {
				// 코드기본(PT_CD_B) 테이블 - UPDATE
				queryQueue.update(ptCdBVo);
			}
			
			// 회사별 언어 설정
//			List<PtCdBVo> langCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
			
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if("reg".equals(mode)){
				model.put("todo", "parent.reloadTree('"+clsCd+"','');");
			} else {
				model.put("todo", "parent.reloadTree('"+clsCd+"','"+cd+"');");
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
	
	/** 코드 저장 - 일괄저장 */
	@RequestMapping(value = "/pt/adm/cd/transCd")
	public String transCd(HttpServletRequest request,
			@Parameter(name="clsCd", required=false)String clsCd,
			@Parameter(name="delList", required=false)String delList,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="noFrameYn", required=false) String noFrameYn,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 리소스기본(PT_RESC_B) 테이블
			PtRescBVo ptRescBVo;
			
			// 삭제 목록의 것 삭제 SQL 처리
			// 코드기본(PT_CD_B) 테이블
			PtCdBVo ptCdBVo, storedPtCdBVo;
			String[] delCds = delList==null || delList.isEmpty() ? null : delList.split(",");
			int i, size = delCds==null ? 0 : delCds.length;
			
			if(size>0){
				
				// 사용유무를 체크할 테이블의 vo 목록 - refVa2에 들어 있으며 콤마(,)로 분리되어 들어 있음
				List<CommonVo> checkVoList = getUsageCheckVoList(clsCd);
				
				for(i=0;i<size;i++){
					
					storedPtCdBVo = ptCmSvc.getCd(clsCd, langTypCd, delCds[i]);
					if(storedPtCdBVo!=null){
						
						// 해당 코드의 사용여부 체크 - 사용중이면 CmException 발생
						if(checkVoList!=null){
							checkCdUsage(checkVoList, delCds[i], storedPtCdBVo, request);
						}
						
						if("Y".equals(storedPtCdBVo.getSysCdYn())){
							//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.
							String msg = messageProperties.getMessage("pt.msg.not.del.cd.sys", request)
									+ " -  code:"+storedPtCdBVo.getCd()+"  value:"+storedPtCdBVo.getRescNm();
							LOGGER.error(msg);
							throw new CmException(msg);
						}
						
						// 리소스기본(PT_RESC_B) 테이블 - 삭제
						if(storedPtCdBVo.getRescId()!=null && !storedPtCdBVo.getRescId().isEmpty()){
							ptRescBVo = new PtRescBVo();
							ptRescBVo.setRescId(storedPtCdBVo.getRescId());
							queryQueue.delete(ptRescBVo);
						}
						
						// 코드기본(PT_CD_B) 테이블 - 삭제
						ptCdBVo = new PtCdBVo();
						ptCdBVo.setClsCd(clsCd);
						ptCdBVo.setCd(delCds[i]);
						queryQueue.delete(ptCdBVo);
					}
				}
			}
			
			// 코드기본(PT_CD_B) 테이블 VO에 데이터 담음
			@SuppressWarnings("unchecked")
			List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>) VoUtil.bindList(request, PtCdBVo.class, new String[]{"cd"});
			
			// 사용안함 처리한 시스템코드 체크
			size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
			for(i=0;i<size;i++){
				ptCdBVo = ptCdBVoList.get(i);
				storedPtCdBVo = ptCmSvc.getCd(ptCdBVo.getClsCd(), langTypCd, ptCdBVo.getCd());
				if("N".equals(ptCdBVo.getUseYn()) && storedPtCdBVo!=null && "Y".equals(storedPtCdBVo.getSysCdYn())){
					//pt.msg.not.use.cd.sys=시스템 코드는 사용안함 처리 할 수 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.use.cd.sys", request)
							+ " -  code:"+storedPtCdBVo.getCd()+"  value:"+storedPtCdBVo.getRescNm();
					LOGGER.error(msg);
					throw new CmException(msg);
				}
			}
			
			
			// 리소스 정보 queryQueue에 담음
			ptRescSvc.collectPtRescBVoList(request, ptCdBVoList, queryQueue);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
			
			// 사용자 조직도 동기화 PUSH - 코드
			if("GRADE_CD".equals(clsCd) || "TITLE_CD".equals(clsCd) || "POSIT_CD".equals(clsCd)
					|| "DUTY_CD".equals(clsCd) || "SECUL_CD".equals(clsCd) || "USER_STAT_CD".equals(clsCd)){
				// push 동기화
				if(pushSyncSvc.hasSync()){
					pushSyncSvc.syncCode();
				}
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if("Y".equals(noFrameYn)){
				// 카테고리 관리 에서 호출 - 되돌아 가기
				model.put("todo", "parent.goBack();");
			} else {
				// 코드 관리에서 호출
				model.put("todo", "parent.location.replace('./listCdFrm.do?menuId="+menuId+"&clsCd="+clsCd+"');");
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			
			CommonVo commonVo = VoHolder.getVo();
			if(commonVo!=null && commonVo.getQueryType() == QueryType.DELETE){
				if(commonVo instanceof PtCdBVo){
					PtCdBVo ptCdBVo = (PtCdBVo)commonVo;
					// pt.msg.not.del.cd.inUse=사용중인 코드는 지울수가 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.del.cd.inUse", request)
							+ " -  code:"+ptCdBVo.getCd()+"  value:"+ptCdBVo.getRescNm();
					LOGGER.error(msg);
					model.put("message", msg);
					return LayoutUtil.getResultJsp();
				}
			}
			
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 사용여부 체크할 Vo 목록을 리턴 */
	private List<CommonVo> getUsageCheckVoList(String clsCd) throws Exception {
		
		CommonVo commonVo;
		List<CommonVo> checkVoList = new ArrayList<CommonVo>();
		// 코드의 카테고리 조회
		PtCdBVo storedPtCdBVo, ptCdBVo = new PtCdBVo();
		ptCdBVo.setCd(clsCd);
		ptCdBVo.setFldYn("Y");
		storedPtCdBVo = (PtCdBVo)commonSvc.queryVo(ptCdBVo);
		
		int p;
		String refVa2, voNm, tabId, attrNm = StringUtil.toCamelNotation(clsCd, false), newAttrNm=null;
		refVa2 = storedPtCdBVo==null ? null : storedPtCdBVo.getRefVa2();
		
		// refVa2 : 사용여부체크 테이블이 있으면 - tab.col or col
		if(refVa2!=null && !refVa2.isEmpty()){
			String[] tables = refVa2.split(",");
			for(int i=0;i<tables.length;i++){
				if(tables[i]!=null && !tables[i].isEmpty()){
					// tab.col 이면 테이블명과 컬럼명 분리 - 아니면 카테고리ID가 컬럼명
					if((p=tables[i].indexOf('.'))>0){
						tabId = tables[i].substring(0, p).trim();
						newAttrNm = StringUtil.toCamelNotation(tables[i].substring(p+1), false);
					} else {
						tabId = tables[i].trim();
						newAttrNm = attrNm;
					}
					if(!tabId.isEmpty() && !newAttrNm.isEmpty()){
						try{
							// vo를 생성하고 checkVoList에 더함
							voNm = "com.innobiz.orange.web."+tabId.substring(0, 2).toLowerCase()+".vo."+StringUtil.toCamelNotation(tabId, true)+"Vo";
							commonVo = (CommonVo)Class.forName(voNm).newInstance();
							commonVo.setSchCat(newAttrNm);//조사할 attribute id 세팅
							checkVoList.add(commonVo);
						} catch(Exception ignore){}
					}
				}
			}
		}
		
		if(checkVoList.isEmpty()){
			return null;
		}
		return checkVoList;
	}
	/** 코드의 사용유무 검사 */
	private void checkCdUsage(List<CommonVo> checkVoList, String value, PtCdBVo ptCdBVo, HttpServletRequest request) throws CmException {
		int i, size = checkVoList.size(), count;
		CommonVo commonVo;
		for(i=0;i<size;i++){
			commonVo = checkVoList.get(i);
			if(VoUtil.setValue(commonVo, commonVo.getSchCat(), value)){
				count = 0;
				try{
					count = commonSvc.count(commonVo);
				} catch(Exception ignore){
					ignore.printStackTrace();
				}
				if(count>0){
					//pt.msg.not.del.cd.inUse=사용중인 코드는 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.del.cd.inUse", request)
							+ " -  code:"+ptCdBVo.getCd()+"  value:"+ptCdBVo.getRescNm();
					LOGGER.error(msg);
					throw new CmException(msg);
				}
			}
		}
	}
	
	/** [AJAX] - 하위 폴더/코드 갯수 조회 */
	@RequestMapping(value = "/pt/adm/cd/getChildCountAjx")
	public String getChildCountAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		//String langTypCd = LoginSession.getLangTypCd(request);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String clsCd = (String)jsonObject.get("clsCd");
		String fldYn = (String)jsonObject.get("fldYn");
		
		// 코드기본(PT_CD_B) 테이블
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setClsCd(clsCd);
		ptCdBVo.setFldYn(fldYn);// 폴더가 아닌 자식노드 갯수 조회
		Integer count = commonSvc.count(ptCdBVo);
		
		model.put("count", count);
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - (폴더,코드) 삭제 */
	@RequestMapping(value = "/pt/adm/cd/transCdDelAjx")
	public String transCdDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		JSONArray jsonArray = (JSONArray)jsonObject.get("cds");
		String cd, clsCd = (String)jsonObject.get("clsCd");
		
		// 코드기본(PT_CD_B) 테이블 - SELECT
		PtCdBVo ptCdBVo = new PtCdBVo(), storedPtCdBVo;
		ptCdBVo.setClsCd(clsCd);
		List<PtCdBVo> list;
		
		PtRescBVo ptRescBVo;
		
		String msg;
		for(int i=0;i<jsonArray.size();i++){
			
			cd = (String)jsonArray.get(i);
			storedPtCdBVo = ptCmSvc.getCd(clsCd, langTypCd, cd);
			
			if("ROOT".equals(cd) || (storedPtCdBVo!=null && "Y".equals(storedPtCdBVo.getSysCdYn()))){
				//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.
				msg = messageProperties.getMessage("pt.msg.not.del.cd.sys", request);
				model.put("message", msg);
				LOGGER.error(msg+"  - clsCd:"+clsCd+" cd:"+cd);
				return JsonUtil.returnJson(model);
			}
			
			// 폴더일 경우
			if("Y".equals(storedPtCdBVo.getFldYn())){
				
				// 자식이 있으면
				list = ptCmSvc.getAllCdList(cd, langTypCd);
				if(list!=null && list.size()>0){
					//pt.msg.not.del.fld.withChild=자식폴더 또는 자식코드가 있는 폴더는 지울수가 없습니다.
					msg = messageProperties.getMessage("pt.msg.not.del.fld.withChild", request);
					model.put("message", msg);
					LOGGER.error(msg+"  - clsCd:"+clsCd+" cd:"+cd);
					return JsonUtil.returnJson(model);
				}
			}
			
			// 리소스기본(PT_RESC_B) 테이블 - 삭제
			if(storedPtCdBVo.getRescId()!=null && !storedPtCdBVo.getRescId().isEmpty()){
				ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(storedPtCdBVo.getRescId());
				queryQueue.delete(ptRescBVo);
			}
			
			// 코드기본(PT_CD_B) 테이블 - 삭제
			ptCdBVo = new PtCdBVo();
			ptCdBVo.setClsCd(clsCd);
			ptCdBVo.setCd(cd);
			queryQueue.delete(ptCdBVo);
			
			// 삭제된 것보다 큰 sortOrder 하나씩 줄임
			PtxSortOrdrChnVo ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			ptxSortOrdrChnVo.setTabId("PT_CD_B");
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setPkCol("CLS_CD");
			ptxSortOrdrChnVo.setPk(clsCd);
			ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedPtCdBVo.getSortOrdr()));
			queryQueue.update(ptxSortOrdrChnVo);
		}
		
		if(!queryQueue.isEmpty()){
			try {
				
//				// 세션 사용자 정보
//				UserVo userVo = LoginSession.getUser(request);
//				// 회사별 언어 설정
//				List<PtCdBVo> langCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
				
			} catch(Exception e){
				//pt.msg.not.delCd.withUse=사용중인 코드는 지울수가 없습니다.
				msg = messageProperties.getMessage("pt.msg.not.delCd.withUse", request);
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
	
	
	/** [AJAX] - (폴더,코드) 위로이동, 아래로 이동 */
	@RequestMapping(value = "/pt/adm/cd/transCdMoveAjx")
	public String transCdMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		JSONArray jsonArray = (JSONArray)jsonObject.get("cds");
		String cd = null;
		String clsCd = (String)jsonObject.get("clsCd");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(clsCd==null || direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(clsCd==null) LOGGER.error("Code move(up/down) - clsCd==null : "+msg);
			if(direction==null) LOGGER.error("Code move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		} else {
			
			PtCdBVo ptCdBVo = new PtCdBVo(), storedPtCdBVo;
			
			// 위로 이동
			if("up".equals(direction)){
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, stdOrdr=1, switchOrdr;
				ptCdBVo = new PtCdBVo();
				
				for(int i=0;i<jsonArray.size();i++){
					cd = (String)jsonArray.get(i);
					
					ptCdBVo.setClsCd(clsCd);
					ptCdBVo.setCd(cd);
					
					storedPtCdBVo = (PtCdBVo)commonSvc.queryVo(ptCdBVo);
					curOrdr = Integer.valueOf(storedPtCdBVo.getSortOrdr());
					
					if(stdOrdr==curOrdr){
						stdOrdr++;
						continue;
					}
					switchOrdr = curOrdr-1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("PT_CD_B");
					ptxSortOrdrChnVo.setPkCol("CLS_CD");
					ptxSortOrdrChnVo.setPk(clsCd);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					storedPtCdBVo = new PtCdBVo();
					storedPtCdBVo.setClsCd(clsCd);
					storedPtCdBVo.setCd(cd);
					storedPtCdBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(storedPtCdBVo);
				}
				
				if(!queryQueue.isEmpty()){
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
//					// 세션 사용자 정보
//					UserVo userVo = LoginSession.getUser(request);
//					// 회사별 언어 설정
//					List<PtCdBVo> langCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);

					// 캐쉬 삭제
					String dbTime = ptCacheExpireSvc.getDbTime();
					ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
					commonSvc.execute(queryQueue);
					ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
					
					// 코드의 순서 조회
					List<PtCdBVo> ptCdBVolist = ptCmSvc.getAllCdList(clsCd, langTypCd);
					List<String> cdList = new ArrayList<String>();
					int i, size = ptCdBVolist==null ? 0 : ptCdBVolist.size();
					for(i=0;i<size;i++){
						cdList.add(ptCdBVolist.get(i).getCd());
					}
					
					model.put("cds", cdList);
					
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				return JsonUtil.returnJson(model);
				
			// 아래로 이동
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_CD_B");
				ptxSortOrdrChnVo.setPkCol("CLS_CD");
				ptxSortOrdrChnVo.setPk(clsCd);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				// curOrdr - 현재순번
				// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
				Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
				ptCdBVo = new PtCdBVo();
				
				for(int i=jsonArray.size()-1;i>=0;i--){
					cd = (String)jsonArray.get(i);
					
					ptCdBVo.setClsCd(clsCd);
					ptCdBVo.setCd(cd);
					
					storedPtCdBVo = (PtCdBVo)commonSvc.queryVo(ptCdBVo);
					curOrdr = Integer.valueOf(storedPtCdBVo.getSortOrdr());
					
					if(stdOrdr==curOrdr){
						stdOrdr--;
						continue;
					}
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("PT_CD_B");
					ptxSortOrdrChnVo.setPkCol("CLS_CD");
					ptxSortOrdrChnVo.setPk(clsCd);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					storedPtCdBVo = new PtCdBVo();
					storedPtCdBVo.setClsCd(clsCd);
					storedPtCdBVo.setCd(cd);
					storedPtCdBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(storedPtCdBVo);
				}
				
				if(!queryQueue.isEmpty()){
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
//					// 세션 사용자 정보
//					UserVo userVo = LoginSession.getUser(request);
//					// 회사별 언어 설정
//					List<PtCdBVo> langCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);

					// 캐쉬 삭제
					String dbTime = ptCacheExpireSvc.getDbTime();
					ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
					commonSvc.execute(queryQueue);
					ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
					
					// 코드의 순서 조회
					List<PtCdBVo> ptCdBVolist = ptCmSvc.getAllCdList(clsCd, langTypCd);
					List<String> cdList = new ArrayList<String>();
					int i, size = ptCdBVolist==null ? 0 : ptCdBVolist.size();
					for(i=0;i<size;i++){
						cdList.add(ptCdBVolist.get(i).getCd());
					}
					
					model.put("cds", cdList);
					
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
	}
	
	/** [팝업] 관리범위 설정 */
	@RequestMapping(value = "/pt/adm/cd/setMngCompPop")
	public String setMngCompPop(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			ModelMap model) throws Exception {
		
		Map<String, String> map = ptSysSvc.getSysSetupMap(PtConstant.PT_MNG_COMP, true);
		String setupVa = map==null ? null : map.get(setupId);
		model.put("setupVa", setupVa==null ? "" : setupVa);
		
		return LayoutUtil.getJspPath("/pt/adm/cd/setMngCompPop");
	}
	
	/** [히든프레임] 관리범위 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/cd/transMngComp")
	public String transMngComp(HttpServletRequest request,
			@Parameter(name="setupId", required=false) String setupId,
			@Parameter(name="setupVa", required=false) String setupVa,
			ModelMap model) throws Exception {
		
		try{
			QueryQueue queryQueue = new QueryQueue();
			
			PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(PtConstant.PT_MNG_COMP);
			ptSysSetupDVo.setSetupId(setupId);
			ptSysSetupDVo.setSetupVa(setupVa==null ? "" : setupVa);
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
	//transMngComp
}
