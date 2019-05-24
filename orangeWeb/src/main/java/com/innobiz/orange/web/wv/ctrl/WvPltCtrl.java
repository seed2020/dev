package com.innobiz.orange.web.wv.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wv.svc.WvSurvSvc;
import com.innobiz.orange.web.wv.vo.WvSurvBVo;

/** 설문조사 */
@Controller
public class WvPltCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private WvSurvSvc wvSurvSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
//	/** 메세지 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
//	/** 첨부파일 서비스 */
//	@Autowired
//	private WvFileSvc wvFileSvc;
	
//	/** 설문 공통 서비스 */
//	@Autowired
//	private WvCmSvc wvCmSvc;
	
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WvPltCtrl.class);
	
	/** 설문 포틀릿*/
	@RequestMapping(value = "/wv/plt/listSurvTabPlt")
	public String listSurvTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		if(tId == null || tId.isEmpty()) tId = "survId";//개인명함
		
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "survId", "hghtPx"));
		return LayoutUtil.getJspPath("/wv/plt/listSurvTabPlt");
	}
	
	
	/** 설문  프레임 */
	@RequestMapping(value= "/wv/plt/listSurvTabFrm")
	public String listSurvTabFrm(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		wvSurvBVo.setCompId(userVo.getCompId());
		wvSurvBVo.setLogUserUid(userVo.getUserUid());
		wvSurvBVo.setLogUserDeptId(userVo.getDeptId());
		//추가 - 조직ID 목록
		wvSurvBVo.setSchOrgPids(userVo.getOrgPids());
		wvSurvBVo.setAuthTgtUid("authTgtUid");
		//wvSurvBVo.setOrderBy("SURV_END_DT");
		wvSurvBVo.setLangTyp(langTypCd);
		List<String> survList=new ArrayList<String>();
		survList.add("3");  // 진행중인것만.

		wvSurvBVo.setSurvSearchList(survList);
		wvSurvBVo.setQueryLang(langTypCd);
		//wvSurvBVo.setSurvPrgStatCd("6");
		Map<String,Object> rsltMap = wvSurvSvc.getWvSurvMapList(request, wvSurvBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("rsltMapList", rsltMap.get("wvSurvBMapList"));
		model.put("logUserUid", wvSurvBVo.getLogUserUid());
		model.put("logUserDeptId", wvSurvBVo.getLogUserDeptId());
		
		return LayoutUtil.getJspPath("/wv/plt/listSurvTabFrm");
		
	}

}
