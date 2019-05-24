package com.innobiz.orange.web.pt.admCtrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
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
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrDbIntgDVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOdurSecuDVo;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserPwHVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtOrgSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.PushSyncSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 사용자 조직 관리 */
@Controller
public class PtOrgCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtOrgCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 조직도 서비스 */
	@Autowired
	private PtOrgSvc ptOrgSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 조직도 사용자 Push 방식 동기화 서비스 */
	@Autowired
	private PushSyncSvc pushSyncSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;

//	/** 컨텍스트 프로퍼티 */
//	@Resource(name="contextProperties")
//	private Properties contextProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 사용자 관리 */
	@RequestMapping(value = {"/pt/adm/org/listUser", "/pt/adm/org/excelDownLoad"})
	public ModelAndView listUser(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="withSub", required=false) String withSub,
			ModelMap model) throws Exception{
		
		
		boolean isExcelDownload = request.getRequestURI().indexOf("excelDownLoad") > 0;
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		OrUserBVo orUserBVo = new OrUserBVo();
		VoUtil.bind(request, orUserBVo);
		
		// 회사 - 전체 를 선택한 경우
		if(compId!=null && compId.isEmpty()){
			compId = null;// 빈 회사 지워줌
			orUserBVo.setCompId(compId);
		}
		
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 시스템 관리자가 아닌 경우에는 - 자기 회사만 검색
		if(!isSysAdmin){
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();// 시스템 관리자가 아니면 - 자기 회사ID 설정
			orUserBVo.setCompId(compId);
		} else {
			model.put("global", "Y");
		}
		
		// 부서 - 하위포함 일 경우
		if("Y".equals(withSub)){
			List<String> subOrgList = orCmSvc.getOrgSubIdList(orgId, langTypCd);
			if(!subOrgList.contains(orgId)) subOrgList.add(orgId);
			orUserBVo.setOrgId(null);
			orUserBVo.setOrgIdList(subOrgList);
		}
		
		// 리스트 환경 설정 - 세팅
		List<PtLstSetupDVo> ptLstSetupDVoList = orCmSvc.setListQueryOptions(orUserBVo, "OR_USRMNG");
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		orUserBVo.setWhereSqllet("AND USER_UID != 'U0000001'");// 시스템 관리자 제외 - 설치/관리 계정
		
		if(isExcelDownload){
			return listUserExcel(request, orUserBVo, isSysAdmin, langTypCd, model);
		}
		
		Integer recodeCount = commonSvc.count(orUserBVo);
		PersonalUtil.setPaging(request, orUserBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		
		// 회사명 맵
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = null;
		if(isSysAdmin){
			ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		}
		
		// 개인정보 맵 조회
		Map<Integer, Map<String, Object>> userPsnInfoMap = orCmSvc.queryUserPsnInfoMap(orUserBVoList);
		
		// 사용자 정보 및 개인정보를 맵으로 합쳐 uInfoList 에 저장함
		Map<String, Object> psnInfoMap, userInfoMap;
		List<Map<String, Object>> orUserMapList = new ArrayList<Map<String, Object>>();
		for(OrUserBVo storedOrUserBVo : orUserBVoList){
			userInfoMap = VoUtil.toMap(storedOrUserBVo, null);
			if(isSysAdmin){//시스템 관리자면 회사명 세팅
				ptCompBVo = ptCompMap.get(storedOrUserBVo.getCompId());
				if(ptCompBVo!=null){
					userInfoMap.put("compNm", ptCompBVo.getRescNm());
				}
			}
			psnInfoMap = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
			if(psnInfoMap!=null) userInfoMap.putAll(psnInfoMap);
			orUserMapList.add(userInfoMap);
		}
		model.put("orUserMapList", orUserMapList);
		
		
		if(!isSysAdmin || compId!=null){
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 회사별 조직코드 사용
			if("Y".equals(sysPlocMap.get("codeByCompEnable"))){
				// 직급 코드
				List<PtCdBVo> gradeCdList = ptCmSvc.getCdListEqCompId("GRADE_CD", langTypCd, compId, "Y");
				model.put("gradeCdList", gradeCdList);
				// 직책 코드
				List<PtCdBVo> titleCdList = ptCmSvc.getCdListEqCompId("TITLE_CD", langTypCd, compId, "Y");
				model.put("titleCdList", titleCdList);
				// 직위코드
				List<PtCdBVo> positCdList = ptCmSvc.getCdListEqCompId("POSIT_CD", langTypCd, compId, "Y");
				model.put("positCdList", positCdList);
				// 직무코드
				List<PtCdBVo> dutyCdList = ptCmSvc.getCdListEqCompId("DUTY_CD", langTypCd, compId, "Y");
				model.put("dutyCdList", dutyCdList);
			} else {
				// 직급 코드
				List<PtCdBVo> gradeCdList = ptCmSvc.getCdListByCompId("GRADE_CD", langTypCd, compId, "Y");
				model.put("gradeCdList", gradeCdList);
				// 직책 코드
				List<PtCdBVo> titleCdList = ptCmSvc.getCdListByCompId("TITLE_CD", langTypCd, compId, "Y");
				model.put("titleCdList", titleCdList);
				// 직위코드
				List<PtCdBVo> positCdList = ptCmSvc.getCdListByCompId("POSIT_CD", langTypCd, compId, "Y");
				model.put("positCdList", positCdList);
				// 직무코드
				List<PtCdBVo> dutyCdList = ptCmSvc.getCdListByCompId("DUTY_CD", langTypCd, compId, "Y");
				model.put("dutyCdList", dutyCdList);
			}
		} else {
			// 직급 코드
			List<PtCdBVo> gradeCdList = ptCmSvc.getCdListByCompId("GRADE_CD", langTypCd, null, "Y");
			model.put("gradeCdList", gradeCdList);
			// 직책 코드
			List<PtCdBVo> titleCdList = ptCmSvc.getCdListByCompId("TITLE_CD", langTypCd, null, "Y");
			model.put("titleCdList", titleCdList);
			// 직위코드
			List<PtCdBVo> positCdList = ptCmSvc.getCdListByCompId("POSIT_CD", langTypCd, null, "Y");
			model.put("positCdList", positCdList);
			// 직무코드
			List<PtCdBVo> dutyCdList = ptCmSvc.getCdListByCompId("DUTY_CD", langTypCd, null, "Y");
			model.put("dutyCdList", dutyCdList);
		}
		
		// 겸직구분코드
		List<PtCdBVo> aduTypCdList = ptCmSvc.getCdList("ADU_TYP_CD", langTypCd, "Y");
		model.put("aduTypCdList", aduTypCdList);
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		model.put("userStatCdList", userStatCdList);
		
		// 검색조건 세팅을 위해 3개씩 나누어 List에 담음
		PtLstSetupDVo ptLstSetupDVo;
		PtLstSetupDVo[] ptLstSetupDVos = null;
		List<PtLstSetupDVo[]> ptLstSetupDVoCondiList = new ArrayList<PtLstSetupDVo[]>();
		int size = ptLstSetupDVoList==null ? 0 : ptLstSetupDVoList.size();
		int index = -1, j;
		
//		String[] sysAdminSkips = {"gradeNm", "titleNm", "positNm", "dutyNm"};
		while(index<size){
			ptLstSetupDVos = new PtLstSetupDVo[3];
			for(j=0;j<3;j++){
				index++;
				if(index<size){
					ptLstSetupDVo = ptLstSetupDVoList.get(index);
					//if(!"none".equals(ptLstSetupDVo.getSortOptVa()) && !(isSysAdmin && ArrayUtil.isInArray(sysAdminSkips, ptLstSetupDVo.getAtrbId()))){
					if(!"none".equals(ptLstSetupDVo.getSortOptVa())){
						ptLstSetupDVos[j] = ptLstSetupDVo;
					} else {
						j--;
						continue;
					}
				} else {
					ptLstSetupDVos[j] = null;
				}
			}
			if(ptLstSetupDVos[0]!=null) ptLstSetupDVoCondiList.add(ptLstSetupDVos);
		}
		
		// 시스템 관리자의 경우 - 검색 조건에 회사목록 추가하기 위한 작업
		if(isSysAdmin){
			// 회사목록
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
			model.put("ptCompBVoList", ptCompBVoList);
			ptLstSetupDVo = new PtLstSetupDVo();
			ptLstSetupDVo.setAtrbId("compId");
			ptLstSetupDVo.setMsgId("cols.comp");
			ptLstSetupDVo.setSortOptVa("idList");
			
			if(ptLstSetupDVos==null || ptLstSetupDVos[2]!=null){
				ptLstSetupDVos = new PtLstSetupDVo[3];
				ptLstSetupDVos[0] = ptLstSetupDVo;
				ptLstSetupDVoCondiList.add(ptLstSetupDVos);
			} else {
				if(ptLstSetupDVos[0]==null) {
					ptLstSetupDVos[0] = ptLstSetupDVo;
					ptLstSetupDVoCondiList.add(ptLstSetupDVos);
				} else if(ptLstSetupDVos[1]==null) {
					ptLstSetupDVos[1] = ptLstSetupDVo;
				} else if(ptLstSetupDVos[2]==null) {
					ptLstSetupDVos[2] = ptLstSetupDVo;
				} else {
					ptLstSetupDVos = new PtLstSetupDVo[3];
					ptLstSetupDVos[0] = ptLstSetupDVo;
					ptLstSetupDVoCondiList.add(ptLstSetupDVos);
				}
			}
		}
		model.put("ptLstSetupDVoCondiList", ptLstSetupDVoCondiList);
		
		return new ModelAndView(LayoutUtil.getJspPath("/pt/adm/org/listUser"));
//		return LayoutUtil.getJspPath("/pt/adm/org/listUser");
	}
	
	private static String[] colNmRescIds = {
		"cols.compNm",//회사명
		"cols.orgNm",//조직명
		"cols.orgId",//조직ID
		"cols.userNm",//사용자명
		"cols.lginId",//로그인ID
//		"cols.userUid",//사용자UID
		"or.term.posit",//직위
		"or.term.grade",//직급
		"or.term.title",//직책
		"cols.aduTyp",//겸직구분
		"or.cols.statCd",//상태코드
		"cols.ein",//사원번호
		"cols.mbno",//휴대전화번호
		"cols.compPhon",//회사전화번호
		"cols.email",//이메일
	};
	/** 사용자 목록 다운로드 */
	private ModelAndView listUserExcel(HttpServletRequest request,
			OrUserBVo orUserBVo, boolean isSysAdmin, String langTypCd,
			ModelMap model) throws SQLException, IOException, CmException {
		
		ModelAndView mv = new ModelAndView("excelDownloadView");
		
		// Sheet 명, 파일명 - or.jsp.setOrg.userListTitle=사용자 목록
		String sheetNm = messageProperties.getMessage("or.jsp.setOrg.userListTitle", request);
		mv.addObject("sheetName", sheetNm);
		mv.addObject("fileName", sheetNm);
		
		List<Integer> widthList = new ArrayList<Integer>();
		Integer[] widths = isSysAdmin
			? new Integer[]{4000,5000,3000,3000,3000,3000,3000,3000,2000,2500,3000,6000,6000,6000}
			: new Integer[]{     5000,3000,3000,3000,3000,3000,3000,2000,2500,3000,6000,6000,6000};
		for(Integer width : widths){
			widthList.add(width);
		}
		mv.addObject("widthList", widthList);
		
		// 상단의 컬럼명
		List<String> colNmList = new ArrayList<String>();
		Map<String, String> orgTermMap = ptSysSvc.getTermMap("or.term", langTypCd);
		String colNm;
		for(String rescId : colNmRescIds){
			if(rescId.startsWith("or.term")){
				colNm = orgTermMap==null ? null : orgTermMap.get(rescId);
				if(colNm==null) colNm = messageProperties.getMessage(rescId, request);
			} else if(!isSysAdmin && rescId.endsWith("compNm")){
				continue;
			} else {
				colNm = messageProperties.getMessage(rescId, request);
			}
			colNmList.add(colNm);
		}
		mv.addObject("colNames", colNmList);
		
		// 엑셀 데이타
		Map<String,Object> colVaMap = new HashMap<String,Object>();
		List<Object> row = null;
		
//		colNames = (List<String>) model.get("colNames");
//		if(!model.containsKey("colValues")) throw new IOException("colValues is Null");
//		colValues = (Map<String,Object>) model.get("colValues");
//		createSheet(workbook, (String) model.get("sheetName"), colNames, colValues , null);
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		
		// 회사명 맵
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		
		// 개인정보 맵 조회
		Map<Integer, Map<String, Object>> userPsnInfoMap = orCmSvc.queryUserPsnInfoMap(orUserBVoList);
		Map<String, Object> psnInfoMap;
		
		int i = 0;
		for(OrUserBVo storedOrUserBVo : orUserBVoList){
			row = new ArrayList<Object>();
			for(String rescId : colNmRescIds){
				if(rescId.endsWith("compNm")){
					if(!isSysAdmin){
						continue;
					}
					ptCompBVo = ptCompMap.get(storedOrUserBVo.getCompId());
					row.add(ptCompBVo!=null ? ptCompBVo.getRescNm() : "");
				} else if(rescId.endsWith("orgNm")){
					row.add(storedOrUserBVo.getOrgRescNm());
				} else if(rescId.endsWith("orgId")){
					row.add(storedOrUserBVo.getOrgId());
				} else if(rescId.endsWith("userNm")){
					row.add(storedOrUserBVo.getUserNm());
				} else if(rescId.endsWith("lginId")){
					row.add(storedOrUserBVo.getLginId());
				} else if(rescId.endsWith("userUid")){
					row.add(storedOrUserBVo.getUserUid());
				} else if(rescId.endsWith("posit")){
					row.add(storedOrUserBVo.getPositNm());
				} else if(rescId.endsWith("grade")){
					row.add(storedOrUserBVo.getGradeNm());
				} else if(rescId.endsWith("title")){
					row.add(storedOrUserBVo.getTitleNm());
				} else if(rescId.endsWith("aduTyp")){
					row.add(storedOrUserBVo.getAduTypNm());
				} else if(rescId.endsWith("statCd")){
					row.add(storedOrUserBVo.getUserStatNm());
				} else if(rescId.endsWith("ein")){
					row.add(storedOrUserBVo.getEin());
				} else if(rescId.endsWith("mbno")){
					psnInfoMap = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
					row.add(psnInfoMap!=null ? psnInfoMap.get("mbno") : "");
				} else if(rescId.endsWith("compPhon")){
					psnInfoMap = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
					row.add(psnInfoMap!=null ? psnInfoMap.get("compPhon") : "");
				} else if(rescId.endsWith("email")){
					psnInfoMap = userPsnInfoMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid()));
					row.add(psnInfoMap!=null ? psnInfoMap.get("email") : "");
				}
			}
			colVaMap.put("col"+i, row);
			i++;
		}
		
		mv.addObject("colValues", colVaMap);
		
		return mv;
	}

	/** 사용자 조직 관리(프레임 페이지) */
	@RequestMapping(value = "/pt/adm/org/setOrg")
	public String setOrg(HttpServletRequest request,
			ModelMap model) throws Exception {
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		// 포털의 조직 관리 페이지
		model.put("adminPage", Boolean.TRUE);
		
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("orgDbSyncEnable"))){
			model.put("orgSyncEnable", Boolean.TRUE);
			
			if(!"U0000001".equals(userVo.getUserUid())){
				OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
				orDbIntgDVo.setCompId(userVo.getCompId());
				orDbIntgDVo = (OrDbIntgDVo)commonSvc.queryVo(orDbIntgDVo);
				if(orDbIntgDVo != null && "Y".equals(orDbIntgDVo.getUseYn())){
					model.put("orDbIntgDVo", orDbIntgDVo);
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/setOrg");
	}

	/** 조직도 트리 조회(왼쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/treeOrgFrm")
	public String treeOrgFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 조직도 트리의 아이콘 조회
		model.put("iconTitle", orCmSvc.getOrgTreeIcon(langTypCd));
		
		UserVo userVo = LoginSession.getUser(request);
		// 시스템 관리자인 경우만 전사 조직도 - 나머지는 자기 회사 조직도
		String compId = SecuUtil.hasAuth(request, "SYS", null) ? null : userVo.getCompId();
		
		// 조직도 트리 조회
		model.put("orOrgBVoList", orCmSvc.getOrgTreeList(compId, null, langTypCd));
		return LayoutUtil.getJspPath("/pt/adm/org/treeOrgFrm");
	}

	/** 조직 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@RequestMapping(value = "/pt/adm/org/listOrgFrm")
	public String listOrgFrm(HttpServletRequest request,
			@Parameter(name="orgPid", required=false) String orgPid,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		String orgTypCd = "D";//조직구분코드 - C:회사, G:기관, D:부서, P:파트
		if("ROOT".equals(orgPid)){
			// 회사 목록 조회
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
			request.setAttribute("ptCompBVoList", ptCompBVoList);
			orgTypCd = "C";
		} else {
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(orgPid);
			orOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
			if(orOrgBVo!=null && "P".equals(orOrgBVo.getOrgTypCd())){
				orgTypCd = "P";
			}
		}

		// 조직구분코드 - 조회
		List<PtCdBVo> orgTypCdList = ptCmSvc.getCdList("ORG_TYP_CD", langTypCd, "Y");
		List<PtCdBVo> newOrgTypCdList = new ArrayList<PtCdBVo>();
		int i, size = orgTypCdList==null ? 0 : orgTypCdList.size();
		if(orgTypCd.equals("C") || orgTypCd.equals("P")){
			for(i=0;i<size;i++){
				if(orgTypCd.equals(orgTypCdList.get(i).getCd())){
					newOrgTypCdList.add(orgTypCdList.get(i));
				}
			}
		} else {
			for(i=0;i<size;i++){
				if(!"C".equals(orgTypCdList.get(i).getCd())){
					newOrgTypCdList.add(orgTypCdList.get(i));
				}
			}
		}
		model.put("orgTypCdList", newOrgTypCdList);
		
		if(orgPid==null || orgPid.isEmpty()){
			List<OrOrgBVo> orOrgBVoList = new ArrayList<OrOrgBVo>();
			// 화면 구성용 2개의 빈 vo 넣음
			orOrgBVoList.add(getDefaultOrOrgBVo(orgTypCd));
			orOrgBVoList.add(getDefaultOrOrgBVo(orgTypCd));
			model.put("orOrgBVoList", orOrgBVoList);
		} else {
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgPid(orgPid);
			orOrgBVo.setOrderBy("SORT_ORDR");
			orOrgBVo.setQueryLang(langTypCd);
			
			@SuppressWarnings("unchecked")
			List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonSvc.queryList(orOrgBVo);
			// 화면 구성용 2개의 빈 vo 넣음
			orOrgBVoList.add(getDefaultOrOrgBVo(orgTypCd));
			orOrgBVoList.add(getDefaultOrOrgBVo(orgTypCd));
			model.put("orOrgBVoList", orOrgBVoList);
			
			// 리소스 조회
			OrRescBVo orRescBVo = new OrRescBVo();
			orRescBVo.setOrgPid(orgPid);
			@SuppressWarnings("unchecked")
			List<OrRescBVo> orRescBVoList = (List<OrRescBVo>)commonSvc.queryList(orRescBVo);
			
			// 코드 리소스 model에 저장
			if(orRescBVoList!=null){
				for(OrRescBVo storedOrRescBVo : orRescBVoList){
					model.put(storedOrRescBVo.getRescId()+"_"+storedOrRescBVo.getLangTypCd(), storedOrRescBVo.getRescVa());
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/listOrgFrm");
	}
	
	/** 행추가용 기본 부서 리턴 */
	private OrOrgBVo getDefaultOrOrgBVo(String orgTypCd){
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgTypCd(orgTypCd);//조직구분코드 - C:회사, G:기관, D:부서, P:파트
		return orOrgBVo;
	}

	/** 조직 상세 조회(오른쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/setOrgFrm")
	public String setOrgFrm(HttpServletRequest request,
			@Parameter(name="orgPid", required=false) String orgPid,
			@Parameter(name="orgId", required=false) String orgId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 조직구분코드 - 조회
		List<PtCdBVo> orgTypCdList = ptCmSvc.getCdList("ORG_TYP_CD", langTypCd, "Y");
		model.put("orgTypCdList", orgTypCdList);

		if("ROOT".equals(orgPid)){
			// 회사 목록 조회
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
			request.setAttribute("ptCompBVoList", ptCompBVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/setOrgFrm");
	}

	/** 사용자 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@RequestMapping(value = "/pt/adm/org/listUserFrm")
	public String listUserFrm(HttpServletRequest request,
			@Parameter(name="orgId", required=false) String orgId,
			ModelMap model) throws Exception {
		
		if("ROOT".equals(orgId)){
			return "/cm/util/reloadable";
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setOrgId(orgId);
		orOrgBVo.setQueryLang(langTypCd);
		orOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
		if(orOrgBVo==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("fail to list user(no Org) - orgId:"+orgId);
			throw new CmException(msg);
		}
		String compId = orOrgBVo.getCompId();
		
		// 성별 코드
		List<PtCdBVo> genCdList = ptCmSvc.getCdList("GEN_CD", langTypCd, "Y");
		model.put("genCdList", genCdList);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 회사별 조직코드 사용
		if("Y".equals(sysPlocMap.get("codeByCompEnable"))){
//			// 직급 코드
//			List<PtCdBVo> gradeCdList = ptCmSvc.getCdListEqCompId("GRADE_CD", langTypCd, compId, "Y");
//			model.put("gradeCdList", gradeCdList);
			// 직책 코드
			List<PtCdBVo> titleCdList = ptCmSvc.getCdListEqCompId("TITLE_CD", langTypCd, compId, "Y");
			model.put("titleCdList", titleCdList);
			// 직위코드
			List<PtCdBVo> positCdList = ptCmSvc.getCdListEqCompId("POSIT_CD", langTypCd, compId, "Y");
			model.put("positCdList", positCdList);
		} else {
//			String refVa1 = orOrgBVo.getRefVa1();
//			// E78CEB : 한화제약
//			if((refVa1!=null && !refVa1.isEmpty()) 
//					&& "E78CEB".equals(CustConfig.CUST_CODE)){
//				// 직책 코드
//				List<PtCdBVo> titleCdList = ptCmSvc.getFilteredCdList("TITLE_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("titleCdList", titleCdList);
//				// 직위코드
//				List<PtCdBVo> positCdList = ptCmSvc.getFilteredCdList("POSIT_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("positCdList", positCdList);
//			} else {
				// 직책 코드
				List<PtCdBVo> titleCdList = ptCmSvc.getCdListByCompId("TITLE_CD", langTypCd, compId, "Y");
				model.put("titleCdList", titleCdList);
				// 직위코드
				List<PtCdBVo> positCdList = ptCmSvc.getCdListByCompId("POSIT_CD", langTypCd, compId, "Y");
				model.put("positCdList", positCdList);
//			}
		}
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		
		// 사용자상태 코드 - 겸직용/원직용
		List<PtCdBVo> userStatAduCdList = new ArrayList<PtCdBVo>();
		List<PtCdBVo> userStatOrgCdList = new ArrayList<PtCdBVo>();
		
		int i, size = userStatCdList==null ? 0 : userStatCdList.size();
		String cd;
		for(i=0;i<size;i++){
			cd = userStatCdList.get(i).getCd();
			if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_ONLY, cd)){
				userStatAduCdList.add(userStatCdList.get(i));
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, cd)){
				userStatAduCdList.add(userStatCdList.get(i));
			} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_BOTH, cd)){
				userStatOrgCdList.add(userStatCdList.get(i));
				userStatAduCdList.add(userStatCdList.get(i));
			} else {
				userStatOrgCdList.add(userStatCdList.get(i));
			}
		}
		model.put("userStatAduCdList", userStatAduCdList);
		model.put("userStatCdList", userStatOrgCdList);
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setOrgId(orgId);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOrgId(orgId);
		orOdurBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
		Map<Integer, OrOdurBVo> odurMap = toOdurMap(orOdurBVoList);
		
		// 역할 조회
		Map<Integer, String> roleNmMap = null;
		if(orUserBVoList!=null){
			List<String> userUidList = new ArrayList<String>();
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				userUidList.add(storedOrUserBVo.getUserUid());
			}
			if(!userUidList.isEmpty()){
				OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
				orUserRoleRVo.setUserUidList(userUidList);
				orUserRoleRVo.setQueryLang(langTypCd);
				orUserRoleRVo.setOrderBy("USER_UID, ROLE_CD");
				@SuppressWarnings("unchecked")
				List<OrUserRoleRVo> orUserRoleRVoList = (List<OrUserRoleRVo>)commonSvc.queryList(orUserRoleRVo);
				roleNmMap = toRoleNameMap(orUserRoleRVoList);
			}
		}
		
		String roleNm;
		if(orUserBVoList!=null){
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				storedOrUserBVo.setOrOdurBVo(odurMap.get(Hash.hashUid(storedOrUserBVo.getOdurUid())));
				
				roleNm = roleNmMap==null ? null : roleNmMap.get(Hash.hashUid(storedOrUserBVo.getUserUid()));
				if(roleNm!=null) storedOrUserBVo.setRoleNms(roleNm);
			}
		} else {
			orUserBVoList = new ArrayList<OrUserBVo>();
		}
		
		orUserBVoList.add(new OrUserBVo());
		orUserBVoList.add(new OrUserBVo());
		model.put("orUserBVoList", orUserBVoList);
		

		// 리소스 조회
		OrRescBVo orRescBVo = new OrRescBVo();
		orRescBVo.setOrgId(orgId);
		@SuppressWarnings("unchecked")
		List<OrRescBVo> orRescBVoList = (List<OrRescBVo>)commonSvc.queryList(orRescBVo);
		
		// 코드 리소스 model에 저장
		if(orRescBVoList!=null){
			for(OrRescBVo storedOrRescBVo : orRescBVoList){
				model.put(storedOrRescBVo.getRescId()+"_"+storedOrRescBVo.getLangTypCd(), storedOrRescBVo.getRescVa());
			}
		}
		
		// 사용자 추가 시 비밀번호 설정
		if("Y".equals(sysPlocMap.get("initPwEnable"))){
			model.put("initPwEnable", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/listUserFrm");
	}

	/** 사용자 역할명 맵으로 전환 */
	private Map<Integer, String> toRoleNameMap(List<OrUserRoleRVo> orUserRoleRVoList) {
		if(orUserRoleRVoList==null) return null;
		
		Map<Integer, String> returnMap = new HashMap<Integer, String>();
		String userUid = null;
		StringBuilder builder = null;
		for(OrUserRoleRVo orUserRoleRVo : orUserRoleRVoList){
			if(orUserRoleRVo.getUserUid().equals(userUid)){
				builder.append(", ").append(orUserRoleRVo.getRoleNm());
			} else {
				if(builder != null){
					returnMap.put(Hash.hashUid(userUid), builder.toString());
				}
				userUid = orUserRoleRVo.getUserUid();
				
				builder = new StringBuilder(40);
				builder.append(orUserRoleRVo.getRoleNm());
			}
		}
		if(userUid != null && builder != null){
			returnMap.put(Hash.hashUid(userUid), builder.toString());
		}
		return returnMap;
	}

	/** 원직자 맵으로 전환 */
	private Map<Integer, OrOdurBVo> toOdurMap(List<OrOdurBVo> orOdurBVoList){
		Map<Integer, OrOdurBVo> map = new HashMap<Integer, OrOdurBVo>();
		if(orOdurBVoList!=null){
			for(OrOdurBVo orOdurBVo : orOdurBVoList){
				map.put(Hash.hashUid(orOdurBVo.getOdurUid()), orOdurBVo);
			}
		}
		return map;
	}
	
	/** [팝업] 사용자 상세 조회 */
	@RequestMapping(value = "/pt/adm/org/setUserPop")
	public String setUserPop(HttpServletRequest request,
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		
//		model.put("userStatCdList", userStatCdList);
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo, searchOrUserBVo;
		
		String odurUid = null, compId = null; 
		
		// 사용자기본(OR_USER_B) 테이블 - 조회
		orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		if(orUserBVo!=null){
			
			model.put("orUserBVo", orUserBVo);
			
			compId = orUserBVo.getCompId();
			
			orgId = orUserBVo.getOrgId();
			odurUid = orUserBVo.getOdurUid();
			// 부서 구하기 - 파트면 부서 가져옴
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orgId, "D", langTypCd);
			model.put("orOrgTreeVo", orOrgTreeVo);
			
			// 부서 사용자 조회
			searchOrUserBVo = new OrUserBVo();
			searchOrUserBVo.setOrgId(orgId);
			searchOrUserBVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(searchOrUserBVo);
			model.put("orUserBVoList", orUserBVoList);
			
			// 사용자상태 코드 - 겸직용/원직용
			List<PtCdBVo> userStatAduCdList = new ArrayList<PtCdBVo>();
			List<PtCdBVo> userStatOrgCdList = new ArrayList<PtCdBVo>();
			
			int i, size = userStatCdList==null ? 0 : userStatCdList.size();
			String cd;
			for(i=0;i<size;i++){
				cd = userStatCdList.get(i).getCd();
				if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_ONLY, cd)){
					userStatAduCdList.add(userStatCdList.get(i));
				} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_ADU_LGIN, cd)){
					userStatAduCdList.add(userStatCdList.get(i));
				} else if(ArrayUtil.isInArray(PtConstant.USER_STAT_BOTH, cd)){
					userStatOrgCdList.add(userStatCdList.get(i));
					userStatAduCdList.add(userStatCdList.get(i));
				} else {
					userStatOrgCdList.add(userStatCdList.get(i));
				}
			}
			if("01".equals(orUserBVo.getAduTypCd())){//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				model.put("userStatCdList", userStatOrgCdList);
			} else {
				model.put("userStatCdList", userStatAduCdList);
			}
			
			// 원직자기본(OR_ODUR_B) 테이블
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			if(orOdurBVo!=null){
				model.put("orOdurBVo", orOdurBVo);
				// 성명 - 리소스 세팅
				orCmSvc.queryRescBVo(orOdurBVo.getRescId(), model);
			}
			
			// 사용자이미지상세(OR_USER_IMG_D) 테이블
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			// 겸직자 이미지 조회
			orUserImgDVo.setUserUid(userUid);
			@SuppressWarnings("unchecked")
			List<OrUserImgDVo> orUserImgDVoList = (List<OrUserImgDVo>)commonSvc.queryList(orUserImgDVo);
			if(orUserImgDVoList!=null){
				// userImgTypCd : 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
				for(OrUserImgDVo storedOrUserImgDVo : orUserImgDVoList){
					if(storedOrUserImgDVo.getImgHght()!=null && Integer.parseInt(storedOrUserImgDVo.getImgHght())>110){
						storedOrUserImgDVo.setImgHght("110");
					}
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
							if(storedOrUserImgDVo.getImgHght()!=null && Integer.parseInt(storedOrUserImgDVo.getImgHght())>110){
								storedOrUserImgDVo.setImgHght("110");
							}
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
			
			// 사용자역할관계(OR_USER_ROLE_R) 테이블
			OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
			orUserRoleRVo.setUserUid(userUid);
			@SuppressWarnings("unchecked")
			List<OrUserRoleRVo> orUserRoleRVoList = (List<OrUserRoleRVo>)commonSvc.queryList(orUserRoleRVo);
			if(orUserRoleRVoList!=null){
				boolean isFirst = true;
				StringBuilder cdBuilder = new StringBuilder(64);
				StringBuilder nmBuilder = new StringBuilder(64);
				for(OrUserRoleRVo storedOrUserRoleRVo : orUserRoleRVoList){
					if(isFirst) isFirst = false;
					else {
						cdBuilder.append(',');
						nmBuilder.append(',').append(' ');
					}
					cdBuilder.append(storedOrUserRoleRVo.getRoleCd());
					nmBuilder.append(storedOrUserRoleRVo.getRoleNm());
				}
				model.put("roleCds", cdBuilder.toString());
				model.put("roleNms", nmBuilder.toString());
			}
			
			Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(odurUid);
			model.put("odurSecuMap", odurSecuMap);
		}
		
		// 성별 코드
		List<PtCdBVo> genCdList = ptCmSvc.getCdList("GEN_CD", langTypCd, "Y");
		model.put("genCdList", genCdList);
		// 겸직구분코드
		List<PtCdBVo> aduTypCdList = ptCmSvc.getCdList("ADU_TYP_CD", langTypCd, "Y");
		model.put("aduTypCdList", aduTypCdList);
		// 결재비밀번호구분코드
		List<PtCdBVo> apvPwTypCdList = ptCmSvc.getCdList("APV_PW_TYP_CD", langTypCd, "Y");
		model.put("apvPwTypCdList", apvPwTypCdList);
		
		// 자동결재선코드
		List<PtCdBVo> autoApvLnCdList = ptCmSvc.getCdList("AUTO_APV_LN_CD", langTypCd, "Y");
		model.put("autoApvLnCdList", autoApvLnCdList);
		
		compId = orUserBVo==null ? userVo.getCompId() : orUserBVo.getCompId();
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 회사별 조직코드 사용
		if("Y".equals(sysPlocMap.get("codeByCompEnable"))){
			// 직급 코드
			List<PtCdBVo> gradeCdList = ptCmSvc.getCdListEqCompId("GRADE_CD", langTypCd, compId, "Y");
			model.put("gradeCdList", gradeCdList);
			// 직책 코드
			List<PtCdBVo> titleCdList = ptCmSvc.getCdListEqCompId("TITLE_CD", langTypCd, compId, "Y");
			model.put("titleCdList", titleCdList);
			// 직위코드
			List<PtCdBVo> positCdList = ptCmSvc.getCdListEqCompId("POSIT_CD", langTypCd, compId, "Y");
			model.put("positCdList", positCdList);
			// 직무코드
			List<PtCdBVo> dutyCdList = ptCmSvc.getCdListEqCompId("DUTY_CD", langTypCd, compId, "Y");
			model.put("dutyCdList", dutyCdList);
		} else {
			
//			String refVa1 = orUserBVo==null ? null : orUserBVo.getRefVa1();
//			// E78CEB : 한화제약
//			if((refVa1!=null && !refVa1.isEmpty()) 
//					&& "E78CEB".equals(CustConfig.CUST_CODE)){
//				// 직급 코드
//				List<PtCdBVo> gradeCdList = ptCmSvc.getFilteredCdList("GRADE_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("gradeCdList", gradeCdList);
//				// 직책 코드
//				List<PtCdBVo> titleCdList = ptCmSvc.getFilteredCdList("TITLE_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("titleCdList", titleCdList);
//				// 직위코드
//				List<PtCdBVo> positCdList = ptCmSvc.getFilteredCdList("POSIT_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("positCdList", positCdList);
//				// 직무코드
//				List<PtCdBVo> dutyCdList = ptCmSvc.getFilteredCdList("DUTY_CD", langTypCd, compId, refVa1, null, "Y");
//				model.put("dutyCdList", dutyCdList);
//			} else {
				// 직급 코드
				List<PtCdBVo> gradeCdList = ptCmSvc.getCdListByCompId("GRADE_CD", langTypCd, compId, "Y");
				model.put("gradeCdList", gradeCdList);
				// 직책 코드
				List<PtCdBVo> titleCdList = ptCmSvc.getCdListByCompId("TITLE_CD", langTypCd, compId, "Y");
				model.put("titleCdList", titleCdList);
				// 직위코드
				List<PtCdBVo> positCdList = ptCmSvc.getCdListByCompId("POSIT_CD", langTypCd, compId, "Y");
				model.put("positCdList", positCdList);
				// 직무코드
				List<PtCdBVo> dutyCdList = ptCmSvc.getCdListByCompId("DUTY_CD", langTypCd, compId, "Y");
				model.put("dutyCdList", dutyCdList);
//			}
		}
		

		// 회사별 보안등급코드 사용
		if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
			// 보안등급코드
			List<PtCdBVo> seculCdList = ptCmSvc.getCdListEqCompId("SECUL_CD", langTypCd, compId, "Y");
			model.put("seculCdList", seculCdList);
		} else {
			// 보안등급코드
			List<PtCdBVo> seculCdList = ptCmSvc.getCdListByCompId("SECUL_CD", langTypCd, compId, "Y");
			model.put("seculCdList", seculCdList);
		}
		
		// 서명방법코드
		List<PtCdBVo> signMthdCdList = ptCmSvc.getCdList("SIGN_MTHD_CD", langTypCd, "Y");
		model.put("signMthdCdList", signMthdCdList);
		
		// 모바일 사용 여부
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnabled", Boolean.TRUE);
		}
		// 조직도 DB 동기화 사용 여부
		if("Y".equals(sysPlocMap.get("orgDbSyncEnable"))){
			model.put("orgSyncEnable", Boolean.TRUE);
		}
		
		// 이메일 수정 가능 (사용자 관리)
		if("Y".equals(sysPlocMap.get("changeEmailEnable"))){
			model.put("changeEmailEnable", Boolean.TRUE);
		}
		
		// 기본도장
		model.put("dftSignMthdCd", ApConstant.DFT_SIGN_MTHD_CD);
		
		return LayoutUtil.getJspPath("/pt/adm/org/setUserPop");
	}
	
	/** [팝업] 비밀번호 변경 */
	@RequestMapping(value = "/pt/adm/org/setPwPop")
	public String setPwPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		String minLength = "6";
		model.put("minLength", minLength);
		return LayoutUtil.getJspPath("/pt/adm/org/setPwPop");
	}
	
	/** [팝업] 초기 비밀번호 변경 */
	@RequestMapping(value = "/pt/adm/org/setInitPwPop")
	public String setInitPwPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		String minLength = "6";
		model.put("minLength", minLength);
		return LayoutUtil.getJspPath("/pt/adm/org/setInitPwPop");
	}
	
	/** [팝업] 상태코드 변경 */
	@RequestMapping(value = "/pt/adm/org/setStatCdPop")
	public String setStatCdPop(HttpServletRequest request,
			ModelMap model) throws Exception {

		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 사용자상태 코드
		List<PtCdBVo> userStatCdList = ptCmSvc.getCdList("USER_STAT_CD", langTypCd, "Y");
		model.put("userStatCdList", userStatCdList);
		
		return LayoutUtil.getJspPath("/pt/adm/org/setStatCdPop");
	}
	
	/** [히든프레임] 상태코드 저장 */
	@RequestMapping(value = "/pt/adm/org/transStatCd")
	public String transStatCd(HttpServletRequest request,
			@Parameter(name="userUids", required=false) String userUids,
			@Parameter(name="userStatCd", required=false) String userStatCd,
			ModelMap model) throws Exception {

		UserVo userVo = LoginSession.getUser(request);
		OrUserBVo orUserBVo;
		OrOdurBVo orOdurBVo;
		QueryQueue queryQueue = new QueryQueue();
		
		if(userUids!=null && !userUids.isEmpty() && userStatCd !=null && !userStatCd.isEmpty()){
			for(String userUid : userUids.split(",")){
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(userUid);
				orUserBVo.setUserStatCd(userStatCd);
				orUserBVo.setModrUid(userVo.getUserUid());
				orUserBVo.setModDt("sysdate");
				queryQueue.update(orUserBVo);
				
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setOdurUid(userUid);
				orOdurBVo.setUserStatCd(userStatCd);
				orOdurBVo.setModrUid(userVo.getUserUid());
				orOdurBVo.setModDt("sysdate");
				queryQueue.update(orUserBVo);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.USER);
			commonSvc.execute(queryQueue);
			orCmSvc.setUsers();
			//ptCacheExpireSvc.checkNow(CacheConfig.USER);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			return LayoutUtil.getResultJsp();
		} else {
			LOGGER.error("fail to save user statCd - userUids:"+userUids+"   userStatCd:"+userStatCd);
			//cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.fail", request));
			return LayoutUtil.getResultJsp();
		}
	}
	
	
	
	/** [팝업] 사진 선택 */
	@RequestMapping(value = "/pt/adm/org/setImagePop")
	public String setImagePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/pt/adm/org/setImagePop");
	}
	
	/** [히든프레임] 사진,도장,사인 업로드 */
	@RequestMapping(value = "/pt/adm/org/transImage")
	public String transImage(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			
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
			
			QueryQueue queryQueue = new QueryQueue();
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			VoUtil.fromMap(orUserImgDVo, paramMap);
			
			File imgFile = fileMap.get("photo");
			if(fileMap.get("photo")!=null){
				BufferedImage bimg = ImageIO.read(imgFile);
				orUserImgDVo.setImgWdth(Integer.toString(bimg.getWidth()));
				orUserImgDVo.setImgHght(Integer.toString(bimg.getHeight()));
				orUserImgDVo.setImgPath(distPath);
			}
			
			UserVo userVo = LoginSession.getUser(request);
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
	
	/** [팝업] 겸직 파견직 선택 */
	@RequestMapping(value = "/pt/adm/org/setAduTypPop")
	public String setAduTypPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 겸직구분코드
		List<PtCdBVo> aduTypCdList = ptCmSvc.getCdList("ADU_TYP_CD", langTypCd, "Y");
		model.put("aduTypCdList", aduTypCdList);
		return LayoutUtil.getJspPath("/pt/adm/org/setAduTypPop");
	}
	
	/** [팝업] 역할 설정 */
	@RequestMapping(value = "/pt/adm/org/setRoleCdPop")
	public String setRoleCdPop(HttpServletRequest request,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 역할코드
		List<PtCdBVo> roleCdList = ptCmSvc.getCdList("ROLE_CD", langTypCd, "Y");
		model.put("roleCdList", roleCdList);
		
		// javascript 로 처리
//		// 사용자역할관계(OR_USER_ROLE_R) 테이블 - 조회
//		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
//		orUserRoleRVo.setUserUid(userUid);
//		@SuppressWarnings("unchecked")
//		List<OrUserRoleRVo> orUserRoleRVoList = (List<OrUserRoleRVo>)commonSvc.queryList(orUserRoleRVo);
//		int i, size = orUserRoleRVoList==null ? 0 : orUserRoleRVoList.size();
//		for(i=0;i<size;i++){
//			orUserRoleRVo = orUserRoleRVoList.get(i);
//			model.put(orUserRoleRVo.getRoleCd(), Boolean.TRUE);
//		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/setRoleCdPop");
	}
	
	/** [히든프레임] 조직목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pt/adm/org/transOrgList")
	public String transOrgList(HttpServletRequest request,
			@Parameter(name="compId", required=false)String compId,
			@Parameter(name="orgPid", required=false)String orgPid,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 조직기본(OR_ORG_B) 테이블 VO
			OrOrgBVo orOrgBVo, parentOrOrgBVo=null, subOrOrgBVo;
			// 사용자기본(OR_USER_B) 테이블 VO
			OrUserBVo orUserBVo;
			// 조직결재상세(OR_ORG_APV_D) 테이블 VO
			OrOrgApvDVo orOrgApvDVo;
			// 관인상세(OR_OFSE_D) 테이블 VO
			OrOfseDVo orOfseDVo;
			// 조직연락처상세(OR_ORG_CNTC_D) 테이블 VO
			OrOrgCntcDVo orOrgCntcDVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			List<OrOrgApvDVo> orOrgApvDVoList;
			int count, i, size;
			String orgId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			for(i=0;i<delCds.length;i++){
				
				orgId = delCds[i];
				// 해당 부서에 사용자가 속해 있는지 조회
				orUserBVo = new OrUserBVo();
				orUserBVo.setOrgId(orgId);
				count = commonSvc.count(orUserBVo);
				if(count>0){
					// or.msg.not.del.withUser=사용자가 있는 조직은 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("or.msg.not.del.withUser", request);
					LOGGER.error("fail to save org list - orgId:"+orgId+"\n"+msg);
					throw new CmException(msg);
				}
				
				// 삭제될 부서가 [대리 문서과]에 지정되어 있으면 - [대리 문서과] 지정 해제 - 실제 문서과에 [문서과] 지정
				orOrgApvDVo = new OrOrgApvDVo();
				orOrgApvDVo.setAgnCrdOrgId(orgId);
				orOrgApvDVoList = (List<OrOrgApvDVo>)commonSvc.queryList(orOrgApvDVo);
				if(orOrgApvDVoList!=null){
					for(OrOrgApvDVo storedOrOrgApvDVo : orOrgApvDVoList){
						orOrgApvDVo = new OrOrgApvDVo();
						orOrgApvDVo.setOrgId(storedOrOrgApvDVo.getOrgId());
						orOrgApvDVo.setAgnCrdOrgId("");
						if(storedOrOrgApvDVo.getCrdOrgId()!=null && !storedOrOrgApvDVo.getCrdOrgId().isEmpty()
								&& !storedOrOrgApvDVo.getCrdOrgId().equals(orgId)){
							orOrgApvDVo.setRealCrdOrgId(storedOrOrgApvDVo.getCrdOrgId());
						} else {
							orOrgApvDVo.setRealCrdOrgId("");
						}
						queryQueue.update(orOrgApvDVo);
					}
				}
				
				// 삭제될 부서가 [문서과]에 지정되어 있으면 - [문서과] 지정 해제 - 대리 문서과는 없다고 가정하고 삭제함
				orOrgApvDVo = new OrOrgApvDVo();
				orOrgApvDVo.setCrdOrgId(orgId);
				orOrgApvDVoList = (List<OrOrgApvDVo>)commonSvc.queryList(orOrgApvDVo);
				if(orOrgApvDVoList!=null){
					for(OrOrgApvDVo storedOrOrgApvDVo : orOrgApvDVoList){
						orOrgApvDVo = new OrOrgApvDVo();
						orOrgApvDVo.setOrgId(storedOrOrgApvDVo.getOrgId());
						orOrgApvDVo.setCrdOrgId("");
						orOrgApvDVo.setAgnCrdOrgId("");
						orOrgApvDVo.setRealCrdOrgId("");
						queryQueue.update(orOrgApvDVo);
					}
				}
				
				// 조직연락처상세(OR_ORG_CNTC_D) 테이블 - 삭제
				orOrgCntcDVo = new OrOrgCntcDVo();
				orOrgCntcDVo.setOrgId(orgId);
				queryQueue.delete(orOrgCntcDVo);
				// 조직결재상세(OR_ORG_APV_D) 테이블 - 삭제
				orOrgApvDVo = new OrOrgApvDVo();
				orOrgApvDVo.setOrgId(orgId);
				queryQueue.delete(orOrgCntcDVo);
				// 관인상세(OR_OFSE_D) 테이블 - 삭제
				orOfseDVo = new OrOfseDVo();
				orOfseDVo.setOrgId(orgId);
				queryQueue.delete(orOfseDVo);
				// 조직기본(OR_ORG_B) 테이블 - 삭제
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(orgId);
				queryQueue.delete(orOrgBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			boolean isRoot = "ROOT".equals(orgPid), isNew = false;
			
			List<String> subOrgList;
			
			// 최상위 조직이 아닐때
			if(!isRoot){
				// 부모조직의 회사ID를 조회해서 하위의 회사ID에 세팅함
				parentOrOrgBVo = new OrOrgBVo();
				parentOrOrgBVo.setOrgId(orgPid);
				parentOrOrgBVo = (OrOrgBVo)commonSvc.queryVo(parentOrOrgBVo);
				if(parentOrOrgBVo == null){
					// or.msg.noParentDept=상위 부서를 확인 할 수 없습니다.
					throw new CmException("or.msg.noParentDept", request);
				}
			}
			
			// 조직기본(OR_ORG_B) 테이블
			List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)VoUtil.bindList(request, OrOrgBVo.class, new String[]{"valid"});
			size = orOrgBVoList==null ? 0 : orOrgBVoList.size();
			OrOrgBVo storedOrOrgBVo = null;
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				orCmSvc.collectOrRescBVoList(request, queryQueue, orOrgBVoList, "valid", "rescId", "orgNm");
			}
			
			// 하위조직을 맵 형태로 리턴
			Map<Integer, OrOrgBVo> subMap = ptOrgSvc.querySubOrgMap(orgPid);
			OrOrgTreeVo orOrgTreeVo;
			for(i=0;i<size;i++){
				orOrgBVo = orOrgBVoList.get(i);
				orOrgBVo.setModDt("sysdate");
				orOrgBVo.setModrUid(userVo.getUserUid());
				
				// 조직ID 가 없으면 생성함
				if(orOrgBVo.getOrgId()==null || orOrgBVo.getOrgId().isEmpty()){
					orOrgBVo.setOrgId(orCmSvc.createId("OR_ORG_B"));
					orOrgBVo.setRegDt("sysdate");
					orOrgBVo.setRegrUid(userVo.getUserUid());
					isNew = true;
					
					if("P".equals(orOrgBVo.getOrgTypCd())){
						// 파트면 - 상위 조직 중 첫번째 부서를 조회함(캐쉬)
						orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orOrgBVo.getOrgPid(), "D", langTypCd);
						orOrgBVo.setDeptId(orOrgTreeVo.getOrgId());
						orOrgBVo.setDeptRescId(orOrgTreeVo.getRescId());
					} else {
						// 회사, 기관, 부서 - 부서ID를 현재 조직ID로 세팅
						orOrgBVo.setDeptId(orOrgBVo.getOrgId());
						orOrgBVo.setDeptRescId(orOrgBVo.getRescId());
					}
				}
				
				// 저장된 데이터 가져오기
				if(!isNew && subMap!=null){
					storedOrOrgBVo = subMap.get(Hash.hashUid(orOrgBVo.getOrgId()));
				} else {
					storedOrOrgBVo = null;
				}

				if(isRoot){
					
					// 회사ID 변경 체크
					if(storedOrOrgBVo != null){

						// 회사ID가 변경 되었으면
						if(storedOrOrgBVo.getCompId().equals(orOrgBVo.getCompId())){
							
							// 서브 조직ID 목록을 가져옴
							subOrgList = orCmSvc.getOrgSubIdList(orOrgBVo.getOrgId(), langTypCd);
							
							if(subOrgList!=null && !subOrgList.isEmpty()){
								// 서브 조직들의 "회사ID" 일괄 변경
								subOrOrgBVo = new OrOrgBVo();
								subOrOrgBVo.setCompId(orOrgBVo.getCompId());
								subOrOrgBVo.setOrgIdList(subOrgList);
								queryQueue.update(subOrOrgBVo);
								
								// 사용자의 "회사ID" 일괄 변경
								orUserBVo = new OrUserBVo();
								orUserBVo.setCompId(orOrgBVo.getCompId());
								orUserBVo.setOrgIdList(subOrgList);
								queryQueue.update(orUserBVo);
							}
						}
					}
					
				} else {// 최상위가 아니면
					// 부모 조직의 회사ID를 서브조직에 세팅함
					orOrgBVo.setCompId(parentOrOrgBVo.getCompId());
				}
				
				
				if(storedOrOrgBVo != null){
					
					// 현재 파트로 지정 되었으나 저장된 것은 파트가 아닐 경우
					// - 하위조직, 하위조직의 사용자 : 부서ID,부서리소스ID 를 - 상위 부서의 부서ID, 부서리소스ID로 변경
					if("P".equals(orOrgBVo.getOrgTypCd()) && !"P".equals(storedOrOrgBVo.getOrgTypCd())){

						// 서브 조직ID 목록을 가져옴
						subOrgList = orCmSvc.getOrgSubIdList(orOrgBVo.getOrgId(), langTypCd);
						if(subOrgList==null){
							subOrgList = new ArrayList<String>();
						}
						subOrgList.add(orOrgBVo.getOrgId());
						
						// 하위조직의 부서ID,부서리소스ID - 일괄 변경
						subOrOrgBVo = new OrOrgBVo();
						subOrOrgBVo.setOrgTypCd("P");
						subOrOrgBVo.setDeptId(parentOrOrgBVo.getDeptId());
						subOrOrgBVo.setDeptRescId(parentOrOrgBVo.getDeptRescId());
						subOrOrgBVo.setOrgIdList(subOrgList);
						subOrOrgBVo.setModDt("sysdate");
						subOrOrgBVo.setModrUid(userVo.getUserUid());
						queryQueue.update(subOrOrgBVo);
						
						// 하위조직 사용자의 부서ID,부서리소스ID - 일괄 변경
						orUserBVo = new OrUserBVo();
						orUserBVo.setDeptId(parentOrOrgBVo.getDeptId());
						orUserBVo.setDeptRescId(parentOrOrgBVo.getDeptRescId());
						orUserBVo.setOrgIdList(subOrgList);
						orUserBVo.setModDt("sysdate");
						orUserBVo.setModrUid(userVo.getUserUid());
						queryQueue.update(orUserBVo);
					}
					
					// 현재 파트가 아니지만 저장된 것은 파트인 경우
					// - 하위조직, 하위조직의 사용자 : 부서ID,부서리소스ID 를 - 현재 부서의 부서ID, 부서리소스ID로 변경
					if(!"P".equals(orOrgBVo.getOrgTypCd()) && "P".equals(storedOrOrgBVo.getOrgTypCd())){
						
						// 서브 조직ID 목록을 가져옴
						subOrgList = orCmSvc.getOrgSubIdList(orOrgBVo.getOrgId(), langTypCd);
						if(subOrgList==null){
							subOrgList = new ArrayList<String>();
						}
						subOrgList.add(orOrgBVo.getOrgId());

						// 하위조직의 부서ID,부서리소스ID - 일괄 변경
						subOrOrgBVo = new OrOrgBVo();
						subOrOrgBVo.setDeptId(orOrgBVo.getOrgId());
						subOrOrgBVo.setDeptRescId(orOrgBVo.getRescId());
						subOrOrgBVo.setOrgIdList(subOrgList);
						subOrOrgBVo.setModDt("sysdate");
						subOrOrgBVo.setModrUid(userVo.getUserUid());
						queryQueue.update(subOrOrgBVo);
						
						// 하위조직 사용자의 부서ID,부서리소스ID - 일괄 변경
						orUserBVo = new OrUserBVo();
						orUserBVo.setDeptId(orOrgBVo.getOrgId());
						orUserBVo.setDeptRescId(orOrgBVo.getRescId());
						orUserBVo.setOrgIdList(subOrgList);
						orUserBVo.setModDt("sysdate");
						orUserBVo.setModrUid(userVo.getUserUid());
						queryQueue.update(orUserBVo);
					}
				}
				
				// 시스템조직여부 - 현재 사용 안함
				if(orOrgBVo.getSysOrgYn()==null){
					orOrgBVo.setSysOrgYn("N");
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.ORG);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.ORG);
			
			// 사용자 조직도 동기화 PUSH - 조직도
			if(pushSyncSvc.hasSync()){
				pushSyncSvc.syncOrg();
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+orgPid+"');");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [AJAX] 조직 붙여 넣기(오른쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/transOrgPasteAjx")
	public String transOrgPasteAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			UserVo userVo = LoginSession.getUser(request);
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String orgPid = (String)jsonObject.get("orgPid"), orgId;
			String toOrgId = (String)jsonObject.get("toOrgId");
			JSONArray jsonArray = (JSONArray)jsonObject.get("ids");
			
			if(orgPid==null || orgPid.isEmpty() || toOrgId==null || toOrgId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("fail to paste Org - from:"+orgPid+"  to:"+toOrgId+"\n"+msg);
				throw new CmException(msg);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			OrOrgBVo orOrgBVo, fromOrOrgBVo, toOrOrgBVo, storedOrOrgBVo, subOrOrgBVo;
			OrUserBVo subOrUserBVo;
			
			// 옮겨 갈 곳
			String toCompId=null, toOrgTypCd=null, toDeptId = null, toDeptRescId = null;
			// from
			fromOrOrgBVo = new OrOrgBVo();
			fromOrOrgBVo.setOrgId(orgPid);
			fromOrOrgBVo = (OrOrgBVo)commonSvc.queryVo(fromOrOrgBVo);
			if(fromOrOrgBVo == null){
				// or.msg.noParentFromDept=옮겨질 부서의 상위 부서를 확인 할 수 없습니다.
				throw new CmException("or.msg.noParentFromDept", request);
			}
			
			// to
			toOrOrgBVo = new OrOrgBVo();
			toOrOrgBVo.setOrgId(toOrgId);
			toOrOrgBVo = (OrOrgBVo)commonSvc.queryVo(toOrOrgBVo);
			if(toOrOrgBVo == null){
				// or.msg.noToDept=옮겨갈 부서를 확인 할 수 없습니다.
				throw new CmException("or.msg.noToDept", request);
			}
			toOrgTypCd = toOrOrgBVo.getOrgTypCd();
			toCompId = toOrOrgBVo.getCompId();
			toDeptId = toOrOrgBVo.getDeptId();
			toDeptRescId = toOrOrgBVo.getDeptRescId();
			
			// 문서과 / 대리문서과 체크
			OrOrgTreeVo fromTreeVo = orCmSvc.getOrgByOrgTypCd(orgPid, "G", langTypCd);
			OrOrgTreeVo toTreeVo = orCmSvc.getOrgByOrgTypCd(toOrgId, "G", langTypCd);
			OrOrgApvDVo orOrgApvDVo = null;// 옮겨 가는곳(from) 의 기관 - 문서과 체크용
			List<String> subOfMoveList = null;
			boolean matched;
			// 서로 다른 기관으로 붙여 넣기 되었 을 때
			if(fromTreeVo!=null && toTreeVo!=null && !fromTreeVo.getOrgId().equals(toTreeVo.getOrgId())){
				orOrgApvDVo = new OrOrgApvDVo();
				orOrgApvDVo.setOrgId(fromTreeVo.getOrgId());
				orOrgApvDVo = (OrOrgApvDVo)commonSvc.queryVo(orOrgApvDVo);
				if(orOrgApvDVo != null){
					if(orOrgApvDVo.getRealCrdOrgId()==null || orOrgApvDVo.getRealCrdOrgId().isEmpty()){
						orOrgApvDVo = null;
					} else {
						OrOrgApvDVo newOrOrgApvDVo = new OrOrgApvDVo();
						newOrOrgApvDVo.setOrgId(orOrgApvDVo.getOrgId());
						newOrOrgApvDVo.setCrdOrgId(orOrgApvDVo.getCrdOrgId());
						newOrOrgApvDVo.setAgnCrdOrgId(orOrgApvDVo.getAgnCrdOrgId());
						newOrOrgApvDVo.setRealCrdOrgId(orOrgApvDVo.getRealCrdOrgId());
						orOrgApvDVo = newOrOrgApvDVo;
					}
				}
				if(orOrgApvDVo != null && (orOrgApvDVo.getRealCrdOrgId()==null || orOrgApvDVo.getRealCrdOrgId().isEmpty())){
					orOrgApvDVo = null;
				}
			}
			
			// 옮겨 갈 곳, 옮겨 가는곳 - 다른회사ID
			boolean diffComp = !fromOrOrgBVo.getCompId().equals(toOrOrgBVo.getCompId());
			List<String> subOrgList = null;
			
			// 최대 정렬순서 쿼리
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			ptxSortOrdrChnVo.setTabId("OR_ORG_B");
			ptxSortOrdrChnVo.setPkCol("ORG_PID");
			ptxSortOrdrChnVo.setPk(toOrgId);
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			if(maxSortOrdr==null) maxSortOrdr = 0;
			
			int i, size = jsonArray==null ? 0 : jsonArray.size();
			for(i=0;i<size;i++){
				
				// 붙여 넣을 조직ID
				orgId = (String)jsonArray.get(i);
				
				// 문서과 / 대리문서과 체크
				if(orOrgApvDVo != null && orOrgApvDVo.getRealCrdOrgId()!=null && !orOrgApvDVo.getRealCrdOrgId().isEmpty()){
					matched = orgId.equals(orOrgApvDVo.getRealCrdOrgId());
					subOfMoveList = matched ? null : orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(matched || (subOfMoveList!=null && subOfMoveList.contains(orOrgApvDVo.getRealCrdOrgId()))){
						if(orOrgApvDVo.getRealCrdOrgId().equals(orOrgApvDVo.getAgnCrdOrgId())
								&& orOrgApvDVo.getCrdOrgId()!=null && !orOrgApvDVo.getCrdOrgId().isEmpty()){
							orOrgApvDVo.setAgnCrdOrgId("");
							orOrgApvDVo.setAgnCrdRescId("");
							orOrgApvDVo.setRealCrdOrgId(orOrgApvDVo.getCrdOrgId());
						} else {
							orOrgApvDVo.setCrdOrgId("");
							orOrgApvDVo.setCrdRescId("");
							orOrgApvDVo.setAgnCrdOrgId("");
							orOrgApvDVo.setAgnCrdRescId("");
							orOrgApvDVo.setRealCrdOrgId("");
						}
					}
				}
				
				// 옮겨갈 데이터 조회 - sortOrdr 조회용 - 옮겨지지 않는 데이터중 sortOrdr 가 해당 건 보다 높은 것은 하나씩 줄여 빈 곳 없게함
				storedOrOrgBVo = new OrOrgBVo();
				storedOrOrgBVo.setOrgId(orgId);
				storedOrOrgBVo = (OrOrgBVo)commonSvc.queryVo(storedOrOrgBVo);
				if(storedOrOrgBVo==null){
					// or.msg.noMoveDept=옮길 부서를 확인 할 수 없습니다.
					throw new CmException("or.msg.noMoveDept", request);
				}
				
				// 옮겨 가는 조직의 sortOrdr 하나씩 줄이기
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("OR_ORG_B");
				ptxSortOrdrChnVo.setPkCol("ORG_PID");
				ptxSortOrdrChnVo.setPk(orgPid);
				// i 빼는 이유 : 루프 돌면서 이미 이전 쿼리에서 i 갯수 만큼 sortOrdr 이 변경 되었기 때문에 보정해야 맞음
				ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedOrOrgBVo.getSortOrdr()) - i);
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				maxSortOrdr++;
				
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(orgId);
				orOrgBVo.setOrgPid(toOrgId);
				orOrgBVo.setSortOrdr(maxSortOrdr.toString());
				if(diffComp) orOrgBVo.setCompId(toCompId);
				
				// 옮기는곳이 파트일 경우 - 옮기는곳 : 옮긴뒤 해당 부서의 부모
				// 해당부서(옮기는 부서)가 파트인 경우
				if("P".equals(toOrgTypCd) || "P".equals(storedOrOrgBVo.getOrgTypCd())){
					
//					orOrgBVo.setDeptId(toDeptId);
//					orOrgBVo.setDeptRescId(toDeptRescId);
//					if("P".equals(toOrgTypCd)) orOrgBVo.setOrgTypCd("P");
					
					// 서브 조직ID 목록을 가져옴
					subOrgList = orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(subOrgList==null){
						subOrgList = new ArrayList<String>();
					}
					subOrgList.add(orgId);
					
					// 서브 조직들의 "회사ID" 일괄 변경
					subOrOrgBVo = new OrOrgBVo();
					// 옮기는곳이 파트일 경우 - 하위는 파트 세팅
					if("P".equals(toOrgTypCd)){
						subOrOrgBVo.setOrgTypCd("P");
					}
					subOrOrgBVo.setDeptId(toDeptId);
					subOrOrgBVo.setDeptRescId(toDeptRescId);
					// 회사가 바뀐경우 - 회사 세팅
					if(diffComp) subOrOrgBVo.setCompId(toCompId);
					subOrOrgBVo.setOrgIdList(subOrgList);
					subOrOrgBVo.setModDt("sysdate");
					subOrOrgBVo.setModrUid(userVo.getUserUid());
					queryQueue.update(subOrOrgBVo);
					
					// 사용자의 "회사ID" 일괄 변경
					subOrUserBVo = new OrUserBVo();
					subOrUserBVo.setDeptId(toDeptId);
					subOrUserBVo.setDeptRescId(toDeptRescId);
					subOrUserBVo.setModDt("sysdate");
					subOrUserBVo.setModrUid(userVo.getUserUid());
					// 회사가 바뀐경우 - 회사 세팅
					if(diffComp) subOrUserBVo.setCompId(toCompId);
					subOrUserBVo.setOrgIdList(subOrgList);
					queryQueue.update(subOrUserBVo);
					
				// 회사가 변경된 경우 - 파트인 경우는 회사 변경여부 비교해서 처리 하였음
				} else if(diffComp){
					
					// 서브 조직ID 목록을 가져옴
					subOrgList = orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(subOrgList==null){
						subOrgList = new ArrayList<String>();
					}
					subOrgList.add(orgId);
					
					// 서브 조직들의 "회사ID" 일괄 변경
					subOrOrgBVo = new OrOrgBVo();
					subOrOrgBVo.setCompId(toCompId);
					subOrOrgBVo.setOrgIdList(subOrgList);
					subOrOrgBVo.setModDt("sysdate");
					subOrOrgBVo.setModrUid(userVo.getUserUid());
					queryQueue.update(subOrOrgBVo);
					
					// 사용자의 "회사ID" 일괄 변경
					subOrUserBVo = new OrUserBVo();
					subOrUserBVo.setCompId(toCompId);
					subOrUserBVo.setOrgIdList(subOrgList);
					subOrUserBVo.setModDt("sysdate");
					subOrUserBVo.setModrUid(userVo.getUserUid());
					queryQueue.update(subOrUserBVo);
				}

				orOrgBVo.setModDt("sysdate");
				orOrgBVo.setModrUid(userVo.getUserUid());
				queryQueue.update(orOrgBVo);
				
			}
			
			// 문서과 / 대리문서과 체크
			if(orOrgApvDVo != null){
				queryQueue.update(orOrgApvDVo);
			}
			
			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.ORG);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.ORG);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("orgId", toOrgId);
				
				// 사용자 조직도 동기화 PUSH - 조직도
				if(pushSyncSvc.hasSync()){
					pushSyncSvc.syncOrg();
				}
			} else {
				//cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] 사용자 붙여 넣기(오른쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/transUserPasteAjx")
	public String transUserPasteAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String orgId = (String)jsonObject.get("orgId"), userUid;
			String toOrgId = (String)jsonObject.get("toOrgId");
			JSONArray jsonArray = (JSONArray)jsonObject.get("ids");
			
			if(orgId==null || orgId.isEmpty() || toOrgId==null || toOrgId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("fail to paste User - orgId:"+orgId+"  toOrgId:"+toOrgId+"\n"+msg);
				throw new CmException(msg);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			OrUserBVo orUserBVo, storedOrUserBVo;
			
			// 옮겨 갈곳 부서의 회사ID, 리소스ID 조회
			String compId=null, orgRescId=null, deptId=null, deptRescId=null;
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(toOrgId);
			orOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
			if(orOrgBVo==null){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("fail to paste User - no data(OR_ORG_B) - toOrgId:"+toOrgId+"\n"+msg);
				throw new CmException(msg);
			}
			compId = orOrgBVo.getCompId();
			orgRescId = orOrgBVo.getRescId();
			deptId = orOrgBVo.getDeptId();
			deptRescId = orOrgBVo.getDeptRescId();
			
			// 현재일 쿼리
			String today = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			
			// 최대 정렬순서 쿼리
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			ptxSortOrdrChnVo.setTabId("OR_USER_B");
			ptxSortOrdrChnVo.setPkCol("ORG_ID");
			ptxSortOrdrChnVo.setPk(toOrgId);
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			if(maxSortOrdr==null) maxSortOrdr = 0;
			
			String aduTypCd = (String)jsonObject.get("aduTypCd");
			boolean isMove = aduTypCd==null || aduTypCd.isEmpty();
			
			// 사용자 조직도 동기화 PUSH - 사용자
			StringBuilder userUidBuilder = new StringBuilder(64);
			boolean emptyUserUid = true;
			
			int i, size = jsonArray==null ? 0 : jsonArray.size();
			for(i=0;i<size;i++){
				
				// 붙여 넣을 조직ID
				userUid = (String)jsonArray.get(i);
				
				// 옮겨갈 데이터 조회 - sortOrdr 조회용 - 옮겨지지 않는 데이터중 sortOrdr 가 해당 건 보다 높은 것은 하나씩 줄여 빈 곳 없게함
				storedOrUserBVo = new OrUserBVo();
				storedOrUserBVo.setUserUid(userUid);
				storedOrUserBVo = (OrUserBVo)commonSvc.queryVo(storedOrUserBVo);
				
				if(storedOrUserBVo!=null){
					
					// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
					if("01".equals(storedOrUserBVo.getAduTypCd())){
						// 사용자 조직도 동기화 PUSH - 사용자
						if(emptyUserUid) emptyUserUid = false;
						else userUidBuilder.append(',');
						userUidBuilder.append(userUid);
					}
					
					// 이동 이면
					if(isMove){
						
						// 옮겨 가는 조직의 sortOrdr 하나씩 줄이기
						ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
						ptxSortOrdrChnVo.setTabId("OR_USER_B");
						ptxSortOrdrChnVo.setPkCol("ORG_ID");
						ptxSortOrdrChnVo.setPk(orgId);
						// i 빼는 이유 : 루프 돌면서 이미 이전 쿼리에서 i 갯수 만큼 sortOrdr 이 변경 되었기 때문에 보정해야 맞음
						ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(storedOrUserBVo.getSortOrdr()) - i);
						ptxSortOrdrChnVo.setChnVa(-1);
						ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
						queryQueue.update(ptxSortOrdrChnVo);
						
						maxSortOrdr++;
						
						orUserBVo = new OrUserBVo();
						orUserBVo.setUserUid(userUid);
						orUserBVo.setCompId(compId);
						orUserBVo.setOrgId(toOrgId);
						orUserBVo.setOrgRescId(orgRescId);
						orUserBVo.setDeptId(deptId);// 부서ID
						orUserBVo.setDeptRescId(deptRescId);// 부서리소스ID
						orUserBVo.setSortOrdr(maxSortOrdr.toString());
						orUserBVo.setModDt("sysdate");
						orUserBVo.setModrUid(userVo.getUserUid());
						queryQueue.update(orUserBVo);
						
					// 겸직 파견 이면
					} else {
						
						maxSortOrdr++;
						
						orUserBVo = new OrUserBVo();
						orUserBVo.setUserUid(orCmSvc.createId("OR_USER_B"));
						orUserBVo.setOdurUid(storedOrUserBVo.getOdurUid());
						orUserBVo.setCompId(compId);
						orUserBVo.setOrgId(toOrgId);
						orUserBVo.setOrgRescId(orgRescId);
						orUserBVo.setDeptId(deptId);// 부서ID
						orUserBVo.setDeptRescId(deptRescId);// 부서리소스ID
						orUserBVo.setSortOrdr(maxSortOrdr.toString());
						orUserBVo.setUserNm(storedOrUserBVo.getUserNm());
						orUserBVo.setRescId(storedOrUserBVo.getRescId());
						orUserBVo.setStrtYmd(today);
						orUserBVo.setAduTypCd(aduTypCd);//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
						orUserBVo.setUserStatCd("02");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
						orUserBVo.setRegDt("sysdate");
						orUserBVo.setRegrUid(userVo.getUserUid());
						orUserBVo.setModDt("sysdate");
						orUserBVo.setModrUid(userVo.getUserUid());
						
						// 회사가 같은 경우
						if(compId.equals(storedOrUserBVo.getCompId())){
							orUserBVo.setGradeCd(storedOrUserBVo.getGradeCd());//직급코드
							orUserBVo.setTitleCd(storedOrUserBVo.getTitleCd());//직책코드
							orUserBVo.setPositCd(storedOrUserBVo.getPositCd());//직위코드
							//orUserBVo.setDutyCd(storedOrUserBVo.getDutyCd());//직무코드
							//orUserBVo.setSeculCd(storedOrUserBVo.getSeculCd());//보안등급코드
						}
						
						queryQueue.insert(orUserBVo);
					}
					
				}
			}
			
			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.ORG);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.ORG);
				
				// 사용자 조직도 동기화 PUSH - 사용자
				if(pushSyncSvc.hasSync()){
					pushSyncSvc.syncUsers(userUidBuilder.toString(), null, null, null);
				}
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("orgId", toOrgId);
			} else {
				//cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [히든프레임] 사용자 목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pt/adm/org/transUserList")
	public String transUserList(HttpServletRequest request,
			@Parameter(name="orgId", required=false)String orgId,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			
			UserVo userVo = LoginSession.getUser(request);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 라이센스가 있는 사용자 목록
			List<String> licensedUserList = null;
			int licCnt = 0;
			String userStatCd;
			// 회사별 라이센스 정책
			boolean licenseByCompEnable = "Y".equals(sysPlocMap.get("licenseByCompEnable"));
			if(licenseByCompEnable){
				licensedUserList = orCmSvc.getActiveUserList(userVo.getCompId());
				PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(userVo.getCompId(), userVo.getLangTypCd());
				if(ptCompBVo!=null && ptCompBVo.getLicCnt()!=null && !ptCompBVo.getLicCnt().isEmpty()){
					licCnt = Integer.parseInt(ptCompBVo.getLicCnt());
				}
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo orUserBVo, storedOrUserBVo;
			// 원직자기본(OR_ODUR_B) 테이블
			OrOdurBVo orOdurBVo, storedOrOdurBVo;
			// 사용자기본(OR_USER_B) 테이블
			List<OrUserBVo> orUserBVoDelList;
			
			// 사용자 조직도 동기화 PUSH - 사용자
			StringBuilder delEmailBuilder = new StringBuilder(128);
			StringBuilder delUserUidBuilder = new StringBuilder(128);
			StringBuilder delRidBuilder = new StringBuilder(128);
			StringBuilder userUidBuilder = new StringBuilder(128);
			boolean delEmailFirst = true;
			boolean delUserUidFirst = true;
			boolean delRidFirst = true;
			boolean userUidFirst = true;
			
			List<OrUserPinfoDVo> orUserPinfoDVoList = new ArrayList<OrUserPinfoDVo>();
			OrUserPinfoDVo orUserPinfoDVo;
			String rid, email;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String userUid, odurUid, aduTypCd;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			for(i=0;i<delCds.length;i++){
				
				userUid = delCds[i];
				// 해당 부서에 사용자가 속해 있는지 조회
				storedOrUserBVo = new OrUserBVo();
				storedOrUserBVo.setUserUid(userUid);
				storedOrUserBVo = (OrUserBVo)commonSvc.queryVo(storedOrUserBVo);
				if(storedOrUserBVo==null) continue;
				
				aduTypCd = storedOrUserBVo.getAduTypCd();//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				
				// 겸직자 정보 삭제 - PK가 userUid 인 테이블 데이터 삭제
				deleteUserByUserUid(queryQueue, userUid);
				
				//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				if("01".equals(aduTypCd)){
					// 원직자UID
					odurUid = storedOrUserBVo.getOdurUid();
					// 원직자 정보 삭제 - PK가 odurUid 인 테이블 데이터 삭제
					deleteOdurByOdurUid(queryQueue, odurUid);
					
					// 해당 원직자의 겸직자가 있으면 같이 삭제
					orUserBVo = new OrUserBVo();
					orUserBVo.setOdurUid(odurUid);
					orUserBVoDelList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
					
					// 해당 원직자의 겸직자가 있으면 - 원직자 제외하고(이미 삭제됨) 삭제
					if(orUserBVoDelList!=null){
						for(OrUserBVo orUserBVo2 : orUserBVoDelList){
							if(!"01".equals(orUserBVo2.getAduTypCd())){//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
								// 겸직자 정보 삭제 - PK가 userUid 인 테이블 데이터 삭제
								deleteUserByUserUid(queryQueue, orUserBVo2.getUserUid());
							}
						}
					}
					
					// 계정 동기화 - 메일계정 기준 - 삭제 처리
					if(pushSyncSvc.hasEmailDel()){
						// 메일 동기화 - 삭제 ID
						orUserPinfoDVo = new OrUserPinfoDVo();
						orUserPinfoDVo.setOdurUid(odurUid);
						orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
						if(orUserPinfoDVo!=null){
							orCmSvc.decryptUserPinfo(orUserPinfoDVo);
							email = orUserPinfoDVo.getEmail();
							if(delEmailFirst) delEmailFirst = false;
							else delEmailBuilder.append(',');
							delEmailBuilder.append(email);
						}
					}
					
					// 계정 동기화 - 참조ID 기준 - 삭제 처리
					if(pushSyncSvc.hasRidDel()){
						storedOrOdurBVo = new OrOdurBVo();
						storedOrOdurBVo.setOdurUid(odurUid);
						storedOrOdurBVo = (OrOdurBVo)commonSvc.queryVo(storedOrOdurBVo);
						rid = storedOrOdurBVo.getRid();
						if(delRidFirst) delRidFirst = false;
						else delRidBuilder.append(',');
						delRidBuilder.append(rid);
					}
					
					// 계정 동기화 - userUid 기준 - 삭제 처리
					if(pushSyncSvc.hasUserUidDel()){
						if(delUserUidFirst) delUserUidFirst = false;
						else delUserUidBuilder.append(',');
						delUserUidBuilder.append(userUid);
					}
					
					// 회사별 라이센스 처리
					if(licenseByCompEnable && licensedUserList!=null){
						licensedUserList.remove(userUid);
					}
				}
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			String initPw = (String)request.getSession().getAttribute("initPw");
			if(initPw!=null) request.getSession().removeAttribute("initPw");
			
			// 원직자기본(OR_ODUR_B) 테이블 - 저장된 원직자 정보 조회 : 로그인 아이디 중복 체크
			String lginId;
			orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOrgId(orgId);
			List<OrOdurBVo> storedOrOdurBVoList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
			// 해당 부서의 로그인아이디 맵 - key : odurUid(원직자UID)
			Map<String, String> storedLognIdMap = new HashMap<String, String>();
			size = storedOrOdurBVoList==null ? 0 : storedOrOdurBVoList.size();
			for(i=0;i<size;i++){
				orOdurBVo = storedOrOdurBVoList.get(i);
				storedLognIdMap.put(orOdurBVo.getOdurUid(), orOdurBVo.getLginId());
			}
			
			// 사용자기본(OR_USER_B) 테이블
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)VoUtil.bindList(request, OrUserBVo.class, new String[]{"valid"});
			// 원직자기본(OR_ODUR_B) 테이블
			List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)VoUtil.bindList(request, OrOdurBVo.class, new String[]{"valid"});
			size = orUserBVoList==null ? 0 : orUserBVoList.size();
			
			// 저장할 부서의 회사ID, 리소스ID 조회
			String compId=null, orgRescId=null, deptId=null, deptRescId=null;
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(orgId);
			orOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
			if(orOrgBVo==null){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				LOGGER.error("fail to save User list - no data(OR_ORG_B) - orgId:"+orgId+"\n"+msg);
				throw new CmException(msg);
			}
			compId = orOrgBVo.getCompId();
			orgRescId = orOrgBVo.getRescId();
			deptId = orOrgBVo.getDeptId();
			deptRescId = orOrgBVo.getDeptRescId();
			
			// 신규 생성 로그인 ID
			List<String> newLginIdList = new ArrayList<String>();
			// 중복된 로그인 ID
			List<String> dupLginIdList = new ArrayList<String>();
			
			// 서버 설정 목록 조회
			//Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			//String mailDomain = svrEnvMap.get("mailDomain");
			String mailDomain = null;
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블
			OrUserPwDVo orUserPwDVo = null;
			List<OrUserPwDVo> orUserPwDVoList = new ArrayList<OrUserPwDVo>();
			
			for(i=0;i<size;i++){
				orUserPwDVo = null;
				
				orUserBVo = orUserBVoList.get(i);
				orOdurBVo = orOdurBVoList.get(i);
				
				orUserBVo.setModDt("sysdate");
				orUserBVo.setModrUid(userVo.getUserUid());
				orOdurBVo.setModDt("sysdate");
				orOdurBVo.setModrUid(userVo.getUserUid());
				
				// 원직자UID, 사용자UID 가 없으면 - 신규(생성)
				odurUid = orOdurBVo.getOdurUid();
				userUid = orUserBVo.getUserUid();
				
				// 최초 등록
				if(odurUid==null || odurUid.isEmpty()){
					odurUid = orCmSvc.createId("OR_USER_B");
					orOdurBVo.setOdurUid(odurUid);
					orUserBVo.setOdurUid(odurUid);
					orUserBVo.setUserUid(odurUid);
					userUid = odurUid;
					
					orUserBVo.setRegDt("sysdate");
					orUserBVo.setRegrUid(userVo.getUserUid());
					orOdurBVo.setRegDt("sysdate");
					orOdurBVo.setRegrUid(userVo.getUserUid());
					
					orUserBVo.setAduTypCd("01");// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
					orOdurBVo.setApvPwTypCd("SYS");//결재비밀번호구분코드 - SYS:시스템 비밀번호, APV:결재 비밀번호
					
					// 신규 생성 로그인ID
					newLginIdList.add(orOdurBVo.getLginId());
					
					// 메일 정보
					if("01".equals(orUserBVo.getAduTypCd())){
						if(mailDomain==null){
							PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, userVo.getLangTypCd());
							mailDomain = ptCompBVo==null ? null : ptCompBVo.getMailDomain();
							if(mailDomain==null){
								LOGGER.error("no mail domain for compId:"+compId);
								//em.msg.noMailDomain=메일도메인이 설정되지 않았습니다.
								String msg = messageProperties.getMessage("em.msg.noMailDomain", request);
								throw new CmException(msg);
							}
						}
						orUserPinfoDVo = new OrUserPinfoDVo();
						orUserPinfoDVo.setOdurUid(odurUid);
						orUserPinfoDVo.setEmail(orOdurBVo.getLginId()+"@"+mailDomain);
						orCmSvc.encryptUserPinfo(orUserPinfoDVo);
						orUserPinfoDVoList.add(orUserPinfoDVo);
					}
					
					if(initPw!=null){
						orUserPwDVo = new OrUserPwDVo();
						orUserPwDVo.setOdurUid(odurUid);
						orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
						orUserPwDVo.setPwEnc(License.ins.encryptPw(initPw, odurUid));
						orUserPwDVo.setModDt("sysdate");
						orUserPwDVo.setModrUid(userVo.getUserUid());
						orUserPwDVoList.add(orUserPwDVo);
					}
				} else {
					lginId = storedLognIdMap.get(orOdurBVo.getOdurUid());
					if(lginId==null || !lginId.equals(orOdurBVo.getLginId())){
						newLginIdList.add(orOdurBVo.getLginId());
					}
				}
				
				orUserBVo.setCompId(compId);// 회사ID
				orUserBVo.setOrgRescId(orgRescId);// 조직리소스ID
				orUserBVo.setDeptId(deptId);// 부서ID
				orUserBVo.setDeptRescId(deptRescId);// 부서리소스ID
				
				// 원직자가 아닌 경우
				if(!"01".equals(orUserBVo.getAduTypCd())){
					// 원직자의 상태 코드를 업데이트 하지 않도록 처리
					orOdurBVo.setUserStatCd(null);
				} else {
					
					// 원직자의 경우 동기화 모듈에 등록함
					// 사용자 조직도 동기화 PUSH - 사용자
					if(userUidFirst) userUidFirst = false;
					else userUidBuilder.append(',');
					userUidBuilder.append(userUid);
					
					// 회사별 라이센스 정책이면
					if(licenseByCompEnable){
						// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
						userStatCd = orUserBVo.getUserStatCd();
						
						// 02:근무중, 03:휴직, 04:정직 - 라이센스 카운팅 할 사용자 상태
						if("02".equals(userStatCd) || "03".equals(userStatCd) || "04".equals(userStatCd)){
							if(!licensedUserList.contains(userUid)){
								licensedUserList.add(userUid);
							}
						// 라이센스 카운팅 안 할 사용자 상태
						} else {
							if(licensedUserList.contains(userUid)){
								licensedUserList.remove(userUid);
							}
						}
					}
				}
				
				// 총 관리계정 로그인 아이디
				if(PtConstant.ADMIN_ID.equals(orOdurBVo.getLginId())
						&& !"U0000001".equals(orOdurBVo.getOdurUid())){
					if(!dupLginIdList.contains(orOdurBVo.getLginId())){
						dupLginIdList.add(orOdurBVo.getLginId());
					}
				}
			}
			
			if(!newLginIdList.isEmpty()){
				// 로그인 아이디 중복 검사
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setLginIdList(newLginIdList);
				List<OrOdurBVo> dubList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
				
				if(dubList!=null && !dubList.isEmpty()){
					size = dubList.size();
					for(i=0;i<size;i++){
						orOdurBVo = dubList.get(i);
						if(!dupLginIdList.contains(orOdurBVo.getLginId())){
							dupLginIdList.add(orOdurBVo.getLginId());
						}
					}
				}
				
				if(!dupLginIdList.isEmpty()){
					//pt.login.dupId=중복된 로그인ID 입니다.
					String msg = messageProperties.getMessage("pt.login.dupId", request);
					msg += " " + dupLginIdList.toString();
					LOGGER.error("fail to save User list - "+msg);
					throw new CmException(msg);
				}
			}
			
			// 리소스 정보 queryQueue에 담음
			//  - collectOrRescBVoList() 함수를 통해서 원직자목록(orOdurBVoList)은 queryQueue 에 담기는데
			//    사용자(겸직자)목록(orUserBVoList)은 queryQueue에 담기지 않아서 따로 더해줌
			orCmSvc.collectOrRescBVoList(request, queryQueue, orOdurBVoList, "valid", "rescId", "userNm");
			
			for(i=0;i<size;i++){
				orUserBVo = orUserBVoList.get(i);
				orOdurBVo = orOdurBVoList.get(i);
				if(orUserBVo.getRescId()==null || orUserBVo.getRescId().isEmpty()){
					queryQueue.insert(orUserBVo);
				} else {
					queryQueue.store(orUserBVo);
				}
				orUserBVo.setRescId(orOdurBVo.getRescId());
				orUserBVo.setUserNm(orOdurBVo.getUserNm());
			}
			
			// 메일정보 저장
			for(OrUserPinfoDVo orUserPinfoDVo2 : orUserPinfoDVoList){
				queryQueue.store(orUserPinfoDVo2);
			}
			
			// 비밀번호 저장
			for(OrUserPwDVo newOrUserPwDVo : orUserPwDVoList){
				queryQueue.insert(newOrUserPwDVo);
			}
			
			// 회사별 라이센스 체크
			if(licenseByCompEnable && licensedUserList.size() > licCnt){
				// pt.msg.exceedLicByComp=회사별 라이선스 수를 초과 하였습니다.(라이선스:{0}, 사용자:{1})
				model.put("message", messageProperties.getMessage("pt.msg.exceedLicByComp", new String[]{Integer.toString(licCnt), Integer.toString(licensedUserList.size())}, request));
				return LayoutUtil.getResultJsp();
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.USER);
			commonSvc.execute(queryQueue);
			orCmSvc.setUsers();
			//ptCacheExpireSvc.checkNow(CacheConfig.USER);
			
			// 사용자 조직도 동기화 PUSH - 사용자
			if(pushSyncSvc.hasSync()){
				pushSyncSvc.syncUsers(userUidBuilder.toString(),
						delEmailBuilder.toString(), delUserUidBuilder.toString(), delRidBuilder.toString());
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+orgId+"');");
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 겸직자 정보 삭제 */
	private void deleteUserByUserUid(QueryQueue queryQueue, String userUid){
		// 사용자이미지상세(OR_USER_IMG_D) 테이블 - 삭제
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		orUserImgDVo.setUserUid(userUid);
		queryQueue.delete(orUserImgDVo);
		
		// 사용자역할관계(OR_USER_ROLE_R) 테이블 - 삭제
		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setUserUid(userUid);
		queryQueue.delete(orUserRoleRVo);
		
		// 사용자기본(OR_USER_B) 테이블 - 삭제
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		queryQueue.delete(orUserBVo);
	}
	/** 원직자 정보 삭제 */
	private void deleteOdurByOdurUid(QueryQueue queryQueue, String odurUid){

		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블 - 삭제
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(odurUid);
		queryQueue.delete(orUserPinfoDVo);
		
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블 - 삭제
		OrUserPwHVo orUserPwHVo = new OrUserPwHVo();
		orUserPwHVo.setOdurUid(odurUid);
		queryQueue.delete(orUserPwHVo);
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 삭제
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		queryQueue.delete(orUserPwDVo);
		
		// 원직자기본(OR_ODUR_B) 테이블 - 삭제
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(odurUid);
		queryQueue.delete(orOdurBVo);
	}
	
	/** [히든프레임] 사용자 저장(오른쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/transUser")
	public String transUser(HttpServletRequest request,
			@Parameter(name="userUid", required=false)String userUid,
			@Parameter(name="roleCds", required=false)String roleCds,
			ModelMap model) throws Exception {
		
		try{
			
			UserVo userVo = LoginSession.getUser(request);
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 라이센스가 있는 사용자 목록
			List<String> licensedUserList = null;
			int licCnt = 0;
			String userStatCd;
			
			// 회사별 라이센스 정책
			boolean licenseByCompEnable = "Y".equals(sysPlocMap.get("licenseByCompEnable"));
			if(licenseByCompEnable){
				licensedUserList = orCmSvc.getActiveUserList(userVo.getCompId());
				PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(userVo.getCompId(), userVo.getLangTypCd());
				if(ptCompBVo!=null && ptCompBVo.getLicCnt()!=null && !ptCompBVo.getLicCnt().isEmpty()){
					licCnt = Integer.parseInt(ptCompBVo.getLicCnt());
				}
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo storedOrUserBVo = new OrUserBVo();
			storedOrUserBVo.setUserUid(userUid);
			storedOrUserBVo = (OrUserBVo)commonSvc.queryVo(storedOrUserBVo);
			if(storedOrUserBVo==null){
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUser", request);
				LOGGER.error("fail to save user - userUid:"+userUid+"\n"+msg);
				throw new CmException(msg);
			}
			
			OrOdurBVo storedOrOdurBVo = new OrOdurBVo();
			storedOrOdurBVo.setOdurUid(storedOrUserBVo.getOdurUid());
			storedOrOdurBVo = (OrOdurBVo)commonSvc.queryVo(storedOrOdurBVo);
			if(storedOrOdurBVo==null){
				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUser", request);
				LOGGER.error("fail to save user - userUid:"+userUid+"\n"+msg);
				throw new CmException(msg);
			}
			
			// 사용자명 리소스
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(storedOrUserBVo.getCompId(), langTypCd);
			OrRescBVo orRescBVo = orCmSvc.collectOrRescBVo(request, queryQueue, null, langTypCdList);
						
			// 사용자기본(OR_USER_B) 테이블
			OrUserBVo orUserBVo = new OrUserBVo();
			VoUtil.bind(request, orUserBVo);
			orUserBVo.setModDt("sysdate");
			orUserBVo.setModrUid(userVo.getUserUid());
			if(orRescBVo!=null){
				orUserBVo.setRescId(orRescBVo.getRescId());
				orUserBVo.setUserNm(orRescBVo.getRescVa());
			}
			if(userUid!=null && !userUid.isEmpty()){
				orUserBVo.setUserUid(userUid);
			}
			queryQueue.update(orUserBVo);
			
			// 회사별 라이선스 체크 - 회사별 라이센스 정책이면, 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
			if(licenseByCompEnable && "01".equals(storedOrUserBVo.getAduTypCd())){
				
				// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
				userStatCd = orUserBVo.getUserStatCd();
				
				// 02:근무중, 03:휴직, 04:정직 - 라이센스 카운팅 할 사용자 상태
				if("02".equals(userStatCd) || "03".equals(userStatCd) || "04".equals(userStatCd)){
					if(!licensedUserList.contains(userUid)){
						licensedUserList.add(userUid);
					}
				// 라이센스 카운팅 안 할 사용자 상태
				} else {
					if(licensedUserList.contains(userUid)){
						licensedUserList.remove(userUid);
					}
				}
				
				// 회사별 라이센스 체크
				if(licenseByCompEnable && licensedUserList.size() > licCnt){
					// pt.msg.exceedLicByComp=회사별 라이선스 수를 초과 하였습니다.(라이선스:{0}, 사용자:{1})
					model.put("message", messageProperties.getMessage("pt.msg.exceedLicByComp", new String[]{Integer.toString(licCnt), Integer.toString(licensedUserList.size())}, request));
					return LayoutUtil.getResultJsp();
				}
			}
			
			// 원직자기본(OR_ODUR_B) 테이블
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			VoUtil.bind(request, orOdurBVo);
			orOdurBVo.setOdurUid(storedOrUserBVo.getOdurUid());
			
			if(orOdurBVo.getSysUserYn()==null) orOdurBVo.setSysUserYn("N");
			
//			if(orOdurBVo.getLginIpExcliYn()==null) orOdurBVo.setLginIpExcliYn("N");
//			if(orOdurBVo.getSesnIpExcliYn()==null) orOdurBVo.setSesnIpExcliYn("N");
//			if(orOdurBVo.getMobUseYn()==null) orOdurBVo.setMobUseYn("Y");
//			if(orOdurBVo.getMsgLginYn()==null) orOdurBVo.setMsgLginYn("Y");
			
			orOdurBVo.setModDt("sysdate");
			// 겸직이면 원직의 상태가 변경 되지 않도록 함
			if(!"01".equals(storedOrUserBVo.getAduTypCd())){//겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				orOdurBVo.setUserStatCd(null);
				orOdurBVo.setRefVa1(null);
				orOdurBVo.setRefVa2(null);
				orOdurBVo.setRefVa3(null);
				orOdurBVo.setSysUserYn(null);
			} else if(orRescBVo!=null){
				orOdurBVo.setRescId(orRescBVo.getRescId());
				orOdurBVo.setUserNm(orRescBVo.getRescVa());
			}
			orOdurBVo.setModrUid(userVo.getUserUid());
			queryQueue.update(orOdurBVo);
			
			// 로그인ID 가 있으면 - 사용자 수정의 경우 - [환경설정 - 개인정보]는 lginId 안 넘어옴
			if(orOdurBVo.getLginId()!=null){
				
				// lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부,
				// useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부
				String[][] odurSecus = {
						{"lginIpExYn", "Y"},
						{"sesnIpExYn", "Y"},
						{"useMobYn", "N"},
						{"useMsgLginYn", "N"},
						{"useMsgrYn", "N"},
						{"useMailYn", "N"},
				};
				
				// 원직자보안상세(OR_ODUR_SECU_D) 저장
				String secuVa;
				OrOdurSecuDVo orOdurSecuDVo;
				for(String[] secus : odurSecus){
					orOdurSecuDVo = new OrOdurSecuDVo();
					orOdurSecuDVo.setOdurUid(storedOrUserBVo.getOdurUid());
					orOdurSecuDVo.setSecuId(secus[0]);
					secuVa = request.getParameter(secus[0]);
					if(secuVa==null || !secus[1].equals(secuVa)){
						queryQueue.delete(orOdurSecuDVo);
					} else {
						orOdurSecuDVo.setSecuVa(secuVa);
						queryQueue.store(orOdurSecuDVo);
					}
				}
				
				if(storedOrOdurBVo.getLginId()!=null && !storedOrOdurBVo.getLginId().equals(orOdurBVo.getLginId())){
					OrOdurBVo checkOrOdurBVo = new OrOdurBVo();
					checkOrOdurBVo.setLginId(orOdurBVo.getLginId());
					Integer count = commonSvc.count(checkOrOdurBVo);
					if(count>0){
						//pt.login.dupId=중복된 로그인ID 입니다.
						String msg = messageProperties.getMessage("pt.login.dupId", request);
						LOGGER.error("fail to save user - userUid:"+userUid+"  lginId:"+orOdurBVo.getLginId()+"\n"+msg);
						throw new CmException(msg);
					}
				}
				
			// 개인정보 변경의 경우
			} else {
				
				// lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부, useMobYn:모바일사용여부,
				// useMsgLginYn:메세지로그인여부, useMsgrYn:메신저사용여부, useMailYn:메일사용여부
				String[][] odurSecus = {
//						{"lginIpExYn", "Y"},
//						{"sesnIpExYn", "Y"},
//						{"useMobYn", "N"},
						{"useMsgLginYn", "N"},
//						{"useMsgrYn", "Y"},
//						{"useMailYn", "Y"},
				};
				
				// 원직자보안상세(OR_ODUR_SECU_D) 저장
				String secuVa;
				OrOdurSecuDVo orOdurSecuDVo;
				for(String[] secus : odurSecus){
					orOdurSecuDVo = new OrOdurSecuDVo();
					orOdurSecuDVo.setOdurUid(storedOrUserBVo.getOdurUid());
					orOdurSecuDVo.setSecuId(secus[0]);
					secuVa = request.getParameter(secus[0]);
					if(secuVa==null || !secus[1].equals(secuVa)){
						queryQueue.delete(orOdurSecuDVo);
					} else {
						orOdurSecuDVo.setSecuVa(secuVa);
						queryQueue.store(orOdurSecuDVo);
					}
				}
				
			}
			
			// RID 중복체크
			OrOdurBVo checkOrOdurBVo;
			if(orOdurBVo.getRid()!=null){// [환경설정 - 개인정보]의 경우 - rid 안 넘어옴
				if(storedOrOdurBVo.getRid()!=null && !storedOrOdurBVo.getRid().isEmpty()){
					if(!storedOrOdurBVo.getRid().equals(orOdurBVo.getRid())){
						checkOrOdurBVo = new OrOdurBVo();
						checkOrOdurBVo.setRid(orOdurBVo.getRid());
						if(commonSvc.count(checkOrOdurBVo)>0){
							//or.msg.dup.rid=중복된 참조ID 입니다.
							String msg = messageProperties.getMessage("or.msg.dup.rid", request);
							LOGGER.error("fail to save user - userUid:"+userUid+"  rid:"+orOdurBVo.getRid()+"\n"+msg);
							throw new CmException(msg);
						}
					}
				}
			}
			
			// 메일은 변경 못함 - 중복체크 생략
			
			// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			VoUtil.bind(request, orUserPinfoDVo);
			orUserPinfoDVo.setOdurUid(storedOrUserBVo.getOdurUid());
			orCmSvc.encryptUserPinfo(orUserPinfoDVo);
			queryQueue.store(orUserPinfoDVo);
			
			if(roleCds!=null){// [환경설정 - 개인정보]의 경우 - roleCds 안 넘어옴
				// 사용자역할관계(OR_USER_ROLE_R) 테이블
				OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
				orUserRoleRVo.setUserUid(userUid);
				queryQueue.delete(orUserRoleRVo);
				String[] roleCdArr = roleCds==null || roleCds.isEmpty() ? null : roleCds.split(",");
				int i, size = roleCdArr==null ? 0 : roleCdArr.length;
				for(i=0;i<size;i++){
					orUserRoleRVo = new OrUserRoleRVo();
					orUserRoleRVo.setUserUid(userUid);
					orUserRoleRVo.setRoleCd(roleCdArr[i]);
					queryQueue.insert(orUserRoleRVo);
				}
			}
			
			// PC 알림 설정 저장 - 환경설정 > 개인정보
			boolean storePcNoti = "Y".equals(sysPlocMap.get("pcNotiEnable"));
			if(storePcNoti){
				
				String odurUid = userVo.getOdurUid();
				String setupClsId = "pt.pcNoti";
				String cacheYn = "Y";
				String setupVa;
				
				// 사용자설정상세(PT_USER_SETUP_D) 테이블 - 기존 데이터 삭제
				PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
				ptUserSetupDVo.setUserUid(odurUid);
				ptUserSetupDVo.setSetupClsId(setupClsId);
				queryQueue.delete(ptUserSetupDVo);
				
				for(String md : ptSysSvc.getPcNotiMds(sysPlocMap)){
					setupVa = request.getParameter("pcNoti"+md);
					if(!"Y".equals(setupVa)) setupVa = "N";
					ptPsnSvc.addUserSetup(queryQueue, odurUid, setupClsId, md, setupVa, cacheYn);
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.USER);
			commonSvc.execute(queryQueue);
			orCmSvc.setUsers();
			//ptCacheExpireSvc.checkNow(CacheConfig.USER);
			
			// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
			if("01".equals(storedOrUserBVo.getAduTypCd())){
				// 사용자 조직도 동기화 PUSH - 사용자
				if(pushSyncSvc.hasSync()){
					pushSyncSvc.syncUsers(userUid, null, null, null);
				}
			}
			
			// PC 알림 설정 저장 - 환경설정 > 개인정보
			if(storePcNoti){
				Map<String, Map<String, ?>> userSetupMap = ptPsnSvc.getUserSetup(userVo.getUserUid(), userVo.getOdurUid(), null, request);
				request.getSession().setAttribute("userSetupMap", userSetupMap);
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.openUserListFrm();");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [히든프레임] - 비밀번호 변경 */
	@RequestMapping(value = "/pt/adm/org/transInitPw")
	public String transInitPw(HttpServletRequest request,
			ModelMap model) throws Exception {
		try {
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Change password fail(by admin) : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				String message = messageProperties.getMessage("pt.login.fail.decrypt", request);
				throw new CmException(message);
			}
			
			String newPw = (String)jsonObject.get("newPw1");
			request.getSession().setAttribute("initPw", newPw);
			
			model.put("todo", "parent.saveInitPwCallback();");
			return LayoutUtil.getResultJsp();
			
		} catch(CmException e){
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch(Exception e){
			e.printStackTrace();
			//cm.msg.modify.fail=변경에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.modify.fail", request));
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** [히든프레임] - 비밀번호 변경 */
	@RequestMapping(value = "/pt/adm/org/transPw")
	public String transPw(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try {
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Change password fail(by admin) : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				String message = messageProperties.getMessage("pt.login.fail.decrypt", request);
				throw new CmException(message);
			}
			
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			String ids = (String)jsonObject.get("userUids");
			String newPw = (String)jsonObject.get("newPw1");
			String pwTypCd = (String)jsonObject.get("pwTypCd");
			
			// 원직자기본(OR_ODUR_B) 테이블
			OrOdurBVo orOdurBVo;
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블
			OrUserPwDVo orUserPwDVo;
			
			if(ids!=null){
				for(String uid : ids.split(",")){
					if(uid!=null && !uid.isEmpty()){
						
						// 원직자기본(OR_ODUR_B) 테이블 - 조회 : 원직자UID 확인용
						orOdurBVo = new OrOdurBVo();
						orOdurBVo.setUserUid(uid);
						orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
						
						if(orOdurBVo==null){
							LOGGER.error("Change password fail(by admin) : no user :"+uid);
							// pt.login.noOdur=원직자 정보를 확인 할 수 없습니다.
							String message = messageProperties.getMessage("pt.login.noOdur", request);
							throw new CmException(message+" - user : "+uid);
						}
						
						// 사용자비밀번호상세(OR_USER_PW_D) 테이블
						orUserPwDVo = new OrUserPwDVo();
						orUserPwDVo.setOdurUid(orOdurBVo.getOdurUid());
						orUserPwDVo.setPwTypCd(pwTypCd);//SYS:시스템 비밀번호, APV:결재 비밀번호
						orUserPwDVo.setPwEnc(License.ins.encryptPw(newPw, orOdurBVo.getOdurUid()));
						orUserPwDVo.setModDt("sysdate");
						orUserPwDVo.setModrUid(userVo.getUserUid());
						queryQueue.store(orUserPwDVo);
					}
				}
			}
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				//cm.msg.modify.success=변경 되었습니다.
				String message = messageProperties.getMessage("cm.msg.modify.success", request);
				
				// 사용자 조직도 동기화 PUSH - 사용자
				if(pushSyncSvc.hasSync()){
					pushSyncSvc.syncUsers(ids, null, null, null);
				}
				
				model.put("message", message);
				model.put("todo", "parent.closePw();");
				return LayoutUtil.getResultJsp();
			} else {
				//cm.msg.noUser.toModify=변경할 사용자가 없습니다.
				String message = messageProperties.getMessage("cm.msg.noUser.toModify", request);
				model.put("message", message);
				return LayoutUtil.getResultJsp();
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch(Exception e){
			e.printStackTrace();
			//cm.msg.modify.fail=변경에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.modify.fail", request));
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** [AJAX] - 조직 트리 순서 변경(왼쪽 프레임) */
	@RequestMapping(value = "/pt/adm/org/transOrgMoveAjx")
	public String transOrgMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String orgId = (String)jsonObject.get("orgId");
			String direction = (String)jsonObject.get("direction");
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			if(	direction==null || (!"up".equals(direction) && !"down".equals(direction))
				|| orgId==null || orgId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				if(direction==null || (!"up".equals(direction) && !"down".equals(direction))){
					LOGGER.error("Org move(up/down) - direction==null  : "+message);
				} else if(orgId==null || orgId.isEmpty()){
					LOGGER.error("Org move(up/down) - direction:"+direction+" orgId:"+orgId+"  : "+message);
				}
				throw new CmException(message);
			}
			
			OrOrgBVo orOrgBVo, storedOrOrgBVo;
			
			// curOrdr - 현재순번
			// switchOrdr - 바꿀 순번
			Integer curOrdr, switchOrdr;
			String orgPid=null;
			
			orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(orgId);
			storedOrOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
			
			if(storedOrOrgBVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(OR_ORG_B) - orgId:"+orgId+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			curOrdr = Integer.valueOf(storedOrOrgBVo.getSortOrdr());
			orgPid = storedOrOrgBVo.getOrgPid();
			
			// 위로 이동
			if("up".equals(direction)){
				
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("OR_ORG_B");
				ptxSortOrdrChnVo.setPkCol("ORG_PID");
				ptxSortOrdrChnVo.setPk(orgPid);
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				storedOrOrgBVo = new OrOrgBVo();
				storedOrOrgBVo.setOrgId(orgId);
				storedOrOrgBVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedOrOrgBVo);
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
					
					// 사용자 조직도 동기화 PUSH - 조직도
					if(pushSyncSvc.hasSync()){
						pushSyncSvc.syncOrg();
					}
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				
				// 아래로 이동
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("OR_ORG_B");
				ptxSortOrdrChnVo.setPkCol("ORG_PID");
				ptxSortOrdrChnVo.setPk(orgPid);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				if(maxSortOrdr>curOrdr){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("OR_ORG_B");
					ptxSortOrdrChnVo.setPkCol("ORG_PID");
					ptxSortOrdrChnVo.setPk(orgPid);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					storedOrOrgBVo = new OrOrgBVo();
					storedOrOrgBVo.setOrgId(orgId);
					storedOrOrgBVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(storedOrOrgBVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
					
					// 사용자 조직도 동기화 PUSH - 조직도
					if(pushSyncSvc.hasSync()){
						pushSyncSvc.syncOrg();
					}
				} else {
					//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
				}
			}
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 임직원 목록 조회 - 보안 설정 */
	@RequestMapping(value = "/pt/adm/org/setSecuPop")
	public String setSecuPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/pt/adm/org/setSecuPop");
	}
	
	/** [AJAX] - 보안설정 - 저장 */
	@RequestMapping(value = "/pt/adm/org/transSecuAjx")
	public String transSecuAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			String secuId = (String)jsonObject.get("secuId");
			String val = (String)jsonObject.get("val");
			String sOdurUids = (String)jsonObject.get("odurUids");
			
			if(		secuId==null || secuId.isEmpty() ||
					val==null || val.isEmpty() ||
					sOdurUids==null || sOdurUids.isEmpty() ){
				
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
				
			} else {
				
//				OrOdurBVo orOdurBVo;
//				UserVo userVo = LoginSession.getUser(request);
				QueryQueue queryQueue = new QueryQueue();
				
				OrOdurSecuDVo orOdurSecuDVo;
				
				String[] odurUids = sOdurUids.split(",");
				for(String odurUid : odurUids){
					
					orOdurSecuDVo = new OrOdurSecuDVo();
					orOdurSecuDVo.setOdurUid(odurUid);
					
					// lginIpExYn:로그인IP제외대상여부, sesnIpExYn:세션IP제외대상여부
					//  - 디폴트:N
					if("lginIpExYn".equals(secuId) || "sesnIpExYn".equals(secuId)){
						orOdurSecuDVo.setSecuId(secuId);
						if("Y".equals(val)){
							orOdurSecuDVo.setSecuVa(val);
							queryQueue.store(orOdurSecuDVo);
						} else {
							queryQueue.delete(orOdurSecuDVo);
						}
						
					// useMailYn:메일 사용 안함, useMsgrYn:메신저 사용 안함
					// useMobYn:모바일사용여부, useMsgLginYn:메세지로그인여부
					//   - 디폴트:Y
					} else if("useMailYn".equals(secuId) || "useMsgrYn".equals(secuId)
							|| "useMobYn".equals(secuId) || "useMsgLginYn".equals(secuId)){
						orOdurSecuDVo.setSecuId(secuId);
						if("N".equals(val)){
							orOdurSecuDVo.setSecuVa(val);
							queryQueue.store(orOdurSecuDVo);
						} else {
							queryQueue.delete(orOdurSecuDVo);
						}
					}
				}
				
				if(queryQueue.isEmpty()){
					// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
					model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
				} else {
					commonSvc.execute(queryQueue);
					//cm.msg.save.success=저장 되었습니다.
					model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
					model.put("result", "ok");
				}
			}
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return JsonUtil.returnJson(model);
	}
}
