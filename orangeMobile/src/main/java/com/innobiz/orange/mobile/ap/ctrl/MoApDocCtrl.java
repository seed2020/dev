package com.innobiz.orange.mobile.ap.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocSecuSvc;
import com.innobiz.orange.web.ap.svc.ApDocSvc;
import com.innobiz.orange.web.ap.svc.ApFormSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApErpIntgChitDVo;
import com.innobiz.orange.web.ap.vo.ApFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormTxtDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.ap.vo.ApOngdPichDVo;
import com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo;
import com.innobiz.orange.web.ap.vo.ApOngdSendDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormApvLnDVo;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 문서 컨트롤러 - 결재 */
@Controller
public class MoApDocCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoApDocCtrl.class);

//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;
	
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
	
//	/** 결재함 컨트롤러 */
//	@Autowired
//	private MoApBxCtrl moApBxCtrl;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 결재 문서 보안 서비스 */
	@Autowired
	private ApDocSecuSvc apDocSecuSvc;
	
//	/** 문서 저장 서비스 */
//	@Autowired
//	private ApDocTransSvc apDocTransSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
//	/** 결재 약식 관리 Ctrl */
//	@Autowired
//    private ApAdmFormCtrl apAdmFormCtrl;
//
//	/** 조직 사용자 컨트롤러 */
//	@Autowired
//	private OrOrgCtrl orOrgCtrl;
	
