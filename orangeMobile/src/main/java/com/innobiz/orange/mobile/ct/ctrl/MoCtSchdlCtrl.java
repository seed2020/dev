package com.innobiz.orange.mobile.ct.ctrl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.calender.CtScdCalDay;
import com.innobiz.orange.web.ct.calender.CtScdCalMonth;
import com.innobiz.orange.web.ct.calender.CtScdCalWeek;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtScdManagerSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlCatClsBVo;
import com.innobiz.orange.web.ct.vo.CtSchdlPromGuestDVo;
import com.innobiz.orange.web.ct.vo.CtSchdlRepetSetupDVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;

/** 일정관리 */
@Controller
public class MoCtSchdlCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoCtSchdlCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtScdManagerSvc ctScdManagerSvc;
	
	/** 게시파일 서비스 */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** schdlID생성 서비스*/
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 일정 보기 */
	@RequestMapping(value = "/ct/schdl/listNewSchdl")
	public String listNewSchdl(HttpServletRequest request,			
			@RequestParam(value="action",required=false)
			String action,
			ModelMap model) throws Exception {
		
		//UserVo userVo = LoginSession.getUser(request);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		//String tpl = request.getParameter("tpl");
		//String preView = request.getParameter("preView");
		String catId = request.getParameter("catId");
		if(catId!=null )
			if(catId.equals("")||catId.equals("undefined"))
					catId=null;
		
		
		
		String fncCal = request.getParameter("fncCal");
		if(fncCal == null ) fncCal ="psn";		
		String tabNo = request.getParameter("tabNo");	
		String timeZone=TimeZone.getDefault().getID();
	
		
		//Action 이 null
		if(action==null){
			Calendar currentCal = Calendar.getInstance();
			int defYear = currentCal.get(Calendar.YEAR);		
			int currentMonth = currentCal.get(Calendar.MONTH) +1;
			int currentWeek = currentCal.get(Calendar.WEEK_OF_MONTH); 
			int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
			
			CtScdCalMonth scdMonth = ctScdManagerSvc.getScdMonth(defYear, currentMonth,timeZone,ctId,catId,true);
			CtScdCalWeek scdWeek = ctScdManagerSvc.getScdWeek(defYear, currentMonth, currentWeek,timeZone,ctId,catId);
			CtScdCalDay scdDay = ctScdManagerSvc.getScdDay(defYear, currentMonth, currentDay,timeZone,ctId,catId);
					
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);		
			model.put("wcScdToday", currentDay);
		}else if(action.equals("")){
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));
			int welyYear = Integer.parseInt(request.getParameter("welyYear"));
			int welyMonth = Integer.parseInt(request.getParameter("welyMonth"));
			int welyWeek = Integer.parseInt(request.getParameter("welyWeek"));
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
			CtScdCalMonth scdMonth = ctScdManagerSvc.getScdMonth(molyYear, molyMonth,timeZone,ctId,catId,true);
			CtScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek, scdMonth):ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,timeZone,ctId,catId);
			CtScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay, scdMonth):ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,timeZone,ctId,catId);
					
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}else if(action.equals("after")){			
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));
			int welyYear = Integer.parseInt(request.getParameter("welyYear"));
			int welyMonth = Integer.parseInt(request.getParameter("welyMonth"));
			int welyWeek = Integer.parseInt(request.getParameter("welyWeek"));
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
			if(tabNo.equals("0")){				
				if(12 >= molyMonth + 1)	molyMonth += 1;
				else{
					molyYear += 1;
					molyMonth = 1;
				}				
			}else if(tabNo.equals("1")){
				
				Calendar cal = Calendar.getInstance();
				cal.setFirstDayOfWeek(Calendar.SUNDAY);				
				cal.set(Calendar.YEAR, welyYear);
				cal.set(Calendar.MONTH, welyMonth-1);
				cal.set(Calendar.DATE, 1);
		
				int start_day = cal.get(Calendar.DAY_OF_WEEK);
				int end_day = cal.getActualMaximum(Calendar.DATE);

				int findSixWeek = end_day + start_day ;
				int endweek=end_day/7;
				
				if(end_day%7!=0||start_day>1){				
					if(findSixWeek > 36){
						endweek=endweek+2;
					}else{
						endweek=endweek+1;
					}
				}
				if(endweek >= welyWeek + 1) welyWeek += 1;
				else{
					if(12 >= welyMonth + 1)	welyMonth += 1;
					else{
						welyYear += 1;
						welyMonth = 1;
					}
					welyWeek = 1;
				}
			}else if(tabNo.equals("2")){
				Calendar cal = Calendar.getInstance();
				cal.setFirstDayOfWeek(Calendar.SUNDAY);
				cal.set(Calendar.YEAR, dalyYear);
				cal.set(Calendar.MONTH, dalyMonth-1);
				cal.set(Calendar.DATE, 1);
				int end_day = cal.getActualMaximum(Calendar.DATE);
				if(end_day >= dalyDay + 1) dalyDay += 1;
				else{
					if(12 >= dalyMonth + 1)	dalyMonth += 1;
					else{
						dalyYear += 1;
						dalyMonth = 1;
					}
					dalyDay = 1;
				}
				
				
			}			
		
			CtScdCalMonth scdMonth = ctScdManagerSvc.getScdMonth(molyYear, molyMonth,timeZone,ctId,catId,true);
			CtScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek, scdMonth):ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,timeZone,ctId,catId);
			CtScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay, scdMonth):ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,timeZone,ctId,catId);
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}else if(action.equals("before")){
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));
			int welyYear = Integer.parseInt(request.getParameter("welyYear"));
			int welyMonth = Integer.parseInt(request.getParameter("welyMonth"));
			int welyWeek = Integer.parseInt(request.getParameter("welyWeek"));
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
			
			if(tabNo.equals("0")){			
				if(0 < molyMonth - 1) molyMonth -= 1;
				else{
					molyYear -= 1;
					molyMonth = 12;
				}				
			}else if(tabNo.equals("1")){
				Calendar cal = Calendar.getInstance();
				cal.setFirstDayOfWeek(Calendar.SUNDAY);

				if(0 < welyWeek - 1) welyWeek -= 1;
				else{
					if(0 < welyMonth - 1) welyMonth -= 1;
					else{
						welyYear -= 1;
						welyMonth = 12;
					}					
					cal.set(Calendar.YEAR, welyYear);
					cal.set(Calendar.MONTH, welyMonth-1);
					cal.set(Calendar.DATE, 1);
					int start_day = cal.get(Calendar.DAY_OF_WEEK);
					int end_day = cal.getActualMaximum(Calendar.DATE);
					int findSixWeek = end_day + start_day ;
					int endweek=end_day/7;					
					if(end_day%7!=0||start_day>1){					
						if(findSixWeek > 36){
							endweek=endweek+2;
						}else{
							endweek=endweek+1;
						}
					}
					welyWeek = endweek;
				}			

			}else if(tabNo.equals("2")){
				Calendar cal = Calendar.getInstance();
				cal.setFirstDayOfWeek(Calendar.SUNDAY);
				if(0 < dalyDay - 1) dalyDay -= 1;
				else{
					if(0 < dalyMonth - 1) dalyMonth -= 1;
					else{
						dalyYear -= 1;
						dalyMonth = 12;
					}
					cal.set(Calendar.YEAR, dalyYear);
					cal.set(Calendar.MONTH, dalyMonth-1);
					cal.set(Calendar.DATE, 1);

					int end_day = cal.getActualMaximum(Calendar.DATE);
					dalyDay = end_day;
				}
			}

			CtScdCalMonth scdMonth = ctScdManagerSvc.getScdMonth(molyYear, molyMonth,timeZone,ctId,catId,true);
			CtScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek, scdMonth):ctScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,timeZone,ctId,catId);
			CtScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay, scdMonth):ctScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,timeZone,ctId,catId);
			
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}
		//
		/*if("my".equals(fncCal)){
			String langTypCd = LoginSession.getLangTypCd(request);
		}*/
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 일정종류 목록 조회
		CtSchdlCatClsBVo wcCatClsBVo = new CtSchdlCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(ctEstbBVo.getCompId());
		wcCatClsBVo.setUseYn("Y");
		@SuppressWarnings("unchecked")
		List<CtSchdlCatClsBVo> wcCatClsBVoList = (List<CtSchdlCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		//일정종류(카테고리)를 model 에 담는다
		for(CtSchdlCatClsBVo storedWcCatClsBVo : wcCatClsBVoList){
			model.put("cat_"+storedWcCatClsBVo.getCatId(), VoUtil.toMap(storedWcCatClsBVo, null));
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		String ctFncUid = request.getParameter("menuId");
		//저장 "W"/ 삭제"D"
		ctCmntSvc.putAuthChk(request, model, "W", ctId, ctFncUid);		
		//ctCmntSvc.putLeftMenu(request, ctId, model);
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCompId(ctEstbBVo.getCompId());
		ctFncDVo.setCtId(ctId);
		ctFncDVo.setLangTyp(langTypCd);
		ctFncDVo.setCtFncId("CTHOME");
		ctFncDVo = (CtFncDVo)commonSvc.queryVo(ctFncDVo);
		model.put("ctFncDVo", ctFncDVo);
		return MoLayoutUtil.getJspPath("/ct/schdl/listNewSchdl", "wc");
	}
	
	/** 조회 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/viewSchdl")
	public String viewSchdl(HttpServletRequest request,
			@Parameter(name="schdlId", required=false) String schdlId,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		model.put("plt", request.getParameter("plt"));
		model.put("listPage", "listNewSchdl");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
		ctSchdlBVo.setSchdlId(schdlId);
		ctSchdlBVo.setUserUid(null);
		ctSchdlBVo.setWithLob(true);
		ctSchdlBVo =(CtSchdlBVo)commonDao.queryVo(ctSchdlBVo);
		// schdlPID로 count검색
		CtSchdlBVo scdPidVo = new CtSchdlBVo();		
		scdPidVo.setSchdlPid(ctSchdlBVo.getSchdlPid());
		scdPidVo.setRepetYn("Y");// 반복여부
		model.put("scdPidCount", commonDao.count(scdPidVo));

		ctFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		
		if("viewAllSchdlPop".equals(path)){
			userAuth ="fail";
		}else{
			if(ctSchdlBVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())) userAuth = "pass";//등록Uid와 로그인Uid와 비교
		}
		
		model.put("CtSchdlBVo", ctSchdlBVo);
		
		//반복설정여부
		if(ctSchdlBVo.getRepetYn() != null && "Y".equals(ctSchdlBVo.getRepetYn()) && ctSchdlBVo.getSchdlPid() != null ){
			CtSchdlRepetSetupDVo wcRepetSetupDVo = new CtSchdlRepetSetupDVo();
			wcRepetSetupDVo.setSchdlId(ctSchdlBVo.getSchdlPid());
			wcRepetSetupDVo =(CtSchdlRepetSetupDVo)commonDao.queryVo(wcRepetSetupDVo);
			model.put("wcRepetSetupDVo", wcRepetSetupDVo);
		}
		
		model.put("userAuth", userAuth);
		
		//참석자 조회
		CtSchdlPromGuestDVo CtSchdlPromGuestDVo = new CtSchdlPromGuestDVo();
		CtSchdlPromGuestDVo.setSchdlId(schdlId);
		@SuppressWarnings("unchecked")
		List<CtSchdlPromGuestDVo> CtSchdlPromGuestDVoList = (List<CtSchdlPromGuestDVo>)commonDao.queryList(CtSchdlPromGuestDVo);
		model.put("CtSchdlPromGuestDVoList", CtSchdlPromGuestDVoList);
		
		model.put("scds_schdlId", schdlId);
		
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		model.put("params", ParamUtil.getQueryString(request));
		model.put("paramsForList", ParamUtil.getQueryString(request, "menuId","schdlId","ctId","noCache"));
		ctCmntSvc.putAuthChk(request, model, "D", request.getParameter("ctId"), request.getParameter("menuId"));
		if(ctSchdlBVo.getRegrUid().equals(userVo.getUserUid())){
			model.put("authChkD", "D");
		}
		return MoLayoutUtil.getJspPath("/ct/schdl/viewSchdl","ct");		
	}     
	
	/** 등록 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/setSchdl")
	public String setSchdlPop(HttpServletRequest request,
			@RequestParam(value="schdlId",required=false) String schdlId,
			@RequestParam(value="schdlStartDt",required=false) String schdlStartDt,
			ModelMap model) throws Exception{
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		model.put("listPage", "listNewSchdl");
		
		UserVo userVo = LoginSession.getUser(request);
		
		CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
		//VoUtil.bind(request, CtSchdlBVo);
		
		List<CtSchdlPromGuestDVo> CtSchdlPromGuestDVoList = null;
		
		//수정
		if(schdlId != null && !"".equals(schdlId)){
			ctSchdlBVo.setSchdlId(schdlId);
			ctSchdlBVo.setUserUid(null);
			ctSchdlBVo.setWithLob(true);
			ctSchdlBVo = (CtSchdlBVo)commonDao.queryVo(ctSchdlBVo);
			
			CtSchdlBVo scdPidVo = new CtSchdlBVo();
			scdPidVo.setSchdlPid(ctSchdlBVo.getSchdlPid());
			scdPidVo.setRepetYn("Y");// 반복여부
			model.put("scdPidCount", commonDao.count(scdPidVo));
			
			CtSchdlPromGuestDVo CtSchdlPromGuestDVo = new CtSchdlPromGuestDVo();
			CtSchdlPromGuestDVo.setSchdlId(schdlId);
			CtSchdlPromGuestDVoList = ctScdManagerSvc.getPromGuestLst(CtSchdlPromGuestDVo);
			
			//model.addAttribute("JS_OPTS", new String[]{"editor"});
		}else{
			Calendar cal = Calendar.getInstance();
			Integer hour = null;
			if(schdlStartDt != null && !"".equals(schdlStartDt)){
				//String schdlStartDt = CtSchdlBVo.getSchdlStartDt(); 
				int year = Integer.parseInt(schdlStartDt.substring(0, 4)); 
				int month = Integer.parseInt(schdlStartDt.substring(4, 6)); 
				int day = Integer.parseInt(schdlStartDt.substring(6,8));
				if(schdlStartDt.substring(6).length() > 2){
					hour = Integer.parseInt(schdlStartDt.substring(8,10));
				}
	            cal.set(Calendar.YEAR, year);
	            cal.set(Calendar.MONTH, month-1); // 0 이 1월, 1 은 2월, .... 
	            cal.set(Calendar.DAY_OF_MONTH, day);
			}
			if(hour != null){
				cal.set(Calendar.HOUR_OF_DAY, hour);
			}else{
				cal.add(Calendar.HOUR_OF_DAY, 1);
			}
			
            cal.set(Calendar.MINUTE, 0);
			//시작일자 세팅
			ctSchdlBVo.setSchdlStartDt(new Timestamp(cal.getTimeInMillis()).toString());
            cal.add(Calendar.HOUR_OF_DAY, 1);
            //종료일자 세팅
            ctSchdlBVo.setSchdlEndDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
		ctFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		model.put("ctSchdlBVo", ctSchdlBVo);
		
		/*// UI 구성용 - 빈 VO 하나 더함
		if(CtSchdlPromGuestDVoList == null) CtSchdlPromGuestDVoList = new ArrayList<CtSchdlPromGuestDVo>();
		CtSchdlPromGuestDVoList.add(new CtSchdlPromGuestDVo());
		*/
		model.put("wcPromGuestDVoList", CtSchdlPromGuestDVoList);		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 일정종류 목록 조회
		CtSchdlCatClsBVo wcCatClsBVo = new CtSchdlCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(ctEstbBVo.getCompId());
		wcCatClsBVo.setUseYn("Y");
		@SuppressWarnings("unchecked")
		List<CtSchdlCatClsBVo> wcCatClsBVoList = (List<CtSchdlCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		model.put("params", ParamUtil.getQueryString(request));
		model.put("paramsForList", ParamUtil.getQueryString(request, "menuId","schdlId","ctId","schdlStartDt","noCache"));
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		// 타사 조직도 조회
		if(ctEstbBVo.getAllCompYn()!=null && "Y".equals(ctEstbBVo.getAllCompYn())){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
		return MoLayoutUtil.getJspPath("/ct/schdl/setSchdl","ct");
	}
	
	/** 등록 수정 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/transSchdlPost" , method = RequestMethod.POST)
	public String transSchdlPost(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;				

		try {	// Multipart 파일 업로드
			
			uploadHandler = ctFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String ctId = ParamUtil.getRequestParam(request, "ctId", true);

			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			//하나의 트랜 잭션으로 묶기 위한 List
			QueryQueue queryQueue = new QueryQueue();
			
			//CtSchdlBVo 일정기본
			CtSchdlBVo ctschdlbVo = new CtSchdlBVo();
			VoUtil.bind(request, ctschdlbVo);
			
			try {
				//schdlId 생성
				ctschdlbVo.setSchdlId(ctCmSvc.createId("CT_SCHDL_B"));
				ctschdlbVo.setSchdlPid(ctschdlbVo.getSchdlId());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
			// 세션의 언어코드
			UserVo userVo = LoginSession.getUser(request);
			
			//커뮤니티 기능의 UID 및 정보 입력
			ctschdlbVo.setCtFncUid(request.getParameter("menuId"));
			CtFncDVo ctFuncDvo=ctCmSvc.getCtFncDVo(userVo, ctschdlbVo.getCtFncUid(), ctschdlbVo.getCtId());
			ctschdlbVo.setCtFncId(ctFuncDvo.getCtFncId());
			ctschdlbVo.setCtFncLocStep(ctFuncDvo.getCtFncLocStep());
			ctschdlbVo.setCtFncPid(ctFuncDvo.getCtFncPid());
			
			// 게시파일 저장
			List<CommonFileVo> deletedFileList = ctFileSvc.saveScdlFile(request, ctschdlbVo.getSchdlId(), queryQueue, "indiv");				
			
			int fileSaveSize=queryQueue.size();
			//=================================================================
			//schdl Param값
			//=================================================================
			//String emailSend = request.getParameter("emailSend");
			//=================================================================
			//=================================================================
			//CtSchdlBVo 일정 userVo값
			//=================================================================
			ctschdlbVo.setCompId(ctEstbBVo.getCompId());
			
			ctschdlbVo.setUserUid(userVo.getUserUid());
			if(request.getParameter("agnt")!=null&&!request.getParameter("agnt").equals("")&&!request.getParameter("agnt").equals("-1"))
				ctschdlbVo.setUserUid(request.getParameter("agnt"));
			
			ctschdlbVo.setDeptId(userVo.getDeptId());
//			wcsVo.setGrpId(userVo.getGradeCd());
			ctschdlbVo.setRegrUid(userVo.getUserUid());
			ctschdlbVo.setRegrNm(userVo.getUserNm());
			
			if(ctschdlbVo.getAlldayYn() == null || ctschdlbVo.getAlldayYn().isEmpty()) ctschdlbVo.setAlldayYn("N");
			
			if(ctschdlbVo.getSchdlStrtTime() == null || ctschdlbVo.getSchdlStrtTime().isEmpty() || "Y".equals(ctschdlbVo.getAlldayYn())) ctschdlbVo.setSchdlStrtTime("00:00");
			if(ctschdlbVo.getSchdlEndTime() == null || ctschdlbVo.getSchdlEndTime().isEmpty() || "Y".equals(ctschdlbVo.getAlldayYn())) ctschdlbVo.setSchdlEndTime("00:00");
			
			//시작 일 +시 간
			ctschdlbVo.setSchdlStartDt(ctschdlbVo.getSchdlStrtYmd() + " " + ctschdlbVo.getSchdlStrtTime());
			//종료 일 +시간
			ctschdlbVo.setSchdlEndDt(ctschdlbVo.getSchdlEndYmd() + " " + ctschdlbVo.getSchdlEndTime());
		
			//일정구분코드  1.약속
			//ct/schdlsVo.setSchdlTypCd("1");
			//일정종류코드 1.나의일정, 2.그룹, 3.회사 4.부서
			//ct/schdlsVo.setSchdlKndCd(String.valueOf(schdlTyp));
			//양음력 값
			
			if(ctschdlbVo.getSolaLunaYn() == null || "".equals(ctschdlbVo.getSolaLunaYn())) ctschdlbVo.setSolaLunaYn("Y");
			
			//휴일여부 NULL체크
			ctschdlbVo.setHoliYn("N");
			//일정상태코드 T보류/C참석/N불참/D약속취소
			ctschdlbVo.setSchdlStatCd("T");
			
			ctschdlbVo.setAttYn(fileSaveSize>0?"Y":"N");
			
//			wcsVo.setSchdlFileId(fileSaveSize>0?schdlId:null);
			
			//=================================================================
			//Repet Param값
			//=================================================================
			String repetSetup = request.getParameter("repetSetup");
			String repetKnd = request.getParameter("repetKnd");
			String dalySelect = request.getParameter("dalySelect");
			String welySelect = request.getParameter("welySelect");
			String dow = request.getParameter("dow");
			String molyKnd = request.getParameter("molyKnd");
			String firMolySelect = request.getParameter("firMolySelect");
			String firMolyDaySelect = request.getParameter("firMolyDaySelect");
			String secMolySelect = request.getParameter("secMolySelect");
			String secMolyWeekSelect = request.getParameter("secMolyWeekSelect");
			String secMolyWeekOfDaySelect = request.getParameter("secMolyWeekOfDaySelect");
			String yelyKnd = request.getParameter("yelyKnd");
			String firYelySelect = request.getParameter("firYelySelect");
			String firYelyDaySelect = request.getParameter("firYelyDaySelect");
			String secYelySelect = request.getParameter("secYelySelect");
			String secYelyWeekSelect = request.getParameter("secYelyWeekSelect");
			String secYelyWeekOfDaySelect = request.getParameter("secYelyWeekOfDaySelect");
			String reStartDt = request.getParameter("repetchoiDt");
			String reEndDt = request.getParameter("repetcmltDt");
			
			//일정반복여부
			ctschdlbVo.setRepetYn(repetSetup);
			
			queryQueue.insert(ctschdlbVo);
			
			// 파일 삭제
			ctFileSvc.deleteDiskFiles(deletedFileList);
			
			//=================================================================
			//CtSchdlPromGuestDVo 참석자
			//=================================================================
			// 참석자 목록 저장
			List<CtSchdlPromGuestDVo> CtSchdlPromGuestDVoList = ctScdManagerSvc.saveGuest(request, queryQueue, ctschdlbVo);
			
			//=================================================================
			//CtSchdlBVo 일정기본
			//=================================================================
			/** 
			 * [반복주기코드] 
			 * 반복일간 :EV_DY
			 * 반복주간 :EV_WK
			 * 반복일월간 : EV_DY_MY
			 * 반복주월간 : EV_WK_MT
			 * 반복일년간 : EV_DY_YR
			 * 반복주년간 : EV_WK_YR
			 * */
			CtSchdlRepetSetupDVo wcRepetVo = new CtSchdlRepetSetupDVo();
			if(repetSetup != null && repetSetup.equalsIgnoreCase("Y")){
				if(repetKnd != null && repetKnd.equalsIgnoreCase("DALY")){
					wcRepetVo.setRepetPerdCd("EV_DY");
					wcRepetVo.setRepetDd(dalySelect);
				}else if(repetKnd != null && repetKnd.equalsIgnoreCase("WELY")){
					wcRepetVo.setRepetPerdCd("EV_WK");
					wcRepetVo.setRepetWk(welySelect);
					wcRepetVo.setApntDy(dow);
				}else if(repetKnd != null && repetKnd.equalsIgnoreCase("MOLY")){
					if(molyKnd != null && molyKnd.equalsIgnoreCase("1")){
						wcRepetVo.setRepetPerdCd("EV_DY_MY");
						wcRepetVo.setRepetMm(firMolySelect);
						wcRepetVo.setApntDd(firMolyDaySelect);
					}else if(molyKnd != null && molyKnd.equalsIgnoreCase("2")){
						wcRepetVo.setRepetPerdCd("EV_WK_MT");
						wcRepetVo.setRepetMm(secMolySelect);
						wcRepetVo.setApntWk(secMolyWeekSelect);
						wcRepetVo.setApntDy(secMolyWeekOfDaySelect);
					}
				}else if(repetKnd != null && repetKnd.equalsIgnoreCase("YELY")){
					if(yelyKnd != null && yelyKnd.equalsIgnoreCase("1")){
						wcRepetVo.setRepetPerdCd("EV_DY_YR");
						wcRepetVo.setRepetMm(firYelySelect);
						wcRepetVo.setApntDd(firYelyDaySelect);
					}else if(yelyKnd != null && yelyKnd.equalsIgnoreCase("2")){
						wcRepetVo.setRepetPerdCd("EV_WK_YR");
						wcRepetVo.setRepetMm(secYelySelect);
						wcRepetVo.setApntWk(secYelyWeekSelect);
						wcRepetVo.setApntDy(secYelyWeekOfDaySelect);
					}
				}
				
				wcRepetVo.setRepetSetup(repetSetup);
				wcRepetVo.setRepetStartDt(reStartDt);
				wcRepetVo.setRepetEndDt(reEndDt);
				wcRepetVo.setSchdlId(ctschdlbVo.getSchdlId());
				queryQueue.insert(wcRepetVo);
				ctScdManagerSvc.repetSetup(wcRepetVo,ctschdlbVo,CtSchdlPromGuestDVoList,queryQueue, request);
			}
			
			commonDao.execute(queryQueue);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			//이메일 발송
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) && 
					ctschdlbVo.getEmailSendYn() != null && "Y".equals(ctschdlbVo.getEmailSendYn())){
				
				List<String> toUserIds = new ArrayList<String>();
				if(CtSchdlPromGuestDVoList.size() > 0 ){
					for(CtSchdlPromGuestDVo storedCtSchdlPromGuestDVo : CtSchdlPromGuestDVoList){
						if(storedCtSchdlPromGuestDVo.getEmail() != null && !"".equals(storedCtSchdlPromGuestDVo.getEmail())){
							toUserIds.add(storedCtSchdlPromGuestDVo.getEmail());
						}
					}
				}
				if(toUserIds != null && toUserIds.size() > 0 ){
					String[] to_list = new String[toUserIds.size()];
					for(int i=0;i<toUserIds.size();i++){
						to_list[i] = toUserIds.get(i);
					}
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					
					// 일정종류 조회
					CtSchdlCatClsBVo wcCatClsBVo = new CtSchdlCatClsBVo();
					wcCatClsBVo.setQueryLang(langTypCd);
					wcCatClsBVo.setCompId(ctEstbBVo.getCompId());
					wcCatClsBVo.setCatId(ctschdlbVo.getSchdlTypCd());
					wcCatClsBVo = (CtSchdlCatClsBVo)commonSvc.queryVo(wcCatClsBVo);
					
					//일정종류
					String catNm = wcCatClsBVo.getCatNm();
					
					String subj = "["+catNm + "]"+ctschdlbVo.getSubj();
					String content = ctschdlbVo.getCont();
					
					String p1 = "<p>";
					String span1 = "<span style='font-weight: bold;'>";
					String span2 = "</span>";
					String p2 = "</p>";
					
					// 이메일 내용 생성
					StringBuilder sb = new StringBuilder();
					
					String strtYmd = ctschdlbVo.getSchdlStrtYmd();
					String endYmd = ctschdlbVo.getSchdlEndYmd();
					String priod = strtYmd;
					if(ctschdlbVo.getAlldayYn() != null && "Y".equals(ctschdlbVo.getAlldayYn())){
						priod += "~"+endYmd;
					}else{
						priod+= " "+ctschdlbVo.getSchdlStrtTime();
						priod += "~";
						priod += endYmd;
						priod+= " "+ctschdlbVo.getSchdlEndTime();
					}
					
					sb.append(p1).append(span1).append(messageProperties.getMessage("wc.cols.schdlPriod", request)).append(span2).append(" : ").append(priod).append(p2);//기간
					sb.append(p1).append(span1).append(span2).append("&nbsp;").append(p2);
					sb.append(p1).append(content).append(p2);//내용
					
					String[][] file_list = null;
					
					// baseDir
					String wasCopyBaseDir = distManager.getWasCopyBaseDir();
					if (wasCopyBaseDir == null) {
						distManager.init();
						wasCopyBaseDir = distManager.getWasCopyBaseDir();
					}
					
					//System.out.println("subj : "+subj);
					//System.out.println("sb.toString() : "+sb.toString());
					if(fileSaveSize>0 || ctschdlbVo.getAttYn() == "Y"){
						CtFileDVo wcFile = new CtFileDVo();
						wcFile.setRefId(ctschdlbVo.getSchdlId());
						@SuppressWarnings("unchecked")
						List<CtFileDVo> wcFileList = (List<CtFileDVo>) commonDao.queryList(wcFile);
						file_list = new String[wcFileList.size()][2];//파일명,파일경로
						for(int i=0; i < wcFileList.size(); i++){
							file_list[i][0] = wcFileList.get(i).getDispNm();
							file_list[i][1] = wasCopyBaseDir + wcFileList.get(i).getSavePath();
						}
					}
					emailSvc.sendMailSvc2(userVo.getUserUid(), to_list, subj, sb.toString(), file_list, true,true, langTypCd);
				}
			}
			
			// set, list 로 시작하는 경우 처리
			//checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "$m.nav.prev(event, '" + listPage + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}
		//공통 처리 페이지
		return MoLayoutUtil.getResultJsp();
	}
	
	/** 수정save [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/transSchdlModPost")
	public String transSchdlModPost(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;		

		try{
			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String ctId = ParamUtil.getRequestParam(request, "ctId", true);

			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			//File Id 생성
			//		String uniqFileId=wcCmSvc.createFileId()+"";

			QueryQueue queryQueue = new QueryQueue();
			
			CtSchdlBVo wcsVo = new CtSchdlBVo();
			VoUtil.bind(request, wcsVo);
			wcsVo.setUserUid(null);
			//=================================================================
			//schdl Param값
			//=================================================================
			String fileSetting = request.getParameter("fileSetting");
			//=================================================================
			//=================================================================
			//수정시작
			//=================================================================

			//fileSetting이 null이면 개별 등록
			if(fileSetting == null){
				fileSetting = "indiv";
			}
			
			if(wcsVo.getAlldayYn() == null || wcsVo.getAlldayYn().isEmpty()) wcsVo.setAlldayYn("N");
			
			if(wcsVo.getSchdlStrtTime() == null || wcsVo.getSchdlStrtTime().isEmpty() || "Y".equals(wcsVo.getAlldayYn())) wcsVo.setSchdlStrtTime("00:00");
			if(wcsVo.getSchdlEndTime() == null || wcsVo.getSchdlEndTime().isEmpty() || "Y".equals(wcsVo.getAlldayYn())) wcsVo.setSchdlEndTime("00:00");
			
			String strtTm = wcsVo.getSchdlStrtTime();
			String endTm = wcsVo.getSchdlEndTime();
			
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(wcsVo.getSchdlStrtYmd() + " " + wcsVo.getSchdlStrtTime());
			//종료 일 +시간
			wcsVo.setSchdlEndDt(wcsVo.getSchdlEndYmd() + " " + wcsVo.getSchdlEndTime());

			//=================================================================
			//CtSchdlPromGuestDVo 참석자
			//=================================================================
			
			// 파일복사 목록 - 반복일정 수정시
			List<DmCommFileDVo> copyFileList = null;
						
			if(fileSetting.equalsIgnoreCase("all")){
				String betweenDay =	ctScdManagerSvc.betweenDay(wcsVo.getSchdlStartDt(), wcsVo.getSchdlEndDt());

				//##############################################################
				// 전체 반영
				//##############################################################
				//String savePath = "";
				//String[] splitData;

				CtSchdlBVo wcsThisVo = new CtSchdlBVo();
				wcsThisVo.setSchdlId(wcsVo.getSchdlId());
				//자신의 pid값 조회
				CtSchdlBVo wcsReturnThisVo = (CtSchdlBVo) commonDao.queryVo(wcsThisVo);
				//			String schdlPid= null;
				CtSchdlBVo wcsPidVo = new CtSchdlBVo();
				//pid 셋팅
				wcsPidVo.setSchdlPid(wcsReturnThisVo.getSchdlPid());
				wcsPidVo.setRepetYn("Y"); // 반복여부
				//부모id에 값이 있다면 부모ID를 가진 객채들에게 모두 file save
				//			if(wcsReturnThisVo.getSchdlPid() != null){
				//				wcsPidVo.setSchdlPid(wcsReturnThisVo.getSchdlPid());
				//				//this가 부모가아니면 false
				//				schdlPid = "false";
				//			}else{
				//				//자기 자신을 부모로 검색
				//				wcsPidVo.setSchdlPid(wcsThisVo.getSchdlId());
				//				//this가 부모이면 true
				//				schdlPid = "true";
				//			}
				@SuppressWarnings("unchecked")
				List<CtSchdlBVo> wcsReturnVo = (List<CtSchdlBVo>) commonDao.queryList(wcsPidVo);
				if(wcsReturnVo.size() != 0){
					for(CtSchdlBVo wcsItem: wcsReturnVo){
						CtSchdlBVo newWcsVo = (CtSchdlBVo) wcsVo.clone();
						newWcsVo.setSchdlId(wcsItem.getSchdlId());
						//같은 스케쥴 아이디면 날짜 변경 작업을 하지 않는다.
						if(!wcsItem.getSchdlId().equals(wcsVo.getSchdlId())){
							@SuppressWarnings("static-access")
							Calendar repetStartCal = ctScdManagerSvc.stringConvertCal(wcsItem.getSchdlStartDt(),true);

							repetStartCal.add(Calendar.DATE, Integer.parseInt(betweenDay)-1);
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							String endDt = formatter.format(repetStartCal.getTime());

							newWcsVo.setSchdlStartDt(wcsItem.getSchdlStartDt().substring(0,10) +  " " + strtTm);
							newWcsVo.setSchdlEndDt(endDt  + " " + endTm);
						}
						
						//전체 참석자 추가
						ctScdManagerSvc.saveGuest(request, queryQueue, newWcsVo);

						//파일전체반영
						ctFileSvc.saveScdlFile(request, wcsItem.getSchdlId() , queryQueue, fileSetting);
						int fileSaveSize=queryQueue.size();
						newWcsVo.setAttYn(fileSaveSize>0?"Y":"N");

						queryQueue.update(newWcsVo);
					}
				}

			}else{
				//참석자 추가
				List<CtSchdlPromGuestDVo> wcPromGuestDVoList = ctScdManagerSvc.saveGuest(request, queryQueue, wcsVo);
				
				// 게시파일 저장
				ctFileSvc.saveScdlFile(request, wcsVo.getSchdlId() , queryQueue, fileSetting);
				int fileSaveSize=queryQueue.size();
				wcsVo.setAttYn(fileSaveSize>0?"Y":"N");
				
				if(fileSetting.equalsIgnoreCase("repet")){// 반복일정 수정
					// 세션의 사용자 정보
					UserVo userVo = LoginSession.getUser(request);
					
					//커뮤니티 기능의 UID 및 정보 입력
					wcsVo.setCtFncUid(request.getParameter("menuId"));
					CtFncDVo ctFuncDvo=ctCmSvc.getCtFncDVo(userVo, wcsVo.getCtFncUid(), wcsVo.getCtId());
					wcsVo.setCtFncId(ctFuncDvo.getCtFncId());
					wcsVo.setCtFncLocStep(ctFuncDvo.getCtFncLocStep());
					wcsVo.setCtFncPid(ctFuncDvo.getCtFncPid());
					
					//기존 반복일정 삭제
					CtSchdlBVo scdThisVo = new CtSchdlBVo();
					scdThisVo.setSchdlId(wcsVo.getSchdlId());
					CtSchdlBVo returnSchdlVo =(CtSchdlBVo)commonDao.queryVo(scdThisVo);
					CtSchdlBVo scdPidVo = new CtSchdlBVo();
					scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
					scdPidVo.setRepetYn("Y"); // 반복여부
					@SuppressWarnings("unchecked")
					List<CtSchdlBVo> schdPidList = (List<CtSchdlBVo>) commonDao.queryList(scdPidVo);
					if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){
						//반복설정된 참석자 삭제
						for(CtSchdlBVo storedWcsVo : schdPidList){							
							if(storedWcsVo.getSchdlId().equals(wcsVo.getSchdlId())) continue; // 수정중인 일정은 삭제하지 않는다.
							//참석자삭제
							CtSchdlPromGuestDVo promGuestVo = new CtSchdlPromGuestDVo();
							promGuestVo.setSchdlId(storedWcsVo.getSchdlId());
							queryQueue.delete(promGuestVo);
							
							//스케쥴 삭제
							CtSchdlBVo wcsOneVo = new CtSchdlBVo();
							wcsOneVo.setSchdlId(storedWcsVo.getSchdlId());
							queryQueue.delete(wcsOneVo);
							
							//파일 삭제
							ctFileSvc.deleteCtFile(storedWcsVo.getSchdlId(), queryQueue);
							
						}
						
						//반복설정 삭제
						CtSchdlRepetSetupDVo repetVo = new CtSchdlRepetSetupDVo();
						repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
						queryQueue.delete(repetVo);
						
						commonDao.execute(queryQueue);
						queryQueue = new QueryQueue();
					}
					
					wcsVo.setCompId(ctEstbBVo.getCompId());
					
					wcsVo.setUserUid(userVo.getUserUid());
					if(request.getParameter("agnt")!=null&&!request.getParameter("agnt").equals("")&&!request.getParameter("agnt").equals("-1"))
						wcsVo.setUserUid(request.getParameter("agnt"));
					
					wcsVo.setDeptId(userVo.getDeptId());
					wcsVo.setRegrUid(userVo.getUserUid());
					wcsVo.setRegrNm(userVo.getUserNm());
					
					wcsVo.setSchdlPid(wcsVo.getSchdlId()); // 부모ID를 동일하게 저장
					
					if(wcsVo.getSolaLunaYn() == null || "".equals(wcsVo.getSolaLunaYn())) wcsVo.setSolaLunaYn("Y");
					
					//휴일여부 NULL체크
					wcsVo.setHoliYn("N");
					//일정상태코드 T보류/C참석/N불참/D약속취소
					wcsVo.setSchdlStatCd("T");
					
					// 반복일정 추가
					String repetSetup = ParamUtil.getRequestParam(request, "repetSetup", false);
					String repetKnd = ParamUtil.getRequestParam(request, "repetKnd", false);
					String dalySelect = ParamUtil.getRequestParam(request, "dalySelect", false);
					String welySelect = ParamUtil.getRequestParam(request, "welySelect", false);
					String dow = ParamUtil.getRequestParam(request, "dow", false);
					String molyKnd = ParamUtil.getRequestParam(request, "molyKnd", false);
					String firMolySelect = ParamUtil.getRequestParam(request, "firMolySelect", false);
					String firMolyDaySelect = ParamUtil.getRequestParam(request, "firMolyDaySelect", false);
					String secMolySelect = ParamUtil.getRequestParam(request, "secMolySelect", false);
					String secMolyWeekSelect = ParamUtil.getRequestParam(request, "secMolyWeekSelect", false);
					String secMolyWeekOfDaySelect = ParamUtil.getRequestParam(request, "secMolyWeekOfDaySelect", false);
					String yelyKnd = ParamUtil.getRequestParam(request, "yelyKnd", false);
					String firYelySelect = ParamUtil.getRequestParam(request, "firYelySelect", false);
					String firYelyDaySelect = ParamUtil.getRequestParam(request, "firYelyDaySelect", false);
					String secYelySelect = ParamUtil.getRequestParam(request, "secYelySelect", false);
					String secYelyWeekSelect = ParamUtil.getRequestParam(request, "secYelyWeekSelect", false);
					String secYelyWeekOfDaySelect = ParamUtil.getRequestParam(request, "secYelyWeekOfDaySelect", false);
					String reStartDt = ParamUtil.getRequestParam(request, "repetchoiDt", false);
					String reEndDt = ParamUtil.getRequestParam(request, "repetcmltDt", false);
					
					wcsVo.setRepetYn(repetSetup); //일정반복여부
					
					/** 
					 * [반복주기코드] 
					 * 반복일간 :EV_DY
					 * 반복주간 :EV_WK
					 * 반복일월간 : EV_DY_MY
					 * 반복주월간 : EV_WK_MT
					 * 반복일년간 : EV_DY_YR
					 * 반복주년간 : EV_WK_YR
					 * */
					CtSchdlRepetSetupDVo wcRepetVo = new CtSchdlRepetSetupDVo();
					if(repetSetup != null && repetSetup.equalsIgnoreCase("Y")){
						if(repetKnd != null && repetKnd.equalsIgnoreCase("DALY")){
							wcRepetVo.setRepetPerdCd("EV_DY");
							wcRepetVo.setRepetDd(dalySelect);
						}else if(repetKnd != null && repetKnd.equalsIgnoreCase("WELY")){
							wcRepetVo.setRepetPerdCd("EV_WK");
							wcRepetVo.setRepetWk(welySelect);
							wcRepetVo.setApntDy(dow);
						}else if(repetKnd != null && repetKnd.equalsIgnoreCase("MOLY")){
							if(molyKnd != null && molyKnd.equalsIgnoreCase("1")){
								wcRepetVo.setRepetPerdCd("EV_DY_MY");
								wcRepetVo.setRepetMm(firMolySelect);
								wcRepetVo.setApntDd(firMolyDaySelect);
							}else if(molyKnd != null && molyKnd.equalsIgnoreCase("2")){
								wcRepetVo.setRepetPerdCd("EV_WK_MT");
								wcRepetVo.setRepetMm(secMolySelect);
								wcRepetVo.setApntWk(secMolyWeekSelect);
								wcRepetVo.setApntDy(secMolyWeekOfDaySelect);
							}
						}else if(repetKnd != null && repetKnd.equalsIgnoreCase("YELY")){
							if(yelyKnd != null && yelyKnd.equalsIgnoreCase("1")){
								wcRepetVo.setRepetPerdCd("EV_DY_YR");
								wcRepetVo.setRepetMm(firYelySelect);
								wcRepetVo.setApntDd(firYelyDaySelect);
							}else if(yelyKnd != null && yelyKnd.equalsIgnoreCase("2")){
								wcRepetVo.setRepetPerdCd("EV_WK_YR");
								wcRepetVo.setRepetMm(secYelySelect);
								wcRepetVo.setApntWk(secYelyWeekSelect);
								wcRepetVo.setApntDy(secYelyWeekOfDaySelect);
							}
						}
						
						wcRepetVo.setRepetSetup(repetSetup);
						wcRepetVo.setRepetStartDt(reStartDt);
						wcRepetVo.setRepetEndDt(reEndDt);
						wcRepetVo.setSchdlId(wcsVo.getSchdlId());
						queryQueue.insert(wcRepetVo);
						
						copyFileList = new ArrayList<DmCommFileDVo>();
						
						// 반복일정 저장
						ctScdManagerSvc.repetSetup(wcRepetVo, wcsVo, wcPromGuestDVoList, queryQueue, request, copyFileList);
					}
					
				}
				queryQueue.update(wcsVo);
			}

			commonDao.execute(queryQueue);
			
			// 파일 복사
			if(copyFileList!=null && copyFileList.size()>0){
				ctFileSvc.copyFileList(request, copyFileList);
			}
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "$m.nav.prev(event, '" + listPage + "');");
		}catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}
		//공통 처리 페이지
		return MoLayoutUtil.getResultJsp();

	}
	
	/** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/ct/schdl/findBc")
	public String findBc(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/ct/schdl/findBc", "empty");
	}
	
    /** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/ct/schdl/findBcAjx")
	public String findBcAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String detlViewType = (String)jsonObject.get("detlViewType");
			//String schOpenTypCd = (String)jsonObject.get("schOpenTypCd");
			String schBcRegrUid = (String)jsonObject.get("schBcRegrUid");
			String pagingYn = (String)jsonObject.get("pagingYn");
			String schCat = (String)jsonObject.get("schCat");
			String schWord = (String)jsonObject.get("schWord");
			
			String selection = (String)jsonObject.get("selection");
			if(selection!=null && !selection.isEmpty()){
				model.put("selection", selection);//for UI - radio/checkbox
			}
			// 조회조건 매핑
			WbBcBVo wbBcBVo = new WbBcBVo();
			if(schCat != null && !schCat.isEmpty())wbBcBVo.setSchCat(schCat);
			if(schWord != null && !schWord.isEmpty())wbBcBVo.setSchWord(schWord);
			//VoUtil.bind(request, wbBcBVo);
			wbBcBVo.setQueryLang(langTypCd);
			wbBcBVo.setCompId(userVo.getCompId());
			
			boolean flag = false;
			if("bcList".equals(detlViewType)){
				String listPage = ParamUtil.getRequestParam(request, "listPage", false);
				if(listPage == null || !"listAllMetng".equals(listPage)){
					wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
				}
				flag = true;
			}else if("bcOpenList".equals(detlViewType)){
				wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
				wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
				wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
				String[] schOpenTypCds =new String[]{"allPubl","deptPubl","apntrPubl"};
				wbBcBVo.setSchOpenTypCds(schOpenTypCds);
				model.put("schOpenTypCds", schOpenTypCds);
				
				// 원본 , 복사원본 , 메인설정 상태 조건절 추가
				wbBcBVo.setWhereSqllet("AND MAIN_SETUP_YN IN ('O', 'C','Y')");
				flag = true;
			}
			if(flag){
				if(pagingYn != null && "N".equals(pagingYn)){
					@SuppressWarnings("unchecked")
					List<WbBcBVo> WbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
					model.put("wbBcBMapList", WbBcBVoList);
				}else{
					Map<String,Object> rsltMap = wbBcSvc.getWbBcMapList(request , wbBcBVo );
					model.put("recodeCount", rsltMap.get("recodeCount"));
					model.put("wbBcBMapList", rsltMap.get("wbBcBMapList"));
				}
				return MoLayoutUtil.getJspPath("/ct/schdl/findBcAjx");
			}else{
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("Search user by [no orgId] and [no userNm]"+" - "+message);
				return JsonUtil.returnJson(model, message);
			}
		}catch (Exception e) {
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** [AJAX]일정삭제 
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/transSchdlDelAjx")
	public String transSchdlDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlId  = (String) object.get("schdlId");
			//			String bullId = (String) object.get("bullId");
			if (schdlId == null || schdlId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 일정 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();


			//schdlID로 List검색
			CtSchdlBVo scdThisVo = new CtSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			CtSchdlBVo returnScdVo = (CtSchdlBVo) commonDao.queryVo(scdThisVo);
					
			
			if(returnScdVo != null){
				//참석자삭제
				CtSchdlPromGuestDVo promGuestVo = new CtSchdlPromGuestDVo();
				promGuestVo.setSchdlId(returnScdVo.getSchdlId());
				queryQueue.delete(promGuestVo);

				//스케쥴 삭제
				CtSchdlBVo ccsOneVo = new CtSchdlBVo();
				ccsOneVo.setSchdlId(returnScdVo.getSchdlId());
				queryQueue.delete(ccsOneVo);

				//파일 삭제
				ctFileSvc.deleteCtFile(returnScdVo.getSchdlId(), queryQueue);

				if(returnScdVo.getSchdlPid().equalsIgnoreCase(scdThisVo.getSchdlId())){
					//반복설정 삭제
					CtSchdlRepetSetupDVo repetVo = new CtSchdlRepetSetupDVo();
					repetVo.setSchdlId(returnScdVo.getSchdlPid());
					queryQueue.delete(repetVo);
				}

				commonDao.execute(queryQueue);
				
				// cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");

			}

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] 전체삭제
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/transSchdlAllDelAjx")
	public String transSchdlAllDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlId  = (String) object.get("schdlId");
			//			String bullId = (String) object.get("bullId");
			if (schdlId == null || schdlId.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 일정 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();


			//반복설정되어있는 schdlPID로 List검색
			CtSchdlBVo scdThisVo = new CtSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			CtSchdlBVo returnSchdlVo =(CtSchdlBVo)commonDao.queryVo(scdThisVo);
			CtSchdlBVo scdPidVo = new CtSchdlBVo();
			scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid()); 
			scdPidVo.setRepetYn("Y"); // 반복여부
			@SuppressWarnings("unchecked")
			List<CtSchdlBVo> schdPidList = (List<CtSchdlBVo>) commonDao.queryList(scdPidVo);
			
			if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){
				//반복설정된 참석자 삭제
				for(CtSchdlBVo wcsVo : schdPidList){
					//참석자삭제
					CtSchdlPromGuestDVo promGuestVo = new CtSchdlPromGuestDVo();
					promGuestVo.setSchdlId(wcsVo.getSchdlId());
					queryQueue.delete(promGuestVo);
					
					//스케쥴 삭제
					CtSchdlBVo wcsOneVo = new CtSchdlBVo();
					wcsOneVo.setSchdlId(wcsVo.getSchdlId());
					queryQueue.delete(wcsOneVo);
					
					//파일 삭제
					ctFileSvc.deleteCtFile(wcsVo.getSchdlId(), queryQueue);
				}
				
				//반복설정 삭제
				CtSchdlRepetSetupDVo repetVo = new CtSchdlRepetSetupDVo();
				repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
				queryQueue.delete(repetVo);
			}

			commonDao.execute(queryQueue);

			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.allDel.success", request));
			model.put("result", "ok");
			

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 참석자 빈시간 확인
	 * @throws Exception */
	@RequestMapping(value = "/ct/schdl/viewEmptyTimeGuest")
	public String viewEmptyTimeGuestPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String guestUids[]=request.getParameter("guestUids").split("\\|");
		String guestNms[]=request.getParameter("guestNms").split("\\|");
		String scdlStartDt = request.getParameter("scdlStartDt");
		String scdlEndDt = request.getParameter("scdlEndDt");
		
		@SuppressWarnings("static-access")
		Calendar startCal = ctScdManagerSvc.stringConvertCal(scdlStartDt, true);
		@SuppressWarnings("static-access")
		Calendar endCal = ctScdManagerSvc.stringConvertCal(scdlEndDt, true);
		
		long difference = (endCal.getTimeInMillis() - startCal.getTimeInMillis())/1000;
		int scdGapDay = (int) (difference/(24*60*60));
		
		Calendar viewCal = (Calendar) startCal.clone();
		DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
		
		List<CtSchdlPromGuestDVo> promGuestLst = new ArrayList<CtSchdlPromGuestDVo>();
		for(String guestUid:guestUids){
			CtSchdlPromGuestDVo guestVo=new CtSchdlPromGuestDVo();
			guestVo.setGuestUid(guestUid);
			promGuestLst.add(guestVo);
		}
		
		
		List<CtSchdlPromGuestDVo> promGuestList = new ArrayList<CtSchdlPromGuestDVo>();
		for(int i=0 ; i<guestUids.length; i++){
			CtSchdlPromGuestDVo promGuestVo=new CtSchdlPromGuestDVo();
			promGuestVo.setGuestUid(guestUids[i]);
			promGuestVo.setGuestNm(guestNms[i]);
			promGuestList.add(promGuestVo);
		}
	
		List<List<CtSchdlBVo>> guestScdlLst=new ArrayList<List<CtSchdlBVo>>();
		for(int i=0; i<=scdGapDay; i++){
			viewCal.add(Calendar.DATE, (i==0?0:1));
			String viewDate=dateFormat.format(viewCal.getTime());
			CtSchdlBVo conditionVo=new CtSchdlBVo();
			conditionVo.setPromGuestLst(promGuestLst);
			conditionVo.setSchdlStartDt(viewDate);
			conditionVo.setInstanceQueryId("ctselectEmptyTime");
			@SuppressWarnings("unchecked")
			List<CtSchdlBVo> schdlVoLst = (List<CtSchdlBVo>) commonDao.queryList(conditionVo);
			guestScdlLst.add(schdlVoLst);
		}
		
		model.put("guestScdlLst", guestScdlLst);
		model.put("scdlStartDt", request.getParameter("scdlStartDt"));
		model.put("scdlEndDt", request.getParameter("scdlEndDt"));
		model.put("scdGapDay", scdGapDay);
		model.put("guestUidList", promGuestList);
		
		return MoLayoutUtil.getJspPath("/ct/schdl/viewEmptyTimeGuest","empty");
	}
	
	/** 반복 상세설정 팝업 */
	@RequestMapping(value = "/ct/schdl/repetSelectSub")
	public String repetSelectPop(HttpServletRequest request,
			@Parameter(name="repetKnd", required=false) String repetKnd,
			Locale locale,
			ModelMap model) throws Exception {
		
		model.put("UI_TITLE", messageProperties.getMessage("wc.btn.repetSetup", locale));//반복설정
		return MoLayoutUtil.getJspPath("/ct/schdl/repetSelectSub","empty");
	}
	
	/** 일정 종류 조회 */
	public void setSchdlKndCdList(ModelMap model , HttpServletRequest request){
		//{schdlKndCd,이름,등록수정시적용여부}
		model.put("schdlKndCdList", new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"},{"2",messageProperties.getMessage("wc.option.grpSchdl", request),"Y"},{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}});
	}
	
}
