package com.innobiz.orange.mobile.or.ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;

@Controller
public class MoOrOrgCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoOrOrgCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;


	/** 사용자 조회 */
	@RequestMapping(value = "/or/user/viewUserPop")
	public String viewUserPop(HttpServletRequest request,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model, Locale locale) throws Exception {
		
		if(userUid==null || userUid.isEmpty()){
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			String msg = messageProperties.getMessage("pt.login.noUser", locale);
			LOGGER.error("fail to view user - empty userUid "+ msg);
			return JsonUtil.returnJson(model, msg);
		}
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		if(userUid!=null) orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		
		if(orUserBVo==null){
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			String msg = messageProperties.getMessage("pt.login.noUser", request);
			LOGGER.error("fail to view user - no user - userUid:"+userUid+"  "+msg);
			return JsonUtil.returnJson(model, msg);
		}
		model.put("orUserBVo", orUserBVo);
		
		String odurUid = orUserBVo.getOdurUid();//원직자UID
		
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(odurUid);
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
		
		if(orOdurBVo==null){
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			String msg = messageProperties.getMessage("pt.login.noUser", request);
			LOGGER.error("fail to view user - no user - userUid:"+userUid+"  odurUid:"+odurUid+"  "+msg);
			return JsonUtil.returnJson(model, msg);
		}
		model.put("orOdurBVo", orOdurBVo);
		
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		// 겸직자 이미지 조회
		orUserImgDVo.setUserUid(userUid);
		orUserImgDVo.setUserImgTypCd("03");// 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
		orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
		
		if(orUserImgDVo!=null){
			model.put("orUserImgDVo", orUserImgDVo);
		// 원직자와 겸직자가 다른 경우
		} else if(!odurUid.equals(userUid)){
			orUserImgDVo = new OrUserImgDVo();
			orUserImgDVo.setUserUid(odurUid);
			orUserImgDVo.setUserImgTypCd("03");// 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
			orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
			
			if(orUserImgDVo!=null){
				model.put("orUserImgDVo", orUserImgDVo);
			}
		}
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(odurUid);
		orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
		if(orUserPinfoDVo!=null){
			orUserPinfoDVo = orCmSvc.getDecryptUserPinfo(orUserPinfoDVo);// 복호화
			if(orUserPinfoDVo != null){
				model.put("orUserPinfoDVo", orUserPinfoDVo);
			}
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 회사명 표시 (사용자 상세 정보)
		if("Y".equals(sysPlocMap.get("showCompNmEnable"))){
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(orUserBVo.getCompId(), langTypCd);
			if(ptCompBVo != null){
				model.put("compNm", ptCompBVo.getRescNm());
				model.put("showCompNmEnable", Boolean.TRUE);
			}
		}
		// 자택 정보 표시 안함 (사용자 상세 정보)
		if("Y".equals(sysPlocMap.get("showHomeInfoDisable"))){
			model.put("showHomeInfoDisable", Boolean.TRUE);
		}
		// 사번 표시 안함
		if("Y".equals(sysPlocMap.get("blockEinEnable"))){
			model.put("blockEinEnable", Boolean.TRUE);
		}
		return MoLayoutUtil.getJspPath("/or/user/viewUserPop");
	}
	/** 임직원 조회 - 빈 화면 / ajax로 사용자 조회함 */
	@RequestMapping(value = {"/or/user/searchUser", "/or/user/selectUser"})
	public String searchUser(HttpServletRequest request,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
		// 조직도 트리의 아이콘 조회 : 회사,기관,부서,파트
		model.put("iconTitle", orCmSvc.getOrgTreeIcon(langTypCd));
		
		String uri = request.getRequestURI();
		if(uri.indexOf("searchUser")>0){
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if("Y".equals(sysPlocMap.get("globalOrgChartEnable"))){
				request.setAttribute("globalOrgChartEnable", Boolean.TRUE);
			}
			
			return MoLayoutUtil.getJspPath("/or/user/searchUser", "empty");
		} else if(uri.indexOf("selectUser")>0){
			
			if(request.getParameter("apvLnOpt") != null){
				// 결재 용어설정을 Model 에 설정 - key : apTermList
				apCmSvc.setApTermList(model, LoginSession.getLocale(request));
				// 옵션설정을 JSON 형태로 Model 에 설정
				apCmSvc.setOptConfigJson(model, userVo.getCompId());
				// 부재사유코드
				List<PtCdBVo> absRsonCdList = ptCmSvc.getCdList("ABS_RSON_CD", langTypCd, "Y");
				model.put("absRsonCdList", absRsonCdList);
			}
			
			return MoLayoutUtil.getJspPath("/or/user/selectUser", "empty");
		}
		return null;
	}
	/** [AJAX] 임직원 목록 조회 - 기본 조직도의 경우 */
	@RequestMapping(value = {"/or/user/listUserAjx", "/or/user/selectUserAjx"})
	public String listUserAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		String uri = request.getRequestURI();
		boolean isSelection = uri.indexOf("selectUserAjx") > 0;
		
		try {
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String orgId = (String)jsonObject.get("orgId");
			String userNm = (String)jsonObject.get("userNm");
			String selection = (String)jsonObject.get("selection");
			String mdRid = (String)jsonObject.get("mdRid");
			if(selection!=null && !selection.isEmpty()){
				model.put("selection", selection);//for UI - radio/checkbox
			}
			
			// 조직도 클릭에 의한 검색
			if(!(orgId==null || orgId.isEmpty())){
				
				String userStatCd = (String)jsonObject.get("userStatCd");
				
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo.setOrgId(orgId);
				
				// selection - null:일반 조직도, single:한명선택, multi:여러명 선택
				//    일반조직도의 경우 - 02:근무중, 03:휴직, 04:정직 보여줌
				//    사용자 선택의 경우(single:한명선택, multi:여러명 선택) - 02:근무중 만 보여줌
				if("02".equals(userStatCd) || "single".equals(selection) || "multi".equals(selection)){
					orUserBVo.setUserStatCd("02");
				} else {
					orUserBVo.setUserStatCdList(orCmSvc.getViewUserStatCdList());
				}
				
				// 리스트 환경 설정 - 세팅
				List<PtLstSetupDVo> ptLstSetupDVoList = orCmSvc.setListQueryOptions(orUserBVo, "OR_ORGC");
				if(ptLstSetupDVoList != null){
					@SuppressWarnings("unchecked")
					List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
					List<Map<String, Object>> orUserMapList = toUserMap(orUserBVoList);
					if(orUserMapList!=null){
						model.put("orUserMapList", orUserMapList);
					}
				}
				
				if(isSelection){
					return MoLayoutUtil.getJspPath("/or/user/selectUserAjx");
				} else {
					return MoLayoutUtil.getJspPath("/or/user/listUserAjx");
				}
				
			} else if(!(userNm==null || userNm.isEmpty())){
				
				//String selection = (String)jsonObject.get("selection");
				String userStatCd = (String)jsonObject.get("userStatCd");
				String compId = (String)jsonObject.get("compId");
				String downward = (String)jsonObject.get("downward");
				String oneDeptId = (String)jsonObject.get("oneDeptId");
				
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo.setRescNm(userNm);
				if(compId!=null) orUserBVo.setCompId(compId);
				if("02".equals(userStatCd)){
					orUserBVo.setUserStatCd(userStatCd);
				} else {
					orUserBVo.setUserStatCdList(orCmSvc.getViewUserStatCdList());
				}
				
				// 하위부서 가 있으면
				if(downward!=null && !downward.isEmpty()){
					// 해당 하위부서 목록을 구해옴
					List<String> subOrgList = orCmSvc.getOrgSubIdList(downward, langTypCd);
					subOrgList.add(downward);
					orUserBVo.setOrgIdList(subOrgList);//조직ID 목록
				} else if(oneDeptId!=null && !oneDeptId.isEmpty()){
					orUserBVo.setDeptId(oneDeptId);
				}

				// 결재의 사용자 검색 - 결재라인 세팅의
				if("AP".equals(mdRid)){
					// 현재 oneDeptId 가 넘어와 한 부서로 제한 되므로 추가 작업 불필요 
					
				// 일반 사용자 검색
				} else {
					// 시스템 정책 조회
					Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
					// 타회사 임직원 검색 안함
					if("Y".equals(sysPlocMap.get("searchUserOnOtherCompDisable"))){
						UserVo userVo = LoginSession.getUser(request);
						orUserBVo.setCompId(userVo.getCompId());
					}
				}
				
				// 레코드 수 조회
				Integer recodeCount = commonSvc.count(orUserBVo);
				PersonalUtil.setPaging(request, orUserBVo, recodeCount);
				
				// 리스트 환경 설정 - 세팅
				List<PtLstSetupDVo> ptLstSetupDVoList = orCmSvc.setListQueryOptions(orUserBVo, "OR_SRCH");
				if(ptLstSetupDVoList != null){
					
					// 목록 조회
					@SuppressWarnings("unchecked")
					List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
					
					List<Map<String, Object>> orUserMapList = toUserMap(orUserBVoList);
					if(orUserMapList!=null){
						model.put("recodeCount", recodeCount);
						model.put("orUserMapList", orUserMapList);
					}
				}
				if(isSelection){
					return MoLayoutUtil.getJspPath("/or/user/selectUserAjx");
				} else {
					return MoLayoutUtil.getJspPath("/or/user/listUserAjx");
				}
			} else {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("Search user by [no orgId] and [no userNm]"+" - "+message);
				return JsonUtil.returnJson(model, message);
			}
			
		} catch(Exception e) {
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** [프레임] 조직도 - 트리(좌측) */
	@RequestMapping(value = {"/or/user/treeOrgAjx"})
	public String treeOrgAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
//			@Parameter(name="multi", required=false) String multi,
//			@Parameter(name="compId", required=false) String compId,// 회사관리에서 다른회사의 관리자 지정할때만 넘어옴
//			@Parameter(name="global", required=false) String global,// global=Y : 전사 조직도 보여줌
//			@Parameter(name="upward", required=false) String upward,// 상위 부서 선택 시작 조직ID
//			@Parameter(name="downward", required=false) String downward,// 하위 부서 선택 시작 조직ID
//			@Parameter(name="oneDeptId", required=false) String oneDeptId,// 하나의 부서 - 조직도에 하나의 부서만 표시 할 때
//			@Parameter(name="orgId", required=false) String orgId,
			ModelMap model) throws Exception {
		
		try {
			JSONObject jsonObject = (data==null || data.isEmpty()) 
					? new JSONObject(): (JSONObject)JSONValue.parse(data);
//			String selection = (String)jsonObject.get("selection");
			String compId = (String)jsonObject.get("compId");
			String global = (String)jsonObject.get("global");
			String upward = (String)jsonObject.get("upward");
			String downward = (String)jsonObject.get("downward");
			String oneDeptId = (String)jsonObject.get("oneDeptId");
//			String orgId = (String)jsonObject.get("orgId");
			

			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			UserVo userVo = LoginSession.getUser(request);
			
//			if(model.get("callback")==null && request.getRequestURI().startsWith("/ap/box/")){
//				model.put("callback","openApUserListFrm");// 결재용 조직도 클릭시 callback 함수 세팅
//			}
			
			// 조직도 트리의 아이콘 조회 : 회사,기관,부서,파트
//			model.put("iconTitle", orCmSvc.getOrgTreeIcon(langTypCd));
//			if("multi".equals(selection)){
//				model.put("treeSelectOption", "2");//트리 javascript 에서 여러개 선택 되도록 함.
//				model.put("processSelect", Boolean.TRUE);
//			}
			
			if(upward != null && !upward.isEmpty()){
				// 상위 조직도 목록
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getUpTreeList(upward, langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				model.put("openLvl", new Integer(100));
			} else if(downward != null && !downward.isEmpty()){
				// 하위 조직도 목록
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getDownTreeList(downward, langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				model.put("openLvl", new Integer(100));
			} else if(oneDeptId != null && !oneDeptId.isEmpty()){
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getOneTreeList(oneDeptId, langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				model.put("openLvl", new Integer(5));
			} else {
				
				// 전체 조직도 목록
				
				/* global=Y : 전사 조직도 보여줌
				 * compId 파라미터 있으면 : 파라미터 회사의 조직도 보여줌
				 * compId 파라미터 없으면 : 자기소속 회사의 조직도 보여줌
				 * */
				String queryCompId = "Y".equals(global) ? null : compId!=null ? compId : userVo.getCompId();
				
				// 조직도 트리 조회
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getOrgTreeList(queryCompId, "Y", langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				
//				if(model.get("selectedOrgId")==null){
//					// 초기 부서 설정
//					if(compId!=null && orOrgBVoList!=null && !orOrgBVoList.isEmpty()){
//						for(OrOrgBVo orOrgBVo : orOrgBVoList){
//							if("ROOT".equals(orOrgBVo.getOrgPid())){
//								model.put("selectedOrgId", orOrgBVo.getOrgId());
//								break;
//							}
//						}
//					}
//				}
			}
			
			return JsonUtil.returnJson(model);
			
		} catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.returnJson(model, e.getMessage());
		}
	}
	
	/** 사용자 정보 맵으로 전환 */
	private List<Map<String, Object>> toUserMap(List<OrUserBVo> orUserBVoList) throws CmException, SQLException, IOException {
		
		if(orUserBVoList==null || orUserBVoList.isEmpty()) return null;
		
		// 개인정보 맵 조회
		Map<Integer, Map<String, Object>> userPsnInfoMap = orCmSvc.queryUserPsnInfoMap(orUserBVoList);
		
		Map<String, Object> pInfo, uInfo;
		List<Map<String, Object>> orUserMapList = new ArrayList<Map<String, Object>>();
		
		for(OrUserBVo storedOrUserBVo :orUserBVoList){
			
			uInfo = new HashMap<String, Object>();
			uInfo.put("userUid", storedOrUserBVo.getUserUid());
			uInfo.put("odurUid", storedOrUserBVo.getOdurUid());
			uInfo.put("rescNm", storedOrUserBVo.getRescNm());
			uInfo.put("compId", storedOrUserBVo.getCompId());
			uInfo.put("orgId", storedOrUserBVo.getOrgId());
			uInfo.put("orgRescNm", storedOrUserBVo.getOrgRescNm());
			uInfo.put("deptId", storedOrUserBVo.getDeptId());
			uInfo.put("deptRescNm", storedOrUserBVo.getDeptRescNm());
			uInfo.put("positCd", storedOrUserBVo.getPositCd());
			uInfo.put("positNm", storedOrUserBVo.getPositNm());
			uInfo.put("titleCd", storedOrUserBVo.getTitleCd());
			uInfo.put("titleNm", storedOrUserBVo.getTitleNm());
			uInfo.put("userStatCd", storedOrUserBVo.getUserStatCd());
			uInfo.put("userStatNm", storedOrUserBVo.getUserStatNm());
			uInfo.put("tichCont", storedOrUserBVo.getTichCont());
			
			pInfo = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
			if(pInfo!=null){
				uInfo.put("mbno", pInfo.get("mbno"));
				uInfo.put("compPhon", pInfo.get("compPhon"));
				uInfo.put("email", pInfo.get("email"));
			}
			
			orUserMapList.add(uInfo);
		}
		return orUserMapList;
	}
	/** 조직도 조회 - 빈 화면 / ajax로 조직도 조회함 */
	@RequestMapping(value = "/or/org/selectOrg")
	public String selectOrg(HttpServletRequest request,
			ModelMap model, Locale locale) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// js - include 옵션
		model.put("JS_OPTS", new String[]{"tree"});
		// 조직도 트리의 아이콘 조회 : 회사,기관,부서,파트
		model.put("iconTitle", orCmSvc.getOrgTreeIcon(langTypCd));
		
		return MoLayoutUtil.getJspPath("/or/org/selectOrg", "empty");
	}
}
