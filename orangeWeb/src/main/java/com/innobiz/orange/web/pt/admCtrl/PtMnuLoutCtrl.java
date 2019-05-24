package com.innobiz.orange.web.pt.admCtrl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;

/** 레이아웃 설정 관리 */
@Controller
public class PtMnuLoutCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMnuLoutCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

//	/** 리소스 조회 저장용 서비스 */
//	@Autowired
//	private PtRescSvc ptRescSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

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
	
	/** 메뉴 설정 관리 컨트롤러 - 메뉴 트리 포워드용 */
	@Autowired
	private PtMnuCtrl ptMnuCtrl;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
//	/** 포털 보안 서비스(레이아웃 포함) */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;

	/** 관리자 레이아웃 설정 */
	@RequestMapping(value = "/pt/adm/lout/setAdminLout")
	public String setAdmLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "mnuLoutId", required = false) String mnuLoutId,
			ModelMap model) throws Exception {
		model.put("mnuGrpMdParam", "&mnuGrpMdCd=A");
		return setBascLout(request, compId, mnuLoutId, "A", model);
	}

	/** 관리자 레이아웃 설정 */
	@RequestMapping(value = "/pt/adm/lout/setMobileLout")
	public String setMobileLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "mnuLoutId", required = false) String mnuLoutId,
			ModelMap model) throws Exception {
		model.put("mnuGrpMdParam", "&mnuGrpMdCd=M");
		return setBascLout(request, compId, mnuLoutId, "M", model);
	}
	
	/** 기본 레이아웃 설정 */
	@RequestMapping(value = "/pt/adm/lout/setBascLout")
	public String setBascLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "mnuLoutId", required = false) String mnuLoutId,
			@RequestParam(value = "loutCatId", required = false) String loutCatId,
			ModelMap model) throws Exception {
		
		// 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
		// 아이콘레이아웃 : setIconLout()
		if(!"A".equals(loutCatId) && !"M".equals(loutCatId)) loutCatId = "B";
		model.put("loutCatId", loutCatId);

		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 레이아웃 시스템 설정
		if("B".equals(loutCatId)){// 기본레이아웃
			Map<String, String> layout = ptSysSvc.getLayoutSetup();
			model.put("layout", layout);
		} else {// 관리자레이아웃
			Map<String, String> layout = new HashMap<String, String>();
			// 관리자쪽 UI 조절 
			layout.put("bascLoutUseYn", "Y");// 기본레이아웃을 사용하지 않을때도 관리자 레이아웃이 보이도록
			layout.put("subMnuOption", "N");// 폴더 추가 안되도록
			model.put("layout", layout);
		}
		
		List<PtMnuLoutDVo> allPtMnuLoutDVoList = ptLoutSvc.getLoutListFromTable(compId, loutCatId, langTypCd);
		
		// 레이아웃위치코드 - 아이콘레이아웃(icon:아이콘), 기본레이아웃(top:상단, main:메인, sub:서브), 관리자레이아웃(adm:관리자)
		String loutLocCd = null;
		// 설정된 메뉴그룹 목록
		List<PtMnuLoutDVo> ptMnuLoutDVoList = null;
		PtMnuLoutDVo ptMnuLoutDVo;
		
		// 지역(top,main,sub), 카테고리(pid)별로 list에 담음
		int i, size = allPtMnuLoutDVoList==null ? 0 : allPtMnuLoutDVoList.size();
		for(i=0;i<size;i++){
			ptMnuLoutDVo = allPtMnuLoutDVoList.get(i);
			if(loutLocCd==null || !loutLocCd.equals(ptMnuLoutDVo.getLoutLocCd())){
				loutLocCd = ptMnuLoutDVo.getMnuLoutPid();
				ptMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
				model.put(loutLocCd+"PtMnuLoutDVoList", ptMnuLoutDVoList);
			}
			ptMnuLoutDVoList.add(ptMnuLoutDVo);
		}
		
		if("B".equals(loutCatId)){
			model.put("areas", new String[]{"home", "top","main","sub"});
		} else if("M".equals(loutCatId)){
			model.put("areas", new String[]{"mobile", "bottom"});
		} else {
			model.put("areas", new String[]{"adm"});
		}
		
		// 회사별 지원 언어 - 스크립트에서 활용 목적으로 "langs"에 담음
		StringBuilder builder = new StringBuilder(32);
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
		size = langTypCdList==null ? 0 : langTypCdList.size();
		boolean first = true;
		for(i=0;i<size;i++){
			if(first) first = false;
			else builder.append(',');
			builder.append(langTypCdList.get(i).getCd());
		}
		model.put("langs", builder.toString());
		
		return LayoutUtil.getJspPath("/pt/adm/lout/setBascLout");
	}
	
	/** 아이콘 레이아웃 설정 */
	@RequestMapping(value = "/pt/adm/lout/setIconLout")
	public String setIconLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "mnuLoutId", required = false) String mnuLoutId,
			ModelMap model) throws Exception {
		
		// css 포함 : /css/header.icon.img.css
		// 구현 : headinc.jsp
		model.put("CSS_OPTS", new String[]{"iconImg"});
		
		// 레이아웃유형ID
		String loutCatId = "I";
		
		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 레이아웃 시스템 설정
		Map<String, String> layout = ptSysSvc.getLayoutSetup();
		model.put("layout", layout);
		
		List<PtMnuLoutDVo> allPtMnuLoutDVoList = ptLoutSvc.getLoutListFromTable(compId, loutCatId, langTypCd);
		
		// 폴더목록
		int i, size = allPtMnuLoutDVoList==null ? 0 : allPtMnuLoutDVoList.size();
		
		// 아이콘 목록
		List<PtMnuLoutDVo> ptMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
		// 설정된 메뉴그룹 목록
		List<PtMnuLoutDVo> mnuGrpPtMnuLoutDVoList = null;
		PtMnuLoutDVo ptMnuLoutDVo;
		
		StringBuilder imgKndVaBuilder = new StringBuilder(64);
		StringBuilder mnuLoutIdBuilder = new StringBuilder(64);
		String mnuLoutPid = null, loutLocCd;
		boolean first = true;
		
		for(i=0;i<size;i++){
			ptMnuLoutDVo = allPtMnuLoutDVoList.get(i);
			loutLocCd = ptMnuLoutDVo.getLoutLocCd();
			
			//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘,left:왼쪽,right:오른쪽), 기본레이아웃(top:상단,main:메인,sub:서브), 관리자레이아웃(adm:관리자)
			if("icon".equals(loutLocCd)){
				//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
				// F:폴더 - 아이콘 표시용 으로 ptMnuLoutDVoList 에 담음
				if("F".equals(ptMnuLoutDVo.getMnuLoutKndCd())){
					ptMnuLoutDVoList.add(ptMnuLoutDVo);
					if(first) first = false;
					else {
						imgKndVaBuilder.append(',');
						mnuLoutIdBuilder.append(',');
					}
					imgKndVaBuilder.append(ptMnuLoutDVo.getImgKndVa());
					mnuLoutIdBuilder.append(ptMnuLoutDVo.getMnuLoutId());
					// param.mnuLoutId 가 있으면 해당 정보 세팅를 담음
					if(mnuLoutId!=null && mnuLoutId.equals(ptMnuLoutDVo.getMnuLoutId())){
						model.put("activeImgKndVa", ptMnuLoutDVo.getImgKndVa());
						model.put("activeImgSeq", ptMnuLoutDVoList.size());
					}
				// G:메뉴그룹, C:메뉴조합 - 아이콘에 딸린 서브그룹으로 "sub_"+ptMnuLoutDVo.getMnuLoutPid() 에 세팅함
				} else {
					if(mnuLoutPid==null || !mnuLoutPid.equals(ptMnuLoutDVo.getMnuLoutPid())){
						mnuLoutPid = ptMnuLoutDVo.getMnuLoutPid();
						mnuGrpPtMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
						model.put("sub_"+mnuLoutPid, mnuGrpPtMnuLoutDVoList);
					}
					mnuGrpPtMnuLoutDVoList.add(ptMnuLoutDVo);
				}
			} else if("left".equals(loutLocCd)){
				if(!"left".equals(mnuLoutPid)){
					mnuLoutPid = "left";
					mnuGrpPtMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
					model.put("sub_"+mnuLoutPid, mnuGrpPtMnuLoutDVoList);
				}
				mnuGrpPtMnuLoutDVoList.add(ptMnuLoutDVo);
			} else if("right".equals(loutLocCd)){
				if(!"right".equals(mnuLoutPid)){
					mnuLoutPid = "right";
					mnuGrpPtMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
					model.put("sub_"+mnuLoutPid, mnuGrpPtMnuLoutDVoList);
				}
				mnuGrpPtMnuLoutDVoList.add(ptMnuLoutDVo);
			} else if("home".equals(loutLocCd)){
				if(!"home".equals(mnuLoutPid)){
					mnuLoutPid = "home";
					mnuGrpPtMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
					model.put("sub_"+mnuLoutPid, mnuGrpPtMnuLoutDVoList);
				}
				mnuGrpPtMnuLoutDVoList.add(ptMnuLoutDVo);
			}
		}
		
		// 화면 구성용 빈 VO
		ptMnuLoutDVoList.add(new PtMnuLoutDVo());
		model.put("ptMnuLoutDVoList", ptMnuLoutDVoList);
		model.put("imgKndVas", imgKndVaBuilder.toString());
		model.put("mnuLoutIds", mnuLoutIdBuilder.toString());
		
		// 왼쪽 오른쪽 영역 추가
		model.put("areaCds", new String[]{"home","left","right"});
		
		// 리소스상세(PT_RESC_D) 테이블 - 리소스 조회
		PtRescDVo ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId(compId);
		ptRescDVo.setWhereSqllet("AND RESC_ID IN (SELECT RESC_ID FROM PT_MNU_LOUT_D WHERE COMP_ID = T.COMP_ID AND LOUT_CAT_ID = 'I' AND RESC_ID IS NOT NULL)");
		@SuppressWarnings("unchecked")
		List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
		if(ptRescDVoList!=null){
			for(PtRescDVo storedPtRescDVo : ptRescDVoList){
				model.put(storedPtRescDVo.getRescId()+"_"+storedPtRescDVo.getLangTypCd(), storedPtRescDVo.getRescVa());
			}
		}
		
		// 회사별 지원 언어 - 스크립트에서 활용 목적으로 "langs"에 담음
		StringBuilder builder = new StringBuilder(32);
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
		size = langTypCdList==null ? 0 : langTypCdList.size();
		first = true;
		for(i=0;i<size;i++){
			if(first) first = false;
			else builder.append(',');
			builder.append(langTypCdList.get(i).getCd());
		}
		model.put("langs", builder.toString());
		
		return LayoutUtil.getJspPath("/pt/adm/lout/setIconLout");
	}

	/** [팝업] 아이콘 선택 팝업 */
	@RequestMapping(value = "/pt/adm/lout/setIconPop")
	public String setIconPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/pt/adm/lout/setIconPop");// Ctrl 로직 없음
	}

	/** [프레임] 메뉴그룹 목록 조회 */
	@RequestMapping(value = "/pt/adm/lout/listMnuGrpFrm")
	public String listMnuGrpFrm(HttpServletRequest request,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="mnuGrpMdCd", required=false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		if(mnuGrpMdCd==null) mnuGrpMdCd = "U";
		
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setMnuGrpMdCd(mnuGrpMdCd);
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		if(schWord!=null && !schWord.isEmpty()){
			ptMnuGrpBVo.setSchCat("mnuGrpNm");
			ptMnuGrpBVo.setSchWord(schWord);
		}
		
		// 전체회사 + 사용자의 회사 - 로 메뉴그룹 목록 제한
		UserVo userVo = LoginSession.getUser(request);
		List<String> openCompIdList = new ArrayList<String>();
		openCompIdList.add(PtConstant.SYS_COMP_ID);
		openCompIdList.add(userVo.getCompId());
		ptMnuGrpBVo.setOpenCompIdList(openCompIdList);
		
		Integer recodeCount = commonSvc.count(ptMnuGrpBVo);
		PersonalUtil.setFixedPaging(request, ptMnuGrpBVo, 10, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
		

		// 공개범위 - 회사명 세팅
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		String openCompId, allCompNm = messageProperties.getMessage("cm.option.allComp", request);//전체회사
		
		// 공개범위 - 회사명 세팅
		if(ptMnuGrpBVoList!=null){
			for(PtMnuGrpBVo storedPtMnuGrpBVo : ptMnuGrpBVoList){
				openCompId = storedPtMnuGrpBVo.getOpenCompId();
				if(openCompId==null || openCompId.equals(PtConstant.SYS_COMP_ID) || openCompId.isEmpty()){
					storedPtMnuGrpBVo.setOpenCompNm(allCompNm);
				} else {
					ptCompBVo = ptCompMap.get(openCompId);
					storedPtMnuGrpBVo.setOpenCompNm(ptCompBVo==null ? null : ptCompBVo.getRescNm());
				}
			}
		}
		
		model.put("recodeCount", recodeCount);
		model.put("ptMnuGrpBVoList", ptMnuGrpBVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/lout/listMnuGrpFrm");
	}

	/** [팝업] 메뉴조합추가 / 메뉴조합수정 - 팝업 */
	@RequestMapping(value = "/pt/adm/lout/setCombNmPop")
	public String setCombNmPop(HttpServletRequest request,
			//@Parameter(name="compId", required=false) String compId,
			@Parameter(name="rescId", required=false) String rescId,
			ModelMap model) throws Exception {
		
		// 리소스 조회
		if(rescId!=null && !rescId.isEmpty()){
			
			PtRescDVo ptRescDVo = new PtRescDVo();
			VoUtil.bind(request, ptRescDVo);
			@SuppressWarnings("unchecked")
			List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
			if(ptRescDVoList!=null){
				for(PtRescDVo storedPtRescDVo : ptRescDVoList){
					model.put(storedPtRescDVo.getRescId()+"_"+storedPtRescDVo.getLangTypCd(), storedPtRescDVo.getRescVa());
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/lout/setCombNmPop");
	}
	
	/** [팝업] 메뉴조합 구성 - 팝업 */
	@RequestMapping(value = "/pt/adm/lout/setCombMnuPop")
	public String setCombMnuPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="loutCatId", required=false) String loutCatId,//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
			@Parameter(name="mnuLoutId", required=false) String mnuLoutId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String mnuGrpMdCd = "A".equals(loutCatId) || "M".equals(loutCatId) ? loutCatId : "U";
		
		// 회사ID
		if(compId==null || compId.isEmpty()){
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 메뉴그룹 목록 조회 - 우측 상단 콤보
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setMnuGrpMdCd(mnuGrpMdCd);
		// 중 포털 에서 메뉴 관리하는 것만 - 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임(URL)
		ptMnuGrpBVo.setWhereSqllet("AND MNU_GRP_TYP_CD IN ('01', '03')");
		ptMnuGrpBVo.setOrderBy("SORT_ORDR");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		
		// 전체회사 + 사용자의 회사 - 로 메뉴그룹 목록 제한
		UserVo userVo = LoginSession.getUser(request);
		List<String> openCompIdList = new ArrayList<String>();
		openCompIdList.add(PtConstant.SYS_COMP_ID);
		openCompIdList.add(userVo.getCompId());
		ptMnuGrpBVo.setOpenCompIdList(openCompIdList);
		
		@SuppressWarnings("unchecked")
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>)commonSvc.queryList(ptMnuGrpBVo);
		model.put("ptMnuGrpBVoList", ptMnuGrpBVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/lout/setCombMnuPop");
	}
	
	/** [팝업 내 프레임] 메뉴그룹 트리 */
	@RequestMapping(value = "/pt/adm/lout/treeMnuFrm")
	public String treeMnuFrm(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		model.put("treeSelectOption", "2");//트리 javascript 에서 여러개 선택 되도록 함.
		return ptMnuCtrl.treeMnuFrm(request, mnuGrpId, "Y", model);
	}
	
	/** [팝업 내 프레임] 메뉴조합 트리 */
	@RequestMapping(value = "/pt/adm/lout/treeCombFrm")
	public String treeCombFrm(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="loutCatId", required=false) String loutCatId,//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
			@Parameter(name="mnuLoutId", required=false) String mnuLoutId,
			ModelMap model) throws Exception {
		
		if(mnuLoutId!=null && !mnuLoutId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			PtMnuLoutCombDVo ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
			ptMnuLoutCombDVo.setCompId(compId);
//			ptMnuLoutCombDVo.setLoutCatId(loutCatId);
			ptMnuLoutCombDVo.setMnuLoutId(mnuLoutId);
			ptMnuLoutCombDVo.setQueryLang(langTypCd);
			ptMnuLoutCombDVo.setOrderBy("SORT_ORDR");
			
			// 조회
			@SuppressWarnings("unchecked")
			List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList = (List<PtMnuLoutCombDVo>)commonSvc.queryList(ptMnuLoutCombDVo);
			model.put("ptMnuLoutCombDVoList", ptMnuLoutCombDVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/lout/treeCombFrm");
	}
	/** [팝업 내 팝업] 메뉴조합 - 폴더추가/폴더수정 */
	@RequestMapping(value = "/pt/adm/lout/setFldPop")
	public String setFldPop(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="rescId", required=false) String rescId,
			@Parameter(name="mnuYn", required=false) String mnuYn,
			@Parameter(name="type", required=false) String type,
			ModelMap model) throws Exception {
		
		if(rescId!=null && !rescId.isEmpty()){
			if("N".equals(mnuYn)){
				// 리소스상세(PT_RESC_D) 테이블 - 폴더명 조회
				PtRescDVo ptRescDVo = new PtRescDVo();
				ptRescDVo.setCompId(compId);
				ptRescDVo.setRescId(rescId);
				@SuppressWarnings("unchecked")
				List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
				if(ptRescDVoList != null){
					for(PtRescDVo storedPtRescDVo : ptRescDVoList){
						model.put(storedPtRescDVo.getRescId()+"_"+storedPtRescDVo.getLangTypCd(), storedPtRescDVo.getRescVa());
					}
				}
			} else if("Y".equals(mnuYn)){
				// 리소스기본(PT_RESC_B) 테이블 - 메뉴명 조회
				PtRescBVo ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(rescId);
				@SuppressWarnings("unchecked")
				List<PtRescBVo> ptRescBVoVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);
				if(ptRescBVoVoList != null){
					for(PtRescBVo storedPtRescBVo : ptRescBVoVoList){
						model.put(storedPtRescBVo.getRescId()+"_"+storedPtRescBVo.getLangTypCd(), storedPtRescBVo.getRescVa());
					}
				}
			}
		}
		// 메뉴조합구성에서 팝업호출 하면 setFldPop
		// 기본레이아웃 설정에서 팝업호출 하면 setFld
		model.put("confirmBtnScript", (type!=null && type.equals("pop")) ? "setFldPop" : "setFld");
		return LayoutUtil.getJspPath("/pt/adm/lout/setFldPop");
	}
	
	
/* : 아이콘 레이아웃 설정 - 저장 JSON
[
{"mnuLoutId":"L00000FG","imgKndVa":"topmu_04","rescId":"R0000ARD","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"게시"},{"langTypCd":"en","rescVa":"게시"}],
	"subs":[
		{"mnuLoutId":"L00000FN","mnuGrpId":"G0000012","mnuGrpRescId":"R00000B0","mnuGrpRescNm":"게시판","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FH","imgKndVa":"topmu_02","rescId":"R0000ARE","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"전자결재"},{"langTypCd":"en","rescVa":"전자결재"}],
	"subs":[
		{"mnuLoutId":"L00000FO","mnuGrpId":"G000000N","mnuGrpRescId":"R000008N","mnuGrpRescNm":"전자결재","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FI","imgKndVa":"topmu_11","rescId":"R0000ARF","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"메일"},{"langTypCd":"en","rescVa":"메일"}],
	"subs":[
		{"mnuLoutId":"L00000FP","mnuGrpId":"G0000015","mnuGrpRescId":"R00000CF","mnuGrpRescNm":"메일","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FJ","imgKndVa":"topmu_05","rescId":"R0000ARG","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"일정"},{"langTypCd":"en","rescVa":"일정"}],
	"subs":[
		{"mnuLoutId":"L00000FQ","mnuGrpId":"G000000P","mnuGrpRescId":"R000008P","mnuGrpRescNm":"일정관리","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FK","imgKndVa":"topmu_07","rescId":"R0000ARH","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"명함"},{"langTypCd":"en","rescVa":"명함"}],
	"subs":[
		{"mnuLoutId":"L00000FR","mnuGrpId":"G000000Z","mnuGrpRescId":"R00000AX","mnuGrpRescNm":"명함관리","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FL","imgKndVa":"topmu_10","rescId":"R0000ARI","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"커뮤니티"},{"langTypCd":"en","rescVa":"커뮤니티"}],
	"subs":[
		{"mnuLoutId":"L00000FS","mnuGrpId":"G000000O","mnuGrpRescId":"R000008O","mnuGrpRescNm":"커뮤니티","mnuLoutKndCd":"G"}]},
{"mnuLoutId":"L00000FM","imgKndVa":"topmu_12","rescId":"R0000ARJ","loutLocCd":"icon",
	"rescs":[{"langTypCd":"ko","rescVa":"업무협업"},{"langTypCd":"en","rescVa":"업무협업"}],
	"subs":[
		{"mnuLoutId":"L00000FT","mnuGrpId":"G000000Y","mnuGrpRescId":"R00000AW","mnuGrpRescNm":"설문조사","mnuLoutKndCd":"G"},
		{"mnuLoutId":"L00000FU","mnuGrpId":"G0000010","mnuGrpRescId":"R00000AY","mnuGrpRescNm":"자원관리","mnuLoutKndCd":"G"},
		{"mnuLoutId":"L00000FV","mnuGrpId":"G0000011","mnuGrpRescId":"R00000AZ","mnuGrpRescNm":"작업관리","mnuLoutKndCd":"G"}]},
{"loutLocCd":"left",
	"subs":[]},
{"loutLocCd":"right",
	"subs":[
		{"mnuLoutId":"L00000CO","mnuGrpId":"G000000W","mnuGrpRescId":"R00000AQ","mnuGrpRescNm":"환경설정","mnuLoutKndCd":"G"},
		{"mnuLoutId":"L00000EC","mnuGrpId":"G000000X","mnuGrpRescId":"R00000AT","mnuGrpRescNm":"도움말","mnuLoutKndCd":"G"}]},
{"loutLocCd":"home",
	"subs":[
		{"mnuGrpId":"G000000S","mnuGrpRescId":"R000008S","mnuGrpRescNm":"홈","mnuLoutKndCd":"G"}]}
]
*/
	/** 아이콘 레이아웃 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/lout/transIconLout")
	public String transIconLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "loutCatId", required = false) String loutCatId,
			@RequestParam(value = "icoLoutUseYn", required = false) String icoLoutUseYn,
			@RequestParam(value = "delList", required = false) String delList,
			@RequestParam(value = "dataString", required = false) String dataString,
			@RequestParam(value = "activeMnuLoutId", required = false) String activeMnuLoutId,
			@RequestParam(value = "activeLoutLocCd", required = false) String activeLoutLocCd,
			@RequestParam(value = "menuId", required = false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 시스템설정 - 레이아웃 - 클래스ID
			String setupClsId = null;// PtConstant.LOUT_SETUP - "pt.layout"
			// 시스템설정 - 레이아웃
			Map<String, String> layout = ptSysSvc.getLayoutSetup();
			// 회사별 언어 설정
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
			
			// 시스템설정이 - 기존 설정된 것과 다르면
			if(icoLoutUseYn!=null && !icoLoutUseYn.equals(layout.get("icoLoutUseYn"))){
				
				if("N".equals(icoLoutUseYn) && "N".equals(layout.get("bascLoutUseYn"))){
					//pt.jsp.setIconLout.noLayout=아이콘 레이아웃과 기본 레이아웃 중 하나는 사용 해야 합니다.
					String msg = messageProperties.getMessage("pt.jsp.setIconLout.noLayout", request);
					throw new CmException(msg);
				}
				
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블
				PtSysSetupDVo ptSysSetupDVo;
//				// 현재 년월일
//				String hstYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
				// 설정분류ID
				setupClsId = PtConstant.PT_LOUT_SETUP;
				
				// 시스템설정 저장 / 이력포함
				Entry<String, String> entry;
				Iterator<Entry<String, String>> iterator = layout.entrySet().iterator();
				while(iterator.hasNext()){
					entry = iterator.next();
					
					// 시스템설정상세(PT_SYS_SETUP_D) 테이블
					ptSysSetupDVo = new PtSysSetupDVo();
					ptSysSetupDVo.setSetupClsId(setupClsId);
					ptSysSetupDVo.setSetupId(entry.getKey());
					if("icoLoutUseYn".equals(entry.getKey())){
						ptSysSetupDVo.setSetupVa(icoLoutUseYn);
					} else {
						ptSysSetupDVo.setSetupVa(entry.getValue());
					}
					queryQueue.store(ptSysSetupDVo);
				}
			}
			
			// 아이콘 레이아웃 을 사용할 경우
			if("Y".equals(icoLoutUseYn)){

				PtRescDVo ptRescDVo;
				PtMnuLoutDVo ptMnuLoutDVo;
				PtMnuLoutCombDVo ptMnuLoutCombDVo;
				
				int i, j, k, jsize, langSize, size;
				
				// 삭제 목록 삭제 처리 - 메뉴레이아웃조합상세(PT_MNU_LOUT_COMB_D) 테이블
				if(delList!=null && !delList.isEmpty()){
					String[] delArr = delList.split(",");
					for(i=0;i<delArr.length;i++){
						ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
						ptMnuLoutCombDVo.setCompId(compId);
//						ptMnuLoutCombDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
						ptMnuLoutCombDVo.setMnuLoutId(delArr[i]);
						queryQueue.delete(ptMnuLoutCombDVo);
					}
				}
				
				langSize = langTypCdList==null ? 0 : langTypCdList.size();
				
				@SuppressWarnings("rawtypes")
				Map map, subMap;
				String rescId, mnuLoutId, mnuLoutPid, mnuLoutNm, mnuLoutKndCd;
				String rescVa, rescMainVa, langCd, loutLocCd;
				JSONArray jsonSubArray, jsonArray = (JSONArray)JSONValue.parse(dataString);
				boolean newResc = false;
				size = jsonArray==null ? 0 : jsonArray.size();
				
				Integer sortOrdr = 1;
				for(i=0;i<size;i++){
					map = (Map<?, ?>)jsonArray.get(i);
					
					loutLocCd = (String)map.get("loutLocCd");//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘), 기본레이아웃(top:상단, main:메인, sub:서브), 관리자레이아웃(adm:관리자)
					
					if(i==0){
						// 메뉴레이아웃상세(PT_MNU_LOUT_D) - 예전 데이터 삭제
						ptMnuLoutDVo = new PtMnuLoutDVo();
						ptMnuLoutDVo.setCompId(compId);
						ptMnuLoutDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
						queryQueue.delete(ptMnuLoutDVo);
					}
					
					// icon 의 경우만
					if("icon".equals(loutLocCd)){
						jsonSubArray = (JSONArray)map.get("rescs");
						// rescId - 가 있는지 검사하여 없으면 생성
						newResc = false;
						rescId = (String)map.get("rescId");
						if(rescId==null || rescId.isEmpty()){
							rescId = ptCmSvc.createId("PT_RESC_D");
							newResc = true;
						}
						mnuLoutNm = null;
						
						/////////////////////////////
						// 메인(카테고리) 리소스
						
						// 리소스상세(PT_RESC_D) 테이블 - INSERT or UPDATE
						jsize = jsonSubArray==null ? 0 : jsonSubArray.size();
						for(j=0;j<jsize;j++){
							subMap = (Map<?, ?>)jsonSubArray.get(j);
							ptRescDVo = new PtRescDVo();
							ptRescDVo.setCompId(compId);
							ptRescDVo.setRescId(rescId);
							ptRescDVo.setLangTypCd((String)subMap.get("langTypCd"));
							ptRescDVo.setRescVa((String)subMap.get("rescVa"));
							// MNU_LOUT_NM(메뉴레이아웃명) 에 넣을 값
							if(mnuLoutNm==null || "ko".equals(subMap.get("langTypCd"))){
								mnuLoutNm = (String)subMap.get("rescVa");
							}
							if(newResc){
								queryQueue.insert(ptRescDVo);
							} else {
								queryQueue.store(ptRescDVo);
							}
						}
						
						/////////////////////////////
						// 메인(카테고리) - (아이콘)
						
						// 메뉴레이아웃상세(PT_MNU_LOUT_D) - INSERT (카테고리)
						ptMnuLoutDVo = new PtMnuLoutDVo();
						ptMnuLoutDVo.setCompId(compId);
						ptMnuLoutDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
						ptMnuLoutDVo.setLoutLocCd(loutLocCd);//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘), 기본레이아웃(top:상단, main:메인, sub:서브), 관리자레이아웃(adm:관리자)
						ptMnuLoutDVo.setMnuLoutKndCd("F");// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
						ptMnuLoutDVo.setImgKndVa((String)map.get("imgKndVa"));
						
						mnuLoutId = (String)map.get("mnuLoutId");
						if(mnuLoutId==null || mnuLoutId.isEmpty()){
							mnuLoutId = ptCmSvc.createId("PT_MNU_LOUT_D");
						}
						mnuLoutPid = mnuLoutId;// 서브(메뉴그룹) 에서 사용
						ptMnuLoutDVo.setMnuLoutId(mnuLoutId);//메뉴레이아웃ID - KEY
						
						ptMnuLoutDVo.setMnuLoutNm(mnuLoutNm);
						ptMnuLoutDVo.setRescId(rescId);
						ptMnuLoutDVo.setSortOrdr(sortOrdr.toString());
						sortOrdr++;
						queryQueue.insert(ptMnuLoutDVo);
					} else {
						mnuLoutPid = null;
					}
					
					/////////////////////////////
					// 서브(메뉴그룹) - 메뉴그룹 링크
					
					// 서브(메뉴그룹) - INSERT (서브)
					jsonSubArray = (JSONArray)map.get("subs");
					jsize = jsonSubArray==null ? 0 : jsonSubArray.size();
					for(j=0;j<jsize;j++){
						subMap = (Map<?, ?>)jsonSubArray.get(j);
						
						// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
						mnuLoutKndCd = (String)subMap.get("mnuLoutKndCd");
						
						// 메뉴레이아웃상세(PT_MNU_LOUT_D) - INSERT
						ptMnuLoutDVo = new PtMnuLoutDVo();
						ptMnuLoutDVo.setCompId(compId);
						ptMnuLoutDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
						ptMnuLoutDVo.setLoutLocCd(loutLocCd);//레이아웃위치코드 - 아이콘레이아웃(icon:아이콘), 기본레이아웃(top:상단, main:메인, sub:서브), 관리자레이아웃(adm:관리자)
						mnuLoutId = (String)subMap.get("mnuLoutId");
						if(mnuLoutId==null || mnuLoutId.isEmpty()){
							mnuLoutId = ptCmSvc.createId("PT_MNU_LOUT_D");
						}
						ptMnuLoutDVo.setMnuLoutId(mnuLoutId);// 메뉴레이아웃ID - KEY
						
						ptMnuLoutDVo.setMnuLoutPid(mnuLoutPid);
						ptMnuLoutDVo.setMnuLoutKndCd(mnuLoutKndCd);// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
						if("G".equals(mnuLoutKndCd)){
							ptMnuLoutDVo.setMnuGrpId((String)subMap.get("mnuGrpId"));
							ptMnuLoutDVo.setMnuGrpRescId((String)subMap.get("mnuGrpRescId"));
							ptMnuLoutDVo.setMnuLoutNm((String)subMap.get("mnuGrpRescNm"));// 메뉴레이아웃명 : DB 조회시 참조용 용 - 그외 사용 안함
						} else if("C".equals(mnuLoutKndCd)){
							rescId = (String)subMap.get("rescId");
							if(rescId==null || rescId.isEmpty()){
								rescId = ptCmSvc.createId("PT_RESC_D");
							}
							
							// 조합의 경우 리소스 입력
							rescMainVa = null;
							for(k=0;k<langSize;k++){
								langCd = langTypCdList.get(k).getCd();
								rescVa = (String)subMap.get("rescVa_"+langCd);
								if(rescVa!=null && !rescVa.isEmpty()){
									ptRescDVo = new PtRescDVo();
									ptRescDVo.setCompId(compId);
									ptRescDVo.setRescId(rescId);
									ptRescDVo.setLangTypCd(langCd);
									ptRescDVo.setRescVa(rescVa);
									queryQueue.store(ptRescDVo);
									if(rescMainVa==null || "ko".equals(langCd)){
										rescMainVa = rescVa;
									}
								}
							}
							
							ptMnuLoutDVo.setRescId(rescId);
							ptMnuLoutDVo.setMnuLoutNm(rescMainVa);// 메뉴레이아웃명 : DB 조회시 참조용 용 - 그외 사용 안함
						}
						
						ptMnuLoutDVo.setSortOrdr(sortOrdr.toString());
						sortOrdr++;
						
						queryQueue.insert(ptMnuLoutDVo);
					}
				}
			}

//System.out.println(queryQueue.getDebugString());
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			if(setupClsId!=null){
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			} else {
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			String param = (activeMnuLoutId!=null && !activeMnuLoutId.isEmpty()) ? "&mnuLoutId="+activeMnuLoutId
					: (activeLoutLocCd!=null && !activeLoutLocCd.isEmpty()) ? "&loutLocCd="+activeLoutLocCd
							: "";
			model.put("todo", "parent.location.replace('./setIconLout.do?menuId="+menuId+param+"');");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 기본 레이아웃 설정 - 저장 */
	@RequestMapping(value = {"/pt/adm/lout/transBascLout", "/pt/adm/lout/transAdmLout", "/pt/adm/lout/transMobileLout"})
	public String transBascLout(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "loutCatId", required = false) String loutCatId,
			@RequestParam(value = "bascLoutUseYn", required = false) String bascLoutUseYn,
			@RequestParam(value = "subMnuOption", required = false) String subMnuOption,
			@RequestParam(value = "delList", required = false) String delList,
			@RequestParam(value = "dataString", required = false) String dataString,
			@RequestParam(value = "activeMnuLoutId", required = false) String activeMnuLoutId,
			@RequestParam(value = "menuId", required = false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일
			if(!"A".equals(loutCatId) && !"M".equals(loutCatId)) loutCatId = "B";
			
			// 시스템설정 - 레이아웃 - 클래스ID
			String setupClsId = null;// PtConstant.LOUT_SETUP - "pt.layout"
			
			// 시스템설정 - 레이아웃
			Map<String, String> layout = ptSysSvc.getLayoutSetup();
			
			// 회사별로 설정된 언어 정보 조회 
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
			
			// 시스템설정이 - 기존 설정된 것과 다르면
			if(bascLoutUseYn!=null && "B".equals(loutCatId)){// B:기본레이아웃 일때만
				
				if("N".equals(bascLoutUseYn) && "N".equals(layout.get("icoLoutUseYn"))){
					//pt.jsp.setIconLout.noLayout=아이콘 레이아웃과 기본 레이아웃 중 하나는 사용 해야 합니다.
					String msg = messageProperties.getMessage("pt.jsp.setIconLout.noLayout", request);
					throw new CmException(msg);
				}

				// 설정분류ID
				setupClsId = PtConstant.PT_LOUT_SETUP;
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블
				PtSysSetupDVo ptSysSetupDVo;
				
				// 시스템설정 저장
				String key, value;
				Entry<String, String> entry;
				Iterator<Entry<String, String>> iterator = layout.entrySet().iterator();
				while(iterator.hasNext()){
					entry = iterator.next();
					
					// 시스템설정상세(PT_SYS_SETUP_D) 테이블
					ptSysSetupDVo = new PtSysSetupDVo();
					ptSysSetupDVo.setSetupClsId(setupClsId);
					key = entry.getKey();
					ptSysSetupDVo.setSetupId(key);
					if("bascLoutUseYn".equals(key)){
						ptSysSetupDVo.setSetupVa(bascLoutUseYn);
					} else if("subMnuOption".equals(key)){
						ptSysSetupDVo.setSetupVa(subMnuOption);
					} else if(key.endsWith("MainMnuMaxCnt")){
						continue;
					} else {
						ptSysSetupDVo.setSetupVa(entry.getValue());
					}
					queryQueue.store(ptSysSetupDVo);
				}
				
				// 어권별 메인메뉴 최대 갯수 - 저장
				int i, size = langTypCdList==null ? 0 : langTypCdList.size();
				for(i=0;i<size;i++){
					key = langTypCdList.get(i).getCd()+"MainMnuMaxCnt";
					value = request.getParameter(key);
					if(value!=null && !value.isEmpty()){
						// 시스템설정상세(PT_SYS_SETUP_D) 테이블
						ptSysSetupDVo = new PtSysSetupDVo();
						ptSysSetupDVo.setSetupClsId(setupClsId);
						ptSysSetupDVo.setSetupId(key);
						ptSysSetupDVo.setSetupVa(value);
						queryQueue.store(ptSysSetupDVo);
					}
				}
			}
			
			// 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
			if("B".equals(loutCatId) || "A".equals(loutCatId) || "M".equals(loutCatId)){

				PtMnuLoutDVo ptMnuLoutDVo;
				PtMnuLoutCombDVo ptMnuLoutCombDVo;
				
				// 삭제 목록 삭제 처리 - 메뉴레이아웃조합상세(PT_MNU_LOUT_COMB_D) 테이블
				if(delList!=null && !delList.isEmpty()){
					String[] delArr = delList.split(",");
					for(int i=0;i<delArr.length;i++){
						ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
						ptMnuLoutCombDVo.setCompId(compId);
//						ptMnuLoutCombDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
						ptMnuLoutCombDVo.setMnuLoutId(delArr[i]);
						queryQueue.delete(ptMnuLoutCombDVo);
					}
				}
				
				// 메뉴레이아웃상세(PT_MNU_LOUT_D) - 예전 데이터 삭제
				ptMnuLoutDVo = new PtMnuLoutDVo();
				ptMnuLoutDVo.setCompId(compId);
				ptMnuLoutDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
				queryQueue.delete(ptMnuLoutDVo);
				
				// 기본레이아웃 정보 모으기
				collectBascLayout(request, compId, loutCatId, queryQueue, 1, 
						(JSONArray)JSONValue.parse(dataString), langTypCdList, null);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			if(setupClsId!=null){
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			} else {
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			String params = (activeMnuLoutId==null || activeMnuLoutId.isEmpty()) ?
					"menuId="+menuId : "menuId="+menuId+"&mnuLoutId="+activeMnuLoutId;
			
			if("A".equals(loutCatId)){//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, M:모바일레이아웃
				model.put("todo", "parent.location.replace('./setAdminLout.do?"+params+"');");
			} else if("M".equals(loutCatId)){
				model.put("todo", "parent.location.replace('./setMobileLout.do?"+params+"');");
			} else {
				model.put("todo", "parent.location.replace('./setBascLout.do?"+params+"');");
			}
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
/* : 기본 레이아웃 설정 - 저장 JSON
[
	{"loutLocCd":"top","mnuGrpId":"N0009","mnuGrpRescId":"R000007E","mnuGrpRescNm":"개인화","mnuLoutKndCd":"G"},
	{"loutLocCd":"main","mnuGrpId":"N0005","mnuGrpRescId":"R000007A","mnuGrpRescNm":"결재","mnuLoutKndCd":"G"},
	{"loutLocCd":"main","mnuGrpRescNm":"협업","mnuLoutKndCd":"F","rescVa_ko":"협업","rescVa_en":"협업E","children":[
		{"loutLocCd":"main","mnuGrpId":"N0006","mnuGrpRescId":"R000007B","mnuGrpRescNm":"게시","mnuLoutKndCd":"G"},
		{"loutLocCd":"main","mnuGrpId":"N0007","mnuGrpRescId":"R000007C","mnuGrpRescNm":"커뮤니티","mnuLoutKndCd":"G"}]},
	{"loutLocCd":"main","mnuGrpRescNm":"나의메뉴","mnuLoutKndCd":"C","rescVa_ko":"나의메뉴","rescVa_en":"나의메뉴E"}
]
*/
	/** 기본 레이아웃 입력 데이터 모이기 */
	private Integer collectBascLayout(HttpServletRequest request,
			String compId, String loutCatId, QueryQueue queryQueue, Integer sortOrdr,
			JSONArray jsonArray, List<PtCdBVo> langTypCdList, String mnuLoutPid) throws SQLException {
		
		// 어권 갯수
		int i, j, size, langSize = langTypCdList==null ? 0 : langTypCdList.size();
		
		@SuppressWarnings("rawtypes")
		Map map;
		String rescId, mnuGrpRescId, mnuLoutId, loutLocCd, mnuLoutNm, mnuLoutKndCd;
		String rescVa, langCd;
		boolean newResc = false;
		size = jsonArray==null ? 0 : jsonArray.size();
		
		PtRescDVo ptRescDVo;
		PtMnuLoutDVo ptMnuLoutDVo;
		
		for(i=0;i<size;i++){
			map = (Map<?, ?>)jsonArray.get(i);
			
			// 리소스 데이터 입력
			newResc = false;
			mnuLoutNm = null;
			rescId = (String)map.get("rescId");
			mnuGrpRescId = (String)map.get("mnuGrpRescId");
			if(mnuGrpRescId==null || mnuGrpRescId.isEmpty()){
				if(rescId==null || rescId.isEmpty()){
					rescId = ptCmSvc.createId("PT_RESC_D");
					newResc = true;
				}
				
				for(j=0; j<langSize; j++){
					langCd = langTypCdList.get(j).getCd();
					rescVa = (String)map.get("rescVa_"+langCd);
					
					if(rescVa!=null){
						ptRescDVo = new PtRescDVo();
						ptRescDVo.setCompId(compId);
						ptRescDVo.setRescId(rescId);
						ptRescDVo.setLangTypCd(langCd);
						ptRescDVo.setRescVa(rescVa);
						// MNU_LOUT_NM(메뉴레이아웃명) 에 넣을 값
						if(mnuLoutNm==null || "ko".equals(langCd)){
							mnuLoutNm = rescVa;
						}
						if(newResc){
							queryQueue.insert(ptRescDVo);
						} else {
							queryQueue.store(ptRescDVo);
						}
					}
				}
			}
			
			// 메뉴레이아웃상세(PT_MNU_LOUT_D) 테이블 - INSERT
			ptMnuLoutDVo = new PtMnuLoutDVo();
			ptMnuLoutDVo.setCompId(compId);
			ptMnuLoutDVo.setLoutCatId(loutCatId);//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
			mnuLoutId = (String)map.get("mnuLoutId");
			if(mnuLoutId==null || mnuLoutId.isEmpty()){
				mnuLoutId = ptCmSvc.createId("PT_MNU_LOUT_D");
			}
			
			ptMnuLoutDVo.setMnuLoutId(mnuLoutId);
			loutLocCd = (String)map.get("loutLocCd");
			if(mnuLoutPid!=null){
				ptMnuLoutDVo.setMnuLoutPid(mnuLoutPid);
			} else {
				ptMnuLoutDVo.setMnuLoutPid(loutLocCd);
			}
			ptMnuLoutDVo.setLoutLocCd(loutLocCd);
			mnuLoutKndCd = (String)map.get("mnuLoutKndCd");
			ptMnuLoutDVo.setMnuLoutKndCd(mnuLoutKndCd);
			ptMnuLoutDVo.setMnuGrpId((String)map.get("mnuGrpId"));
			ptMnuLoutDVo.setMnuGrpRescId(mnuGrpRescId);
			
			if(mnuLoutNm==null){ mnuLoutNm = (String)map.get("mnuLoutNm"); }
			if(mnuLoutNm==null){ mnuLoutNm = (String)map.get("mnuGrpRescNm"); }
			ptMnuLoutDVo.setMnuLoutNm(mnuLoutNm);
			
			ptMnuLoutDVo.setRescId(rescId);
			ptMnuLoutDVo.setSortOrdr(sortOrdr.toString());
			sortOrdr++;
			
			queryQueue.insert(ptMnuLoutDVo);
			
			// 폴더면 - 서브 입력
			if("F".equals(mnuLoutKndCd)){
				sortOrdr = collectBascLayout(request, compId, loutCatId, queryQueue, sortOrdr, 
						(JSONArray)map.get("children"), langTypCdList, mnuLoutId);
			}
		}
		
		return sortOrdr;
	}
	
	/** 메뉴조합 - 저장 */
	@RequestMapping(value = "/pt/adm/lout/transComb")
	public String transComb(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "loutCatId", required = false) String loutCatId,
			@RequestParam(value = "mnuLoutId", required = false) String mnuLoutId,
			@RequestParam(value = "dataString", required = false) String dataString,
			ModelMap model) throws Exception {
		
		try {
			
			if(compId==null || compId.isEmpty() || loutCatId==null || loutCatId.isEmpty() || mnuLoutId==null || mnuLoutId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			String hstYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			JSONArray jsonArray = (JSONArray)JSONValue.parse(dataString);
			PtMnuLoutCombDVo ptMnuLoutCombDVo;
			
			// 메뉴레이아웃조합상세(PT_MNU_LOUT_COMB_D) 테이블 - 기존 데이터 삭제
			ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
			ptMnuLoutCombDVo.setCompId(compId);
//			ptMnuLoutCombDVo.setLoutCatId(loutCatId);
			ptMnuLoutCombDVo.setMnuLoutId(mnuLoutId);
			queryQueue.delete(ptMnuLoutCombDVo);
			
			// 메뉴조합 Json 정보 파싱하여 QueryQueue에 저장
			parseCombJson(queryQueue, jsonArray, compId, loutCatId, mnuLoutId, mnuLoutId, hstYmd, 0);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.LAYOUT_GRP, CacheConfig.LAYOUT);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.doAfterSave();");
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	/** 메뉴조합 Json 정보 파싱하여 QueryQueue에 저장 */
	private int parseCombJson(QueryQueue queryQueue, JSONArray jsonArray,
			String compId, String loutCatId, String mnuLoutId, String mnuLoutCombPid, String hstYmd, int sortOrdr) throws SQLException{
		
		Map<?,?> map, rescMap;
		boolean newResc;
		String mnuLoutCombId, rescId, langTypCd, fldYn, fldNm=null;
		PtRescDVo ptRescDVo;
		Iterator<?> iterator;
		JSONArray jsonSubArray;
		PtMnuLoutCombDVo ptMnuLoutCombDVo;
		int i, size = jsonArray==null ? 0 : jsonArray.size();
		for(i=0;i<size;i++){
			map = (Map<?, ?>)jsonArray.get(i);
			
			rescId = null;
			fldNm = null;
			// 리소스상세(PT_RESC_D) 테이블 VO - 폴더 생성 또는 폴더명 변경
			rescMap = (Map<?,?>)map.get("rescs");
			if(rescMap!=null && !rescMap.isEmpty()){
				
				newResc = false;
				rescId = (String)map.get("rescId");
				if(rescId==null || rescId.isEmpty()){
					rescId = ptCmSvc.createId("PT_RESC_D");
					newResc = true;
				}
				
				iterator = rescMap.keySet().iterator();
				while(iterator.hasNext()){
					langTypCd = (String)iterator.next();
					ptRescDVo = new PtRescDVo();
					ptRescDVo.setCompId(compId);
					ptRescDVo.setLangTypCd(langTypCd);
					ptRescDVo.setRescId(rescId);
					ptRescDVo.setRescVa((String)rescMap.get(langTypCd));
					if(newResc) queryQueue.insert(ptRescDVo);
					else queryQueue.store(ptRescDVo);
					
					if(fldNm==null || "ko".equals(langTypCd)){
						fldNm = ptRescDVo.getRescVa();
					}
				}
			} else {
				rescId = (String)map.get("rescId");
				if(rescId!=null && rescId.isEmpty()) rescId = null;
			}
			
			mnuLoutCombId = (String)map.get("mnuLoutCombId");
			if(mnuLoutCombId==null) {
				mnuLoutCombId = ptCmSvc.createId("PT_MNU_LOUT_COMB_D");
			}
			
			ptMnuLoutCombDVo = new PtMnuLoutCombDVo();
			ptMnuLoutCombDVo.setCompId(compId);
			ptMnuLoutCombDVo.setLoutCatId(loutCatId);
			ptMnuLoutCombDVo.setMnuLoutId(mnuLoutId);
			ptMnuLoutCombDVo.setMnuLoutCombId(mnuLoutCombId);
			ptMnuLoutCombDVo.setMnuLoutCombPid(mnuLoutCombPid);
			
			fldYn = (String)map.get("fldYn");
			if("Y".equals(fldYn)){
				if(fldNm==null){
					ptMnuLoutCombDVo.setFldNm((String)map.get("mnuRescNm"));
				} else {
					ptMnuLoutCombDVo.setFldNm(fldNm);
				}
			}
			ptMnuLoutCombDVo.setFldYn(fldYn);
			ptMnuLoutCombDVo.setRescId(rescId);
			ptMnuLoutCombDVo.setMnuId((String)map.get("mnuId"));
			if(rescId==null){
				ptMnuLoutCombDVo.setMnuRescId((String)map.get("mnuRescId"));
			}
			ptMnuLoutCombDVo.setSortOrdr(Integer.toString(++sortOrdr));
			queryQueue.insert(ptMnuLoutCombDVo);
			
			jsonSubArray = (JSONArray)map.get("children");
			if(jsonSubArray!=null && !jsonSubArray.isEmpty()){
				sortOrdr = parseCombJson(queryQueue, jsonSubArray, compId, loutCatId, mnuLoutId, mnuLoutCombId, hstYmd, sortOrdr);
			}
/*
// 변경
[
	{"mnuLoutCombId":"C0000AN2","fldYn":"Y","fldNm":"111","rescId":"R0000AOY","mnuId":"","mnuRescId":"","children":[
		{"mnuLoutCombId":"C0000AN3","fldYn":"Y","fldNm":"22","rescId":"","mnuId":"M000000X","mnuRescId":"R0000072","children":[
			{"mnuLoutCombId":"C0000AN4","fldYn":"N","fldNm":"","rescId":"","mnuId":"M0000010","mnuRescId":"R0000075"},
			{"mnuLoutCombId":"C0000AN5","fldYn":"N","fldNm":"","rescId":"","mnuId":"M0000011","mnuRescId":"R0000076"}]
		},
		{"mnuLoutCombId":"C0000AN6","fldYn":"Y","fldNm":"33333","rescId":"R0000AOZ","mnuId":"M000000U","mnuRescId":"","children":[
			{"mnuLoutCombId":"C0000AN7","fldYn":"N","fldNm":"","rescId":"","mnuId":"M0000012","mnuRescId":"R0000077"},
			{"mnuLoutCombId":"C0000AN8","fldYn":"N","fldNm":"","rescId":"","mnuId":"M0000013","mnuRescId":"R0000078"}]
		}]
	}
]
// 신규 저장
[
	{"fldYn":"Y","sortOrdr":"1","rescs":{"ko":"11","en":"11e"},"children":[
		{"mnuId":"M000000X","fldYn":"Y","mnuRescId":"R0000072","mnuRescNm":"22","sortOrdr":"1","children":[
			{"mnuId":"M0000010","fldYn":"N","mnuRescId":"R0000075","mnuRescNm":"22-1","sortOrdr":"1"},
			{"mnuId":"M0000011","fldYn":"N","mnuRescId":"R0000076","mnuRescNm":"22-2","sortOrdr":"2"}]
		},
		{"mnuId":"M000000U","fldYn":"Y","mnuRescId":"R0000071","mnuRescNm":"33","sortOrdr":"2","rescs":{"ko":"3333","en":"3333e"},"children":[
			{"mnuId":"M0000012","fldYn":"N","mnuRescId":"R0000077","mnuRescNm":"33-1","sortOrdr":"1"},
			{"mnuId":"M0000013","fldYn":"N","mnuRescId":"R0000078","mnuRescNm":"33-2","sortOrdr":"2"}]
		}]
	}
]
 */
			
		}
		return sortOrdr;
	}
	
	/** 스킨 이미지 설정 */
	@RequestMapping(value = "/pt/adm/lout/setSkinImg")
	public String setSkinImg(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 시스템설정 - 레이아웃
		Map<String, String> layout = ptSysSvc.getLayoutSetup();
		model.put("layout", layout);
		
		return LayoutUtil.getJspPath("/pt/adm/lout/setSkinImg");
	}
	/** 스킨 이미지 설정 - 저장 (로고, 배경이미지 업로드) */
	@RequestMapping(value = "/pt/adm/lout/transSkinImg")
	public String transSkinImg(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		UserVo userVo = LoginSession.getUser(request);
		
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "pt");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
//			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			
			String path = "images/upload/pt/skin";
			DistHandler distHandler = distManager.createHandler(path, locale);//업로드 경로 설정
			
			PtSysSetupDVo ptSysSetupDVo;
			
			QueryQueue queryQueue = new QueryQueue();
			
//			// 현재 년월일
//			String hstYmd = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
			
			String setupClsId = PtConstant.PT_SKIN_IMG + userVo.getCompId();
			
			// 시스템설정 - 레이아웃
			String _cxPth = (String)request.getAttribute("_cxPth");
			Map<String, String> layout = ptSysSvc.getSkinImage(userVo.getCompId(), _cxPth);
			
			Iterator<Entry<String, String>> entries = layout.entrySet().iterator();
			Entry<String, String> entry;
			String setupId, webPath;
			
			while(entries.hasNext()){
				entry = entries.next();
				setupId = entry.getKey();
				
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 저장
				ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId(setupId);
				
				if(fileMap.get(setupId)==null){
					// 업로드한 파일이 없으면 - 예전 데이터 세팅
					ptSysSetupDVo.setSetupVa(entry.getValue());
				} else {
					// 업로드한 파일이 있으면 - 배포 경로로 세팅
					webPath = distHandler.addWebList(uploadHandler.getAbsolutePath(setupId));
					ptSysSetupDVo.setSetupVa(webPath);
				}
				queryQueue.store(ptSysSetupDVo);
			}
			
			// 이미지 배포
			distHandler.distribute();
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
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
	/** 스킨 이미지 설정 - 초기화 (로고, 배경이미지 업로드) */
	@RequestMapping(value = "/pt/adm/lout/transSkinImgResetAjx")
	public String transSkinImgResetAjx(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		String setupClsId = PtConstant.PT_SKIN_IMG + userVo.getCompId();
		
		PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
		ptSysSetupDVo.setSetupClsId(setupClsId);
		queryQueue.delete(ptSysSetupDVo);
		
		// 캐쉬 삭제
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
		
		//cm.msg.setting.success=설정 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("result", "ok");
		
		return JsonUtil.returnJson(model);
	}
	
	/** [POPUP] 스킨 이미지 보기 */
	@RequestMapping(value = {"/pt/adm/lout/viewSkinImgPop", "/pt/adm/mobile/viewSkinImgPop"})
	public String viewSkinImgPop(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "setupId", required = false) String setupId,
			ModelMap model) throws Exception {
		
		boolean isMobile = request.getRequestURI().startsWith("/pt/adm/mobile/");
		if(setupId==null || setupId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		UserVo userVo = LoginSession.getUser(request);
		if(compId==null || compId.isEmpty()) compId=userVo.getCompId();
		
		if(isMobile){
			// 서버 설정 목록 조회
			Map<String, String> mobLginMap = ptSysSvc.getMobileLogin(userVo.getLangTypCd());
			if(!mobLginMap.containsKey(setupId)){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("skinImg", mobLginMap.get(setupId));
		}else{
			Map<String, String> layout = ptSysSvc.getSysSetupMap(PtConstant.PT_SKIN_IMG+compId, true);
			
			if(!layout.containsKey(setupId)){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("skinImg", layout.get(setupId));
		}
		
		
		return LayoutUtil.getJspPath("/pt/adm/lout/viewSkinImgPop");
	}
	
}
