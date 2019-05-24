package com.innobiz.orange.web.ap.ctrl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.ap.cust.ApCustFncSvc;
import com.innobiz.orange.web.ap.cust.ApErpNotiSvc;
import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocNoSvc;
import com.innobiz.orange.web.ap.svc.ApDocSecuSvc;
import com.innobiz.orange.web.ap.svc.ApDocSvc;
import com.innobiz.orange.web.ap.svc.ApDocTransSvc;
import com.innobiz.orange.web.ap.svc.ApFormSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApErpFormBVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgChitDVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgFileDVo;
import com.innobiz.orange.web.ap.vo.ApFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormJspDVo;
import com.innobiz.orange.web.ap.vo.ApFormTxtDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.ap.vo.ApOngdErpFormDVo;
import com.innobiz.orange.web.ap.vo.ApOngdExDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPichDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPubBxCnfmLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRefVwDVo;
import com.innobiz.orange.web.ap.vo.ApOngdSendDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormBVo;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.em.vo.CmEmailFileDVo;
import com.innobiz.orange.web.or.ctrl.OrOrgCtrl;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;

/** 문서 컨트롤러 - 결재 */
@Controller
public class ApDocCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocCtrl.class);

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 결재 문서 서비스 */
	@Autowired
	private ApDocSvc apDocSvc;
	
	/** 결재 양식 서비스 */
	@Autowired
	private ApFormSvc apFormSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 결재함 컨트롤러 */
	@Autowired
	private ApBxCtrl apBxCtrl;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 결재 문서 보안 서비스 */
	@Autowired
	private ApDocSecuSvc apDocSecuSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApCustFncSvc apCustFncSvc;
	
	/** 문서 저장 서비스 */
	@Autowired
	private ApDocTransSvc apDocTransSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 결재 약식 관리 Ctrl */
	@Autowired
    private ApAdmFormCtrl apAdmFormCtrl;

	/** 조직 사용자 컨트롤러 */
	@Autowired
	private OrOrgCtrl orOrgCtrl;
	
	/** 문서번호 체번 서비스 */
	@Autowired
	private ApDocNoSvc apDocNoSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** ERP 연계 알림 서비스 */
	@Autowired
	private ApErpNotiSvc apErpNotiSvc;

	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
