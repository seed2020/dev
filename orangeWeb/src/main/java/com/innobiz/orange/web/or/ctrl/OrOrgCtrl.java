package com.innobiz.orange.web.or.ctrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOdurSecuDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.admCtrl.PtOrgCtrl;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.SysSetupUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;

/** 조직 사용자 컨트롤러 */
@Controller
public class OrOrgCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(OrOrgCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 사용자 조직 관리 */
	@Autowired
	private PtOrgCtrl ptOrgCtrl;
	
	/** [팝업] 조직도 - 세팅용 */
	@RequestMapping(value = "/or/org/searchOrgPop")
	public String selectOrgPop(HttpServletRequest request,
			@Parameter(name="multi", required=false) String multi,
			@Parameter(name="upward", required=false) String upward,// 상위 부서 선택 시작 조직ID
			@Parameter(name="downward", required=false) String downward,// 하위 부서 선택 시작 조직ID
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="global", required=false) String global,
			@Parameter(name="withDel", required=false) String withDel,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		boolean appended = false;
		StringBuilder builder = new StringBuilder();
		if("Y".equals(multi)){
			builder.append("?multi=Y");
			appended = true;
		}
		if(upward!=null){
			builder.append(appended ? '&' : '?').append("upward=").append(upward);
			appended = true;
		} else if(downward!=null){
			builder.append(appended ? '&' : '?').append("downward=").append(downward);
			appended = true;
		} else if(global!=null){
			builder.append(appended ? '&' : '?').append("global=").append(global);
			appended = true;
		}
		if(orgId!=null){
			builder.append(appended ? '&' : '?').append("orgId=").append(orgId);
			appended = true;
		}
		if(withDel!=null){
			builder.append(appended ? '&' : '?').append("withDel=").append(withDel);
			appended = true;
		}
		if(compId!=null){
			builder.append(appended ? '&' : '?').append("compId=").append(compId);
			appended = true;
		}
		model.put("urlParam", builder.toString());
		
		if("Y".equals(multi)){
			return LayoutUtil.getJspPath("/or/org/searchOrgMultiPop");
		} else {
			return LayoutUtil.getJspPath("/or/org/searchOrgPop");
		}
	}
	
	/** [팝업] 조직도 - 조회용(선택된 리스트) */
	@RequestMapping(value = "/or/org/viewOrgPop")
	public String viewOrgPop(HttpServletRequest request,
			@Parameter(name="multi", required=false) String multi,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/or/org/viewOrgPop");
	}
	/** [프레임] 조직도 - 트리(좌측) - 조직 선택의 트리, 디폴트 자기부서 선택 안되도록 함 */
	@RequestMapping(value = "/or/org/selectOrgTreeFrm")
	public String selectOrgTreeFrm(HttpServletRequest request,
			@Parameter(name="multi", required=false) String multi,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="global", required=false) String global,
			@Parameter(name="upward", required=false) String upward,
			@Parameter(name="downward", required=false) String downward,
			@Parameter(name="oneDeptId", required=false) String oneDeptId,
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="mode", required=false) String mode,// foreign : 대외 조직도 - 결재경로의 옵션에 의해 대외 조직도가 표시 될 경우
			@Parameter(name="brother", required=false) String brother,
			@Parameter(name="withDel", required=false) String withDel,//삭제 조직 포함
			ModelMap model) throws Exception {
		model.put("callback", "setOrgVo");
		return treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, mode, brother, withDel, model);
	}
	
	/** [프레임] 조직도 - 트리(좌측) */
	@RequestMapping(value = {"/or/org/treeOrgFrm", "/cm/demo/treeOrgFrm",
			"/ap/box/treeOrgFrm", "/ap/adm/box/treeOrgFrm"})
	public String treeOrgFrm(HttpServletRequest request,
			@Parameter(name="multi", required=false) String multi,
			@Parameter(name="compId", required=false) String compId,// 회사관리에서 다른회사의 관리자 지정할때만 넘어옴
			@Parameter(name="global", required=false) String global,// global=Y : 전사 조직도 보여줌
			@Parameter(name="upward", required=false) String upward,// 상위 부서 선택 시작 조직ID
			@Parameter(name="downward", required=false) String downward,// 하위 부서 선택 시작 조직ID
			@Parameter(name="oneDeptId", required=false) String oneDeptId,// 하나의 부서 - 조직도에 하나의 부서만 표시 할 때
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="mode", required=false) String mode,// foreign : 대외 조직도 - 결재경로의 옵션에 의해 대외 조직도가 표시 될 경우
			@Parameter(name="brother", required=false) String brother,//상위 조직도에서 형제 조직까지 포함 여부
			@Parameter(name="withDel", required=false) String withDel,//삭제 조직 포함
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		String uri = request.getRequestURI();
		// 데모 로그인용 - 조직도 보이기
		if(uri.startsWith("/cm/demo")){
			if(!SysSetupUtil.isDemoSite()){
				model.put("todo", "top.location.href = '/';");
				return LayoutUtil.getResultJsp();
			}
			global = "Y";
			model.put("openLvl", "100");
			model.put("callback","openDemoUser");
		}
		// 데모 로그인용
		
		if(model.get("callback")==null && (uri.startsWith("/ap/box/") || uri.startsWith("/ap/adm/box/"))){
			model.put("callback","openApUserListFrm");// 결재용 조직도 클릭시 callback 함수 세팅
		}
		
		// 조직도 트리의 아이콘 조회 : 회사,기관,부서,파트
		model.put("iconTitle", orCmSvc.getOrgTreeIcon(langTypCd));
		if("Y".equals(multi)){
			model.put("treeSelectOption", "2");//트리 javascript 에서 여러개 선택 되도록 함.
			model.put("processSelect", Boolean.TRUE);
		}
		
		if(upward != null){
			if("Y".equals(brother)){
				// 상위, 방계 조직도 목록
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getUpAndBrotherTreeList(upward, true, langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				model.put("openLvl", new Integer(100));
				model.put("callback","clickRecLstOrg");// 결재용 조직도 클릭시 callback 함수 세팅
			} else {
				// 상위 조직도 목록
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getUpTreeList(upward, langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				model.put("openLvl", new Integer(100));
			}
			model.put("selectedOrgId", upward);
		} else if(downward != null){
			// 하위 조직도 목록
			List<OrOrgBVo> orOrgBVoList = orCmSvc.getDownTreeList(downward, langTypCd);
			model.put("orOrgBVoList", orOrgBVoList);
			model.put("openLvl", new Integer(100));
			model.put("selectedOrgId", downward);
		} else if(oneDeptId != null){
			List<OrOrgBVo> orOrgBVoList = orCmSvc.getOneTreeList(oneDeptId, langTypCd);
			model.put("orOrgBVoList", orOrgBVoList);
			model.put("openLvl", new Integer(5));
			model.put("selectedOrgId", oneDeptId);
		} else {
			
			// 대외 조직도 - 면
			if("foreign".equals(mode)){
				// 조직도 트리 조회
				List<OrOrgBVo> orOrgBVoList = orCmSvc.getForeignOrgTreeList(userVo.getCompId(), "Y", langTypCd);
				model.put("orOrgBVoList", orOrgBVoList);
				
				if(model.get("selectedOrgId")==null){
					if(orOrgBVoList==null || orOrgBVoList.isEmpty()){
						model.put("selectedOrgId", "ROOT");
					} else {
						// 초기 부서 설정
						if(orOrgBVoList!=null && !orOrgBVoList.isEmpty()){
							for(OrOrgBVo orOrgBVo : orOrgBVoList){
								if("ROOT".equals(orOrgBVo.getOrgPid())){
									model.put("selectedOrgId", orOrgBVo.getOrgId());
									break;
								}
							}
						}
					}
				}
				
			// 기존 로직
			} else {
				
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
				// 조직도 트리 조회
				List<OrOrgBVo> orOrgBVoList = null;
				
				// 전사 조직도 이고 계열사 사용 일 경우
				if("Y".equals(global) && "Y".equals(sysPlocMap.get("affiliatesEnable"))){
					
					orOrgBVoList = orCmSvc.getAffiliateOrgTreeList(userVo.getCompId(), 
							"Y".equals(withDel) ? null : "Y", langTypCd);
					model.put("orOrgBVoList", orOrgBVoList);
					
				} else {
					/* global=Y : 전사 조직도 보여줌
					 * compId 파라미터 있으면 : 파라미터 회사의 조직도 보여줌
					 * compId 파라미터 없으면 : 자기소속 회사의 조직도 보여줌
					 * */
					String queryCompId = "Y".equals(global) ? null : compId!=null ? compId : userVo.getCompId();
					
					orOrgBVoList = orCmSvc.getOrgTreeList(queryCompId,
							"Y".equals(withDel) ? null : "Y", langTypCd);
					model.put("orOrgBVoList", orOrgBVoList);
				}
				
				if(model.get("selectedOrgId")==null){
					// 초기 부서 설정
					if(compId!=null && orOrgBVoList!=null && !orOrgBVoList.isEmpty()){
						for(OrOrgBVo orOrgBVo : orOrgBVoList){
							if("ROOT".equals(orOrgBVo.getOrgPid())){
								model.put("selectedOrgId", orOrgBVo.getOrgId());
								break;
							}
						}
					}
				}
			}
			
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/treeOrgFrm");
	}
	/** [프레임] 조직도 - 선택된 부서(우측) */
	@RequestMapping(value = "/or/org/listSeltdOrgFrm")
	public String listSeltdOrgFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/or/org/listSeltdOrgFrm");
	}
	
	/** 개인정보 */
	@RequestMapping(value = "/or/user/setUser")
	public String setUser(HttpServletRequest request, HttpSession session,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		viewUserPop(request, userVo.getUserUid(), null, model);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 모바일 사용 여부
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnabled", Boolean.TRUE);
		}
		// 사진 수정 안함
		if("Y".equals(sysPlocMap.get("blockPnsPhotoEnable"))){
			model.put("blockPnsPhotoEnable", Boolean.TRUE);
		}
		// 개인정보 수정 안함
		if("Y".equals(sysPlocMap.get("blockPnsInfoEnable"))){
			model.put("blockPnsInfoEnable", Boolean.TRUE);
		}
		// PC 알림 사용
		if("Y".equals(sysPlocMap.get("pcNotiEnable"))){
			model.put("pcNotiEnable", Boolean.TRUE);
			model.put("pcNotiMds", ptSysSvc.getPcNotiMds(sysPlocMap));
			
			@SuppressWarnings("unchecked")
			Map<String, Map<String, ?>> userSetupMap = (Map<String, Map<String, ?>>)session.getAttribute("userSetupMap");
			Map<String, ?> psnPcNotiMap = userSetupMap.get("pcNotiMap");
			if(psnPcNotiMap != null){
				model.put("psnPcNotiMap", psnPcNotiMap);
			}
		}
		
		// 원직자 보안 정보
		Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(userVo.getOdurUid());
		model.put("odurSecuMap", odurSecuMap);
		
		return LayoutUtil.getJspPath("/or/user/setUser");
	}
	/** 사용자 조회 */
	@RequestMapping(value = "/or/user/viewUserPop")
	private String viewUserPop(HttpServletRequest request,
			@Parameter(name="userUid", required=false) String userUid,
			@Parameter(name="userNm", required=false) String userNm,
			ModelMap model) throws Exception {
		
		try {

			if((userUid==null || userUid.isEmpty()) && (userNm==null || userNm.isEmpty())){
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUser", request);
				LOGGER.error("fail to view user - empty userUid "+ msg);
				throw new CmException(msg);
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo orUserBVo = new OrUserBVo();
			if(userUid!=null) orUserBVo.setUserUid(userUid);
			if(userNm!=null){
				orUserBVo.setRescNm(userNm);
				orUserBVo.setUserStatCdList(orCmSvc.getViewUserStatCdList());
			}
			// 타회사 임직원 검색 안함
			if("Y".equals(sysPlocMap.get("searchUserOnOtherCompDisable"))){
				UserVo userVo = LoginSession.getUser(request);
				orUserBVo.setCompId(userVo.getCompId());
			}
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			
			if(orUserBVo==null){
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUser", request);
				LOGGER.error("fail to view user - no user - userUid:"+userUid+"  "+msg);
				throw new CmException(msg);
			}
			model.put("orUserBVo", orUserBVo);
			if(userUid==null) userUid = orUserBVo.getUserUid();//명으로 조회 경우
			
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
				throw new CmException(msg);
			}
			model.put("orOdurBVo", orOdurBVo);
			
			// 사용자이미지상세(OR_USER_IMG_D) 테이블
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			// 겸직자 이미지 조회
			orUserImgDVo.setUserUid(userUid);
			@SuppressWarnings("unchecked")
			List<OrUserImgDVo> orUserImgDVoList = (List<OrUserImgDVo>)commonSvc.queryList(orUserImgDVo);
			if(orUserImgDVoList!=null){
				// userImgTypCd : 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
				for(OrUserImgDVo storedOrUserImgDVo : orUserImgDVoList){
					model.put("orUserImgDVo"+storedOrUserImgDVo.getUserImgTypCd(), storedOrUserImgDVo);
				}
			}
			// 원직자와 겸직자가 다른 경우
			if(!odurUid.equals(userUid)){
				// 원직자 이미지 조회
				orUserImgDVo = new OrUserImgDVo();
				orUserImgDVo.setUserUid(odurUid);
				@SuppressWarnings("unchecked")
				List<OrUserImgDVo> orUserImgDVoList2 = (List<OrUserImgDVo>)commonSvc.queryList(orUserImgDVo);
				if(orUserImgDVoList2!=null){
					for(OrUserImgDVo storedOrUserImgDVo : orUserImgDVoList2){
						// 겸직자 이미지가 세팅되지 않으면 - 원직자 이미지 세팅
						if(model.get("orUserImgDVo"+storedOrUserImgDVo.getUserImgTypCd())==null){
							model.put("orUserImgDVo"+storedOrUserImgDVo.getUserImgTypCd(), storedOrUserImgDVo);
						}
					}
				}
			}
			
			// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			orUserPinfoDVo.setOdurUid(odurUid);
			orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
			if(orUserPinfoDVo!=null){
				orCmSvc.decryptUserPinfo(orUserPinfoDVo);// 복호화
				model.put("orUserPinfoDVo", orUserPinfoDVo);
			}
			
			// 회사명 표시 (사용자 상세 정보)
			if("Y".equals(sysPlocMap.get("showCompNmEnable"))){
				model.put("showCompArea", Boolean.TRUE);
				model.put("compAreaNm", messageProperties.getMessage("cols.comp", request));//회사
				
				PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(orUserBVo.getCompId(), langTypCd);
				if(ptCompBVo != null){
					model.put("compNm", ptCompBVo.getRescNm());
				}
			} else if("Y".equals(sysPlocMap.get("showInstNmEnable"))){
				model.put("compAreaNm", messageProperties.getMessage("cols.comp", request));//회사
				model.put("showCompArea", Boolean.TRUE);
				
				OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orUserBVo.getOrgId(), "G", langTypCd);
				if(orOrgTreeVo!=null){
					model.put("compNm", orOrgTreeVo.getRescNm());
				}
			} else if("Y".equals(sysPlocMap.get("showParentDeptEnable"))){
				model.put("compAreaNm", messageProperties.getMessage("or.cols.parentDept", request));//상위 부서
				model.put("showCompArea", Boolean.TRUE);
				
				OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orUserBVo.getOrgId(), null, langTypCd);
				if(orOrgTreeVo!=null){
					orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orOrgTreeVo.getOrgPid(), null, langTypCd);
					if(orOrgTreeVo!=null){
						model.put("compNm", orOrgTreeVo.getRescNm());
					}
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
			// 사진 수정 안함
			if("Y".equals(sysPlocMap.get("blockPnsPhotoEnable"))){
				model.put("blockPnsPhotoEnable", Boolean.TRUE);
			}
			// 비밀번호 찾기
			if("Y".equals(sysPlocMap.get("lostPwEnable"))){
				model.put("lostPwEnable", Boolean.TRUE);
			}
			
			return LayoutUtil.getJspPath("/or/user/viewUserPop");
		} catch(CmException e){
			model.put("exception", e);
			return "/cm/error/error500";
		}
	}
	
	/** [팝업] 사진 보기 */
	@RequestMapping(value = "/or/user/viewImagePop")
	public String viewImagePop(HttpServletRequest request,
			@Parameter(name="userUid", required=false) String userUid,
			@Parameter(name="userImgTypCd", required=false) String userImgTypCd,
			ModelMap model) throws Exception {
		
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		
		// 겸직자 이미지 조회
		orUserImgDVo.setUserUid(userUid);
		orUserImgDVo.setUserImgTypCd(userImgTypCd);
		orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
		if(orUserImgDVo==null){
			
			// 사용자기본(OR_USER_B) 테이블 - 조회
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(userUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			
			orUserImgDVo = new OrUserImgDVo();
			orUserImgDVo.setUserUid(orUserBVo.getOdurUid());
			orUserImgDVo.setUserImgTypCd(userImgTypCd);
			orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
		}
		
		if(orUserImgDVo!=null){
			try{
				String imgWdth = orUserImgDVo.getImgWdth();
				if(imgWdth!=null && !imgWdth.isEmpty()){
					int w = Integer.parseInt(orUserImgDVo.getImgWdth());
					if(w<200){
						model.put("dialogWidth", "200px");
					} else {
						model.put("dialogWidth", orUserImgDVo.getImgWdth()+"px");
					}
				}
			} catch(NumberFormatException e){}
			model.put("orUserImgDVo", orUserImgDVo);
		}
		
		return LayoutUtil.getJspPath("/or/user/viewImagePop");
	}
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = "/or/user/setMyImagePop")
	public String setImagePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/or/user/setMyImagePop");
	}
	
	/** [히든프레임] 사진,도장,사인 업로드 */
	@RequestMapping(value = "/or/user/transMyImage")
	public String transMyImage(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			
			// 업로드 경로
			String userImgTypCd = paramMap.get("userImgTypCd");// 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
			String path = 
				"01".equals(userImgTypCd) ? "images/upload/or/stamp" :
					"02".equals(userImgTypCd) ? "images/upload/or/sign" :
						"03".equals(userImgTypCd) ? "images/upload/or/photo" :
							"images/upload/or/else";
							
			DistHandler distHandler = distManager.createHandler(path, locale);//업로드 경로 설정
			String distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("photo"));// file-tag 의 name
			distHandler.distribute();
			
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			VoUtil.fromMap(orUserImgDVo, paramMap);
			orUserImgDVo.setUserUid(userVo.getUserUid());
			
			File imgFile = fileMap.get("photo");
			if(fileMap.get("photo")!=null){
				BufferedImage bimg = ImageIO.read(imgFile);
				orUserImgDVo.setImgWdth(Integer.toString(bimg.getWidth()));
				orUserImgDVo.setImgHght(Integer.toString(bimg.getHeight()));
				orUserImgDVo.setImgPath(distPath);
			}
			
			orUserImgDVo.setModrUid(userVo.getUserUid());
			orUserImgDVo.setModDt("sysdate");
			queryQueue.store(orUserImgDVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.setUserImage('"+orUserImgDVo.getUserUid()+"', '"+orUserImgDVo.getUserImgTypCd()+"', '"+request.getAttribute("_cxPth")+distPath+"', "+orUserImgDVo.getImgWdth()+", "+orUserImgDVo.getImgHght()+");");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
	
		return LayoutUtil.getResultJsp();
	}
	/** [AJAX] 이미지 삭제 */
	@RequestMapping(value = "/or/user/transMyImageDelAjx")
	public String transMyImageDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String userImgTypCd = (String)jsonObject.get("userImgTypCd");
			
			UserVo userVo = LoginSession.getUser(request);
			
			// 사용자이미지상세(OR_USER_IMG_D) 테이블
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			orUserImgDVo.setUserUid(userVo.getUserUid());
			orUserImgDVo.setUserImgTypCd(userImgTypCd);
			commonSvc.delete(orUserImgDVo);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} catch(Exception e){
			//cm.msg.del.fail=삭제에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.fail", request));
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 임직원 수 조회(이름으로) */
	@RequestMapping(value = "/or/user/getUserCountByNmAjx")
	public String getUserCountByNmAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String userNm = (String)jsonObject.get("userNm");
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setRescNm(userNm);
		orUserBVo.setUserStatCdList(orCmSvc.getViewUserStatCdList());// 조회용 사용자 상태 코드 저장 - 02:근무중, 03:휴직, 04:정직
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 타회사 임직원 검색 안함
		if("Y".equals(sysPlocMap.get("searchUserOnOtherCompDisable"))){
			UserVo userVo = LoginSession.getUser(request);
			orUserBVo.setCompId(userVo.getCompId());
		}
		
		Integer count = commonSvc.count(orUserBVo);
		model.put("count", count);
		return JsonUtil.returnJson(model);
	}

	
	/** [팝업] 임직원 목록 조회 - 프레임 - 선택된 사용자 조회 */
	@RequestMapping(value = "/or/user/listUserPop")
	public String listUserPop(HttpServletRequest request,
			@Parameter(name="userUid", required=false) String userUid,
			@Parameter(name="opt", required=false) String opt,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/or/user/listUserPop");
	}
	
	/** [팝업] 임직원 목록 조회 - 프레임 */
	@RequestMapping(value = "/or/user/searchUserPop")
	public String searchUserPop(HttpServletRequest request,
			@Parameter(name="global", required=false) String global,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="userUid", required=false) String userUid,
			@Parameter(name="opt", required=false) String opt,
			@Parameter(name="downward", required=false) String downward,
			@Parameter(name="oneDeptId", required=false) String oneDeptId,
			@Parameter(name="orgId", required=false) String orgId,
			ModelMap model) throws Exception {
		
		/* 조직도
		 *   - 디폴트 : 자기가 속한 회사의 조직만 보여줌
		 *   - compId 넘길때 : 해당 회사의 조직도 보여줌
		 *   - global=Y 넘길때 : 전사 조직도 보여줌
		 * 
		 * 사용자 검색
		 *   - 디폴트 : 전사의 사용자 조회함
		 *   - compId 넘길때 : 해당 회사의 사용자를 조회함
		 * */
		
		// 시스템 설정
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		char concatChar = '?';
		StringBuilder orgTreeParam = new StringBuilder(128);
		StringBuilder schUserParam = new StringBuilder(128);
		
		boolean isChoice = opt!=null && (opt.equals("multi") || opt.equals("single"));
		
		// 회사관리 > 시스템 관리자 지정 - 전사의 조직도, 전사의 사용자 검색
		if("Y".equals(global)){
			orgTreeParam.append(concatChar).append("global=Y");
			concatChar = '&';
		}
		// 회사관리 > [버튼] 회사관리자 설정 클릭  - 다른회사의 조직을 조회하려고 넘김
		else if(compId!=null){
			orgTreeParam.append(concatChar).append("compId=").append(compId);
			concatChar = '&';
			schUserParam.append("&compId=").append(compId);
		}
		// 사용자 한명 또는 여러명 지정의 경우 - 일반 조직도 띄워서 사용자 선택 할 경우
		else if(isChoice){
			UserVo userVo = LoginSession.getUser(request);
			schUserParam.append("&compId=").append(userVo.getCompId());
		}
		
		// 1명 선택이고, 사용자가 선택된 경우 : 선택된 사용자의 부서를 열어줌
		if(userUid!=null && !userUid.isEmpty()){
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(userUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			if(orUserBVo!=null){
				orgTreeParam = new StringBuilder(128);
				orgTreeParam.append("?orgId=").append(orUserBVo.getOrgId());
				concatChar = '&';
			}
		}
		
		// 하위 부서 트리 옵션
		if(downward!=null && !downward.isEmpty()){
			orgTreeParam.append(concatChar).append("downward=").append(downward);
			concatChar = '&';
			schUserParam.append("&downward=").append(downward);
		}
		
		if(oneDeptId!=null && !oneDeptId.isEmpty()){
			orgTreeParam.append(concatChar).append("oneDeptId=").append(oneDeptId);
			concatChar = '&';
			schUserParam.append("&oneDeptId=").append(oneDeptId);
		}
		
		if(orgId!=null && !orgId.isEmpty()){
			orgTreeParam.append(concatChar).append("orgId=").append(orgId);
			concatChar = '&';
		} else {
			// 조직도 전체 사용자 기본 표시
			if(!isChoice && "Y".equals(sysPlocMap.get("dispAllUsersEnable"))){
				orgTreeParam.append(concatChar).append("orgId=ROOT");
				concatChar = '&';
			}
		}
		
		model.put("orgTreeParam", orgTreeParam.toString());
		model.put("schUserParam", schUserParam.toString());
		
		if(orgTreeParam.length()==0){
			model.put("foreignTreeParam", "?mode=foreign");
		} else {
			model.put("foreignTreeParam", orgTreeParam.append("?mode=foreign").toString());
		}
		
		if(!isChoice){
			// 조직도 부서 주소 표시
			if("Y".equals(sysPlocMap.get("dispDeptAddrEnable"))){
				model.put("dispDeptAddrEnable", Boolean.TRUE);
			}
			// 조직도 전체 사용자 기본 표시
			if("Y".equals(sysPlocMap.get("dispAllUsersEnable"))){
				model.put("dispAllUsersEnable", Boolean.TRUE);
			}
		}
		
		return LayoutUtil.getJspPath("/or/user/searchUserPop");
	}
	
	/** [프레임] 임직원 목록 조회(사용자명 검색) */
	@RequestMapping(value = "/or/user/searchUserFrm")
	public String searchUserFrm(HttpServletRequest request,
			@Parameter(name="userNm", required=false) String userNm,
			@Parameter(name="opt", required=false) String opt,
			@Parameter(name="userStatCd", required=false) String userStatCd,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="upward", required=false) String upward,
			@Parameter(name="downward", required=false) String downward,
			@Parameter(name="oneDeptId", required=false) String oneDeptId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		if(userNm!=null && !userNm.isEmpty()){
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo.setRescNm(userNm);
			if(compId!=null) orUserBVo.setCompId(compId);
			if("02".equals(userStatCd)){
				orUserBVo.setUserStatCd(userStatCd);
			} else if("999".equals(userStatCd)){/** 문서관리(개인) 사용자 상태 코드 - 05:퇴직, 11:해제, 99:삭제, (02:근무중, 03:휴직, 04:정직 제외) */
				orUserBVo.setUserStatCdList(ArrayUtil.toList(new String[]{"05","11","99"}, false));
			} else {
				orUserBVo.setUserStatCdList(orCmSvc.getViewUserStatCdList());
			}
			
			// 직계 상위부서 검색
			if(upward!=null && !upward.isEmpty()){
				// 해당 하위부서 목록을 구해옴
				List<String> upOrgList = orCmSvc.getOrgUpIdLine(upward, langTypCd);
				orUserBVo.setOrgIdList(upOrgList);//조직ID 목록
			// 하위부서 검색
			} else if(downward!=null && !downward.isEmpty()){
				// 해당 하위부서 목록을 구해옴
				List<String> subOrgList = orCmSvc.getOrgSubIdList(downward, langTypCd);
				subOrgList.add(downward);
				orUserBVo.setOrgIdList(subOrgList);//조직ID 목록
			// 하나의 부서  검색
			} else if(oneDeptId!=null && !oneDeptId.isEmpty()){
				orUserBVo.setDeptId(oneDeptId);
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 타회사 임직원 검색 안함
			if("Y".equals(sysPlocMap.get("searchUserOnOtherCompDisable"))){
				orUserBVo.setCompId(userVo.getCompId());
			// 계열사 사용
			} else if("Y".equals(sysPlocMap.get("affiliatesEnable"))){
				// 계열사
				PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(userVo.getCompId(), langTypCd);
				List<String> affiliateIds = ptCompBVo==null ? null : ptCompBVo.getAffiliateIds();
				if(affiliateIds == null){
					orUserBVo.setCompId(userVo.getCompId());
				} else {
					orUserBVo.setCompIdList(affiliateIds);
				}
			}
			
			// 레코드 수 조회
			Integer recodeCount = commonSvc.count(orUserBVo);
			if("multi".equals(opt)){ request.setAttribute("pageRowCnt", Integer.valueOf(5)); }
			else { request.setAttribute("pageRowCnt", Integer.valueOf(10)); }
			
			PersonalUtil.setPaging(request, orUserBVo, recodeCount);
			// 리스트 환경 설정 - 세팅
			List<PtLstSetupDVo> ptLstSetupDVoList = orCmSvc.setListQueryOptions(orUserBVo, "OR_SRCH");
			model.put("ptLstSetupDVoList", ptLstSetupDVoList);
			
			// 목록 조회
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
			if(orUserBVoList!=null && !orUserBVoList.isEmpty()){
				// 개인정보 맵 조회
				Map<Integer, Map<String, Object>> userPsnInfoMap = orCmSvc.queryUserPsnInfoMap(orUserBVoList);
				
				// 사용자 정보 및 개인정보를 맵으로 합쳐 uInfoList 에 저장함
				Map<String, Object> psnInfoMap, userInfoMap;
				List<Map<String, Object>> orUserMapList = new ArrayList<Map<String, Object>>();
				for(OrUserBVo storedOrUserBVo :orUserBVoList){
					userInfoMap = VoUtil.toMap(storedOrUserBVo, null);
					psnInfoMap = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
					if(psnInfoMap!=null) userInfoMap.putAll(psnInfoMap);
					orUserMapList.add(userInfoMap);
				}
				
				model.put("recodeCount", recodeCount);
				model.put("orUserMapList", orUserMapList);
			}
		} else {
			List<PtLstSetupDVo> ptLstSetupDVoList = orCmSvc.setListQueryOptions(null, "OR_SRCH");
			model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		}
		
		return LayoutUtil.getJspPath("/or/user/searchUserFrm");
	}
	
	/** [프레임] 임직원 목록 조회 - 기본 조직도의 경우 */
	@RequestMapping(value = {"/or/user/listUserFrm", "/cm/demo/listUserFrm"})
	public String listUserFrm(HttpServletRequest request,
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="opt", required=false) String opt,
			@Parameter(name="userStatCd", required=false) String userStatCd,
			@Parameter(name="clickCallback", required=false) String clickCallback,
			ModelMap model) throws Exception {
		
		String uri = request.getRequestURI();
		// 데모 로그인용 - 조직도 보이기
		if(uri.startsWith("/cm/demo")){
			if(!SysSetupUtil.isDemoSite()){
				model.put("todo", "top.location.href = '/';");
				return LayoutUtil.getResultJsp();
			}
			userStatCd = "02";
			model.put("demoSite", Boolean.TRUE);
		}
		// 데모 로그인용
		
		if(orgId==null || orgId.isEmpty()){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String message = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("no orgId:"+orgId+" - "+message);
			throw new CmException(message);
		}
		
		//    opt - null:일반 조직도, single:한명선택, multi:여러명 선택
		//    일반조직도의 경우 - 02:근무중, 03:휴직, 04:정직 보여줌
		//    사용자 선택의 경우(single:한명선택, multi:여러명 선택) - 02:근무중 만 보여줌
		if(userStatCd==null || userStatCd.isEmpty()){
			userStatCd = opt==null || opt.isEmpty() ? null : "02";
		}
		
		if("multi".equals(opt)){
			if(clickCallback==null) {
				model.put("clickCallback", "clickUserCheck");
			} else {
				model.put("clickCallback", clickCallback);
			}
		}
		
		if("ROOT".equals(orgId) && !"multi".equals(opt) && !"single".equals(opt)){
			// 시스템 설정
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			if("Y".equals(sysPlocMap.get("dispAllUsersEnable"))){// 조직도 전체 사용자 기본 표시
				model.put("dispAllUsersEnable", Boolean.TRUE);
			}
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		orCmSvc.setUserMapByOrgId(orgId, userStatCd, langTypCd, model);
		return LayoutUtil.getJspPath("/or/user/listUserFrm");
	}
	
	/** [프레임] 사용자 목록(조직도 하단) - 선택된 사용자 목록을 설정된 UI 기준으로 만듬 */
	@RequestMapping(value = "/or/user/listSeltdUserFrm")
	public String listSeltdUserFrm(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			@Parameter(name="userUids", required=false) String userUids,
			ModelMap model) throws Exception {
		
		// userUids 를 List로 만듬
		List<String> userUidList = new ArrayList<String>();
		if(userUids!=null && !userUids.isEmpty()){
			for(String userUid : userUids.split(",")){
				userUidList.add(userUid);
			}
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		orCmSvc.setUserMapByUserUids(lstSetupMetaId, userUidList, langTypCd, model);
		return LayoutUtil.getJspPath("/or/user/listSeltdUserFrm");
	}
	
	/** 사용자 정보 저장 */
	@RequestMapping(value = "/or/user/transUser")
	public String transUser(HttpServletRequest request,
			@Parameter(name="userUids", required=false) String userUids,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("blockPnsInfoEnable"))){
			OrOdurSecuDVo orOdurSecuDVo = new OrOdurSecuDVo();
			orOdurSecuDVo.setOdurUid(userVo.getOdurUid());
			orOdurSecuDVo.setSecuId("useMsgLginYn");
			String secuVa = request.getParameter("useMsgLginYn");
			if(secuVa==null || !"N".equals(secuVa)){
				commonSvc.delete(orOdurSecuDVo);
			} else {
				orOdurSecuDVo.setSecuVa(secuVa);
				if(commonSvc.update(orOdurSecuDVo)==0){
					commonSvc.insert(orOdurSecuDVo);
				}
			}
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			return LayoutUtil.getResultJsp();
		}
		
		
		ptOrgCtrl.transUser(request, userVo.getUserUid(), null, model);
		model.remove("todo");
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 부서 주소 조회 */
	@RequestMapping(value = "/or/org/getDeptAddrAjx")
	public String getDeptAddrAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String orgId = (String)jsonObject.get("orgId");
		
		if(orgId!=null && !orgId.isEmpty()){
			OrOrgCntcDVo orOrgCntcDVo = new OrOrgCntcDVo();
			orOrgCntcDVo.setOrgId(orgId);
			orOrgCntcDVo = (OrOrgCntcDVo)commonSvc.queryVo(orOrgCntcDVo);
			
			if(orOrgCntcDVo!=null){
				
				String adr = orOrgCntcDVo.getAdr();
				String detlAdr = orOrgCntcDVo.getDetlAdr();
				
				if(adr!=null) adr = adr.trim();
				if(detlAdr!=null) detlAdr = detlAdr.trim();
				
				if(adr!=null && !adr.isEmpty()){
					if(detlAdr!=null && !detlAdr.isEmpty()){
						model.put("deptAddr", adr+" "+detlAdr);
					} else {
						model.put("deptAddr", adr);
					}
				} else if(detlAdr!=null && !detlAdr.isEmpty()){
					model.put("deptAddr", detlAdr);
				}
			}
		}
		
		return JsonUtil.returnJson(model);
	}
}
