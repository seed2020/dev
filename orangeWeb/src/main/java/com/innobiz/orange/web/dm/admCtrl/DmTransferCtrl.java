package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocNoSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmTransferSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmTakovrBVo;
import com.innobiz.orange.web.dm.vo.DmTranBVo;
import com.innobiz.orange.web.dm.vo.DmTranProcLVo;
import com.innobiz.orange.web.dm.vo.DmxTranVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** 이관 저장소 컨트롤러 */
@Controller
public class DmTransferCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 문서 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
//	/** 문서 리소스 처리 서비스 */
//	@Autowired
//	private DmRescSvc dmRescSvc;
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서 이관 서비스 */
	@Autowired
	private DmTransferSvc dmTransferSvc;
	
	/** 캐쉬 만료 처리 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "dmDocNoSvc")
	private DmDocNoSvc dmDocNoSvc;
	
	/** 이관 목록 조회 */
	@RequestMapping(value = "/dm/adm/stor/listTran")
	public String listTran(HttpServletRequest request,
			@Parameter(name="delYn", required=false) String delYn,
			@Parameter(name="schWord", required=false) String schWord,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사ID
		String paramCompId = "U0000001".equals(userVo.getUserUid()) ? dmDocSvc.getParamCompId(request, model, userVo, null, true) : userVo.getCompId();
		
		if(!"U0000001".equals(userVo.getUserUid())){
			model.put("paramCompId", paramCompId);
		}
		
		if(paramCompId == null || paramCompId.isEmpty()){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			model.put("messageCode", "cm.msg.noData");
			model.put("todo", "history.go(-1);");
			return LayoutUtil.getResultJsp();
		}
		
		DmTranBVo dmTranBVo = new DmTranBVo();
		dmTranBVo.setCompId(paramCompId);
		dmTranBVo.setQueryLang(langTypCd);
		if(schWord!=null && !schWord.isEmpty()){
			dmTranBVo.setSchCat("storRescNm");
			dmTranBVo.setSchWord(schWord);
		}
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmTranBVo);
		PersonalUtil.setPaging(request, dmTranBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		
		if(recodeCount.intValue()>0){
			dmTranBVo.setOrderBy("TRAN_ID DESC");
			@SuppressWarnings("unchecked")
			List<DmTranBVo> dmTranBVoList = (List<DmTranBVo>)commonSvc.queryList(dmTranBVo);
			
			if(dmTranBVoList != null){
				model.put("dmTranBVoList", dmTranBVoList);
			}
		}
		
		// 진행중인 이관 확인
		HttpSession session = request.getSession();
		String processingTranId = (String)session.getAttribute("DM_TRAN_ID");
		if(processingTranId != null){
			model.put("processingTranId", processingTranId);
			session.removeAttribute("DM_TRAN_ID");
		}
		
		// 시스템 설정 조회 - 문서 이관 허용(시스템관리자)
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("dmTranEnable"))){
			model.put("dmTranEnable", Boolean.TRUE);
		}
		
		// 시스템 차단 조회
		if(ptSysSvc.isSysInHalt()){
			model.put("sysHalt", Boolean.TRUE);
		}
				
		return LayoutUtil.getJspPath("/dm/adm/tran/listTran");
	}
	
	/** 문서 이관 - 팝업 */
	@RequestMapping(value = "/dm/adm/stor/setTranPop")
	public String setTranPop(HttpServletRequest request,
			@Parameter(name="paramCompId", required=false) String paramCompId,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 계정 여부
		boolean admin = "U0000001".equals(userVo.getUserUid());
		if(!admin || (admin && (paramCompId==null || paramCompId.isEmpty()))) paramCompId = userVo.getCompId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		////////////////////////////////////////////////////////
		//
		// 저장소 목록 조회[기본저장소제외]
		model.put("dmStorBVoList", dmStorSvc.getStorList(langTypCd, paramCompId, true, "Y"));
		
		model.put("today", StringUtil.getCurrYmd());
		
		return LayoutUtil.getJspPath("/dm/adm/tran/setTranPop");
	}
	
	/** [AJX] 문서 이관 - 대상 카운트 조회 */
	@RequestMapping(value = "/dm/adm/stor/getTranCntAjx")
	public String getTranCntAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String paramCompId = (String)jsonObject.get("paramCompId");
		String durStrtDt = (String)jsonObject.get("durStrtDt");
		String durEndDt = (String)jsonObject.get("durEndDt");
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
		}
				
		// 문서 - 이관건수 조회
		DmxTranVo dmxTranVo = dmTransferSvc.createTranTgtVo(paramCompId, durStrtDt, durEndDt);
		String langTypCd = LoginSession.getLangTypCd(request);
		dmxTranVo.setQueryLang(langTypCd);
		dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.selectDmxTranCnt");
		dmxTranVo.setSrcTbl(dmStorBVo.getTblNm());
		dmxTranVo.setWhereSqllet("AND T.STAT_CD = 'C'");
		model.put("totalCnt", commonSvc.count(dmxTranVo));
		
		// 심의중인 문서가 있는지 여부
		dmxTranVo.setWhereSqllet("AND T.STAT_CD = 'S'");
		model.put("discCnt", commonSvc.count(dmxTranVo));
		
		// 인수대기 문서가 있는지 여부
		dmxTranVo.setWhereSqllet("AND T.STAT_CD = 'O'");
		model.put("takovrCnt", commonSvc.count(dmxTranVo));
				
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJX] 문서 이관 - 이관 실행 */
	@RequestMapping(value = "/dm/adm/stor/transTranAjx")
	public String transTranAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		// 1. 전달된 데이터 저장 : 이관정보
		// 2. dmTransferSvc.addTranId(tranId) 호출하여 이관 Thread 를 시작 시킴
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String storId = (String)jsonObject.get("storId");
		String paramCompId = (String) jsonObject.get("paramCompId");
		String durStrtDt = (String)jsonObject.get("durStrtDt");
		String durEndDt = (String)jsonObject.get("durEndDt");
		String defragYn = (String)jsonObject.get("defrag");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		if(paramCompId == null || paramCompId.isEmpty()) paramCompId = userVo.getCompId();
		
		JSONArray rescArray = (JSONArray)jsonObject.get("resces");
		
		boolean storAdded = false;
		
		try {
			
			QueryQueue queryQueue = new QueryQueue();
			
			DmStorBVo dmStorBVo = null;

			if(storId != null){
				
				dmStorBVo = new DmStorBVo();
				dmStorBVo.setStorId(storId);
				dmStorBVo = (DmStorBVo)commonSvc.queryVo(dmStorBVo);
				if(dmStorBVo == null){
					//dm.msg.noStorage=해당 저장소를 찾을 수 없습니다.
					throw new CmException("dm.msg.noStorage", locale);
				}
				
			} else {
				
				// 저장소 생성
				dmStorBVo = new DmStorBVo();
				dmStorBVo.setCompId(paramCompId);
				
				// 등록자, 등록일시
				dmStorBVo.setRegrUid(userVo.getUserUid());
				dmStorBVo.setRegDt("sysdate");
				// 저장소ID 생성				
				storId = dmCmSvc.createId("DM_STOR_B");
				dmStorBVo.setStorId(storId);
				// 테이블명 생성
				//dmStorBVo.setTblNm(dmStorSvc.createTblNm(paramCompId));
				dmStorBVo.setUseYn("Y");
				dmStorBVo.setDftYn("N");
				
				// 저장소 기본 리소스 생성
				String rescId = dmCmSvc.createId("DM_RESC_B");
				
				// 저장소 리소스 저장
				DmRescBVo dmRescBVo;
				JSONObject jsonResc;
				int i, size = rescArray.size();
				for(i=0; i<size; i++){
					jsonResc = (JSONObject)rescArray.get(i);
					dmRescBVo = new DmRescBVo();
					dmRescBVo.setStorId(storId);
					dmRescBVo.setRescId(rescId);
					dmRescBVo.setLangTypCd((String)jsonResc.get("langTypCd"));
					dmRescBVo.setRescVa((String)jsonResc.get("rescVa"));
					queryQueue.insert(dmRescBVo);
					if(i==0){
						dmStorBVo.setStorNm(dmRescBVo.getRescVa());						
					}
				}
				dmStorBVo.setRescId(rescId);
				queryQueue.insert(dmStorBVo);
				
				storAdded = true;
			}
			
			String today = StringUtil.getCurrYmd();
			
			String tranId = dmCmSvc.createId("DM_TRAN_B");
			
			// 이관기본(DM_TRAN_B) 테이블 - INSERT
			DmTranBVo dmTranBVo = new DmTranBVo();
			dmTranBVo.setTranId(tranId);
			dmTranBVo.setCompId(paramCompId);
			dmTranBVo.setStorId(storId);
			dmTranBVo.setStorRescId(dmStorBVo.getRescId());
			dmTranBVo.setTranTgtStrtYmd(durStrtDt);
			dmTranBVo.setTranTgtEndYmd(durEndDt==null || durEndDt.isEmpty() ? today : durEndDt);
			dmTranBVo.setTranProcStatCd("preparing");
			dmTranBVo.setDefragYn(defragYn);
			dmTranBVo.setUserUid(userVo.getUserUid());
			queryQueue.insert(dmTranBVo);
			
			if(!storAdded){
				commonSvc.execute(queryQueue);
			} else {
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.STOR);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(DmConstant.STOR);
			}
			
			model.put("result", "ok");
			request.getSession().setAttribute("DM_TRAN_ID", tranId);
			dmTransferSvc.addTranId(tranId);
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [AJX] 재실행 - 이관 실행 */
	@RequestMapping(value = "/dm/adm/stor/transRetransferAjx")
	public String transRetransferAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		dmTransferSvc.addTranId(tranId);
		
		return LayoutUtil.returnJson(model);
	}
	/** [팝업] 이관 진행 내역 */
	@RequestMapping(value = "/dm/adm/stor/listTranProcPop")
	public String listTranProcPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 내용은 스크립트로 Ajax 호출 하여 가져옴
		return LayoutUtil.getJspPath("/dm/adm/tran/listTranProcPop");
	}
	/** [AJX] 이관 진행 내역 */
	@RequestMapping(value = "/dm/adm/stor/getTranProcListAjx")
	public String getTranProcListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		String strtSeq = (String)jsonObject.get("strtSeq");
		
		// 이관기본(DM_TRAN_B) 테이블 - 프로세스 완료여부 조회
		DmTranBVo dmTranBVo = new DmTranBVo();
		dmTranBVo.setTranId(tranId);
		dmTranBVo = (DmTranBVo)commonSvc.queryVo(dmTranBVo);
		if(dmTranBVo != null){
			// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
			String tranProcStatCd = dmTranBVo.getTranProcStatCd();
			if("preparing".equals(tranProcStatCd) || "processing".equals(tranProcStatCd)){
				model.put("completed", "N");
			}
			if("error".equals(tranProcStatCd)){
				model.put("hasError", "Y");
			}
		}
		
		// 이관진행내역(DM_TRAN_PROC_L) 테이블 - 개별 내역 조회
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(tranId);
		if(strtSeq != null && !strtSeq.isEmpty()) dmTranProcLVo.setStrtSeq(strtSeq);
		@SuppressWarnings("unchecked")
		List<DmTranProcLVo> dmTranProcLVoList = (List<DmTranProcLVo>)commonSvc.queryList(dmTranProcLVo);
		if(dmTranProcLVoList != null){
			model.put("procList", dmTranProcLVoList);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX] 이관이력 삭제 */
	@RequestMapping(value = "/dm/adm/stor/transTranDelAjx")
	public String transTranDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)object.get("tranIds"); // 이력ID
			if (jsonArray == null || jsonArray.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			// 회사ID
			String paramCompId = ParamUtil.getRequestParam(request, "paramCompId", false);
			
			if(!admin) paramCompId = userVo.getCompId();
			
			// 테이블 삭제
			QueryQueue queryQueue = new QueryQueue();
			// 문서이관이력VO
			DmTranBVo dmTranBVo = null;
			DmTranProcLVo dmTranProcLVo = null;
			String tranId = null;
			for(int i=0;i<jsonArray.size();i++){
				tranId = (String)jsonArray.get(i);
				// 이관기본
				dmTranBVo = new DmTranBVo();
				dmTranBVo.setTranId(tranId);
				dmTranBVo.setCompId(paramCompId);
				if(commonSvc.count(dmTranBVo)==0) continue;
				// 이관진행내역
				dmTranProcLVo = new DmTranProcLVo();
				dmTranProcLVo.setTranId(tranId);
				queryQueue.delete(dmTranProcLVo);
				
				queryQueue.delete(dmTranBVo);
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 심의 - 승인,반려 (일괄) */
	@RequestMapping(value = "/dm/adm/stor/transDiscDocAllAjx")
	public String transDiscDocAllAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String statCd = (String) object.get("statCd");
			if ( statCd == null || statCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			String paramCompId = (String) object.get("paramCompId");
			
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			if(!admin) paramCompId = userVo.getCompId();
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
			// 문서상세 조회조건 매핑
			DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
			
			// 시작일자
			String durStrtDt = (String) object.get("durStrtDt");
			// 종료일자
			String durEndDt = (String) object.get("durEndDt");
			
			String addWhere = "";
			// 기간조회
			// 시작일자
			if(durStrtDt != null && !durStrtDt.isEmpty()){
				addWhere += " AND "+dmDocSvc.getQueryYmdString("REG_DT")+" >= '"+durStrtDt+"'";
			}
			// 종료일자
			if(durEndDt != null && !durEndDt.isEmpty()){
				addWhere += " AND "+dmDocSvc.getQueryYmdString("REG_DT")+" <= '"+durEndDt+"'";
			}
			
			if(!"".equals(addWhere)) dmDocLVo.setWhereSqllet(addWhere);
			dmDocLVo.setStatCd("S"); // 심의대기상태
			dmDocLVo.setDftYn("Y");
			// 심의대기 문서조회
			@SuppressWarnings("unchecked")
			List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>)commonSvc.queryList(dmDocLVo);
			if(dmDocLVoList.size()==0){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			DmDocLVo newDmDocLVo = null;
			if("A".equals(statCd)){
				// 문서그룹ID 목록
				List<String> docGrpList = new ArrayList<String>(); 
				// 문서목록 맵
				Map<String,DmDocLVo> dmDocLVoMap = new HashMap<String,DmDocLVo>();
				for(DmDocLVo storedDmDocLVo : dmDocLVoList){
					docGrpList.add(storedDmDocLVo.getDocGrpId());
					dmDocLVoMap.put(storedDmDocLVo.getDocGrpId(),storedDmDocLVo);
				}
				
				// 심의대기 중인 문서그룹ID 배열
				String[] docGrpIds = ArrayUtil.toArray(docGrpList);
				// 복원할 문서 목록맵
				Map<String,List<DmDocDVo>> addDocVoMap = new HashMap<String,List<DmDocDVo>>();
				DmDocDVo dmDocDVo = null;
				
				// 문서그룹 맵 세팅
				dmDocSvc.setSubDocGrpMap(tableName, addDocVoMap, docGrpIds, null, null, false);
				
				// 문서상세 초기화
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				// 완료 상태 문서만 조회
				dmDocDVo.setStatCd("C");
				// 최상위 문서그룹ID 맵
				Map<String,String> topDocMap = new HashMap<String,String>();
				// 관련문서 전체 목록
				List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
				// 관련 문서맵
				Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
				dmDocSvc.setSubDocGrpSortOrdrs(dmDocDVo, addDocVoMap, topDocMap, subGrpList, subGrpListMap, null, 0);
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				// 관리자 환경설정 조회
				Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, paramCompId);
				DmDocLVo originVo = null;
				for(DmDocDVo storedDmDocDVo : subGrpList){
					originVo = dmDocLVoMap.get(storedDmDocDVo.getDocGrpId());
					newDmDocLVo = new DmDocLVo();
					newDmDocLVo.setTableName(tableName);
					newDmDocLVo.setDocGrpId(storedDmDocDVo.getDocGrpId());
					newDmDocLVo.setSortOrdr(storedDmDocDVo.getSortOrdr());
					newDmDocLVo.setSubYn(storedDmDocDVo.getSubYn());
					newDmDocLVo.setSortDpth(storedDmDocDVo.getSortDpth());
					newDmDocLVo.setDocPid(storedDmDocDVo.getDocPid());
					newDmDocLVo.setSubDocGrpId(storedDmDocDVo.getSubDocGrpId());
					newDmDocLVo.setStatCd("C");
					newDmDocLVo.setDocKeepPrdCd(storedDmDocDVo.getDocKeepPrdCd()); // 문서보존기한코드
					newDmDocLVo.setFldId(storedDmDocDVo.getFldId());
					
					//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
					dmDocNoSvc.setDocNo(newDmDocLVo, storId, langTypCd, "C", dmStorBVo.getCompId(), newDmDocLVo.getFldId(), originVo.getRegrUid());
					newDmDocLVo.setCmplDt("sysdate");
					
					if(newDmDocLVo.getDocPid() != null && !newDmDocLVo.getDocPid().isEmpty()){
						// 문서상세 저장[보존기한등]
						dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, dmDocLVo.getDocPid());
					}else{
						// 문서상세 저장[보존기한등]
						dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, null);
					}
					
					// 기본버전 저장
					newDmDocLVo.setVerVa(envConfigMap.get("verDft"));
					// 버전 목록 저장
					dmDocSvc.saveDocVer(queryQueue, newDmDocLVo.getDocGrpId(), originVo.getDocId(), dmStorBVo.getTblNm(), newDmDocLVo.getVerVa(), "store");
					// 상신저장
					dmDocSvc.saveDisc(queryQueue, dmStorBVo.getCompId(), originVo.getDocGrpId(), statCd, null, null, userVo.getUserUid(), "update");
					/** 검색 색인 데이터를 더함 */
					dmDocSvc.addSrchIndex(storedDmDocDVo.getDocGrpId(), userVo, queryQueue, "I");
					
					//queryQueue.update(dmDocDVo);
				}
			}else{
				for(DmDocLVo storedDmDocLVo : dmDocLVoList){
					newDmDocLVo = new DmDocLVo();
					newDmDocLVo.setTableName(tableName);
					newDmDocLVo.setDocGrpId(storedDmDocLVo.getDocGrpId());
					newDmDocLVo.setStatCd(statCd);
					// 문서상세 저장[보존기한등]
					dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, null);
					// 상신저장
					dmDocSvc.saveDisc(queryQueue, dmStorBVo.getCompId(), storedDmDocLVo.getDocGrpId(), statCd, null, null, userVo.getUserUid(), "update");
				}
			}
			if(queryQueue != null && !queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 인계 - 취소 (일괄) */
	@RequestMapping(value = "/dm/adm/stor/transTakovrDocAllAjx")
	public String transTakovrDocAllAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			String paramCompId = (String) object.get("paramCompId");
			
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			if(!admin) paramCompId = userVo.getCompId();
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
			
			// 문서상세 조회조건 매핑
			DmDocDVo dmDocDVo = new DmDocDVo();
			dmDocDVo.setTableName(tableName);
			
			// 시작일자
			String durStrtDt = (String) object.get("durStrtDt");
			// 종료일자
			String durEndDt = (String) object.get("durEndDt");
			
			dmDocDVo.setDurCat("regDt");
			// 기간조회
			// 시작일자
			if(durStrtDt != null && !durStrtDt.isEmpty()){
				dmDocDVo.setDurStrtDt(durStrtDt);
			}
			// 종료일자
			if(durEndDt != null && !durEndDt.isEmpty()){
				dmDocDVo.setDurEndDt(durEndDt);
			}
			
			// 인수대기 문서가 있는지 여부
			dmDocDVo.setStatCd("O"); 
			Integer takovrCnt = commonSvc.count(dmDocDVo);
			
			// 이관할 대상 건수[전체,기간] - 인수대기중인 문서가 있으면 일괄 인계취소 처리
			if(takovrCnt>0){
				QueryQueue queryQueue = new QueryQueue();
				// 문서 상태코드 변경
				DmDocDVo newDmDocDVo = new DmDocDVo();
				newDmDocDVo.setTableName(tableName);
				newDmDocDVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocDDao.updateTakovrDmDocD");
				newDmDocDVo.setStatCd("O");
				queryQueue.update(newDmDocDVo);
				
				// 인수인계기본 삭제
				DmTakovrBVo dmTakovrBVo = new DmTakovrBVo();
				dmTakovrBVo.setStorId(storId);
				dmTakovrBVo.setTakStatCd("O");
				queryQueue.delete(dmTakovrBVo);
				
				commonSvc.execute(queryQueue);
				// dm.msg.cancel.trans=인계를 취소하였습니다.
				model.put("message", messageProperties.getMessage("dm.msg.cancel.trans", request));
			}else{
				// cm.msg.noData=해당하는 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.noData", request));
			}
			
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJX] 시스템 차단 - 차단/차단 해제 */
	@RequestMapping(value = "/dm/adm/stor/setSysHaltAjx")
	public String setSysHaltAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		UserVo userVo = LoginSession.getUser(request);
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String uri = request.getRequestURI();
		String module = uri.startsWith("/ap") ? "ap" : uri.startsWith("/dm") ? "dm" : null;
		
		// 시스템 관리자 여부
		boolean isSysAdm = userVo==null ? false : ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_SYS_ADMIN);
		
		if(userVo != null && module!=null && isSysAdm){
			
			String cmd = (String)jsonObject.get("cmd");
			boolean inHalt = ptSysSvc.isSysInHalt();
			
			if("halt".equals(cmd)){
				if(inHalt){
					//cm.msg.halt.inHalt=시스템이 이미 중단 중 입니다.
					model.put("message", messageProperties.getMessage("cm.msg.halt.inHalt", locale));
				} else {
					ptSysSvc.setSysHalt(userVo.getUserUid(), module, "cm.msg.halt."+module+"Tran");
					//cm.msg.sysHaltOk=시스템이 차단되었습니다.
					model.put("message", messageProperties.getMessage("cm.msg.sysHaltOk", locale));
				}
			} else if("continuation".equals(cmd)){
				if(inHalt){
					ptSysSvc.clearSysHalt();
					//cm.msg.sysContinuationOk=시스템이 차단 해제되었습니다.
					model.put("message", messageProperties.getMessage("cm.msg.sysContinuationOk", locale));
				} else {
					//cm.msg.halt.notInHalt=시스템이 중단 중이 아닙니다.
					model.put("message", messageProperties.getMessage("cm.msg.halt.notInHalt", locale));
				}
			}
		}
		
		return LayoutUtil.returnJson(model);
	}
	
}
