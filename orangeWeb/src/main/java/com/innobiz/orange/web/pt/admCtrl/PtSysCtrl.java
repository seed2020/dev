package com.innobiz.orange.web.pt.admCtrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
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
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtLginImgDVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupMetaDVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.pt.vo.PtTermSetupDVo;

/** 시스템, 정책 컨트롤러 */
@Controller
public class PtSysCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtSysCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 비밀번호 변경 정책 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setPwPolc")
	public String setPwPolc(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(compId==null || compId.isEmpty()){
			compId = userVo.getCompId();
		}
		model.put("compId", compId);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("lostPwEnable"))){
			model.put("lostPwEnable", Boolean.TRUE);
			
			Map<String, String> lostPwPolc = ptSysSvc.getLostPwPolc();
			model.put("lostPwPolc", lostPwPolc);
		}
		
		// 시스템프로퍼티상세 맵 조회
		Map<String, String> pwPolc = ptSysSvc.getPwPolc(compId);
		model.put("pwPolc", pwPolc);
		return LayoutUtil.getJspPath("/pt/adm/sys/setPwPolc");
	}
	
	/** 시스템 설정 저장 */
	private String transSysSetup(HttpServletRequest request,
			String setupClsId,
			ModelMap model) throws Exception {
		
		try{
			
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(setupClsId, queryQueue, false, request);
			
			boolean needCodeExpire = false;
			
			// 시스템 설정 - 저장일 경우
			if(PtConstant.PT_SYS_PLOC.equals(setupClsId)){
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				String codeByCompEnableOld = sysPlocMap.get("codeByCompEnable")==null ? "N" : sysPlocMap.get("codeByCompEnable");
				String seculByCompEnableOld = sysPlocMap.get("seculByCompEnable")==null ? "N" : sysPlocMap.get("seculByCompEnable");
				
				String codeByCompEnableNew = request.getParameter("pt.sysPloc.codeByCompEnable")==null ? "N" : request.getParameter("pt.sysPloc.codeByCompEnable");
				String seculByCompEnableNew = request.getParameter("pt.sysPloc.seculByCompEnable")==null ? "N" : request.getParameter("pt.sysPloc.seculByCompEnable");
				
				if(!codeByCompEnableOld.equals(codeByCompEnableNew) || !seculByCompEnableOld.equals(seculByCompEnableNew)){
					needCodeExpire = true;
				}
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				if(needCodeExpire){
					ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP, CacheConfig.CODE);
					commonSvc.execute(queryQueue);
					ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP, CacheConfig.CODE);
				} else {
					ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
					commonSvc.execute(queryQueue);
					ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				}
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 비밀번호 변경 정책 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transPwPolc")
	public String transPwPolc(HttpServletRequest request,
			@Parameter(name="compId", required=false) String compId,
			@Parameter(name="senderName", required=false) String senderName,
			@Parameter(name="senderEmail", required=false) String senderEmail,
			ModelMap model) throws Exception {
		
		if(compId==null || compId.isEmpty()){
			UserVo userVo = LoginSession.getUser(request);
			compId = userVo.getCompId();
		}
		
		// 비밀번호 찾기 메일 발송자 세팅
		if(senderName!=null && !senderName.isEmpty() && senderEmail!=null && !senderEmail.isEmpty()){
			PtSysSetupDVo ptSysSetupDVo;
			QueryQueue queryQueue = new QueryQueue();
			
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(PtConstant.PT_LOST_PW);
			queryQueue.delete(ptSysSetupDVo);
			
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(PtConstant.PT_LOST_PW);
			ptSysSetupDVo.setSetupId("senderName");
			ptSysSetupDVo.setSetupVa(senderName);
			queryQueue.insert(ptSysSetupDVo);
			
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(PtConstant.PT_LOST_PW);
			ptSysSetupDVo.setSetupId("senderEmail");
			ptSysSetupDVo.setSetupVa(senderEmail);
			queryQueue.insert(ptSysSetupDVo);
			
			commonSvc.execute(queryQueue);
		}
		
		return transSysSetup(request, PtConstant.PT_PW_PLOC+compId, model);
	}
	
	/** 포틀릿 변경 정책 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setPltPolc")
	public String setPltPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 포틀릿 이동 최대 픽셀
		model.put("maxPx", PtConstant.PT_PLT_MOVE_MAX_PX);
		// 시스템프로퍼티상세 맵 조회
		Map<String, String> pltPolc = ptSysSvc.getPltPolc();
		model.put("pltPolc", pltPolc);
		return LayoutUtil.getJspPath("/pt/adm/sys/setPltPolc");
	}
	
	/** 포틀릿 변경 정책 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transPltPolc")
	public String transPltPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		return transSysSetup(request, PtConstant.PT_PLT_PLOC, model);
	}
	
	/** 페이지별 레코드 수 설정 */
	@RequestMapping(value = "/pt/adm/sys/setPageRecCnt")
	public String setPageRecCnt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 페이지별레코드설정코드 목록 조회
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("pageRecSetupCdList", pageRecSetupCdList);
		
		String[] recordCnts = {"10","20","30","40","50","60","70","80","90","100"};
		model.put("recordCnts", recordCnts);
		// 시스템 설정 조회
		Map<String, Integer> pageRecCntMap = ptSysSvc.getSysSetupIntMap(PtConstant.PT_PAGE_RCNT, true);
		model.put("pageRecCntMap", pageRecCntMap);
		return LayoutUtil.getJspPath("/pt/adm/sys/setPageRecCnt");
	}
	
	/** 페이지별 레코드 수 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transPageRecCnt")
	public String transPageRecCnt(HttpServletRequest request,
			ModelMap model) throws Exception {
		return transSysSetup(request, PtConstant.PT_PAGE_RCNT, model);
	}
	
	/** 첨부 용량 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setAttachBodySize")
	public String setAttachBodySize(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		String[] attachSizes = {"10","20","30","40","50","60","70","80","90","100","200","300","400","500"};
		model.put("attachSizes", attachSizes);
		
		String[] bodySizes = {"100","200","300","400","500","600","700","800","900","1000"};
		model.put("bodySizes", bodySizes);
		
		// 설정해야할 모듈 목록
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("pageRecSetupCdList", pageRecSetupCdList);
		
		// 시스템 설정 조회 - 본문 사이즈
		Map<String, Integer> attachSizeMap = ptSysSvc.getAttachSizeMap(langTypCd, userVo.getCompId());
		model.put("attachPrefix",  PtConstant.PT_ATTC_SIZE+userVo.getCompId());
		model.put("attachSizeMap", attachSizeMap);
		// 시스템 설정 조회 - 본문 사이즈
		Map<String, Integer> bodySizeMap = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId());
		model.put("bodyPrefix",  PtConstant.PT_BODY_SIZE+userVo.getCompId());
		model.put("bodySizeMap", bodySizeMap);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setAttachBodySize");
	}
	
	/** 첨부 용량 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transAttachBodySize")
	public String transAttachBodySize(HttpServletRequest request,
			ModelMap model) throws Exception {
		try{

			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			if(userVo!=null){
				ptSysSvc.setSysSetup(PtConstant.PT_ATTC_SIZE+userVo.getCompId(), queryQueue, false, request);
				ptSysSvc.setSysSetup(PtConstant.PT_BODY_SIZE+userVo.getCompId(), queryQueue, false, request);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 용어 설정 - 조회 */
	private void setTerms(HttpServletRequest request,
			String setupClsId,
			String[] terms,
			ModelMap model) throws Exception {
		
		model.put("terms", terms);
		model.put("setupClsId", setupClsId);
		// 리소스설정상세(PT_RESC_SETUP_D) 테이블 - 조회
		PtTermSetupDVo ptTermSetupDVo = new PtTermSetupDVo();
		ptTermSetupDVo.setSetupClsId(setupClsId);
		@SuppressWarnings("unchecked")
		List<PtTermSetupDVo> atRescSetupDVoList = (List<PtTermSetupDVo>)commonSvc.queryList(ptTermSetupDVo);
		if(atRescSetupDVoList!=null){
			for(PtTermSetupDVo storedPtTermSetupDVo : atRescSetupDVoList){
				model.put(storedPtTermSetupDVo.getSetupId()+"_"+storedPtTermSetupDVo.getLangTypCd(),
						storedPtTermSetupDVo.getTermVa());
			}
		}
	}
	
	/** 결재 용어 설정 - 조회 */
	@RequestMapping(value = {"/pt/adm/sys/setApTerms", "/ap/adm/term/setApTerms"})
	public String setApTerms(HttpServletRequest request,
			ModelMap model) throws Exception {
		String setupClsId = "ap.term";
		setTerms(request, setupClsId, PtConstant.getTerms(setupClsId), model);
		return LayoutUtil.getJspPath("/pt/adm/sys/setApTerms");
	}
	
	/** 조직도 용어 설정 - 조회 */
	@RequestMapping(value = {"/pt/adm/sys/setOrTerms", "/pt/adm/org/setOrTerms"})
	public String setOrTerms(HttpServletRequest request,
			ModelMap model) throws Exception {
		String setupClsId = "or.term";
		setTerms(request, setupClsId, PtConstant.getTerms(setupClsId), model);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setOrTerms");
	}
	
	/** 용어 설정 - 저장 */
	private String transTerms(HttpServletRequest request,
			String setupClsId,
			ModelMap model) throws Exception {
		
		try{
			QueryQueue queryQueue = new QueryQueue();
			
			// 용어설정상세(PT_TERM_SETUP_D) 테이블 - DELETE
			PtTermSetupDVo ptTermSetupDVo = new PtTermSetupDVo();
			ptTermSetupDVo.setSetupClsId(setupClsId);
			queryQueue.delete(ptTermSetupDVo);
			
//			@SuppressWarnings("unchecked")
//			Enumeration<String> enums = (Enumeration<String>)request.getParameterNames();
			Enumeration<String> enums = request.getParameterNames();
			String paramNm, setupId, langTypCd, rescVa;
			int prefixLength = setupClsId.length()+1, p;
			while(enums.hasMoreElements()){
				paramNm = enums.nextElement();
				if(paramNm!=null && paramNm.startsWith(setupClsId)){
					p = paramNm.lastIndexOf('_');
					if(p>0){
						setupId = paramNm.substring(prefixLength, p);
						langTypCd = paramNm.substring(p+1);
						rescVa = request.getParameter(paramNm);
						
						ptTermSetupDVo = new PtTermSetupDVo();
						ptTermSetupDVo.setSetupClsId(setupClsId);
						ptTermSetupDVo.setSetupId(setupId);
						ptTermSetupDVo.setLangTypCd(langTypCd);
						ptTermSetupDVo.setTermVa(rescVa);
						
						// 리소스설정상세(PT_RESC_SETUP_D) 테이블 - INSERT
						queryQueue.insert(ptTermSetupDVo);
					}
				}
			}
			
			if(queryQueue.size()>1){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.TERMS);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.TERMS);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}

	/** 결재 용어 설정 - 저장 */
	@RequestMapping(value = {"/pt/adm/sys/transApTerms", "/ap/adm/term/transApTerms"})
	public String transApTerms(HttpServletRequest request,
			ModelMap model) throws Exception {
		return transTerms(request, "ap.term", model);
	}

	/** 결재 용어 설정 - 저장 */
	@RequestMapping(value = {"/pt/adm/sys/transOrTerms", "/pt/adm/org/transOrTerms"})
	public String transOrTerms(HttpServletRequest request,
			ModelMap model) throws Exception {
		return transTerms(request, "or.term", model);
	}
	
	/** 리스트 환경 설정 */
	@RequestMapping(value = {"/pt/adm/org/setLstSetup", "/ap/adm/opt/setLstSetup"})
	public String setLstSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String clsCd = null, uri = request.getRequestURI();
		if(uri.indexOf("/org/")>0){
			clsCd = "OR_LST_SETUP_CD";
		} else if(uri.indexOf("/ap/")>=0){
			clsCd = "AP_LST_SETUP_CD";
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 리스트환경설정코드
		List<PtCdBVo> lstSetupCdList = ptCmSvc.getCdList(clsCd, langTypCd, "Y");
		if(lstSetupCdList!=null){
			model.put("lstSetupCdList", lstSetupCdList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setLstSetup");
	}
	/** [프레임] (왼쪽) 리스트 메타정보 목록 조회 */
	@RequestMapping(value = {"/pt/adm/org/listLstSetupMetaFrm", "/ap/adm/opt/listLstSetupMetaFrm"})
	public String listLstEnvSetupMetaFrm(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 목록설정메타상세(PT_LST_SETUP_META_D) 테이블 - 목록조회
		PtLstSetupMetaDVo ptLstSetupMetaDVo = new PtLstSetupMetaDVo();
		ptLstSetupMetaDVo.setLstSetupMetaId(lstSetupMetaId);
		ptLstSetupMetaDVo.setQueryLang(langTypCd);
		ptLstSetupMetaDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtLstSetupMetaDVo> ptLstSetupMetaDVoList = (List<PtLstSetupMetaDVo>)commonSvc.queryList(ptLstSetupMetaDVo);
		model.put("ptLstSetupMetaDVoList", ptLstSetupMetaDVoList);
		return LayoutUtil.getJspPath("/pt/adm/sys/listLstSetupMetaFrm");
	}
	/** [프레임] (오른쪽) 리스트 설정정보 목록 조회 */
	@RequestMapping(value = {"/pt/adm/org/listLstSetupFrm", "/ap/adm/opt/listLstSetupFrm"})
	public String listLstSetupFrm(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 목록설정상세(PT_LST_SETUP_D) 테이블 - 목록조회
		PtLstSetupDVo ptLstSetupDVo = new PtLstSetupDVo();
		ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
		ptLstSetupDVo.setDftVaYn("N");
		ptLstSetupDVo.setQueryLang(langTypCd);
		ptLstSetupDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<PtLstSetupDVo> ptLstSetupDVoList = (List<PtLstSetupDVo>)commonSvc.queryList(ptLstSetupDVo);
		// UI 구성용 - 빈 VO 하나 더함
		if(ptLstSetupDVoList==null) ptLstSetupDVoList = new ArrayList<PtLstSetupDVo>();
		ptLstSetupDVoList.add(new PtLstSetupDVo());
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/listLstSetupFrm");
	}
	
	/** [히든프레임] - 리스트 환경설정 - 저장 */
	@RequestMapping(value = {"/pt/adm/org/transLstSetup", "/ap/adm/opt/transLstSetup"})
	public String transLstSetup(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			String dftVaYn,
			ModelMap model) throws Exception {
		
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			PtLstSetupDVo ptLstSetupDVo = new PtLstSetupDVo();
			ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
			ptLstSetupDVo.setDftVaYn(dftVaYn==null ? "N" : dftVaYn);
			queryQueue.delete(ptLstSetupDVo);
			
			Integer sortOrdr = 1;
			@SuppressWarnings("unchecked")
			List<PtLstSetupDVo> list = (List<PtLstSetupDVo>)VoUtil.bindList(request, PtLstSetupDVo.class, new String[]{"atrbId"});
			if(list != null){
				for(PtLstSetupDVo storedPtLstSetupDVo : list){
					storedPtLstSetupDVo.setDftVaYn(dftVaYn==null ? "N" : dftVaYn);
					storedPtLstSetupDVo.setSortOrdr(sortOrdr.toString());
					storedPtLstSetupDVo.setModrUid(userVo.getUserUid());
					storedPtLstSetupDVo.setModDt("sysdate");
					queryQueue.insert(storedPtLstSetupDVo);
					sortOrdr++;
				}
			}
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LST_ENV);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.LST_ENV);
			
			//cm.msg.save.success=저장 되었습니다.
			String message = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", message);
			return LayoutUtil.getResultJsp();
			
//		} catch(CmException e){
//			model.put("message", e.getMessage());
//			return LayoutUtil.getResultJsp();
		} catch(Exception e){
			e.printStackTrace();
			//cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("m.msg.save.fail", request));
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** [히든프레임] - 리스트 환경설정 - 기본값으로 저장 */
	@RequestMapping(value = "/pt/adm/sys/transLstSetupAsDefault")
	public String transLstSetupAsDefault(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			ModelMap model) throws Exception {
		return transLstSetup(request, lstSetupMetaId, "Y", model);
	}

	/** [히든프레임] - 리스트 환경설정 - 기본값으로 저장 */
	@RequestMapping(value = "/pt/adm/sys/transLstSetupDefault")
	public String transLstSetupDefault(HttpServletRequest request,
			@Parameter(name="lstSetupMetaId", required=false) String lstSetupMetaId,
			ModelMap model) throws Exception {
		
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			PtLstSetupDVo ptLstSetupDVo = new PtLstSetupDVo();
			ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
			ptLstSetupDVo.setDftVaYn("N");
			queryQueue.delete(ptLstSetupDVo);
			
			ptLstSetupDVo = new PtLstSetupDVo();
			ptLstSetupDVo.setLstSetupMetaId(lstSetupMetaId);
			ptLstSetupDVo.setDftVaYn("Y");
			
			@SuppressWarnings("unchecked")
			List<PtLstSetupDVo> list = (List<PtLstSetupDVo>)commonSvc.queryList(ptLstSetupDVo);
			
			if(list != null){
				for(PtLstSetupDVo storedPtLstSetupDVo : list){
					storedPtLstSetupDVo.setDftVaYn("N");
					storedPtLstSetupDVo.setModrUid(userVo.getUserUid());
					storedPtLstSetupDVo.setModDt("sysdate");
					queryQueue.insert(storedPtLstSetupDVo);
				}
			}
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.LST_ENV);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.LST_ENV);
			
			//cm.msg.save.success=저장 되었습니다.
			String message = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", message);
			model.put("todo", "parent.location.replace(parent.location.href);");
			return LayoutUtil.getResultJsp();
			
