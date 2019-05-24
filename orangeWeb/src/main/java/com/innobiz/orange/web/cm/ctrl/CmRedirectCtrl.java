package com.innobiz.orange.web.cm.ctrl;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** 검색엔지 URL 외부 URL 포워드용 */
@Controller
public class CmRedirectCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 결재 문서열기 - 검색엔진 또는 외부 링크 */
	@RequestMapping(value = "/cm/ap/redirect", method = RequestMethod.GET)
	public String apForward(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "bxId", required = false) String bxId,
			@RequestParam(value = "apvNo", required = false) String apvNo,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			if(ServerConfig.IS_MOBILE){
				model.put("todo", "top.location.replace('/');");
				return LayoutUtil.getResultJsp();
			} else {
				response.sendRedirect(PtConstant.URL_LOGIN);
				return null;
			}
		}
		
		if(bxId==null || bxId.isEmpty()){
			model.put("todo", "alert('parameter(bxId) required !'); top.location.replace('/');");
			return LayoutUtil.getResultJsp();
		}
		
		if(apvNo==null || apvNo.isEmpty()){
			model.put("todo", "alert('parameter(apvNo) required !'); top.location.replace('/');");
			return LayoutUtil.getResultJsp();
		}
		
		if(ServerConfig.IS_MOBILE){
			String url = "/ap/box/viewDoc.do?apvNo="+apvNo+"&bxId="+bxId;
			String authCheckUrl = "/ap/box/listApvBx.do?bxId="+bxId;
			url = ptSecuSvc.toAuthMenuUrl(userVo, url, authCheckUrl);
			response.sendRedirect(url);
		} else {
			
			String url = "/ap/box/setDoc.do?apvNo="+apvNo+"&bxId="+bxId;
			String authCheckUrl = "/ap/box/listApvBx.do?bxId="+bxId;
			if("regRecLst".equals(bxId) || "recvRecLst".equals(bxId)){
				authCheckUrl = "/ap/box/listApvRecBx.do?bxId="+bxId;
			}
			
			// 등록대장이면
			if("regRecLst".equals(bxId)){
				
				// 저장소 조회
				String storage = apCmSvc.queryStorage(apvNo);
				
				// 진행문서기본(AP_ONGD_B) 테이블 - 조회
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setStorage(storage);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				
				if(apOngdBVo!=null){
					// 대장의 부서와 자신의 부서가 다르면
					if(apOngdBVo.getRecLstDeptId()!=null
							&& !apOngdBVo.getRecLstDeptId().equals(userVo.getDeptId())){
						
						// 전체공개문서 - 등록대장의 해당 부서문서 조회
						if("Y".equals(apOngdBVo.getAllReadYn())){
							url += "&queryString="+URLEncoder.encode("tabCd=allDept&recLstDeptId=", "UTF-8");
						// 반려된 문서
						} else if("rejt".equals(apOngdBVo.getDocProsStatCd())){
							authCheckUrl = "/ap/box/listApvBx.do?bxId=rejtBx";
						} else {
							// 결재선 조회 후 - 통보자, 결재자 구별
							
							ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
							apOngdApvLnDVo.setApvNo(apvNo);
							apOngdApvLnDVo.setStorage(storage);
							apOngdApvLnDVo.setQueryLang(apOngdBVo.getDocLangTypCd());
							@SuppressWarnings("unchecked")
							List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
							
							if(apOngdApvLnDVoList != null){
								String userUid = userVo.getUserUid();
								String deptId = userVo.getDeptId();
								
								for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
									// 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
									//		befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, 
									//		hold:보류, cncl:취소, reRevw:재검토, 
									//		inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료
//									String apvStatCd;
									
									// 통보중 - 통보함
									if("inInfm".equals(storedApOngdApvLnDVo.getApvStatCd())){
										if(userUid.equals(storedApOngdApvLnDVo.getApvrUid())){
											// 통보함
											authCheckUrl = "/ap/box/listApvBx.do?bxId=postApvdBx";
											break;
										} else if(deptId.equals(storedApOngdApvLnDVo.getApvDeptId())
												&& "Y".equals(storedApOngdApvLnDVo.getApvrDeptYn())){
											// 통보함
											authCheckUrl = "/ap/box/listApvBx.do?bxId=postApvdBx";
											break;
										}
									} else if(userUid.equals(storedApOngdApvLnDVo.getApvrUid())){
										// 완료함
										authCheckUrl = "/ap/box/listApvBx.do?bxId=apvdBx";
									}
								}
							}
						}
					}
				}
				
			}
			
			url = ptSecuSvc.toAuthMenuUrl(userVo, url, authCheckUrl);
			response.sendRedirect(url);
		}
		return null;
	}
	
	
	/** 게시판 열기 - 검색엔진 또는 외부 링크 */
	@RequestMapping(value = "/cm/bb/redirect", method = RequestMethod.GET)
	public String bbForward(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			if(ServerConfig.IS_MOBILE){
				model.put("todo", "top.location.replace('/');");
				return LayoutUtil.getResultJsp();
			} else {
				response.sendRedirect(PtConstant.URL_LOGIN);
				return null;
			}
		}
		
		if(brdId==null || brdId.isEmpty()){
			model.put("todo", "alert('parameter(brdId) required !'); top.location.replace('/');");
			return LayoutUtil.getResultJsp();
		}
		
		if(bullId==null || bullId.isEmpty()){
			model.put("todo", "alert('parameter(bullId) required !'); top.location.replace('/');");
			return LayoutUtil.getResultJsp();
		}
		
		String url = "/bb/viewBull.do?brdId="+brdId+"&bullId="+bullId;
		String authCheckUrl = "/bb/listBull.do?brdId="+brdId;
		url = ptSecuSvc.toAuthMenuUrl(userVo, url, authCheckUrl);
		response.sendRedirect(url);
		return null;
	}
	
	/** 문서 열기 - 검색엔진 또는 외부 링크 */
	@RequestMapping(value = "/cm/dm/redirect", method = RequestMethod.GET)
	public String dmForward(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "docGrpId", required = false) String docGrpId,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			if(ServerConfig.IS_MOBILE){
				model.put("todo", "top.location.replace('/');");
				return LayoutUtil.getResultJsp();
			} else {
				response.sendRedirect(PtConstant.URL_LOGIN);
				return null;
			}
		}
		
		if(docGrpId==null || docGrpId.isEmpty()){
			model.put("todo", "alert('parameter(docGrpId) required !'); top.location.replace('/');");
			return LayoutUtil.getResultJsp();
		}
		
		String url = "/dm/doc/viewDoc.do?docGrpId="+docGrpId;
		String authCheckUrl = "/dm/doc/listDoc.do";
		url = ptSecuSvc.toAuthMenuUrl(userVo, url, authCheckUrl);
		response.sendRedirect(url);
		return null;
	}
	
}
