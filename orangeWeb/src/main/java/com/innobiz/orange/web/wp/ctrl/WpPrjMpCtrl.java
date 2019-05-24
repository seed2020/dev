package com.innobiz.orange.web.wp.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wp.util.Crc32Map;
import com.innobiz.orange.web.wp.util.PrjMpDetails;
import com.innobiz.orange.web.wp.util.UsrMpDetails;
import com.innobiz.orange.web.wp.vo.WpMpBVo;
import com.innobiz.orange.web.wp.vo.WpPrjAuthDVo;
import com.innobiz.orange.web.wp.vo.WpPrjBVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpPlanDVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpPlanLVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpRsltDVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpRsltLVo;

/** 프로잭트 투입인력 컨트롤러 */
@Controller
public class WpPrjMpCtrl {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	
	/** [프로잭트 공수입력] - 공수 조회 */
	@RequestMapping(value = "/wp/setPrjMp")
	public String setPrjMp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			@RequestParam(value = "mpId", required = false) String mpId,
			@RequestParam(value = "year", required = false) String year,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(mpId==null || mpId.isEmpty()) mpId = userVo.getUserUid();
		model.put("mpId", mpId);
		
		// 프로잭트기본(WP_PRJ_B) 테이블
		WpPrjBVo wpPrjBVo = new WpPrjBVo();
		wpPrjBVo.setPrjNo(prjNo);
		wpPrjBVo= (WpPrjBVo)commonSvc.queryVo(wpPrjBVo);
		model.put("wpPrjBVo", wpPrjBVo);
		
		WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
		wpPrjMpPlanDVo.setPrjNo(prjNo);
		wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
		model.put("wpPrjMpPlanDVoList", wpPrjMpPlanDVoList);
		
		// 프로잭트 소속인지 검사
		boolean isInProject = false;
		if(wpPrjMpPlanDVoList!=null && !wpPrjMpPlanDVoList.isEmpty()){
			for(WpPrjMpPlanDVo vo : wpPrjMpPlanDVoList){
				if(mpId.equals(vo.getMpId())){
					isInProject = true;
					break;
				}
			}
		}
		if(!isInProject){
			// 권한 없음 - 포워드
			request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
			return null;
		}
		
		GregorianCalendar calendar = new GregorianCalendar();
		
		int nYear = 0;
		if(year!=null && !year.isEmpty()){
			try{ nYear = Integer.parseInt(year); }
			catch(Exception e){}
		}
		if(nYear==0 && wpPrjBVo!=null){
			if(wpPrjBVo.getCmplYmd()!=null && wpPrjBVo.getCmplYmd().length()>4){
				nYear = Integer.parseInt(wpPrjBVo.getCmplYmd().substring(0, 4));
			} else if(wpPrjBVo.getEndYmd()!=null && wpPrjBVo.getEndYmd().length()>4){
				nYear = Integer.parseInt(wpPrjBVo.getEndYmd().substring(0, 4));
			}
			int cYear = calendar.get(Calendar.YEAR);
			if(nYear > cYear){
				nYear = cYear;
			}
		}
		
		if(nYear==0){
			nYear = calendar.get(Calendar.YEAR);
		} else {
			calendar.set(Calendar.YEAR, nYear);
		}
		year = Integer.toString(nYear);
		model.put("year", nYear);
		
		int[] weekOfFirstDay = new int[12];
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		for(int i=0; i<12; i++){
			calendar.set(Calendar.MONTH, i);
			weekOfFirstDay[i] = calendar.get(Calendar.DAY_OF_WEEK);
		}
		model.put("weekOfFirstDay", weekOfFirstDay);
		
		int[] lastDayOfMonth = { 31,28,31,30,31,30,31,31,30,31,30,31 };
		if((nYear%400==0 || nYear%100!=0) && (nYear%4==0)) lastDayOfMonth[1] = 29;
		model.put("lastDayOfMonth", lastDayOfMonth);
		
		WpPrjMpRsltLVo wpPrjMpRsltLVo = new WpPrjMpRsltLVo();
		wpPrjMpRsltLVo.setPrjNo(prjNo);
		wpPrjMpRsltLVo.setMpId(mpId);
		wpPrjMpRsltLVo.setMinRsltYmd(nYear+"-01-01");
		wpPrjMpRsltLVo.setMaxRsltYmd((nYear+1)+"-01-01");
		@SuppressWarnings("unchecked")
		List<WpPrjMpRsltLVo> wpPrjMpRsltLVoList = (List<WpPrjMpRsltLVo>)commonSvc.queryList(wpPrjMpRsltLVo);
		if(wpPrjMpRsltLVoList!=null && !wpPrjMpRsltLVoList.isEmpty()){
			Map<String, WpPrjMpRsltLVo> wpPrjMpRsltLVoMap = new Crc32Map<String, WpPrjMpRsltLVo>();
			for(WpPrjMpRsltLVo vo : wpPrjMpRsltLVoList){
				if(vo.getRsltMd()!=null && !vo.getRsltMd().isEmpty() && vo.getRsltYmd()!=null && !vo.getRsltYmd().isEmpty()){
					wpPrjMpRsltLVoMap.put(vo.getRsltYmd().substring(0, 10), vo);
				}
			}
			model.put("wpPrjMpRsltLVoMap", wpPrjMpRsltLVoMap);
		}
		
