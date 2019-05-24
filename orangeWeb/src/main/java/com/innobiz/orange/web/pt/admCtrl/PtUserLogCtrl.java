package com.innobiz.orange.web.pt.admCtrl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.innobiz.orange.web.or.vo.OrUserLginHVo;
import com.innobiz.orange.web.or.vo.OrUserLginSetupBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wr.svc.WrCmSvc;

/** 시스템, 정책 컨트롤러 */
@Controller
public class PtUserLogCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtUserLogCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 자원관리 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 사용자 로그 - 조회 */
	@RequestMapping(value = "/pt/adm/log/listUserLog")
	public String listUserLog(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = null;
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		
		OrUserLginHVo orUserLginHVo = new OrUserLginHVo();
		VoUtil.bind(request, orUserLginHVo);
		orUserLginHVo.setQueryLang(langTypCd);
		if(!isSysAdmin){
			compId = userVo.getCompId();
			orUserLginHVo.setCompId(compId);
		}
		
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(orUserLginHVo);
		PersonalUtil.setPaging(request, orUserLginHVo, recodeCount);
				
		@SuppressWarnings("unchecked")
		List<OrUserLginHVo> orUserLginHVoList = (List<OrUserLginHVo>)commonSvc.queryList(orUserLginHVo);
		
		// 회사명 맵
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = null;
		if(isSysAdmin){
			ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		}
		if(isSysAdmin){//시스템 관리자면 회사명 세팅
			for(OrUserLginHVo storedOrUserLginHVo : orUserLginHVoList){
				ptCompBVo = ptCompMap.get(storedOrUserLginHVo.getCompId());
				storedOrUserLginHVo.setCompNm(ptCompBVo.getRescNm());
			}
		}
		
		model.put("orUserLginHVoList", orUserLginHVoList);
		model.put("recodeCount", recodeCount);
		
		// 시스템 관리자의 경우 - 검색 조건에 회사목록 추가하기 위한 작업
		if(isSysAdmin){
			// 회사목록
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
			model.put("ptCompBVoList", ptCompBVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/log/listUserLog");
	}
	
	
	/** 로그 선택 삭제 */
	@RequestMapping(value = "/pt/adm/log/transUserLogDel")
	public String transMetngDel(HttpServletRequest request,
			@RequestParam(value = "delList", required = false) String delList,
			ModelMap model) throws Exception {
		
		try {
			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
			
			if (delList == null || delList.isEmpty() ) {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				return JsonUtil.returnJson(model);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			String compId = null;
			// 시스템 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			if(!isSysAdmin){
				compId = userVo.getCompId();
			}
			
			if(delList != null && !delList.isEmpty() ){
				OrUserLginHVo orUserLginHVo = null;
				String[] ids = delList.split(",");
				for(String id : ids){
					String[] info = id.split(":");
					orUserLginHVo = new OrUserLginHVo();
					orUserLginHVo.setUserUid(info[0]);
					orUserLginHVo.setSessionId(info[1]);
					if(compId !=null) orUserLginHVo.setCompId(compId);
					queryQueue.delete(orUserLginHVo);
				}
				
				commonSvc.execute(queryQueue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
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
	
	/** 로그 전체 삭제 */
	@RequestMapping(value = "/pt/adm/log/transUserLogAllDel")
	public String transUserLogAllDel(HttpServletRequest request,
			@RequestParam(value = "delCompId", required = false) String delCompId,
			ModelMap model) throws Exception {
		
		try {
			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			String compId = null;
			
			if(delCompId != null && !delCompId.isEmpty()) compId = delCompId;
			
			// 시스템 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			if(!isSysAdmin){
				compId = userVo.getCompId();
			}
			
			OrUserLginHVo orUserLginHVo = new OrUserLginHVo();
			if(compId !=null) orUserLginHVo.setCompId(compId);
			queryQueue.delete(orUserLginHVo);
			
			commonSvc.execute(queryQueue);
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
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
	
	/** 상세보기 */
	@RequestMapping(value = "/pt/adm/log/setUserLogSetupPop")
	public String viewUserLogSetupPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		OrUserLginSetupBVo searchVo = new OrUserLginSetupBVo();
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		if(!isSysAdmin){
			searchVo.setCompId(userVo.getCompId());
		}
		
		OrUserLginSetupBVo orUserLginSetupBVo = (OrUserLginSetupBVo)commonSvc.queryVo(searchVo);
		model.put("orUserLginSetupBVo", orUserLginSetupBVo);
		
		return LayoutUtil.getJspPath("/pt/adm/log/setUserLogSetupPop");
	}
	
	/** [AJAX] 사용자 로그인 이력 설정 (삭제주기)  */
	@RequestMapping(value = "/pt/adm/log/transUserLogSetupAjx")
	public String transBcFldCopyAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 리소스기본(WB_BC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String repetTypCd = (String)jsonObject.get("repetTypCd");// 반복구분코드
			String strtYmd = (String)jsonObject.get("strtYmd");// 시작년월일
			String useYn = (String)jsonObject.get("useYn");// 사용여부
			String nextRepetYmd = (String)jsonObject.get("nextRepetYmd");// 다음삭제연월일
			
			if (repetTypCd == null || repetTypCd.isEmpty() || strtYmd == null || strtYmd.isEmpty()) {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("parameter : null  msg:"+msg);
				model.put("message", msg);
				return JsonUtil.returnJson(model);
			}
			
			// 설정 테이블
			OrUserLginSetupBVo orUserLginSetupBVo = new OrUserLginSetupBVo();
			//orUserLginSetupBVo.setCompId(userVo.getCompId());
			//회사별이 아닌 전체 설정으로 변경됨에 따라 'A01' 코드를 삽입
			orUserLginSetupBVo.setCompId("A01");
			orUserLginSetupBVo.setRepetTypCd(repetTypCd);
			orUserLginSetupBVo.setStrtYmd(strtYmd);
			orUserLginSetupBVo.setRegrUid(userVo.getUserUid());
			orUserLginSetupBVo.setUseYn(useYn);			
			orUserLginSetupBVo.setDelYn("N");
			if(nextRepetYmd !=null && !nextRepetYmd.isEmpty()) orUserLginSetupBVo.setNextRepetYmd(nextRepetYmd);
			else orUserLginSetupBVo.setNextRepetYmd("");//다음 주기 설정 초기화
			queryQueue.store(orUserLginSetupBVo);
			
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [SCHEDULED] 사용자 로그삭제 스케줄링 */
	@Scheduled(cron=" 0 30 23 * * *")//매일 23시 30분에 초기화
	//@Scheduled(fixedDelay=20000)
	public void deleteUserLogScheduled() {
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			String today = sdf.format(cal.getTime());
			
			// 설정 테이블
			OrUserLginSetupBVo orUserLginSetupBVo = new OrUserLginSetupBVo();
			//orUserLginSetupBVo.setUseYn("Y");//사용여부
			//다음 주기를 수정할 목록 대상 추출
			@SuppressWarnings("unchecked")
			List<OrUserLginSetupBVo> targetOrUserLginSetupBVoList = (List<OrUserLginSetupBVo>)commonSvc.queryList(orUserLginSetupBVo);
			
			//대상 목록이 있을경우
			if(targetOrUserLginSetupBVoList.size() > 0 ){
				OrUserLginSetupBVo updateVo = null;
				String nextRepetYmd="",nextDay,tmpDay,repetTypCd;
				for(OrUserLginSetupBVo setupVo : targetOrUserLginSetupBVoList){
					//반복 주기
					repetTypCd = setupVo.getRepetTypCd();
					//다음반복일시
					nextRepetYmd = setupVo.getNextRepetYmd() == null || setupVo.getNextRepetYmd().isEmpty() ? setupVo.getStrtYmd() : setupVo.getNextRepetYmd();
					nextDay = getDayString(repetTypCd, nextRepetYmd.replaceAll("[- ]", ""));
					tmpDay = getDayString(repetTypCd, today); 
					
					if(Integer.parseInt(nextDay) < Integer.parseInt(tmpDay)){
						nextRepetYmd = getNextRepetDay(repetTypCd, nextRepetYmd, today);
					}else{
						if (LOGGER.isDebugEnabled()) LOGGER.debug("nextRepetYmd >= today - nextRepetYmd : "+nextRepetYmd);
						continue;
					}
					
					updateVo = new OrUserLginSetupBVo();
					//updateVo.setCompId(setupVo.getCompId());
					//회사별이 아닌 전체 설정으로 변경됨에 따라 'A01' 코드를 삽입
					updateVo.setCompId("A01");
					updateVo.setNextRepetYmd(nextRepetYmd);
					updateVo.setDelYn("N");//삭제여부
					queryQueue.update(updateVo);
				}
			}
			orUserLginSetupBVo.setUseYn("Y");//사용여부
			orUserLginSetupBVo.setDelYn("N");//삭제여부
			orUserLginSetupBVo.setDurCat("today");	
			orUserLginSetupBVo.setSchWord(getYmdString(today, "-"));	
			
			//설정한 일자가 오늘일경우
			@SuppressWarnings("unchecked")
			List<OrUserLginSetupBVo> orUserLginSetupBVoList = (List<OrUserLginSetupBVo>)commonSvc.queryList(orUserLginSetupBVo);
			//System.out.println("orUserLginSetupBVoList.size() : "+orUserLginSetupBVoList.size());
			int cnt = 0;
			if(orUserLginSetupBVoList.size() > 0){
				OrUserLginHVo orUserLginHVo = null; 
				for(OrUserLginSetupBVo setupBVo : orUserLginSetupBVoList){
					//로그 삭제
					orUserLginHVo = new OrUserLginHVo();
					//orUserLginHVo.setCompId(setupBVo.getCompId()); //회사코드 삭제
					orUserLginHVo.setCompId(null);
					orUserLginHVo.setDurCat("lginDt"); // 로그인일시
					orUserLginHVo.setDurEndDt(wrCmSvc.getDateOfDay(setupBVo.getNextRepetYmd(), setupBVo.getRepetTypCd(), "n", null , 0)); // 로그인일시
					queryQueue.delete(orUserLginHVo);
					
					//삭제여부 'Y'로 업데이트
					setupBVo.setDelYn("Y");
					queryQueue.update(setupBVo);
					cnt++;
				}
			}
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scheduled deleteUserLogScheduled completed at " + new Timestamp(System.currentTimeMillis()).toString() + ", delete count = " + cnt);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/** 다음 일자 세팅 */ 
	public String getNextRepetDay(String repetTypCd, String nextRepetYmd, String tmpDay){
		nextRepetYmd = wrCmSvc.getDateOfDay(nextRepetYmd, repetTypCd, "p", null , 0);
		String nextDay = getDayString(repetTypCd, nextRepetYmd.replaceAll("[- ]", ""));
		String toDay = getDayString(repetTypCd, tmpDay.replaceAll("[- ]", "")); 
		if(Integer.parseInt(nextDay) < Integer.parseInt(toDay)){
			return getNextRepetDay(repetTypCd, nextRepetYmd, tmpDay);
		}
		return nextRepetYmd;
	}
	
	/** 연월일에 맞는 날짜자리수만큼 반환*/
	public String getDayString(String repetTypCd, String day){
		if("year".equals(repetTypCd)) 
			day = day.substring(0,4);
		else if("month".equals(repetTypCd)) 
			day = day.substring(0,6);
		else if("week".equals(repetTypCd)) 
			day = day.substring(0,8);
		return day;
	}
	
	/** 포맷에 맞는 날짜형식 리턴 */
	public String getYmdString(String day, String fmt){
        return day.substring(0, 4) + fmt +day.substring(4, 6)+fmt+day.substring(6, 8);
	}
	
}
