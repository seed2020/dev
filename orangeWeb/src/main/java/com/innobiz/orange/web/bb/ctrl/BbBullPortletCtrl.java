package com.innobiz.orange.web.bb.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFavotSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullRecmdSvc;
import com.innobiz.orange.web.bb.svc.BbBullScreSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullSubmLVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaMyBullMVo;
import com.innobiz.orange.web.bb.vo.BaPltSetupDVo;
import com.innobiz.orange.web.bb.vo.BaSetupBVo;
import com.innobiz.orange.web.bb.vo.BaUserSetupBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;


/* 게시물 */
@Controller
public class BbBullPortletCtrl {

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

//	/** 게시판 공통 서비스 */
//	@Autowired
//	private BbCmSvc bbCmSvc;

	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;

	/** 게시대상 서비스 */
	@Resource(name = "bbBullTgtSvc")
	public BbBullTgtSvc bbBullTgtSvc;

	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;

	/** 게시물사진 서비스 */
	@Resource(name = "bbBullPhotoSvc")
	private BbBullPhotoSvc bbBullPhotoSvc;

	/** 게시물 추천 서비스 */
	@Resource(name = "bbBullRecmdSvc")
	private BbBullRecmdSvc bbBullRecmdSvc;

	/** 게시물 찬반투표 서비스 */
	@Resource(name = "bbBullFavotSvc")
	private BbBullFavotSvc bbBullFavotSvc;

	/** 게시물 점수주기 서비스 */
	@Resource(name = "bbBullScreSvc")
	private BbBullScreSvc bbBullScreSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 탭형 게시판 */
	@RequestMapping(value = "/bb/plt/listBullTabPlt")
	public String listBullTabPlt(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pltId", required=false) String pltId,
			ModelMap model) throws Exception {
		
		// 목록 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 
		
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		List<BaPltSetupDVo> storedBaPltSetupDVoList = new ArrayList<BaPltSetupDVo>();
		
		// 필수 게시판 조회
		BaPltSetupDVo requiredVo=new BaPltSetupDVo();
		requiredVo.setUserUid(userVo.getCompId());
		requiredVo.setPltId(pltId);
		requiredVo.setQueryLang(langTypCd);
		requiredVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<BaPltSetupDVo> requiredVoList=(List<BaPltSetupDVo>) commonSvc.queryList(requiredVo);
		if(requiredVoList!=null && requiredVoList.size()>0)
			storedBaPltSetupDVoList.addAll(requiredVoList);
		
		List<BaPltSetupDVo> list= queryBaPltSetupDVoList(userVo, pltId, langTypCd);
		List<BaPltSetupDVo> removeList=new ArrayList<BaPltSetupDVo>();
		if(list!=null && list.size()>0){
			for(BaPltSetupDVo storedBaPltSetupDVo : list){
				for(BaPltSetupDVo storedRequiredVo : requiredVoList){
					if(storedBaPltSetupDVo.getBxId().equals(storedRequiredVo.getBxId())) removeList.add(storedBaPltSetupDVo);
				}
			}
			for(BaPltSetupDVo storedBaPltSetupDVo : removeList){
				list.remove(storedBaPltSetupDVo);
			}
			if(list.size()>0) storedBaPltSetupDVoList.addAll(list);
		}
				
		List<BaPltSetupDVo> targetBaPltSetupDVoList = new ArrayList<BaPltSetupDVo>();  
		List<BaPltSetupDVo> baPltSetupDVoList = new ArrayList<BaPltSetupDVo>(); 
		
		// 권한 있는 게시판 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);

		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("BRD_FNC_MENU", langTypCd, "Y");
		for (PtCdBVo cdVo : brdFncList) {
			if (cdVo.getRefVa2()==null || cdVo.getRefVa2().isEmpty()) continue; 
			url = cdVo.getRefVa1();
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				BaPltSetupDVo baPltSetupDVo = new BaPltSetupDVo();	
				baPltSetupDVo.setBxId(cdVo.getRefVa2());
				baPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				baPltSetupDVo.setMenuId(menuId);
				targetBaPltSetupDVoList.add(baPltSetupDVo);
			}
		}
		
