package com.innobiz.orange.mobile.wc.ctrl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;
import com.innobiz.orange.web.wc.calender.WcScdCalMonth;
import com.innobiz.orange.web.wc.calender.WcScdCalWeek;
import com.innobiz.orange.web.wc.svc.WcAdmSvc;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcFileSvc;
import com.innobiz.orange.web.wc.svc.WcMailSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.util.LunalCalenderUtil;
import com.innobiz.orange.web.wc.utils.WcConstant;
import com.innobiz.orange.web.wc.vo.WcAgntRVo;
import com.innobiz.orange.web.wc.vo.WcAnnvDVo;
import com.innobiz.orange.web.wc.vo.WcBumkDVo;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcMailSchdlRVo;
import com.innobiz.orange.web.wc.vo.WcNatBVo;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcRepetSetupDVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlDeptRVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpMbshDVo;
import com.innobiz.orange.web.wc.vo.WcUserGrpBVo;
import com.innobiz.orange.web.wc.vo.WcUserGrpLVo;

/** 일정관리 */
@Controller
public class MoWcSchdlCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoWcSchdlCtrl.class);
	
	/**일정 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;
	
	/** 게시파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	@Autowired
	private LunalCalenderUtil lunalCalenderUtil;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 관리 서비스 */
	@Autowired
	private WcAdmSvc wcAdmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 일정 메일 서비스 */
	@Autowired
	private WcMailSvc wcMailSvc;
	
	/** 일정 보기 */
	@RequestMapping(value = "/wc/listNewSchdl")
	public String listNewSchdl(HttpServletRequest request,			
			@RequestParam(value="action",required=false)
			String action,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		// set, list 로 시작하는 경우 처리
		//checkPath(request, "listPsnSchdl", model);
		int schdlTyp=-1;
		
		String fncCal = request.getParameter("fncCal");
		if(fncCal == null ) fncCal ="psn";		
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
		
		String catId = request.getParameter("catId");
		if(catId!=null )
			if(catId.equals("")||catId.equals("undefined"))
					catId=null;
		
		String schdlKndCd = request.getParameter("schdlKndCd");
		
		// 사용자 설정 정보 조회
		Map<String, String> userSetupMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), WcConstant.USER_CONFIG, false);
		if(schdlKndCd==null) {
			if(userSetupMap!=null && userSetupMap.containsKey("schdlKndCd")) schdlKndCd = userSetupMap.get("schdlKndCd");
			else{
				// 환경설정
				Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
				if(envConfigMap!=null && envConfigMap.containsKey("schdlKndCd")) schdlKndCd = envConfigMap.get("schdlKndCd");				
			}
			if(schdlKndCd!=null) model.put("paramSchdlKndCd", schdlKndCd);
		}
		
		if(choiGrpIds==null && (schdlKndCd == null || "".equals(schdlKndCd) || "2".equals(schdlKndCd)))
		{
			grpResetFlag="false";
			WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
			wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
			wcSchdlGrpBVo.setFncMy("Y");

			//대리인
			if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
				wcSchdlGrpBVo.setUserUid(agnt);
			}
			
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
		
		if("my".equals(fncCal)){
			if(schdlKndCd != null && !"".equals(schdlKndCd)){
				schdlTyp = Integer.parseInt(schdlKndCd);
			}
			// 사용자 설정
			model.put("userSetupMap", userSetupMap);
			model.put("setupClsId", WcConstant.USER_CONFIG);
			
		}else if("open".equals(fncCal)){
			if(schdlKndCd != null && !"".equals(schdlKndCd)){
				schdlTyp = Integer.parseInt(schdlKndCd);
			}else{
				schdlTyp = 1;
			}
			if((viewUserUid == null || viewUserUid.isEmpty()) && (viewOrgId == null || viewOrgId.isEmpty()) && (bumk == null || "-1".equals(bumk)) ){
				viewUserUid = "000000000000";
			}
			
		}else{
			if("psn".equals(fncCal)) schdlTyp=1;
			else if("grp".equals(fncCal)) schdlTyp=2;
			else if("dept".equals(fncCal)) schdlTyp=3;
			else if("comp".equals(fncCal)) schdlTyp=4;
		}
		
		//Action 이 null
		if(action==null){
			Calendar currentCal = Calendar.getInstance();
			int defYear = currentCal.get(Calendar.YEAR);		
			int currentMonth = currentCal.get(Calendar.MONTH) +1;
			int currentWeek = currentCal.get(Calendar.WEEK_OF_MONTH);
			int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(defYear, currentMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId,true);
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
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
					
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

			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
					
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
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId,true);
			WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),catId);
					
			model.put("wcScdCalMonth", scdMonth);
			model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}
		//
		if("my".equals(fncCal)){
			String langTypCd = LoginSession.getLangTypCd(request);
			List<WcAgntRVo> wcAgntList = wcScdManagerSvc.getAgntTgtList(langTypCd, userVo.getUserUid());
			model.put("wcAgntVos", wcAgntList);
		}else if("open".equals(fncCal)){
			List<WcBumkDVo> bumkList = null;
			if(schdlTyp == 1 ) bumkList = wcScdManagerSvc.getBumkPsn(userVo.getUserUid());
			else bumkList = wcScdManagerSvc.getBumkDept(userVo.getUserUid());
			model.put("bumkList", bumkList);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		//일정종류(카테고리)를 model 에 담는다
		for(WcCatClsBVo storedWcCatClsBVo : wcCatClsBVoList){
			model.put("cat_"+storedWcCatClsBVo.getCatId(), VoUtil.toMap(storedWcCatClsBVo, null));
		}
		
		//그룹 목록
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		//대리인
		if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
			wcSchdlGrpBVo.setUserUid(agnt);
		}
		
		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		// 일정대상 목록 조회
		setSchdlKndCdList(model , request);
		model.put("fncCal", fncCal);
		model.put("params", ParamUtil.getQueryString(request));
		return MoLayoutUtil.getJspPath("/wc/listNewSchdl","wc");		
	}
	
	/** 일정 종류 조회 */
	public void setSchdlKndCdList(ModelMap model , HttpServletRequest request){
		//{schdlKndCd,이름,등록수정시적용여부}
		model.put("schdlKndCdList", new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"},{"2",messageProperties.getMessage("wc.option.grpSchdl", request),"Y"},{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}});
	}
	
	/** 조회 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/viewSchdl","/wc/viewMySchdl"}, method = RequestMethod.GET)
	public String viewSchdl(HttpServletRequest request,
			@Parameter(name="schdlId", required=false) String schdlId,
			Locale locale,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		if("viewMySchdl".equals(path)){
			model.put("listPage", "listMySchdl");
			model.put("setPage", "setMySchdl");
		}else{
			model.put("listPage", "listNewSchdl");
			model.put("setPage", "setSchdl");
		}
		
		model.put("plt", request.getParameter("plt"));
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setQueryLang(langTypCd);
		wcSchdlBVo.setSchdlId(schdlId);
		wcSchdlBVo.setUserUid(null);
		/*if(schAdmin){
			wcSchdlBVo.setUserUid(null);
		}else{
			wcSchdlBVo.setUserUid(userVo.getUserUid());
			wcSchdlBVo.setDeptId(userVo.getDeptId());
			wcSchdlBVo.setCompId(userVo.getCompId());
		}*/
		wcSchdlBVo.setWithLob(true);
		wcSchdlBVo =(WcSchdlBVo)commonDao.queryVo(wcSchdlBVo);
		// schdlPID로 count검색
		WcSchdlBVo scdPidVo = new WcSchdlBVo();		
		scdPidVo.setSchdlPid(wcSchdlBVo.getSchdlPid());
		scdPidVo.setRepetYn("Y");// 반복여부
		model.put("scdPidCount", commonDao.count(scdPidVo));

		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		
		if("viewAllSchdlPop".equals(path)){
			userAuth ="fail";
		}else{
			if(wcSchdlBVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())) userAuth = "pass";//등록Uid와 로그인Uid와 비교
			if(userAuth == null) userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), wcSchdlBVo.getUserUid());
		}
		
		// 그룹일정 일 경우 권한을 체크한다.
		if(wcSchdlBVo.getSchdlKndCd() != null && "2".equals(wcSchdlBVo.getSchdlKndCd())){
			String grpId = wcSchdlBVo.getGrpId();
			if(grpId!=null && !grpId.isEmpty()){
				WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = wcScdManagerSvc.getWcSchdlGrpMbshDVo(userVo, grpId);
				if(wcSchdlGrpMbshDVo!=null){
					userAuth = ("O".equals(wcSchdlGrpMbshDVo.getMbshTypCd()) || ("M".equals(wcSchdlGrpMbshDVo.getMbshTypCd()) && ("A".equals(wcSchdlGrpMbshDVo.getAuthGradCd()) || "W".equals(wcSchdlGrpMbshDVo.getAuthGradCd()) ))) ? "pass" : "fail";
				}
			}
		}
		
		model.put("wcSchdlBVo", wcSchdlBVo);
		
		/** 부서일정 - 선택부서 목록 세팅 */
		if(wcSchdlBVo!=null && wcSchdlBVo.getOpenGradCd()!=null && "4".equals(wcSchdlBVo.getOpenGradCd()))
			wcScdManagerSvc.setDeptList(model, schdlId);
		
		//반복설정여부
		if(wcSchdlBVo.getRepetYn() != null && "Y".equals(wcSchdlBVo.getRepetYn()) && wcSchdlBVo.getSchdlPid() != null ){
			WcRepetSetupDVo wcRepetSetupDVo = new WcRepetSetupDVo();
			wcRepetSetupDVo.setSchdlId(wcSchdlBVo.getSchdlPid());
			wcRepetSetupDVo =(WcRepetSetupDVo)commonDao.queryVo(wcRepetSetupDVo);
			model.put("wcRepetSetupDVo", wcRepetSetupDVo);
		}
		
		model.put("userAuth", userAuth);
		
		//참석자 조회
		WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
		wcPromGuestDVo.setQueryLang(langTypCd);
		wcPromGuestDVo.setSchdlId(schdlId);
		@SuppressWarnings("unchecked")
		List<WcPromGuestDVo> wcPromGuestDVoList = (List<WcPromGuestDVo>)commonDao.queryList(wcPromGuestDVo);
		model.put("wcPromGuestDVoList", wcPromGuestDVoList);
		
		model.put("scds_schdlId", schdlId);
		
		//그룹 목록
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setQueryLang(langTypCd);
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		//model.put("paramsForList", ParamUtil.getQueryString(request, "menuId","data","schdlId"));
		model.put("paramsForList", ParamUtil.getQueryString(request, "menuId","schdlId","noCache"));
		model.put("UI_TITLE", messageProperties.getMessage("wc.btn.schdlDetail", locale));//상세보기
		return MoLayoutUtil.getJspPath("/wc/viewSchdl");
	}      
	
	/** 등록 화면
	 * @throws Exception */
	@SuppressWarnings("unused")
	@RequestMapping(value = {"/wc/setSchdl","/wc/setMySchdl"})
	public String setSchdl(HttpServletRequest request,
			@RequestParam(value="schdlId",required=false) String schdlId,
			@RequestParam(value="schdlStartDt",required=false) String schdlStartDt,
			Locale locale,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		boolean schAdmin = false;
		if("setMySchdl".equals(path)){
			model.put("listPage", "listMySchdl");
		}else{
			model.put("listPage", "listNewSchdl");
		}
		
		UserVo userVo = LoginSession.getUser(request);
		
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		//VoUtil.bind(request, wcSchdlBVo);
		
		List<WcPromGuestDVo> wcPromGuestDVoList = null;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		//수정
		if(schdlId != null && !"".equals(schdlId)){
			wcSchdlBVo.setQueryLang(langTypCd);
			wcSchdlBVo.setSchdlId(schdlId);
			wcSchdlBVo.setUserUid(null);
			wcSchdlBVo.setWithLob(true);
			wcSchdlBVo = (WcSchdlBVo)commonDao.queryVo(wcSchdlBVo);
			
			WcSchdlBVo scdPidVo = new WcSchdlBVo();
			scdPidVo.setSchdlPid(wcSchdlBVo.getSchdlPid());
			scdPidVo.setRepetYn("Y");// 반복여부
			model.put("scdPidCount", commonDao.count(scdPidVo));
			
			WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
			wcPromGuestDVo.setSchdlId(schdlId);
			wcPromGuestDVoList = wcScdManagerSvc.getPromGuestLst(wcPromGuestDVo);
			
			/** 부서일정 - 선택부서 목록 세팅 */
			if(wcSchdlBVo!=null && wcSchdlBVo.getOpenGradCd()!=null && "4".equals(wcSchdlBVo.getOpenGradCd()))
				wcScdManagerSvc.setDeptList(model, schdlId);
			
		}else{
			Calendar cal = Calendar.getInstance();
			Integer hour = null;
			if(schdlStartDt != null && !"".equals(schdlStartDt)){
				//String schdlStartDt = wcSchdlBVo.getSchdlStartDt(); 
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
			wcSchdlBVo.setSchdlStartDt(new Timestamp(cal.getTimeInMillis()).toString());
            cal.add(Calendar.HOUR_OF_DAY, 1);
            //종료일자 세팅
            wcSchdlBVo.setSchdlEndDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
		// 첨부파일 리스트 model에 추가
		wcFileSvc.putFileListToModel(schdlId != null ? schdlId : null, model, userVo.getCompId());
		
		model.put("wcSchdlBVo", wcSchdlBVo);
		
		// UI 구성용 - 빈 VO 하나 더함
		/*if(wcPromGuestDVoList == null) wcPromGuestDVoList = new ArrayList<WcPromGuestDVo>();
		wcPromGuestDVoList.add(new WcPromGuestDVo());
		*/
		model.put("wcPromGuestDVoList", wcPromGuestDVoList);		
				
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		//그룹 목록
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");
		//관리 또는 쓰기 권한
		wcSchdlGrpBVo.setWhereSqllet("AND T.AUTH_GRAD_CD IN('A','W')");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		// 일정대상 목록 조회
		setSchdlKndCdList(model , request);
		
		model.put("fncCal", request.getParameter("fncCal"));
		model.put("paramsForList", ParamUtil.getQueryString(request, "menuId","schdlId","schdlStartDt","noCache"));
		String msgCode = "wc.btn.schdl"+(schdlId != null && !"".equals(schdlId) ? "Mod" : "Reg");
		
		model.put("UI_TITLE", messageProperties.getMessage(msgCode, locale));//일정등록:일정수정
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		// 타사 조직도 조회
		if("Y".equals(sysPlocMap.get("globalOrgChartEnable"))){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
		return MoLayoutUtil.getJspPath("/wc/setSchdl");
	}
	
	/** 등록 수정 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/transSchdlPost")
	public String transSchdl(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;				

		try {	// Multipart 파일 업로드
			
			uploadHandler = wcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			//하나의 트랜 잭션으로 묶기 위한 List
			QueryQueue queryQueue = new QueryQueue();
			
			//WcSchdlBVo 일정기본
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			VoUtil.bind(request, wcsVo);
			try {
				//schdlId 생성
				wcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));
				wcsVo.setSchdlPid(wcsVo.getSchdlId());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			// 세션의 언어코드
			UserVo userVo = LoginSession.getUser(request);
			
			// 게시파일 저장
			List<CommonFileVo> deletedFileList = wcFileSvc.saveScdlFile(request, wcsVo.getSchdlId(), queryQueue, "indiv");				
			
			int fileSaveSize=queryQueue.size();
			//=================================================================
			//schdl Param값
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
			
			if(wcsVo.getAlldayYn() == null || wcsVo.getAlldayYn().isEmpty()) wcsVo.setAlldayYn("N");
			
			String schdlStrtTime = ParamUtil.getRequestParam(request, "schdlStrtTime", false);
			String schdlEndTime = ParamUtil.getRequestParam(request, "schdlEndTime", false);
			
			if("Y".equals(wcsVo.getAlldayYn()) || schdlStrtTime == null || schdlStrtTime.isEmpty()) schdlStrtTime="00:00";
			if("Y".equals(wcsVo.getAlldayYn()) || schdlEndTime == null || schdlEndTime.isEmpty()) schdlEndTime="00:00";
			
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(wcsVo.getSchdlStrtYmd() + " " + schdlStrtTime);
			//종료 일 +시간
			wcsVo.setSchdlEndDt(wcsVo.getSchdlEndYmd() + " " + schdlEndTime);
		
			//일정구분코드  1.약속
			//wcsVo.setSchdlTypCd("1");
			//일정종류코드 1.나의일정, 2.그룹, 4.회사 3.부서
			//wcsVo.setSchdlKndCd(String.valueOf(schdlTyp));
			//양음력 값
			
			if(wcsVo.getSolaLunaYn() == null || "".equals(wcsVo.getSolaLunaYn())) wcsVo.setSolaLunaYn("Y");
			
			//휴일여부 NULL체크
			wcsVo.setHoliYn("N");
			//일정상태코드 T보류/C참석/N불참/D약속취소
			wcsVo.setSchdlStatCd("T");
			
			wcsVo.setAttYn(fileSaveSize>0?"Y":"N");
			
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
			wcsVo.setRepetYn(repetSetup);
			
			queryQueue.insert(wcsVo);
			
			// 부서일정 관계 저장 - 부서일정 선택부서 목록
			if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
				wcScdManagerSvc.saveWcSchdlDeptRVo(request, queryQueue, null, wcsVo.getSchdlId(), false);
						
			// 파일 삭제
			wcFileSvc.deleteDiskFiles(deletedFileList);
			
			//=================================================================
			//WcPromGuestDVo 참석자
			//=================================================================
			// 참석자 목록 저장
			List<WcPromGuestDVo> wcPromGuestDVoList = wcScdManagerSvc.saveGuest(request, queryQueue, wcsVo);
			
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
				wcScdManagerSvc.repetSetup(wcRepetVo,wcsVo,wcPromGuestDVoList,queryQueue, request);
			}
			
			commonDao.execute(queryQueue);
			
			// 메일 발송
			wcMailSvc.sendEmail(request, userVo, wcsVo, wcPromGuestDVoList, null);
			
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
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = "/wc/transSchdlModPost")
	public String transSchdlMod(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		UploadHandler uploadHandler = null;		

		try{
			// Multipart 파일 업로드
			uploadHandler = wcFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			//File Id 생성
			//		String uniqFileId=wcCmSvc.createFileId()+"";

			QueryQueue queryQueue = new QueryQueue();
			
			WcSchdlBVo wcsVo = new WcSchdlBVo();
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
			
			String schdlStrtTime = ParamUtil.getRequestParam(request, "schdlStrtTime", false);
			String schdlEndTime = ParamUtil.getRequestParam(request, "schdlEndTime", false);
			
			if("Y".equals(wcsVo.getAlldayYn()) || schdlStrtTime == null || schdlStrtTime.isEmpty()) schdlStrtTime="00:00";
			if("Y".equals(wcsVo.getAlldayYn()) || schdlEndTime == null || schdlEndTime.isEmpty()) schdlEndTime="00:00";
			
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(wcsVo.getSchdlStrtYmd() + " " + schdlStrtTime);
			//종료 일 +시간
			wcsVo.setSchdlEndDt(wcsVo.getSchdlEndYmd() + " " + schdlEndTime);

			//=================================================================
			//WcPromGuestDVo 참석자
			//=================================================================
			
			// 파일복사 목록 - 반복일정 수정시
			List<DmCommFileDVo> copyFileList = null;
			
			// 부서일정 관계 저장 - 부서일정 선택부서 목록
			List<WcSchdlDeptRVo> wcSchdlDeptRVoList=null;			
			if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
				wcSchdlDeptRVoList = (List<WcSchdlDeptRVo>)VoUtil.bindList(request, WcSchdlDeptRVo.class, new String[]{"orgId"});
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 참석자 목록
			List<WcPromGuestDVo> wcPromGuestDVoList = null; 
						
			if(fileSetting.equalsIgnoreCase("all")){
				String betweenDay =	wcScdManagerSvc.betweenDay(wcsVo.getSchdlStartDt(), wcsVo.getSchdlEndDt());

				//##############################################################
				// 전체 반영
				//##############################################################
				WcSchdlBVo wcsThisVo = new WcSchdlBVo();
				wcsThisVo.setSchdlId(wcsVo.getSchdlId());
				
				//자신의 pid값 조회
				WcSchdlBVo wcsReturnThisVo = (WcSchdlBVo) commonDao.queryVo(wcsThisVo);
				//			String schdlPid= null;
				WcSchdlBVo wcsPidVo = new WcSchdlBVo();
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
				List<WcSchdlBVo> wcsReturnVo = (List<WcSchdlBVo>) commonDao.queryList(wcsPidVo);
				if(wcsReturnVo.size() != 0){
					for(WcSchdlBVo wcsItem: wcsReturnVo){
						WcSchdlBVo newWcsVo = (WcSchdlBVo) wcsVo.clone();
						newWcsVo.setSchdlId(wcsItem.getSchdlId());
						//같은 스케쥴 아이디면 날짜 변경 작업을 하지 않는다.
						if(!wcsItem.getSchdlId().equals(wcsVo.getSchdlId())){
							Calendar repetStartCal = wcScdManagerSvc.stringConvertCal(wcsItem.getSchdlStartDt(),true);

							repetStartCal.add(Calendar.DATE, Integer.parseInt(betweenDay)-1);
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							String endDt = formatter.format(repetStartCal.getTime());

							newWcsVo.setSchdlStartDt(wcsItem.getSchdlStartDt().substring(0,10) +  " " + schdlStrtTime);
							newWcsVo.setSchdlEndDt(endDt  + " " + schdlEndTime);
						}
						
						//전체 참석자 추가
						wcPromGuestDVoList = wcScdManagerSvc.saveGuest(request, queryQueue, newWcsVo);

						//파일전체반영
						wcFileSvc.saveScdlFile(request, wcsItem.getSchdlId() , queryQueue, fileSetting);
						int fileSaveSize=queryQueue.size();
						newWcsVo.setAttYn(fileSaveSize>0?"Y":"N");

						queryQueue.update(newWcsVo);
						
						if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
							wcScdManagerSvc.saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, newWcsVo.getSchdlId(), true);
					}
				}

			}else{
				//참석자 추가
				wcPromGuestDVoList = wcScdManagerSvc.saveGuest(request, queryQueue, wcsVo);
				
				// 파일 저장
				wcFileSvc.saveScdlFile(request, wcsVo.getSchdlId() , queryQueue, fileSetting);
				int fileSaveSize=queryQueue.size();
				wcsVo.setAttYn(fileSaveSize>0?"Y":"N");
				
				if(fileSetting.equalsIgnoreCase("repet")){// 반복일정 수정
					//기존 반복일정 삭제
					WcSchdlBVo scdThisVo = new WcSchdlBVo();
					scdThisVo.setSchdlId(wcsVo.getSchdlId());
					WcSchdlBVo returnSchdlVo =(WcSchdlBVo)commonDao.queryVo(scdThisVo);
					WcSchdlBVo scdPidVo = new WcSchdlBVo();
					scdPidVo.setSchdlPid(returnSchdlVo.getSchdlPid());
					scdPidVo.setRepetYn("Y"); // 반복여부
					List<WcSchdlBVo> schdPidList = (List<WcSchdlBVo>) commonDao.queryList(scdPidVo);
					if(schdPidList.size() != 0 && scdPidVo.getSchdlPid() != null){
						//반복설정된 참석자 삭제
						for(WcSchdlBVo storedWcsVo : schdPidList){							
							if(storedWcsVo.getSchdlId().equals(wcsVo.getSchdlId())) continue; // 수정중인 일정은 삭제하지 않는다.
							//참석자삭제
							WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
							promGuestVo.setSchdlId(storedWcsVo.getSchdlId());
							queryQueue.delete(promGuestVo);
							
							//스케쥴 삭제
							WcSchdlBVo wcsOneVo = new WcSchdlBVo();
							wcsOneVo.setSchdlId(storedWcsVo.getSchdlId());
							queryQueue.delete(wcsOneVo);
							
							//파일 삭제
							wcFileSvc.deleteWcFile(storedWcsVo.getSchdlId(), queryQueue);
							
							// 부서일정 - 부서선택 목록 삭제
							if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
								wcScdManagerSvc.deleteWcSchdlDeptRVo(queryQueue, storedWcsVo.getSchdlId());
							
						}
						
						//반복설정 삭제
						WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
						repetVo.setSchdlId(returnSchdlVo.getSchdlPid());
						queryQueue.delete(repetVo);
						
						commonDao.execute(queryQueue);
						queryQueue = new QueryQueue();
					}
					
					wcsVo.setCompId(userVo.getCompId());
					
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
						
						copyFileList = new ArrayList<DmCommFileDVo>();
						
						// 반복일정 저장
						wcScdManagerSvc.repetSetup(wcRepetVo, wcsVo, wcPromGuestDVoList, queryQueue, request, copyFileList);
					}
					
				}
				queryQueue.update(wcsVo);
				
				if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
					wcScdManagerSvc.saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, wcsVo.getSchdlId(), true);
			}

			commonDao.execute(queryQueue);
			
			// 메일 발송
			wcMailSvc.sendEmail(request, userVo, wcsVo, wcPromGuestDVoList, null);
			
			// 파일 복사
			if(copyFileList!=null && copyFileList.size()>0){
				wcFileSvc.copyFileList(request, copyFileList);
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
	
	
	/** 조회 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/viewSchdlPop","/wc/viewMySchdlPop"}, method = RequestMethod.GET)
	public String viewSchdlPop(HttpServletRequest request,
			@Parameter(name="schdlId", required=false) String schdlId,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		if("viewMySchdlPop".equals(path)){
			model.put("listPage", "listMySchdl");
		}else{
			model.put("listPage", "listNewSchdl");
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setSchdlId(schdlId);
		wcSchdlBVo.setUserUid(null);
		
		wcSchdlBVo =(WcSchdlBVo)commonDao.queryVo(wcSchdlBVo);
		// schdlPID로 count검색
		WcSchdlBVo scdPidVo = new WcSchdlBVo();		
		scdPidVo.setSchdlPid(wcSchdlBVo.getSchdlPid());
		model.put("scdPidCount", commonDao.count(scdPidVo));

		wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
		
		//버튼에대한 권한 설정
		String userAuth =null;
		
		if(wcSchdlBVo.getUserUid().equalsIgnoreCase(userVo.getUserUid())) userAuth = "pass";//등록Uid와 로그인Uid와 비교
		if(userAuth == null) userAuth = wcScdManagerSvc.getAgntTgtAuth(langTypCd, userVo.getUserUid(), wcSchdlBVo.getUserUid());
		
		model.put("wcSchdlBVo", wcSchdlBVo);
		
		//반복설정여부
		if(wcSchdlBVo.getRepetYn() != null && "Y".equals(wcSchdlBVo.getRepetYn()) && wcSchdlBVo.getSchdlPid() != null ){
			WcRepetSetupDVo wcRepetSetupDVo = new WcRepetSetupDVo();
			wcRepetSetupDVo.setSchdlId(wcSchdlBVo.getSchdlPid());
			wcRepetSetupDVo =(WcRepetSetupDVo)commonDao.queryVo(wcRepetSetupDVo);
			model.put("wcRepetSetupDVo", wcRepetSetupDVo);
		}
		
		model.put("userAuth", userAuth);
		
		//참석자 조회
		WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
		wcPromGuestDVo.setSchdlId(schdlId);
		@SuppressWarnings("unchecked")
		List<WcPromGuestDVo> wcPromGuestDVoList = (List<WcPromGuestDVo>)commonDao.queryList(wcPromGuestDVo);
		model.put("wcPromGuestDVoList", wcPromGuestDVoList);
		
		model.put("scds_schdlId", schdlId);

		return MoLayoutUtil.getJspPath("/wc/viewSchdlPop");
	}
	
	/** 일정 검색 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = "/wc/listMySchdl" )
	public String listMySchdl(HttpServletRequest request,
			@Parameter(name="schSchdlKndCd", required=false) String schSchdlKndCd,
			@Parameter(name="schSchdlTypCd", required=false) String schSchdlTypCd,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WcSchdlBVo wcsVo = new WcSchdlBVo();
		VoUtil.bind(request, wcsVo);
		wcsVo.setQueryLang(langTypCd);
		
		wcsVo.setUserUid(userVo.getUserUid());
		wcsVo.setDeptId(userVo.getDeptId());
		wcsVo.setCompId(userVo.getCompId());
		model.put("setPage", "setMySchdl");
		model.put("viewPage", "viewMySchdl");
		model.put("listPage", "listMySchdl");
		
		String whereSqllet = "";
		
		//일정대상
		String[] schdlKndCds = schSchdlKndCd == null || schSchdlKndCd.isEmpty() ? new String[]{} : schSchdlKndCd.split(",");
		if(schdlKndCds != null && schdlKndCds.length > 0 ){
			String schdlKndWhere = "";
			whereSqllet += "AND SCHDL_KND_CD IN(";
			for(String schdlKnd : schdlKndCds){
				schdlKndWhere += "".equals(schdlKndWhere) ? "'"+schdlKnd+"'" : ",'"+schdlKnd+"'";
			}
			whereSqllet += schdlKndWhere;
			whereSqllet += ")";
			model.put("schSchdlKndCds", schdlKndCds);
		}
		
		//일정종류
		String[] schdlTypCds = schSchdlTypCd == null || schSchdlTypCd.isEmpty() ? new String[]{} : schSchdlTypCd.split(",");
		if(schdlTypCds != null && schdlTypCds.length > 0 ){
			String schdlTypWhere = "";
			whereSqllet += " AND SCHDL_TYP_CD IN(";
			for(String schdlTyp : schdlTypCds){
				schdlTypWhere += "".equals(schdlTypWhere) ? "'"+schdlTyp+"'" : ",'"+schdlTyp+"'";
			}
			whereSqllet += schdlTypWhere;
			whereSqllet += ")";
			model.put("schSchdlTypCds", schdlTypCds);
		}
		
		if(!"".equals(whereSqllet)){
			wcsVo.setWhereSqllet(whereSqllet);
		}
		
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(wcsVo);
		PersonalUtil.setPaging(request, wcsVo, recodeCount);
		
		//목록 조회
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> wcSchdlBVoList = (List<WcSchdlBVo>)commonSvc.queryList(wcsVo);
	
		model.put("wcSchdlBVoList", wcSchdlBVoList);
		model.put("recodeCount", recodeCount);
		
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		if("listMySchdl".equals(path)) wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		// 일정대상 목록 조회
		setSchdlKndCdList(model , request);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return MoLayoutUtil.getJspPath("/wc/listMySchdl");		
	}
	
	/** [AJAX] 날짜이동 달력 휴일조회 */
	@RequestMapping(value = "/cm/transGetCalendarHolidayAjax")
	public String transGetCalendarHolidayAjax(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String year = (String) object.get("year");
			String month = (String) object.get("month");
			String holyYn = (String) object.get("holyYn");//휴일여부
			if (year == null || month == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
		
			
			Calendar Cal = Calendar.getInstance();
			Cal.set(Calendar.YEAR, Integer.parseInt(year));
			Cal.set(Calendar.MONTH, Integer.parseInt(month)-1); 
			Cal.set(Calendar.DATE, 1);
			int LastDay = Cal.getActualMaximum(Calendar.DATE);//전월의 일수
			
	    	WcSchdlBVo searchVo = new WcSchdlBVo();
	    	// 시작 일자와 종료일자 세팅
	    	searchVo.setSchdlStartDt(getDateOfDay(year+"-"+month+"-"+"01", "month", "s", null, 1));
	    	searchVo.setSchdlEndDt(getDateOfDay(year+"-"+month+"-"+LastDay, "month", "p", null, 1));

	    	searchVo.setSchdlTypCd("5");//기념일
	    	searchVo.setInstanceQueryId("selectWcSchdlBPlt");
	    	
	    	// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
	    	// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			//if(!isSysAdmin){
				searchVo.setCompId(userVo.getCompId());
			//}
	    					
	    	//공통 기념일 조회
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> wcsList = (List<WcSchdlBVo>)commonDao.queryList(searchVo);
			
			String strHolidays = "";
			//음력일정일 경우 일자와의 비교를 위해 양력일정으로 변경한다.
	    	String strtYmd , endYmd;
	    	
	    	// 국가코드 조회
	    	String natCd = wcScdManagerSvc.getNatCd(userVo);
	    	// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
	    	WcAnnvDVo wcAnnvDVo = null;
			for(WcSchdlBVo wcSchdlBVo : wcsList){
				if(holyYn != null && !holyYn.isEmpty() && !wcSchdlBVo.getHoliYn().equals(holyYn) ) continue;
				// 기념일상세 조회
				wcAnnvDVo = wcScdManagerSvc.getWcAnnvDVo(langTypCd, userVo.getCompId(), wcSchdlBVo.getSchdlPid());
				if(!wcScdManagerSvc.isSchdlNatChk(wcAnnvDVo, natCd)) continue;
				strtYmd = wcSchdlBVo.getSchdlStartDt().replaceAll("[-: ]", "").substring(0,8);
				endYmd = wcSchdlBVo.getSchdlEndDt().replaceAll("[-: ]", "").substring(0,8);
				strtYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? lunalCalenderUtil.toSolar(strtYmd) : strtYmd;
				endYmd = wcSchdlBVo.getSolaLunaYn() != null && "N".equals(wcSchdlBVo.getSolaLunaYn()) ? lunalCalenderUtil.toSolar(endYmd) : endYmd;

				strtYmd = strtYmd.replaceAll("[-: ]", "").substring(0,8);
				endYmd = endYmd.replaceAll("[-: ]", "").substring(0,8);
				strHolidays += strtYmd + ","; 
				
				int intStrt = Integer.parseInt(strtYmd);
				while(intStrt < Integer.parseInt(endYmd))
				{
					String pYear = (intStrt+"").substring(0,4);
					String pMonth = (intStrt+"").substring(4,6);
					String pDay = (intStrt+"").substring(6,8);
					String pDate = getDateOfDay(pYear+"-"+pMonth+"-"+pDay, "day", "p", null, 1).replaceAll("[-: ]", "").substring(0,8);
					strHolidays += pDate + ","; 
					intStrt = Integer.parseInt(pDate);
				}
				
				strHolidays += endYmd + ":"; 
			}

			model.put("message", strHolidays);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	//이전 다음 일정 세팅
    public String getDateOfDay(  String startDay , String tabType , String schedulePmValue , String clicknps , int stepValue){
 		if(startDay != null && ( schedulePmValue == null || "".equals(schedulePmValue) )){
 			return startDay;
 		}
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		GregorianCalendar cal = new GregorianCalendar ( );
 		String daysValue = "";
 		if(startDay != null && !"".equals(startDay)){
 			cal.set(Integer.parseInt(startDay.split("-")[0]) , Integer.parseInt(startDay.split("-")[1])-1 , Integer.parseInt(startDay.split("-")[2]));
 	 		
 	 		if("month".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.MONTH, 1);
 	 			else
 	 				cal.add(Calendar.MONTH, -1);
 	 		}else if("week".equals(tabType) || "weeks".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 7);
 	 			else
 	 				cal.add(Calendar.DATE, -7);
 	 		}else if("day".equals(tabType) ){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 1);
 	 			else
 	 				cal.add(Calendar.DATE, -1);
 	 		}else if( "year".equals(tabType) ){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.YEAR, 1);
 	 			else
 	 				cal.add(Calendar.YEAR, -1);
 	 		}else if("allSchedule".equals(tabType)){
 	 			if("".equals(clicknps)){
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, 1);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -1);
 	 			}else{
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, stepValue);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -stepValue);
 	 			}
 	 		}
 	 		Date days = cal.getTime();
 	 		daysValue = sdf.format(days);
 		}else{
 			daysValue = sdf.format((Date)(cal.getTime()));
 		}
 		return daysValue;
 	}
    
    /** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/wc/findBc")
	public String findBc(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/wc/findBc", "empty");
	}
	
    /** [AJAX] 개인,공개 명함 목록 조회 */
	@RequestMapping(value = "/wc/findBcAjx")
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
				return MoLayoutUtil.getJspPath("/wc/findBcAjx");
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
	@RequestMapping(value = "/wc/transSchdlDelAjx")
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
	
	/** [AJAX] 전체삭제
	 * @throws Exception */
	@RequestMapping(value = "/wc/transSchdlAllDelAjx")
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
	
	/** 참석자 빈시간 확인
	 * @throws Exception */
	@RequestMapping(value = "/wc/viewEmptyTimeGuest")
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
		
		return MoLayoutUtil.getJspPath("/wc/viewEmptyTimeGuest","empty");
	}
	
	/** 반복 상세설정 팝업 */
	@RequestMapping(value = "/wc/repetSelectSub")
	public String repetSelectPop(HttpServletRequest request,
			@Parameter(name="repetKnd", required=false) String repetKnd,
			Locale locale,
			ModelMap model) throws Exception {
		
		model.put("UI_TITLE", messageProperties.getMessage("wc.btn.repetSetup", locale));//반복설정
		return MoLayoutUtil.getJspPath("/wc/repetSelectSub", "empty");
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wc/downFile","/wc/preview/downFile"})
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
	
	/** [Ajax] 국가목록 조회 */
	@RequestMapping(value = {"/wc/getNatListAjx"})
	public String getNatListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
					
			WcNatBVo wcNatBVo = new WcNatBVo();
			wcNatBVo.setCompId(userVo.getCompId());
			wcNatBVo.setQueryLang(langTypCd);
			
			@SuppressWarnings("unchecked")
			List<WcNatBVo> wcNatBVoList = (List<WcNatBVo>)commonSvc.queryList(wcNatBVo);
			model.put("wcNatBVoList", wcNatBVoList);
			
			// 개인설정 또는 일정
			model.put("chkNatCd", wcScdManagerSvc.getNatCd(userVo));
			
			return JsonUtil.returnJson(model);
			
		} catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** [AJAX] 국가설정 저장 */
	@RequestMapping(value = "/wc/transNatAjx")
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String natCd = (String)object.get("natCd"); // 회사ID
			if (natCd == null || natCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				String msg = messageProperties.getMessage("pt.msg.nodata.passed", request);
				LOGGER.error("natCd == null || natCd.isEmpty()  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String compId = userVo.getCompId();
			
			QueryQueue queryQueue = new QueryQueue();
			// 국가코드 유무 체크
			WcNatBVo wcNatBVo = new WcNatBVo();
			wcNatBVo.setCompId(compId);
			wcNatBVo.setCd(natCd);
			wcNatBVo = (WcNatBVo)commonSvc.queryVo(wcNatBVo);
			if(wcNatBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				String msg = messageProperties.getMessage("pt.msg.nodata.passed", request);
				LOGGER.error("wcNatBVo==null  msg:"+msg);
				model.put("message", msg);
				return LayoutUtil.getResultJsp();
			}
			// 리소스ID 세팅
			String rescId = wcNatBVo.getRescId();
			wcNatBVo = new WcNatBVo();
			wcNatBVo.setCompId(compId);
			wcNatBVo.setCd(natCd);
			wcNatBVo.setRescId(rescId);
			wcNatBVo.setUserUid(userVo.getUserUid());
			// 저장
			queryQueue.store(wcNatBVo);
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [SUB] 사용자그룹 목록 조회 */
	@RequestMapping(value = "/wc/findUserGrp")
	public String findUserGrp(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/wc/findUserGrp", "empty");
	}
	
	/** [AJAX] 사용자그룹 조회 */
	@RequestMapping(value = "/wc/findUserGrpAjx")
	public String findUserGrpAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String userUid = (String)jsonObject.get("paramUserUid");
			String schWord = (String)jsonObject.get("schWord");
			String pagingYn = (String)jsonObject.get("pagingYn");
			
			// 관리자 여부
			boolean isAdmin = request.getRequestURI().startsWith("/wc/adm/");
			if(!isAdmin && userUid!=null && !userUid.isEmpty() && !wcScdManagerSvc.chkSchdlUser(userUid, userVo.getUserUid(), "agnt")){ // 대리인 여부 확인
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(userUid==null || userUid.isEmpty()) userUid = userVo.getUserUid();
			
			// 사용자그룹 기본 조회
			WcUserGrpBVo wcUserGrpBVo = new WcUserGrpBVo();
			wcUserGrpBVo.setQueryLang(langTypCd);
			wcUserGrpBVo.setCompId(userVo.getCompId());
			wcUserGrpBVo.setModrUid(userUid);
			wcUserGrpBVo.setOrderBy("SORT_ORDR");
			// 조회조건
			if(schWord!=null && !schWord.isEmpty()) wcUserGrpBVo.setSchWord(schWord);
			// 조회
			if(pagingYn != null && "Y".equals(pagingYn)){
				Integer recodeCount = commonSvc.count(wcUserGrpBVo);
				PersonalUtil.setPaging(request, wcUserGrpBVo, recodeCount);
				model.put("recodeCount", recodeCount);
			}
			@SuppressWarnings("unchecked")
			List<WcUserGrpBVo> wcUserGrpBVoList = (List<WcUserGrpBVo>)commonSvc.queryList(wcUserGrpBVo);
			model.put("wcUserGrpBVoList", wcUserGrpBVoList);
			
			return MoLayoutUtil.getJspPath("/wc/findUserGrpAjx");
		}catch (Exception e) {
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** [AJAX] 사용자그룹 상세(사용자) 목록 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wc/listUserGrpDtlListAjx")
	public String listUserGrpDtlListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		// set, list 로 시작하는 경우 처리
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String userGrpId = (String)jsonObject.get("userGrpId");
			if(userGrpId==null || userGrpId.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String userUid = (String)jsonObject.get("paramUserUid");
			
			// 관리자 여부
			boolean isAdmin = request.getRequestURI().startsWith("/wc/adm/");
			if(!isAdmin && userUid!=null && !userUid.isEmpty() && !wcScdManagerSvc.chkSchdlUser(userUid, userVo.getUserUid(), "agnt")){ // 대리인 여부 확인
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(userUid==null || userUid.isEmpty()) userUid = userVo.getUserUid();
			
			// 사용자그룹 목록
			WcUserGrpLVo wcUserGrpLVo = new WcUserGrpLVo();
			wcUserGrpLVo.setQueryLang(langTypCd);			
			wcUserGrpLVo.setOrderBy("SORT_ORDR");
			
			String[] userGrpIds = userGrpId.split(",") ;
			List<WcUserGrpLVo> wcUserGrpLVoList = new ArrayList<WcUserGrpLVo>();
			for(String id : userGrpIds){
				wcUserGrpLVo.setUserGrpId(id);
				wcUserGrpLVoList.addAll((List<WcUserGrpLVo>)commonSvc.queryList(wcUserGrpLVo));
			}
			List<Map<String, Object>> userInfoList = new ArrayList<Map<String, Object>>();
			Map<String, Object> userMap;
			// 사용자 정보를 조회한다
			for(WcUserGrpLVo storedWcUserGrpLVo : wcUserGrpLVoList){
				if(storedWcUserGrpLVo==null || storedWcUserGrpLVo.getUserUid() == null || storedWcUserGrpLVo.getUserUid().isEmpty()) continue;
				userMap=orCmSvc.getUserMap(storedWcUserGrpLVo.getUserUid(), langTypCd);
				if(userMap!=null) userInfoList.add(orCmSvc.getUserMap(storedWcUserGrpLVo.getUserUid(), langTypCd));
			}
			if(userInfoList!=null && userInfoList.size()>0) model.put("userInfoList", JsonUtil.toJson(userInfoList));
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [POPUP] 날짜 조회 - 공휴일제외(결재ERP) */
	@RequestMapping(value = {"/cm/date/getSrchDateListPop"})
	public String getSrchDateListPop(HttpServletRequest request,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end,
			@RequestParam(value = "natCd", required = false) String natCd,
			@RequestParam(value = "holiYn", required = false) String holiYn,
			ModelMap model) throws Exception {
		
		try {
			if(start==null || start.isEmpty()) // 시작일자가 없으면 현재 날짜 삽입
				start=commonDao.querySysdate(null).substring(0,10);
			
			if(end==null || end.isEmpty()) // 종료일자가 없으면 현재 날짜 삽입
				end=commonDao.querySysdate(null).substring(0,10);
			
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if(natCd==null || natCd.isEmpty()){
				natCd = wcScdManagerSvc.getNatCd(userVo);
			}
			
			// 제외할 날짜 목록
			List<String> excludeList = null;
			
			if(holiYn!=null && !holiYn.isEmpty() && "N".equals(holiYn)){
				excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), langTypCd, natCd, realStrtDt, end, null);
				model.put("excludeList", excludeList);
			}
			
			realStrtDt=start.replaceAll("[-: ]", "");
			end=end.replaceAll("[-: ]", "");
			List<String[]> fromToList = wcScdManagerSvc.getFromToDates(realStrtDt, end, excludeList, true, null);
			model.put("fromToList", fromToList);

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return MoLayoutUtil.getJspPath("/wc/cm/getSrchDateListPop");
	}
	
	/** [메일] - 참석자 일정 등록 */
	@RequestMapping(value = "/cm/wc/transGuestSchdl")
	public String transGuestSchdl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "acptId", required = false) String acptId,
			@RequestParam(value = "acptYn", required = false) String acptYn,
			@RequestParam(value = "mailId", required = false) String mailId,
			ModelMap model) throws Exception{
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			if(userVo==null){
				LOGGER.info("[ERROR] - userVo is null");
				// pt.logout.timeout=로그인 세션이 종료 되었습니다.
				throw new CmException("pt.logout.timeout", request);
			}
			
			// 환경설정
			Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			
			// 설정에서 참석자 수락여부가 'N' 인 경우 메세지 리턴
			boolean acceptYn=envConfigMap.containsKey("acceptYn") && "Y".equals(envConfigMap.get("acceptYn"));
			if(!acceptYn){
				LOGGER.info("[ERROR] - Schedule Setup[Guest Accept] is 'N'");
				// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException("cm.msg.notValidCall", request);
			}
			
			if(acptId==null || acptId.isEmpty() || acptYn==null || acptYn.isEmpty() || mailId==null || mailId.isEmpty()){
				LOGGER.info("[ERROR] - acptId==null || acptId.isEmpty() || acptYn==null || acptYn.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 일정ID 복호화
			String schdlId = cryptoSvc.decryptPersanal(acptId);
			// 메일ID 복호화
			String descriptMailId = cryptoSvc.decryptPersanal(mailId);
			
			WcMailSchdlRVo wcMailSchdlRVo = new WcMailSchdlRVo();
			wcMailSchdlRVo.setSchdlId(schdlId);
			wcMailSchdlRVo.setUserUid(userVo.getUserUid());
			wcMailSchdlRVo.setMailId(descriptMailId); // 메일ID
			if(commonSvc.count(wcMailSchdlRVo)==0){
				LOGGER.info("[ERROR] - setMailId is wcMailSchdlRVo==null");
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
				//model.put("todo", "window.close();");
			}
			wcMailSchdlRVo.setMailId(null);
			wcMailSchdlRVo = (WcMailSchdlRVo)commonSvc.queryVo(wcMailSchdlRVo);
			if(wcMailSchdlRVo==null){
				LOGGER.info("[ERROR] - wcMailSchdlRVo==null");
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
				//model.put("todo", "window.close();");
			}
			if(wcMailSchdlRVo.getCmplDt()!=null){
				LOGGER.info("[ERROR] - Schedule Duplication : AcptYn['"+wcMailSchdlRVo.getAcptYn()+"']");
				// wc.dup.schdl=이미 등록되었습니다.
				throw new CmException("wc.dup.schdl", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 삭제할 파일 경로[기존 일정]
			List<CommonFileVo> deletedFileList=new ArrayList<CommonFileVo>();
			
			// 이미 복사한 일정ID가 있을경우 기존거 삭제
			wcScdManagerSvc.deleteSchdl(queryQueue, wcMailSchdlRVo.getCopySchdlId(), deletedFileList);
						
			// 파일복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
			
			// 복사일정ID
			String copySchdlId = null;
						
			if("Y".equals(acptYn)) // 일정 복사
				copySchdlId=wcScdManagerSvc.copySchdl(request, queryQueue, userVo, schdlId, copyFileList);
			
			// 메일일정 수정
			wcScdManagerSvc.setMailSchdlVoList(queryQueue, null, schdlId, copySchdlId, userVo.getUserUid(), null, acptYn);
			
			commonSvc.execute(queryQueue);
			
			// 파일 제거
			if(deletedFileList.size()>0)
				wcFileSvc.deleteDiskFiles(deletedFileList);
						
			// 파일 복사
			if(copyFileList.size()>0){
				wcFileSvc.copyFileList(request, copyFileList);
			}
						
			// cm.msg.save.success=저장 되었습니다.	
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
		} catch (CmException e) {
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		model.put("todo", "window.open('about:blank', '_self').close();");
		
		//공통 처리 페이지
		return "/wc/result/customResult";
	}
	
}