//	/** 문서번호 체번 서비스 */
//	@Autowired
//	private ApDocNoSvc apDocNoSvc;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	////////////////////////////////////////////////////////////////////
	//
	//				문서작성 / 문서조회
	/** 작성 및 조회 */
	@RequestMapping(value = {"/ap/box/viewDoc", "/ap/box/viewDocSub", "/ap/box/viewDocHisSub", "/dm/doc/viewApDocSub"})
	public String viewDoc(HttpServletRequest request,
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
			@Parameter(name="selectedDocs", required=false) String selectedDocs,//선택열기용 목록
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="msgId", required=false) String msgId,// 모바일 푸쉬 - 메세지ID
			@Parameter(name="intgTypCd", required=false) String intgTypCd,// 통합유형코드- 참조문서의 경우 - ERP_CHIT 넘어옴
			Locale locale,
			ModelMap model) throws Exception {
		
		// 서브 페이지 여부 - 참조 문서의 경우
		boolean isSub = false;
		// 이력 조회 페이지 여부
		boolean isHis = false;
		// 전표보기 여부
		boolean isChit = false;
		String uri = request.getRequestURI();
		if(uri.indexOf("/viewDocSub")>0){
			isSub = true;
			model.put("isSub", "Y");
			if("ERP_CHIT".equals(intgTypCd)){
				isChit = true;
				model.put("isChit", "Y");
				// ap.btn.viewChit=전표보기
				model.put("UI_TITLE", messageProperties.getMessage("ap.btn.viewChit", locale));
			} else {
				// map.sub.viewRefDoc=참조 문서
				model.put("UI_TITLE", messageProperties.getMessage("map.sub.viewRefDoc", locale));
			}
		} else if(uri.indexOf("/viewDocHisSub")>0){
			isHis = true;
			model.put("isHis", "Y");
			// map.sub.viewModHis=변경 이력
			model.put("UI_TITLE", messageProperties.getMessage("map.sub.viewModHis", locale));
		// 문서관리에 저장된 결재문서
		} else if(uri.indexOf("/viewApDocSub")>0){
			isSub = true;
			model.put("isSub", "Y");
			model.put("isDM", "Y");
			
			if("ERP_CHIT".equals(intgTypCd)){
				isChit = true;
				model.put("isChit", "Y");
				// ap.btn.viewChit=전표보기
				model.put("UI_TITLE", messageProperties.getMessage("ap.btn.viewChit", locale));
			} else {
				// map.sub.viewApDoc=결재 문서 조회
				model.put("UI_TITLE", messageProperties.getMessage("map.sub.viewApDoc", locale));
			}
		}
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		try {
			
			if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
			
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
			// 시행문 변환 용 - 원본 결재번호
			} else if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
				apvNo = orgnApvNo;
			}
			
			// 결재번호가 있으면 TABLE 조회 - 양식 일련번호 조회용
			if(apvNo!=null && !apvNo.isEmpty()){
				
				// 원문보기, 변환문보기 전환용
				if(vwMode!=null && ("trx".equals(vwMode) || "orgn".equals(vwMode))){
					model.put("vwMode", vwMode);
				}
				
				boolean forDetlPop = false;// 상세보기 팝업용은 - 결재방, 첨부, 참조문서 등의 데이터 조회 안함
				
				// 진행중인 문서 조회
				apOngdBVo = apDocSvc.getOngoDoc(apvNo, apvLnPno, apvLnNo,
						sendSeq, bxId, pubBxDeptId,
						forDetlPop, refdBy,
						userVo, locale, model);
				
				// 푸쉬메세지에 의한 열람
				if(msgId!=null && !msgId.isEmpty()){
					// 자신의 경로를 찾음
					ApOngdApvLnDVo myApOngdApvLnDVo = (ApOngdApvLnDVo)model.get("myApOngdApvLnDVo");
					if(myApOngdApvLnDVo!=null){
						
						//결재상태코드
						// befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
						// befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, 
						// hold:보류, cncl:취소, reRevw:재검토, 
						// inInfm:통보중, 
						// befoVw:공람전, inVw:공람중, cmplVw:공람완료
						if(ArrayUtil.isInArray(new String[]{"apvd", "rejt", "cons", "pros", "cmplVw"},
								myApOngdApvLnDVo.getApvStatCd())){
							
							// ap.msg.alreadyDone=이미 처리한 문서 입니다.
							model.put("notiMsg", messageProperties.getMessage("ap.msg.alreadyDone", locale));
						}
						
					}
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
					// 저장된 결재 데이터 가 있으면 결재 데이터의 양식 정보 조회
					if(apOngdBVo.getFormId()!=null && orgnApvNo==null){
						formId = apOngdBVo.getFormId();
						// 양식일련번호가 있으면 - 진행중인 결재에 해당 - 진행결재양식 조회
						if(apOngdBVo.getFormSeq()!=null){
							formSeq = apOngdBVo.getFormSeq();
						}
					}
					
					// 문서 비밀번호 확인 - 비밀번호가 있고 기안자가 아니면
					if(apOngdBVo.getDocPwEnc()!=null && !apOngdBVo.getDocPwEnc().isEmpty() 
							&& !userVo.getUserUid().equals(apOngdBVo.getMakrUid())){
						
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
							if("paper".equals(apOngdBVo.getDocTypCd())){
								model.put("noPw", "Y");
								return MoLayoutUtil.getJspPath("/ap/box/viewPaperDoc");
							}
							return MoLayoutUtil.getJspPath("/ap/box/viewDocPwCfrm");
//							if(isFrame){
//								return MoLayoutUtil.getJspPath("/ap/box/viewDocPwCfrmFrm");
//							} else {
//								return MoLayoutUtil.getJspPath("/ap/box/viewDocPwCfrm");
//							}
						}
					}
					
					if("paper".equals(apOngdBVo.getDocTypCd())){
						model.put("viewPaper", "Y");
						return MoLayoutUtil.getJspPath("/ap/box/viewPaperDoc");
					}
				}
			}
			
			// 양식 조회
			if(formId!=null && !formId.isEmpty()){
				
				// 진행중인 결재 양식 조회
				if(formSeq!=null && !formSeq.isEmpty()){
					
					// 진행결재양식 조회 : 양식일련번호가 있으면 - 진행중인 결재에 해당
					apFormSvc.setOngoForm(formId, formSeq, request, model);
					
					// 양식 조회 후 기본 데이터 만들기
					@SuppressWarnings("unchecked")
					Map<String, Object> apvData = (Map<String, Object>)model.get("apvData");
					if(apvData==null){
						apvData = new HashMap<String, Object>();
						model.put("apvData", apvData);
					}
					
					// 전표보기 세팅
					if(isChit){
						String intgNo = (String)apvData.get("intgNo");
						if(intgNo!=null && !intgNo.isEmpty()){
							ApErpIntgChitDVo apErpIntgChitDVo = new ApErpIntgChitDVo();
							apErpIntgChitDVo.setIntgNo(intgNo);
							apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
							
							// 양식명 - 전표의 제목으로
							String formNm = apErpIntgChitDVo.getFormNm();
							if(formNm!=null && !formNm.isEmpty()){
								apvData.put("docSubj", formNm);
							}
							// 전표본문HTML - 전표의 본문HTML로
							String chitBodyHtml = apErpIntgChitDVo.getChitBodyHtml();
							if(chitBodyHtml!=null && !chitBodyHtml.isEmpty()){
								apvData.put("bodyHtml", chitBodyHtml);
							}
						}
					}
					
				// 시행변환용 양식 조회
				} else if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
					
					// 결재양식 조회
					apFormSvc.setSetupForm(formId, langTypCd, request, model);
					
				// 결재양식 조회
				} else {
					
					// 결재양식 조회
					apFormSvc.setSetupForm(formId, langTypCd, request, model);
					
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
						
					ApFormTxtDVo apFormTxtDVo = (ApFormTxtDVo)model.get("docName");
					if(apFormTxtDVo!=null) apvData.put("formNm", apFormTxtDVo.getTxtCont());//양식명
					
					apFormTxtDVo = (ApFormTxtDVo)model.get("docSender");
					if(apFormTxtDVo!=null) apvData.put("sendrNmRescNm", apFormTxtDVo.getTxtCont());//발신명의
					
					ApFormBVo apFormBVo = (ApFormBVo)model.get("apFormBVo");
					if(apFormBVo!=null){
						apvData.put("bodyHtml", apFormBVo.getBodyHtml());//본문HTML
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
						
					}
					
					// 결재 라인정보 세팅 - 실제 문서용
					// 도장방 - 4곳 >> [apv:결재], [agr:합의], [req:신청], [prc:처리]
					setSignDispArea(model, formId, false);
					
					// 참조 기안일때 - 본문, 첨부 조회
					if(refDocApvNo != null && !refDocApvNo.isEmpty()){
						apDocSvc.setRefDocMak(refDocApvNo, apvData, model);
					}
					
					// 전표보기 세팅
					if(isChit){
						String intgNo = (String)apvData.get("intgNo");
						if(intgNo!=null && !intgNo.isEmpty()){
							ApErpIntgChitDVo apErpIntgChitDVo = new ApErpIntgChitDVo();
							apErpIntgChitDVo.setIntgNo(intgNo);
							apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
							
							// 양식명 - 전표의 제목으로
							String formNm = apErpIntgChitDVo.getFormNm();
							if(formNm!=null && !formNm.isEmpty()){
								apvData.put("docSubj", formNm);
							}
							// 전표본문HTML - 전표의 본문HTML로
							String chitBodyHtml = apErpIntgChitDVo.getChitBodyHtml();
							if(chitBodyHtml!=null && !chitBodyHtml.isEmpty()){
								apvData.put("bodyHtml", chitBodyHtml);
							}
						}
					}
				}
			}
			
			String regRecLstMenuId = apBxSvc.getMenuIdByBxId(userVo, "regRecLst");
			if(regRecLstMenuId != null) model.put("regRecLstMenuId", regRecLstMenuId);
			
			model.put("apFileModule", ApConstant.AP_FILE_MODULE);
			
			// 다운로드|문서뷰어 사용여부
			emAttachViewSvc.chkAttachSetup(model, userVo.getCompId());
						
			if(isSub || isHis){
				return MoLayoutUtil.getJspPath("/ap/box/viewDoc", "Sub");
			} else {
				return MoLayoutUtil.getJspPath("/ap/box/viewDoc");
			}