//		} catch(CmException e){
//			model.put("message", e.getMessage());
//			return LayoutUtil.getResultJsp();
		} catch(Exception e){
			e.printStackTrace();
			//cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("m.msg.save.fail", request));
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** 어권별 폰트 조회 */
	@RequestMapping(value = "/pt/adm/sys/setFontByLang")
	public String setFontByLang(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 셋업분류ID
		model.put("setupClsId", PtConstant.PT_LANG_FONT);
		// 시스템프로퍼티상세 맵 조회
		Map<String, String> fontMap = ptSysSvc.getFontByLangMap();
		Iterator<Entry<String, String>> iterator = fontMap.entrySet().iterator();
		Entry<String, String> entry;
		String value;
		while(iterator.hasNext()){
			entry = iterator.next();
			value = entry.getValue();
			if(value!=null && !value.isEmpty()){
				model.put(entry.getKey(), value.split("\\|"));
			}
		}
		
		String langTypCd = LoginSession.getLangTypCd(request);
		List<PtCdBVo> langTypCdList = ptCmSvc.getCdList("LANG_TYP_CD", langTypCd, null);
		model.put("langTypCdList", langTypCdList);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setFontByLang");
	}
	
	/** 어권별 폰트 저장 */
	@RequestMapping(value = "/pt/adm/sys/transFontByLang")
	public String transFontByLang(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		String langTypCd = LoginSession.getLangTypCd(request);
		List<PtCdBVo> langTypCdList = ptCmSvc.getCdList("LANG_TYP_CD", langTypCd, null);
		
		boolean hasValue=false, deletePrevious=false;
		int i;
		StringBuilder builder;
		String[] arr;
		
		String setupClsId = PtConstant.PT_LANG_FONT;
		PtSysSetupDVo ptSysSetupDVo;
		
//		String currDt = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		
		if(langTypCdList!=null){
			for(PtCdBVo ctCdBVo : langTypCdList){
				arr = request.getParameterValues(ctCdBVo.getCd());
				if(arr!=null && arr.length>0){
					hasValue = false;
					builder = new StringBuilder(256);
					for(i=0; i<arr.length; i++){
						if(arr[i]==null) continue;
						arr[i] = arr[i].trim();
						if(arr[i].isEmpty()) continue;
						
						if(hasValue) builder.append('|');
						else hasValue = true;
						builder.append(arr[i]);
					}
					
					if(hasValue){
						
						if(!deletePrevious){
							// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 기존 데이터 삭제
							ptSysSetupDVo = new PtSysSetupDVo();
							ptSysSetupDVo.setSetupClsId(setupClsId);
							queryQueue.delete(ptSysSetupDVo);
							deletePrevious = true;
						}
						
						// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - INSERT
						ptSysSetupDVo = new PtSysSetupDVo();
						ptSysSetupDVo.setSetupClsId(setupClsId);
						ptSysSetupDVo.setSetupId(ctCdBVo.getCd());
						ptSysSetupDVo.setSetupVa(builder.toString());
						queryQueue.insert(ptSysSetupDVo);
					}
				}
			}
		}
		
		try{

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 연계 서버 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setEngSvrSetup")
	public String setEngSvrSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 셋업분류ID
		model.put("setupClsId", PtConstant.PT_SVR_ENV);
//		model.put("envIds", ptSysSvc.getSvrEnvIds());

		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		model.put("svrEnvMap", svrEnvMap);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setEngSvrSetup");
	}
	
	/** 연계 서버 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transEngSvrSetup")
	public String transEngSvrSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		return transSysSetup(request, PtConstant.PT_SVR_ENV, model);
	}
	
	/** ERP SSO 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setErpSsoSetup")
	public String setErpSsoSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String setupClsId = PtConstant.PT_ERP_SSO+userVo.getCompId();
		
		Map<String, String> erpSsoMap = ptSysSvc.getSysSetupMap(setupClsId, true);
		model.put("setupClsId", setupClsId);
		model.put("erpSsoMap", erpSsoMap);

		return LayoutUtil.getJspPath("/pt/adm/sys/setErpSsoSetup");
	}
	
	/** ERP SSO 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transErpSsoSetup")
	public String transErpSsoSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String setupClsId = PtConstant.PT_ERP_SSO+userVo.getCompId();
		
		return transSysSetup(request, setupClsId, model);
	}
	
	/** 로그인 페이지 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setLoginPage")
	public String setLoginPage(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
		ptLginImgDVo.setQueryLang(langTypCd);
		ptLginImgDVo.setOrderBy("LGIN_IMG_ID DESC");
		
		Integer recodeCount = commonSvc.count(ptLginImgDVo);
		PersonalUtil.setPaging(request, ptLginImgDVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<PtLginImgDVo> ptLginImgDVoList = (List<PtLginImgDVo>)commonSvc.queryList(ptLginImgDVo);
		if(ptLginImgDVoList != null){
			model.put("ptLginImgDVoList", ptLginImgDVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setLoginPage");
	}
	
	/** 로그인 페이지 설정 - 등록/수정 */
	@RequestMapping(value = "/pt/adm/sys/setLoginPagePop")
	public String setLoginPagePop(HttpServletRequest request,
			@RequestParam(value = "lginImgId", required = false) String lginImgId,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(lginImgId != null){
			PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setQueryLang(langTypCd);
			ptLginImgDVo.setLginImgId(lginImgId);
			ptLginImgDVo = (PtLginImgDVo)commonSvc.queryVo(ptLginImgDVo);
			if(ptLginImgDVo != null){
				model.put("ptLginImgDVo", ptLginImgDVo);
				
				// 리소스 조회
				PtRescBVo ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(ptLginImgDVo.getRescId());
				@SuppressWarnings("unchecked")
				List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);
				if(ptRescBVoList!=null){
					for(PtRescBVo storedPtRescBVo : ptRescBVoList){
						model.put(storedPtRescBVo.getRescId()+"_"+storedPtRescBVo.getLangTypCd(), storedPtRescBVo.getRescVa());
					}
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setLoginPagePop");
	}
	
	/** 로그인 페이지 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transLoginPage")
	public String transLoginPage(HttpServletRequest request,
			String setupClsId,
			ModelMap model, Locale locale) throws Exception {
		
		try{

			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "pt");
			uploadHandler.upload();//업로드 파일 정보
			
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			Map<String, File> fileMap = uploadHandler.getFileMap();//파일 리스트 Map
			
			MultipartHttpServletRequest mRequest = uploadHandler.getMultipartRequest();
			
			PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.fromMap(paramMap);
			
			boolean isNew = false;
			// 로그인이미지ID - KEY
			if(ptLginImgDVo.getLginImgId()==null){
				isNew = true;
				ptLginImgDVo.setLginImgId(ptCmSvc.createId("PT_LGIN_IMG_D"));
			}
			// 기본이미지여부
			if(ptLginImgDVo.getDftImgYn()==null) {
				ptLginImgDVo.setDftImgYn("N");
			} else if("Y".equals(ptLginImgDVo.getDftImgYn())){
				// 기본이미지로 지정해서 넘오오면 - 기존 기본이미지 - 지정을 제거함
				PtLginImgDVo dftPtLginImgDVo = new PtLginImgDVo();
				dftPtLginImgDVo.setDftImgYn("N");
				queryQueue.update(dftPtLginImgDVo);
			}
			ptLginImgDVo.setModrUid(userVo.getUserUid());
			ptLginImgDVo.setModDt("sysdate");
			
			PtRescBVo ptRescBVo = ptRescSvc.collectPtRescBVo(mRequest, null, queryQueue);
			if(ptRescBVo != null){
				ptLginImgDVo.setRescId(ptRescBVo.getRescId());
				ptLginImgDVo.setLginImgNm(ptRescBVo.getRescVa());
			}
			
			DistHandler distHandler = distManager.createHandler("images/upload/pt/login", locale);
			String distPath;
			boolean hasImage = false;
			// 배경 이미지
			File imgFile = fileMap.get("bgImgFile");
			if(imgFile != null){
				hasImage = true;
				distPath = distHandler.addWebList(imgFile.getAbsolutePath());
				ptLginImgDVo.setBgImgPath(distPath);
				
				BufferedImage bimg = ImageIO.read(imgFile);
				int left = (756 - bimg.getWidth()) / 2;
				int top = (465 - bimg.getHeight()) / 2;
				ptLginImgDVo.setLeftPx(Integer.toString(left));
				ptLginImgDVo.setTopPx(Integer.toString(top));
			}
			// 로고 이미지
			imgFile = fileMap.get("logoImgFile");
			if(imgFile != null){
				hasImage = true;
				distPath = distHandler.addWebList(imgFile.getAbsolutePath());
				ptLginImgDVo.setLogoImgPath(distPath);
			}
			
			if(isNew){
				queryQueue.insert(ptLginImgDVo);
			} else {
				queryQueue.store(ptLginImgDVo);
			}
			
			if(hasImage){
				distHandler.distribute();
			}
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			uploadHandler.removeTempDir();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			LOGGER.error(message);
			// cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.fail", request));
			e.printStackTrace();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 로그인 페이지 설정 - 최상위로 */
	@RequestMapping(value = "/pt/adm/sys/transLoginPageToTopAjx")
	public String transLoginPageToTopAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		try {
			// 2015-12-04
			// 기존 테이블 수정하지 않기 위해서 테이블의 KEY를 새로 생성 저장하고, 기존 데이터를 삭제함
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String lginImgId = (String)jsonObject.get("lginImgId");
			
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 해당 데이터 삭제
			PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setLginImgId(lginImgId);
			queryQueue.delete(ptLginImgDVo);
			
			// 해당 데이터 조회
			ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setLginImgId(lginImgId);
			ptLginImgDVo = (PtLginImgDVo)commonSvc.queryVo(ptLginImgDVo);
			
			if(ptLginImgDVo == null){
				//cm.msg.noSelect=선택한 항목이 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.noSelect", request));
				return JsonUtil.returnJson(model);
			}
			
			ptLginImgDVo.setLginImgId(ptCmSvc.createId("PT_LGIN_IMG_D"));
			ptLginImgDVo.setModrUid(userVo.getUserUid());
			ptLginImgDVo.setModDt("sysdate");
			queryQueue.insert(ptLginImgDVo);
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 로그인 페이지 설정 - 삭제 */
	@RequestMapping(value = "/pt/adm/sys/transLoginPageDelAjx")
	public String transLoginPageDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray)jsonObject.get("lginImgIds");
			String lginImgId;
			
			int i, size = jsonArray==null ? 0 : jsonArray.size();
			if(size==0){
				//cm.msg.noSelect=선택한 항목이 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.noSelect", request));
				return JsonUtil.returnJson(model);
			}
			
			PtLginImgDVo ptLginImgDVo;
			for(i=0;i<size;i++){
				lginImgId = (String)jsonArray.get(i);
				ptLginImgDVo = new PtLginImgDVo();
				ptLginImgDVo.setLginImgId(lginImgId);
				queryQueue.delete(ptLginImgDVo);
			}

			String dbTime = ptCacheExpireSvc.getDbTime();
			
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", locale));
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 보안 정책 설정 */
	@RequestMapping(value = "/pt/adm/sys/setSecuPolc")
	public String setSecuPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		// IP 보안 정책 조회
		Map<String, String> secuPolc = ptSysSvc.getSecuPolc();
		model.put("secuPolc", secuPolc);
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		int seq = 0;
		String va, prefix = "lginIpRange";
		
		List<String> lginIpRangeList = new ArrayList<String>();
		while(true){
			va = secuPolc.get(prefix+(++seq));
			if(va==null) break;
			if(!va.isEmpty()) lginIpRangeList.add(va);
		}
		model.put("lginIpRangeList", lginIpRangeList);
		model.put("lginIpRangeSize", Integer.valueOf(lginIpRangeList.size()));
		
		seq = 0;
		prefix = "sessionIpExcp";
		List<String> sessionIpExcpList = new ArrayList<String>();
		while(true){
			va = secuPolc.get(prefix+(++seq));
			if(va==null) break;
			if(!va.isEmpty()) sessionIpExcpList.add(va);
		}
		model.put("sessionIpExcpList", sessionIpExcpList);
		model.put("sessionIpExcpSize", Integer.valueOf(sessionIpExcpList.size()));
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setSecuPolc");
	}
	
	/** 보안 정책 설정 - 해더 조회 */
	@RequestMapping(value = "/pt/adm/sys/viewHeaderPop")
	public String viewHeaderPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/pt/adm/sys/viewHeaderPop");
	}
	
	/** 보안 정책 설정 - 해더 조회 */
	@RequestMapping(value = "/pt/adm/sys/viewProxyPop")
	public String viewProxyPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		model.put("headerNames", ipChecker.getProxyHeaders());
		return LayoutUtil.getJspPath("/pt/adm/sys/viewProxyPop");
	}
	
	/** IP 정책 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transSecuPolc")
	public String transSecuPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		transSysSetup(request, PtConstant.PT_SECU_PLOC, model);
		model.put("todo", "parent.location.replace(parent.location.href)");
		return LayoutUtil.getResultJsp();
	}
	
	/** 시스템 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setSysPolcPop")
	public String setSysPolcPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		model.put("pcNotiMds", PtConstant.PC_NOTI_MDS);
		
		// 회사 목록 조회
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", LoginSession.getLangTypCd(request));
		model.put("ptCompBVoList", ptCompBVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setSysPolcPop");
	}
	
	/** 시스템 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/transSysPolc")
	public String transSysPolc(HttpServletRequest request,
			ModelMap model) throws Exception {
		transSysSetup(request, PtConstant.PT_SYS_PLOC, model);
		model.put("todo", "parent.location.replace(parent.location.href)");
		
		if("Y".equals(request.getParameter("pt.sysPloc.orgMailSyncLogEnable"))){
			if(ServerConfig.IS_LINUX){
				System.out.println("\n\n[MAIL LOG] : /app/NavidMail/apache-tomcat-5.5.27/webapps/zmail/logs/navidmail.log\n\n");
			}
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** 복사방지 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setAntiCopyPop")
	public String setAntiCopyPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 페이지별레코드설정코드 목록 조회
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("moduleCdList", pageRecSetupCdList);
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 설정 조회
		Map<String, String> antiCopyMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ANTI_COPY+userVo.getCompId(), true);
		model.put("antiCopyMap", antiCopyMap);
		model.put("setupClsId", PtConstant.PT_ANTI_COPY+userVo.getCompId()+".");
		return LayoutUtil.getJspPath("/pt/adm/sys/setAntiCopyPop");
	}
	/** 복사방지 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transAntiCopy")
	public String transAntiCopy(HttpServletRequest request,
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		transSysSetup(request, PtConstant.PT_ANTI_COPY+userVo.getCompId(), model);
		model.put("todo", "parent.dialog.close('setAntiCopyDialog');");
		return LayoutUtil.getResultJsp();
	}
	
	/** 첨부 확장자 설정 - 조회 */
	@RequestMapping(value = "/pt/adm/sys/setAttachExt")
	public String setAttachExt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 설정해야할 모듈 목록
		List<PtCdBVo> pageRecSetupCdList = ptCmSvc.getCdList("PAGE_REC_SETUP_CD", langTypCd, "Y");
		model.put("pageRecSetupCdList", pageRecSetupCdList);
		
		// 시스템 설정 조회 - 확장자
		Map<String, String> attachExtMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ATTC_EXT+userVo.getCompId(), true);
		model.put("attachPrefix",  PtConstant.PT_ATTC_EXT+userVo.getCompId());
		model.put("attachExtMap", attachExtMap);
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setAttachExt");
	}
	
	/** 첨부 확장자 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transAttachExt")
	public String transAttachExt(HttpServletRequest request,
			ModelMap model) throws Exception {
		try{

			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			if(userVo!=null){
				ptSysSvc.setSysSetup(PtConstant.PT_ATTC_EXT+userVo.getCompId(), queryQueue, false, request);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 확장자 목록 */
	@RequestMapping(value = "/pt/adm/sys/viewExtsPop")
	public String viewExtsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/pt/adm/sys/viewExtsPop");
	}
	
	/** [팝업] 첨부파일 설정 */
	@RequestMapping(value = "/pt/adm/sys/setAttachSetupPop")
	public String setAttachSetupPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 사용자 정보 조회
		UserVo userVo = LoginSession.getUser(request);
		
		// 문서뷰어 사용여부
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean viewerWebEnable = sysPlocMap != null && sysPlocMap.containsKey("docViewerWebEnable") && "Y".equals(sysPlocMap.get("docViewerWebEnable"));
		model.put("viewerWebEnable", viewerWebEnable);
		
		boolean viewerMobEnable = sysPlocMap != null && sysPlocMap.containsKey("docViewerMobEnable") && "Y".equals(sysPlocMap.get("docViewerMobEnable"));
		model.put("viewerMobEnable", viewerMobEnable);
		
		// 시스템 설정 조회 - 첨부설정
		Map<String, String> attcSetupMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ATTC_SETUP+userVo.getCompId(), true);
		model.put("attcSetupMap", attcSetupMap);
		
		model.put("attcSetupPrefix",  PtConstant.PT_ATTC_SETUP+userVo.getCompId());
		
		return LayoutUtil.getJspPath("/pt/adm/sys/setAttachSetupPop");
	}
	
	/** 첨부파일 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transAttachSetup")
	public String transAttachSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		try{
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			if(userVo!=null){
				ptSysSvc.setSysSetup(PtConstant.PT_ATTC_SETUP+userVo.getCompId(), queryQueue, false, request);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 첨부파일 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/sys/transAttachSetupAjx")
	public String transAttachSetupAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		try{
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			// 다운로드여부
			String downloadYn = (String) object.get("downloadYn");
			
			// 미리보기 여부
			String previewYn = (String) object.get("previewYn");
			// 미리보기 다운로드 여부
			String previewDownYn = (String) object.get("previewDownYn");
			
			if ( downloadYn == null || downloadYn.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			if(previewYn==null || previewYn.isEmpty()) previewYn = "N";
			if(previewDownYn==null || previewDownYn.isEmpty()) previewDownYn = "N";
			
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			if(userVo!=null){
				ptSysSvc.setSysSetup(PtConstant.PT_ATTC_SETUP+userVo.getCompId(), queryQueue, false, request);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 인쇄 미리보기 여부( IE ) */
	@RequestMapping(value = "/cm/getPreviewPrintAjx")
	public String getPreviewPrintAjx(HttpServletRequest request,
			ModelMap model) {
		
		try{
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if(sysPlocMap != null && sysPlocMap.containsKey("previewEnable")){
				model.put("previewEnable", sysPlocMap.get("previewEnable"));
			}else{
				model.put("previewEnable", "N");
			}
		}catch(SQLException se){
			model.put("previewEnable", "N");
			LOGGER.error("sysPlocMap error!!");
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 빈번한 리프레쉬 사용자 조회 */
	@RequestMapping(value = "/pt/adm/sys/getFuncUserAjx")
	public String getFuncUserAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) {
		
		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String compId = (String)jsonObject.get("compId");
			String funcId = (String)jsonObject.get("funcId");
			

			String setupClsId = 
				"frequentRefreshUsers".equals(funcId) ? PtConstant.PT_FREQUENT_REFRESH :
				"apBodyViewUsers".equals(funcId) ? PtConstant.AP_BODY_VIEW_USER :
				"ptPwExceptionUsers".equals(funcId) ? PtConstant.PT_PW_EXCEPTION_USER : null;
			
			if(setupClsId != null){
				Map<String, String> sysSetupMap = ptSysSvc.getSysSetupMap(setupClsId, true);
				String userUids = sysSetupMap.get(compId);
				model.put("userUids", userUids==null ? "" : userUids);
			}
			
		}catch(SQLException se){
			model.put("userUids", "");
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 빈번한 리프레쉬 사용자 조회 */
	@RequestMapping(value = "/pt/adm/sys/transFuncUserAjx")
	public String transFuncUserAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) {
		
		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String compId = (String)jsonObject.get("compId");
			String funcId = (String)jsonObject.get("funcId");
			String userUids = (String)jsonObject.get("userUids");
			
			QueryQueue queryQueue = new QueryQueue();
			
			
			String setupClsId = 
				"frequentRefreshUsers".equals(funcId) ? PtConstant.PT_FREQUENT_REFRESH :
				"apBodyViewUsers".equals(funcId) ? PtConstant.AP_BODY_VIEW_USER :
				"ptPwExceptionUsers".equals(funcId) ? PtConstant.PT_PW_EXCEPTION_USER : null;
			
			if(setupClsId!=null){
				
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블
				PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId(compId);
				if(userUids==null || userUids.isEmpty()){
					queryQueue.delete(ptSysSetupDVo);
				} else {
					ptSysSetupDVo.setSetupVa(userUids);
					queryQueue.store(ptSysSetupDVo);
				}
				
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				
			} else {
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.notValidCall", request));
			}
			
			
		}catch(Exception se){
			String message = se.getMessage();
			if(message==null || message.isEmpty()) message = se.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	//transFrequentRefreshAjx
	//Map<String, String> frequentRefresh = getSysSetupMap(PtConstant.PT_FREQUENT_REFRESH, true);
}