//	@Autowired
//	private DmStorSvc dmStorSvc;
//
//	@Autowired
//	private DmDocSvc dmDocSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	////////////////////////////////////////////////////////////////////
	//
	//				문서작성 / 문서조회
	/** 작성 및 조회 */
	@RequestMapping(value = {
			"/ap/box/setDoc","/ap/box/viewDoc",
			"/ap/box/viewDocFrm", "/ap/adm/box/viewDocFrm", 
			"/ap/box/viewIntgDoc", "/ap/adm/box/setDoc",
			"/dm/doc/viewApDocFrm", "/dm/adm/doc/viewApDocFrm",
			"/ap/box/viewDocWinPop", "/wd/adm/viewDocWinPop",
			"/ap/self/downloadDoc"})
	public String setDoc(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="vwMode", required=false) String vwMode,// 원문:orgn, 시행변환문:trx
			@Parameter(name="refdBy", required=false) String refdBy,// 문서번호 - 참조문서를 건 문서의 번호 - 권한체크용
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			@Parameter(name="apvLnNo", required=false) String apvLnNo,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="formSeq", required=false) String formSeq,
			@Parameter(name="rejtApvNo", required=false) String rejtApvNo,// 반려문서 재기안 용 - 반려된 결재번호
			@Parameter(name="orgnApvNo", required=false) String orgnApvNo,// 시행문 변환 용 - 원본 결재번호
			@Parameter(name="refDocApvNo", required=false) String refDocApvNo,// 참조기안 용 - 원본 결재번호
			@Parameter(name="sendSeq", required=false) String sendSeq,//발송일련번호 - 진행문서발송상세(AP_ONGD_SEND_D) 테이블 - 배부함,접수함만
			@Parameter(name="pubBxDeptId", required=false) String pubBxDeptId,//공람부서ID
			@Parameter(name="intgNo", required=false) String intgNo,//연계번호 -ERP
			@Parameter(name="apvLnGrpId", required=false) String apvLnGrpId,//ERP 연계 - 개인결재경로그룹ID - 디폴트로 설정해줌
			@Parameter(name="intgTypCd", required=false) String intgTypCd,//ERP 연계 - 연계구분코드 - ERP_CHIT:전표보기(프라코)
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="queryString", required=false) String queryString,// 목록에서 넘겨오는 조회 파라미터
			@Parameter(name="pltQueryString", required=false) String pltQueryString,// 포틀릿에서 넘겨오는 조회 파라미터
			Locale locale,
			ModelMap model) throws Exception {
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 프레임 여부 - 팝업 조회용 - 참조문서
		boolean isFrame = false;
//		boolean isAdminPage = false;// 관리자 페이지 여부
		boolean hasNextPrevDoc = true;//이전 다음 문서 표시 여부
		boolean downDocYn = false;
		String uri = request.getRequestURI();
		if(uri.indexOf("/viewDocFrm")>0){
			isFrame = true;
			model.put("frmYn", "Y");
			hasNextPrevDoc = false;//이전 다음 문서 표시 여부
		} else if(uri.indexOf("/viewIntgDoc")>0){
			isFrame = true;
			model.put("frmYn", "Y");
			model.put("callByIntg", "Y");
			hasNextPrevDoc = false;//이전 다음 문서 표시 여부
		} else if(uri.indexOf("/viewDocWinPop")>0){
			isFrame = true;
			model.put("frmYn", "Y");
			model.put("winPop", "Y");
			hasNextPrevDoc = false;//이전 다음 문서 표시 여부
		} else if(uri.indexOf("/downloadDoc")>0){
			downDocYn = true;
			model.put("frmYn", "Y");
			model.put("downDocYn", "Y");
			if("Y".equals(request.getParameter("withFile"))){
				model.put("downWithFile", "Y");
			}
		}
		if(uri.indexOf("/dm/")>-1){
			isFrame = true;
			model.put("frmYn", "Y");
			setDmUriBase(request);
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 문서관리 - 사용여부
		if("Y".equals(sysPlocMap.get("dmEnable"))){
			model.put("dmEnable", Boolean.TRUE);
		}
		// 인쇄 미리보기 설정 - IE의 경우
		if("Y".equals(sysPlocMap.get("previewEnable"))){
			model.put("previewEnable", Boolean.TRUE);
		}
		// 결재 시스템 반려
		if("Y".equals(sysPlocMap.get("apSysRejtEnable"))){
			model.put("apSysRejtEnable", Boolean.TRUE);
		}
		// 완결 취소
		if("Y".equals(sysPlocMap.get("apCnclApvEnable"))){
			model.put("apCnclApvEnable", Boolean.TRUE);
		}
		
		try {
			
			boolean isPaper = false;
			
			if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
			
			// RSA 암호화 스크립트 추가 - 결재 비밀번호 확인용
			// 에디터 사용하겠다고 JS 등 include 함
			model.addAttribute("JS_OPTS", new String[]{"pt.rsa", "editor"});
			
			// [결재옵션] 세팅
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
			// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
			apCmSvc.setOptConfigJson(model, userVo.getCompId());
			
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo apOngdBVo = null;
			
			// 반려문서 재기안 용 - 반려된 결재번호
			if(rejtApvNo!=null && !rejtApvNo.isEmpty()){
				apvNo = rejtApvNo;
				hasNextPrevDoc = false;//이전 다음 문서 표시 여부
			// 시행문 변환 용 - 원본 결재번호
			} else if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
				apvNo = orgnApvNo;
				hasNextPrevDoc = false;//이전 다음 문서 표시 여부
				model.put("inTrx", Boolean.TRUE);// 시행문 양식 변환 중 - 표시
			}
			
			
			// ERP연계기본(AP_ERP_INTG_B) 테이블
			ApErpIntgBVo apErpIntgBVo = null;
			if(intgNo!=null && !intgNo.isEmpty() && apvNo==null){
				apErpIntgBVo = new ApErpIntgBVo();
				apErpIntgBVo.setIntgNo(intgNo);
				apErpIntgBVo = (ApErpIntgBVo)commonSvc.queryVo(apErpIntgBVo);
				
				if(apErpIntgBVo==null){
					// ap.msg.noIntgData=연계 데이터가 없습니다.
					String message = messageProperties.getMessage("ap.msg.noIntgData", request);
					model.put("message", message);
					model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/ap/box/listApvBx.do?bxId=myBx"));
					return LayoutUtil.getResultJsp();
				}
				model.put("apErpIntgBVo", apErpIntgBVo);
				
				formId = apErpIntgBVo.getFormId();
				hasNextPrevDoc = false;//이전 다음 문서 표시 여부
			}
			
			// 결재번호가 있으면 TABLE 조회 - 양식 일련번호 조회용
			if(apvNo!=null && !apvNo.isEmpty()){
				
				// 원문보기, 변환문보기 전환용
				if(vwMode!=null && ("trx".equals(vwMode) || "orgn".equals(vwMode))){
					model.put("vwMode", vwMode);
					hasNextPrevDoc = false;//이전 다음 문서 표시 여부
				}
				
				boolean forDetlPop = false;// 상세보기 팝업용은 - 결재방, 첨부, 참조문서 등의 데이터 조회 안함
				
				// ERP 전표보기의 경우 - 전표보기임 세팅
				if(ApConstant.ERP_CHIT.equals(intgTypCd)){
					model.put("intgTypCd", intgTypCd);
				}
				
				// 진행문서기본(AP_ONGD_B) 조회 - 저장된 결재 문서 조회
				apOngdBVo = apDocSvc.getOngoDoc(apvNo, apvLnPno, apvLnNo,
						sendSeq, bxId, pubBxDeptId,
						forDetlPop, refdBy,
						userVo, locale, model);
				
				// 연차 - 내용수정 못하게 막음
				if(apOngdBVo!=null && ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, apOngdBVo.getXmlTypId())){
					model.put("WD_XML", Boolean.TRUE);
				}
				
				// 재기안의 경우
				if(rejtApvNo!=null && !rejtApvNo.isEmpty()){
					
					@SuppressWarnings("unchecked")
					Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
					apvData.remove("docStatCd");
					apvData.remove("docProsStatCd");
					
					// 도장/서명일 지우기 - 도장방
					@SuppressWarnings("unchecked")
					List<ApOngdApvLnDVo> rootApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)model.get("rootApOngdApvLnDVoList");
					boolean first = true;
					for(ApOngdApvLnDVo apOngdApvLnDVo : rootApOngdApvLnDVoList){
						apOngdApvLnDVo.setSignDispVa(null);//서명표시값 
						apOngdApvLnDVo.setDtDispVa(null);//일시표시값
						apOngdApvLnDVo.setSignImgPath(null);//서명이미지경로
						apOngdApvLnDVo.setApvStatCd(null);//결재상태코드
						apOngdApvLnDVo.setPichApntYn(null);//담당자지정여부
						
						if(first){
							first = false;
							// 결재자 정보
							apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
							apOngdApvLnDVo.setApvrRoleCd("mak");
							apOngdApvLnDVo.setApvrDeptYn("N");
							apOngdApvLnDVo.setApvStatCd("inApv");
							model.put("myTurnApOngdApvLnDVo", apOngdApvLnDVo);
						} else {
							apOngdApvLnDVo.setApvOpinCont(null);
							apOngdApvLnDVo.setApvOpinDispYn(null);
						}
					}
					
				}
				
				if(apOngdBVo!=null){
					isPaper = "paper".equals(apOngdBVo.getDocTypCd());
					
					// 양식정보 - 저장된 결재 데이터 가 있으면 결재 데이터의 양식 정보 조회
					if(apOngdBVo.getFormId()!=null && orgnApvNo==null){
						formId = apOngdBVo.getFormId();
						// 양식일련번호가 있으면 - 진행중인 결재에 해당 - 진행결재양식 조회
						if(apOngdBVo.getFormSeq()!=null){
							formSeq = apOngdBVo.getFormSeq();
						}
					}
					
					// 문서 비밀번호 확인 - 비밀번호가 있고 기안자가 아니면
					if(apOngdBVo.getDocPwEnc()!=null && !apOngdBVo.getDocPwEnc().isEmpty() 
							&& !userVo.getUserUid().equals(apOngdBVo.getMakrUid())
							&& !downDocYn){
						
						String secuId = request.getParameter("secuId");
						String message = null;
						if(secuId==null || secuId.isEmpty()){
							//ap.msg.notCfrmDocPw=문서 비밀번호가 확인 되지 않았습니다.
							message = messageProperties.getMessage("ap.msg.notCfrmDocPw", request);
						} else if(!apDocSecuSvc.confirmSecuId(request.getSession(), secuId)){
							//ap.msg.exprdCfrmDocPw=문서 비밀번호를 입력해 주십시요.
							message = messageProperties.getMessage("ap.msg.exprdCfrmDocPw", request);
						}
						if(message != null){
							model.put("message", message);
							if(isPaper){
								model.put("noPw", "Y");
								return LayoutUtil.getJspPath("/ap/box/viewPaperDoc");
							}
							if(isFrame){
								return LayoutUtil.getJspPath("/ap/box/viewDocPwCfrmFrm");
							} else {
								return LayoutUtil.getJspPath("/ap/box/viewDocPwCfrm");
							}
						}
					}
					
				}
			} else {
				hasNextPrevDoc = false;//이전 다음 문서 표시 여부
			}
			
			
			// ERP연계 - 전표보기 처리
			
			// ERP연계전표상세(AP_ERP_INTG_CHIT_D) 테이블 - 조회
			ApErpIntgChitDVo apErpIntgChitDVo = null;
			boolean isStoredErpIntgChit = false;
			if(ApConstant.ERP_CHIT.equals(intgTypCd)){
				// 파라미터:intgTypCd 와 파라미터:intgNo 가 넘어옴 - 저장되지 않은 문서의 - 연계문서 보기
				if(apErpIntgBVo!=null && ApConstant.ERP_CHIT.equals(apErpIntgBVo.getIntgTypCd())){
					apErpIntgChitDVo = new ApErpIntgChitDVo();
					apErpIntgChitDVo.setIntgNo(apErpIntgBVo.getIntgNo());
					apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
				// 파라미터:intgTypCd -넘어옴, 파라미터:intgNo -안 넘어옴 - 저장된 문서
				} else if(apOngdBVo!=null && ApConstant.ERP_CHIT.equals(apOngdBVo.getIntgTypCd())){
					apErpIntgChitDVo = (ApErpIntgChitDVo)model.get("apErpIntgChitDVo");
					if(apErpIntgChitDVo == null){
						apErpIntgChitDVo = new ApErpIntgChitDVo();
						apErpIntgChitDVo.setIntgNo(apOngdBVo.getIntgNo());
						apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
					}
					isStoredErpIntgChit = true;
				}
				// 전표보기 양식ID 세팅
				if(apErpIntgChitDVo != null){
					formId = apErpIntgChitDVo.getChitFormId();
					formSeq = null;
				}
			}
			
			// 양식 조회
			if(formId!=null && !formId.isEmpty()){
				
				// 진행중인 결재 양식 조회 - 임시저장 또는 기안 된것
				if(formSeq!=null && !formSeq.isEmpty()){
					
					// 진행결재양식 조회 : 양식일련번호가 있으면 - 진행중인 결재에 해당
					apFormSvc.setOngoForm(formId, formSeq, request, model);
					// apvLnOneTopList=최종결재 리스트
					if("apvLnOneTopList".equals(model.get("formApvLnTypCd"))){
						setLastApvr(model);
					}
					
					// 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
					String docProsStatCd = apOngdBVo.getDocProsStatCd();
					if("temp".equals(docProsStatCd) || "mak".equals(docProsStatCd) || "ongo".equals(docProsStatCd)){
						apDocSvc.setRefDocBody(apOngdBVo, model);
					}
					
					// ERP 양식 결재 - XML 조회
					ApOngoFormBVo apOngoFormBVo = (ApOngoFormBVo)model.get("apFormBVo");
					if(apOngoFormBVo != null){
						apDocSvc.setFormBodyXML(apvNo, apOngoFormBVo.getErpFormId(), apOngoFormBVo.getErpFormTypCd(), model);
					}
					
					String wfWorkNo = (String)model.get("wfWorkNo");
					String wfFormNo = (String)model.get("wfFormNo");
					if(wfWorkNo!=null && wfFormNo!=null){
						// 업무관리용 - 데이터 세팅
						wfMdFormSvc.setFormByAP(request, model, wfFormNo, apOngoFormBVo.getFormId(), wfWorkNo, false);
					}
					
				// ERP 전표보기 - 저장된 문서의 경우
				} else if(apErpIntgChitDVo != null && isStoredErpIntgChit){
					
					// 결재양식 조회
					apFormSvc.setSetupForm(formId, langTypCd, request, model);
					if(model.get("apFormBVo")==null){
						// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
						String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#ap.jsp.form"}, locale);
						model.put("message", message);
					}
					
					// 양식 조회 후 기본 데이터 만들기
					@SuppressWarnings("unchecked")
					Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
					if(apvData==null){
						apvData = new HashMap<String, Object>();
						model.put("apvData", apvData);
					}
					
					// ERP전표 - 양식명, 본문 세팅
					if(apErpIntgChitDVo!=null){
						if(apErpIntgChitDVo.getChitBodyHtml()!=null && !apErpIntgChitDVo.getChitBodyHtml().isEmpty()){
							apvData.put("bodyHtml", apErpIntgChitDVo.getChitBodyHtml());
						}
						if(apErpIntgChitDVo.getFormNm()!=null && !apErpIntgChitDVo.getFormNm().isEmpty()){
							apvData.put("formNm", apErpIntgChitDVo.getFormNm());
						}
						// 전표양식의 본문 높이
						ApFormBVo apFormBVo = (ApFormBVo)model.get("apFormBVo");
						String bodyHghtPx = apFormBVo.getBodyHghtPx();
						if(bodyHghtPx==null || bodyHghtPx.isEmpty() || bodyHghtPx.equals("0")){
							apvData.remove("bodyHghtPx");
						} else {
							apvData.put("bodyHghtPx", bodyHghtPx);
						}
					}
					
				// 시행변환용 양식 조회
				} else if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
					
					// 결재양식 조회
					apFormSvc.setSetupForm(formId, langTypCd, request, model);
					
					// ERP 양식 결재 - XML 조회
					ApFormBVo apFormBVo = (ApFormBVo)model.get("apFormBVo");
					if(apFormBVo != null){
						apDocSvc.setFormBodyXML(orgnApvNo, apFormBVo.getErpFormId(), apFormBVo.getErpFormTypCd(), model);
					}
					
				// 결재양식 조회
				} else {
					
					// 결재양식 조회
					apFormSvc.setSetupForm(formId, langTypCd, request, model);
					
					ApFormBVo apFormBVo = (ApFormBVo)model.get("apFormBVo");
					if(apFormBVo==null){
						// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
						String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#ap.jsp.form"}, locale);
						model.put("message", message);
					}
					
					// 양식 조회 후 기본 데이터 만들기
					@SuppressWarnings("unchecked")
					Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
					if(apvData==null){
						apvData = new HashMap<String, Object>();
						model.put("apvData", apvData);
					}
					
					apvData.put("makrNm", userVo.getUserNm());//기안자명
					OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "D", langTypCd);
					if(orOrgTreeVo!=null){
						apvData.put("makDeptNm", orOrgTreeVo.getRescNm());//기안부서 명
					}
					
					// 기안자 정보 세팅
					setMakrApvLn(userVo, model);
					
					// 발신명의
					ApFormTxtDVo apFormTxtDVo = (ApFormTxtDVo)model.get("docSender");
					if(apFormTxtDVo!=null) apvData.put("sendrNmRescNm", apFormTxtDVo.getTxtCont());//발신명의
					
					// 연계 양식명
					String formNm = apErpIntgBVo==null ? null : apErpIntgBVo.getFormNm();
					String storedFormNm = null;
					
					// storedFormNm : 연계 양식명이 없으면 저장된 양식명으로
					if(formNm==null || formNm.isEmpty()){
						apFormTxtDVo = (ApFormTxtDVo)model.get("docName");
						if(apFormTxtDVo == null){
							storedFormNm = apFormBVo.getRescNm();
						} else {
							storedFormNm = apFormTxtDVo.getTxtCont();//양식명
						}
					} else {
						storedFormNm = formNm;
					}
					
					apvData.put("formNm", storedFormNm);//양식명
					
					// [제목 - 양식에 설정된] : 양식명
					if(apFormBVo!=null && "formNm".equals(apFormBVo.getDocSubjCd())){
						apvData.put("docSubj", storedFormNm);// 제목 : 양식명
					// [제목 - 양식에 설정된] : 양식명 + 년월일
					} else if(apFormBVo!=null && "formNmDt".equals(apFormBVo.getDocSubjCd())){
						apvData.put("docSubj", storedFormNm+" ("+StringUtil.getCurrYmd()+")");// 제목 : 양식명 + 년월일
					} else {
						if("Y".equals(optConfigMap.get("formNmToSubj"))){//[옵션] 양식명을 제목으로
							apvData.put("docSubj", storedFormNm);// 제목 : 양식명
						}
					}
					
					if(apFormBVo!=null){
						
						String bodyHtml = null;
						if(apErpIntgBVo!=null){
							// 연계 본문 세팅
							bodyHtml = apErpIntgBVo.getBodyHtml();
							if("ERP_ONECARD".equals(apErpIntgBVo.getIntgTypCd())){
								model.put("erpBodyLock", "Y");//ERP 에서 넘어온 데이터 본문 수정 금지 - 원래 결재 옵션인데, 원카드의 경우 결제 옵션처럼 동작 하도록
								model.put("scriptBodyHtml", "Y");//본문의 javascript 사용금지 안함
								model.put("noBodyOverflow", "Y");//본문에 overflow 스타일 주지 않음 - 마이너스 마진 적용되도록
								apvData.put("regRecLstRegYn", "N");//대장 미등록
							}
							
							// 연계 본문이 XML 일 경우
							if("ERP_XML".equals(apErpIntgBVo.getIntgTypCd())){
								String erpFormId = apFormBVo.getErpFormId();
								String erpFormTypCd = apFormBVo.getErpFormTypCd();
								
								if(erpFormId!=null && !erpFormId.isEmpty() && "xmlFromErp".equals(erpFormTypCd)){
									bodyHtml = apCustFncSvc.createBodyHtmlFromErpXml(apErpIntgBVo.getIntgNo(), erpFormId);
								} else {
									bodyHtml = null;
								}
							}
							
							// 연계 제목 세팅
							apvData.put("docSubj", apErpIntgBVo.getDocSubj());
							
							String refVa = apErpIntgBVo.getRefVa();
							if(refVa != null && refVa.startsWith("{") && refVa.endsWith("}")){
								try{
									JSONObject jsonObject = (JSONObject)JSONValue.parse(refVa);
									@SuppressWarnings("unchecked")
									Iterator<Object> iterator = jsonObject.keySet().iterator();
									Object key;
									Map<Object, Object> erpObject = new HashMap<Object, Object>();
									while(iterator.hasNext()){
										key = iterator.next();
										erpObject.put(key, jsonObject.get(key));
									}
									erpObject.put("bodyHtml", apErpIntgBVo.getBodyHtml());
									model.put("erpObject", erpObject);
								} catch(Exception e){
									e.printStackTrace();
								}
							}
							
							// 연계 첨부파일 조회
							ApErpIntgFileDVo apErpIntgFileDVo = new ApErpIntgFileDVo();
							apErpIntgFileDVo.setIntgNo(intgNo);
							@SuppressWarnings("unchecked")
							List<ApErpIntgFileDVo> apErpIntgFileDVoList = (List<ApErpIntgFileDVo>)commonSvc.queryList(apErpIntgFileDVo);
							if(apErpIntgFileDVoList != null){
								model.put("apErpIntgFileDVoList", apErpIntgFileDVoList);
							}
							
							if(apErpIntgBVo.getIntgTypCd()!=null && !apErpIntgBVo.getIntgTypCd().isEmpty()){
								apvData.put("intgTypCd", apErpIntgBVo.getIntgTypCd());//연계구분코드
							}
							
							// ERP전표 - 양식명, 본문 세팅 - 저장되지 않은 문서의 경우
							if(apErpIntgChitDVo!=null){
								bodyHtml = apErpIntgChitDVo.getChitBodyHtml();
								String chitFormNm = apErpIntgChitDVo.getFormNm();
								if(chitFormNm!=null && !chitFormNm.isEmpty()){
									apvData.put("formNm", chitFormNm);
								}
							}
						}
						
						if(bodyHtml==null || bodyHtml.isEmpty()) bodyHtml = apFormBVo.getBodyHtml();
						else {
							// 본문수정 허용 이고, 전표보기 가 아닐 경우 - 한단에 한줄 추가
							if(!"Y".equals(optConfigMap.get("erpBodyLock")) && !"ERP_CHIT".equals(intgTypCd)){
								bodyHtml = bodyHtml + "<p><br/><p>";
							}
						}
						apvData.put("bodyHtml", bodyHtml);//본문HTML
						
						apvData.put("formWdthTypCd", apFormBVo.getFormWdthTypCd());//양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개
						if(!"0".equals(apFormBVo.getBodyHghtPx())){
							apvData.put("bodyHghtPx", apFormBVo.getBodyHghtPx());//본문높이픽셀
						}
						
						//양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용
						if("extro".equals(apFormBVo.getFormTypCd())){
							//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
							apvData.put("docTypCd", "extro");
							
							// [옵션] bothSendEnab=대 내외 동시 시행
							if("Y".equals(optConfigMap.get("bothSendEnab"))){
								//시행범위코드 - dom:대내, for:대외, both:대내외
								apvData.put("enfcScopCd", "both");
							} else {
								//시행범위코드 - dom:대내, for:대외, both:대내외
								apvData.put("enfcScopCd", "dom");
							}
							
						} else {
							//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
							apvData.put("docTypCd", "intro");
						}
						
						String formApvLnGrpId = apFormBVo.getApvLnGrpId();//apvLnGrpId
						if(formApvLnGrpId!=null && !formApvLnGrpId.isEmpty()){
							model.put("formApvLnGrpId", formApvLnGrpId);
							model.put("fixdApvrYn", "Y".equals(apFormBVo.getFixdApvrYn()) ? "Y" : "N");
						}
						String autoApvLnCd = apFormBVo.getAutoApvLnCd();
						if(autoApvLnCd!=null && !autoApvLnCd.isEmpty()){
							model.put("autoApvLnCd", autoApvLnCd);
						}
						
						// ERP 양식 결재 - XML 조회
						if(refDocApvNo != null && !refDocApvNo.isEmpty()){
							
							String wfFormNo = (String)model.get("wfFormNo");
							apDocSvc.setFormBodyXML(refDocApvNo, apFormBVo.getErpFormId(), apFormBVo.getErpFormTypCd(), model);
							
							if(wfFormNo!=null && !wfFormNo.isEmpty()){
								String refWfFormNo = (String)model.get("wfFormNo");
								String refWfWorkNo = (String)model.get("wfWorkNo");
								if(wfFormNo.equals(refWfFormNo) && refWfWorkNo!=null && !refWfFormNo.isEmpty()){
									// 업무관리용 - 데이터 세팅
									wfMdFormSvc.setFormByAP(request, model, refWfFormNo, apFormBVo.getFormId(), refWfWorkNo, false);
								} else {
									model.put("wfFormNo", wfFormNo);
									model.put("wfWorkNo", "");
								}
							}
							
						} else {
							apDocSvc.setFormBodyXML(apvNo, apFormBVo.getErpFormId(), apFormBVo.getErpFormTypCd(), model);
						}
						
						// [옵션] 양식 참조열람
						if("Y".equals(optConfigMap.get("fixdRefVw"))){
							String refVwGrpId = apFormBVo.getRefVwGrpId();
							String refVwFixdApvrYn = apFormBVo.getRefVwFixdApvrYn();
							apDocSvc.setFixedRefVw(refVwGrpId, refVwFixdApvrYn, userVo, model);
						}
					}
					
					// 결재 라인정보 세팅 - 실제 문서용
					// 도장방 - 4곳 >> [apv:결재], [agr:합의], [req:신청], [prc:처리]
					setSignDispArea(model, formId, false);
					
					// 참조 기안일때 - 본문, 첨부 조회
					if(refDocApvNo != null && !refDocApvNo.isEmpty()){
						apDocSvc.setRefDocMak(refDocApvNo, apvData, model);
						hasNextPrevDoc = false;//이전 다음 문서 표시 여부
						
						// [옵션] refMakWithApvLn=참조기안 결재선 유지
						if("Y".equals(optConfigMap.get("refMakWithApvLn"))){
							String formApvLnTypCd = apFormBVo.getFormApvLnTypCd();
							apDocSvc.setRefApvLn(refDocApvNo, formApvLnTypCd, userVo, model);
						}
					}
					
					// 양식에 설정된 보존년한 세팅
					if(apFormBVo!=null){
						apvData.put("docKeepPrdCd", apFormBVo.getDocKeepPrdCd());
					}
					
					// [옵션] unregDft=미등록 기본값으로
					if("Y".equals(optConfigMap.get("unregDft"))){
						apvData.put("regRecLstRegYn", "N");
					}
				}
				
				if(formSeq==null || formSeq.isEmpty()){

					String footerVa = (String)model.get("footerVa");
					if(footerVa!=null && !footerVa.isEmpty()){
						@SuppressWarnings("unchecked")
						Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
						if(apvData==null){
							apvData = new HashMap<String, Object>();
							model.put("apvData", apvData);
						}
						// 바닥글 치환
						footerVa = apDocTransSvc.replaceFooterVa(footerVa, userVo.getDeptId());
						apvData.put("footerVa", footerVa);
					}
					
					// 양식 조회 후 기본 데이터 만들기
					@SuppressWarnings("unchecked")
					Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
					if(apvData==null){
						apvData = new HashMap<String, Object>();
						model.put("apvData", apvData);
					}
				}
			}
			
			// 참조열람 목록에 빈라인 표시용
			ApOngdApvLnDVo myTurnApOngdApvLnDVo = (ApOngdApvLnDVo)model.get("myTurnApOngdApvLnDVo");
			if(myTurnApOngdApvLnDVo!=null && "mak".equals(myTurnApOngdApvLnDVo.getApvrRoleCd())){
				@SuppressWarnings("unchecked")
				List<ApOngdRefVwDVo> apOngdRefVwDVoList = (List<ApOngdRefVwDVo>)model.get("apOngdRefVwDVoList");
				if(apOngdRefVwDVoList==null || apOngdRefVwDVoList.isEmpty()){
					apOngdRefVwDVoList = new ArrayList<ApOngdRefVwDVo>();
					apOngdRefVwDVoList.add(new ApOngdRefVwDVo());
					model.put("apOngdRefVwDVoList", apOngdRefVwDVoList);
				}
			}
			
			String regRecLstMenuId = apBxSvc.getMenuIdByBxId(userVo, "regRecLst");
			if(regRecLstMenuId != null) model.put("regRecLstMenuId", regRecLstMenuId);
			
			if(uri.startsWith("/dm/adm/")){
				model.put("apFileModule", ApConstant.DM_ADM_FILE_MODULE);
				model.put("apFileTarget", "Ap");
			} else if(uri.startsWith("/dm/")){
				model.put("apFileModule", ApConstant.DM_FILE_MODULE);
				model.put("apFileTarget", "Ap");
			} else if(uri.startsWith("/ap/adm/")){
				model.put("apFileModule", ApConstant.AP_ADM_FILE_MODULE);
			} else {
				model.put("apFileModule", ApConstant.AP_FILE_MODULE);
			}
			
			// 다운로드|문서뷰어 사용여부
			emAttachViewSvc.chkAttachSetup(model, userVo.getCompId());
			
			// 이전 문서 다음 문서가 있으면
			if(hasNextPrevDoc){
				if(pltQueryString!=null && !pltQueryString.isEmpty()){
					queryString = pltQueryString;
				}
				if(queryString==null || queryString.isEmpty()){
					model.addAttribute("queryString", "pageNo=1&pageRowCnt="+PersonalUtil.getPageRowCnt("ap", request.getSession()));
				}
				
				boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
				apBxSvc.setNextPrevDoc(userVo, langTypCd, bxId, apvNo, hasAdmin, queryString, request.getSession(), model);
			}
			
			// 서면 결재 일 경우
			if("apvLnWrtn".equals(model.get("formApvLnTypCd"))){
				apCustFncSvc.setCustApvLnWrtn(model, 
						apOngdBVo==null ? null : apOngdBVo.getMakDt(),
						apOngdBVo==null ? langTypCd : apOngdBVo.getDocLangTypCd());
			}
			
