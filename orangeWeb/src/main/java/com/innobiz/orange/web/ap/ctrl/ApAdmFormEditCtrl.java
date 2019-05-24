package com.innobiz.orange.web.ap.ctrl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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

import com.innobiz.orange.web.ap.cust.ApCustFncSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApFormSvc;
import com.innobiz.orange.web.ap.svc.ApRescSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.utils.ApParamUtil;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApErpFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormCombDVo;
import com.innobiz.orange.web.ap.vo.ApFormImgDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemLVo;
import com.innobiz.orange.web.ap.vo.ApFormTxtDVo;
import com.innobiz.orange.web.ap.vo.ApFormXmlDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdRefVwDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormBVo;
import com.innobiz.orange.web.ap.vo.ApRescDVo;
import com.innobiz.orange.web.cm.config.CustConfig;
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
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;
import com.innobiz.orange.web.wf.vo.WfRescBVo;

/** 결재 약식 관리 Ctrl (관리자용) */
@Controller
public class ApAdmFormEditCtrl {

	/** Logger */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ApAdmFormEditCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 결재 양식 서비스 */
	@Autowired
	private ApFormSvc apFormSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
//
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;

	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 고객사별 기능 서비스 */
	@Autowired
	private ApCustFncSvc apCustFncSvc;
	
	/** [오른쪽 프레임:팝업] 양식설정 설정 - 양식 설정용 전환 화면 */
	@RequestMapping(value = {"/ap/adm/form/setFormEdit", "/ap/env/setFormEdit"})
	public String setFormEdit(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		// 에디터 사용하겠다고 JS 등 include 함
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(formId!=null && !formId.isEmpty()){
			// 업무관리 - 에디터 모드인지
			model.put("isFromEdit", Boolean.TRUE);
			apFormSvc.setSetupForm(formId, langTypCd, request, model);
			
			@SuppressWarnings("unchecked")
			List<ApFormCombDVo> apFormCombDVoList = (List<ApFormCombDVo>)model.get("apFormCombDVoList");
			if(apFormCombDVoList==null || apFormCombDVoList.isEmpty()){
				
				apFormCombDVoList = new ArrayList<ApFormCombDVo>();
				
				ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
				apFormCombDVo.setFormId(formId);
				apFormCombDVo.setFormCombId("bodyHtml");
				apFormCombDVo.setFormCombSeq("1");
				apFormCombDVo.setSortOrdr("1");
				apFormCombDVoList.add(apFormCombDVo);
				
				model.put("apFormCombDVoList", apFormCombDVoList);
				model.put("hasBodyHtml", Boolean.TRUE);
			}
		}
		
		// 결제자 목록에 빈라인 표시용
		List<ApOngdApvLnDVo> currApOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
		currApOngdApvLnDVoList.add(new ApOngdApvLnDVo());
		model.put("currApOngdApvLnDVoList", currApOngdApvLnDVoList);
		
		// 참조열람 목록에 빈라인 표시용
		List<ApOngdRefVwDVo> apOngdRefVwDVoList = new ArrayList<ApOngdRefVwDVo>();
		apOngdRefVwDVoList.add(new ApOngdRefVwDVo());
		model.put("apOngdRefVwDVoList", apOngdRefVwDVoList);
		
		model.put("custCode", CustConfig.CUST_CODE);
		if(model.get("formBodyXML") == null) {
			model.put("formBodyXML", new XMLElement(null));
		}
		
		UserVo userVo = LoginSession.getUser(request);
		if("U0000001".equals(userVo.getUserUid())){
			model.put("isAdmin", Boolean.TRUE);
		}
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		model.put("sysPlocMap", sysPlocMap);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setFormEdit");
	}
	