		// 최초 설정상태의 경우 기능메뉴(최신게시글, 나의게시물)를 디폴트로 설정하고 추가한다. 
		if(storedBaPltSetupDVoList==null || storedBaPltSetupDVoList.isEmpty()){
			for(BaPltSetupDVo baPltSetupDVo  : targetBaPltSetupDVoList)
			{
				if(baPltSetupDVo.getBxId().equals("new") || baPltSetupDVo.getBxId().equals("my"))
					storedBaPltSetupDVoList.add(baPltSetupDVo);
			}
		}

		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
		
		if(mdIds != null && baBrdBVoList != null){
			for(String mdId : mdIds){
				for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
					if(mdId.equals(storedBaBrdBVo.getBrdId())){
						BaPltSetupDVo baPltSetupDVo = new BaPltSetupDVo();	
						baPltSetupDVo.setBxId(storedBaBrdBVo.getBrdId());
						baPltSetupDVo.setBxNm(storedBaBrdBVo.getRescNm());
						targetBaPltSetupDVoList.add(baPltSetupDVo);
						break;
					}
				}
			}
		}

		for(BaPltSetupDVo storedBaPltSetupDVo  : storedBaPltSetupDVoList)
		{
			//대상목록에서 리소스명을 가져온다.
			for(BaPltSetupDVo targetBaPltSetupDVo  : targetBaPltSetupDVoList)
			{
				if(storedBaPltSetupDVo.getBxId().equals(targetBaPltSetupDVo.getBxId()))
				{
					storedBaPltSetupDVo.setBxNm(targetBaPltSetupDVo.getBxNm());
					storedBaPltSetupDVo.setMenuId(targetBaPltSetupDVo.getMenuId());
					break;
				}
			}
			
			// 대상목록에 미존재시(관리자삭제시) 제외한다.
			if(storedBaPltSetupDVo.getBxNm() != null && !storedBaPltSetupDVo.getBxNm().equals(""))
				baPltSetupDVoList.add(storedBaPltSetupDVo);	
		}
		
		List<BaPltSetupDVo> baPltSetupDVoMnuList = new ArrayList<BaPltSetupDVo>(); 
		for(BaPltSetupDVo baPltSetupDVo  : baPltSetupDVoList)
		{
			if(!(baPltSetupDVo.getBxId().equals("new") || baPltSetupDVo.getBxId().equals("my") || baPltSetupDVo.getBxId().equals("disc")))
			{
				url = "/bb/listBull.do?brdId="+baPltSetupDVo.getBxId();
				menuId = ptSecuSvc.getSecuMenuId(userVo, url);
				if(menuId == null)
					continue;
				ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
				if(ptMnuDVo != null){
					baPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					baPltSetupDVo.setMenuId(menuId);
				}
			}
			
			baPltSetupDVoMnuList.add(baPltSetupDVo);
		}
				
		model.put("brdId", baPltSetupDVoMnuList.size()==0?"":baPltSetupDVoMnuList.get(0).getBxId());
		model.put("menuId", baPltSetupDVoMnuList.size()==0?"":baPltSetupDVoMnuList.get(0).getMenuId());
		model.put("baPltSetupDVoList", baPltSetupDVoMnuList);
		model.put("paramsForList", ParamUtil.getQueryString(request, "brdId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/bb/plt/listBullTabPlt");
	}
	
	/** 단독 게시판 */
	@RequestMapping(value = "/bb/plt/listBullPlt")
	public String listBullPlt(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			@Parameter(name="brdId", required=false) String brdId,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 권한 있는 게시판 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);

		List<BaPltSetupDVo> baPltSetupDVoMnuList = new ArrayList<BaPltSetupDVo>(); 

		BaPltSetupDVo baPltSetupDVo = new BaPltSetupDVo();
		baPltSetupDVo.setBxId(brdId);
		
		url = "/bb/listBull.do?brdId="+baPltSetupDVo.getBxId();
		menuId = ptSecuSvc.getSecuMenuId(userVo, url);
		if(menuId == null)
			menuId = "";
		ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
		if(ptMnuDVo != null){
			baPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
			baPltSetupDVo.setMenuId(menuId);
		}
		
		baPltSetupDVoMnuList.add(baPltSetupDVo);
		
		model.put("brdId", baPltSetupDVoMnuList.size()==0?"":baPltSetupDVoMnuList.get(0).getBxId());
		model.put("menuId", baPltSetupDVoMnuList.size()==0?"":baPltSetupDVoMnuList.get(0).getMenuId());
		model.put("baPltSetupDVoList", baPltSetupDVoMnuList);
		model.put("paramsForList", ParamUtil.getQueryString(request, "brdId", "hghtPx", "menuId"));
		return LayoutUtil.getJspPath("/bb/plt/listBullTabPlt");
	}
	
	/** 탭형 게시판 프레임 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listBullTabFrm")
	public String listBullTabFrm(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		//String dftBrdIds = "new,my,disc";//brdId가 없는 게시판(고정) - 최신,나의,심의
		Map<String, Object> listMap;//vo를 map에 담는다
		List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();//map vo를 list에 담는다.
		List<BbBullLVo> list = new ArrayList<BbBullLVo>();
		
		if(brdId == null || brdId.isEmpty()) brdId = "my";//brdId가 없을경우 나의게시물로 조회
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입
		
		
		Integer recodeCount;
		if("new".equals(brdId) || "my".equals(brdId) || "disc".equals(brdId)){
			if("disc".equals(brdId)){
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
				BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
				baBullSubmLVo.setQueryLang(langTypCd);
				//baBullSubmLVo.setPageRowCnt(pageRowCnt);
				String orderBy = "T.BULL_ID DESC";
				baBullSubmLVo.setOrderBy(orderBy);
				
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - COUNT
				baBullSubmLVo.setDiscrUid(userVo.getUserUid());
				baBullSubmLVo.setBullStatCd("S");
				recodeCount = commonSvc.count(baBullSubmLVo);
				PersonalUtil.setPaging(request, baBullSubmLVo, recodeCount);
				
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
				list = (List<BbBullLVo>) commonSvc.queryList(baBullSubmLVo);
				
				model.put("listPage", "listDiscBull");
				model.put("viewPage", "setDiscBull");
			}else {
				// 모듈 별 권한 있는 모듈참조ID 목록
				List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
				
				// 게시판ID 목록
				List<String> brdIdList = new ArrayList<String>();
				// 기한
				int ddln = 4;
				if("my".equals(brdId)){
					// 나의게시물설정(BA_MY_BULL_M) 테이블 - SELECT
					BaMyBullMVo paramMyBullVo = new BaMyBullMVo();
					paramMyBullVo.setUserUid(userVo.getUserUid());
					List<BaMyBullMVo> baMyBullMVoList = (List<BaMyBullMVo>) commonSvc.queryList(paramMyBullVo);
							
					for (BaMyBullMVo baMyBullMVo : baMyBullMVoList) {
						if(!bbBrdSvc.chkMdIds(mdIds, baMyBullMVo.getBrdId())) continue;
						brdIdList.add(baMyBullMVo.getBrdId());
					}
					
					// 기한(BA_SETUP_B) 테이블 - SELECT
					BaUserSetupBVo paramUserSetupVo = new BaUserSetupBVo();
					paramUserSetupVo.setUserUid(userVo.getUserUid());
					BaUserSetupBVo baUserSetupBVo = (BaUserSetupBVo) commonSvc.queryVo(paramUserSetupVo);
					
					if (baUserSetupBVo != null && baUserSetupBVo.getDdln() != null) {
						ddln = baUserSetupBVo.getDdln();
					}
					model.put("listPage", "listMyBull");
					model.put("viewPage", "viewMyBull");
				}else{
					// 게시판관리(BA_BRD_B) 테이블 - SELECT
					BaBrdBVo baBrdBVo = new BaBrdBVo();
					baBrdBVo.setQueryLang(langTypCd);
					bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
					baBrdBVo.setWhereSqllet("AND T.LAST_BULL_YN = 'Y'");
					List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
					
					for (BaBrdBVo brdVo : baBrdBVoList) {
						if(!bbBrdSvc.chkMdIds(mdIds, brdVo.getBrdId()))	continue;
						brdIdList.add(brdVo.getBrdId());
					}
					
					// 기한(BA_SETUP_B) 테이블 - SELECT
					BaSetupBVo paramSetupVo = new BaSetupBVo();
					BaSetupBVo baSetupBVo = (BaSetupBVo) commonSvc.queryVo(paramSetupVo);
					
					if (baSetupBVo != null && baSetupBVo.getDdln() != null) {
						ddln = baSetupBVo.getDdln();
					}
					model.put("listPage", "listNewBull");
					model.put("viewPage", "viewNewBull");
				}
				
				// 최신게시물 목록 얻기
				List<BbBullLVo> bbBullLVoList = bbBullSvc.getNewBullList(request, brdIdList, ddln);
				
				BbBullLVo paramBullVo = new BbBullLVo();
				recodeCount = bbBullLVoList.size();
				PersonalUtil.setPaging(request, paramBullVo, recodeCount);
				
				int fromIndex = (paramBullVo.getPageNo() - 1) * paramBullVo.getPageRowCnt();
				int toIndex = paramBullVo.getPageNo() * paramBullVo.getPageRowCnt();
				toIndex = toIndex < recodeCount ? toIndex : recodeCount;
				list = bbBullLVoList.subList(fromIndex, toIndex);
			}
		}else{
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			model.put("baBrdBVo", baBrdBVo);

			// 컬럼표시여부 리스트
			List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null, null, false);
			model.put("baColmDispDVoList", baColmDispDVoList);
			
			// 확장컬럼 코드 리스트 model에 추가
			bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, null);
			
			// 담당자 정보 세팅
			bbBrdSvc.setPichVo(baBrdBVo, langTypCd);
			
			if ("Y".equals(baBrdBVo.getCatYn())) {
				// 카테고리 목록 얻기
				model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
			}
			
			// 게시물(BB_X000X_L) 테이블 - BIND
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
			VoUtil.bind(request, paramBullVo);
			
			// 사용자정보 세팅
			bbBullSvc.setUserInfo(request, paramBullVo);
			
			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				if (userVo.getDeptId() == null) {
					paramBullVo.setDeptId("DEPT_ID_NULL");
				} else {
					bbBullSvc.setOrgHstIdList(langTypCd, userVo, paramBullVo);
					//paramBullVo.setDeptId(userVo.getDeptId());
				}
			}
			
			// 전사게시판이면
			if ("Y".equals(baBrdBVo.getAllCompYn())) {
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				bbBullSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, paramBullVo);
			} else {
				paramBullVo.setCompId(userVo.getCompId());
			}
			
			// 답변형인 경우
			String orderBy = "T.BULL_ID DESC";
			if ("Y".equals(baBrdBVo.getReplyYn())) {
				orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
			}
			paramBullVo.setOrderBy(orderBy);
			// 게시물(BB_X000X_L) 테이블 - COUNT
			recodeCount = commonSvc.count(paramBullVo);
			PersonalUtil.setPaging(request, paramBullVo, recodeCount);
			
			// 게시물 목록
			List<BbBullLVo> bbBullLVoList = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
			//list = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
			
			// 공지글 목록
			// 게시물(BB_X000X_L) 테이블 - SELECT
			paramBullVo.setNotcYn("Y");
			String strtYmd = StringUtil.afterDays(-baBrdBVo.getNotcDispPrd());
			paramBullVo.setStrtYmd(strtYmd);
			list = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
			if(list!=null && !list.isEmpty() && ( pagingYn == null || "".equals(pagingYn) || "N".equals(pagingYn) )){
				int maxCnt = rowCnt-list.size(); 
				BbBullLVo tempBbBullLVo = null;
				if(maxCnt > bbBullLVoList.size())
					maxCnt = bbBullLVoList.size();
				for(int i=0;i<maxCnt;i++){
					tempBbBullLVo = bbBullLVoList.get(i);
					list.add(tempBbBullLVo);
				}
			}else{
				list.addAll(bbBullLVoList);//공지글과 게시글 통합
			}
			model.put("listPage", "listBull");
			model.put("viewPage", "viewBull");
		}
		
		//목록을 vo => map => list 형태로 변환
		if(list!=null && !list.isEmpty()){
			for(BbBullLVo storedBbBullLVo : list){
				listMap = VoUtil.toMap(storedBbBullLVo, null);
				rsltMapList.add(listMap);
			}
			model.put("rsltMapList", rsltMapList);
			model.put("recodeCount", recodeCount);
		}
		
		return LayoutUtil.getJspPath("/bb/plt/listBullTabFrm");
	}
	
	/** 게시판통합 포틀릿 설정 */
	@RequestMapping(value = {"/bb/plt/setBaBxPltSetupPop", "/bb/plt/adm/setBaBxPltSetupPop"})
	public String setBaBxPltSetupPop(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			@Parameter(name="byAdm", required=false) String byAdm,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<BaPltSetupDVo> storedBaPltSetupDVoList = queryBaPltSetupDVoList(userVo, pltId, langTypCd);
		List<BaPltSetupDVo> targetBaPltSetupDVoList = new ArrayList<BaPltSetupDVo>();  
		List<BaPltSetupDVo> baPltSetupDVoList = new ArrayList<BaPltSetupDVo>(); 
		List<String> storedBxIdList = new ArrayList<String>();
		
		// 권한 있는 게시판 체크
		String url, menuId;
		PtMnuDVo ptMnuDVo;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);

		// 포틀렛설정 가능한 기능메뉴 
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("BRD_FNC_MENU", langTypCd, "Y");
		for (PtCdBVo cdVo : brdFncList) {
			if (cdVo.getRefVa2()==null || cdVo.getRefVa2().isEmpty()) continue; 
			url = cdVo.getRefVa1();
			menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
			if(ptMnuDVo != null){
				BaPltSetupDVo baPltSetupDVo = new BaPltSetupDVo();	
				baPltSetupDVo.setBxId(cdVo.getRefVa2());
				baPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
				baPltSetupDVo.setMenuId(menuId);
				targetBaPltSetupDVoList.add(baPltSetupDVo);
			}
		}
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
		
		if(mdIds != null && baBrdBVoList != null){
			for(String mdId : mdIds){
				for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
					if(mdId.equals(storedBaBrdBVo.getBrdId())){
						BaPltSetupDVo baPltSetupDVo = new BaPltSetupDVo();	
						baPltSetupDVo.setBxId(storedBaBrdBVo.getBrdId());
						baPltSetupDVo.setBxNm(storedBaBrdBVo.getRescNm());
						targetBaPltSetupDVoList.add(baPltSetupDVo);
						break;
					}
				}
			}
		}
		
		// 관리자 여부
		boolean isSysAdmin = byAdm!=null && !byAdm.isEmpty() && "U0000001".equals(userVo.getUserUid());
		
		// 최초 설정상태의 경우 기능메뉴(최신게시글, 나의게시물)를 디폴트로 설정하고 추가한다. 
		if(storedBaPltSetupDVoList==null || storedBaPltSetupDVoList.isEmpty()){
			for(BaPltSetupDVo baPltSetupDVo  : targetBaPltSetupDVoList)
			{
				if(!isSysAdmin && baPltSetupDVo.getBxId().equals("new") || baPltSetupDVo.getBxId().equals("my"))
					baPltSetupDVo.setUseYn("Y");
				baPltSetupDVoList.add(baPltSetupDVo);
			}
		}
		else
		{
			// 저장목록이 있을경우 우선순위 정렬로 추가한다.
			for(BaPltSetupDVo storedBaPltSetupDVo  : storedBaPltSetupDVoList)
			{
				storedBxIdList.add(storedBaPltSetupDVo.getBxId());
				storedBaPltSetupDVo.setUseYn("Y");
				
				//대상목록에서 리소스명을 가져온다.
				for(BaPltSetupDVo targetBaPltSetupDVo  : targetBaPltSetupDVoList)
				{
					if(storedBaPltSetupDVo.getBxId().equals(targetBaPltSetupDVo.getBxId()))
					{
						storedBaPltSetupDVo.setBxNm(targetBaPltSetupDVo.getBxNm());
						break;
					}
				}
				
				// 대상목록에 미존재시(관리자삭제시) 제외한다.
				if(storedBaPltSetupDVo.getBxNm() != null && !storedBaPltSetupDVo.getBxNm().equals(""))
					baPltSetupDVoList.add(storedBaPltSetupDVo);	
			}
			
			// 대상목록에서 저장목록을 제외하고 추가한다.
			for(BaPltSetupDVo targetBaPltSetupDVo  : targetBaPltSetupDVoList)
			{
				if(storedBxIdList.contains(targetBaPltSetupDVo.getBxId())) continue;
				baPltSetupDVoList.add(targetBaPltSetupDVo);
			}
		}
		

		List<BaPltSetupDVo> baPltSetupDVoMnuList = new ArrayList<BaPltSetupDVo>(); 
		for(BaPltSetupDVo baPltSetupDVo  : baPltSetupDVoList)
		{
			if(!(baPltSetupDVo.getBxId().equals("new") || baPltSetupDVo.getBxId().equals("my") || baPltSetupDVo.getBxId().equals("disc")))
			{
				url = "/bb/listBull.do?brdId="+baPltSetupDVo.getBxId();
				menuId = ptSecuSvc.getSecuMenuId(userVo, url);
				if(menuId == null)
					continue;
				ptMnuDVo = getMenuByMenuId(menuId, loutCombByCombIdMap);
				if(ptMnuDVo != null){
					baPltSetupDVo.setBxNm(ptMnuDVo.getRescNm());
					baPltSetupDVo.setMenuId(menuId);
				}
			}
			
			baPltSetupDVoMnuList.add(baPltSetupDVo);
		}
		
		// 필수 게시판 조회
		BaPltSetupDVo requiredVo=new BaPltSetupDVo();
		requiredVo.setUserUid(userVo.getCompId());
		requiredVo.setPltId(pltId);
		@SuppressWarnings("unchecked")
		List<BaPltSetupDVo> requiredVoList=(List<BaPltSetupDVo>) commonSvc.queryList(requiredVo);
		// 필수여부 체크
		if(isSysAdmin || (requiredVoList!=null && requiredVoList.size()>0)){
			for(BaPltSetupDVo mnuVo : baPltSetupDVoMnuList){
				if(isSysAdmin) mnuVo.setUseYn("N");
				if(requiredVoList!=null && requiredVoList.size()>0){
					for(BaPltSetupDVo storedBaPltSetupDVo : requiredVoList){
						if(storedBaPltSetupDVo.getBxId().equals(mnuVo.getBxId())){
							mnuVo.setUseYn("Y");
							if(!isSysAdmin) mnuVo.setRequiredYn("Y");
						}
					}
				}
			}
			/*if(!isSysAdmin && baPltSetupDVoMnuList.size()>0 && (requiredVoList!=null && requiredVoList.size()>0)){
				Comparator<BaPltSetupDVo> comp = new Comparator<BaPltSetupDVo>(){
					@Override
					public int compare(BaPltSetupDVo o1, BaPltSetupDVo o2) {
						int sortOrder1=Integer.parseInt(o1.getSortOrdr());
						int sortOrder2=Integer.parseInt(o2.getSortOrdr());
						return sortOrder1<sortOrder2 ? -1 : sortOrder1>sortOrder2 ? 0 : 1; 
					}
				};
				// 정렬
				Collections.sort(baPltSetupDVoMnuList , comp);
			}*/
			
		}
		
		model.put("baPltSetupDVoList", baPltSetupDVoMnuList);
		model.put("isSysAdmin", isSysAdmin);
		
		return LayoutUtil.getJspPath("/bb/plt/setBaBxPltSetupPop");
	}
	
	/** 사용자별 결재 포틀릿 설정 조회 */
	private List<BaPltSetupDVo> queryBaPltSetupDVoList(UserVo userVo, String pltId, String langTypCd) throws SQLException{
		BaPltSetupDVo BaPltSetupDVo = new BaPltSetupDVo();
		BaPltSetupDVo.setUserUid(userVo.getUserUid());
		BaPltSetupDVo.setPltId(pltId);
		BaPltSetupDVo.setQueryLang(langTypCd);
		BaPltSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<BaPltSetupDVo> BaPltSetupDVoList = (List<BaPltSetupDVo>)commonSvc.queryList(BaPltSetupDVo);
		return BaPltSetupDVoList;
	}
	
	/** [AJAX]  포틀릿 설정 저장 */
	@RequestMapping(value = {"/bb/plt/transBaBxPltSetupAjx", "/bb/plt/adm/transBaBxPltSetupAjx"})
	public String transBaBxPltSetupAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="byAdm", required=false) String byAdm,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 관리자 여부
		boolean isSysAdmin = byAdm!=null && !byAdm.isEmpty() && "U0000001".equals(userVo.getUserUid());
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String bxIds = (String)jsonObject.get("bxIds");
		String pltId = (String)jsonObject.get("pltId");
		
		QueryQueue queryQueue = new QueryQueue();
		BaPltSetupDVo baPltSetupDVo;
		
		// 전체삭제(관리자)
		if((bxIds==null || bxIds.isEmpty())){
			baPltSetupDVo = new BaPltSetupDVo();
			baPltSetupDVo.setUserUid(isSysAdmin ? userVo.getCompId() : userVo.getUserUid());
			queryQueue.delete(baPltSetupDVo);
		}
		
		if(bxIds!=null && !bxIds.isEmpty()){
			
			baPltSetupDVo = new BaPltSetupDVo();
			if(isSysAdmin) baPltSetupDVo.setUserUid(userVo.getCompId());
			else baPltSetupDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(baPltSetupDVo);
			Integer sortOrdr = 1;
			
			for(String bxId : bxIds.split(",")){
				baPltSetupDVo = new BaPltSetupDVo();
				if(isSysAdmin) baPltSetupDVo.setUserUid(userVo.getCompId());
				else baPltSetupDVo.setUserUid(userVo.getUserUid());
				baPltSetupDVo.setBxId(bxId);
				baPltSetupDVo.setPltId(pltId);
				baPltSetupDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(baPltSetupDVo);
			}
		}

		try {
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("result", "ok");
			
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			/*LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+e.getStackTrace()[0].toString()
					+(message==null || message.isEmpty() ? "" : "\n"+message));*/
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** menuId로 메뉴 구하기 */
	private PtMnuDVo getMenuByMenuId(String menuId, Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap){
		if(menuId==null) return null;
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
}