//			// 프라코 전표보기 - 서면결재 - 하드코딩
//			if(ApConstant.ERP_CHIT.equals(intgTypCd)){
//				apCustFncSvc.setApvLnWrtn(model, apOngdBVo==null ? langTypCd : apOngdBVo.getDocLangTypCd());
//			}
			
			model.put("custCode", CustConfig.CUST_CODE);
			
			// [옵션] 관인 자동 날인 - [옵션] 자동발송
			if("Y".equals(optConfigMap.get("autoOfcSeal")) && !"Y".equals(optConfigMap.get("autoSend"))){
				@SuppressWarnings("unchecked")
				Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
				if(apvData != null){
					// apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소
					String enfcStatCd = (String)apvData.get("enfcStatCd");
					String sendrNmOrgId = (String)apvData.get("sendrNmOrgId");
					String ofsePath = (String)apvData.get("ofsePath");
					if(		("befoEnfc".equals(enfcStatCd) || "befoSend".equals(enfcStatCd))
							&& (ofsePath==null || ofsePath.isEmpty())
							&& sendrNmOrgId!=null && !sendrNmOrgId.isEmpty()
							&& ("myBx".equals(bxId) || "toSendBx".equals(bxId) || "censrBx".equals(bxId))
							){
						
						// 관인, 서명인 목록 조회
						OrOfseDVo orOfseDVo = new OrOfseDVo();
						orOfseDVo.setOrgId(sendrNmOrgId);
						orOfseDVo.setDftOfseYn("Y");
						orOfseDVo.setDisuYn("N");
						orOfseDVo.setOrderBy("SEQ");
						@SuppressWarnings("unchecked")
						List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonSvc.queryList(orOfseDVo);
						
						if(orOfseDVoList!=null && !orOfseDVoList.isEmpty()){
							orOfseDVo = orOfseDVoList.get(0);
							
							String imgPath = orOfseDVo.getImgPath();
							if(imgPath!=null && !imgPath.isEmpty()){
								apvData.put("ofsePath", imgPath);
								apvData.put("ofseHghtPx", "80");
								model.put("autoOfcSeal", Boolean.TRUE);
							}
						}
					}
				}
			}
			
			// 대외공문
			@SuppressWarnings("unchecked")
			Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
			// 발송 된 경우
			if("sent".equals(apvData.get("enfcStatCd"))) {
				ApFormJspDVo apFormJspDVo = new ApFormJspDVo();
				apFormJspDVo.setJspId("exDoc");
				apFormJspDVo.setFormId((String)apvData.get("formId"));
				apFormJspDVo = (ApFormJspDVo)commonSvc.queryVo(apFormJspDVo);
				if(apFormJspDVo != null) {
					model.put("apFormJspDVo", apFormJspDVo);
				}
			}
			
			if("extDoc".equals(vwMode)) { // 대외공문 - 인쇄 모드
				
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(userVo.getUserUid());
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				model.put("orUserBVo", orUserBVo);
				
				OrOrgCntcDVo orOrgCntcDVo = new OrOrgCntcDVo();
				orOrgCntcDVo.setOrgId(userVo.getDeptId());
				orOrgCntcDVo = (OrOrgCntcDVo)commonSvc.queryVo(orOrgCntcDVo);
				model.put("orOrgCntcDVo", orOrgCntcDVo);
				
				return LayoutUtil.getJspPath("/ap/box/viewExtDoc");
			} else if(isPaper){
				model.put("viewPaper", "Y");
				return LayoutUtil.getJspPath("/ap/box/viewPaperDoc");
			} else if(downDocYn){
				return LayoutUtil.getDownloadPath("/ap/box/setDoc");
			} else if(isFrame){
				return LayoutUtil.getJspPath("/ap/box/viewDocFrm");
			} else {
				return LayoutUtil.getJspPath("/ap/box/setDoc");
			}
		} catch(CmException cmEx) {
			model.put("message", cmEx.getMessage());
			String paramMenuId = menuId!=null && menuId.startsWith("Y") ? menuId : null;
			model.put("togo", apBxSvc.getBxUrlByBxId(userVo, bxId, paramMenuId));
			return LayoutUtil.getJspPath("/ap/box/viewMessage");
		}
	}
	/** 결재라인타입이 - 최종결재+리스트 인 경우, 최종 결재자 세팅함 */
	private void setLastApvr(ModelMap model) {
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> rootApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)model.get("stampApOngdApvLnDVoList");
		int i, size = rootApOngdApvLnDVoList==null ? 0 : rootApOngdApvLnDVoList.size();
		ApOngdApvLnDVo apOngdApvLnDVo;
		String apvrRoleCd;
		boolean findLast = false;
		for(i=size-1;i>=0;i--){
			apOngdApvLnDVo = rootApOngdApvLnDVoList.get(i);
			// 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, 
			// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
			// prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, 
			// abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, 
			// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 
			apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
			if("apv".equals(apvrRoleCd) || "pred".equals(apvrRoleCd) || "revw".equals(apvrRoleCd) || "revw2".equals(apvrRoleCd) || "revw3".equals(apvrRoleCd) || "byOne".equals(apvrRoleCd)){
				model.put("lastApOngdApvLnDVo", apOngdApvLnDVo);
				findLast = true;
				break;
			}
		}
		if(!findLast && size > 0){
			model.put("lastApvr", new ApOngdApvLnDVo());
		}
	}
	/** 도장방 표시영역 데이터 세팅 */
	private void setSignDispArea(ModelMap model, String formId, boolean isOngo){
		// 결재 라인정보 세팅 - 실제 문서용
		// 도장방 - 4곳 >> [apv:결재], [agr:합의], [req:신청], [prc:처리]
		ApFormApvLnDVo apFormApvLnDVo;
		ApOngoFormApvLnDVo apOngoFormApvLnDVo;
		int i, maxCnt = 0;
		List<ApOngdApvLnDVo> apvrList;
		for(String signArea : new String[]{"apv", "agr", "req", "prc"}){
			if(isOngo){
				apOngoFormApvLnDVo = (ApOngoFormApvLnDVo)model.get(signArea);
				if(apOngoFormApvLnDVo!=null){
					if(apOngoFormApvLnDVo.getMaxCnt() != null && !apOngoFormApvLnDVo.getMaxCnt().isEmpty()){
						try{
							maxCnt = Integer.parseInt(apOngoFormApvLnDVo.getMaxCnt());
						} catch(Exception e){
							maxCnt = 0;
						}
					} else {
						maxCnt = 0;
					}
				}
			} else {
				apFormApvLnDVo = (ApFormApvLnDVo)model.get(signArea);
				if(apFormApvLnDVo!=null){
					if(apFormApvLnDVo.getMaxCnt() != null && !apFormApvLnDVo.getMaxCnt().isEmpty()){
						try{
							maxCnt = Integer.parseInt(apFormApvLnDVo.getMaxCnt());
						} catch(Exception e){
							maxCnt = 0;
						}
					} else {
						maxCnt = 0;
					}
				}
			}
			if(maxCnt>0){
				apvrList = new ArrayList<ApOngdApvLnDVo>();
				for(i=0;i<maxCnt;i++){
					apvrList.add(new ApOngdApvLnDVo());
				}
				model.put(signArea+"ApvrList", apvrList);
			}
		}
	}
	
	/** 문서 팝업 조회 */
	@RequestMapping(value = {
			"/ap/box/viewDocPop",   "/ap/adm/box/viewDocPop",
			"/dm/doc/viewApDocPop", "/dm/adm/doc/viewApDocPop",
			"/wd/adm/viewApDocPop"} )
	public String viewDocPop(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="vwMode", required=false) String vwMode,// 원문:orgn, 시행변환문:trx
			@Parameter(name="refdBy", required=false) String refdBy,
			Locale locale,
			ModelMap model) throws Exception {
		
		setDmUriBase(request);
		return LayoutUtil.getJspPath("/ap/box/viewDocPop");
	}
	
	/** 기안자 정보 세팅 */
	private void setMakrApvLn(UserVo userVo, ModelMap model){
		// 결재자 정보
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
		apOngdApvLnDVo.setApvrRoleCd("mak");
		apOngdApvLnDVo.setApvrDeptYn("N");
		apOngdApvLnDVo.setApvStatCd("inApv");
		model.put("myTurnApOngdApvLnDVo", apOngdApvLnDVo);
		
		// 결재선 정보
		List<ApOngdApvLnDVo> currApOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
		currApOngdApvLnDVoList.add(apOngdApvLnDVo);
		model.put("currApOngdApvLnDVoList", currApOngdApvLnDVoList);
	}

	////////////////////////////////////////////////////////////////////
	//
	//				상세정보 - 문서정보 / 결재정보(결재선) / 수신처정보
	/** [팝업] 문서정보 */
	@RequestMapping(value = {"/ap/box/viewDocDetlPop", "/ap/adm/box/viewDocDetlPop",
			"/dm/doc/viewDocDetlPop", "/dm/adm/doc/viewDocDetlPop", "/wd/adm/viewDocDetlPop"})
	public String viewDocDetlPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			@Parameter(name="apvLnNo", required=false) String apvLnNo,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="refdBy", required=false) String refdBy,
			Locale locale,
			ModelMap model) throws Exception {
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
		
		String sendSeq = null;// 발송일련번호 - 접수/배부 처리할때 발송한 문서의 해당 일련번호에 수신여부 표시해야함
		String pubBxDeptId = null;// 공람부서ID
		boolean forDetlPop = true;// 상세보기 팝업용 - 결재방, 첨부, 참조문서 등의 데이터 조회 안함
		
		// 진행문서기본(AP_ONGD_B) 테이블
		apDocSvc.getOngoDoc(apvNo, apvLnPno, apvLnNo, 
				sendSeq, bxId, pubBxDeptId, 
				forDetlPop, refdBy,
				userVo, locale, model);
		
		String uri = request.getRequestURI();
		if(uri.indexOf("adm/box")>0){// 관리자 페이지 여부
			if(userVo.isAdmin() || userVo.isSysAdmin()){
				model.put("isAdminPage", Boolean.TRUE);
			}
		}
		
		if(uri.startsWith("/dm/adm/")){
			model.put("apFileModule", ApConstant.DM_ADM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/dm/")){
			model.put("apFileModule", ApConstant.DM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/ap/adm/")){
			model.put("apFileModule", ApConstant.AP_ADM_FILE_MODULE);
		} else {
			model.put("apFileModule", ApConstant.AP_FILE_MODULE);
		}
		
		// 문서관리 - 보낸 폴더명 조회
		if(apvNo!=null && !apvNo.isEmpty()){
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			apOngdExDVo.setStorage(storage);
			apOngdExDVo.setExId("sendToDm");
			apOngdExDVo = (ApOngdExDVo)commonSvc.queryVo(apOngdExDVo);
			if(apOngdExDVo!=null && apOngdExDVo.getExVa()!=null && !apOngdExDVo.getExVa().isEmpty()){
				JSONObject jsonObject = (JSONObject)JSONValue.parse(apOngdExDVo.getExVa());
				String sendToDmNm = (String)jsonObject.get("fldNm");
				model.put("sendToDmNm", sendToDmNm);
			}
		}
		
		// 관리자 - 완료문서
		if("admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId)){
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if("Y".equals(sysPlocMap.get("apAdmAddInfm"))){//결재 관리자 통보추가
				model.put("apAdmAddInfm", Boolean.TRUE);
			}
		}
		
		
		return LayoutUtil.getJspPath("/ap/box/viewDocDetlPop");
	}
	
	/** [팝업] 제목 수정 */
	@RequestMapping(value = "/ap/box/setModifySubjPop")
	public String setModifySubjPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			ModelMap model) throws Exception {
		
		if(apvNo!=null && !apvNo.isEmpty()){
			
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			String langTypCd = LoginSession.getLangTypCd(request);
			
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setStorage(storage);
			apOngdBVo.setQueryLang(langTypCd);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			if(apOngdBVo != null){
				model.put("docSubj", apOngdBVo.getDocSubj());
			}
		}
		
		return LayoutUtil.getJspPath("/ap/box/setModifySubjPop");
	}

	////////////////////////////////////////////////////////////////////
	//
	//				본문수정
	/** [팝업] 본문수정 */
	@RequestMapping(value = "/ap/box/setDocBodyHtmlPop")
	public String setDocBodyHtmlPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="bodyHstNo", required=false) String bodyHstNo,
			ModelMap model) throws Exception {
		
		if(apvNo!=null && !apvNo.isEmpty()){
			
			if(bodyHstNo==null || bodyHstNo.isEmpty()){
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				if(apOngdBVo!=null){
					bodyHstNo = apOngdBVo.getBodyHstNo();
				}
			}
			if(bodyHstNo!=null && !bodyHstNo.isEmpty()){
				
				ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
				apOngdBodyLVo.setApvNo(apvNo);
				apOngdBodyLVo.setBodyHstNo(bodyHstNo);
				apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
				if(apOngdBodyLVo != null){
					model.put("apOngdBodyLVo", apOngdBodyLVo);
				}
			}
			
		}
		
		return LayoutUtil.getJspPath("/ap/box/setDocBodyHtmlPop");
	}
	/** [팝업] 본문 이력조회 */
	@RequestMapping(value = {"/ap/box/viewBodyHisPop", "/ap/adm/box/viewBodyHisPop",
			"/dm/doc/viewBodyHisPop", "/dm/adm/doc/viewBodyHisPop", "/wd/adm/viewBodyHisPop"})
	public String viewBodyHisPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="bodyHstNo", required=false) String bodyHstNo,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
		apOngdBodyLVo.setApvNo(apvNo);
		apOngdBodyLVo.setBodyHstNo(bodyHstNo);
		apOngdBodyLVo.setStorage(storage);
		apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
		if(apOngdBodyLVo != null) model.put("apOngdBodyLVo", apOngdBodyLVo);
		
		return LayoutUtil.getJspPath("/ap/box/viewBodyHisPop");
	}

	////////////////////////////////////////////////////////////////////
	//
	//				문서정보 - 등록/수정
	/** [팝업] 문서정보 */
	@RequestMapping(value = "/ap/box/setDocInfoPop")
	public String setDocInfoPop(HttpServletRequest request,
			ModelMap model) throws Exception {

		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 문서보존기간코드
		List<PtCdBVo> docKeepPrdCdList = ptCmSvc.getCdList("DOC_KEEP_PRD_CD", langTypCd, "Y");
		model.put("docKeepPrdCdList", docKeepPrdCdList);
		

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

		// 문서구분코드
		List<PtCdBVo> docTypCdList = ptCmSvc.getCdList("DOC_TYP_CD", langTypCd, "Y");
		model.put("docTypCdList", docTypCdList);
		
		// 시행범위코드
		List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
		model.put("enfcScopCdList", enfcScopCdList);
		
		// 발신명의 목록
		List<OrOrgApvDVo> orOrgApvDVoList = apDocSvc.getSenderList(userVo, langTypCd);
		if(orOrgApvDVoList != null) model.put("orOrgApvDVoList", orOrgApvDVoList);
		
		// 문서관리 - 사용여부
		if("Y".equals(sysPlocMap.get("dmEnable"))){
			model.put("dmEnable", Boolean.TRUE);
		}
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ap/box/setDocInfoPop");
	}
	/** [AJAX] 문서번호 조회 */
	@RequestMapping(value = "/ap/box/getDocNoAjx")
	public String getDocNoAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			// 옵션 조회 
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
			
			String docLangTypCd = (String)jsonObject.get("docLangTypCd");
			String clsInfoId = (String)jsonObject.get("clsInfoId");
			
			// 문서정보
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setClsInfoId(clsInfoId);
			apOngdBVo.setDocLangTypCd(docLangTypCd);
			
			// 결재선 정보
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvDeptId(userVo.getDeptId());
			
			// 부서정보
			String apvDeptId = userVo.getDeptId();
			OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, docLangTypCd, null);
			if(orOrgBVo==null){
				LOGGER.error("Fail trans - dept not found ! - apvDeptId:"+apvDeptId);
				//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
				message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
						+ apvDeptId+ " : " + apOngdApvLnDVo.getApvDeptNm();
				throw new CmException(message);
			}
			
			apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
			apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getOrgAbbrRescNm());
			
			apDocNoSvc.setDocNo(apOngdBVo, apOngdApvLnDVo, optConfigMap, "ongoing", locale);
			
			model.put("docNo", apOngdBVo.getDocNo());
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		
		return LayoutUtil.returnJson(model, message);
	}

	////////////////////////////////////////////////////////////////////
	//
	//				관인 / 서명인
	/** [팝업] 관인 / 서명인 */
	@RequestMapping(value = "/ap/box/setOfsePop")
	public String setOfsePop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="ofseTypCd", required=false) String ofseTypCd,// 관인구분코드 - 01:관인(기관), 02:서명인(부서)
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 서명인
		if("02".equals(ofseTypCd)){//서명인 - 부서부터
			model.put("upParam", "&upward="+orgId);
		} else {// 관인 - 기관부터
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orgId, "G", langTypCd);//G:기관
			model.put("upParam", "&upward="+orOrgTreeVo.getOrgId());
		}
		// 옵션 조회(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ap/box/setOfsePop");
	}
	/** 상위조직 트리 프레임 */
	@RequestMapping(value = {"/ap/box/treeUpFrm", "/ap/box/treeDownFrm", "/ap/box/treeRecLstOrgFrm"})
	public String treeUpFrm(HttpServletRequest request,
			@Parameter(name="multi", required=false) String multi,
			@Parameter(name="compId", required=false) String compId,// 회사관리에서 다른회사의 관리자 지정할때만 넘어옴
			@Parameter(name="global", required=false) String global,// global=Y : 전사 조직도 보여줌
			@Parameter(name="upward", required=false) String upward,// 상위 부서 선택 시작 조직ID
			@Parameter(name="downward", required=false) String downward,// 하위 부서 선택 시작 조직ID
			@Parameter(name="oneDeptId", required=false) String oneDeptId,// 하나의 부서 - 조직도에 하나의 부서만 표시 할 때
			@Parameter(name="orgId", required=false) String orgId,
			@Parameter(name="mode", required=false) String mode,// foreign : 대외 조직도 - 결재경로의 옵션에 의해 대외 조직도가 표시 될 경우
			@Parameter(name="brother", required=false) String brother,
			ModelMap model) throws Exception {
		String uri = request.getRequestURI();
		// 상위조직 조회, 하위조직 조회
		if(uri.indexOf("/treeUpFrm")>0 || uri.indexOf("/treeDownFrm")>0){
			model.put("callback", "clickApTree");
			if(upward!=null && !upward.isEmpty()) model.put("selectedOrgId", upward);
			else if(downward!=null && !downward.isEmpty()) model.put("selectedOrgId", downward);
		// 전체 부서 조회 - 등록대장
		} else if(uri.indexOf("/treeRecLstOrgFrm")>0){
			model.put("noPart", "Y");
			model.put("callback", "clickRecLstOrg");
			if(orgId!=null && !orgId.isEmpty()){
				model.put("selectedOrgId", orgId);
			} else {
				UserVo userVo = LoginSession.getUser(request);
				model.put("selectedOrgId", userVo.getDeptId());
			}
		}
		
		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, mode, brother, null, model);
	}
	/** [AJAX]부서별 관인 목록 - 관인선택용 */
	@RequestMapping(value = "/ap/box/listOfcSealAjx")
	public String listOfcSealFrm(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String orgId = (String)jsonObject.get("orgId");
		String ofseTypCd = (String)jsonObject.get("ofseTypCd");// 관인구분코드 - 01:관인(기관), 02:서명인(부서)
		// 문서의 어권
		String docLangTypCd = (String)jsonObject.get("docLangTypCd");
		
		// 발신명의 조회
		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setOrgId(orgId);
		orOrgApvDVo.setQueryLang(docLangTypCd);
		orOrgApvDVo = (OrOrgApvDVo)commonSvc.queryVo(orOrgApvDVo);
		if(orOrgApvDVo != null){
			model.put("sendrNmRescId", orOrgApvDVo.getSendrNmRescId());
			model.put("sendrNmRescNm", orOrgApvDVo.getSendrNmRescNm());
		}
		
		// 세션의 어권
		String sessionLangTypCd = LoginSession.getLangTypCd(request);
		
		// 관인, 서명인 목록 조회
		OrOfseDVo orOfseDVo = new OrOfseDVo();
		orOfseDVo.setOrgId(orgId);
		orOfseDVo.setOfseTypCd(ofseTypCd);
		orOfseDVo.setDisuYn("N");
		orOfseDVo.setOrderBy("SEQ");
		orOfseDVo.setQueryLang(sessionLangTypCd);
		@SuppressWarnings("unchecked")
		List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonSvc.queryList(orOfseDVo);
		model.put("orOfseDVoList", orOfseDVoList);
		
		return LayoutUtil.returnJson(model);
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				결재라인
	/** [팝업] 결재라인 - 설정 */
	@RequestMapping(value = {"/ap/box/setApvLnPop", "/ap/adm/box/setApvLnPop"})
	public String setApvLnPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			@Parameter(name="apvLnHstNo", required=false) String apvLnHstNo,
			@Parameter(name="downward", required=false) String downward,
			@Parameter(name="oneDeptId", required=false) String oneDeptId,
			@Parameter(name="modified", required=false) String modified,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 옵션 조회(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		// 옵션설정을 JSON 형태로 Model 에 설정 - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		// apvrFromOtherComp=대외 조직도 사용(타 회사 결재선 지정)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		if("Y".equals(optConfigMap.get("apvrFromOtherComp"))){
			model.put("apvrFromOtherComp", Boolean.TRUE);
		}
		
		// 부재사유코드
		List<PtCdBVo> absRsonCdList = ptCmSvc.getCdList("ABS_RSON_CD", langTypCd, "Y");
		model.put("absRsonCdList", absRsonCdList);
		
		return LayoutUtil.getJspPath("/ap/box/setApvLnPop");
	}
	/** [팝업/프레임] 결재라인 - 선택된 목록 */
	@RequestMapping(value = {"/ap/box/listApvLnFrm","/ap/adm/box/listApvLnFrm"})
	public String listApvLnFrm(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			@Parameter(name="apvLnHstNo", required=false) String apvLnHstNo,
			@Parameter(name="formApvLnTypCd", required=false) String formApvLnTypCd,
			@Parameter(name="makrRoleCd", required=false) String makrRoleCd,
			@Parameter(name="modified", required=false) String modified,
			Locale locale,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 고객사 코드
		model.put("custCode", CustConfig.CUST_CODE);
		
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = null;
		// 결재선 지정이 안된 경우
		boolean isEmptyApvLn = true;
		// 이중 결재의 경우
		boolean isDblApvLn = "apvLnDbl".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd);
		// 부서대기함 여부
		boolean isDeptBx = "deptBx".equals(bxId);
		// 합의기안의 경우
		boolean isMakAgr = isDeptBx && "makAgr".equals(makrRoleCd);
		// 이중결재의 처리부서의 시작자를 변경해야 하는지 - 처리부서가 결재라인에 세팅된 경우
		boolean needChangePrcDept = false;
		// 합의기안자를 변경해야 하는지
		boolean needChangeMakAgr = false;
		
		
		ApOngdApvLnDVo apOngdApvLnDVo = null;
		
		if(apvNo!=null && !apvNo.isEmpty() && !"Y".equals(modified)){
			if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";//최상위 결재 라인
			
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnPno(apvLnPno);
			apOngdApvLnDVo.setStorage(storage);
			//storage
			if(apvLnHstNo!=null && !apvLnHstNo.isEmpty()){
				apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);//결재라인이력번호
				apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
			}
			apOngdApvLnDVo.setOrderBy("APV_NO, APV_LN_PNO, APV_LN_NO");
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apOngdApvLnDVoList2 = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
			apOngdApvLnDVoList = apOngdApvLnDVoList2;
			
			isEmptyApvLn = apOngdApvLnDVoList==null || apOngdApvLnDVoList.isEmpty();
			
			// 부서대기함의 이중결재의 경우 - 부서가 선택된 경우로 - 처리부서를 > 처리부서의 검토자로 변경
			if(isDeptBx && isDblApvLn){
				int index = apOngdApvLnDVoList==null ? -1 : apOngdApvLnDVoList.size()-1;
				while(index>-1){
					apOngdApvLnDVo = apOngdApvLnDVoList.get(index--);
					if("prcDept".equals(apOngdApvLnDVo.getApvrRoleCd())){//prcDept:처리부서
						needChangePrcDept = true;
						// 처리부서가 설정되어 있는 것을 현재 부서대기함에서 작업하는 사용자로 전환
						apOngdApvLnDVo.setApvrDeptYn("N");
						break;
					}
				}
			}
			
			// 부서대기함의 합의기안[재검토]의 경우 - 합의기안자가 현재 부서대기함을 조회한 문담위와 다를 수 있으므로 현재 결재자 체크
			if(isMakAgr && !needChangePrcDept && !isEmptyApvLn){
				needChangeMakAgr = true;
				apOngdApvLnDVo = apOngdApvLnDVoList.get(0);
			}
		}
		
		if(isEmptyApvLn || needChangePrcDept || needChangeMakAgr){
			
			if(isEmptyApvLn){
				apOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
			}
			
			if(!(needChangePrcDept || needChangeMakAgr)){
				apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVoList.add(apOngdApvLnDVo);
			}
			
			if(needChangePrcDept){// 결재라인에 - 처리부서가 설정되었을때 - 처리부서를 현재 작업자로 변경
				apOngdApvLnDVo.setApvrDeptYn("N");
				apOngdApvLnDVo.setApvrRoleCd("revw");
				apOngdApvLnDVo.setApvStatCd("inApv");//inApv:결재중
			} else if(needChangeMakAgr){// 부서합의 - 합의기안자 - 재검토로 올라갈때 합의 기안자가 바뀔수 있으므로
				apOngdApvLnDVo.setApvrRoleCd("makAgr");
				apOngdApvLnDVo.setApvStatCd("inAgr");//inAgr:합의중
			} else if(makrRoleCd!=null && !makrRoleCd.isEmpty()){// 담당, 합의기안자용
				apOngdApvLnDVo.setApvrRoleCd(makrRoleCd);
				apOngdApvLnDVo.setApvStatCd(makrRoleCd.equals("makAgr") ? "inAgr" : "inVw");//inAgr:합의중, inVw:공람중
			} else {
				apOngdApvLnDVo.setApvrRoleCd("mak");
				apOngdApvLnDVo.setApvStatCd("inApv");//inApv:결재중
			}
			
			apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
			apOngdApvLnDVo.setApvrNm(userVo.getUserNm());
			
			OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), langTypCd, null);
			if(orUserBVo!=null){
				apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());//직위코드
				apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());//직책코드
			}
			
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "D", langTypCd);
			apOngdApvLnDVo.setApvDeptId(orOrgTreeVo.getOrgId());
			apOngdApvLnDVo.setApvDeptNm(orOrgTreeVo.getRescNm());//부서명
			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgTreeVo.getOrgAbbrRescNm());//부서약어
			
			if(isDblApvLn){
				// 현재 작업자를 - 이중결재의 처리부서 검토자로 추가해야 하는지 여부
				if(needChangePrcDept){
					apOngdApvLnDVo.setDblApvTypCd("prcDept");// 이중결재 - prcDept:처리부서
				// 이중결재의 기안자일 경우
				} else if("apvLnDbl".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd)){
					apOngdApvLnDVo.setDblApvTypCd("reqDept");// 이중결재 - reqDept:신청부서
				}
			}
		}
		
		apOngdApvLnDVoList.add(new ApOngdApvLnDVo());//UI구성용 HIDDEN
		model.put("apOngdApvLnDVoList", apOngdApvLnDVoList);
		
		// 결재 용어설정을 Model 에 설정 - key : apTermList
		apCmSvc.setApTermList(model, LoginSession.getLocale(request));
		// 옵션설정을 JSON 형태로 Model 에 설정
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		// ap.msg.onlyCan3="{0}", "{1}", "{2}" 만 지정 할 수 있습니다.
		// ap.msg.onlyCanNot3="{0}", "{1}", "{2}" 은(는) 지정 할 수 없습니다.
		String[] terms = new String[]{apCmSvc.getApTerm("fstVw", locale),
				apCmSvc.getApTerm("pubVw", locale),
				apCmSvc.getApTerm("paralPubVw", locale)};
		model.put("onlyCan3", messageProperties.getMessage("ap.msg.onlyCan3", terms, locale));
		model.put("onlyCanNot3", messageProperties.getMessage("ap.msg.onlyCanNot3", terms, locale));
		
		// postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보
		terms = new String[]{
				apCmSvc.getApTerm("postApvd", locale),
				apCmSvc.getApTerm("infm", locale)};
		// ap.msg.deptAgrNotInfm=부서합의에 "{0}", "{1}" 은(는) 지정 할 수 없습니다.
		model.put("deptAgrNotInfm", messageProperties.getMessage("ap.msg.deptAgrNotInfm", terms, locale));
		
		return LayoutUtil.getJspPath("/ap/box/listApvLnFrm");
	}
	/** [팝업/프레임] 참조열람 */
	@RequestMapping(value = "/ap/box/listRefVwFrm")
	public String listRefVwFrm(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="modified", required=false) String modified,
			Locale locale,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 고객사 코드
		model.put("custCode", CustConfig.CUST_CODE);
		
		List<ApOngdRefVwDVo> apOngdRefVwDVoList = null;
		// 결재선 지정이 안된 경우
		boolean isEmptyApvLn = true;
		