//			if(isFrame){
//				return MoLayoutUtil.getJspPath("/ap/box/viewDocFrm");
//			} else {
//				return MoLayoutUtil.getJspPath("/ap/box/viewDoc");
//			}
		} catch(CmException cmEx) {
			
			cmEx.printStackTrace();
			
			model.put("message", cmEx.getMessage());
			String paramMenuId = menuId!=null && menuId.startsWith("Y") ? menuId : null;
			model.put("togo", apBxSvc.getBxUrlByBxId(userVo, bxId, paramMenuId));
			return MoLayoutUtil.getJspPath("/ap/box/viewMessage");
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
					try{ 
						maxCnt = Integer.parseInt(apOngoFormApvLnDVo.getMaxCnt());
					} catch(Exception e){
						LOGGER.error("Sign area max count is null - formId:"+formId+"   apvLnTitlTypCd:"+apOngoFormApvLnDVo.getApvLnTitlTypCd());
						maxCnt = 0;
					}
				}
			} else {
				apFormApvLnDVo = (ApFormApvLnDVo)model.get(signArea);
				if(apFormApvLnDVo!=null){
					try{ 
						maxCnt = Integer.parseInt(apFormApvLnDVo.getMaxCnt());
					} catch(Exception e){
						LOGGER.error("Sign area max count is null - formId:"+formId+"   apvLnTitlTypCd:"+apFormApvLnDVo.getApvLnTitlTypCd());
						maxCnt = 0;
					}
				}
			}
			apvrList = new ArrayList<ApOngdApvLnDVo>();
			for(i=0;i<maxCnt;i++){
				apvrList.add(new ApOngdApvLnDVo());
			}
			model.put(signArea+"ApvrList", apvrList);
		}
	}
	
//	/** 문서 팝업 조회 */
//	@RequestMapping(value = "/ap/box/viewDocPop")
//	public String viewDocPop(HttpServletRequest request,
//			@Parameter(name="bxId", required=false) String bxId,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="vwMode", required=false) String vwMode,// 원문:orgn, 시행변환문:trx
//			@Parameter(name="refdBy", required=false) String refdBy,
//			Locale locale,
//			ModelMap model) throws Exception {
//		
//		return MoLayoutUtil.getJspPath("/ap/box/viewDocPop");
//	}
	
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

//	////////////////////////////////////////////////////////////////////
//	//
//	//				상세정보 - 문서정보 / 결재정보(결재선) / 수신처정보
//	/** [팝업] 문서정보 */
//	@RequestMapping(value = {"/ap/box/viewDocDetlPop", "/ap/adm/box/viewDocDetlPop"})
//	public String viewDocDetlPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="apvLnPno", required=false) String apvLnPno,
//			@Parameter(name="apvLnNo", required=false) String apvLnNo,
//			@Parameter(name="bxId", required=false) String bxId,
//			Locale locale,
//			ModelMap model) throws Exception {
//		
//		// 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
////		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
//		
//		String sendSeq = null;// 발송일련번호 - 접수/배부 처리할때 발송한 문서의 해당 일련번호에 수신여부 표시해야함
//		String pubBxDeptId = null;// 공람부서ID
//		boolean forDetlPop = true;// 상세보기 팝업용 - 결재방, 첨부, 참조문서 등의 데이터 조회 안함
//		
//		// 진행문서기본(AP_ONGD_B) 테이블
//		apDocSvc.getOngoDoc(apvNo, apvLnPno, apvLnNo, 
//				sendSeq, bxId, pubBxDeptId, 
//				forDetlPop,
//				userVo, locale, model);
//		
//		return MoLayoutUtil.getJspPath("/ap/box/viewDocDetlPop");
//	}

//	////////////////////////////////////////////////////////////////////
//	//
//	//				본문수정
//	/** [팝업] 본문수정 */
//	@RequestMapping(value = "/ap/box/setDocBodyHtmlPop")
//	public String setDocBodyHtmlPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="bodyHstNo", required=false) String bodyHstNo,
//			ModelMap model) throws Exception {
//		
//		if(apvNo!=null && !apvNo.isEmpty()){
//			
//			if(bodyHstNo==null || bodyHstNo.isEmpty()){
//				ApOngdBVo apOngdBVo = new ApOngdBVo();
//				apOngdBVo.setApvNo(apvNo);
//				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
//				if(apOngdBVo!=null){
//					bodyHstNo = apOngdBVo.getBodyHstNo();
//				}
//			}
//			if(bodyHstNo!=null && !bodyHstNo.isEmpty()){
//				
//				ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
//				apOngdBodyLVo.setApvNo(apvNo);
//				apOngdBodyLVo.setBodyHstNo(bodyHstNo);
//				apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
//				if(apOngdBodyLVo != null){
//					model.put("apOngdBodyLVo", apOngdBodyLVo);
//				}
//			}
//			
//		}
//		
//		return MoLayoutUtil.getJspPath("/ap/box/setDocBodyHtmlPop");
//	}
//	/** [팝업] 본문 이력조회 */
//	@RequestMapping(value = "/ap/box/viewBodyHisPop")
//	public String viewBodyHisPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="bodyHstNo", required=false) String bodyHstNo,
//			ModelMap model) throws Exception {
//		
//		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
//		apOngdBodyLVo.setApvNo(apvNo);
//		apOngdBodyLVo.setBodyHstNo(bodyHstNo);
//		apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
//		if(apOngdBodyLVo != null) model.put("apOngdBodyLVo", apOngdBodyLVo);
//		
//		return MoLayoutUtil.getJspPath("/ap/box/viewBodyHisPop");
//	}
//
//	////////////////////////////////////////////////////////////////////
//	//
//	//				문서정보 - 등록/수정
//	/** [팝업] 문서정보 */
//	@RequestMapping(value = "/ap/box/setDocInfoPop")
//	public String setDocInfoPop(HttpServletRequest request,
//			ModelMap model) throws Exception {
//
//		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// 문서보존기간코드
//		List<PtCdBVo> docKeepPrdCdList = ptCmSvc.getCdList("DOC_KEEP_PRD_CD", langTypCd, "Y");
//		model.put("docKeepPrdCdList", docKeepPrdCdList);
//		
//		// 보안등급코드
//		List<PtCdBVo> seculCdList = ptCmSvc.getCdList("SECUL_CD", langTypCd, "Y");
//		model.put("seculCdList", seculCdList);
//
//		// 문서구분코드
//		List<PtCdBVo> docTypCdList = ptCmSvc.getCdList("DOC_TYP_CD", langTypCd, "Y");
//		model.put("docTypCdList", docTypCdList);
//		
//		// 시행범위코드
//		List<PtCdBVo> enfcScopCdList = ptCmSvc.getCdList("ENFC_SCOP_CD", langTypCd, "Y");
//		model.put("enfcScopCdList", enfcScopCdList);
//		
//		// 발신명의 목록
//		List<OrOrgApvDVo> orOrgApvDVoList = apDocSvc.getSenderList(userVo, langTypCd);
//		if(orOrgApvDVoList != null) model.put("orOrgApvDVoList", orOrgApvDVoList);
//		
//		// 결재 옵션 세팅
//		apCmSvc.getOptConfigMap(model);
//		
//		return MoLayoutUtil.getJspPath("/ap/box/setDocInfoPop");
//	}
//	/** [AJAX] 문서번호 조회 */
//	@RequestMapping(value = "/ap/box/getDocNoAjx")
//	public String getDocNoAjx(HttpServletRequest request,
//			@Parameter(name="data", required=false) String data,
//			Locale locale,
//			ModelMap model) throws Exception {
//		
//		String message = null;
//		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
//		UserVo userVo = LoginSession.getUser(request);
//		
//		try {
//			// 옵션 조회 
//			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null);
//			
//			String docLangTypCd = (String)jsonObject.get("docLangTypCd");
//			String clsInfoId = (String)jsonObject.get("clsInfoId");
//			
//			// 문서정보
//			ApOngdBVo apOngdBVo = new ApOngdBVo();
//			apOngdBVo.setClsInfoId(clsInfoId);
//			apOngdBVo.setDocLangTypCd(docLangTypCd);
//			
//			// 결재선 정보
//			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
//			apOngdApvLnDVo.setApvDeptId(userVo.getDeptId());
//			
//			// 부서정보
//			String apvDeptId = userVo.getDeptId();
//			OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, docLangTypCd, null);
//			if(orOrgBVo==null){
//				LOGGER.error("Fail trans - dept not found ! - apvDeptId:"+apvDeptId);
//				//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
//				message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
//						+ apvDeptId+ " : " + apOngdApvLnDVo.getApvDeptNm();
//				throw new CmException(message);
//			}
//			
//			apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
//			apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
//			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getOrgAbbrRescNm());
//			
//			apDocNoSvc.setDocNo(apOngdBVo, apOngdApvLnDVo, optConfigMap, "ongoing", locale);
//			
//			model.put("docNo", apOngdBVo.getDocNo());
//			
//		} catch(Exception e){
//			message = e.getMessage();
//			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
//		}
//		
//		
//		return LayoutUtil.returnJson(model, message);
//	}

