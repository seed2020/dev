package com.innobiz.orange.web.dm.admCtrl;

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

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmTranBVo;
import com.innobiz.orange.web.dm.vo.DmTranProcLVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Controller
public class DmAdmStorCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmStorCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 목록 조회 */
	@RequestMapping(value = "/dm/adm/stor/listStor")
	public String listStor(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Stor");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 계정 여부
		boolean admin = "U0000001".equals(userVo.getUserUid());
		// 회사ID
		String paramCompId = ParamUtil.getRequestParam(request, "paramCompId", false);
		
		if(admin){
			// 전체회사목록 조회
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", userVo.getLangTypCd());
			if(ptCompBVoList.size()>0){
				model.put("ptCompBVoList", ptCompBVoList);
				if(paramCompId == null || paramCompId.isEmpty()) {
					paramCompId = ptCompBVoList.get(0).getCompId();
				}
			}
			model.put("paramCompId", paramCompId);
		}else{
			paramCompId = userVo.getCompId();
		}
				
		// 조회조건 매핑
		DmStorBVo dmStorBVo = new DmStorBVo();
		VoUtil.bind(request, dmStorBVo);
		dmStorBVo.setQueryLang(langTypCd);
		dmStorBVo.setCompId(paramCompId);
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmStorBVo);
		
		PersonalUtil.setPaging(request, dmStorBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmCatBVo> dmStorBVoList = (List<DmCatBVo>)commonSvc.queryList(dmStorBVo);
			model.put("dmStorBVoList", dmStorBVoList);
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "storId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		model.put("multi", Boolean.TRUE);
		
		// 기본 저장소가 없으면 초기화 버튼을 활성화 한다.
		model.put("resetYn", dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null) == null ? "Y" : "N");
		
		// 시스템 설정 조회 - 문서 이관 허용(시스템관리자)
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("dmTranEnable"))){
			model.put("dmTranEnable", Boolean.TRUE);
		}
				
		return LayoutUtil.getJspPath("/dm/adm/stor/listStor");
	}
	
	/** [팝업] 등록 수정 화면 출력 */
	@RequestMapping(value = "/dm/adm/stor/setStorPop")
	public String setStorPop(HttpServletRequest request,
			@Parameter(name="storId", required=false) String storId,
			@Parameter(name="paramCompId", required=false) String paramCompId,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dftStorBVo = dmStorSvc.getDmStorBVo(request, null, paramCompId, null, null, null);
		
		// 기본저장소가 없으면 기본여부 옵션을 '예'로 활성화한다.
		if(dftStorBVo == null)	model.put("isDftYn", Boolean.FALSE);
		else model.put("isDftYn", Boolean.TRUE);
				
		if(storId != null && !storId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			DmStorBVo dmStorBVo = new DmStorBVo();
			dmStorBVo.setQueryLang(langTypCd);
			dmStorBVo.setCompId(paramCompId);
			dmStorBVo.setStorId(storId);
			dmStorBVo = (DmStorBVo)commonSvc.queryVo(dmStorBVo);
			if(dmStorBVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			if (dmStorBVo.getRescId() != null) {
				// 리소스기본(WR_RESC_B) 테이블 - 조회, 모델에 추가
				dmRescSvc.queryRescBVo(storId, dmStorBVo.getRescId(), model);
			}
			model.put("dmStorBVo", dmStorBVo);
		}
		
		return LayoutUtil.getJspPath("/dm/adm/stor/setStorPop");
	}
	
	/** 등록 수정 (저장) */
	@RequestMapping(value = "/dm/adm/stor/transStor")
	public String transStor(HttpServletRequest request,
			@RequestParam(value = "storId", required = false) String storId,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			ModelMap model) throws Exception {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			
			if(storId==null || storId.isEmpty() || (admin && (paramCompId == null || paramCompId.isEmpty()))){
				LOGGER.error("[ERROR] paramCompId == null || paramCompId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			if(!admin) paramCompId = userVo.getCompId();
			// 조회조건 매핑
			DmStorBVo dmStorBVo = new DmStorBVo();
			VoUtil.bind(request, dmStorBVo);
			dmStorBVo.setCompId(paramCompId);
			
			DmRescBVo dmRescBVo = dmRescSvc.collectDmRescBVo(request, "", queryQueue, storId);//리소스 저장
			
			if (dmRescBVo == null) {
				LOGGER.error("[ERROR] dmRescBVo == null");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			dmStorBVo.setRescId(dmRescBVo.getRescId());
			dmStorBVo.setStorNm(dmRescBVo.getRescVa());
			
			// 등록 또는 수정되는 저장소의 기본여부가 'Y' 이면 기존 저장소의 기본여부를 'N'으로 변경한다. 
			if("Y".equals(dmStorBVo.getDftYn())){
				// 기본저장소 조회
				DmStorBVo dftStorBVo = dmStorSvc.getDmStorBVo(request, null, null, null, null, null);
				if(dftStorBVo != null){
					DmStorBVo srchDmStorBVo = new DmStorBVo();
					srchDmStorBVo.setCompId(paramCompId);
					srchDmStorBVo.setStorId(dftStorBVo.getStorId());
					srchDmStorBVo.setDftYn("N");
					queryQueue.update(srchDmStorBVo);
				}
			}
			queryQueue.update(dmStorBVo);
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.STOR);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.STOR);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('" + listPage + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 저장소관리 (관리자) */
	@RequestMapping(value = {"/dm/adm/stor/findStorPop","/dm/adm/doc/findStorPop"})
	public String findStorPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/dm/adm/stor/findStorPop");
	}
	
	/** [FRAME] 저장소관리 목록 조회 */
	@RequestMapping(value = {"/dm/adm/stor/listStorFrm","/dm/adm/doc/listStorFrm"})
	public String listStorFrm(HttpServletRequest request,
			@RequestParam(value = "useYn", required = false) String useYn,
			@RequestParam(value = "dftYn", required = false) String dftYn,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 계정 여부
		boolean admin = "admin".equals(userVo.getLginId());
		
		// 회사ID
		String paramCompId = !admin ? userVo.getCompId() : dmDocSvc.getParamCompId(request, model, userVo, null, true);
				
		// 조회조건 매핑
		DmStorBVo dmStorBVo = new DmStorBVo();
		VoUtil.bind(request, dmStorBVo);
		dmStorBVo.setQueryLang(langTypCd);
		dmStorBVo.setCompId(paramCompId); // 회사ID
		
		if(useYn == null || useYn.isEmpty()) dmStorBVo.setUseYn("Y");
		if(dftYn != null && !dftYn.isEmpty()) dmStorBVo.setDftYn(dftYn);
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmStorBVo);
		
		PersonalUtil.setPaging(request, dmStorBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmStorBVo> dmStorBVoList = (List<DmStorBVo>)commonSvc.queryList(dmStorBVo);
			model.put("dmStorBVoList", dmStorBVoList);
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "storId"));
		model.put("params", ParamUtil.getQueryString(request));
				
		return LayoutUtil.getJspPath("/dm/adm/stor/listStorFrm");
	}
	
	/** [히든프레임] 저장소 - 초기화 [회사별 최초 한번만]*/
	@RequestMapping(value = "/dm/adm/stor/transStorReset")
	public String transStorReset(HttpServletRequest request,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			if(!admin) paramCompId = userVo.getCompId();
						
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			if(paramCompId == null || paramCompId.isEmpty()) paramCompId = userVo.getCompId();
			//기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
			
			if(dmStorBVo == null ){
				// 저장소VO 초기화
				DmStorBVo newDmStorBVo = new DmStorBVo();
				newDmStorBVo.setTblNm(dmStorSvc.createTblNm(paramCompId));
				// 테이블 생성[인덱스포함]
				dmStorSvc.createTables(newDmStorBVo);
				
				// 저장소 정보 저장
				newDmStorBVo.setCompId(paramCompId);
				// 등록자, 등록일시
				newDmStorBVo.setRegrUid(userVo.getUserUid());
				newDmStorBVo.setRegDt("sysdate");
				// 저장소ID 생성
				newDmStorBVo.setStorId(dmCmSvc.createId("DM_STOR_B"));
				String rescPrefix = "tbl";
				
				// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT
				QueryQueue queryQueue = new QueryQueue();
				
				DmRescBVo dmRescBVo = dmRescSvc.collectDmRescBVo(request, rescPrefix, queryQueue, newDmStorBVo.getStorId(), dmStorSvc.getDftRescMap(request, paramCompId, langTypCd, rescPrefix));
				if (dmRescBVo == null) {
					LOGGER.error("[ERROR] dmRescBVo is null!!");
				}
				
				// 리소스 조회 후 리소스ID와 리소스명 세팅
				newDmStorBVo.setRescId(dmRescBVo.getRescId());
				newDmStorBVo.setStorNm(dmRescBVo.getRescVa());
				
				newDmStorBVo.setUseYn("Y");//사용여부
				newDmStorBVo.setDftYn("Y");//기본여부
				
				queryQueue.insert(newDmStorBVo);
				
				// 기본 항목 저장
				dmStorSvc.makeColmVoList(request, queryQueue, newDmStorBVo);
				
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.STOR);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(DmConstant.STOR);
				
				// dm.msg.storCreate.success=기본저장소가 생성되었습니다.
				model.put("message", messageProperties.getMessage("dm.msg.storCreate.success", request));
				model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/adm/stor/listStor.do")+"&paramCompId="+paramCompId);
			}else{
				// dm.msg.stor.dup=기본저장소가 이미 있습니다.
				String message = messageProperties.getMessage("dm.msg.stor.dup", request);
				model.put("message", message);
				model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/adm/stor/listStor.do")+"&paramCompId="+paramCompId);
				return LayoutUtil.getResultJsp();
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 저장소 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/stor/transStorDelAjx")
	public String transStorDel(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
			
			JSONArray jsonArray = (JSONArray)jsonObject.get("storIds");
			
			if (jsonArray == null || jsonArray.size() == 0) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				String msg = messageProperties.getMessage("pt.msg.nodata.passed", request);
				LOGGER.error("jsonArray size : 0  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 관리자 계정 여부
			boolean admin = "U0000001".equals(userVo.getUserUid());
			
			String paramCompId = admin ? (String) jsonObject.get("paramCompId") : userVo.getCompId(); // 회사ID
			
			if(paramCompId == null || paramCompId.isEmpty()){
				LOGGER.error("[ERROR] paramCompId == null || paramCompId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 테이블 삭제
			QueryQueue queryQueue = new QueryQueue();
			DmStorBVo dmStorBVo = null;
			String storId;
			
			List<DmTranBVo> dmTranBVoList = null;
			DmTranBVo dmTranBVo = null;
			DmTranProcLVo dmTranProcLVo = null;
			int cnt = 0;
			// 이관목록
			for(int i=0;i<jsonArray.size();i++){
				storId = (String)jsonArray.get(i);
				dmStorBVo = new DmStorBVo();
				dmStorBVo.setStorId(storId);
				dmStorBVo.setCompId(paramCompId);
				// 해당 저장소가 없으면 continue
				if(commonSvc.count(dmStorBVo) == 0) continue;
				dmTranBVo = new DmTranBVo();
				dmTranBVo.setStorId(storId);
				dmTranBVo.setTranProcStatCd("processing"); // 진행중
				cnt+=commonSvc.count(dmTranBVo);
				dmTranBVo.setTranProcStatCd("error"); // 오류
				cnt+=commonSvc.count(dmTranBVo);
				if(cnt>0){
					LOGGER.error("[ERROR] storedDmDocTranHVo.getNotCmplCnt()>0");
					// dm.msg.transfer.not.del.withTran=이관이 완료되지 않은 문서가 있어서 삭제할수 없습니다.
					throw new CmException("dm.msg.transfer.not.del.withTran", request);
				}
				dmTranBVo.setTranProcStatCd(null);
				dmTranBVoList = (List<DmTranBVo>)commonSvc.queryList(dmTranBVo);
				for(DmTranBVo storedDmTranBVo : dmTranBVoList){
					// 로그 삭제
					dmTranProcLVo = new DmTranProcLVo();
					dmTranProcLVo.setTranId(storedDmTranBVo.getTranId());
					queryQueue.delete(dmTranProcLVo);
				}
				// 이관이력삭제
				queryQueue.delete(dmTranBVo);
				
				dmStorSvc.deleteTbl(request, storId, queryQueue);
				// 저장소문서관계 삭제
				dmStorSvc.saveStorId(queryQueue, storId, null, true);
			}
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.STOR);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.STOR);
			
			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