//		ApOngdApvLnDVo apOngdApvLnDVo = null;
		ApOngdRefVwDVo apOngdRefVwDVo = null;
		
		if(apvNo!=null && !apvNo.isEmpty() && !"Y".equals(modified)){
			
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			apOngdRefVwDVo = new ApOngdRefVwDVo();
			apOngdRefVwDVo.setApvNo(apvNo);
			apOngdRefVwDVo.setStorage(storage);
			apOngdRefVwDVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<ApOngdRefVwDVo> apOngdRefVwDVoList2 = (List<ApOngdRefVwDVo>)commonSvc.queryList(apOngdRefVwDVo);
			if(apOngdRefVwDVoList2==null || apOngdRefVwDVoList2.isEmpty()){
				isEmptyApvLn = true;
			} else {
				apOngdRefVwDVoList = apOngdRefVwDVoList2;
				isEmptyApvLn = false;
			}
		}
		
		if(isEmptyApvLn){
			apOngdRefVwDVoList = new ArrayList<ApOngdRefVwDVo>();
		}
		
		apOngdRefVwDVoList.add(new ApOngdRefVwDVo());//UI구성용 HIDDEN
		model.put("apOngdRefVwDVoList", apOngdRefVwDVoList);
		
		// 결재 용어설정을 Model 에 설정 - key : apTermList
		apCmSvc.setApTermList(model, LoginSession.getLocale(request));
		// 옵션설정을 JSON 형태로 Model 에 설정
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ap/box/listRefVwFrm");
	}
	/** [팝업] 결재라인 이력조회 */
	@RequestMapping(value = {"/ap/box/viewApvLnHisPop", "/ap/adm/box/viewApvLnHisPop",
			"/dm/doc/viewApvLnHisPop", "/dm/adm/doc/viewApvLnHisPop", "/wd/adm/viewApvLnHisPop"})
	public String viewApvLnHisPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			@Parameter(name="apvLnHstNo", required=false) String apvLnHstNo,
			ModelMap model) throws Exception {

		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
		apOngdApvLnDVo.setStorage(storage);
		apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
		if(apOngdApvLnDVoList != null) model.put("apOngdApvLnDVoList", apOngdApvLnDVoList);
		
		return LayoutUtil.getJspPath("/ap/box/viewApvLnHisPop");
	}

	/** [팝업] 결재라인 조회 - 전체경로, 해당 서브경로 */
	@RequestMapping(value = {"/ap/box/viewApvLnPop","/ap/adm/box/viewApvLnPop",
			"/dm/doc/viewApvLnPop", "/dm/adm/doc/viewApvLnPop", "/wd/adm/viewApvLnPop"})
	public String viewApvLnPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		// 진행문서기본(AP_ONGD_B) 테이블 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setStorage(storage);
		
		// 배부대장, 접수대장 문서의 경우 - 이행 되어온 결재라인 조회
		if(apOngdBVo != null && ("recvRecLst".equals(apOngdBVo.getRecLstTypCd()) || "distRecLst".equals(apOngdBVo.getRecLstTypCd()))){
			apOngdApvLnDVo.setExecution();
		}
		
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
		if(apOngdApvLnDVoList != null) model.put("currApOngdApvLnDVoList", apOngdApvLnDVoList);
		
		return LayoutUtil.getJspPath("/ap/box/viewApvLnPop");
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				의견작성
	/** [팝업] 의견작성 */
	@RequestMapping(value = "/ap/box/setDocOpinPop")
	public String setDocOpinPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 옵션 조회 및 설정(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/ap/box/setDocOpinPop");
	}
	/** [팝업] 상신,승인,반려,찬성,반대 - 서명이미지 + 비밀번호입력 + 의견 */
	@RequestMapping(value = "/ap/box/setDocProsPop")
	public String setDocProsPop(HttpServletRequest request,
			@Parameter(name="apvStatCd", required=false) String apvStatCd,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 옵션 조회 및 설정(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		// apvStatCd:결재상태코드
		//    - byOne:1인결재, mak:기안, befoApv:결재전, inApv:결재중, hold:보류, apvd:승인, rejt:반려, pros:찬성, cons:반대
		// mak:기안, apvd:승인, pros:찬성, cons:반대 - 찍어야 할 도장 조회
		if("mak".equals(apvStatCd) || "apvd".equals(apvStatCd) || "pros".equals(apvStatCd) || "cons".equals(apvStatCd)){
			// 서명 이미지(변수명:orUserImgDVo) 서명방법(변수명:signMthdCd) 조회하여 모델에 세팅
			apDocSvc.setUserSignImg(userVo, userVo.getOdurUid(), model);
		}
		return LayoutUtil.getJspPath("/ap/box/setDocProsPop");
	}
	/** [팝업] 심사 */
	@RequestMapping(value = "/ap/box/setDocCensrPop")
	public String setDocCensrPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		// 심사 요청 의견 조회
		ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd("reqCensr");//reqCensr:심사요청
		apOngdPichDVo.setStorage(storage);
		apOngdPichDVo = (ApOngdPichDVo)commonSvc.queryVo(apOngdPichDVo);
		if(apOngdPichDVo != null){
			model.put("apOngdPichDVo", apOngdPichDVo);
		}
		
		return LayoutUtil.getJspPath("/ap/box/setDocCensrPop");
	}
	/** [팝업] 반송 */
	@RequestMapping(value = "/ap/box/setDocRetnPop")
	public String setDocRetnPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/setDocRetnPop");
	}
	/** [팝업] 반송 의견 조회 */
	@RequestMapping(value = "/ap/box/viewRetnOpinPop")
	public String viewRetnOpinPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="sendSeq", required=false) String sendSeq,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo.setStorage(storage);
		apOngdSendDVo = (ApOngdSendDVo)commonSvc.queryVo(apOngdSendDVo);
		if(apOngdSendDVo!=null){
			model.put("apOngdSendDVo", apOngdSendDVo);
		}
		return LayoutUtil.getJspPath("/ap/box/viewRetnOpinPop");
	}
	/** [팝업] 참조열람 의견 작성 */
	@RequestMapping(value = "/ap/box/setRefVwOpinPop")
	public String setRefVwOpinPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/setRefVwOpinPop");
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				일괄결재
	/** [AJAX] 도장 세팅여부 확인 - 일괄결재 용 */
	@RequestMapping(value = "/ap/box/getStampInfoAjx")
	public String getStampInfoAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		ModelMap checkModel = new ModelMap();
		// 서명 이미지(orUserImgDVo) 서명방법(signMthdCd) 조회하여 모델에 세팅
		apDocSvc.setUserSignImg(userVo, userVo.getOdurUid(), checkModel);
		
		String signMthdCd = (String)checkModel.get("signMthdCd");
		OrUserImgDVo orUserImgDVo = (OrUserImgDVo)checkModel.get("orUserImgDVo");
		
		model.put("signMthdCd", signMthdCd);
		// 이름서명이 아닌경우 - 도장이 필요한 경우
		if(!"03".equals(signMthdCd)){
			// 도장 이미지가 없으면
			if(orUserImgDVo==null || orUserImgDVo.getImgPath()==null || orUserImgDVo.getImgPath().isEmpty()){
				// ap.trans.notProcess={0}가(이) 설정되지 않아서 진행 할 수 없습니다.
				// ap.trans.signStampImg=서명 또는 도장 이미지
				String message = messageProperties.getMessage("ap.trans.notProcess", new String[]{"#ap.trans.signStampImg"}, locale);
				model.put("message", message);
			} else {
				model.put("imgPath", orUserImgDVo.getImgPath());
			}
		}
		return LayoutUtil.returnJson(model);
	}
	/** [팝업] 일괄 결재 */
	@RequestMapping(value = "/ap/box/listBulkApvPop")
	public String listBulkApvPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/listBulkApvPop");
	}
	////////////////////////////////////////////////////////////////////
	//
	//				수신처 지정
	/** [팝업] 수신처 지정 */
	@RequestMapping(value = "/ap/box/setDocRecvDeptPop")
	public String setDocRecvDeptPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="enfcScopCd", required=false) String enfcScopCd,
			@Parameter(name="recvDeptHstNo", required=false) String recvDeptHstNo,
			@Parameter(name="modified", required=false) String modified,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(enfcScopCd!=null && !enfcScopCd.isEmpty()){
			model.put("enfcScopCd", enfcScopCd);
			if("dom".equals(enfcScopCd) || "both".equals(enfcScopCd)){
				model.put("onTab", "dom");//대내 탭 열기
			} else {
				model.put("onTab", "for");//대외 탭 열기
			}
		}
		