//	////////////////////////////////////////////////////////////////////
//	//
//	//				관인 / 서명인
//	/** [팝업] 관인 / 서명인 */
//	@RequestMapping(value = "/ap/box/setOfsePop")
//	public String setOfsePop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="orgId", required=false) String orgId,
//			@Parameter(name="ofseTypCd", required=false) String ofseTypCd,// 관인구분코드 - 01:관인(기관), 02:서명인(부서)
//			ModelMap model) throws Exception {
//		
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// 서명인
//		if("02".equals(ofseTypCd)){//서명인 - 부서부터
//			model.put("upParam", "&upward="+orgId);
//		} else {// 관인 - 기관부터
//			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(orgId, "G", langTypCd);//G:기관
//			model.put("upParam", "&upward="+orOrgTreeVo.getOrgId());
//		}
//		// 옵션 조회(캐쉬) - key : optConfigMap
//		apCmSvc.getOptConfigMap(model);
//		
//		return MoLayoutUtil.getJspPath("/ap/box/setOfsePop");
//	}
	
//	/** 상위조직 트리 프레임 */
//	@RequestMapping(value = {"/ap/box/treeUpFrm", "/ap/box/treeDownFrm", "/ap/box/treeRecLstOrgFrm"})
//	public String treeUpFrm(HttpServletRequest request,
//			@Parameter(name="multi", required=false) String multi,
//			@Parameter(name="compId", required=false) String compId,// 회사관리에서 다른회사의 관리자 지정할때만 넘어옴
//			@Parameter(name="global", required=false) String global,// global=Y : 전사 조직도 보여줌
//			@Parameter(name="upward", required=false) String upward,// 상위 부서 선택 시작 조직ID
//			@Parameter(name="downward", required=false) String downward,// 하위 부서 선택 시작 조직ID
//			@Parameter(name="oneDeptId", required=false) String oneDeptId,// 하나의 부서 - 조직도에 하나의 부서만 표시 할 때
//			@Parameter(name="orgId", required=false) String orgId,
//			ModelMap model) throws Exception {
//		String uri = request.getRequestURI();
//		// 상위조직 조회, 하위조직 조회
//		if(uri.indexOf("/treeUpFrm")>0 || uri.indexOf("/treeDownFrm")>0){
//			model.put("callback", "clickApTree");
//			if(upward!=null && !upward.isEmpty()) model.put("selectedOrgId", upward);
//			else if(downward!=null && !downward.isEmpty()) model.put("selectedOrgId", downward);
//		// 전체 부서 조회 - 등록대장
//		} else if(uri.indexOf("/treeRecLstOrgFrm")>0){
//			model.put("noPart", "Y");
//			model.put("callback", "clickRecLstOrg");
//			if(orgId!=null && !orgId.isEmpty()){
//				model.put("selectedOrgId", orgId);
//			} else {
//				UserVo userVo = LoginSession.getUser(request);
//				model.put("selectedOrgId", userVo.getDeptId());
//			}
//		}
//		
//		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, model);
//	}
//	/** [AJAX]부서별 관인 목록 - 관인선택용 */
//	@RequestMapping(value = "/ap/box/listOfcSealAjx")
//	public String listOfcSealFrm(HttpServletRequest request,
//			@Parameter(name="data", required=false)String data,
//			ModelMap model) throws Exception {
//		
//		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
//		String orgId = (String)jsonObject.get("orgId");
//		String ofseTypCd = (String)jsonObject.get("ofseTypCd");// 관인구분코드 - 01:관인(기관), 02:서명인(부서)
//		String docLangTypCd = (String)jsonObject.get("docLangTypCd");
//		
//		// 발신명의 조회
//		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
//		orOrgApvDVo.setOrgId(orgId);
//		orOrgApvDVo.setQueryLang(docLangTypCd);
//		orOrgApvDVo = (OrOrgApvDVo)commonSvc.queryVo(orOrgApvDVo);
//		if(orOrgApvDVo != null){
//			model.put("sendrNmRescId", orOrgApvDVo.getSendrNmRescId());
//			model.put("sendrNmRescNm", orOrgApvDVo.getSendrNmRescNm());
//		}
//		
//		// 관인, 서명인 목록 조회
//		OrOfseDVo orOfseDVo = new OrOfseDVo();
//		orOfseDVo.setOrgId(orgId);
//		orOfseDVo.setOfseTypCd(ofseTypCd);
//		orOfseDVo.setDisuYn("N");
//		orOfseDVo.setOrderBy("SEQ");
//		@SuppressWarnings("unchecked")
//		List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonSvc.queryList(orOfseDVo);
//		model.put("orOfseDVoList", orOfseDVoList);
//		
//		return LayoutUtil.returnJson(model);
//	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				결재라인
	/** [팝업] 결재라인 - 설정 */
	@RequestMapping(value = "/ap/box/setApvLnPop")
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

		// 옵션설정을 JSON 형태로 Model 에 설정 - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
