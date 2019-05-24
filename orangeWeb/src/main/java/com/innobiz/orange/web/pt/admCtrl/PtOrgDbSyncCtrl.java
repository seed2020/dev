package com.innobiz.orange.web.pt.admCtrl;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.sync.OrDbSyncSvc;
import com.innobiz.orange.web.or.vo.OrDbIntgDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/** 조직도 DB 동기화 컨트롤러 */
@Controller
public class PtOrgDbSyncCtrl {

//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(PtOrgDbSyncCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직도 DB 동기화 서비스 */
	@Autowired
	private OrDbSyncSvc orDbSyncSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 지금 실행 호출 */
	private boolean runNowCalled = false;
	
	/** [팝업] 조직도 DB 동기화 - 조회 */
	@RequestMapping(value = "/pt/adm/org/setOrgDbSyncPop")
	public String setOrgDbSyncPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId();
		String langTypCd = LoginSession.getLangTypCd(request);
		
		OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
		orDbIntgDVo.setCompId(compId);
		orDbIntgDVo = (OrDbIntgDVo)commonSvc.queryVo(orDbIntgDVo);
		if(orDbIntgDVo!=null){
			model.put("orDbIntgDVo", orDbIntgDVo);
			
			// 리소스명 검색 - 부서없음조직ID / 삭제사용자이동조직ID / 삭제조직이동조직ID
			String rescVa;
			String noDeptOrgId = orDbIntgDVo.getNoDeptOrgId();
			if(noDeptOrgId != null && !noDeptOrgId.isEmpty()){
				rescVa = orCmSvc.getOrgRescNmByOrgTypCd(noDeptOrgId, null, langTypCd);
				if(rescVa!=null) model.put("noDeptOrgNm", rescVa);
				else model.put("noDeptOrgNm", noDeptOrgId);
			}
			String delUserMoveOrgId = orDbIntgDVo.getDelUserMoveOrgId();
			if(delUserMoveOrgId != null && !delUserMoveOrgId.isEmpty()){
				rescVa = orCmSvc.getOrgRescNmByOrgTypCd(delUserMoveOrgId, null, langTypCd);
				if(rescVa!=null) model.put("delUserMoveOrgNm", rescVa);
				else model.put("delUserMoveOrgNm", delUserMoveOrgId);
			}
			String delOrgMoveOrgId = orDbIntgDVo.getDelOrgMoveOrgId();
			if(delOrgMoveOrgId != null && !delOrgMoveOrgId.isEmpty()){
				rescVa = orCmSvc.getOrgRescNmByOrgTypCd(delOrgMoveOrgId, null, langTypCd);
				if(rescVa!=null) model.put("delOrgMoveOrgNm", rescVa);
				else model.put("delOrgMoveOrgNm", delUserMoveOrgId);
			}
			
		}
		
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
		if(ptCompBVoList!=null && ptCompBVoList.size()>1){
			model.put("ptCompBVoList", ptCompBVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/setOrgDbSyncPop");
	}
	
	/** [히든프레임] - 조직도 DB 동기화 - 저장 */
	@RequestMapping(value = "/pt/adm/org/transOrgDbSync")
	public String transOrgDbSync(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="intgTm", required=false) String intgTm,
			@Parameter(name="useYn", required=false) String useYn,
			ModelMap model) throws Exception {
		
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			String langTypCd = LoginSession.getLangTypCd(request);
			if(compId==null || compId.isEmpty()){
				compId = userVo.getCompId();
			}
			
			OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
			VoUtil.bind(request, orDbIntgDVo);
			
			String errMsg = null;
			if(orDbIntgDVo.getDelOrgMoveOrgId() != null && !orDbIntgDVo.getDelOrgMoveOrgId().isEmpty()){
				if(orCmSvc.getOrgByOrgTypCd(orDbIntgDVo.getDelOrgMoveOrgId(), null, langTypCd)==null){
					//or.msg.syscNoToDept=부서를 확인 할 수 없습니다.
					//or.cols.delOrgMoveOrgId=삭제된 조직을 이동할 조직ID
					errMsg = messageProperties.getMessage("or.msg.syscNoToDept", request)
							+ " : " + messageProperties.getMessage("or.cols.delOrgMoveOrgId", request);
				}
			}
			if(orDbIntgDVo.getDelUserMoveOrgId() != null && !orDbIntgDVo.getDelUserMoveOrgId().isEmpty()){
				if(orCmSvc.getOrgByOrgTypCd(orDbIntgDVo.getDelUserMoveOrgId(), null, langTypCd)==null){
					// or.msg.syscNoToDept=부서를 확인 할 수 없습니다.
					// or.cols.delUserMoveOrgId=삭제된 사용자를 이동할 조직ID
					errMsg = messageProperties.getMessage("or.msg.syscNoToDept", request)
							+ " : " + messageProperties.getMessage("or.cols.delUserMoveOrgId", request);
				}
			}
			if(orDbIntgDVo.getNoDeptOrgId() != null && !orDbIntgDVo.getNoDeptOrgId().isEmpty()){
				if(orCmSvc.getOrgByOrgTypCd(orDbIntgDVo.getNoDeptOrgId(), null, langTypCd)==null){
					// or.msg.syscNoToDept=부서를 확인 할 수 없습니다.
					// or.cols.noDeptOrgId=부서 없음 조직ID
					errMsg = messageProperties.getMessage("or.msg.syscNoToDept", request)
							+ " : " + messageProperties.getMessage("or.cols.noDeptOrgId", request);
				}
			}
			if(errMsg != null){
				model.put("message", errMsg);
				return LayoutUtil.getResultJsp();
			}
			
			// 파라미터 intgTm 의 시간이 잘못 된 경우 고침
			Pattern pattern = Pattern.compile("[0-9]{4}");
			if(intgTm!=null && pattern.matcher(intgTm).matches()){
				int h = Integer.parseInt(intgTm.substring(0,2));
				int m = Integer.parseInt(intgTm.substring(2));
				
				if(m>59){
					h++;
					m = m - 60;
				}
				if(h>23) h = h % 24;
				
				int tm = h*100 + m;
				intgTm = tm >= 1000 ? Integer.toString(tm) : tm >= 100 ? "0"+tm : tm >= 10 ? "00"+tm : "000"+tm;
			} else {
				intgTm = "";
			}
			
			if(useYn==null) useYn = "Y";
			
			orDbIntgDVo.setIntgTm(intgTm);
			orDbIntgDVo.setCompId(compId);
			orDbIntgDVo.setUseYn(useYn);
			
			QueryQueue queryQueue = new QueryQueue();
			queryQueue.store(orDbIntgDVo);
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.OR_DB_SYNC);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.OR_DB_SYNC);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			//model.put("todo", "parent.dialog.close('orgDbSyncDialog');");
			return LayoutUtil.getResultJsp();
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** [AJAX] - 동기화 - 연결 테스트 */
	@RequestMapping(value = "/pt/adm/org/testConnAjx")
	public String testConnAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
		orDbIntgDVo.setDbUserId((String)jsonObject.get("dbUserId"));
		orDbIntgDVo.setDbPw((String)jsonObject.get("dbPw"));
		orDbIntgDVo.setDbDriver((String)jsonObject.get("dbDriver"));
		orDbIntgDVo.setDbUrl((String)jsonObject.get("dbUrl"));
		orDbIntgDVo.setDbTestSql((String)jsonObject.get("dbTestSql"));
		
		try {
			
			orDbSyncSvc.testConnection(orDbIntgDVo);
			// or.txt.successConn=성공적으로 연결 하였습니다.
			String message = messageProperties.getMessage("or.txt.successConn", request);
			model.put("message", message);
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 동기화 - 지금 실행 */
	@RequestMapping(value = "/pt/adm/org/runNow")
	public String runNow(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		/*
		 * 비동기 방식으로 호출 되며 동기화 완료시 메세지 전달함
		 * */
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String compId = (String)jsonObject.get("compId");
		
		UserVo userVo = LoginSession.getUser(request);
		if(compId==null || compId.isEmpty()){
			compId = userVo.getCompId();
		}
		
		if(runNowCalled) {
			// or.msg.syncStarted=동기화가 이미 진행 중 입니다.
			String message = messageProperties.getMessage("or.msg.syncStarted", request);
			model.put("message", message);
		} else {
			try {
				runNowCalled = true;
				boolean result = orDbSyncSvc.sync(compId);
				
				if(!result){
					// or.msg.syncNoSetup=동기화가 설정되지 않았습니다.
					String message = messageProperties.getMessage("or.msg.syncNoSetup", request);
					model.put("message", message);
				} else {
					// or.msg.syncCmpl=동기화가 완료 되었습니다.
					String message = messageProperties.getMessage("or.msg.syncCmpl", request);
					model.put("message", message);
				}
				
			} catch(Exception e){
				String message = e.getMessage();
				model.put("message", message);
			} finally {
				runNowCalled = false;
			}
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