	/** [팝업:스타일] 스타일 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocStylePop", "/ap/env/setDocStylePop"})
	public String setDocStylePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		String langTypCd = LoginSession.getLangTypCd(request);
		// 어권별 폰트 조회
		String[] fonts = ptSysSvc.getFontFamilyByLang(langTypCd);
		if(fonts!=null){
			model.put("fontFamilies", fonts);
		}
		
		String[] sizes = ptSysSvc.getFontSizeArray();
		model.put("fontSizes", sizes);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setDocStylePop");
	}
	
	///////////////////////////////////////////////
	// 머리글
	//
	/** [팝업:머리글] 머리글 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocHeaderPop", "/ap/env/setDocHeaderPop"})
	public String setDocHeaderPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		// 결재 옵션 세팅
		UserVo userVo = LoginSession.getUser(request);
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		
		ApFormTxtDVo apFormTxtDVo;
		
		// 디폴트 머리글 설정
		// 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		String formTxtTypCd = "docHeader";
		apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormTxtTypCd(formTxtTypCd);
		// ap.cmpt.areaForHeader=머리글 들어갈 자리 입니다.
		apFormTxtDVo.setTxtCont(messageProperties.getMessage("ap.cmpt.areaForHeader", request));
		apFormTxtDVo.setTxtSize("9pt");
		apFormTxtDVo.setTxtFontVa("돋움체");
		apFormTxtDVo.setTxtStylVa("font-weight:bold;");
		model.put(formTxtTypCd, apFormTxtDVo);
		
		// 디폴트 양식명 설정
		// 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		formTxtTypCd = "docName";
		apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormTxtTypCd(formTxtTypCd);
		// ap.cmpt.areaForDocName=양 식 명 자 리
		apFormTxtDVo.setTxtCont(messageProperties.getMessage("ap.cmpt.areaForDocName", request));
		apFormTxtDVo.setTxtSize("36pt");
		apFormTxtDVo.setTxtFontVa("돋움체");
		apFormTxtDVo.setTxtStylVa("font-weight:bold;");
		model.put(formTxtTypCd, apFormTxtDVo);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setDocHeaderPop");
	}
	/** [히든플레임] 머리글 로고/심볼 업로드 */
	@RequestMapping(value = {"/ap/adm/form/transDocHeaderImage", "/ap/env/transDocHeaderImage"})
	public String transDocHeaderImage(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
//			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			
			// 업로드 경로
			DistHandler distHandler = distManager.createHandler("images/upload/ap/form", locale);//업로드 경로 설정
			
			model.put("docLogo-useYn", paramMap.get("docLogo-useYn"));
			model.put("docSymbol-useYn", paramMap.get("docSymbol-useYn"));
			model.put("docHeaderImg-useYn", paramMap.get("docHeaderImg-useYn"));
			
			String distPath;
			BufferedImage bimg;
			File imgFile = fileMap.get("docLogo-image");
			if(imgFile!=null){
				bimg = ImageIO.read(imgFile);
				model.put("docLogo-imgWdth", Integer.toString(bimg.getWidth()));
				model.put("docLogo-imgHght", Integer.toString(bimg.getHeight()));
				distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("docLogo-image"));
				model.put("docLogo-imgPath", distPath);
			}
			imgFile = fileMap.get("docSymbol-image");
			if(imgFile!=null){
				bimg = ImageIO.read(imgFile);
				model.put("docSymbol-imgWdth", Integer.toString(bimg.getWidth()));
				model.put("docSymbol-imgHght", Integer.toString(bimg.getHeight()));
				distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("docSymbol-image"));
				model.put("docSymbol-imgPath", distPath);
			}
			imgFile = fileMap.get("docHeaderImg-image");
			if(imgFile!=null){
				bimg = ImageIO.read(imgFile);
				model.put("docHeaderImg-imgWdth", Integer.toString(bimg.getWidth()));
				model.put("docHeaderImg-imgHght", Integer.toString(bimg.getHeight()));
				distPath = distHandler.addWebList(uploadHandler.getAbsolutePath("docHeaderImg-image"));
				model.put("docHeaderImg-imgPath", distPath);
			}
			distHandler.distribute();
			
			String jsonString = JsonUtil.toJson(model);
			model.put("todo", "parent.processHeaderData("+jsonString+");");
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

	///////////////////////////////////////////////
	// 결재라인
	//
	/** [팝업:결재라인] 결재라인 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocApvLnPop", "/ap/env/setDocApvLnPop"})
	public String setDocApvLnPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="formApvLnTypCd", required=false) String formApvLnTypCd,
			ModelMap model) throws Exception {
		
		// 도장방 최대수 설정 - 사이트마다 다를 수 있음
		model.put("signAreaMaxCnt", apCustFncSvc.getIntCustValue("signAreaMaxCnt"));
		
		// formApvLnTypCd
		// 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄),
		//  apvLnDbl:신청+처리(이중결재), apvLnDblList:리스트(이중결재), apvLnWrtn:신청+처리(서면결재)
		//	apvLn2LnAgr:결재+합의(2줄)
		//  apvLnOneTopList:최종결재+리스트, apvLnMultiTopList:서명+리스트
		
		// 결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLnMultiTopList:서명+리스트
		if("apvLn".equals(formApvLnTypCd) || "apvLnMixd".equals(formApvLnTypCd) || "apvLnMultiTopList".equals(formApvLnTypCd)
				 || "apvLnOnecard".equals(formApvLnTypCd)){
			// 결재라인타이틀구분코드 - KEY - apv:결재, agr:합의, req:신청, prc:처리 - 양식결재라인상세(AP_FORM_APV_LN_D) 테이블
			model.put("apvLnTitlTypCd", "apv");
			return LayoutUtil.getJspPath("/ap/adm/form/setDocApvLnPop");
			
		// apvLn1LnAgr:결재+합의(1줄)
		} else if("apvLn1LnAgr".equals(formApvLnTypCd)){
			model.put("apvLnTitlTypCd1", "apv");
			model.put("apvLnTitlTypCd2", "agr");
			return LayoutUtil.getJspPath("/ap/adm/form/setDocApvLn1LnAgrPop");
			
		// apvLn2LnAgr:결재+합의(2줄)
		} else if("apvLn2LnAgr".equals(formApvLnTypCd)){
			model.put("apvLnTitlTypCd1", "apv");
			model.put("apvLnTitlTypCd2", "agr");
			return LayoutUtil.getJspPath("/ap/adm/form/setDocApvLn2LnAgrPop");
			
		// apvLnDbl:신청+처리(이중결재), apvLnDblList:리스트(이중결재), apvLnWrtn:신청+처리(서면결재)
		} else if("apvLnDbl".equals(formApvLnTypCd) || "apvLnWrtn".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd)){
			model.put("apvLnTitlTypCd1", "req");
			model.put("apvLnTitlTypCd2", "prc");
			return LayoutUtil.getJspPath("/ap/adm/form/setDocApvLn1LnAgrPop");
			
		// apvLnOneTopList:최종결재+리스트
		} else if("apvLnOneTopList".equals(formApvLnTypCd)){
			// 항목 지정할 목록
			model.put("items", getItems());
			return LayoutUtil.getJspPath("/ap/adm/form/setDocApvLnOneTopPop");
		}
		return null;
	}

	///////////////////////////////////////////////
	// 항목지정
	//
	/** [팝업:항목지정] 항목지정 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocItemsPop", "/ap/env/setDocItemsPop"})
	public String setDocItemsPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="seq", required=false) String seq,
			ModelMap model) throws Exception {
		
		// 항목 지정할 목록
		model.put("items", getItems());
		
		return LayoutUtil.getJspPath("/ap/adm/form/setDocItemsPop");
	}
	
	/** 항목 지정할 목록 리턴 */
	private String[] getItems(){
		/*
		makrNm:기안자, makDeptNm:기안부서, makDd:기안일자, makDt:기안일시, 
		docNo:문서번호, docSubj:제목, docSecuNm:문서보안, seculNm:보안등급, 
		docTypNm:문서구분, docKeepPrdNm:보존기간, refDocNm:참조문서, opin:의견, 
		attFile:첨부파일, enfcScopNm:시행범위, enfcDd:시행일자, enfcDt:시행일시, 
		enfcDocKeepPrdNm:시행보존기간, recvDeptNm:수신처, recvDeptRefNm:수신처참조
		recvNo:접수번호, recvDd:접수일자, recvDt:접수일시
		*/
		return new String[]{
				"makrNm", "makDeptNm", "makDd", "makDt", 
				"docNo", "docSubj", "seculNm", 
				"docTypNm", "docKeepPrdNm", "refDocNm", "opin", 
				"attFile", "enfcScopNm", "enfcDd", "enfcDt", 
				"enfcDocKeepPrdNm", "recvDeptNm", "recvDeptRefNm",
				"recvNo", "recvDd", "recvDt"};
	}

	///////////////////////////////////////////////
	// 발신명의
	//
	/** [팝업:발신명의] 발신명의 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocSenderPop", "/ap/env/setDocSenderPop"})
	public String setDocSenderPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		ApFormTxtDVo apFormTxtDVo;
		
		// 디폴트 발신명의 설정
		// 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		String formTxtTypCd = "docSender";
		apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormTxtTypCd(formTxtTypCd);
		// ap.cmpt.areaForSender=발 신 명 의
		String docSenderTxt = messageProperties.getMessage("ap.cmpt.areaForSender", request);
		apFormTxtDVo.setTxtCont(docSenderTxt);
		apFormTxtDVo.setTxtSize("28pt");
		apFormTxtDVo.setTxtFontVa("돋움체");
		apFormTxtDVo.setTxtStylVa("font-weight:bold;");
		model.put(formTxtTypCd, apFormTxtDVo);
		model.put("docSenderTxt", docSenderTxt);
		
		// 디폴트 수신처 설정
		// 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		formTxtTypCd = "docReceiver";
		apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormTxtTypCd(formTxtTypCd);
		// ap.cmpt.areaForReceiver=수신처 들어가는 자리 입니다.
		String docReceiverTxt = messageProperties.getMessage("ap.cmpt.areaForReceiver", request);
		apFormTxtDVo.setTxtCont(docReceiverTxt);
		apFormTxtDVo.setTxtSize("9pt");
		apFormTxtDVo.setTxtFontVa("돋움체");
		model.put(formTxtTypCd, apFormTxtDVo);
		model.put("docReceiverTxt", docReceiverTxt);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setDocSenderPop");
	}
	
	///////////////////////////////////////////////
	// 바닥글
	//
	/** [팝업:바닥글] 바닥글 설정 */
	@RequestMapping(value = {"/ap/adm/form/setDocFooterPop", "/ap/env/setDocFooterPop"})
	public String setDocFooterPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		ApFormTxtDVo apFormTxtDVo;
		
		// 디폴트 바닥글 설정
		// 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		String formTxtTypCd = "docFooter";
		apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormTxtTypCd(formTxtTypCd);
		apFormTxtDVo.setTxtSize("9pt");
		apFormTxtDVo.setTxtFontVa("돋움체");
		model.put(formTxtTypCd, apFormTxtDVo);

		return LayoutUtil.getJspPath("/ap/adm/form/setDocFooterPop");
	}
	
	///////////////////////////////////////////////
	// 인쇄설정
	//
	/** [팝업:인쇄설정] */
	@RequestMapping(value = {"/ap/adm/form/setDocPrinterPop", "/ap/env/setDocPrinterPop"})
	public String setDocPrinterPop(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);

		// 양식넓이구분코드
		List<PtCdBVo> formWdthTypCdList = ptCmSvc.getCdList("FORM_WDTH_TYP_CD", langTypCd, "Y");
		model.put("formWdthTypCdList", formWdthTypCdList);
		
		return LayoutUtil.getJspPath("/ap/adm/form/setDocPrinterPop");
	}
	
	
	/** [오른쪽 프레임:팝업-저장] 양식설정 저장 */
	@RequestMapping(value = {"/ap/adm/form/transForm", "/ap/env/transForm"})
	public String transForm(HttpServletRequest request,
			@Parameter(name="formId", required=false) String formId,
			@Parameter(name="formCombIds", required=false) String formCombIds,
			@Parameter(name="formApvLnTypCd", required=false) String formApvLnTypCd,
			@Parameter(name="maxItemSeq", required=false) String maxItemSeq,
			@Parameter(name="bodyHtml", required=false) String bodyHtml,
			ModelMap model) throws Exception {
		
		if(formId==null || formId.isEmpty()){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String message = messageProperties.getMessage("cm.msg.notValidCall", request);
			model.put("message", message);
			return LayoutUtil.getResultJsp();
		}
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		// 양식기본(AP_FORM_B) 테이블 - 양식 본문 UPDATE
		ApFormBVo apFormBVo = new ApFormBVo();
		VoUtil.bind(request, apFormBVo);
		apFormBVo.setModrUid(userVo.getUserUid());
		apFormBVo.setFormSeq("next");//변경할때마다 formSeq 를 올리고 상신할때 formSeq 에 해당하는 양식정보가 진행양식에 없으면 진행양식으로 복사함
		apFormBVo.setModDt("sysdate");
		queryQueue.update(apFormBVo);
		
		// 양식구성상세(AP_FORM_COMB_D) 테이블 - 조합구성 순서
		if(formCombIds!=null && !formCombIds.isEmpty()){
			Integer sortOrdr = 1;
			int p;
			// 양식구성상세(AP_FORM_COMB_D) 테이블
			ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
			apFormCombDVo.setFormId(formId);
			queryQueue.delete(apFormCombDVo);
			
			for(String formCombId : formCombIds.split(",")){
				apFormCombDVo = new ApFormCombDVo();
				apFormCombDVo.setFormId(formId);
				if((p = formCombId.indexOf(':'))>0){
					apFormCombDVo.setFormCombId(formCombId.substring(0, p));
					apFormCombDVo.setFormCombSeq(formCombId.substring(p+1));
				} else {
					apFormCombDVo.setFormCombId(formCombId);
					apFormCombDVo.setFormCombSeq("1");
				}
				apFormCombDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				queryQueue.insert(apFormCombDVo);
			}
		}
		
		// 양식결재라인상세(AP_FORM_APV_LN_D) 테이블
		ApFormApvLnDVo apFormApvLnDVo = new ApFormApvLnDVo();
		apFormApvLnDVo.setFormId(formId);
		queryQueue.delete(apFormApvLnDVo);
		
		// formApvLnTypCd:양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재
		if(formApvLnTypCd!=null && !formApvLnTypCd.isEmpty()){
			// apv:결재, agr:합의, req:신청, prc:처리
			String[][] arr = formApvLnTypCd.equals("apvLn") || formApvLnTypCd.equals("apvLnMixd")
					|| formApvLnTypCd.equals("apvLnOnecard") ? new String[][] {{"1","apv"}} :
				formApvLnTypCd.equals("apvLn1LnAgr") ? new String[][] {{"1","apv"}, {"1","agr"}} :
					formApvLnTypCd.equals("apvLn2LnAgr") ? new String[][] {{"1","apv"}, {"2","agr"}} :
						formApvLnTypCd.equals("apvLnDbl") ? new String[][] {{"1","req"}, {"1","prc"}} : 
						formApvLnTypCd.equals("apvLnDblList") ? new String[][] {{"1","req"}, {"1","prc"}} : 
						formApvLnTypCd.equals("apvLnWrtn") ? new String[][] {{"1","req"}, {"1","prc"}} : 
							// apvLnList=결재자 리스트, apvLnOneTopList=최종결재+리스트, apvLnMultiTopList=서명+리스트
							formApvLnTypCd.equals("apvLnList") || formApvLnTypCd.equals("apvLnOneTopList") || formApvLnTypCd.equals("apvLnMultiTopList") ? new String[][] {{"1","apv"}} :
							new String[][] {{}};
			for(String[] item : arr){
				apFormApvLnDVo = new ApFormApvLnDVo();
				apFormApvLnDVo.setFormId(formId);
				apFormApvLnDVo.setFormCombSeq(item[0]);
				apFormApvLnDVo.setApvLnTitlTypCd(item[1]);
				apFormApvLnDVo.setApvLnDispTypCd(request.getParameter(item[1]+"-apvLnDispTypCd"));
				apFormApvLnDVo.setMaxCnt(request.getParameter(item[1]+"-maxCnt"));
				apFormApvLnDVo.setAlnVa(request.getParameter(item[1]+"-alnVa"));
				apFormApvLnDVo.setBordUseYn(request.getParameter(item[1]+"-bordUseYn"));
				apFormApvLnDVo.setTitlUseYn(request.getParameter(item[1]+"-titlUseYn"));
				apFormApvLnDVo.setLstDupDispYn(request.getParameter(item[1]+"-lstDupDispYn"));
				queryQueue.insert(apFormApvLnDVo);
			}
		}
		
		// 양식텍스트상세(AP_FORM_TXT_D) 테이블
		ApFormTxtDVo apFormTxtDVo;
		
		// docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글, docPrinter:인쇄설정
		for(String item : new String[]{"docHeader", "docName", "docSender", "docReceiver", "docFooter"}){
			apFormTxtDVo = new ApFormTxtDVo();
			apFormTxtDVo.setFormId(formId);
			apFormTxtDVo.setFormTxtTypCd(item);
			
			if("Y".equals(request.getParameter(item+"-useYn"))){
				apFormTxtDVo.setTxtCont(request.getParameter(item+"-txtCont"));
				apFormTxtDVo.setTxtFontVa(request.getParameter(item+"-txtFontVa"));
				apFormTxtDVo.setTxtStylVa(request.getParameter(item+"-txtStylVa"));
				apFormTxtDVo.setTxtSize(request.getParameter(item+"-txtSize"));
				apFormTxtDVo.setTxtColrVa(request.getParameter(item+"-txtColrVa"));
				
//				if(item.equals("docReceiver") && 
//						(apFormTxtDVo.getTxtCont()==null || apFormTxtDVo.getTxtCont().isEmpty())){
//					apFormTxtDVo.setTxtCont(ApConstant.NO_VALUE);
//				}
				
				queryQueue.store(apFormTxtDVo);
			} else {
				queryQueue.delete(apFormTxtDVo);
			}
		}
		
		// 양식이미지상세(AP_FORM_IMG_D) 테이블
		ApFormImgDVo apFormImgDVo;
		// docLogo:로고, docSymbol:심볼
		for(String item : new String[]{"docHeaderImg", "docLogo", "docSymbol"}){
			apFormImgDVo = new ApFormImgDVo();
			apFormImgDVo.setFormId(formId);
			apFormImgDVo.setFormImgTypCd(item);
			
			if("Y".equals(request.getParameter(item+"-useYn"))){
				apFormImgDVo.setImgPath(request.getParameter(item+"-imgPath"));
				apFormImgDVo.setImgWdth(request.getParameter(item+"-imgWdth"));
				apFormImgDVo.setImgHght(request.getParameter(item+"-imgHght"));
				queryQueue.store(apFormImgDVo);
			} else {
				queryQueue.delete(apFormImgDVo);
			}
		}
		
		// 항목지정
		
		// 양식항목상세(AP_FORM_ITEM_D) 테이블 - 삭제
		ApFormItemDVo apFormItemDVo = new ApFormItemDVo();
		apFormItemDVo.setFormId(formId);
		queryQueue.delete(apFormItemDVo);
		
		// 양식항목내역(AP_FORM_ITEM_L) 테이블 - 삭제
		ApFormItemLVo apFormItemLVo = new ApFormItemLVo();
		apFormItemLVo.setFormId(formId);
		queryQueue.delete(apFormItemLVo);
		
		// maxItemSeq - 항목지정 최대 시퀀스
		//if(maxItemSeq!=null && !maxItemSeq.isEmpty() && !maxItemSeq.equals("0")){
			
		int i, p, q;
		int itemSeq = (maxItemSeq!=null && !maxItemSeq.isEmpty() && !maxItemSeq.equals("0"))
				? Integer.parseInt(maxItemSeq) : 0;
		String formCombSeq, value;
		String[] paramNames;
		//for(i=1;i<=itemSeq;i++){
		for(i=1;;i++){
			
			// 99999 등 항목지정 외에 항목을 지정할 경우 - [예] 결재라인) 최종결재+결재라인
			//   99999 : 결재라인) 최종결재+결재라인
			if(i>itemSeq){
				if(i<90000){
					i = 99999;
				} else if(i>99999) {
					break;
				}
			}
			
			if(request.getParameter("items-"+i+"-rowCnt")!=null){
				
				formCombSeq = Integer.toString(i);
				
				apFormItemDVo = new ApFormItemDVo();
				apFormItemDVo.setFormId(formId);
				apFormItemDVo.setFormCombSeq(formCombSeq);
				apFormItemDVo.setRowCnt(request.getParameter("items-"+i+"-rowCnt"));
				apFormItemDVo.setColCnt(request.getParameter("items-"+i+"-colCnt"));
				queryQueue.insert(apFormItemDVo);
				
				paramNames = ApParamUtil.getParameterNames(request, "items-"+i+"-", null);
				for(String key : paramNames){
					q = key.lastIndexOf('-');
					p = key.lastIndexOf('-', q-1);
					if(p<0 || q<0) continue;
					
					apFormItemLVo = new ApFormItemLVo();
					apFormItemLVo.setFormId(formId);
					apFormItemLVo.setFormCombSeq(formCombSeq);
					apFormItemLVo.setRowNo(key.substring(p+1, q));
					apFormItemLVo.setColNo(key.substring(q+1));
					
					value = request.getParameter(key);
					p = value==null ? -1 : value.lastIndexOf('-');
					if(p<0) continue;
					
					apFormItemLVo.setItemId(value.substring(0, p));
					apFormItemLVo.setCspnVa(value.substring(p+1));
					queryQueue.insert(apFormItemLVo);
				}
			}
		}
		
		// 본문 (편집양식) - XML 저장
		String erpFormTypCd = request.getParameter("erpFormTypCd");
		if("xmlEditFromAp".equals(erpFormTypCd)){
			String formEditBodyXML = request.getParameter("formEditBodyXML");
			if(formEditBodyXML!=null){
				formEditBodyXML = formEditBodyXML.trim().replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
				
				ApFormXmlDVo apFormXmlDVo = new ApFormXmlDVo();
				apFormXmlDVo.setFormId(formId);
				queryQueue.delete(apFormXmlDVo);
				
				apFormXmlDVo = new ApFormXmlDVo();
				apFormXmlDVo.setFormId(formId);
				apFormXmlDVo.setFormXmlCd("formEditBodyXML");
				apFormXmlDVo.setFormXmlCont(formEditBodyXML);
				queryQueue.insert(apFormXmlDVo);
			}
		}
		
		
		
		// 업무관리 양식
		if("wfForm".equals(erpFormTypCd)){
			
			String wfFormNo = request.getParameter("wfFormNo");
			String wfGenId = request.getParameter("wfGenId");
			String erpFormId = request.getParameter("erpFormId");
			String wfRescId = request.getParameter("wfRescId");
			
			// 업무관리 - 양식 변경
			if(wfFormNo!=null && !wfFormNo.isEmpty() && wfGenId!=null && !wfGenId.isEmpty()){
				
				if(erpFormId == null || erpFormId.isEmpty()){
					erpFormId = apCmSvc.createId("AP_ERP_FORM_B");
					apFormBVo.setErpFormId(erpFormId);
				}
				
				ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
				apErpFormBVo.setErpFormId(erpFormId);
				apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
				
				String rescId;
				ApRescDVo apRescDVo;
				boolean newErpForm = false;
				if(apErpFormBVo == null){
					newErpForm = true;
					apErpFormBVo = new ApErpFormBVo();
					apErpFormBVo.setErpFormId(erpFormId);
					apErpFormBVo.setCompId(userVo.getCompId());
					apErpFormBVo.setErpFormTypCd(erpFormTypCd);
					
					rescId = apCmSvc.createId("AP_RESC_D");
					apErpFormBVo.setRescId(rescId);
				} else {
					rescId = apErpFormBVo.getRescId();
					
					apErpFormBVo = new ApErpFormBVo();
					apErpFormBVo.setErpFormId(erpFormId);
					
					apRescDVo = new ApRescDVo();
					apRescDVo.setCompId(userVo.getCompId());
					apRescDVo.setRescId(rescId);
					queryQueue.delete(apRescDVo);
				}
				
				WfRescBVo wfRescBVo = new WfRescBVo();
				wfRescBVo.setRescId(wfRescId);
				@SuppressWarnings("unchecked")
				List<WfRescBVo> rescList = (List<WfRescBVo>)commonSvc.queryList(wfRescBVo);
				if(rescList != null){
					for(WfRescBVo rescVo : rescList){
						apRescDVo = new ApRescDVo();
						apRescDVo.setCompId(userVo.getCompId());
						apRescDVo.setRescId(rescId);
						apRescDVo.setLangTypCd(rescVo.getLangTypCd());
						apRescDVo.setRescVa(rescVo.getRescVa());
						queryQueue.insert(apRescDVo);
					}
				}
				
				apErpFormBVo.setRegUrl(ApConstant.WF_REG_PATH+".jsp?formNo="+wfFormNo+"&genId="+wfGenId);
				
				if(newErpForm) queryQueue.insert(apErpFormBVo);
				else queryQueue.update(apErpFormBVo);
			}
			
			// 업무관리 양식 저장 - 양식 편집 시점
			wfMdFormSvc.saveByMdFormData(request, queryQueue, "AP", wfGenId, wfFormNo, formId);
			
		}
		
		try {
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] ERP 양식 - 프레임 가지고 있는 팝업 */
	@RequestMapping(value = {"/ap/adm/form/listErpFormPop", "/ap/env/listErpFormPop"})
	public String listErpFormPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/adm/form/listErpFormPop");
	}
	
	/** [프레임] ERP 양식 - 목록 조회  */
	@RequestMapping(value = {"/ap/adm/form/listErpFormFrm", "/ap/env/listErpFormFrm"})
	public String listErpFormFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		//String langTypCd = LoginSession.getLangTypCd(request);
		
		UserVo userVo = LoginSession.getUser(request);
		
		ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
		VoUtil.bind(request, apErpFormBVo);
		apErpFormBVo.setCompId(userVo.getCompId());
		apErpFormBVo.setErpFormId(null);
		
		Integer recodeCount = commonSvc.count(apErpFormBVo);
		
		if(recodeCount!=null && recodeCount>0){
			request.setAttribute("pageRowCnt", Integer.valueOf(10));
			PersonalUtil.setPaging(request, apErpFormBVo, recodeCount);
			model.put("recodeCount", recodeCount);
			
			@SuppressWarnings("unchecked")
			List<ApOngdBVo> apErpFormBVoList = (List<ApOngdBVo>)commonSvc.queryList(apErpFormBVo);
			model.put("apErpFormBVoList", apErpFormBVoList);
		}
		
		return LayoutUtil.getJspPath("/ap/adm/form/listErpFormFrm");
	}
	
	/** [프레임] ERP 양식 - 상세 조회(수정)  */
	@RequestMapping(value = {"/ap/adm/form/setErpFormPop", "/ap/env/setErpFormPop"})
	public String listErpFormFrm(HttpServletRequest request,
			@Parameter(name="erpFormId", required=false) String erpFormId,
			ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(erpFormId != null && !erpFormId.isEmpty()){
			ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			apErpFormBVo.setQueryLang(langTypCd);
			apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
			
			if(apErpFormBVo != null){
				model.put("apErpFormBVo", apErpFormBVo);
				
				UserVo userVo = LoginSession.getUser(request);
				ApRescDVo apRescDVo = new ApRescDVo();
				apRescDVo.setCompId(userVo.getCompId());
				apRescDVo.setRescId(apErpFormBVo.getRescId());
				
				@SuppressWarnings("unchecked")
				List<ApRescDVo> apRescDVoList = (List<ApRescDVo>)commonSvc.queryList(apRescDVo);
				if(apRescDVoList != null){
					for(ApRescDVo storedApRescDVo : apRescDVoList){
						model.put(storedApRescDVo.getRescId()+"_"+storedApRescDVo.getLangTypCd(), storedApRescDVo.getRescVa());
					}
				}
			}
		}
//		//SSO_MET_CD
//		List<PtCdBVo> ssoMetCdList = ptCmSvc.getAllCdList("SSO_MET_CD", langTypCd);
//		if(ssoMetCdList!=null && !ssoMetCdList.isEmpty()){
//			model.put("ssoMetCdList", ssoMetCdList);
//		}
		
		return LayoutUtil.getJspPath("/ap/adm/form/setErpFormPop");
	}
	/** [히든플레임] ERP 양식 - 저장 */
	@RequestMapping(value = {"/ap/adm/form/transErpForm", "/ap/env/transErpForm"})
	public String transErpForm(HttpServletRequest request,
			Locale locale,
			ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
		VoUtil.bind(request, apErpFormBVo);
		
		if(apErpFormBVo.getErpFormId()==null || apErpFormBVo.getErpFormId().isEmpty()){
			UserVo userVo = LoginSession.getUser(request);
			apErpFormBVo.setCompId(userVo.getCompId());
			apErpFormBVo.setErpFormId(apCmSvc.createId("AP_ERP_FORM_B"));
			
			queryQueue.insert(apErpFormBVo);
		} else {
			queryQueue.update(apErpFormBVo);
		}
		
		// 리소스기본(AP_RESC_D) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장
		ApRescDVo apRescDVo = apRescSvc.collectApRescDVo(request, null, queryQueue);
		apErpFormBVo.setRescId(apRescDVo.getRescId());
		
		commonSvc.execute(queryQueue);
		
		//cm.msg.save.success=저장 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "parent.dialog.close('setErpFormDialog'); parent.getIframeContent('listErpFormFrm').reload();");
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJX] ERP 양식 삭제 */
	@RequestMapping(value = {"/ap/adm/form/transErpFormDelAjx", "/ap/env/transErpFormDelAjx"})
	public String transErpFormDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String erpFormId = (String)jsonObject.get("erpFormId");
		
		try {
			
			ApFormBVo apFormBVo = new ApFormBVo();
			apFormBVo.setErpFormId(erpFormId);
			if(commonSvc.count(apFormBVo) > 0){
				// ap.msg.canNotDelErpFormInUse=양식에 포함된 폼양식은 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("ap.msg.canNotDelErpFormInUse", request));
				return LayoutUtil.returnJson(model);
			}
			ApOngoFormBVo apOngoFormBVo = new ApOngoFormBVo();
			apOngoFormBVo.setErpFormId(erpFormId);
			if(commonSvc.count(apOngoFormBVo) > 0){
				// ap.msg.canNotDelErpFormInProg=진행문서에 포함된 폼양식은 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("ap.msg.canNotDelErpFormInProg", request));
				return LayoutUtil.returnJson(model);
			}
			
			ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
			
			if(apErpFormBVo==null){
				// cm.msg.del.noData=삭제할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.del.noData", request));
				return LayoutUtil.returnJson(model);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			UserVo userVo = LoginSession.getUser(request);
			
			ApRescDVo apRescDVo = new ApRescDVo();
			apRescDVo.setCompId(userVo.getCompId());
			apRescDVo.setRescId(apErpFormBVo.getRescId());
			queryQueue.delete(apErpFormBVo);
			
			apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			queryQueue.delete(apErpFormBVo);
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", locale));
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJX] ERP 양식 HTML 조회 */
	@RequestMapping(value = {"/ap/adm/form/getErpFormHtmlAjx", "/ap/env/getErpFormHtmlAjx", "/ap/box/getErpFormHtmlAjx", "/ap/adm/box/getErpFormHtmlAjx"})
	public String getErpFormHtmlAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String erpFormId = (String)jsonObject.get("erpFormId");
		String formBodyXML = (String)jsonObject.get("formBodyXML");
		String formBodyMode = (String)jsonObject.get("formBodyMode");//reg, view, edit
		
		if(formBodyXML!=null){
			formBodyXML = formBodyXML.trim().replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
		}
		
		ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
		apErpFormBVo.setErpFormId(erpFormId);
		apErpFormBVo = (ApErpFormBVo)commonSvc.queryVo(apErpFormBVo);
		
		String regUrl = apErpFormBVo==null ? null : apErpFormBVo.getRegUrl();
		if(regUrl!=null && regUrl.endsWith(".jsp")){
			if(formBodyXML==null || formBodyXML.isEmpty()){
				if(formBodyMode==null || formBodyMode.isEmpty()) formBodyMode = "reg";
				model.put("formBodyMode", formBodyMode);
				model.put("formBodyXML", new XMLElement(null));
			} else {
				if(formBodyMode==null || formBodyMode.isEmpty()) formBodyMode = "view";
				XMLElement xmlElement = SAXHandler.parse(formBodyXML);
				model.put("formBodyMode", formBodyMode);
				model.put("formBodyXML", xmlElement);
			}
			model.put("formBodyCall", "ajax");
			return regUrl.substring(0, regUrl.length()-4);
		}
		
		// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.formCmpt=폼 양식
		String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#ap.formCmpt"}, locale);
		model.put("message", message);
		return LayoutUtil.returnJson(model);
	}
	
	@RequestMapping(value = {"/ap/adm/form/getWfFormHtmlAjx", "/ap/env/getWfFormHtmlAjx", "/ap/box/getWfFormHtmlAjx", "/ap/adm/box/getWfFormHtmlAjx"})
	public String getWfFormHtmlAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String wfMode = (String)jsonObject.get("wfMode");
		
		if("view".equals(wfMode)){
			// 변경된 데이터로 조회 HTML 만들기
			String wfFormNo = (String)jsonObject.get("wfFormNo");
			String wfWorkNo = (String)jsonObject.get("wfWorkNo");
			wfMdFormSvc.viewFormByAP(request, model, wfFormNo, wfWorkNo);
			return ApConstant.WF_VIEW_PATH;
		} else {// wfMode:edit
			// 양식 편집에서 - 업무관리 양식을 변경 할때 - 등록화면 다시 불러옴
			String formId = (String)jsonObject.get("formId");
			String wfFormNo = (String)jsonObject.get("wfFormNo");
			String wfWorkNo = null;//새 양식 세팅에서 - workNo 없음 - 최신 문서
			boolean isFormEdit = true;
			wfMdFormSvc.setFormByAP(request, model, wfFormNo, formId, wfWorkNo, isFormEdit);
			return ApConstant.WF_REG_PATH;
		}
	}
}