//		// 시행범위코드 - 시행범위 변경 기능 제외
//		List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
//		model.put("enfcScopCdList", enfcScopCdList);
		
		//수신처 조회
		if(apvNo!=null && !apvNo.isEmpty() && !"Y".equals(modified)){
			
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			ApOngdBVo apOngdBVo = null;
			if(recvDeptHstNo==null || recvDeptHstNo.isEmpty()){
				// 진행문서 조회
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setStorage(storage);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				
				recvDeptHstNo = apOngdBVo==null ? null : apOngdBVo.getRecvDeptHstNo();
			}
			
			if(recvDeptHstNo!=null && !recvDeptHstNo.isEmpty()){
				ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
				apOngdRecvDeptLVo.setStorage(storage);
				apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);//수신처이력번호
				apOngdRecvDeptLVo.setQueryLang(langTypCd);
				@SuppressWarnings("unchecked")
				List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)commonSvc.queryList(apOngdRecvDeptLVo);
				
				model.put("apOngdRecvDeptLVoList", addEmptyApOngdRecvDeptLVo(apOngdRecvDeptLVoList));
			} else {
				model.put("apOngdRecvDeptLVoList", addEmptyApOngdRecvDeptLVo(null));
			}
		} else {
			model.put("apOngdRecvDeptLVoList", addEmptyApOngdRecvDeptLVo(null));
		}
		
		return LayoutUtil.getJspPath("/ap/box/setDocRecvDeptPop");
	}
	/** UI 구성용 빈 ApOngdRecvDeptLVo 를 더해서 리턴 */
	private List<ApOngdRecvDeptLVo> addEmptyApOngdRecvDeptLVo(List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList){
		if(apOngdRecvDeptLVoList==null) apOngdRecvDeptLVoList = new ArrayList<ApOngdRecvDeptLVo>();
		apOngdRecvDeptLVoList.add(new ApOngdRecvDeptLVo());//UI 구성용
		return apOngdRecvDeptLVoList;
	}
	
	/** [프레임] 수신처 조직도 트리 프레임 */
	@RequestMapping(value = "/ap/box/treeRecvDeptFrm")
	public String treeRecvDeptFrm(HttpServletRequest request,
			@Parameter(name="recvDeptTypCd", required=false) String recvDeptTypCd,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		String multi = "Y";//다중선택 가능
		String compId = null;//해당 회사만 - 회사ID
		String global = null;//모든 회사
		String upward = null;//상위 조직만 - orgId
		String downward = null;//하위 조직만 - orgId
		String oneDeptId = null;
		String orgId = null;
		
		// recvDeptTypCd : 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
		
		if("for".equals(recvDeptTypCd)){// for:대외 면
			global = "Y";
			
		} else if("dom".equals(recvDeptTypCd)){// dom:대내 면
			//[옵션] introScope=대내 조직도 범위 - org:기관, comp:회사
			if("comp".equals(optConfigMap.get("introScope"))){
				compId = userVo.getCompId();
			} else {
				OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "G", langTypCd);
				downward = orOrgTreeVo.getOrgId();
//				downward = userVo.getInstId();
			}
			model.put("selectedOrgId", userVo.getDeptId());
		} else {
			return null;
		}
		
		model.put("callback", "setRecvDeptVo");
		model.put("noPart", "Y");//파트는 보여주지 않음
		//model.put("noInitSelect", Boolean.TRUE);//초기에 자기 부서를 선택 안함
		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, null, null, null, model);
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				배부
	/** [팝업] 배부 */
	@RequestMapping(value = "/ap/box/setDocDistPop")
	public String setDocDistPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="bxId", required=false) String bxId,
			ModelMap model) throws Exception {
		
		if(apvNo!=null && !apvNo.isEmpty() && bxId != null && "distRecLst".equals(bxId)){
			// 스토리지 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			// 진행문서발송상세(AP_ONGD_SEND_D) 테이블 - 목록조회
			ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
			apOngdSendDVo.setApvNo(apvNo);
			apOngdSendDVo.setStorage(storage);
			@SuppressWarnings("unchecked")
			List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonSvc.queryList(apOngdSendDVo);
			if(apOngdSendDVoList==null){
				apOngdSendDVoList = new ArrayList<ApOngdSendDVo>();
			}
			apOngdSendDVoList.add(new ApOngdSendDVo());//UI 구성용 빈 VO 추가
			model.put("apOngdSendDVoList", apOngdSendDVoList);
		} else {
			List<ApOngdSendDVo> apOngdSendDVoList = new ArrayList<ApOngdSendDVo>();
			apOngdSendDVoList.add(new ApOngdSendDVo());//UI 구성용 빈 VO 추가
			model.put("apOngdSendDVoList", apOngdSendDVoList);
		}
		
		return LayoutUtil.getJspPath("/ap/box/setDocDistPop");
	}
	
	/** [프레임] 수신처 조직도 트리 프레임 */
	@RequestMapping(value = "/ap/box/treeDistDeptFrm")
	public String treeDistDeptFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		String multi = "Y";//다중선택 가능
		String compId = null;//해당 회사만 - 회사ID
		String global = null;//모든 회사
		String upward = null;//상위 조직만 - orgId
		
		OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "G", langTypCd);
		String downward = orOrgTreeVo.getOrgId();//하위 조직만 - 기관ID
		//String downward = userVo.getInstId();//하위 조직만 - 기관ID
		String oneDeptId = null;
		String orgId = null;
		
		model.put("callback", "setDistDeptVo");
		model.put("noPart", "Y");//파트는 보여주지 않음
		//model.put("noInitSelect", Boolean.TRUE);//초기에 자기 부서를 선택 안함
		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, null, null, null, model);
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				접수확인 / 배부확인
	/** [팝업] 접수확인 */
	@RequestMapping(value = "/ap/box/setCfrmRecvPop")
	public String setCfrmRecvPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		if(storage != null) model.put("storage", storage);
		
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setStorage(storage);
		apOngdSendDVo.setOrderBy("SEND_SEQ");
		apOngdSendDVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonSvc.queryList(apOngdSendDVo);
		if(apOngdSendDVoList != null){
			model.put("apOngdSendDVoList", apOngdSendDVoList);
			int sendCnt=0, cmplCnt=0, retrvCnt=0, retnCnt=0, befoCnt=0, dupCnt=0;
			String hdlStatCd;
			for(ApOngdSendDVo storedApOngdSendDVo : apOngdSendDVoList){
				sendCnt++;
				hdlStatCd = storedApOngdSendDVo.getHdlStatCd();
				// recvCmpl:접수완료, distCmpl:배부완료, manlCmpl:수동완료,
				if("recvCmpl".equals(hdlStatCd) || "distCmpl".equals(hdlStatCd) || "manlCmpl".equals(hdlStatCd)){
					cmplCnt++;
				// recvRetrv:접수회수, distRetrv:배부회수, manlRetrv:수동회수
				} else if("recvRetrv".equals(hdlStatCd) || "distRetrv".equals(hdlStatCd) || "manlRetrv".equals(hdlStatCd)){
					retrvCnt++;
				// recvRetn:접수반송, distRetn:배부반송, 
				} else if("recvRetn".equals(hdlStatCd) || "distRetn".equals(hdlStatCd)){
					retnCnt++;
				// dupSend:중복발송
				} else if("dupSend".equals(hdlStatCd)){
					dupCnt++;
				// befoRecv:접수대기, befoDist:배부대기, manl:수동발송
				} else {
					befoCnt++;
				}
			}
			model.put("sendCnt", Integer.valueOf(sendCnt));
			model.put("cmplCnt", Integer.valueOf(cmplCnt));
			model.put("retrvCnt", Integer.valueOf(retrvCnt));
			model.put("retnCnt", Integer.valueOf(retnCnt));
			model.put("dupCnt", Integer.valueOf(dupCnt));
			model.put("befoCnt", Integer.valueOf(befoCnt));
		} else {
			Integer zero = Integer.valueOf(0);
			model.put("sendCnt", zero);
			model.put("cmplCnt", zero);
			model.put("retrvCnt", zero);
			model.put("retnCnt", zero);
			model.put("dupCnt", zero);
			model.put("befoCnt", zero);
		}
		
		return LayoutUtil.getJspPath("/ap/box/setCfrmRecvPop");
	}

	////////////////////////////////////////////////////////////////////
	//
	//				결재 비밀번호 + 문서 비밀번호
	/** [AJAX] 결재 비밀번호 / 문서 비밀번호 확인하고 보안ID 발급 */
	@RequestMapping(value = {"/ap/box/getSecuIdAjx", "/ap/adm/box/getSecuIdAjx",
			"/dm/doc/getSecuIdAjx", "/dm/adm/doc/getSecuIdAjx", "/wd/adm/getSecuIdAjx"})
	public String getSecuIdAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			try {
				jsonObject = cryptoSvc.processRsa(request, (String)jsonObject.get("secu"));
			} catch(CmException e){
				//pt.login.fail.decrypt=복호화에 실패하였습니다.
				message = messageProperties.getMessage("pt.login.fail.decrypt", request);
			}
			
			// 전송된 결재 비밀번호
			String apvPw = (String)jsonObject.get("apvPw");
			// 전송된 문서 비밀번호
			String docPw = (String)jsonObject.get("docPw");
			
			if(apvPw!=null && !apvPw.isEmpty()){
				
				// 저장된 결재 비밀번호 조회
				UserVo userVo = LoginSession.getUser(request);
				OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
				orUserPwDVo.setOdurUid(userVo.getOdurUid());
				orUserPwDVo.setPwTypCd("APV");
				orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
				
				if(orUserPwDVo==null || orUserPwDVo.getPwEnc()==null){
					model.put("noPw", "Y");
					return LayoutUtil.returnJson(model);
				} else if(!userVo.isAdminSesn()  && !orUserPwDVo.getPwEnc().equals(cryptoSvc.encryptPw(apvPw, userVo.getOdurUid()))){
					// ap.trans.notSameApvPw=결재비밀번호가 다릅니다.
					throw new CmException("ap.trans.notSameApvPw", request);
				} else {
					model.put("secuId", apDocSecuSvc.createSecuId(request.getSession()));
				}
				
			} else if(docPw!=null && !docPw.isEmpty()){
				
				String apvNo = (String)jsonObject.get("apvNo");
				if(apvNo==null || apvNo.isEmpty()){
					//ap.trans.noSumtData=전송된 {0}가(이) 없습니다. - 결재번호
					throw new CmException("ap.trans.noSumtData", new String[]{"#cols.apvNo"}, request);
				}
				
				// 저장소 조회
				String storage = apCmSvc.queryStorage(apvNo);
				
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setStorage(storage);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				if(apOngdBVo==null){
					//ap.msg.noDoc=해당 문서가 없습니다.
					throw new CmException("ap.msg.noDoc", request);
				}
				if(apOngdBVo.getDocPwEnc()==null || apOngdBVo.getDocPwEnc().isEmpty()){
					//ap.msg.noDocPw=문서 비밀번호가 설정되지 않았습니다.
					throw new CmException("ap.msg.noDocPw", request);
				}
				if(!docPw.equals(cryptoSvc.decryptPersanal(apOngdBVo.getDocPwEnc()))){
					//ap.trans.notSameDocPw=문서비밀번호가 다릅니다.
					throw new CmException("ap.trans.notSameDocPw", request);
				} else {
					model.put("secuId", apDocSecuSvc.createSecuId(request.getSession()));
				}
			} else {
				//ap.trans.noSumtData=전송된 {0}가(이) 없습니다. - 비밀번호
				throw new CmException("ap.trans.noSumtData", new String[]{"#cols.pw"}, request);
			}
			
		} catch(CmException e){
			message = e.getMessage();
		} catch(Exception e){
			message = e.getMessage();
			e.printStackTrace();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	/** [팝업] 문서 비밀번호 입력 팝업 */
	@RequestMapping(value = {"/ap/box/setDocPwPop","/ap/adm/box/setDocPwPop",
			"/dm/doc/setDocPwPop","/dm/adm/doc/setDocPwPop", "/wd/adm/setDocPwPop"})
	public String setDocPwPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		setDmUriBase(request);
		return LayoutUtil.getJspPath("/ap/box/setDocPwPop");
	}
	
	/** 문서관리 링크 처리 */
	private void setDmUriBase(HttpServletRequest request){
		String uri = request.getRequestURI();
		if(uri.startsWith("/dm/doc")){
			request.setAttribute("dmUriBase", "/dm/doc");
		} else if(uri.startsWith("/dm/adm/doc")){
			request.setAttribute("dmUriBase", "/dm/adm/doc");
		}
	}

	////////////////////////////////////////////////////////////////////
	//
	//				양식선택
	/** [팝업] 양식선택 */
	@RequestMapping(value = "/ap/box/selectFormPop")
	public String selectFormPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/selectFormPop");
	}
	/** [팝업] 양식선택 - 양식함 트리 프레임 */
	@RequestMapping(value = "/ap/box/treeFormBxFrm")
	public String treeFormBxFrm(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			ModelMap model) throws Exception {
		return apAdmFormCtrl.treeFormBxFrm(request, formBxId, null, "Y", model);
	}
	/** [팝업] 양식선택 - 양식 프레임 */
	@RequestMapping(value = "/ap/box/listFormFrm")
	public String listFormFrm(HttpServletRequest request,
			@Parameter(name="formBxId", required=false) String formBxId,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="formTypCd", required=false) String formTypCd,
			@Parameter(name="forTrans", required=false) String forTrans,
			ModelMap model) throws Exception {
		
		// 변환용 여부
		if("trans".equals(formTypCd)) forTrans = "Y";
		apAdmFormCtrl.listFormFrm(request, formBxId, schWord, formTypCd, forTrans, "Y", model);
		return LayoutUtil.getJspPath("/ap/box/listFormFrm");
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				공람
	/** [팝업] 공람게시 */
	@RequestMapping(value = "/ap/box/setPubBxPop")
	public String setPubBxPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		// 게시기간
		model.put("pubBxEndYmd", StringUtil.getDiffYmd(30));
		return LayoutUtil.getJspPath("/ap/box/setPubBxPop");
	}

	/** [팝업] 공람게시 */
	@RequestMapping(value = "/ap/box/listPubBxVwPop")
	public String listPubBxVwPop(HttpServletRequest request,
			@Parameter(name="pubBxDeptId", required=false) String pubBxDeptId,//공람부서ID
			@Parameter(name="apvNo", required=false) String apvNo,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		// 진행문서공람게시확인내역(AP_ONGD_PUB_BX_CNFM_L) 테이블 - 조회
		ApOngdPubBxCnfmLVo apOngdPubBxCnfmLVo = new ApOngdPubBxCnfmLVo();
		apOngdPubBxCnfmLVo.setPubBxDeptId(pubBxDeptId);
		apOngdPubBxCnfmLVo.setApvNo(apvNo);
		apOngdPubBxCnfmLVo.setStorage(storage);
		apOngdPubBxCnfmLVo.setOrderBy("VW_DT DESC");
		@SuppressWarnings("unchecked")
		List<ApOngdPubBxCnfmLVo> apOngdPubBxCnfmLVoList = (List<ApOngdPubBxCnfmLVo>)commonSvc.queryList(apOngdPubBxCnfmLVo);
		if(apOngdPubBxCnfmLVoList != null){
			model.put("apOngdPubBxCnfmLVoList", apOngdPubBxCnfmLVoList);
		}
		
		return LayoutUtil.getJspPath("/ap/box/listPubBxVwPop");
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				첨부
	/** [팝업] 첨부 */
	@RequestMapping(value = {"/ap/box/setDocAttchPop", 
			"/ap/box/viewDocAttchPop", "/ap/adm/box/viewDocAttchPop",
			"/dm/doc/viewDocAttchPop", "/dm/adm/doc/viewDocAttchPop", "/wd/adm/viewDocAttchPop"})
	public String setDocAttchPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="attHstNo", required=false) String attHstNo,
			@Parameter(name="refDocHstNo", required=false) String refDocHstNo,
			@Parameter(name="modified", required=false) String modified,
			@Parameter(name="vwMode", required=false) String vwMode,
			ModelMap model) throws Exception {
		
		setDmUriBase(request);
		
		String mode = request.getRequestURI().indexOf("/viewDocAttchPop")>0 ? "view" : "set";
		model.put("mode", mode);
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		boolean trxMode = "trx".equals(vwMode);
		
		String storage = null;
		if("view".equals(mode)){
			storage = apCmSvc.queryStorage(apvNo);
		}
		
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, userVo.getCompId());
		
		if(apvNo!=null && !apvNo.isEmpty() && !"Y".equals(modified)){
			
			String trxApvNo = null;
			ApOngdBVo apOngdBVo = null;
			if(attHstNo==null || attHstNo.isEmpty() || refDocHstNo==null || refDocHstNo.isEmpty() || trxMode){
				// 진행문서 조회
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setStorage(storage);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			}
			
			// 문서 비밀번호 확인 - 비밀번호가 있고 기안자가 아니면
			if(apOngdBVo!=null && apOngdBVo.getDocPwEnc()!=null && !apOngdBVo.getDocPwEnc().isEmpty() 
					&& !userVo.getUserUid().equals(apOngdBVo.getMakrUid())){
				
				String referer = request.getHeader("referer");
				String secuId = request.getParameter("secuId");
				
				if(referer==null || referer.indexOf("&secuId=")<0){
					if(secuId == null || secuId.isEmpty() || !apDocSecuSvc.confirmSecuId(request.getSession(), secuId)){
						//ap.msg.notCfrmDocPw=문서 비밀번호가 확인 되지 않았습니다.
						String message = messageProperties.getMessage("ap.msg.notCfrmDocPw", request);
						model.put("message", message);
						model.put("todo", "dialog.close('setDocAttchDialog');");
						return LayoutUtil.getResultJsp();
					}
				}
			}
			
			if(trxMode){
				attHstNo = "1";
				trxApvNo = apOngdBVo==null ? null : apOngdBVo.getTrxApvNo();
			} else {
				if(attHstNo==null || attHstNo.isEmpty()){
					attHstNo = apOngdBVo==null ? null : apOngdBVo.getAttHstNo();
				}
			}
			
			if(attHstNo!=null && !attHstNo.isEmpty() && !(trxMode && apOngdBVo==null)){
				// 첨부파일 조회
				ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(trxMode ? trxApvNo : apvNo);
				apOngdAttFileLVo.setAttHstNo(attHstNo);
				apOngdAttFileLVo.setStorage(storage);
				@SuppressWarnings("unchecked")
				List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
				model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
			}
			
			if(refDocHstNo==null || refDocHstNo.isEmpty()){
				refDocHstNo = apOngdBVo==null ? null : apOngdBVo.getRefDocHstNo();
			}
			if(refDocHstNo!=null && !refDocHstNo.isEmpty()){
				// 참조문서 조회
				ApOngdBVo refApOngdBVo = new ApOngdBVo();
				refApOngdBVo.setRefApvNo(apvNo);
				refApOngdBVo.setRefDocHstNo(refDocHstNo);//참조문서이력번호
				refApOngdBVo.setStorage(storage);
				refApOngdBVo.setOrderBy("D.SORT_ORDR");
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> refApOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(refApOngdBVo);
				// UI 구성용 빈 VO 추가
				if(refApOngdBVoList == null){
					refApOngdBVoList = new ArrayList<ApOngdBVo>();
				}
				refApOngdBVoList.add(new ApOngdBVo());
				model.put("refApOngdBVoList", refApOngdBVoList);
			}
			
		} else {
			List<ApOngdBVo> refApOngdBVoList = new ArrayList<ApOngdBVo>();
			refApOngdBVoList.add(new ApOngdBVo());
			model.put("refApOngdBVoList", refApOngdBVoList);
		}
		// 등록대장 메뉴ID
		String regRecLstMenuId = apBxSvc.getMenuIdByBxId(userVo, "regRecLst");
		if(regRecLstMenuId != null) model.put("regRecLstMenuId", regRecLstMenuId);
		// 통보함 메뉴ID
		String postApvdBxMenuId = apBxSvc.getMenuIdByBxId(userVo, "postApvdBx");
		if(postApvdBxMenuId != null) model.put("postApvdBxMenuId", postApvdBxMenuId);
		// 접수대장 메뉴ID
		String recvRecLstMenuId = apBxSvc.getMenuIdByBxId(userVo, "recvRecLst");
		if(recvRecLstMenuId != null) model.put("recvRecLstMenuId", recvRecLstMenuId);
		// 기안함 메뉴ID
		String myBxMenuId = apBxSvc.getMenuIdByBxId(userVo, "myBx");
		if(myBxMenuId != null) model.put("myBxMenuId", myBxMenuId);
		
		String uri = request.getRequestURI();
		if(uri.startsWith("/dm/adm/")){
			model.put("apFileModule", ApConstant.DM_ADM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/dm/")){
			model.put("apFileModule", ApConstant.DM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/ap/adm/")){
			model.put("apFileModule", ApConstant.AP_ADM_FILE_MODULE);
		} else {
			model.put("apFileModule", ApConstant.AP_FILE_MODULE);
		}
		
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, userVo.getCompId(), "ap");
				
		return LayoutUtil.getJspPath("/ap/box/setDocAttchPop");
	}
	/** [팝업] 첨부 이력 조회 */
	@RequestMapping(value = {"/ap/box/viewAttHisPop", "/ap/adm/box/viewAttHisPop",
			"/dm/doc/viewAttHisPop", "/dm/adm/doc/viewAttHisPop", "/wd/adm/viewAttHisPop"})
	public String viewAttHisPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="attHstNo", required=false) String attHstNo,
			ModelMap model) throws Exception {
		
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
		apOngdAttFileLVo.setApvNo(apvNo);
		apOngdAttFileLVo.setAttHstNo(attHstNo);
		apOngdAttFileLVo.setStorage(storage);
		@SuppressWarnings("unchecked")
		List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
		if(apOngdAttFileLVoList != null) model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
		
		String uri = request.getRequestURI();
		if(uri.startsWith("/dm/adm/")){
			model.put("apFileModule", ApConstant.DM_ADM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/dm/")){
			model.put("apFileModule", ApConstant.DM_FILE_MODULE);
			model.put("apFileTarget", "Ap");
		} else if(uri.startsWith("/ap/adm/")){
			model.put("apFileModule", ApConstant.AP_ADM_FILE_MODULE);
		} else {
			model.put("apFileModule", ApConstant.AP_FILE_MODULE);
		}
		
		return LayoutUtil.getJspPath("/ap/box/viewAttHisPop");
	}
	/** [팝업] 참조문서 - 등록대장 조회 - 프레임 삽입 페이지 */
	@RequestMapping(value = "/ap/box/listRefDocLstPop")
	public String listRegRecLstPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		// [옵션] 기안함 개인 분류 사용
		if("Y".equals(optConfigMap.get("psnCatEnab"))){
			
		}
		return LayoutUtil.getJspPath("/ap/box/listRefDocLstPop");
	}
	/** [팝업/프레임] 참조문서 - 등록대장 조회 */
	@RequestMapping(value = "/ap/box/listRefDocLstFrm")
	public String listRegRecLstFrm(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="schCat", required=false) String schCat,
			@Parameter(name="schWord", required=false) String schWord,
			@Parameter(name="pageRowCnt", required=false) String pageRowCnt,
			@Parameter(name="refDocBxId", required=false) String refDocBxId,
			ModelMap model) throws Exception {
		// 참조문서 팝업용
		model.put("forRefDocPop", "Y");//페이지별 레코드수 10개로 고정함 - 쿼리에 적용
		if(refDocBxId==null || refDocBxId.isEmpty()) refDocBxId = "regRecLst";
		if("myBx".equals(refDocBxId)){
			model.put("apvdOnly", Boolean.TRUE);
		}
		apBxCtrl.listApvBx(request, response, refDocBxId, null, schCat, schWord, null, null, null, pageRowCnt, model);
		return LayoutUtil.getJspPath("/ap/box/listRefDocLstFrm");
	}
	/** [팝업] 오프라인 발송 - 수동완료 */
	@RequestMapping(value = "/ap/box/setManlSendCmplPop")
	public String setManlSendCmplPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/setManlSendCmplPop");
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {
			"/ap/box/downFile","/ap/adm/box/downFile",
			"/dm/box/downApFile","/dm/adm/box/downApFile","/ap/preview/downFile", "/wd/adm/downApFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "apvNo", required = false) String apvNo,
			@RequestParam(value = "attHstNo", required = false) String attHstNo,
			@RequestParam(value = "attSeq", required = false) String attSeq,
			@RequestParam(value = "intgNo", required = false) String intgNo,//연계 파일 번호
			ModelMap model) throws Exception {
		
		try {
			if (apvNo==null || apvNo.isEmpty() || attSeq==null || attSeq.isEmpty()) {
				if(intgNo==null || intgNo.isEmpty()){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
			
			// 기본 로직 - 연계 아닌 것
			if(intgNo==null || intgNo.isEmpty()){
				
				// baseDir
				String wasCopyBaseDir = distManager.getWasCopyBaseDir();
				if (wasCopyBaseDir == null) {
					distManager.init();
					wasCopyBaseDir = distManager.getWasCopyBaseDir();
				}
				
				// 첨부일련번호
				String[] attSeqs = attSeq.split(",");
				List<ApOngdAttFileLVo> apOngdAttFileLVoList = apDocSvc.getApOngdAttFileLVoList(apvNo, attHstNo, attSeqs);
				
				int size = apOngdAttFileLVoList==null ? 0 : apOngdAttFileLVoList.size();
				
				if(size==0){
					// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
					String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
					LOGGER.error("O ATTCH DATA (apvNo:"+apvNo
							+(attHstNo==null || attHstNo.isEmpty() ? "" : "  attHstNo:"+attHstNo)
							+"  attSeqs:"+ArrayUtil.toString(attSeqs)+")");
					throw new CmException(message);
				} else if(size==1){
					ApOngdAttFileLVo apOngdAttFileLVo = apOngdAttFileLVoList.get(0);
					File file = new File(wasCopyBaseDir+apOngdAttFileLVo.getFilePath());
					if(!file.isFile()){
						// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
						String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
						LOGGER.error("FILE NOT FOUND (apvNo:"+apvNo
								+(attHstNo==null || attHstNo.isEmpty() ? "" : "  attHstNo:"+attHstNo)
								+"  attSeq:"+apOngdAttFileLVo.getAttSeq()
								+"  path:"+wasCopyBaseDir+apOngdAttFileLVo.getFilePath()
								+"  name:"+apOngdAttFileLVo.getAttDispNm()+")");
						
						throw new CmException(message + " : "+apOngdAttFileLVo.getAttDispNm());
					}
					ModelAndView mv = new ModelAndView("fileDownloadView");
					mv.addObject("downloadFile", file);
					mv.addObject("realFileName", apOngdAttFileLVo.getAttDispNm());
					return mv;
					
				} else {
					
					File file, dir = new File(uploadManager.getTempDir());
					if(!dir.isDirectory()) dir.mkdirs();
					File zipFile = new File(dir, ApConstant.DOWN_ZIP_FILE_NAME);
					
					byte[] bytes = new byte[2048];
					FileInputStream fileIn = null;
					ZipEntry zipEntry = null;
					ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile), Charset.forName("UTF-8"));
					
					try {
						for(ApOngdAttFileLVo apOngdAttFileLVo : apOngdAttFileLVoList){
							file = new File(wasCopyBaseDir+apOngdAttFileLVo.getFilePath());
							if(file.isFile()){
								
								fileIn = new FileInputStream(file);
								zipEntry = new ZipEntry(apOngdAttFileLVo.getAttDispNm());
								zipOut.putNextEntry(zipEntry);
								zipOut.setLevel(9);
								
								while ((size = fileIn.read(bytes)) != -1) {
									zipOut.write(bytes, 0, size);
								}
								fileIn.close();
								fileIn = null;
								zipOut.closeEntry();
								
							} else {
								// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
								String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
								LOGGER.error("FILE NOT FOUND (apvNo:"+apvNo
										+(attHstNo==null || attHstNo.isEmpty() ? "" : "  attHstNo:"+attHstNo)
										+"  attSeq:"+apOngdAttFileLVo.getAttSeq()
										+"  path:"+wasCopyBaseDir+apOngdAttFileLVo.getFilePath()
										+"  name:"+apOngdAttFileLVo.getAttDispNm()+")");
								zipOut.close();
								zipOut = null;
								
								throw new CmException(message + " : "+apOngdAttFileLVo.getAttDispNm());
							}
						}
						zipOut.close();
						zipOut = null;
						
					} finally {
						if (zipOut != null) try { zipOut.close(); } catch (Exception e) {}
					}

					ModelAndView mv = new ModelAndView("fileDownloadView");
					mv.addObject("downloadFile", zipFile);
					mv.addObject("realFileName", zipFile.getName());
					return mv;
				}
				
			// 연계에 의한 파일 조회 - DB에서 조회해서
			} else {
				
				ApErpIntgFileDVo apErpIntgFileDVo = new ApErpIntgFileDVo();
				apErpIntgFileDVo.setIntgNo(intgNo);
				apErpIntgFileDVo.setSeq(attSeq);
//				apErpIntgFileDVo.setWithLob(true);
				apErpIntgFileDVo = (ApErpIntgFileDVo)commonSvc.queryVo(apErpIntgFileDVo);
				
				if(apErpIntgFileDVo==null){
					LOGGER.error("INTG FILE NOT FOUND (intgNo:"+intgNo+"  seq:"+attSeq);
					// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
					String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
					throw new CmException(message);
				}
				
				ModelAndView mv = new ModelAndView("fileDownloadView");
				mv.addObject("realFileName", apErpIntgFileDVo.getAttDispNm());
				//mv.addObject("downloadBytes", apErpIntgFileDVo.getFileCont());
				
				mv.addObject("lobHandler", lobHandler.create(
						"SELECT FILE_CONT FROM AP_ERP_INTG_FILE_D WHERE INTG_NO = ? AND SEQ = ?", 
						new String[]{intgNo, attSeq}
				));
				return mv;
			}
			
		} catch (CmException e) {
			//LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("/cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mv = new ModelAndView("/cm/result/commonResult");
			mv.addObject("message", e.getClass().getCanonicalName()+" : "+ e.getMessage());
			return mv;
		}
	}
	
	/** [팝업] 오프라인 발송 - 수동완료 */
	@RequestMapping(value = "/ap/adm/box/setSeculCdPop")
	public String setSeculCdPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
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
		
		return LayoutUtil.getJspPath("/ap/adm/box/setSeculCdPop");
	}
	/** [AJAX] 참조문서 본문 조회 */
	@RequestMapping(value = "/ap/box/getRefBodyAjx")
	public String getRefBodyAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		try {
			
			String refApvNos = (String)jsonObject.get("refApvNos");
			if(refApvNos==null || refApvNos.isEmpty()){
				// ap.trans.noSumtData=전송된 {0}가(이) 없습니다. - 결재번호
				message = messageProperties.getMessage("ap.trans.noSumtData", new String[]{"#cols.apvNo"}, locale);
				model.put("message", message);
				return LayoutUtil.returnJson(model, message);
			}
			
			String bodyHtml;
			Map<String, String> apvBodyMap;
			ApOngdBVo apOngdBVo;
			ApOngdBodyLVo apOngdBodyLVo;
			
			for(String refApvNo : refApvNos.split(",")){
				
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(refApvNo);
				apOngdBVo.setQueryLang(langTypCd);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				
				if(apOngdBVo != null){
					
					apOngdBodyLVo = new ApOngdBodyLVo();
					apOngdBodyLVo.setApvNo(refApvNo);
					apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
					apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
					bodyHtml = apOngdBodyLVo==null || apOngdBodyLVo.getBodyHtml()==null ? "" : apOngdBodyLVo.getBodyHtml();
					
					apvBodyMap = new HashMap<String, String>();
					apvBodyMap.put("cmplDt", apOngdBVo.getCmplDt());
					apvBodyMap.put("bodyHtml", bodyHtml);
					
					model.put(refApvNo, apvBodyMap);
				}
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	/** [AJAX] 현재 라인 외의 결재라인의 사용자 조회 */
	@RequestMapping(value = {"/ap/box/getOtherApvLnUidAjx", "/ap/adm/box/getOtherApvLnUidAjx"})
	public String getOtherApvLnUidAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		// 부서합의 의 경우 - 상위 결재라인에 있는 사용자(개인통보 제외)를 추가하는 것을 방지하기 위해
		//				상위결재라인의 사용자를 조회 하는 함수
		
		String message = null;
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		try {
			
			String apvNo = (String)jsonObject.get("apvNo");
			String apvLnPno = (String)jsonObject.get("apvLnPno");
			
			if(apvNo!=null && !apvNo.isEmpty() && apvLnPno!=null && !apvLnPno.isEmpty()){
				
				ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVo.setApvNo(apvNo);
				apOngdApvLnDVo.setQueryLang(langTypCd);
				
				boolean appended = false;
				StringBuilder builder = new StringBuilder(64);
				
				@SuppressWarnings("unchecked")
				List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
				if(apOngdApvLnDVoList!=null){
					for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
						
						if(!"Y".equals(storedApOngdApvLnDVo.getApvrDeptYn())
								&& !"psnInfm".equals(storedApOngdApvLnDVo.getApvrRoleCd()) // psnInfm:개인통보
								&& !apvLnPno.equals(storedApOngdApvLnDVo.getApvLnPno())){ // - 자신의 결재라인 제외
							if(appended) builder.append(',');
							else appended = true;
							builder.append(storedApOngdApvLnDVo.getApvrUid());
						}
					}
				}
				
				if(appended){
					model.put("apvrUids", builder.toString());
				}
			}
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		
		return LayoutUtil.returnJson(model, message);
	}
	/** [AJAX] 메일 보내기 - 메일 보내기 테이블에 데이터 저장 */
	@RequestMapping(value = {"/ap/box/transEmailAjx", "/ap/adm/box/transEmailAjx"})
	public String transEmailAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String bxId = (String)object.get("bxId");
			String apvNo = (String)object.get("apvNo");
			if (bxId == null || apvNo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 진행문서기본
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setQueryLang(langTypCd);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			
			if(apOngdBVo==null){
				//cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 본문조회
			ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
			apOngdBodyLVo.setApvNo(apvNo);
			apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
			apOngdBodyLVo.setQueryLang(langTypCd);
			apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
			
			// 첨부 조회
			ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apvNo);
			apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
			apOngdAttFileLVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
			
			// 메일 조회
			String email = null;
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			orUserPinfoDVo.setOdurUid(userVo.getOdurUid());
			orUserPinfoDVo.setQueryLang(langTypCd);
			orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
			if(orUserPinfoDVo!=null && orUserPinfoDVo.getEmailEnc()!=null){
				email = cryptoSvc.decryptPersanal(orUserPinfoDVo.getEmailEnc());
			}
			
			// 메일ID 생성
			Integer emailId = commonSvc.nextVal("CM_EMAIL_B").intValue();
			
			QueryQueue queryQueue = new QueryQueue();
			
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			cmEmailBVo.setEmailId(emailId);
			cmEmailBVo.setSubj(apOngdBVo.getDocSubj());
			if(email!=null) cmEmailBVo.setSendNm("\""+userVo.getUserNm()+"\" <"+email+">");
			if(apOngdBodyLVo!=null){
				// 시스템 설정
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				// 첫 줄 삽입여부
				boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));						
				// 첫 줄 
				String cont=isFirstRow ? "<p><br/></p>" : "";
				
				if(apOngdBodyLVo.getBodyHtml()!=null)
					cont+=apOngdBodyLVo.getBodyHtml();
				if(!cont.isEmpty()) cmEmailBVo.setCont(cont);
				
				/*if(apOngdBodyLVo.getBodyHtml()==null){
					cmEmailBVo.setCont("<p><br/></p>");
				} else {
					cmEmailBVo.setCont("<p><br/></p>"+apOngdBodyLVo.getBodyHtml());
				}*/
			}
			
			//file
			cmEmailBVo.setRegDt("sysdate");
			queryQueue.insert(cmEmailBVo);
			
			boolean hasFile = false;
			
			File file;
			FileInputStream in;
			ByteArrayOutputStream out;
			int len = 0;
			byte[] bytes = new byte[1024 * 4], fileBytes;
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			
			Integer dispOrdr = 1;
			CmEmailFileDVo cmEmailFileDVo;
			if(apOngdAttFileLVoList != null){
				for(ApOngdAttFileLVo storedApOngdAttFileLVo : apOngdAttFileLVoList){
					
					file = new File(wasCopyBaseDir + storedApOngdAttFileLVo.getFilePath());
					if(!file.isFile()) continue;
					
					cmEmailFileDVo = new CmEmailFileDVo();
					cmEmailFileDVo.setEmailId(emailId);
					cmEmailFileDVo.setDispOrdr(dispOrdr);
					cmEmailFileDVo.setDispNm(storedApOngdAttFileLVo.getAttDispNm());
					cmEmailFileDVo.setFileExt(storedApOngdAttFileLVo.getFileExt());
					dispOrdr++;
					
					in = new FileInputStream(file);
					out = new ByteArrayOutputStream();
					while((len = in.read(bytes, 0, 1024 * 4)) > 0){
						out.write(bytes, 0, len);
					}
					
					fileBytes = out.toByteArray();
					cmEmailFileDVo.setFileSize((long)fileBytes.length);
					cmEmailFileDVo.setFileCont(fileBytes);
					queryQueue.insert(cmEmailFileDVo);
					hasFile = true;
				}
			}
			
			// 파일여부
			cmEmailBVo.setFileYn(hasFile ? "Y" : "N");
			
			commonSvc.execute(queryQueue);
			
			model.put("emailId", emailId);
			model.put("result", "ok");
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
	/** [팝업] 업무관리 양식 결재 - 내용 수정 */
	@RequestMapping(value = "/ap/box/setWfFormBodyPop")
	public String setWfFormBodyPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String formId = (String)jsonObject.get("formId");
		String wfFormNo = (String)jsonObject.get("wfFormNo");
		String wfWorkNo = (String)jsonObject.get("wfWorkNo");
		
		model.put("includeJsp", ApConstant.WF_REG_PATH+".jsp");
		wfMdFormSvc.setFormByAP(request, model, wfFormNo, formId, wfWorkNo, false);
		
		return "ap/box/setWfFormBodyPop";
	}
	
	/** [팝업] ERP 양식 결재 - 내용 수정 */
	@RequestMapping(value = "/ap/box/setFormBodyXMLPop")
	public String setFormBodyXMLPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String erpFormId = (String)jsonObject.get("erpFormId");
		String formBodyXML = (String)jsonObject.get("formBodyXML");
		
		ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
		apErpFormBVo.setErpFormId(erpFormId);
		apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
		
		String regUrl = apErpFormBVo==null ? null : apErpFormBVo.getRegUrl();
		if(regUrl!=null && regUrl.endsWith(".jsp")){
			
			if(formBodyXML==null || formBodyXML.isEmpty()){
				ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
				apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				
				if(apOngdErpFormDVo == null){
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
					apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				}
				
				if(apOngdErpFormDVo!=null) formBodyXML = apOngdErpFormDVo.getErpVa();
			}
			
			model.put("formBodyMode", "pop");
			XMLElement xmlElement = (formBodyXML==null || formBodyXML.isEmpty()) ?
					new XMLElement(null) : SAXHandler.parse(formBodyXML);
			model.put("formBodyXML", xmlElement);
			
			// 연결 문서 확인
			String xmlTypId = xmlElement.getAttr("head.typId");
			if(xmlTypId!=null && xmlTypId.startsWith("linked")){
				
				String erpLinkedApvNo = xmlElement.getAttr("body/linked.erpLinkedApvNo");
				
				if(erpLinkedApvNo != null && !erpLinkedApvNo.isEmpty()){
					ApOngdBVo apOngdBVo = new ApOngdBVo();
					apOngdBVo.setApvNo(erpLinkedApvNo);
					apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
					if(apOngdBVo != null){
						model.put("linkedApOngdBVo", apOngdBVo);
					}
				}
			}
			
			return regUrl.substring(0, regUrl.length()-4);
		}
		
		// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.formCmpt=폼 양식
		String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#ap.formCmpt"}, locale);
		model.put("message", message);
		return LayoutUtil.returnJson(model);
	}
	
	/** [팝업] 발송 - 참조문서 유지 - 옵션 설정 시 */
	@RequestMapping(value = "/ap/box/setSendDocCfrmPop")
	public String setSendDocCfrm(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/setSendDocCfrmPop");
	}
	
	/** [팝업] 참조열람 */
	@RequestMapping(value = "/ap/box/setRefVwPop")
	public String setRefVwPop(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		// apvrFromOtherComp=대외 조직도 사용(타 회사 결재선 지정)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		if("Y".equals(optConfigMap.get("apvrFromOtherComp"))){
			model.put("apvrFromOtherComp", Boolean.TRUE);
		}
		
		return LayoutUtil.getJspPath("/ap/box/setRefVwPop");
	}
	/** [팝업] 되돌리기 */
	@RequestMapping(value = "/ap/adm/box/setTurnBackPop")
	public String setTurnBackPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			Locale locale, ModelMap model) throws Exception {
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			// ap.msg.noDoc=해당 문서가 없습니다.
			throw new CmException("ap.msg.noDoc", locale);
		}
		if(!"ongo".equals(apOngdBVo.getDocProsStatCd())){
			// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
			// cm.btn.turnBack=되돌리기
			throw new CmException("ap.msg.not.stat", new String[]{"#cm.btn.turnBack"}, locale);
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setOrderBy("APV_LN_PNO, APV_LN_NO");
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
		
		model.put("apOngdApvLnDVoList", apOngdApvLnDVoList);
		
		return LayoutUtil.getJspPath("/ap/box/setTurnBackPop");
	}

	/** [AJAX] 시행변환 양식 검증 */
	@RequestMapping(value = {"/ap/box/checkEnfcFormAjx", "/ap/adm/box/checkEnfcFormAjx"})
	public String checkEnfcFormAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String fromFormId = (String)object.get("fromFormId");
			String toFormId = (String)object.get("toFormId");
			
			ApFormBVo fromApFormBVo = new ApFormBVo();
			fromApFormBVo.setFormId(fromFormId);
			fromApFormBVo = (ApFormBVo)commonSvc.queryVo(fromApFormBVo);
			
			ApFormBVo toApFormBVo = new ApFormBVo();
			toApFormBVo.setFormId(toFormId);
			toApFormBVo = (ApFormBVo)commonSvc.queryVo(toApFormBVo);
			
			//ap.msg.noFormSelected=선택된 양식이 없습니다.
			if(toApFormBVo==null){
				message = messageProperties.getMessage("ap.msg.noFormSelected", locale);
			}
			
			String fromErpFormId = fromApFormBVo.getErpFormId();
			String toErpFormId = toApFormBVo.getErpFormId();
			if(fromErpFormId==null || fromErpFormId.isEmpty() || "xmlFromErp".equals(fromApFormBVo.getErpFormTypCd())){
				if(toErpFormId==null || toErpFormId.isEmpty() || "xmlFromErp".equals(toApFormBVo.getErpFormTypCd())){
					model.put("result", "ok");
				} else {
					// ap.msg.notTrxForm=변환 될 수 없는 양식 입니다.
					message = messageProperties.getMessage("ap.msg.notTrxForm", locale);
					model.put("message", message);
				}
				return LayoutUtil.returnJson(model);
			}
			
			// [결재옵션]
			UserVo userVo = LoginSession.getUser(request);
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
			// [옵션] 폼양식 에디트 양식으로 전환 
			if("Y".equals(optConfigMap.get("formToEditEnabled"))){
				if(toErpFormId==null || toErpFormId.isEmpty() || "xmlFromErp".equals(toApFormBVo.getErpFormTypCd())){
					model.put("result", "ok");
					return LayoutUtil.returnJson(model);
				}
			}
			
			ApErpFormBVo fromApErpFormBVo = null;
			if(!(fromErpFormId==null || fromErpFormId.isEmpty())){
				fromApErpFormBVo = new ApErpFormBVo();
				fromApErpFormBVo.setErpFormId(fromErpFormId);
				fromApErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(fromApErpFormBVo);
			}
			
			ApErpFormBVo toApErpFormBVo = null;
			if(!(toErpFormId==null || toErpFormId.isEmpty())){
				toApErpFormBVo = new ApErpFormBVo();
				toApErpFormBVo.setErpFormId(toErpFormId);
				toApErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(toApErpFormBVo);
			}
			
			String fromErpFormTypCd = fromApErpFormBVo==null ? null : fromApErpFormBVo.getErpFormTypCd();
			String toErpFormTypCd   = toApErpFormBVo  ==null ? null :   toApErpFormBVo.getErpFormTypCd();
			
			String fromRegUrl = fromApErpFormBVo==null ? null : fromApErpFormBVo.getRegUrl();
			String toRegUrl   = toApErpFormBVo  ==null ? null :   toApErpFormBVo.getRegUrl();
			
			if("wfForm".equals(fromErpFormTypCd)){
				if(!"wfForm".equals(toErpFormTypCd)){
					// ap.msg.notTrxForm=변환 될 수 없는 양식 입니다.
					message = messageProperties.getMessage("ap.msg.notTrxForm", locale);
				}
				int p, q;
				String fromFormNo = null;
				p = fromRegUrl==null ? -1 : fromRegUrl.indexOf("formNo=");
				q = fromRegUrl==null ? -1 : fromRegUrl.indexOf("&", p+1);
				if(p>0 && q>p){
					fromFormNo = fromRegUrl.substring(p+7, q);
				}
				String toFormNo = null;
				p = toRegUrl==null ? -1 : toRegUrl.indexOf("formNo=");
				q = toRegUrl==null ? -1 : toRegUrl.indexOf("&", p+1);
				if(p>0 && q>p){
					toFormNo = toRegUrl.substring(p+7, q);
				}
				if(fromFormNo!=null && !fromFormNo.equals(toFormNo)){
					// ap.msg.notTrxForm=변환 될 수 없는 양식 입니다.
					message = messageProperties.getMessage("ap.msg.notTrxForm", locale);
				}
			} else if("xmlFromAp".equals(fromErpFormTypCd) || "xmlEditFromAp".equals(fromErpFormTypCd)){
				if(!fromErpFormTypCd.equals(toErpFormTypCd)){
					// ap.msg.notTrxForm=변환 될 수 없는 양식 입니다.
					message = messageProperties.getMessage("ap.msg.notTrxForm", locale);
				}
				if(fromRegUrl!=null && !fromRegUrl.equals(toRegUrl)){
					// ap.msg.notTrxForm=변환 될 수 없는 양식 입니다.
					message = messageProperties.getMessage("ap.msg.notTrxForm", locale);
				}
			}
			
			if(message == null){
				model.put("result", "ok");
			} else {
				model.put("message", message);
			}
			return LayoutUtil.returnJson(model, message);
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [AJAX] 시행변환 양식 검증 */
	@RequestMapping(value = {"/ap/box/checkCancelCmplAjx", "/ap/adm/box/checkCancelCmplAjx"})
	public String checkCancelCmplAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		
		String message = null;
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String apvNo = (String)object.get("apvNo");
			
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			
			if(apOngdBVo == null) {
				// ap.msg.noDoc=해당 문서가 없습니다.
				message = messageProperties.getMessage("ap.msg.noDoc", locale);
				model.put("message", message);
				LayoutUtil.returnJson(model, message);
			}
			
			ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
			apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
			
			boolean isNotiDoc = apErpNotiSvc.isErpNotiFromAp(apOngdBVo, apOngdErpFormDVo==null ? null : apOngdErpFormDVo.getErpVa());
			model.put("notiDoc", isNotiDoc ? "Y" : "N");
			
			return LayoutUtil.returnJson(model, message);
			
		} catch(Exception e){
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
}
