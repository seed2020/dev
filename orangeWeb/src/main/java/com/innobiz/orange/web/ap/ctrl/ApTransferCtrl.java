package com.innobiz.orange.web.ap.ctrl;

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

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.svc.ApTransferSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApRescBVo;
import com.innobiz.orange.web.ap.vo.ApStorBVo;
import com.innobiz.orange.web.ap.vo.ApStorCompRVo;
import com.innobiz.orange.web.ap.vo.ApTranBVo;
import com.innobiz.orange.web.ap.vo.ApTranFromRVo;
import com.innobiz.orange.web.ap.vo.ApTranProcLVo;
import com.innobiz.orange.web.ap.vo.ApxTranVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/** 이관 저장소 컨트롤러 */
@Controller
public class ApTransferCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 결재 이관 서비스 */
	@Autowired
	private ApTransferSvc apTransferSvc;
	
	/** 캐쉬 만료 처리 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 이관 목록 조회 */
	@RequestMapping(value = "/ap/adm/tran/listTran")
	public String listTran(HttpServletRequest request,
//			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		if(compId==null || compId.isEmpty()){
			compId = userVo.getCompId();
		}
		
		ApTranBVo apTranBVo = new ApTranBVo();
		apTranBVo.setCompId(compId);
		apTranBVo.setQueryLang(langTypCd);
		if(schWord!=null && !schWord.isEmpty()){
			apTranBVo.setSchCat("storRescNm");
			apTranBVo.setSchWord(schWord);
		}
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(apTranBVo);
		PersonalUtil.setPaging(request, apTranBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		
		if(recodeCount.intValue()>0){
			apTranBVo.setOrderBy("TRAN_ID DESC");
			@SuppressWarnings("unchecked")
			List<ApTranBVo> apTranBVoList = (List<ApTranBVo>)commonSvc.queryList(apTranBVo);
			
			if(apTranBVoList != null){
				model.put("apTranBVoList", apTranBVoList);
				
				// 전체대상건수 계산(문서대상건수 + 종이대상건수)
				int allTgtCnt;
				for(ApTranBVo storedApTranBVo : apTranBVoList){
					allTgtCnt = 
							(storedApTranBVo.getDocTgtCnt() == null || storedApTranBVo.getDocTgtCnt().isEmpty()
								? 0 : Integer.parseInt(storedApTranBVo.getDocTgtCnt()))
							+
							(storedApTranBVo.getPapTgtCnt() == null || storedApTranBVo.getPapTgtCnt().isEmpty()
								? 0 : Integer.parseInt(storedApTranBVo.getPapTgtCnt()));
					storedApTranBVo.setAllTgtCnt(Integer.toString(allTgtCnt));
				}
			}
		}
		
		if(userVo.getUserUid().equals("U0000001")){
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", userVo.getLangTypCd());
			model.put("ptCompBVoList", ptCompBVoList);
		}
		
		// 진행중인 이관 확인
		HttpSession session = request.getSession();
		String processingTranId = (String)session.getAttribute("AP_TRAN_ID");
		if(processingTranId != null){
			model.put("processingTranId", processingTranId);
			session.removeAttribute("AP_TRAN_ID");
		}
		
		// 시스템 설정 조회 - 결재 이관 허용(시스템관리자)
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("apTranEnable"))){
			model.put("apTranEnable", Boolean.TRUE);
		}
		
		// 시스템 차단 조회
		if(ptSysSvc.isSysInHalt()){
			model.put("sysHalt", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/ap/adm/tran/listTran");
	}
	
	/** 문서 이관 - 팝업 */
	@RequestMapping(value = "/ap/adm/tran/setTranPop")
	public String setTranPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		if(compId==null || compId.isEmpty()){
			compId = userVo.getCompId();
		}
		
		////////////////////////////////////////////////////////
		//
		// 저장소 목록
		
		ApStorBVo apStorBVo = new ApStorBVo();
		apStorBVo.setQueryLang(langTypCd);
		apStorBVo.setOrderBy("STOR_ID DESC");
		@SuppressWarnings("unchecked")
		List<ApStorBVo> apStorBVoList = (List<ApStorBVo>)commonSvc.queryList(apStorBVo);
		if(apStorBVoList != null){
			model.put("apStorBVoList", apStorBVoList);
		}
		
		////////////////////////////////////////////////////////
		//
		// 회사별 양식 목록
		
		ApFormBVo apFormBVo = new ApFormBVo();
		apFormBVo.setCompId(compId);
		apFormBVo.setTplYn("N");
		apFormBVo.setUseYn("Y");
		apFormBVo.setSortOrdr("SORT_ORDR");
		apFormBVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<ApFormBVo> apFormBVoList = (List<ApFormBVo>)commonSvc.queryList(apFormBVo);
		model.put("apFormBVoList", apFormBVoList);
		
		model.put("today", StringUtil.getCurrYmd());
		
		return LayoutUtil.getJspPath("/ap/adm/tran/setTranPop");
	}
	
	/** [AJX] 문서 이관 - 대상 카운트 조회 */
	@RequestMapping(value = "/ap/adm/tran/getTranCntAjx")
	public String getTranCntAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String compId = (String)jsonObject.get("compId");
		String durStrtDt = (String)jsonObject.get("durStrtDt");
		String durEndDt = (String)jsonObject.get("durEndDt");
		String formIds = (String)jsonObject.get("formIds");
		String docInclYn = (String)jsonObject.get("docInclYn");
		String papDocInclYn = (String)jsonObject.get("papDocInclYn");
		
		// 문서포함여부
		boolean withDoc = "Y".equals(docInclYn);
		// 종이문서포함여부
		boolean withPapDoc = "Y".equals(papDocInclYn);
		
		if(withDoc){
			String[] formIdArr = formIds==null || formIds.isEmpty() ? null : formIds.split(",");
			
			// 등록대장,접수대장,배부대장 - 이관건수 조회
			ApxTranVo apxTranVo = apTransferSvc.createTranTgtVo(compId, durStrtDt, durEndDt, formIdArr, false);
			String langTypCd = LoginSession.getLangTypCd(request);
			apxTranVo.setQueryLang(langTypCd);
			apxTranVo.setWhereSqllet("AND DOC_TYP_CD IN('intro', 'extro')");//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.selectApxTranCnt");
			@SuppressWarnings("unchecked")
			List<ApxTranVo> apxTranVoList = (List<ApxTranVo>)commonSvc.queryList(apxTranVo);
			if(apxTranVoList != null){
				for(ApxTranVo storedApxTranVo : apxTranVoList){
					model.put(storedApxTranVo.getRecLstTypCd(), storedApxTranVo.getCnt());
				}
			}
		}
		
		if(withPapDoc){
			// 종이문서 - 이관건수 조회
			ApxTranVo apxTranVo = apTransferSvc.createTranTgtVo(compId, durStrtDt, durEndDt, null, true);
			String langTypCd = LoginSession.getLangTypCd(request);
			apxTranVo.setQueryLang(langTypCd);
			apxTranVo.setWhereSqllet("AND DOC_TYP_CD = 'paper'");//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.selectApxTranPapCnt");
			Integer paperCnt = commonSvc.queryInt(apxTranVo);
			if(paperCnt != null) model.put("paperDoc", paperCnt.toString());
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJX] 문서 이관 - 이관 실행 */
	@RequestMapping(value = "/ap/adm/tran/transTranAjx")
	public String transTranAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		// 1. 전달된 데이터 저장 : 이관정보
		// 2. apTransferSvc.addTranId(tranId) 호출하여 이관 Thread 를 시작 시킴
		
		UserVo userVo = LoginSession.getUser(request);
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String storId = (String)jsonObject.get("storId");
		String compId = (String)jsonObject.get("compId");
		String durStrtDt = (String)jsonObject.get("durStrtDt");
		String durEndDt = (String)jsonObject.get("durEndDt");
		String formIds = (String)jsonObject.get("formIds");
		String docInclYn = (String)jsonObject.get("docInclYn");
		String papDocInclYn = (String)jsonObject.get("papDocInclYn");
		String defragYn = (String)jsonObject.get("defrag");
		
		JSONArray rescArray = (JSONArray)jsonObject.get("resces");
		
		boolean storAdded = false;
		
		try {
			
			QueryQueue queryQueue = new QueryQueue();
			
			ApStorBVo apStorBVo = null;
			ApStorCompRVo apStorCompRVo = null;
		
			if(storId != null){
				
				apStorBVo = new ApStorBVo();
				apStorBVo.setStorId(storId);
				apStorBVo = (ApStorBVo)commonSvc.queryVo(apStorBVo);
				if(apStorBVo == null){
					//ap.msg.noStorage=해당 저장소를 찾을 수 없습니다.
					throw new CmException("ap.msg.noStorage", locale);
				}
				
				// 저장소회사관계 - 데이터 없으면 생성
				apStorCompRVo = new ApStorCompRVo();
				apStorCompRVo.setCompId(compId);
				apStorCompRVo.setStorId(storId);
				apStorCompRVo = (ApStorCompRVo)commonSvc.queryVo(apStorCompRVo);
				
				if(apStorCompRVo == null){
					
					storAdded = true;
					
					// 저장소회사관계(AP_STOR_COMP_R)
					apStorCompRVo = new ApStorCompRVo();
					apStorCompRVo.setCompId(compId);
					apStorCompRVo.setStorId(storId);
					apStorCompRVo.setStorRescId(apStorBVo.getRescId());
					apStorCompRVo.setCompDocCnt("0");
					apStorCompRVo.setUseYn("Y");
					queryQueue.insert(apStorCompRVo);
				}
				
			} else {
				
				// 저장소 기본 리소스 생성
				String rescId = apCmSvc.createId("AP_RESC_B");
				
				// 저장소 리소스 저장
				ApRescBVo apRescBVo;
				JSONObject jsonResc;
				int i, size = rescArray.size();
				for(i=0; i<size; i++){
					jsonResc = (JSONObject)rescArray.get(i);
					apRescBVo = new ApRescBVo();
					apRescBVo.setRescId(rescId);
					apRescBVo.setLangTypCd((String)jsonResc.get("langTypCd"));
					apRescBVo.setRescVa((String)jsonResc.get("rescVa"));
					queryQueue.insert(apRescBVo);
				}
				
				// 저장소ID 생성
				long storNo = commonSvc.nextVal("AP_STOR_B");
				storId = to4Letters(storNo);
				
				// 저장소 생성
				apStorBVo = new ApStorBVo();
				apStorBVo.setStorId(storId);
				apStorBVo.setRescId(rescId);
				apStorBVo.setCrtYn("N");
				apStorBVo.setAllDocCnt("0");
				queryQueue.insert(apStorBVo);
				
				
				storAdded = true;
				
				// 저장소회사관계
				apStorCompRVo = new ApStorCompRVo();
				apStorCompRVo.setCompId(compId);
				apStorCompRVo.setStorId(storId);
				apStorCompRVo.setStorRescId(rescId);
				apStorCompRVo.setCompDocCnt("0");
				apStorCompRVo.setUseYn("Y");
				queryQueue.insert(apStorCompRVo);
			}
			
			String today = StringUtil.getCurrYmd();
			
			String tranId = apCmSvc.createId("AP_TRAN_B");
			
			// 이관기본(AP_TRAN_B) 테이블 - INSERT
			ApTranBVo apTranBVo = new ApTranBVo();
			apTranBVo.setTranId(tranId);
			apTranBVo.setCompId(compId);
			apTranBVo.setStorId(storId);
			apTranBVo.setStorRescId(apStorBVo.getRescId());
			apTranBVo.setTranTgtStrtYmd(durStrtDt);
			apTranBVo.setTranTgtEndYmd(durEndDt==null || durEndDt.isEmpty() ? today : durEndDt);
			apTranBVo.setDocInclYn(docInclYn);
			apTranBVo.setPapDocInclYn(papDocInclYn);
			apTranBVo.setTranProcStatCd("preparing");
			apTranBVo.setDefragYn(defragYn);
			apTranBVo.setUserUid(userVo.getUserUid());
			queryQueue.insert(apTranBVo);
			
			// 이관양식관계(AP_TRAN_FROM_R) 테이블 - INSERT
			String[] formIdArr = formIds==null || formIds.isEmpty() ? null : formIds.split(",");
			if(formIdArr != null){
				ApTranFromRVo apTranFromRVo;
				for(String formId : formIdArr){
					apTranFromRVo = new ApTranFromRVo();
					apTranFromRVo.setTranId(tranId);
					apTranFromRVo.setFormId(formId);
					queryQueue.insert(apTranFromRVo);
				}
			}
			
			if(!storAdded){
				commonSvc.execute(queryQueue);
			} else {
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, ApConstant.AP_STORAGE);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(ApConstant.AP_STORAGE);
			}
			
			model.put("result", "ok");
			request.getSession().setAttribute("AP_TRAN_ID", tranId);
			apTransferSvc.addTranId(tranId);
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [AJX] 재실행 - 이관 실행 */
	@RequestMapping(value = "/ap/adm/tran/transRetransferAjx")
	public String transRetransferAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		apTransferSvc.addTranId(tranId);
		
		return LayoutUtil.returnJson(model);
	}
	/** [팝업] 이관 지행 내역 */
	@RequestMapping(value = "/ap/adm/tran/listTranProcPop")
	public String listTranProcPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 내용은 스크립트로 Ajax 호출 하여 가져옴
		return LayoutUtil.getJspPath("/ap/adm/tran/listTranProcPop");
	}
	/** [AJX] 이관 지행 내역 */
	@RequestMapping(value = "/ap/adm/tran/getTranProcListAjx")
	public String getTranProcListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String tranId = (String)jsonObject.get("tranId");
		String strtSeq = (String)jsonObject.get("strtSeq");
		
		// 이관기본(AP_TRAN_B) 테이블 - 프로세스 완료여부 조회
		ApTranBVo apTranBVo = new ApTranBVo();
		apTranBVo.setTranId(tranId);
		apTranBVo = (ApTranBVo)commonSvc.queryVo(apTranBVo);
		if(apTranBVo != null){
			// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
			String tranProcStatCd = apTranBVo.getTranProcStatCd();
			if("preparing".equals(tranProcStatCd) || "processing".equals(tranProcStatCd)){
				model.put("completed", "N");
			}
			if("error".equals(tranProcStatCd)){
				model.put("hasError", "Y");
			}
		}
		
		// 이관진행내역(AP_TRAN_PROC_L) 테이블 - 개별 내역 조회
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(tranId);
		if(strtSeq != null) apTranProcLVo.setStrtSeq(strtSeq);
		@SuppressWarnings("unchecked")
		List<ApTranProcLVo> apTranProcLVoList = (List<ApTranProcLVo>)commonSvc.queryList(apTranProcLVo);
		if(apTranProcLVoList != null){
			model.put("procList", apTranProcLVoList);
		}
		
		return LayoutUtil.returnJson(model);
	}

	/** [팝업] 저장소 조회 */
	@RequestMapping(value = "/ap/adm/tran/setStorPop")
	public String setStorPop(HttpServletRequest request,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId();
		
		// 저장소기본(AP_STOR_B) 테이블 - 저장소의 전체문서건수 조회
		ApStorBVo apStorBVo = new ApStorBVo();
		apStorBVo.setStorId(storId);
		apStorBVo.setQueryLang(langTypCd);
		apStorBVo = (ApStorBVo)commonSvc.queryVo(apStorBVo);
		
		// 저장소회사관계(AP_STOR_COMP_R) 테이블 - 회사별 문서 건수 조회
		ApStorCompRVo apStorCompRVo = new ApStorCompRVo();
		apStorCompRVo.setStorId(storId);
		apStorCompRVo.setCompId(compId);
		apStorCompRVo = (ApStorCompRVo)commonSvc.queryVo(apStorCompRVo);
		if(apStorCompRVo != null){
			model.put("apStorCompRVo", apStorCompRVo);
			
			if(apStorBVo != null){
				apStorCompRVo.setAllDocCnt(apStorBVo.getAllDocCnt());
			}
		}
		
		boolean hasSysAuth = "SYS".equals(request.getAttribute("_AUTH"));
		if(hasSysAuth){
			model.put("hasSysAuth", Boolean.TRUE);
			
			if(apStorCompRVo != null){
				ApRescBVo apRescBVo = new ApRescBVo();
				apRescBVo.setRescId(apStorCompRVo.getStorRescId());
				@SuppressWarnings("unchecked")
				List<ApRescBVo> apRescBVoList= (List<ApRescBVo>)commonSvc.queryList(apRescBVo);
				
				if(apRescBVoList != null){
					for(ApRescBVo storedApRescBVo : apRescBVoList){
						model.put(storedApRescBVo.getRescId()+"_"+storedApRescBVo.getLangTypCd(), storedApRescBVo.getRescVa());
					}
				}
			}
		}
		
		return LayoutUtil.getJspPath("/ap/adm/tran/setStorPop");
	}
	
	/** [프레임] 저장소 저장 */
	@RequestMapping(value = "/ap/adm/tran/transStorPop")
	public String transStorPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="rescId", required=false) String rescId,
			@Parameter(name="useYn", required=false) String useYn,
			ModelMap model, Locale locale) throws Exception{

		try{
			
			//String langTypCd = LoginSession.getLangTypCd(request);
			UserVo userVo = LoginSession.getUser(request);
			if(compId==null || compId.isEmpty()) compId = userVo.getCompId();
			
			QueryQueue queryQueue = new QueryQueue();
			
			ApStorCompRVo apStorCompRVo = new ApStorCompRVo();
			apStorCompRVo.setCompId(compId);
			apStorCompRVo.setStorId(storId);
			apStorCompRVo.setUseYn(useYn);
			queryQueue.update(apStorCompRVo);
			
			if(rescId!=null && !rescId.isEmpty()){
				apRescSvc.collectApRescBVo(request, null, queryQueue);
			}
			
			// 캐쉬 삭제 - 저장소 캐쉬
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, ApConstant.AP_STORAGE);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(ApConstant.AP_STORAGE);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 4자리 스트링으로 변환 */
	private String to4Letters(long no){
		if(no<10) return "000"+no;
		if(no<100) return "00"+no;
		if(no<1000) return "0"+no;
		return Long.toString(no);
	}
	/** [AJX] 시스템 차단 - 차단/차단 해재 */
	@RequestMapping(value = "/ap/adm/tran/setSysHaltAjx")
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
