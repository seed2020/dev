package com.innobiz.orange.web.wc.ctrl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;
import com.innobiz.orange.web.wc.calender.WcScdCalMonth;
import com.innobiz.orange.web.wc.calender.WcScdCalWeek;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcFileSvc;
import com.innobiz.orange.web.wc.svc.WcRescSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcAgntRVo;
import com.innobiz.orange.web.wc.vo.WcAnnvDVo;
import com.innobiz.orange.web.wc.vo.WcApntrRVo;
import com.innobiz.orange.web.wc.vo.WcBumkDVo;
import com.innobiz.orange.web.wc.vo.WcEnvSetupDVo;
import com.innobiz.orange.web.wc.vo.WcNatBVo;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcRepetSetupDVo;
import com.innobiz.orange.web.wc.vo.WcRescBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpMbshDVo;
import com.innobiz.orange.web.wc.vo.WcUserGrpBVo;


/** 일정관리 */
@Controller
public class WcSchdlCtrl {
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;

	/** schdlID생성 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 게시파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcSchdlCtrl.class);
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WcRescSvc wcRescSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 일정 보기 */
	@RequestMapping(value = "/wc/listPsnSchdl")
	public String listPsnSchdl(HttpServletRequest request,			
			@RequestParam(value="action",required=false)
			String action,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		// set, list 로 시작하는 경우 처리
		checkPath(request, "listPsnSchdl", model);
		int schdlTyp=-1;
		
		String fncCal = request.getParameter("fncCal");
		String tabNo = request.getParameter("tabNo");
		String bumk = request.getParameter("bumk");
		String agnt = request.getParameter("agnt");
		String viewUserUid = request.getParameter("viewUserUid");
		String viewUserNm = request.getParameter("viewUserNm");
		String viewOrgId = request.getParameter("viewOrgId");
		String viewOrgNm = request.getParameter("viewOrgNm");
		String othr = request.getParameter("othr");
		String grpResetFlag = request.getParameter("grpResetFlag");
		String choiGrpIds[] = request.getParameterValues("choiGrpIds");
		String choiGrpNms[] = request.getParameterValues("choiGrpNms");
		String timeZone=TimeZone.getDefault().getID();
		if(choiGrpIds==null &&  (grpResetFlag==null||grpResetFlag.equals("false"))){
			grpResetFlag="false";
			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
			wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
			wcSchdlGrpBVo.setFncMy("Y");

			List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
			if(wcSchdlGroupBVoList!=null&&wcSchdlGroupBVoList.size()>0){
				choiGrpIds = new String[wcSchdlGroupBVoList.size()];
				choiGrpNms = new String[wcSchdlGroupBVoList.size()];
				int idx=0;
				for(WcSchdlGrpBVo grpVo:wcSchdlGroupBVoList){
					choiGrpIds[idx]=grpVo.getSchdlGrpId();
					choiGrpNms[idx]=grpVo.getGrpNm();
					idx++;
				}
			}
		}
		
		
		
		model.put("bumk", bumk);
		model.put("agnt", agnt);
		model.put("viewUserUid", viewUserUid);
		model.put("viewUserNm", viewUserNm);
		model.put("viewOrgId", viewOrgId);
		model.put("viewOrgNm", viewOrgNm);
		model.put("choiGrpIds", choiGrpIds);
		model.put("choiGrpNms", choiGrpNms);
		model.put("grpResetFlag", grpResetFlag);
		model.put("othr", othr);
		
		if(fncCal.equals("psn"))
			schdlTyp=1;
		else if(fncCal.equals("my"))
			schdlTyp=-1;
		else if(fncCal.equals("grp"))
			schdlTyp=2;
		else if(fncCal.equals("dept"))
			schdlTyp=3;
		else if(fncCal.equals("comp"))
			schdlTyp=4;
		
		
		
		
		//Action 이 null
		if(action==null){
			Calendar currentCal = Calendar.getInstance();
			int defYear = currentCal.get(Calendar.YEAR);		
			int currentMonth = currentCal.get(Calendar.MONTH) +1;
			int currentWeek = currentCal.get(Calendar.WEEK_OF_MONTH);
			int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(defYear, currentMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			WcScdCalWeek scdWeek = wcScdManagerSvc.getScdWeek(defYear, currentMonth, currentWeek,schdlTyp,scdMonth);
			WcScdCalDay scdDay = wcScdManagerSvc.getScdDay(defYear, currentMonth, currentDay,schdlTyp,scdMonth);
					
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
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
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

			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
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
			
			
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}
		
		if(schdlTyp==1){
			String langTypCd = LoginSession.getLangTypCd(request);
			List<WcAgntRVo> wcAgntList = wcScdManagerSvc.getAgntTgtList(langTypCd, userVo.getUserUid());
			model.put("wcAgntVos", wcAgntList);
			List<WcBumkDVo> bumkList = wcScdManagerSvc.getBumkPsn(userVo.getUserUid());
			model.put("bumkList", bumkList);
		}else if(schdlTyp==3){
			List<WcBumkDVo> bumkList = wcScdManagerSvc.getBumkDept(userVo.getUserUid());
			model.put("bumkList", bumkList);
		}
		
		return LayoutUtil.getJspPath("/wc/listPsnSchdl");
	}

	private void checkPath(HttpServletRequest request, String path1,
			ModelMap model) throws SQLException {
		// 페이지 정보 세팅
		CommonVo commonVo = new CommonVoImpl();
		PersonalUtil.setPaging(request, commonVo, 0);
		model.put("recodeCount", 0);

		// setXXX 이면
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
	}

//	/** 임시2 */
//	@RequestMapping(value = "/wc/{path1}/{path2}")
//	public String boardLv2(HttpServletRequest request,
//			@PathVariable("path1") String path1,
//			@PathVariable("path2") String path2,
//			ModelMap model) throws Exception {
//
//		// set, list 로 시작하는 경우 처리
//		checkPath(request, path2, model);
//
//		return LayoutUtil.getJspPath("/wc/" + path1 + "/" + path2);
//	}
	
	/** 할일등록 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setWorkPop")
	public String setWorkPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setWorkPop", model);		
		return LayoutUtil.getJspPath("/wc/setWorkPop");
	}
	
	/** 즐겨찾기 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setBumkPop")
	public String setBumkPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setBumkPop", model);	
		//String bumkNm = request.getParameter("bumkNm");
		model.put("menuId", request.getParameter("menuId"));
		model.put("fncCal", request.getParameter("fncCal"));
		model.put("fncCalSub", request.getParameter("fncCalSub"));
		model.put("viewOrgId", request.getParameter("viewOrgId"));
		model.put("viewUserUid", request.getParameter("viewUserUid"));
		model.put("viewUserNm", request.getParameter("viewUserNm"));
		model.put("viewOrgNm", request.getParameter("viewOrgNm"));
		
		String bumkId = request.getParameter("bumkId");
		if(bumkId != null)
		{
			String[] arrBumk = bumkId.split(":");
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			WcBumkDVo bumkDvo=new WcBumkDVo();
			bumkDvo.setQueryLang(langTypCd);
			bumkDvo.setUserUid(userVo.getUserUid());
			bumkDvo.setCompId(userVo.getCompId());
			bumkDvo.setBumkId(arrBumk[1]);
			
			bumkDvo = (WcBumkDVo)commonDao.queryVo(bumkDvo);
			model.put("bumkDvo", bumkDvo);
		}
		
		model.put("bumkId", bumkId);
		return LayoutUtil.getJspPath("/wc/setBumkPop");
	}
	
	
	/** 참석자 빈시간 확인
	 * @throws Exception */
	@RequestMapping(value = {"/wc/viewEmptyTimeGuestPop", "/wr/viewEmptyTimeGuestPop"})
	public String viewEmptyTimeGuestPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String guestUids[]=request.getParameter("guestUids").split("\\|");
		String guestNms[]=request.getParameter("guestNms").split("\\|");
		String scdlStartDt = request.getParameter("scdlStartDt");
		String scdlEndDt = request.getParameter("scdlEndDt");
		
		@SuppressWarnings("static-access")
		Calendar startCal = wcScdManagerSvc.stringConvertCal(scdlStartDt, true);
		@SuppressWarnings("static-access")
		Calendar endCal = wcScdManagerSvc.stringConvertCal(scdlEndDt, true);
		
		long difference = (endCal.getTimeInMillis() - startCal.getTimeInMillis())/1000;
		int scdGapDay = (int) (difference/(24*60*60));
		
		Calendar viewCal = (Calendar) startCal.clone();
		DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
		
		List<WcPromGuestDVo> promGuestLst = new ArrayList<WcPromGuestDVo>();
		for(String guestUid:guestUids){
			WcPromGuestDVo guestVo=new WcPromGuestDVo();
			guestVo.setGuestUid(guestUid);
			promGuestLst.add(guestVo);
		}
		
		
		List<WcPromGuestDVo> promGuestList = new ArrayList<WcPromGuestDVo>();
		for(int i=0 ; i<guestUids.length; i++){
			WcPromGuestDVo promGuestVo=new WcPromGuestDVo();
			promGuestVo.setGuestUid(guestUids[i]);
			promGuestVo.setGuestNm(guestNms[i]);
			promGuestList.add(promGuestVo);
		}
	
		List<List<WcSchdlBVo>> guestScdlLst=new ArrayList<List<WcSchdlBVo>>();
		for(int i=0; i<=scdGapDay; i++){
			viewCal.add(Calendar.DATE, (i==0?0:1));
			String viewDate=dateFormat.format(viewCal.getTime());
			WcSchdlBVo conditionVo=new WcSchdlBVo();
			conditionVo.setPromGuestLst(promGuestLst);
			conditionVo.setSchdlStartDt(viewDate);
			conditionVo.setInstanceQueryId("selectEmptyTime");
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> schdlVoLst = (List<WcSchdlBVo>) commonDao.queryList(conditionVo);
			guestScdlLst.add(schdlVoLst);
		}
		
		model.put("guestScdlLst", guestScdlLst);
		model.put("scdlStartDt", request.getParameter("scdlStartDt"));
		model.put("scdlEndDt", request.getParameter("scdlEndDt"));
		model.put("scdGapDay", scdGapDay);
		model.put("guestUidList", promGuestList);
		
		return LayoutUtil.getJspPath("/wc/viewEmptyTimeGuestPop");
	}
	
	
	/** 명함 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = {"/wc/findBcPop", "/wc/adm/findBcPop", "/wr/findBcPop", "/wr/adm/findBcPop"})
	public String findBcPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		return LayoutUtil.getJspPath("/wc/findBcPop");
	}
	
	/** [FRAME] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = {"/wc/findBcFrm", "/wc/adm/findBcFrm", "/wr/findBcFrm", "/wr/adm/findBcFrm"})
	public String findBcFrm(HttpServletRequest request,
			@Parameter(name="detlViewType", required=true) String detlViewType,
			@Parameter(name="schOpenTypCd", required=false) String schOpenTypCd,
			@Parameter(name="schBcRegrUid", required=false) String schBcRegrUid,
			@Parameter(name="pagingYn", required=false) String pagingYn,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WbBcBVo wbBcBVo = new WbBcBVo();
		VoUtil.bind(request, wbBcBVo);
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
		}
				
		return LayoutUtil.getJspPath("/wc/findBcFrm");
	}
	
	
	/** 즐겨찾기 팝업 저장
	* @throws Exception */
	@RequestMapping(value = "/wc/transSetBumkPopSave")
	public String transSetBumkPopSave(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		// set, list 로 시작하는 경우 처리
		
		try{	
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String bumkNm = (String) object.get("bumkNm");
			String fncCal = (String) object.get("fncCal");
			String fncCalSub = (String) object.get("fncCalSub");
			String bumkTgtUid = (String) object.get("viewUserUid");
			String bumkTgtDeptId = (String) object.get("viewOrgId");
			String bumkId = (String) object.get("bumkId");
			
			model.put("menuId", (String) object.get("menuId"));
			model.put("fncCal", fncCal);
			model.put("fncCalSub", fncCalSub);
			model.put("viewOrgId", bumkTgtDeptId);
			model.put("viewUserUid", bumkTgtUid);
			//model.put("viewUserNm", new String(((String) object.get("viewUserNm")).getBytes("iso-8859-1"), "EUC-KR"));
			model.put("viewUserNm", (String) object.get("viewUserNm"));
			model.put("viewOrgNm", (String) object.get("viewOrgNm"));
			UserVo userVo = LoginSession.getUser(request);
			
			WcBumkDVo bumkDvo=new WcBumkDVo();
			bumkDvo.setBumkDispNm(bumkNm);
			bumkDvo.setUserUid(userVo.getUserUid());
			bumkDvo.setCompId(userVo.getCompId());
			
			if(bumkId != null && !bumkId.equals(""))
			{
				String[] arrBumk = bumkId.split(":");
				bumkDvo.setBumkId(arrBumk[1]);
				commonDao.update(bumkDvo);
			}
			else
			{
				bumkDvo.setBumkId(wcCmSvc.createId("WC_BUMK_D"));
				bumkDvo.setBumkTgtUid(bumkTgtUid);
				bumkDvo.setBumkTgtDeptId(bumkTgtDeptId);
				bumkDvo.setUserDeptTypCd(fncCalSub.equals("psn")?"1":"2");
				commonDao.insert(bumkDvo);
			}
			
			model.put("message", messageProperties.getMessage("wc.msg.save.success", request));
			model.put("result", "ok");
	
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
		
		
	}
	
	
	/** 즐겨찾기 팝업 삭제
	* @throws Exception */
	@RequestMapping(value = "/wc/transSetBumkPopDel")
	public String transSetBumkPopDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		// set, list 로 시작하는 경우 처리
		
		try{	
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String bumkId = (String) object.get("bumkId");
			String fncCal = (String) object.get("fncCal");
			
			model.put("menuId", (String) object.get("menuId"));
			model.put("fncCal", fncCal);
			UserVo userVo = LoginSession.getUser(request);
			
			WcBumkDVo bumkDvo=new WcBumkDVo();
			bumkDvo.setUserUid(userVo.getUserUid());
			bumkDvo.setCompId(userVo.getCompId());
			
			String[] arrBumk = bumkId.split(":");
			bumkDvo.setBumkId(arrBumk[1]);
			commonDao.delete(bumkDvo);

			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
	
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 행사등록 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setEvntPop")
	public String setEvntPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setEvntPop", model);

		// 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		model.put("loginUserUid", userVo.getUserUid());
		return LayoutUtil.getJspPath("/wc/setEvntPop");
	}
	
	/** 기념일등록 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setAnnvPop")
	public String setAnnvPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setAnnvPop", model);
		// 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		return LayoutUtil.getJspPath("/wc/setAnnvPop");
	}
	
	/** 환경 설정 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setMySetupPop")
	public String setMySetupPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setMySetupPop", model);	
		UserVo userVo = LoginSession.getUser(request);
		WcEnvSetupDVo setupDvo=new WcEnvSetupDVo();
		setupDvo.setCompId(userVo.getCompId());
		setupDvo.setUserUid(userVo.getUserUid());
		setupDvo.setPagePltTypCd("1");
	
		WcEnvSetupDVo selectVo=(WcEnvSetupDVo) commonDao.queryVo(setupDvo);
		if(selectVo!=null){
			model.put("PROM", !selectVo.getSchdlTypCd().split("\\:")[0].equals("0")?true:false);
			model.put("WORK", !selectVo.getSchdlTypCd().split("\\:")[1].equals("0")?true:false);
			model.put("EVNT", !selectVo.getSchdlTypCd().split("\\:")[2].equals("0")?true:false);
			model.put("ANNV", !selectVo.getSchdlTypCd().split("\\:")[3].equals("0")?true:false);
			model.put("PSN",  !selectVo.getSchdlKndCd().split("\\:")[0].equals("0")?true:false);
			model.put("DEPT", !selectVo.getSchdlKndCd().split("\\:")[1].equals("0")?true:false);
			model.put("COMP", !selectVo.getSchdlKndCd().split("\\:")[2].equals("0")?true:false);
			model.put("GRP",  !selectVo.getSchdlKndCd().split("\\:")[3].equals("0")?true:false);
		}else{
			model.put("PROM", true);
			model.put("WORK", true);
			model.put("EVNT", true);
			model.put("ANNV", true);
			model.put("PSN", true);
			model.put("DEPT", true);
			model.put("COMP", true);
			model.put("GRP", true);
		}
		return LayoutUtil.getJspPath("/wc/setMySetupPop");
	}
	
	/** 설정 팝업 저장
	* @throws Exception */
	@RequestMapping(value = "/wc/transSetMySetupPopSave")
	public String transSetMySetupPopSave(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try{	
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			boolean prom = (Boolean) object.get("PROM");
			boolean work = (Boolean) object.get("WORK");
			boolean event = (Boolean) object.get("EVNT");
			boolean annv = (Boolean) object.get("ANNV");
			boolean psn = (Boolean)object.get("PSN");
			boolean dept = (Boolean) object.get("DEPT");
			boolean comp = (Boolean) object.get("COMP");
			boolean grp = (Boolean) object.get("GRP");
			
			UserVo userVo = LoginSession.getUser(request);			
			String typeCds = (prom?"1":"0")+":"+(work?"2":"0")+":"+(event?"3":"0")+":"+(annv?"4":"0");
			String kndCds = (psn?"1":"0")+":"+(dept?"2":"0")+":"+(comp?"3":"0")+":"+(grp?"4":"0");
			
			WcEnvSetupDVo setupDvo=new WcEnvSetupDVo();
			setupDvo.setCompId(userVo.getCompId());
			setupDvo.setUserUid(userVo.getUserUid());
			setupDvo.setPagePltTypCd("1");
			setupDvo.setSchdlKndCd(kndCds);
			setupDvo.setSchdlTypCd(typeCds);
			
			WcEnvSetupDVo selectVo=(WcEnvSetupDVo) commonDao.queryVo(setupDvo);
			if(selectVo!=null){
				commonDao.update(setupDvo);
			}else{
				commonDao.insert(setupDvo);
			}
			
//			setupDvo.setSolaLunaYn(solaLunaYn);
			
			model.put("message", messageProperties.getMessage("wc.msg.save.success", request));
			model.put("result", "ok");
	
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	/** 그룹선택 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setSaveGrpChoiPop")
	public String setSaveGrpChoiPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		
		checkPath(request,"setGrpChoiPop", model);		
		return LayoutUtil.getJspPath("/wc/setGrpChoiPop");
	}
	
	/** 그룹에서 행사등록 또는 기념일 등록시 그룹선택 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setGrpChoiPop")
	public String setGrpChoiPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
			// 	세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
			wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
			wcSchdlGrpBVo.setFncMy("Y");

			List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
			model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
			
		checkPath(request,"setGrpChoiPop", model);		
		return LayoutUtil.getJspPath("/wc/setGrpChoiPop");
	}
	
	
	/** 약속팝업 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/setRepetPop")
	public String setRepetPop(HttpServletRequest request,
			ModelMap model) throws Exception{		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setPromPop", model);			
		return LayoutUtil.getJspPath("/wc/setRepetPop");
	}
	
	
	/** 공통기념일등록 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/adm/transSetCommAnnvPopSave")
	public String transSetCommAnnvPopSave(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;				

		try {	// Multipart 파일 업로드
			
			uploadHandler = wcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
		
			//하나의 트랜 잭션으로 묶기 위한 List
			QueryQueue queryQueue = new QueryQueue();
			
			//File Id 생성
//			String uniqFileId=wcCmSvc.createFileId()+"";
			
			//=================================================================
			//WcSchdlBVo 일정기본
			//=================================================================
			
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			
			UserVo userVo = LoginSession.getUser(request);
			
			try {
				wcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));
				wcsVo.setSchdlPid(wcsVo.getSchdlId());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			// 게시파일 저장
//			List<CommonFileVo> deletedFileList = wcFileSvc.saveScdlFile(request, wcsVo.getSchdlId(), queryQueue, "indiv");				
			
			//int fileSaveSize=queryQueue.size();
//			String fncCal=request.getParameter("fncCal");
			
			int schdlTyp=-1;		
//			if(fncCal.equals("psn"))
//				schdlTyp=1;
//			else if(fncCal.equals("my"))
//				schdlTyp=1;
//			else if(fncCal.equals("grp"))
//				schdlTyp=2;
//			else if(fncCal.equals("dept"))
//				schdlTyp=3;
//			else if(fncCal.equals("comp"))
//				schdlTyp=4;
			//=================================================================
			//schdl Param값
			//=================================================================
			String choiDt = request.getParameter("setAnnvChoiDt");
			String grpId = request.getParameter("grpId");
			//종료일시 만들어야 함
//			Calendar startCal=Calendar.getInstance();
//			startCal.set(Integer.parseInt(choiDt.substring(0,4)), Integer.parseInt(choiDt.substring(5,7))-1, Integer.parseInt(choiDt.substring(8,10)));
//			startCal.add(Calendar.DATE, Integer.parseInt(afterDay)-1);
//			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
			String holiYn = request.getParameter("holiYn");	
			String solaYn = request.getParameter("solaYn");	
			String cmltDt = request.getParameter("setAnnvCmltDt");
			//String publYn = request.getParameter("publYn");			
//			String editor1 = request.getParameter("editor1");
//			String loc = request.getParameter("loc");
			String endTm = "23:59";
			String strtTm = "00:00";
			
		
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

			//=================================================================
			//WcSchdlBVo 일정 userVo값
			//=================================================================
			wcsVo.setCompId(userVo.getCompId());
			wcsVo.setUserUid(userVo.getUserUid());
			if(request.getParameter("agnt")!=null&&!request.getParameter("agnt").equals("")&&!request.getParameter("agnt").equals("-1"))
				wcsVo.setUserUid(request.getParameter("agnt"));
			wcsVo.setDeptId(userVo.getDeptId());
//			wcsVo.setGrpId(userVo.getGradeCd());
			wcsVo.setRegrUid(userVo.getUserUid());
			wcsVo.setRegrNm(userVo.getUserNm());
			
			
			WcRescBVo wcRescBVo = wcRescSvc.collectBaRescBVo(request, "", queryQueue);
			if (wcRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String natCd = request.getParameter("natCd");
			// 기념일 상세 저장
			WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
			wcAnnvDVo.setCompId(userVo.getCompId());
			wcAnnvDVo.setSchdlId(wcsVo.getSchdlId());
			wcAnnvDVo.setRescId(wcRescBVo.getRescId());
			wcAnnvDVo.setNatCd(natCd);
			
			queryQueue.insert(wcAnnvDVo);
			
			// 제목 
			String annvNm = wcRescBVo.getRescVa();
			
			//제목
			wcsVo.setSubj(annvNm);
			//장소명
//			wcsVo.setLocNm(loc);			
			//공개여부 1.공개, 2.지정인공개, 3.비공개
//			publYn ="1";
			
//			wcsVo.setOpenGradCd(publYn);			
			//내용
//			wcsVo.setCont(editor1);
			//=================================================================
//				wscVo.setSchdlId("");
			
			//일정구분코드  5.공통기념일
			wcsVo.setSchdlTypCd("5");
			//일정종류코드 1.나의일정, 2.그룹, 3.회사 4.부서
			wcsVo.setSchdlKndCd(String.valueOf(schdlTyp));
			//양음력 값
			wcsVo.setSolaLunaYn(solaYn);
			//휴일여부 NULL체크
			wcsVo.setHoliYn(holiYn);
			
			wcsVo.setAttYn("N");
			
//			wcsVo.setSchdlFileId(fileSaveSize>0?uniqFileId:null);
			
			if(schdlTyp==2)
				wcsVo.setGrpId(grpId);
						
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(choiDt + " "+strtTm);
			//종료 일 +시간
			wcsVo.setSchdlEndDt(cmltDt + " "+endTm);
			
			//일정반복여부
			if(reStartDt.equals(reEndDt))
				repetSetup="N";
			else
				repetSetup="Y";
			wcsVo.setRepetYn(repetSetup);
			
			
			queryQueue.insert(wcsVo);
		
			
			//=================================================================
			//WcSchdlBVo 일정기본
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
			WcRepetSetupDVo wcRepetVo = new WcRepetSetupDVo();
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
				wcScdManagerSvc.repetSetup(wcRepetVo,wcsVo,new ArrayList<WcPromGuestDVo>(),queryQueue, request);
			}
			commonDao.execute(queryQueue);
			// set, list 로 시작하는 경우 처리
			checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/wc/adm/listCommAnnv.do?fncCal="+request.getParameter("fncCal")+
					"&menuId="+request.getParameter("menuId")
					+ "&molyYear="+request.getParameter("molyYear")
					+ "&molyMonth="+request.getParameter("molyMonth")
					+ "&welyYear="+request.getParameter("welyYear")
					+ "&welyMonth="+request.getParameter("welyMonth")
					+ "&welyWeek="+request.getParameter("welyWeek")
					+ "&dalyYear="+request.getParameter("dalyYear")
					+ "&dalyMonth="+request.getParameter("dalyMonth")
					+ "&dalyDay="+request.getParameter("dalyDay")
					+ "&fncCal="+request.getParameter("fncCal")
					+ "&tabNo="+request.getParameter("tabNo")
					+ "&agnt="+request.getParameter("agnt")
					+"');");
			
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
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 환경설정등록 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/setSchdlEnv" )
	public String setSchdlEnv(HttpServletRequest request,
			ModelMap model) throws Exception{

		//RescId로 전체 삭제인지 아닌지 판단.
		String[] agntUserUid =request.getParameterValues("agntUserUid");
		String[] agntRescId =request.getParameterValues("agntRescId");
		String[] apntUserUid =request.getParameterValues("apntUserUid");
		String[] apntRescId =request.getParameterValues("apntRescId");
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue=new QueryQueue();
		List<WcAgntRVo> wcAgntVos = new ArrayList<WcAgntRVo>();
		if(agntUserUid != null){
			for(int i=0; i < agntUserUid.length; i++){
				WcAgntRVo wcAgntVo = new WcAgntRVo();
				wcAgntVo.setUserUid(userVo.getUserUid());
				wcAgntVo.setAgntUid(agntUserUid[i]);
				wcAgntVo.setCompId(userVo.getCompId());
				wcAgntVos.add(wcAgntVo);
			}
		}
		if(agntRescId != null){
			wcScdManagerSvc.setAgntSave(wcAgntVos,queryQueue, userVo.getUserUid());
		}
		
		

		List<WcApntrRVo> wcApntVos = new ArrayList<WcApntrRVo>();
		if(apntUserUid != null){
			for(int i=0; i < apntUserUid.length; i++){
				WcApntrRVo wcApntVo = new WcApntrRVo();
				wcApntVo.setUserUid(userVo.getUserUid());
				wcApntVo.setApntrUid(apntUserUid[i]);
				wcApntVo.setCompId(userVo.getCompId());
				wcApntVos.add(wcApntVo);
			}
			
		}
		if(apntRescId != null){
			wcScdManagerSvc.setApntSave(wcApntVos,queryQueue, userVo.getUserUid());
		}
		
		commonDao.execute(queryQueue);

		//대리인지정인 조회
		//listSchdlEnv(request, model);
		
		wcScdManagerSvc.getAgntApntModel(request, model, userVo.getUserUid());
		
		if(agntRescId != null || apntRescId != null){
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/wc/setSchdlEnv.do?menuId="+request.getParameter("menuId")+"');");
			return LayoutUtil.getResultJsp();
		}
		else{
			// 사용자그룹 조회
			WcUserGrpBVo wcUserGrpBVo = new WcUserGrpBVo();
			wcUserGrpBVo.setModrUid(userVo.getUserUid());
			@SuppressWarnings("unchecked")
			List<WcUserGrpBVo> wcUserGrpBVoList = (List<WcUserGrpBVo>)commonSvc.queryList(wcUserGrpBVo);
			model.put("wcUserGrpBVoList", wcUserGrpBVoList);
			
			return LayoutUtil.getJspPath("/wc/setSchdlEnv");
		}
	}
	

	/** 나의일정 검색 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/listSchdl" )
	public String listSchdl(HttpServletRequest request,
			ModelMap model) throws Exception{

		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		//구분1 체크 박스 부분  약속/할일/행사/기념일
		String chkProm = request.getParameter("chkProm");
		String chkWork = request.getParameter("chkWork");
		String chkEvnt = request.getParameter("chkEvnt");
		String chkAnnv = request.getParameter("chkAnnv");
		
		//구분2 체크 박스 부분  약속/할일/행사/기념일
		String chkPsn = request.getParameter("chkPsn");
		String chkDept = request.getParameter("chkDept");
		String chkComp = request.getParameter("chkComp");
		String chkGrp = request.getParameter("chkGrp");
		
		
		String startDt = request.getParameter("choiDt");
		String endDt = request.getParameter("cmltDt");				
		String fncCal = request.getParameter("fncCal");
		
		;
		
		
		// 조회조건 매핑
		WcSchdlBVo wcsVo = new WcSchdlBVo();
		
		wcsVo.setPageNo(request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo")));
		
		wcsVo.setPageRowCnt(request.getParameter("pageRowCnt")==null?10:Integer.parseInt(request.getParameter("pageRowCnt")));
		VoUtil.bind(request, wcsVo);
		
		//나의일정검색 or 관리자일정검색
		if(fncCal.equalsIgnoreCase("psn")){
			wcsVo.setUserUid(userVo.getUserUid());
			wcsVo.setDeptId(userVo.getDeptId());
			wcsVo.setCompId(userVo.getCompId());
		}else if(fncCal.equalsIgnoreCase("mng")){
			//관리자 userId 전체 검색
			wcsVo.setUserUid(null);
		}
			
		
	

		//구분1 체크 박스 부분 set 약속/할일/행사/기념일
		if(chkProm != null) wcsVo.setSearchPromChk(chkProm.equalsIgnoreCase("true") ? "1" : null);
		if(chkWork != null) wcsVo.setSearchWorkChk(chkWork.equalsIgnoreCase("true") ? "2" : null);
		if(chkEvnt != null) wcsVo.setSearchEvntChk(chkEvnt.equalsIgnoreCase("true") ? "3" : null);
		if(chkAnnv != null) wcsVo.setSearchAnnvChk(chkAnnv.equalsIgnoreCase("true") ? "4" : null);
			
		//구분1 체크 박스 부분 set 개인/부서/회사/그룹 일정
		if(chkPsn != null) wcsVo.setSearchPsnChk(chkPsn.equalsIgnoreCase("true") ? "1" : null);
		if(chkDept != null) wcsVo.setSearchDeptChk(chkDept.equalsIgnoreCase("true") ? "4" : null);
		if(chkComp != null) wcsVo.setSearchCompChk(chkComp.equalsIgnoreCase("true") ? "3" : null);
		if(chkGrp != null) wcsVo.setSearchGrpChk(chkGrp.equalsIgnoreCase("true") ? "2" : null);
			
		
		
		//체크박스 처리
		model.put("chkProm",wcsVo.getSearchPromChk() == null 
				|| wcsVo.getSearchPromChk().equals("") ? "false" : "true");
		model.put("chkWork",wcsVo.getSearchWorkChk() == null 
				|| wcsVo.getSearchWorkChk().equals("") ? "false" : "true");
		model.put("chkEvnt",wcsVo.getSearchEvntChk() == null 
				|| wcsVo.getSearchEvntChk().equals("") ? "false" : "true");
		model.put("chkAnnv",wcsVo.getSearchAnnvChk() == null 
				|| wcsVo.getSearchAnnvChk().equals("") ? "false" : "true");
		
		model.put("chkPsn",wcsVo.getSearchPsnChk() == null 
				|| wcsVo.getSearchPsnChk().equals("") ? "false" : "true");
		model.put("chkDept",wcsVo.getSearchDeptChk() == null 
				|| wcsVo.getSearchDeptChk().equals("") ? "false" : "true");
		model.put("chkComp",wcsVo.getSearchCompChk() == null 
				|| wcsVo.getSearchCompChk().equals("") ? "false" : "true");
		model.put("chkGrp",wcsVo.getSearchGrpChk() == null 
				|| wcsVo.getSearchGrpChk().equals("") ? "false" : "true");
	
		
		
		wcsVo.setSchdlStartDt(startDt);
		wcsVo.setSchdlEndDt(endDt);
		
		wcsVo.setQueryLang(langTypCd);
		
		Map<String,Object> rsltMap = wcScdManagerSvc.getSchdlMapList(request, wcsVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("schdlGroupBMapList", rsltMap.get("schdlGroupBMapList"));
		
		//start/end DT 처리
		model.put("startDt", wcsVo.getSchdlStartDt());
		model.put("endDt", wcsVo.getSchdlEndDt());
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		PersonalUtil.setPaging(request, wcsVo, (Integer)rsltMap.get("recodeCount"));
		return LayoutUtil.getJspPath("/wc/listSchdl");
	}

	/*private void checkPaging(HttpServletRequest request, String path1,
			ModelMap model) throws Exception {
		
		// 페이지 정보 세팅
		if (path1.startsWith("list") || path1.equals("index")) {
//			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
//			CommonVo commonVo = new CommonVoImpl();
//			Map<String,Object> rsltMap = wcScdManagerSvc.getWcGroupMapList(request, wcSchdlGrpBVo);
			
//			PersonalUtil.setPaging(request, commonVo, (Integer)rsltMap.get("recodeCount"));
//			model.put("recodeCount", rsltMap.get("recodeCount"));
//			PersonalUtil.setPaging(request, commonVo, wvSurvSvc.getSurvSurvey().size());
//			model.put("recodeCount", wvSurvSvc.getSurvSurvey().size());
		}

		// setXXX 이면
		// 에디터 사용
		if (path1.startsWith("set") || path1.equals("index")) {
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
		}
	}*/
	
	/** 그룹 관리 [PAGE] */
	@RequestMapping(value = "/wc/grp/listGrp")
	public String listSurv(HttpServletRequest request, 
			ModelMap model) throws Exception {
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
		VoUtil.bind(request, wcSchdlGrpBVo);
		
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setQueryLang(langTypCd);

		Map<String,Object> rsltMap = wcScdManagerSvc.getWcGroupMapList(request, wcSchdlGrpBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wcSchdlGroupBMapList", rsltMap.get("wcSchdlGroupBMapList"));

		return LayoutUtil.getJspPath("/wc/grp/listGrp");
	}
	
	/** 그룹 삭제 [BUTTON] */
	@RequestMapping(value = "/wc/grp/transListGrpDel")
	public String transListGrpDel(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlGrpId = (String) object.get("schdlGrpId");
			if (schdlGrpId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 일정 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();
			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
			wcScdManagerSvc.deleteSchldGrp(wcSchdlGrpBVo, schdlGrpId, queryQueue);
			
			commonDao.execute(queryQueue);

			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 그룹 탈퇴 [BUTTON]*/
	@RequestMapping(value = "/wc/grp/transListGrpWidr")
	public String transListGrpWidr(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlGrpId = (String) object.get("schdlGrpId");
//			String bullId = (String) object.get("bullId");
			if (schdlGrpId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			UserVo userVo = LoginSession.getUser(request);
	
			// 일정 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();
			WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
			wcSchdlGrpMbshDVo.setUserUid(userVo.getUserUid());
			//WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
			wcScdManagerSvc.withdrawSchldGrp(wcSchdlGrpMbshDVo, schdlGrpId, queryQueue);
			
			commonDao.execute(queryQueue);

			// wc.msg.widr.success=탈퇴 되었습니다.
			model.put("message", messageProperties.getMessage("wc.msg.widr.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** 약속조회 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/viewPromPop" , method = RequestMethod.POST)
	public String viewPromPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				

		String schdlId = request.getParameter("scds_schdlId");
		

		WcPromGuestDVo wcPromGuest = new WcPromGuestDVo();
		wcPromGuest.setSchdlId(schdlId);
		// schdlPID로 count검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		
		WcSchdlBVo scdPidVo = new WcSchdlBVo();
		scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
		model.put("scdPidCount", commonDao.count(scdPidVo));

		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		String fncCal=request.getParameter("fncCal");
		
		//등록Uid와 로그인Uid와 비교
		if(returnSchdlVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())){
			userAuth = "pass";
		}else{
			//나의일정검색 or 관리자 검색
			if(fncCal != null){
				if(fncCal.equals("psn") || fncCal.equals("my") ){
					userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), returnSchdlVo.getUserUid());
				}else{
					userAuth ="fail";
				}
			}else{
				userAuth ="fail";
			}
				
		}
		
		model.put("userAuth", userAuth);
		model.put("promGuestLst", wcScdManagerSvc.getPromGuestLst(wcPromGuest));
		model.put("scds_schdlId", schdlId);
		
		
		return LayoutUtil.getJspPath("/wc/viewPromPop");
	}      
	/** 약속등록 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/wc/setPromPop")
	public String setPromPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setPromPop", model);		
		return LayoutUtil.getJspPath("/wc/setPromPop");
	}

	/** 기념일조회view 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/wc/viewAnnvPop")
	public String viewAnnvPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		String schdlId = request.getParameter("scds_schdlId");
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WcRepetSetupDVo returnRepet = new WcRepetSetupDVo();
		WcRepetSetupDVo wcrVo = new WcRepetSetupDVo();
		
		wcrVo.setSchdlId(schdlId);
		
		returnRepet = (WcRepetSetupDVo) commonDao.queryVo(wcrVo);
		
		if(returnRepet == null){
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			wcsVo.setSchdlId(schdlId);
			wcsVo = (WcSchdlBVo) commonDao.queryVo(wcsVo);
			WcRepetSetupDVo wcsPidVo = new WcRepetSetupDVo();
			wcsPidVo.setSchdlId(wcsVo.getSchdlPid());
			returnRepet = (WcRepetSetupDVo) commonDao.queryVo(wcsPidVo);
		}
		 
		model.put("returnWcrVo", returnRepet);
		model.put("scds_schdlId", schdlId);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		// schdlPID로 count검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		//버튼에대한 권한 설정
		String userAuth =null;
		String fncCal=request.getParameter("fncCal");
		
		if(returnSchdlVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())){
			userAuth = "pass";
		}else{
			if(fncCal != null){
				if(fncCal.equals("psn") || fncCal.equals("my") ){
					userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), returnSchdlVo.getUserUid());
				}else{
					userAuth ="fail";
				}
			}else{
				userAuth ="fail";
			}
		}
		model.put("userAuth", userAuth);//추가
		// set, list 로 시작하는 경우 처리
		checkPath(request,"viewAnnvPop", model);		
		return LayoutUtil.getJspPath("/wc/viewAnnvPop");
	}

	/** 약속수정view [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/setPromModPop")
	public String setPromModPop(HttpServletRequest request,
			ModelMap model) throws Exception{


		String schdlId = request.getParameter("scds_schdlId");

		WcPromGuestDVo wcPromGuest = new WcPromGuestDVo();
		wcPromGuest.setSchdlId(schdlId);

		// schdlPID로 count검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		WcSchdlBVo scdPidVo = new WcSchdlBVo();
		scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
		model.put("scdPidCount", commonDao.count(scdPidVo));
		
//	 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
			
		model.put("promGuestLst", wcScdManagerSvc.getPromGuestLst(wcPromGuest));
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setPromModPop", model);
		//공통 처리 페이지
		return LayoutUtil.getJspPath("/wc/setPromModPop");

	}

	

	public void setPromGuest(String[] promGuestUid,String[] promGuestRescNm,String[] promGuestPositNm,
							 String[] promGuestDeptRescNm, String[] promGuestEmail, 
							 String[]  promFriGuestUid, String[] promFriGuestNm,
							 String schdlId, QueryQueue queryQueue) throws SQLException{
		
		WcPromGuestDVo delPromGuest = new WcPromGuestDVo();
		delPromGuest.setSchdlId(schdlId);
		queryQueue.delete(delPromGuest);

		//임직원
		if(promGuestUid != null){
			for(int i=0; i < promGuestUid.length; i++){
				WcPromGuestDVo wcGuest = new WcPromGuestDVo();
				//일정아이디
				wcGuest.setSchdlId(schdlId);
				//수정은 스케쥴에 참석자들을 삭제후 insert
				wcGuest.setGuestUid(promGuestUid[i]);					
				wcGuest.setGuestNm(promGuestRescNm[i]);
				wcGuest.setGuestPositNm(promGuestPositNm[i]);
				wcGuest.setGuestDeptNm(promGuestDeptRescNm[i]);
				wcGuest.setEmail(promGuestEmail[i]);
				wcGuest.setGuestEmplYn("Y");
				//상태코드 T(참석미정) : C참석, N불참, D약속취소, M주참석자, A 추가참석자 
				wcGuest.setStatCd("T");
				queryQueue.insert(wcGuest);
				//			guestsList.add(wcGuest);
			}
		}
		//지인
		if(promFriGuestUid != null){
			for(int i=0; i < promFriGuestUid.length; i++){
				WcPromGuestDVo wcGuest = new WcPromGuestDVo();
				//일정아이디
				wcGuest.setSchdlId(schdlId);
				wcGuest.setGuestUid(promFriGuestUid[i]);					
				wcGuest.setGuestNm(promFriGuestNm[i]);
				wcGuest.setGuestPositNm(null);
				wcGuest.setGuestDeptNm(null);
				wcGuest.setEmail(null);
				wcGuest.setGuestEmplYn("N");
				//상태코드 T(참석미정) : C참석, N불참, D약속취소, M주참석자, A 추가참석자 
				wcGuest.setStatCd("T");
				queryQueue.insert(wcGuest);
				//			guestsList.add(wcGuest);

			}
		}
	}

	
	/** 약속삭제 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/transSetPromModPopDel","/wc/adm/transSetPromModPopDel"})
	public String transSetPromModPopDel(HttpServletRequest request,
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
			WcSchdlBVo scdThisVo = new WcSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			WcSchdlBVo returnScdVo = (WcSchdlBVo) commonDao.queryVo(scdThisVo);
					
			
			if(returnScdVo != null){
				//참석자삭제
				WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
				promGuestVo.setSchdlId(returnScdVo.getSchdlId());
				queryQueue.delete(promGuestVo);

				//스케쥴 삭제
				WcSchdlBVo wcsOneVo = new WcSchdlBVo();
				wcsOneVo.setSchdlId(returnScdVo.getSchdlId());
				queryQueue.delete(wcsOneVo);

				//파일 삭제
				wcFileSvc.deleteWcFile(returnScdVo.getSchdlId(), queryQueue);
				
				/** 부서일정 - 선택부서 목록 삭제 */
				if(returnScdVo.getOpenGradCd()!=null && "4".equals(returnScdVo.getOpenGradCd()))
					wcScdManagerSvc.deleteWcSchdlDeptRVo(queryQueue, schdlId);
				
				if(returnScdVo.getSchdlPid().equalsIgnoreCase(scdThisVo.getSchdlId())){
					//반복설정 삭제
					WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
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
	
	/** 할일수정 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setWorkModPop")
	public String setWorkModPop(HttpServletRequest request,
			ModelMap model) throws Exception{
	
		String schdlId = request.getParameter("scds_schdlId");

		model.put("scds_schdlId", schdlId);
//	 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		// schdlPID로 count 검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		WcSchdlBVo scdPidVo = new WcSchdlBVo();
		scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
		model.put("scdPidCount", commonDao.count(scdPidVo));
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setWorkModPop", model);		
		return LayoutUtil.getJspPath("/wc/setWorkModPop");
	}
	
	/** 할일조회view 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/wc/viewWorkPop")
	public String viewWorkPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		String schdlId = request.getParameter("scds_schdlId");
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// schdlPID로 count검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		WcSchdlBVo scdPidVo = new WcSchdlBVo();
		scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
		//전체삭제 버튼
		model.put("scdPidCount", commonDao.count(scdPidVo));

		model.put("scds_schdlId", schdlId);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		String fncCal=request.getParameter("fncCal");
		
		if(returnSchdlVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())){
			userAuth = "pass";
		}else{
			if(fncCal != null){
				if(fncCal.equals("psn") || fncCal.equals("my") ){
					userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), returnSchdlVo.getUserUid());
				}else{
					userAuth ="fail";
				}
			}else{
				userAuth ="fail";
			}
		}
		
		model.put("userAuth", userAuth);
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"viewWorkPop", model);		
		return LayoutUtil.getJspPath("/wc/viewWorkPop");
	}

	/** 행사조회view 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/wc/viewEvntPop")
	public String viewEvntPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		String schdlId = request.getParameter("scds_schdlId");
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		// schdlPID로 count검색
		WcSchdlBVo scdThisVo = new WcSchdlBVo();
		scdThisVo.setSchdlId(schdlId);
		WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
		WcSchdlBVo scdPidVo = new WcSchdlBVo();
		scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
		//전체삭제 버튼
		model.put("scdPidCount", commonDao.count(scdPidVo));

		model.put("scds_schdlId", schdlId);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		String fncCal=request.getParameter("fncCal");
		
		if(returnSchdlVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())){
			userAuth = "pass";
		}else{
			if(fncCal != null){
				if(fncCal.equals("psn") || fncCal.equals("my") ){
					userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), returnSchdlVo.getUserUid());
				}else{
					userAuth ="fail";
				}
			}else{
				userAuth ="fail";
			}
		}
		
		model.put("userAuth", userAuth);
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"viewEvntPop", model);		
		return LayoutUtil.getJspPath("/wc/viewEvntPop");
	}
	
	/** 공통기념일수정 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/adm/setCommAnnvModPop")
	public String setCommAnnvModPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception{
		
		String schdlId = request.getParameter("scds_schdlId");
		
		String repetStartY = request.getParameter("repetStartY");
		String repetEndY = request.getParameter("repetEndY");
		
		model.put("repetStartY", repetStartY);
		model.put("repetEndY", repetEndY);
		model.put("scds_schdlId", schdlId);
//	 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId(); 
		
		// 기념일 상세
		WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
		wcAnnvDVo.setCompId(compId);
		wcAnnvDVo.setSchdlId(schdlId);
		
		wcAnnvDVo = (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
		if (wcAnnvDVo != null) {
			// 리소스기본(DM_RESC_B) 테이블 - 조회, 모델에 추가
			wcRescSvc.queryRescBVo(wcAnnvDVo.getRescId(), model);
			model.put("wcAnnvDVo", wcAnnvDVo);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 국가 목록
		WcNatBVo wcNatBVo = new WcNatBVo();
		wcNatBVo.setQueryLang(langTypCd);
		wcNatBVo.setCompId(compId);
		
		@SuppressWarnings("unchecked")
		List<WcNatBVo> wcNatBVoList = (List<WcNatBVo>)commonSvc.queryList(wcNatBVo);
		model.put("wcNatBVoList", wcNatBVoList);
				
		// set, list 로 시작하는 경우 처리
//		checkPath(request,"setCommonAnnvModPop", model);		
		return LayoutUtil.getJspPath("/wc/adm/setCommAnnvModPop");
	}
	
	/** 공통기념일수정 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/adm/transSetCommAnnvPopUpdate")
	public String transSetCommAnnvPopUpdate(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;				

		try {	// Multipart 파일 업로드
			
			uploadHandler = wcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
		
			//하나의 트랜 잭션으로 묶기 위한 List
			QueryQueue queryQueue = new QueryQueue();
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			
			
			/**
			 * 수정처리 로직변경 : 일괄삭제후 -> 인서트 
			 * 반복설정 때문
			 */
			String schdlId = request.getParameter("schdlId");
			String commAnnvTypCode = "5";
			
			// 일정 그룹 삭제
			//반복설정되어있는 schdlPID로 List검색
			WcSchdlBVo scdThisVo = new WcSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			scdThisVo.setSchdlTypCd(commAnnvTypCode);
			WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
			WcSchdlBVo scdPidVo = new WcSchdlBVo();
			scdPidVo.setSchdlTypCd(commAnnvTypCode);
			scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid()); 
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> schdPidList = (List<WcSchdlBVo>) commonDao.queryList(scdPidVo);

			if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){
					for(WcSchdlBVo scdPid : schdPidList){
						WcSchdlBVo scdVo = new WcSchdlBVo();
						scdVo.setSchdlId(scdPid.getSchdlId());
						scdVo.setSchdlTypCd(commAnnvTypCode);
						queryQueue.delete(scdVo);

						//반복설정되어있으면 파일도 같이 삭제
						wcFileSvc.deleteWcFile(scdPid.getSchdlId(), queryQueue);
					}
					//반복설정되어있는 해당일정PID 모두삭제

				WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
				repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
				WcRepetSetupDVo repetVal = (WcRepetSetupDVo) commonDao.queryVo(repetVo);

				//반복설정삭제
				if(repetVal.getSchdlId() != null && repetVal != null) queryQueue.delete(repetVal);
			}	
				
			
			
			UserVo userVo = LoginSession.getUser(request);
			
			try {
				wcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));
				wcsVo.setSchdlPid(wcsVo.getSchdlId());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			int schdlTyp=-1;	
			
			//=================================================================
			//schdl Param값
			//=================================================================
			String choiDt = request.getParameter("setAnnvChoiDt");
			String grpId = request.getParameter("grpId");
			//종료일시 만들어야 함
//			Calendar startCal=Calendar.getInstance();
//			startCal.set(Integer.parseInt(choiDt.substring(0,4)), Integer.parseInt(choiDt.substring(5,7))-1, Integer.parseInt(choiDt.substring(8,10)));
//			startCal.add(Calendar.DATE, Integer.parseInt(afterDay)-1);
//			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
			String holiYn = request.getParameter("holiYn");	
			String solaYn = request.getParameter("solaYn");	
			String cmltDt = request.getParameter("setAnnvCmltDt");
			//String publYn = request.getParameter("publYn");			
//			String editor1 = request.getParameter("editor1");
//			String loc = request.getParameter("loc");
			String endTm = "23:59";
			String strtTm = "00:00";
			
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
			

			//=================================================================
			//WcSchdlBVo 일정 userVo값
			//=================================================================
			wcsVo.setCompId(userVo.getCompId());
			wcsVo.setUserUid(userVo.getUserUid());
			if(request.getParameter("agnt")!=null&&!request.getParameter("agnt").equals("")&&!request.getParameter("agnt").equals("-1"))
				wcsVo.setUserUid(request.getParameter("agnt"));
			wcsVo.setDeptId(userVo.getDeptId());
//			wcsVo.setGrpId(userVo.getGradeCd());
			wcsVo.setRegrUid(userVo.getUserUid());
			wcsVo.setRegrNm(userVo.getUserNm());
			
			String natCd = request.getParameter("natCd");
			// 일정이 수정이 아닌 삭제 되고 새로 생성 되므로 일정상세도 동일하게 적용
			WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
			wcAnnvDVo.setCompId(userVo.getCompId());
			wcAnnvDVo.setSchdlId(schdlId);
			wcAnnvDVo = (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
			
			if(wcAnnvDVo != null){
				WcRescBVo wcRescBVo = new WcRescBVo();
				wcRescBVo.setCompId(userVo.getCompId());
				wcRescBVo.setRescId(wcAnnvDVo.getRescId());
				//리소스 삭제
				queryQueue.delete(wcRescBVo);
				//상세 삭제
				queryQueue.delete(wcAnnvDVo);
			}
			
			WcRescBVo wcRescBVo = wcRescSvc.collectBaRescBVo(request, "", queryQueue);
			if (wcRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기념일 상세 저장
			wcAnnvDVo = new WcAnnvDVo();
			wcAnnvDVo.setCompId(userVo.getCompId());
			wcAnnvDVo.setSchdlId(wcsVo.getSchdlId());
			wcAnnvDVo.setRescId(wcRescBVo.getRescId());
			wcAnnvDVo.setNatCd(natCd);
			
			queryQueue.insert(wcAnnvDVo);
			
			// 제목 
			String annvNm = wcRescBVo.getRescVa();
			
			//제목
			wcsVo.setSubj(annvNm);
			//장소명
//			wcsVo.setLocNm(loc);			
			//공개여부 1.공개, 2.지정인공개, 3.비공개
//			publYn ="1";
			
//			wcsVo.setOpenGradCd(publYn);			
			//내용
//			wcsVo.setCont(editor1);
			//=================================================================
//				wscVo.setSchdlId("");
			
			//일정구분코드  5.공통기념일
			wcsVo.setSchdlTypCd("5");
			//일정종류코드 1.나의일정, 2.그룹, 3.회사 4.부서
			wcsVo.setSchdlKndCd(String.valueOf(schdlTyp));
			//양음력 값
			wcsVo.setSolaLunaYn(solaYn);
			//휴일여부 NULL체크
			wcsVo.setHoliYn(holiYn);
			
			wcsVo.setAttYn("N");
			
//			wcsVo.setSchdlFileId(fileSaveSize>0?uniqFileId:null);
			
			if(schdlTyp==2)
				wcsVo.setGrpId(grpId);
		
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(choiDt + " "+strtTm);
			//종료 일 +시간
			wcsVo.setSchdlEndDt(cmltDt + " "+endTm);
			
			//일정반복여부
			if(reStartDt.equals(reEndDt))
				repetSetup="N";
			else
				repetSetup="Y";
			wcsVo.setRepetYn(repetSetup);

			queryQueue.insert(wcsVo);
			
			//=================================================================
			//WcSchdlBVo 일정기본
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
			WcRepetSetupDVo wcRepetVo = new WcRepetSetupDVo();
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
				wcScdManagerSvc.repetSetup(wcRepetVo,wcsVo,new ArrayList<WcPromGuestDVo>(),queryQueue, request);
			}
			commonDao.execute(queryQueue);

			// set, list 로 시작하는 경우 처리
			checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/wc/adm/listCommAnnv.do?menuId="+request.getParameter("menuId")
					+"');");
			
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
		return LayoutUtil.getResultJsp();
	}
	
	/** 공통기념일삭제 [FORM SUBMIT] 
	 * @throws Exception */          
	@RequestMapping(value = "/wc/adm/transSetCommAnnvModPopDel" , method = RequestMethod.GET)
	public String transSetCommAnnvModPopDel(HttpServletRequest request,
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

			String commAnnvTypCode = "5";
			// 일정 그룹 삭제
			QueryQueue queryQueue = new QueryQueue();


			//반복설정되어있는 schdlPID로 List검색
			WcSchdlBVo scdThisVo = new WcSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			scdThisVo.setSchdlTypCd(commAnnvTypCode);
			WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
			WcSchdlBVo scdPidVo = new WcSchdlBVo();
			scdPidVo.setSchdlTypCd(commAnnvTypCode);
			scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid()); 
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> schdPidList = (List<WcSchdlBVo>) commonDao.queryList(scdPidVo);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 일정상세 삭제
			WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
			wcAnnvDVo.setCompId(userVo.getCompId());
			wcAnnvDVo.setSchdlId(returnSchdlVo.getSchdlPid());
			wcAnnvDVo = (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
			
			if(wcAnnvDVo != null){
				WcRescBVo wcRescBVo = new WcRescBVo();
				wcRescBVo.setCompId(userVo.getCompId());
				wcRescBVo.setRescId(wcAnnvDVo.getRescId());
				//리소스 삭제
				queryQueue.delete(wcRescBVo);
				//상세 삭제
				queryQueue.delete(wcAnnvDVo);
			}
						
			if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){

					
					for(WcSchdlBVo scdPid : schdPidList){
						
						WcSchdlBVo scdVo = new WcSchdlBVo();
						scdVo.setSchdlId(scdPid.getSchdlId());
						scdVo.setSchdlTypCd(commAnnvTypCode);
						queryQueue.delete(scdVo);

						//반복설정되어있으면 파일도 같이 삭제
						wcFileSvc.deleteWcFile(scdPid.getSchdlId(), queryQueue);
					}
					//반복설정되어있는 해당일정PID 모두삭제

				WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
				repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
				WcRepetSetupDVo repetVal = (WcRepetSetupDVo) commonDao.queryVo(repetVo);

				//반복설정삭제
				if(repetVal.getSchdlId() != null && repetVal != null) queryQueue.delete(repetVal);

				commonDao.execute(queryQueue);
				
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
			}
			
			//반복설정 이면 부모 삭제 없으면 this 삭제
//			WcSchdlBVo scdVo = new WcSchdlBVo();
//			scdVo.setSchdlId(schdlId);
//			queryQueue.delete(scdVo);
			
			//반복설정 부모삭제 / 없으면  this 파일삭제
//			wcFileSvc.deleteWcFile(schdlId, queryQueue);



			//반복설정 이면 부모 참석자삭제 없으면 this 삭제
//			WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
//			promGuestVo.setSchdlId(schdlId);
//			queryQueue.delete(promGuestVo);

			//반복설정 검색
		


			// cm.msg.del.success=삭제 되었습니다.
			
			

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 기념일수정 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/setAnnvModPop")
	public String setAnnvModPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		String schdlId = request.getParameter("scds_schdlId");
		
		String repetStartY = request.getParameter("repetStartY");
		String repetEndY = request.getParameter("repetEndY");
		
		
		
		model.put("repetStartY", repetStartY);
		model.put("repetEndY", repetEndY);
		model.put("scds_schdlId", schdlId);
//	 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setAnnvModPop", model);		
		return LayoutUtil.getJspPath("/wc/setAnnvModPop");
	}
	
	/** 기념일수정 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/transSetAnnvPopUpdate")
	public String transSetAnnvPopUpdate(HttpServletRequest request,
			ModelMap model) throws Exception{

		UploadHandler uploadHandler = null;				

		try {	// Multipart 파일 업로드

			uploadHandler = wcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();

			//하나의 트랜 잭션으로 묶기 위한 List
			QueryQueue queryQueue = new QueryQueue();

			//File Id 생성
			//			String uniqFileId=wcCmSvc.createFileId()+"";

			// 게시파일 저장
			//			List<CommonFileVo> deletedFileList = wcFileSvc.saveScdlFile(request, uniqFileId, queryQueue);				

			String schdlId = request.getParameter("schdlId");
			//=================================================================
			//schdl Param값
			//=================================================================
			String annvNm = request.getParameter("annvNm");
			String locNm = request.getParameter("loc");
			String choiDt = request.getParameter("setAnnvChoiDt");
			//String afterDay = request.getParameter("selectStartYmd");
			//종료일시 만들어야 함
			//			Calendar startCal=Calendar.getInstance();
			//			startCal.set(Integer.parseInt(choiDt.substring(0,4)), Integer.parseInt(choiDt.substring(5,7))-1, Integer.parseInt(choiDt.substring(8,10)));
			//			startCal.add(Calendar.DATE, Integer.parseInt(afterDay)-1);
			//			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
			//			String cmltDt = repetDateFormat.format(startCal.getTime());
			//			String strtTm = request.getParameter("strtTm");
			//			String endTm = request.getParameter("endTm");
			String cmltDt = request.getParameter("setAnnvCmltDt");
			String cont = request.getParameter("editor1");

			String holiYn = request.getParameter("holiYn");
			String solaYn = request.getParameter("solaYn");
			String publYn = request.getParameter("publYn");


			//String repetStartYear = request.getParameter("startYear");
			//String repetEndYear = request.getParameter("endYear");
			String strtTm = "00:00";
			String endTm = "23:00";
			//=================================================================
			//수정시작
			//=================================================================
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			wcsVo.setSchdlId(schdlId);
			wcsVo.setSubj(annvNm);
			wcsVo.setLocNm(locNm);
			wcsVo.setSchdlStartDt(choiDt + " " + strtTm);
			wcsVo.setSchdlEndDt(cmltDt + " " + endTm);
			wcsVo.setHoliYn(holiYn);
			wcsVo.setSolaLunaYn(solaYn);
			wcsVo.setCont(cont);
			//공개여부 1.공개, 2.지정인공개, 3.비공개
			if(publYn.equalsIgnoreCase("publ")){
				publYn ="1";
			}else if(publYn.equalsIgnoreCase("apntPubl")){
				publYn ="2";
			}else{
				publYn ="3";
			}
			wcsVo.setOpenGradCd(publYn);

			queryQueue.update(wcsVo);

			//			WcRepetSetupDVo wcrVo = new WcRepetSetupDVo();
			//			wcrVo.setSchdlId(schdlId);
			//			wcrVo.setRepetStartDt(repetStartYear);
			//			wcrVo.setRepetEndDt(repetEndYear);
			//			
			//			queryQueue.update(wcrVo);

			int fileSaveSize;
			String betweenDay =	wcScdManagerSvc.betweenDay(wcsVo.getSchdlStartDt(), wcsVo.getSchdlEndDt());

			//##############################################################
			// 기념일은 무조건 전체 반영
			//##############################################################
			//String savePath = "";
			//String[] splitData;


			WcSchdlBVo wcsThisVo = new WcSchdlBVo();
			wcsThisVo.setSchdlId(wcsVo.getSchdlId());
			WcSchdlBVo wcsReturnThisVo = (WcSchdlBVo) commonDao.queryVo(wcsThisVo);
			//String schdlPid= null;
			WcSchdlBVo wcsPidVo = new WcSchdlBVo();
			//pid 셋팅
			wcsPidVo.setSchdlPid(wcsReturnThisVo.getSchdlPid());
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
			List<WcSchdlBVo> wcsReturnVo = (List<WcSchdlBVo>) commonDao.queryList(wcsPidVo);
			if(wcsReturnVo.size() != 0){
				for(WcSchdlBVo wcsItem: wcsReturnVo){
					WcSchdlBVo newWcsVo = (WcSchdlBVo) wcsVo.clone();
					newWcsVo.setSchdlId(wcsItem.getSchdlId());
					//같은 스케쥴 아이디면 날짜 변경 작업을 하지 않는다.
					if(!wcsItem.getSchdlId().equals(wcsVo.getSchdlId())){
						@SuppressWarnings("static-access")
						Calendar repetStartCal = wcScdManagerSvc.stringConvertCal(wcsItem.getSchdlStartDt(),true);

						int date = Integer.parseInt(wcsVo.getSchdlStartDt().substring(8,10));
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

						//기념일 공통 전체 선택한일 년도 변경
						repetStartCal.set(Calendar.DATE, date);

						String startDt = formatter.format(repetStartCal.getTime());

						//선택한 며칠 부터 공통작업 endDt변경
						repetStartCal.add(Calendar.DATE, Integer.parseInt(betweenDay)-1);
						String endDt = formatter.format(repetStartCal.getTime());

						newWcsVo.setSchdlStartDt(startDt);
						newWcsVo.setSchdlEndDt(endDt);
					}



					//파일전체반영
					wcFileSvc.saveScdlFile(request, wcsItem.getSchdlId() , queryQueue, "all");
					fileSaveSize=queryQueue.size();
					newWcsVo.setAttYn(fileSaveSize>0?"Y":"N");

					queryQueue.update(newWcsVo);
				}

				//				//startDt와 EndDt때문에 최종부모의 값을 가져와야한다.
				//				WcSchdlBVo wcsPid = new WcSchdlBVo();
				//				wcsPid.setSchdlId(wcsPidVo.getSchdlPid());
				//				WcSchdlBVo wcsFindPid = (WcSchdlBVo) commonDao.queryVo(wcsPid);
				//				
				//				
				//				//부모 값도 clone으로 변경
				//				WcSchdlBVo newWcsVo = (WcSchdlBVo) wcsVo.clone();
				//				newWcsVo.setSchdlId(wcsFindPid.getSchdlId());
				//
				//				//this가 부모이면 날짜를 수정하면 안된다.
				//				if(schdlPid.equals("false")){
				//					Calendar repetStartCal = wcScdManagerSvc.stringConvertCal(wcsFindPid.getSchdlStartDt(),true);
				//					
				//					int date = Integer.parseInt(wcsFindPid.getSchdlStartDt().substring(8,10));
				//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				//					
				//					//기념일 공통 전체 선택한일 년도 변경
				//					repetStartCal.set(Calendar.DATE, date);
				//					
				//					String startDt = formatter.format(repetStartCal.getTime());
				//
				//					//선택한 며칠 부터 공통작업 endDt변경
				//					repetStartCal.add(Calendar.DATE, Integer.parseInt(betweenDay) -1);
				//					String endDt = formatter.format(repetStartCal.getTime());
				//					 
				//					
				//					newWcsVo.setSchdlStartDt(startDt);
				//					newWcsVo.setSchdlEndDt(endDt);
				//					
				//				}
				//				
				//				//(파일) 자기자신 저장 후 시작
				//				wcFileSvc.saveScdlFile(request, wcsFindPid.getSchdlId() , queryQueue, "all");
				//				fileSaveSize=queryQueue.size();
				//				newWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
				//				
				//				queryQueue.update(newWcsVo);
			}

			commonDao.execute(queryQueue);

			// set, list 로 시작하는 경우 처리
			checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.

			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(request.getParameter("fncCal").equalsIgnoreCase("mng")){
				model.put("todo", "parent.location.replace('/wc/listSchdl.do?fncCal="+request.getParameter("fncCal")+
						"&menuId="+request.getParameter("menuId")
						+"');");
			}else{
				model.put("todo", "parent.location.replace('/wc/listPsnSchdl.do?fncCal="+request.getParameter("fncCal")+
						"&menuId="+request.getParameter("menuId")
						+ "&molyYear="+request.getParameter("molyYear")
						+ "&molyMonth="+request.getParameter("molyMonth")
						+ "&welyYear="+request.getParameter("welyYear")
						+ "&welyMonth="+request.getParameter("welyMonth")
						+ "&welyWeek="+request.getParameter("welyWeek")
						+ "&dalyYear="+request.getParameter("dalyYear")
						+ "&dalyMonth="+request.getParameter("dalyMonth")
						+ "&dalyDay="+request.getParameter("dalyDay")
						+ "&fncCal="+request.getParameter("fncCal")
						+ "&tabNo="+request.getParameter("tabNo")
						+ "&agnt="+request.getParameter("agnt")
						+"');");
			}
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
		return LayoutUtil.getResultJsp();
	}
	
	/** 기념일삭제 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/transSetAnnvModPopDel" , method = RequestMethod.POST)
	public String transSetAnnvModPopDel(HttpServletRequest request,
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
			WcSchdlBVo scdThisVo = new WcSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
			WcSchdlBVo scdPidVo = new WcSchdlBVo();
			scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid()); 
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> schdPidList = (List<WcSchdlBVo>) commonDao.queryList(scdPidVo);

			if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){

					//반복설정된 참석자 삭제
					for(WcSchdlBVo scdPid : schdPidList){
						//참석자삭제
						WcSchdlBVo scdVo = new WcSchdlBVo();
						scdVo.setSchdlId(scdPid.getSchdlId());
						queryQueue.delete(scdVo);

						//반복설정되어있으면 파일도 같이 삭제
						wcFileSvc.deleteWcFile(scdPid.getSchdlId(), queryQueue);
					}
					//반복설정되어있는 해당일정PID 모두삭제

				WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
				repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
				WcRepetSetupDVo repetVal = (WcRepetSetupDVo) commonDao.queryVo(repetVo);

				//반복설정삭제
				if(repetVal.getSchdlId() != null && repetVal != null) queryQueue.delete(repetVal);

				commonDao.execute(queryQueue);
				
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				model.put("result", "ok");
			}
			
			//반복설정 이면 부모 삭제 없으면 this 삭제
//			WcSchdlBVo scdVo = new WcSchdlBVo();
//			scdVo.setSchdlId(schdlId);
//			queryQueue.delete(scdVo);
			
			//반복설정 부모삭제 / 없으면  this 파일삭제
//			wcFileSvc.deleteWcFile(schdlId, queryQueue);



			//반복설정 이면 부모 참석자삭제 없으면 this 삭제
//			WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
//			promGuestVo.setSchdlId(schdlId);
//			queryQueue.delete(promGuestVo);

			//반복설정 검색
		


			// cm.msg.del.success=삭제 되었습니다.
			
			

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wc/downFile","/wc/preview/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds,
			@RequestParam(value = "actionParam", required = false) String actionParam
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
						
			// 파일 목록조회
			ModelAndView mv = wcCmSvc.getFileList(request , fileIds , actionParam);
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	
	/** 일정 보내기 이메일전송 [FORM SUBMIT] -삭제예정
	 * @throws Exception */
	@RequestMapping(value = "/wc/setSchdlSendTemp" , method = RequestMethod.POST)
	public String setSchdlSendTemp(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String sendEmailDt  = (String) object.get("sendEmailDt");
			String sendOpt  = (String) object.get("sendOpt");
			//			String bullId = (String) object.get("bullId");
			if (sendEmailDt == null || sendEmailDt.equalsIgnoreCase("")) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			
			List<WcSchdlBVo> wcsList = wcScdManagerSvc.getSchdlSendMail(sendEmailDt, sendOpt, request, cmEmailBVo);
			
			if(wcsList.size() == 0){
				// 해당일에 대한 일정이 없습니다.
				throw new CmException("wc.jsp.setProm.sendSchdlError", request);
			}
			
			String Context = "";
			 
			//반복설정과 여러개의 스케쥴의 파일이 많아서 안보내기로함.
			List<CommonFileVo> allFileList = new ArrayList<CommonFileVo>();
			
			for(WcSchdlBVo wcsVo : wcsList){
				Context += "[" + wcsVo.getSchdlStartDt().substring(0,16) + " ~ " + wcsVo.getSchdlStartDt().substring(0,16) + "] "
						+ wcsVo.getLocNm() + " (" + wcsVo.getSubj() + ")" + "</br>";
			}
			
//			cmEmailBVo.setSubj(userVo.getUserNm() + "[" + wcsList  +"]");
			cmEmailBVo.setCont(Context);
			
			
//			JSONArray recvIds = (JSONArray) object.get("recvIds");
			JSONArray recvIds = null;
			
			
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , recvIds , allFileList , queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			model.put("emailId", emailId);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 권한설정 [POP] */
	@RequestMapping(value = "/wc/grp/setAuthPop")
	public String setAuthPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		
		return LayoutUtil.getJspPath("/wc/grp/setAuthPop");
	}
	
	/** 마스터변경 [POP] */
	@RequestMapping(value = "/wc/grp/setMastPop")
	public String setMastPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
		WcSchdlGrpMbshDVo wcSchdlGrpMast = wcScdManagerSvc.findMastUser(request, wcSchdlGrpMbshDVo);
		model.put("mastNm", wcSchdlGrpMast.getUserNm());
		model.put("prevMastUid", wcSchdlGrpMast.getUserUid());
		model.put("schdlGrpId", wcSchdlGrpMast.getSchdlGrpId());

		return LayoutUtil.getJspPath("/wc/grp/setMastPop");
	}
	
	
	/** 그룹등록 [POP] */
	@RequestMapping (value = "/wc/grp/setGrpPop")
	public String setGrpPop(HttpServletRequest request, ModelMap model) throws Exception{
		
		
		model.put("schdlGrpId", request.getParameter("grpMbshSchdlGrpId"));
		model.put("grpMbshUids", request.getParameterValues("grpMbshUid"));
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/wc/grp/setGrpPop");
	}
	
	
	/** 그룹수정 [POP] */
	@RequestMapping (value = "/wc/grp/setGrpModPop")
	public String setGrpModPop(HttpServletRequest request, ModelMap model) throws Exception{

		// 조회조건 매핑
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();

		wcSchdlGrpBVo.setSchdlGrpId(request.getParameter("schdlGrpId"));

		Map<String,Object> rsltMap2 = wcScdManagerSvc.getWcGroupMapList(request, wcSchdlGrpBVo);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> wcSchdlGroupBMapList = (List<Map<String, Object>>)rsltMap2.get("wcSchdlGroupBMapList");
		
		if( wcSchdlGroupBMapList!=null && wcSchdlGroupBMapList.size()>0)
		{
			Map<String, Object> wcSchdlGroupMap = wcSchdlGroupBMapList.get(0);
			VoUtil.fromMap(wcSchdlGrpBVo, wcSchdlGroupMap);
		}
		model.put("wcSchdlGroupB", wcSchdlGrpBVo);
		
		
		return LayoutUtil.getJspPath("/wc/grp/setGrpModPop");
	}
	
	
	/** 그룹등록 저장 [BUTTON] */
	@RequestMapping (value = "/wc/grp/transSetGrpPopSave" , method= RequestMethod.POST)
	public String transSetGrpPopSave(HttpServletRequest request, ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
		VoUtil.bind(request, wcSchdlGrpBVo);
		
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setQueryLang(langTypCd);
		
		wcSchdlGrpBVo.setCompId(userVo.getCompId());
		wcSchdlGrpBVo.setRegrUid(userVo.getUserUid());
		wcSchdlGrpBVo.setMastrUid(userVo.getUserUid());
		wcSchdlGrpBVo.setRegrNm(userVo.getUserNm());
		wcScdManagerSvc.setWcSchdlGrpReg(request, wcSchdlGrpBVo);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/wc/grp/listGrp.do?menuId="+request.getParameter("menuId")+"')");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 그룹등록 수정 [BUTTON] */
	@RequestMapping (value = "/wc/grp/transSetGrpModPopUpdate" , method= RequestMethod.POST)
	public String transSetGrpModPopUpdate(HttpServletRequest request, ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
		VoUtil.bind(request, wcSchdlGrpBVo);
		
		wcSchdlGrpBVo.setQueryLang(langTypCd);
		
		wcScdManagerSvc.setWcSchdlGrpMod(request, wcSchdlGrpBVo);
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.location.replace('/wc/grp/listGrp.do?&menuId="+request.getParameter("menuId")+"')");
		
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	/** 그룹 내 관리 [POP] */
	@RequestMapping(value = "/wc/grp/listGrpMng")
	public String listGrpMng(HttpServletRequest request, ModelMap model) throws Exception {
		
	
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		QueryQueue queryQueue = new QueryQueue();
		
		// 조회조건 매핑
		WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
		VoUtil.bind(request, wcSchdlGrpMbshDVo);
		wcSchdlGrpMbshDVo.setQueryLang(langTypCd);
		
		model.put("schdlGrpId", request.getParameter("schdlGrpId"));
		
		// 회원 등록
		if(request.getParameter("behave").equals("add")){
		model.put("wvSurvBVo", wcScdManagerSvc.setWcSchldGrpMbshList(request, wcSchdlGrpMbshDVo, queryQueue));
		}
		
		// 회원 삭제
		if(request.getParameter("behave").equals("del")){
			wcScdManagerSvc.delGrpMbsh(request, wcSchdlGrpMbshDVo, queryQueue);
		}
		
		// 권한 설정
		if(request.getParameter("behave").equals("authSet")){
			wcScdManagerSvc.updateGrpMbshAuthGrad(request, wcSchdlGrpMbshDVo, queryQueue);
		}
		
		// 마스터 변경
		if(request.getParameter("behave").equals("modMast")){
			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
			wcScdManagerSvc.updateGrpMast(request, wcSchdlGrpMbshDVo, wcSchdlGrpBVo);
			model.put("mastUid", request.getParameter("userUid"));
		}
		
		Map<String,Object> rsltMap = wcScdManagerSvc.getWcSchdlGrpMbshList(request, wcSchdlGrpMbshDVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wcSchdlGroupMbshDMapList", rsltMap.get("wcSchdlGroupMbshDMapList"));
		/*if(!request.getParameter("behave").equals("modMast"))
		{
			model.put("mastUid", rsltMap.get("mastUid"));
		}*/
		model.put("mastUid", rsltMap.get("mastUid"));
		model.put("grpNm", request.getParameter("grpNm"));
		if(request.getParameter("behave").equals("N")){
			model.put("schdlGrpId", wcSchdlGrpMbshDVo.getSchdlGrpId());
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 사용자 권한상태에 따른 하단 버튼 처리 
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo();
		wcSchdlGrpBVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		
		Map<String,Object> rsltMap2 = wcScdManagerSvc.getWcGroupMapList(request, wcSchdlGrpBVo);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> wcSchdlGroupBMapList = (List<Map<String, Object>>)rsltMap2.get("wcSchdlGroupBMapList");
		
		if( wcSchdlGroupBMapList!=null && wcSchdlGroupBMapList.size()>0)
		{
			Map<String, Object> wcSchdlGroupMap = wcSchdlGroupBMapList.get(0);
			VoUtil.fromMap(wcSchdlGrpBVo, wcSchdlGroupMap);
		}
		model.put("wcSchdlGroupB", wcSchdlGrpBVo);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/wc/grp/listGrpMng");
	}
	

	/** 지도팝업 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/setMapPop" , method = RequestMethod.POST)
	public String setMapPop(HttpServletRequest request,
			ModelMap model) throws Exception{		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setMapPop", model);			
		return LayoutUtil.getJspPath("/wc/setMapPop");
	}
	
	/** 전체삭제 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/transSetAllModPopAllDel","/wc/adm/transSetAllModPopAllDel"})
	public String transSetAllModPopAllDel(HttpServletRequest request,
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
			WcSchdlBVo scdThisVo = new WcSchdlBVo();
			scdThisVo.setSchdlId(schdlId);
			WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
			WcSchdlBVo scdPidVo = new WcSchdlBVo();
			scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
			scdPidVo.setRepetYn("Y"); // 반복여부
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> schdPidList = (List<WcSchdlBVo>) commonDao.queryList(scdPidVo);
			
			if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){
				//반복설정된 참석자 삭제
				for(WcSchdlBVo wcsVo : schdPidList){
					//참석자삭제
					WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
					promGuestVo.setSchdlId(wcsVo.getSchdlId());
					queryQueue.delete(promGuestVo);
					
					/** 부서일정 - 선택부서 목록 삭제 */
					if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
						wcScdManagerSvc.deleteWcSchdlDeptRVo(queryQueue, wcsVo.getSchdlId());
					
					//스케쥴 삭제
					WcSchdlBVo wcsOneVo = new WcSchdlBVo();
					wcsOneVo.setSchdlId(wcsVo.getSchdlId());
					queryQueue.delete(wcsOneVo);
					
					//파일 삭제
					wcFileSvc.deleteWcFile(wcsVo.getSchdlId(), queryQueue);
				}
				
				//반복설정 삭제
				WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
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
	
	/** 공통 기념일 관리 */
	@RequestMapping(value = "/wc/adm/listCommAnnv")
	public String listCommAnnv(HttpServletRequest request, 
			ModelMap model) throws Exception {
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		VoUtil.bind(request, wcSchdlBVo);
		
		wcSchdlBVo.setUserUid(userVo.getUserUid());
		wcSchdlBVo.setSchdlTypCd("5");
		wcSchdlBVo.setQueryLang(langTypCd);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			wcSchdlBVo.setSchCompId(userVo.getCompId());
		//}
				
		checkPath(request,"listCommAnnv", model);
		
		Map<String,Object> rsltMap = wcScdManagerSvc.getCommAnnv(request, wcSchdlBVo, model);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("commonAnnv", rsltMap.get("commAnnvList"));

		return LayoutUtil.getJspPath("/wc/adm/listCommAnnv");
	}
	
	/** 공통기념일view 팝업 오픈
	 * @throws Exception */
	@RequestMapping(value = "/wc/adm/viewCommAnnvPop")
	public String viewCommAnnvPop(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		String schdlId = request.getParameter("scds_schdlId");
		
		WcRepetSetupDVo returnRepet = new WcRepetSetupDVo();
		WcRepetSetupDVo wcrVo = new WcRepetSetupDVo();
		
		wcrVo.setSchdlId(schdlId);
		
		returnRepet = (WcRepetSetupDVo) commonDao.queryVo(wcrVo);
		
		if(returnRepet == null){
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			wcsVo.setSchdlId(schdlId);
			wcsVo = (WcSchdlBVo) commonDao.queryVo(wcsVo);
			WcRepetSetupDVo wcsPidVo = new WcRepetSetupDVo();
			wcsPidVo.setSchdlId(wcsVo.getSchdlPid());
			returnRepet = (WcRepetSetupDVo) commonDao.queryVo(wcsPidVo);
		}
		
		 
		model.put("returnWcrVo", returnRepet);
		model.put("scds_schdlId", schdlId);
//	 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		// set, list 로 시작하는 경우 처리
		checkPath(request,"viewCommAnnvPop", model);		
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		// 기념일 상세
		WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
		wcAnnvDVo.setQueryLang(langTypCd);
		wcAnnvDVo.setCompId(userVo.getCompId());
		wcAnnvDVo.setSchdlId(schdlId);
		
		wcAnnvDVo = (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
		if (wcAnnvDVo != null) {
			// 국가 정보
			WcNatBVo wcNatBVo = new WcNatBVo();
			wcNatBVo.setQueryLang(langTypCd);
			wcNatBVo.setCompId(userVo.getCompId());
			wcNatBVo.setCd(wcAnnvDVo.getNatCd());
			wcNatBVo = (WcNatBVo)commonSvc.queryVo(wcNatBVo);
			model.put("wcNatBVo", wcNatBVo);
			model.put("wcAnnvDVo", wcAnnvDVo);
		}
				
		return LayoutUtil.getJspPath("/wc/adm/viewCommAnnvPop");
	}
	
	/** 공통기념일등록 팝업 오픈
	* @throws Exception */
	@RequestMapping(value = "/wc/adm/setCommAnnvPop")
	public String setCommAnnvPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception{
		// set, list 로 시작하는 경우 처리
		checkPath(request,"setCommAnnvPop", model);
		// 	세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		WcNatBVo wcNatBVo = new WcNatBVo();
		wcNatBVo.setQueryLang(langTypCd);
		if(compId==null || compId.isEmpty()) compId = userVo.getCompId(); 
		wcNatBVo.setCompId(compId);
		
		@SuppressWarnings("unchecked")
		List<WcNatBVo> wcNatBVoList = (List<WcNatBVo>)commonSvc.queryList(wcNatBVo);
		model.put("wcNatBVoList", wcNatBVoList);
		
		return LayoutUtil.getJspPath("/wc/adm/setCommAnnvPop");
	}
		
	
	/** 이메일전송 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/grp/transEmailAjx")
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray sltUserUids=(JSONArray) object.get("selectUserList");
			
			if (sltUserUids == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			 
			//첨부파일이 없을때 allFileList size 0
			List<CommonFileVo> allFileList = new ArrayList<CommonFileVo>();
			
			String Context = "";
//			cmEmailBVo.setSubj(userVo.getUserNm() + "[" + wcsList  +"]");
			cmEmailBVo.setCont(Context);
			
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , sltUserUids , allFileList , queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			model.put("emailId", emailId);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
			
}
