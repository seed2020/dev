package com.innobiz.orange.web.wc.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;
import com.innobiz.orange.web.wc.calender.WcScdCalMonth;
import com.innobiz.orange.web.wc.svc.WcCmSvc;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wc.vo.WcAgntRVo;
import com.innobiz.orange.web.wc.vo.WcApntrRVo;
import com.innobiz.orange.web.wc.vo.WcApntrSchdlDVo;
import com.innobiz.orange.web.wc.vo.WcBumkDVo;
import com.innobiz.orange.web.wc.vo.WcCatClsBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpBVo;
import com.innobiz.orange.web.wr.svc.WrCmSvc;
import com.innobiz.orange.web.wr.vo.WrDayVo;
import com.innobiz.orange.web.wr.vo.WrMonthVo;
import com.innobiz.orange.web.wr.vo.WrWeekVo;


/** 일정관리 */
@Controller
public class WcSchdlPltCtrl {
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WcScdManagerSvc wcScdManagerSvc;

	/** schdlID생성 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
//	/** 게시파일 서비스 */
//	@Autowired
//	private WcFileSvc wcFileSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 명함 공통 서비스 */
//	@Autowired
//	private WbBcSvc wbBcSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WrCmSvc wrCmSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WcSchdlPltCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	

	/** 나의 일정 보기 - 기존 */
	@RequestMapping(value = {"/wc/plt/viewSchdlMngPlt","/wc/plt/viewMySchdlPlt"})
	public String viewSchdlMngPlt(HttpServletRequest request,			
			@RequestParam(value="action",required=false) String action,
			ModelMap model) throws Exception {
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
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
		
		String schdlKndCd = request.getParameter("schdlKndCd");
		
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
			if(viewUserUid == null || viewOrgId == null || "-1".equals(bumk) ){
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
			int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(defYear, currentMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			//WcScdCalWeek scdWeek = wcScdManagerSvc.getScdWeek(defYear, currentMonth, currentWeek,schdlTyp,scdMonth);
			WcScdCalDay scdDay = wcScdManagerSvc.getScdDay(defYear, currentMonth, currentDay,schdlTyp,scdMonth);
					
			model.put("wcScdCalMonth", scdMonth);
			//model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);		
			model.put("wcScdToday", currentDay);
		}else if(action.equals("")){
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));
			
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(molyYear, molyMonth,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			//WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId());
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
			model.put("wcScdCalMonth", scdMonth);
			//model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}else if(action.equals("after")){			
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));
			
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
	

