package com.innobiz.orange.web.wd.ctrl;

import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ExcelReader;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wd.svc.WdCmSvc;
import com.innobiz.orange.web.wd.svc.WdCreSvc;
import com.innobiz.orange.web.wd.vo.WdAnbBVo;
import com.innobiz.orange.web.wd.vo.WdAnbModLVo;
import com.innobiz.orange.web.wd.vo.WdAnbUseLVo;

/** 년차관리 관리자 컨트롤 */
@Controller
public class WdAdmCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 연차 공통 서비스 */
	@Autowired
	private WdCmSvc wdCmSvc;
	
	/** 연차 생성 서비스 */
	@Autowired
	private WdCreSvc wdCreSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 업로드 메니저 */
	@Autowired
	private UploadManager uploadManager;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 환경 설정 - 조회 */
	@RequestMapping(value = "/wd/adm/setAnbPolc")
	public String setAnbPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		model.put("langTypCdList", langTypCdList);
		
		model.put("months", ApConstant.MONTHS);
		model.put("days", ApConstant.DAYS);
		
		return LayoutUtil.getJspPath("/wd/adm/setAnbPolc");
	}
	
	/** 환경 설정 - 저장 */
	@RequestMapping(value = "/wd/adm/transAnbPolc")
	public String transAnbPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{

			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(WdCmSvc.WD_SYS_PLOC+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 수동 연차 관리 - 목록 조회 */
	@RequestMapping(value = {"/wd/adm/listManlAnb", "/wd/adm/excelDownLoad"})
	public ModelAndView listManlAnb(HttpServletRequest request, HttpSession session,
			@RequestParam(value="userUids", required=false) String userUids,
			@RequestParam(value="orgIds", required=false) String orgIds,
			@RequestParam(value="year", required=false) String year,
			ModelMap model) throws Exception {
		
		boolean isExcelDownload = request.getRequestURI().indexOf("excelDownLoad") > 0;
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		String anbTypCd = "anb";
		
		// 정책 조회
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
		// 년도 기준일
		int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));
		int newYearDay   = toInt(sysPlocMap.get("newYearDay"));
		if(newYearMonth==0) newYearMonth = 1;
		if(newYearDay==0) newYearDay = 1;
		
		Calendar calendar = new GregorianCalendar();
		int currYear = calendar.get(Calendar.YEAR);
		model.put("currYear", currYear);
		
		OrUserBVo orUserBVo = new OrUserBVo();
		VoUtil.bind(request, orUserBVo);
		orUserBVo.setAduTypCd("01");//01:원직
		orUserBVo.setCompId(userVo.getCompId());
		if(userUids!=null && !userUids.isEmpty()){
			List<String> userUidList = ArrayUtil.toList(userUids, ",", true);
			if(userUidList!=null && !userUidList.isEmpty()){
				orUserBVo.setUserUidList(userUidList);
			}
		}
		int p;
		List<String> list;
		if(orgIds!=null && !orgIds.isEmpty()){
			List<String> orgIdList = new ArrayList<String>();
			for(String orgId : orgIds.split(",")){
				if((p = orgId.indexOf('|'))>0){
					orgId = orgId.substring(0, p);
					
					if(!orgIdList.contains(orgId)){
						orgIdList.add(orgId);
					}
					
					list = orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(list != null){
						for(String id : list){
							if(!orgIdList.contains(id)){
								orgIdList.add(id);
							}
						}
					}
					
				} else {
					if(!orgIdList.contains(orgId)){
						orgIdList.add(orgId);
					}
				}
			}
			orUserBVo.setOrgIdList(orgIdList);
		}
		orUserBVo.setOrderBy("RESC_NM");
		if("_".equals(orUserBVo.getUserStatCd())) orUserBVo.setUserStatCd(null);
		else if(orUserBVo.getUserStatCd() == null) orUserBVo.setUserStatCd("02");
		
		if(isExcelDownload) {
			year = year==null||year.isEmpty() ? Integer.toString(currYear) : year;
			return listManlAnbExcel(request, year, orUserBVo, langTypCd, model);
		}
		
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.countOrUserBForAnbList");
		Integer recodeCount = commonSvc.count(orUserBVo);
		model.put("recodeCount", recodeCount);
		
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBForAnbList");
		PersonalUtil.setPaging(request, orUserBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		if(orUserBVoList != null && !orUserBVoList.isEmpty()){
			List<String> odurUidList = new ArrayList<String>();
			for(OrUserBVo uVo : orUserBVoList){
				odurUidList.add(uVo.getOdurUid());
			}
			
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year==null||year.isEmpty() ? Integer.toString(currYear) : year);
			wdAnbBVo.setAnbTypCd(anbTypCd==null||anbTypCd.isEmpty() ? "anb" : anbTypCd);// anb:연차
			wdAnbBVo.setOdurUidList(odurUidList);
			@SuppressWarnings("unchecked")
			List<WdAnbBVo> wdAnbBVoList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
			if(wdAnbBVoList != null){
				for(WdAnbBVo anbVo : wdAnbBVoList){
					model.put("wdAnbBVo"+anbVo.getOdurUid(), anbVo);
				}
			}
			
			model.put("orUserBVoList", orUserBVoList);
		}
		
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		
		// 사용자상태 코드 - 겸직용/원직용
		List<PtCdBVo> userStatOrgCdList = new ArrayList<PtCdBVo>();
		
		int i, size = userStatCdList==null ? 0 : userStatCdList.size();
		String cd;
		for(i=0;i<size;i++){
			cd = userStatCdList.get(i).getCd();
			if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_ONLY, cd)){
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, cd)){
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_BOTH, cd)){
				userStatOrgCdList.add(userStatCdList.get(i));
			} else {
				userStatOrgCdList.add(userStatCdList.get(i));
			}
		}
		model.put("userStatCdList", userStatOrgCdList);
		
		
		String reloadScript = (String)session.getAttribute("WD_RELOAD_SCRIPT");
		if(reloadScript != null){
			request.setAttribute("WD_RELOAD_SCRIPT", reloadScript);
			session.removeAttribute("WD_RELOAD_SCRIPT");
		}
		
		String byUserUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/wd/adm/listUserAnb.do");
		model.put("byUserUrl", byUserUrl);
		
		return new ModelAndView(LayoutUtil.getJspPath("/wd/adm/listManlAnb"));
	}
	private static String[] colNmRescIds = {
			"cols.dept",//부서
			"cols.userNm",//사용자명
			"cols.entraYmd",//입사일
			"cols.userNm",//사용자명
			"or.cols.statCd",//상태코드
			
			"wd.cnt.cre",//발생수
			"wd.cnt.use",//사용수
			"wd.cnt.ongo",//결재중
			"wd.cnt.left",//잔여
		};
	/** 사용자 목록 다운로드 */
	private ModelAndView listManlAnbExcel(HttpServletRequest request,
			String year, OrUserBVo orUserBVo, String langTypCd,
			ModelMap model) throws SQLException, IOException, CmException {
		
		ModelAndView mv = new ModelAndView("excelDownloadView");
		
		// Sheet 명, 파일명 - wd.anbTypCd.anb=연차
		String fileName = messageProperties.getMessage("wd.anbTypCd.anb", request);
		mv.addObject("sheetName", year);
		mv.addObject("fileName", fileName+"_"+year);
		
		List<Integer> widthList = new ArrayList<Integer>();
		Integer[] widths = new Integer[]{ 5000,3000,3000,3000,3000, 2500,2500,2500,2500, 3000,2500};
		for(Integer width : widths){
			widthList.add(width);
		}
		mv.addObject("widthList", widthList);
		
		// 상단의 컬럼명
		List<String> colNmList = new ArrayList<String>();
		String colNm;
		for(String rescId : colNmRescIds){
			colNm = messageProperties.getMessage(rescId, request);
			colNmList.add(colNm);
		}
		colNmList.add("UID");
		colNmList.add("COUNT");
		mv.addObject("colNames", colNmList);
		
		// 엑셀 데이타
		Map<String,Object> colVaMap = new HashMap<String,Object>();
		List<Object> row = null;
		
		// 사용자 조회
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBForAnbList");
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		
		if(orUserBVoList!=null && !orUserBVoList.isEmpty()) {
			
			// 연차 조회
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year);
			wdAnbBVo.setAnbTypCd("anb");// anb:연차
			@SuppressWarnings("unchecked")
			List<WdAnbBVo> wdAnbBVoList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
			
			Map<Integer, WdAnbBVo> wdAnbBVoMap = new HashMap<Integer, WdAnbBVo>();
			if(wdAnbBVoList!=null && !wdAnbBVoList.isEmpty()) {
				for(WdAnbBVo vo : wdAnbBVoList) {
					wdAnbBVoMap.put(CRC32.hash(vo.getOdurUid().getBytes()), vo);
				}
			}
			
			int i = 0;
			float cre, use, ongo, forw;
			for(OrUserBVo vo : orUserBVoList){
				row = new ArrayList<Object>();
				wdAnbBVo = wdAnbBVoMap.get(CRC32.hash(vo.getOdurUid().getBytes()));
				cre=0;
				use=0;
				ongo=0;
				forw=0;
				if(wdAnbBVo != null) {
					cre = toFloat(wdAnbBVo.getCreCnt()) + toFloat(wdAnbBVo.getCreModCnt());
					use = toFloat(wdAnbBVo.getUseCnt()) + toFloat(wdAnbBVo.getUseModCnt());
					ongo = toFloat(wdAnbBVo.getOngoCnt()) + toFloat(wdAnbBVo.getOngoModCnt());
					forw = toFloat(wdAnbBVo.getForwCnt()) + toFloat(wdAnbBVo.getForwModCnt());
				}
				
				for(String rescId : colNmRescIds){
					if(rescId.endsWith("dept")){
						row.add(vo.getOrgRescNm());
					} else if(rescId.endsWith("userNm")){
						row.add(vo.getRescNm());
					} else if(rescId.endsWith("entraYmd")){
						row.add(vo.getEntraYmd());
					} else if(rescId.endsWith("statCd")){
						row.add(vo.getUserStatNm());
					} else if(rescId.endsWith("cre")){
						row.add(wdAnbBVo==null ? "" : Float.toString(cre));
					} else if(rescId.endsWith("use")){
						row.add(wdAnbBVo==null ? "" : Float.toString(use));
					} else if(rescId.endsWith("ongo")){
						row.add(wdAnbBVo==null ? "" : Float.toString(ongo));
					} else if(rescId.endsWith("left")){
						row.add(wdAnbBVo==null ? "" : Float.toString(forw+cre-use));
					}
				}
				
				row.add(vo.getOdurUid());
				row.add("");
				
				colVaMap.put("col"+i, row);
				i++;
			}
		}
		
		mv.addObject("colValues", colVaMap);
		
		return mv;
	}
	
	
	/** 연도별 관리 - 목록 조회 */
	@RequestMapping(value = "/wd/adm/listYearAnb")
	public String listYearAnb(HttpServletRequest request, HttpSession session,
			@RequestParam(value="userUids", required=false) String userUids,
			@RequestParam(value="orgIds", required=false) String orgIds,
			@RequestParam(value="year", required=false) String year,
			@RequestParam(value="anbTypCd", required=false) String anbTypCd,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 정책 조회
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
		// 입사일 기준 연차 생성
		boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
		// 년도 기준일
		int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));
		int newYearDay   = toInt(sysPlocMap.get("newYearDay"));
		if(newYearMonth==0) newYearMonth = 1;
		if(newYearDay==0) newYearDay = 1;
		
		Calendar calendar = new GregorianCalendar();
		int currYear = calendar.get(Calendar.YEAR);
		int currMonth = calendar.get(Calendar.MONTH) + 1;
		int currDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		// 회계기준일때 - 회계기준 월일이 현제 월일보다 크면 - 년도 하나 빼줌
		if(!enterBaseAnbMak){
			if(newYearMonth > currMonth || (newYearMonth==currMonth && newYearDay>currDay)){
				currYear--;
			}
		}
		model.put("currYear", currYear);
		
		OrUserBVo orUserBVo = new OrUserBVo();
		VoUtil.bind(request, orUserBVo);
		orUserBVo.setAduTypCd("01");//01:원직
		orUserBVo.setCompId(userVo.getCompId());
		if(userUids!=null && !userUids.isEmpty()){
			List<String> userUidList = ArrayUtil.toList(userUids, ",", true);
			if(userUidList!=null && !userUidList.isEmpty()){
				orUserBVo.setUserUidList(userUidList);
			}
		}
		int p;
		List<String> list;
		if(orgIds!=null && !orgIds.isEmpty()){
			List<String> orgIdList = new ArrayList<String>();
			for(String orgId : orgIds.split(",")){
				if((p = orgId.indexOf('|'))>0){
					orgId = orgId.substring(0, p);
					
					if(!orgIdList.contains(orgId)){
						orgIdList.add(orgId);
					}
					
					list = orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(list != null){
						for(String id : list){
							if(!orgIdList.contains(id)){
								orgIdList.add(id);
							}
						}
					}
					
				} else {
					if(!orgIdList.contains(orgId)){
						orgIdList.add(orgId);
					}
				}
			}
			orUserBVo.setOrgIdList(orgIdList);
		}
		orUserBVo.setOrderBy("RESC_NM");
		if("_".equals(orUserBVo.getUserStatCd())) orUserBVo.setUserStatCd(null);
		else if(orUserBVo.getUserStatCd() == null) orUserBVo.setUserStatCd("02");
		
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.countOrUserBForAnbList");
		Integer recodeCount = commonSvc.count(orUserBVo);
		model.put("recodeCount", recodeCount);
		
		orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBForAnbList");
		PersonalUtil.setPaging(request, orUserBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		if(orUserBVoList != null && !orUserBVoList.isEmpty()){
			List<String> odurUidList = new ArrayList<String>();
			for(OrUserBVo uVo : orUserBVoList){
				odurUidList.add(uVo.getOdurUid());
			}
			
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year==null||year.isEmpty() ? Integer.toString(currYear) : year);
			wdAnbBVo.setAnbTypCd(anbTypCd==null||anbTypCd.isEmpty() ? "anb" : anbTypCd);// anb:연차
			wdAnbBVo.setOdurUidList(odurUidList);
			@SuppressWarnings("unchecked")
			List<WdAnbBVo> wdAnbBVoList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
			if(wdAnbBVoList != null){
				for(WdAnbBVo anbVo : wdAnbBVoList){
					model.put("wdAnbBVo"+anbVo.getOdurUid(), anbVo);
				}
			}
			
			model.put("orUserBVoList", orUserBVoList);
		}
		
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		
		// 사용자상태 코드 - 겸직용/원직용
		List<PtCdBVo> userStatOrgCdList = new ArrayList<PtCdBVo>();
		
		int i, size = userStatCdList==null ? 0 : userStatCdList.size();
		String cd;
		for(i=0;i<size;i++){
			cd = userStatCdList.get(i).getCd();
			if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_ONLY, cd)){
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, cd)){
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_BOTH, cd)){
				userStatOrgCdList.add(userStatCdList.get(i));
			} else {
				userStatOrgCdList.add(userStatCdList.get(i));
			}
		}
		model.put("userStatCdList", userStatOrgCdList);
		
		
		String reloadScript = (String)session.getAttribute("WD_RELOAD_SCRIPT");
		if(reloadScript != null){
			request.setAttribute("WD_RELOAD_SCRIPT", reloadScript);
			session.removeAttribute("WD_RELOAD_SCRIPT");
		}
		
		String byUserUrl = ptSecuSvc.toAuthMenuUrl(userVo, "/wd/adm/listUserAnb.do");
		model.put("byUserUrl", byUserUrl);
		
		return LayoutUtil.getJspPath("/wd/adm/listYearAnb");
	}
	
	/** 사용자별 관리 - 목록 조회 */
	@RequestMapping(value = "/wd/adm/listUserAnb")
	public String listUserAnb(HttpServletRequest request, HttpSession session,
			@RequestParam(value="odurUid", required=false) String odurUid,
			ModelMap model) throws Exception {
		
		OrUserBVo orUserBVo = null;
		if(odurUid != null && !odurUid.isEmpty()){
			orUserBVo = new OrUserBVo();
			orUserBVo.setAduTypCd("01");
			orUserBVo.setUserUid(odurUid);
			orUserBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrUserBDao.selectOrUserBForAnbList");
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			model.put("orUserBVo", orUserBVo);
		}
		
		if(orUserBVo == null){
			return LayoutUtil.getJspPath("/wd/adm/listUserAnb");
		}
		
		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 정책 조회
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
//		// 입사일 기준 연차 생성
//		boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
//		// 년도 기준일
//		int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));
//		int newYearDay   = toInt(sysPlocMap.get("newYearDay"));
//		if(newYearMonth==0) newYearMonth = 1;
//		if(newYearDay==0) newYearDay = 1;
//		
//		Calendar calendar = new GregorianCalendar();
//		int currYear = calendar.get(Calendar.YEAR);
//		int currMonth = calendar.get(Calendar.MONTH) + 1;
//		int currDay = calendar.get(Calendar.DAY_OF_MONTH);
//		
//		// 회계기준일때 - 회계기준 월일이 현제 월일보다 크면 - 년도 하나 빼줌
//		if(!enterBaseAnbMak){
//			if(newYearMonth > currMonth || (newYearMonth==currMonth && newYearDay>currDay)){
//				currYear--;
//			}
//		}
		
		// 연차기본(WD_ANB_B) - 조회
		WdAnbBVo wdAnbBVo = new WdAnbBVo();
		wdAnbBVo.setOdurUid(odurUid);
		wdAnbBVo.setOrderBy("YEAR DESC, ANB_TYP_CD");
		@SuppressWarnings("unchecked")
		List<WdAnbBVo> wdAnbBVoList = (List<WdAnbBVo>)commonSvc.queryList(wdAnbBVo);
		
		int firstYear, lastYear;
		int size = wdAnbBVoList==null ? 0 : wdAnbBVoList.size();
		if(size>0){
			firstYear = Integer.parseInt(wdAnbBVoList.get(0).getYear());
			lastYear = Integer.parseInt(wdAnbBVoList.get(size-1).getYear());
			
			model.put("wdFirstYear", Integer.valueOf(firstYear));
			model.put("wdCount", Integer.valueOf(firstYear - lastYear + 1));
			
			for(WdAnbBVo vo : wdAnbBVoList){
				model.put("wd"+vo.getYear()+vo.getAnbTypCd(), vo);
			}
		}
		
		String reloadScript = (String)session.getAttribute("WD_RELOAD_SCRIPT");
		if(reloadScript != null){
			request.setAttribute("WD_RELOAD_SCRIPT", reloadScript);
			session.removeAttribute("WD_RELOAD_SCRIPT");
		}
		
		return LayoutUtil.getJspPath("/wd/adm/listUserAnb");
	}
	
	/** [AJAX] 개정연차 생성 */
	@RequestMapping(value = {"/wd/adm/processAnbAjx", "/wd/adm/processNanbAjx"})
	public String processAnbAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		int cnt = 0;
		boolean isAnb = request.getRequestURI().indexOf("processAnbAjx")>0;
		try {
			UserVo userVo = LoginSession.getUser(request);
			if(isAnb){
				
				Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
				
				// 대체 근무 이월
				if("Y".equals(sysPlocMap.get("repbNextYear"))){
					wdCreSvc.fowardWd(userVo.getCompId(), "repb");
				}
				
				cnt = wdCreSvc.createAnb(userVo.getCompId());
				
			} else {
				// 개정년차 이월
				wdCreSvc.fowardWd(userVo.getCompId(), "nanb");
				
				// 개정년차 생성
				cnt = wdCreSvc.createNanb(userVo.getCompId());
			}
			if(cnt>0){
				// cm.msg.create.successCnt={0} 건의 데이터가 생성 되었습니다.
				String message2 = messageProperties.getMessage("cm.msg.create.successCnt",
						new String[]{Integer.toString(cnt)}, request);
				model.put("message", message2);
				model.put("result", "ok");
			} else {
				// cm.msg.noData=해당하는 데이터가 없습니다.
				String message2 = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", message2);
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [팝업] 상세조회 */
	@RequestMapping(value = "/wd/adm/viewDetlPop")
	public String viewDetlPop(HttpServletRequest request,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "anbTypCd", required = false) String anbTypCd,
			@RequestParam(value = "modTypCd", required = false) String modTypCd,
			@RequestParam(value = "odurUid", required = false) String odurUid,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 정책 조회
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
		// 원직 조회
		if(odurUid != null && !odurUid.isEmpty()){
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			orOdurBVo.setQueryLang(langTypCd);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			model.put("orOdurBVo", orOdurBVo);
		}
		
		if(odurUid != null && !odurUid.isEmpty()
				&& year != null && !year.isEmpty()
				&& anbTypCd != null && !anbTypCd.isEmpty()){
			
			// 연차기본(WD_ANB_B) - 조회
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year);
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOdurUid(odurUid);
			wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
			model.put("wdAnbBVo", wdAnbBVo);
			
			// 연차차감내역(WD_ANB_MOD_L) - 조회
			WdAnbModLVo wdAnbModLVo = new WdAnbModLVo();
			wdAnbModLVo.setYear(year);
			wdAnbModLVo.setAnbTypCd(anbTypCd);
			wdAnbModLVo.setOdurUid(odurUid);
			wdAnbModLVo.setOrderBy("SEQ");
			@SuppressWarnings("unchecked")
			List<WdAnbModLVo> wdAnbModLVoList = (List<WdAnbModLVo>)commonSvc.queryList(wdAnbModLVo);
			model.put("wdAnbModLVoList", wdAnbModLVoList);
			
			// 연차사용내역(WD_ANB_USE_L) - 조회
			WdAnbUseLVo wdAnbUseLVo = new WdAnbUseLVo();
			wdAnbUseLVo.setYear(year);
			wdAnbUseLVo.setAnbTypCd(anbTypCd);
			wdAnbUseLVo.setOdurUid(odurUid);
//			wdAnbUseLVo.setCmplYn("Y");
			wdAnbModLVo.setOrderBy("USE_YMD");
			@SuppressWarnings("unchecked")
			List<WdAnbUseLVo> wdAnbUseLVoList = (List<WdAnbUseLVo>)commonSvc.queryList(wdAnbUseLVo);
			
			if(wdAnbUseLVoList!=null && !wdAnbUseLVoList.isEmpty()){
				// repb:대휴 - 경우 음수는 제거하고 보여줌 (음수는 대근 신청에 해당)
				if("repb".equals(anbTypCd)){
					List<WdAnbUseLVo> wdAnbUseLVoList2 = new ArrayList<WdAnbUseLVo>();
					for(WdAnbUseLVo vo : wdAnbUseLVoList){
						if(vo.getUseCnt()!=null && !vo.getUseCnt().startsWith("-")){
							wdAnbUseLVoList2.add(vo);
						}
					}
					model.put("wdAnbUseLVoList", wdAnbUseLVoList2);
				} else {
					model.put("wdAnbUseLVoList", wdAnbUseLVoList);
				}
			}
		}
		
		return LayoutUtil.getJspPath("/wd/adm/viewDetlPop");
	}
	
	/** [상세정보] 상세내역 차감, 사용내역 추가 - 팝업 */
	@RequestMapping(value = {"/wd/adm/setModifyPop", "/wd/adm/setModifyDelPop"})
	public String setModifyPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		boolean isDel = request.getRequestURI().indexOf("setModifyDelPop") > 0;
		
		UserVo userVo = LoginSession.getUser(request);
		// 정책 조회
		Map<String, String> sysPlocMap = wdCmSvc.getConfig(userVo.getCompId());
		model.put("sysPlocMap", sysPlocMap);
		
		if(isDel){
			return LayoutUtil.getJspPath("/wd/adm/setModifyDelPop");
		}
		return LayoutUtil.getJspPath("/wd/adm/setModifyPop");
	}
	
	/** [상세정보 - 저장] 상세내역 차감, 사용내역 추가 */
	@RequestMapping(value = "/wd/adm/transModify")
	public String transModify(HttpServletRequest request,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "anbTypCd", required = false) String anbTypCd,
			@RequestParam(value = "odurUid", required = false) String odurUid,
			@RequestParam(value = "modTypCd", required = false) String modTypCd,
			@RequestParam(value = "useYmd", required = false) String useYmd,
			@RequestParam(value = "modCnt", required = false) String modCnt,
			@RequestParam(value = "note", required = false) String note,
			ModelMap model) throws Exception {
		
		try{
			
			if(year==null || year.isEmpty()
					|| anbTypCd==null || anbTypCd.isEmpty()
					|| odurUid==null || odurUid.isEmpty()
					|| modTypCd==null || modTypCd.isEmpty()
					|| modCnt==null || modCnt.isEmpty() || "0".equals(modCnt)){
				// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
				return LayoutUtil.getResultJsp();
			}
			
			UserVo userVo = LoginSession.getUser(request);
			
			// 연차기본(WD_ANB_B) 에 - 데이터 있는지 확인
			WdAnbBVo wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year);
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOdurUid(odurUid);
			boolean hasWdAnbBVo = commonSvc.count(wdAnbBVo) > 0;
			
			// 연차사용내역(WD_ANB_USE_L) - 있는지 확인
			WdAnbUseLVo wdAnbUseLVo = new WdAnbUseLVo();
			if("use".equals(modTypCd) && !(useYmd==null || useYmd.isEmpty())){
				wdAnbUseLVo.setYear(year);
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				if(commonSvc.count(wdAnbUseLVo) > 0){
					// wd.msg.dupUseYmd=해당 일자의 사용내역이 이미 등록되어 있습니다.
					model.put("message", messageProperties.getMessage("wd.msg.dupUseYmd", request));
					return LayoutUtil.getResultJsp();
				}
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			wdAnbBVo = new WdAnbBVo();
			wdAnbBVo.setYear(year);
			wdAnbBVo.setAnbTypCd(anbTypCd);
			wdAnbBVo.setOdurUid(odurUid);
			
			// 데이터가 없으면 모든 데이터 0값 세팅
			if(!hasWdAnbBVo){
				wdAnbBVo.setForwCnt("0");
				wdAnbBVo.setForwModCnt("0");
				wdAnbBVo.setCreCnt("0");
				wdAnbBVo.setCreModCnt("0");
				wdAnbBVo.setUseCnt("0");
				wdAnbBVo.setUseModCnt("0");
				wdAnbBVo.setOngoCnt("0");
				wdAnbBVo.setOngoModCnt("0");
			}
			
			// 변수 바인딩
			if("forw".equals(modTypCd)){
				wdAnbBVo.setForwModCnt(modCnt);
			} else if("cre".equals(modTypCd)){
				wdAnbBVo.setCreModCnt(modCnt);
			} else if("use".equals(modTypCd)){
				wdAnbBVo.setUseModCnt(modCnt);
			} else if("ongo".equals(modTypCd)){
				wdAnbBVo.setOngoModCnt(modCnt);
			} else {
				// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
				return LayoutUtil.getResultJsp();
			}
			
			// 데이터가 있으면 - add
			if(hasWdAnbBVo){
				wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
				queryQueue.add(wdAnbBVo);
			} else {
				// compId
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(odurUid);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				if(orUserBVo != null) {
					wdAnbBVo.setCompId(orUserBVo.getCompId());
				}
				queryQueue.insert(wdAnbBVo);
			}
			
			// 연차사용내역(WD_ANB_USE_L) - 사용내역 입력
			if("use".equals(modTypCd) && !(useYmd==null || useYmd.isEmpty())){
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(year);
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				wdAnbUseLVo.setUseCnt(modCnt);
				wdAnbUseLVo.setCmplYn("Y");
				wdAnbUseLVo.setRegDt("sysdate");
				wdAnbUseLVo.setRson(note);
				queryQueue.insert(wdAnbUseLVo);
			}
			
			// 연차차감내역(WD_ANB_MOD_L)
			WdAnbModLVo wdAnbModLVo = new WdAnbModLVo();
			wdAnbModLVo.setYear(year);
			wdAnbModLVo.setAnbTypCd(anbTypCd);
			wdAnbModLVo.setOdurUid(odurUid);
			wdAnbModLVo.setModTypCd(modTypCd+"Mod");
			wdAnbModLVo.setModCnt(modCnt);
			if("use".equals(modTypCd) && !(useYmd==null || useYmd.isEmpty())){
				wdAnbModLVo.setUseYmd(useYmd);
			}
			wdAnbModLVo.setModUid(userVo.getUserUid());
			wdAnbModLVo.setModDt("sysdate");
			wdAnbModLVo.setNote(note);
			queryQueue.insert(wdAnbModLVo);
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if("use".equals(modTypCd) && (useYmd==null || useYmd.isEmpty())) modTypCd = "cre";//상세내역 탭 열리도록 - 사용내역 탭 아닌
			request.getSession().setAttribute("WD_RELOAD_SCRIPT", "viewDetlPop('"+year+"', '"+anbTypCd+"', '"+odurUid+"', '"+modTypCd+"');");
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [상세정보 - 저장] 사용내역 삭제 */
	@RequestMapping(value = "/wd/adm/transModifyDel")
	public String transModifyDel(HttpServletRequest request,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "anbTypCd", required = false) String anbTypCd,
			@RequestParam(value = "odurUid", required = false) String odurUid,
//			@RequestParam(value = "modTypCd", required = false) String modTypCd,
			@RequestParam(value = "useYmds", required = false) String strUseYmds,
			@RequestParam(value = "note", required = false) String note,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			UserVo userVo = LoginSession.getUser(request);
			
			if(year==null || year.isEmpty()
					|| anbTypCd==null || anbTypCd.isEmpty()
					|| odurUid==null || odurUid.isEmpty()
					|| strUseYmds==null || strUseYmds.isEmpty()){
				// cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
				return LayoutUtil.returnJson(model, message);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			int delCnt = 0;
			
			boolean isCmpl;
			WdAnbBVo wdAnbBVo;
			WdAnbUseLVo wdAnbUseLVo;
			WdAnbModLVo wdAnbModLVo;
			
			for(String useYmd : strUseYmds.split(",")){
				
				// 연차사용내역(WD_ANB_USE_L) - 조회
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(year);
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				wdAnbUseLVo = (WdAnbUseLVo)commonSvc.queryVo(wdAnbUseLVo);
				// 없으면 건너뜀
				if(wdAnbUseLVo == null){
					continue;
				}
				
				// 완료 여부 - 완료되었으면:사용차감, 진행중이면:결재중차감
				isCmpl = !"N".equals(wdAnbUseLVo.getCmplYn());
				
				// 연차기본(WD_ANB_B) - 사용차감수
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(year);
				wdAnbBVo.setAnbTypCd(anbTypCd);
				wdAnbBVo.setOdurUid(odurUid);
				if(isCmpl){
					// 완료 - 사용 차감
					wdAnbBVo.setUseModCnt("-"+wdAnbUseLVo.getUseCnt());
				} else {
					// 미완료 - 결재중 차감
					wdAnbBVo.setOngoModCnt("-"+wdAnbUseLVo.getUseCnt());
				}
				wdAnbBVo.setInstanceQueryId("com.innobiz.orange.web.wd.dao.WdAnbBDao.addWdAnbB");
				queryQueue.add(wdAnbBVo);
				
				// 연차차감내역(WD_ANB_MOD_L) - 입력
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(year);
				wdAnbModLVo.setAnbTypCd(anbTypCd);
				wdAnbModLVo.setOdurUid(odurUid);
				if(isCmpl){
					wdAnbModLVo.setModTypCd("useMod");
				} else {
					wdAnbModLVo.setModTypCd("ongoMod");
				}
				
				wdAnbModLVo.setModCnt("-"+wdAnbUseLVo.getUseCnt());		// 연차사용내역 의 사용수
				wdAnbModLVo.setUseYmd(wdAnbUseLVo.getUseYmd());			// 연차사용내역 의 사용년월일
				if(wdAnbUseLVo.getApvNo()!=null && !wdAnbUseLVo.getApvNo().isEmpty()){
					wdAnbModLVo.setRson("["+wdAnbUseLVo.getApvNo()+"] "+wdAnbUseLVo.getRson());
				} else {
					wdAnbModLVo.setRson(wdAnbUseLVo.getRson());
				}
				wdAnbModLVo.setRson(wdAnbUseLVo.getRson());				// 연차사용내역 의 근거
				
				wdAnbModLVo.setModUid(userVo.getUserUid());
				wdAnbModLVo.setModDt("sysdate");
				wdAnbModLVo.setNote(note);
				queryQueue.insert(wdAnbModLVo);
				
				// 연차사용내역(WD_ANB_USE_L) - 삭제
				wdAnbUseLVo = new WdAnbUseLVo();
				wdAnbUseLVo.setYear(year);
				wdAnbUseLVo.setAnbTypCd(anbTypCd);
				wdAnbUseLVo.setOdurUid(odurUid);
				wdAnbUseLVo.setUseYmd(useYmd);
				queryQueue.delete(wdAnbUseLVo);
				
				commonSvc.execute(queryQueue);
				queryQueue.removeAll();
				delCnt++;
			}
			
			if(delCnt == 0){
				// cm.msg.del.noData=삭제할 데이터가 없습니다.
				String message2 = messageProperties.getMessage("cm.msg.del.noData", request);
				model.put("message", message2);
			} else {
				// cm.msg.del.successCnt={0} 건의  데이터가 삭제 되었습니다.
				String message2 = messageProperties.getMessage("cm.msg.del.successCnt", new String[]{Integer.toString(delCnt)}, request);
				model.put("message", message2);
				request.getSession().setAttribute("WD_RELOAD_SCRIPT", "viewDetlPop('"+year+"', '"+anbTypCd+"', '"+odurUid+"', 'use');");
				model.put("todo", "parent.reload();");
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [상세정보] 상세내역 차감, 사용내역 추가 - 팝업 */
	@RequestMapping(value = "/wd/adm/setInitDataPop")
	public String setInitDataPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wd/adm/setInitDataPop");
	}
	
	/** [수동 연차 관리 - 수정] - 팝업 */
	@RequestMapping(value = "/wd/adm/setManlModifyPop")
	public String setManlModifyPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wd/adm/setManlModifyPop");
	}
	
	/** [수동 연차 관리 - 엑셀업로드] - 팝업 */
	@RequestMapping(value = "/wd/adm/setExcelPop")
	public String setExcelPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/wd/adm/setExcelPop");
	}
	
	/** [AJAX] 전체 데이터 삭제 */
	@RequestMapping(value = "/wd/adm/processClearDataAjx")
	public String processClearDataAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			
			commonSvc.delete(new WdAnbBVo());
			commonSvc.delete(new WdAnbModLVo());
			commonSvc.delete(new WdAnbUseLVo());
			
			// cm.msg.allDel.success=전체삭제 되었습니다.
			String message2 = messageProperties.getMessage("cm.msg.allDel.success", request);
			model.put("message", message2);
			model.put("result", "ok");
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	
	/** 수동연차관리 - 엑셀 업로드 */
	@RequestMapping(value = "/wd/adm/transExcel")
	public String transExcelUpload(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);

		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			String year = uploadHandler.getParamMap().get("year");
			
			File file = fileMap.get("excel");
			if(file==null || !file.isFile()){
				// cm.msg.noFileSelected=선택한 파일이 없습니다.
				throw new CmException("cm.msg.noFileSelected", locale);
			}
			
			List<String[][]> list = ExcelReader.readToList(file, new int[]{0});
			String[][] sheet = list==null || list.isEmpty() ? null : list.get(0);
			int i, indexUID=-1, indexCOUNT=-1;
			if(sheet!=null && sheet.length>0) {
				for(i=0; i<sheet[0].length; i++) {
					if("UID".equals(sheet[0][i])) {
						indexUID = i;
					} else if("COUNT".equals(sheet[0][i])) {
						indexCOUNT = i;
					}
				}
			}
			
			if(indexUID<0 || indexCOUNT<0) {
				// wd.msg.notPropExcel=올바른 엑셀 파일이 아닙니다.
				model.put("message", messageProperties.getMessage("wd.msg.notPropExcel", request));
				return LayoutUtil.getResultJsp();
			}
			
			WdAnbBVo wdAnbBVo;
			WdAnbModLVo wdAnbModLVo;
			QueryQueue queryQueue;
			
			String odurUid, creCnt;
			for(i=1; i<sheet.length; i++) {
				odurUid = sheet[i][indexUID];
				creCnt  = sheet[i][indexCOUNT];
				
				if(odurUid==null || odurUid.isEmpty()) {
					continue;
				}
				if(creCnt==null || creCnt.isEmpty()) {
					creCnt = "0";
				}
				
				queryQueue = new QueryQueue();
				
				// 발생수-엑셀 수치, 발생차감수 0 로 저장함
				wdAnbBVo = new WdAnbBVo();
				wdAnbBVo.setYear(year);
				wdAnbBVo.setAnbTypCd("anb");
				wdAnbBVo.setOdurUid(odurUid);
				//compId
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(odurUid);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				if(orUserBVo != null) {
					wdAnbBVo.setCompId(orUserBVo.getCompId());
				}
				wdAnbBVo.setCreCnt(creCnt);//발생수 - 엑셀상의 수치
				wdAnbBVo.setCreModCnt("0");//발생차감수
				queryQueue.store(wdAnbBVo);
				
				// 발생로그 삭제
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(year);
				wdAnbModLVo.setAnbTypCd("anb");
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("cre");
				queryQueue.delete(wdAnbModLVo);
				
				// 발생차감로그 삭제
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(year);
				wdAnbModLVo.setAnbTypCd("anb");
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("creMod");
				queryQueue.delete(wdAnbModLVo);
				
				// 발생로그 - 엑셀로 남김
				wdAnbModLVo = new WdAnbModLVo();
				wdAnbModLVo.setYear(year);
				wdAnbModLVo.setAnbTypCd("anb");
				wdAnbModLVo.setOdurUid(odurUid);
				wdAnbModLVo.setModTypCd("cre");
				wdAnbModLVo.setModCnt(creCnt);
				wdAnbModLVo.setModUid(userVo.getUserUid());
				wdAnbModLVo.setModDt("sysdate");
				wdAnbModLVo.setNote("Excel");
				queryQueue.insert(wdAnbModLVo);
				
				commonSvc.execute(queryQueue);
				
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
		} catch(CmException e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		} catch(NullPointerException e){
			e.printStackTrace();
			model.put("message", "[Excel Format Error] - Download sample file and fill the data !");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	private int toInt(String no){
		if(no==null || no.isEmpty()) return 0;
		return Integer.parseInt(no);
	}
	private float toFloat(String no){
		if(no==null || no.isEmpty()) return 0;
		return Float.parseFloat(no);
	}
}