//		if(downward!=null && !downward.isEmpty()){
//			model.put("downwardParam", "&downward="+downward);
//		}
		// 부재사유코드
		List<PtCdBVo> absRsonCdList = ptCmSvc.getCdList("ABS_RSON_CD", langTypCd, "Y");
		model.put("absRsonCdList", absRsonCdList);
		
		return MoLayoutUtil.getJspPath("/ap/box/setApvLnPop");
	}
	/** [팝업/프레임] 결재라인 - 선택된 목록 */
	@RequestMapping(value = "/ap/box/listApvLnFrm")
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
		
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = null;
		// 결재선 지정이 안된 경우
		boolean isEmptyApvLn = true;
		// 이중 결재의 경우
		boolean isDblApvLn = "apvLnDbl".equals(formApvLnTypCd);
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
//			else if(apvLnPno==null || apvLnPno.isEmpty() || "0".equals(apvLnPno)){
//				apOngdApvLnDVo.setApvrRoleCd("mak");
//				apOngdApvLnDVo.setApvStatCd("inApv");//inApv:결재중
//			} else {
//				apOngdApvLnDVo.setApvrRoleCd("makAgr");
//				apOngdApvLnDVo.setApvStatCd("inAgr");//inAgr:합의중
//			}
			apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
			apOngdApvLnDVo.setApvrNm(userVo.getUserNm());
			
			OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), langTypCd, null);
			apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());//직위코드
			apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());//직책코드
			//apOngdApvLnDVo.setApvrPositNm(ptCmSvc.getCdRescNm("POSIT_CD", langTypCd, userVo.getPositCd()));//직위코드
			//apOngdApvLnDVo.setApvrTitleNm(ptCmSvc.getCdRescNm("TITLE_CD", langTypCd, userVo.getTitleCd()));//직책코드
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "D", langTypCd);
			apOngdApvLnDVo.setApvDeptNm(orOrgTreeVo.getRescNm());//부서명
			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgTreeVo.getOrgAbbrRescNm());//부서약어
			
			if(isDblApvLn){
				// 현재 작업자를 - 이중결재의 처리부서 검토자로 추가해야 하는지 여부
				if(needChangePrcDept){
					apOngdApvLnDVo.setDblApvTypCd("prcDept");// 이중결재 - prcDept:처리부서
				// 이중결재의 기안자일 경우
				} else if("apvLnDbl".equals(formApvLnTypCd)){
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
		
		return MoLayoutUtil.getJspPath("/ap/box/listApvLnFrm");
	}
	/** [팝업] 결재라인 이력조회 */
	@RequestMapping(value = "/ap/box/viewApvLnHisPop")
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
		
		return MoLayoutUtil.getJspPath("/ap/box/viewApvLnHisPop");
	}

	/** [팝업] 결재라인 조회 - 전체경로, 해당 서브경로 */
	@RequestMapping(value = {"/ap/box/viewApvLnPop","/ap/adm/box/viewApvLnPop"})
	public String viewApvLnPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="apvLnPno", required=false) String apvLnPno,
			ModelMap model) throws Exception {
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setStorage(storage);
		
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
		if(apOngdApvLnDVoList != null) model.put("currApOngdApvLnDVoList", apOngdApvLnDVoList);
		
		return MoLayoutUtil.getJspPath("/ap/box/viewApvLnPop");
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				의견작성
	/** [팝업] 의견작성 */
	@RequestMapping(value = "/ap/box/setDocOpinPop")
	public String setDocOpinPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/ap/box/setDocOpinPop");
	}
	/** [팝업] 참조열람 */
	@RequestMapping(value = "/ap/box/setRefVwOpinPop")
	public String setRefVwOpinPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/ap/box/setRefVwOpinPop");
	}
	/** [팝업] 상신,승인,반려,찬성,반대 - 서명이미지 + 비밀번호입력 + 의견 */
	@RequestMapping(value = "/ap/box/setDocProsPop")
	public String setDocProsPop(HttpServletRequest request,
			@Parameter(name="apvStatCd", required=false) String apvStatCd,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 옵션 조회 및 설정(캐쉬) - key : optConfigMap
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		// apvStatCd:결재상태코드
		//    - byOne:1인결재, mak:기안, befoApv:결재전, inApv:결재중, hold:보류, apvd:승인, rejt:반려, pros:찬성, cons:반대
		// mak:기안, apvd:승인, pros:찬성, cons:반대 - 찍어야 할 도장 조회
		
		if("mak".equals(apvStatCd) || "apvd".equals(apvStatCd) || "pros".equals(apvStatCd) || "cons".equals(apvStatCd)){
			// 서명 이미지(변수명:orUserImgDVo) 서명방법(변수명:signMthdCd) 조회하여 모델에 세팅
			apDocSvc.setUserSignImg(userVo, userVo.getOdurUid(), model);
		}
		return MoLayoutUtil.getJspPath("/ap/box/setDocProsPop");
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
		
		return MoLayoutUtil.getJspPath("/ap/box/setDocCensrPop");
	}
	/** [팝업] 반송 */
	@RequestMapping(value = "/ap/box/setDocRetnPop")
	public String setDocRetnPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return MoLayoutUtil.getJspPath("/ap/box/setDocRetnPop");
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
		return MoLayoutUtil.getJspPath("/ap/box/viewRetnOpinPop");
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
		return MoLayoutUtil.getJspPath("/ap/box/listBulkApvPop");
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
			
			ApOngdBVo apOngdBVo = null;
			if(recvDeptHstNo==null || recvDeptHstNo.isEmpty()){
				// 진행문서 조회
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				
				recvDeptHstNo = apOngdBVo==null ? null : apOngdBVo.getRecvDeptHstNo();
			}
			
			if(recvDeptHstNo!=null && !recvDeptHstNo.isEmpty()){
				ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
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
		
		return MoLayoutUtil.getJspPath("/ap/box/setDocRecvDeptPop");
	}
	/** UI 구성용 빈 ApOngdRecvDeptLVo 를 더해서 리턴 */
	private List<ApOngdRecvDeptLVo> addEmptyApOngdRecvDeptLVo(List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList){
		if(apOngdRecvDeptLVoList==null) apOngdRecvDeptLVoList = new ArrayList<ApOngdRecvDeptLVo>();
		apOngdRecvDeptLVoList.add(new ApOngdRecvDeptLVo());//UI 구성용
		return apOngdRecvDeptLVoList;
	}
	
