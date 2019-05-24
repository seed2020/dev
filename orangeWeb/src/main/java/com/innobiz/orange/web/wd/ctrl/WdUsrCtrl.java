package com.innobiz.orange.web.wd.ctrl;


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wd.svc.WdCmSvc;
import com.innobiz.orange.web.wd.vo.WdAnbBVo;
import com.innobiz.orange.web.wd.vo.WdAnbUseLVo;

@Controller
public class WdUsrCtrl {

	@Autowired
	private WdCmSvc wdCmSvc;
	
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 연차 포틀릿 */
	@RequestMapping(value = "/wd/plt/usrAnbPlt")
	public String listApvBxPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			@Parameter(name="anbTypCd", required=false) String anbTypCd,
			@Parameter(name="year", required=false) String year,
			ModelMap model, Locale locale) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 
		
		UserVo userVo = LoginSession.getUser(request);
		
		Map<String, String> sysPlocMap = ptSysSvc.getSysSetupMap(WdCmSvc.WD_SYS_PLOC+userVo.getCompId(), true);
		
		int intYear = (year==null || year.isEmpty()) ?
				wdCmSvc.getAnbYear(null, userVo.getOdurUid(), sysPlocMap, locale)
				: Integer.parseInt(year);
		model.put("year", Integer.valueOf(intYear));
		if(anbTypCd==null || anbTypCd.isEmpty()) anbTypCd = "anb";
		
		WdAnbBVo wdAnbBVo = new WdAnbBVo();
		wdAnbBVo.setYear(Integer.toString(intYear));
		wdAnbBVo.setAnbTypCd(anbTypCd);
		wdAnbBVo.setOdurUid(userVo.getOdurUid());
		wdAnbBVo = (WdAnbBVo)commonSvc.queryVo(wdAnbBVo);
		model.put("wdAnbBVo", wdAnbBVo);
		
		return LayoutUtil.getJspPath("/wd/plt/usrAnbPlt");
	}
	
	/** [수동 연차 관리 - 엑셀업로드] - 팝업 */
	@RequestMapping(value = "/wd/plt/listAnbDetlPop")
	public String listAnbDetlPop(HttpServletRequest request,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "anbTypCd", required = false) String anbTypCd,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(anbTypCd==null || anbTypCd.isEmpty()) anbTypCd = "anb";
		
		WdAnbUseLVo wdAnbUseLVo = new WdAnbUseLVo();
		wdAnbUseLVo.setYear(year);
		wdAnbUseLVo.setAnbTypCd(anbTypCd);
		wdAnbUseLVo.setOdurUid(userVo.getOdurUid());
		wdAnbUseLVo.setCmplYn("Y");
		wdAnbUseLVo.setOrderBy("USE_YMD DESC");
		@SuppressWarnings("unchecked")
		List<WdAnbUseLVo> wdAnbUseLVoList = (List<WdAnbUseLVo>)commonSvc.queryList(wdAnbUseLVo);
		if(wdAnbUseLVoList!=null && !wdAnbUseLVoList.isEmpty()) {
			model.put("wdAnbUseLVoList", wdAnbUseLVoList);
		}
		
		return LayoutUtil.getJspPath("/wd/plt/listAnbDetlPop");
	}
}
