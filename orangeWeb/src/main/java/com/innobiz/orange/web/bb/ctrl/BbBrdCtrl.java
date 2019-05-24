package com.innobiz.orange.web.bb.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.svc.BbRescSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaMyBullMVo;
import com.innobiz.orange.web.bb.vo.BaRescBVo;
import com.innobiz.orange.web.bb.vo.BaSetupBVo;
import com.innobiz.orange.web.bb.vo.BaTblBVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BaUserSetupBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfMdFormDVo;

/* 게시판관리 */
@Controller
public class BbBrdCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBrdCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "bbRescSvc")
	private BbRescSvc bbRescSvc;

	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 모듈 양식 서비스 */
	@Resource(name = "wfMdFormSvc")
	private WfMdFormSvc wfMdFormSvc;
	
	/** 게시판관리 목록조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/listBb")
	public String listBb(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리(BA_TBL_B) 테이블 - SELECT
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setQueryLang(langTypCd);
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baTblBVo.setCompId(userVo.getCompId());
		//}
		List<BaTblBVo> baTblBVoList = (List<BaTblBVo>) commonSvc.queryList(baTblBVo);

		model.put("baTblBVoList", baTblBVoList);

		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baBrdBVo);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID

		// 게시판관리(BA_BRD_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baBrdBVo);
		PersonalUtil.setPaging(request, baBrdBVo, recodeCount);

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);

		model.put("recodeCount", recodeCount);
		model.put("baBrdBVoList", baBrdBVoList);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mobileEnable"))){
			model.put("mobileEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/bb/adm/listBb");
	}
	
	/** [팝업] 포틀릿조회 - 수정,등록용 */
	@RequestMapping(value = "/bb/adm/setBbPltPop")
	public String setBbPltPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {
		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		
		if (baBrdBVo.getRescId() != null) {
			// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
			bbRescSvc.queryRescBVo(baBrdBVo.getRescId(), model);
		}
		model.put("baBrdBVo", baBrdBVo);
		// 코드조회 : PLT_CAT_CD (포틀릿카테고리코드)
		List<PtCdBVo> pltCatCdList = ptCmSvc.getCdList("PLT_CAT_CD", langTypCd, "Y");
		model.put("pltCatCdList", pltCatCdList);
		
		// 포틀릿상세(PT_PLT_D) 테이블 - 단건조회
		PtPltDVo storedPtPltDVo = new PtPltDVo();
		// 포틀릿 설정 조회
		Map<String, String> pltPolc = ptSysSvc.getPltPolc();
		storedPtPltDVo.setWdthPx(pltPolc.get("defaultWidth"));
		storedPtPltDVo.setHghtPx(pltPolc.get("defaultHeight"));
		model.put("ptPltDVo", storedPtPltDVo);
		
		
		return LayoutUtil.getJspPath("/bb/adm/setBbPltPop");
	}
	
	/** [팝업] 포틀릿저장 */
	@RequestMapping(value = "/bb/adm/transBbPlt")
	public String transBbPlt(HttpServletRequest request,
			@Parameter(name="pltId", required=false) String pltId,
			@Parameter(name="menuId", required=false) String menuId,
			ModelMap model) throws Exception {
		
		try {
			// 포틀릿상세(PT_PLT_D) 테이블 - 변수 바인딩
			PtPltDVo ptPltDVo = new PtPltDVo();
			VoUtil.bind(request, ptPltDVo);
			
			// 세션 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(PT_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(request, null, queryQueue);
			if(ptRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 리소스ID, 포틀릿명
			ptPltDVo.setRescId(ptRescBVo.getRescId());
			ptPltDVo.setPltNm(ptRescBVo.getRescVa());
			
			// 수정일, 수정자
			ptPltDVo.setModDt("sysdate");
			ptPltDVo.setModrUid(userVo.getUserUid());
			
			// 신규 포틀릿 등록
			boolean isNew = false;
			if(pltId==null || pltId.isEmpty()){
				isNew = true;
				
				// 포틀릿 ID 생성
				ptPltDVo.setPltId(ptCmSvc.createId("PT_PLT_D"));
				// 등록일, 등록자
				ptPltDVo.setModDt("sysdate");
				ptPltDVo.setModrUid(userVo.getUserUid());
				if(ptPltDVo.getUseYn()==null || ptPltDVo.getUseYn().isEmpty()){
					ptPltDVo.setUseYn("Y");
				}
				
				if(ptPltDVo.getOpenCompId()==null) {
					ptPltDVo.setOpenCompId(userVo.getCompId());
				}
				
				queryQueue.insert(ptPltDVo);
			// 수정
			} else {
				queryQueue.update(ptPltDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.PLT, CacheConfig.PLT_LAYOUT, CacheConfig.LAYOUT);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(isNew){
				model.put("todo", "parent.location.replace('./listBb.do?menuId="+menuId+"');");
			} else {
				model.put("todo", "parent.location.replace(parent.location.href);");
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
	
	
	/** [팝업] 게시판 선택 화면 */
	@RequestMapping(value = {"/bb/selectBbPop", "/bb/adm/selectBbPop", "/dm/doc/selectBbPop", "/dm/amd/doc/selectBbPop"})
	public String selectBbPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		// 확장여부(exYn)가 같고
		// 확장게시판이면 같은 테이블(tblId)이고
		// 답변형여부(replyYn)가 같고
		// 게시판종류(kndCd)가 같고
		// 게시판타입(brdTypCd)이 같고
		// 심의여부(discYn)가 같은 게시판
		// 부서게시판이면 같은 부서게시판이여야 하고 (deptBrdYn)
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 게시판관리 VO
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

		StringBuilder sb = new StringBuilder();
		sb.append(ParamUtil.getQueryString(request, "data"))
				.append("&exYn=").append(baBrdBVo.getExYn())
				.append("&replyYn=").append(baBrdBVo.getReplyYn())
				.append("&kndCd=").append(baBrdBVo.getKndCd())
				.append("&brdTypCd=").append(baBrdBVo.getBrdTypCd())
				.append("&discYn=").append(baBrdBVo.getDiscYn());
		if ("Y".equals(baBrdBVo.getExYn()))
			sb.append("&tblId=").append(baBrdBVo.getTblId());
		if ("Y".equals(baBrdBVo.getDeptBrdYn()))
			sb.append("&deptBrdYn=").append(baBrdBVo.getDeptBrdYn());
		
		model.put("params", sb.toString());

		return LayoutUtil.getJspPath("/bb/selectBbPop");
	}

	/** [프레임] 게시판 선택 화면 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/bb/selectBbFrm", "/bb/adm/selectBbFrm", "/dm/doc/selectBbFrm", "/dm/adm/doc/selectBbFrm"})
	public String selectBbFrm(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "compId", required = false) String compId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리 VO
		model.put("baBrdBVo", bbBrdSvc.getBaBrdBVo(langTypCd, brdId));
		
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo paramBrdVo = new BaBrdBVo();
		paramBrdVo.setQueryLang(langTypCd);
		VoUtil.bind(request, paramBrdVo);
		paramBrdVo.setBrdId(null);
		
		// 법인별 게시판 관리여부[ 회사ID + 메뉴 관리권한]
		boolean isCoprUser=compId!=null && !compId.isEmpty() && SecuUtil.hasAuth(request, "A", null);
		
		if(isCoprUser){
			paramBrdVo.setCompId(compId);
			paramBrdVo.setAllCompYn("N"); // 전사게시판 제외
			paramBrdVo.setDeptBrdYn("N");
		}else bbBrdSvc.setCompId(request, paramBrdVo);    // 회사ID
		
		// 게시판관리(BA_BRD_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(paramBrdVo);
		PersonalUtil.setPaging(request, paramBrdVo, recodeCount);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(paramBrdVo);
		
		model.put("recodeCount", recodeCount);
		model.put("baBrdBVoList", baBrdBVoList);
		
		return LayoutUtil.getJspPath("/bb/selectBbFrm");
	}

	/** 게시판관리 조회 (관리자) */
	@RequestMapping(value = "/bb/adm/viewBb")
	public String viewBb(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

		if (baBrdBVo.getRescId() != null) {
			// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
			bbRescSvc.queryRescBVo(baBrdBVo.getRescId(), model);
		}

		//  담당자 정보 세팅
		bbBrdSvc.setPichVo(baBrdBVo, langTypCd);

		model.put("baBrdBVo", baBrdBVo);
		
		// 양식 사용여부[본문삽입용]
		String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
		
		if(wfFormNo!=null){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 양식기본정보 세팅
			WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(userVo, wfFormNo, null);
			model.put("wfFormBVo", wfFormBVo);
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPloc", sysPlocMap);
				
		return LayoutUtil.getJspPath("/bb/adm/viewBb");
	}

	/** 게시판관리 수정용 조회 및 등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setBb")
	public String setBb(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시판타입코드
		List<PtCdBVo> brdTypCdList = ptCmSvc.getCdList("BRD_TYP_CD", langTypCd, "Y");
		model.put("brdTypCdList", brdTypCdList);

		// 게시판종류코드
		List<PtCdBVo> kndCdList = ptCmSvc.getCdList("KND_CD", langTypCd, "Y");
		model.put("kndCdList", kndCdList);

		// 테이블관리(BA_TBL_B) 테이블 - SELECT
		BaTblBVo baTblBVo = new BaTblBVo();
				
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baTblBVo.setCompId(userVo.getCompId());
		//}
		
		baTblBVo.setQueryLang(langTypCd);
		baTblBVo.setOrderBy("T.TBL_ID ASC");
		List<BaTblBVo> baTblBVoList = (List<BaTblBVo>) commonSvc.queryList(baTblBVo);
		model.put("baTblBVoList",  baTblBVoList);

		// 수정인 경우
		if (brdId != null && !brdId.isEmpty()) {
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			if (baBrdBVo.getRescId() != null) {
				// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
				bbRescSvc.queryRescBVo(baBrdBVo.getRescId(), model);
			}
			//  담당자 정보 세팅
			bbBrdSvc.setPichVo(baBrdBVo, langTypCd);

			model.put("baBrdBVo", baBrdBVo);
			
			// 양식 사용여부[본문삽입용]
			String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
			
			if(wfFormNo!=null){
				// 양식기본정보 세팅
				WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(userVo, wfFormNo, null);
				model.put("wfFormBVo", wfFormBVo);
			}
		} else {
			// 기본값 세팅
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setNotcDispPrd(3);
			
			model.put("baBrdBVo", baBrdBVo);
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPloc", sysPlocMap);
				
		return LayoutUtil.getJspPath("/bb/adm/setBb");
	}

	/** 게시판관리 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transBb")
	public String transBb(HttpServletRequest request,
			@RequestParam(value = "params", required = true) String params,
			@RequestParam(value = "brdId", required = false) String brdId,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			BaRescBVo baRescBVo = bbRescSvc.collectBaRescBVo(request, "brd", queryQueue);
			if (baRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 게시판관리(BA_BRD_B) 테이블 - BIND
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			VoUtil.bind(request, baBrdBVo);

			// 리소스 조회 후 리소스ID와 리소스명 세팅
			baBrdBVo.setRescId(baRescBVo.getRescId());
			baBrdBVo.setBrdNm(baRescBVo.getRescVa());
			
			// 회사ID
			baBrdBVo.setCompId(userVo.getCompId());

			// 수정자, 수정일시
			baBrdBVo.setModrUid(userVo.getUserUid());
			baBrdBVo.setModDt("sysdate");

			// 기본값
			if (baBrdBVo.getDiscYn() == null) baBrdBVo.setDiscYn("N");

			if (brdId == null || brdId.isEmpty()) {
				// 게시판ID 생성
				baBrdBVo.setBrdId(bbCmSvc.createId("BA_BRD_B"));
				// 최근게시물여부
				baBrdBVo.setLastBullYn("N");
				// 전사게시판 여부
				if(baBrdBVo.getAllCompYn()==null || baBrdBVo.getAllCompYn().isEmpty()) baBrdBVo.setAllCompYn("N");
				// 게시판관리(BA_BRD_B) 테이블 - INSERT
				queryQueue.insert(baBrdBVo);

				// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
				BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
				baTblColmDVo.setTblId(baBrdBVo.getTblId());
				List<BaTblColmDVo> colmVoList = (List<BaTblColmDVo>) commonSvc.queryList(baTblColmDVo);

				int dispOrdr = 1;
				for (BaTblColmDVo colmVo : colmVoList) {
					// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - INSERT
					BaColmDispDVo dispVo = new BaColmDispDVo();
					dispVo.setBrdId(baBrdBVo.getBrdId());
					dispVo.setUseYn("Y");
					
					// 속성 복사
					String[] ignores = new String[] {"regrUid", "regDt", "modrUid", "modDt"};
					BeanUtils.copyProperties(colmVo, dispVo, ignores);
					 
					if ("Y".equals(dispVo.getListDispYn()))
						dispVo.setListDispOrdr(dispOrdr++);
					else
						dispVo.setListDispOrdr(99); 
					
					// 수정자, 수정일시
					dispVo.setModrUid(userVo.getUserUid());
					dispVo.setModDt("sysdate");
					
					// 컬럼표시 목록표시여부 세팅
					bbBrdSvc.setListDispYn(baBrdBVo, colmVo, dispVo);
					
					queryQueue.insert(dispVo);
				}

			} else {
				// 게시판관리(BA_BRD_B) 테이블 - UPDATE
				queryQueue.update(baBrdBVo);

				// 컬럼표시 목록표시여부 세팅
				List<BaColmDispDVo> colmDispVoList = bbBrdSvc.getBaColmDispDVoList(request, baBrdBVo.getBrdId(), false, null, null, null, false);
				BaColmDispDVo updateVo=null;
				for (BaColmDispDVo dispVo : colmDispVoList) {
					updateVo=new BaColmDispDVo();
					if (bbBrdSvc.setListDispYn(baBrdBVo, dispVo.getColmVo(), updateVo)) {						
						updateVo.setModDt("sysdate");
						updateVo.setBrdId(brdId);
						updateVo.setColmId(dispVo.getColmId());
						queryQueue.update(updateVo);
					}
				}
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 업무관리 사용여부가 'Y' 일 경우 양식 데이터 조회
			if(sysPlocMap.containsKey("wfEnable") && "Y".equals(sysPlocMap.get("wfEnable"))){
				// 게시판 양식 조회
				String mdCd="BB";
				String mdRefId=brdId;
				String wfFormNo = null;
				String wfGenId = null;
				WfMdFormDVo wfMdFormDVo = wfMdFormSvc.getWfMdFormDVo(mdCd, mdRefId, null);
				
				// 옵션값[양식 데이터가 있을경우]
				if(baBrdBVo.getOptVa()!=null && !baBrdBVo.getOptVa().isEmpty()){
					JSONObject jsonOptVa = (JSONObject) JSONValue.parse(baBrdBVo.getOptVa());
					if(jsonOptVa!=null){
						wfFormNo = (String) jsonOptVa.get("wfFormNo");
						wfGenId = (String) jsonOptVa.get("wfGenId");
						if(wfFormNo!=null && wfGenId!=null){
							wfMdFormSvc.saveByMdFormData(request, queryQueue, mdCd, wfGenId, wfFormNo, mdRefId);
						}
					}
				}
				
				if(brdId != null && !brdId.isEmpty() && (wfFormNo==null || wfGenId==null) && wfMdFormDVo!=null){
					wfMdFormSvc.deleteByMdFormData(queryQueue, mdCd, mdRefId);
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.BRD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.BRD);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));

			if (brdId == null || brdId.isEmpty()) {
				model.put("todo", "parent.location.replace('./listBb.do?" + params + "');");
			} else {
				model.put("todo", "parent.location.replace('./viewBb.do?" + params + "&brdId=" + brdId + "');");
			}

		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}

	/** [AJAX] 게시판관리 삭제 (관리자) */
	@RequestMapping(value = "/bb/adm/transBbDelAjx")
	public String transBbDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			if (brdId == null || brdId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			if (baBrdBVo.getTblId() != null) {
				// 테이블 사용 게시물수, 게시물(BB_X000X_L) - COUNT
				BbBullLVo bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
				bbBullLVo.setBrdId(brdId);
				
				// 게시물이 존재하면 삭제 불가
				if (commonSvc.count(bbBullLVo) > 0) {
					// bb.msg.bullExists=게시물이 존재합니다.
					throw new CmException("bb.msg.bullExists", request);
				}
			}

			// 메뉴상세(PT_MNU_D) 테이블 - COUNT
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setWhereSqllet("AND T.MD_ID = '" + brdId + "'");
			// 메뉴가 존재하면 삭제 불가
			if (commonSvc.count(ptMnuDVo) > 0) {
				// bb.msg.mnuExists=메뉴가 존재합니다.
				throw new CmException("bb.msg.mnuExists", request);
			}

			// 게시판관리(BA_BRD_B) 테이블 - DELETE
			QueryQueue queryQueue = new QueryQueue();
			queryQueue.delete(baBrdBVo);

			// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - DELETE
			BaColmDispDVo dispVo = new BaColmDispDVo();
			dispVo.setBrdId(baBrdBVo.getBrdId());
			queryQueue.delete(dispVo);

			// 나의게시물설정(BA_MY_BULL_M) 테이블 - DELETE
			BaMyBullMVo baMyBullMVo = new BaMyBullMVo();
			baMyBullMVo.setBrdId(baBrdBVo.getBrdId());
			queryQueue.delete(baMyBullMVo);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.BRD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.BRD);

			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** [팝업] 컬럼관리 (관리자) */
	@RequestMapping(value = "/bb/adm/setColmMngPop")
	public String setColmMngPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, false, null, null, null, false);
		
		model.put("baColmDispDVoList", bbBrdSvc.getColDispList(langTypCd, brdId, baColmDispDVoList));

		return LayoutUtil.getJspPath("/bb/adm/setColmMngPop");
	}

	/** 컬럼관리/목록순서 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transDisp")
	public String transDisp(HttpServletRequest request,
			@RequestParam(value = "dialog", required = true) String dialog,
			ModelMap model) {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			QueryQueue queryQueue = new QueryQueue();
			
			// 목록조건적용여부
            String listCondApplyYn = ParamUtil.getRequestParam(request, "listCondApplyYn", false);
            
            // 모바일대표컬럼ID
            String titleColmId = ParamUtil.getRequestParam(request, "titleColmId", false);
            
            boolean isListCondApply=false;
            if(listCondApplyYn!=null && !listCondApplyYn.isEmpty() && "Y".equals(listCondApplyYn) && 
            		titleColmId!=null && !titleColmId.isEmpty()){ // 모바일대표컬럼여부 'N'으로 일괄수정
            	// 게시판ID
                String brdId = ParamUtil.getRequestParam(request, "brdId", true);
            	BaColmDispDVo baColmDispDVo = new BaColmDispDVo();
            	baColmDispDVo.setBrdId(brdId);
            	baColmDispDVo.setTitlColYn("N");
            	queryQueue.update(baColmDispDVo);
            	isListCondApply=true;
            }
            
			// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - BIND
			List<BaColmDispDVo> boundDispVoList = (List<BaColmDispDVo>) VoUtil.bindList(request, BaColmDispDVo.class, new String[]{"colmId"});
    		
			for (BaColmDispDVo dispVo : boundDispVoList) {
				if(isListCondApply && dispVo.getColmId().equals(titleColmId))
					dispVo.setTitlColYn("Y");
				// 수정자, 수정일시
				dispVo.setModrUid(userVo.getUserUid());
				dispVo.setModDt("sysdate");
				queryQueue.update(dispVo);
			}

			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('" + dialog + "');");

//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}

	/** [팝업] 목록순서 (관리자) */
	@RequestMapping(value = "/bb/adm/setListOrdrPop")
	public String setListOrdrPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.containsKey("mobileEnable") && !"N".equals(sysPlocMap.get("mobileEnable"))){
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			// 목록 조회조건 적용여부
			if(baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().get("listCondApplyYn")!=null && "Y".equals(baBrdBVo.getOptMap().get("listCondApplyYn")))
				model.put("isListCondApply", Boolean.TRUE);
		}
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, "Y", "Y", null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		return LayoutUtil.getJspPath("/bb/adm/setListOrdrPop");
	}
	
	/** [팝업] 항목구성 (관리자) */
	@RequestMapping(value = "/bb/adm/setDtlOrdrPop")
	public String setDtlOrdrPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}

		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getColDispList(request, null, brdId, true, null, "Y", false, true);
		model.put("baColmDispDVoList", baColmDispDVoList);
		model.put("isItemDtl",Boolean.TRUE);
		return LayoutUtil.getJspPath("/bb/adm/setListOrdrPop");
	}

	/** [팝업] 메뉴등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setBbMnuPop")
	public String setBbMnuPop(HttpServletRequest request,
			@RequestParam(value = "mnuGrpId", required = false) String mnuGrpId,
			@RequestParam(value = "valUM", required = false) String valUM,
			ModelMap model) throws Exception {

		if(valUM==null || valUM.equals("")){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		model.put("valUM", valUM);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		baBrdBVo.setCompId(userVo.getCompId());
		List<BaBrdBVo> baBrdBVoLlist = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 기능목록 초기화
		List<PtCdBVo> brdFncList = ptCmSvc.getCdList("BRD_FNC_MENU", langTypCd, "Y");
		if("M".equals(valUM)){
			List<PtCdBVo> tmpList = new ArrayList<PtCdBVo>();
			for(PtCdBVo storedPtCdBVo : brdFncList){
				if (storedPtCdBVo.getRefVa2()==null || storedPtCdBVo.getRefVa2().isEmpty()) tmpList.add(storedPtCdBVo); 
			}			
			for(PtCdBVo storedPtCdBVo2 : tmpList){
				brdFncList.remove(storedPtCdBVo2);
			}
		}
		
		model.put("brdFncList", brdFncList);
		
		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - SELECT
		PtMnuGrpBVo ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setSysgYn("N");
		ptMnuGrpBVo.setMnuGrpMdCd(valUM);
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setOpenCompId(userVo.getCompId());
		List<PtMnuGrpBVo> ptMnuGrpBVoList = (List<PtMnuGrpBVo>) commonSvc.queryList(ptMnuGrpBVo);
		model.put("ptMnuGrpBVoList", ptMnuGrpBVoList);
		
		if (mnuGrpId == null || mnuGrpId.isEmpty()) {
			if(ptMnuGrpBVoList!=null && ptMnuGrpBVoList.size()>0)
				mnuGrpId = ptMnuGrpBVoList.get(0).getMnuGrpId();
		}

		// 메뉴그룹기본(PT_MNU_GRP_B) 테이블 - SELECT
		ptMnuGrpBVo = new PtMnuGrpBVo();
		ptMnuGrpBVo.setSysgYn("N");
		ptMnuGrpBVo.setMnuGrpMdCd(valUM);
		ptMnuGrpBVo.setUseYn("Y");
		ptMnuGrpBVo.setQueryLang(langTypCd);
		ptMnuGrpBVo.setMnuGrpId(mnuGrpId);
		ptMnuGrpBVo = (PtMnuGrpBVo) commonSvc.queryVo(ptMnuGrpBVo);
		model.put("ptMnuGrpBVo", ptMnuGrpBVo);

		if (ptMnuGrpBVo == null) {
			// bb.msg.mnuGrp.notFound=게시판 메뉴그룹을 찾을 수 없습니다.
			throw new CmException("bb.msg.mnuGrp.notFound", request);
		}

		// 메뉴상세(PT_MNU_D) 테이블
		PtMnuDVo ptMnuDVo = new PtMnuDVo();
		ptMnuDVo.setMnuGrpId(ptMnuGrpBVo.getMnuGrpId());
		ptMnuDVo.setQueryLang(langTypCd);
		ptMnuDVo.setOrderBy("T.SORT_ORDR");
		List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
		model.put("ptMnuDVoList", ptMnuDVoList);

		// 메뉴에 이미 등록된 게시판관리VO 제거
		for (PtMnuDVo mnuVo : ptMnuDVoList) {
			String mdId = mnuVo.getMdId();
			if (mdId == null || mdId.isEmpty()) 
				mdId = mnuVo.getMnuUrl();
			if(mdId == null) continue;
			BaBrdBVo vo = null;
			for (BaBrdBVo brdVo : baBrdBVoLlist) {
				if (mdId.indexOf(brdVo.getBrdId()) > -1) vo = brdVo;
			}
			if (vo != null) baBrdBVoLlist.remove(vo);
			
			// 기능메뉴도 확인한다.
			PtCdBVo vo2 = null;
			for (PtCdBVo cdVo : brdFncList) {
				if (mdId.equals(cdVo.getCd())) vo2 = cdVo;
			}
			if (vo2 != null) brdFncList.remove(vo2);
		}
		
		List<BaBrdBVo> allCompBrdList = new ArrayList<BaBrdBVo>();
		if(baBrdBVoLlist.size() > 0){
			for(BaBrdBVo storedBaBrdBVo : baBrdBVoLlist){
				//전사 게시판
				if("Y".equals(storedBaBrdBVo.getAllCompYn())) allCompBrdList.add(storedBaBrdBVo);
			}
			if(allCompBrdList.size() > 0){
				for(BaBrdBVo storedBaBrdBVo : allCompBrdList){
					baBrdBVoLlist.remove(storedBaBrdBVo);
				}
			}
		}
		model.put("allCompBrdList", allCompBrdList);
		model.put("baBrdBVoLlist", baBrdBVoLlist);
		
		return LayoutUtil.getJspPath("/bb/adm/setBbMnuPop");
	}

	/** [AJAX] 메뉴저장 (관리자) */
	@RequestMapping(value = "/bb/adm/transBbMnuAjx")
	public String transBbMnuAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mnuId = (String) object.get("mnuId");
			String mnuGrpId = (String) object.get("mnuGrpId");
			JSONArray jsonArray = (JSONArray) object.get("brdIds");
			if (jsonArray == null || mnuId == null || ("ROOT".equals(mnuId) && mnuGrpId == null)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			String valUM = request.getParameter("valUM");
			if(valUM==null || valUM.equals("")){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			QueryQueue queryQueue = new QueryQueue();
			for (int i = 0; i < jsonArray.size(); i++) {
				String brdId = (String) jsonArray.get(i);
				PtRescBVo firstPtRescBVo = null;
				
				String orgRescId = null;
				String fncModUrl = null;
				
				if(!brdId	.startsWith("F"))
				{
					// 게시판 메뉴의 경우 리소스 아이디를 가져온다
					BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
					if (baBrdBVo == null) {
						// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
						throw new CmException("pt.msg.nodata.passed", request);
					}
					orgRescId = baBrdBVo.getRescId();
				}
				else
				{
					// 기능 메뉴의 경우 코드관리에서 리소스 아이디와 모듈 url를 가져온다.
					PtCdBVo ptCdBVo = ptCmSvc.getCd("BRD_FNC_MENU", langTypCd, brdId);
					orgRescId = ptCdBVo.getRescId();
					fncModUrl = ptCdBVo.getRefVa1();
				}
				
				List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
				String rescId = ptCmSvc.createId("PT_RESC_B");
				for (PtCdBVo ptCdBVo : ptCdBVoList) {
					BaRescBVo baRescBVo = new BaRescBVo();
					
					String ptLangTypCd = null;
					String ptRescVa = null;
					if(!brdId	.startsWith("F"))
					{
						// 게시판 메뉴의 경우 게시판 리소스기본에서 리소스 정보를 가져온다.
						baRescBVo.setRescId(orgRescId);
						baRescBVo.setLangTypCd(ptCdBVo.getCd());
						baRescBVo = (BaRescBVo) commonSvc.queryVo(baRescBVo);
						ptLangTypCd = baRescBVo.getLangTypCd();
						ptRescVa = baRescBVo.getRescVa();
					}
					else
					{
						// 기능 메뉴의 경우 리소스기본에서 리소스 정보를 가져온다.
						PtRescBVo ptRescBVo = new PtRescBVo();
						ptRescBVo.setRescId(orgRescId);
						ptRescBVo.setLangTypCd(ptCdBVo.getCd());
						ptRescBVo =	(PtRescBVo) commonSvc.queryVo(ptRescBVo);
						ptLangTypCd = ptRescBVo.getLangTypCd();
						ptRescVa = ptRescBVo.getRescVa();
					}
					
					PtRescBVo ptRescBVo = new PtRescBVo();
					ptRescBVo.setRescId(rescId);
					ptRescBVo.setLangTypCd(ptLangTypCd);
					ptRescBVo.setRescVa(ptRescVa);

					if (firstPtRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
						firstPtRescBVo = ptRescBVo;
					}

					// 리소스기본(PT_RESC_B) 테이블 - INSERT
					queryQueue.insert(ptRescBVo);
				}

				if (firstPtRescBVo == null) {
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				// 메뉴상세(PT_MNU_D) 테이블 - INSERT
				PtMnuDVo ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuId(ptCmSvc.createId("PT_MNU_D")); // "PT_MNU_D", 'M', 8
				ptMnuDVo.setMnuGrpId(mnuGrpId);
				ptMnuDVo.setMnuGrpMdCd(valUM);
				ptMnuDVo.setMnuNm(firstPtRescBVo.getRescVa());
				ptMnuDVo.setRescId(firstPtRescBVo.getRescId());
				ptMnuDVo.setMnuPid("ROOT".equals(mnuId) ? mnuGrpId : mnuId);
				ptMnuDVo.setMnuTypCd("IN_URL");
				ptMnuDVo.setPopSetupCont("");
				ptMnuDVo.setMnuDesc("");
				ptMnuDVo.setFldYn("N");
				ptMnuDVo.setSysMnuYn("N");
				ptMnuDVo.setMdRid("BB");
				ptMnuDVo.setMdId(brdId);
				ptMnuDVo.setUseYn("Y");
				ptMnuDVo.setModDt("sysdate");
				ptMnuDVo.setModrUid(userVo.getUserUid());

				if(!brdId	.startsWith("F"))
					ptMnuDVo.setMnuUrl("/bb/listBull.do?brdId=" + brdId);
				else
					ptMnuDVo.setMnuUrl(fncModUrl);
				
				queryQueue.insert(ptMnuDVo);
			}

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime,
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);

			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 메뉴삭제 (관리자) */
	@RequestMapping(value = "/bb/adm/transBbMnuDelAjx")
	public String transBbMnuDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mnuId = (String) object.get("mnuId");
			if (mnuId == null || mnuId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 메뉴상세(PT_MNU_D) 테이블 - SELECT
			PtMnuDVo ptMnuDVo = new PtMnuDVo();
			ptMnuDVo.setMnuId(mnuId);
			ptMnuDVo = (PtMnuDVo) commonSvc.queryVo(ptMnuDVo);

			if (ptMnuDVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			QueryQueue queryQueue = new QueryQueue();
			// 리소스기본(PT_RESC_B) 테이블 - DELETE
			PtRescBVo ptRescBVo = new PtRescBVo();
			ptRescBVo.setRescId(ptMnuDVo.getRescId());
			queryQueue.delete(ptRescBVo);

			// 메뉴상세(PT_MNU_D) 테이블 - DELETE
			queryQueue.delete(ptMnuDVo);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime,
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);

			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** 최신게시물 설정 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setNewBull")
	public String setNewBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
		
		List<BaBrdBVo> storedBaBrdBVoList = new ArrayList<BaBrdBVo>();
		if(mdIds != null){
			for(String mdId : mdIds){
				baBrdBVo = null;
				
				// mdId 해당하는 BaBrdBVo 찾기 - 권한있고 메뉴에 붙어 있는
				if(baBrdBVoList != null){
					for (BaBrdBVo brdVo : baBrdBVoList) {
						if(mdId.equals(brdVo.getBrdId())){
							baBrdBVo = brdVo;
							break;
						}
					}
				}
				
				// baBrdBVo 가 있으면 - 권한있고 메뉴에 붙어 있으면
				if(baBrdBVo != null){
					// 제목이 목록 조회조건에 없으면 continue
					if(!bbBrdSvc.isColmDisp(langTypCd, baBrdBVo.getBrdId(), "Y", "subj"))
						continue;
					storedBaBrdBVoList.add(baBrdBVo);
				}
			}
		}
		model.put("baBrdBVoList", storedBaBrdBVoList);
		
		/*List<BaBrdBVo> storedBaBrdBVoList = new ArrayList<BaBrdBVo>();
		for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
			if(bbBrdSvc.chkMdIds(mdIds, storedBaBrdBVo.getBrdId())){
				storedBaBrdBVoList.add(storedBaBrdBVo);
			}
		}
		model.put("baBrdBVoList", storedBaBrdBVoList);*/
		
		// 기한(BA_SETUP_B) 테이블 - SELECT
		BaSetupBVo baSetupBVo = new BaSetupBVo();
		baSetupBVo = (BaSetupBVo) commonSvc.queryVo(baSetupBVo);
		model.put("baSetupBVo", baSetupBVo);
		
		return LayoutUtil.getJspPath("/bb/adm/setNewBull");
	}

	/** [AJAX] 최신게시물 설정 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transNewBullAjx")
	public String transNewBullAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("brdIds");
			String ddln = (String) object.get("ddln");
			if (jsonArray == null || ddln == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 게시판관리(BA_BRD_B) 테이블 - BIND
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setQueryLang(langTypCd);
			bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
			baBrdBVo.setOrderBy("T.BRD_ID ASC");

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
			
			// 게시판관리(BA_BRD_B) 테이블 - UPDATE
			QueryQueue queryQueue = new QueryQueue();
			for (BaBrdBVo brdVo : baBrdBVoList) {
				// 최신게시글 사용안함으로 리셋.
				BaBrdBVo vo1 = new BaBrdBVo();
				vo1.setLastBullYn("N");
				vo1.setBrdId(brdVo.getBrdId());
				queryQueue.update(vo1);
				
				for (int i = 0; i < jsonArray.size(); i++) {
					String brdId = (String) jsonArray.get(i);
					if(brdVo.getBrdId().equals(brdId))
					{
						// 최신게시글 사용함
						BaBrdBVo vo2 = new BaBrdBVo();
						vo2.setLastBullYn("Y");
						vo2.setBrdId(brdId);
						queryQueue.update(vo2);
					}
				}	
			}
			
			// 기한(BA_SETUP_B) 테이블 - UPDATE
			BaSetupBVo baSetupBVo = new BaSetupBVo();
			baSetupBVo.setDdln(Integer.parseInt(ddln));
			queryQueue.store(baSetupBVo);
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.BRD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.BRD);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** 나의게시물설정 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/setMyBullPop")
	public String setMyBullPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 나의게시물설정(BA_MY_BULL_M) 테이블 - SELECT
		BaMyBullMVo baMyBullMVo = new BaMyBullMVo();
		baMyBullMVo.setUserUid(userVo.getUserUid());
		baMyBullMVo.setQueryLang(langTypCd);
		List<BaMyBullMVo> baMyBullMVoList = (List<BaMyBullMVo>) commonSvc.queryList(baMyBullMVo);
		model.put("baMyBullMVoList", baMyBullMVoList);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
		
		boolean added = false;
		List<BaBrdBVo> otherBrdList = new ArrayList<BaBrdBVo>();
		if(mdIds != null){
			for(String mdId : mdIds){
				baBrdBVo = null;
				
				// mdId 해당하는 BaBrdBVo 찾기 - 권한있고 메뉴에 붙어 있는
				if(baBrdBVoList != null){
					for (BaBrdBVo brdVo : baBrdBVoList) {
						if(mdId.equals(brdVo.getBrdId())){
							baBrdBVo = brdVo;
							break;
						}
					}
				}
				
				// baBrdBVo 가 있으면 - 권한있고 메뉴에 붙어 있으면
				if(baBrdBVo != null){
					// 제목이 목록 조회조건에 없으면 continue
					if(!bbBrdSvc.isColmDisp(langTypCd, baBrdBVo.getBrdId(), "Y", "subj"))
						continue;
					// 나의게시물에 추가 된것인지 확인
					added = false;
					for (BaMyBullMVo myVo : baMyBullMVoList) {
						if (mdId.equals(myVo.getBrdId())){
							added = true;
							break;
						}
					}
					
					// 나의게시물에 없으면
					if(!added){
						otherBrdList.add(baBrdBVo);
					}
				}
			}
		}
		model.put("baBrdBVoList", otherBrdList);
		
		/*
		// 전체 게시판 목록에서 선택된 게시판 목록을 제거
		List<BaBrdBVo> otherBrdList = new ArrayList<BaBrdBVo>();
		for (BaBrdBVo brdVo : baBrdBVoList) {
			if(!bbBrdSvc.chkMdIds(mdIds, brdVo.getBrdId())) continue;
			otherBrdList.add(brdVo);
			for (BaMyBullMVo myVo : baMyBullMVoList) {
				if (brdVo.getBrdId().equals(myVo.getBrdId())) otherBrdList.remove(brdVo);
			}
		}
		model.put("baBrdBVoList", otherBrdList);
		*/
		
		// 기한(BA_SETUP_B) 테이블 - SELECT
		BaUserSetupBVo paramUserSetupVo = new BaUserSetupBVo();
		paramUserSetupVo.setUserUid(userVo.getUserUid());
		paramUserSetupVo.setQueryLang(langTypCd);
		BaUserSetupBVo baUserSetupBVo = (BaUserSetupBVo) commonSvc.queryVo(paramUserSetupVo);
		
		if (baUserSetupBVo != null) {
			model.put("baUserSetupBVo", baUserSetupBVo);
		} else {
			paramUserSetupVo.setDdln(4);
			model.put("baUserSetupBVo", paramUserSetupVo);
		}
		
		return LayoutUtil.getJspPath("/bb/setMyBullPop");
	}

	/** [AJAX] 나의게시물설정 저장 (사용자) */
	@RequestMapping(value = "/bb/transMyBullAjx")
	public String transMyBullAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray brdIds = (JSONArray) object.get("brdIds");
			JSONArray rescIds = (JSONArray) object.get("rescIds");
			String ddln = (String) object.get("ddln");
			
			if (brdIds == null || rescIds == null || ddln == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 나의게시물설정(BA_MY_BULL_M) 테이블 - DELETE
			QueryQueue queryQueue = new QueryQueue();
			BaMyBullMVo paramMyBullVo = new BaMyBullMVo();
			paramMyBullVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(paramMyBullVo);
			
			for (int i = 0; i < brdIds.size(); i++) {
				String brdId = (String) brdIds.get(i);
				String rescId = (String) rescIds.get(i);
				// 나의게시물설정(BA_MY_BULL_M) 테이블 - INSERT
				BaMyBullMVo baMyBullMVo = new BaMyBullMVo();
				baMyBullMVo.setUserUid(userVo.getUserUid());
				baMyBullMVo.setBrdId(brdId);
				baMyBullMVo.setRescId(rescId);
				queryQueue.insert(baMyBullMVo);
			}
			
			// 기한(BA_SETUP_B) 테이블 - UPDATE
			BaUserSetupBVo baUserSetupBVo = new BaUserSetupBVo();
			baUserSetupBVo.setUserUid(userVo.getUserUid());
			baUserSetupBVo.setDdln(Integer.parseInt(ddln));
			queryQueue.store(baUserSetupBVo);
			
			commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** 포틀릿게시판 설정 (관리자) : 사용안함 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/setPortletBoard")
	public String setPortletBoard(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		model.put("baBrdBVoList", baBrdBVoList);

		return LayoutUtil.getJspPath("/bb/adm/setPortletBoard");
	}
	
	/** [AJAX] 포틀릿게시판 설정 저장 (관리자)  : 사용안함 */
	@RequestMapping(value = "/bb/adm/transPortletBoardAjx")
	public String transPortletBoardAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("brdIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			/*// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - UPDATE
			QueryQueue queryQueue = new QueryQueue();
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setPltYn("N");
			baBrdBVo.setQueryLang(langTypCd);
			bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
			queryQueue.update(baBrdBVo);
			
			// 게시판관리(BA_BRD_B) 테이블 - UPDATE
			for (int i = 0; i < jsonArray.size(); i++) {
				String brdId = (String) jsonArray.get(i);
				BaBrdBVo vo = new BaBrdBVo();
				vo.setPltYn("Y");
				vo.setBrdId(brdId);
				queryQueue.update(vo);
			}

			commonSvc.execute(queryQueue);*/
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 폴더순서변경 */
	@RequestMapping(value = "/bb/adm/transFldMoveAjx")
	public String transFldMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		//
		// 폴더 및 메뉴의 순서 목적으로 만들었는데
		// 메뉴의 순서는 저장하면서 한번에 바뀌므로 실제로
		// mnuIds - 여기에 한개의 폴더 아이디만 들어옴
		//
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		JSONArray jsonArray = (JSONArray)jsonObject.get("mnuIds");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(direction==null) LOGGER.error("Menu move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
			
		PtMnuDVo ptMnuDVo = new PtMnuDVo(), storedPtMnuDVo;
		// 위로 이동
		if("up".equals(direction) || "tup".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			ptMnuDVo = new PtMnuDVo();
			
			String mnuId, mnuPid=null;
			for(int i=0;i<jsonArray.size();i++){
				mnuId = (String)jsonArray.get(i);
				
				ptMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
				if(storedPtMnuDVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-UP - mnuId:"+mnuId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedPtMnuDVo.getSortOrdr());
				mnuPid = storedPtMnuDVo.getMnuPid();
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_D");
				ptxSortOrdrChnVo.setPkCol("MNU_PID");
				ptxSortOrdrChnVo.setPk(storedPtMnuDVo.getMnuPid());
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				if("tup".equals(direction)){
					switchOrdr = 1;
					ptxSortOrdrChnVo.setLessThen(curOrdr-1);
				}else{
					switchOrdr = curOrdr-1;
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				queryQueue.update(ptxSortOrdrChnVo);
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuDVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mnuIds 에 세팅함
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuPid(mnuPid);
				ptMnuDVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
				int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
				List<String> mnuIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mnuIdList.add(ptMnuDVoList.get(i).getMnuId());
				}
				model.put("mnuIds", mnuIdList);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			return JsonUtil.returnJson(model);
			
			// 아래로 이동
		} else if("down".equals(direction) || "tdown".equals(direction)){
			
			String mnuPid = (String)jsonObject.get("mnuPid");
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("PT_MNU_D");
			ptxSortOrdrChnVo.setPkCol("MNU_PID");
			ptxSortOrdrChnVo.setPk(mnuPid);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			ptMnuDVo = new PtMnuDVo();
			
			String mnuId;
			for(int i=jsonArray.size()-1;i>=0;i--){
				mnuId = (String)jsonArray.get(i);
				
				ptMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo = (PtMnuDVo)commonSvc.queryVo(ptMnuDVo);
				if(storedPtMnuDVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-DOWN - mnuId:"+mnuId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedPtMnuDVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("PT_MNU_D");
				ptxSortOrdrChnVo.setPkCol("MNU_PID");
				ptxSortOrdrChnVo.setPk(storedPtMnuDVo.getMnuPid());
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				
				//맨아래 일경우 기준순번을 최대 정렬 순서로 변경한다.			
				if("tdown".equals(direction)){
					switchOrdr = maxSortOrdr.intValue();
					ptxSortOrdrChnVo.setMoreThen(curOrdr+1);
					ptxSortOrdrChnVo.setLessThen(maxSortOrdr.intValue());
				}else{
					switchOrdr = curOrdr+1;
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				queryQueue.update(ptxSortOrdrChnVo);
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedPtMnuDVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
						CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mnuIds 에 세팅함
				ptMnuDVo = new PtMnuDVo();
				ptMnuDVo.setMnuPid(mnuPid);
				ptMnuDVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<PtMnuDVo> ptMnuDVoList = (List<PtMnuDVo>)commonSvc.queryList(ptMnuDVo);
				int i, size = ptMnuDVoList==null ? 0 : ptMnuDVoList.size();
				List<String> mnuIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mnuIdList.add(ptMnuDVoList.get(i).getMnuId());
				}
				
				model.put("mnuIds", mnuIdList);
				
			} else {
				//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
			}
			return JsonUtil.returnJson(model);
			
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("Code move(up/down) - direction=="+direction+" : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}

	}
	
	
	
	/** [AJAX] - 폴더순서변경 - 일괄저장 */
	@RequestMapping(value = "/bb/adm/transFldMoveSaveAjx")
	public String transFldMoveSaveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		// 메뉴 JSON String
		String mnuList = (String)jsonObject.get("mnuList");
		if(mnuList == null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		// JSON String 을 Map 으로 변환
		Map<String, List<String>> mnuMap = new ObjectMapper().readValue(mnuList.toString(), new TypeReference<Map<String, List<String>>>(){}) ;
		
		if(mnuMap==null || mnuMap.size()==0){
			//cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		Entry<String, List<String>> entry;
		Iterator<Entry<String, List<String>>> iterator = mnuMap.entrySet().iterator();
		List<String> mnuIdList=null;
		PtMnuDVo storedPtMnuDVo;
		String mnuId;
		
		QueryQueue queryQueue = new QueryQueue();
		while(iterator.hasNext()){
			entry = iterator.next();
			mnuIdList=entry.getValue();
			for(int i=0;i<mnuIdList.size();i++){
				mnuId = (String)mnuIdList.get(i);
				storedPtMnuDVo = new PtMnuDVo();
				storedPtMnuDVo.setMnuPid(entry.getKey());
				storedPtMnuDVo.setMnuId(mnuId);
				storedPtMnuDVo.setSortOrdr(String.valueOf(i+1));
				queryQueue.update(storedPtMnuDVo);
			}
		}
		
		if(!queryQueue.isEmpty()){
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, 
					CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.MENU, CacheConfig.LAYOUT, CacheConfig.MENU_GRP, CacheConfig.LAYOUT_GRP);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
						
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			model.put("message",  messageProperties.getMessage("cm.msg.notValidCall", request));
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 게시판조회 */
	@RequestMapping(value = "/bb/adm/listBrdPop")
	public String listBrdPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/bb/adm/listBrdPop");
	}
	
	/** [프레임] 게시판조회 */
	@RequestMapping(value = "/bb/adm/listBrdFrm")
	public String listBrdFrm(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		// 시스템 관리자 여부
		boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우 오류 처리
		if(!isSysAdmin){
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		if(ptCompBVoList == null || ptCompBVoList.size() == 0) {
			LOGGER.error("[ptCompBVoList] Size = 0");
			// cm.msg.errors.500=처리중 오류가 발생 하였습니다.
			throw new CmException("cm.msg.errors.500", request);
		}
		
		// 테이블관리(BA_TBL_B) 테이블 - SELECT
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setQueryLang(langTypCd);
			
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baBrdBVo);
		
		if(compId == null || compId.isEmpty()) compId = ptCompBVoList.get(0).getCompId();
		baBrdBVo.setCompId(compId);

		// 게시판관리(BA_BRD_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baBrdBVo);
		PersonalUtil.setPaging(request, baBrdBVo, recodeCount);

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);

		model.put("recodeCount", recodeCount);
		model.put("baBrdBVoList", baBrdBVoList);
		
		model.put("compId", compId);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/bb/adm/listBrdFrm");
	}
	
	/** [AJAX] 게시판 목록 조회 */
	@RequestMapping(value = {"/bb/selectBrdListAjx","/bb/adm/selectBrdListAjx"})
	public String selectBrdListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String compId = (String) object.get("compId");
			if ( compId == null || compId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판 목록 조회
			List<BaBrdBVo> baBrdBVoList = bbBrdSvc.getCompBrdList(langTypCd, compId, "N", "N", "N", true);
			if(baBrdBVoList==null || baBrdBVoList.size()==0){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("baBrdBVoList", baBrdBVoList);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
}
