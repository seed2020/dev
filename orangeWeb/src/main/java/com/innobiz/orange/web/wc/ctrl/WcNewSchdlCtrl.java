package com.innobiz.orange.web.wc.ctrl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.Browser;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;
import com.innobiz.orange.web.wc.calender.WcScdCalMonth;
import com.innobiz.orange.web.wc.calender.WcScdCalWeek;
import com.innobiz.orange.web.wc.svc.WcAdmSvc;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcFileSvc;
import com.innobiz.orange.web.wc.svc.WcMailSvc;
import com.innobiz.orange.web.wc.svc.WcRescSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.util.LunalCalenderUtil;
import com.innobiz.orange.web.wc.utils.WcConstant;
import com.innobiz.orange.web.wc.vo.WcAgntRVo;
import com.innobiz.orange.web.wc.vo.WcAnnvDVo;
import com.innobiz.orange.web.wc.vo.WcBumkDVo;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcMailSchdlRVo;
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
public class WcNewSchdlCtrl {
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;

	/** 일정 공통 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 명함 공통 서비스 */
//	@Autowired
//	private WbBcSvc wbBcSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcNewSchdlCtrl.class);
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	@Autowired
	private LunalCalenderUtil lunalCalenderUtil;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 리소스 서비스 */
	@Autowired
	private WcRescSvc wcRescSvc;
	
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
	@RequestMapping(value = "/wc/listNewSchdlTmp")
	public String listNewSchdlTmp(HttpServletRequest request,			
			@RequestParam(value="action",required=false)
			String action,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		// set, list 로 시작하는 경우 처리
		checkPath(request, "listPsnSchdl", model);
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
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/listNewSchdl");
	}
	
	/** 일정 종류 조회 */
	public void setSchdlKndCdList(ModelMap model , HttpServletRequest request){
		//{schdlKndCd,이름,등록수정시적용여부}
		model.put("schdlKndCdList", wcCmSvc.getSchdlKndList(request));
	}
	
	/** 등록 팝업 오픈
	 * @throws Exception */
	@SuppressWarnings("unused")
	@RequestMapping(value = {"/wc/setSchdlPop","/wc/setMySchdlPop","/wc/adm/setAllSchdlPop"})
	public String setSchdlPop(HttpServletRequest request,
			@RequestParam(value="schdlId",required=false) String schdlId,
			@RequestParam(value="schdlStartDt",required=false) String schdlStartDt,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		boolean schAdmin = false;
		if("setMySchdlPop".equals(path)){
			model.put("listPage", "listMySchdl");
		}else if("setAllSchdlPop".equals(path)){
			schAdmin = true;
			model.put("listPage", "listAllSchdl");
		}else{
			model.put("listPage", "listNewSchdl");
		}
		
		//관리자 (사용자 권한 체크)
		if("setAllSchdlPop".equals(path) ) this.checkUserAuth(request, "A", null);
				
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
			/*if(schAdmin){
				wcSchdlBVo.setUserUid(null);
			}else{
				wcSchdlBVo.setUserUid(userVo.getUserUid());
				wcSchdlBVo.setDeptId(userVo.getDeptId());
				wcSchdlBVo.setCompId(userVo.getCompId());
			}*/
			wcSchdlBVo.setWithLob(true);
			wcSchdlBVo = (WcSchdlBVo)commonDao.queryVo(wcSchdlBVo);
			
			// schdlPID로 count검색
			WcSchdlBVo scdPidVo = new WcSchdlBVo();		
			scdPidVo.setSchdlPid(wcSchdlBVo.getSchdlPid());
			//scdPidVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			scdPidVo.setRepetYn("Y");// 반복여부
			model.put("scdPidCount", commonDao.count(scdPidVo));
			
			WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
			wcPromGuestDVo.setSchdlId(schdlId);
			wcPromGuestDVoList = wcScdManagerSvc.getPromGuestLst(wcPromGuestDVo);
			
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			wcFileSvc.putFileListToModel(schdlId, model, userVo.getCompId());
			
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
            
            // 종일일정
            String alldayYn = ParamUtil.getRequestParam(request, "alldayYn", false);
            if(alldayYn!=null && !alldayYn.isEmpty()) wcSchdlBVo.setAlldayYn(alldayYn);
            
		}
		
		model.put("wcSchdlBVo", wcSchdlBVo);
		
		// UI 구성용 - 빈 VO 하나 더함
		if(wcPromGuestDVoList == null) wcPromGuestDVoList = new ArrayList<WcPromGuestDVo>();
		wcPromGuestDVoList.add(new WcPromGuestDVo());
		model.put("wcPromGuestDVoList", wcPromGuestDVoList);
				
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		if(!"setAllSchdlPop".equals(path)) wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
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
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("fncCal", request.getParameter("fncCal"));
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","schdlId","schdlStartDt"));
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		// 타사 조직도 조회
		if("Y".equals(sysPlocMap.get("globalOrgChartEnable"))){
			request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/wc/setSchdlPop");
	}
	
	/** 등록 수정 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/transSchdl","/wc/adm/transSchdl"})
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
			
			String schdlStrtTime=null, schdlEndTime=null;
			
			if("Y".equals(wcsVo.getAlldayYn()) || wcsVo.getSchdlStrtHour() == null || wcsVo.getSchdlStrtHour().isEmpty() || wcsVo.getSchdlStrtMinute() == null || wcsVo.getSchdlStrtMinute().isEmpty()) schdlStrtTime="00:00";
			if("Y".equals(wcsVo.getAlldayYn()) || wcsVo.getSchdlEndHour() == null || wcsVo.getSchdlEndHour().isEmpty() || wcsVo.getSchdlEndMinute() == null || wcsVo.getSchdlEndMinute().isEmpty()) schdlEndTime="00:00";
			
			if(schdlStrtTime==null){
				schdlStrtTime=wcsVo.getSchdlStrtHour()+":"+wcsVo.getSchdlStrtMinute();
			}
			if(schdlEndTime==null){
				schdlEndTime=wcsVo.getSchdlEndHour()+":"+wcsVo.getSchdlEndMinute();
			}
			
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
			
			// set, list 로 시작하는 경우 처리
			checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadCalendar('" + listPage + "');");
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
	
	/** 수정save [FORM SUBMIT] 
	 * @throws Exception */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(value = {"/wc/transSchdlMod","/wc/adm/transSchdlMod"})
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
			
			String schdlStrtTime=null, schdlEndTime=null;
			
			if("Y".equals(wcsVo.getAlldayYn()) || wcsVo.getSchdlStrtHour() == null || wcsVo.getSchdlStrtHour().isEmpty() || wcsVo.getSchdlStrtMinute() == null || wcsVo.getSchdlStrtMinute().isEmpty()) schdlStrtTime="00:00";
			if("Y".equals(wcsVo.getAlldayYn()) || wcsVo.getSchdlEndHour() == null || wcsVo.getSchdlEndHour().isEmpty() || wcsVo.getSchdlEndMinute() == null || wcsVo.getSchdlEndMinute().isEmpty()) schdlEndTime="00:00";
			