		// 일정관리 공휴일
		String realStrtDt = (nYear-1)+"1001";
		String end = (nYear+1)+"0101";
		String natCd = wcScdManagerSvc.getNatCd(userVo);
		List<String> excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), userVo.getLangTypCd(), natCd, realStrtDt, end, null);
		
		if(excludeList != null){
			List<String> holidayList = new ArrayList<String>();
			for(String dt : excludeList){
				if(!dt.startsWith(year)) continue;
				holidayList.add(dt.substring(0,4)+"-"+dt.substring(4,6)+"-"+dt.substring(6,8));
			}
			model.put("holidayList", holidayList);
		}
		
		// 권한 조회
		WpPrjAuthDVo wpPrjAuthDVo = new WpPrjAuthDVo();
		wpPrjAuthDVo.setPrjNo(prjNo);
		wpPrjAuthDVo.setAuthCd("coverOuts");
		wpPrjAuthDVo.setMpId(userVo.getUserUid());
		if(commonSvc.count(wpPrjAuthDVo) > 0){
			model.put("coverOuts", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/wp/setPrjMp");
	}
	
	/** [프로잭트 공수입력] - 공수 선택 팝업 */
	@RequestMapping(value = "/wp/setRsltMdPop")
	public String setModifyMpPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		return LayoutUtil.getJspPath("/wp/setRsltMdPop");
	}
	
	/** [프로잭트 공수입력] - 공수 저장 */
	@RequestMapping(value = "/wp/transPrjMp")
	public String transPrjMp(HttpServletRequest request,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			@RequestParam(value = "mpId", required = false) String mpId,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "workDays", required = false) String strWorkDays,
			ModelMap model) throws Exception {
		try{
			
			UserVo userVo = LoginSession.getUser(request);
			if(mpId==null || mpId.isEmpty()) mpId = userVo.getUserUid();
			
			WpPrjMpRsltLVo wpPrjMpRsltLVo;
			JSONArray jsonArray = (JSONArray)JSONValue.parse(data);
			JSONObject jsonObject;
			
			QueryQueue queryQueue = new QueryQueue();
			
			wpPrjMpRsltLVo = new WpPrjMpRsltLVo();
			wpPrjMpRsltLVo.setPrjNo(prjNo);
			wpPrjMpRsltLVo.setMpId(mpId);
			wpPrjMpRsltLVo.setMinRsltYmd(year+"-01-01");
			wpPrjMpRsltLVo.setMaxRsltYmd((Integer.parseInt(year)+1)+"-01-01");
			queryQueue.delete(wpPrjMpRsltLVo);
			
			int month, nYear = Integer.parseInt(year);
			double[] mdSums = new double[12];
			
			int i, size = jsonArray.size();
			for(i=0;i<size;i++){
				jsonObject = (JSONObject)jsonArray.get(i);
				
				wpPrjMpRsltLVo = new WpPrjMpRsltLVo();
				wpPrjMpRsltLVo.setPrjNo(prjNo);
				wpPrjMpRsltLVo.setMpId(mpId);
				wpPrjMpRsltLVo.setRsltYmd((String)jsonObject.get("rsltYmd"));
				wpPrjMpRsltLVo.setRsltMd((String)jsonObject.get("rsltMd"));
				wpPrjMpRsltLVo.setNote((String)jsonObject.get("note"));
				queryQueue.insert(wpPrjMpRsltLVo);
				
				month = Integer.parseInt(wpPrjMpRsltLVo.getRsltYmd().substring(5, 7));
				mdSums[month-1] += Double.parseDouble(wpPrjMpRsltLVo.getRsltMd());
			}
			
			// 프로잭트인력결과상세(WP_PRJ_MP_RSLT_D) 테이블 - 삭제
			WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
			wpPrjMpRsltDVo.setPrjNo(prjNo);
			wpPrjMpRsltDVo.setMpId(mpId);
			wpPrjMpRsltDVo.setMinRsltYm(year+"-01-01");
			wpPrjMpRsltDVo.setMaxRsltYm((nYear+1)+"-01-01");
			queryQueue.delete(wpPrjMpRsltDVo);
			
			if(size>0){
				String[] workDays = strWorkDays.split(",");
				double rsltMm;
				for(i=1; i<=mdSums.length; i++){
					mdSums[i-1] = (double)Math.round(mdSums[i-1] * 1000) / 1000; 
					if(mdSums[i-1]>0){
						
						wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
						wpPrjMpRsltDVo.setPrjNo(prjNo);
						wpPrjMpRsltDVo.setMpId(mpId);
						wpPrjMpRsltDVo.setRsltYm(year+"-"+ (i<10 ? "0" : "")+i+"-01");
						wpPrjMpRsltDVo.setMMd(workDays[i-1]);
						
						rsltMm = (double)Math.round((mdSums[i-1] * 10) / Integer.parseInt(workDays[i-1])) / 10D;
						wpPrjMpRsltDVo.setRsltMm(Double.toString(rsltMm));
						queryQueue.insert(wpPrjMpRsltDVo);
					}
				}
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload()");
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [개인별 집계]  */
	@RequestMapping(value = "/wp/listUsrPrjMm")
	public String listUsrPrjMm(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "mpId", required = false) String mpId,
			@RequestParam(value = "mpTypCd", required = false) String mpTypCd,
			@RequestParam(value = "strtYmd", required = false) String strtYmd,
			@RequestParam(value = "endYmd", required = false) String endYmd,
			ModelMap model, Locale locale) throws SQLException{
		
		if(strtYmd==null){
			strtYmd = getYmd(-12);// 디폴트 시작일 - 1년전 해당월 1일
		}
		model.put("strtYmd", strtYmd);
		
		boolean valid = false;
		if(mpId!=null && !mpId.isEmpty()){
			if("emp".equals(mpTypCd)){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(mpId);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				if(orUserBVo != null){
					model.put("orUserBVo", orUserBVo);
					valid = true;
				}
			} else if("out".equals(mpTypCd)){
				WpMpBVo wpMpBVo = new WpMpBVo();
				wpMpBVo.setMpId(mpId);
				wpMpBVo = (WpMpBVo)commonSvc.queryVo(wpMpBVo);
				if(wpMpBVo != null){
					model.put("wpMpBVo", wpMpBVo);
					valid = true;
				}
			}
		}
		if(!valid){
			return LayoutUtil.getJspPath("/wp/listUsrPrjMm");
		}
		
		// 기간 시작 - 해당월 1일로 변경
		if(strtYmd!=null && !strtYmd.isEmpty() && strtYmd.length()==10){
			if(!strtYmd.endsWith("01")){
				strtYmd = strtYmd.substring(0, 8)+"01";
			}
		}
		// 기간 종료 - 해당월의 익월 1일로 변경
		if(endYmd!=null && !endYmd.isEmpty() && endYmd.length()==10){
			int year = Integer.parseInt(endYmd.substring(0, 4));
			int month = Integer.parseInt(endYmd.substring(5, 7));
			if(month==12){
				year++;
				month = 1;
			} else {
				month++;
			}
			endYmd = year+"-"+(month>9 ? "" : "0")+month+"-01";
		}
		
		// 화면 구성용 데이터 홀더
		UsrMpDetails usrMpDetails = new UsrMpDetails();
		model.put("usrMpDetails", usrMpDetails);
		
		// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블
		WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
		wpPrjMpPlanLVo.setMpId(mpId);
		wpPrjMpPlanLVo.setMinPlanMm("0");
		if(strtYmd!=null && !strtYmd.isEmpty()){
			wpPrjMpPlanLVo.setMinPlanYm(strtYmd);
		}
		if(endYmd!=null && !endYmd.isEmpty()){
			wpPrjMpPlanLVo.setMaxPlanYm(endYmd);
		}
		wpPrjMpPlanLVo.setOrderBy("PRJ_NO, PLAN_YM");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanLVo> wpPrjMpPlanLVoList = (List<WpPrjMpPlanLVo>)commonSvc.queryList(wpPrjMpPlanLVo);
		usrMpDetails.setPlanLList(wpPrjMpPlanLVoList);
		
		// 프로잭트인력결과상세(WP_PRJ_MP_RSLT_D) 테이블
		WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
		wpPrjMpRsltDVo.setMpId(mpId);
		if(strtYmd!=null && !strtYmd.isEmpty()){
			wpPrjMpRsltDVo.setMinRsltYm(strtYmd);
		}
		if(endYmd!=null && !endYmd.isEmpty()){
			wpPrjMpRsltDVo.setMaxRsltYm(endYmd);
		}
		wpPrjMpRsltDVo.setOrderBy("PRJ_NO, RSLT_YM");
		@SuppressWarnings("unchecked")
		List<WpPrjMpRsltDVo> wpPrjMpRsltDVoList = (List<WpPrjMpRsltDVo>)commonSvc.queryList(wpPrjMpRsltDVo);
		usrMpDetails.setRsltDList(wpPrjMpRsltDVoList);
		
		// 계획 과 결과에 있는 프로잭트만 조회
		List<String> prjNoList = usrMpDetails.getPrjNoList();
		if(prjNoList!=null && !prjNoList.isEmpty()){
			WpPrjBVo wpPrjBVo = new WpPrjBVo();
			wpPrjBVo.setPrjNoList(prjNoList);
			wpPrjBVo.setOrderBy("STRT_YMD DESC, PRJ_NO DESC");
			@SuppressWarnings("unchecked")
			List<WpPrjBVo> wpPrjBVoList = (List<WpPrjBVo>)commonSvc.queryList(wpPrjBVo);
			model.put("wpPrjBVoList", wpPrjBVoList);
		}
		
		return LayoutUtil.getJspPath("/wp/listUsrPrjMm");
	}
	
	/** [개인별 집계] */
	@RequestMapping(value = "/wp/listUsrMm")
	public String listUsrMm(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "mpId", required = false) String mpId,
			@RequestParam(value = "mpTypCd", required = false) String mpTypCd,
			@RequestParam(value = "strtYmd", required = false) String strtYmd,
			@RequestParam(value = "endYmd", required = false) String endYmd,
			ModelMap model, Locale locale) throws SQLException, CmException{
		

		if(strtYmd==null){
			strtYmd = getYmd(-36);// 디폴트 시작일 - 5년전 해당월 1일
		}
		model.put("strtYmd", strtYmd);
		
		boolean valid = false;
		if(mpId!=null && !mpId.isEmpty()){
			if("emp".equals(mpTypCd)){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(mpId);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				if(orUserBVo != null){
					model.put("orUserBVo", orUserBVo);
					valid = true;
				}
			} else if("out".equals(mpTypCd)){
				WpMpBVo wpMpBVo = new WpMpBVo();
				wpMpBVo.setMpId(mpId);
				wpMpBVo = (WpMpBVo)commonSvc.queryVo(wpMpBVo);
				if(wpMpBVo != null){
					model.put("wpMpBVo", wpMpBVo);
					valid = true;
				}
			}
		}
		if(!valid){
			return LayoutUtil.getJspPath("/wp/listUsrPrjMm");
		}
		
		// 기간 시작 - 해당월 1일로 변경
		if(strtYmd!=null && !strtYmd.isEmpty() && strtYmd.length()==10){
			if(!strtYmd.endsWith("01")){
				strtYmd = strtYmd.substring(0, 8)+"01";
			}
		}
		// 기간 종료 - 해당월의 익월 1일로 변경
		if(endYmd!=null && !endYmd.isEmpty() && endYmd.length()==10){
			int year = Integer.parseInt(endYmd.substring(0, 4));
			int month = Integer.parseInt(endYmd.substring(5, 7));
			if(month==12){
				year++;
				month = 1;
			} else {
				month++;
			}
			endYmd = year+"-"+(month>9 ? "" : "0")+month+"-01";
		}
		

		Map<Integer, String> prjNmMap = new HashMap<Integer, String>();
		
		WpPrjBVo wpPrjBVo = new WpPrjBVo();
		wpPrjBVo.setMpId(mpId);
		if(strtYmd!=null && !strtYmd.isEmpty()){
			wpPrjBVo.setStrtYmd(strtYmd);
		}
		if(endYmd!=null && !endYmd.isEmpty()){
			wpPrjBVo.setEndYmd(endYmd);
		}
		wpPrjBVo.setOrderBy("STRT_YMD DESC");
		@SuppressWarnings("unchecked")
		List<WpPrjBVo> wpPrjBVoList = (List<WpPrjBVo>)commonSvc.queryList(wpPrjBVo);
		if(wpPrjBVoList != null && !wpPrjBVoList.isEmpty()){
			// 프로잭트 정보
			model.put("wpPrjBVoList", wpPrjBVoList);
			
			List<String> prjNoList = new ArrayList<String>();
			for(WpPrjBVo vo : wpPrjBVoList){
				prjNoList.add(vo.getPrjNo());
				prjNmMap.put(Integer.valueOf(vo.getPrjNo()), vo.getPrjNm());
			}
			
			// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블 - 프로잭트 역할, MP_MM_SUM:인력멘먼스합계
			Map<Integer, WpPrjMpPlanDVo> planDMap = new HashMap<Integer, WpPrjMpPlanDVo>();
			WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
			wpPrjMpPlanDVo.setPrjNoList(prjNoList);
			wpPrjMpPlanDVo.setMpId(mpId);
			@SuppressWarnings("unchecked")
			List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
			if(wpPrjMpPlanDVoList!=null && !wpPrjMpPlanDVoList.isEmpty()){
				for(WpPrjMpPlanDVo vo : wpPrjMpPlanDVoList){
					planDMap.put(Integer.valueOf(vo.getPrjNo()), vo);
				}
				model.put("planDMap", planDMap);
			}
			
			// 프로잭트인력결과상세(WP_PRJ_MP_RSLT_D) 테이블 - 결과MM 합계
			Map<Integer, WpPrjMpRsltDVo> rsltDMap = new HashMap<Integer, WpPrjMpRsltDVo>();
			WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
			wpPrjMpRsltDVo.setPrjNoList(prjNoList);
			wpPrjMpRsltDVo.setMpId(mpId);
			wpPrjMpRsltDVo.setInstanceQueryId("com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.selectRsltMmSumByPrjNo");
			@SuppressWarnings("unchecked")
			List<WpPrjMpRsltDVo> wpPrjMpRsltDVoList = (List<WpPrjMpRsltDVo>)commonSvc.queryList(wpPrjMpRsltDVo);
			if(wpPrjMpRsltDVoList!=null && !wpPrjMpRsltDVoList.isEmpty()){
				for(WpPrjMpRsltDVo vo : wpPrjMpRsltDVoList){
					rsltDMap.put(Integer.valueOf(vo.getPrjNo()), vo);
				}
				model.put("rsltDMap", rsltDMap);
			}
			
			
			WpPrjMpRsltLVo wpPrjMpRsltLVo = new WpPrjMpRsltLVo();
			wpPrjMpRsltLVo.setPrjNoList(prjNoList);
			wpPrjMpRsltLVo.setMpId(mpId);
			wpPrjMpRsltLVo.setOrderBy("RSLT_YMD DESC, PRJ_NO DESC");
			@SuppressWarnings("unchecked")
			List<WpPrjMpRsltLVo> wpPrjMpRsltLVoList = (List<WpPrjMpRsltLVo>)commonSvc.queryList(wpPrjMpRsltLVo);
			
			if(wpPrjMpRsltLVoList !=null && !wpPrjMpRsltLVoList.isEmpty()){
				
				// 각 데이터를 날자키로 맵에 담음
				Map<String, WpPrjMpRsltLVo> rsltLMap = new HashMap<String, WpPrjMpRsltLVo>();
				model.put("rsltLMap", rsltLMap);
				
				int startIndexVa = 0;
				int colorIndex = Math.abs(StringUtil.getNextInt()) % colors.length;
				int colorIndexVa;
				Integer colorIndexObj;
				double rsltMd;
				Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
				String prjNo=null, oldPrjNo=null, rsltYmd, prjNm=null, prjColor=null;
				WpPrjMpRsltLVo dupVo;
				
				for(WpPrjMpRsltLVo vo : wpPrjMpRsltLVoList){
					
					prjNo = vo.getPrjNo();
					if(oldPrjNo==null || !prjNo.equals(oldPrjNo)){
						oldPrjNo = prjNo;
						prjNm = prjNmMap.get(Integer.valueOf(prjNo));
						
						colorIndexObj = colorMap.get(Integer.valueOf(prjNo));
						if(colorIndexObj == null){
							colorMap.put(Integer.valueOf(prjNo), colorIndex);
							colorIndexObj = colorIndex;
							colorIndex++;
							if(colorIndex >= colors.length) colorIndex = 0;
						}
						colorIndexVa = colorIndexObj.intValue();
						
						if(colorIndexVa+startIndexVa >= colors.length){
							prjColor = colors[colorIndexVa + startIndexVa - colors.length];
						} else {
							prjColor = colors[colorIndexVa + startIndexVa];
						}
					}
					
					vo.setPrjNm(prjNm);
					vo.setColor(prjColor);
					
					rsltYmd = vo.getRsltYmd().substring(0, 10);
					
					dupVo = rsltLMap.get(rsltYmd);
					if(dupVo == null){
						rsltLMap.put(rsltYmd, vo);
					} else {
						dupVo.setColor(dupColor);
						
						rsltMd = Double.parseDouble(dupVo.getRsltMd()) + Double.parseDouble(vo.getRsltMd());
						rsltMd = Math.round(rsltMd * 10.0) / 10.0;
						dupVo.setRsltMd(Double.toString(rsltMd));
						dupVo.setPrjNm(dupVo.getPrjNm()+" / "+prjNm);
					}
				}
				
				// 달력 준비
				String currYmd = StringUtil.getCurrYmd();
				String calStartYmd = wpPrjMpRsltLVoList.get(0).getRsltYmd().substring(0, 10);
				if(endYmd==null || endYmd.isEmpty()){//파라미터
					if(calStartYmd.compareTo(currYmd) < 0){ // 역순이기 때문에 종료일임
						calStartYmd = currYmd;
					}
				}
				
				int startY = Integer.parseInt(calStartYmd.substring(0, 4));
				int startM = Integer.parseInt(calStartYmd.substring(5, 7));
				
				String calEndYmd = wpPrjMpRsltLVoList.get(wpPrjMpRsltLVoList.size()-1).getRsltYmd().substring(0, 10);
				int endY = Integer.parseInt(calEndYmd.substring(0, 4));
				int endM = Integer.parseInt(calEndYmd.substring(5, 7));
				
				GregorianCalendar calendar = new GregorianCalendar();
				
				Map<String, Integer> lastDayOfMonthMap = new HashMap<String, Integer>();
				model.put("lastDayOfMonthMap", lastDayOfMonthMap);
				Map<String, Integer> firstDayOfWeekMap = new HashMap<String, Integer>();
				model.put("firstDayOfWeekMap", firstDayOfWeekMap);
				List<String> yearMonthList = new ArrayList<String>();
				model.put("yearMonthList", yearMonthList);
				
				String yearMonth;
				for(int y = startY; y>=endY; y--){
					for(int m = (y==startY ? startM : 12);
						m>0 && (y>endY || (y==endY && m>=endM));
						m-- ){
						
						yearMonth = y+"-"+(m<10?"0":"")+m;
						
						yearMonthList.add(yearMonth);
						lastDayOfMonthMap.put(yearMonth, getLastDayOfMonth(y, m));
						firstDayOfWeekMap.put(yearMonth, getFirstDayOfWeek(calendar, y, m));
					}
				}
				
				// 일정관리 공휴일
				UserVo userVo = LoginSession.getUser(request);
				String realStrtDt = addMonthDay(calEndYmd, -2, 0);
				String end = addMonthDay(calStartYmd, 0, 1);
				String natCd = wcScdManagerSvc.getNatCd(userVo);
				List<String> excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), userVo.getLangTypCd(), natCd, realStrtDt, end, null);
				
				if(excludeList != null){
					List<String> holidayList = new ArrayList<String>();
					for(String dt : excludeList){
						holidayList.add(dt.substring(0,4)+"-"+dt.substring(4,6)+"-"+dt.substring(6,8));
					}
					model.put("holidayList", holidayList);
				}
			}
			
		}
		
		return LayoutUtil.getJspPath("/wp/listUsrMm");
	}
	
	/** 현제일 에서 파라미터 월을 더한 1일을 리턴 */
	private String getYmd(int monthAdd){
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1 + monthAdd;
		
		if(monthAdd > 0){
			while(month > 12){
				month -= 12;
				year++;
			}
		} else if(monthAdd < 0){
			while(month < 1){
				month += 12;
				year--;
			}
		}
		return year+"-"+(month>9 ? "" : "0")+month+"-01";
	}
	
	/** 개인별 수행 내역 색상 - 중복색 - 두개 이상의 프로잭트에 속할 경우 표시되는 색 - 연회색 */
	private static String dupColor = "#E0E0E0";
	/** 개인별 수행 내역 색상 - 색상표 - 프로잭트별로 다르게 할당 할 색 */
	private static String[] colors = {
		"#FDD2D0", "#FADAC5", "#FCE6AB", "#EFED9E", "#E7EEB5", 
		"#E1F2D5", "#D1F5E8", "#C4F3F9", "#DDEDFB", "#E6EAFD", 
		"#EBE4FC", "#F1DBF9", "#FCDEF0", "#F7D9E3"};
	
	
	/** [프로잭트별 집계] - 프로잭트 공수 요약 */
	@RequestMapping(value = "/wp/listPrjMp")
	public String listPrjMp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			ModelMap model, Locale locale) throws Exception {
		
		// 프로잭트기본(WP_PRJ_B) 테이블
		WpPrjBVo wpPrjBVo = new WpPrjBVo();
		wpPrjBVo.setPrjNo(prjNo);
		wpPrjBVo= (WpPrjBVo)commonSvc.queryVo(wpPrjBVo);
		model.put("wpPrjBVo", wpPrjBVo);
		
		// 화면 구성용 데이터
		PrjMpDetails prjMpDetails = new PrjMpDetails(wpPrjBVo.getStrtYmd());
		model.put("prjMpDetails", prjMpDetails);
		
		// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블
		WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
		wpPrjMpPlanDVo.setPrjNo(prjNo);
		wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
		prjMpDetails.setPlanDList(wpPrjMpPlanDVoList);
		
		List<String> conMpIdList = new ArrayList<String>();
		List<WpPrjMpPlanDVo> conWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
		List<WpPrjMpPlanDVo> devWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
		if(wpPrjMpPlanDVoList != null && !wpPrjMpPlanDVoList.isEmpty()){
			for(WpPrjMpPlanDVo vo : wpPrjMpPlanDVoList){
				if("con".equals(vo.getPrjRole1Cd())){
					conWpPrjMpPlanDVoList.add(vo);
					conMpIdList.add(vo.getMpId());
				} else {
					devWpPrjMpPlanDVoList.add(vo);
				}
			}
		}
		model.put("conWpPrjMpPlanDVoList", conWpPrjMpPlanDVoList);
		model.put("devWpPrjMpPlanDVoList", devWpPrjMpPlanDVoList);
		
		// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블
		WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
		wpPrjMpPlanLVo.setPrjNo(prjNo);
		wpPrjMpPlanLVo.setOrderBy("MP_ID, M_NO");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanLVo> wpPrjMpPlanLVoList = (List<WpPrjMpPlanLVo>)commonSvc.queryList(wpPrjMpPlanLVo);
		prjMpDetails.setPlanLList(wpPrjMpPlanLVoList);
		
		
		WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
		wpPrjMpRsltDVo.setPrjNo(prjNo);
		wpPrjMpRsltDVo.setOrderBy("MP_ID, RSLT_YM");
		@SuppressWarnings("unchecked")
		List<WpPrjMpRsltDVo> wpPrjMpRsltDVoList = (List<WpPrjMpRsltDVo>)commonSvc.queryList(wpPrjMpRsltDVo);
		prjMpDetails.setRsltDList(wpPrjMpRsltDVoList);
		
		return LayoutUtil.getJspPath("/wp/listPrjMp");
	}
	
	/** [프로잭트별 집계] - 프로잭트 공수 상세(사용자별 일자별 출력) */
	@RequestMapping(value = "/wp/listPrjMpDetl")
	public String listPrjMpDetl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			ModelMap model, Locale locale) throws Exception {
		
		// 프로잭트기본(WP_PRJ_B) 테이블
		WpPrjBVo wpPrjBVo = new WpPrjBVo();
		wpPrjBVo.setPrjNo(prjNo);
		wpPrjBVo= (WpPrjBVo)commonSvc.queryVo(wpPrjBVo);
		model.put("wpPrjBVo", wpPrjBVo);
		
		// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블
		WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
		wpPrjMpPlanDVo.setPrjNo(prjNo);
		wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
		model.put("wpPrjMpPlanDVoList", wpPrjMpPlanDVoList);
		
		// 프로잭트인력결과내역(WP_PRJ_MP_RSLT_L) 테이블
		WpPrjMpRsltLVo wpPrjMpRsltLVo = new WpPrjMpRsltLVo();
		wpPrjMpRsltLVo.setPrjNo(prjNo);
		wpPrjMpRsltLVo.setOrderBy("MP_ID, RSLT_YMD");
		@SuppressWarnings("unchecked")
		List<WpPrjMpRsltLVo> wpPrjMpRsltLVoList = (List<WpPrjMpRsltLVo>)commonSvc.queryList(wpPrjMpRsltLVo);
		
		
		if(wpPrjMpRsltLVoList != null && !wpPrjMpRsltLVoList.isEmpty()){
			
			Map<String, Integer> lastDayOfMonthMap = new HashMap<String, Integer>();
			model.put("lastDayOfMonthMap", lastDayOfMonthMap);
			Map<String, Integer> firstDayOfWeekMap = new HashMap<String, Integer>();
			model.put("firstDayOfWeekMap", firstDayOfWeekMap);
			
			List<String> yearMonthList = null;
			Map<String, WpPrjMpRsltLVo> monthlyMap = null;
			String mpId=null, oldMpId=null, yearMonth, oldYearMonth=null, minYmd=null, maxYmd=null, rsltYmd=null, oldRsltYmd=null;
			int year, month;
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			for(WpPrjMpRsltLVo vo : wpPrjMpRsltLVoList){
				
				mpId = vo.getMpId();
				rsltYmd = vo.getRsltYmd().substring(0, 10);
				
				yearMonth = rsltYmd.substring(0, 7);
				
				if(oldMpId == null || !oldMpId.equals(mpId)){
					
					if(minYmd==null) minYmd = rsltYmd;
					else if(minYmd.compareTo(rsltYmd) > 0) minYmd = rsltYmd;
					
					if(maxYmd==null) maxYmd = rsltYmd;
					else if(oldRsltYmd!=null && maxYmd.compareTo(oldRsltYmd) < 0) maxYmd = rsltYmd;
					
					oldMpId = mpId;
					yearMonthList = new ArrayList<String>();
					model.put("yearMonthList"+mpId, yearMonthList);
					
					oldYearMonth = "";
					
					monthlyMap = new HashMap<String, WpPrjMpRsltLVo>();
					model.put("monthlyMap"+mpId, monthlyMap);
					
				}
				
				if(!yearMonth.equals(oldYearMonth)){
					oldYearMonth = yearMonth;
					yearMonthList.add(yearMonth);
					
					if(lastDayOfMonthMap.get(yearMonth)==null){
						
						year = Integer.parseInt(yearMonth.substring(0, 4));
						month = Integer.parseInt(yearMonth.substring(5, 7));
						
						lastDayOfMonthMap.put(yearMonth, getLastDayOfMonth(year, month));
						firstDayOfWeekMap.put(yearMonth, getFirstDayOfWeek(calendar, year, month));
					}
				}
				
				monthlyMap.put(rsltYmd, vo);
				oldRsltYmd = rsltYmd;
			}
			
			if(oldRsltYmd!=null && maxYmd.compareTo(oldRsltYmd) < 0) maxYmd = rsltYmd;
			
			// 일정관리 공휴일
			UserVo userVo = LoginSession.getUser(request);
			String realStrtDt = addMonthDay(minYmd, -2, 0);
			String end = addMonthDay(maxYmd, 0, 1);
			String natCd = wcScdManagerSvc.getNatCd(userVo);
			List<String> excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), userVo.getLangTypCd(), natCd, realStrtDt, end, null);
			
			if(excludeList != null){
				List<String> holidayList = new ArrayList<String>();
				for(String dt : excludeList){
					holidayList.add(dt.substring(0,4)+"-"+dt.substring(4,6)+"-"+dt.substring(6,8));
				}
				model.put("holidayList", holidayList);
			}
		}
		
		return LayoutUtil.getJspPath("/wp/listPrjMpDetl");
	}
	
	
	/** [AJAX] M/D 조회 */
	@RequestMapping(value = "/wp/getMdBetweenAjx")
	public String getMdBetweenAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			String start = (String)jsonObject.get("start");
			String end = (String)jsonObject.get("end");
			
			String realStrtDt = addMonthDay(start, -2, 0);
			String endDt = addMonthDay(end, 0, 1);
			String natCd = wcScdManagerSvc.getNatCd(userVo);
			List<String> excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), userVo.getLangTypCd(), natCd, realStrtDt, endDt, null);
			
			Long endTime = java.sql.Date.valueOf(end).getTime();
	    	
	    	GregorianCalendar calendar = new GregorianCalendar();
	    	calendar.setTime(java.sql.Date.valueOf(start));
	    	
	    	int week, mdSum=0;
	    	for(;calendar.getTime().getTime() <= endTime; calendar.add(Calendar.DAY_OF_MONTH, 1)){
	    		week = calendar.get(Calendar.DAY_OF_WEEK);
	    		if(week==1 || week==7) continue;
	    		
	    		if(excludeList!=null && excludeList.contains(toDateCompareString(calendar))) continue;
	    		mdSum++;
	    	}
	    	model.put("md", Integer.toString(mdSum));
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	private static int[] LAST_DAYS = { 31,28,31,30,31,30,31,31,30,31,30,31 };
	private int getLastDayOfMonth(int year, int month){
		if(month!=2){
			return LAST_DAYS[month-1];
		}
		if((year%400==0 || year%100!=0) && (year%4==0)) return 29;
		return 28;
	}
	
	private String addMonthDay(String ymd, int month, int day){
		GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(java.sql.Date.valueOf(ymd));
    	
    	if(month!=0){
    		calendar.add(Calendar.MONTH, month);
    	}
    	if(day!=0){
    		calendar.add(Calendar.DAY_OF_MONTH, day);
    	}
    	return toDateCompareString(calendar);
	}
	private Integer getFirstDayOfWeek(GregorianCalendar calendar, int year, int month){
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	private String toDateCompareString(GregorianCalendar calendar){
    	int y = calendar.get(Calendar.YEAR);
    	int m = calendar.get(Calendar.MONTH)+1;
    	int d = calendar.get(Calendar.DAY_OF_MONTH);
    	return y+(m<10?"0":"")+m+(d<10?"0":"")+d;
    }
}