//	/** [프레임] 수신처 조직도 트리 프레임 */
//	@RequestMapping(value = "/ap/box/treeRecvDeptFrm")
//	public String treeRecvDeptFrm(HttpServletRequest request,
//			@Parameter(name="recvDeptTypCd", required=false) String recvDeptTypCd,
//			ModelMap model) throws Exception {
//		
//		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null);
//		
//		String multi = "Y";//다중선택 가능
//		String compId = null;//해당 회사만 - 회사ID
//		String global = null;//모든 회사
//		String upward = null;//상위 조직만 - orgId
//		String downward = null;//하위 조직만 - orgId
//		String oneDeptId = null;
//		String orgId = null;
//		
//		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// recvDeptTypCd : 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
//		
//		if("for".equals(recvDeptTypCd)){// for:대외 면
//			global = "Y";
//			
//		} else if("dom".equals(recvDeptTypCd)){// dom:대내 면
//			//[옵션] introScope=대내 조직도 범위 - org:기관, comp:회사
//			if("comp".equals(optConfigMap.get("introScope"))){
//				compId = userVo.getCompId();
//			} else {
//				OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "G", langTypCd);
//				downward = orOrgTreeVo.getOrgId();
////				downward = userVo.getInstId();
//			}
//			model.put("selectedOrgId", userVo.getDeptId());
//		} else {
//			return null;
//		}
//		
//		model.put("callback", "setRecvDeptVo");
//		model.put("noPart", "Y");//파트는 보여주지 않음
//		//model.put("noInitSelect", Boolean.TRUE);//초기에 자기 부서를 선택 안함
//		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, model);
//	}
	
//	////////////////////////////////////////////////////////////////////
//	//
//	//				배부
//	/** [팝업] 배부 */
//	@RequestMapping(value = "/ap/box/setDocDistPop")
//	public String setDocDistPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="bxId", required=false) String bxId,
//			ModelMap model) throws Exception {
//		
//		if(apvNo!=null && !apvNo.isEmpty() && bxId != null && "distRecLst".equals(bxId)){
//			// 스토리지 조회
//			String storage = apCmSvc.queryStorage(apvNo);
//			
//			// 진행문서발송상세(AP_ONGD_SEND_D) 테이블 - 목록조회
//			ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
//			apOngdSendDVo.setApvNo(apvNo);
//			apOngdSendDVo.setStorage(storage);
//			@SuppressWarnings("unchecked")
//			List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonSvc.queryList(apOngdSendDVo);
//			if(apOngdSendDVoList==null){
//				apOngdSendDVoList = new ArrayList<ApOngdSendDVo>();
//			}
//			apOngdSendDVoList.add(new ApOngdSendDVo());//UI 구성용 빈 VO 추가
//			model.put("apOngdSendDVoList", apOngdSendDVoList);
//		} else {
//			List<ApOngdSendDVo> apOngdSendDVoList = new ArrayList<ApOngdSendDVo>();
//			apOngdSendDVoList.add(new ApOngdSendDVo());//UI 구성용 빈 VO 추가
//			model.put("apOngdSendDVoList", apOngdSendDVoList);
//		}
//		
//		return MoLayoutUtil.getJspPath("/ap/box/setDocDistPop");
//	}
	
//	/** [프레임] 수신처 조직도 트리 프레임 */
//	@RequestMapping(value = "/ap/box/treeDistDeptFrm")
//	public String treeDistDeptFrm(HttpServletRequest request,
//			ModelMap model) throws Exception {
//		
//		UserVo userVo = LoginSession.getUser(request);
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		String multi = "Y";//다중선택 가능
//		String compId = null;//해당 회사만 - 회사ID
//		String global = null;//모든 회사
//		String upward = null;//상위 조직만 - orgId
//		
//		OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(userVo.getDeptId(), "G", langTypCd);
//		String downward = orOrgTreeVo.getOrgId();//하위 조직만 - 기관ID
//		//String downward = userVo.getInstId();//하위 조직만 - 기관ID
//		String oneDeptId = null;
//		String orgId = null;
//		
//		model.put("callback", "setDistDeptVo");
//		model.put("noPart", "Y");//파트는 보여주지 않음
//		//model.put("noInitSelect", Boolean.TRUE);//초기에 자기 부서를 선택 안함
//		return orOrgCtrl.treeOrgFrm(request, multi, compId, global, upward, downward, oneDeptId, orgId, model);
//	}
	