			if(schdlStrtTime==null){
				schdlStrtTime=wcsVo.getSchdlStrtHour()+":"+wcsVo.getSchdlStrtMinute();
			}
			if(schdlEndTime==null){
				schdlEndTime=wcsVo.getSchdlEndHour()+":"+wcsVo.getSchdlEndMinute();
			}
			
			//시작 일 +시 간
			wcsVo.setSchdlStartDt(wcsVo.getSchdlStrtYmd() + " " + schdlStrtTime);
			//종료 일 +시간
			wcsVo.setSchdlEndDt(wcsVo.getSchdlEndYmd() + " " + schdlEndTime);

			//=================================================================
			//WcPromGuestDVo 참석자
			//=================================================================

			// 파일 삭제 목록
			//List<CommonFileVo> deletedFileList = null; 
								
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
				//deletedFileList = wcFileSvc.saveScdlFile(request, wcsVo.getSchdlId() , queryQueue, fileSetting);
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
						
			checkPath(request, "", model);
			// cm.msg.save.success=저장 되었습니다.

			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadCalendar('" + listPage + "');");
			
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
		return LayoutUtil.getResultJsp();

	}
	
	public void setPromGuest(String[] promGuestUid,String[] promGuestRescNm,String[] promGuestPositNm,
			 String[] promGuestDeptRescNm, String[] promGuestEmail, 
			 String[]  promFriGuestUid, String[] promFriGuestNm, String[] promFriGuesCompNm,
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
			wcGuest.setGuestDeptNm(promFriGuesCompNm[i]);
			wcGuest.setEmail(null);
			wcGuest.setGuestEmplYn("N");
			//상태코드 T(참석미정) : C참석, N불참, D약속취소, M주참석자, A 추가참석자 
			wcGuest.setStatCd("T");
			queryQueue.insert(wcGuest);
			//			guestsList.add(wcGuest);
		
		}
	}
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
	
	/** 조회 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/viewSchdlPop","/wc/viewMySchdlPop","/wc/adm/viewAllSchdlPop"}, method = RequestMethod.GET)
	public String viewSchdlPop(HttpServletRequest request,
			@Parameter(name="schdlId", required=false) String schdlId,
			ModelMap model) throws Exception{
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		boolean schAdmin = false;
		if("viewMySchdlPop".equals(path)){
			model.put("listPage", "listMySchdl");
		}else if("viewAllSchdlPop".equals(path)){
			schAdmin = true;
			model.put("listPage", "listAllSchdl");
		}else{
			model.put("listPage", "listNewSchdl");
		}
		
		model.put("plt", request.getParameter("plt"));
		
		//관리자 (사용자 권한 체크)
		if("viewAllSchdlPop".equals(path) ) this.checkUserAuth(request, "A", null);
		
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
		//scdPidVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
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
		   
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 환경설정
		Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		
		// 설정에서 참석자 수락여부가 'Y' 인 경우에만 수락여부 메일에 포함
		boolean acceptYn=envConfigMap.containsKey("acceptYn") && "Y".equals(envConfigMap.get("acceptYn"));
					
		//이메일 발송
		if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) && acceptYn && wcPromGuestDVoList.size()>0){
			WcMailSchdlRVo wcMailSchdlRVo = new WcMailSchdlRVo();
			wcMailSchdlRVo.setQueryLang(langTypCd);
			wcMailSchdlRVo.setSchdlId(wcSchdlBVo.getSchdlPid());
			if(!schAdmin) wcMailSchdlRVo.setRegrUid(userVo.getUserUid());
			if(commonSvc.count(wcMailSchdlRVo)>0){
				Map<String, WcMailSchdlRVo> mailAcceptMap = new HashMap<String, WcMailSchdlRVo>();
				@SuppressWarnings("unchecked")
				List<WcMailSchdlRVo> wcMailSchdlRVoList = (List<WcMailSchdlRVo>) commonSvc.queryList(wcMailSchdlRVo);
				for(WcMailSchdlRVo storedWcMailSchdlRVo : wcMailSchdlRVoList){
					mailAcceptMap.put(storedWcMailSchdlRVo.getUserUid(), storedWcMailSchdlRVo);
				}
				model.put("mailAcceptMap", mailAcceptMap);
			}
		}
		model.put("scds_schdlId", schdlId);
		
		//그룹 목록
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcPromGuestDVo.setQueryLang(langTypCd);
		wcSchdlGrpBVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpBVo.setFncMy("Y");

		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
		model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","schdlId","paramUserUid"));
		
		return LayoutUtil.getJspPath("/wc/viewSchdlPop");
	}      
	
	
	
	/** [AJAX] 즐겨찾기 조회 */
	@RequestMapping(value = "/wc/selectBumkAjx")
	public String selectBumkAjx(HttpServletRequest request,
			HttpServletResponse response,
			@Parameter(name="schdlKndCd", required=true) String schdlKndCd,
			ModelMap model) throws Exception {
		
		if (schdlKndCd == null || schdlKndCd.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		UserVo userVo = LoginSession.getUser(request);
		
		List<WcBumkDVo> bumkList = null;
		if(Integer.parseInt(schdlKndCd) == 1 ) bumkList = wcScdManagerSvc.getBumkPsn(userVo.getUserUid());
		else bumkList = wcScdManagerSvc.getBumkDept(userVo.getUserUid());
		model.put("list", bumkList);
        
        return JsonUtil.returnJson(model);
	}
	
	
	
	/** 일정 검색 [FORM SUBMIT] 
	 * @throws Exception */
	@RequestMapping(value = {"/wc/listMySchdl","/wc/adm/listAllSchdl"} )
	public String listSchdl(HttpServletRequest request,
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
		
		//나의일정검색 or 관리자일정검색
		if("listAllSchdl".equals(path)){
			wcsVo.setUserUid(null);
			wcsVo.setDeptId(null);
			wcsVo.setCompId(null);
			
			// 시스템 관리자 여부
			boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
			if(!isSysAdmin){
				wcsVo.setCompId(userVo.getCompId());
			}
			
			model.put("setPage", "setAllSchdlPop");
			model.put("viewPage", "viewAllSchdlPop");
			model.put("listPage", "listAllSchdl");
		}else{
			wcsVo.setUserUid(userVo.getUserUid());
			wcsVo.setDeptId(userVo.getDeptId());
			wcsVo.setCompId(userVo.getCompId());
			model.put("setPage", "setMySchdlPop");
			model.put("viewPage", "viewMySchdlPop");
			model.put("listPage", "listMySchdl");
		}
		
		String whereSqllet = "";
		
		//일정대상
		String[] schdlKndCds = schSchdlKndCd == null ? new String[]{} : schSchdlKndCd.split(",");
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
		String[] schdlTypCds = schSchdlTypCd == null ? new String[]{} : schSchdlTypCd.split(",");
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
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		model.addAttribute("JS_OPTS", new String[]{"editor"});
				
		return LayoutUtil.getJspPath("/wc/listSchSchdl");
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	/** 일정보내기 > 기존
	 * @throws Exception */
	@RequestMapping(value = {"/wc/setSchdlSend","/wc/adm/setAllSchdlSend"})
	public String setSchdlSend(HttpServletRequest request,			
			ModelMap model) throws Exception {
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		if("setSchdlSend".equals(path)) wcCatClsBVo.setUseYn("Y");//사용중인 카테고리만 조회
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		Calendar cal = Calendar.getInstance();
		model.put("startDay", new Timestamp(cal.getTimeInMillis()).toString());
		return LayoutUtil.getJspPath("/wc/setSchdlSend");
	}
	
	/** [AJAX] 일정 보내기 > 이메일발송 [기존 - 일일,주간,월간 메세지]*/
	@RequestMapping(value = "/wc/transEmailAjxTemp" , method = RequestMethod.POST)
	public String transEmailAjxTemp(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String startDay  = (String) object.get("startDay");
			String listType  = (String) object.get("listType");
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			wcsVo.setQueryLang(langTypCd);
			
			//일정의 시작과 종료일을 세팅한다.
			wcCmSvc.setSchdlDt(startDay, wcsVo, listType);
			
			String whereSqllet = "";
			
			String schSchdlKndCd  = (String) object.get("schSchdlKndCd");
			//일정대상
			String[] schdlKndCds = schSchdlKndCd == null || "".equals(schSchdlKndCd) ? new String[]{} : schSchdlKndCd.split(",");
			if(schdlKndCds != null && schdlKndCds.length > 0 ){
				String schdlKndWhere = "";
				whereSqllet += "AND SCHDL_KND_CD IN(";
				for(String schdlKnd : schdlKndCds){
					schdlKndWhere += "".equals(schdlKndWhere) ? "'"+schdlKnd+"'" : ",'"+schdlKnd+"'";
				}
				whereSqllet += schdlKndWhere;
				whereSqllet += ")";
			}
			
			String schSchdlTypCd  = (String) object.get("schSchdlTypCd");
			//일정종류
			String[] schdlTypCds = schSchdlTypCd == null || "".equals(schSchdlTypCd) ? new String[]{} : schSchdlTypCd.split(",");
			if(schdlTypCds != null && schdlTypCds.length > 0 ){
				String schdlTypWhere = "";
				whereSqllet += " AND SCHDL_TYP_CD IN(";
				for(String schdlTyp : schdlTypCds){
					schdlTypWhere += "".equals(schdlTypWhere) ? "'"+schdlTyp+"'" : ",'"+schdlTyp+"'";
				}
				whereSqllet += schdlTypWhere;
				whereSqllet += ")";
			}
			
			if(!"".equals(whereSqllet)){
				wcsVo.setWhereSqllet(whereSqllet);
			}
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> wcsList = (List<WcSchdlBVo>)commonSvc.queryList(wcsVo);
			if(wcsList.size() == 0){
				// 해당일에 대한 일정이 없습니다.
				throw new CmException("wc.jsp.setProm.sendSchdlError", request);
			}
			
			//이메일 발송 정보 조회
			CmEmailBVo cmEmailBVo = wcCmSvc.getEmailInfoTemp(request, listType, wcsVo, wcsList);
			JSONArray recvIds = null;
			
			QueryQueue queryQueue = new QueryQueue();
						
			//이메일 정보 저장 - 반복설정과 여러개의 스케쥴의 파일이 많아서 안보내기로함.
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , recvIds , null , queryQueue , userVo);
			
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
	
	
	/** [AJAX] 일정 보내기 > 이메일발송 */
	@RequestMapping(value = {"/wc/transEmailAjx","/wc/adm/transEmailAjx"})
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			//요청 페이지(관리자 메뉴 구분을 위함)
			String path = request.getRequestURI();
			if(path.indexOf("adm") > -1 ){
				path = "transAllEmailAjx";
			}else{
				path = path.substring(path.lastIndexOf("/")+1);
				path = path.substring(0,path.lastIndexOf("."));
			}
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String schdlStartDt  = (String) object.get("schdlStartDt");//시작일자
			String schdlEndDt  = (String) object.get("schdlEndDt");//종료일자
			String schWord  = (String) object.get("schWord");//제목
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회조건 매핑
			WcSchdlBVo wcsVo = new WcSchdlBVo();
			VoUtil.bind(request, wcsVo);
			wcsVo.setQueryLang(langTypCd);
			if(schdlStartDt != null && !"".equals(schdlStartDt)) wcsVo.setSchdlStartDt(schdlStartDt);
			if(schdlEndDt != null && !"".equals(schdlEndDt)) wcsVo.setSchdlEndDt(schdlEndDt);
			if(schWord != null && !"".equals(schWord)) wcsVo.setSchWord(schWord);
			
			//나의일정검색 or 관리자일정검색
			if("transAllEmailAjx".equals(path)){
				wcsVo.setUserUid(null);
			}else{
				wcsVo.setUserUid(userVo.getUserUid());
				wcsVo.setDeptId(userVo.getDeptId());
				wcsVo.setCompId(userVo.getCompId());
			}
			
			String whereSqllet = "";
			
			String schSchdlKndCd  = (String) object.get("schSchdlKndCd");
			//일정대상
			String[] schdlKndCds = schSchdlKndCd == null || "".equals(schSchdlKndCd) ? new String[]{} : schSchdlKndCd.split(",");
			if(schdlKndCds != null && schdlKndCds.length > 0 ){
				String schdlKndWhere = "";
				whereSqllet += "AND SCHDL_KND_CD IN(";
				for(String schdlKnd : schdlKndCds){
					schdlKndWhere += "".equals(schdlKndWhere) ? "'"+schdlKnd+"'" : ",'"+schdlKnd+"'";
				}
				whereSqllet += schdlKndWhere;
				whereSqllet += ")";
			}
			
			String schSchdlTypCd  = (String) object.get("schSchdlTypCd");
			//일정종류
			String[] schdlTypCds = schSchdlTypCd == null || "".equals(schSchdlTypCd) ? new String[]{} : schSchdlTypCd.split(",");
			if(schdlTypCds != null && schdlTypCds.length > 0 ){
				String schdlTypWhere = "";
				whereSqllet += " AND SCHDL_TYP_CD IN(";
				for(String schdlTyp : schdlTypCds){
					schdlTypWhere += "".equals(schdlTypWhere) ? "'"+schdlTyp+"'" : ",'"+schdlTyp+"'";
				}
				whereSqllet += schdlTypWhere;
				whereSqllet += ")";
			}
			
			if(!"".equals(whereSqllet)){
				wcsVo.setWhereSqllet(whereSqllet);
			}
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> wcsList = (List<WcSchdlBVo>)commonSvc.queryList(wcsVo);
			if(wcsList.size() == 0){
				// 해당일에 대한 일정이 없습니다.
				throw new CmException("wc.jsp.setProm.sendSchdlError", request);
			}
			
			if(!"transAllEmailAjx".equals(path)) wcsVo.setRegrNm(userVo.getUserNm()); 
			
			//이메일 발송 정보 조회
			CmEmailBVo cmEmailBVo = wcCmSvc.getEmailInfo(request, wcsVo, wcsList);
			JSONArray recvIds = null;
			QueryQueue queryQueue = new QueryQueue();
						
			//이메일 정보 저장 - 반복설정과 여러개의 스케쥴의 파일이 많아서 안보내기로함.
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , recvIds , null , queryQueue , userVo);
			
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
	    	searchVo.setSchdlStartDt(wcScdManagerSvc.getDateOfDay(year+"-"+month+"-"+"01", "month", "s", null, 1));
	    	searchVo.setSchdlEndDt(wcScdManagerSvc.getDateOfDay(year+"-"+month+"-"+LastDay, "month", "p", null, 1));

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
					String pDate = wcScdManagerSvc.getDateOfDay(pYear+"-"+pMonth+"-"+pDay, "day", "p", null, 1).replaceAll("[-: ]", "").substring(0,8);
					strHolidays += pDate + ","; 
					intStrt = Integer.parseInt(pDate);
				}
				
				strHolidays += endYmd + ":"; 
			}

			model.put("message", strHolidays);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
    
    /** 일정 보기 */
	/*@RequestMapping(value = "/wc/listNewSchdl")
	public String listNewSchdl(HttpServletRequest request,			
			@RequestParam(value="action",required=false)
			String action,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String schdlStartDt = ParamUtil.getRequestParam(request, "schdlStartDt", false);
			String viewTyp = ParamUtil.getRequestParam(request, "viewTyp", false);
			
			// 날짜 형식 체크
			if(schdlStartDt!=null && !schdlStartDt.isEmpty() && !isDateChk(schdlStartDt)) schdlStartDt=null;
			
			if(viewTyp==null || viewTyp.isEmpty()) viewTyp = "month";
			// 시작 종료일자 조회
			String[] strtEnd = getPeriodDt(schdlStartDt, viewTyp);
			String startDt = strtEnd[0];
			String endDt = strtEnd[1];
			
			// 조회
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			
			//음력일자 계산을 위해 한달 전부터 조회
			String realStrtDt = wcScdManagerSvc.getDateOfDay(startDt, "month", "s", null, 1);
			
			//일정 조회
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlEndDt(endDt);
			 
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> wcSchdlBVoList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
			model.put("wcSchdlBVoList", getLunarSchdlList(wcSchdlBVoList));
			
			// 기념일 조회(음력일정을 구하기 위해 한달전 일정까지 조회한다.)
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlTypCd("5");//기념일
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> annvList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
			model.put("annvList", getLunarSchdlList(annvList));
			
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
			
			// 일정대상 목록 조회(추후 다국어 처리)
			setSchdlKndCdList(model , request);
			
			// 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/listNewSchdl");
	}*/
	
    /** 일정 보기 */
	@RequestMapping(value = "/wc/listNewSchdl")
	public String listNewSchdl(HttpServletRequest request,
			@RequestParam(value="fncCal",required=false) String fncCal,
			@RequestParam(value="schdlKndCd",required=false) String schdlKndCd,
			@RequestParam(value="agnt",required=false) String agnt,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			if(fncCal==null || fncCal.isEmpty()) fncCal = "my";
			if("my".equals(fncCal)){
				List<WcAgntRVo> wcAgntList = wcScdManagerSvc.getAgntTgtList(langTypCd, userVo.getUserUid());
				model.put("wcAgntVos", wcAgntList);
			}else if("open".equals(fncCal)){
				if(schdlKndCd==null || schdlKndCd.isEmpty()) schdlKndCd = "1";
				
				List<WcBumkDVo> bumkList = null;
				if("1".equals(schdlKndCd)) bumkList = wcScdManagerSvc.getBumkPsn(userVo.getUserUid());
				else bumkList = wcScdManagerSvc.getBumkDept(userVo.getUserUid());
				model.put("bumkList", bumkList);
			}
			
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

			//대리인
			if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
				wcSchdlGrpBVo.setUserUid(agnt);
			}
			
			List<WcSchdlGrpBVo> wcSchdlGroupBVoList = wcScdManagerSvc.getWcSchdlGroupList(wcSchdlGrpBVo);
			model.put("wcSchdlGroupBVoList", wcSchdlGroupBVoList);
			
			// 일정대상 목록 조회(추후 다국어 처리)
			setSchdlKndCdList(model , request);
			
			// 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
			// 일정 시작일자
			String strtDt = ParamUtil.getRequestParam(request, "strtDt", false);
			
			if(strtDt==null || strtDt.isEmpty() || !isDateChk(strtDt)) strtDt = StringUtil.getCurrYmd();
			
			model.put("strtDt", strtDt);
			
			// 언어 코드 변환
			model.put("calLangTypCd", wcScdManagerSvc.getCalLangTypCd(langTypCd));
			
			if(schdlKndCd==null) {
				// 사용자 설정 정보 조회
				Map<String, String> userSetupMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), WcConstant.USER_CONFIG, false);				
				if(userSetupMap!=null && userSetupMap.containsKey("schdlKndCd")) schdlKndCd = userSetupMap.get("schdlKndCd");
				else{
					// 환경설정
					Map<String, String> envConfigMap = wcAdmSvc.getEnvConfigMap(null, userVo.getCompId());
					if(envConfigMap!=null && envConfigMap.containsKey("schdlKndCd")) schdlKndCd = envConfigMap.get("schdlKndCd");				
				}
				if(schdlKndCd!=null) model.put("paramSchdlKndCd", schdlKndCd);
			}
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/listCalendar");
	}
	
    /** [AJAX] 일정 조회 */
	@RequestMapping(value = "/wc/listSchdlAjx")
	public String listSchdlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String start = (String) object.get("start");
			String end = (String) object.get("end");
			if ( start == null || start.isEmpty() || end == null || end.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 국가코드
			String natCd = (String) object.get("natCd");
			if(natCd==null || natCd.isEmpty()){
				natCd = wcScdManagerSvc.getNatCd(userVo);
			}
						
			//음력일자 계산을 위해 한달 전부터 조회
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
			// 조회
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			wcSchdlBVo.setCompId(userVo.getCompId());
			
			// 조회여부
			boolean isSrch = true;
			
			// 추가조회조건 세팅
			String fncCal = (String) object.get("fncCal");
			if(fncCal==null || fncCal.isEmpty()) fncCal = "my";
			String schdlKndCd = (String) object.get("schdlKndCd");
			
			// 사용자 비공개 체크
			boolean isOthrUserChk = false;
			// 부서 비공개 체크
			boolean isOthrDeptChk = false;
			// 그룹 권한 체크
			boolean isUserGrpChk = false;
			
			if("open".equals(fncCal)){// 공개
				String viewUserUid = (String) object.get("viewUserUid");
				String viewOrgId = (String) object.get("viewOrgId");
				String bumk = (String) object.get("bumk");
				
				if((schdlKndCd==null || schdlKndCd.isEmpty()) || ((viewUserUid == null || viewUserUid.isEmpty()) && (viewOrgId == null || viewOrgId.isEmpty()) && (bumk == null || "-1".equals(bumk))) ){
					isSrch = false;
				}else{
					// 공개일정 : 사용자 1 , 부서 3 체크
					if(!"1".equals(schdlKndCd) && !"3".equals(schdlKndCd)) isSrch = false;
					else{
						wcSchdlBVo.setSchdlKndCd(schdlKndCd);
						if(viewUserUid!=null && !viewUserUid.isEmpty()){
							wcSchdlBVo.setUserUid(viewUserUid);
						}else if(viewOrgId!=null && !viewOrgId.isEmpty()){
							wcSchdlBVo.setDeptId(viewOrgId);
						}
						if(bumk!=null && !bumk.isEmpty() && !"-1".equals(bumk)){
							String[] tempValue = bumk.split(":");
							if(tempValue.length > 1 ) bumk = tempValue[0];
							if("1".equals(schdlKndCd)) wcSchdlBVo.setUserUid(bumk);
							else wcSchdlBVo.setDeptId(bumk);
						}
						
						if(wcSchdlBVo.getUserUid() != null) isOthrUserChk = true;
						else if(wcSchdlBVo.getDeptId() != null) isOthrDeptChk = true;
					}
					
				}
				
			}else if("dept".equals(fncCal)){ // 부서
				wcSchdlBVo.setSchdlKndCd("3");
				wcSchdlBVo.setDeptId(userVo.getOrgId());
			}else if("comp".equals(fncCal)){ // 회사
				wcSchdlBVo.setSchdlKndCd("4");
			}else{ // 나의 일정
				String userUid = userVo.getUserUid();
				// 부서ID
				String deptId = userVo.getOrgId();
				String agnt = (String) object.get("agnt"); // 대리인
				if(agnt!=null && !agnt.isEmpty() && !"-1".equals(agnt)){
					// 대리인 여부 확인
					isSrch = wcScdManagerSvc.chkSchdlUser(wcSchdlBVo.getUserUid(), userVo.getUserUid(), "agnt");
					// 대리인의 부서정보 조회
					OrUserBVo orUserBVo = new OrUserBVo();
					orUserBVo.setUserUid(agnt);
					orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
					if(orUserBVo==null){
						// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
						throw new CmException("pt.login.noUser", request);
					}
					userUid = agnt;
					deptId = orUserBVo.getDeptId();
				}
				
				if(isSrch){
					String[] grpList = wcScdManagerSvc.getGrpList(userVo.getUserUid());
					if(schdlKndCd==null || schdlKndCd.isEmpty()) { // 일정대상이 없을 경우
						wcSchdlBVo.setUserUid(userUid);
						wcSchdlBVo.setDeptId(deptId);
						wcSchdlBVo.setChoiGrpIds(grpList);
						isUserGrpChk=true; // 그룹권한체크
					}else{ // 일정대상이 있을 경우
						wcSchdlBVo.setSchdlKndCd(schdlKndCd);
						if("2".equals(schdlKndCd)){ // 그룹일정							
							if(grpList==null) isSrch=false;// 그룹이 없으면 조회안함
							else{
								isUserGrpChk=true; // 그룹권한체크
								String myGrp = (String) object.get("myGrp");
								if(myGrp!=null && !myGrp.isEmpty()){ // 그룹 상세조건이 있을경우
									if(ArrayUtil.isInArray(grpList, myGrp)) wcSchdlBVo.setGrpId(myGrp); // 전체그룹중에 해당그룹이 있을경우에만 조회
									else isSrch=false;
								}else wcSchdlBVo.setChoiGrpIds(grpList);
							}
							
						}else if("3".equals(schdlKndCd)){
							wcSchdlBVo.setDeptId(deptId);
						}else if("4".equals(schdlKndCd)){
							wcSchdlBVo.setCompId(userVo.getCompId()); // 타회사 일정 배제
						}else{
							wcSchdlBVo.setUserUid(userUid);
						}
					}
				}
				
			}
			
			//일정 조회
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlEndDt(end);
			 
			if(isSrch){
				@SuppressWarnings("unchecked")
				List<WcSchdlBVo> wcSchdlBVoList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
				if(isOthrUserChk || isOthrDeptChk){
					boolean isApntOk = wcScdManagerSvc.chkSchdlUser(wcSchdlBVo.getUserUid(), userVo.getUserUid(), "apnt");
					List<WcSchdlBVo> removeList = new ArrayList<WcSchdlBVo>();
					for(WcSchdlBVo storedWcSchdlBVo : wcSchdlBVoList){
						if(storedWcSchdlBVo.getOpenGradCd()==null) continue;
						//지정인공개
						if(isOthrUserChk && "2".equals(storedWcSchdlBVo.getOpenGradCd()) && !isApntOk) removeList.add(storedWcSchdlBVo);
						else if((isOthrUserChk || isOthrDeptChk) && "3".equals(storedWcSchdlBVo.getOpenGradCd())) removeList.add(storedWcSchdlBVo);
					}
					for(WcSchdlBVo removeVo : removeList){
						wcSchdlBVoList.remove(removeVo);
					}
				}
				
				// 그룹권한 체크
				if(isUserGrpChk){
					List<WcSchdlBVo> removeList = new ArrayList<WcSchdlBVo>();
					// 그룹권한 VO
					WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = null;
					for(WcSchdlBVo storedWcSchdlBVo : wcSchdlBVoList){// 그룹권한 체크
						if((storedWcSchdlBVo.getSchdlKndCd()!=null && !"2".equals(storedWcSchdlBVo.getSchdlKndCd())) || storedWcSchdlBVo.getGrpId()==null || storedWcSchdlBVo.getGrpId().isEmpty()) continue;
						wcSchdlGrpMbshDVo = wcScdManagerSvc.getWcSchdlGrpMbshDVo(userVo, storedWcSchdlBVo.getGrpId());
						if(wcSchdlGrpMbshDVo==null){
							removeList.add(storedWcSchdlBVo);
							continue;
						}
						// 그룹생성자(O), 멤버(M)면서 W(쓰기), A(관리) 일경우 수정권한 부여
						storedWcSchdlBVo.setEditYn(("O".equals(wcSchdlGrpMbshDVo.getMbshTypCd()) || ("M".equals(wcSchdlGrpMbshDVo.getMbshTypCd()) && ("A".equals(wcSchdlGrpMbshDVo.getAuthGradCd()) || "W".equals(wcSchdlGrpMbshDVo.getAuthGradCd()) ))) ? "Y" : "N");
					}
					for(WcSchdlBVo removeVo : removeList){
						wcSchdlBVoList.remove(removeVo);
					}
					
				}
				
				model.put("wcSchdlBVoList", wcScdManagerSvc.getLunarSchdlList(wcSchdlBVoList));
			}else{
				model.put("wcSchdlBVoList", null);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			wcSchdlBVo.setQueryLang(langTypCd);
			wcSchdlBVo.setCompId(userVo.getCompId());
			wcSchdlBVo.setNatCd(natCd); // 국가코드
			// 기념일 조회(음력일정을 구하기 위해 한달전 일정까지 조회한다.)
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlEndDt(end);
			wcSchdlBVo.setSchdlTypCd("5");//기념일
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> annvList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
			model.put("annvList", wcScdManagerSvc.getLunarSchdlList(annvList));
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);			
		
	}
	
	/** [AJAX] 기념일 조회 : 사용안함 */
	@RequestMapping(value = "/wc/getAnnvListAjx")
	public String getAnnvListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try{
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String start = (String) object.get("start");
			String end = (String) object.get("end");
			if ( start == null || start.isEmpty() || end == null || end.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 국가코드
			String natCd = (String) object.get("natCd");
			if(natCd==null || natCd.isEmpty()){
				natCd = wcScdManagerSvc.getNatCd(userVo);
			}
						
			//음력일자 계산을 위해 한달 전부터 조회
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
			// 조회
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
			wcSchdlBVo.setCompId(userVo.getCompId());
			
			//일정 조회
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlEndDt(end);
			
			wcSchdlBVo.setNatCd(natCd); // 국가코드
			// 기념일 조회(음력일정을 구하기 위해 한달전 일정까지 조회한다.)
			wcSchdlBVo.setSchdlStartDt(realStrtDt);
			wcSchdlBVo.setSchdlTypCd("5");//기념일
			
			@SuppressWarnings("unchecked")
			List<WcSchdlBVo> annvList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
			
			model.put("annvList", JsonUtil.toJson(wcScdManagerSvc.getLunarSchdlList(annvList)));
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);			
		
	}
	
	
	/** [AJAX] 일정 저장 */
	@RequestMapping(value = "/wc/transSchdlAjx")
	public String transSchdlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String schdlId = (String) object.get("schdlId"); // 일정ID
			String schdlStartDt = (String) object.get("start"); // 시작일시
			String schdlEndDt = (String) object.get("end"); // 종료일시
			String alldayYn = (String) object.get("alldayYn"); // 종일여부
			
			if ( schdlId == null || schdlId.isEmpty() || schdlStartDt == null || schdlStartDt.isEmpty() || 
					schdlEndDt == null || schdlEndDt.isEmpty() || alldayYn == null || alldayYn.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
			wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.updateSchdlB");
			wcSchdlBVo.setSchdlId(schdlId);
			wcSchdlBVo.setSchdlPid(schdlId);
			wcSchdlBVo.setSchdlStartDt(schdlStartDt);
			wcSchdlBVo.setSchdlEndDt(schdlEndDt);
			wcSchdlBVo.setRepetYn("N");//반복 제거
			wcSchdlBVo.setAlldayYn(alldayYn);
			wcSchdlBVo.setCompId(userVo.getCompId());
			
			commonSvc.update(wcSchdlBVo);
			
			// dm.msg.dtlView.cancel.success=처리되었습니다.
			//model.put("message", messageProperties.getMessage("dm.msg.dtlView.cancel.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 월,주,일의 시작 종료일 구하기 */
	public String[] getPeriodDt(String day, String viewTyp){
		String[] period = new String[2];
		if("month".equals(viewTyp) || "week".equals(viewTyp)){
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.SUNDAY); // 주 시작일을 일요일로 설정
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(day!=null){
				day = day.replaceAll("[-: ]", "");//-:공백 제거
				cal.set(Calendar.YEAR, Integer.parseInt(day.substring(0,4)) ) ;
		        cal.set(Calendar.MONTH, Integer.parseInt(day.substring(4,6))-1 ) ;
		        if(!"month".equals(viewTyp)) cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.substring(6,8)) ) ;
			}
			if("month".equals(viewTyp)) {
				cal.set(Calendar.DAY_OF_MONTH, 1);
				
				// 현재월의 시작
				int start = cal.get(Calendar.DAY_OF_WEEK);
				
				if(start>1){
					// 전월 구하기
					cal.add(Calendar.MONTH, -1);
					int beforeStrt = cal.getActualMaximum(Calendar.DATE);
					beforeStrt = (beforeStrt - start)+2;
					cal.set(Calendar.DAY_OF_MONTH, beforeStrt);
				}
				
				period[0] = sdf.format(cal.getTime());
				// 6주 더하기
				cal.add(Calendar.DATE, (42-1));
				period[1] = sdf.format(cal.getTime());
			}else{// 주간 구하기
				cal.set(Calendar.DAY_OF_WEEK, 1);
				period[0] = sdf.format(cal.getTime());
				cal.set(Calendar.DAY_OF_WEEK, 7);
				period[1] = sdf.format(cal.getTime());
			}
		}else{
			period[0] = day;
			period[1] = day;
		}
		return period;
		
		
	}
	
	/** 날짜 형식 체크(yyyy-mm-dd) */
	public boolean isDateChk(String val){
		return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", val);
	}
	
	/** 사용자그룹 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wc/listUserGrpFrm","/wc/setUserGrpPop","/wc/findUserGrpPop", "/wr/findUserGrpPop", "/wc/adm/listUserGrpFrm","/wc/adm/setUserGrpPop"})
	public String listUserGrpFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		//model.put("path", path);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wc/adm/");
		String userUid = ParamUtil.getRequestParam(request, "paramUserUid", false);
		if(!isAdmin && userUid!=null && !userUid.isEmpty() && !wcScdManagerSvc.chkSchdlUser(userUid, userVo.getUserUid(), "agnt")){ // 대리인 여부 확인
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(userUid==null || userUid.isEmpty()) userUid = userVo.getUserUid();
		model.put("isAdmin", isAdmin);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<WcUserGrpBVo> wcUserGrpBVoList = new ArrayList<WcUserGrpBVo>();
		if(userUid!=null && !userUid.isEmpty()){
			// 사용자그룹 기본 조회
			WcUserGrpBVo wcUserGrpBVo = new WcUserGrpBVo();
			wcUserGrpBVo.setQueryLang(langTypCd);
			wcUserGrpBVo.setCompId(userVo.getCompId());
			wcUserGrpBVo.setModrUid(userUid);
			wcUserGrpBVo.setOrderBy("SORT_ORDR");			
			// 조회
			wcUserGrpBVoList = (List<WcUserGrpBVo>)commonSvc.queryList(wcUserGrpBVo);
		}
		model.put("wcUserGrpBVoList", wcUserGrpBVoList);
		if(path.endsWith("Pop")){
			if(path.startsWith("set")){
				// 화면 구성용 2개의 빈 vo 넣음
				wcUserGrpBVoList.add(new WcUserGrpBVo());
				wcUserGrpBVoList.add(new WcUserGrpBVo());
				for (WcUserGrpBVo storedWcUserGrpBVo : wcUserGrpBVoList) {
					if (storedWcUserGrpBVo.getRescId() != null) {
						// 리소스기본(DM_RESC_B) 테이블 - 조회
						wcRescSvc.queryRescBVo(storedWcUserGrpBVo.getRescId(), model);
					}
				}
				model.put("isSelect", Boolean.FALSE); // 선택불가
			}else{
				model.put("isSelect", Boolean.TRUE); // 선택가능
			}
			return LayoutUtil.getJspPath("/wc/env/setUserGrpPop");
		}
		
		return LayoutUtil.getJspPath("/wc/env/listUserGrpFrm");
	}
	
	/** 사용자그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wc/listUserGrpDtlListFrm", "/wr/listUserGrpDtlListFrm", "/wc/adm/listUserGrpDtlListFrm"})
	public String listUserGrpDtlListFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 사용자그룹ID
		String userGrpId = ParamUtil.getRequestParam(request, "userGrpId", false);
		
		if(userGrpId!=null && !userGrpId.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 관리자 여부
			boolean isAdmin = request.getRequestURI().startsWith("/wc/adm/");
			String userUid = ParamUtil.getRequestParam(request, "paramUserUid", false);
			if(!isAdmin && userUid!=null && !userUid.isEmpty() && !wcScdManagerSvc.chkSchdlUser(userUid, userVo.getUserUid(), "agnt")){ // 대리인 여부 확인
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			if(userUid==null || userUid.isEmpty()) userUid = userVo.getUserUid();
			
			// 사용자그룹 기본 [검증]
			WcUserGrpBVo wcUserGrpBVo = new WcUserGrpBVo();
			wcUserGrpBVo.setCompId(userVo.getCompId());
			wcUserGrpBVo.setUserGrpId(userGrpId);
			wcUserGrpBVo.setModrUid(userUid);
			if(commonSvc.count(wcUserGrpBVo)==0){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 사용자그룹 목록
			WcUserGrpLVo wcUserGrpLVo = new WcUserGrpLVo();
			wcUserGrpLVo.setQueryLang(langTypCd);
			wcUserGrpLVo.setUserGrpId(userGrpId);
			wcUserGrpLVo.setOrderBy("SORT_ORDR");
			// 목록 조회(USER_UID)
			List<WcUserGrpLVo> wcUserGrpLVoList = (List<WcUserGrpLVo>)commonSvc.queryList(wcUserGrpLVo);
			//List<String> userUidList = new ArrayList<String>();
			List<OrUserBVo> orUserBVoList = new ArrayList<OrUserBVo>();
			OrUserBVo orUserBVo;
			for(WcUserGrpLVo storedWcUserGrpLVo : wcUserGrpLVoList){
				//userUidList.add(storedWcUserGrpLVo.getUserUid());
				orUserBVo = new OrUserBVo();
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo.setUserUid(storedWcUserGrpLVo.getUserUid());
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				if(orUserBVo!=null) orUserBVoList.add(orUserBVo);
			}
			/*if(userUidList.size()>0){
				// 사용자기본(OR_USER_B) 테이블 조회
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo.setUserUidList(userUidList);
				orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
			}*/
			// 화면 구성용 1개의 빈 vo 넣음
			orUserBVoList.add(new OrUserBVo());
			model.put("orUserBVoList", orUserBVoList);
			
		}
		
		return LayoutUtil.getJspPath("/wc/env/listUserGrpDtlListFrm");
	}
	
	/** [히든프레임]  사용자그룹 일괄 저장(왼쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wc/transUserGrpList")
	public String transUserGrpList(HttpServletRequest request,			
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 기본(WC_USER_GRP_B) 테이블 VO
			WcUserGrpBVo wcUserGrpBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String userGrpId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			// 사용자그룹 목록
			WcUserGrpLVo wcUserGrpLVo;
			for(i=0;i<delCds.length;i++){
				userGrpId = delCds[i];
				
				// 기본(WC_USER_GRP_B) 테이블 - 삭제
				wcUserGrpBVo = new WcUserGrpBVo();
				wcUserGrpBVo.setCompId(userVo.getCompId());
				wcUserGrpBVo.setUserGrpId(userGrpId);
				wcUserGrpBVo.setModrUid(userVo.getUserUid()); // 사용자 확인
				
				if(commonSvc.count(wcUserGrpBVo)==0) continue;
				
				// 목록(WC_USER_GRP_L) 테이블 - 삭제
				wcUserGrpLVo = new WcUserGrpLVo();
				wcUserGrpLVo.setUserGrpId(userGrpId);
				queryQueue.delete(wcUserGrpLVo);
				
				queryQueue.delete(wcUserGrpBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 기본(WC_USER_GRP_B) 테이블
			List<WcUserGrpBVo> wcUserGrpBVoList = (List<WcUserGrpBVo>)VoUtil.bindList(request, WcUserGrpBVo.class, new String[]{"valid"});
			size = wcUserGrpBVoList==null ? 0 : wcUserGrpBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				wcRescSvc.collectWcRescBVoList(request, queryQueue, wcUserGrpBVoList, "valid", "rescId", "userGrpNm");
			}
			
			for(i=0;i<size;i++){
				wcUserGrpBVo = wcUserGrpBVoList.get(i);				
				// 수정자, 수정일시
				wcUserGrpBVo.setModrUid(userVo.getUserUid());
				wcUserGrpBVo.setModDt("sysdate");
				
				if(wcUserGrpBVo.getUserGrpId() == null || wcUserGrpBVo.getUserGrpId().isEmpty()){
					wcUserGrpBVo.setUserGrpId(wcCmSvc.createId("WC_USER_GRP_B"));
				}
				wcUserGrpBVo.setCompId(userVo.getCompId());
				queryQueue.store(wcUserGrpBVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// 팝업이름
			String popNm = ParamUtil.getRequestParam(request, "popNm", false);
			if(popNm==null || popNm.isEmpty()) popNm = "setUserGrpDialog";
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadFrm(null, '"+popNm+"');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [히든프레임]  사용자그룹 목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wc/transUserGrpDtlList")
	public String transUserGrpDtlList(HttpServletRequest request,			
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 사용자그룹ID
			String userGrpId = ParamUtil.getRequestParam(request, "userGrpId", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 사용자그룹 기본 [검증]
			WcUserGrpBVo wcUserGrpBVo = new WcUserGrpBVo();
			wcUserGrpBVo.setCompId(userVo.getCompId());
			wcUserGrpBVo.setUserGrpId(userGrpId);
			wcUserGrpBVo.setModrUid(userVo.getUserUid()); // 사용자 확인
			if(commonSvc.count(wcUserGrpBVo)==0){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 기본(WC_USER_GRP_L) 테이블 VO
			WcUserGrpLVo wcUserGrpLVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String userUid;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				userUid = delCds[i];
				
				// 기본(WC_USER_GRP_L) 테이블 - 삭제
				wcUserGrpLVo = new WcUserGrpLVo();
				wcUserGrpLVo.setUserGrpId(userGrpId);
				wcUserGrpLVo.setUserUid(userUid);
				queryQueue.delete(wcUserGrpLVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 기본(WC_USER_GRP_L) 테이블
			List<WcUserGrpLVo> wcUserGrpLVoList = (List<WcUserGrpLVo>)VoUtil.bindList(request, WcUserGrpLVo.class, new String[]{"valid"});
			size = wcUserGrpLVoList==null ? 0 : wcUserGrpLVoList.size();
			
			for(i=0;i<size;i++){
				wcUserGrpLVo = wcUserGrpLVoList.get(i);
				wcUserGrpLVo.setUserGrpId(userGrpId);
				queryQueue.store(wcUserGrpLVo);
			}
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+userGrpId+"');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [AJAX] 날짜 조회 - 공휴일제외(결재ERP) */
	@RequestMapping(value = {"/cm/date/getSrchDateListAjx"})
	public String getSrchDateListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String start = (String) object.get("start"); // 시작일
			String end = (String) object.get("end"); // 종료일
			String holiYn = (String) object.get("holiYn"); // 휴일여부[null 이면 전체 날짜 조회]
			String onlyHoli = (String) object.get("onlyHoli"); // 휴일만 리턴
			String format = (String) object.get("format"); // 날짜포맷
			// 국가코드
			String natCd = (String) object.get("natCd");
			
			if(start==null || start.isEmpty()) // 시작일자가 없으면 현재 날짜 삽입
				start=commonDao.querySysdate(null).substring(0,10);
			
			if(end==null || end.isEmpty()) // 종료일자가 없으면 현재 날짜 삽입
				end=commonDao.querySysdate(null).substring(0,10);
			
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
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
			}
			
			realStrtDt=start.replaceAll("[-: ]", "");
			end=end.replaceAll("[-: ]", "");
			
			// 휴일만 리턴
			boolean isOnlyHoli=onlyHoli!=null && "Y".equals(onlyHoli);
			List<String> fromToList = wcScdManagerSvc.getFromToDate(realStrtDt, end, excludeList, excludeList==null ? true : false, format, isOnlyHoli);
			model.put("fromToList", fromToList);
			model.put("result", "ok");

		} catch (CmException e) {
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
			@RequestParam(value = "format", required = false) String format,
			ModelMap model) throws Exception {
		try {
			if(start==null || start.isEmpty()) // 시작일자가 없으면 현재 날짜 삽입
				start=commonDao.querySysdate(null).substring(0,10);
			
			if(end==null || end.isEmpty()) // 종료일자가 없으면 현재 날짜 삽입
				end=commonDao.querySysdate(null).substring(0,10);
			
			String realStrtDt = wcScdManagerSvc.getDateOfDay(start, "month", "s", null, 1);
			realStrtDt = wcScdManagerSvc.getDateOfDay(realStrtDt, "month", "s", null, 1);
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
				excludeList = wcScdManagerSvc.getSelectSchdlList(userVo.getCompId(), langTypCd, natCd, realStrtDt, end, format);
				model.put("excludeList", excludeList);
			}
			
			realStrtDt=start.replaceAll("[-: ]", "");
			end=end.replaceAll("[-: ]", "");
			List<String[]> fromToList = wcScdManagerSvc.getFromToDates(realStrtDt, end, excludeList, true, format);
			model.put("fromToList", fromToList);

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getJspPath("/wc/cm/getSrchDateListPop");
	}
	
	/** [메일] - 참석자 일정 등록 */
	@RequestMapping(value = "/cm/wc/transGuestSchdl")
	public String transGuestSchdl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "acptId", required = false) String acptId,
			@RequestParam(value = "acptYn", required = false) String acptYn,
			@RequestParam(value = "mailId", required = false) String mailId,
			ModelMap model) throws Exception{
		
		// 모바일 디바이스의 경우 - 모바일 웹서버로 redirect
		if(Browser.isMobile(request)){
			// 모바일 서버로 리다이렉트
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String mobileDomain = svrEnvMap.get("mobileDomain");
			if(mobileDomain!=null && !mobileDomain.isEmpty()){
				boolean useSSL = request.isSecure();
				response.sendRedirect((useSSL ? "https://" : "http://")+mobileDomain+request.getRequestURI()+"?"+request.getQueryString());
				return null;
			} else {
				LOGGER.error("Mobile domain is not set !");
			}
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		if(useSSL && !request.isSecure() && request.getServerPort()==80){
			response.sendRedirect("https://"+request.getServerName()+request.getRequestURI()+"?"+request.getQueryString());
			return null;
		}
		
		//String jspPath = "/wc/mail/viewHdlResult";
		
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
			
			//String schdlId = acptId;
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
			//return LayoutUtil.getJspPath(jspPath, "Pop");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		// 팝업 닫기
		model.put("todo", "window.open('about:blank', '_self').close();");
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
				
		//return LayoutUtil.getJspPath(jspPath, "Pop");
	}
	
	/** [POPUP] 개인 설정 */
	@RequestMapping(value = "/wc/setUserSetupPop")
	public String setUserSetupPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 일정대상 조회
		model.put("schdlKndCdList", wcCmSvc.getSchdlKndList(request));
		
		Map<String, String> userSetupMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), WcConstant.USER_CONFIG, false);
		model.put("userSetupMap", userSetupMap);
		
		model.put("setupClsId", WcConstant.USER_CONFIG);
		
		return LayoutUtil.getJspPath("/wc/env/setUserSetupPop");
	}
	
}
