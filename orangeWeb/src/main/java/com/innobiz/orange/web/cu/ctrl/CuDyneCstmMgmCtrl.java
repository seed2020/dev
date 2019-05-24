package com.innobiz.orange.web.cu.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cu.svc.CuDyneCstmMgmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 고객관리 */
@Controller
public class CuDyneCstmMgmCtrl {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuDyneCstmMgmCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 미팅업무 처리용 서비스 */
	@Resource(name = "cuDyneCstmMgmSvc")
	private CuDyneCstmMgmSvc cuDyneCstmMgmSvc;
	
	
	/** 미팅 목록 조회 */
	@RequestMapping(value = "/cu/dyne/csmt/meet/listMeet")
	public String listMeet(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리 권한여부
		boolean isAuthMgm = SecuUtil.hasAuth(request, "A", null);
		model.put("isAdmin", isAuthMgm);
		
		return LayoutUtil.getJspPath("/cu/dyne/csmt/meet/listMeet");
	}
	
	/** [FRAME] 미팅 목록 조회 - 오른쪽 화면 */
	@RequestMapping(value = "/cu/dyne/csmt/meet/listMeetFrm")
	public String listMeetFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		                               
		return LayoutUtil.getJspPath("/cu/dyne/csmt/meet/listMeetFrm");
	}
	
	/** 조직도 트리 조회 */
	@RequestMapping(value = {"/cu/dyne/csmt/meet/treeOrgFrm", "/cu/dyne/adm/csmt/meet/treeOrgFrm"})
	public String treeOrgFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/cu/dyne/adm/");
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 관리 권한여부
		boolean isAuthMgm = SecuUtil.hasAuth(request, "A", null);
		
		// 회사ID
		String compId = userVo.getCompId();
		
		// 조직도 조회
		List<OrOrgBVo> orgList = cuDyneCstmMgmSvc.getOrgAddUserList(compId, langTypCd, userVo, true, isAdmin || isAuthMgm);
		model.put("orgList", orgList);
		
		return LayoutUtil.getJspPath("/cu/dyne/csmt/meet/treeOrgFrm");
	}
	
	/** 미팅 등록수정 */
	@RequestMapping(value = "/cu/dyne/csmt/meet/setMeet")
	public String setMeet(HttpServletRequest request,
			ModelMap model) throws Exception {
		                               
		return LayoutUtil.getJspPath("/cu/dyne/csmt/meet/setMeet", "Frm");
	}
	
	
}