//	////////////////////////////////////////////////////////////////////
//	//
//	//				접수확인 / 배부확인
//	/** [팝업] 접수확인 */
//	@RequestMapping(value = "/ap/box/setCfrmRecvPop")
//	public String setCfrmRecvPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			ModelMap model) throws Exception {
//		
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
//		apOngdSendDVo.setApvNo(apvNo);
//		apOngdSendDVo.setOrderBy("SEND_SEQ");
//		apOngdSendDVo.setQueryLang(langTypCd);
//		
//		@SuppressWarnings("unchecked")
//		List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonSvc.queryList(apOngdSendDVo);
//		if(apOngdSendDVoList != null){
//			model.put("apOngdSendDVoList", apOngdSendDVoList);
//			int sendCnt=0, cmplCnt=0, retrvCnt=0, retnCnt=0, befoCnt=0, dupCnt=0;
//			String hdlStatCd;
//			for(ApOngdSendDVo storedApOngdSendDVo : apOngdSendDVoList){
//				sendCnt++;
//				hdlStatCd = storedApOngdSendDVo.getHdlStatCd();
//				// recvCmpl:접수완료, distCmpl:배부완료, manlCmpl:수동완료,
//				if("recvCmpl".equals(hdlStatCd) || "distCmpl".equals(hdlStatCd) || "manlCmpl".equals(hdlStatCd)){
//					cmplCnt++;
//				// recvRetrv:접수회수, distRetrv:배부회수, manlRetrv:수동회수
//				} else if("recvRetrv".equals(hdlStatCd) || "distRetrv".equals(hdlStatCd) || "manlRetrv".equals(hdlStatCd)){
//					retrvCnt++;
//				// recvRetn:접수반송, distRetn:배부반송, 
//				} else if("recvRetn".equals(hdlStatCd) || "distRetn".equals(hdlStatCd)){
//					retnCnt++;
//				// dupSend:중복발송
//				} else if("dupSend".equals(hdlStatCd)){
//					dupCnt++;
//				// befoRecv:접수대기, befoDist:배부대기, manl:수동발송
//				} else {
//					befoCnt++;
//				}
//			}
//			model.put("sendCnt", Integer.valueOf(sendCnt));
//			model.put("cmplCnt", Integer.valueOf(cmplCnt));
//			model.put("retrvCnt", Integer.valueOf(retrvCnt));
//			model.put("retnCnt", Integer.valueOf(retnCnt));
//			model.put("dupCnt", Integer.valueOf(dupCnt));
//			model.put("befoCnt", Integer.valueOf(befoCnt));
//		} else {
//			model.put("sendCnt", Integer.valueOf(0));
//			model.put("cmplCnt", Integer.valueOf(0));
//			model.put("retrvCnt", Integer.valueOf(0));
//			model.put("retnCnt", Integer.valueOf(0));
//			model.put("dupCnt", Integer.valueOf(0));
//			model.put("befoCnt", Integer.valueOf(0));
//		}
//		
//		return MoLayoutUtil.getJspPath("/ap/box/setCfrmRecvPop");
//	}

	////////////////////////////////////////////////////////////////////
	//
	//				결재 비밀번호 + 문서 비밀번호
	/** [AJAX] 결재 비밀번호 / 문서 비밀번호 확인하고 보안ID 발급 */
	@RequestMapping(value = {"/ap/box/getSecuIdAjx", "/dm/doc/getSecuIdAjx"})
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
				} else if(!orUserPwDVo.getPwEnc().equals(cryptoSvc.encryptPw(apvPw, userVo.getOdurUid()))){
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
				
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
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
//	/** [팝업] 문서 비밀번호 입력 팝업 */
//	@RequestMapping(value = {"/ap/box/setDocPwPop","/ap/adm/box/setDocPwPop"})
//	public String setDocPwPop(HttpServletRequest request,
//			ModelMap model) throws Exception {
//		return MoLayoutUtil.getJspPath("/ap/box/setDocPwPop");
//	}

//	////////////////////////////////////////////////////////////////////
//	//
//	//				양식선택
//	/** [팝업] 양식선택 */
//	@RequestMapping(value = "/ap/box/selectFormPop")
//	public String selectFormPop(HttpServletRequest request,
//			ModelMap model) throws Exception {
//		return MoLayoutUtil.getJspPath("/ap/box/selectFormPop");
//	}
//	/** [팝업] 양식선택 - 양식함 트리 프레임 */
//	@RequestMapping(value = "/ap/box/treeFormBxFrm")
//	public String treeFormBxFrm(HttpServletRequest request,
//			@Parameter(name="formBxId", required=false) String formBxId,
//			ModelMap model) throws Exception {
//		return apAdmFormCtrl.treeFormBxFrm(request, formBxId, "Y", model);
//	}
//	/** [팝업] 양식선택 - 양식 프레임 */
//	@RequestMapping(value = "/ap/box/listFormFrm")
//	public String listFormFrm(HttpServletRequest request,
//			@Parameter(name="formBxId", required=false) String formBxId,
//			@Parameter(name="schWord", required=false) String schWord,
//			@Parameter(name="formTypCd", required=false) String formTypCd,
//			@Parameter(name="forTrans", required=false) String forTrans,
//			ModelMap model) throws Exception {
//		
//		// 변환용 여부
//		if("trans".equals(formTypCd)) forTrans = "Y";
//		apAdmFormCtrl.listFormFrm(request, formBxId, schWord, formTypCd, forTrans, "Y", model);
//		return MoLayoutUtil.getJspPath("/ap/box/listFormFrm");
//	}
	
//	////////////////////////////////////////////////////////////////////
//	//
//	//				공람
//	/** [팝업] 공람게시 */
//	@RequestMapping(value = "/ap/box/setPubBxPop")
//	public String setPubBxPop(HttpServletRequest request,
//			ModelMap model) throws Exception {
//		// 게시기간
//		model.put("pubBxEndYmd", StringUtil.getDiffYmd(30));
//		return MoLayoutUtil.getJspPath("/ap/box/setPubBxPop");
//	}
//
//	/** [팝업] 공람게시 */
//	@RequestMapping(value = "/ap/box/listPubBxVwPop")
//	public String listPubBxVwPop(HttpServletRequest request,
//			@Parameter(name="pubBxDeptId", required=false) String pubBxDeptId,//공람부서ID
//			@Parameter(name="apvNo", required=false) String apvNo,
//			ModelMap model) throws Exception {
//		
//		// 저장소 조회
//		String storage = apCmSvc.queryStorage(apvNo);
//		
//		// 진행문서공람게시확인내역(AP_ONGD_PUB_BX_CNFM_L) 테이블 - 조회
//		ApOngdPubBxCnfmLVo apOngdPubBxCnfmLVo = new ApOngdPubBxCnfmLVo();
//		apOngdPubBxCnfmLVo.setPubBxDeptId(pubBxDeptId);
//		apOngdPubBxCnfmLVo.setApvNo(apvNo);
//		apOngdPubBxCnfmLVo.setStorage(storage);
//		apOngdPubBxCnfmLVo.setOrderBy("VW_DT DESC");
//		@SuppressWarnings("unchecked")
//		List<ApOngdPubBxCnfmLVo> apOngdPubBxCnfmLVoList = (List<ApOngdPubBxCnfmLVo>)commonSvc.queryList(apOngdPubBxCnfmLVo);
//		if(apOngdPubBxCnfmLVoList != null){
//			model.put("apOngdPubBxCnfmLVoList", apOngdPubBxCnfmLVoList);
//		}
//		
//		return MoLayoutUtil.getJspPath("/ap/box/listPubBxVwPop");
//	}
	
	////////////////////////////////////////////////////////////////////
	//
	//				첨부
	/** [팝업] 첨부 */
	@RequestMapping(value = {"/ap/box/setDocAttchPop", "/ap/box/viewDocAttchPop", "/ap/adm/box/viewDocAttchPop"})
	public String setDocAttchPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="attHstNo", required=false) String attHstNo,
			@Parameter(name="refDocHstNo", required=false) String refDocHstNo,
			@Parameter(name="modified", required=false) String modified,
			ModelMap model) throws Exception {
		
		String mode = request.getRequestURI().indexOf("/viewDocAttchPop")>0 ? "view" : "set";
		model.put("mode", mode);
		
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String storage = null;
		if("view".equals(mode)){
			storage = apCmSvc.queryStorage(apvNo);
		}
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, userVo.getCompId());
		if(apvNo!=null && !apvNo.isEmpty() && !"Y".equals(modified)){
			
			ApOngdBVo apOngdBVo = null;
			if(attHstNo==null || attHstNo.isEmpty() || refDocHstNo==null || refDocHstNo.isEmpty()){
				// 진행문서 조회
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setStorage(storage);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			}
			
			if(attHstNo==null || attHstNo.isEmpty()){
				attHstNo = apOngdBVo==null ? null : apOngdBVo.getAttHstNo();
			}
			if(attHstNo!=null && !attHstNo.isEmpty()){
				// 첨부파일 조회
				ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
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
		
		String regRecLstMenuId = apBxSvc.getMenuIdByBxId(userVo, "regRecLst");
		if(regRecLstMenuId != null) model.put("regRecLstMenuId", regRecLstMenuId);
		
		model.put("apFileModule", ApConstant.AP_FILE_MODULE);
		
		return MoLayoutUtil.getJspPath("/ap/box/setDocAttchPop");
	}
//	/** [팝업] 첨부 이력 조회 */
//	@RequestMapping(value = "/ap/box/viewAttHisPop")
//	public String viewAttHisPop(HttpServletRequest request,
//			@Parameter(name="apvNo", required=false) String apvNo,
//			@Parameter(name="attHstNo", required=false) String attHstNo,
//			ModelMap model) throws Exception {
//		
//		String storage = apCmSvc.queryStorage(apvNo);
//		
//		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
//		apOngdAttFileLVo.setApvNo(apvNo);
//		apOngdAttFileLVo.setAttHstNo(attHstNo);
//		apOngdAttFileLVo.setStorage(storage);
//		@SuppressWarnings("unchecked")
//		List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
//		if(apOngdAttFileLVoList != null) model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
//		
//		model.put("apFileModule", ApConstant.AP_FILE_MODULE);
//		
//		return MoLayoutUtil.getJspPath("/ap/box/viewAttHisPop");
//	}
//	/** [팝업] 참조문서 - 등록대장 조회 - 프레임 삽입 페이지 */
//	@RequestMapping(value = "/ap/box/listRegRecLstPop")
//	public String listRegRecLstPop(HttpServletRequest request,
//			ModelMap model) throws Exception {
//		return MoLayoutUtil.getJspPath("/ap/box/listRegRecLstPop");
//	}
//	/** [팝업/프레임] 참조문서 - 등록대장 조회 */
//	@RequestMapping(value = "/ap/box/listRegRecLstFrm")
//	public String listRegRecLstFrm(HttpServletRequest request,
//			@Parameter(name="schCat", required=false) String schCat,
//			@Parameter(name="schWord", required=false) String schWord,
//			ModelMap model) throws Exception {
//		// 참조문서 팝업용
//		model.put("forRefDocPop", "Y");//페이지별 레코드수 10개로 고정함 - 쿼리에 적용
//		moApBxCtrl.listApvBx(request, "regRecLst", schCat, schWord, null, null, model);
//		return MoLayoutUtil.getJspPath("/ap/box/listRegRecLstFrm");
//	}
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/ap/box/downFile","/ap/preview/downFile"})//, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "apvNo", required = false) String apvNo,
			@RequestParam(value = "attHstNo", required = false) String attHstNo,
			@RequestParam(value = "attSeq", required = false) String attSeq,
			ModelMap model) throws Exception {
		
		try {
			if (apvNo==null || apvNo.isEmpty() || attSeq==null || attSeq.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
						
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
				LOGGER.error(message+ " : NO ATTCH DATA (apvNo:"+apvNo
						+(attHstNo==null || attHstNo.isEmpty() ? "" : "  attHstNo:"+attHstNo)
						+"  attSeqs:"+ArrayUtil.toString(attSeqs)+")");
				throw new CmException(message);
			} else if(size==1){
				ApOngdAttFileLVo apOngdAttFileLVo = apOngdAttFileLVoList.get(0);
				File file = new File(wasCopyBaseDir+apOngdAttFileLVo.getFilePath());
				if(!file.isFile()){
					// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
					String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
					LOGGER.error(message+ " : FILE NOT FOUND (apvNo:"+apvNo
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
							LOGGER.error(message+ " : FILE NOT FOUND (apvNo:"+apvNo
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
	
	/** [팝업] 결재 비밀번호 */
	@RequestMapping(value = "/ap/box/setApvPwPop")
	public String setApvPwPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		return MoLayoutUtil.getJspPath("/ap/box/setApvPwPop");
	}
	
	/** [팝업] 결재 본문보기 */
	@RequestMapping(value = "/ap/box/viewBodyWinPop")
	public String viewBodyWinPop(HttpServletRequest request,
			@RequestParam(value = "apvNo", required = false) String apvNo,
			ModelMap model) throws Exception {
		// 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// [결재옵션] - JSON 형태로 Model 에 설정(Javascript 용) - key : optConfig
		apCmSvc.setOptConfigJson(model, userVo.getCompId());
		
		if(apvNo!=null && !apvNo.isEmpty()){
			
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			
			String bodyHstNo = apOngdBVo==null ? null  : apOngdBVo.getBodyHstNo();
			if(bodyHstNo!=null && !bodyHstNo.isEmpty()){
				
				ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
				apOngdBodyLVo.setApvNo(apvNo);
				apOngdBodyLVo.setBodyHstNo(bodyHstNo);
				apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
				
				if(apOngdBodyLVo!=null){
					model.put("bodyHtml", apOngdBodyLVo.getBodyHtml());
				}
			}
		}
		
		return MoLayoutUtil.getJspPath("/ap/box/viewBodyWinPop");
	}
}