			if(tabNo.equals("0")){				
				if(12 >= molyMonth + 1)	molyMonth += 1;
				else{
					molyYear += 1;
					molyMonth = 1;
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
			//WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId());
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
			model.put("wcScdCalMonth", scdMonth);
			//model.put("wcScdCalWeek", scdWeek);
			model.put("wcScdCalDay", scdDay);
			
		}else if(action.equals("before")){
			int molyYear = Integer.parseInt(request.getParameter("molyYear"));
			int molyMonth = Integer.parseInt(request.getParameter("molyMonth"));			
			int dalyYear = Integer.parseInt(request.getParameter("dalyYear"));
			int dalyMonth = Integer.parseInt(request.getParameter("dalyMonth"));
			int dalyDay = Integer.parseInt(request.getParameter("dalyDay"));
			
		
			if(tabNo.equals("0")){			
				if(0 < molyMonth - 1) molyMonth -= 1;
				else{
					molyYear -= 1;
					molyMonth = 12;
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
			//WcScdCalWeek scdWeek = (molyYear==welyYear&&molyMonth==welyMonth)?wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp, scdMonth):wcScdManagerSvc.getScdWeek(welyYear, welyMonth, welyWeek,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId());
			WcScdCalDay scdDay = (molyYear==dalyYear&&molyMonth==dalyMonth)?wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,scdMonth):wcScdManagerSvc.getScdDay(dalyYear, dalyMonth, dalyDay,schdlTyp,timeZone,bumk,agnt, choiGrpIds,viewUserUid,viewOrgId,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null);
					
			model.put("wcScdCalMonth", scdMonth);
			//model.put("wcScdCalWeek", scdWeek);
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
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		//일정종류(카테고리)를 model 에 담는다
		for(WcCatClsBVo storedWcCatClsBVo : wcCatClsBVoList){
			model.put("cat_"+storedWcCatClsBVo.getCatId(), VoUtil.toMap(storedWcCatClsBVo, null));
		}
		
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/plt/"+path);
	}
	
	
	
	/** 타인 일정 보기  */
	@RequestMapping(value = "/wc/plt/viewApgntSchdlPlt")
	public String viewApgntSchdlPlt(HttpServletRequest request,				
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		List<WcApntrSchdlDVo> apntSchdlList = wcScdManagerSvc.getApntSchdlList(userVo.getLangTypCd(),userVo.getUserUid());
		// set, list 로 시작하는 경우 처리
		checkPath(request, "listPsnSchdl", model);
		int schdlTyp=1;
		String timeZone=TimeZone.getDefault().getID();
	
			
		
		Calendar currentCal = Calendar.getInstance();
		int defYear = currentCal.get(Calendar.YEAR);		
		int currentMonth = currentCal.get(Calendar.MONTH) +1;		
		int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
		
		
		for(WcApntrSchdlDVo apntSchdlDvo:apntSchdlList){
			//반복구간 S
			String viewUserUid =apntSchdlDvo.getApntrUid();
			WcScdCalMonth scdMonth = wcScdManagerSvc.getScdMonth(defYear, currentMonth,schdlTyp,timeZone,null,null, null,viewUserUid,null,userVo.getUserUid(),userVo.getDeptId(),userVo.getCompId(),null,true);
			WcScdCalDay scdDay = wcScdManagerSvc.getScdDay(defYear, currentMonth, currentDay,schdlTyp,scdMonth);
			apntSchdlDvo.setScdCalDay(scdDay);	
			//반복구간 E
		}
		model.put("wcScdToday", currentDay);
		model.put("apntSchdlList", apntSchdlList);
			
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 일정종류 목록 조회
		WcCatClsBVo wcCatClsBVo = new WcCatClsBVo();
		wcCatClsBVo.setQueryLang(langTypCd);
		wcCatClsBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WcCatClsBVo> wcCatClsBVoList = (List<WcCatClsBVo>)commonSvc.queryList(wcCatClsBVo);
		model.put("wcCatClsBVoList", wcCatClsBVoList);
		
		//일정종류(카테고리)를 model 에 담는다
		for(WcCatClsBVo storedWcCatClsBVo : wcCatClsBVoList){
			model.put("cat_"+storedWcCatClsBVo.getCatId(), VoUtil.toMap(storedWcCatClsBVo, null));
		}
		
		
		// 일정대상 목록 조회(추후 다국어 처리)
		setSchdlKndCdList(model , request);
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/plt/viewApgntSchdlPlt");
	}
	
	
	/** 타인 일정 설정  */
	@RequestMapping(value = "/wc/plt/setApgntSchdlPlt")
	public String setApgntSchdlPlt(HttpServletRequest request,				
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		List<WcApntrRVo> wcApntRvoLst=wcScdManagerSvc.getApntTgtList(userVo.getLangTypCd(), userVo.getUserUid());
		List<WcApntrSchdlDVo> wcApntSchdlDvoLst=wcScdManagerSvc.getApntSchdlList(userVo.getLangTypCd(), userVo.getUserUid());
		List<WcApntrRVo> deleteList=new ArrayList<WcApntrRVo>();
		for(WcApntrSchdlDVo apntSchdlDvo:wcApntSchdlDvoLst){
			for(WcApntrRVo apntDvo:wcApntRvoLst){
				if(apntSchdlDvo.getApntrUid().equals(apntDvo.getUserUid())){
					deleteList.add(apntDvo);
				}
			}
		}
		
		for(WcApntrRVo apntDvo:deleteList){
			wcApntRvoLst.remove(apntDvo);
		}
		model.put("wcApntRvoLst", wcApntRvoLst);
		model.put("wcApntSchdlDvoLst", wcApntSchdlDvoLst);
		return LayoutUtil.getJspPath("/wc/plt/setApgntSchdlPlt");
	}
	
	/** 타인 일정 설정 저장  */
	@RequestMapping(value = "/wc/plt/transApgntSchdlPlt")
	public String transApgntSchdlPlt(HttpServletRequest request,				
			ModelMap model) throws Exception {
		try{
			UserVo userVo = LoginSession.getUser(request);
			String choiApntIds[] = request.getParameterValues("choiApntIds");
			//String choiApntNms[] = request.getParameterValues("choiApntNms");
			WcApntrSchdlDVo conditionVo=new WcApntrSchdlDVo();
			conditionVo.setUserUid(userVo.getUserUid());
			conditionVo.setCompId(userVo.getCompId());
			commonDao.delete(conditionVo);
			QueryQueue queue=new QueryQueue();
			if(choiApntIds!=null && choiApntIds.length>0){
				for(int i=0; i<choiApntIds.length; i++){
					WcApntrSchdlDVo insertVo=new WcApntrSchdlDVo();
					insertVo.setUserUid(userVo.getUserUid());
					insertVo.setApntrUid(choiApntIds[i]);
					insertVo.setCompId(userVo.getCompId());
					insertVo.setOrdr(i);
					queue.insert(insertVo);
				}
				commonDao.execute(queue);
			}
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setupPltDialog');");
		
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			
		}
		//공통 처리 페이지
		return LayoutUtil.getResultJsp();
	}
	
	
	
	
	/** 일정 종류 조회 */
	public void setSchdlKndCdList(ModelMap model , HttpServletRequest request){
		//{schdlKndCd,이름,등록수정시적용여부}
		model.put("schdlKndCdList", new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"},{"2",messageProperties.getMessage("wc.option.grpSchdl", request),"Y"},{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}});
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
	
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	
	/** 월간, 일간 일정 (나의일정) */
	@RequestMapping(value = "/wc/plt/listSchdlPlt")
	public String listSchdlPlt(HttpServletRequest request,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "schedulePmValue", required = false) String schedulePmValue,
			@RequestParam(value = "listType", required = false) String listType,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
					
		UserVo userVo = LoginSession.getUser(request);
		
		//오늘
		if(schedulePmValue != null && "t".equals(schedulePmValue)){
			startDay = null;
		}
		
		if(listType == null || listType.isEmpty()){
			listType = "month";
		}
		
		// 현재 날짜 또는 조회한 날짜 세팅
		String paramDay = wrCmSvc.getDateOfDay(startDay, listType, schedulePmValue, null, 1);
	
		String durStrtDt = "";
		String durEndDt = "";
		if("month".equals(listType)){
			WrMonthVo wrMonthVo = wrCmSvc.getMonth(paramDay);
			model.put("wrMonthVo", wrMonthVo);
			durStrtDt = wrMonthVo.getBeforeStrtDt();
			durEndDt = wrMonthVo.getAfterEndDt();
			//일정관리에 등록되어 있는 기념일 조회
			
			wrCmSvc.setSpclDays(wrMonthVo, durStrtDt, durEndDt);
		}else if("day".equals(listType)){
			WrDayVo wrDayVo = wrCmSvc.getDay(paramDay);
			model.put("wrDayVo", wrDayVo);
			durStrtDt = paramDay;
			durEndDt = paramDay;
			//일정관리에 등록되어 있는 기념일 조회
			wrCmSvc.setSpclDays(wrDayVo, paramDay, paramDay);
		}else{
			// 날짜를 기준으로 주간날짜 정보 조회
			WrWeekVo wrWeekVo = wrCmSvc.getWeek(paramDay);
			//Map<String,String> weekMonth = wrCmSvc.getDateWeek(paramDay);
			model.put("wrWeekVo", wrWeekVo);
			durStrtDt = wrWeekVo.getStrtDt();
			durEndDt = wrWeekVo.getEndDt();
			//일정관리에 등록되어 있는 기념일 조회
			wrCmSvc.setSpclDays(wrWeekVo, durStrtDt, durEndDt);
		}
		
		// 조회조건 매핑
		WcSchdlBVo wcsVo = new WcSchdlBVo();
		wcsVo.setQueryLang(langTypCd);
		
		//나의 일정만 조회
		wcsVo.setUserUid(userVo.getUserUid());
		wcsVo.setDeptId(userVo.getDeptId());
		wcsVo.setCompId(userVo.getCompId());
		
		// 시작 일자와 종료일자 세팅
		wcsVo.setSchdlStartDt(wrCmSvc.getDateOfDay(durStrtDt, "month", "s", null, 1));//음력일정을 구하기 위해 한달전 일정까지 조회한다.
		wcsVo.setSchdlEndDt(durEndDt);
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> wcsList = (List<WcSchdlBVo>)commonSvc.queryList(wcsVo);
		
		//model.put("recodeCount", recodeCount);
		model.put("wcsList", wcCmSvc.getSchdlList(durStrtDt, durEndDt, wcsList));
		
		//시작일자와 종료일자를 원래일자로 돌린다.
		wcsVo.setSchdlStartDt(durStrtDt);
		//wcsVo.setSchdlEndDt(durEndDt);
		
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
				
		return LayoutUtil.getJspPath("/wc/plt/listSchdlPlt");
	}
	
	
	/** 일정 보기 */
	@RequestMapping(value = "/wc/plt/listErpSchdlPlt")
	public String listErpSchdlPlt(HttpServletRequest request,
			@RequestParam(value = "strtDt", required = false) String strtDt,
			@RequestParam(value = "viewTyp", required = false) String viewTyp,
			ModelMap model) throws Exception {
		
		try{
			if(strtDt==null || strtDt.isEmpty()) strtDt = StringUtil.getCurrYmd();
			
			model.put("strtDt", strtDt);
			
			if(viewTyp==null || viewTyp.isEmpty()) viewTyp = "month";
			
			String durStrtDt = "";
			String durEndDt = "";
			
			Object dayVo = null;
			if("month".equals(viewTyp)){
				dayVo = wrCmSvc.getMonth(strtDt);
				durStrtDt = ((WrMonthVo)dayVo).getBeforeStrtDt();
				durEndDt = ((WrMonthVo)dayVo).getAfterEndDt();
				WrMonthVo wrMonthVo = (WrMonthVo)dayVo;
				model.put("wrMonthVo", wrMonthVo);
				
				wrCmSvc.setSpclDays(wrMonthVo, durStrtDt, durEndDt);
			}else{
				// 날짜를 기준으로 주간날짜 정보 조회
				dayVo = wrCmSvc.getWeek(strtDt);
				durStrtDt = ((WrWeekVo)dayVo).getStrtDt();
				durEndDt = ((WrWeekVo)dayVo).getEndDt();
				model.put("wrWeekVo", (WrWeekVo)dayVo);
			}
			
			model.put("start", durStrtDt);
			model.put("end", durEndDt);
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/wc/plt/listErpSchdlPlt");
	}
	
	
	
			
}
