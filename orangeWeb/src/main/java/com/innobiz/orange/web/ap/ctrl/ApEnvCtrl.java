package com.innobiz.orange.web.ap.ctrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApAgnApntDVo;
import com.innobiz.orange.web.ap.vo.ApAgnApntRVo;
import com.innobiz.orange.web.ap.vo.ApClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApPsnClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApRescBVo;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
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
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgHstRVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;

/** 환경설정 컨트롤러 - 결재 */
@Controller
public class ApEnvCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApEnvCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
//	/** 조직도 사용자 Push 방식 동기화 서비스 */
//	@Autowired
//	private PushSyncSvc pushSyncSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 결재 리소스 처리 서비스 */
	@Resource(name = "apRescSvc")
	private ApRescSvc apRescSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	
	///////////////////////////////////////////////////
	//
	//  개인정보
	
	/** 개인정보 */
	@RequestMapping(value = "/ap/env/setPsnEnv")
	public String setPsnEnv(HttpServletRequest request,
					ModelMap model) throws Exception {
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		String userUid = userVo.getUserUid();
		String odurUid = userVo.getOdurUid();
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		if(orUserBVo!=null) model.put("orUserBVo", orUserBVo);
		
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		// 겸직자 이미지 조회
		orUserImgDVo.setUserUid(userUid);
		@SuppressWarnings("unchecked")
		List<OrUserImgDVo> orUserImgDVoList = (List<OrUserImgDVo>)commonSvc.queryList(orUserImgDVo);
		if(orUserImgDVoList!=null){
			// userImgTypCd : 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
			for(OrUserImgDVo storedOrUserImgDVo : orUserImgDVoList){
				if(storedOrUserImgDVo.getImgHght()!=null && Integer.parseInt(storedOrUserImgDVo.getImgHght())>150){
					storedOrUserImgDVo.setImgHght("150");
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
						if(storedOrUserImgDVo.getImgHght()!=null && Integer.parseInt(storedOrUserImgDVo.getImgHght())>150){
							storedOrUserImgDVo.setImgHght("150");
						}
						model.put("orUserImgDVo"+storedOrUserImgDVo.getUserImgTypCd(), storedOrUserImgDVo);
					}
				}
			}
		}
		
		//서명방법코드
		List<PtCdBVo> signMthdCdList = ptCmSvc.getCdList("SIGN_MTHD_CD", langTypCd, "Y");
		model.put("signMthdCdList", signMthdCdList);
		
		model.put("deftSignMthdCd", ApConstant.DFT_SIGN_MTHD_CD);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("blockPnsPhotoEnable"))){
			model.put("blockPnsPhotoEnable", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/ap/env/setPsnEnv");
	}
	/** [히든프레임] 서명방법 저장 */
	@RequestMapping(value = "/ap/env/transPsnSignMthd")
	public String transPsnSignMthd(HttpServletRequest request,
			@Parameter(name="signMthdCd", required=false) String signMthdCd,
			ModelMap model) throws Exception {
		try{
			
			UserVo userVo = LoginSession.getUser(request);
			
			// signMthdCd : 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자)
			
			// 도장, 서명 - 이미지 유무 확인
			if("01".equals(signMthdCd) || "02".equals(signMthdCd)){
				
				OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
				
				// 겸직자 도장/사인 이미지 확인
				orUserImgDVo.setUserUid(userVo.getUserUid());
				// 사용자이미지구분코드 - 01:도장, 02:싸인, 03:사진
				orUserImgDVo.setUserImgTypCd(signMthdCd);
				orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
				
				// 이미지가 없으면
				if(orUserImgDVo == null || orUserImgDVo.getImgPath() == null || orUserImgDVo.getImgPath().isEmpty()){
					
					// 원직자 와 겸직자 같으면
					if(userVo.getUserUid().equals(userVo.getOdurUid())){
						// ap.msg.noImgNoSignMthChange=이미지가 등록되지 않아서 서명방법을 변경 할 수 없습니다.
						model.put("message", messageProperties.getMessage("ap.msg.noImgNoSignMthChange", request));
						return LayoutUtil.getResultJsp();
					}
					
					// 원직자 도장/사인 이미지 확인
					orUserImgDVo = new OrUserImgDVo();
					orUserImgDVo.setUserUid(userVo.getOdurUid());
					orUserImgDVo.setUserImgTypCd(signMthdCd);
					orUserImgDVo = (OrUserImgDVo)commonSvc.queryVo(orUserImgDVo);
					
					// 이미지가 없으면
					if(orUserImgDVo == null || orUserImgDVo.getImgPath() == null || orUserImgDVo.getImgPath().isEmpty()){
						// ap.msg.noImgNoSignMthChange=이미지가 등록되지 않아서 서명방법을 변경 할 수 없습니다.
						model.put("message", messageProperties.getMessage("ap.msg.noImgNoSignMthChange", request));
						return LayoutUtil.getResultJsp();
					}
				}
			}
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(userVo.getUserUid());
			orUserBVo.setSignMthdCd(signMthdCd);
			orUserBVo.setModDt("sysdate");
			orUserBVo.setModrUid(userVo.getUserUid());
			commonSvc.update(orUserBVo);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	/** [히든프레임] 결재 비밀번호 저장 */
	@RequestMapping(value = "/ap/env/transPsnApvPw")
	public String transPsnApvPw(HttpServletRequest request,
			@RequestParam(value = "secu", required = true) String secu,
			Locale locale,
			ModelMap model) throws Exception {
		
		String focusId = "apvPw1";
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Change Apv. password fail(by user) : "+e.getMessage());
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				throw new CmException(messageProperties.getMessage("pt.login.fail.decrypt", locale));
			}
			
			String lginPw = (String)jsonObject.get("lginPw");
			String apvPw1 = (String)jsonObject.get("apvPw1");
			String apvPw2 = (String)jsonObject.get("apvPw2");
			String odurUid = userVo.getOdurUid();
			
			if(lginPw==null || lginPw.isEmpty() || apvPw1==null || apvPw1.isEmpty() || apvPw2==null || apvPw2.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", locale);
				throw new CmException(msg);
			}
			
			if(lginPw.equals(apvPw1)){
				// pt.jsp.setPsnEnv.pw.sameWithLgin=로그인 비밀번호와 같은 번호를 설정 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.jsp.setPsnEnv.pw.sameWithLgin", locale);
				LOGGER.error(msg+"  odurUid:"+odurUid);
				throw new CmException(msg);
			}
			
			if(apvPw1.length()<6){
				// cm.input.check.minlength="{0}"(은)는 최소 {1}자리 입력해야 합니다.
				// pt.jsp.setPw.typeApp=결재 비밀번호
				String msg = messageProperties.getMessage("cm.input.check.minlength", new String[]{
						"#pt.jsp.setPw.typeApp", "6"}, locale);
				LOGGER.error(msg+"  odurUid:"+odurUid);
				throw new CmException(msg);
			}
			
			OrUserPwDVo orUserPwDVo;
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 로그인 비밀번호 조회
			orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(odurUid);
			orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
			orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
			
			// 비밀번호 암호화 - 비교를 위한것
			String encryptedOrgPw = cryptoSvc.encryptPw(lginPw, odurUid);
			if(encryptedOrgPw!=null && orUserPwDVo!=null && !encryptedOrgPw.equals(orUserPwDVo.getPwEnc())){
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				String msg = messageProperties.getMessage("pt.login.noUserNoPw", locale);
				LOGGER.error(msg+"  odurUid:"+odurUid+" - not matched");
				focusId = "lginPw";
				throw new CmException(msg);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 새로운 비밀번호 암호화(파라미터) - 비교를 위한것
			String encryptedNewPw = cryptoSvc.encryptPw(apvPw1, odurUid);
			String sysdate = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - UPDATE
			orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(odurUid);
			orUserPwDVo.setPwTypCd("APV");//SYS:시스템 비밀번호, APV:결재 비밀번호
			orUserPwDVo.setModDt(sysdate);
			orUserPwDVo.setModrUid(userVo.getUserUid());
			orUserPwDVo.setPwEnc(encryptedNewPw);
			queryQueue.store(orUserPwDVo);
			
			// 일괄실행
			commonSvc.execute(queryQueue);
			
			// 결과 메세지
			// cm.msg.modify.success=변경 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.modify.success", request));
			model.put("todo", "parent.reload();");
			
//		} catch(CmException e){
//			model.put("message", e.getMessage());
//			model.put("todo", "parent.getElementById('"+focusId+"').focus()");
		} catch(Exception e){
			model.put("message", e.getMessage());
			model.put("todo", "parent.document.getElementById('"+focusId+"').focus();");
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	///////////////////////////////////////////////////
	//
	//  결재정보 - 대결 정보
	
	/** 결재정보 */
	@RequestMapping(value = "/ap/env/setApvEnv")
	public String setApvEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/env/setApvEnv");
	}
	/** 결재정보 [탭] 부재 설정 */
	@RequestMapping(value = "/ap/env/setApvAgnApntFrm")
	public String setApvAgnApntFrm(HttpServletRequest request,
			@RequestParam(value = "userUid", required = false) String userUid,
			ModelMap model) throws Exception {
		
		setApvAgnApntAdmPop(request, userUid, null, null, model);
		listApvAgnApntAdmFrm(request, userUid, model);
		return LayoutUtil.getJspPath("/ap/env/setApvAgnApntFrm");
	}
	/** 결재정보 [탭] 부재 설정 - 관리자용 */
	@RequestMapping(value = {"/ap/env/listApvAgnApntAdmFrm", "/pt/adm/org/listApvAgnApntAdmFrm", "/pt/adm/user/listApvAgnApntAdmFrm"})
	public String listApvAgnApntAdmFrm(HttpServletRequest request,
			@RequestParam(value = "userUid", required = false) String userUid,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);

		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		// DB 기준 시간
//		String dbTime = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD HH24:MI:SS"));
//		int hour = Integer.parseInt(dbTime.substring(11, 13), 10);
//		String durEndDt = dbTime.substring(0, 11) + (hour < 12 ? "00:00:00" : "12:00:00");
		
		// WAS 기준 시간으로 해야함
		// - 0시 12시 was의 스프링 스케쥴러가 대결자 정보 expire 시키므로 was 시간 기준으로 해야 놓치지 않음
		long current = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String dbTime = format.format(new Date(current));
		int hour = Integer.parseInt(dbTime.substring(11, 13), 10);
		String durEndDt = dbTime.substring(0, 11) + (hour < 12 ? "01:00:00" : "13:00:00");
		
		// 대리지정 상세 목록
		ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
		apAgnApntDVo.setUserUid(userUid);
		apAgnApntDVo.setDurCat("endDt");
		apAgnApntDVo.setDurEndDt(durEndDt);
		apAgnApntDVo.setOrderBy("END_DT DESC");
		apAgnApntDVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<ApAgnApntDVo> apAgnApntDVoList = (List<ApAgnApntDVo>)commonSvc.queryList(apAgnApntDVo);
		model.put("apAgnApntDVoList", apAgnApntDVoList);
		
		return LayoutUtil.getJspPath("/ap/env/listApvAgnApntAdmFrm");
	}
	/** 결재정보 [탭] 부재 설정 - 팝업(관리자용) */
	@RequestMapping(value = {"/ap/env/setApvAgnApntAdmPop", "/pt/adm/org/setApvAgnApntAdmPop", "/pt/adm/user/setApvAgnApntAdmPop"})
	public String setApvAgnApntAdmPop(HttpServletRequest request,
			@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(value = "agntUid", required = false) String agntUid,
			@RequestParam(value = "seq", required = false) String seq,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);

		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			model.put("uidParam", "&userUid="+userUid);
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		// 부재사유코드
		List<PtCdBVo> absRsonCdList = ptCmSvc.getCdList("ABS_RSON_CD", langTypCd, "Y");
		model.put("absRsonCdList", absRsonCdList);
		
		// 대리인 목록 조회
		ApAgnApntRVo apAgnApntRVo = new ApAgnApntRVo();
		apAgnApntRVo.setUserUid(userUid);
		apAgnApntRVo.setOrderBy("SORT_ORDR");
		apAgnApntRVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<ApAgnApntRVo> apAgnApntRVoList = (List<ApAgnApntRVo>)commonSvc.queryList(apAgnApntRVo);
		model.put("apAgnApntRVoList", apAgnApntRVoList);
		
		if(agntUid!=null && seq!=null){
			ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
			apAgnApntDVo.setUserUid(userUid);
			apAgnApntDVo.setAgntUid(agntUid);
			apAgnApntDVo.setSeq(seq);
			apAgnApntDVo = (ApAgnApntDVo)commonSvc.queryVo(apAgnApntDVo);
			model.put("apAgnApntDVo", apAgnApntDVo);
		}
		
		return LayoutUtil.getJspPath("/ap/env/setApvAgnApntAdmPop");
	}
	/** [AJAX] 대결자 추가/삭제 */
	@RequestMapping(value = {"/ap/env/transAgntAjx", "/pt/adm/org/transAgntAjx", "/pt/adm/user/transAgntAjx"})
	public String transAgntAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		ApAgnApntRVo apAgnApntRVo = new ApAgnApntRVo();
		apAgnApntRVo.setUserUid(userUid);
		apAgnApntRVo.setAgntUid((String)jsonObject.get("agntUid"));
		apAgnApntRVo.setAgntRescId((String)jsonObject.get("agntRescId"));
		
		if("reg".equals(jsonObject.get("mode"))){
			// 대결자 추가
			int count = commonSvc.count(apAgnApntRVo);
			if(count>0){
				//ap.jsp.setApvEnv.msg.agnDup=이미 추가된 대결자 입니다.
				model.put("message", messageProperties.getMessage("ap.jsp.setApvEnv.msg.agnDup", request));
			} else {
				commonSvc.insert(apAgnApntRVo);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("result", "ok");
			}
		} else if("del".equals(jsonObject.get("mode"))){
			// 대결자 삭제
			commonSvc.delete(apAgnApntRVo);
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [히트프레임] 대리지정 - 저장 */
	@RequestMapping(value = {"/ap/env/transAgnApnt", "/pt/adm/org/transAgnApnt", "/pt/adm/user/transAgnApnt"})
	public String transAgnApnt(HttpServletRequest request,
			@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(value = "strtDt", required = false) String strtDt,
			@RequestParam(value = "strtDtAmPm", required = false) String strtDtAmPm,
			@RequestParam(value = "endDt", required = false) String endDt,
			@RequestParam(value = "endDtAmPm", required = false) String endDtAmPm,
			@RequestParam(value = "mode", required = false) String mode,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isAdminPage = false;
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			isAdminPage = true;
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			if("del".equals(mode)){
				ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
				VoUtil.bind(request, apAgnApntDVo);
				apAgnApntDVo.setUserUid(userUid);
				
				queryQueue.delete(apAgnApntDVo);
				//cm.msg.del.success=삭제 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				if(!isAdminPage){
					model.put("todo", "parent.reload();");
				} else {
					model.put("todo", "parent.setUserPopTab('agnApnt');");
				}
			} else {
				ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
				VoUtil.bind(request, apAgnApntDVo);
				apAgnApntDVo.setUserUid(userUid);

				if(apAgnApntDVo.getStrtDt()!=null && !apAgnApntDVo.getStrtDt().isEmpty()){
					apAgnApntDVo.setStrtDt(strtDt +("PM".equals(strtDtAmPm) ? " 12:00:00" : " 00:00:00")); 
				}
				if(apAgnApntDVo.getEndDt()!=null && !apAgnApntDVo.getEndDt().isEmpty()){
					apAgnApntDVo.setEndDt(endDt +("PM".equals(endDtAmPm) ? " 23:59:59" : " 12:00:00")); 
				}

				if(apAgnApntDVo.getSeq()!=null && apAgnApntDVo.getSeq().isEmpty()){
					apAgnApntDVo.setSeq(null);
				}

				if(apAgnApntDVo.getSeq()==null){
					
					ApAgnApntDVo apAgnApntDVo2 = new ApAgnApntDVo();
					apAgnApntDVo2.setUserUid(apAgnApntDVo.getUserUid());
					apAgnApntDVo2.setAgntUid(apAgnApntDVo.getAgntUid());
					apAgnApntDVo2.setEndDt(endDt+" 12:00:00");
					queryQueue.delete(apAgnApntDVo2);
					
					apAgnApntDVo2 = new ApAgnApntDVo();
					apAgnApntDVo2.setUserUid(apAgnApntDVo.getUserUid());
					apAgnApntDVo2.setAgntUid(apAgnApntDVo.getAgntUid());
					apAgnApntDVo2.setEndDt(endDt+" 23:59:59");
					queryQueue.delete(apAgnApntDVo2);
					
					queryQueue.insert(apAgnApntDVo);
				} else {
					queryQueue.update(apAgnApntDVo);
				}
				
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.AP_AGNT);
				
				commonSvc.execute(queryQueue);
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				if(!isAdminPage){
					model.put("todo", "parent.reload();");
				} else {
					model.put("todo", "parent.setUserPopTab('agnApnt'); parent.dialog.close('setAgnApntDialog');");
				}
			}
			
		} catch(Exception e){
			model.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		return LayoutUtil.getResultJsp();
	}
	/** [AJAX] 대리지정 - 삭제 */
	@RequestMapping(value = {"/ap/env/transAgnApntDelAjx", "/pt/adm/org/transAgnApntDelAjx", "/pt/adm/user/transAgnApntDelAjx"})
	public String transAgnApntDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
		apAgnApntDVo.setUserUid(userUid);
		apAgnApntDVo.setAgntUid((String)jsonObject.get("agntUid"));
		apAgnApntDVo.setSeq((String)jsonObject.get("seq"));
		
		if(commonSvc.delete(apAgnApntDVo)>0){
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
		} else {
			//cm.msg.del.noData=삭제할 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.noData", request));
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [AJAX] 대결자 추가/삭제 */
	@RequestMapping(value = {"/ap/env/getAgnApntAjx", "/pt/adm/org/getAgnApntAjx", "/pt/adm/user/getAgnApntAjx"})
	public String getAgnApntAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="userUid", required=false) String userUid,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
			userUid = userVo.getUserUid();
		}
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
		apAgnApntDVo.setUserUid(userUid);
		apAgnApntDVo.setAgntUid((String)jsonObject.get("agntUid"));
		apAgnApntDVo.setSeq((String)jsonObject.get("seq"));
		
		apAgnApntDVo = (ApAgnApntDVo)commonSvc.queryVo(apAgnApntDVo);
		if(apAgnApntDVo!=null){
			model.put("apAgnApntDVo", apAgnApntDVo);
		} else {
			//cm.msg.noData=해당하는 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.noData", request));
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	///////////////////////////////////////////////////
	//
	//  결재정보 - 결재 경로 설정
	
	/** 결재정보 [탭] 결재 경로 설정 */
	@RequestMapping(value = "/ap/env/setApvApvLnFrm")
	public String setApvApvLnFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/env/setApvApvLnFrm");
	}
	
	///////////////////////////////////////////////////
	//
	//  결재정보 - 수신처 설정
	
	/** 결재정보 [탭] 수신처 설정 */
	@RequestMapping(value = "/ap/env/setApvDestFrm")
	public String setApvDestFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/env/setApvDestFrm");
	}
	
	///////////////////////////////////////////////////
	//
	//  부서정보 - 부서정보
	
	/** 부서정보 */
	@RequestMapping(value = "/ap/env/setOrgEnv")
	public String setOrgEnv(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		if(orgId!=null && !orgId.isEmpty()){
			model.put("orgParam", "&orgId="+orgId);
		}
		
		return LayoutUtil.getJspPath("/ap/env/setOrgEnv");
	}

	/** 부서정보 - 부서정보[탭] */
	@RequestMapping(value = {"/ap/env/setOrgInfoFrm", "/pt/adm/org/setOrgInfoFrm"})
	public String setOrgInfoFrm(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 결재 환경설정 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 조직도 DB 동기화 사용 여부
			if("Y".equals(sysPlocMap.get("orgDbSyncEnable"))){
				model.put("orgSyncEnable", Boolean.TRUE);
			}
		} else {
			// 결재의 환경설정 페이지
			orgId = userVo.getDeptId();
		}
		
		// 조직이력관계(OR_ORG_HST_R) 테이블
		OrOrgHstRVo orOrgHstRVo = new OrOrgHstRVo();
		orOrgHstRVo.setOrgId(orgId);
		orOrgHstRVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrOrgHstRVo> orOrgHstRVoList = (List<OrOrgHstRVo>)commonSvc.queryList(orOrgHstRVo);
		if(orOrgHstRVoList != null && !orOrgHstRVoList.isEmpty()){
			StringBuilder orgHisOrgNms = new StringBuilder();
			StringBuilder orgHisOrgIds = new StringBuilder();
			StringBuilder orgHisRescIds = new StringBuilder();
			
			boolean first = true;
			for(OrOrgHstRVo storedOrOrgHstRVo : orOrgHstRVoList){
				if(first) first = false;
				else {
					orgHisOrgNms.append(", ");
					orgHisOrgIds.append(",");
					orgHisRescIds.append(",");
				}
				orgHisOrgNms.append(storedOrOrgHstRVo.getHstRescNm());
				orgHisOrgIds.append(storedOrOrgHstRVo.getHstOrgId());
				orgHisRescIds.append(storedOrOrgHstRVo.getHstRescId());
			}
			model.put("orgHisOrgNms", orgHisOrgNms.toString());
			model.put("orgHisOrgIds", orgHisOrgIds.toString());
			model.put("orgHisRescIds", orgHisRescIds.toString());
		}
		
		orCmSvc.setOrgDetl(orgId, langTypCd, model);
		return LayoutUtil.getJspPath("/ap/env/setOrgInfoFrm");
	}
	/** [히트프레임] 부서정보 - 저장 */
	@RequestMapping(value = {"/ap/env/transOrgInfo", "/pt/adm/org/transOrgInfo"})
	public String transOrgInfo(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
			orgId = userVo.getDeptId();
		}
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			// 조직연락처상세(OR_ORG_CNTC_D) 테이블 - 저장
			OrOrgCntcDVo orOrgCntcDVo = new OrOrgCntcDVo();
			VoUtil.bind(request, orOrgCntcDVo);
			orOrgCntcDVo.setOrgId(orgId);
			queryQueue.store(orOrgCntcDVo);
			
			// 조직결재상세(OR_ORG_APV_D) 테이블 - 저장
			OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
			VoUtil.bind(request, orOrgApvDVo);
			orOrgApvDVo.setOrgId(orgId);
			
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			OrRescBVo orRescBVo;
			
			// 부서장 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			orRescBVo = orCmSvc.collectOrRescBVo(request, queryQueue, "hodpPosit", langTypCdList);
			if(orRescBVo!=null) orOrgApvDVo.setHodpPositRescId(orRescBVo.getRescId());
			
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(orOrgApvDVo.getOrgId());
			
			// 시스템 조직 여부 - 동기화 제외 대상 여부
			String sysOrgYn = request.getParameter("sysOrgYn");
			if(sysOrgYn==null) sysOrgYn = "N";
			orOrgBVo.setSysOrgYn(sysOrgYn);
			
			// 조직약어 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			orRescBVo = orCmSvc.collectOrRescBVo(request, queryQueue, "orgAbbr", langTypCdList);
			if(orRescBVo!=null){
				orOrgBVo.setOrgAbbrRescId(orRescBVo.getRescId());
			}
			queryQueue.update(orOrgBVo);
			
			// 발신명의 직위 - 리소스 테이블용 리소스 데이터 모으고, 리소스ID 세팅
			orRescBVo = orCmSvc.collectOrRescBVo(request, queryQueue, "sendrNm", langTypCdList);
			if(orRescBVo!=null) orOrgApvDVo.setSendrNmRescId(orRescBVo.getRescId());
			
			// 조직결재상세(OR_ORG_APV_D) 테이블 - 조회
			OrOrgApvDVo storedOrOrgApvDVo = new OrOrgApvDVo();
			storedOrOrgApvDVo.setOrgId(orgId);
			storedOrOrgApvDVo.setQueryLang(langTypCd);
			storedOrOrgApvDVo = (OrOrgApvDVo)commonSvc.queryVo(storedOrOrgApvDVo);
			
			// 이전에 저장된 데이터가 없으면 - insert
			if(storedOrOrgApvDVo==null){
				if(orOrgApvDVo.getInspYn()==null) orOrgApvDVo.setInspYn("N");
				queryQueue.insert(orOrgApvDVo);
			} else {
				queryQueue.update(orOrgApvDVo);
			}
			
			// 문서과 정보, 대리문서과 정보가 같이 있으면 - 대리문서과는 문서과 데이터에 세팅함
			if(orOrgApvDVo.getCrdOrgId()!=null && !orOrgApvDVo.getCrdOrgId().isEmpty()){
				if(orOrgApvDVo.getAgnCrdOrgId()!=null && !orOrgApvDVo.getAgnCrdOrgId().isEmpty()){
					orOrgApvDVo.setRealCrdOrgId(orOrgApvDVo.getAgnCrdOrgId());
					// - 대리문서과를 - 문서과의 대리문서과 컬럼에 저장하던 것을 기관의 대리문서과에 저장하는 것으로 변경
					
//					// 문서과의 데이터에 대리문서과 세팅
//					OrOrgApvDVo orOrgApvDVo2 = new OrOrgApvDVo();
//					orOrgApvDVo2.setOrgId(orOrgApvDVo.getCrdOrgId());
//					if(commonSvc.count(orOrgApvDVo2)>0){
//						orOrgApvDVo2.setAgnCrdOrgId(orOrgApvDVo.getAgnCrdOrgId());
//						orOrgApvDVo2.setAgnCrdRescId(orOrgApvDVo.getAgnCrdRescId());
//						queryQueue.update(orOrgApvDVo2);
//					} else {
//						orOrgApvDVo2.setAgnCrdOrgId(orOrgApvDVo.getAgnCrdOrgId());
//						orOrgApvDVo2.setAgnCrdRescId(orOrgApvDVo.getAgnCrdRescId());
//						orOrgApvDVo2.setInspYn("N");
//						queryQueue.insert(orOrgApvDVo2);
//					}
//					// 현재 submit 데이터의 대리문서과 삭제
//					orOrgApvDVo.setAgnCrdOrgId("");
//					orOrgApvDVo.setAgnCrdRescId("");
				} else {
					orOrgApvDVo.setRealCrdOrgId(orOrgApvDVo.getCrdOrgId());
					if(orOrgApvDVo.getAgnCrdRescId()!=null && !orOrgApvDVo.getAgnCrdRescId().isEmpty()){
						orOrgApvDVo.setAgnCrdRescId("");
					}
				}
			} else {
				orOrgApvDVo.setRealCrdOrgId("");
				orOrgApvDVo.setCrdOrgId("");
				orOrgApvDVo.setCrdRescId("");
				orOrgApvDVo.setAgnCrdOrgId("");
				orOrgApvDVo.setAgnCrdRescId("");
			}
			
			if(orOrgApvDVo.getRealCrdOrgId()!=null && !orOrgApvDVo.getRealCrdOrgId().isEmpty()){
				
				// 문서과 또는 대리문서과로 해당 부서를 지정 한 경우 - 해제함
				OrOrgApvDVo checkOrOrgApvDVo = new OrOrgApvDVo();
				checkOrOrgApvDVo.setRealCrdOrgId(orOrgApvDVo.getAgnCrdOrgId());
				@SuppressWarnings("unchecked")
				List<OrOrgApvDVo> orOrgApvDVoList = (List<OrOrgApvDVo>)commonSvc.queryList(checkOrOrgApvDVo);
				if(orOrgApvDVoList != null){
					for(OrOrgApvDVo updateOrOrgApvDVo : orOrgApvDVoList){
						
						if(updateOrOrgApvDVo.getOrgId().equals(orgId)) continue;
						
						checkOrOrgApvDVo = new OrOrgApvDVo();
						checkOrOrgApvDVo.setOrgId(updateOrOrgApvDVo.getOrgId());
						
						// 대리문서과와 일치하고, 문서과가 있으면
						if(orOrgApvDVo.getRealCrdOrgId().equals(updateOrOrgApvDVo.getAgnCrdOrgId())
								&& updateOrOrgApvDVo.getCrdOrgId()!=null && !updateOrOrgApvDVo.getCrdOrgId().isEmpty()) {
							// 대리문서과 해제, 문서과 유지
							checkOrOrgApvDVo.setRealCrdOrgId(updateOrOrgApvDVo.getAgnCrdOrgId());
							checkOrOrgApvDVo.setAgnCrdOrgId("");
							checkOrOrgApvDVo.setAgnCrdRescId("");
							queryQueue.update(checkOrOrgApvDVo);
							
						} else {
							// 문서과, 대리문서과 해제
							checkOrOrgApvDVo.setRealCrdOrgId("");
							checkOrOrgApvDVo.setCrdOrgId("");
							checkOrOrgApvDVo.setCrdRescId("");
							checkOrOrgApvDVo.setAgnCrdOrgId("");
							checkOrOrgApvDVo.setAgnCrdRescId("");
							queryQueue.update(checkOrOrgApvDVo);
						}
					}
				}
			}
			
			
			// - 대리문서과를 - 문서과의 대리문서과 컬럼에 저장하던 것을 기관의 대리문서과에 저장하는 것으로 변경
