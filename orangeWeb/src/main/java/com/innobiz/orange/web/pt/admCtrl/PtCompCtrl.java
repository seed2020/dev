package com.innobiz.orange.web.pt.admCtrl;

import java.sql.SQLException;
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
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.PushSyncSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtAuthGrpBVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpUserRVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtCompLangRVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 회사정보 관리, 회사별 환경설정 관리 */
@Controller
public class PtCompCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtCompCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
    private MessageProperties messageProperties;

	/** 조직도 사용자 Push 방식 동기화 서비스 */
	@Autowired
	private PushSyncSvc pushSyncSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
//	
	/** 회사목록조회 */
	@RequestMapping(value = "/pt/adm/comp/listComp")
	public String listComp(HttpServletRequest request,
			@RequestParam(value = "schWord", required = false) String schWord,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사 목록 조회
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(schWord, null, langTypCd);
		request.setAttribute("ptCompBVoList", ptCompBVoList);
		
		// 회사별 사용 언어 세팅
		if(ptCompBVoList!=null){
			for(PtCompBVo ptCompBVo : ptCompBVoList){
				model.put("lang"+ptCompBVo.getCompId(),
						ptCmSvc.getLangTypCdListByCompId(ptCompBVo.getCompId(), langTypCd));
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/comp/listComp");
	}
	
	/** 회사상세조회 - 설정용 */
	@RequestMapping(value = "/pt/adm/comp/setCompPop")
	public String setCompPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사기본(PT_COMP_B) 테이블 조회
		PtCompBVo ptCompBVo = null;
		if(compId!=null && !compId.isEmpty()){
			ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		}
		
		// 언어코드 조회
		List<PtCdBVo> allLangTypCdList = ptCmSvc.getAllCdList("LANG_TYP_CD", langTypCd);
		model.put("langTypCdList", allLangTypCdList);
		
		// 저장된 회사가 있을 경우
		if(ptCompBVo!=null){
			
			model.put("ptCompBVo", ptCompBVo);
			
			// 회사별로 설정된 언어 정보 조회
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
			if(langTypCdList!=null){
				for(PtCdBVo ptCdBVo2 : langTypCdList){
					model.put("lang_"+ptCdBVo2.getCd(), ptCdBVo2.getCd());
				}
			}
			
			// 회사명 언어별 리소스 조회
			// 리소스상세(PT_RESC_D) 테이블 조회
			ptRescSvc.queryRescDVo(compId, ptCompBVo.getRescId(), model);
			
		} else { // 신규회사 - 회사기본(PT_COMP_B) 테이블에 데이터가 없으면
			
			String maxCompId = null;
			
			// 이미 등록된 회사 목록 조회
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
			if(ptCompBVoList != null){
				for(PtCompBVo ptCompBVo2 : ptCompBVoList){
					if(ptCompBVo2.getCompId()!=null && !ptCompBVo2.getCompId().isEmpty()){
						if(maxCompId==null){
							maxCompId = ptCompBVo2.getCompId();
						} else {
							if(ptCompBVo2.getCompId().compareTo(maxCompId) > 0){
								maxCompId = ptCompBVo2.getCompId();
							}
						}
					}
				}
			}
			
			ptCompBVo = new PtCompBVo();
			ptCompBVo.setCompId(getNextCompId(maxCompId));
			model.put("ptCompBVo", ptCompBVo);
			
			if(allLangTypCdList!=null && !allLangTypCdList.isEmpty()){
				String defaultLangCd = allLangTypCdList.get(0).getCd();
				// 신규 생성의 경우 - 디폴트로 첫번째꺼 세팅
				model.put("lang_"+defaultLangCd, defaultLangCd);
			}
			
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("licenseByCompEnable"))){//회사별 라이센스 사용 여부
			model.put("licenseByCompEnable", Boolean.TRUE);
		}
		if("Y".equals(sysPlocMap.get("affiliatesEnable"))){//계열사 사용
			model.put("affiliatesEnable", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/comp/setCompPop");
	}
	
	/** 회사상세조회 - 설정용 */
	@RequestMapping(value = "/pt/adm/comp/setAffiliatesPop")
	public String setAffiliatesPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사기본(PT_COMP_B) 테이블 조회
		if(compId!=null && !compId.isEmpty()){
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			if(ptCompBVo != null){
				model.put("affiliateIds", ptCompBVo.getAffiliateIds());
			}
		}
		
		model.put("compList", ptCmSvc.getFilteredCompList(null, null, langTypCd));
		
		return LayoutUtil.getJspPath("/pt/adm/comp/setAffiliatesPop");
	}
	
	/** 다음 순서의 compId 구하기 */
	private String getNextCompId(String compId){
		if(compId==null || compId.isEmpty()) return "A01";
		if(compId.length()==1) return Character.toString((char)(compId.charAt(0)+1));
		char[] chars = compId.toCharArray();
		int index = chars.length-1;
		
		while(index>=0){
			if(index==0){
				if(chars[index]>='A' && chars[index]<'Z'){
					chars[index]++;
					return new String(chars);
				} else {
					throw new IllegalArgumentException("No compId next of '"+compId+"'");
				}
			} else {
				if(chars[index]>='0' && chars[index]<'9'){
					chars[index]++;
					return new String(chars);
				} else if(chars[index]=='9'){
					chars[index]='0';
					index--;
				} else if(chars[index]>='A' && chars[index]<'Z'){//이전 로직 호환성
					chars[index]='0';
					index--;
				} else {
					throw new IllegalArgumentException("No compId next of '"+compId+"'");
				}
			}
		}
		
		throw new IllegalArgumentException("No compId next of '"+compId+"'");
	}
//	private String getNextCompId(String compId){
//		if(compId==null || compId.isEmpty()) return "A01";
//		if(compId.length()==1) return Character.toString((char)(compId.charAt(0)+1));
//		char[] chars = compId.toCharArray();
//		int index = chars.length-1;
//		
//		while(index>=0){
//			if(		(chars[index]>='0' && chars[index]<'9')
//					||	(chars[index]>='A' && chars[index]<'Z')){
//				chars[index]++;
//				return new String(chars);
//			} else if(chars[index]=='9'){
//				chars[index] = 'A';
//				return new String(chars);
//			} else if(chars[index]=='Z'){
//				chars[index]='0';
//				index--;
//			}
//		}
//		
//		throw new IllegalArgumentException("No compId next of '"+compId+"'");
//	}
	
	/** 회사저장 */
	@RequestMapping(value = "/pt/adm/comp/transComp")
	public String transComp(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{

			// 회사ID
			String compId = request.getParameter("compId");
			// 회사별 언어
			String[] langTypCds = request.getParameterValues("langTypCd");
			
			// 리소스상세(PT_RESC_D) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescDVo ptRescDVo = ptRescSvc.collectPtRescDVo(request, null, langTypCds, queryQueue);
			if(ptRescDVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 회사기본(PT_COMP_B) 테이블 - SELECT
			PtCompBVo storedPtCompBVo = new PtCompBVo();
			storedPtCompBVo.setCompId(compId);
			storedPtCompBVo = (PtCompBVo)commonSvc.queryVo(storedPtCompBVo);
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 회사기본(PT_COMP_B) 테이블 - UPDATE or INSERT
			PtCompBVo ptCompBVo = new PtCompBVo();
			VoUtil.bind(request, ptCompBVo);
			ptCompBVo.setCompNm(ptRescDVo.getRescVa());
			ptCompBVo.setRescId(ptRescDVo.getRescId());
			ptCompBVo.setModDt("sysdate");
			ptCompBVo.setModrUid(userVo.getUserUid());
			
			if(storedPtCompBVo==null){
				ptCompBVo.setDelYn("N");
				ptCompBVo.setRegDt("sysdate");
				ptCompBVo.setRegrUid(userVo.getUserUid());
				queryQueue.insert(ptCompBVo);
				
				setCompRelated(compId, langTypCds, userVo, queryQueue);
				
				// 관리자 메뉴 세팅
				addAdmMnu(compId, queryQueue);
			} else {
				queryQueue.update(ptCompBVo);
			}
			
			// 회사언어관계(PT_COMP_LANG_R) 테이블 VO
			PtCompLangRVo ptCompLangRVo;
			
			// 회사언어관계(PT_COMP_LANG_R) 테이블 - DELETE
			ptCompLangRVo = new PtCompLangRVo();
			if(storedPtCompBVo!=null){
				ptCompLangRVo.setCompId(ptCompBVo.getCompId());
				queryQueue.delete(ptCompLangRVo);
			}
			
			// 회사언어관계(PT_COMP_LANG_R) 테이블 - INSERT
			for(int i=0;i<langTypCds.length;i++){
				if(langTypCds[i]!=null && !langTypCds[i].isEmpty()){
					ptCompLangRVo = new PtCompLangRVo();
					ptCompLangRVo.setCompId(ptCompBVo.getCompId());
					ptCompLangRVo.setLangTypCd(langTypCds[i]);
					queryQueue.insert(ptCompLangRVo);
				}
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.COMP, CacheConfig.COMP_LANG);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.COMP, CacheConfig.COMP_LANG);
			
			// 사용자 조직도 동기화 PUSH - 회사
			if(pushSyncSvc.hasSync()){
				pushSyncSvc.syncComp();
			}
			
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
	
	/** 회사 관련 초기화 - 권한 */
	private void setCompRelated(String compId, String[] langTypCds, UserVo userVo, QueryQueue queryQueue) throws SQLException{
		
		PtRescDVo ptRescDVo, mainPtRescDVo = null;
		PtAuthGrpBVo ptAuthGrpBVo;
		String rescId = ptCmSvc.createId("PT_RESC_D");
		
		// 모든 사용자
		for(String langTypCd : langTypCds){
			ptRescDVo = new PtRescDVo();
			ptRescDVo.setCompId(compId);
			ptRescDVo.setRescId(rescId);
			ptRescDVo.setLangTypCd(langTypCd);
			if("ko".equals(langTypCd)){
				ptRescDVo.setRescVa("모든 사용자");
				mainPtRescDVo = ptRescDVo;
			} else {
				ptRescDVo.setRescVa("ALL USER");
			}
			if(mainPtRescDVo==null) {
				mainPtRescDVo = ptRescDVo;
			}
			queryQueue.store(ptRescDVo);
		}
		
		ptAuthGrpBVo = new PtAuthGrpBVo();
		ptAuthGrpBVo.setCompId(compId);
		ptAuthGrpBVo.setAuthGrpTypCd("G");
		ptAuthGrpBVo.setAuthGrpId(PtConstant.AUTH_USERS);//PtConstant.AUTH_USERS
		ptAuthGrpBVo.setRescId(mainPtRescDVo.getRescId());
		ptAuthGrpBVo.setAuthGrpNm(mainPtRescDVo.getRescVa());
		ptAuthGrpBVo.setAuthGrpCatCd("bySys");
		ptAuthGrpBVo.setSysAuthGrpYn("Y");
		ptAuthGrpBVo.setUseYn("Y");
		ptAuthGrpBVo.setRegrUid(userVo.getUserUid());
		ptAuthGrpBVo.setRegDt("sysdate");
		queryQueue.store(ptAuthGrpBVo);
		
		// 관리자
		rescId = ptCmSvc.createId("PT_RESC_D");
		for(String langTypCd : langTypCds){
			ptRescDVo = new PtRescDVo();
			ptRescDVo.setCompId(compId);
			ptRescDVo.setRescId(rescId);
			ptRescDVo.setLangTypCd(langTypCd);
			if("ko".equals(langTypCd)){
				ptRescDVo.setRescVa("관리자 그룹");
				mainPtRescDVo = ptRescDVo;
			} else {
				ptRescDVo.setRescVa("Admin Group");
			}
			if(mainPtRescDVo==null) {
				mainPtRescDVo = ptRescDVo;
			}
			queryQueue.store(ptRescDVo);
		}
		
		ptAuthGrpBVo = new PtAuthGrpBVo();
		ptAuthGrpBVo.setCompId(compId);
		ptAuthGrpBVo.setAuthGrpTypCd("A");
		ptAuthGrpBVo.setAuthGrpId(PtConstant.AUTH_ADMIN);
		ptAuthGrpBVo.setRescId(mainPtRescDVo.getRescId());
		ptAuthGrpBVo.setAuthGrpNm(mainPtRescDVo.getRescVa());
		ptAuthGrpBVo.setAuthGrpCatCd("bySys");
		ptAuthGrpBVo.setSysAuthGrpYn("Y");
		ptAuthGrpBVo.setUseYn("Y");
		ptAuthGrpBVo.setRegrUid(userVo.getUserUid());
		ptAuthGrpBVo.setRegDt("sysdate");
		queryQueue.store(ptAuthGrpBVo);
	}
	
	
	/** [AJAX] 회사순서변경 */
	@RequestMapping(value = "/pt/adm/comp/transCompMoveAjx")
	public String transCompMoveAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		JSONArray jsonArray = (JSONArray)jsonObject.get("compIds");
		
		// 쿼리의 일괄 SQL 실행을 위한  Vo 보관 객체
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		String compId, direction = (String)jsonObject.get("direction");
		PtCompBVo ptCompBVo, storedPtCompBVo;
		
		// 위로 이동
		if("up".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			ptCompBVo = new PtCompBVo();
			for(int i=0;i<jsonArray.size();i++){
				compId = (String)jsonArray.get(i);
				
				ptCompBVo.setCompId(compId);
				storedPtCompBVo = (PtCompBVo)commonSvc.queryVo(ptCompBVo);
				curOrdr = Integer.valueOf(storedPtCompBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				// 옮겨 갈곳의 순서를 내림
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_COMP_B");
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				// 선택된 것의 순서를 올림
				storedPtCompBVo = new PtCompBVo();
				storedPtCompBVo.setCompId(compId);
				storedPtCompBVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtCompBVo);
				
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.COMP, CacheConfig.COMP_LANG);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.COMP, CacheConfig.COMP_LANG);
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
				List<String> compIdList = new ArrayList<String>();
				int i, size = ptCompBVoList==null ? 0 : ptCompBVoList.size();
				for(i=0;i<size;i++){
					compIdList.add(ptCompBVoList.get(i).getCompId());
				}
				
				model.put("compIds", compIdList);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			
			return JsonUtil.returnJson(model);
			
		// 아래로 이동
		} else if("down".equals(direction)){
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_COMP_B");
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			ptCompBVo = new PtCompBVo();
			for(int i=jsonArray.size()-1;i>=0;i--){
				compId = (String)jsonArray.get(i);
				
				ptCompBVo.setCompId(compId);
				storedPtCompBVo = (PtCompBVo)commonSvc.queryVo(ptCompBVo);
				curOrdr = Integer.valueOf(storedPtCompBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				// 옮겨갈 곳의 순서를 올림
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_COMP_B");
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				// 선택된 것의 순서를 내림
				storedPtCompBVo = new PtCompBVo();
				storedPtCompBVo.setCompId(compId);
				storedPtCompBVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtCompBVo);
				
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.COMP, CacheConfig.COMP_LANG);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.COMP, CacheConfig.COMP_LANG);
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
				List<String> compIdList = new ArrayList<String>();
				int i, size = ptCompBVoList==null ? 0 : ptCompBVoList.size();
				for(i=0;i<size;i++){
					compIdList.add(ptCompBVoList.get(i).getCompId());
				}
				
				model.put("compIds", compIdList);
				
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
	
//	/** [AJAX] 회사의 기본 관리자 메뉴 설정 */
//	@RequestMapping(value = "/pt/adm/comp/transInitAdmMnuAjx")
//	public String transInitAdmMnuAjx(HttpServletRequest request,
//			@Parameter(name="data", required=false)String data,
//			ModelMap model) throws Exception {
//		
//		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
//		ptMnuGrpBVo.setMnuGrpMdCd("A");//관리자 메뉴그룹
//		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
//		@SuppressWarnings("unchecked")
//		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
//		
//		PtMnuLoutDVo ptMnuLoutDVo;
//		Integer sortOrdr;
//		
//		QueryQueue queryQueue = new QueryQueue();
//		
//		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
//		JSONArray jsonArray = (JSONArray)jsonObject.get("compIds");
//		String compId;
//		
//		int i, size = jsonArray==null ? 0 : jsonArray.size(), j, jsize;
//		for(i=0;i<size;i++){
//			compId = (String)jsonArray.get(i);
//			
//			ptMnuLoutDVo = new PtMnuLoutDVo();
//			ptMnuLoutDVo.setCompId(compId);
//			ptMnuLoutDVo.setLoutCatId("A");
//			queryQueue.delete(ptMnuLoutDVo);
//			
//			sortOrdr = 1;
//			jsize = ptMnuGrpBVoList==null ? 0 : ptMnuGrpBVoList.size();
//			for(j=0;j<jsize;j++){
//				ptMnuGrpBVo = ptMnuGrpBVoList.get(j);
//				
//				ptMnuLoutDVo = new PtMnuLoutDVo();
//				ptMnuLoutDVo.setCompId(compId);
//				ptMnuLoutDVo.setMnuLoutId(ptCmSvc.createId("PT_MNU_LOUT_D"));
//				ptMnuLoutDVo.setLoutCatId("A");
//				ptMnuLoutDVo.setMnuLoutPid("adm");
//				ptMnuLoutDVo.setLoutLocCd("adm");
//				ptMnuLoutDVo.setMnuLoutKndCd("G");
//				ptMnuLoutDVo.setMnuGrpId(ptMnuGrpBVo.getMnuGrpId());
//				ptMnuLoutDVo.setMnuGrpRescId(ptMnuGrpBVo.getRescId());
//				ptMnuLoutDVo.setMnuLoutNm(ptMnuGrpBVo.getRescNm());
//				ptMnuLoutDVo.setSortOrdr(sortOrdr.toString());
//				sortOrdr++;
//				queryQueue.insert(ptMnuLoutDVo);
//			}
//		}
//		
//		// 캐쉬 삭제
//		String dbTime = ptCacheExpireSvc.getDbTime();
//		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
//		commonSvc.execute(queryQueue);
//		ptCacheExpireSvc.checkNow(CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
//		
//		//cm.msg.setting.success=설정 되었습니다.
//		model.put("message", messageProperties.getMessage("cm.msg.setting.success", request));
//		
//		return JsonUtil.returnJson(model);
//	}
	
	/** 관리자 메뉴를 더함 */
	private void addAdmMnu(String compId, QueryQueue queryQueue) throws SQLException {
		
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setMnuGrpMdCd("A");//관리자 메뉴그룹
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
		
		PtMnuLoutDVo ptMnuLoutDVo;
		Integer sortOrdr = 1;
		
		int i, size = ptMnuGrpBVoList==null ? 0 : ptMnuGrpBVoList.size();
		for(i=0;i<size;i++){
			ptMnuGrpBVo = ptMnuGrpBVoList.get(i);
			
			ptMnuLoutDVo = new PtMnuLoutDVo();
			ptMnuLoutDVo.setCompId(compId);
			ptMnuLoutDVo.setMnuLoutId(ptCmSvc.createId("PT_MNU_LOUT_D"));
			ptMnuLoutDVo.setLoutCatId("A");
			ptMnuLoutDVo.setMnuLoutPid("adm");
			ptMnuLoutDVo.setLoutLocCd("adm");
			ptMnuLoutDVo.setMnuLoutKndCd("G");
			ptMnuLoutDVo.setMnuGrpId(ptMnuGrpBVo.getMnuGrpId());
			ptMnuLoutDVo.setMnuGrpRescId(ptMnuGrpBVo.getRescId());
			ptMnuLoutDVo.setMnuLoutNm(ptMnuGrpBVo.getRescNm());
			ptMnuLoutDVo.setSortOrdr(sortOrdr.toString());
			sortOrdr++;
			queryQueue.insert(ptMnuLoutDVo);
		}
		
	}

	/** [AJAX] 회사 관리자 조회 */
	@RequestMapping(value = "/pt/adm/comp/getCompAdmAjx")
	public String getCompAdmAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		model.put("userUids", getAdminUids(compId, PtConstant.AUTH_ADMIN));
		return LayoutUtil.returnJson(model);
	}
	
	/** 관리자 UID 조회 */
	private String getAdminUids(String compId, String authGrpId) throws Exception{
		
		StringBuilder uidBuilder = new StringBuilder(128);
		boolean first = true;
		
		//권한그룹사용자관계(PT_AUTH_GRP_USER_R) 테이블
		PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
		ptAuthGrpUserRVo.setCompId(compId);
		ptAuthGrpUserRVo.setAuthGrpId(authGrpId);
		ptAuthGrpUserRVo.setExcliYn("N");
		
		@SuppressWarnings("unchecked")
		List<PtAuthGrpUserRVo> ptAuthGrpUserRVoList = (List<PtAuthGrpUserRVo>)commonSvc.queryList(ptAuthGrpUserRVo);
		if(ptAuthGrpUserRVoList!=null){
			for(PtAuthGrpUserRVo storedPtAuthGrpUserRVo : ptAuthGrpUserRVoList){
				if(first) first = false;
				else uidBuilder.append(',');
				uidBuilder.append(storedPtAuthGrpUserRVo.getUserUid());
			}
		}
		
		return uidBuilder.toString();
	}
	
	/** [AJAX] 회사 관리자 설정 */
	@RequestMapping(value = "/pt/adm/comp/transCompAdmAjx")
	public String transCompAdmAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		String userUids = (String)jsonObject.get("userUids");
		
		PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
		ptAuthGrpUserRVo.setCompId(compId);
		ptAuthGrpUserRVo.setAuthGrpId(PtConstant.AUTH_ADMIN);
		queryQueue.delete(ptAuthGrpUserRVo);
		
		for(String userUid : userUids.split(",")){
			ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
			ptAuthGrpUserRVo.setCompId(compId);
			ptAuthGrpUserRVo.setAuthGrpId(PtConstant.AUTH_ADMIN);
			ptAuthGrpUserRVo.setUserUid(userUid);
			ptAuthGrpUserRVo.setExcliYn("N");
			ptAuthGrpUserRVo.setAuthGrpTypCd("A");
			queryQueue.insert(ptAuthGrpUserRVo);
		}
		
		commonSvc.execute(queryQueue);
		
		//cm.msg.setting.success=설정 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.setting.success", request));
		
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 시스템 관리자 조회 */
	@RequestMapping(value = "/pt/adm/comp/getSysAdmAjx")
	public String getSysAdmAjx(HttpServletRequest request,
			ModelMap model) throws Exception {
		model.put("userUids", getAdminUids(PtConstant.SYS_COMP_ID, PtConstant.AUTH_SYS_ADMIN));
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX] 시스템 관리자 설정 */
	@RequestMapping(value = "/pt/adm/comp/transSysAdmAjx")
	public String transSysAdmAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String userUids = (String)jsonObject.get("userUids");
		
		QueryQueue queryQueue = new QueryQueue();
		
		PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
		ptAuthGrpUserRVo.setCompId(PtConstant.SYS_COMP_ID);
		ptAuthGrpUserRVo.setAuthGrpId(PtConstant.AUTH_SYS_ADMIN);
		ptAuthGrpUserRVo.setExcliYn("N");
		queryQueue.delete(ptAuthGrpUserRVo);
		
		if(userUids!=null && !userUids.isEmpty()){
			
			Integer sortOrdr = 1;
			for(String userUid : userUids.split(",")){
				ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
				ptAuthGrpUserRVo.setCompId(PtConstant.SYS_COMP_ID);
				ptAuthGrpUserRVo.setAuthGrpId(PtConstant.AUTH_SYS_ADMIN);
				ptAuthGrpUserRVo.setExcliYn("N");
				ptAuthGrpUserRVo.setUserUid(userUid);
				ptAuthGrpUserRVo.setAuthGrpTypCd("A");
				ptAuthGrpUserRVo.setSortOrdr(sortOrdr.toString());
				queryQueue.insert(ptAuthGrpUserRVo);
				sortOrdr++;
			}
		}
		
		// 캐쉬 삭제
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AUTH);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.AUTH);
		
		//cm.msg.setting.success=설정 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.setting.success", request));
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 회사 관리자 설정 */
	@RequestMapping(value = "/pt/adm/comp/transCompAffiliatesAjx")
	public String transCompAffiliatesAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		String affiliates = (String)jsonObject.get("affiliates");
		
		// 해당 compId 외에 계열사로 묶을 compId 목록
		List<String> submitedList = null;
		if(affiliates!=null && affiliates.indexOf(',')>0){
			submitedList = new ArrayList<String>();
			for(String submitedId : affiliates.split(",")){
				if(!submitedId.isEmpty() && !submitedId.equals(compId)){
					submitedList.add(submitedId);
				}
			}
			if(submitedList.isEmpty()) submitedList = null;
		}
		
		// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 계열사 설정 조회
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(CacheConfig.COMP_AFFILIATES);
		@SuppressWarnings("unchecked")
		List<PtSysSetupDVo> ptSysSetupDVoList = (List<PtSysSetupDVo>)commonSvc.queryList(ptSysSetupDVo);
		
		int index = 0;
		String setupId, setupVa;
		String[] storedArr;
		
		if(ptSysSetupDVoList!=null){
			// 시스템 설정 삭제
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(CacheConfig.COMP_AFFILIATES);
			queryQueue.delete(ptSysSetupDVo);
			
			StringBuilder builder;
			
			boolean matched = false;
			boolean removed = false;
			boolean first = true;
			List<String> storedList;
			
//			boolean first = true;
			for(PtSysSetupDVo storedPtSysSetupDVo : ptSysSetupDVoList){
				
				setupVa = storedPtSysSetupDVo.getSetupVa();
				if(setupVa==null || setupVa.isEmpty()) continue;
				
				// 해당 compId 가 포함된 설정이 있는지
				matched = false;
				storedArr = setupVa.split(",");
				for(String storedId : storedArr){
					if(storedId.equals(compId)){
						matched = true;
						break;
					}
				}
				
				// 해당 compId 가 포함된 설정이 있으면 삭제함  - 다시 저장안함
				if(matched) continue;
				
				// 해당 compId 외에 설정할 계열사 compId 가 이미 저장된 설정에 있으면 - 설정할 compId 제거 : 회사의 계열사 설정은 하나만 가능하도록
				if(submitedList!=null){
					
					// 저장된 compId 가   submit 된 compId 에 있으면 제외하고 나머지 compId 만 저장
					storedList = new ArrayList<String>();
					removed = false;
					for(String storedId : storedArr){
						if(!submitedList.contains(storedId)){
							storedList.add(storedId);
						} else {
							removed = true;
						}
					}
					
					// 하나 이하의 계열사는 서로 연결될게 없는 상태이므로 - 저장 안함
					if(storedList.size()<2){
						continue;
					}
					
					// 제거된 compId 가 있으면 값을 다시 말아 넣음
					if(removed){
						builder = new StringBuilder(64);
						first = true;
						for(String storedId : storedList){
							if(first) first = false;
							else builder.append(',');
							builder.append(storedId);
							
							storedPtSysSetupDVo.setSetupVa(builder.toString());
						}
					}
					
				}
				
				// setupId - 001번 부터 순차적으로 다시 넣음
				index++;
				setupId = index<10 ? "00"+index : index<100 ? "0"+index : Integer.toString(index);
				storedPtSysSetupDVo.setSetupId(setupId);
				
				queryQueue.insert(storedPtSysSetupDVo);
				
			}
		}
		
		// 서밋된것 저장 - 1개 이상일 때만
		if(submitedList!=null){
			
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(CacheConfig.COMP_AFFILIATES);
			
			index++;
			setupId = index<10 ? "00"+index : index<100 ? "0"+index : Integer.toString(index);
			ptSysSetupDVo.setSetupId(setupId);
			
			ptSysSetupDVo.setSetupVa(affiliates);
			queryQueue.insert(ptSysSetupDVo);
			
		}
		
		// 캐쉬 삭제
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.COMP, CacheConfig.COMP_LANG);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.COMP, CacheConfig.COMP_LANG);
		
		//cm.msg.save.success=저장 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("result", "ok");
		
		return JsonUtil.returnJson(model);
	}


	/** [AJAX] 시스템 관리자 조회 */
	@RequestMapping(value = "/pt/adm/comp/transEmailBulkAjx")
	public String transEmailBulkAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		String mailDomain = null;
		
		// 회사기본(PT_COMP_B) 테이블 조회
		if(compId!=null && !compId.isEmpty()){
			
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			mailDomain = ptCompBVo.getMailDomain();
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setCompId(compId);
			orUserBVo.setAduTypCd("01");//01:원직, 02:겸직, 03:파견직 
			
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
			
			OrUserPinfoDVo orUserPinfoDVo;
			if(orUserBVoList != null){
				for(OrUserBVo storedOrUserBVo : orUserBVoList){
					orUserPinfoDVo = new OrUserPinfoDVo();
					orUserPinfoDVo.setOdurUid(storedOrUserBVo.getOdurUid());
					orUserPinfoDVo.setEmailEnc(cryptoSvc.encryptPersanal(storedOrUserBVo.getLginId()+"@"+mailDomain));
					queryQueue.update(orUserPinfoDVo);
				}
			}
			
		}
		
		if(!queryQueue.isEmpty()){
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.USER);
			commonSvc.execute(queryQueue);
			orCmSvc.setUsers();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		} else {
			//cm.msg.nodata.toSave=저장할 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
		}
		return LayoutUtil.returnJson(model);
	}
}