//			// 저장 되어 있던 문서과와 submit 된 문서과가 다르면 - 저장된 문서과의 대리 문서과를 지워줌
//			if(storedOrOrgApvDVo!=null && storedOrOrgApvDVo.getCrdOrgId()!=null && !storedOrOrgApvDVo.getCrdOrgId().isEmpty()){
//				if(!storedOrOrgApvDVo.getCrdOrgId().equals(orOrgApvDVo.getCrdOrgId())){
//					OrOrgApvDVo orOrgApvDVo2 = new OrOrgApvDVo();
//					orOrgApvDVo2.setOrgId(storedOrOrgApvDVo.getCrdOrgId());
//					orOrgApvDVo2.setAgnCrdOrgId("");
//					orOrgApvDVo2.setAgnCrdRescId("");
//					queryQueue.update(orOrgApvDVo2);
//				}
//			}
			
			// 부서 이력 - 조직이력관계(OR_ORG_HST_R) 테이블
			String orgHisOrgIds  = request.getParameter("orgHisOrgIds");
			String orgHisRescIds = request.getParameter("orgHisRescIds");
			if(orgHisOrgIds != null && orgHisRescIds != null){
				
				OrOrgHstRVo orOrgHstRVo = new OrOrgHstRVo();
				orOrgHstRVo.setOrgId(orgId);
				queryQueue.delete(orOrgHstRVo);
				
				if(!orgHisOrgIds.isEmpty() && !orgHisRescIds.isEmpty()){
					
					String[] hisOrgIds  = orgHisOrgIds.split(",");
					String[] hisRescIds = orgHisRescIds.split(",");
					
					int i, size = Math.min(hisOrgIds.length, hisRescIds.length);
					for(i=0;i<size;i++){
						if(hisOrgIds[i].equals(orgId)) continue;
						
						orOrgHstRVo = new OrOrgHstRVo();
						orOrgHstRVo.setOrgId(orgId);
						orOrgHstRVo.setHstOrgId(hisOrgIds[i].trim());
						orOrgHstRVo.setHstRescId(hisRescIds[i].trim());
						queryQueue.insert(orOrgHstRVo);
					}
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.ORG);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.ORG);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e){
			model.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	///////////////////////////////////////////////////
	//
	//  부서정보 - 관인/서명인 관리
	
	/** 부서정보 - 관인/서명인[탭] */
	@RequestMapping(value = {"/ap/env/setOrgImgFrm", "/pt/adm/org/setOrgImgFrm"})
	public String setOrgImgFrm(HttpServletRequest request,
			@RequestParam(value = "ofseTypCd", required = false) String ofseTypCd,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			// 관인, 서명인 관리할 부서 목록
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgTreeVo(orgId, langTypCd);
			List<OrOrgTreeVo> orgTreeVoList = new ArrayList<OrOrgTreeVo>();
			orgTreeVoList.add(orOrgTreeVo);
			model.put("orgTreeVoList", orgTreeVoList);
		} else {
			// 결재의 환경설정 페이지
			orgId = userVo.getDeptId();
			// 관인, 서명인 관리할 부서 목록
			List<OrOrgTreeVo> orgTreeVoList = orCmSvc.getOrgListToMngImage(orgId, langTypCd);
			model.put("orgTreeVoList", orgTreeVoList);
		}
		
		// 관인구분코드
		List<PtCdBVo> ofseTypCdList = ptCmSvc.getCdList("OFSE_TYP_CD", langTypCd, "Y");
		model.put("ofseTypCdList", ofseTypCdList);
		
		// 관인, 서명인 목록 조회
		OrOfseDVo orOfseDVo = new OrOfseDVo();
		VoUtil.bind(request, orOfseDVo);
		orOfseDVo.setOrgId(orgId);
		orOfseDVo.setQueryLang(langTypCd);
		orOfseDVo.setOrderBy("OFSE_TYP_CD, SEQ");
		@SuppressWarnings("unchecked")
		List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonSvc.queryList(orOfseDVo);
		model.put("orOfseDVoList", orOfseDVoList);
		
		return LayoutUtil.getJspPath("/ap/env/setOrgImgFrm");
	}
	/** [팝업] 관인/서명인 추가 - 부서이미지[탭] - 추가 버튼 */
	@RequestMapping(value = {"/ap/env/setOrgImgPop", "/pt/adm/org/setOrgImgPop"})
	public String setOrgImgPop(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			@RequestParam(value = "seq", required = false) String seq,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			// 관인, 서명인 관리할 부서 목록
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgTreeVo(orgId, langTypCd);
			List<OrOrgTreeVo> orgTreeVoList = new ArrayList<OrOrgTreeVo>();
			orgTreeVoList.add(orOrgTreeVo);
			model.put("orgTreeVoList", orgTreeVoList);
		} else {
			// 결재의 환경설정 페이지
			orgId = userVo.getDeptId();
			// 관인, 서명인 관리할 부서 목록
			List<OrOrgTreeVo> orgTreeVoList = orCmSvc.getOrgListToMngImage(orgId, langTypCd);
			model.put("orgTreeVoList", orgTreeVoList);
		}
		
		if(orgId!=null && !orgId.isEmpty() && seq!=null && !seq.isEmpty()){
			
			// 관인 데이터 조회
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			orOfseDVo.setOrgId(orgId);
			orOfseDVo.setSeq(seq);
			orOfseDVo.setQueryLang(langTypCd);
			orOfseDVo = (OrOfseDVo)commonSvc.queryVo(orOfseDVo);
			
			if(orOfseDVo != null){
				
				model.put("orOfseDVo", orOfseDVo);
				// 리소스 조회
				OrRescBVo orRescBVo = new OrRescBVo();
				orRescBVo.setRescId(orOfseDVo.getRescId());
				@SuppressWarnings("unchecked")
				List<OrRescBVo> orRescBVoList = (List<OrRescBVo>)commonSvc.queryList(orRescBVo);
				if(orRescBVoList!=null){
					for(OrRescBVo storedOrRescBVo : orRescBVoList){
						model.put(storedOrRescBVo.getRescId()+"_"+storedOrRescBVo.getLangTypCd(), storedOrRescBVo.getRescVa());
					}
				}
			}
		}
		
		// 관인구분코드
		List<PtCdBVo> ofseTypCdList = ptCmSvc.getCdList("OFSE_TYP_CD", langTypCd, "Y");
		model.put("ofseTypCdList", ofseTypCdList);
		
		return LayoutUtil.getJspPath("/ap/pop/setOrgImgPop");
	}
	/** [AJAX] 관인/서명인 상세 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/ap/env/getOrgImgDetlAjx", "/pt/adm/org/getOrgImgDetlAjx"})
	public String getOrgImgDetlAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		OrOfseDVo orOfseDVo = new OrOfseDVo();
		VoUtil.fromMap(orOfseDVo, jsonObject);
		if(orOfseDVo.getOrgId()!=null && orOfseDVo.getSeq()!=null){
			orOfseDVo = (OrOfseDVo)commonSvc.queryVo(orOfseDVo);
			if(orOfseDVo!=null) model.put("vo", orOfseDVo);
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [히든프레임] 관인/서명인 추가 - 저장 */
	@RequestMapping(value = {"/ap/env/transOrgImage", "/pt/adm/org/transOrgImage"})
	public String transOrgImage(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isAdminPage = false;
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			isAdminPage = true;
		} else {
			// 결재의 환경설정 페이지
		}

		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "ap");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			
			String orgId = paramMap.get("orgId");
			String ofseTypCd = paramMap.get("ofseTypCd");//관인구분코드 - 01:관인(기관), 02:서명인(부서)
			
			DistHandler distHandler = distManager.createHandler("images/upload/or/org", locale);//업로드 경로 설정
			String distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("image"));// file-tag 의 name
			distHandler.distribute();
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 관인상세(OR_OFSE_D) 테이블
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			VoUtil.fromMap(orOfseDVo, paramMap);
			
			File imgFile = fileMap.get("image");
			if(imgFile!=null){
				BufferedImage bimg = ImageIO.read(imgFile);
				orOfseDVo.setImgWdth(Integer.toString(bimg.getWidth()));
				orOfseDVo.setImgHght(Integer.toString(bimg.getHeight()));
				orOfseDVo.setImgPath(distPath);
			}
			
			if("Y".equals(orOfseDVo.getDftOfseYn())){
				// 디폴트 이전의 관인들 디폴트 해제
				OrOfseDVo storedOrOfseDVo = new OrOfseDVo();
				storedOrOfseDVo.setOrgId(orgId);
				storedOrOfseDVo.setOfseTypCd(ofseTypCd);
				storedOrOfseDVo.setDftOfseYn("N");
				queryQueue.update(storedOrOfseDVo);
			} else {
				// 디폴트로 관인으로 선택된 것이 없으면 - 디폴트 관인으로 설정
				OrOfseDVo storedOrOfseDVo = new OrOfseDVo();
				storedOrOfseDVo.setOrgId(orgId);
				storedOrOfseDVo.setOfseTypCd(ofseTypCd);
				storedOrOfseDVo.setDisuYn("N");
				storedOrOfseDVo.setDftOfseYn("Y");
				// 디폴트로 관인으로 선택된 갯수
				int dftCount = commonSvc.count(storedOrOfseDVo);
				if(dftCount==0) orOfseDVo.setDftOfseYn("Y");
			}
			
			orOfseDVo.setRegrUid(userVo.getUserUid());
			orOfseDVo.setRegDt("sysdate");
			
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 리소스기본(OR_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장
			OrRescBVo orRescBVo = orCmSvc.collectOrRescBVo(paramMap, userVo.getCompId(), langTypCd, queryQueue);
			orOfseDVo.setRescId(orRescBVo.getRescId());
			orOfseDVo.setOfseNm(orRescBVo.getRescVa());
			
			String seq = paramMap.get("seq");
			if(seq == null || seq.isEmpty()) {
				orOfseDVo.setDisuYn("N");
				queryQueue.insert(orOfseDVo);
				seq = "last";
			} else {
				queryQueue.update(orOfseDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			String orgParamForUser = "";
			if(!isAdminPage){
				orgParamForUser = "&orgId="+orgId;
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setOrgImgDialog'); parent.setDetl('Img', '"+orgParamForUser+"&selectedSeq="+seq+"');");
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
	/** [팝업] 관인/서명인 폐기 - 소버튼 */
	@RequestMapping(value = {"/ap/env/setOrgImgDisuPop", "/pt/adm/org/setOrgImgDisuPop"})
	public String setOrgImgDisuPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
		} else {
			// 결재의 환경설정 페이지
		}
		return LayoutUtil.getJspPath("/ap/pop/setOrgImgDisuPop");
	}
	/** [히든프레임] 관인/서명인 폐기 - 저장 */
	@RequestMapping(value = {"/ap/env/transOrgImgDisu", "/pt/adm/org/transOrgImgDisu"})
	public String transOrgImgDisu(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		boolean isAdminPage = false;
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			isAdminPage = true;
		} else {
			// 결재의 환경설정 페이지
		}
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 관인상세(OR_OFSE_D) 테이블
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			VoUtil.bind(request, orOfseDVo);
			orOfseDVo.setDisuYn("Y");
			orOfseDVo.setDftOfseYn("N");
			orOfseDVo.setDisuDt("sysdate");
			orOfseDVo.setDisurUid(userVo.getUserUid());
			queryQueue.update(orOfseDVo);
			
			String orgId = orOfseDVo.getOrgId();
			String seq = orOfseDVo.getSeq();
			
			// 폐기될 이미지가 기본 이미지 인 경우
			OrOfseDVo storedOrOfseDVo = new OrOfseDVo();
			storedOrOfseDVo.setOrgId(orOfseDVo.getOrgId());
			storedOrOfseDVo.setSeq(orOfseDVo.getSeq());
			storedOrOfseDVo = (OrOfseDVo)commonSvc.queryVo(storedOrOfseDVo);
			if(storedOrOfseDVo!=null && "Y".equals(storedOrOfseDVo.getDftOfseYn())){
				
				// 폐기될 이미지 구분의 기본 이미지가 아닌 것의 목록 조회 후
				orOfseDVo = new OrOfseDVo();
				orOfseDVo.setOrgId(storedOrOfseDVo.getOrgId());
				orOfseDVo.setOfseTypCd(storedOrOfseDVo.getOfseTypCd());
				orOfseDVo.setDisuYn("N");
				orOfseDVo.setDftOfseYn("N");
				orOfseDVo.setOrderBy("SEQ");
				@SuppressWarnings("unchecked")
				List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonSvc.queryList(orOfseDVo);
				
				// 해당 이미지가 있으면 디폴트로 전환함
				if(orOfseDVoList!=null && !orOfseDVoList.isEmpty()){
					orOfseDVo = orOfseDVoList.get(0);
					storedOrOfseDVo = new OrOfseDVo();
					storedOrOfseDVo.setOrgId(orOfseDVo.getOrgId());
					storedOrOfseDVo.setSeq(orOfseDVo.getSeq());
					storedOrOfseDVo.setDftOfseYn("Y");
					queryQueue.update(storedOrOfseDVo);
				}
			}
			
			commonSvc.execute(queryQueue);
			
			String orgParamForUser = "";
			if(!isAdminPage){
				orgParamForUser = "&orgId="+orgId;
			}
			
			//cm.msg.disu.success=폐기 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.disu.success", request));
			model.put("todo", "parent.dialog.close('disuseOrgImgDialog'); parent.setDetl('Img', '"+orgParamForUser+"&selectedSeq="+seq+"');");
//		} catch(CmException e){
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	/** [AJAX] 관인/서명인 폐기 취소 - 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/ap/env/transOrgImgCancelDisuAjx", "/pt/adm/org/transOrgImgCancelDisuAjx"})
	public String transOrgImgCancelDisuAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		boolean isAdminPage = false;
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			isAdminPage = true;
		} else {
			// 결재의 환경설정 페이지
		}
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			// 관인상세(OR_OFSE_D) 테이블
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			VoUtil.fromMap(orOfseDVo, jsonObject);
			orOfseDVo.setDisuYn("N");
			orOfseDVo.setDisuDt("");
			orOfseDVo.setChnRson("");
			orOfseDVo.setDisurUid("");
			queryQueue.update(orOfseDVo);
			
			// 폐기 취소될 데이터 조회
			OrOfseDVo storedOrOfseDVo = new OrOfseDVo();
			storedOrOfseDVo.setOrgId(orOfseDVo.getOrgId());
			storedOrOfseDVo.setSeq(orOfseDVo.getSeq());
			storedOrOfseDVo = (OrOfseDVo)commonSvc.queryVo(storedOrOfseDVo);
			
			// 폐기 취소될 이미지 타입의 기본 이미지가 지정되지 않은 경우
			OrOfseDVo countOrOfseDVo = new OrOfseDVo();
			countOrOfseDVo.setOrgId(storedOrOfseDVo.getOrgId());
			countOrOfseDVo.setOfseTypCd(storedOrOfseDVo.getOfseTypCd());
			countOrOfseDVo.setDftOfseYn("Y");
			int dftCount = commonSvc.count(countOrOfseDVo);
			
			if(dftCount == 0){
				orOfseDVo.setDftOfseYn("Y");
			}
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.cancelDisu.success=폐기 취소 하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.cancelDisu.success", request));
			if(!isAdminPage){
				model.put("result", "ok");
			} else {
				model.put("result", "okAdmin");
			}
			
//		} catch(CmException e){
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return LayoutUtil.returnJson(model);
	}
	/** [AJAX] 관인/서명인 - 기본이미지로 설정 - 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/ap/env/transOrgImgSetAsDftAjx", "/pt/adm/org/transOrgImgSetAsDftAjx"})
	public String transOrgImgSetAsDftAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		boolean isAdminPage = false;
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			isAdminPage = true;
		} else {
			// 결재의 환경설정 페이지
		}
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
			// 관인상세(OR_OFSE_D) 테이블
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			VoUtil.fromMap(orOfseDVo, jsonObject);
			
			// 기본이미지 설정할 데이터 조회
			OrOfseDVo storedOrOfseDVo = new OrOfseDVo();
			storedOrOfseDVo.setOrgId(orOfseDVo.getOrgId());
			storedOrOfseDVo.setSeq(orOfseDVo.getSeq());
			storedOrOfseDVo = (OrOfseDVo)commonSvc.queryVo(storedOrOfseDVo);
			
			// 기존의 기본 이미지 설정 해제
			OrOfseDVo dftOrOfseDVo = new OrOfseDVo();
			dftOrOfseDVo.setOrgId(storedOrOfseDVo.getOrgId());
			dftOrOfseDVo.setOfseTypCd(storedOrOfseDVo.getOfseTypCd());
			dftOrOfseDVo.setDftOfseYn("N");
			queryQueue.update(dftOrOfseDVo);
			
			// submit 된 데이터 기본 이미지 설정
			orOfseDVo.setDftOfseYn("Y");
			queryQueue.update(orOfseDVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.setting.success=설정 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.setting.success", request));
			if(!isAdminPage){
				model.put("result", "ok");
			} else {
				model.put("result", "okAdmin");
			}
//		} catch(CmException e){
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return LayoutUtil.returnJson(model);
	}
	
	///////////////////////////////////////////////////
	//
	//  부서정보 - 분류 정보
	
	/** 부서정보 - 분류정보[탭] */
	@RequestMapping(value = {
			"/pt/adm/org/treeOrgClsFrm", "/pt/adm/org/treeOrgClsPop",
			"/ap/env/treeOrgClsFrm", "/ap/env/treeOrgClsPop",
			"/ap/box/treeOrgClsFrm", "/ap/box/treeOrgClsPop"})
	public String treeOrgClsFrm(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			@RequestParam(value = "noCls", required = false) String noCls,
			@RequestParam(value = "withSecul", required = false) String withSecul,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		if(request.getRequestURI().startsWith("/pt/adm/")){
			// 포털의 조직 관리 페이지
			request.setAttribute("adminPage", Boolean.TRUE);
			model.put("orgParam", "&orgId="+orgId);
		} else {
			// 결재의 환경설정 페이지
			orgId = userVo.getDeptId();
		}
		
		ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
		apClsInfoDVo.setOrgId(orgId);
		apClsInfoDVo.setQueryLang(langTypCd);
		String uri = request.getRequestURI();
		// 일반 사용자 - 분류정보 선택 팝업
		if(uri.startsWith("/ap/box/treeOrgClsPop")){
			apClsInfoDVo.setUseYn("Y");
			request.setAttribute("endUserPage", Boolean.TRUE);
			
		} else if(uri.startsWith("/ap/env/treeOrgClsPop") || uri.startsWith("/pt/adm/org/treeOrgClsPop")){
			request.setAttribute("endUserPage", Boolean.TRUE);
			// tree ID 가 중복되어 발생하는 스크립트 오동작 방지 
			request.setAttribute("treeId", "clsPopTree");
			
		// 등록대장, 접수대장에서 분류정보 프레임
		} else if(uri.startsWith("/ap/box/treeOrgClsFrm")){
			request.setAttribute("forRecLst", Boolean.TRUE);
			request.setAttribute("callback", "clickClsInfo");
		}
		
		if(!"Y".equals(noCls)){
			@SuppressWarnings("unchecked")
			List<ApClsInfoDVo> apClsInfoDVoList = (List<ApClsInfoDVo>)commonSvc.queryList(apClsInfoDVo);
			if(apClsInfoDVoList !=null && !apClsInfoDVoList.isEmpty()){
				for(ApClsInfoDVo storedApClsInfoDVo : apClsInfoDVoList){
					// 부모ID 가 부서ID 이면 부모ID를 "ROOT"로 변환 - 스크립트 처리용
					if(orgId.equals(storedApClsInfoDVo.getClsInfoPid())){
						storedApClsInfoDVo.setClsInfoPid("ROOT");
					}
				}
				model.put("apClsInfoDVoList", apClsInfoDVoList);
			}
		}
		
		// 보안등급 설정 이면
		if("Y".equals(withSecul)){
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 회사별 보안등급코드 사용
			if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
				// 보안등급코드
				List<PtCdBVo> seculCdList = ptCmSvc.getCdListEqCompId("SECUL_CD", langTypCd, userVo.getCompId(), "Y");
				model.put("seculCdList", seculCdList);
			} else {
				// 보안등급코드
				List<PtCdBVo> seculCdList = ptCmSvc.getCdListByCompId("SECUL_CD", langTypCd, userVo.getCompId(), "Y");
				model.put("seculCdList", seculCdList);
			}
		}
		if(uri.indexOf("Pop.do")>0){
			return LayoutUtil.getJspPath("/ap/env/treeOrgClsFrm", "Pop");
		} else {
			return LayoutUtil.getJspPath("/ap/env/treeOrgClsFrm");
		}
	}
	/** 부서정보 - 분류정보[탭] */
	@RequestMapping(value = {"/ap/env/setOrgClsPop", "/pt/adm/org/setOrgClsPop"})
	public String setOrgClsPop(HttpServletRequest request,
			@RequestParam(value = "clsInfoId", required = false) String clsInfoId,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		if(clsInfoId!=null && !clsInfoId.isEmpty()){
			
			UserVo userVo = LoginSession.getUser(request);
			if(request.getRequestURI().startsWith("/pt/adm/")){
				// 포털의 조직 관리 페이지
				request.setAttribute("adminPage", Boolean.TRUE);
			} else {
				// 결재의 환경설정 페이지
				orgId = userVo.getDeptId();
			}
			
			ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
			VoUtil.bind(request, apClsInfoDVo);
			apClsInfoDVo = (ApClsInfoDVo)commonSvc.queryVo(apClsInfoDVo);
			
			if(apClsInfoDVo != null){
				model.put("apClsInfoDVo", apClsInfoDVo);
				
				ApRescBVo apRescBVo = new ApRescBVo();
				apRescBVo.setRescId(apClsInfoDVo.getRescId());
				@SuppressWarnings("unchecked")
				List<ApRescBVo> apRescBVoList = (List<ApRescBVo>)commonSvc.queryList(apRescBVo);
				if(apRescBVoList!=null){
					for(ApRescBVo storedApRescBVo : apRescBVoList){
						model.put(storedApRescBVo.getRescId()+"_"+storedApRescBVo.getLangTypCd(), storedApRescBVo.getRescVa());
					}
				}
			}
		}
		
		return LayoutUtil.getJspPath("/ap/pop/setOrgClsPop");
	}
	/** [히든프레임] 분류정보 - 저장 */
	@RequestMapping(value = {"/ap/env/transOrgCls", "/pt/adm/org/transOrgCls"})
	public String transOrgCls(HttpServletRequest request,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			if(request.getRequestURI().startsWith("/pt/adm/")){
				// 포털의 조직 관리 페이지
				request.setAttribute("adminPage", Boolean.TRUE);
			} else {
				// 결재의 환경설정 페이지
				orgId = userVo.getDeptId();
			}
			boolean isNew = false;
			QueryQueue queryQueue = new QueryQueue();
			
			ApRescBVo apRescBVo = apRescSvc.collectApRescBVo(request, null, queryQueue);
			if(apRescBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 분류정보상세(AP_CLS_INFO_D) 테이블
			ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
			VoUtil.bind(request, apClsInfoDVo);
			apClsInfoDVo.setOrgId(orgId);
			
			// 부모ID 가 ROOT 이면 부모ID를 부서ID로 변환
			if("ROOT".equals(apClsInfoDVo.getClsInfoPid())){
				apClsInfoDVo.setClsInfoPid(orgId);
			}
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
			apClsInfoDVo.setRescId(apRescBVo.getRescId());
			apClsInfoDVo.setClsInfoNm(apRescBVo.getRescVa());
			
			apClsInfoDVo.setModDt("sysdate");
			apClsInfoDVo.setModrUid(userVo.getUserUid());
			
			if(apClsInfoDVo.getClsInfoId() == null){
				// 신규 등록
				isNew = true;
				apClsInfoDVo.setClsInfoId(apCmSvc.createId("AP_CLS_INFO_D"));
				apClsInfoDVo.setRegDt("sysdate");
				apClsInfoDVo.setRegrUid(userVo.getUserUid());
				apClsInfoDVo.setSysClsInfoYn("N");//시스템분류정보여부 - 완결된 문서의 분류정보를 Y로 바꾸어 삭제되지 않도록 함
				queryQueue.insert(apClsInfoDVo);
			} else {
				// 수정
				queryQueue.update(apClsInfoDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			String clsInfoId = isNew ? (orgId.equals(apClsInfoDVo.getClsInfoPid()) ? "ROOT" : apClsInfoDVo.getClsInfoPid()) : apClsInfoDVo.getClsInfoId();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setOrgClsDialog'); parent.setDetl('Cls', '&clsInfoId="+clsInfoId+"');");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	/** [AJAX] 분류정보 - 삭제 */
	@RequestMapping(value = {"/ap/env/transOrgClsDelAjx", "/pt/adm/org/transOrgClsDelAjx"})
	public String transOrgClsDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			if(request.getRequestURI().startsWith("/pt/adm/")){
				// 포털의 조직 관리 페이지
				request.setAttribute("adminPage", Boolean.TRUE);
			} else {
				// 결재의 환경설정 페이지
				orgId = userVo.getDeptId();
			}
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String clsInfoId = (String)jsonObject.get("clsInfoId");
			if(clsInfoId==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 분류정보상세(AP_CLS_INFO_D) 테이블
			ApClsInfoDVo storedApClsInfoDVo = new ApClsInfoDVo();
			storedApClsInfoDVo.setOrgId(orgId);
			storedApClsInfoDVo.setClsInfoId(clsInfoId);
			storedApClsInfoDVo = (ApClsInfoDVo)commonSvc.queryVo(storedApClsInfoDVo);
			
			if(storedApClsInfoDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			} else if("Y".equals(storedApClsInfoDVo.getSysClsInfoYn())){
				// ap.msg.notDel.clsInfo.inUse=사용중인 분류정보는 삭제 할 수 없습니다.
				throw new CmException("ap.msg.notDel.clsInfo.inUse", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			ApRescBVo apRescBVo = new ApRescBVo();
			apRescBVo.setRescId(storedApClsInfoDVo.getRescId());
			queryQueue.delete(apRescBVo);
			
			ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
			apClsInfoDVo.setOrgId(orgId);
			apClsInfoDVo.setClsInfoId(clsInfoId);
			queryQueue.delete(apClsInfoDVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] - 분류 정보 - 트리 순서 변경 */
	@RequestMapping(value = {"/ap/env/transOrgClsMoveAjx", "/pt/adm/org/transOrgClsMoveAjx"})
	public String transOrgClsMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String clsInfoId = (String)jsonObject.get("clsInfoId");
			String direction = (String)jsonObject.get("direction");
			
			UserVo userVo = LoginSession.getUser(request);
			if(request.getRequestURI().startsWith("/pt/adm/")){
				// 포털의 조직 관리 페이지
				request.setAttribute("adminPage", Boolean.TRUE);
			} else {
				// 결재의 환경설정 페이지
				orgId = userVo.getDeptId();
			}
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			if(	direction==null || (!"up".equals(direction) && !"down".equals(direction))
				|| clsInfoId==null || clsInfoId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				if(direction==null || (!"up".equals(direction) && !"down".equals(direction))){
					LOGGER.error("Org move(up/down) - direction==null  : "+message);
				} else {
					LOGGER.error("Org move(up/down) - direction:"+direction+"  orgId:"+orgId+"  clsInfoId:"+clsInfoId+" : "+message);
				}
				throw new CmException(message);
			}
			
			ApClsInfoDVo apClsInfoDVo, storedApClsInfoDVo;
			
			// curOrdr - 현재순번
			// switchOrdr - 바꿀 순번
			Integer curOrdr, switchOrdr;
			String clsInfoPid=null;
			
			apClsInfoDVo = new ApClsInfoDVo();
			apClsInfoDVo.setOrgId(orgId);
			apClsInfoDVo.setClsInfoId(clsInfoId);
			storedApClsInfoDVo = (ApClsInfoDVo)commonSvc.queryVo(apClsInfoDVo);
			
			if(storedApClsInfoDVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(AP_CLS_INFO_D) - orgId:"+orgId+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			curOrdr = Integer.valueOf(storedApClsInfoDVo.getSortOrdr());
			clsInfoPid = storedApClsInfoDVo.getClsInfoPid();
			
			// 위로 이동
			if("up".equals(direction)){
				
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_CLS_INFO_D");
				ptxSortOrdrChnVo.setPkCol("ORG_ID");
				ptxSortOrdrChnVo.setPk(orgId);
				ptxSortOrdrChnVo.setPkCol2("CLS_INFO_PID");
				ptxSortOrdrChnVo.setPk2(clsInfoPid);
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				storedApClsInfoDVo = new ApClsInfoDVo();
				storedApClsInfoDVo.setOrgId(orgId);
				storedApClsInfoDVo.setClsInfoId(clsInfoId);
				storedApClsInfoDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedApClsInfoDVo);
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				
				// 아래로 이동
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_CLS_INFO_D");
				ptxSortOrdrChnVo.setPkCol("ORG_ID");
				ptxSortOrdrChnVo.setPk(orgId);
				ptxSortOrdrChnVo.setPkCol2("CLS_INFO_PID");
				ptxSortOrdrChnVo.setPk2(clsInfoPid);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				if(maxSortOrdr>curOrdr){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_CLS_INFO_D");
					ptxSortOrdrChnVo.setPkCol("ORG_ID");
					ptxSortOrdrChnVo.setPk(orgId);
					ptxSortOrdrChnVo.setPkCol2("CLS_INFO_PID");
					ptxSortOrdrChnVo.setPk2(clsInfoPid);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					storedApClsInfoDVo = new ApClsInfoDVo();
					storedApClsInfoDVo.setOrgId(orgId);
					storedApClsInfoDVo.setClsInfoId(clsInfoId);
					storedApClsInfoDVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(storedApClsInfoDVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
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
	
	/** [AJAX] - 분류 정보 - 이동 : 하위로 옮기기 */
	@RequestMapping(value = {"/ap/env/transOrgClsCutPasteAjx", "/pt/adm/org/transOrgClsCutPasteAjx"})
	public String transOrgClsCutPasteAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			@RequestParam(value = "orgId", required = false) String orgId,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String from = (String)jsonObject.get("from");
			String to = (String)jsonObject.get("to");
			
			UserVo userVo = LoginSession.getUser(request);
			if(request.getRequestURI().startsWith("/pt/adm/")){
				// 포털의 조직 관리 페이지
				//request.setAttribute("adminPage", Boolean.TRUE);
			} else {
				// 결재의 환경설정 페이지
				orgId = userVo.getDeptId();
			}
			if("ROOT".equals(to)) to = orgId;
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			ApClsInfoDVo fromApClsInfoDVo, toApClsInfoDVo;
			
			fromApClsInfoDVo = new ApClsInfoDVo();
			fromApClsInfoDVo.setOrgId(orgId);
			fromApClsInfoDVo.setClsInfoId(from);
			fromApClsInfoDVo = (ApClsInfoDVo)commonSvc.queryVo(fromApClsInfoDVo);
			
			if(fromApClsInfoDVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(AP_PSN_CLS_INFO_D) - orgId:"+orgId+", clsInfoId:"+from+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			// from 에 위치에서 아래의 것 정렬순서 하나씩 위로 (-1)
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("AP_CLS_INFO_D");
			ptxSortOrdrChnVo.setPkCol("ORG_ID");
			ptxSortOrdrChnVo.setPk(orgId);
			ptxSortOrdrChnVo.setPkCol2("CLS_INFO_PID");
			ptxSortOrdrChnVo.setPk2(fromApClsInfoDVo.getClsInfoPid());
			ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(fromApClsInfoDVo.getSortOrdr()) + 1);
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			queryQueue.update(ptxSortOrdrChnVo);
			
			// to 하위의 최대 정렬순서 구하기
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("AP_CLS_INFO_D");
			ptxSortOrdrChnVo.setPkCol("ORG_ID");
			ptxSortOrdrChnVo.setPk(orgId);
			ptxSortOrdrChnVo.setPkCol2("CLS_INFO_PID");
			ptxSortOrdrChnVo.setPk2(to);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// 해당 데이타 옮기기
			toApClsInfoDVo = new ApClsInfoDVo();
			toApClsInfoDVo.setOrgId(orgId);
			toApClsInfoDVo.setClsInfoId(from);
			toApClsInfoDVo.setClsInfoPid(to);
			if(!fromApClsInfoDVo.getClsInfoPid().equals(to)){//자신의 부모 디렉토리를 지정한 경우 - 마지막 순번으로 변경
				maxSortOrdr++;
			}
			toApClsInfoDVo.setSortOrdr(maxSortOrdr.toString());
			queryQueue.update(toApClsInfoDVo);
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	///////////////////////////////////////////////////
	//
	//  개인 분류 정보
	
	/** 개인 분류 정보 - 트리 조회 */
	@RequestMapping(value = {"/ap/box/treePsnClsPop", "/ap/box/treePsnClsFrm"})
	public String treePsnClsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		ApPsnClsInfoDVo apPsnClsInfoDVo = new ApPsnClsInfoDVo();
		apPsnClsInfoDVo.setUserUid(userVo.getUserUid());
		apPsnClsInfoDVo.setQueryLang(langTypCd);
		apPsnClsInfoDVo.setSortOrdr("PSN_CLS_INFO_PID, SORT_ORDR");
		
		@SuppressWarnings("unchecked")
		List<ApPsnClsInfoDVo> apPsnClsInfoDVoList = (List<ApPsnClsInfoDVo>)commonSvc.queryList(apPsnClsInfoDVo);
		if(apPsnClsInfoDVoList != null){
			model.put("apPsnClsInfoDVoList", apPsnClsInfoDVoList);
		}
		
		String uri = request.getRequestURI();
		if(uri.startsWith("/ap/box/treePsnClsPop")){
			model.put("isPop", Boolean.TRUE);
			return LayoutUtil.getJspPath("/ap/env/treePsnClsFrm", "Pop");
		} else {
			// 클릭했을 때 - 해당 분류의 목록을 나타나게 하기 위한것
			request.setAttribute("callback", "clickPsnClsInfo");
			return LayoutUtil.getJspPath("/ap/env/treePsnClsFrm");
		}
	}
	
	/** 개인 분류 정보 - 등록/수정 팝업 */
	@RequestMapping(value = "/ap/box/setPsnClsPop")
	public String setPsnClsPop(HttpServletRequest request,
			@RequestParam(value = "psnClsInfoId", required = false) String psnClsInfoId,
			ModelMap model) throws Exception {
		
		if(psnClsInfoId!=null && !psnClsInfoId.isEmpty()){
			
			UserVo userVo = LoginSession.getUser(request);
			String langTypCd = LoginSession.getLangTypCd(request);
			
			ApPsnClsInfoDVo apPsnClsInfoDVo = new ApPsnClsInfoDVo();
			apPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			apPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
			apPsnClsInfoDVo.setQueryLang(langTypCd);
			
			apPsnClsInfoDVo = (ApPsnClsInfoDVo)commonSvc.queryVo(apPsnClsInfoDVo);
			
			if(apPsnClsInfoDVo != null){
				model.put("apPsnClsInfoDVo", apPsnClsInfoDVo);
				
//				ApRescBVo apRescBVo = new ApRescBVo();
//				apRescBVo.setRescId(apPsnClsInfoDVo.getRescId());
//				@SuppressWarnings("unchecked")
//				List<ApRescBVo> apRescBVoList = (List<ApRescBVo>)commonSvc.queryList(apRescBVo);
//				if(apRescBVoList!=null){
//					for(ApRescBVo storedApRescBVo : apRescBVoList){
//						model.put(storedApRescBVo.getRescId()+"_"+storedApRescBVo.getLangTypCd(), storedApRescBVo.getRescVa());
//					}
//				}
			}
		}
		
		return LayoutUtil.getJspPath("/ap/pop/setPsnClsPop");
	}
	/** [히든프레임] 개인 분류 정보  - 저장 */
	@RequestMapping(value = "/ap/box/transPsnCls")
	public String transPsnCls(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			boolean isNew = false;
			QueryQueue queryQueue = new QueryQueue();
			
//			ApRescBVo apRescBVo = apRescSvc.collectApRescBVo(request, null, queryQueue);
//			if(apRescBVo==null){
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
			
			// 분류정보상세(AP_CLS_INFO_D) 테이블
			ApPsnClsInfoDVo apPsnClsInfoDVo = new ApPsnClsInfoDVo();
			VoUtil.bind(request, apPsnClsInfoDVo);
			apPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			
			// 리소스 조회후 리소스의 리소스ID와 리소스명 세팅
//			apPsnClsInfoDVo.setRescId(apRescBVo.getRescId());
//			apPsnClsInfoDVo.setPsnClsInfoNm(apRescBVo.getRescVa());
			
			if(apPsnClsInfoDVo.getPsnClsInfoId() == null){
				// 신규 등록
				isNew = true;
				apPsnClsInfoDVo.setPsnClsInfoId(apCmSvc.createId("AP_PSN_CLS_INFO_D"));
				apPsnClsInfoDVo.setUseYn("Y");
				queryQueue.insert(apPsnClsInfoDVo);
			} else {
				// 수정
				queryQueue.update(apPsnClsInfoDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			String psnClsInfoId = isNew ? apPsnClsInfoDVo.getPsnClsInfoPid() : apPsnClsInfoDVo.getPsnClsInfoId();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setPsnClsDialog'); parent.reloadTree('"+psnClsInfoId+"', 'psn');");
//		} catch(CmException e){
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		return LayoutUtil.getResultJsp();
	}
	/** [AJAX] 개인 분류 정보 - 삭제 */
	@RequestMapping(value = "/ap/box/transPsnClsDelAjx")
	public String transPsnClsDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String psnClsInfoId = (String)jsonObject.get("psnClsInfoId");
			if(psnClsInfoId==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 개인분류정보상세(AP_PSN_CLS_INFO_D) 테이블
			ApPsnClsInfoDVo storedApPsnClsInfoDVo = new ApPsnClsInfoDVo();
			storedApPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			storedApPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
			storedApPsnClsInfoDVo = (ApPsnClsInfoDVo)commonSvc.queryVo(storedApPsnClsInfoDVo);
			
			if(storedApPsnClsInfoDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setPsnClsInfoId(psnClsInfoId);
			if(commonSvc.count(apOngdBVo) > 0){
				// ap.msg.notDel.clsInfo.inUse=사용중인 분류정보는 삭제 할 수 없습니다.
				throw new CmException("ap.msg.notDel.clsInfo.inUse", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
//			ApRescBVo apRescBVo = new ApRescBVo();
//			apRescBVo.setRescId(storedApPsnClsInfoDVo.getRescId());
//			queryQueue.delete(apRescBVo);
			
			ApPsnClsInfoDVo apPsnClsInfoDVo = new ApPsnClsInfoDVo();
			apPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			apPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
			queryQueue.delete(apPsnClsInfoDVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] - 개인 분류 정보 - 트리 순서 변경 */
	@RequestMapping(value = "/ap/box/transPsnClsMoveAjx")
	public String transPsnClsMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String psnClsInfoId = (String)jsonObject.get("psnClsInfoId");
			String direction = (String)jsonObject.get("direction");
			
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			if(	direction==null || (!"up".equals(direction) && !"down".equals(direction))
				|| psnClsInfoId==null || psnClsInfoId.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.notValidCall", request);
				if(direction==null || (!"up".equals(direction) && !"down".equals(direction))){
					LOGGER.error("Org move(up/down) - direction==null  : "+message);
				} else {
					LOGGER.error("Org move(up/down) - direction:"+direction+"  userUid:"+userVo.getUserUid()+"  clsInfoId:"+psnClsInfoId+" : "+message);
				}
				throw new CmException(message);
			}
			
			ApPsnClsInfoDVo apPsnClsInfoDVo, storedApPsnClsInfoDVo;
			
			// curOrdr - 현재순번
			// switchOrdr - 바꿀 순번
			Integer curOrdr, switchOrdr;
			String psnClsInfoPid=null;
			
			apPsnClsInfoDVo = new ApPsnClsInfoDVo();
			apPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			apPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
			storedApPsnClsInfoDVo = (ApPsnClsInfoDVo)commonSvc.queryVo(apPsnClsInfoDVo);
			
			if(storedApPsnClsInfoDVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(AP_PSN_CLS_INFO_D) - userUid:"+userVo.getUserUid()+"  psnClsInfoId:"+psnClsInfoId+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			curOrdr = Integer.valueOf(storedApPsnClsInfoDVo.getSortOrdr());
			psnClsInfoPid = storedApPsnClsInfoDVo.getPsnClsInfoPid();
			
			// 위로 이동
			if("up".equals(direction)){
				
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_PSN_CLS_INFO_D");
				ptxSortOrdrChnVo.setPkCol("USER_UID");
				ptxSortOrdrChnVo.setPk(userVo.getUserUid());
				ptxSortOrdrChnVo.setPkCol2("PSN_CLS_INFO_PID");
				ptxSortOrdrChnVo.setPk2(psnClsInfoPid);
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				ptxSortOrdrChnVo.setLessThen(switchOrdr);
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				queryQueue.update(ptxSortOrdrChnVo);
				
				storedApPsnClsInfoDVo = new ApPsnClsInfoDVo();
				storedApPsnClsInfoDVo.setUserUid(userVo.getUserUid());
				storedApPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
				storedApPsnClsInfoDVo.setSortOrdr(switchOrdr.toString());
				queryQueue.update(storedApPsnClsInfoDVo);
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
				} else {
					//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
					model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
				}
				
				// 아래로 이동
			} else if("down".equals(direction)){
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("AP_PSN_CLS_INFO_D");
				ptxSortOrdrChnVo.setPkCol("USER_UID");
				ptxSortOrdrChnVo.setPk(userVo.getUserUid());
				ptxSortOrdrChnVo.setPkCol2("PSN_CLS_INFO_PID");
				ptxSortOrdrChnVo.setPk2(psnClsInfoPid);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
				Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
				
				if(maxSortOrdr>curOrdr){
					
					switchOrdr = curOrdr+1;
					
					ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
					ptxSortOrdrChnVo.setTabId("AP_PSN_CLS_INFO_D");
					ptxSortOrdrChnVo.setPkCol("USER_UID");
					ptxSortOrdrChnVo.setPk(userVo.getUserUid());
					ptxSortOrdrChnVo.setPkCol2("PSN_CLS_INFO_PID");
					ptxSortOrdrChnVo.setPk2(psnClsInfoPid);
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
					ptxSortOrdrChnVo.setChnVa(-1);
					ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
					queryQueue.update(ptxSortOrdrChnVo);
					
					storedApPsnClsInfoDVo = new ApPsnClsInfoDVo();
					storedApPsnClsInfoDVo.setUserUid(userVo.getUserUid());
					storedApPsnClsInfoDVo.setPsnClsInfoId(psnClsInfoId);
					storedApPsnClsInfoDVo.setSortOrdr(switchOrdr.toString());
					queryQueue.update(storedApPsnClsInfoDVo);
				}
				
				if(!queryQueue.isEmpty()){
					commonSvc.execute(queryQueue);
					model.put("result", "ok");
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
	
	/** [AJAX] - 개인 분류 정보 - 이동 : 하위로 옮기기 */
	@RequestMapping(value = "/ap/box/transPsnClsCutPasteAjx")
	public String transPsnClsCutPasteAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
		
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String from = (String)jsonObject.get("from");
			String to = (String)jsonObject.get("to");
			
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			PtxSortOrdrChnVo ptxSortOrdrChnVo;
			
			ApPsnClsInfoDVo fromApPsnClsInfoDVo, toApPsnClsInfoDVo;
			
			fromApPsnClsInfoDVo = new ApPsnClsInfoDVo();
			fromApPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			fromApPsnClsInfoDVo.setPsnClsInfoId(from);
			fromApPsnClsInfoDVo = (ApPsnClsInfoDVo)commonSvc.queryVo(fromApPsnClsInfoDVo);
			
			if(fromApPsnClsInfoDVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.noData", request);
				model.put("message", msg);
				LOGGER.error("no data(AP_PSN_CLS_INFO_D) - userUid:"+userVo.getUserUid()+", psnClsInfoId:"+from+"  : "+msg);
				return JsonUtil.returnJson(model);
			}
			
			// from 에 위치에서 아래의 것 정렬순서 하나씩 위로 (-1)
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("AP_PSN_CLS_INFO_D");
			ptxSortOrdrChnVo.setPkCol("USER_UID");
			ptxSortOrdrChnVo.setPk(userVo.getUserUid());
			ptxSortOrdrChnVo.setPkCol2("PSN_CLS_INFO_PID");
			ptxSortOrdrChnVo.setPk2(fromApPsnClsInfoDVo.getPsnClsInfoPid());
			ptxSortOrdrChnVo.setMoreThen(Integer.parseInt(fromApPsnClsInfoDVo.getSortOrdr()) + 1);
			ptxSortOrdrChnVo.setChnVa(-1);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
			queryQueue.update(ptxSortOrdrChnVo);
			
			// to 하위의 최대 정렬순서 구하기
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("AP_PSN_CLS_INFO_D");
			ptxSortOrdrChnVo.setPkCol("USER_UID");
			ptxSortOrdrChnVo.setPk(userVo.getUserUid());
			ptxSortOrdrChnVo.setPkCol2("PSN_CLS_INFO_PID");
			ptxSortOrdrChnVo.setPk2(to);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// 해당 데이타 옮기기
			toApPsnClsInfoDVo = new ApPsnClsInfoDVo();
			toApPsnClsInfoDVo.setUserUid(userVo.getUserUid());
			toApPsnClsInfoDVo.setPsnClsInfoId(from);
			toApPsnClsInfoDVo.setPsnClsInfoPid(to);
			if(!fromApPsnClsInfoDVo.getPsnClsInfoPid().equals(to)){//자신의 부모 디렉토리를 지정한 경우 - 마지막 순번으로 변경
				maxSortOrdr++;
			}
			toApPsnClsInfoDVo.setSortOrdr(maxSortOrdr.toString());
			queryQueue.update(toApPsnClsInfoDVo);
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	
	///////////////////////////////////////////////////
	//
	//  양식정보 >> ApAdmFormCtrl
	
}
