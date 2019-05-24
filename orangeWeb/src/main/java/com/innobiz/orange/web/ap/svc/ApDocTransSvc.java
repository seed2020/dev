package com.innobiz.orange.web.ap.svc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.cust.ApErpNotiSvc;
import com.innobiz.orange.web.ap.cust.ApWdSvc;
import com.innobiz.orange.web.ap.utils.ApCmUtil;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.utils.ApDocTransUtil;
import com.innobiz.orange.web.ap.utils.ApDocUtil;
import com.innobiz.orange.web.ap.utils.ApvLines;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgFileDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.ap.vo.ApOngdErpFormDVo;
import com.innobiz.orange.web.ap.vo.ApOngdExDVo;
import com.innobiz.orange.web.ap.vo.ApOngdExtnDocDVo;
import com.innobiz.orange.web.ap.vo.ApOngdHoldOpinDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPichDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPubBxCnfmLVo;
import com.innobiz.orange.web.ap.vo.ApOngdPubBxDVo;
import com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRefDocLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRefVwDVo;
import com.innobiz.orange.web.ap.vo.ApOngdSendDVo;
import com.innobiz.orange.web.ap.vo.ApOngdTrxDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormBVo;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.svc.EmSrchSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.svc.MessengerSvc;
import com.innobiz.orange.web.em.vo.CmSrchBVo;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.noti.AsyncNotiData;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtMobilePushMsgSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.svc.PtWebPushMsgSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;

/** 문서 저장 서비스 */
@Service
public class ApDocTransSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocTransSvc.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
//	/** 결재 문서 서비스 */
//	@Autowired
//	private ApDocSvc apDocSvc;
	
	/** 결재 양식 서비스 */
	@Autowired
	private ApFormSvc apFormSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 문서번호 체번 서비스 */
	@Autowired
	private ApDocNoSvc apDocNoSvc;
	
	/** 결재 문서 보안 서비스 */
	@Autowired
	private ApDocSecuSvc apDocSecuSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
//	/** 고객사별 기능 서비스 */
//	@Autowired
//	private ApCustFncSvc apCustFncSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 메신저 서비스 */
	@Resource(name = "emMessengerSvc")
	private MessengerSvc messengerSvc;
	
	/** PC 알림 발송 */
	@Autowired
	private PtWebPushMsgSvc ptWebPushMsgSvc;
	
	/** CLOB, BLOB 데이터 핸들링 */
	@Resource(name = "lobHandler")
	LobHandler lobHandler;
	
	/** ERP 연계 알림 서비스 */
	@Autowired
	private ApErpNotiSvc apErpNotiSvc;

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 모바일 푸쉬 메세지 발송용 */
	@Autowired
	private PtMobilePushMsgSvc ptMobilePushMsgSvc;
	
	/** 검색 서비스 */
	@Autowired
	private EmSrchSvc emSrchSvc;
	
	/** 문서관리 - 문서 서비스 - 보내기 용 */
	@Autowired
	private DmDocSvc dmDocSvc;
	
	/** 문서관리 - 저장소 서비스 - 보내기 용 */
	@Autowired
	private DmStorSvc dmStorSvc;
	
	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 연차관리 */
	@Autowired
	private ApWdSvc apWdSvc;
	
	/** 시행변환 처리 */
	private void processTrxTrans(QueryQueue queryQueue, UploadHandler uploadHandler, String userAuth, String menuId,
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException, IOException {
		
		Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
		Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
		Map<String, List<File>> fileListMap = uploadHandler.getFileListMap();//파일 리스트
		
		String statCd = paramMap.get("statCd");// 상태코드 - 문서상태코드 or 결재상태코드
		String trxApvNo = paramMap.get("trxApvNo");// 변환결재번호
		String orgnApvNo = paramMap.get("orgnApvNo");// 원본결재번호
		String apvNo = paramMap.get("apvNo");
		
		DistHandler distHandler = null;
		
		if(apvNo==null || apvNo.isEmpty()){
			apvNo = orgnApvNo;
		}
		
		// 문서 조회
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		
		// 문서 변환의 경우
		if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
			
			// 변환 문서번호 생성
			boolean isNewDoc = false;
			if(trxApvNo==null || trxApvNo.isEmpty()){
				Long no = apCmSvc.createNo("AP_ONGD_B");
				trxApvNo = (no==null) ? null : no.toString();
				isNewDoc = true;
			}
			
			if(storedApOngdBVo==null){
				LOGGER.error("Fail trx - no doc ! - orgnApvNo:"+orgnApvNo+"  userUid:"+userVo.getUserUid());
				// ap.msg.noDoc=해당 문서가 없습니다.
				throw new CmException("ap.msg.noDoc", locale);
			}
			
			// 진행문서변환상세
			ApOngdTrxDVo apOngdTrxDVo = new ApOngdTrxDVo();
			apOngdTrxDVo.setTrxApvNo(trxApvNo);
			apOngdTrxDVo.setOrgnApvNo(orgnApvNo);
			
			// 신규 양식으로 만들때만 양식ID 넘기고 체크함
			String formId = paramMap.get("formId");// 양식ID
			if(formId==null || formId.isEmpty()){
				LOGGER.error("Fail trx - no formId ! - orgnApvNo:"+orgnApvNo+"  userUid:"+userVo.getUserUid());
				// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
				throw new CmException("cm.msg.notValidPage", locale);
			}
			apOngdTrxDVo.setFormId(formId);
			
			// 같은 양식이 아니면
			if(!formId.equals(storedApOngdBVo.getFormId())){
				String formSeq = apFormSvc.storeOngoForm(queryQueue, formId, model);
				apOngdTrxDVo.setFormSeq(formSeq);
				
				// 본문높이, 양식넓이 세팅
				ApFormBVo apFormBVo = new ApFormBVo();
				apFormBVo.setFormId(formId);
				apFormBVo.setFormSeq(formSeq);
				apFormBVo = (ApFormBVo) commonDao.queryVo(apFormBVo);
				if(apFormBVo != null){
					apOngdTrxDVo.setBodyHghtPx(apFormBVo.getBodyHghtPx());
					apOngdTrxDVo.setFormWdthTypCd(apFormBVo.getFormWdthTypCd());
				}
				
				// 바닥글 세팅
				String footerVa = (String)model.get("footerVa");
				footerVa = replaceFooterVa(footerVa, storedApOngdBVo.getMakDeptId());
				apOngdTrxDVo.setFooterVa(footerVa);
				
			} else {// 같은 양식이면
				apOngdTrxDVo.setFormSeq(storedApOngdBVo.getFormSeq());
				apOngdTrxDVo.setBodyHghtPx(storedApOngdBVo.getBodyHghtPx());
				apOngdTrxDVo.setFormWdthTypCd(storedApOngdBVo.getFormWdthTypCd());
				apOngdTrxDVo.setFooterVa(storedApOngdBVo.getFooterVa());
			}
			
			// 변환문 저장
			if(isNewDoc){
				queryQueue.insert(apOngdTrxDVo);
			} else {
				queryQueue.update(apOngdTrxDVo);
			}
			
			// 본문 저장
			String bodyHtml = paramMap.get("bodyHtml");// 본문HTML
			if(bodyHtml==null || bodyHtml.isEmpty()){
				String noBodyHtml = paramMap.get("noBodyHtml");
				String formBodyHTML = paramMap.get("formBodyHTML");
				if("Y".equals(noBodyHtml) && formBodyHTML!=null && !formBodyHTML.isEmpty()){
					bodyHtml = formBodyHTML;
				}
			}
			if(bodyHtml==null || bodyHtml.isEmpty()){
				ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
				apOngdBodyLVo.setApvNo(apvNo);
				apOngdBodyLVo.setBodyHstNo(storedApOngdBVo.getBodyHstNo());
				apOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(apOngdBodyLVo);
				if(apOngdBodyLVo == null){
					LOGGER.error("Fail trans - no stored body html ! - apvNo:"+apvNo);
					// ap.trans.noBody=본문 내용이 없습니다.
					throw new CmException("ap.trans.noBody", locale);
				}
				apOngdBodyLVo = createApOngdBodyLVo(trxApvNo, "1", apOngdBodyLVo.getBodyHtml(), userVo);
				queryQueue.insert(apOngdBodyLVo);
			} else {
				ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(trxApvNo, "1", bodyHtml, userVo);
				queryQueue.insert(apOngdBodyLVo);
			}

			/////////// 첨부파일
			distHandler = distManager.createHandler("ap", locale);
			
			// 업로드된 첨부파일과 DB에 저장된 데이터로 한세트의 첨부파일 목록을 만듬
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = parseAttFile(paramListMap, fileListMap, distHandler, null,
					trxApvNo, storedApOngdBVo.getAttHstNo(), orgnApvNo, userVo.getUserUid(), "Y", locale, null);
			
			// 조합된 데이터(기존첨부(DB) + 신규첨부(업로드)) 입력
			for(ApOngdAttFileLVo newApOngdAttFileLVo : apOngdAttFileLVoList){
				newApOngdAttFileLVo.setAttHstNo("1");
				queryQueue.insert(newApOngdAttFileLVo);
			}
			
		}
		
		/////////// 수신처
		List<ApOngdRecvDeptLVo> recvDeptList = parseRecvDeptJson(paramListMap.get("recvDept"), apvNo,
				storedApOngdBVo.getDocLangTypCd(), null, locale, userVo.getUserUid(), null);
		
		// 수신처이력번호
		String recvDeptHstNo = storedApOngdBVo.getRecvDeptHstNo();
		
		String recvDeptModified = paramMap.get("recvDeptModified");// 수신처 변경 여부
		if("Y".equals(recvDeptModified)){
			ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			apOngdRecvDeptLVo.setApvNo(apvNo);
			apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
			queryQueue.delete(apOngdRecvDeptLVo);
		}
		
		//전송된 값 입력
		if(recvDeptList!=null){
			for(ApOngdRecvDeptLVo paramApOngdRecvDeptLVo : recvDeptList){
				paramApOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
				queryQueue.insert(paramApOngdRecvDeptLVo);
			}
		}
		
		List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
		
		// 심사요청
		if("reqCensr".equals(statCd)){
			String pichUid = paramMap.get("pichUid");
			String pichOpinCont = paramMap.get("pichOpinCont");
			reqCensr(queryQueue, messengerQueue, storedApOngdBVo, apvNo, 
					trxApvNo, pichUid, pichOpinCont, userAuth, 
					userVo, locale, model);
			
		// 발송
		} else if("sendDoc".equals(statCd)){
			
			// 수신처가 없으면
			if(recvDeptList==null && "Y".equals(recvDeptModified)){
				LOGGER.error("Fail sendDoc - no recvDept -  apvNo:"+apvNo);
				//ap.msg.noRecvDept=수신처가 지정되지 않았습니다.
				throw new CmException("ap.msg.noRecvDept", locale);
			}
			
			String ofsePath = paramMap.get("ofsePath");
			String ofseHghtPx = paramMap.get("ofseHghtPx");
			String sendrNmRescId = paramMap.get("sendrNmRescId");
			String sendrNmRescNm = paramMap.get("sendrNmRescNm");
			String sendWithRefDocYn = paramMap.get("sendWithRefDocYn");
			
			processSendDoc(queryQueue, messengerQueue, 
					apvNo, trxApvNo, ofsePath, ofseHghtPx, 
					sendrNmRescId, sendrNmRescNm, sendWithRefDocYn,
					storedApOngdBVo, recvDeptList, "Y".equals(recvDeptModified), false,
					userVo, locale, model);
			
		// 시행처리
		} else if("toToSendBx".equals(statCd)){
			// 기안자, 관리자 체크
			checkMakr(storedApOngdBVo, "ap.btn.toToSendBx", userAuth,
					userVo, locale, model);//ap.btn.toToSendBx=시행처리
			toToSendBx(queryQueue, apvNo, 
					trxApvNo, storedApOngdBVo, 
					userVo, locale, model);
			
		}
		
		// 파일 배포
		if(distHandler!=null){
			distHandler.distribute();
			uploadHandler.removeTempDir();
		}
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 모바일 푸쉬
		List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
		addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
		
		commonSvc.execute(queryQueue);
		
		// 메신저 메세지 일괄 발송
		sendMesseger(messengerQueue, optConfigMap);
		// 모바일 푸쉬 메세지 - 보내기
		ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
		
		String bxId = paramMap.get("bxId");
		String url = apBxSvc.getBxUrlByBxId(userVo, bxId, menuId);
		char sp = url.indexOf('?') > 0 ? '&' : '?';
		String queryString = paramMap.get("queryString");
		model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+sp+queryString+"\");");
	}
	
	/** 관리자 결재 처리 */
	public void processAdmTrans(HttpServletRequest request,
			QueryQueue queryQueue, Locale locale,
			ModelMap model) throws CmException, SQLException, IOException, Exception {
		
		String apvNo = request.getParameter("apvNo");// 결재번호
		String apvLnPno = request.getParameter("apvLnPno");// 부모결재라인번호
		String apvLnNo = request.getParameter("apvLnNo");// 결재라인번호
		String process = request.getParameter("process");
		
//System.out.println("apvNo : "+apvNo);
//System.out.println("apvLnPno : "+apvLnPno);
//System.out.println("apvLnNo : "+apvLnNo);
//System.out.println("process : "+process);
		
		ApOngdBVo apOngdBVo = null;
		if(apvNo!=null && !apvNo.isEmpty()){
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setQueryLang(locale.getLanguage());
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		}
		
		// 결재선 변경 - 관리자 / 진행문서
		if("apvLn".equals(process)){
			
			String[] apvLns = request.getParameterValues("apvLn");
			List<String> apvLnList = ArrayUtil.toList(apvLns, true);
			
			String docLangTypCd = apOngdBVo.getDocLangTypCd();
			
			// JSON 파라미터 들을 파싱해서 에 담은 List 
			List<ApOngdApvLnDVo> paramApOngdApvLnDVoList = parseApvLnJson(apvLnList, apvNo, apvLnPno, docLangTypCd, locale);
			
			// 결재 옵션
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, apOngdBVo.getCompId());
			
			// 리소스 캐쉬용 - 문서언어구분코드 에 따른 어권별 리소스 설정용
			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
			Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
			
			ApOngdApvLnDVo apOngdApvLnDVo;
			int nParamApvLnNo = 0;
			int nApvLnNo = Integer.parseInt(apvLnNo);
			int i, size;
			
			// 저장된 데이터 조회
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnPno(apvLnPno);
			apOngdApvLnDVo.setQueryLang(docLangTypCd);
			apOngdApvLnDVo.setOrderBy("APV_LN_NO");
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> storedApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
			
			// 저장된 데이터 - 맵으로 전환
			String storedApvrRoleCd, paramApvrRoleCd, apvLnHstNo=null;
			Map<Integer, ApOngdApvLnDVo> storedMap = new HashMap<Integer, ApOngdApvLnDVo>();
			Map<Integer, ApOngdApvLnDVo> storedInfmMap = new HashMap<Integer, ApOngdApvLnDVo>();
			ApOngdApvLnDVo lastApOngdApvLnDVo = null;
			if(storedApOngdApvLnDVoList!=null){
				for(ApOngdApvLnDVo storedApOngdApvLnDVo : storedApOngdApvLnDVoList){
					if(apvLnHstNo==null){
						apvLnHstNo = storedApOngdApvLnDVo.getApvLnHstNo();
					}
					
					storedApvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
					
					if("psnInfm".equals(storedApvrRoleCd)){
						storedInfmMap.put(Hash.hashId(storedApOngdApvLnDVo.getApvrUid()), storedApOngdApvLnDVo);
					} else if("deptInfm".equals(storedApvrRoleCd)){
						storedInfmMap.put(Hash.hashId(storedApOngdApvLnDVo.getApvDeptId()), storedApOngdApvLnDVo);
					} else if("Y".equals(storedApOngdApvLnDVo.getApvrDeptYn())){
						storedMap.put(Hash.hashId(storedApOngdApvLnDVo.getApvDeptId()), storedApOngdApvLnDVo);
					} else {
						storedMap.put(Hash.hashId(storedApOngdApvLnDVo.getApvrUid()), storedApOngdApvLnDVo);
					}
					lastApOngdApvLnDVo = storedApOngdApvLnDVo;
				}
			}
			
			
			String storedApvStatCd, prevRoleCd, currRoleCd;
			ApOngdApvLnDVo paramApOngdApvLnDVo, storedApOngdApvLnDVo, updateApOngdApvLnDVo, prevApOngdApvLnDVo = null;
			
			size = paramApOngdApvLnDVoList==null ? 0 : paramApOngdApvLnDVoList.size();
			for(i=0; i<size; i++){
				
				paramApOngdApvLnDVo = paramApOngdApvLnDVoList.get(i);
				
				nParamApvLnNo = Integer.parseInt(paramApOngdApvLnDVo.getApvLnNo());
				if(nParamApvLnNo < nApvLnNo){
					continue;
					
				} else {
					
					paramApvrRoleCd = paramApOngdApvLnDVo.getApvrRoleCd();
					
					// 저장된 동일한 결재자/부서 찾음
					storedApOngdApvLnDVo = null;
					if("psnInfm".equals(paramApvrRoleCd)){
						storedApOngdApvLnDVo = storedInfmMap.get(Hash.hashId(paramApOngdApvLnDVo.getApvrUid()));
					} else if("deptInfm".equals(paramApvrRoleCd)){
						storedApOngdApvLnDVo = storedInfmMap.get(Hash.hashId(paramApOngdApvLnDVo.getApvDeptId()));
					} else if("Y".equals(paramApOngdApvLnDVo.getApvrDeptYn())){
						storedApOngdApvLnDVo = storedMap.get(Hash.hashId(paramApOngdApvLnDVo.getApvDeptId()));
					} else {
						storedApOngdApvLnDVo = storedMap.get(Hash.hashId(paramApOngdApvLnDVo.getApvrUid()));
					}
					
					// 저장된 결재상태코드
					storedApvStatCd = storedApOngdApvLnDVo==null ? null : storedApOngdApvLnDVo.getApvStatCd();
					
					// 기존것 - 해당 순번 삭제
					updateApOngdApvLnDVo = new ApOngdApvLnDVo();
					updateApOngdApvLnDVo.setApvNo(apvNo);
					updateApOngdApvLnDVo.setApvLnPno(apvLnPno);
					updateApOngdApvLnDVo.setApvLnNo(paramApOngdApvLnDVo.getApvLnNo());
					queryQueue.delete(updateApOngdApvLnDVo);
					
					if(storedApOngdApvLnDVo != null){
						
						if("apvd".equals(storedApvStatCd) || "rejt".equals(storedApvStatCd) || "cons".equals(storedApvStatCd)
								|| "pros".equals(storedApvStatCd) || "cmplVw".equals(storedApvStatCd)){
							
							storedApOngdApvLnDVo.setApvLnNo(paramApOngdApvLnDVo.getApvLnNo());
							
							queryQueue.insert(storedApOngdApvLnDVo);
							prevApOngdApvLnDVo = storedApOngdApvLnDVo;
						} else {
							
							storedApOngdApvLnDVo.setApvLnNo(paramApOngdApvLnDVo.getApvLnNo());
							
							storedApOngdApvLnDVo.setDblApvTypCd(paramApOngdApvLnDVo.getDblApvTypCd());	// 이중결재구분코드
							storedApOngdApvLnDVo.setApvrRoleCd(paramApOngdApvLnDVo.getApvrRoleCd());	// 결재자역할코드
							storedApOngdApvLnDVo.setApvDeptId(paramApOngdApvLnDVo.getApvDeptId());		// 결재부서ID
							
							// 결재선 데이터에 문서언어에 따른 리스스 바인딩
							setApOngdApvLnDVoResc(storedApOngdApvLnDVo, docLangTypCd, 
									userCacheMap, orgCacheMap, optConfigMap, locale);
							
							if(paramApvrRoleCd!=null && !paramApvrRoleCd.equals(storedApOngdApvLnDVo.getApvrRoleCd())){
								storedApOngdApvLnDVo.setApvrRoleCd(paramApvrRoleCd);
								storedApOngdApvLnDVo.setApvStatCd(getProperApvStatCd(paramApvrRoleCd, storedApOngdApvLnDVo.getApvStatCd()));
							}
							
							queryQueue.insert(storedApOngdApvLnDVo);
							prevApOngdApvLnDVo = storedApOngdApvLnDVo;
						}
						
					} else {
						
						paramApOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
						
						// 결재선 데이터에 문서언어에 따른 리스스 바인딩
						setApOngdApvLnDVoResc(paramApOngdApvLnDVo, docLangTypCd, 
								userCacheMap, orgCacheMap, optConfigMap, locale);
						
						
						prevRoleCd = prevApOngdApvLnDVo==null ? null : prevApOngdApvLnDVo.getApvrRoleCd();
						
						storedApvStatCd = "befo";
						if(prevRoleCd!=null && prevRoleCd.indexOf("ParalAgr")>0){
							
							currRoleCd = paramApOngdApvLnDVo.getApvrRoleCd();
							if(currRoleCd.indexOf("OrdrdAgr")>0){
								currRoleCd = currRoleCd.replace("OrdrdAgr", "ParalAgr");
								paramApOngdApvLnDVo.setApvrRoleCd(currRoleCd);
							}
							
							if(currRoleCd.indexOf("ParalAgr")>0){
								storedApvStatCd = "in";
								paramApOngdApvLnDVo.setPrevApvrApvDt(prevApOngdApvLnDVo.getPrevApvrApvDt());
							}
						}
						paramApOngdApvLnDVo.setApvStatCd(getProperApvStatCd(paramApvrRoleCd, storedApvStatCd));
						
						queryQueue.insert(paramApOngdApvLnDVo);
						prevApOngdApvLnDVo = paramApOngdApvLnDVo;
					}
				}
			}
			
			// 마지막 파라미터에 없는 저장되어 있는 결재라인번호 - 삭제함
			int lastApvLnNo = lastApOngdApvLnDVo==null ? 0 : Integer.parseInt(lastApOngdApvLnDVo.getApvLnNo());
			for(i=nParamApvLnNo+1; i<=lastApvLnNo; i++){
				updateApOngdApvLnDVo = new ApOngdApvLnDVo();
				updateApOngdApvLnDVo.setApvNo(apvNo);
				updateApOngdApvLnDVo.setApvLnPno(apvLnPno);
				updateApOngdApvLnDVo.setApvLnNo(Integer.toString(i));
				queryQueue.delete(updateApOngdApvLnDVo);
			}
		}
		
		commonSvc.execute(queryQueue);
		
		//cm.msg.save.success=저장 되었습니다.
		String message = messageProperties.getMessage("cm.msg.save.success", locale);
		model.put("message", message);
		model.put("todo", "parent.location.reload();");

	}
	
	private String getProperApvStatCd(String apvrRoleCd, String oldApvStatCd){
		if(apvrRoleCd.endsWith("Agr")){
			if(oldApvStatCd.startsWith("in")) return "inAgr";
			else return "befoAgr";
		} else if(apvrRoleCd.endsWith("Vw")){
			if(oldApvStatCd.startsWith("in")) return "inVw";
			else return "befoVw";
		} else if(apvrRoleCd.endsWith("Infm")){
			return "";
		} else {
			if(oldApvStatCd.startsWith("in")) return "inApv";
			else return "befoApv";
		}
	}
	
	/** 결재 처리 */
	public void processTrans(HttpServletRequest request,
			QueryQueue queryQueue, Locale locale,
			ModelMap model) throws CmException, SQLException, IOException {
		
		UploadHandler uploadHandler = null;
		if(model.get("uploadHandler")==null){
			uploadHandler = uploadManager.createHandler(request, "temp", "ap");
			uploadHandler.upload();//업로드 파일 정보
		} else {
			// ERP 결재 연동 - 일괄 임시저장 - ApErpBulkCtrl.processErpBulk 에서 담아서 보냄
			uploadHandler = (UploadHandler)model.get("uploadHandler");
		}
		
		Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
		Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
		
		String apvNo = paramMap.get("apvNo");// 결재번호
		String statCd = paramMap.get("statCd");// 상태코드 - 문서상태코드 or 결재상태코드
		String apvLnPno = paramMap.get("apvLnPno");// 부모결재라인번호
		String apvLnNo = paramMap.get("apvLnNo");// 결재라인번호
		if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";// 결재라인부모번호 - 기본라인:0, 파생라인:부모의 결재라인번호
		model.put("statCd", statCd);
		
		// reqCensr:심사요청, sendDoc:발송, toToSendBx:시행처리
		if("reqCensr".equals(statCd) || "sendDoc".equals(statCd) || "toToSendBx".equals(statCd)){
			String userAuth = (String)request.getAttribute("_AUTH");
			String menuId = request.getParameter("menuId");
			UserVo userVo = LoginSession.getUser(request);
			processTrxTrans(queryQueue, uploadHandler, userAuth, menuId, userVo, locale, model);
			return;
		}
		
		boolean isMak	= "mak".equals(statCd);
		boolean isTemp	= "temp".equals(statCd);
		boolean isHold	= "hold".equals(statCd);
		boolean isReRevw = "reRevw".equals(statCd);//reRevw:재검토
		boolean isRejt = "rejt".equals(statCd);//rejt:반려
		boolean isCons = "cons".equals(statCd);//cons:합의반대
		// 합의기안 - 담당자 지정전 결재라인 세팅해서 합의승인 할 경우
		boolean atMakAgr = "Y".equals(paramMap.get("atMakAgr"));

		UserVo userVo = LoginSession.getUser(request);
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// [옵션] - 결재비밀번호사용 함 - 비밀번호 체크
		if(!isTemp && !isHold && !"Y".equals(optConfigMap.get("notAlwApvPw"))
				&& !(isMak && "Y".equals(optConfigMap.get("noMakPw")))){
			String secuId = paramMap.get("secuId");
			if(!apDocSecuSvc.confirmSecuId(request.getSession(), secuId)){
				if(!userVo.isAdminSesn()) {
					LOGGER.error("Fail trans - apv pw not confirmed ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
					//ap.trans.notCfrmApvPw=결재비밀번호를 확인 할 수 없습니다.
					throw new CmException("ap.trans.notCfrmApvPw", request);
				}
			}
		}
		
		//makVw:담당 - 접수문서 기안자
		if("makVw".equals(statCd)){
			// 메세지 큐 - 메신저 메세지 발송용
			List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
			processMakVw(request, queryQueue, messengerQueue, paramMap, paramListMap, locale, model);
			return;
		// fstVw:선람, pubVw:공람, paralPubVw:동시공람
		} else if("fstVw".equals(statCd) || "pubVw".equals(statCd) || "paralPubVw".equals(statCd)){
			// 메세지 큐 - 메신저 메세지 발송용
			List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
			String menuId = request.getParameter("menuId");
			paramMap.put("menuId", menuId);
			processVw(queryQueue, messengerQueue, paramMap, paramListMap, optConfigMap, false, statCd, userVo, locale, model);
			return;
		}
		
		DistHandler distHandler = distManager.createHandler("ap", locale);
		
		// 리소스 캐쉬용 - 문서언어구분코드 에 따른 어권별 리소스 설정용
		Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
		Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
		
		// 결재선 정보 관리용 유틸 - 전체 경로선을 가지고 루트경로, 현재경로, 부서별 경로 등을 리턴함
		ApvLines apvLines = new ApvLines(queryApvLn(apvNo), atMakAgr ? "0" : apvLnPno);
		
		// DB 조회한 내 결재선 정보
		ApOngdApvLnDVo myTurnApOngdApvLnDVo = null;
		// 저장할 내 결재선 정보
		ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave = new ApOngdApvLnDVo();
		
		if(apvNo != null && !apvNo.isEmpty()){
			
			// 부서합의 - 담당자 지정전 - 재검토 넘어올 경우
			if(atMakAgr){
				
				// 합의기안자 생성
				myTurnApOngdApvLnDVo = ApDocTransUtil.createMakAgrVo(userVo, apvLnPno);
				
				ApOngdApvLnDVo parent = apvLines.getParent(apvLnPno);
				if(parent==null){
					LOGGER.error("Fail trans - no parent of dept agr ! apvNo:"+apvNo+"  apvLnPno:"+apvLnPno);
					// ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
					throw new CmException("ap.msg.noApvr", locale);
				}
				myTurnApOngdApvLnDVo.setApvLnHstNo(parent.getApvLnHstNo());
				
				myTurnApOngdApvLnDVoToSave.setApvNo(apvNo);
				myTurnApOngdApvLnDVoToSave.setApvLnPno("0");
				myTurnApOngdApvLnDVoToSave.setApvLnNo(apvLnPno);
				
			} else {
				
				// 결재라인VO목록 에서 해당 (결재할 차례의) 사용자의 결재라인VO 찾기
				myTurnApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, locale);
				
				if(myTurnApOngdApvLnDVo == null){
					if(!isTemp && !isMak){
						LOGGER.error("Fail trans - no apvr ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
						//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
						throw new CmException("ap.msg.noApvr", locale);
					}
				} else if(!ApDocUtil.isInApvOfApvStat(myTurnApOngdApvLnDVo.getApvStatCd())){
					
					if(ApDocUtil.isCmplOfApvStat(myTurnApOngdApvLnDVo.getApvStatCd())){
						LOGGER.error("Fail trans - already done ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid()+"  apvStatCd:"+myTurnApOngdApvLnDVo.getApvStatCd());
						// ap.msg.alreadyDone=이미 처리한 문서 입니다.
						throw new CmException("ap.msg.alreadyDone", locale);
					} else {
						LOGGER.error("Fail trans - not your turn ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid()+"  apvStatCd:"+myTurnApOngdApvLnDVo.getApvStatCd());
						// ap.msg.notMyTurn=결재 문서가 회수 되었거나 결재 할 차례가 아닙니다.
						throw new CmException("ap.msg.notMyTurn", locale);
					}
					
				} else {
					myTurnApOngdApvLnDVoToSave.setApvNo(apvNo);
					myTurnApOngdApvLnDVoToSave.setApvLnPno(apvLnPno);
					myTurnApOngdApvLnDVoToSave.setApvLnNo(apvLnNo);
				}
			}
		}
		
		////////////////////////////////////////////////////////////////
		// 문서기본, 본문HTML
		//   - 진행문서기본(AP_ONGD_B), 진행문서본문내역(AP_ONGD_BODY_L)
		ApOngdBVo apOngdBVo = processBody(queryQueue, paramMap, 
				userVo, locale, model,
				myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave,
				userCacheMap, orgCacheMap, optConfigMap);
		ApOngdBVo storedApOngdBVo = (ApOngdBVo)model.get("storedApOngdBVo");
		
		if(isTemp || isMak){
			String formBodyXML = paramMap.get("formBodyXML");
			if(formBodyXML != null && !formBodyXML.isEmpty()){
				try {
					XMLElement xmlElement = SAXHandler.parse(formBodyXML);
					String xmlTypId = xmlElement.getAttr("head.typId");
					if(xmlTypId!=null && !xmlTypId.isEmpty()) apOngdBVo.setXmlTypId(xmlTypId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(isTemp){
			if(apOngdBVo.getApvLnTypCd()==null || apOngdBVo.getApvLnTypCd().isEmpty()){
				if("apvLnDbl".equals(apOngdBVo.getFormApvLnTypCd()) || "apvLnDblList".equals(apOngdBVo.getFormApvLnTypCd())){
					apOngdBVo.setApvLnTypCd("dblApv");
				} else {
					apOngdBVo.setApvLnTypCd("nomlApv");
				}
			}
		}
		
		boolean isByOne	= "byOne".equals(apOngdBVo.getApvLnTypCd());
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		// 결재번호 - KEY
		apvNo = apOngdBVo.getApvNo();
		
		///////////////////////////////////////////////////
		// 첨부 파일
		processAttach(queryQueue, uploadHandler, distHandler, apOngdBVo, locale, userVo,
				myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);
		
		///////////////////////////////////////////////////
		// 참조 문서
		processRefDoc(paramListMap.get("refApv"), paramMap.get("refApvModified"), 
				queryQueue, apOngdBVo, userVo,
				myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);
		
		///////////////////////////////////////////////////
		// 수신처
		processRecvDept(paramListMap.get("recvDept"), paramMap.get("recvDeptModified"), 
				apvNo, apOngdBVo, queryQueue, userVo,
				docLangTypCd, orgCacheMap, locale, 
				myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);

		// 메세지 큐 - 메신저 메세지 발송용
		List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
		
		///////////////////////////////////////////////////
		// 참조열람
		processRefVw(paramListMap.get("refVw"), paramMap.get("refVwModified"), apvNo, apOngdBVo,
				queryQueue, messengerQueue, userVo, model, docLangTypCd, orgCacheMap, locale, statCd);
		
		////////////////////////////////////////////////////////////////
		// 결재라인
		
		List<String> apvLnList = paramListMap.get("apvLn");
		// JSON 파라미터 들을 파싱해서 에 담은 List 
		List<ApOngdApvLnDVo> paramApOngdApvLnDVoList = parseApvLnJson(apvLnList, apvNo, apvLnPno, docLangTypCd, locale);
		
		// 임시저장, 기안
		if(isTemp || isMak){
			
			if(paramApOngdApvLnDVoList==null){
				LOGGER.error("Fail trans - no apv line ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
				// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [결재라인]
				throw new CmException("ap.msg.needToSetup", new String[]{"#ap.cmpt.apvLine"}, request);
			}
			
			// 임시저장, 기안 - 결재라인 저장
			processMakApvLn(paramApOngdApvLnDVoList, apvLines,
					paramMap, apOngdBVo, 
					userVo, userCacheMap, orgCacheMap,
					locale, queryQueue, messengerQueue, optConfigMap, model, 
					myTurnApOngdApvLnDVo, isTemp, isByOne);
			
		} else if(isHold){
			
			// 보류 - 결재라인 저장
			processHoldApvLn(paramApOngdApvLnDVoList, apvLines,
					paramMap, apOngdBVo, 
					userVo, userCacheMap, orgCacheMap, 
					locale, queryQueue, optConfigMap, 
					apvLnPno, myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);
			
		} else if(isReRevw || isRejt){
			
			// 재검토, 반려 - 결재라인 변경정보 무시
			processRejtApvLn(paramApOngdApvLnDVoList, apvLines,
					paramMap, apOngdBVo, 
					locale, queryQueue, messengerQueue, optConfigMap, model, docLangTypCd,
					myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, userVo, statCd);
			
		} else {
			
			// 승인,  찬성,반대
			processApvLn(paramApOngdApvLnDVoList, apvLines,
					paramMap, apOngdBVo, 
					userVo, userCacheMap, orgCacheMap, 
					locale, queryQueue, messengerQueue, optConfigMap, model, 
					apvLnPno, myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);
			
		}
		
		/*
		한화제약 에서 시작
		--
		커스텀 폼(컨트롤이 올라가 있는 커스텀 양식)을 만들어서 결재 진행하는 방식
			- 해당 필드의 값을 XML로 말아서 보관하며
			- XML을 html 전환 본문 html에 보관하며
			- XML 값을 추출 ERP에 통보 (결재에서 시작하여 ERP에 연동하는 방식)
		 * */
		
		// ERP 양식 결재 - XML
		ApOngdErpFormDVo apOngdErpFormDVo;
		String formBodyXML = paramMap.get("formBodyXML");
		
		if(isHold){
			if(formBodyXML != null && !formBodyXML.isEmpty()){
				
				formBodyXML = formBodyXML.trim().replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
				
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
				apOngdErpFormDVo.setErpVa(formBodyXML);
				queryQueue.store(apOngdErpFormDVo);
			}
		} else if(isReRevw || isRejt){
			
			apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
			queryQueue.delete(apOngdErpFormDVo);
			
			formBodyXML = null;
			apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
			apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
			if(apOngdErpFormDVo != null){
				formBodyXML = apOngdErpFormDVo.getErpVa();
			}
			
		} else {
			
			if(formBodyXML != null && !formBodyXML.isEmpty()){
				
				formBodyXML = formBodyXML.trim().replaceAll("\r\n", "&#10;").replaceAll("&amp;quot;", "&quot;");
				
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
				apOngdErpFormDVo.setErpVa(formBodyXML);
				queryQueue.store(apOngdErpFormDVo);
				
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
				queryQueue.delete(apOngdErpFormDVo);
				
			} else {
				
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
				apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				
				if(apOngdErpFormDVo != null){
					
					formBodyXML = apOngdErpFormDVo.getErpVaTypCd();
					
					apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
					queryQueue.store(apOngdErpFormDVo);
					
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
					queryQueue.delete(apOngdErpFormDVo);
				} else {
					
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
					apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
					if(apOngdErpFormDVo != null){
						formBodyXML = apOngdErpFormDVo.getErpVa();
					}
					
				}
			}
		}
		
		// [상신]업무관리 데이터 - 처리
		String wfWorkNo = paramMap.get("wfWorkNo");
		List<String> delWfWorkNoList = new ArrayList<String>();
		
		if(wfWorkNo!=null){//본문에 javascript 허용 세팅
			ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			apOngdExDVo.setExId("scriptBodyHtml");
			apOngdExDVo.setExVa("Y");
			queryQueue.store(apOngdExDVo);
		}
		
		// 보류 workNo 조회
		apOngdErpFormDVo = new ApOngdErpFormDVo();
		apOngdErpFormDVo.setApvNo(apvNo);
		apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
		ApOngdErpFormDVo storedHoldVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
		
		// 보류의 경우 - 보류 데이터로 저장
		if(isHold){
			if(wfWorkNo != null){
				if(storedHoldVo != null){// 보류 데이터 있으며
					if(!wfWorkNo.equals(storedHoldVo.getErpVa())){//기존 workNo와 보류 workNo가 다르면
						// 기존 보류 workNo - 삭제 처리(업무관리 쪽)
						delWfWorkNoList.add(storedHoldVo.getErpVa());
						// 신규 workNo 저장 - update
						new ApOngdErpFormDVo();
						apOngdErpFormDVo.setApvNo(apvNo);
						apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
						apOngdErpFormDVo.setErpVa(wfWorkNo);
						queryQueue.update(apOngdErpFormDVo);
					}
				} else {//보류 데이터 없으면
					// 신규  workNo 저장 - insert
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
					apOngdErpFormDVo.setErpVa(wfWorkNo);
					queryQueue.insert(apOngdErpFormDVo);
				}
			}
		// 재검토, 반려
		} else if(isReRevw || isRejt){
		
			apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
			queryQueue.delete(apOngdErpFormDVo);
			
		// 상신, 승인, 찬성
		} else {
			// 보류 데이터 있을 경우
			if(storedHoldVo != null){
				if(wfWorkNo != null){
					// 파라미터 workNo, 보류 workNo 다른값 - 보류데이터 삭제 처리(업무관리쪽)
					if(!wfWorkNo.equals(storedHoldVo.getErpVa())){
						delWfWorkNoList.add(storedHoldVo.getErpVa());
					}
				} else {
					// 파라미터 workNo가 없을 경우 - 보류 workNo를 신규값으로
					wfWorkNo = storedHoldVo.getErpVa();
				}
				// 보류 workNo 삭제(결재쪽)
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
				queryQueue.delete(apOngdErpFormDVo);
			}
			
			// 저장된 workNo 조회
			apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("wfWorkNo");
			ApOngdErpFormDVo storedVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
			
			// 저장된 workNo가 있을 경우
			if(storedVo != null){
				// 현 workNo(파라미터 또는 보류된)가 저장된 workNo와 다를 경우
				if(wfWorkNo != null && !wfWorkNo.equals(storedVo.getErpVa())){
					// 저장된 데이터 삭제 처리(업무관리쪽)
					delWfWorkNoList.add(storedVo.getErpVa());
					// 현 workNo(파라미터 또는 보류된) 저장 - update
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfWorkNo");
					apOngdErpFormDVo.setErpVa(wfWorkNo);
					queryQueue.update(apOngdErpFormDVo);
				}
			// 저장된 workNo가 없을 경우
			} else {
				
				// 현 workNo(파라미터 또는 보류된)가 있을 경우
				if(wfWorkNo != null){
					
					// workNo 저장
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfWorkNo");
					apOngdErpFormDVo.setErpVa(wfWorkNo);
					queryQueue.insert(apOngdErpFormDVo);
					
					// genId 저장
					String wfGenId = paramMap.get("wfGenId");
					if(wfGenId != null){
						apOngdErpFormDVo = new ApOngdErpFormDVo();
						apOngdErpFormDVo.setApvNo(apvNo);
						apOngdErpFormDVo.setErpVaTypCd("wfGenId");
						apOngdErpFormDVo.setErpVa(wfGenId);
						queryQueue.store(apOngdErpFormDVo);
					}
					
					// formNo 저장
					String wfFormNo = paramMap.get("wfFormNo");
					if(wfFormNo != null){
						apOngdErpFormDVo = new ApOngdErpFormDVo();
						apOngdErpFormDVo.setApvNo(apvNo);
						apOngdErpFormDVo.setErpVaTypCd("wfFormNo");
						apOngdErpFormDVo.setErpVa(wfFormNo);
						queryQueue.store(apOngdErpFormDVo);
					}
					
					// workNo - 커밋 처리
					// 결재에서 팝업으로 업무관리 양식의 데이터를 변경하고 확인 처리하면
					//  - 서버에 임시 데이터가 생성되고, 이것을 commit 처리 하지 않으면, 주기적으로 해당 데이터를 지움
					wfWorkNo = paramMap.get("wfWorkNo");
					if(wfWorkNo != null && wfFormNo != null){
						wfMdFormSvc.commitFormByAP(queryQueue, wfFormNo, wfWorkNo);
					}
				}
			}
		}
		
		// 원카드의 경우
		if("ERP_ONECARD".equals(apOngdBVo.getIntgTypCd())
				|| (storedApOngdBVo!=null && "ERP_ONECARD".equals(storedApOngdBVo.getIntgTypCd()))){
			
			ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			apOngdExDVo.setExId("erpBodyLock");//ERP 에서 넘어온 데이터 본문 수정 금지 - 원래 결재 옵션인데, 원카드의 경우 결제 옵션처럼 동작 하도록
			apOngdExDVo.setExVa("Y");
			queryQueue.store(apOngdExDVo);
			
			apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			apOngdExDVo.setExId("scriptBodyHtml");//본문의 javascript 사용금지 안함
			apOngdExDVo.setExVa("Y");
			queryQueue.store(apOngdExDVo);
			
			apOngdExDVo = new ApOngdExDVo();
			apOngdExDVo.setApvNo(apvNo);
			apOngdExDVo.setExId("noBodyOverflow");//본문에 overflow 스타일 주지 않음 - 마이너스 마진 적용되도록
			apOngdExDVo.setExVa("Y");
			queryQueue.store(apOngdExDVo);
		}
		
		if(!delWfWorkNoList.isEmpty()){
			
			String wfFormNo = paramMap.get("wfFormNo");
			if(wfFormNo == null){
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("wfFormNo");
				apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				wfFormNo = apOngdErpFormDVo.getErpVa();
			}
			for(String delWfWorkNo : delWfWorkNoList){
				wfMdFormSvc.deleteFormByAP(queryQueue, wfFormNo, delWfWorkNo);
			}
		}
		
		
		// 불필요
//		// ERP 양식 결재 - HTML
//		String formBodyHTML = paramMap.get("formBodyHTML");
//		if(formBodyHTML != null && !formBodyHTML.isEmpty()){
//			apOngdErpFormDVo = new ApOngdErpFormDVo();
//			apOngdErpFormDVo.setApvNo(apvNo);
//			apOngdErpFormDVo.setErpVaTypCd("formBodyHTML");
//			apOngdErpFormDVo.setErpVa(formBodyHTML);
//			queryQueue.store(apOngdErpFormDVo);
//		}
		
//		ApOngdBVo storedApOngdBVo = (ApOngdBVo)model.get("storedApOngdBVo");
		String prevDocStatCd = storedApOngdBVo==null ? null : storedApOngdBVo.getDocStatCd();
		String docStatCd = apOngdBVo.getDocStatCd();
		
		// 현재 결재자가 종결 했는지 여부
		boolean currApvd = false;
		
		// 결재 알림 처리 - [결재에서 시작 - ERP 통보]
		
		// [결재에서 시작 - ERP 통보] - 필요여부
		boolean needErpNotiFromAp = false;
		// 완결 되었을 때 
		if("apvd".equals(docStatCd) && !docStatCd.equals(prevDocStatCd)){
			currApvd = true;
			
			if(formBodyXML != null && !formBodyXML.isEmpty()){
				needErpNotiFromAp = true;
			}
		}
		
		// 결재 알림 처리 - [ERP에서 시작한 결재]:S
		AsyncNotiData asyncNotiData = null;
		String paramApvNo = paramMap.get("apvNo");
		String intgNo = apOngdBVo.getIntgNo();
		String intgTypCd = apOngdBVo.getIntgTypCd();
		if(storedApOngdBVo!=null && (intgNo==null || intgNo.isEmpty())){
			intgNo = storedApOngdBVo.getIntgNo();
			intgTypCd = storedApOngdBVo.getIntgTypCd();
		}
		
		if(intgNo!=null && !intgNo.isEmpty()){
			
			// 연계상태코드
			String intgStatCd = isByOne ? "apvd" : 
				isTemp ? "temp" :
					("apvd".equals(docStatCd) || "rejt".equals(docStatCd)) ? docStatCd :
						"ongo";
			
			if(CustConfig.CUST_SENTEC){// 센텍
				String formId = storedApOngdBVo!=null ? storedApOngdBVo.getFormId() : apOngdBVo.getFormId();
				String apvrRoleCd = myTurnApOngdApvLnDVo==null ? null : myTurnApOngdApvLnDVo.getApvrRoleCd();
				
				// 심의  - 양식:구매품의-F000010R & revw2 
				if("apvd".equals(statCd) && "revw2".equals(apvrRoleCd) && "F000010R".equals(formId)){
					intgStatCd = "consi";//심의
				}
			}
			
			asyncNotiData = createErpNotiData(intgTypCd, intgNo, apvNo, intgStatCd, 
					apOngdBVo.getDocNo(), queryQueue, locale);
			
			// 최초 기안시
			if(paramApvNo==null || paramApvNo.isEmpty()){
				// ERP 연계 파일 데이터 삭제
				ApErpIntgFileDVo apErpIntgFileDVo = new ApErpIntgFileDVo();
				apErpIntgFileDVo.setIntgNo(intgNo);
				queryQueue.delete(apErpIntgFileDVo);
			}
		}
		// 결재 알림 처리 - [ERP에서 시작한 결재]:E
		
		// 검색엔진 인덱싱 처리
		if(docStatCd!=null && !docStatCd.equals(prevDocStatCd)){
			// 승인 되거나, 반려 후 - keepRejtDoc:반려된 문서 등록대장에 저장 [옵션]
			if("apvd".equals(docStatCd) || ("rejt".equals(docStatCd) && "Y".equals(optConfigMap.get("keepRejtDoc")))){
				// 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
				String recLstTypCd = apOngdBVo.getRecLstTypCd();
				if("Y".equals(apOngdBVo.getRegRecLstRegYn()) && recLstTypCd!=null && !recLstTypCd.isEmpty()){
					addSrchIndex(apOngdBVo, userVo, queryQueue);
				}
			}
		}
		
		///////////////////////////////////////////////////
		// 문서관리 보내기 - 진행문서확장상세 테이블에 저장
		// >> 기안할때 문서관리 보내기 정보가 있으면
		String sSendToDm = paramMap.get("sendToDm");
		if(sSendToDm!=null && !sSendToDm.isEmpty()){
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 문서관리 사용
			if("Y".equals(sysPlocMap.get("dmEnable"))){
				
				// 승인 되고, 이전 문서가 승인 상태가 아니면(최초 기안 이거나)
				if("apvd".equals(docStatCd) && !"apvd".equals(prevDocStatCd)){
					// 문서관리 보내기 정보 저장 - AP 쪽 테이블에
					ApOngdExDVo apOngdExDVo = saveSendToDm(request, model, queryQueue, apOngdBVo, sSendToDm);
					// 문서관리 보내기 - DM 쪽 테이블에
					sendToDm(request, model, queryQueue, apOngdBVo, apOngdExDVo, "regRecLst");
				} else if(isMak || isTemp){
					// 문서관리 보내기 정보 저장 - AP 쪽 테이블에
					saveSendToDm(request, model, queryQueue, apOngdBVo, sSendToDm);
				}
				
			} else { // 문서관리 사용 안함
				
				// 승인 되었을 때
				if("apvd".equals(docStatCd) || isMak){
					// 문서관리 보내기 정보 삭제 - AP 쪽 테이블
					ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
					apOngdExDVo.setApvNo(apvNo);
					apOngdExDVo.setExId("sendToDm");
					queryQueue.delete(apOngdExDVo);
				}
				
			}
		}
		
		// 모바일 푸쉬
		List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
		if(!isTemp && !isHold){
			addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
		}
		
		// 완결 되었을 때 
		if("apvd".equals(docStatCd) && !docStatCd.equals(prevDocStatCd)){
			// 참조열람 - 열람 안한 것 - 열람 안함 처리
			@SuppressWarnings("unchecked")
			List<ApOngdRefVwDVo> ongoApOngdRefVwDVoList = (List<ApOngdRefVwDVo>)model.get("ongoApOngdRefVwDVoList");
			if(ongoApOngdRefVwDVoList != null){
				for(ApOngdRefVwDVo storedApOngdRefVwDVo : ongoApOngdRefVwDVoList){
					// befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함
					storedApOngdRefVwDVo.setRefVwStatCd("noRefVw");
					if(storedApOngdRefVwDVo.getQueryType()==null){
						queryQueue.update(storedApOngdRefVwDVo);
					}
				}
			}
		}
		
		// 연결 문서
		String xmlTypId = (storedApOngdBVo==null || isMak ? apOngdBVo : storedApOngdBVo).getXmlTypId();
		if(xmlTypId != null && xmlTypId.startsWith("linkedSol")){
			String xml = paramMap.get("formBodyXML");
			if(xml!=null && !xml.isEmpty()){
				try {
					XMLElement xmlElement = SAXHandler.parse(xml);
					String erpLinkedApvNo = xmlElement.getAttr("body/linked.erpLinkedApvNo");
					if(erpLinkedApvNo!=null && !erpLinkedApvNo.isEmpty()){
						
						// 저장된 - 관련문서 조회
						ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
						apOngdExDVo.setApvNo(apvNo);
						apOngdExDVo.setExId("erpLinkedApvNo");
						apOngdExDVo = (ApOngdExDVo)commonSvc.queryVo(apOngdExDVo);
						
						if(apOngdExDVo != null){
							// 저장된 - 관련문서번호
							String oldLinkedApvNo = apOngdExDVo.getExVa();
							
							// (저장된-관련 문서)와 (파라미터-관련문서)가 다르면
							if(oldLinkedApvNo!=null && !oldLinkedApvNo.equals(erpLinkedApvNo)){
								
								// (저장된 - 관련문서의 관련문서) 조회
								apOngdExDVo = new ApOngdExDVo();
								apOngdExDVo.setApvNo(oldLinkedApvNo);
								apOngdExDVo.setExId("erpLinkedApvNo");
								apOngdExDVo = (ApOngdExDVo)commonSvc.queryVo(apOngdExDVo);
								String counterLinkedApvNo = apOngdExDVo==null ? null : apOngdExDVo.getExVa();
								
								// (저장된 - 관련문서의 관련문서)가 현제 문서이면 - 삭제함
								if(counterLinkedApvNo!=null && counterLinkedApvNo.equals(apvNo)){
									
									apOngdExDVo = new ApOngdExDVo();
									apOngdExDVo.setApvNo(oldLinkedApvNo);
									apOngdExDVo.setExId("erpLinkedApvNo");
									queryQueue.delete(apOngdExDVo);
									
									LOGGER.warn("AP LINKED DOC CLEARED ["+xmlTypId+"] - user:"
											+userVo.getUserNm()+"("+userVo.getUserUid()+")"
											+" doc:"+oldLinkedApvNo+" link:"+counterLinkedApvNo);
								}
								
								// 현제 문서의 - 관련문서 변경
								apOngdExDVo = new ApOngdExDVo();
								apOngdExDVo.setApvNo(apvNo);
								apOngdExDVo.setExId("erpLinkedApvNo");
								apOngdExDVo.setExVa(erpLinkedApvNo);
								queryQueue.update(apOngdExDVo);
							}
							
						} else {
							// 현제 문서의 - 관련문서 등록
							apOngdExDVo = new ApOngdExDVo();
							apOngdExDVo.setApvNo(apvNo);
							apOngdExDVo.setExId("erpLinkedApvNo");
							apOngdExDVo.setExVa(erpLinkedApvNo);
							queryQueue.insert(apOngdExDVo);
						}
						
						// 파라미터 관련문서의 관련문서 조회
						apOngdExDVo = new ApOngdExDVo();
						apOngdExDVo.setApvNo(erpLinkedApvNo);
						apOngdExDVo.setExId("erpLinkedApvNo");
						apOngdExDVo = (ApOngdExDVo)commonSvc.queryVo(apOngdExDVo);
						
						if(apOngdExDVo != null){
							
							if(!apvNo.equals(apOngdExDVo.getExVa())){
								LOGGER.warn("AP LINKED DOC CHANGED ["+xmlTypId+"] - user:"
										+userVo.getUserNm()+"("+userVo.getUserUid()+")"
										+" doc:"+erpLinkedApvNo+" from:"+apOngdExDVo.getExVa()+" to:"+apvNo);
								
								// 현제 문서 - 관련문서의 관련문서를 현제문서로 등록
								apOngdExDVo = new ApOngdExDVo();
								apOngdExDVo.setApvNo(erpLinkedApvNo);
								apOngdExDVo.setExId("erpLinkedApvNo");
								apOngdExDVo.setExVa(apvNo);
								queryQueue.update(apOngdExDVo);
							}
							
						} else {
							
							// 현제 문서 - 관련문서의 관련문서를 현제문서로 등록
							apOngdExDVo = new ApOngdExDVo();
							apOngdExDVo.setApvNo(erpLinkedApvNo);
							apOngdExDVo.setExId("erpLinkedApvNo");
							apOngdExDVo.setExVa(apvNo);
							queryQueue.insert(apOngdExDVo);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		distHandler.distribute();
		uploadHandler.removeTempDir();
		commonSvc.execute(queryQueue);
		
		// [옵션] 자동발송
		if(currApvd && "Y".equals(optConfigMap.get("autoSend"))){
			ApOngdBVo currApOngdBVo = isMak || isTemp ? apOngdBVo : storedApOngdBVo;
			// 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			if("extro".equals(currApOngdBVo.getDocTypCd())){
				processAutoSendDoc(apvNo, userVo, locale, model);
			}
		}
		
		if(asyncNotiData!=null){
			apErpNotiSvc.processErpNoti(asyncNotiData);
		}
		
		if(!isTemp && !isHold){
			// 메신저 메세지 일괄 발송
			sendMesseger(messengerQueue, optConfigMap);
			// 모바일 푸쉬 메세지 - 보내기
			ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
		}
		
		// 결재 알림 처리 - [결재에서 시작 - ERP 통보]
		if(needErpNotiFromAp){
			apErpNotiSvc.sendErpNotiFromAp(userVo, apvNo, formBodyXML);
		}
		
		// 연차
//		String xmlTypId = (storedApOngdBVo==null || isMak ? apOngdBVo : storedApOngdBVo).getXmlTypId();
		if(ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, xmlTypId)){
			
			if(isMak || currApvd || isRejt || (isReRevw && model.get("reReviewToMak")!=null)){
				String wdStatCd = currApvd ? "apvd" : statCd;
				String apvSubject = (storedApOngdBVo==null || isMak ? apOngdBVo : storedApOngdBVo).getDocSubj();
				apWdSvc.sendWdNotiFromAp(apvNo, apvSubject, wdStatCd, userVo, formBodyXML);
			}
		}
		
		
		// 의견 메일 보내기
		if(model.get("needOpinMail") != null){
			
			List<ApOngdApvLnDVo> receiverList = ApDocTransUtil.getOpinMailApvrList(apvLines.getAllApvLn(), apvLnPno, apvLnNo);
			String subject = apOngdBVo.getDocSubj() != null ? apOngdBVo.getDocSubj()
					: model.get("storedApOngdBVo")!=null ? ((ApOngdBVo)model.get("storedApOngdBVo")).getDocSubj() : null;
			String apvOpinCont = paramMap.get("apvOpinCont");
			String senderUid = myTurnApOngdApvLnDVo==null ? null : myTurnApOngdApvLnDVo.getApvrUid();
			String apvrNm = myTurnApOngdApvLnDVo==null ? null : myTurnApOngdApvLnDVo.getApvrNm();
			
			sendOpinMail(receiverList, subject, apvOpinCont, senderUid, apvrNm, 
					docLangTypCd, isRejt, isCons, isReRevw, "apvd".equals(docStatCd), false, model);
		}
		
		String bxId = request.getParameter("bxId");
		String menuId = request.getParameter("menuId");
		
		if(isTemp){
			//ap.trans.submitOk={0} 하였습니다. - 임시저장
			String message = messageProperties.getMessage("ap.trans.submitOk", new String[]{"#ap.btn.temp"}, request);
			model.put("message", message);
			if(!ServerConfig.IS_MOBILE){
				if(menuId!=null && menuId.startsWith("Y")){//마이포탈
					String url = apBxSvc.getBxUrlByBxId(userVo, bxId, menuId);//현재함 유지
					model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
				} else {
					if(myTurnApOngdApvLnDVo != null && "reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd())){
						String url = apBxSvc.getBxUrlByBxId(userVo, "myBx", null);//기안함
						model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
					} else {
						String url = apBxSvc.getBxUrlByBxId(userVo, "drftBx", null);//개인함
						model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
					}
				}
			}
		} else if(isMak){
			//ap.trans.submitOk={0} 하였습니다. - 상신
			String message = messageProperties.getMessage("ap.trans.submitOk", new String[]{"#ap.btn.subm"}, request);
			model.put("message", message);
			if(!ServerConfig.IS_MOBILE){
				if(menuId!=null && menuId.startsWith("Y")){//마이포탈
					String url = apBxSvc.getBxUrlByBxId(userVo, bxId, menuId);//현재함 유지
					model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
				} else {
					String url = apBxSvc.getBxUrlByBxId(userVo, "myBx", null);//기안함
					model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
				}
			}
		} else {
			String actNm = "hold".equals(statCd) ? messageProperties.getMessage("ap.btn.hold", locale)
					: ptSysSvc.getTerm("ap.term."+statCd, locale.getLanguage());
			//ap.trans.processOk={0} 처리 하였습니다.
			String message = messageProperties.getMessage("ap.trans.processOk", new String[]{actNm}, request);
			model.put("message", message);
			
			if(!ServerConfig.IS_MOBILE){
				// 다음 문서로 이동
				model.put("todo", "parent.removeApCachedCountMap(); parent.moveToNextDoc(true);");
			}
			
//			if(!ServerConfig.IS_MOBILE){
//				String url = apBxSvc.getBxUrlByBxId(userVo, bxId, menuId);
//				char sp = url.indexOf('?') > 0 ? '&' : '?';
//				String queryString = paramMap.get("queryString");
//				model.put("todo", "parent.location.replace(\""+url+sp+queryString+"\");");
//			}
		}
	}
	
	/** 의견메일 보내기 */
	private void sendOpinMail(List<ApOngdApvLnDVo> receiverList,
			String subject, String apvOpinCont, String senderUid, String apvrNm, String docLangTypCd,
			boolean isRejt, boolean isCons, boolean isReRevw, boolean isApvd, boolean isSaved, ModelMap model) throws SQLException, CmException, IOException{
		
		String rejtPrefix=null, consPrefix=null, reRevwPrefix=null;
		
		// [반려]
		if(isRejt){
			rejtPrefix = "["+ptSysSvc.getTerm("ap.term.rejt", docLangTypCd)+"] ";
			if(apvOpinCont.startsWith(rejtPrefix)){
				apvOpinCont = apvOpinCont.substring(rejtPrefix.length());
			}
		// [합의반대]
		} else if(isCons){
			consPrefix = "["+ptSysSvc.getTerm("ap.term.cons", docLangTypCd)+"] ";
			apvOpinCont = apvOpinCont.substring(consPrefix.length());
		// [재검토]
		} else if(isReRevw){
			reRevwPrefix = "["+ptSysSvc.getTerm("ap.term.reRevw", docLangTypCd)+"] ";
			if(apvOpinCont.startsWith(reRevwPrefix)){
				apvOpinCont = apvOpinCont.substring(reRevwPrefix.length());
			}
		}
		
		if(receiverList!=null && !receiverList.isEmpty() && senderUid!=null && subject!=null){
			
			String receiverUid;
			String receiverLangTypCd;
			Locale receiverLocale;
			String apvrNmByLang;
			OrUserBVo orUserBVo;
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String webDomain = svrEnvMap.get("webDomain");
			
			// 시스템 정책 조회
//			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
//			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			
			PtPushMsgDVo ptPushMsgDVo;
			String pushMsgId, bxId;
//			String msgUrl = (useSSL ? "https://" : "http://")+webDomain+"/index.do?msgId=";
			String msgUrl = "http://"+webDomain+"/index.do?msgId=";
			
			String subjPrefix;
			String mailSubject;
			String mailContent;
			
			for(ApOngdApvLnDVo opinApOngdApvLnDVo : receiverList){
				
				receiverUid = opinApOngdApvLnDVo.getApvrUid();
				receiverLangTypCd = ptPsnSvc.getLastLginLangTypCd(receiverUid, false);
				if(receiverLangTypCd==null) receiverLangTypCd = ptPsnSvc.getLastLginLangTypCd(senderUid, true);
				receiverLocale = SessionUtil.toLocale(receiverLangTypCd);
				
				// ap.jsp.doc.apvOpin=결재의견
				subjPrefix = "["+messageProperties.getMessage("ap.jsp.doc.apvOpin", receiverLocale)+"] ";
				mailSubject = subjPrefix+subject;
				
				pushMsgId = StringUtil.getNextHexa(24);
				
				ptPushMsgDVo = new PtPushMsgDVo();
				ptPushMsgDVo.setPushMsgId(pushMsgId);
				ptPushMsgDVo.setLangTypCd(receiverLangTypCd);
				ptPushMsgDVo.setMdRid("AP");
				ptPushMsgDVo.setMdId(opinApOngdApvLnDVo.getApvNo());
				ptPushMsgDVo.setPushSubj(mailSubject);
				
				if("0".equals(opinApOngdApvLnDVo.getApvLnPno()) && "1".equals(opinApOngdApvLnDVo.getApvLnNo())){
					bxId = "myBx";
				} else if(isApvd){
					bxId = "apvdBx";
				} else {
					bxId = "ongoBx";
				}
				
				ptPushMsgDVo.setWebUrl("/ap/box/setDoc.do?bxId="+bxId+"&apvNo="+opinApOngdApvLnDVo.getApvNo()+"&apvLnPno="+opinApOngdApvLnDVo.getApvLnPno()+"&apvLnNo="+opinApOngdApvLnDVo.getApvLnNo());
				ptPushMsgDVo.setWebAuthUrl("/ap/box/listApvBx.do?bxId="+bxId);
				ptPushMsgDVo.setMobUrl("/ap/box/viewDoc.do?bxId="+bxId+"&apvNo="+opinApOngdApvLnDVo.getApvNo()+"&apvLnPno="+opinApOngdApvLnDVo.getApvLnPno()+"&apvLnNo="+opinApOngdApvLnDVo.getApvLnNo());
				ptPushMsgDVo.setMobAuthUrl("/ap/box/listApvBx.do?bxId="+bxId);
				
				ptPushMsgDVo.setUserUid(receiverUid);
				
				ptPushMsgDVo.setIsuDt("sysdate");
				ptPushMsgDVo.setValdLginCnt("3");
				
				commonSvc.insert(ptPushMsgDVo);
				
				// 결재자 명 - 어권 전환
				apvrNmByLang = apvrNm;
				if(!docLangTypCd.equals(receiverLangTypCd)){
					orUserBVo = new OrUserBVo();
					orUserBVo.setUserUid(senderUid);
					orUserBVo.setQueryLang(receiverLangTypCd);
					orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
					if(orUserBVo!=null && orUserBVo.getRescNm()!=null){
						apvrNmByLang = orUserBVo.getRescNm();
					}
				}
				
				if(!receiverLangTypCd.equals(docLangTypCd)){
					// [반려]
					if(isRejt){
						rejtPrefix = "["+ptSysSvc.getTerm("ap.term.rejt", receiverLangTypCd)+"] ";
						apvOpinCont = "<strong style=\"color:#A5260C\">"+rejtPrefix+"</strong>"+apvOpinCont;
					// [합의반대]
					} else if(isCons){
						consPrefix = "["+ptSysSvc.getTerm("ap.term.cons", docLangTypCd)+"] ";
						apvOpinCont = "<strong style=\"color:#A5260C\">"+consPrefix+"</strong>"+apvOpinCont;
					// [재검토]
					} else if(isReRevw){
						consPrefix = "["+ptSysSvc.getTerm("ap.term.reRevw", docLangTypCd)+"] ";
						apvOpinCont = "<strong style=\"color:#A5260C\">"+consPrefix+"</strong>"+apvOpinCont;
					}
				}
				
				mailContent = getOpinMailHTML(subject, apvrNmByLang, apvOpinCont, msgUrl+pushMsgId, isSaved, receiverLocale);
				emailSvc.sendMailSvc(senderUid, new String[]{receiverUid}, mailSubject, mailContent, null, false, false, receiverLangTypCd);
			}
		}
	}
	
	/** 참조열람 - 열람자 저장 */
	public void transRefVw(List<String> refVwList, String apvNo,
			QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			UserVo userVo, ModelMap model, Locale locale) throws CmException, SQLException{
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
			
		// 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
		} else if(
				! ("mak".equals(apOngdBVo.getDocProsStatCd())
				|| "ongo".equals(apOngdBVo.getDocProsStatCd())
				|| "pubVw".equals(apOngdBVo.getDocProsStatCd())	)	){
			
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.term.refVw=참조열람
			throw new CmException("ap.msg.notStat", new String[]{"#ap.term.refVw"}, locale);
		}
		
		processRefVw(refVwList, "Y", apvNo, apOngdBVo,
				queryQueue, messengerQueue, userVo, model, apOngdBVo.getDocLangTypCd(), null, locale, "revw");
	}

	/** 참조열람 - 열람확인 */
	public void processRefVw(String apvNo, String refVwOpinCont,
			QueryQueue queryQueue, UserVo userVo, ModelMap model, Locale locale) throws SQLException, CmException {
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
			
		// 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
		} else if( "temp".equals(apOngdBVo.getDocProsStatCd())
				|| "apvd".equals(apOngdBVo.getDocProsStatCd())
				|| "rejt".equals(apOngdBVo.getDocProsStatCd())	){
			
			LOGGER.error("processRefVw fail - apvNo:"+apvNo+"  docProsStatCd:"+apOngdBVo.getDocProsStatCd());
			
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.term.cfrmRefVw=열람확인
			throw new CmException("ap.msg.notStat", new String[]{"#ap.term.cfrmRefVw"}, locale);
		}
		
		String refVwrUid = userVo.getUserUid();
		
		ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
		apOngdRefVwDVo.setApvNo(apvNo);
		apOngdRefVwDVo.setRefVwrUid(refVwrUid);
		apOngdRefVwDVo = (ApOngdRefVwDVo)commonDao.queryVo(apOngdRefVwDVo);
		
		if(apOngdRefVwDVo==null){
			// cm.msg.noAuth=권한이 없습니다.
			throw new CmException("cm.msg.noAuth", locale);
		} else if(!"inRefVw".equals(apOngdRefVwDVo.getRefVwStatCd())){
			LOGGER.error("processRefVw fail - apvNo:"+apvNo+"  refVwrUid:"+refVwrUid+"  refVwStatCd:"+apOngdRefVwDVo.getRefVwStatCd());
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.term.cfrmRefVw=열람확인
			throw new CmException("ap.msg.notStat", new String[]{"#ap.term.cfrmRefVw"}, locale);
		}
		
		apOngdRefVwDVo = new ApOngdRefVwDVo();
		apOngdRefVwDVo.setApvNo(apvNo);
		apOngdRefVwDVo.setRefVwrUid(refVwrUid);
		apOngdRefVwDVo.setRefVwStatCd("cmplRefVw");
		apOngdRefVwDVo.setRefVwDt("sysdate");
		apOngdRefVwDVo.setRefVwOpinCont(refVwOpinCont);
		queryQueue.update(apOngdRefVwDVo);
	}
	
	/** 참조열람 */
	private void processRefVw(List<String> refVwList, String refVwModified, String apvNo, ApOngdBVo apOngdBVo,
			QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			UserVo userVo, ModelMap model, String docLangTypCd,
			Map<Integer, OrOrgBVo> orgCacheMap, Locale locale, String statCd) throws SQLException, CmException {
		
		boolean isModified = refVwModified!=null && refVwModified.equals("Y");
		boolean isEmptyParam = refVwList==null || refVwList.isEmpty();
		boolean isTemp = "temp".equals(statCd);
		boolean isMak = "mak".equals(statCd);
		
		// 완료 안된 참조열람 목록
		List<ApOngdRefVwDVo> ongoApOngdRefVwDVoList = new ArrayList<ApOngdRefVwDVo>();
		
		// 이전 데이터 조회
		ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
		apOngdRefVwDVo.setApvNo(apvNo);
		apOngdRefVwDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApOngdRefVwDVo> storedApOngdRefVwDVoList = (isTemp || isMak) ? null : (List<ApOngdRefVwDVo>)commonDao.queryList(apOngdRefVwDVo);
		
		if(isTemp || isMak){
			apOngdRefVwDVo = new ApOngdRefVwDVo();
			apOngdRefVwDVo.setApvNo(apvNo);
			queryQueue.delete(apOngdRefVwDVo);
		}
		
		if(isModified){
			
			Integer sortOrdr = 0;
			JSONObject jsonObject;
			String refVwrUid, refVwrNm, odurUid = userVo.getOdurUid();
			String refVwFixdApvrYn;
			
			// 이력 삭제
			apOngdRefVwDVo = new ApOngdRefVwDVo();
			apOngdRefVwDVo.setApvNo(apvNo);
			apOngdRefVwDVo.setRegrUid(odurUid);
			apOngdRefVwDVo.setHistory();
			queryQueue.delete(apOngdRefVwDVo);
			
			// 이전 데이터 맵
			Map<Integer, ApOngdRefVwDVo> storedMap = null;
			
			// 이전 데이터 맵으로 변환
			if(storedApOngdRefVwDVoList != null){
				storedMap = new LinkedHashMap<Integer, ApOngdRefVwDVo>();
				for(ApOngdRefVwDVo storedApOngdRefVwDVo : storedApOngdRefVwDVoList){
					storedMap.put(Hash.hashId(storedApOngdRefVwDVo.getRefVwrUid()), storedApOngdRefVwDVo);
				}
			}
			
			ApOngdRefVwDVo storedApOngdRefVwDVo, hisApOngdRefVwDVo;
			
			if(!isEmptyParam) {
				
				// 서버 설정 목록 조회
				Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
				String webDomain = svrEnvMap.get("webDomain");
				
				// 옵션 조회(캐쉬)
				Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
				boolean sendMailNoti = "Y".equals(optConfigMap.get("sendMailNoti"));
				
				for(String json : refVwList){
					jsonObject = (JSONObject)JSONValue.parse(json);
					
					refVwrUid = (String)jsonObject.get("refVwrUid");
					refVwrNm  = (String)jsonObject.get("refVwrNm");
					refVwFixdApvrYn = (String)jsonObject.get("refVwFixdApvrYn");
					
					apOngdRefVwDVo = new ApOngdRefVwDVo();
					
					apOngdRefVwDVo.setApvNo(apvNo);
					apOngdRefVwDVo.setRefVwrUid(refVwrUid);
					apOngdRefVwDVo.setRefVwFixdApvrYn(refVwFixdApvrYn);
					
					sortOrdr++;
					apOngdRefVwDVo.setSortOrdr(sortOrdr.toString());
					
					// 이전 데이터 가져옴
					storedApOngdRefVwDVo = storedMap==null ? null : storedMap.remove(Hash.hashId(refVwrUid));
					
					// 완료 된것 - 정렬 순서만 바꿈 (cmplRefVw:참조열람완료)
					if(storedApOngdRefVwDVo!=null && "cmplRefVw".equals(storedApOngdRefVwDVo.getRefVwStatCd())){
						// 본 테이블
						queryQueue.update(apOngdRefVwDVo);
						
						// 이력 테이블
						storedApOngdRefVwDVo.setRegrUid(odurUid);
						storedApOngdRefVwDVo.setRegDt("sysdate");
						storedApOngdRefVwDVo.setSortOrdr(sortOrdr.toString());
						storedApOngdRefVwDVo.setHistory();
						queryQueue.insert(storedApOngdRefVwDVo);
						
					// 완료 안된것 - 사용자 정보 재 설정
					} else {
						
						// 원직자로
						apOngdRefVwDVo.setRegrUid(userVo.getOdurUid());
						
						// 사용자 정보 조회
						OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(refVwrUid, docLangTypCd, null);
						if(orUserBVo == null){
							LOGGER.error("Fail trans - user not found ! - refVwrUid:"+refVwrUid);
							//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
							String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.user"}, locale)
									+ refVwrUid+ " : " + refVwrNm;
							throw new CmException(message);
						}
						String refVwrOdurUid = orUserBVo.getOdurUid();
						
						apOngdRefVwDVo.setRefVwrNm(orUserBVo.getRescNm());
						if(orUserBVo.getPositCd()!=null && !orUserBVo.getPositCd().isEmpty()){
							apOngdRefVwDVo.setRefVwrPositCd(orUserBVo.getPositCd());
							apOngdRefVwDVo.setRefVwrPositNm(orUserBVo.getPositNm());
						}
						if(orUserBVo.getTitleCd()!=null && !orUserBVo.getTitleCd().isEmpty()){
							apOngdRefVwDVo.setRefVwrTitleCd(orUserBVo.getTitleCd());
							apOngdRefVwDVo.setRefVwrTitleNm(orUserBVo.getTitleNm());
						}
						apOngdRefVwDVo.setRefVwrDeptId(orUserBVo.getDeptId());
						apOngdRefVwDVo.setRefVwrDeptNm(orUserBVo.getDeptRescNm());
						
						// 신규의 경우
						if(storedApOngdRefVwDVo==null){
							
							// befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함
							apOngdRefVwDVo.setRefVwStatCd(isTemp ? "befoRefVw" : "inRefVw");
							
							// 입력일 세팅
							apOngdRefVwDVo.setRegDt("sysdate");
							apOngdRefVwDVo.setRegrUid(odurUid);
							
							queryQueue.insert(apOngdRefVwDVo);
							
							// 이력
							hisApOngdRefVwDVo = new ApOngdRefVwDVo();
							VoUtil.fromMap(hisApOngdRefVwDVo, VoUtil.toMap(apOngdRefVwDVo, null));
							hisApOngdRefVwDVo.setHistory();
							queryQueue.insert(hisApOngdRefVwDVo);
							
							if(!isTemp){
								// 메세지 처리
								addMessegerQueue(apOngdBVo, 
										null, null, null,
										refVwrUid, refVwrOdurUid, "inRefVw", webDomain, null,
										messengerQueue, sendMailNoti, userVo, locale);
							}
							
						// 기존의 데이타
						} else {
							
							// 기존 데이타 변경
							apOngdRefVwDVo.setRefVwStatCd(isTemp ? "befoRefVw" : "inRefVw");// befoRefVw:참조열람전, inRefVw:참조열람중
							queryQueue.update(apOngdRefVwDVo);
							
							// 이력 테이블
							storedApOngdRefVwDVo.setRegrUid(odurUid);
							storedApOngdRefVwDVo.setRegDt("sysdate");//변경일
							storedApOngdRefVwDVo.setSortOrdr(sortOrdr.toString());
							storedApOngdRefVwDVo.setHistory();
							queryQueue.insert(storedApOngdRefVwDVo);
							
						}
						
						// 완료 안된 참조열람 목록 - 에 저장함 : 추후 완결 되었을때 처리 목적
						ongoApOngdRefVwDVoList.add(apOngdRefVwDVo);
					}
				}
			}
			
			// 저장된 것중 - 넘겨온 곳에 없는 데이터 처리
			if(storedMap != null && !storedMap.isEmpty()){
		    	Iterator<Entry<Integer, ApOngdRefVwDVo>> iterator = storedMap.entrySet().iterator();
		    	while(iterator.hasNext()){
		    		storedApOngdRefVwDVo = iterator.next().getValue();
		    		
		    		// 완료 된것 - 정렬 순서만 바꿈 (cmplRefVw:참조열람완료)
					if("cmplRefVw".equals(storedApOngdRefVwDVo.getRefVwStatCd())){
						sortOrdr++;
						
						apOngdRefVwDVo = new ApOngdRefVwDVo();
						apOngdRefVwDVo.setApvNo(apvNo);
						apOngdRefVwDVo.setRefVwrUid(storedApOngdRefVwDVo.getRefVwrUid());
						apOngdRefVwDVo.setSortOrdr(sortOrdr.toString());
						queryQueue.update(apOngdRefVwDVo);
						
						// 이력 테이블
						storedApOngdRefVwDVo.setRegrUid(odurUid);
						storedApOngdRefVwDVo.setSortOrdr(sortOrdr.toString());
						storedApOngdRefVwDVo.setHistory();
						queryQueue.insert(storedApOngdRefVwDVo);
						
					// 완료 안된것 - 삭제
					} else {
						apOngdRefVwDVo = new ApOngdRefVwDVo();
						apOngdRefVwDVo.setApvNo(apvNo);
						apOngdRefVwDVo.setRefVwrUid(storedApOngdRefVwDVo.getRefVwrUid());
						queryQueue.delete(apOngdRefVwDVo);
					}
		    	}
			}
			
		} else { // if(isModified){
			
			// 완료 안된 목록 - 단계가 완료되면 - noRefVw:참조열람안함 으로 변경하기 위해서
			if(storedApOngdRefVwDVoList != null){
				for(ApOngdRefVwDVo storedApOngdRefVwDVo : storedApOngdRefVwDVoList){
					if(!"cmplRefVw".equals(storedApOngdRefVwDVo.getRefVwStatCd())){
						apOngdRefVwDVo = new ApOngdRefVwDVo();
						apOngdRefVwDVo.setApvNo(storedApOngdRefVwDVo.getApvNo());
						apOngdRefVwDVo.setRefVwrUid(storedApOngdRefVwDVo.getRefVwrUid());
						ongoApOngdRefVwDVoList.add(apOngdRefVwDVo);
					}
				}
			}
		}
		
		if(!ongoApOngdRefVwDVoList.isEmpty()){
			model.put("ongoApOngdRefVwDVoList", ongoApOngdRefVwDVoList);
		}
	}

	/** 의견 메일 내용 */
	private String getOpinMailHTML(String subject, String apvrNm, String opion, String msgUrl, boolean isSaved, Locale receiverLocale){
		
		StringBuilder builder = new StringBuilder(1024);
		
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
		builder.append("<table style=\"border:0px\" border=\"0\">\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("ap.doc.docSubj", receiverLocale)).append("</strong></td>\n");//제목
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if(msgUrl==null || msgUrl.isEmpty()){
			builder.append(subject);
		} else {
			builder.append("<a href=\"").append(msgUrl).append("\" target=\"_top\">").append(subject).append("</a>");
		}
		builder.append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("ap.cfg.apvr", receiverLocale)).append("</strong></td>\n");//결재자
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(apvrNm).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage(isSaved ? "ap.col.regDt" : "ap.list.apvDt", receiverLocale)).append("</strong></td>\n");//작성일시/결재일시
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(StringUtil.getCurrDateTime()).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("ap.doc.opin", receiverLocale)).append("</strong></td>\n");//의견
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(breakLine(opion)).append("</td></tr>\n");
		
		builder.append("</table>\n");
		builder.append("</p>\n<br/><br/>\n");
		
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px; color:#2B4AB9\">\n");
		//ap.msg.thisIsAutoMail=위 내용은 자동으로 발송되는 메일 입니다.
		builder.append(messageProperties.getMessage("ap.msg.thisIsAutoMail", receiverLocale));
		builder.append("</p>\n");
		
		return builder.toString();
	}
	
	/** HTML용 라인 개행 */
	private String breakLine(String text){
		String temp = text;
		temp = temp.replace("\r\n", "<br/>");
		temp = temp.replace("\r", "<br/>");
		temp = temp.replace("\n", "<br/>");
		return temp;
	}
	
	/** 문서관리 보내기 정보 저장 */
	@SuppressWarnings("unchecked")
	private ApOngdExDVo saveSendToDm(HttpServletRequest request, ModelMap model, QueryQueue queryQueue, 
			ApOngdBVo apOngdBVo, String sendToDm) throws SQLException, CmException{
		
		ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
		apOngdExDVo.setApvNo(apOngdBVo.getApvNo());
		apOngdExDVo.setExId("sendToDm");
		apOngdExDVo.setExVa(sendToDm);
		queryQueue.store(apOngdExDVo);
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(apOngdExDVo.getExVa());
		
		// 세션과 결재문서와 어권이 다르면 - 결재문서 어권으로 폴더명 지정
		String langTypCd = LoginSession.getLangTypCd(request);
		if(!langTypCd.equals(apOngdBVo.getDocLangTypCd())){
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			DmFldBVo dmFldBVo = dmDocSvc.getDmFldBVo(dmStorBVo.getStorId(), 
					(String)jsonObject.get("fldId"), apOngdBVo.getDocLangTypCd());
			if(dmFldBVo != null){
				String value = dmFldBVo.getFldNm();
				if(value!=null && !value.isEmpty()){
					jsonObject.put("fldNm", value);
					apOngdExDVo.setExVa(jsonObject.toJSONString());
				}
			}
		}
		
		return apOngdExDVo;
	}
	
	/** 문서관리로 보내기  */
	@SuppressWarnings("unchecked")
	private boolean sendToDm(HttpServletRequest request, ModelMap model, QueryQueue queryQueue, 
			ApOngdBVo apOngdBVo, ApOngdExDVo apOngdExDVo, String bxId) throws SQLException, IOException, CmException{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(apOngdExDVo.getExVa());
		Map<String,String> dmMap = new HashMap<String,String>();
		
		Object obj;
		String key, value;
		@SuppressWarnings("rawtypes")
		Iterator iterator = jsonObject.keySet().iterator();
		while(iterator.hasNext()){
			obj = iterator.next();
			if(obj==null) continue;
			key = obj.toString();
			obj = jsonObject.get(key);
			if(obj==null) continue;
			value = obj.toString();
			dmMap.put(key, value);
		}
		dmMap.put("refUrl", "/dm/doc/viewApDocPop.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo());
		
		//url[링크URL(결재)],subj[제목],fromTyp[구분:게시판(bb),결재(ap)]
		dmMap.put("fromTyp", "ap");
		dmMap.put("subj", apOngdBVo.getDocSubj());
		dmMap.put("regrUid", apOngdBVo.getMakrUid());
		dmMap.put("refId", apOngdBVo.getApvNo());
		dmMap.put("langTypCd", apOngdBVo.getDocLangTypCd());
		
		dmMap = dmDocSvc.sendDoc(request, queryQueue, dmMap, null);
		boolean changed = false;
		if(dmMap != null){
			value = dmMap.get("fldId");
			if(value != null){
				jsonObject.put("fldId", value);
				changed = true;
			}
			value = dmMap.get("fldNm");
			if(value != null){
				jsonObject.put("fldNm", value);
				changed = true;
			}
		}
		
		if(changed){
			apOngdExDVo.setExVa(jsonObject.toJSONString());
		}
		return changed;
	}
	
	/** 담당자 기안(접수 문서의 상신), 공람, 선람, 동시공람 처리 */
	private void processVw(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, 
			Map<String, String> paramMap, Map<String, List<String>> paramListMap, Map<String, String> optConfigMap,
			boolean isBulkApv, String statCd, 
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException {
		
		String apvNo = paramMap.get("apvNo");// 결재번호
		String apvLnPno = "0";
		String apvLnNo = paramMap.get("apvLnNo");// 결재번호
		
		// isBulkApv : 일괄결재의 경우
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, statCd, userVo, locale);
		if(!"pubVw".equals(storedApOngdBVo.getDocProsStatCd())){//pubVw:공람
			LOGGER.error("Fail "+statCd+" - not stat(pubVw) - apvNo:"+apvNo+"  docStatCd:"+storedApOngdBVo.getDocStatCd());
			String statNm = ptSysSvc.getTerm("ap.term."+statCd, locale.getLanguage());
			// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
			throw new CmException("ap.msg.not.stat", new String[]{statNm}, locale);
		}
		
		// [히든옵션] 유통문서 어권 기준 - session:세션기준, orgnDoc:원본문서의 어권
		String docLangTypCd = "session".equals(optConfigMap.get("distDocLangTypCd")) ? 
				locale.getLanguage() : storedApOngdBVo.getDocLangTypCd();
		
		List<String> apvLnList = paramListMap==null ? null : paramListMap.get("apvLn");
		// JSON 파라미터 들을 파싱해서 에 담은 List 
		List<ApOngdApvLnDVo> paramApOngdApvLnDVoList = parseApvLnJson(apvLnList, apvNo, apvLnPno, docLangTypCd, locale);
		// 저장할 
		List<ApOngdApvLnDVo> toStoreApOngdApvLnDVoList;

		// 결재선 정보 관리용 유틸 - 전체 경로선을 가지고 루트경로, 현재경로, 부서별 경로 등을 리턴함
		ApvLines apvLines = new ApvLines(queryApvLn(apvNo), apvLnPno);
		
		// 해당 사용자의 결재라인
		ApOngdApvLnDVo myTurnApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, locale);
		if(!"inVw".equals(myTurnApOngdApvLnDVo.getApvStatCd())){
			LOGGER.error("Fail "+statCd+" - not stat(inVw) - apvNo:"+apvNo+"  apvStatCd:"+myTurnApOngdApvLnDVo.getApvStatCd());
			String statNm = ptSysSvc.getTerm("ap.term."+statCd, locale.getLanguage());
			// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
			throw new CmException("ap.msg.not.stat", new String[]{statNm}, locale);
		}
		
		// 저장되어 있던 결재라인(상신 할때 DB에 저장되어 있던) 결재라인
		List<ApOngdApvLnDVo> storedApOngdApvLnDVoList = apvLines.getCurrLn();

		// 결재라인이력번호
		String apvLnHstNo;
		// 현 결재자의 - 이전결재라인이력번호
		String myPrevApvLnHstNo = null;
		// insert 모드 여부
		boolean needInsert = false;
		
		// 전송된 결재선이 없으면
		if(paramApOngdApvLnDVoList==null){
			
			// 저장 할 결재 라인 - 저장되어 있던 결재라인(보류 되기전, 나에게 넘겨 온) 결재라인
			toStoreApOngdApvLnDVoList = storedApOngdApvLnDVoList;
			// 저장 할 결재라인이력번호 
			apvLnHstNo = myTurnApOngdApvLnDVo.getApvLnHstNo();
			needInsert = false;
		// 변경사항이 있으면
		} else {
			
			// 전송된 것과 저장되어 있던 결재라인(원본) 이 같으면
			if(isIdenticalApvLn(paramApOngdApvLnDVoList, storedApOngdApvLnDVoList)){
				
				// 저장 할 결재 라인 - 저장되어 있던 결재라인(보류 되기전, 나에게 넘겨 온) 결재라인
				toStoreApOngdApvLnDVoList = storedApOngdApvLnDVoList;
				// 저장 할 결재라인이력번호 
				apvLnHstNo = myTurnApOngdApvLnDVo.getApvLnHstNo();
				needInsert = false;
			// 전송된 것과 저장되어 있던 결재라인(원본) 이 다르면
			} else {
				
				// 저장 할 결재라인이력번호  - 생성
				apvLnHstNo = getNextApvLnHstNo(apvNo, apvLnPno);
				
				// 이력 테이블 생성
				storeApvLnHistory(storedApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnHstNo(), queryQueue);
				
				// 저장 할 결재 라인 - 전송된 결재라인
				toStoreApOngdApvLnDVoList = paramApOngdApvLnDVoList;
				needInsert = true;
				// 이전결재라인이력번호 - 세팅
				myPrevApvLnHstNo = myTurnApOngdApvLnDVo.getApvLnHstNo();
			}
			
		}
		
		// 다음 공람자 목록
		List<ApOngdApvLnDVo> nextVwLst = null;
		// 동시공람이 완료 안됨
		boolean notDoneSyncVw = false;
		// 내가 동시공람 일때
		if("paralPubVw".equals(myTurnApOngdApvLnDVo.getApvrRoleCd())){
			int myIndex = ApDocTransUtil.findMyIndex(toStoreApOngdApvLnDVoList, apvLnNo);
			// 동시공람자 중 공람안한 자가 있는지 체크
			if(ApDocTransUtil.findNotDonSyncVw(toStoreApOngdApvLnDVoList, myIndex) != null){
				notDoneSyncVw = true;
			} else {
				// 다음 공람자 목록
				nextVwLst = ApDocTransUtil.getNextVwList(toStoreApOngdApvLnDVoList, apvLnNo);
			}
		} else {
			// 다음 공람자 목록
			nextVwLst = ApDocTransUtil.getNextVwList(toStoreApOngdApvLnDVoList, apvLnNo);
		}
		
		// 결재의견내용
		String apvOpinCont = paramMap.get("apvOpinCont");
		
		// update 모드 - 결재선 변경이 없었음
		if(!needInsert){
			
			// 내상태 - 공람완료
			ApOngdApvLnDVo updateVo = new ApOngdApvLnDVo();
			updateVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			updateVo.setApvLnPno(myTurnApOngdApvLnDVo.getApvLnPno());
			updateVo.setApvLnNo(myTurnApOngdApvLnDVo.getApvLnNo());
			updateVo.setApvDt("sysdate");//결재일시
			//updateVo.setVwDt("sysdate");
			updateVo.setApvStatCd("cmplVw");//cmplVw:공람완료
			updateVo.setApvOpinCont(apvOpinCont==null ? "" : apvOpinCont);//결재의견내용
			if(myPrevApvLnHstNo!=null) updateVo.setPrevApvLnHstNo(myPrevApvLnHstNo);
			queryQueue.update(updateVo);

			// 다음차례 - 공람중
			if(!notDoneSyncVw && nextVwLst!=null && !nextVwLst.isEmpty()){
				// 다음 결재자들 - 공람중으로
				for(ApOngdApvLnDVo nextVo : nextVwLst){
					updateVo = new ApOngdApvLnDVo();
					updateVo.setApvNo(nextVo.getApvNo());
					updateVo.setApvLnPno(nextVo.getApvLnPno());
					updateVo.setApvLnNo(nextVo.getApvLnNo());
					updateVo.setPrevApvrApvDt("sysdate");
					updateVo.setVwDt("");
					updateVo.setApvStatCd("inVw");//inVw:공람중
					queryQueue.update(updateVo);
					
					// 메신저 보내기
					nextVo.setApvStatCd("inVw");//inVw:공람중
					addMesseger(storedApOngdBVo, nextVo, userVo, messengerQueue, locale);
				}
			}
			
		// insert 모드
		} else {
			
			// 경로선 삭제
			ApOngdApvLnDVo updateVo = new ApOngdApvLnDVo();
			updateVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			updateVo.setApvLnPno(myTurnApOngdApvLnDVo.getApvLnPno());
			queryQueue.delete(updateVo);
			
			// 리소스 캐쉬용 - 문서언어구분코드 에 따른 어권별 리소스 설정용
			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
			Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
			
			int i, size, storedSize = storedApOngdApvLnDVoList.size();
			
			// nextApvLnNoList 에 다음결재자의 결재라인번호 만 담아둠
			List<Integer> nextApvLnNoList = null;
			if(nextVwLst!=null && !nextVwLst.isEmpty()){
				nextApvLnNoList = new ArrayList<Integer>();
				for(ApOngdApvLnDVo nextVw : nextVwLst){
					nextApvLnNoList.add(Integer.valueOf(nextVw.getApvLnNo()));
				}
			}
			
			ApOngdApvLnDVo storedVo, paramVo;
			
			size = paramApOngdApvLnDVoList.size();
			for(i=0;i<size;i++){
				
				storedVo = (i>=storedSize) ? null : storedApOngdApvLnDVoList.get(i);
				// 결재상태코드 - befoVw:공람전, inVw:공람중, cmplVw:공람완료
				// 공람완료 or (공람중 상태고, 내 차례가 아니면)
				if(storedVo!=null && ( "cmplVw".equals(storedVo.getApvStatCd()) || (
						"inVw".equals(storedVo.getApvStatCd()) && !apvLnNo.equals(storedVo.getApvLnNo()) ))){
					// 이력번호만 올려서 그대로 insert 함
					storedVo.setApvLnHstNo(apvLnHstNo);
					queryQueue.insert(storedVo);
					
				// 내차례 이면
				} else if(storedVo!=null && apvLnNo.equals(storedVo.getApvLnNo())){
					storedVo.setApvLnHstNo(apvLnHstNo);
					storedVo.setApvDt("sysdate");
					storedVo.setVwDt("sysdate");
					storedVo.setApvStatCd("cmplVw");//cmplVw:공람완료
					storedVo.setApvOpinCont(apvOpinCont);//결재의견내용
					storedVo.setPrevApvLnHstNo(myPrevApvLnHstNo);
					queryQueue.insert(storedVo);
					
				} else {
					
					paramVo = paramApOngdApvLnDVoList.get(i);
					
					paramVo.setApvLnHstNo(apvLnHstNo);
					paramVo.setApvrDeptYn("N");//결재자부서여부
					
					// 결재선 데이터에 문서언어에 따른 리스스 바인딩
					setApOngdApvLnDVoResc(paramVo, docLangTypCd, 
							userCacheMap, orgCacheMap, optConfigMap, locale);
					
					// 다음 결재자면
					if(nextApvLnNoList!=null && nextApvLnNoList.contains(Integer.valueOf(paramVo.getApvLnNo()))){
						paramVo.setPrevApvrApvDt("sysdate");
						paramVo.setVwDt("");
						paramVo.setApvStatCd("inVw");//inVw:공람중
						// 메신저 보내기
						addMesseger(storedApOngdBVo, paramVo, userVo, messengerQueue, locale);
					} else {
						paramVo.setApvStatCd("befoVw");//befoVw:공람전
					}
					
					queryQueue.insert(paramVo);
				}
			}
		}
		
		// 다음 공람자 없음
		if(nextVwLst == null || nextVwLst.isEmpty()){
			// 문서상태 - 공람완료
			ApOngdBVo docApOngdBVo = new ApOngdBVo();
			docApOngdBVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			docApOngdBVo.setDocStatCd("cmplVw");//문서상태코드 - cmplVw:공람완료
			docApOngdBVo.setCurApvrId("");//현재결재자ID
			docApOngdBVo.setCurApvrNm("");//현재결재자명
			docApOngdBVo.setCurApvrDeptYn("");//현재결재자부서여부
			queryQueue.update(docApOngdBVo);
		// 다음 공람자 있음 - 내상태 공람완료, 다음차례 - 공람중
		} else {
			// 문서상태 - 다음 단계의 공람자 상태로
			ApOngdBVo docApOngdBVo = new ApOngdBVo();
			docApOngdBVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			ApOngdApvLnDVo nextFirstVw = nextVwLst.get(0);
			docApOngdBVo.setCurApvrId(nextFirstVw.getApvrUid());//현재결재자ID
			docApOngdBVo.setCurApvrNm(nextFirstVw.getApvrNm());//현재결재자명
			docApOngdBVo.setCurApvrDeptYn("N");//현재결재자부서여부
			docApOngdBVo.setDocStatCd(nextFirstVw.getApvrRoleCd());//문서상태코드
			queryQueue.update(docApOngdBVo);
		}
		
		
		// 일괄결재에서 들어올 경우가 아니면
		if(!isBulkApv){
			
			// 모바일 푸쉬
			List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
			addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
			
			commonSvc.execute(queryQueue);
			
			// 메신저 메세지 일괄 발송
			sendMesseger(messengerQueue, optConfigMap);
			
			// 모바일 푸쉬 메세지 - 보내기
			ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
			
			//ap.trans.processOk={0} 처리 하였습니다. - 선람/공람
			String actNm = ptSysSvc.getTerm("ap.term."+statCd, locale.getLanguage());
			String message = messageProperties.getMessage("ap.trans.processOk", new String[]{actNm}, locale);
			model.put("message", message);
			
			String menuId = paramMap.get("menuId");
			String paramMenuId = menuId!=null && menuId.startsWith("Y") ? menuId : null;
			
			String bxId = paramMap.get("bxId");
			String url = apBxSvc.getBxUrlByBxId(userVo, bxId, paramMenuId);
			String queryString = paramMap.get("queryString");
			if(queryString != null && !queryString.isEmpty()){
				url = url + (url.indexOf('?') > 0 ? '&' : '?') + queryString;
			}
			model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
		}
	}

	/** 담당자 기안 - 접수 문서의 상신 */
	private void processMakVw(HttpServletRequest request, QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			Map<String, String> paramMap, Map<String, List<String>> paramListMap, 
			Locale locale, ModelMap model) throws CmException, SQLException {
		
		String apvNo = paramMap.get("apvNo");// 결재번호
		UserVo userVo = LoginSession.getUser(request);
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, "processMakVw", userVo, locale);
		
		if(!"recv".equals(storedApOngdBVo.getDocStatCd())
				&& !"makVw".equals(storedApOngdBVo.getDocStatCd())){//recv:접수, makVw:담당
			LOGGER.error("Fail makVw - not stat - apvNo:"+apvNo+"  docStatCd:"+storedApOngdBVo.getDocStatCd());
			// ap.msg.makVw.notStat=담당자가 상신할 수 있는 문서상태가 아닙니다.
			throw new CmException("ap.msg.makVw.notStat", locale);
		}
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setDocProsStatCd("pubVw");
		queryQueue.update(apOngdBVo);
		
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// [히든옵션] 유통문서 어권 기준 - session:세션기준, orgnDoc:원본문서의 어권
		String docLangTypCd = "session".equals(optConfigMap.get("distDocLangTypCd")) ? 
				locale.getLanguage() : storedApOngdBVo.getDocLangTypCd();
				
		// 결재선 파라미터 리스트
		List<String> apvLnList = paramListMap.get("apvLn");
		// JSON 파라미터 들을 파싱해서 에 담은 List 
		List<ApOngdApvLnDVo> paramApOngdApvLnDVoList = parseApvLnJson(apvLnList, apvNo, "0", docLangTypCd, locale);
		
		// 기존 결재선 삭제 - 오류 방지용, 없어도 되는 로직
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		queryQueue.delete(apOngdApvLnDVo);
		
		// 담당 조회
		apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		ApOngdApvLnDVo makVwVo = (ApOngdApvLnDVo)commonDao.queryVo(apOngdApvLnDVo);
		
		// 리소스 캐쉬용 - 문서언어구분코드 에 따른 어권별 리소스 설정용
		Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
		Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
		
		boolean prevParalPubVw = false;
		
		// 결재의견내용
		String apvOpinCont = paramMap.get("apvOpinCont");
		
		// makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
		String apvrRoleCd;
		int i, size = paramApOngdApvLnDVoList.size();
		for(i=0; i<size; i++){
			
			apOngdApvLnDVo = paramApOngdApvLnDVoList.get(i);
			apOngdApvLnDVo.setApvLnHstNo("1");
			
			// 결재선 데이터에 문서언어에 따른 리스스 바인딩
			setApOngdApvLnDVoResc(apOngdApvLnDVo, docLangTypCd, 
					userCacheMap, orgCacheMap, optConfigMap, locale);
			
			// 결재자역할코드
			apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
			
			if(i==0){
				apOngdApvLnDVo.setApvOpinCont(paramMap.get("apvOpinCont"));//결재의견내용
				apOngdApvLnDVo.setVwDt("sysdate");// 조회일시
				apOngdApvLnDVo.setPrevApvrApvDt(makVwVo==null || makVwVo.getPrevApvrApvDt()==null
						? "sysdate" : makVwVo.getPrevApvrApvDt());
				apOngdApvLnDVo.setApvOpinCont(apvOpinCont);//결재의견내용
				apOngdApvLnDVo.setApvStatCd("cmplVw");//결재상태코드 - cmplVw:공람완료
				apOngdApvLnDVo.setApvDt("sysdate");//결재일시
			} else if(i==1) {
				apOngdApvLnDVo.setPrevApvrApvDt("sysdate");// 완료문서 - 맨 위로 올라오게
				apOngdApvLnDVo.setApvStatCd("inVw");//결재상태코드 - befoVw:공람전, inVw:공람중, cmplVw:공람완료
				prevParalPubVw = "paralPubVw".equals(apvrRoleCd);
				//문서에 현재 결재자 정보 세팅
				setCurApvrAtApOngdBVo(apOngdBVo, apOngdApvLnDVo);
			} else {
				if(prevParalPubVw){
					prevParalPubVw = "paralPubVw".equals(apvrRoleCd);
				}
				
				if(prevParalPubVw){
					apOngdApvLnDVo.setPrevApvrApvDt("sysdate");// 완료문서 - 맨 위로 올라오게
					apOngdApvLnDVo.setApvStatCd("inVw");//결재상태코드 - befoVw:공람전, inVw:공람중, cmplVw:공람완료
				} else {
					apOngdApvLnDVo.setApvStatCd("befoVw");//결재상태코드 - befoVw:공람전, inVw:공람중, cmplVw:공람완료
				}
			}
			if("inVw".equals(apOngdApvLnDVo.getApvStatCd())){
				// 메신저 보내기
				addMesseger(storedApOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
			}
			queryQueue.insert(apOngdApvLnDVo);
		}
		
		// 모바일 푸쉬
		List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
		addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
		
		commonSvc.execute(queryQueue);
		
		// 메신저 메세지 일괄 발송
		sendMesseger(messengerQueue, optConfigMap);
		// 모바일 푸쉬 메세지 - 보내기
		ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
		
		//ap.trans.submitOk={0} 하였습니다. - 상신
		String message = messageProperties.getMessage("ap.trans.submitOk", new String[]{"#ap.btn.subm"}, request);
		model.put("message", message);
		
		String menuId = request.getParameter("menuId");
		String paramMenuId = menuId!=null && menuId.startsWith("Y") ? menuId : null;
		
		String bxId = paramMap.get("bxId");
		String url = apBxSvc.getBxUrlByBxId(userVo, bxId, paramMenuId);
		String queryString = paramMap.get("queryString");
		if(queryString != null && !queryString.isEmpty()){
			url = url + (url.indexOf('?') > 0 ? '&' : '?') + queryString;
		}
		model.put("todo", "parent.removeApCachedCountMap(); parent.location.replace(\""+url+"\");");
	}

	/** 참조문서 처리 */
	private void processRefDoc(List<String> refApvList, String refApvModified,
			QueryQueue queryQueue, ApOngdBVo apOngdBVo, UserVo userVo,
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, String statCd) throws SQLException {
		
		String apvNo = apOngdBVo.getApvNo();
		List<ApOngdRefDocLVo> apOngdRefDocLVoList = parseRefDocLVoList(refApvList, apvNo, userVo.getUserUid());

		int i, size = apOngdRefDocLVoList==null ? 0 : apOngdRefDocLVoList.size();
		ApOngdRefDocLVo apOngdRefDocLVo;
		
		// 기안, 임시저장
		if("mak".equals(statCd) || "temp".equals(statCd)){
			
			// 참조문서이력번호
			String refDocHstNo = "1";
			apOngdBVo.setRefDocHstNo("1");//수신처이력번호
			//String refDocHstNo = apOngdBVo.getRefDocHstNo();
			
			// 기존것 삭제
			apOngdRefDocLVo = new ApOngdRefDocLVo();
			apOngdRefDocLVo.setApvNo(apvNo);
			//apOngdRefDocLVo.setRefDocHstNo(refDocHstNo);
			queryQueue.delete(apOngdRefDocLVo);
			
			// 파라미터로 넘겨진 데이터 INSERT
			for(i=0;i<size;i++){
				apOngdRefDocLVo = apOngdRefDocLVoList.get(i);
				apOngdRefDocLVo.setRefDocHstNo(refDocHstNo);
				queryQueue.insert(apOngdRefDocLVo);
			}
			
			// 이력 없음
			
		// 보류
		} else if("hold".equals(statCd)) {
			
			// 전송된 것이 없으면
			if(!"Y".equals(refApvModified)) return;
			
			// 보류참조문서이력번호
			String holdRefDocHstNo = myTurnApOngdApvLnDVo.getHoldRefDocHstNo();
			
			// 저장된 참조문서 조회
			apOngdRefDocLVo = new ApOngdRefDocLVo();
			apOngdRefDocLVo.setApvNo(apvNo);
			apOngdRefDocLVo.setRefDocHstNo(apOngdBVo.getRefDocHstNo());
			apOngdRefDocLVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<ApOngdRefDocLVo> storedApOngdRefDocLVoList = (List<ApOngdRefDocLVo>)commonDao.queryList(apOngdRefDocLVo);
			
			// 저장된 참조문서와 전송된 참조문서가 동일하면
			if(isIdenticalApOngdRefDocLVoList(apOngdRefDocLVoList, storedApOngdRefDocLVoList)){
				
				// 보류참조문서이력번호 - 가 있으면
				if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
					// 보류참조문서 데이터 삭제
					apOngdRefDocLVo = new ApOngdRefDocLVo();
					apOngdRefDocLVo.setApvNo(apvNo);
					apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
					queryQueue.delete(apOngdRefDocLVo);
					
					// 보류참조문서 이력 있음 - 삭제
					myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo("");
				}
				
			// 저장된 참조문서와 전송된 참조문서가 다르면
			} else {
				
				// 보류참조문서이력번호 - 가 있으면
				if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
					
					// 보류 내역 삭제
					apOngdRefDocLVo = new ApOngdRefDocLVo();
					apOngdRefDocLVo.setApvNo(apvNo);
					apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
					queryQueue.delete(apOngdRefDocLVo);
					
					// 전송된 내역 입력
					if(apOngdRefDocLVoList != null){
						for(ApOngdRefDocLVo newApOngdRefDocLVo : apOngdRefDocLVoList){
							newApOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
							queryQueue.insert(newApOngdRefDocLVo);
						}
					}
					
				} else {
					// 보류참조문서이력번호 - 생성
					holdRefDocHstNo = apCmSvc.addNo(apOngdBVo.getRefDocHstNo(), 1);
					
					// 전송된 내역 입력
					if(apOngdRefDocLVoList != null){
						for(ApOngdRefDocLVo newApOngdRefDocLVo : apOngdRefDocLVoList){
							newApOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
							queryQueue.insert(newApOngdRefDocLVo);
						}
					}
					
					// 보류참조문서이력번호 - 세팅
					myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo(holdRefDocHstNo);
				}
			}
			
		// 재검토, 반려
		} else if("reRevw".equals(statCd) || "rejt".equals(statCd)){
			
			// 보류참조문서이력번호
			String holdRefDocHstNo = myTurnApOngdApvLnDVo.getHoldRefDocHstNo();
			
			// 보류된 데이터 - 삭제
			if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
				apOngdRefDocLVo = new ApOngdRefDocLVo();
				apOngdRefDocLVo.setApvNo(apvNo);
				apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
				queryQueue.delete(apOngdRefDocLVo);
			}
			
			// 보류참조문서이력번호 - 지움
			myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo("");
			
		// 승인, 찬성(합의), 반대(합의)
		} else {
			
			// 이전참조문서이력번호
			String prevRefDocHstNo = myTurnApOngdApvLnDVo.getPrevRefDocHstNo();
			// 보류참조문서이력번호
			String holdRefDocHstNo = myTurnApOngdApvLnDVo.getHoldRefDocHstNo();
			
			// 전송된 것이 없으면
			if(!"Y".equals(refApvModified)) {
				
				// 보류된 것이 있으면
				if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
					
					if(prevRefDocHstNo==null || prevRefDocHstNo.isEmpty()){
						// 이전참조문서이력번호 - 현재 저장되어 있는 본문이력번호 세팅
						myTurnApOngdApvLnDVoToSave.setPrevRefDocHstNo(apOngdBVo.getRefDocHstNo());
					}
					
					// 본문이력번호 - 보류본문이력번호로 변경함
					apOngdBVo.setRefDocHstNo(holdRefDocHstNo);
					
					// 보류참조문서이력번호 - 지워줌
					myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo("");
				}
				
			// 전송된 것이 있으면
			} else {
				
				// 저장된 참조문서 조회
				apOngdRefDocLVo = new ApOngdRefDocLVo();
				apOngdRefDocLVo.setApvNo(apvNo);
				apOngdRefDocLVo.setRefDocHstNo(apOngdBVo.getRefDocHstNo());
				apOngdRefDocLVo.setOrderBy("SORT_ORDR");
				@SuppressWarnings("unchecked")
				List<ApOngdRefDocLVo> storedApOngdRefDocLVoList = (List<ApOngdRefDocLVo>)commonDao.queryList(apOngdRefDocLVo);
				
				// 저장된 참조문서와 전송된 참조문서가 동일하면
				if(isIdenticalApOngdRefDocLVoList(apOngdRefDocLVoList, storedApOngdRefDocLVoList)){
					
					// 보류참조문서이력번호 - 가 있으면
					if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
						// 보류참조문서 데이터 삭제
						apOngdRefDocLVo = new ApOngdRefDocLVo();
						apOngdRefDocLVo.setApvNo(apvNo);
						apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
						queryQueue.delete(apOngdRefDocLVo);
						
						// 보류참조문서이력번호 - 삭제
						myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo("");
					}
					
				// 저장된 참조문서와 전송된 참조문서가 다르면
				} else {

					// 보류참조문서이력번호 - 가 있으면
					if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
						
						// 보류 내역 삭제
						apOngdRefDocLVo = new ApOngdRefDocLVo();
						apOngdRefDocLVo.setApvNo(apvNo);
						apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
						queryQueue.delete(apOngdRefDocLVo);
						
						// 전송된 내역 입력
						if(apOngdRefDocLVoList != null){
							for(ApOngdRefDocLVo newApOngdRefDocLVo : apOngdRefDocLVoList){
								newApOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
								queryQueue.insert(newApOngdRefDocLVo);
							}
						}
						
						// 보류참조문서이력번호 - 지워줌
						myTurnApOngdApvLnDVoToSave.setHoldRefDocHstNo("");
						
						if(prevRefDocHstNo==null || prevRefDocHstNo.isEmpty()){
							// 이전본문이력번호 - 현재 저장되어 있는 본문이력번호 세팅
							myTurnApOngdApvLnDVoToSave.setPrevBodyHstNo(apOngdBVo.getRefDocHstNo());
						}
						
						// 참조문서이력번호 - 보류참조문서이력번호 사용
						apOngdBVo.setRefDocHstNo(holdRefDocHstNo);
						
					} else {
						
						// 참조문서이력번호 - 다음번호 생성
						String recvDeptHstNo = apCmSvc.addNo(apOngdBVo.getRefDocHstNo(), 1);
						
						// 전송된 내역 입력
						if(apOngdRefDocLVoList != null){
							for(ApOngdRefDocLVo newApOngdRefDocLVo : apOngdRefDocLVoList){
								newApOngdRefDocLVo.setRefDocHstNo(recvDeptHstNo);
								queryQueue.insert(newApOngdRefDocLVo);
							}
						}
						
						if(prevRefDocHstNo==null || prevRefDocHstNo.isEmpty()){
							// 이전참조문서이력번호 - 현재 저장되어 있는 참조문서이력 세팅
							myTurnApOngdApvLnDVoToSave.setPrevRefDocHstNo(apOngdBVo.getRefDocHstNo());
						}
						
						// 참조문서이력번호 - 새로 생성된 번호 세팅
						apOngdBVo.setRefDocHstNo(recvDeptHstNo);
					}
				}
			}
		}
		
	}
	
	/** 파싱해서 [참조문서] 목록 데이터 리턴 */
	private List<ApOngdRefDocLVo> parseRefDocLVoList(List<String> refApvList, String apvNo, String userUid){
		if(refApvList==null || refApvList.isEmpty()) return null;
		
		JSONObject jsonObject;
		ApOngdRefDocLVo apOngdRefDocLVo;
		List<ApOngdRefDocLVo> list = new ArrayList<ApOngdRefDocLVo>();
		for(String json : refApvList){
			jsonObject = (JSONObject)JSONValue.parse(json);
			if(jsonObject != null){
				apOngdRefDocLVo = new ApOngdRefDocLVo();
				apOngdRefDocLVo.setApvNo(apvNo);
				apOngdRefDocLVo.setRefApvNo((String)jsonObject.get("apvNo"));
				apOngdRefDocLVo.setModrUid(userUid);
				apOngdRefDocLVo.setModDt("sysdate");
				list.add(apOngdRefDocLVo);
			} else {
				if(json!=null && !json.isEmpty()){
					LOGGER.warn("AP attach-RefDoc json : "+json);
				}
			}
		}
		return list.isEmpty() ? null : list;
	}

	/** 수신처 정보 QueryQueue 에 저장 */
	private void processRecvDept(List<String> recvDeptList, String recvDeptModified,
			String apvNo, ApOngdBVo apOngdBVo, QueryQueue queryQueue, UserVo userVo,
			String docLangTypCd, Map<Integer, OrOrgBVo> orgCacheMap, Locale locale,
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, String statCd) throws SQLException, CmException {
		
		//JSON 목록 데이터 파싱해서 [수신처] 목록 데이터 리턴
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = parseRecvDeptJson(recvDeptList, apvNo, docLangTypCd, 
				orgCacheMap, locale, userVo.getUserUid(), null);
		
		// 중복 수신처 확인
		checkDupRecvDept(apOngdRecvDeptLVoList, apvNo, locale);
		
		int i, size = apOngdRecvDeptLVoList==null ? 0 : apOngdRecvDeptLVoList.size();
		ApOngdRecvDeptLVo apOngdRecvDeptLVo;
		
		// 기안, 임시저장
		if("mak".equals(statCd) || "temp".equals(statCd)){
			
			// 수신처이력번호
			String recvDeptHstNo = "1";
			apOngdBVo.setRecvDeptHstNo(recvDeptHstNo); //수신처이력번호
			
			// 기존것 삭제
			apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			apOngdRecvDeptLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdRecvDeptLVo);
			
			// 파라미터로 넘겨진 데이터 INSERT
			for(i=0;i<size;i++){
				apOngdRecvDeptLVo = apOngdRecvDeptLVoList.get(i);
				apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
				queryQueue.insert(apOngdRecvDeptLVo);
			}
			
			// 이력 없음
			
		// 보류
		} else if("hold".equals(statCd)) {
			
			// 전송된 것이 없으면 - 변경 없음
			if(!"Y".equals(recvDeptModified)) return;
			
			// 보류수신처이력번호
			String holdRecvDeptHstNo = myTurnApOngdApvLnDVo.getHoldRecvDeptHstNo();
			
			// 저장된 수신처 조회
			apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			apOngdRecvDeptLVo.setApvNo(apvNo);
			apOngdRecvDeptLVo.setRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());
			@SuppressWarnings("unchecked")
			List<ApOngdRecvDeptLVo> storedApOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
			
			// 저장된 수신처와 전송된 수신처가 동일하면
			if(isIdenticalApOngdRecvDeptLVoList(apOngdRecvDeptLVoList, storedApOngdRecvDeptLVoList)){
				
				// 보류수신처이력번호 - 가 있으면
				if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
					// 보류수신처 데이터 삭제
					apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
					apOngdRecvDeptLVo.setApvNo(apvNo);
					apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
					queryQueue.delete(apOngdRecvDeptLVo);
					
					// 보류수신처 이력 있음 - 삭제
					myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo("");
				}
				
			// 저장된 수신처와 전송된 수신처가 다르면
			} else {
				
				// 보류수신처이력번호 - 가 있으면
				if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
					
					// 보류 내역 삭제
					apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
					apOngdRecvDeptLVo.setApvNo(apvNo);
					apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
					queryQueue.delete(apOngdRecvDeptLVo);
					
					// 전송된 내역 입력
					if(apOngdRecvDeptLVoList != null){
						for(ApOngdRecvDeptLVo newApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
							newApOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
							queryQueue.insert(newApOngdRecvDeptLVo);
						}
					}
					
				} else {
					// 보류수신처이력번호 - 생성
					holdRecvDeptHstNo = apCmSvc.addNo(apOngdBVo.getRecvDeptHstNo(), 1);
					
					// 전송된 내역 입력
					if(apOngdRecvDeptLVoList != null){
						for(ApOngdRecvDeptLVo newApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
							newApOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
							queryQueue.insert(newApOngdRecvDeptLVo);
						}
					}
					
					// 보류수신처이력번호 - 세팅
					myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo(holdRecvDeptHstNo);
				}
			}
			
		// 재검토, 반려
		} else if("reRevw".equals(statCd) || "rejt".equals(statCd)){
			
			// 보류수신처이력번호
			String holdRecvDeptHstNo = myTurnApOngdApvLnDVo.getHoldRecvDeptHstNo();
			
			// 보류된 데이터 - 삭제
			if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
				apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
				apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
				queryQueue.delete(apOngdRecvDeptLVo);
			}
			
			// 보류수신처이력번호 - 지움
			myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo("");
			
		// 승인, 찬성(합의), 반대(합의)
		} else {
			
			// 이전수신처이력번호
			String prevRecvDeptHstNo = myTurnApOngdApvLnDVo.getPrevRecvDeptHstNo();
			// 보류수신처이력번호
			String holdRecvDeptHstNo = myTurnApOngdApvLnDVo.getHoldRecvDeptHstNo();
			
			// 전송된 것이 없으면
			if(!"Y".equals(recvDeptModified)) {
				
				// 보류된 것이 있으면
				if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
					
					if(prevRecvDeptHstNo==null || prevRecvDeptHstNo.isEmpty()){
						// 이전수신처이력번호 - 현재 저장되어 있는 본문이력번호 세팅
						myTurnApOngdApvLnDVoToSave.setPrevRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());
					}
					
					// 본문이력번호 - 보류본문이력번호로 변경함
					apOngdBVo.setRecvDeptHstNo(holdRecvDeptHstNo);
					
					// 보류수신처이력번호 - 지워줌
					myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo("");
				}
				
			// 전송된 것이 있으면
			} else {
				
				// 저장된 수신처 조회
				apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
				apOngdRecvDeptLVo.setRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());
				@SuppressWarnings("unchecked")
				List<ApOngdRecvDeptLVo> storedApOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
				
				// 저장된 수신처와 전송된 수신처가 동일하면
				if(isIdenticalApOngdRecvDeptLVoList(apOngdRecvDeptLVoList, storedApOngdRecvDeptLVoList)){
					
					// 보류수신처이력번호 - 가 있으면
					if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
						// 보류수신처 데이터 삭제
						apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
						apOngdRecvDeptLVo.setApvNo(apvNo);
						apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
						queryQueue.delete(apOngdRecvDeptLVo);
						
						// 보류수신처이력번호 - 삭제
						myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo("");
					}
					
				// 저장된 수신처와 전송된 수신처가 다르면
				} else {

					// 보류수신처이력번호 - 가 있으면
					if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
						
						// 보류 내역 삭제
						apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
						apOngdRecvDeptLVo.setApvNo(apvNo);
						apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
						queryQueue.delete(apOngdRecvDeptLVo);
						
						// 전송된 내역 입력
						if(apOngdRecvDeptLVoList != null){
							for(ApOngdRecvDeptLVo newApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
								newApOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
								queryQueue.insert(newApOngdRecvDeptLVo);
							}
						}
						
						// 보류수신처이력번호 - 지워줌
						myTurnApOngdApvLnDVoToSave.setHoldRecvDeptHstNo("");
						
						if(prevRecvDeptHstNo==null || prevRecvDeptHstNo.isEmpty()){
							// 이전본문이력번호 - 현재 저장되어 있는 본문이력번호 세팅
							myTurnApOngdApvLnDVoToSave.setPrevBodyHstNo(apOngdBVo.getRecvDeptHstNo());
						}
						
						// 수신처이력번호 - 보류수신처이력번호 사용
						apOngdBVo.setRecvDeptHstNo(holdRecvDeptHstNo);
						
					} else {
						
						// 수신처이력번호 - 다음번호 생성
						String recvDeptHstNo = apCmSvc.addNo(apOngdBVo.getRecvDeptHstNo(), 1);
						
						// 전송된 내역 입력
						if(apOngdRecvDeptLVoList != null){
							for(ApOngdRecvDeptLVo newApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
								newApOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
								queryQueue.insert(newApOngdRecvDeptLVo);
							}
						}
						
						if(prevRecvDeptHstNo==null || prevRecvDeptHstNo.isEmpty()){
							// 이전수신처이력번호 - 현재 저장되어 있는 수신처이력번호 세팅
							myTurnApOngdApvLnDVoToSave.setPrevRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());
						}
						
						// 수신처이력번호 - 새로 생성된 번호 세팅
						apOngdBVo.setRecvDeptHstNo(recvDeptHstNo);
					}
				}
			}
		}
	}
	
	/** 동일한 결재라인 인지 검사 */
	private boolean isIdenticalApvLn(List<ApOngdApvLnDVo> apOngdApvLnDVoList1, List<ApOngdApvLnDVo> apOngdApvLnDVoList2){
		int size1 = apOngdApvLnDVoList1==null ? 0 : apOngdApvLnDVoList1.size();
		int size2 = apOngdApvLnDVoList2==null ? 0 : apOngdApvLnDVoList2.size();
		if(size1 != size2) return false;
		
		ApOngdApvLnDVo vo1, vo2;
		for(int i=0;i<size1; i++){
			vo1 = apOngdApvLnDVoList1.get(i);
			vo2 = apOngdApvLnDVoList2.get(i);
			
			// 결재자UID
			if(!ApCmUtil.isIdentical(vo1.getApvrUid(), vo2.getApvrUid())) return false;
			// 결재부서ID
			if(!ApCmUtil.isIdentical(vo1.getApvDeptId(), vo2.getApvDeptId())) return false;
			// 결재자역할코드
			if(!ApCmUtil.isIdentical(vo1.getApvrRoleCd(), vo2.getApvrRoleCd())) return false;
		}
		return true;
	}
	
	/** 동일한 수신처 목록인지 검사 */
	private boolean isIdenticalApOngdRecvDeptLVoList(List<ApOngdRecvDeptLVo> list1, List<ApOngdRecvDeptLVo> list2){
		int i, size = list1==null ? 0 : list1.size(), size2 = list2==null ? 0 : list2.size();
		if(size != size2) return false;
		
		ApOngdRecvDeptLVo vo1, vo2;
		for(i=0;i<size; i++){
			vo1 = list1.get(i);
			vo2 = list2.get(i);
			
			// 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
			if(!ApCmUtil.isIdentical(vo1.getRecvDeptTypCd(), vo2.getRecvDeptTypCd())) return false;
			// 수신처ID
			if(!ApCmUtil.isIdentical(vo1.getRecvDeptId(), vo2.getRecvDeptId())) return false;
			// 수신처ID
			if(!ApCmUtil.isIdentical(vo1.getRecvDeptNm(), vo2.getRecvDeptNm())) return false;
			
			// 참조처ID
			if(!ApCmUtil.isIdentical(vo1.getRefDeptId(), vo2.getRefDeptId())) return false;
			// 참조처명
			if(!ApCmUtil.isIdentical(vo1.getRefDeptNm(), vo2.getRefDeptNm())) return false;
		}
		return true;
	}
	
//	/** 동일한 참조 열람자 목록인지 */
//	private boolean isIdenticalRefVwList(List<String> refVwrUidList, List<ApOngdRefVwDVo> storedApOngdRecvDeptLVoList){
//		
//		int size1 = refVwrUidList==null ? 0 : refVwrUidList.size();
//		int size2 = storedApOngdRecvDeptLVoList==null ? 0 : storedApOngdRecvDeptLVoList.size();
//		if(size1 != size2) return false;
//		
//		for(int i=0; i<size1; i++){
//			if(!refVwrUidList.get(i).equals(storedApOngdRecvDeptLVoList.get(i).getRefVwrUid())){
//				return false;
//			}
//		}
//		return true;
//	}
	
	/** 동일한 참조문서 목록인지 검사 */
	private boolean isIdenticalApOngdRefDocLVoList(List<ApOngdRefDocLVo>list1, List<ApOngdRefDocLVo>list2){
		
		int i, size = list1==null ? 0 : list1.size(), size2 = list2==null ? 0 : list2.size();
		if(size != size2) return false;
		
		ApOngdRefDocLVo vo1, vo2;
		for(i=0;i<size; i++){
			vo1 = list1.get(i);
			vo2 = list2.get(i);
			// 참조결재번호
			if(!vo1.getRefApvNo().equals(vo2.getRefApvNo())) return false;
		}
		return true;
		
	}
	
	/** 히스토리 테이블 입력 */
	private void storeApvLnHistory(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnHstNo, QueryQueue queryQueue){
		if(apOngdApvLnDVoList!=null){
			ApOngdApvLnDVo apOngdApvLnDVo;
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
				apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVo.fromMap(storedApOngdApvLnDVo.toMap());
				apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
				apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
				queryQueue.insert(apOngdApvLnDVo);
			}
		}
	}

	/** null 또는 공백 체크 */
	private boolean isEmpty(String txt){
		return txt==null || txt.isEmpty();
	}
	
	/** 결재라인 저장 - 승인, 찬성, 반대  */
	private void processApvLn(List<ApOngdApvLnDVo> paramApOngdApvLnDVoList, ApvLines apvLines, 
			Map<String, String> paramMap, ApOngdBVo apOngdBVo,
			UserVo userVo, Map<Integer, OrUserBVo> userCacheMap, Map<Integer, OrOrgBVo> orgCacheMap,
			Locale locale, QueryQueue queryQueue, List<Map<String, String>> messengerQueue, Map<String, String> optConfigMap, ModelMap model,
			String apvLnPno, ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, String statCd) throws SQLException, CmException{

		String apvNo = apOngdBVo.getApvNo();
		
		// 합의기안 여부 - 부서대기함에서 경로지정 후 합의승인/합의반려 했을때
		boolean atMakAgr = "Y".equals(paramMap.get("atMakAgr"));
		
		// 이전결재라인이력번호
		String prevApvLnHstNo = myTurnApOngdApvLnDVo.getPrevApvLnHstNo();
		// 보류결재라인이력번호
		String holdApvLnHstNo = myTurnApOngdApvLnDVo.getHoldApvLnHstNo();

		// 저장되어 있던 결재라인(상신 할때 DB에 저장되어 있던) 결재라인
		List<ApOngdApvLnDVo> storedApOngdApvLnDVoList = atMakAgr ? null : apvLines.getCurrLn();

		// 저장 할 결재라인
		List<ApOngdApvLnDVo> toStoreApOngdApvLnDVoList = null;
		
		// 결재라인이력번호
		String apvLnHstNo;
		
		// 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:위임, postApvd:후열, psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
		String myRoleCd = myTurnApOngdApvLnDVo.getApvrRoleCd();
		
		// 의견
		String apvOpinCont = paramMap.get("apvOpinCont");
		boolean hasOpin = apvOpinCont!=null && !apvOpinCont.trim().isEmpty();
		boolean hasSubLnNextApvr = false;
		
		// 합의기안 이면 - byOneAgr:합의1인결재, makAgr:합의기안
		if("byOneAgr".equals(myRoleCd) || "makAgr".equals(myRoleCd)){
		
			apvLnHstNo = "1";
			
			// 저장 할 결재 라인 - 전송된 결재라인
			toStoreApOngdApvLnDVoList = paramApOngdApvLnDVoList;
			
			// 저장되어 있던 결재라인 - 의견 유지용
			storedApOngdApvLnDVoList = apvLines.getSubLnByApvLnPno(myTurnApOngdApvLnDVo.getApvLnPno());
			
			// 보류된 것이 있으면
			if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
				// 보류 내역 삭제
				deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
			}
			// 보류결재라인이력번호 - 삭제
			myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
			
		// 변경사항 없음
		} else if(paramApOngdApvLnDVoList==null){
			
			// 보류된 것이 있으면
			if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
				
				// 저장 할 결재 라인 - 조회
				toStoreApOngdApvLnDVoList = queryApvLn(apvNo, apvLnPno, holdApvLnHstNo);
				
				// 보류 데이터 삭제
				deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
				
				// 이력 테이블 생성
				storeApvLnHistory(storedApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnHstNo(), queryQueue);
				
				// 저장 할 결재라인이력번호 
				apvLnHstNo = holdApvLnHstNo;
				
				if(prevApvLnHstNo==null || prevApvLnHstNo.isEmpty()){
					// 이전결재라인이력번호 - 세팅
					myTurnApOngdApvLnDVoToSave.setPrevApvLnHstNo(myTurnApOngdApvLnDVo.getApvLnHstNo());
				}
				
				// 보류결재라인이력번호 - 삭제
				myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
				
			} else {
				// 저장 할 결재 라인 - 저장되어 있던 결재라인(보류 되기전, 나에게 넘겨 온) 결재라인
				toStoreApOngdApvLnDVoList = storedApOngdApvLnDVoList;
				// 저장 할 결재라인이력번호 
				apvLnHstNo = myTurnApOngdApvLnDVo.getApvLnHstNo();
			}
			
		// 변경사항이 있으면
		} else {
			
			// 전송된 것과 저장되어 있던 결재라인(원본) 이 같으면
			if(isIdenticalApvLn(paramApOngdApvLnDVoList, storedApOngdApvLnDVoList)){
				
				// 보류된 것이 있으면
				if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
					
					// 보류 내역 삭제
					deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
					
					// 보류결재라인이력번호 - 삭제
					myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
					
				}
				// 저장 할 결재 라인 - 저장되어 있던 결재라인(보류 되기전, 나에게 넘겨 온) 결재라인
				toStoreApOngdApvLnDVoList = storedApOngdApvLnDVoList;
				// 저장 할 결재라인이력번호 
				apvLnHstNo = myTurnApOngdApvLnDVo.getApvLnHstNo();
				
			// 전송된 것과 저장되어 있던 결재라인(원본) 이 다르면
			} else {
				
				// 보류된 것이 있으면
				if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
					// 보류 내역 삭제
					deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
					
					//보류결재라인이력번호 로 결재라인이력번호 사용
					apvLnHstNo = holdApvLnHstNo;
					
					// 보류결재라인이력번호 - 삭제
					myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
				} else {
					// 저장 할 결재라인이력번호  - 생성
					apvLnHstNo = getNextApvLnHstNo(apvNo, apvLnPno);
				}
				
				// 이력 테이블 생성
				storeApvLnHistory(storedApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnHstNo(), queryQueue);
				
				// 저장 할 결재 라인 - 전송된 결재라인
				toStoreApOngdApvLnDVoList = paramApOngdApvLnDVoList;
				
				if(prevApvLnHstNo==null || prevApvLnHstNo.isEmpty()){
					// 이전결재라인이력번호 - 세팅
					myTurnApOngdApvLnDVoToSave.setPrevApvLnHstNo(myTurnApOngdApvLnDVo.getApvLnHstNo());
				}
			}
			
		}
		
		// 이전 결재라인에서 의견을 백업할 맵
		Map<Integer, ApOngdApvLnDVo> opinMap = new HashMap<Integer, ApOngdApvLnDVo>();
		
		// 기존 결재선 - 삭제
		deleteApvLn(apvNo, apvLnPno, null, queryQueue);
		
		String myApvLnNo = myTurnApOngdApvLnDVo.getApvLnNo();
		
		// 합의기안 일때 - 부서대기함에서
		if(atMakAgr){
			// 의견 유지용 맵 - 만 생성 - 부서함에서 기안할 때는 상신자 이전의 경로가 없기 때문에
			opinMap = toOpinMap(storedApOngdApvLnDVoList, null);
		} else {
			// 내차례 이전까지 입력 진행 - 원본 데이터 이용 생성
			storeApvLnBeforeMyTurn(storedApOngdApvLnDVoList, 
					myApvLnNo, apvLnHstNo, false,
					opinMap, queryQueue);
		}
		
		/////////////////////////////////////////////////////
		//
		//    나를 포함 나의 뒤 결재 라인 처리
		
		// 내차례와 내차례 뒤의 결재라인 여부
		boolean afterMyTurn = false;
		
		// 최상위(본) 결재라인이 종료 되었는지
		boolean isRootCmpl = false;
		
		// 현재 작업(submit)한 결재자가 병렬인지 여부
		boolean isCurrApvrParal = false;
		
		// 결재자역할코드
		String apvrRoleCd, opin, vwDt, apvOpinPrefix = null;
		
		// 문서언어구분코드
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		ApOngdApvLnDVo apOngdApvLnDVo, opinApOngdApvLnDVo;
		
		// 결재 후 다음차례의 결재자/부서 를 담아둘 맵
		Map<Integer, ApOngdApvLnDVo> nextTurnMap = null;

		// 합의기안 - 부서대기함에서 경로 지정해서 보낸 경우
		if(atMakAgr){
			// 부모라인의 담당자 지정여부 UPDATE
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnPno("0");
			apOngdApvLnDVo.setApvLnNo(apvLnPno);
			apOngdApvLnDVo.setPichApntYn("Y");
			// 부모의 의견 지움
			apOngdApvLnDVo.setApvOpinCont("");
			apOngdApvLnDVo.setApvOpinDispYn("");
			queryQueue.update(apOngdApvLnDVo);
		}
		
		// 부서합의를 담아둘 목록 - 루트라인 일때 - 부서합의의 순서가 바뀌었을 때 해당 하위 라인의 부모라인 번호를 맞추는 역할
		//   재검토로 서브라인(합의라인)에서 루트라인으로 넘어 왔을때 부서합의의 순서가 변경되면, 하위라인의 정보를 찾지 못하는 것 보완
		List<ApOngdApvLnDVo> deptAgrList =  "0".equals(myTurnApOngdApvLnDVo.getApvLnPno()) ? new ArrayList<ApOngdApvLnDVo>() : null;
		
		int i, size = toStoreApOngdApvLnDVoList.size();
		for(i=0; i<size; i++){
			
			apOngdApvLnDVo = toStoreApOngdApvLnDVoList.get(i);
			apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
			
			// 결재자 차례 또는 결재자 뒤의 결재 라인
			if(afterMyTurn || myApvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
				
				// 부서고 사용자UID가 있으면 - 부서합의에서 재검토 내려갈때 재검토자 세팅하며 - 재기안시 지워줘야 함
				if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())
						&& apOngdApvLnDVo.getApvrUid() != null && !apOngdApvLnDVo.getApvrUid().isEmpty()){
					apOngdApvLnDVo.setApvrUid(null);
				}
				
				// 결재선 데이터에 문서언어에 따른 리스스 바인딩
				setApOngdApvLnDVoResc(apOngdApvLnDVo, docLangTypCd, 
						userCacheMap, orgCacheMap, optConfigMap, locale);
				
				// 내 다음 순서일 때
				if(afterMyTurn){
					
					// 의견, 조회일시 복원 - // 의견이 있으면 의견 해쉬에 담음(백업)
					opinApOngdApvLnDVo = getFromMap(opinMap, apOngdApvLnDVo);
					if(opinApOngdApvLnDVo != null){
						
						opin = opinApOngdApvLnDVo.getApvOpinCont();
						vwDt = opinApOngdApvLnDVo.getVwDt();
						
						if(opin != null && !opin.isEmpty() && ApDocUtil.hasOpin(apvrRoleCd)){
							apOngdApvLnDVo.setApvOpinCont(opinApOngdApvLnDVo.getApvOpinCont());
							apOngdApvLnDVo.setApvOpinDispYn(opinApOngdApvLnDVo.getApvOpinDispYn());
						}
						
						if(vwDt != null && !vwDt.isEmpty()){
							apOngdApvLnDVo.setVwDt(vwDt);
						}
					}
					
					// 완료 되었을 때
					if(isRootCmpl){
						// 통보면
						if(ApDocUtil.isInfmOfApvrRole(apvrRoleCd)){
							apOngdApvLnDVo.setApvStatCd("inInfm");//결재상태코드 - inInfm:통보중
							apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//	이전결재자결재일시
							apOngdApvLnDVo.setVwDt("");//조회일시
							
							// 개인통보 일때 - 통보 메세지
							if("psnInfm".equals(apvrRoleCd)){
								//[옵션] msgPsnInfm=통보 메세지 사용
								if("Y".equals(optConfigMap.get("msgPsnInfm"))){
									addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
								}
							}
							
						// 공석이면
						} else if("abs".equals(apvrRoleCd)){
							apOngdApvLnDVo.setApvStatCd("apvd");//결재상태코드 - apvd:승인
							apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//	이전결재자결재일시
							apOngdApvLnDVo.setVwDt("");//조회일시
						} else {
							LOGGER.warn("Not appropriate apvr when completed - apvNo:"+apvNo
									+"  apvLnPno:"+apvLnPno+"  apvLnNo:"+apOngdApvLnDVo.getApvLnNo()+"  apvrRoleCd:"+apvrRoleCd);
						}
						
					} else{
						
						// 현재 작업(submit)한 결재자가 병렬인지 여부
						if(isCurrApvrParal){
							// 다음번재 작업자도 병렬인지 체크
							isCurrApvrParal = ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd());
						}
						
						// 다음번재 작업자도 병렬이면 - 저장된 데이터 유지
						if(isCurrApvrParal){
							
							// 저장되어있는 데이터로 교체 - 병렬은 루트 라인에만 있음
							apOngdApvLnDVo = ApDocTransUtil.findApvLnByApvLnNo(apvLines.getRootLn(), apOngdApvLnDVo.getApvLnNo());
							
						} else {
							
							// 결재자 다음 차례인 경우
							if(getFromMap(nextTurnMap, apOngdApvLnDVo) != null){
								
								apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//	이전결재자결재일시
								apOngdApvLnDVo.setVwDt("");//조회일시
								
								if("abs".equals(apvrRoleCd)){
									apOngdApvLnDVo.setApvStatCd("apvd");//결재상태코드 - apvd:승인
								} else if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd) || !ApDocUtil.isRootLine(apvLnPno)) {
									apOngdApvLnDVo.setApvStatCd("inAgr");//결재상태코드 - inAgr:합의중
								} else {
									apOngdApvLnDVo.setApvStatCd("inApv");//결재상태코드 - inApv:결재중
								}
								
								// 부서면 - 담당자 지정여부 - 지정안합으로 변경
								if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
									apOngdApvLnDVo.setPichApntYn("N");
								}
								
							} else {
								if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd) || !ApDocUtil.isRootLine(apvLnPno)) {
									apOngdApvLnDVo.setApvStatCd("befoAgr");//결재상태코드 - befoAgr:합의전
								} else {
									apOngdApvLnDVo.setApvStatCd("befoApv");//결재상태코드 - befoApv:결재전
								}
								
								// 부서의 경우 - 사용자가 있으면
							}
							
							// 현재 결재라인이 루트 경로고(부서합의 목록이 정의되어 있고), 현재 결재자가 부서합의면 - 부서합의 목록에 더함
							//  - 차후 부서합의 라인 데이터가 있고, 부서합의의 순서가 바뀌었을때 apvLnPno를 맞추어서 예전 데이터 유지 목적
							if(deptAgrList != null && ApDocUtil.isDeptAgrRole(apvrRoleCd)){
								deptAgrList.add(apOngdApvLnDVo);
							}
							
							// 결재일시 지우기
							if(apOngdApvLnDVo.getApvDt()!=null && !apOngdApvLnDVo.getApvDt().isEmpty()){
								apOngdApvLnDVo.setApvDt(null);
							}
						}
						
						// [재검토] 의 [재검토] 가 되면 다음 결재자가 [이전변경이력]이 있을 수 있음
						//   이때 현재 결재자 뒤의 결재자가 [이전변경이력]이 있으면 삭제함
						//   - 재검토에서 올리면 다음결재자의 변경도 현재 결재가 변경한 것을 처리 
						clearPrevHst(apOngdApvLnDVo);
						
					}
 					
				// 내 순서에 왔을때
				} else {
					
					afterMyTurn = true;
					
//					String apvOpinCont = paramMap.get("apvOpinCont");
					if(apvOpinCont!=null && "cons".equals(statCd)){
						// [반대]
						String prefix = "["+ptSysSvc.getTerm("ap.term.cons", docLangTypCd)+"]";
						if(!apvOpinCont.startsWith(prefix)){
							apvOpinPrefix = prefix+" ";
						}
					}
					
					// 보류 의견 - 삭제
					ApOngdHoldOpinDVo apOngdHoldOpinDVo = new ApOngdHoldOpinDVo();
					apOngdHoldOpinDVo.setApvNo(apOngdApvLnDVo.getApvNo());
					apOngdHoldOpinDVo.setApvrUid(apOngdApvLnDVo.getApvrUid());
					queryQueue.delete(apOngdHoldOpinDVo);
					
					apOngdApvLnDVo.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));	//	결재의견표시여부
					apOngdApvLnDVo.setApvOpinCont(apvOpinPrefix==null ? apvOpinCont : apvOpinPrefix + apvOpinCont);		//	결재의견내용
					if(myTurnApOngdApvLnDVo.getVwDt()==null || myTurnApOngdApvLnDVo.getVwDt().isEmpty()){// myTurnApOngdApvLnDVo - 저장된 데이터
						apOngdApvLnDVo.setVwDt("sysdate");							//	조회일시 - 전송된 데이터 일 수 있음
					} else {
						apOngdApvLnDVo.setVwDt(myTurnApOngdApvLnDVo.getVwDt());
					}
					//apOngdApvLnDVo.setPrevApvrApvDt(myTurnApOngdApvLnDVo.getPrevApvrApvDt());//이전결재자결재일시
					
					// 해당 결재 라인
					apOngdApvLnDVo.setApvStatCd(statCd);//apvd:승인, cons:반대, pros:찬성
					apOngdApvLnDVo.setHoldApvLnHstNo("");//현재결재라인이력번호 - 라인정보 보류 번호 삭제
					
					// 전결여부 세팅 - 부서합의 의 경우 승인여부와 전결여부가 따로 표시되어야 함
					if(apvrRoleCd.equals("pred")){//pred:전결
						apOngdApvLnDVo.setPredYn("Y");
					}
					
					// 부서대기함 - 대리 체크 안함
					String bxId = paramMap.get("bxId");
					boolean agnChk = !"deptBx".equals(bxId);
					
					// 해당 결재라인 서명 처리
					setApvrSign(apOngdApvLnDVo, agnChk, optConfigMap,
							userCacheMap, docLangTypCd, userVo, locale);
					
					/* 
					 * 1.  자신이 병렬이면 - 병렬이 완료 되었는지 보고
					 * 1.1 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
					 * 1.2 (다음 결재자로 보낼지 여부) 확인 - 자신이 병렬이 아니거나 자신이 속한 병렬이 완료 되었으면 - 보냄
					 * 
					 * -- 다음 결재자로 보낼 것이면 --
					 * 2.1  다음 결재자 찾음 - 현재 결재선에서
					 * 2.2  다음 결재자 없고 현재 결재선이 서브라인 이면
					 * 2.2.1  부모결재자(합의부서)를 찾아 완료처리함
					 * 2.2.2  부모결재자(합의부서)가 병렬이면 - 완료여부 체크
					 * 2.2.3  부모결재자(합의부서) 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
					 * 2.2.4 (다음 결재자로 보낼지 여부) 확인 후 - 다음 결재자 찾음
					 * 
					 * 3. 다음결재자 없으면 - 완료처리 - 등록대장
					 * 
					 * 4. (다음 결재자로 보낼지 여부) 확인 후 보낼 것이면
					 * 4.1 [다음결재자] 결재중/합의중, 승인(공석의 경우) 처리 >> 문서 해당 결재자에게 보내기
					 * 4.2 [현재결재자]가 루트라인 이거나, [부모라인]에서 [다음결재자]를 찾았으면 - 문서상태 [다음결재자]로 변경
					 * 
					 * 5. 완료 되었을때 - 통보의 경우 - 문서 도착 처리
					 * 
					 * */
					
					// 병렬 여부
					isCurrApvrParal = ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd());
					// [다음결재자] 가 있는지 여부 - 완결처리 할지 확인
					boolean hasNextApvr = false;
					// [다음결재자] 로 보낼 것인지 여부 - 병렬 완료 안되면 안보냄
					boolean passToNextApvr = true;
					// 다음 결재자를 부모라인에서 찾았는지 여부
					boolean findNextApvrAtParentLn = false;
					
					// 현재 결재자 라인의 다음 결재자 목록
					List<ApOngdApvLnDVo> currNextApvrList = null;
					// 부모 라인의 다음 결재자 목록
					List<ApOngdApvLnDVo> parentNextApvrList = null;
					
					/* 
					 * 1.  자신이 병렬이면 - 병렬이 완료 되었는지 보고
					 * 1.1 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
					 * 1.2 (다음 결재자로 보낼지 여부) 확인 - 자신이 병렬이 아니거나 자신이 속한 병렬이 완료 되었으면 - 보냄
					 * */
					
					// 1. 자신이 병렬이면
					if(isCurrApvrParal){
						// 1. 자신이 병렬이면 - 병렬이 완료 되었는지 보고
						//  - 참조 - 개인합의 의 경우 결재라인을 변경하지 못하므로 저장된 root 경로에서 찾음
						ApOngdApvLnDVo notDoneParalApOngdApvLnDVo = ApDocTransUtil.findFirstNotDoneParalAgr(apvLines.getRootLn(), i, false);
						// 1.1 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
						if(notDoneParalApOngdApvLnDVo != null){
							// 문서에 병렬중 완료 안된 결재자 정보 세팅
							setCurApvrAtApOngdBVo(apOngdBVo, notDoneParalApOngdApvLnDVo);
							// 다음 결재할 사람 있음
							hasNextApvr = true;
							// 1.2 (다음 결재자로 보낼지 여부) 확인
							// 다음 결재자에게 보내지 않음
							passToNextApvr = false;
						}
					}
					
					/*
					 * -- 다음 결재자로 보낼 것이면 --
					 * 2.1  다음 결재자 찾음 - 현재 결재선에서
					 * 2.2  다음 결재자 없고 현재 결재선이 서브라인 이면
					 * 2.2.1  부모결재자(합의부서)를 찾아 완료처리함
					 * 2.2.2  부모결재자(합의부서)가 병렬이면 - 완료여부 체크
					 * 2.2.3  부모결재자(합의부서) 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
					 * 2.2.4 (다음 결재자로 보낼지 여부) 확인 후 - 다음 결재자 찾음
					 * */
					
					// 다음 결재자에게 보낼 것이면
					if(passToNextApvr){
						
						// 2.1  다음 결재자 찾음 - 현재 결재선에서
						currNextApvrList = ApDocTransUtil.getNextApvrList(toStoreApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnNo(), false);
						// 2.2  다음 결재자 없고
						if(currNextApvrList==null || currNextApvrList.isEmpty() || "abs".equals(currNextApvrList.get(currNextApvrList.size()-1).getApvrRoleCd())){
							
							// 2.2  결재선이 서브라인 이면
							if(!ApDocUtil.isRootLine(myTurnApOngdApvLnDVo.getApvLnPno())){
								
								// 2.2.1  부모결재자(합의부서)를 찾아 완료처리함
								ApOngdApvLnDVo parentApvr = ApDocTransUtil.findApvLnByApvLnNo(apvLines.getRootLn(), myTurnApOngdApvLnDVo.getApvLnPno());
								if(parentApvr==null){
									LOGGER.error("Fail trans - no parent apv line - apvNo:"+myTurnApOngdApvLnDVo.getApvNo()+"  apvLnPno:"+myTurnApOngdApvLnDVo.getApvLnPno());
									//ap.msg.noParentApvr=부모 결재라인을 확인 할 수 없습니다.
									throw new CmException("ap.msg.noParentApvr", locale);
								}
								
								// 부모 결재라인 완료 처리
								ApOngdApvLnDVo parentToSave = new ApOngdApvLnDVo();
								parentToSave = new ApOngdApvLnDVo();
								parentToSave.setApvNo(parentApvr.getApvNo());
								parentToSave.setApvLnPno(parentApvr.getApvLnPno());
								parentToSave.setApvLnNo(parentApvr.getApvLnNo());
								parentToSave.setPrevApvrApvDt("sysdate");
								
								// 결재자 리소스
								parentToSave.setApvrUid(apOngdApvLnDVo.getApvrUid());// 결재자UID
								// [옵션] 부서합의 최종부서 표기
								if("Y".equals(optConfigMap.get("dispLastAgrDept"))){
									parentToSave.setApvDeptId(apOngdApvLnDVo.getApvDeptId()); // 결재부서ID
								} else {
									parentToSave.setApvDeptId(parentApvr.getApvDeptId());// 결재부서ID
								}
								parentToSave.setApvrDeptYn("Y");//결재자부서여부
								// 결재자 리소스 세팅 + 도장방 타이틀
								setApOngdApvLnDVoResc(parentToSave, docLangTypCd, 
										userCacheMap, orgCacheMap, optConfigMap, locale);
								
								// 결재자 서명 정보
								parentToSave.setSignImgPath(apOngdApvLnDVo.getSignImgPath());//서명이미지경로
								parentToSave.setSignDispVa(apOngdApvLnDVo.getSignDispVa());//서명표시값
								parentToSave.setDtDispVa(apOngdApvLnDVo.getDtDispVa());//일시표시값
								parentToSave.setApvDt(apOngdApvLnDVo.getApvDt());//결재일시
								parentToSave.setApvStatCd(apOngdApvLnDVo.getApvStatCd());//결재상태코드
								
								// 전결, 대결 정보
								parentToSave.setAgntUid(apOngdApvLnDVo.getAgntUid());//대결자UID
								parentToSave.setPredYn(apOngdApvLnDVo.getPredYn());//전결여부
								
								parentToSave.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));	//	결재의견표시여부
								parentToSave.setApvOpinCont(apvOpinPrefix==null ? apvOpinCont : apvOpinPrefix + apvOpinCont);		//	결재의견내용
								
								queryQueue.update(parentToSave);
								
								// 2.2.2  부모결재자(합의부서)가 병렬이면 - 완료여부 체크
								int parentIndex = ApDocTransUtil.findMyIndex(apvLines.getRootLn(), parentApvr.getApvLnNo());
								// 완료되지 않은 병렬합의 찾기
								ApOngdApvLnDVo notDoneParalApOngdApvLnDVo = ApDocTransUtil.findFirstNotDoneParalAgr(apvLines.getRootLn(), parentIndex, false);
								
								// 2.2.3  부모결재자(합의부서) 병렬이 완료 안되었으면 - [병렬중 완료 안된 첫번째 결재자]로 문서상태 표시
								if(notDoneParalApOngdApvLnDVo != null){
									// 문서에 병렬중 완료 안된 결재자 정보 세팅
									setCurApvrAtApOngdBVo(apOngdBVo, notDoneParalApOngdApvLnDVo);
									// 다음 결재할 사람 있음
									hasNextApvr = true;
									// 다음 결재자에게 보내지 않음
									passToNextApvr = false;
									
								// 2.2.4 (다음 결재자로 보낼지 여부) 확인 후 - 다음 결재자 찾음
								} else {
									// 부모라인에서 다음 결재자 찾기
									parentNextApvrList = ApDocTransUtil.getNextApvrList(apvLines.getRootLn(), myTurnApOngdApvLnDVo.getApvLnPno(), false);
									// 유효한 결재자가 없으면
									if(parentNextApvrList==null || parentNextApvrList.isEmpty() || "abs".equals(parentNextApvrList.get(parentNextApvrList.size()-1).getApvrRoleCd())){
										// 다음 결재자 없음
										hasNextApvr = false;
									} else {
										// 다음 결재자 있음
										hasNextApvr = true;
										// 부모라인에서 다음결재자 찾았음
										findNextApvrAtParentLn = true;
									}
								}
							// 2.2-else 결재선이 서브라인 이 아니면
							} else {
								// 다음 결재자 없음
								hasNextApvr = false;
							}
						// 2.2-else 다음 결재자 있음
						} else {
							// 다음 결재자 있음
							hasNextApvr = true;
							// 결재선이 서브라인 이면
							if(!ApDocUtil.isRootLine(myTurnApOngdApvLnDVo.getApvLnPno())){
								hasSubLnNextApvr = true;
							}
						}
						
					}
					
					// 3. 다음결재자 없으면 - 완료처리 - 등록대장
					if(!hasNextApvr){
						
						// 완료 되었음
						isRootCmpl = true;
						boolean makDeptDel = false;//기안부서 삭제 여부
						// 기안자
						ApOngdApvLnDVo makrVo = apvLines.getMakr();
						
						// 기안부서 체크 - 기안부서가 삭제 되었는지 체크함
						OrOrgTreeVo currOrOrgTreeVo = orCmSvc.getOrgTreeVo(makrVo.getApvDeptId(), docLangTypCd);
						// 기안부서가 없으면 - 부서가 삭제됨
						if(currOrOrgTreeVo==null){
							makDeptDel = true;
							// 기안자 정보 조회
							OrUserBVo orUserBVo = new OrUserBVo();
							orUserBVo.setUserUid(makrVo.getApvrUid());
							orUserBVo.setQueryLang(docLangTypCd);
							orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
							
							if(orUserBVo!=null){
								// 기안자의 부서 조회
								currOrOrgTreeVo = orCmSvc.getOrgTreeVo(orUserBVo.getDeptId(), docLangTypCd);
							}
						}
						
						if(ApDocUtil.isRootLine(apOngdApvLnDVo.getApvLnPno())){
							// 기안자 - 조회일시, 전 결재자 처리일시 지우기 - 볼드, 맨 위로 올리기
							makrVo.setPrevApvrApvDt("sysdate");
							makrVo.setVwDt(null);
							
							// 기안부서 삭제되고 기안자의 현재부서가 있을 경우
							if(makDeptDel && currOrOrgTreeVo!=null){
								makrVo.setApvDeptId(currOrOrgTreeVo.getOrgId());
								makrVo.setApvDeptNm(currOrOrgTreeVo.getRescNm());
								makrVo.setApvDeptAbbrNm(currOrOrgTreeVo.getOrgAbbrRescNm());
							}
							
						} else {
							// 기안자 - 조회일시, 전 결재자 처리일시 지우기 - 볼드, 맨 위로 올리기
							ApOngdApvLnDVo makrUpdateVo = new ApOngdApvLnDVo();
							makrUpdateVo.setApvNo(makrVo.getApvNo());
							makrUpdateVo.setApvLnPno(makrVo.getApvLnPno());
							makrUpdateVo.setApvLnNo(makrVo.getApvLnNo());
							makrUpdateVo.setPrevApvrApvDt("sysdate");
							makrUpdateVo.setVwDt("");
							
							// 기안부서 삭제되고 기안자의 현재부서가 있을 경우
							if(makDeptDel && currOrOrgTreeVo!=null){
								makrUpdateVo.setApvDeptId(currOrOrgTreeVo.getOrgId());
								makrUpdateVo.setApvDeptNm(currOrOrgTreeVo.getRescNm());
								makrUpdateVo.setApvDeptAbbrNm(currOrOrgTreeVo.getOrgAbbrRescNm());
							}
							
							queryQueue.update(makrUpdateVo);
						}
						// 기안부서 삭제되고 기안자의 현재부서가 있을 경우
						if(makDeptDel && currOrOrgTreeVo!=null){
							apOngdBVo.setMakDeptId(currOrOrgTreeVo.getOrgId());
							apOngdBVo.setMakDeptNm(currOrOrgTreeVo.getRescNm());
						}
						
						// 문서 완료 처리
						setDocCmpl(apOngdBVo, apOngdApvLnDVo);
						// 문서번호 세팅
						apDocNoSvc.setDocNo(apOngdBVo, storedApOngdApvLnDVoList.get(0), optConfigMap, "regRecLst", locale);
						// 등록대장 - 등록
						registerRecLst(apOngdBVo, queryQueue, model);
						// 메신저 보내기
						addMesseger(apOngdBVo, storedApOngdApvLnDVoList.get(0), userVo, messengerQueue, locale);
						
					/* 
					 * 4. (다음 결재자로 보낼지 여부) 확인 후 보낼 것이면
					 * 4.1 [다음결재자] 결재중/합의중, 승인(공석의 경우) 처리 >> 문서 해당 결재자에게 보내기
					 * 4.2 [현재결재자]가 루트라인 이거나, [부모라인]에서 [다음결재자]를 찾았으면 - 문서상태 [다음결재자]로 변경
					 * */
						
					// 4. (다음 결재자로 보낼지 여부) 확인 후 보낼 것이면
					} else if(passToNextApvr){
						
						// 4.1 [다음결재자] 결재중/합의중, 승인(공석의 경우) 처리 >> 문서 해당 결재자에게 보내기
						
						// 나와 같은 결재라인의 다음 결재자들은 맵에 세팅함 - 루프 돌면서 해당것 INSERT 할때 문서 도착 처리
						if(currNextApvrList != null && !currNextApvrList.isEmpty()){
							nextTurnMap = toMap(currNextApvrList);
						}
						
						ApOngdApvLnDVo toSave;
						// 부모라인의 다음 결재자들은 - 문서 도착함 UPDATE
						if(parentNextApvrList != null && !parentNextApvrList.isEmpty()){
							for(ApOngdApvLnDVo parentApOngdApvLnDVo : parentNextApvrList){
								apvrRoleCd = parentApOngdApvLnDVo.getApvrRoleCd();
								
								toSave = new ApOngdApvLnDVo();
								toSave.setApvNo(apvNo);
								toSave.setApvLnPno("0");
								toSave.setApvLnNo(parentApOngdApvLnDVo.getApvLnNo());
								toSave.setPrevApvrApvDt("sysdate");
								toSave.setVwDt("");
								
								if("abs".equals(apvrRoleCd)){
									toSave.setApvDt("sysdate");
									toSave.setApvStatCd("apvd");//apvd:승인
								} else {
									if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd)){
										toSave.setApvStatCd("inAgr");//inAgr:합의중
										parentApOngdApvLnDVo.setApvStatCd("inAgr");//inAgr:합의중 - 알림 작성용
									} else {
										toSave.setApvStatCd("inApv");//inApv:결재중
										parentApOngdApvLnDVo.setApvStatCd("inApv");//inApv:결재중 - 알림 작성용
									}
									// 메신저 보내기
									addMesseger(apOngdBVo, parentApOngdApvLnDVo, userVo, messengerQueue, locale);
								}
								queryQueue.update(toSave);
							}
						}
						
						// 4.2 [현재결재자]가 루트라인 이거나, [부모라인]에서 [다음결재자]를 찾았으면 - 문서상태 [다음결재자]로 변경
						if(ApDocUtil.isRootLine(myTurnApOngdApvLnDVo.getApvLnPno()) || findNextApvrAtParentLn){
							
							// [다음결재자]로 변경 - 여부
							boolean setDocToNextApvr = false;
							// 현재 라인에서 다음 결재자 있는지 검사
							if(currNextApvrList != null && !currNextApvrList.isEmpty()){
								for(ApOngdApvLnDVo nextApOngdApvLnDVo : currNextApvrList){
									// 공석이 아니면
									if(!"abs".equals(nextApOngdApvLnDVo.getApvrRoleCd())){
										setDocToNextApvr = true;
										setCurApvrAtApOngdBVo(apOngdBVo, nextApOngdApvLnDVo);// 문서에 현재 결재자 정보 세팅
										break;
									}
								}
							}
							// 부모 라인에서 다음 결재자 있는지 검사
							if(!setDocToNextApvr && parentNextApvrList != null && !parentNextApvrList.isEmpty()){
								for(ApOngdApvLnDVo nextApOngdApvLnDVo : parentNextApvrList){
									// 공석이 아니면
									if(!"abs".equals(nextApOngdApvLnDVo.getApvrRoleCd())){
										setDocToNextApvr = true;
										setCurApvrAtApOngdBVo(apOngdBVo, nextApOngdApvLnDVo);// 문서에 현재 결재자 정보 세팅
										break;
									}
								}
							}
							
							if(!setDocToNextApvr){
								LOGGER.error("Fail trans - no next apvr - apvNo:"+myTurnApOngdApvLnDVo.getApvNo()+"  apvLnPno:"+myTurnApOngdApvLnDVo.getApvLnPno()+"  apvLnNo:"+myTurnApOngdApvLnDVo.getApvLnNo());
								// ap.msg.noNextApvr=다음 결재자 정보를 확인 할 수 없습니다.
								throw new CmException("ap.msg.noNextApvr", locale);
							}
						}
					}
					
				}
				
				// inApv:결재중, inAgr:합의중
				if("inApv".equals(apOngdApvLnDVo.getApvStatCd()) || "inAgr".equals(apOngdApvLnDVo.getApvStatCd())){
					// 메신저 보내기
					addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
				}
				
				apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
				queryQueue.insert(apOngdApvLnDVo);
			}
		}
		
		// 부서합의 목록으로 부서합의의 서브경로 맞춤
		if(!isRootCmpl && deptAgrList!=null && !deptAgrList.isEmpty()){
			apvLines.transDeptAgrLn(queryQueue, deptAgrList);
		}
		
		queryQueue.update(myTurnApOngdApvLnDVoToSave);
		
		// 부서합의 의 위임의 경우 - 후열함에 보이게
		if(isRootCmpl){
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			apOngdApvLnDVo.setApvrRoleCdList(ApCmUtil.toList("entu"));//entu:위임
			apOngdApvLnDVo.setWhereSqllet("AND APV_LN_PNO != 0");
			apOngdApvLnDVo.setApvStatCd("inInfm");//inInfm:통보중
			queryQueue.update(apOngdApvLnDVo);
		}
		
		// [옵션] 의견 메일 보내기
		if("Y".equals(optConfigMap.get("mailOpin"))){
			if(hasOpin && !hasSubLnNextApvr){
				model.put("needOpinMail", Boolean.TRUE);
			}
		}
	}
	
	/** 문서 완료 처리 */
	private void setDocCmpl(ApOngdBVo apOngdBVo, ApOngdApvLnDVo apOngdApvLnDVo) throws SQLException{
		
		apOngdBVo.setDocStatCd("apvd");//문서상태코드
		apOngdBVo.setDocProsStatCd("apvd");//문서승인상태코드
		apOngdBVo.setCmplDt("sysdate");//완결일시
		apOngdBVo.setCmplrUid(apOngdApvLnDVo.getApvrUid());//완결자UID
		apOngdBVo.setCmplrNm(apOngdApvLnDVo.getApvrNm());//완결자명
		apOngdBVo.setCurApvrId("");//현재결재자ID
		apOngdBVo.setCurApvrNm("");//현재결재자명
		apOngdBVo.setCurApvrDeptYn("");//현재결재자부서여부
		
		// 시행상태코드 - 세팅
		if("intro".equals(apOngdBVo.getDocTypCd())){//intro:내부문서
			apOngdBVo.setEnfcStatCd("apvd");//시행상태코드 - apvd:승인
		} else if("extro".equals(apOngdBVo.getDocTypCd())){//extro:시행문서
			apOngdBVo.setEnfcStatCd("befoEnfc");//시행상태코드 - befoEnfc:시행대기
		}
	}
	
	/** 문서 완료 처리 */
	private void registerRecLst(ApOngdBVo apOngdBVo, QueryQueue queryQueue, ModelMap model) throws SQLException{
		
		ApOngdBVo storedApOngdBVo = (ApOngdBVo)model.get("storedApOngdBVo");
		if(storedApOngdBVo == null) storedApOngdBVo = apOngdBVo;
		
		// 등록대장 등록
		apOngdBVo.setRecLstDeptId(storedApOngdBVo.getMakDeptId());
		apOngdBVo.setRecLstTypCd("regRecLst");//대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		
		// 분류정보 삭제 방지 처리
		String clsInfoId = storedApOngdBVo.getClsInfoId();
		if(clsInfoId != null && !clsInfoId.isEmpty()){
			// 분류정보상세(AP_CLS_INFO_D) 테이블
			ApClsInfoDVo storedApClsInfoDVo = new ApClsInfoDVo();
			storedApClsInfoDVo.setOrgId(storedApOngdBVo.getMakDeptId());
			storedApClsInfoDVo.setClsInfoId(clsInfoId);
			storedApClsInfoDVo = (ApClsInfoDVo)commonDao.queryVo(storedApClsInfoDVo);
			
			if(storedApClsInfoDVo != null && !"Y".equals(storedApClsInfoDVo.getSysClsInfoYn())){
				storedApClsInfoDVo = new ApClsInfoDVo();
				storedApClsInfoDVo.setOrgId(storedApOngdBVo.getMakDeptId());
				storedApClsInfoDVo.setClsInfoId(clsInfoId);
				storedApClsInfoDVo.setSysClsInfoYn("Y");//시스템분류정보여부 - 완결된 문서의 분류정보를 Y로 바꾸어 삭제되지 않도록 함
				queryQueue.update(storedApClsInfoDVo);
			}
		}

		// 보안등급 - 설정 되어 있으면 삭제되지 않도록 함
		String seculCd = storedApOngdBVo.getSeculCd();
		if(seculCd!=null && !seculCd.isEmpty() && !"none".equals(seculCd)){
			PtCdBVo ptCdBVo = ptCmSvc.getCd("SECUL_CD", storedApOngdBVo.getDocLangTypCd(), seculCd);
			if(ptCdBVo!=null && !"Y".equals(ptCdBVo.getSysCdYn())){
				ptCdBVo = new PtCdBVo();
				ptCdBVo.setClsCd("SECUL_CD");
				ptCdBVo.setCd(seculCd);
				ptCdBVo.setSysCdYn("Y");
				queryQueue.update(ptCdBVo);
			}
		}
	}
	
	/** 의견과 조회일 등의 결재선 변경 때에도 유지해야 할 정보를 저장한 맵 리턴 - apvrUid: null 이면 전부 null 이 아니면 해당 결재자 뒤만 */
	private Map<Integer, ApOngdApvLnDVo> toOpinMap(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvrUid){
		
		String opin, vwDt, key;
		boolean afterMyTurn = false;
		// 이미 저장된 결재라인 결재자의 의견을 담을 맵 - 재검토 후 상신할때 재검토 의견 유지를 위한것
		Map<Integer, ApOngdApvLnDVo> opinMap = new HashMap<Integer, ApOngdApvLnDVo>();
		
		if(apOngdApvLnDVoList!=null){
			
			for(ApOngdApvLnDVo apOngdApvLnDVo : apOngdApvLnDVoList){
				if(apvrUid!=null && !afterMyTurn){
					if(apvrUid.equals(apOngdApvLnDVo.getApvrUid())){
						afterMyTurn = true;
					}
				} else {
					key = "Y".equals(apOngdApvLnDVo.getApvrDeptYn())
							? apOngdApvLnDVo.getApvDeptId()
							: apOngdApvLnDVo.getApvrUid();
					if(key!=null && !key.isEmpty()){
						// 의견이 있으면 의견 해쉬에 담음(백업)
						opin = apOngdApvLnDVo.getApvOpinCont();
						vwDt = apOngdApvLnDVo.getVwDt();
						if(apvrUid==null){//조회일시 제거 - 전체 경로의 의견을 담을 때는 합의부서의 의견 남기는 용도로 재검토로 취소된 라인임
							if(vwDt!=null && !vwDt.isEmpty()){
								vwDt = null;
								apOngdApvLnDVo.setVwDt(null);
							}
						}
						if( (opin!=null && !opin.isEmpty()) || (vwDt!=null && !vwDt.isEmpty()) ){
							opinMap.put(Hash.hashId(key), apOngdApvLnDVo);
						}
					}
				}
			}
		}
		
		return opinMap.isEmpty() ? null : opinMap;
	}
	/** 의견/조회일시 등 참조 목적으로 백업된 결재라인VO(ApOngdApvLnDVo) 리턴 */
	private ApOngdApvLnDVo getFromMap(Map<Integer, ApOngdApvLnDVo> opinBackupMap, ApOngdApvLnDVo apOngdApvLnDVo){
		if(opinBackupMap==null) return null;
		String key = "Y".equals(apOngdApvLnDVo.getApvrDeptYn())
				? apOngdApvLnDVo.getApvDeptId()
				: apOngdApvLnDVo.getApvrUid();
		if(key!=null && !key.isEmpty()){
			return opinBackupMap.get(Hash.hashId(key));
		}
		return null;
	}
	/** 맵으로 전환 */
	private Map<Integer, ApOngdApvLnDVo> toMap(List<ApOngdApvLnDVo> apOngdApvLnDVoList){
		if(apOngdApvLnDVoList==null || apOngdApvLnDVoList.isEmpty()) return null;
		// 리턴맵
		Map<Integer, ApOngdApvLnDVo> returnMap = new HashMap<Integer, ApOngdApvLnDVo>();
		String key;
			
		for(ApOngdApvLnDVo apOngdApvLnDVo : apOngdApvLnDVoList){
			key = "Y".equals(apOngdApvLnDVo.getApvrDeptYn())
					? apOngdApvLnDVo.getApvDeptId()
					: apOngdApvLnDVo.getApvrUid();
			if(key!=null && !key.isEmpty()){
				returnMap.put(Hash.hashId(key), apOngdApvLnDVo);
			}
		}
		return returnMap;
	}
	
	/** 결재라인 저장 - 반려, 재검토 */
	private void processRejtApvLn(List<ApOngdApvLnDVo> paramApOngdApvLnDVoList, ApvLines apvLines,
			Map<String, String> paramMap, ApOngdBVo apOngdBVo,
			Locale locale, QueryQueue queryQueue, List<Map<String, String>> messengerQueue, Map<String, String> optConfigMap, ModelMap model, String docLangTypCd,
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, UserVo userVo,
			String statCd) throws SQLException, CmException{
		
		// 의견 / 의견표시여부
		myTurnApOngdApvLnDVoToSave.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));	//	결재의견표시여부
		String apvOpinCont = paramMap.get("apvOpinCont");
		if(apvOpinCont!=null){
			
			if("reRevw".equals(statCd) || "rejt".equals(statCd)){
				// ap.msg.prefix.reRevw=[재검토요청]
				String reviewPrefix = messageProperties.getMessage("ap.msg.prefix.reRevw", locale);
				String reviewStart = reviewPrefix.substring(0, reviewPrefix.length()-1);
				
				if(apvOpinCont.startsWith(reviewStart)){
					int p = apvOpinCont.indexOf(']');
					if(p>0) apvOpinCont = apvOpinCont.substring(p+1).trim();
				}
				if("reRevw".equals(statCd)){
					apvOpinCont = reviewStart+":"+StringUtil.getCurrDateTime()+"] "+apvOpinCont;
				} else if("rejt".equals(statCd)){
					// [반려]
					String prefix = "["+ptSysSvc.getTerm("ap.term.rejt", docLangTypCd)+"] ";
					if(!apvOpinCont.startsWith(prefix)){
						apvOpinCont = prefix+apvOpinCont;
					}
				}
				
				// [옵션] 의견 메일 보내기
				if("Y".equals(optConfigMap.get("mailOpin"))){
					if(apvOpinCont!=null && !apvOpinCont.isEmpty()){
						model.put("needOpinMail", Boolean.TRUE);
					}
				}
			}
		}
		myTurnApOngdApvLnDVoToSave.setApvOpinCont(apvOpinCont);	//	결재의견내용
		myTurnApOngdApvLnDVoToSave.setApvStatCd(statCd);		//	reRevw:재검토, rejt:보류
		if(myTurnApOngdApvLnDVo.getVwDt()==null || myTurnApOngdApvLnDVo.getVwDt().isEmpty()){
			myTurnApOngdApvLnDVoToSave.setVwDt("sysdate");		//	조회일시
		}
		
		// 보류결재라인이력번호
		String holdApvLnHstNo = myTurnApOngdApvLnDVo.getHoldApvLnHstNo();
		
		// 보류된 정보 있으면 - 삭제
		if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
			// 보류 결재라인 삭제
			deleteApvLn(myTurnApOngdApvLnDVo.getApvNo(), myTurnApOngdApvLnDVo.getApvLnPno(), holdApvLnHstNo, queryQueue);
			// 보류결재라인이력번호 - 삭제
			myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
		}
		
		// 재검토 면
		if("reRevw".equals(statCd)){

			// 합의기안 - 담당자 지정전에 [부서대기함]에서 재검토 넘어올 경우
			boolean atMakAgr = "Y".equals(paramMap.get("atMakAgr"));
			
			// 이전 결재자 정보
			ApOngdApvLnDVo prevApOngdApvLnDVo = null;
			boolean isRootLn = ApDocUtil.isRootLine(myTurnApOngdApvLnDVo.getApvLnPno());
			boolean prevAtRootLine = false;

			if(atMakAgr){
				prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(apvLines.getRootLn(), myTurnApOngdApvLnDVo.getApvLnPno());
				prevAtRootLine = true;
			} else {
				prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(apvLines.getCurrLn(), myTurnApOngdApvLnDVo.getApvLnNo());
				if(prevApOngdApvLnDVo == null && !isRootLn){
					prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(apvLines.getRootLn(), myTurnApOngdApvLnDVo.getApvLnPno());
					prevAtRootLine = true;
				}
			}
			
			if(prevApOngdApvLnDVo == null){
				LOGGER.error("Fail trans - no prev apvr ! : reRevw - apvNo:"+myTurnApOngdApvLnDVo.getApvNo());
				//ap.trans.notFindPrevApvr=이전 결재자 정보를 찾지 못하였습니다.
				throw new CmException("ap.trans.notFindPrevApvr", locale);
			}
			// 기안자에게 돌아간 것인지 여부
			if("mak".equals(prevApOngdApvLnDVo.getApvrRoleCd())){
				model.put("reReviewToMak", Boolean.TRUE);
			}
			
			if(ApDocUtil.isAgrOfApvrRole(prevApOngdApvLnDVo.getApvrRoleCd())){
				// [옵션] 이전 합의자로 재검토 사용
				boolean isReRevwToPrevAgr = "Y".equals(optConfigMap.get("reRevwToPrevAgr"));
				//psnOrdrdAgr:개인순차합의
				if(!(isReRevwToPrevAgr && "psnOrdrdAgr".equals(prevApOngdApvLnDVo.getApvrRoleCd()))){
					LOGGER.error("Fail trans - can not reRevw to Agr ! : reRevw - apvNo:"+myTurnApOngdApvLnDVo.getApvNo());
					// ap.trans.noReRevwToAgr=합의로는 재검토 요청 할 수 없습니다.
					throw new CmException("ap.trans.noReRevwToAgr", locale);
				}
			}
			
			// 부서대기함에서 재검토 시킨것 - 이 아니면 - 부서대기함에서 재검토시 현재 작업자의 정보는 결재라인에 없으므로 부서 정보에만 세팅함
			if(!atMakAgr){
				// 해당 결재 라인
				if(ApDocUtil.isAgrOfApvrRole(myTurnApOngdApvLnDVo.getApvrRoleCd())){
					myTurnApOngdApvLnDVoToSave.setApvStatCd("befoAgr");//befoAgr:합의전
				} else {
					myTurnApOngdApvLnDVoToSave.setApvStatCd("befoApv");//befoApv:결재전
				}
				//의견에 일시 보여줘서 일시 삭제
				myTurnApOngdApvLnDVoToSave.setApvDt("");//결재일시
				//myTurnApOngdApvLnDVoToSave.setApvDt("sysdate");//결재일시
				
				queryQueue.update(myTurnApOngdApvLnDVoToSave);
			}
			
			// 이전자 결재자 - 저장용
			ApOngdApvLnDVo prevToSave = new ApOngdApvLnDVo();
			prevToSave.setApvNo(prevApOngdApvLnDVo.getApvNo());
			prevToSave.setApvLnPno(prevApOngdApvLnDVo.getApvLnPno());
			prevToSave.setApvLnNo(prevApOngdApvLnDVo.getApvLnNo());
			
			// 이전자 결재자
			prevToSave.setApvStatCd("reRevw");//reRevw:재검토
			prevToSave.setVwDt("");//조회일시 - 볼드체 표시
			prevToSave.setPrevApvrApvDt("sysdate");//이전결재자결재일시 - 정렬순서 관련
			
			// 이전자 결재자 - 이전자의 서명 정보
			prevToSave.setSignDispVa("");// 서명표시명
			prevToSave.setSignImgPath("");// 서명이미지경로
			prevToSave.setDtDispVa("");// 도장방 날짜표시
			prevToSave.setApvDt("");//결재일시
			
			queryQueue.update(prevToSave);
			// 메신저 보내기
			prevApOngdApvLnDVo.setApvStatCd("reRevw");//reRevw:재검토
			addMesseger(apOngdBVo, prevApOngdApvLnDVo, userVo, messengerQueue, locale);
			
			//상위 결재라인에서 이전 결재자를 찾았을 경우
			if(prevAtRootLine){
				
				// [현재결재자]의 [부모 결재라인](부서)를 찾아서 의견 및 재검토자 세팅
				ApOngdApvLnDVo parentApOngdApvLnDVo = ApDocTransUtil.findApvLnByApvLnNo(
						apvLines.getRootLn(), myTurnApOngdApvLnDVo.getApvLnPno());
				
				if(parentApOngdApvLnDVo == null){
					LOGGER.error("Fail trans - reRevw : no parent apvr - apvNo:"+myTurnApOngdApvLnDVo.getApvNo()+"  apvLnNo:"+myTurnApOngdApvLnDVo.getApvLnPno()+"  userUid:"+userVo.getUserUid());
					//ap.msg.noParentApvr=부모 결재라인을 확인 할 수 없습니다.
					throw new CmException("ap.msg.noParentApvr", locale);
				}
				
				// 저장할 [부모 결재라인](부서) 정보
				ApOngdApvLnDVo parentToSave = new ApOngdApvLnDVo();
				parentToSave.setApvNo(parentApOngdApvLnDVo.getApvNo());
				parentToSave.setApvLnPno(parentApOngdApvLnDVo.getApvLnPno());
				parentToSave.setApvLnNo(parentApOngdApvLnDVo.getApvLnNo());
				parentToSave.setApvStatCd("befoApv");//결재상태코드 - befoApv:결재전
				
				// 결재자UID
				parentToSave.setApvrUid(userVo.getUserUid());
				// 결재부서ID
				parentToSave.setApvDeptId(parentApOngdApvLnDVo.getApvDeptId());
				// 결재자부서여부
				parentToSave.setApvrDeptYn("Y");
				
				// 결재자 리소스 세팅 + 도장방 타이틀
				setApOngdApvLnDVoResc(parentToSave, docLangTypCd, 
						null, null, optConfigMap, locale);
				
				parentToSave.setApvDt("sysdate");//결재일시
				
				parentToSave.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));	//	결재의견표시여부
				parentToSave.setApvOpinCont(apvOpinCont);	//	결재의견내용
				
				queryQueue.update(parentToSave);
			}
			
			// 보류의견 삭제
			ApOngdHoldOpinDVo apOngdHoldOpinDVo = new ApOngdHoldOpinDVo();
			apOngdHoldOpinDVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			apOngdHoldOpinDVo.setApvrUid(myTurnApOngdApvLnDVo.getApvrUid());
			queryQueue.delete(apOngdHoldOpinDVo);
			
			// 문서
			apOngdBVo.setDocStatCd("reRevw");//reRevw:재검토
			if(isRootLn || prevAtRootLine){
				setCurApvrAtApOngdBVo(apOngdBVo, prevApOngdApvLnDVo);//문서에 현재 결재자 정보 세팅
			}
			
		// 반려 면
		} else if("rejt".equals(statCd)){
			
			// 해당 결재 라인
			myTurnApOngdApvLnDVoToSave.setApvStatCd("rejt");//rejt:반려
			myTurnApOngdApvLnDVoToSave.setSignDispVa(ptSysSvc.getTerm("ap.term.rejt", docLangTypCd));// 서명표시명 - 반려
			myTurnApOngdApvLnDVoToSave.setApvDt("sysdate");//결재일시
			
			/////////////////////////////////
			// [도장방 날짜]
			String signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, myTurnApOngdApvLnDVo, docLangTypCd, true);
			myTurnApOngdApvLnDVoToSave.setDtDispVa(signAreaDtDisp);
//			// 도장방 날짜표시 - 표시 방법 조회
//			String signAreaDtDisp = optConfigMap.get("signAreaDt");
//			if(signAreaDtDisp==null || signAreaDtDisp.isEmpty()) signAreaDtDisp = ApConstant.DFT_SIGN_AREA_DT;
//			SimpleDateFormat signDtFormat = new SimpleDateFormat(signAreaDtDisp);
//			signAreaDtDisp = signDtFormat.format(new Date(System.currentTimeMillis()));
//			
//			// nameAtDateSignAreaEnable=날짜 표시란에 이름 표시
//			if("Y".equals(optConfigMap.get("nameAtDateSignAreaEnable"))){
//				signAreaDtDisp = signAreaDtDisp+"<br/>"+myTurnApOngdApvLnDVo.getApvrNm();
//			}
//			// 도장방 날짜표시 - 세팅
//			myTurnApOngdApvLnDVoToSave.setDtDispVa(signAreaDtDisp);
			
			queryQueue.update(myTurnApOngdApvLnDVoToSave);
			
			// 기안자 - 읽음정보 삭제
			ApOngdApvLnDVo makApOngdApvLnDVo = new ApOngdApvLnDVo();
			makApOngdApvLnDVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			makApOngdApvLnDVo.setApvLnNo("1");
			makApOngdApvLnDVo.setVwDt("");// 읽음 정보 삭제
			makApOngdApvLnDVo.setPrevApvrApvDt("sysdate");
			queryQueue.update(makApOngdApvLnDVo);
			
			// 문서
			apOngdBVo.setDocStatCd("rejt");//rejt:반려
			apOngdBVo.setDocProsStatCd("rejt");//rejt:반려
			apOngdBVo.setEnfcStatCd("rejt");//rejt:반려
			apOngdBVo.setCmplDt("sysdate");
			apOngdBVo.setCmplrUid(myTurnApOngdApvLnDVo.getApvrUid());
			apOngdBVo.setCmplrNm(myTurnApOngdApvLnDVo.getApvrNm());
			setCurApvrAtApOngdBVo(apOngdBVo, null);//문서에 현재 결재자 정보 지움
			
			//[옵션] keepRejtDoc=반려된 문서 등록대장에 저장
			if("Y".equals(optConfigMap.get("keepRejtDoc"))){
				// 문서번호 세팅
				apDocNoSvc.setDocNo(apOngdBVo, apvLines.getMakr(), optConfigMap, "regRecLst", locale);
				// 등록대장 - 등록
				registerRecLst(apOngdBVo, queryQueue, model);
			}
			// 메신저 보내기
			addMesseger(apOngdBVo, apvLines.getMakr(), userVo, messengerQueue, locale);
			
//			// [옵션] 의견 메일 보내기
//			if("Y".equals(optConfigMap.get("mailOpin"))){
//				if(apvOpinCont!=null && !apvOpinCont.isEmpty()){
//					model.put("needOpinMail", Boolean.TRUE);
//				}
//			}
		}
	}
	
	/** 문서에 현재 결재자 정보 세팅 */
	private void setCurApvrAtApOngdBVo(ApOngdBVo apOngdBVo, ApOngdApvLnDVo apOngdApvLnDVo){
		// 현재 결재자 정보 세팅
		if(apOngdApvLnDVo==null){
			apOngdBVo.setCurApvrId("");//현재결재자ID
			apOngdBVo.setCurApvrNm("");//현재결재자명
			apOngdBVo.setCurApvrDeptYn("");//현재결재자부서여부
		} else if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){//결재자부서여부
			apOngdBVo.setCurApvrId(apOngdApvLnDVo.getApvDeptId());//현재결재자ID
			apOngdBVo.setCurApvrNm(apOngdApvLnDVo.getApvDeptNm());//현재결재자명
			apOngdBVo.setCurApvrDeptYn("Y");//현재결재자부서여부
			apOngdBVo.setDocStatCd(apOngdApvLnDVo.getApvrRoleCd());//문서상태코드
		} else {
			apOngdBVo.setCurApvrId(apOngdApvLnDVo.getApvrUid());//현재결재자ID
			apOngdBVo.setCurApvrNm(apOngdApvLnDVo.getApvrNm());//현재결재자명
			apOngdBVo.setCurApvrDeptYn("N");//현재결재자부서여부
			apOngdBVo.setDocStatCd(apOngdApvLnDVo.getApvrRoleCd());//문서상태코드
		}
	}
	
	/** 결재라인 저장 - 보류 */
	private void processHoldApvLn(List<ApOngdApvLnDVo> paramApOngdApvLnDVoList, ApvLines apvLines, 
			Map<String, String> paramMap, ApOngdBVo apOngdBVo,
			UserVo userVo, Map<Integer, OrUserBVo> userCacheMap, Map<Integer, OrOrgBVo> orgCacheMap,
			Locale locale, QueryQueue queryQueue, Map<String, String> optConfigMap,
			String apvLnPno, ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, String statCd) throws SQLException, CmException{
		
		// 결재의견내용
		String apvOpinCont = paramMap.get("apvOpinCont");
		if(apvOpinCont != null && myTurnApOngdApvLnDVo.getApvrUid() != null && !myTurnApOngdApvLnDVo.getApvrUid().isEmpty()){
			// 진행문서보류의견상세 - 저장
			ApOngdHoldOpinDVo apOngdHoldOpinDVo = new ApOngdHoldOpinDVo();
			apOngdHoldOpinDVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
			apOngdHoldOpinDVo.setApvrUid(myTurnApOngdApvLnDVo.getApvrUid());
			apOngdHoldOpinDVo.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));	//	결재의견표시여부
			apOngdHoldOpinDVo.setApvOpinCont(apvOpinCont);		//	결재의견내용
			queryQueue.store(apOngdHoldOpinDVo);
		}
		
		if(myTurnApOngdApvLnDVo.getVwDt()==null || myTurnApOngdApvLnDVo.getVwDt().isEmpty()){
			myTurnApOngdApvLnDVoToSave.setVwDt("sysdate");							//	조회일시
		} else {
			myTurnApOngdApvLnDVoToSave.setVwDt(myTurnApOngdApvLnDVo.getVwDt());
		}
		
		// 해당 결재상태 보류로 바꿈 - 보류때 본문에 보류내용 표시
		if(!"reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd())//reRevw:재검토
				&& !"cncl".equals(myTurnApOngdApvLnDVo.getApvStatCd())){//cncl:취소
			myTurnApOngdApvLnDVoToSave.setApvStatCd("hold");
		}
		
		// 변경사항 없음 - 의견만 update
		if(paramApOngdApvLnDVoList==null){
			queryQueue.update(myTurnApOngdApvLnDVoToSave);
			return;
		}
		
		// 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블
		ApOngdApvLnDVo apOngdApvLnDVo, opinApOngdApvLnDVo;
		
		String apvNo = apOngdBVo.getApvNo();
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		
		// 보류결재라인이력번호
		String holdApvLnHstNo = myTurnApOngdApvLnDVo.getHoldApvLnHstNo();
		
		// 저장된(보류전 원본 결재 라인) 결재라인 조회
		List<ApOngdApvLnDVo> storedApOngdApvLnDVoList = apvLines.getCurrLn();
		
		// 저장된(보류전 원본 결재 라인) 결재라인 과 전송된 결재라인 이 동일하면
		if(isIdenticalApvLn(paramApOngdApvLnDVoList, storedApOngdApvLnDVoList)){
			
			// 보류결재라인이력번호 - 가 있으면
			if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
				
				// 보류결재라인 데이터 삭제
				deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
				
				// 보류결재라인이력번호 - 삭제
				myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo("");
				
			}
			
		// 저장된 결재라인 과 전송된 결재라인 이 다르면
		} else {
			
			// 보류결재라인이력번호 - 가 있으면
			if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
				
				// 보류결재라인 데이터 삭제
				deleteApvLn(apvNo, apvLnPno, holdApvLnHstNo, queryQueue);
				
			} else {
				
				// 보류참조문서이력번호 - 생성
				holdApvLnHstNo = getNextApvLnHstNo(apvNo, apvLnPno);
				
				// 보류결재라인이력번호 - 세팅
				myTurnApOngdApvLnDVoToSave.setHoldApvLnHstNo(holdApvLnHstNo);
			}
			// 전송된 내역 입력
			
			// 이전 결재라인에서 의견을 백업할 맵
			Map<Integer, ApOngdApvLnDVo> opinBackupMap = new HashMap<Integer, ApOngdApvLnDVo>();
			
			// 내 결재라인 정보
			String myApvLnNo = myTurnApOngdApvLnDVo.getApvLnNo();
			
			// 내차례 이전까지 - 원본 데이터 이용 생성
			storeApvLnBeforeMyTurn(storedApOngdApvLnDVoList, 
					myApvLnNo, holdApvLnHstNo, true,
					opinBackupMap, queryQueue);
			
			// 내차례와 내차례 뒤의 결재라인 - submit 된 데이터로 입력
			boolean afterMyTurn = false;
			String apvrRoleCd;
			int i, size = paramApOngdApvLnDVoList.size();
			
			for(i=0; i<size; i++){
				
				apOngdApvLnDVo = paramApOngdApvLnDVoList.get(i);
				if(afterMyTurn || myApvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
					
					// 사용자UID 가 없으면 - 부서로 세팅
					if(apOngdApvLnDVo.getApvrUid()==null || apOngdApvLnDVo.getApvrUid().isEmpty()){
						apOngdApvLnDVo.setApvrDeptYn("Y");
					} else {
						apOngdApvLnDVo.setApvrDeptYn("N");
					}
					
					// 결재선 데이터에 문서언어에 따른 리스스 바인딩
					setApOngdApvLnDVoResc(apOngdApvLnDVo, docLangTypCd, 
							userCacheMap, orgCacheMap, optConfigMap, locale);
					
					apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
					
					// 내 차례 뒤 이면
					if(afterMyTurn){
						// 맵에서 의견 복원 << // 의견이 있으면 의견 해쉬에 담음(백업)
						opinApOngdApvLnDVo = getFromMap(opinBackupMap, apOngdApvLnDVo);
						if(opinApOngdApvLnDVo != null){
							// 의견
							if(ApDocUtil.hasOpin(apvrRoleCd)
									&& opinApOngdApvLnDVo.getApvOpinCont()!=null && !opinApOngdApvLnDVo.getApvOpinCont().isEmpty()){
								apOngdApvLnDVo.setApvOpinCont(opinApOngdApvLnDVo.getApvOpinCont());
								apOngdApvLnDVo.setApvOpinDispYn(opinApOngdApvLnDVo.getApvOpinDispYn());
							}
							// 조회일시
							if(opinApOngdApvLnDVo.getVwDt()!=null && !opinApOngdApvLnDVo.getVwDt().isEmpty()){
								apOngdApvLnDVo.setVwDt(opinApOngdApvLnDVo.getVwDt());
							}
						}
						
						// 부서 역할 인지 체크 - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서
						if(ApDocUtil.isDeptRole(apvrRoleCd)){
							apOngdApvLnDVo.setPichApntYn("N");//담당자지정여부
						}
						
						////////////////////////////////////////////
						//  보류할때 - 합의자는 결재라인을 바꾸지 못하므로 이전 결재자는 합의자가 아닌걸로 간주함
						
						if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd)){
							apOngdApvLnDVo.setApvStatCd("befoAgr");//befoAgr:합의전
						} else {
							apOngdApvLnDVo.setApvStatCd("befoApv");//befoApv:결재전
						}
						
					// 내 차례 이면
					} else {
						afterMyTurn = true;
						
						if("reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd())//reRevw:재검토
								|| "cncl".equals(myTurnApOngdApvLnDVo.getApvStatCd())){//cncl:취소
							apOngdApvLnDVo.setApvStatCd(myTurnApOngdApvLnDVo.getApvStatCd());
						} else {
							apOngdApvLnDVo.setApvStatCd("hold");
						}
					}
					
					apOngdApvLnDVo.setApvLnHstNo(holdApvLnHstNo);
					apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
					queryQueue.insert(apOngdApvLnDVo);
				}
			}
		}
		
		queryQueue.update(myTurnApOngdApvLnDVoToSave);
	}
	
	/** 결재라인 이력을 저장할 다음 결재 이력번호 생성 */
	private String getNextApvLnHstNo(String apvNo, String apvLnPno) throws SQLException {
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.selectMaxHstNo");
		Integer maxNo = commonDao.queryInt(apOngdApvLnDVo);
		maxNo = maxNo==null ? 2 : maxNo+2;
		return maxNo.toString();
	}
	
	/** 결재라인 삭제 */
	private void deleteApvLn(String apvNo, String apvLnPno, String apvLnHstNo, QueryQueue queryQueue){
		// 보류결재라인 데이터 삭제
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		if(apvLnHstNo != null && !apvLnHstNo.isEmpty()){
			apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
			apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
		}
		queryQueue.delete(apOngdApvLnDVo);
	}
	
	/** 나 이전까지의 결재 라인을 QueryQueue에 저장하고 나 이후는 의견이 있을 경우 Map에 저장함, 나의 읽은일시 리턴 */
	private void storeApvLnBeforeMyTurn(List<ApOngdApvLnDVo> storedApOngdApvLnDVoList,
			String myApvLnNo, String apvLnHstNo, boolean history,
			Map<Integer, ApOngdApvLnDVo> opinMap, 
			QueryQueue queryQueue){

		// 내차례 전까지 - 저장되어 있는 데이터 그대로 입력 - 히스토리 테이블
		if(storedApOngdApvLnDVoList!=null){
			
			boolean afterMyTurn = false;
			String opin, vwDt, key;
			
			for(ApOngdApvLnDVo apOngdApvLnDVo : storedApOngdApvLnDVoList){
				
				if(!afterMyTurn){
					
					// 내 차례
					if(myApvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
						afterMyTurn = true;
					} else {
						if(apvLnHstNo!=null) apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
						if(history){
							apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
						}
						// 저장 - 이전에 결재 한것 & 내차례 전에것
						queryQueue.insert(apOngdApvLnDVo);
					}
				} else {
					key = "Y".equals(apOngdApvLnDVo.getApvrDeptYn())
							? apOngdApvLnDVo.getApvDeptId()
							: apOngdApvLnDVo.getApvrUid();
					// 의견, 읽은 날짜가 있으면 의견 해쉬에 담음(백업)
					if(key!=null && !key.isEmpty()){
						opin = apOngdApvLnDVo.getApvOpinCont();
						vwDt = apOngdApvLnDVo.getVwDt();
						if( (opin!=null && !opin.isEmpty()) || (vwDt!=null && !vwDt.isEmpty()) ){
							opinMap.put(Hash.hashId(key), apOngdApvLnDVo);
						}
					}
				}
			}
		}
	}
	
	/** 결재라인 저장 - 임시저장, 기안 */
	private void processMakApvLn(List<ApOngdApvLnDVo> paramApOngdApvLnDVoList, ApvLines apvLines,
			Map<String, String> paramMap, ApOngdBVo apOngdBVo,
			UserVo userVo, Map<Integer, OrUserBVo> userCacheMap, Map<Integer, OrOrgBVo> orgCacheMap,
			Locale locale, QueryQueue queryQueue, List<Map<String, String>> messengerQueue, Map<String, String> optConfigMap, ModelMap model, 
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, boolean isTemp, boolean isByOne) throws SQLException, CmException{
		
		// 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블
		ApOngdApvLnDVo apOngdApvLnDVo, storedApOngdApvLnDVo;
		
		String apvNo = apOngdBVo.getApvNo();
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		
		// 결재라인이력번호
		String apvLnHstNo = myTurnApOngdApvLnDVo==null ? "1" : myTurnApOngdApvLnDVo.getApvLnHstNo();
		
		// 부서합의를 담아둘 목록 - 루트라인 일때 - 부서합의의 순서가 바뀌었을 때 해당 하위 라인의 부모라인 번호를 맞추는 역할
		//   재검토로 서브라인(합의라인)에서 루트라인으로 넘어 왔을때 부서합의의 순서가 변경되면, 하위라인의 정보를 찾지 못하는 것 보완
		List<ApOngdApvLnDVo> deptAgrList =  myTurnApOngdApvLnDVo==null ? null : new ArrayList<ApOngdApvLnDVo>();
		
		// 결재 후 다음차례의 결재자/부서 를 담아둘 맵
		Map<Integer, ApOngdApvLnDVo> nextTurnMap = null;
		
		// 현재 결재자(기안자)가 재검토 상태 인지
		boolean isReRevw = myTurnApOngdApvLnDVo==null ? false : "reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd());
		
		// 기존 결재라인 삭제
		apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		queryQueue.delete(apOngdApvLnDVo);
		
		// (재검토 문서의 임시저장)이 아닌 경우 - (재검토 문서의 임시저장)은 변경 내역을 조회 해야함
		if(!(isTemp && isReRevw)){
			// 기존 결재라인 [이력] 삭제
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
			queryQueue.delete(apOngdApvLnDVo);
		}
		
		// 의견, 조회여부 저장된 맵으로 변환
		Map<Integer, ApOngdApvLnDVo> opinMap =  myTurnApOngdApvLnDVo==null ?
				null : toOpinMap(apvLines.getRootLn(), userVo.getUserUid());
		
		if(!isTemp){
			if(isByOne){
				// 문서승인상태코드 - apvd:승인
				apOngdBVo.setDocProsStatCd("apvd");
			} else {
				// 문서승인상태코드 - ongo:결재중
				apOngdBVo.setDocProsStatCd("ongo");
			}
		} else {
			if(isEmpty(apOngdBVo.getDocProsStatCd())){
				apOngdBVo.setDocProsStatCd("temp");//temp:임시저장
			}
		}
		
		// 첫번째 다음 결재자 인지
		boolean firstNextApvr = true;
		// 결재자역할코드
		String apvrRoleCd;
		int i, size = paramApOngdApvLnDVoList.size();
		for(i=0; i<size; i++){
			
			apOngdApvLnDVo = paramApOngdApvLnDVoList.get(i);
			
			// 부서고 사용자UID가 있으면 - 부서합의에서 재검토 내려갈때 재검토자 세팅하며 - 재기안시 지워줘야 함
			if(!isTemp && "Y".equals(apOngdApvLnDVo.getApvrDeptYn()) && !isEmpty(apOngdApvLnDVo.getApvrUid())){
				apOngdApvLnDVo.setApvrUid(null);
			}
			
			// 저장되어 있는 결재선 정보 - 조회일시, 의견 - 새 데이터에 세팅함
			if(i > 0 && opinMap != null){
				
				// DB에 저장된 정보 - 임시저장, 재기안 일때 있음
				storedApOngdApvLnDVo = getFromMap(opinMap, apOngdApvLnDVo);
				if(storedApOngdApvLnDVo != null){
					// 예전 데이터 조회일시 - 유무 확인
					if(!isEmpty(storedApOngdApvLnDVo.getVwDt())){
						apOngdApvLnDVo.setVwDt(storedApOngdApvLnDVo.getVwDt());
					}
					// 예전 데이터 결재의견내용 - 유무 확인
					if(!isEmpty(storedApOngdApvLnDVo.getApvOpinCont())){
						apOngdApvLnDVo.setApvOpinCont(storedApOngdApvLnDVo.getApvOpinCont());//결재의견내용
						apOngdApvLnDVo.setApvOpinDispYn(storedApOngdApvLnDVo.getApvOpinDispYn());//결재의견표시여부
					}
					// (재검토 문서의 임시저장) 일때 - 재검토 요청자들의 이력 유지 > 상신할때 지워줌
					if(isTemp && isReRevw){
						if(!isEmpty(storedApOngdApvLnDVo.getApvDt())){
							apOngdApvLnDVo.setApvDt(storedApOngdApvLnDVo.getApvDt());
						}
						if(!isEmpty(storedApOngdApvLnDVo.getPrevBodyHstNo())){
							apOngdApvLnDVo.setPrevBodyHstNo(storedApOngdApvLnDVo.getPrevBodyHstNo());
						}
						if(!isEmpty(storedApOngdApvLnDVo.getPrevApvLnHstNo())){
							apOngdApvLnDVo.setPrevApvLnHstNo(storedApOngdApvLnDVo.getPrevApvLnHstNo());
						}
						if(!isEmpty(storedApOngdApvLnDVo.getPrevAttHstNo())){
							apOngdApvLnDVo.setPrevAttHstNo(storedApOngdApvLnDVo.getPrevAttHstNo());
						}
						if(!isEmpty(storedApOngdApvLnDVo.getPrevRecvDeptHstNo())){
							apOngdApvLnDVo.setPrevRecvDeptHstNo(storedApOngdApvLnDVo.getPrevRecvDeptHstNo());
						}
					}
				}
			}
			
			// 결재선 데이터에 문서언어에 따른 리스스 바인딩
			setApOngdApvLnDVoResc(apOngdApvLnDVo, docLangTypCd, 
					userCacheMap, orgCacheMap, optConfigMap, locale);
			
			// 결재자역할코드
			apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
			
			if(i==0){// 첫번째 결재자
				
				////////////////////////////////////////////////////////
				// 문서정보세팅
				apOngdBVo.setCompId(userVo.getCompId());//회사ID
				apDocNoSvc.setDocNo(apOngdBVo, apOngdApvLnDVo, optConfigMap, "ongoing", locale);//문서번호 세팅
				
				////////////////////////////////////////////////////////
				// 결재라인정보세팅
				apOngdApvLnDVo.setApvOpinDispYn(paramMap.get("apvOpinDispYn"));//	결재의견표시여부
				apOngdApvLnDVo.setApvOpinCont(paramMap.get("apvOpinCont"));//		결재의견내용
				apOngdApvLnDVo.setVwDt("sysdate");//								조회일시
				apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//완료문서 - 맨 위로 올라오게
				
				// 임시저장 이면
				if(isTemp){
					// reRevw:재검토, cncl:취소(기안회수) 면
					if(myTurnApOngdApvLnDVo != null
							&& ("reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd())
								|| "cncl".equals(myTurnApOngdApvLnDVo.getApvStatCd()) )){
						apOngdApvLnDVo.setApvStatCd(myTurnApOngdApvLnDVo.getApvStatCd());//결재상태코드
					} else {
						apOngdApvLnDVo.setApvStatCd("inApv");//결재상태코드 - inApv:결재중
					}
				// 상신 이면
				} else {
					apOngdApvLnDVo.setApvStatCd("apvd");//결재상태코드 - apvd:승인
					
					// 해당 결재라인 서명 처리
					setApvrSign(apOngdApvLnDVo, false, optConfigMap,
							userCacheMap, docLangTypCd, userVo, locale);
					
					// 다음 결재자 찾음
					List<ApOngdApvLnDVo> currNextApvrList = ApDocTransUtil.getNextApvrList(paramApOngdApvLnDVoList, "1", false);
					if(currNextApvrList != null && !currNextApvrList.isEmpty()){
						// 다음 결재자 맵으로 전환
						nextTurnMap = toMap(currNextApvrList);
					} else {
						apOngdBVo.setDocStatCd("byOne");//문서상태코드 - byOne:1인결재
						// 문서 완료 처리
						setDocCmpl(apOngdBVo, apOngdApvLnDVo);
						// 문서번호 세팅
						apDocNoSvc.setDocNo(apOngdBVo, apOngdApvLnDVo, optConfigMap, "regRecLst", locale);
						// 등록대장 - 등록
						registerRecLst(apOngdBVo, queryQueue, model);
					}
				}

			// 두번째 결재자 부터
			} else {
				
				// 임시저장 이면
				if(isTemp){
					if(ApDocUtil.isParalAgrOfApvrRole(apvrRoleCd)){
						apOngdApvLnDVo.setApvStatCd("befoAgr");//결재상태코드 - befoAgr:합의전
					} else {
						apOngdApvLnDVo.setApvStatCd("befoApv");//befoApv:결재전
					}
				} else if(isByOne){//1인결재 이면
					if(ApDocUtil.isInfmOfApvrRole(apvrRoleCd)){
						apOngdApvLnDVo.setApvStatCd("inInfm");//inInfm:통보중
						apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//완료문서 - 맨 위로 올라오게
						
						// 개인통보 일때 - 통보 메세지
						if("psnInfm".equals(apvrRoleCd)){
							//[옵션] msgPsnInfm=통보 메세지 사용
							if("Y".equals(optConfigMap.get("msgPsnInfm"))){
								addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
							}
						}
						
					} else {
						continue;//1인 결재에서 통보가 아닌것 삭제
					}
				} else {
					
					// 다음 차례인 결재자 인 경우
					if(getFromMap(nextTurnMap, apOngdApvLnDVo) != null){
						
						apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//완료문서 - 맨 위로 올라오게
						apOngdApvLnDVo.setVwDt("");//조회일시
						
						if("abs".equals(apvrRoleCd)){
							apOngdApvLnDVo.setApvStatCd("apvd");//결재상태코드 - apvd:승인
						} else if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd)) {
							apOngdApvLnDVo.setApvStatCd("inAgr");//결재상태코드 - inAgr:합의중
						} else {
							apOngdApvLnDVo.setApvStatCd("inApv");//결재상태코드 - inApv:결재중
						}
						
						// 부서면 - 담당자 지정여부 - 지정안합으로 변경
						if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
							apOngdApvLnDVo.setPichApntYn("N");
						}
						
						if(firstNextApvr && !"abs".equals(apvrRoleCd)){
							firstNextApvr = false;
							// 문서에 현재 결재자 정보 세팅
							setCurApvrAtApOngdBVo(apOngdBVo, apOngdApvLnDVo);
						}
						
					// 다음 차례인 결재자가 아닌 경우
					} else {
						if(ApDocUtil.isAgrOfApvrRole(apvrRoleCd)) {
							apOngdApvLnDVo.setApvStatCd("befoAgr");//결재상태코드 - befoAgr:합의전
						} else {
							apOngdApvLnDVo.setApvStatCd("befoApv");//결재상태코드 - befoApv:결재전
						}
					}
					
					// 부서합의 목록이 정의되어 있고(루트경로), 부서합의면 - 부서합의 목록에 더함
					//  - 차후 서브라인 데이터가 있고, 부서합의의 순서가 바뀌었을때 apvLnPno를 맞추어서 예전 데이터 유지 목적
					if(deptAgrList != null && ApDocUtil.isDeptAgrRole(apvrRoleCd)){
						deptAgrList.add(apOngdApvLnDVo);
					}
					
					// 결재일시 지우기
					if(apOngdApvLnDVo.getApvDt()!=null && !apOngdApvLnDVo.getApvDt().isEmpty()){
						apOngdApvLnDVo.setApvDt(null);
					}
					
					clearPrevHst(apOngdApvLnDVo);
				}
			}
			// 결재라인이력번호
			apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
			
			if("inApv".equals(apOngdApvLnDVo.getApvStatCd()) || "inAgr".equals(apOngdApvLnDVo.getApvStatCd())){
				// 메신저 보내기
				addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
			}
			
			// 결재라인 - 입력
			queryQueue.insert(apOngdApvLnDVo);
		}
		
		// 부서합의 목록으로 부서합의의 서브경로 맞춤
		if(deptAgrList!=null && !deptAgrList.isEmpty()){
			apvLines.transDeptAgrLn(queryQueue, deptAgrList);
		}
	}
	
	
	
	/** 결재라인 조회 */
	private List<ApOngdApvLnDVo> queryApvLn(String apvNo) throws SQLException{
		return queryApvLn(apvNo, null, null);
	}
	
	/** 결재라인 조회 */
	private List<ApOngdApvLnDVo> queryApvLn(String apvNo, String apvLnPno, String apvLnHstNo) throws SQLException{
		// 저장된 결재선 조회
		ApOngdApvLnDVo storedApOngdApvLnDVo = new ApOngdApvLnDVo();
		storedApOngdApvLnDVo.setApvNo(apvNo);
		storedApOngdApvLnDVo.setApvLnPno(apvLnPno);
		if(apvLnHstNo != null && !apvLnHstNo.isEmpty()){
			storedApOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);//이력번호세팅
			storedApOngdApvLnDVo.setHistory();//히스토리테이블 세팅
		}
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(storedApOngdApvLnDVo);
		
		return apOngdApvLnDVoList;
	}
	
	/** 업로드된 첨부파일과 DB에 저장된 데이터로 한세트의 첨부파일 목록을 만듬 */
	private List<ApOngdAttFileLVo> parseAttFile(Map<String, List<String>> paramListMap,
			Map<String, List<File>> fileListMap, DistHandler distHandler, String uploadDir,
			String apvNo, String attHstNo, String rejtApvNo, String userUid, String attFileModified,
			Locale locale, Map<String, Boolean> attParseMap) throws CmException, SQLException, IOException {
		
		// attFileModified - 첨부 파일을 세팅해서 파라미터가 넘어 왔다는 것 - 실제로 첨부파일이 변경 되었는지는 알 수 없음
		
		boolean rejected = rejtApvNo != null && !rejtApvNo.isEmpty();
		// 반려된 첨부이력번호
		String rejtAttHstNo = null;
		// 반려된 결재번호가 있으면 해당 데이터 조회
		if(rejected){
			ApOngdBVo rejtApOngdBVo = new ApOngdBVo();
			rejtApOngdBVo.setApvNo(rejtApvNo);
			rejtApOngdBVo = (ApOngdBVo)commonDao.queryVo(rejtApOngdBVo);
			
			if(rejtApOngdBVo==null){
				LOGGER.error("Fail trans - no rejt doc ! - rejtApvNo:"+rejtApvNo);
				// ap.msg.noRejtDoc=해당 반려된 문서가 없습니다.
				throw new CmException("ap.msg.noRejtDoc", locale);
			}
			
			rejtAttHstNo = rejtApOngdBVo.getAttHstNo();
		}
		
		// 리턴할 목록
		List<ApOngdAttFileLVo> returnList = new ArrayList<ApOngdAttFileLVo>();
		
		Integer attSeq = 0;
		List<String> attNameList = paramListMap.get("docAttchFile");// 업로드된 File 명 목록
		List<File> attFileList = fileListMap.get("docAttchFile");// 업로드된 File 목록(물리적 파일)
		
		List<String> attSeqList = paramListMap.get("attSeq");//첨부파일 일련번호
		List<String> attUseYnList = paramListMap.get("attUseYn");//첨부파일 사용여부 - 삭제된것 파악용
		
		List<String> attIntgNoList = paramListMap.get("attIntgNo");//ERP 연계 파일 - 연계파일인지 확인용
		String intgNo = attIntgNoList==null || attIntgNoList.isEmpty() ? null : attIntgNoList.get(0);
		
		// 첨부 데이터 조회
		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
		if(rejected){
			apOngdAttFileLVo.setApvNo(rejtApvNo);
			apOngdAttFileLVo.setAttHstNo(rejtAttHstNo);
		} else {
			apOngdAttFileLVo.setApvNo(apvNo);
			apOngdAttFileLVo.setAttHstNo(attHstNo);
		}
		@SuppressWarnings("unchecked")
		List<ApOngdAttFileLVo> storedApOngdAttFileLVoList = intgNo!=null ? null : (List<ApOngdAttFileLVo>)commonDao.queryList(apOngdAttFileLVo);
		// 조회된 첨부 데이터를 맵으로 전환
		Map<Integer, ApOngdAttFileLVo> storedAttMap = new HashMap<Integer, ApOngdAttFileLVo>();
		if(storedApOngdAttFileLVoList != null){
			for(ApOngdAttFileLVo storedApOngdAttFileLVo : storedApOngdAttFileLVoList){
				storedAttMap.put(Integer.valueOf(storedApOngdAttFileLVo.getAttSeq()), storedApOngdAttFileLVo);
			}
		}
		
		boolean deleted = false;
		boolean hasFile = false;
		boolean newAdded = false;
		
		int i, p, fileKb;
		int erpIntgSize = (attIntgNoList==null || uploadDir==null) ? 0 : attIntgNoList.size();
		
		File file;
		String fileName, filePath, ext;
		
		// 마지막 파일 정보가 있도록함
		ApOngdAttFileLVo storedApOngdAttFileLVo = null;
		
		ApErpIntgFileDVo apErpIntgFileDVo;
		
		FileOutputStream out = null;
		LobHandler erpLobHandler;
		
		// 첨부 관련 파라미터가 전송 되었으면
		if("Y".equals(attFileModified)){
			
			int attSeqSize = Math.min(
					attUseYnList==null ? 0 : attUseYnList.size(),
					attSeqList==null ? 0: attSeqList.size() );
			
			for(i=0;i<attSeqSize; i++){
				// 사용하는 데이터만 모집
				if("Y".equals(attUseYnList.get(i))){
					// 저장된 데이터 꺼내기
					storedApOngdAttFileLVo = storedAttMap.get(Integer.valueOf(attSeqList.get(i)));
					// 저장된 데이터가 있을 경우만
					if(storedApOngdAttFileLVo != null){
						if(rejected){
							storedApOngdAttFileLVo.setApvNo(apvNo);
							newAdded = true;
							// 반려문서 재상신의 경우 시퀀스 다시 생성함
							attSeq++;
							storedApOngdAttFileLVo.setAttSeq(attSeq.toString());
						}
						
						storedApOngdAttFileLVo.setModrUid(userUid);
						storedApOngdAttFileLVo.setModDt("sysdate");
						returnList.add(storedApOngdAttFileLVo);
						hasFile = true;
					} else if(erpIntgSize > i && uploadDir != null){
						
						apErpIntgFileDVo = new ApErpIntgFileDVo();
						apErpIntgFileDVo.setIntgNo(intgNo);
						apErpIntgFileDVo.setSeq(attSeqList.get(i));
						apErpIntgFileDVo = (ApErpIntgFileDVo)commonDao.queryVo(apErpIntgFileDVo);
						
						if(apErpIntgFileDVo != null){
							
							storedApOngdAttFileLVo = new ApOngdAttFileLVo();
							storedApOngdAttFileLVo.setApvNo(apvNo);
							
							attSeq++;
							storedApOngdAttFileLVo.setAttSeq(attSeq.toString());
							
							fileName = apErpIntgFileDVo.getAttDispNm();
							storedApOngdAttFileLVo.setAttDispNm(fileName);
							
							p = fileName.lastIndexOf('.');
							if(p>0){
								ext = fileName.substring(p+1);
								storedApOngdAttFileLVo.setFileExt(ext);
							} else {
								ext = "";
							}
							
							erpLobHandler = lobHandler.create(
									"SELECT FILE_CONT FROM AP_ERP_INTG_FILE_D WHERE INTG_NO = ? AND SEQ = ? ",
									new String[]{intgNo, apErpIntgFileDVo.getSeq()});
							
							filePath = uploadDir +"/" + StringUtil.getNextHexa() + (ext.isEmpty() ? ext : "."+ext);
							out = new FileOutputStream(filePath);
							erpLobHandler.writeFile(out);
							
							file = new File(filePath);
							fileKb = (int)(file.length()/1024L);
							if(fileKb==0) fileKb = 1;
							storedApOngdAttFileLVo.setFileKb(String.valueOf(fileKb));
							filePath = distHandler.addWasList(file.getAbsolutePath());
							storedApOngdAttFileLVo.setFilePath(filePath);
							storedApOngdAttFileLVo.setModrUid(userUid);
							storedApOngdAttFileLVo.setModDt("sysdate");
							
							returnList.add(storedApOngdAttFileLVo);
							newAdded = true;
						}
					} else {
						deleted = true;
					}
				} else {
					deleted = true;
				}
			}
			if(deleted && attParseMap!=null) attParseMap.put("deleted", Boolean.TRUE);
			
		// 첨부 관련 파라미터가 전송 되지 않았으면
		} else {// else - if("Y".equals(attFileModified)){
			
			int attSeqSize = storedApOngdAttFileLVoList==null ? 0 : storedApOngdAttFileLVoList.size();
			for(i=0; i<attSeqSize; i++){
				storedApOngdAttFileLVo = storedApOngdAttFileLVoList.get(i);
				
				if(rejected){
					storedApOngdAttFileLVo.setApvNo(apvNo);
					newAdded = true;
					// 반려문서 재상신의 경우 시퀀스 다시 생성함
					attSeq++;
					storedApOngdAttFileLVo.setAttSeq(attSeq.toString());
				}
				
				storedApOngdAttFileLVo.setModrUid(userUid);
				storedApOngdAttFileLVo.setModDt("sysdate");
				returnList.add(storedApOngdAttFileLVo);
				hasFile = true;
			}
		}
		
		// 첨부 시퀀스 - 마지막 VO 첨부번호
		if(!rejected && storedApOngdAttFileLVo!=null){
			attSeq = Integer.valueOf(storedApOngdAttFileLVo.getAttSeq());
		}
		
		// 임시파일 시작
		List<String> tmpFileIdList = paramListMap.get("tmpFileId");// 업로드된 임시 FileId  목록
		// 임시파일VO
		EmTmpFileTVo emTmpFileTVo;
		if(tmpFileIdList!=null && tmpFileIdList.size()>0){
			for(String tmpFileId : tmpFileIdList){
				if(tmpFileId==null || tmpFileId.isEmpty()) continue;
				emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpFileId));
				if(emTmpFileTVo==null) continue;
				apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				
				attSeq++;
				apOngdAttFileLVo.setAttSeq(attSeq.toString());
				
				fileName = emTmpFileTVo.getDispNm();
				apOngdAttFileLVo.setAttDispNm(fileName);
				
				apOngdAttFileLVo.setFileExt(emTmpFileTVo.getFileExt());
				fileKb = (int)(emTmpFileTVo.getFileSize()/1024L);
				if(fileKb==0) fileKb = 1;
				apOngdAttFileLVo.setFileKb(String.valueOf(fileKb));
				filePath = distHandler.addWasList(emTmpFileTVo.getSavePath());
				apOngdAttFileLVo.setFilePath(filePath);
				apOngdAttFileLVo.setModrUid(userUid);
				apOngdAttFileLVo.setModDt("sysdate");
				
				returnList.add(apOngdAttFileLVo);
				newAdded = true;
			}
		}
				
		// multi-part 로 전송된 파일 처리
		int fileCount = attFileList==null ? 0 : attFileList.size();
		for(i=0;i<fileCount;i++){
			
			file = attFileList.get(i);
			apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apvNo);
			
			attSeq++;
			apOngdAttFileLVo.setAttSeq(attSeq.toString());
			
			fileName = attNameList.get(i);
			apOngdAttFileLVo.setAttDispNm(fileName);
			
			p = fileName.lastIndexOf('.');
			if(p>0) apOngdAttFileLVo.setFileExt(fileName.substring(p+1));
			fileKb = (int)(file.length()/1024L);
			if(fileKb==0) fileKb = 1;
			apOngdAttFileLVo.setFileKb(String.valueOf(fileKb));
			filePath = distHandler.addWasList(file.getAbsolutePath());
			apOngdAttFileLVo.setFilePath(filePath);
			apOngdAttFileLVo.setModrUid(userUid);
			apOngdAttFileLVo.setModDt("sysdate");
			
			returnList.add(apOngdAttFileLVo);
			newAdded = true;
		}
		
		if((hasFile || newAdded) && attParseMap!=null) attParseMap.put("hasFile", Boolean.TRUE);
		if(newAdded && attParseMap!=null) attParseMap.put("newAdded", Boolean.TRUE);
		
		return returnList;
	}
	
	/** 첨부 파일 처리 */
	private void processAttach(QueryQueue queryQueue, UploadHandler uploadHandler, DistHandler distHandler,
			ApOngdBVo apOngdBVo, Locale locale, UserVo userVo,
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, String statCd) throws IOException, CmException, SQLException{

		Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
		Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
		Map<String, List<File>> fileListMap = uploadHandler.getFileListMap();//파일 리스트
		
		String apvNo = apOngdBVo.getApvNo();
		String tempApvNo = (String)paramMap.get("tempApvNo");//임시저장에서 상신 한 경우
		String rejtApvNo = (String)paramMap.get("rejtApvNo");//반려 문서로 재기안 할 경우
		String attFileModified = (String)paramMap.get("attFileModified");//첨부문서 변경 여부
		String attHstNo = null;
		
		// - 임시저장하면 임시저장한 문서를 유지하고, 새로운 문서를 등록하도록 하기 위해서
		// 반려문서번호가 없고, 임시저장문서번호가 있을 때
		//   - 반려문서처럼 임시저장번호를 넘겨 줘서, 임시저장된 데이터를 조회해서 첨부파일 저장된 내역을 조회 해야 함
		if(rejtApvNo == null || rejtApvNo.isEmpty()){
			if(tempApvNo != null && !tempApvNo.isEmpty()){
				rejtApvNo = tempApvNo;
			}
		}
		
		ApOngdAttFileLVo apOngdAttFileLVo;
		Map<String, Boolean> attParseMap = new HashMap<String, Boolean>();
		
		// 기안, 임시저장
		if("mak".equals(statCd) || "temp".equals(statCd)){
			// 첨부이력번호
			attHstNo = apOngdBVo.getAttHstNo();
			apOngdBVo.setAttHstNo("1");//첨부이력번호
			
			// 업로드된 첨부파일과 DB에 저장된 데이터로 한세트의 첨부파일 목록을 만듬
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = parseAttFile(paramListMap, fileListMap, distHandler, uploadHandler.getUploadDir(),
					apvNo, attHstNo, rejtApvNo, userVo.getUserUid(), "Y", locale, attParseMap);
			
			// 이전 첨부 데이터 삭제
			apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apvNo);
			//apOngdAttFileLVo.setAttHstNo(attHstNo);
			queryQueue.delete(apOngdAttFileLVo);
			
			// 조합된 데이터(기존첨부(DB) + 신규첨부(업로드)) 입력
			for(ApOngdAttFileLVo newApOngdAttFileLVo : apOngdAttFileLVoList){
				newApOngdAttFileLVo.setAttHstNo("1");
				queryQueue.insert(newApOngdAttFileLVo);
			}
			
		// 보류
		} else if("hold".equals(statCd)){
			
			// 보류첨부이력번호
			String holdAttHstNo = myTurnApOngdApvLnDVo.getHoldAttHstNo();
			
			if(holdAttHstNo==null || holdAttHstNo.isEmpty()){
				attHstNo = apOngdBVo.getAttHstNo();
			} else {
				attHstNo = holdAttHstNo;
			}
			
			// 업로드된 첨부파일과 DB에 저장된 데이터로 한세트의 첨부파일 목록을 만듬
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = parseAttFile(paramListMap, fileListMap, distHandler, null, 
					apvNo, attHstNo, rejtApvNo, userVo.getUserUid(), attFileModified, locale, attParseMap);
			
			// 변경된 파일이 있으면
			if(attParseMap.get("deleted") != null || attParseMap.get("newAdded") != null){
				
				// 보류번호가 없으면 - 신규 생성
				if(holdAttHstNo==null || holdAttHstNo.isEmpty()){
					holdAttHstNo = apCmSvc.addNo(apOngdBVo.getAttHstNo(), 1);
				// 보류번호가 있으면 - 기존 데이터 삭제
				} else {
					apOngdAttFileLVo = new ApOngdAttFileLVo();
					apOngdAttFileLVo.setApvNo(apvNo);
					apOngdAttFileLVo.setAttHstNo(holdAttHstNo);
					queryQueue.delete(apOngdAttFileLVo);
				}
				
				// 조합된 데이터(기존첨부(DB) + 신규첨부(업로드)) 입력
				for(ApOngdAttFileLVo newApOngdAttFileLVo : apOngdAttFileLVoList){
					newApOngdAttFileLVo.setAttHstNo(holdAttHstNo);
					queryQueue.insert(newApOngdAttFileLVo);
				}
				
				// 보류첨부이력번호 - 세팅
				myTurnApOngdApvLnDVoToSave.setHoldAttHstNo(holdAttHstNo);
			}

		// 재검토, 반려
		} else if("reRevw".equals(statCd) || "rejt".equals(statCd)){
			
			// 보류첨부이력번호
			String holdAttHstNo = myTurnApOngdApvLnDVo.getHoldAttHstNo();
			
			// 보류첨부이력번호 - 가 있으면 - 보류된 데이터 삭제
			if(holdAttHstNo!=null && !holdAttHstNo.isEmpty()){
				
				apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setAttHstNo(holdAttHstNo);
				queryQueue.delete(apOngdAttFileLVo);
				
				// 보류첨부이력번호 - 지움
				myTurnApOngdApvLnDVoToSave.setHoldAttHstNo("");
			}
			
		// 승인, 찬성(합의), 반대(합의)
		} else {
			
			// 이전첨부이력번호
			String prevAttHstNo = myTurnApOngdApvLnDVo.getPrevAttHstNo();
			// 보류첨부이력번호
			String holdAttHstNo = myTurnApOngdApvLnDVo.getHoldAttHstNo();
			
			if(holdAttHstNo==null || holdAttHstNo.isEmpty()){
				attHstNo = apOngdBVo.getAttHstNo();
			} else {
				attHstNo = holdAttHstNo;
			}
			
			// 업로드된 첨부파일과 DB에 저장된 데이터로 한세트의 첨부파일 목록을 만듬
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = parseAttFile(paramListMap, fileListMap, distHandler, null,
					apvNo, attHstNo, rejtApvNo, userVo.getUserUid(), attFileModified, locale, attParseMap);
			
			// 변경된 파일이 없으면
			if(attParseMap.get("deleted") == null && attParseMap.get("newAdded") == null){
				
				// 보류첨부이력번호 - 가 있으면 - 첨부이력번호 를 보류첨부이력번호로 변경함
				if(holdAttHstNo!=null && !holdAttHstNo.isEmpty()){
					
					// 보류본문이력번호 - 지워줌
					myTurnApOngdApvLnDVoToSave.setHoldAttHstNo("");
					
					if(prevAttHstNo==null || prevAttHstNo.isEmpty()){
						// 이전첨부이력번호 - 현재 저장되어 있는 이전첨부이력번호 세팅
						myTurnApOngdApvLnDVoToSave.setPrevAttHstNo(apOngdBVo.getAttHstNo());
					}
					// 첨부이력번호 - 보류첨부이력번호로 변경함
					apOngdBVo.setAttHstNo(holdAttHstNo);
				}
				
			// 변경된 파일이 있으면
			} else {
				
				// 보류첨부일련번호 가 있으면
				if(holdAttHstNo!=null && !holdAttHstNo.isEmpty()){
					
					// 기존 데이터 삭제
					apOngdAttFileLVo = new ApOngdAttFileLVo();
					apOngdAttFileLVo.setApvNo(apvNo);
					apOngdAttFileLVo.setAttHstNo(holdAttHstNo);
					queryQueue.delete(apOngdAttFileLVo);
					
					// 조합된 데이터(기존첨부(DB) + 신규첨부(업로드)) 입력
					for(ApOngdAttFileLVo newApOngdAttFileLVo : apOngdAttFileLVoList){
						newApOngdAttFileLVo.setAttHstNo(holdAttHstNo);
						queryQueue.insert(newApOngdAttFileLVo);
					}
					
					// 보류첨부이력번호 - 지움
					myTurnApOngdApvLnDVoToSave.setHoldAttHstNo("");
					
					if(prevAttHstNo==null || prevAttHstNo.isEmpty()){
						// 이전첨부이력번호 - 현재 저장되어 있는 이전첨부이력번호 세팅
						myTurnApOngdApvLnDVoToSave.setPrevAttHstNo(apOngdBVo.getAttHstNo());
					}
					
					// 첨부이력번호 - 보류첨부이력번호로 변경함
					apOngdBVo.setAttHstNo(holdAttHstNo);
					
				// 보류첨부일련번호 가 없으면
				} else {
					
					// 첨부이력번호 - 생성
					attHstNo = apCmSvc.addNo(apOngdBVo.getAttHstNo(), 1);
					
					// 조합된 데이터(기존첨부(DB) + 신규첨부(업로드)) 입력
					for(ApOngdAttFileLVo newApOngdAttFileLVo : apOngdAttFileLVoList){
						newApOngdAttFileLVo.setAttHstNo(attHstNo);
						queryQueue.insert(newApOngdAttFileLVo);
					}
					
					if(prevAttHstNo==null || prevAttHstNo.isEmpty()){
						// 이전첨부이력번호 - 현재 저장되어 있는 이전첨부이력번호 세팅
						myTurnApOngdApvLnDVoToSave.setPrevAttHstNo(apOngdBVo.getAttHstNo());
					}
					
					// 첨부이력번호 - 신규 생성한 첨부이력번호 - 세팅
					apOngdBVo.setAttHstNo(attHstNo);
				}
			}
		}
		
		// 보류, 재검토, 반려가 아닐 때
		if(!"hold".equals(statCd) && !"reRevw".equals(statCd) && !"rejt".equals(statCd)){
			// 첨부파일 존재 여부 표시
			if(attParseMap.get("hasFile") != null){
				apOngdBVo.setAttFileYn("Y");
			} else {
				apOngdBVo.setAttFileYn("N");
			}
		}
		
	}
	
	/** 해당 결재라인 서명 처리 */
	private void setApvrSign(ApOngdApvLnDVo apOngdApvLnDVo, boolean agnChk, Map<String, String> optConfigMap, 
			Map<Integer, OrUserBVo> userCacheMap, String docLangTypCd, UserVo userVo, Locale locale) throws SQLException, CmException{
		
		String apvrUid = apOngdApvLnDVo.getApvrUid();
		boolean isSameUser = userVo.getUserUid().equals(apOngdApvLnDVo.getApvrUid())
				|| ArrayUtil.isIn2Array(userVo.getAdurs(), 1, apOngdApvLnDVo.getApvrUid());
		if(!isSameUser){
			apvrUid = userVo.getUserUid();
			if(agnChk){
				apOngdApvLnDVo.setAgntUid(apvrUid);
			}
		}
//		String apvrUid = userVo.getUserUid();
//		// 세션의 사용자와 결재자와 다르면 - 대리인UID에 세팅 함
//		if(agnChk && !apvrUid.equals(apOngdApvLnDVo.getApvrUid())){
//			apOngdApvLnDVo.setAgntUid(apvrUid);
//		}
		
		// 사용자 정보 조회
		OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(apvrUid, docLangTypCd, userCacheMap);
		
		/////////////////////////////////
		// [도장방 서명]
		
		//[옵션] 서명란 날인 방법
		String signMthdCd = optConfigMap.get("signAreaSign");
		if("psn".equals(signMthdCd)){
			signMthdCd = orUserBVo.getSignMthdCd();
			
			// 디폴트 세팅 - 서명방법
			if(signMthdCd==null || signMthdCd.isEmpty()){
				signMthdCd = ApConstant.DFT_SIGN_MTHD_CD;
			}
		}
		
		// 서명방법코드 - 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자)
		if("03".equals(signMthdCd)){
			// 서명표시명 - 세팅
			apOngdApvLnDVo.setSignDispVa(orUserBVo.getRescNm());
		} else {
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			orUserImgDVo.setUserUid(apvrUid);
			orUserImgDVo.setUserImgTypCd(signMthdCd);
			orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
			if(orUserImgDVo==null){
				orUserImgDVo = new OrUserImgDVo();
				orUserImgDVo.setUserUid(orUserBVo.getOdurUid());
				orUserImgDVo.setUserImgTypCd(signMthdCd);
				orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
			}
			if(orUserImgDVo==null){
				LOGGER.error("Fail trans - no stamp image ! - userUid:"+orUserBVo.getUserUid()+"  userNm:"+orUserBVo.getRescNm());
				//ap.trans.signStampImg=서명 또는 도장 이미지
				//ap.trans.notProcess={0}가(이) 설정되지 않아서 진행 할 수 없습니다.
				throw new CmException("ap.trans.notProcess", new String[]{"#ap.trans.signStampImg"}, locale);
			}
			// 서명이미지경로 - 세팅
			apOngdApvLnDVo.setSignImgPath(orUserImgDVo.getImgPath());
		}
		apOngdApvLnDVo.setApvDt("sysdate");//결재일시

		/////////////////////////////////
		// [도장방 날짜]
		String signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, apOngdApvLnDVo, docLangTypCd, true);
		apOngdApvLnDVo.setDtDispVa(signAreaDtDisp);
	}
	
	/** 도장방 날짜 표시값 생성 */
	private String getSignAreaDtDispVa(Map<String, String> optConfigMap,
			ApOngdApvLnDVo apOngdApvLnDVo, String docLangTypCd, boolean withDt) throws SQLException{
		
		// 도장방 날짜표시 - 표시 방법 조회
		String signAreaDtDisp = null;
		if(withDt){
			signAreaDtDisp = optConfigMap.get("signAreaDt");
			if(signAreaDtDisp==null || signAreaDtDisp.isEmpty()) signAreaDtDisp = ApConstant.DFT_SIGN_AREA_DT;
			SimpleDateFormat signDtFormat = new SimpleDateFormat(signAreaDtDisp);
			String date = commonDao.querySysdate(null);
			signAreaDtDisp = signDtFormat.format(java.sql.Timestamp.valueOf(date));
//			signAreaDtDisp = signDtFormat.format(new Date(System.currentTimeMillis()));
		}
		
		String signAreaDtDisp2 = optConfigMap.get("signAreaDt2");
		if("Y".equals(optConfigMap.get("nameAtDateSignAreaEnable"))) signAreaDtDisp2 = "name";
		
		String rescVa = null;
		if(signAreaDtDisp2!=null && !signAreaDtDisp2.isEmpty()){
			
			// posit:직위, title:직책, name:성명, deptNm:부서명, deptAbs:부서약어, 
			// namePosit:성명(직위), nameTitle:성명(직책), 
			// nameDeptNm:성명(부서명), nameDeptAbs:성명(부서약어), apvTyp:결재구분
			if(signAreaDtDisp2==null || signAreaDtDisp2.isEmpty() || signAreaDtDisp2.equals("posit")){
				rescVa = apOngdApvLnDVo.getApvrPositNm();
				if(rescVa==null) rescVa = "";
			} else if(signAreaDtDisp2.equals("title")){
				rescVa = apOngdApvLnDVo.getApvrTitleNm();
				if(rescVa==null) rescVa = "";
			} else if(signAreaDtDisp2.equals("name")){
				rescVa = apOngdApvLnDVo.getApvrNm();
				if(rescVa==null) rescVa = "";
			} else if(signAreaDtDisp2.equals("deptNm")){
				rescVa = apOngdApvLnDVo.getApvDeptNm();
				if(rescVa==null) rescVa = "";
			} else if(signAreaDtDisp2.equals("deptAbs")){
				rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
				if(rescVa==null) rescVa = "";
			} else if(signAreaDtDisp2.equals("namePosit")){
				rescVa = apOngdApvLnDVo.getApvrPositNm();
				rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
			} else if(signAreaDtDisp2.equals("nameTitle")){
				rescVa = apOngdApvLnDVo.getApvrTitleNm();
				rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
			} else if(signAreaDtDisp2.equals("nameDeptNm")){
				rescVa = apOngdApvLnDVo.getApvDeptNm();
				rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
			} else if(signAreaDtDisp2.equals("nameDeptAbs")){
				rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
				rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
			} else if(signAreaDtDisp2.equals("apvTyp")){
				// byOne:1인결재, mak:기안, revw:검토, 
				// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
				// prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), 
				// postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
				String apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
				if(apvrRoleCd!=null && !apvrRoleCd.isEmpty()){
					String termId, termVa;
					if(!"0".equals(apOngdApvLnDVo.getApvLnPno())){
						termId = "deptAgr";
					} else if("psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd) || "byOneAgr".equals(apvrRoleCd)){// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의 
						termId = "agr";
					} else if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)){// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
						termId = "deptAgr";
					} else if("pred".equals(apvrRoleCd)){// pred:전결
						termId = "apv";
					} else if("paralPubVw".equals(apvrRoleCd)){// paralPubVw:동시공람
						termId = "pubVw";
					} else {
						termId = apvrRoleCd;
					}
					Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", docLangTypCd);
					termVa = termMap.get(termId);
					if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, SessionUtil.toLocale(docLangTypCd));
					rescVa = termVa;
				}
			}
			if(rescVa!=null && !rescVa.isEmpty()){
				if(withDt){
					signAreaDtDisp = signAreaDtDisp+"<br/>"+rescVa;
				} else {
					signAreaDtDisp = "&nbsp;<br/>"+rescVa;
				}
			}
		}
		
		return signAreaDtDisp;
	}
	
	/** 결재선 데이터에 문서언어에 따른 리스스 바인딩 <br/>
	 *  - 어권에 따른 (결재자명, 직위, 직책, 부서명, 부서약어, 도장방 상단 직위칸) 세팅 <br/>
	 *  - 진행문서결재라인상세(AP_ONGD_APV_LN_D) */
	private void setApOngdApvLnDVoResc(ApOngdApvLnDVo apOngdApvLnDVo, String docLangTypCd,
			Map<Integer, OrUserBVo> userCacheMap, Map<Integer, OrOrgBVo> orgCacheMap, 
			Map<String, String> optConfigMap, Locale locale) throws SQLException, CmException{
		
		// 사용자 정보
		String apvrUid = apOngdApvLnDVo.getApvrUid();
		String apvDeptId = apOngdApvLnDVo.getApvDeptId();
		String apvStatCd = apOngdApvLnDVo.getApvStatCd();
		
		if(apvrUid!=null && !apvrUid.isEmpty()){//결재자 설정이 사용자 이면
			// 사용자 정보 조회
			OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(apvrUid, docLangTypCd, userCacheMap);
			if(orUserBVo == null){
				LOGGER.error("Fail trans - user not found ! - apvrUid:"+apvrUid);
				//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
				String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.user"}, locale)
						+ apvrUid+ " : " + apOngdApvLnDVo.getApvrNm();
				throw new CmException(message);
			}
			
			apOngdApvLnDVo.setApvrNm(orUserBVo.getRescNm());
			apOngdApvLnDVo.setApvrPositCd(orUserBVo.getPositCd());
			apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());
			apOngdApvLnDVo.setApvrTitleCd(orUserBVo.getTitleCd());
			apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());
			
			// 결재자부서여부 - 결재자가 부서가 아니면 부서정보는 사용자의 부서정보 세팅
			if(apvDeptId==null || apvDeptId.isEmpty()){
				if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
					apvDeptId = orUserBVo.getDeptId();
					apOngdApvLnDVo.setApvDeptId(apvDeptId);
				} else if(apvDeptId==null || apvDeptId.isEmpty()){
					apvDeptId = orUserBVo.getDeptId();
					apOngdApvLnDVo.setApvDeptId(apvDeptId);
				}
			}
			
		} else if(apOngdApvLnDVo.getApvrNm() != null){
			apOngdApvLnDVo.setApvrNm(null);
			apOngdApvLnDVo.setApvrPositCd(null);
			apOngdApvLnDVo.setApvrPositNm(null);
			apOngdApvLnDVo.setApvrTitleCd(null);
			apOngdApvLnDVo.setApvrTitleNm(null);
		}
		
		// 부서정보
		OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, docLangTypCd, orgCacheMap);
		if(orOrgBVo==null){
			LOGGER.error("Fail trans - dept not found ! - apvDeptId:"+apvDeptId);
			//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
			String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
					+ apvDeptId+ " : " + apOngdApvLnDVo.getApvDeptNm();
			throw new CmException(message);
		}
		
		apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
		apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
		String rescVa = orOrgBVo.getOrgAbbrRescNm();
		if(rescVa==null || rescVa.isEmpty()){
			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
		} else {
			apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
		}
		
		// 도장방 - [맨위 : 직위영역] 표시명 세팅
		
		// 루트 결재선의 경우만 도장방에 표시
		if(ApDocUtil.isRootLine(apOngdApvLnDVo.getApvLnPno())){
			
			// 사용자
			if(!"Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
				
				// [사용자] 서명란 직위 표시 방법
				String signAreaUserDisp = optConfigMap.get("signAreaUserTitl");
				
				// posit:직위, title:직책, name:성명, deptNm:부서명, deptAbs:부서약어, 
				// namePosit:성명(직위), nameTitle:성명(직책), 
				// nameDeptNm:성명(부서명), nameDeptAbs:성명(부서약어), apvTyp:결재구분
				if(signAreaUserDisp==null || signAreaUserDisp.isEmpty() || signAreaUserDisp.equals("posit")){
					rescVa = apOngdApvLnDVo.getApvrPositNm();
					if(rescVa==null) rescVa = "";
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("title")){
					rescVa = apOngdApvLnDVo.getApvrTitleNm();
					if(rescVa==null) rescVa = "";
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("name")){
					rescVa = apOngdApvLnDVo.getApvrNm();
					if(rescVa==null) rescVa = "";
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("deptNm")){
					rescVa = apOngdApvLnDVo.getApvDeptNm();
					if(rescVa==null) rescVa = "";
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("deptAbs")){
					rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
					if(rescVa==null) rescVa = "";
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("namePosit")){
					rescVa = apOngdApvLnDVo.getApvrPositNm();
					rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("nameTitle")){
					rescVa = apOngdApvLnDVo.getApvrTitleNm();
					rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("nameDeptNm")){
					rescVa = apOngdApvLnDVo.getApvDeptNm();
					rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("nameDeptAbs")){
					rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
					rescVa = rescVa==null ? apOngdApvLnDVo.getApvrNm() : (apOngdApvLnDVo.getApvrNm()+"("+rescVa+")");
					apOngdApvLnDVo.setPositDispVa(rescVa);
				} else if(signAreaUserDisp.equals("apvTyp")){
					// byOne:1인결재, mak:기안, revw:검토, 
					// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
					// prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), 
					// postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
					String apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
					if(apvrRoleCd!=null && !apvrRoleCd.isEmpty()){
						String termId, termVa;
						if("psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd) || "byOneAgr".equals(apvrRoleCd)){// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의 
							termId = "agr";
						} else if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)){// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
							termId = "deptAgr";
						} else if("pred".equals(apvrRoleCd)){// pred:전결
							termId = "apv";
						} else if("paralPubVw".equals(apvrRoleCd)){// paralPubVw:동시공람
							termId = "pubVw";
						} else {
							termId = apvrRoleCd;
						}
						Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", docLangTypCd);
						termVa = termMap.get(termId);
						if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, SessionUtil.toLocale(docLangTypCd));
						apOngdApvLnDVo.setPositDispVa(termVa);
					}
				}
				
			// 부서
			} else {
				
				// [부서] 서명란 직위 표시 방법
				String signAreaDeptTitl = optConfigMap.get("signAreaDeptTitl");
				
				// 부서의 사용자가 있을때 - 합의승인, 합의반대 경우 - 부모칸에 표시 할 때
				//  재검토 의 경우는 부서명만 표시 - befoAgr 로 넘어옴
				if(apvrUid!=null && !apvrUid.isEmpty() && !"befoAgr".equals(apvStatCd)){
					
					if(signAreaDeptTitl==null || signAreaDeptTitl.isEmpty() || signAreaDeptTitl.equals("deptAbs")){
						rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("posit")){
						rescVa = apOngdApvLnDVo.getApvrPositNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("title")){
						rescVa = apOngdApvLnDVo.getApvrTitleNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("name")){
						rescVa = apOngdApvLnDVo.getApvrNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptNm")){
						rescVa = apOngdApvLnDVo.getApvDeptNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptAbs")){
						rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
						if(rescVa==null) rescVa = "";
						apOngdApvLnDVo.setPositDispVa(rescVa);
						
					} else if(signAreaDeptTitl.equals("deptNmPosit")){
						rescVa = apOngdApvLnDVo.getApvrPositNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptNm() : apOngdApvLnDVo.getApvDeptNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptNmTitle")){
						rescVa = apOngdApvLnDVo.getApvrTitleNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptNm() : apOngdApvLnDVo.getApvDeptNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptNmName")){
						rescVa = apOngdApvLnDVo.getApvrNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptNm() : apOngdApvLnDVo.getApvDeptNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);

					} else if(signAreaDeptTitl.equals("deptAbsPosit")){
						rescVa = apOngdApvLnDVo.getApvrPositNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptAbbrNm() : apOngdApvLnDVo.getApvDeptAbbrNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptAbsTitle")){
						rescVa = apOngdApvLnDVo.getApvrTitleNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptAbbrNm() : apOngdApvLnDVo.getApvDeptAbbrNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);
					} else if(signAreaDeptTitl.equals("deptAbsName")){
						rescVa = apOngdApvLnDVo.getApvrNm();
						rescVa = rescVa==null ? apOngdApvLnDVo.getApvDeptAbbrNm() : apOngdApvLnDVo.getApvDeptAbbrNm()+"("+rescVa+")";
						apOngdApvLnDVo.setPositDispVa(rescVa);
						
					} else if(signAreaDeptTitl.equals("apvTyp")){
						// byOne:1인결재, mak:기안, revw:검토, 
						// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
						// prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), 
						// postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
						String apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
						if(apvrRoleCd!=null && !apvrRoleCd.isEmpty()){
							String termId, termVa;
							if("psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd)){// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의 
								termId = "agr";
							} else if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)){// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
								termId = "deptAgr";
							} else if("pred".equals(apvrRoleCd)){// pred:전결
								termId = "apv";
							} else if("paralPubVw".equals(apvrRoleCd)){// paralPubVw:동시공람
								termId = "pubVw";
							} else {
								termId = apvrRoleCd;
							}
							Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", docLangTypCd);
							termVa = termMap.get(termId);
							if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, SessionUtil.toLocale(docLangTypCd));
							apOngdApvLnDVo.setPositDispVa(termVa);
						}
					}
				} else {
					String apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
					if(signAreaDeptTitl.equals("apvTyp") &&
							("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd))){
						String termId = "deptAgr";
						Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", docLangTypCd);
						String termVa = termMap.get(termId);
						if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, SessionUtil.toLocale(docLangTypCd));
						apOngdApvLnDVo.setPositDispVa(termVa);
					} else {
						// 부서의 경우 - 부서약어 가 있으면 부서약어 로 없으면 부서명 으로
						rescVa = apOngdApvLnDVo.getApvDeptAbbrNm();
						if(rescVa==null || rescVa.isEmpty()){
							rescVa = apOngdApvLnDVo.getApvDeptNm();
						}
						apOngdApvLnDVo.setPositDispVa(rescVa==null ? "" : rescVa);
					}
				}
			}
			
		}
		
		boolean withDt = false;
		// apvd:승인, rejt:반려, cons:반대, pros:찬성, cmplVw:공람완료
		if("apvd".equals(apvStatCd) || "rejt".equals(apvStatCd) || "cons".equals(apvStatCd) || "pros".equals(apvStatCd) || "cmplVw".equals(apvStatCd)){
			withDt = true;
		}
		
		String signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, apOngdApvLnDVo, docLangTypCd, withDt);
		apOngdApvLnDVo.setDtDispVa(signAreaDtDisp);
	}
	
	/** JSON 목록 데이터 파싱해서 [결재선] 목록 데이터 리턴 - 진행문서결재라인상세(AP_ONGD_APV_LN_D)  */
	private List<ApOngdApvLnDVo> parseApvLnJson(List<String> apvLnList, String apvNo, String apvLnPno, 
			String docLangTypCd, Locale locale) throws SQLException, CmException{
		if(apvLnList==null) return null;
		
		JSONObject jsonObject;
		ApOngdApvLnDVo apOngdApvLnDVo;
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
		String absRsonCd, apvrRoleCd;
		
		Integer apvLnNo = 0;
		for(String json : apvLnList){
			jsonObject = (JSONObject)JSONValue.parse(json);
			apvLnNo++;
			
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);//결재번호 - KEY
			apOngdApvLnDVo.setApvLnPno(apvLnPno);//결재라인부모번호 - KEY
			apOngdApvLnDVo.setApvLnNo(apvLnNo.toString());//결재라인번호 - KEY
			apOngdApvLnDVo.setDblApvTypCd((String)jsonObject.get("dblApvTypCd"));//이중결재구분코드
			
			//결재자역할코드
			apvrRoleCd = (String)jsonObject.get("apvrRoleCd");
			apOngdApvLnDVo.setApvrRoleCd(apvrRoleCd);
			
			// 부재사유
			absRsonCd = (String)jsonObject.get("absRsonCd");
			if(absRsonCd!=null && !absRsonCd.isEmpty()){
				apOngdApvLnDVo.setAbsRsonCd(absRsonCd);
				// 부재사유명 - 어권전환
				apOngdApvLnDVo.setAbsRsonNm(ptCmSvc.getCdRescNm("ABS_RSON_CD", docLangTypCd, absRsonCd));
			}
			
			if(!"Y".equals(jsonObject.get("apvrDeptYn"))){//결재자부서여부
				apOngdApvLnDVo.setApvrDeptYn("N");//결재자부서여부
				apOngdApvLnDVo.setApvrUid((String)jsonObject.get("apvrUid"));//결재자UID
				// 메세지 처리용 (저장되는 데이터는 어권 처리해서 생성) - 어권 전환중 해당 어권이 없을때
				apOngdApvLnDVo.setApvrNm((String)jsonObject.get("apvrNm"));
				apOngdApvLnDVo.setApvDeptNm((String)jsonObject.get("apvDeptNm"));
			} else {
				apOngdApvLnDVo.setApvrDeptYn("Y");//결재자부서여부
				apOngdApvLnDVo.setPichApntYn("N");//담당자지정여부
				// 메세지 처리용 (저장되는 데이터는 어권 처리해서 생성) - 어권 전환중 해당 어권이 없을때
				apOngdApvLnDVo.setApvDeptNm((String)jsonObject.get("apvDeptNm"));
			}
			apOngdApvLnDVo.setApvDeptId((String)jsonObject.get("apvDeptId"));//결재부서ID
			
			if("Y".equals(jsonObject.get("fixdApvrYn"))) apOngdApvLnDVo.setFixdApvrYn("Y");//고정결재자여부
			else apOngdApvLnDVo.setFixdApvrYn("");//고정결재자여부
			
			apOngdApvLnDVoList.add(apOngdApvLnDVo);
		}
		
		if(!apOngdApvLnDVoList.isEmpty()) {
			for(int i=apOngdApvLnDVoList.size()-1; i>=0; i--) {
				apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
				if(ApDocUtil.isInfmOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())) {
					continue;
				}
				if("reqDept".equals(apOngdApvLnDVo.getDblApvTypCd())) {
					//ap.msg.noPrcDept=처리부서가 지정되지 않았습니다.
					throw new CmException("ap.msg.noPrcDept", locale);
				} else {
					break;
				}
			}
		}
		
		return apOngdApvLnDVoList;
	}
	
	/** JSON 목록 데이터 파싱해서 [수신처] 목록 데이터 리턴 - 진행문서수신처상세(AP_ONGD_RECV_DEPT_D) */
	private List<ApOngdRecvDeptLVo> parseRecvDeptJson(List<String> recvDeptList, String apvNo, 
			String docLangTypCd, Map<Integer, OrOrgBVo> orgCacheMap, Locale locale, String userUid, Integer oldMaxRecvDeptSeq) throws SQLException, CmException {
		if(recvDeptList==null) return null;
		
		JSONObject jsonObject;
		ApOngdRecvDeptLVo apOngdRecvDeptLVo;
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = new ArrayList<ApOngdRecvDeptLVo>();
		OrOrgBVo orOrgBVo;
		String recvDeptTypCd, recvDeptId, refDeptId;
		
		// 기존에 저장된 수신처가 있는지 여부
		boolean hasOldMax = oldMaxRecvDeptSeq!=null;
		
		boolean sent = false;//발송 여부
		Integer recvDeptSeq = 0;
		
		for(String json : recvDeptList){
			jsonObject = (JSONObject)JSONValue.parse(json);
			
			apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			apOngdRecvDeptLVo.setApvNo(apvNo);//결재번호 - KEY
			apOngdRecvDeptLVo.setAddSendYn("N");//추가발송여부

			if("Y".equals(jsonObject.get("sendYn"))){
				apOngdRecvDeptLVo.setSendYn("Y");// 발송여부
				sent = true;
			} else {
				apOngdRecvDeptLVo.setSendYn("N");// 발송여부
				sent = false;
			}
			
			// 수신처가 중간 시퀀스가 DB 조작에 의해 삭제되었을때 - 최대 시퀀스 부터 하나씩 증가 하도록 함 - 오류 방지
			if(!hasOldMax){
				recvDeptSeq++;
				apOngdRecvDeptLVo.setRecvDeptSeq(recvDeptSeq.toString());//수신처일련번호 - KEY
			} else {
				if(sent){
					recvDeptSeq++;
					apOngdRecvDeptLVo.setRecvDeptSeq(recvDeptSeq.toString());//수신처일련번호 - KEY
				} else {
					oldMaxRecvDeptSeq++;
					apOngdRecvDeptLVo.setRecvDeptSeq(oldMaxRecvDeptSeq.toString());//수신처일련번호 - KEY
				}
			}
			
			//수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
			recvDeptTypCd = (String)jsonObject.get("recvDeptTypCd");
			apOngdRecvDeptLVo.setRecvDeptTypCd(recvDeptTypCd);//수신처구분코드
			if("outOrg".equals(recvDeptTypCd)){//outOrg:외부기관
				apOngdRecvDeptLVo.setRecvDeptNm((String)jsonObject.get("recvDeptNm"));//수신처명
				apOngdRecvDeptLVo.setRefDeptNm((String)jsonObject.get("refDeptNm"));//참조처명
				apOngdRecvDeptLVo.setRecvDeptId("");
				apOngdRecvDeptLVo.setRefDeptId("");
			} else {
				// 수신처
				recvDeptId = (String)jsonObject.get("recvDeptId");
				if(recvDeptId!=null && !recvDeptId.isEmpty()){
					apOngdRecvDeptLVo.setRecvDeptId(recvDeptId);//수신처ID
					if(!sent){// 발송된 것은 중복 체크용으로 리소스 담을 필요 없음
						// 수신처ID로 DB 조회
						orOrgBVo = apRescSvc.getOrOrgBVo(recvDeptId, docLangTypCd, orgCacheMap);
						if(orOrgBVo==null){
							LOGGER.error("Fail trans - dept not found ! - recvDeptId:"+recvDeptId);
							//ap.msg.notFound={0} 정보를 찾지 못했습니다. -cols.recvDept cols.refDept
							String message = messageProperties.getMessage("ap.msg.notFound", new String[]{"#cols.recvDept"}, locale);
							throw new CmException(message);
						}
						apOngdRecvDeptLVo.setRecvDeptNm(orOrgBVo.getRescNm());//수신처명
					}
				}
				// 참조처
				refDeptId = (String)jsonObject.get("refDeptId");
				if(refDeptId!=null && !refDeptId.isEmpty()){
					apOngdRecvDeptLVo.setRefDeptId(refDeptId);//참조처ID
					if(!sent){// 발송된 것은 중복 체크용으로 리소스 담을 필요 없음
						// 참조처ID로 DB 조회
						orOrgBVo = apRescSvc.getOrOrgBVo(refDeptId, docLangTypCd, orgCacheMap);
						if(orOrgBVo==null){
							LOGGER.error("Fail trans - dept not found ! - refDeptId:"+refDeptId);
							//ap.msg.notFound={0} 정보를 찾지 못했습니다. -cols.recvDept cols.refDept
							String message = messageProperties.getMessage("ap.msg.notFound", new String[]{"#cols.recvDept"}, locale);
							throw new CmException(message);
						}
						apOngdRecvDeptLVo.setRefDeptNm(orOrgBVo.getRescNm());//참조처명
					}
				}
			}
			
			apOngdRecvDeptLVo.setModrUid(userUid);
			apOngdRecvDeptLVo.setModDt("sysdate");
			apOngdRecvDeptLVoList.add(apOngdRecvDeptLVo);
		}
		return apOngdRecvDeptLVoList;
	}
	
	/** 문서기본, 본문HTML 데이터 모으기 - 진행문서기본(AP_ONGD_B), 진행문서본문내역(AP_ONGD_BODY_L) */
	private ApOngdBVo processBody(QueryQueue queryQueue, Map<String, String> paramMap,
			UserVo userVo, Locale locale, ModelMap model,
			ApOngdApvLnDVo myTurnApOngdApvLnDVo, ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave, 
			Map<Integer, OrUserBVo> userCacheMap, Map<Integer, OrOrgBVo> orgCacheMap,
			Map<String, String> optConfigMap) throws SQLException, CmException, IOException{
		
		boolean isNewDoc = false;
		
		// 결재문서 - 진행문서기본(AP_ONGD_B) 테이블
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		
		String apvNo = paramMap.get("apvNo");// 결재번호
		
		String statCd = paramMap.get("statCd");// 상태코드 - 문서상태코드 or 결재상태코드
		String bodyHtml = paramMap.get("bodyHtml");// 본문HTML
		String docLangTypCd = paramMap.get("docLangTypCd");// 문서언어구분코드
		
		// 진행문서기본(AP_ONGD_B) 테이블 VO
		ApOngdBVo storedApOngdBVo = null;// 이미 저장 되어있는 문서 조회용
		
		// 결재번호 - KEY 가 있으면 저장된 데이터 조회
		if(apvNo!=null && !apvNo.isEmpty()){
			storedApOngdBVo = new ApOngdBVo();
			storedApOngdBVo.setApvNo(apvNo);
			storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		}
		
		if(storedApOngdBVo==null){
			Long no = apCmSvc.createNo("AP_ONGD_B");
			apvNo = (no==null) ? null : no.toString();
			isNewDoc = true;
		} else {
			// 이력번호 세팅
			apOngdBVo.setAttHstNo(storedApOngdBVo.getAttHstNo());
			apOngdBVo.setRecvDeptHstNo(storedApOngdBVo.getRecvDeptHstNo());
			apOngdBVo.setRefDocHstNo(storedApOngdBVo.getRefDocHstNo());
			apOngdBVo.setDocLangTypCd(storedApOngdBVo.getDocLangTypCd());
			// 분류정보
			apOngdBVo.setClsInfoId(storedApOngdBVo.getClsInfoId());
			apOngdBVo.setDocTypCd(storedApOngdBVo.getDocTypCd());
			apOngdBVo.setMakDt(storedApOngdBVo.getMakDt());
			
			docLangTypCd = storedApOngdBVo.getDocLangTypCd();
			apOngdBVo.setDocLangTypCd(docLangTypCd);
			
			apOngdBVo.setCompId(storedApOngdBVo.getCompId());//for - messenger
			apOngdBVo.setDocSubj(storedApOngdBVo.getDocSubj());//for - messenger
			
			// 알림 메일 보내기일 경우
			if("Y".equals(optConfigMap.get("sendMailNoti"))){
				apOngdBVo.setMakDeptNm(storedApOngdBVo.getMakDeptNm());// 기안부서 - 메일 내용에 사용
				apOngdBVo.setMakrNm(storedApOngdBVo.getMakrNm());//기안자 - 메일 내용에 사용
			}
			
			model.put("storedApOngdBVo", storedApOngdBVo);
		}
		
		// 변수 매핑
		VoUtil.fromMap(apOngdBVo, paramMap);
		if(isNewDoc) apOngdBVo.setApvNo(apvNo);// 결재번호 - KEY
		if(apOngdBVo.getIntgNo()!=null && apOngdBVo.getIntgTypCd()==null){
			if("mak".equals(statCd) || "temp".equals(statCd)){
				ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
				apErpIntgBVo.setIntgNo(apOngdBVo.getIntgNo());
				apErpIntgBVo = (ApErpIntgBVo)commonDao.queryVo(apErpIntgBVo);
				
				if(apErpIntgBVo != null){
					if(ArrayUtil.isInArray(ApConstant.INTG_TYP_CDS, apErpIntgBVo.getIntgTypCd())){
						apOngdBVo.setIntgTypCd(apErpIntgBVo.getIntgTypCd());//연계구분코드
					} else {
						apOngdBVo.setIntgTypCd("ERP");//연계구분코드 - ERP로 세팅함
					}
				}
			}
		}
		// 등록대장등록여부 - Y 면 - 예약 등록일 지워줌
		if("Y".equals(apOngdBVo.getRegRecLstRegYn())){
			apOngdBVo.setRegRecLstRegSkedYmd("");
		}
		
		String bodyHstNo = null;
		if("mak".equals(statCd) || "temp".equals(statCd)){
			
			// 신규 양식으로 만들때만 양식ID 넘기고 체크함
			String formId = paramMap.get("formId");// 양식ID
			if(formId!=null){
				// 양식일련번호
				String formSeq = apFormSvc.storeOngoForm(queryQueue, formId, model);
				if(formSeq!=null) apOngdBVo.setFormSeq(formSeq);
				
				// 바닥글 세팅
				String footerVa = (String)model.get("footerVa");
				footerVa = replaceFooterVa(footerVa, userVo.getDeptId());
				apOngdBVo.setFooterVa(footerVa);
			}
			
			//문서 비밀번호 - 암호화
			if(apOngdBVo.getDocPw()!=null && !apOngdBVo.getDocPw().isEmpty()){
				apOngdBVo.setDocPwEnc(cryptoSvc.encryptPersanal(apOngdBVo.getDocPw()));
			} else {
				if("mak".equals(statCd) || "temp".equals(statCd)){
					if(!isNewDoc && storedApOngdBVo.getDocPwEnc()!=null && !storedApOngdBVo.getDocPwEnc().isEmpty()){
						apOngdBVo.setDocPwEnc("");
					}
				}
			}
			
			// 시행문의 경우 - 시행범위,발신명의 
			if("extro".equals(apOngdBVo.getDocTypCd())){
				String rescVa = apRescSvc.getOrRescVa(apOngdBVo.getSendrNmRescId(), docLangTypCd);
				apOngdBVo.setSendrNmRescNm(rescVa);//발신명의리소스명
			} else {
				apOngdBVo.setEnfcScopCd("");//시행범위코드 - dom:대내, for:대외, both:대내외
				apOngdBVo.setSendrNmRescId("");//발신명의리소스ID
				apOngdBVo.setSendrNmRescNm("");//발신명의리소스명
				apOngdBVo.setEnfcDocKeepPrdCd("");//시행문서보존기간코드
			}
			
			// 기안자 설정
			OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), docLangTypCd, userCacheMap);
			if(orUserBVo!=null){
				// 기안자 - 설정
				apOngdBVo.setMakrUid(orUserBVo.getUserUid());
				apOngdBVo.setMakrNm(orUserBVo.getRescNm());
			}
			OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(userVo.getDeptId(), docLangTypCd, orgCacheMap);
			if(orOrgBVo!=null){
				// 기안 부서 - 설정
				apOngdBVo.setMakDeptId(orOrgBVo.getOrgId());
				apOngdBVo.setMakDeptNm(orOrgBVo.getRescNm());
			}
			apOngdBVo.setMakDt("sysdate");//기안일시 - 저장일시(임시저장일시)
			
			// 임시저장
			if("temp".equals(statCd)){
				// 기안자 재검토 중 - 임시저장 할때 - 임시저장 상태로 바꾸지 않기 위함 - reRevw:재검토
				if(myTurnApOngdApvLnDVo==null || (
						!"retrvMak".equals(apOngdBVo.getDocStatCd())
						&& !"reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd()) ) ){
					// 문서상태코드
					apOngdBVo.setDocStatCd("temp");//temp:임시저장
					// 문서승인상태코드
					apOngdBVo.setDocProsStatCd("temp");//temp:임시저장
				}
			// 1인결재 - 임시저장이 아닌 경우
			} else if("byOne".equals(apOngdBVo.getApvLnTypCd())){
				// 완결일시
				apOngdBVo.setCmplDt("sysdate");
				// 완결자
				apOngdBVo.setCmplrUid(apOngdBVo.getMakrUid());
				apOngdBVo.setCmplrNm(apOngdBVo.getMakrUid());
				// 문서상태코드
				apOngdBVo.setDocStatCd("byOne");//byOne:1인결재
				// 문서승인상태코드
				apOngdBVo.setDocProsStatCd("apvd");//apvd:승인
			}
			
		// 기안, 임시저장 이 - 아닌 경우
		} else {
			apOngdBVo.setFormId(null);//양식ID - update 안하도록 지워줌
			apOngdBVo.setFormSeq(null);//양식일련번호 - update 안하도록 지워줌
		}
		
		// 예전 저장된 첨부로 우선 세팅함 - 첨부 처리 쪽에서 나머지 처리함
		if(storedApOngdBVo!=null){
			apOngdBVo.setAttFileYn(storedApOngdBVo.getAttFileYn());
		}
		
		
		//////////////////////////////////////
		//  본문
		String formBodyHTML = paramMap.get("formBodyHTML");
		String noBodyHtml = paramMap.get("noBodyHtml");
		if("Y".equals(noBodyHtml)){
			bodyHtml = formBodyHTML;
		}
		
		// 기안, 임시저장
		if("mak".equals(statCd) || "temp".equals(statCd)){
			
			if(bodyHtml==null || bodyHtml.isEmpty()){
				LOGGER.error("Fail trans - no body html ! - apvNo:"+apvNo);
				// ap.trans.noBody=본문 내용이 없습니다.
				throw new CmException("ap.trans.noBody", locale);
			}
			
			// 본문이력번호
			bodyHstNo = "1";
			apOngdBVo.setBodyHstNo(bodyHstNo);//본문이력번호
//			apOngdBVo.setAttHstNo("1");//첨부이력번호
//			apOngdBVo.setRecvDeptHstNo("1");//수신처이력번호
//			apOngdBVo.setRefDocHstNo("1");//참조문서이력번호
			
			ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(apvNo, bodyHstNo, bodyHtml, userVo);
			if(isNewDoc){
				// 결재문서 - 진행문서기본(AP_ONGD_B)
				queryQueue.insert(apOngdBVo);
				//진행문서본문내역(AP_ONGD_BODY_L) 테이블
				queryQueue.insert(apOngdBodyLVo);
			} else {
				// 결재문서 - 진행문서기본(AP_ONGD_B)
				queryQueue.update(apOngdBVo);
				
				// 기존 진행문서본문내역 - 삭제
				ApOngdBodyLVo storedApOngdBodyLVo = new ApOngdBodyLVo();
				storedApOngdBodyLVo.setApvNo(apvNo);
				queryQueue.delete(storedApOngdBodyLVo);
				
				// 진행문서본문내역(AP_ONGD_BODY_L) 테이블
				queryQueue.insert(apOngdBodyLVo);
			}
			
		} else {
			// 결재문서 - 진행문서기본(AP_ONGD_B)
			queryQueue.update(apOngdBVo);
			
			// 보류
			if("hold".equals(statCd)){
				
				// 보류본문이력번호
				String holdBodyHstNo = myTurnApOngdApvLnDVo.getHoldBodyHstNo();
				
				// 본문 전송된 것이 있으면
				if(bodyHtml!=null && !bodyHtml.isEmpty()){
					
					// 저장된 본문 조회 - 진행문서기본 의 본문이력번호로 조회
					ApOngdBodyLVo storedApOngdBodyLVo = new ApOngdBodyLVo();
					storedApOngdBodyLVo.setApvNo(apvNo);
					storedApOngdBodyLVo.setBodyHstNo(storedApOngdBVo.getBodyHstNo());
					storedApOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(storedApOngdBodyLVo);
					
					// 저장된 본문과 전송된 본문이 동일 할 경우
					if(storedApOngdBodyLVo != null && bodyHtml.equals(storedApOngdBodyLVo.getBodyHtml())){
						
						// 보류본문이력번호 - 가 있으면
						if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
							// 보류본문 데이터 삭제
							deleteApOngdBodyLVo(apvNo, holdBodyHstNo, queryQueue);
							// 보류본문이력번호 - 삭제 (내 결재라인 정보에서)
							myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo("");
						}
						
					// 저장된 본문과 전송된 본문이 다름
					} else {
						
						// 보류본문이력번호 가 있으면
						if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
							// 본문 VO 생성 - UPDATE
							ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(apvNo, holdBodyHstNo, bodyHtml, userVo);
							queryQueue.update(apOngdBodyLVo);
						} else {
							// 보류본문이력번호 - 생성
							holdBodyHstNo = apCmSvc.addNo(storedApOngdBVo.getBodyHstNo(), 1);
							// 본문 VO 생성 - INSERT
							ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(apvNo, holdBodyHstNo, bodyHtml, userVo);
							queryQueue.insert(apOngdBodyLVo);
							// 보류본문이력번호 - 세팅 (내 결재라인 정보에)
							myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo(holdBodyHstNo);
						}
					}
					
				}
				
			// 재검토, 반려
			} else if("reRevw".equals(statCd) || "rejt".equals(statCd)){
				
				// 보류본문이력번호
				String holdBodyHstNo = myTurnApOngdApvLnDVo.getHoldBodyHstNo();
				// 보류본문이력번호 - 가 있으면
				if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
					// 보류본문 데이터 삭제
					deleteApOngdBodyLVo(apvNo, holdBodyHstNo, queryQueue);
					// 보류본문이력번호 - 삭제 (내 결재라인 정보에서)
					myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo("");
				}
				
			// 승인, 찬성(합의), 반대(합의)
			} else {
				
				// 이전본문이력번호
				String prevBodyHstNo = myTurnApOngdApvLnDVo.getPrevBodyHstNo();
				// 보류본문이력번호
				String holdBodyHstNo = myTurnApOngdApvLnDVo.getHoldBodyHstNo();
				
				// 본문 변경이 없을때
				if(bodyHtml==null || bodyHtml.isEmpty()){
					
					// 보류본문이력번호 - 가 있으면
					if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
						// 본문이력번호 - 보류본문이력번호로 변경함
						apOngdBVo.setBodyHstNo(holdBodyHstNo);
						// 보류본문이력번호 - 삭제 (내 결재라인 정보에서)
						myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo("");
						
						if(prevBodyHstNo==null || prevBodyHstNo.isEmpty()){
							// 이전본문이력번호 - 현재 저장되어 있는 본문이력번호 세팅
							myTurnApOngdApvLnDVoToSave.setPrevBodyHstNo(storedApOngdBVo.getBodyHstNo());
						}
					}
					
				// 본문 변경이 있을때
				} else {
					
					// 저장된 본문 조회
					ApOngdBodyLVo storedApOngdBodyLVo = new ApOngdBodyLVo();
					storedApOngdBodyLVo.setApvNo(apvNo);
					storedApOngdBodyLVo.setBodyHstNo(storedApOngdBVo.getBodyHstNo());
					storedApOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(storedApOngdBodyLVo);
					
					// 저장된 본문과 전송된 본문이 동일 할 경우
					if(storedApOngdBodyLVo != null && bodyHtml.equals(storedApOngdBodyLVo.getBodyHtml())){
						
						// 보류본문이력번호 - 가 있으면
						if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
							// 보류본문 데이터 삭제
							deleteApOngdBodyLVo(apvNo, holdBodyHstNo, queryQueue);
							// 보류본문이력번호 - 삭제 (내 결재라인 정보에서)
							myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo("");
						}
					// 저장된 본문과 전송된 본문이 다를 경우
					} else {
						
						// 보류본문이력번호 가 있으면
						if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
							// 본문 VO 생성 - UPDATE
							ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(apvNo, holdBodyHstNo, bodyHtml, userVo);
							queryQueue.update(apOngdBodyLVo);
							// 보류본문이력번호 - 삭제 (내 결재라인 정보에서)
							myTurnApOngdApvLnDVoToSave.setHoldBodyHstNo("");
							// 본문이력번호 - 보류일련번호 사용
							apOngdBVo.setBodyHstNo(holdBodyHstNo);
							
							if(prevBodyHstNo==null || prevBodyHstNo.isEmpty()){
								// 이전본문이력번호 - 현재 저장되어 있는 본문이력번호 세팅
								myTurnApOngdApvLnDVoToSave.setPrevBodyHstNo(storedApOngdBVo.getBodyHstNo());
							}
						// 보류본문이력번호 가 없으면
						} else {
							// 본문이력번호 - 생성 - 다음번호 생성
							bodyHstNo = apCmSvc.addNo(storedApOngdBVo.getBodyHstNo(), 1);
							// 본문 VO 생성 - INSERT
							ApOngdBodyLVo apOngdBodyLVo = createApOngdBodyLVo(apvNo, bodyHstNo, bodyHtml, userVo);
							queryQueue.insert(apOngdBodyLVo);
							// 본문이력번호
							apOngdBVo.setBodyHstNo(bodyHstNo);
							
							if(prevBodyHstNo==null || prevBodyHstNo.isEmpty()){
								// 이전본문이력번호 - 현재 저장되어 있는 본문이력번호 세팅
								myTurnApOngdApvLnDVoToSave.setPrevBodyHstNo(storedApOngdBVo.getBodyHstNo());
							}
						}
						
					}
				}
			}
			
		}
		
		return apOngdBVo;
	}
	
	/** 본문 Vo 생성 리턴 */
	private ApOngdBodyLVo createApOngdBodyLVo(String apvNo, String bodyHstNo, String bodyHtml, UserVo userVo){
		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
		apOngdBodyLVo.setApvNo(apvNo);
		apOngdBodyLVo.setBodyHstNo(bodyHstNo);
		apOngdBodyLVo.setModDt("sysdate");
		apOngdBodyLVo.setModrUid(userVo.getUserUid());
		apOngdBodyLVo.setBodyHtml(bodyHtml);
		return apOngdBodyLVo;
	}
	/** 본문 삭제 */
	private void deleteApOngdBodyLVo(String apvNo, String bodyHstNo, QueryQueue queryQueue){
		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
		apOngdBodyLVo.setApvNo(apvNo);
		apOngdBodyLVo.setBodyHstNo(bodyHstNo);
		queryQueue.delete(apOngdBodyLVo);
	}
	/** [이전변경이력] 삭제 - [재검토] 의 [재검토] 가 되면 다음 결재자가 [이전변경이력]이 있을 수 있음 */
	private void clearPrevHst(ApOngdApvLnDVo apOngdApvLnDVo){
		if(!isEmpty(apOngdApvLnDVo.getPrevBodyHstNo())){
			apOngdApvLnDVo.setPrevBodyHstNo(null);
		}
		if(!isEmpty(apOngdApvLnDVo.getPrevApvLnHstNo())){
			apOngdApvLnDVo.setPrevApvLnHstNo(null);
		}
		if(!isEmpty(apOngdApvLnDVo.getPrevAttHstNo())){
			apOngdApvLnDVo.setPrevAttHstNo(null);
		}
		if(!isEmpty(apOngdApvLnDVo.getPrevRecvDeptHstNo())){
			apOngdApvLnDVo.setPrevRecvDeptHstNo(null);
		}
	}
	
	/** 문서 조회 */
	private ApOngdBVo queryApOngdBVo(String apvNo, String calledBy, UserVo userVo, Locale locale) throws CmException, SQLException{
		
		//결재번호 체크
		if(apvNo==null || apvNo.isEmpty()){
			LOGGER.error("Fail "+calledBy+" - no apvNo ! - userUid:"+userVo.getUserUid());
			//ap.msg.noApvNo=결재번호가 정확하지 않습니다.
			throw new CmException("ap.msg.noApvNo", locale);
		}
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo) commonDao.queryVo(apOngdBVo);
		if(apOngdBVo==null){
			LOGGER.error("Fail "+calledBy+" : no doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
		}
		return apOngdBVo;
	}
	
	/** 기안자, 관리자 체크 - 권한체크 */
	private void checkMakr(ApOngdBVo apOngdBVo, 
			String actMsgId, String userAuth,
			UserVo userVo, Locale locale, ModelMap model)
			throws CmException{
		// 기안자가 아니고, 관리자가 아니면
		if(		!userVo.getUserUid().equals(apOngdBVo.getMakrUid())
				&& !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, apOngdBVo.getMakrUid())
				&& !SecuUtil.hasAuth(userVo, userAuth, "A", null)){
			LOGGER.error("Fail trx - action auth ! - apvNo:"+apOngdBVo.getApvNo()+"  userUid:"+userVo.getUserUid());
			//ap.msg.notActUser={0} 할 수 있는 사용자가 아닙니다.
			throw new CmException("ap.msg.notActUser", new String[]{"#"+actMsgId}, locale);
		}
	}
	
	/** 대장등록, 비밀번호 해제(삭제) */
	public void processDocByOwner(String process, QueryQueue queryQueue, String apvNo, UserVo userVo, Locale locale) throws CmException, SQLException{
		if("regRegRecLst".equals(process) || "delDocPw".equals(process)){
			// 문서 조회하여 기안자 확인
			ApOngdBVo apOngdBVo = queryApOngdBVo(apvNo, process, userVo, locale);
			if(!userVo.getUserUid().equals(apOngdBVo.getMakrUid())
					&& !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, apOngdBVo.getMakrUid())
					){
				// ap.msg.notMakr=해당문서의 기안자가 아닙니다.
				throw new CmException("ap.msg.notMakr", locale);
			}
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			if("regRegRecLst".equals(process)) {
				apOngdBVo.setRegRecLstRegYn("Y");//등록대장등록여부
				apOngdBVo.setRegRecLstRegSkedYmd("");//등록대장등록예정년월일
			} else if("delDocPw".equals(process)) {
				apOngdBVo.setDocPwEnc("");
			}
			queryQueue.update(apOngdBVo);
		}
	}
	
	/** 비밀번호 삭제(관리자), 보안등급 변경(관리자) */
	public void processDocByAdmin(String process, QueryQueue queryQueue, String apvNos, String seculCd, UserVo userVo, Locale locale) throws CmException, SQLException{
		
		// 비빌번호 삭제 - 관리자
		if("delDocPwByAdm".equals(process)){
			ApOngdBVo apOngdBVo;
			for(String apvNo : apvNos.split(",")){
				if(apvNo==null || apvNo.isEmpty()) continue;
				
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setDocPwEnc("");
				queryQueue.update(apOngdBVo);
			}
		// 보안등급 변경 - 관리자
		} else if("changeSeculCdByAdm".equals(process)){
			ApOngdBVo apOngdBVo;
			for(String apvNo : apvNos.split(",")){
				if(apvNo==null || apvNo.isEmpty()) continue;
				
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo.setSeculCd(seculCd==null ? "" : seculCd);
				queryQueue.update(apOngdBVo);
			}
		}
	}
	
	/** 문서 삭제 */
	public void processDelDocs(QueryQueue queryQueue, String apvNos,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, IOException, CmException {
		
		int delCount = 0;
		boolean hasMsg = false;
		StringBuilder msgBuilder = new StringBuilder(256);
		ApOngdBVo apOngdBVo;
		String[] msgParam = new String[]{"#cm.btn.del"};
		
		boolean isByAdmin = userVo.getUserUid()!=null && userVo.getUserUid().equals("U0000001");
		
		for(String apvNo : apvNos.split(",")){
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
			
			if(userVo.getUserUid()!=null && !isByAdmin
					&& !userVo.getUserUid().equals(apOngdBVo.getMakrUid())
					&& !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, apOngdBVo.getMakrUid())
					){
				if(hasMsg) msgBuilder.append('\n');
				else hasMsg = true;
				// ap.msg.notMakr=해당문서의 기안자가 아닙니다.
				msgBuilder.append(messageProperties.getMessage("ap.msg.notMakr", msgParam, locale) + " : "+apOngdBVo.getDocSubj());
				continue;
			}
			
			if(apOngdBVo == null) continue;
			
			String intgNo = apOngdBVo.getIntgNo();
			String docStatCd = apOngdBVo.getDocStatCd();
			String intgTypCd = apOngdBVo.getIntgTypCd();
			
			boolean isIntgDocDel = intgNo!=null && ("retrvMak".equals(docStatCd) || "mak".equals(docStatCd) || "temp".equals(docStatCd));
			
			if(!"temp".equals(docStatCd) && !"rejt".equals(docStatCd) && !"retrvMak".equals(docStatCd) && !"mak".equals(docStatCd) && !isByAdmin){
				if(hasMsg) msgBuilder.append('\n');
				else hasMsg = true;
				// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
				msgBuilder.append(messageProperties.getMessage("ap.msg.not.stat", msgParam, locale) + " : "+apOngdBVo.getDocSubj());
				continue;
			}
			if(apOngdBVo.getRecLstTypCd() != null && !apOngdBVo.getRecLstTypCd().isEmpty() && !isByAdmin){
				// ap.msg.notDel.recLst=대장에 등록된 문서는 삭제 할 수 없습니다.
				msgBuilder.append(messageProperties.getMessage("ap.msg.notDel.recLst", locale) + " : "+apOngdBVo.getDocSubj());
				continue;
			}
			
			// 진행문서기본(AP_ONGD_B) 테이블 - 삭제
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			queryQueue.delete(apOngdBVo);
			
			// 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블 - 삭제
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			queryQueue.delete(apOngdApvLnDVo);
			
			// 진행문서결재라인이력(AP_ONGD_APV_LN_H) 테이블 - 삭제
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setHistory();
			queryQueue.delete(apOngdApvLnDVo);
			
			// 진행문서보류의견상세(AP_ONGD_HOLD_OPIN_D) 테이블 - 삭제
			ApOngdHoldOpinDVo apOngdHoldOpinDVo = new ApOngdHoldOpinDVo();
			apOngdHoldOpinDVo.setApvNo(apvNo);
			queryQueue.delete(apOngdHoldOpinDVo);
			
			// 진행문서본문내역(AP_ONGD_BODY_L) 테이블 - 삭제
			ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
			apOngdBodyLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdBodyLVo);
			
			// 진행문서첨부파일내역(AP_ONGD_ATT_FILE_L) 테이블 - 삭제
			ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdAttFileLVo);
			
			// 진행문서수신처내역(AP_ONGD_RECV_DEPT_L) 테이블 - 삭제
			ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
			apOngdRecvDeptLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdRecvDeptLVo);
			
			// 진행문서참조문서내역(AP_ONGD_REF_DOC_L) 테이블 - 삭제
			ApOngdRefDocLVo apOngdRefDocLVo = new ApOngdRefDocLVo();
			apOngdRefDocLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdRefDocLVo);
			
			// 진행문서참조열람상세(AP_ONGD_REF_VW_D) 테이블
			ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
			apOngdRefVwDVo.setApvNo(apvNo);
			queryQueue.delete(apOngdRefVwDVo);
			
			if(isByAdmin){
				ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
				apOngdExDVo.setApvNo(apvNo);
				queryQueue.delete(apOngdExDVo);
				
				ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				queryQueue.delete(apOngdErpFormDVo);
				
				ApOngdExtnDocDVo apOngdExtnDocDVo = new ApOngdExtnDocDVo();
				apOngdExtnDocDVo.setApvNo(apvNo);
				queryQueue.delete(apOngdExtnDocDVo);
				
				ApOngdPubBxDVo apOngdPubBxDVo = new ApOngdPubBxDVo();
				apOngdPubBxDVo.setApvNo(apvNo);
				queryQueue.delete(apOngdPubBxDVo);
				
				ApOngdPubBxCnfmLVo apOngdPubBxCnfmLVo = new ApOngdPubBxCnfmLVo();
				apOngdPubBxCnfmLVo.setApvNo(apvNo);
				queryQueue.delete(apOngdExtnDocDVo);
				
				ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
				apOngdSendDVo.setApvNo(apvNo);
				queryQueue.delete(apOngdSendDVo);
				
				ApOngdTrxDVo apOngdTrxDVo = new ApOngdTrxDVo();
				apOngdTrxDVo.setTrxApvNo(apvNo);
				queryQueue.delete(apOngdTrxDVo);
			}
			
			if(isIntgDocDel && !isByAdmin){
				// ERP 삭제 통보
				AsyncNotiData asyncNotiData = createErpNotiData(intgTypCd, intgNo, apvNo, "cncl", null, queryQueue, locale);
				if(asyncNotiData!=null){
					apErpNotiSvc.processErpNoti(asyncNotiData);
				}
			}
			
			delCount++;
		}
		
		if(delCount>0){
			if(hasMsg) msgBuilder.append("\n\n");
			// ap.msg.delResult={0} 건의 문서가 삭제 되었습니다.
			msgBuilder.append(messageProperties.getMessage("ap.msg.delResult", new String[]{delCount+""}, locale));
		}
		
		model.put("message", msgBuilder.toString());
	}
	
	/** 문서관리 보내기 */
	public void processSendToDm(QueryQueue queryQueue, String apvNos, String bxId, String dmData,
			UserVo userVo, Locale locale, ModelMap model, HttpServletRequest request) throws SQLException, IOException, CmException {
		if(apvNos==null || apvNos.isEmpty()) return;
		
		String[] nos =  apvNos.indexOf(',') > 0 ? apvNos.split(",") : new String[]{apvNos};
		JSONObject jsonObject = (JSONObject)JSONValue.parse(dmData);
		
		ApOngdBVo apOngdBVo;
		Map<String,String> dmMap;
		Object obj;
		String key, value;
		
		@SuppressWarnings("rawtypes")
		Iterator iterator;
		
		for(String apvNo : nos){
			
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
			
			if(apOngdBVo!=null){
				
				dmMap = new HashMap<String,String>();
				iterator = jsonObject.keySet().iterator();
				
				while(iterator.hasNext()){
					obj = iterator.next();
					if(obj==null) continue;
					key = obj.toString();
					obj = jsonObject.get(key);
					if(obj==null) continue;
					value = obj.toString();
					dmMap.put(key, value);
				}
				
				dmMap.put("refUrl", "/dm/doc/viewApDocPop.do?bxId="+bxId+"&apvNo="+apvNo);
				
				dmMap.put("fromTyp", "ap");
				dmMap.put("subj", apOngdBVo.getDocSubj());
				dmMap.put("regrUid", apOngdBVo.getMakrUid());
				dmMap.put("refId", apOngdBVo.getApvNo());
			}
			
			dmMap = new HashMap<String,String>();
			iterator = jsonObject.keySet().iterator();
			
			while(iterator.hasNext()){
				obj = iterator.next();
				if(obj==null) continue;
				key = obj.toString();
				obj = jsonObject.get(key);
				if(obj==null) continue;
				value = obj.toString();
				dmMap.put(key, value);
			}
			
			dmMap.put("refUrl", "/dm/doc/viewApDocPop.do?bxId="+bxId+"&apvNo="+apvNo);
			
			dmMap.put("fromTyp", "ap");
			dmMap.put("subj", apOngdBVo.getDocSubj());
			dmMap.put("regrUid", apOngdBVo.getMakrUid());
			dmMap.put("refId", apOngdBVo.getApvNo());
			dmMap.put("langTypCd", apOngdBVo.getDocLangTypCd());
			
			dmDocSvc.sendDoc(request, queryQueue, dmMap, null);
		}
	}
	
	/** 분류변경 */
	public void processChngCls(QueryQueue queryQueue, String apvNos, String clsInfoId, 
			ModelMap model, HttpServletRequest request) throws SQLException, IOException, CmException {
		if(apvNos==null || apvNos.isEmpty()) return;
		
		String[] nos =  apvNos.indexOf(',') > 0 ? apvNos.split(",") : new String[]{apvNos};
		ApOngdBVo apOngdBVo;
		String storage;
		for(String apvNo : nos){
			// 저장소 조회
			storage = apCmSvc.queryStorage(apvNo);
			
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setStorage(storage);
			apOngdBVo.setClsInfoId(clsInfoId);
			queryQueue.update(apOngdBVo);
		}
	}
	
	/** 개인 분류변경 */
	public void processPsnChngCls(QueryQueue queryQueue, String apvNos, String psnClsInfoId, 
			ModelMap model, HttpServletRequest request) throws SQLException, IOException, CmException {
		if(apvNos==null || apvNos.isEmpty()) return;
		
		String[] nos =  apvNos.indexOf(',') > 0 ? apvNos.split(",") : new String[]{apvNos};
		ApOngdBVo apOngdBVo;
		String storage;
		for(String apvNo : nos){
			// 저장소 조회
			storage = apCmSvc.queryStorage(apvNo);
			
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setStorage(storage);
			apOngdBVo.setPsnClsInfoId(psnClsInfoId);
			queryQueue.update(apOngdBVo);
		}
	}
	
	/** 통보추가 */
	public void processAddInfm(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, 
			String apvNo, String apvrUids, UserVo userVo, ModelMap model, Locale locale) throws SQLException, CmException{
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 진행문서기본(AP_ONGD_B) 테이블 - 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setQueryLang(locale.getLanguage());
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo == null){
			//ap.msg.noDoc=해당 문서가 없습니다.
			model.put("message", messageProperties.getMessage("ap.msg.noDoc", locale));
			return;
		}
		
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		// 결재라인이력번호
		String apvLnHstNo = null;
		Integer apvLnNo = 0;
		// 기존 통보자 조회
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setQueryLang(docLangTypCd);
		apOngdApvLnDVo.setApvLnPno("0");
		
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
		
		// 결재선에 있는 UID 목록
		List<String> alreadyList = new ArrayList<String>();
		int i, size = apOngdApvLnDVoList==null ? 0 : apOngdApvLnDVoList.size();
		for(i=0;i<size;i++){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			
			if(i==0) apvLnHstNo = apOngdApvLnDVo.getApvLnHstNo();
			if(i==size-1) apvLnNo = Integer.valueOf(apOngdApvLnDVo.getApvLnNo());
			
			// 부서가 아닌 사용자 
			if(!"Y".equals(apOngdApvLnDVo.getApvrDeptYn()) 
					&& apOngdApvLnDVo.getApvrUid()!=null && !apOngdApvLnDVo.getApvrUid().isEmpty()){
				alreadyList.add(apOngdApvLnDVo.getApvrUid());
			}
		}
		
		int addCnt = 0;
		// 사용자 정보
		OrUserBVo orUserBVo;
		// 부서정보
		OrOrgBVo orOrgBVo;
		Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
		
		for(String apvrUid : apvrUids.split(",")){
			// 기존 결재라인 사용자 제외
			if(!alreadyList.contains(apvrUid)){
				apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVo.setApvNo(apvNo);
				apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
				apOngdApvLnDVo.setApvLnPno("0");
				apvLnNo++;
				apOngdApvLnDVo.setApvLnNo(apvLnNo.toString());
				apOngdApvLnDVo.setApvrRoleCd("psnInfm");
				apOngdApvLnDVo.setApvStatCd("inInfm");
				apOngdApvLnDVo.setApvrDeptYn("N");
				apOngdApvLnDVo.setPrevApvrApvDt("sysdate");
				
				// 사용자 정보
				orUserBVo = apRescSvc.getOrUserBVo(apvrUid, docLangTypCd, null);
				if(orUserBVo == null){
					LOGGER.error("Fail trans - user not found ! - apvrUid:"+apvrUid);
					String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.user"}, locale)
							+ " - apvrUid : " + apvrUid;
					throw new CmException(message);
				}
				apOngdApvLnDVo.setApvrUid(apvrUid);
				apOngdApvLnDVo.setApvrNm(orUserBVo.getRescNm());
				apOngdApvLnDVo.setApvrPositCd(orUserBVo.getPositCd());
				apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());
				apOngdApvLnDVo.setApvrTitleCd(orUserBVo.getTitleCd());
				apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());
				
				// 부서정보
				orOrgBVo = apRescSvc.getOrOrgBVo(orUserBVo.getDeptId(), docLangTypCd, orgCacheMap);
				if(orOrgBVo==null){
					LOGGER.error("Fail trans - dept not found ! - apvDeptId:"+orUserBVo.getDeptId());
					//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
					String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
							+ " - apvDeptId : " + orUserBVo.getDeptId();
					throw new CmException(message);
				}
				
				apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
				apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
				String rescVa = orOrgBVo.getOrgAbbrRescNm();
				if(rescVa==null || rescVa.isEmpty()){
					apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
				} else {
					apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
				}
				
				queryQueue.insert(apOngdApvLnDVo);
				
				//[옵션] msgPsnInfm=통보 메세지 사용
				if("Y".equals(optConfigMap.get("msgPsnInfm"))){
					addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
				}
				addCnt++;
			}
		}
		
		if(addCnt==0){
			// ap.msg.noOneToInfm=통보할 사용자가 없습니다.
			model.put("message", messageProperties.getMessage("ap.msg.noOneToInfm", locale));
		} else if(addCnt==1) {
			// ap.msg.infmTo1=1명에게 통보하였습니다.
			model.put("message", messageProperties.getMessage("ap.msg.infmTo1", locale));
		} else {
			// ap.msg.infmTo2="{0}"명에게 통보하였습니다.
			model.put("message", messageProperties.getMessage("ap.msg.infmTo2", new String[]{Integer.toString(addCnt)}, locale));
		}
	}
	
	/** 시스템 강제 반려 */
	public void forceRejt(QueryQueue queryQueue, String apvNo, ModelMap model, Locale locale) throws SQLException, CmException, IOException {
		
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		
		if(storedApOngdBVo==null){
			// ap.msg.noDoc=해당 문서가 없습니다.
			throw new CmException("ap.msg.noDoc", locale);
		} else if(!"ongo".equals(storedApOngdBVo.getDocProsStatCd())){
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.msg.forceRejt=시스템 반려
			throw new CmException("ap.msg.notStat", new String[]{"#ap.btn.forceRejt"}, locale);
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		
		// ap.msg.forceRejt=시스템 반려
		String opin = "["+messageProperties.getMessage("ap.btn.forceRejt", SessionUtil.toLocale(storedApOngdBVo.getDocLangTypCd()))
				+" : "+StringUtil.getCurrDateTime()+"]";
		apOngdApvLnDVo.setApvOpinCont(opin);
		apOngdApvLnDVo.setVwDt("");
		queryQueue.update(apOngdApvLnDVo);
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setDocProsStatCd("rejt");
		apOngdBVo.setDocStatCd("rejt");
		apOngdBVo.setCmplDt("sysdate");
		queryQueue.update(apOngdBVo);
		

		// 결재 알림 처리 - [ERP에서 시작한 결재]:S
		AsyncNotiData asyncNotiData = null;
		String intgNo = storedApOngdBVo.getIntgNo();
		String intgTypCd = storedApOngdBVo.getIntgTypCd();
		
		if(intgNo!=null && !intgNo.isEmpty()){
			
			// 연계상태코드
			String intgStatCd = "rejt";
			
			asyncNotiData = createErpNotiData(intgTypCd, intgNo, apvNo, intgStatCd, 
					storedApOngdBVo.getDocNo(), queryQueue, locale);
			
			// ERP 연계 파일 데이터 삭제
			ApErpIntgFileDVo apErpIntgFileDVo = new ApErpIntgFileDVo();
			apErpIntgFileDVo.setIntgNo(intgNo);
			queryQueue.delete(apErpIntgFileDVo);
		}
		// 결재 알림 처리 - [ERP에서 시작한 결재]:E
		
		if(asyncNotiData!=null){
			apErpNotiSvc.processErpNoti(asyncNotiData);
		}
		
		// 연차
		if(ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, storedApOngdBVo.getXmlTypId())){
			
			ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
			apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
			
			if(apOngdErpFormDVo!=null){
				String formBodyXML = apOngdErpFormDVo.getErpVa();
				if(formBodyXML != null && !formBodyXML.isEmpty()){
					Map<String, String> wdNotiMap = new HashMap<String, String>();
					wdNotiMap.put("apvNo", apvNo);
					wdNotiMap.put("docSubj", storedApOngdBVo.getDocSubj());
					wdNotiMap.put("statCd", "rejt");
					wdNotiMap.put("formBodyXML", apOngdErpFormDVo.getErpVa());
					model.put("wdNotiMap", wdNotiMap);
				}
			}
		}
	}

	/** 결재비밀번호 체크 */
	public void checkSecuId(HttpServletRequest request, String secuId, String apvNo,UserVo userVo) throws SQLException, CmException{
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// [옵션] - 결재비밀번호사용 함 - 비밀번호 체크
		if(!"Y".equals(optConfigMap.get("notAlwApvPw"))){
			if(!apDocSecuSvc.confirmSecuId(request.getSession(), secuId)){
				LOGGER.error("Fail trans - apv pw not confirmed ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
				// ap.trans.notCfrmApvPw=결재비밀번호를 확인 할 수 없습니다.
				throw new CmException("ap.trans.notCfrmApvPw", request);
			}
		}
	}
	
	/** 일괄결재 처리 */
	public void processBulkApv(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, String apvNo,
			String apvLnPno, String apvLnNo, String menuId, StringBuilder formBodyXMLBuilder,
			UserVo userVo, Locale locale, ModelMap model, HttpServletRequest request) throws CmException, SQLException, IOException{
		
		ModelMap processModel = new ModelMap();
		
		// 문서정보 조회
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, "processBulkApv", userVo, locale);
		processModel.put("storedApOngdBVo", storedApOngdBVo);
		
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 공람처리
		if("pubVw".equals(storedApOngdBVo.getDocProsStatCd())){
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("apvNo", apvNo);
			paramMap.put("apvLnNo", apvLnNo);
			paramMap.put("menuId", menuId);
			
			Map<String, List<String>> paramListMap = null;
			String statCd = storedApOngdBVo.getDocStatCd();
			
			processVw(queryQueue, messengerQueue, paramMap, paramListMap, optConfigMap, true, statCd, userVo, locale, processModel);
			
			model.put("statNm", messageProperties.getMessage("ap.btn.cmplVw", locale));
			
		// 결재처리(승인, 합의승인)
		} else {
			
			// 저장할 문서
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setCompId(storedApOngdBVo.getCompId());//for - messenger
			apOngdBVo.setDocSubj(storedApOngdBVo.getDocSubj());//for - messenger
			apOngdBVo.setClsInfoId(storedApOngdBVo.getClsInfoId());
			apOngdBVo.setDocLangTypCd(storedApOngdBVo.getDocLangTypCd());
			apOngdBVo.setDocTypCd(storedApOngdBVo.getDocTypCd());
			apOngdBVo.setMakDt(storedApOngdBVo.getMakDt());
			apOngdBVo.setCmplDt(storedApOngdBVo.getCmplDt());
			queryQueue.update(apOngdBVo);
			
			// 결재선 정보 관리용 유틸 - 전체 경로선을 가지고 루트경로, 현재경로, 부서별 경로 등을 리턴함
			ApvLines apvLines = new ApvLines(queryApvLn(apvNo), apvLnPno);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
			Map<Integer, OrOrgBVo> orgCacheMap = new HashMap<Integer, OrOrgBVo>();
			
			// 결재라인VO목록 에서 해당 (결재할 차례의) 사용자의 결재라인VO 찾기
			ApOngdApvLnDVo myTurnApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, locale);
			
			if(myTurnApOngdApvLnDVo == null){
				LOGGER.error("Fail trans - no apvr ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
				//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
				throw new CmException("ap.msg.noApvr", locale);
			} else if(!ApDocUtil.isInApvOfApvStat(myTurnApOngdApvLnDVo.getApvStatCd())){
				
				LOGGER.error("Fail trans - not your turn ! apvNo:"+apvNo+"  userUid:"+userVo.getUserUid()+"  apvStatCd:"+myTurnApOngdApvLnDVo.getApvStatCd());
				// ap.msg.notMyTurn=결재 문서가 회수 되었거나 결재 할 차례가 아닙니다.
				throw new CmException("ap.msg.notMyTurn", locale);
				
			}
			
			// 자기가 이중결재의 처리부서에 있을 때 - 옵션의 최종결재자 필수 지정 여부를 확인해서
			//  - 필수 지정이면 최종 결재자(결재,전결)이 세팅 되었는지 확인 필요함
			// prcDept:처리부서
			// [옵션] needLastApvr=최종결재자 필수 지정
			if("prcDept".equals(myTurnApOngdApvLnDVo.getDblApvTypCd())
					&& "Y".equals(optConfigMap.get("needLastApvr"))){
				
				boolean hasLastApvr = false;
				List<ApOngdApvLnDVo> rootLines = apvLines.getRootLn();
				if(rootLines != null){
					for(ApOngdApvLnDVo apOngdApvLnDVo : rootLines){
						//apv:결재, pred:전결
						if("apv".equals(apOngdApvLnDVo.getApvrRoleCd()) || "pred".equals(apOngdApvLnDVo.getApvrRoleCd())){
							hasLastApvr = true;
							break;
						}
					}
				}
				if(!hasLastApvr){
					// ap.apvLn.canNotBulkWithoutLastApvr=최종 결재자({0} 또는 {1})가 지정되지 않아서 일괄 결재 할 수 없습니다.
					//'#ap.term.apv','#ap.term.pred'
					throw new CmException("ap.apvLn.canNotBulkWithoutLastApvr", new String[]{
							ptSysSvc.getTerm("ap.term.apv", locale.getLanguage()),
							ptSysSvc.getTerm("ap.term.pred", locale.getLanguage())}, locale);
				}
			}
			
			ApOngdApvLnDVo myTurnApOngdApvLnDVoToSave = new ApOngdApvLnDVo();
			myTurnApOngdApvLnDVoToSave.setApvNo(apvNo);
			myTurnApOngdApvLnDVoToSave.setApvLnPno(apvLnPno);
			myTurnApOngdApvLnDVoToSave.setApvLnNo(apvLnNo);
			
			String statCd = !"0".equals(apvLnPno) ? "pros" :
					ApDocUtil.isAgrOfApvrRole(myTurnApOngdApvLnDVo.getApvrRoleCd()) ? "pros" : "apvd";
			
			// 승인, 합의승인
			processApvLn(null, apvLines,
					paramMap, apOngdBVo, 
					userVo, userCacheMap, orgCacheMap, 
					locale, queryQueue, messengerQueue, optConfigMap, processModel, 
					apvLnPno, myTurnApOngdApvLnDVo, myTurnApOngdApvLnDVoToSave, statCd);
			
			model.put("statNm", ptSysSvc.getTerm("ap.term."+statCd, locale.getLanguage()));
			
			String docStatCd = apOngdBVo.getDocStatCd();
			
			// ERP 연계 처리
			String intgNo = storedApOngdBVo.getIntgNo();
			String prevDocStatCd = storedApOngdBVo.getDocStatCd();
			
			if(intgNo!=null && !intgNo.isEmpty()){
				
				String intgTypCd = storedApOngdBVo.getIntgTypCd();
				String intgStatCd = docStatCd;
				
				if(CustConfig.CUST_SENTEC){// 센텍
					String formId = storedApOngdBVo!=null ? storedApOngdBVo.getFormId() : apOngdBVo.getFormId();
					String apvrRoleCd = myTurnApOngdApvLnDVo==null ? null : myTurnApOngdApvLnDVo.getApvrRoleCd();
					
					// 심의  - 양식:구매품의-F000010R & revw2 
					if("apvd".equals(statCd) && "revw2".equals(apvrRoleCd) && "F000010R".equals(formId)){
						intgStatCd = "consi";//심의
					}
				}
				
				AsyncNotiData asyncNotiData = createErpNotiData(intgTypCd, intgNo, apvNo, intgStatCd, apOngdBVo.getDocNo(), queryQueue, locale);
				if(asyncNotiData!=null){
					apErpNotiSvc.processErpNoti(asyncNotiData);
				}
			}
			
			// 검색엔진 인덱싱 처리
			if(!docStatCd.equals(prevDocStatCd)){
				// 승인 되거나, 반려 후 - keepRejtDoc:반려된 문서 등록대장에 저장 [옵션]
				if("apvd".equals(docStatCd) || ("rejt".equals(docStatCd) && "Y".equals(optConfigMap.get("keepRejtDoc")))){
					// 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
					String recLstTypCd = storedApOngdBVo.getRecLstTypCd();
					if("Y".equals(storedApOngdBVo.getRegRecLstRegYn()) && recLstTypCd!=null && !recLstTypCd.isEmpty()){
						addSrchIndex(apOngdBVo, userVo, queryQueue);
					}
				}
			}
			
			// 결재 알림 처리 - [결재에서 시작 - ERP 통보]
			if("apvd".equals(docStatCd) && !docStatCd.equals(prevDocStatCd)){
				
				ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("formBodyHoldXML");
				apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				if(apOngdErpFormDVo==null){
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
					apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				}
				
				if(apOngdErpFormDVo!=null){
					String formBodyXML = apOngdErpFormDVo.getErpVa();
					if(formBodyXML != null && !formBodyXML.isEmpty()){
						// ERP 통보용
						formBodyXMLBuilder.append(formBodyXML);
						
						// 연차
						if(ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, storedApOngdBVo.getXmlTypId())){
							Map<String, String> wdNotiMap = new HashMap<String, String>();
							wdNotiMap.put("apvNo", apvNo);
							wdNotiMap.put("docSubj", storedApOngdBVo.getDocSubj());
							wdNotiMap.put("statCd", "apvd");
							wdNotiMap.put("formBodyXML", apOngdErpFormDVo.getErpVa());
							model.put("wdNotiMap", wdNotiMap);
						}
					}
				}
				
				
				// [일괄결재]업무관리 데이터 - 보류 데이터 처리
				
				// 업무관리 workNo 삭제용
				List<String> delWfWorkNoList = new ArrayList<String>();
				
				// 보류 데이터 조회
				apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNo(apvNo);
				apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
				ApOngdErpFormDVo storedHoldVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
				
				if(storedHoldVo != null){
					
					// 보류 저장된 workNo
					String wfWorkNo = storedHoldVo.getErpVa();
					
					// 보류 저장된 workNo 삭제
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfWorkNoHold");
					queryQueue.delete(apOngdErpFormDVo);
					
					// 현재의 workNo 조회
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfWorkNo");
					ApOngdErpFormDVo storedVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
					
					// 현 workNo 와 보류된 workNo 가 다르면 - 항상 그러함 
					if(storedVo != null && !wfWorkNo.equals(storedVo.getErpVa())){
						// 현 workNo - 삭제(업무관리)
						delWfWorkNoList.add(storedVo.getErpVa());
						
						// 현 workNo 를 보류된 workNo 로
						apOngdErpFormDVo = new ApOngdErpFormDVo();
						apOngdErpFormDVo.setApvNo(apvNo);
						apOngdErpFormDVo.setErpVaTypCd("wfWorkNo");
						apOngdErpFormDVo.setErpVa(wfWorkNo);
						queryQueue.update(apOngdErpFormDVo);
					}
				}
				
				if(!delWfWorkNoList.isEmpty()){
					// 업무관리 formNo 조회
					apOngdErpFormDVo = new ApOngdErpFormDVo();
					apOngdErpFormDVo.setApvNo(apvNo);
					apOngdErpFormDVo.setErpVaTypCd("wfFormNo");
					apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
					
					if(apOngdErpFormDVo!=null){
						String wfFormNo = apOngdErpFormDVo.getErpVa();
						
						// 업무관리 데이터 삭제
						for(String delWfWorkNo : delWfWorkNoList){
							wfMdFormSvc.deleteFormByAP(queryQueue, wfFormNo, delWfWorkNo);
						}
					}
				}
				
				// 자동 발송 처리용 - 현재문서가 완료됨 표시
				if("Y".equals(optConfigMap.get("autoSend"))){
					if("extro".equals(storedApOngdBVo.getDocTypCd())){
						request.setAttribute("needAutoSend", Boolean.TRUE);
					}
				}
				
				// 참조열람 - 이전 데이터 조회
				ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
				apOngdRefVwDVo.setApvNo(apvNo);
				apOngdRefVwDVo.setOrderBy("SORT_ORDR");
				@SuppressWarnings("unchecked")
				List<ApOngdRefVwDVo> apOngdRefVwDVoList = (List<ApOngdRefVwDVo>)commonDao.queryList(apOngdRefVwDVo);

				if(apOngdRefVwDVoList != null){
					for(ApOngdRefVwDVo storedApOngdRefVwDVo : apOngdRefVwDVoList){
						// 완료 안된것 - 참조열람안함 으로
						if(!"cmplRefVw".equals(storedApOngdRefVwDVo.getRefVwStatCd())){
							apOngdRefVwDVo = new ApOngdRefVwDVo();
							apOngdRefVwDVo.setApvNo(apvNo);
							apOngdRefVwDVo.setRefVwrUid(storedApOngdRefVwDVo.getRefVwrUid());
							apOngdRefVwDVo.setRefVwStatCd("noRefVw");//noRefVw:참조열람안함
							queryQueue.update(apOngdRefVwDVo);
						}
					}
				}
			}
			
			// 문서관리 보내기
			if("apvd".equals(docStatCd)){
				ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
				apOngdExDVo.setApvNo(apvNo);
				apOngdExDVo.setExId("sendToDm");
				apOngdExDVo = (ApOngdExDVo)commonDao.queryVo(apOngdExDVo);
				if(apOngdExDVo != null){
					boolean changed = sendToDm(request, model, queryQueue, apOngdBVo, apOngdExDVo, "regRecLst");
					if(changed){
						queryQueue.update(apOngdExDVo);
					}
				}
			}
			
		}
		
	}
	
	/** 공람완료 */
	public void setCmplVw(QueryQueue queryQueue, String apvNo,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, CmException {
		
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, "setCmplVw", userVo, locale);
		if( !"makVw".equals(storedApOngdBVo.getDocStatCd())// makVw:담당
				|| !"pubVw".equals(storedApOngdBVo.getDocProsStatCd())){// pubVw:공람
			LOGGER.error("Fail setCmplVw - stat -  apvNo:"+apvNo
					+"  docProsStatCd:"+storedApOngdBVo.getDocProsStatCd()
					+"  docStatCd:"+storedApOngdBVo.getDocStatCd()
					+"  userUid:"+userVo.getUserUid());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 공람완료
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn.cmplVw"}, locale);
		}
		
		// 문서 - 공람완료로
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setDocStatCd("cmplVw");//cmplVw:공람완료
		queryQueue.update(apOngdBVo);
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		apOngdApvLnDVo.setApvDt("sysdate");
		apOngdApvLnDVo.setApvStatCd("cmplVw");//cmplVw:공람완료
		queryQueue.update(apOngdApvLnDVo);
		
	}
	/** 공람게시 */
	public void processSetPubBx(QueryQueue queryQueue, String apvNos,
			String pubBxDeptId, String pubBxEndYmd, String regDeptId, String langTypCd, 
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, CmException {
		

		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		String docLangTypCd = langTypCd;
		boolean useSessionLangCd = "session".equals(optConfigMap.get("distDocLangTypCd"));
		
		ApOngdPubBxDVo apOngdPubBxDVo;
		ApOngdBVo apOngdBVo;
		
		Map<String, OrUserBVo> userBylangCdMap = new HashMap<String, OrUserBVo>();
		Map<String, OrOrgBVo > deptBylangCdMap = new HashMap<String, OrOrgBVo >();
		
		// 사용자 정보 조회
		OrUserBVo orUserBVo;
		// 부서 정보 조회
		OrOrgBVo orOrgBVo;
		
		for(String apvNo : apvNos.split(",")){
			
			if(!useSessionLangCd){
				// 기존 문서 조회 - 문서 언어 체크
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
				if(apOngdBVo==null){
					LOGGER.error("Fail trans - setPubBx - no doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid());
					// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
					throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
				}
				docLangTypCd = apOngdBVo.getDocLangTypCd();
			}
			
			if(regDeptId==null || regDeptId.isEmpty()) regDeptId = userVo.getUserUid();
			
			// 사용자 정보 조회
			orUserBVo = userBylangCdMap.get(docLangTypCd);
			if(orUserBVo==null){
				orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), docLangTypCd, null);
				userBylangCdMap.put(docLangTypCd, orUserBVo);
			}
			
			// 조직 정보 조회
			orOrgBVo = deptBylangCdMap.get(docLangTypCd);
			if(orOrgBVo==null){
				orOrgBVo = apRescSvc.getOrOrgBVo(regDeptId, docLangTypCd, null);
				deptBylangCdMap.put(docLangTypCd, orOrgBVo);
			}
			
			// 공람 저장
			apOngdPubBxDVo = new ApOngdPubBxDVo();
			apOngdPubBxDVo.setPubBxDeptId(pubBxDeptId);
			apOngdPubBxDVo.setApvNo(apvNo);
			apOngdPubBxDVo.setPubBxDt("sysdate");
			apOngdPubBxDVo.setPubBxEndYmd(pubBxEndYmd);
			apOngdPubBxDVo.setRegrUid(userVo.getUserUid());
			if(orUserBVo!=null) apOngdPubBxDVo.setRegrNm(orUserBVo.getRescNm());
			apOngdPubBxDVo.setRegDeptId(regDeptId);
			if(orOrgBVo!=null) apOngdPubBxDVo.setRegDeptNm(orOrgBVo.getRescNm());
			
			queryQueue.store(apOngdPubBxDVo);
		}
		
	}

	/** 접수문서 담당 지정 */
	public void processSetMakVw(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, String apvNo,
			String apvrUid,
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{

		String process = "setMakVw";
		
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, process, userVo, locale);
		if(!"recv".equals(storedApOngdBVo.getDocStatCd()) 
				&& !"makVw".equals(storedApOngdBVo.getDocStatCd())){ // recv:접수, makVw:담당
			LOGGER.error("Fail setMakVw - stat -  apvNo:"+apvNo
					+"  docStatCd:"+storedApOngdBVo.getDocStatCd()
					+"  userUid:"+userVo.getUserUid());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 담당자지정
			throw new CmException("ap.msg.notStat", new String[]{"#ap.btn.setPich"}, locale);
		}
		
		// 결재선 삭제 - 오류방지용(원래 필요없는 로직)
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		queryQueue.delete(apOngdApvLnDVo);
		
		// 담당 - INSERT
		apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);//결재번호 - KEY
		apOngdApvLnDVo.setApvLnHstNo("1");//결재라인이력번호
		apOngdApvLnDVo.setApvLnPno("0");//결재라인부모번호 - KEY
		apOngdApvLnDVo.setApvLnNo("1");//결재라인번호 - KEY
		apOngdApvLnDVo.setApvrRoleCd("makVw");//결재자역할코드 - makVw:담당
		apOngdApvLnDVo.setApvStatCd("inVw");//결재상태코드 - inVw:공람중
		apOngdApvLnDVo.setPrevApvrApvDt("sysdate");//이전결재자결재일시
		apOngdApvLnDVo.setApvrUid(apvrUid);//결재자UID
		apOngdApvLnDVo.setApvrDeptYn("N");//결재자부서여부
		
		// 메신저 보내기
		addMesseger(storedApOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
		
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		// 결재선 데이터에 문서언어에 따른 리스스 바인딩
		setApOngdApvLnDVoResc(apOngdApvLnDVo, storedApOngdBVo.getDocLangTypCd(), null, null, optConfigMap, locale);
		
		queryQueue.insert(apOngdApvLnDVo);
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);//결재번호
		apOngdBVo.setCurApvrId(apOngdApvLnDVo.getApvrUid());
		apOngdBVo.setCurApvrNm(apOngdApvLnDVo.getApvrNm());
		apOngdBVo.setCurApvrDeptYn("N");
		apOngdBVo.setDocProsStatCd("pubVw");
		apOngdBVo.setDocStatCd("makVw");
		queryQueue.update(apOngdBVo);
	}
	
	/** 배부용 발송정보 데이터 생성 */
	private void createSendDVoForDist(String apvNo, String orgnApvNo, 
			List<JSONObject> distList, String langTypCd, QueryQueue queryQueue)
			throws SQLException{
		
		ApOngdSendDVo apOngdSendDVo, storedApOngdSendDVo;
		String sendSeq, recvDeptId;
		OrOrgBVo orOrgBVo;
		if(distList != null){
			for(JSONObject jsonObject : distList){
				sendSeq = (String)jsonObject.get("sendSeq");
				if(sendSeq==null || sendSeq.isEmpty()){// 신규로 추가된 것만 발송
					recvDeptId = (String)jsonObject.get("recvDeptId");
					
					apOngdSendDVo = new ApOngdSendDVo();
					apOngdSendDVo.setApvNo(apvNo);
					apOngdSendDVo.setRecvDeptId(recvDeptId);//수신처ID
					
					// 수신처명 리소스 조회
					orOrgBVo = apRescSvc.getOrOrgBVo(recvDeptId, langTypCd, null);
					if(orOrgBVo != null && orOrgBVo.getRescNm()!=null && !orOrgBVo.getRescNm().isEmpty()){
						apOngdSendDVo.setRecvDeptNm(orOrgBVo.getRescNm());//수신처명
					} else {
						apOngdSendDVo.setRecvDeptNm((String)jsonObject.get("recvDeptNm"));//수신처명
					}

					// 중복발송 체크
					storedApOngdSendDVo = new ApOngdSendDVo();
					storedApOngdSendDVo.setApvNo(orgnApvNo);
					storedApOngdSendDVo.setRecvDeptId(recvDeptId);
					// befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수
					storedApOngdSendDVo.setWhereSqllet("AND HDL_STAT_CD IN ('befoRecv', 'recvCmpl', 'recvRetn', 'recvRetrv')");
					if(commonDao.count(storedApOngdSendDVo)>0){
						apOngdSendDVo.setHdlStatCd("dupSend");//dupSend:중복발송
					} else {
						apOngdSendDVo.setHdlStatCd("befoRecv");//befoRecv:접수대기
					}
					
					apOngdSendDVo.setPrevApvrApvDt("sysdate");
					queryQueue.insert(apOngdSendDVo);
				}
			}
		}
	}
		
	/** 대장문서 생성 */
	public ApOngdBVo createRecLstDoc(QueryQueue queryQueue, String apvNo,
			String sendSeq, String recLstTypCd, String clsInfoId,
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		//////////////////////
		// 문서 기본
		
		// 진행문서기본(AP_ONGD_B) 테이블 - 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setStorage(storage);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		if(apOngdBVo == null){
			LOGGER.error("Fail - create "+recLstTypCd+" : no doc -  apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
		}
		
		// 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		String orgnRecLstTypCd = apOngdBVo.getRecLstTypCd();
		
		// 결재번호 생성
		Long no = apCmSvc.createNo("AP_ONGD_B");
		String newApvNo = no.toString();
		apOngdBVo.setApvNo(newApvNo);
		
		
		//////////////////////
		// 시행 변환 정보
		
		// 변환결재번호 - 시행문 변환된 정보 세팅
		String trxApvNo = apOngdBVo.getTrxApvNo();
		boolean hasTrx = trxApvNo != null && !trxApvNo.isEmpty();
		if(hasTrx){
			// 진행문서변환상세(AP_ONGD_TRX_D) 테이블 - 조회
			ApOngdTrxDVo apOngdTrxDVo = new ApOngdTrxDVo();
			apOngdTrxDVo.setTrxApvNo(trxApvNo);
			apOngdTrxDVo.setStorage(storage);
			apOngdTrxDVo = (ApOngdTrxDVo)commonDao.queryVo(apOngdTrxDVo);
			if(apOngdTrxDVo == null){
				LOGGER.error("Fail - create "+recLstTypCd+" : no trx -  apvNo:"+apvNo+" trxApvNo:"+trxApvNo+"  userUid:"+userVo.getUserUid());
				// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.transEnfcDoc=시행변환문
				throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.transEnfcDoc"}, locale);
			}
			// 변환정보 세팅
			apOngdBVo.setFormId(apOngdTrxDVo.getFormId());//양식ID
			apOngdBVo.setFormSeq(apOngdTrxDVo.getFormSeq());//양식일련번호
			apOngdBVo.setBodyHghtPx(apOngdTrxDVo.getBodyHghtPx());//본문높이픽셀
			apOngdBVo.setFormWdthTypCd(apOngdTrxDVo.getFormWdthTypCd());//양식넓이구분코드
			apOngdBVo.setFooterVa(apOngdTrxDVo.getFooterVa());//바닥글값
		}
		

		//////////////////////
		// 발송 상세 조회 - 문서를 어느 부서에 둘 것인지 알기 위함
		
		// 진행문서발송상세(AP_ONGD_SEND_D) 테이블 - 조회
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo.setStorage(storage);
		apOngdSendDVo = (ApOngdSendDVo)commonDao.queryVo(apOngdSendDVo);
		if(apOngdSendDVo == null){
			LOGGER.error("Fail - create "+recLstTypCd+" : no send - apvNo:"+apvNo+"  sendSeq:"+sendSeq+"  userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.btn.sendDoc=발송
			throw new CmException("ap.trans.notFound", new String[]{"#ap.btn.sendDoc"}, locale);
		}
		apOngdBVo.setRecLstTypCd(recLstTypCd);//대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		if("recvRecLst".equals(recLstTypCd)){
			apOngdBVo.setDocStatCd("recv");
		} else if("distRecLst".equals(recLstTypCd)){
			apOngdBVo.setDocStatCd("dist");
		}
		
		if(recLstTypCd.equals("distRecLst")){//distRecLst:배부 대장
			// 문서과 또는 대리문서과 조회
			String crdOrgId = apCmSvc.getCrdOrgId(userVo.getDeptId());
			if(crdOrgId==null) crdOrgId = userVo.getDeptId();
			apOngdBVo.setRecLstDeptId(crdOrgId);//대장부서ID - 오너부서ID
		} else {
			apOngdBVo.setRecLstDeptId(apOngdSendDVo.getRecvDeptId());//대장부서ID - 오너부서ID
		}
		
		apOngdBVo.setOrgnApvNo(apvNo); //원본결재번호
		apOngdBVo.setTrxApvNo(null);
		apOngdBVo.setRecvDt("sysdate");
		apOngdBVo.setUgntDocYn("N");
		apOngdBVo.setDocPwEnc(null);// 문서 비밀번호 지워줌
		apOngdBVo.setClsInfoId(clsInfoId);//분류정보ID
		
		queryQueue.insert(apOngdBVo);
		
		// 발송부서 처리상태 변경
		String hdlStatCd = apOngdSendDVo.getHdlStatCd();
		apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo.setHdlDt("sysdate");
		apOngdSendDVo.setHdlrUid(userVo.getUserUid());
		// 어권별 사용자 조회
		OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), apOngdBVo.getDocLangTypCd(), null);
		apOngdSendDVo.setHdlrNm(orUserBVo==null ? userVo.getUserNm() : orUserBVo.getRescNm());
		if("befoRecv".equals(hdlStatCd)){//befoRecv:접수대기, recvCmpl:접수완료
			apOngdSendDVo.setHdlStatCd("recvCmpl");
		} else if("befoDist".equals(hdlStatCd)){//befoDist:배부대기, distCmpl:배부완료
			apOngdSendDVo.setHdlStatCd("distCmpl");
		} else {
			LOGGER.error("Fail - create "+recLstTypCd+" : not stat -  apvNo:"+apvNo
					+"  sendSeq:"+sendSeq
					+"  hdlStatCd:"+hdlStatCd
					+"  userUid:"+userVo.getUserUid());
			// recLstTypCd:대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.btn.recv=접수, ap.btn.dist=배부
			throw new CmException("ap.msg.noApvr", new String[]{"recvRecLst".equals(recLstTypCd) ? "#ap.btn.recv" : "#ap.btn.dist"}, locale);
		}
		queryQueue.update(apOngdSendDVo);
		
		
		//////////////////////
		// 결재 라인
		
		// 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setStorage(storage);
		// 배부대장에서 온 문서면 - 이행 테이블에 결재선 정보가 있으므로 세팅함
		if("distRecLst".equals(orgnRecLstTypCd)){
			apOngdApvLnDVo.setExecution();
		}
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
		if(apOngdApvLnDVoList != null){
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
				storedApOngdApvLnDVo.setApvNo(newApvNo);
				storedApOngdApvLnDVo.setApvLnHstNo("1");
				storedApOngdApvLnDVo.setExecution();
				queryQueue.insert(storedApOngdApvLnDVo);
			}
		}

		
		//////////////////////
		// 결재 본문
		
		// 진행문서본문내역(AP_ONGD_BODY_L) 테이블
		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
		if(hasTrx){
			apOngdBodyLVo.setApvNo(trxApvNo);
			apOngdBodyLVo.setBodyHstNo("1");
		} else {
			apOngdBodyLVo.setApvNo(apvNo);
			apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
		}
		apOngdApvLnDVo.setStorage(storage);
		apOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(apOngdBodyLVo);
		if(apOngdBodyLVo == null){
			LOGGER.error("Fail - create "+recLstTypCd+" : no body - apvNo:"+(hasTrx ? trxApvNo : apvNo)+"  bodyHstNo:"+(hasTrx ? "1" : apOngdBVo.getBodyHstNo())+"  userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.bodyHtml=결재본문
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.bodyHtml"}, locale);
		}
		apOngdBodyLVo.setApvNo(newApvNo);
		apOngdBodyLVo.setBodyHstNo("1");
		queryQueue.insert(apOngdBodyLVo);

		// 본문 이력 초기화
		apOngdBVo.setBodyHstNo("1");
		
		
		//////////////////////
		// 첨부 파일
		
		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
		if(hasTrx){
			apOngdAttFileLVo.setApvNo(trxApvNo);
			apOngdAttFileLVo.setAttHstNo("1");
		} else {
			apOngdAttFileLVo.setApvNo(apvNo);
			apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
		}
		apOngdAttFileLVo.setStorage(storage);
		@SuppressWarnings("unchecked")
		List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonDao.queryList(apOngdAttFileLVo);
		if(apOngdAttFileLVoList != null){
			for(ApOngdAttFileLVo storedApOngdAttFileLVo : apOngdAttFileLVoList){
				storedApOngdAttFileLVo.setApvNo(newApvNo);
				storedApOngdAttFileLVo.setAttHstNo("1");
				queryQueue.insert(storedApOngdAttFileLVo);
			}
		}
		
		// 첨부 이력 초기화
		apOngdBVo.setAttHstNo("1");

		
		//////////////////////
		// 수신처 파일
		
		ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
		apOngdRecvDeptLVo.setApvNo(apvNo);
		apOngdRecvDeptLVo.setRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());
		apOngdRecvDeptLVo.setStorage(storage);
		
		@SuppressWarnings("unchecked")
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
		if(apOngdRecvDeptLVoList != null){
			for(ApOngdRecvDeptLVo storedApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
				storedApOngdRecvDeptLVo.setApvNo(newApvNo);
				storedApOngdRecvDeptLVo.setRecvDeptHstNo("1");
				queryQueue.insert(storedApOngdRecvDeptLVo);
			}
		}
		
		// 수신처 이력 초기화
		apOngdBVo.setRecvDeptHstNo("1");

		
		//////////////////////
		// 참조 문서
		
		ApOngdRefDocLVo apOngdRefDocLVo = new ApOngdRefDocLVo();
		apOngdRefDocLVo.setApvNo(apvNo);
		apOngdRefDocLVo.setRefDocHstNo(apOngdBVo.getRefDocHstNo());
		apOngdRefDocLVo.setStorage(storage);
		
		@SuppressWarnings("unchecked")
		List<ApOngdRefDocLVo> apOngdRefDocLVoList = (List<ApOngdRefDocLVo>)commonDao.queryList(apOngdRefDocLVo);
		if(apOngdRefDocLVoList != null){
			for(ApOngdRefDocLVo storedApOngdRefDocLVo : apOngdRefDocLVoList){
				storedApOngdRefDocLVo.setApvNo(newApvNo);
				storedApOngdRefDocLVo.setRefDocHstNo("1");
				queryQueue.insert(storedApOngdRefDocLVo);
			}
		}

		// 참조문서 이력 초기화
		apOngdBVo.setRefDocHstNo("1");
		
		
		//////////////////////
		// 참조열람
		
		ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
		apOngdRefVwDVo.setApvNo(apvNo);
		apOngdRefVwDVo.setStorage(storage);
		
		@SuppressWarnings("unchecked")
		List<ApOngdRefVwDVo> apOngdRefVwDVoList = (List<ApOngdRefVwDVo>)commonDao.queryList(apOngdRefVwDVo);
		if(apOngdRefVwDVoList != null){
			for(ApOngdRefVwDVo storedApOngdRefVwDVo : apOngdRefVwDVoList){
				storedApOngdRefVwDVo.setApvNo(newApvNo);
				storedApOngdRefVwDVo.setExecution();
				queryQueue.insert(storedApOngdRefVwDVo);
			}
		}
		
		
		//////////////////////
		// ERP 양식 결재
		
		ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
		apOngdErpFormDVo.setApvNo(apvNo);
		@SuppressWarnings("unchecked")
		List<ApOngdErpFormDVo> apOngdErpFormDVoList = (List<ApOngdErpFormDVo>)commonDao.queryList(apOngdErpFormDVo);
		if(apOngdErpFormDVoList != null){
			for(ApOngdErpFormDVo storedApOngdErpFormDVo : apOngdErpFormDVoList){
				storedApOngdErpFormDVo.setApvNo(newApvNo);
				queryQueue.insert(storedApOngdErpFormDVo);
			}
		}
		
		//////////////////////
		// 업무관리 양식 - html로 왔을 경우 : 본문 스크립트 허용
		ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
		apOngdExDVo.setApvNo(apvNo);
		@SuppressWarnings("unchecked")
		List<ApOngdExDVo> apOngdExDVoList = (List<ApOngdExDVo>)commonDao.queryList(apOngdExDVo);
		if(apOngdExDVoList != null){
			for(ApOngdExDVo storedApOngdExDVo : apOngdExDVoList){
				if("sendToDm".equals(storedApOngdExDVo.getExId())) continue;
				
				storedApOngdExDVo.setApvNo(newApvNo);
				queryQueue.insert(storedApOngdExDVo);
			}
		}
		
		//////////////////////
		// 문서번호 생성
		
		apOngdApvLnDVo = createApOngdApvLnDVoByDeptId(apOngdBVo.getRecLstDeptId(), apOngdBVo.getDocLangTypCd(), locale);
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		apDocNoSvc.setDocNo(apOngdBVo, apOngdApvLnDVo, optConfigMap, recLstTypCd, locale);
		
		return apOngdBVo;
	}
	
	/** 부서ID로 결재선 정보 생성 - 문서번호 생성용 */
	private ApOngdApvLnDVo createApOngdApvLnDVoByDeptId(String apvDeptId, String docLangTypCd, Locale locale)
			throws SQLException, CmException{
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		// 부서정보
		OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, docLangTypCd, null);
		if(orOrgBVo==null){
			LOGGER.error("Fail create recLst - dept not found ! - apvDeptId:"+apvDeptId);
			//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
			String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
					+ apvDeptId+ " : " + apOngdApvLnDVo.getApvDeptNm();
			throw new CmException(message);
		}
		
		apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
		apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
		String rescVa = orOrgBVo.getOrgAbbrRescNm();
		if(rescVa==null || rescVa.isEmpty()){
			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
		} else {
			apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
		}
		return apOngdApvLnDVo;
	}
	
	/** 배부 처리 */
	public void processDist(QueryQueue queryQueue, String apvNo,
			String sendSeq, List<JSONObject> distList,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, CmException{

		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 배부함에서 배부 하는 것
		if(sendSeq!=null && !sendSeq.isEmpty()){
			// 대장문서 생성
			ApOngdBVo apOngdBVo = createRecLstDoc(queryQueue, apvNo,
					sendSeq, "distRecLst", null, //regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
					userVo, locale, model);
			
			// [히든옵션] 유통문서 어권 기준 - session:세션기준, orgnDoc:원본문서의 어권
			String langTypCd = "session".equals(optConfigMap.get("distDocLangTypCd")) ? 
					locale.getLanguage() : apOngdBVo.getDocLangTypCd();
			
			// 배부처 등록
			createSendDVoForDist(apOngdBVo.getApvNo(), apvNo, distList, langTypCd, queryQueue);
			
			// 배부대장 - 검색 인덱싱
			addSrchIndex(apOngdBVo, userVo, queryQueue);
			
		// 배부대장에서 재배부 하는 것
		} else {
			ApOngdBVo apOngdBVo = queryApOngdBVo(apvNo, "processDist", userVo, locale);
			
			// [히든옵션] 유통문서 어권 기준 - session:세션기준, orgnDoc:원본문서의 어권
			String langTypCd = "session".equals(optConfigMap.get("distDocLangTypCd")) ? 
					locale.getLanguage() : apOngdBVo.getDocLangTypCd();
			
			// 배부처 등록
			createSendDVoForDist(apvNo, apOngdBVo.getOrgnApvNo(), distList, langTypCd, queryQueue);
		}
	}
	/** 반송 처리 */
	public void processRetn(QueryQueue queryQueue, String apvNo,
			String sendSeq, String retnOpin, 
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		// 문서정보 조회
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		if(storedApOngdBVo==null){
			LOGGER.error("Fail trans - retnDoc - no doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
		}
		
		if(sendSeq==null || sendSeq.isEmpty()){
			LOGGER.error("Fail trans - missing parameter - no sendSeq ! - userUid:"+userVo.getUserUid());
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			throw new CmException("cm.msg.notValidCall", locale);
		}
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo = (ApOngdSendDVo) commonDao.queryVo(apOngdSendDVo);
		if(apOngdSendDVo == null){
			// ap.msg.noSendData=발송 데이터가 없습니다.
			throw new CmException("ap.msg.noSendData", locale);
		}
		
		// 처리상태코드 - befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수
		String hdlStatCd = apOngdSendDVo.getHdlStatCd();
		if(!"befoRecv".equals(hdlStatCd) && !"befoDist".equals(hdlStatCd)){
			LOGGER.error("Fail retnDoc - stat -  apvNo:"+apvNo
					+"  sendSeq:"+sendSeq
					+"  hdlStatCd:"+hdlStatCd
					+"  userUid:"+userVo.getUserUid());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 시행취소
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn.retn"}, locale);
		}
		
		apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo.setHdlStatCd("befoRecv".equals(hdlStatCd) ? "recvRetn" : "distRetn");
		apOngdSendDVo.setHdlrOpinCont(retnOpin);
		
		apOngdSendDVo.setHdlDt("sysdate");
		apOngdSendDVo.setHdlrUid(userVo.getUserUid());
		OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), storedApOngdBVo.getDocLangTypCd(), null);
		if(orUserBVo != null){
			apOngdSendDVo.setHdlrNm(orUserBVo.getRescNm());
		}
		
		queryQueue.update(apOngdSendDVo);
	}
	
	/** 회수, 재발송, 수동완료처리 - 접수확인 팝업 처리 */
	public void processCfrmRecv(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, String apvNo,
			String process, List<String> sendSeqs, String hdlrNm, String hdlDt,
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		// 발송상세테이블 맵형태로 조회
		Map<Integer, ApOngdSendDVo> apOngdSendDVoMap = loadApOngdSendDVoMap(apvNo);
		String hdlStatCd;
		ApOngdSendDVo apOngdSendDVo, storedApOngdSendDVo;
		
		ApOngdBVo storedApOngdBVo = null;
		
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		boolean msgRecvBxEnable = "Y".equals(optConfigMap.get("msgRecvBx"));// [옵션] 접수함 알림 사용
		if(msgRecvBxEnable){
			storedApOngdBVo = new ApOngdBVo();
			storedApOngdBVo.setApvNo(apvNo);
			storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		}
		
		if(sendSeqs!=null && !sendSeqs.isEmpty()){
			
			for(String sendSeq : sendSeqs){
				storedApOngdSendDVo = apOngdSendDVoMap.get(Integer.valueOf(sendSeq));
				if(storedApOngdSendDVo == null){
					// ap.msg.noSendData=발송 데이터가 없습니다.
					throw new CmException("ap.msg.noSendData", locale);
				}
				hdlStatCd = storedApOngdSendDVo.getHdlStatCd();
				
				apOngdSendDVo = new ApOngdSendDVo();
				apOngdSendDVo.setApvNo(apvNo);// 결재부서요청여부 - KEY
				apOngdSendDVo.setSendSeq(sendSeq);// 수신처일련번호 - KEY
				
				
				// 회수
				if("retrvDoc".equals(process)){
					
					if("befoRecv".equals(hdlStatCd)){//befoRecv:접수대기
						apOngdSendDVo.setHdlStatCd("recvRetrv");//recvRetrv:접수회수
						
					} else if("befoDist".equals(hdlStatCd)){//befoDist:배부대기
						apOngdSendDVo.setHdlStatCd("distRetrv");//distRetrv:배부회수
						
					} else if("manl".equals(hdlStatCd) || "manlCmpl".equals(hdlStatCd)){//manl:수동발송, manlCmpl:수동완료
						apOngdSendDVo.setHdlStatCd("manlRetrv");//manlRetrv:수동회수
						apOngdSendDVo.setHdlrNm("");
						apOngdSendDVo.setHdlDt("");
					} else {
						// ap.msg.canRetrvAftRecvd=접수대지, 배부대기, 수동발송 문서만 회수 할 수 있습니다.
						throw new CmException("ap.msg.canRetrvAftRecvd", locale);
					}
					queryQueue.update(apOngdSendDVo);
					
					
				// 재발송
				} else if("reSendDoc".equals(process)){
					apOngdSendDVo.setPrevApvrApvDt("sysdate");
					apOngdSendDVo.setVwDt("");
					
					// 이전에 반송 내역 있으면 삭제
					apOngdSendDVo.setHdlDt("");
					apOngdSendDVo.setHdlrOpinCont("");
					apOngdSendDVo.setHdlrUid("");
					apOngdSendDVo.setHdlrNm("");
					
					if("recvRetn".equals(hdlStatCd) || "recvRetrv".equals(hdlStatCd)){//recvRetn:접수반송, recvRetrv:접수회수
						apOngdSendDVo.setHdlStatCd("befoRecv");//befoRecv:접수대기
						
						
						if(msgRecvBxEnable && storedApOngdBVo!=null){
							
							// 메신저용 데이터 만들기
							ApOngdBVo apOngdBVo = new ApOngdBVo();
							apOngdBVo.setApvNo(apvNo);
							apOngdBVo.setCompId(storedApOngdBVo.getCompId());
							apOngdBVo.setEnfcStatCd("inSending");//시행상태코드 - 코드에 없으며 발송용 임시로 사용
							apOngdBVo.setDocSubj(storedApOngdBVo.getDocSubj());
							
							ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
							apOngdApvLnDVo.setApvrDeptYn("Y");
							apOngdApvLnDVo.setApvDeptId(storedApOngdSendDVo.getRecvDeptId());
							apOngdApvLnDVo.setSendSeq(storedApOngdSendDVo.getSendSeq());
							
							// 메신저 보내기
							addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
						}
						
					} else if("distRetn".equals(hdlStatCd) || "distRetrv".equals(hdlStatCd)){//distRetn:배부반송, distRetrv:배부회수
						apOngdSendDVo.setHdlStatCd("befoDist");//distRetrv:배부회수
						
					} else if("manlRetrv".equals(hdlStatCd)){//manlRetrv:수동회수 
						apOngdSendDVo.setHdlStatCd("manl");//manl:수동발송
						
					} else {
						// ap.msg.canReSendAftRetriv=회수되거나 반송된 문서만 재발송 할 수 있습니다.
						throw new CmException("ap.msg.canReSendAftRetriv", locale);
					}
					queryQueue.update(apOngdSendDVo);
					
				// 수동완료처리
				} else if("manlSendCmpl".equals(process)){
					
					if("manl".equals(hdlStatCd) || "manlRetrv".equals(hdlStatCd)){ // manl:수동발송, manlRetrv:수동회수
						apOngdSendDVo.setHdlStatCd("manlCmpl");//manlCmpl:수동완료
						apOngdSendDVo.setHdlrNm(hdlrNm);
						if(hdlDt!=null && hdlDt.length()==10){
							hdlDt = hdlDt+" 00:00:00";
						}
						apOngdSendDVo.setHdlDt(hdlDt);
					} else {
						// ap.msg.canManlCmplWhenOuter=수동발송(외부기관) 만 수동전송완료 처리 할 수 있습니다.
						throw new CmException("ap.msg.canManlCmplWhenOuter", locale);
					}
					queryQueue.update(apOngdSendDVo);
					
				}
			}
			
			// 회수
			if("retrvDoc".equals(process)){
				// ap.trans.processOk={0} 처리 하였습니다. - 회수
				String message = messageProperties.getMessage("ap.trans.processOk", new String[]{"#ap.btn.retrvDoc"}, locale);
				model.put("message", message);
				
			// 재발송
			} else if("reSendDoc".equals(process)){
				// ap.trans.processOk={0} 처리 하였습니다. - 재발송
				String message = messageProperties.getMessage("ap.trans.processOk", new String[]{"#ap.btn.reSendDoc"}, locale);
				model.put("message", message);
				
			// 수동완료처리
			} else if("manlSendCmpl".equals(process)){
				// ap.trans.submitOk={0} 하였습니다. - 수동완료처리
				String message = messageProperties.getMessage("ap.trans.submitOk", new String[]{"#ap.btn.manlSendCmpl"}, locale);
				model.put("message", message);
				
			}
		} else {
			// ap.msg.noSendData=발송 데이터가 없습니다.
			throw new CmException("ap.msg.noSendData", locale);
		}
		
	}
	
	/** 발송상세테이블 맵형태로 조회 */
	private Map<Integer, ApOngdSendDVo> loadApOngdSendDVoMap(String apvNo) throws SQLException{
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		@SuppressWarnings("unchecked")
		List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonDao.queryList(apOngdSendDVo);
		if(apOngdSendDVoList!=null){
			Map<Integer, ApOngdSendDVo> returnMap = new HashMap<Integer, ApOngdSendDVo>();
			for(ApOngdSendDVo storedApOngdSendDVo : apOngdSendDVoList){
				returnMap.put(Integer.valueOf(storedApOngdSendDVo.getSendSeq()), storedApOngdSendDVo);
			}
			return returnMap;
		}
		return null;
	}
	
	/** 자동발송 */
	public void processAutoSendDoc(String apvNo, UserVo userVo, Locale locale, ModelMap model){
		
		QueryQueue queryQueue = new QueryQueue();
		List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
		
		try {
			// 진행문서기본(AP_ONGD_B) - 조회
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
			if(apOngdBVo == null){
				LOGGER.warn("AUTO-SEND - No doc - apvNo:"+apvNo);
				return;
			}
			
			// 결재 옵션
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, apOngdBVo.getCompId());
			
			String trxApvNo = null;			// 변환결재번호
			String sendrNmRescId = null;	// 발신명의 리소스ID
			String sendrNmRescNm = null;	// 발신명의 리소스 명
			
			// [옵션] 참조문서 포함 - 자동발송
			String sendWithRefDocYn = optConfigMap.get("autoSendWithRefDoc");
			
			String ofsePath = null;
			String ofseHghtPx = null;
			
			// [옵션] 발신명의 날인 포함 - 자동발송
			if("Y".equals(optConfigMap.get("autoSendSign"))){
				// 발신명의조직ID
				String sendrNmOrgId = apOngdBVo.getSendrNmOrgId();
				
				OrOfseDVo orOfseDVo = new OrOfseDVo();
				orOfseDVo.setOrgId(sendrNmOrgId);
				orOfseDVo.setDftOfseYn("Y");
				orOfseDVo.setDisuYn("N");
				@SuppressWarnings("unchecked")
				List<OrOfseDVo> orOfseDVoList = (List<OrOfseDVo>)commonDao.queryList(orOfseDVo);
				if(orOfseDVoList != null){
					for(OrOfseDVo storedOrOfseDVo : orOfseDVoList){
						ofsePath = storedOrOfseDVo.getImgPath();
						//ofseHghtPx = storedOrOfseDVo.getImgHght();
						ofseHghtPx = "80";
						break;
					}
				}
			}
			
			List<ApOngdRecvDeptLVo> recvDeptList = null;
			
			processSendDoc(queryQueue, messengerQueue, 
					apvNo, trxApvNo, ofsePath, ofseHghtPx, 
					sendrNmRescId, sendrNmRescNm, sendWithRefDocYn, 
					apOngdBVo, recvDeptList, false, true, 
					userVo, locale, model);
			
			// 모바일 푸쉬
			List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
			addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
			
			commonSvc.execute(queryQueue);

			// 메신저 메세지 일괄 발송
			sendMesseger(messengerQueue, optConfigMap);
			// 모바일 푸쉬 메세지 - 보내기
			ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);

		} catch (CmException e) {
			LOGGER.warn("AUTO-SEND - apvNo:"+apvNo+"  "+e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 발송 */
	private void processSendDoc(QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			String apvNo, String trxApvNo, String ofsePath, String ofseHghtPx,
			String sendrNmRescId, String sendrNmRescNm, String sendWithRefDocYn,
			ApOngdBVo storedApOngdBVo, List<ApOngdRecvDeptLVo> recvDeptList, boolean inserted, boolean isAuto,
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		String process = "sendDoc";//로그용
		
		if( !"apvd".equals(storedApOngdBVo.getDocProsStatCd()) //apvd:승인
			|| "censrRejt".equals(storedApOngdBVo.getEnfcStatCd()) // censrRejt:심사반려
			|| "cnclEnfc".equals(storedApOngdBVo.getEnfcStatCd()) // cnclEnfc:시행취소
			){
			if(!isAuto){
				LOGGER.error("Fail sendDoc - stat -  apvNo:"+apvNo
						+"  docProsStatCd:"+storedApOngdBVo.getDocProsStatCd()
						+"  enfcStatCd:"+storedApOngdBVo.getEnfcStatCd()
						+"  userUid:"+userVo.getUserUid());
			}
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 발송
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn."+process}, locale);
		}
		
		// 수신처이력번호
		String recvDeptHstNo = storedApOngdBVo.getRecvDeptHstNo();
		
		// 문서 - 시행상태 - 발송 으로
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setTrxApvNo(trxApvNo);
		apOngdBVo.setEnfcStatCd("sent");//시행상태코드 - sent:발송
		if(ofsePath!=null) apOngdBVo.setOfsePath(ofsePath);//관인경로
		if(ofseHghtPx!=null) apOngdBVo.setOfseHghtPx(ofseHghtPx);//관인높이픽셀
		if(sendrNmRescId!=null) apOngdBVo.setSendrNmRescId(sendrNmRescId);//발신명의리소스ID
		if(sendrNmRescNm!=null) apOngdBVo.setSendrNmRescNm(sendrNmRescNm);//발신명의리소스명
		if(sendWithRefDocYn!=null) apOngdBVo.setSendWithRefDocYn(sendWithRefDocYn);//참조문서포함발송여부
		
		OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(storedApOngdBVo.getMakDeptId(), "G", storedApOngdBVo.getDocLangTypCd());
		if(orOrgTreeVo != null){
			apOngdBVo.setSendInstId(orOrgTreeVo.getOrgId());//발송기관ID
			apOngdBVo.setSendInstNm(orOrgTreeVo.getRescNm());//발송기관명
		}
		apOngdBVo.setEnfcDt("sysdate");
		queryQueue.update(apOngdBVo);
		
		// 진행문서수신처내역 - 발송 안된 데이터 조회
		ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
		apOngdRecvDeptLVo.setApvNo(apvNo);
		apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
		apOngdRecvDeptLVo.setSendYn("N");
		@SuppressWarnings("unchecked")
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = inserted ? recvDeptList : 
			(List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
		if(apOngdRecvDeptLVoList==null || apOngdRecvDeptLVoList.isEmpty()){
			if(!isAuto){
				LOGGER.error("Fail sendDoc - no recvDept -  apvNo:"+apvNo
						+"  userUid:"+userVo.getUserUid());
			}
			//ap.msg.noRecvDept=수신처가 지정되지 않았습니다.
			throw new CmException("ap.msg.noRecvDept", locale);
		}
		
		if(isAuto){
			boolean hasValidRecvDept = false;
			for(ApOngdRecvDeptLVo storedApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
				if("dom".equals(storedApOngdRecvDeptLVo.getRecvDeptTypCd())
						|| "for".equals(storedApOngdRecvDeptLVo.getRecvDeptTypCd())){
					hasValidRecvDept = true;
					break;
				}
			}
			if(!hasValidRecvDept){
				//ap.msg.noRecvDept=수신처가 지정되지 않았습니다.
				throw new CmException("ap.msg.noRecvDept", locale);
			}
		}
		
		checkDupRecvDept(apOngdRecvDeptLVoList, apvNo, locale);
		
		// 발송 테이블에 데이터 넣기
		sendToApOngdSendDVo(queryQueue, messengerQueue, storedApOngdBVo, apvNo, process, recvDeptHstNo, 
				apOngdRecvDeptLVoList, inserted, true, userVo, locale, model);
	}

	/** 추가 발송 */
	public void sendMoreRecvDept(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, 
			String apvNo, List<String> recvDeptList,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, CmException{
		
		String process = "sendMore";//로그용
		
		// 문서정보 조회
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		
		if( !"apvd".equals(storedApOngdBVo.getDocProsStatCd()) //apvd:승인
				|| "censrRejt".equals(storedApOngdBVo.getEnfcStatCd()) // censrRejt:심사반려
				|| "cnclEnfc".equals(storedApOngdBVo.getEnfcStatCd()) // cnclEnfc:시행취소
				){
			LOGGER.error("Fail sendDoc - stat -  apvNo:"+apvNo
					+"  docProsStatCd:"+storedApOngdBVo.getDocProsStatCd()
					+"  enfcStatCd:"+storedApOngdBVo.getEnfcStatCd()
					+"  userUid:"+userVo.getUserUid());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 발송
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn."+process}, locale);
		}
		
		// 수신처이력번호
		String recvDeptHstNo = storedApOngdBVo.getRecvDeptHstNo();
		
		// 최대 시퀀스 조회용
		ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
		apOngdRecvDeptLVo.setApvNo(apvNo);
		apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
		apOngdRecvDeptLVo.setOrderBy("RECV_DEPT_SEQ DESC");
		apOngdRecvDeptLVo.setPageNo(1);
		apOngdRecvDeptLVo.setPageRowCnt(2);
		@SuppressWarnings("unchecked")
		List<ApOngdRecvDeptLVo> list = (List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
		
		// 최대 시퀀스
		Integer recvDeptSeq = null;
		if(list != null && !list.isEmpty()){
			recvDeptSeq = Integer.valueOf(list.get(0).getRecvDeptSeq());
		}
		
		// 전송된 수신처 파싱
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = parseRecvDeptJson(recvDeptList, apvNo,
				storedApOngdBVo.getDocLangTypCd(), null, locale, userVo.getUserUid(), recvDeptSeq);
		
		// 중복된 수신처 체크
		checkDupRecvDept(apOngdRecvDeptLVoList, apvNo, locale);
		
		// 발송된것 제거
		apOngdRecvDeptLVoList = removeSent(apOngdRecvDeptLVoList, recvDeptHstNo);
		
		// 발송 테이블에 데이터 넣기
		sendToApOngdSendDVo(queryQueue, messengerQueue,
				storedApOngdBVo, apvNo, process, recvDeptHstNo, 
				apOngdRecvDeptLVoList, false, false, userVo, locale, model);
	}
	/** 발송된것 제외 */
	private List<ApOngdRecvDeptLVo> removeSent(List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList, String recvDeptHstNo){
		if(apOngdRecvDeptLVoList==null) return null;
		List<ApOngdRecvDeptLVo> returnList = new ArrayList<ApOngdRecvDeptLVo>();
		for(ApOngdRecvDeptLVo apOngdRecvDeptLVo : apOngdRecvDeptLVoList){
			if(!"Y".equals(apOngdRecvDeptLVo.getSendYn())){
				apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
				apOngdRecvDeptLVo.setAddSendYn("Y");//추가발송여부
				returnList.add(apOngdRecvDeptLVo);
			}
		}
		return returnList;
	}
	
	/** 발송 - 발송 테이블에 데이터 넣기 */
	private void sendToApOngdSendDVo(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, 
			ApOngdBVo storedApOngdBVo, String apvNo, String process, String recvDeptHstNo,
			List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList, 
			boolean newInserted, // 변경된 수신처면 - 발송 할때 수신처를 변경 한 경우
			boolean oldStored, // 이미 저장되어 있는 있는 수신처 - 발송할때 수신처 변경 안함
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		// 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
		String recvDeptTypCd;
		// 진행문서수신처내역(AP_ONGD_RECV_DEPT_L) 테이블
		ApOngdRecvDeptLVo apOngdRecvDeptLVo;
		// 진행문서발송상세(AP_ONGD_SEND_D) 테이블
		ApOngdSendDVo apOngdSendDVo, storedApOngdSendDVo;
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		boolean msgRecvBxEnable = "Y".equals(optConfigMap.get("msgRecvBx"));// [옵션] 접수함 알림 사용
		Integer maxSendSeq = 1;
		
		if(msgRecvBxEnable){
			apOngdSendDVo = new ApOngdSendDVo();
			apOngdSendDVo.setApvNo(apvNo);
			apOngdSendDVo.setOrderBy("SEND_SEQ DESC");
			@SuppressWarnings("unchecked")
			List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonDao.queryList(apOngdSendDVo);
			if(apOngdSendDVoList != null && !apOngdSendDVoList.isEmpty()){
				maxSendSeq = Integer.parseInt(apOngdSendDVoList.get(0).getSendSeq()) + 1;
			}
		}
		
		for(ApOngdRecvDeptLVo storedApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
			
			// 변경된 수신처면 - 발송 할때 수신처를 변경 한 경우
			// - 현재 데이터를 앞의 로직에서 신규로 INSERT 하므로 데이터의 발송여부만 변경하여 발송 처리함
			if(newInserted){
				storedApOngdRecvDeptLVo.setSendYn("Y");//발송여부
			// 이미 저장되어 있는 있는 수신처 - 발송할때 수신처 변경 안함
			// - 저장된 레코드의 발송여부만 UPDATE 함
			} else if(oldStored){
				// 진행문서수신처내역 - 발송여부 UPDATE
				apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
				apOngdRecvDeptLVo.setRecvDeptHstNo(recvDeptHstNo);
				apOngdRecvDeptLVo.setRecvDeptSeq(storedApOngdRecvDeptLVo.getRecvDeptSeq());
				apOngdRecvDeptLVo.setSendYn("Y");//발송여부
				queryQueue.update(apOngdRecvDeptLVo);
			// 추가발송의 경우 - 넘겨온 데이터 INSERT
			// - 추가로 넘겨온 수신처 정보가 저장 되지 않았으므로 INSERT 처리
			} else {
				storedApOngdRecvDeptLVo.setSendYn("Y");//발송여부
				queryQueue.insert(storedApOngdRecvDeptLVo);
			}
			
			// 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
			recvDeptTypCd = storedApOngdRecvDeptLVo.getRecvDeptTypCd();
			
			// 진행문서발송상세 - INSERT
			apOngdSendDVo = new ApOngdSendDVo();
			apOngdSendDVo.setApvNo(apvNo);
			apOngdSendDVo.setRecvDeptNm(storedApOngdRecvDeptLVo.getRecvDeptNm());//수신처명
			apOngdSendDVo.setRefDeptNm(storedApOngdRecvDeptLVo.getRefDeptNm());//참조처명
			apOngdSendDVo.setRecvDeptTypCd(recvDeptTypCd);//수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
			apOngdSendDVo.setPrevApvrApvDt("sysdate");
			
			// 수신처구분코드 - dom:대내
			if("dom".equals(recvDeptTypCd)){
				
				// 접수함
				apOngdSendDVo.setHdlStatCd("befoRecv");//befoRecv:접수대기
				apOngdSendDVo.setRecvDeptId(storedApOngdRecvDeptLVo.getRecvDeptId());
				
			// 수신처구분코드 - for:대외
			} else if("for".equals(recvDeptTypCd)){
				
				// 참조처가 없으면 - 배부함
				if(storedApOngdRecvDeptLVo.getRefDeptId()==null || storedApOngdRecvDeptLVo.getRefDeptId().isEmpty()){
					apOngdSendDVo.setHdlStatCd("befoDist");//befoDist:배부대기
					apOngdSendDVo.setRecvDeptId(storedApOngdRecvDeptLVo.getRecvDeptId());
					
				// 참조처가 있으면 - 접수함
				} else {
					apOngdSendDVo.setHdlStatCd("befoRecv");//befoRecv:접수대기
					apOngdSendDVo.setRecvDeptId(storedApOngdRecvDeptLVo.getRefDeptId());
				}
				
			// 수신처구분코드 - outOrg:외부기관
			} else if("outOrg".equals(recvDeptTypCd)){
				apOngdSendDVo.setHdlStatCd("manl");//manl:수동발송
			}
			
			// 중복발송여부 체크 - 배부함에서 배부한 부서로 기안부서에서 추가 발송하는 경우인지 체크
			// - 추가발송이고(!oldStored && !newInserted), 수신처ID가 있으면
			if(!oldStored && !newInserted && apOngdSendDVo.getRecvDeptId()!=null){
				storedApOngdSendDVo = new ApOngdSendDVo();
				storedApOngdSendDVo.setOrgnApvNo(apvNo);
				storedApOngdSendDVo.setRecvDeptId(apOngdSendDVo.getRecvDeptId());
				// befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수
				storedApOngdSendDVo.setWhereSqllet("AND HDL_STAT_CD IN ('befoRecv', 'recvCmpl', 'recvRetn', 'recvRetrv')");
				if(commonDao.count(storedApOngdSendDVo)>0){
					apOngdSendDVo.setHdlStatCd("dupSend");//dupSend:중복발송
				}
			}
			
			// 접수함 메세지 전송의 경우
			// 문서 조회에서 sendSeq 가 필요해서 - sendSeq 작업해서 insert 함
			if(msgRecvBxEnable){
				apOngdSendDVo.setSendSeq(maxSendSeq.toString());
				maxSendSeq++;
			}
			queryQueue.insert(apOngdSendDVo);
			
			
			// 메신저용 데이터 만들기
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setCompId(storedApOngdBVo.getCompId());
			apOngdBVo.setEnfcStatCd("inSending");//시행상태코드 - 코드에 없으며 발송용 임시로 사용
			apOngdBVo.setDocSubj(storedApOngdBVo.getDocSubj());
			
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvrDeptYn("Y");
			apOngdApvLnDVo.setApvDeptId(apOngdSendDVo.getRecvDeptId());
			apOngdApvLnDVo.setSendSeq(apOngdSendDVo.getSendSeq());
			
			// 메신저 보내기
			addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
		}
	}
	
	/** 중복된 수신처 확인 */
	private void checkDupRecvDept(List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList, String apvNo, Locale locale) throws CmException {
		if(apOngdRecvDeptLVoList==null) return;
		
		// 중복된 수신/접수 처 확인용
		List<Integer> recvList = new ArrayList<Integer>();
		List<Integer> distList = new ArrayList<Integer>();
		Integer hashId;
		String recvDeptTypCd;
		
		for(ApOngdRecvDeptLVo storedApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
			// 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관
			recvDeptTypCd = storedApOngdRecvDeptLVo.getRecvDeptTypCd();
			
			// 수신처구분코드 - dom:대내
			if("dom".equals(recvDeptTypCd)){
				// 중복확인
				hashId = Hash.hashId(storedApOngdRecvDeptLVo.getRecvDeptId());
				if(recvList.contains(hashId)){
					// ap.msg.dupRecvDept=중복된 수신처가 지정되었습니다.
					String message = messageProperties.getMessage("ap.msg.dupRecvDept", locale) + " : "+storedApOngdRecvDeptLVo.getRecvDeptNm();
					LOGGER.error("Fail - dup recv dept -  apvNo:"+apvNo+"  recvDept:"+storedApOngdRecvDeptLVo.getRecvDeptId()+"/"+storedApOngdRecvDeptLVo.getRecvDeptNm());
					throw new CmException(message);
				} else {
					recvList.add(hashId);
				}
				
			// 수신처구분코드 - for:대외
			} else if("for".equals(recvDeptTypCd)){
				// 참조처가 없으면 - 배부함
				if(storedApOngdRecvDeptLVo.getRefDeptId()==null || storedApOngdRecvDeptLVo.getRefDeptId().isEmpty()){
					// 중복확인
					hashId = Hash.hashId(storedApOngdRecvDeptLVo.getRecvDeptId());
					if(distList.contains(hashId)){
						// ap.msg.dupDistDept=중복된 배부처가 지정되었습니다.
						String message = messageProperties.getMessage("ap.msg.dupDistDept", locale) + " : "+storedApOngdRecvDeptLVo.getRecvDeptNm();
						LOGGER.error("Fail - dup dist dept -  apvNo:"+apvNo+" recvDept:"+storedApOngdRecvDeptLVo.getRecvDeptId()+"/"+storedApOngdRecvDeptLVo.getRecvDeptNm());
						throw new CmException(message);
					} else {
						distList.add(hashId);
					}
				// 참조처가 있으면 - 접수함
				} else {
					// 중복확인
					hashId = Hash.hashId(storedApOngdRecvDeptLVo.getRefDeptId());
					if(recvList.contains(hashId)){
						// ap.msg.dupRecvDept=중복된 수신처가 지정되었습니다.
						String message = messageProperties.getMessage("ap.msg.dupRecvDept", locale) + " : "+storedApOngdRecvDeptLVo.getRefDeptNm();
						LOGGER.error("Fail - dup ref recv dept -  apvNo:"+apvNo+" recvDept:"+storedApOngdRecvDeptLVo.getRefDeptId()+"/"+storedApOngdRecvDeptLVo.getRefDeptNm());
						throw new CmException(message);
					} else {
						recvList.add(hashId);
					}
				}
			}
		}
	}
	
	/** 시행취소 처리 */
	public void processCnclEnfc(QueryQueue queryQueue,
			String apvNo, UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		String process = "cnclEnfc";
		
		// 문서 조회
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, process, userVo, locale);
		if( !"apvd".equals(storedApOngdBVo.getDocProsStatCd()) //apvd:승인
			|| !ArrayUtil.isInArray(new String[]{"apvd","befoEnfc","inCensr","censrRejt","befoSend"}, storedApOngdBVo.getEnfcStatCd())){//apvd:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기
			LOGGER.error("Fail cnclEnfc - stat -  apvNo:"+apvNo
					+"  docProsStatCd:"+storedApOngdBVo.getDocProsStatCd()
					+"  enfcStatCd:"+storedApOngdBVo.getEnfcStatCd()
					+"  userUid:"+userVo.getUserUid());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 시행취소
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn."+process}, locale);
		}
		
		// 문서 - 시행상태 - 시행취소 로
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setEnfcStatCd("cnclEnfc");//시행상태코드 - cnclEnfc:시행취소
		queryQueue.update(apOngdBVo);
		
		// 기안자 - 읽음상태, 이전결재자결재일시 - 바꿈
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		apOngdApvLnDVo.setVwDt("");
		apOngdApvLnDVo.setPrevApvrApvDt("sysdate");
		queryQueue.update(apOngdApvLnDVo);
	}
	
	/** 심사 처리 */
	public void processCensr(QueryQueue queryQueue,
			String apvNo, String censrStatCd, String pichOpinCont, HttpServletRequest request, UserVo userVo,
			Locale locale, ModelMap model) throws CmException, SQLException{
		
		String calledBy = "trans Censr";
		
		if(!"apvd".equals(censrStatCd) && !"rejt".equals(censrStatCd)){
			LOGGER.error("Fail trans - missing parameter ! - userUid:"+userVo.getUserUid());
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			throw new CmException("cm.msg.notValidCall", locale);
		}
		
		// 문서 조회
		ApOngdBVo storedApOngdBVo = queryApOngdBVo(apvNo, calledBy, userVo, locale);
		
		if(!"inCensr".equals(storedApOngdBVo.getEnfcStatCd())){
			LOGGER.error("Fail trans - not inCensr - apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.cfg.censr=심사 
			throw new CmException("ap.msg.notStat", new String[]{"#ap.cfg.censr"}, locale);
		}
		
		// 심사자 조회
		ApOngdPichDVo storedApOngdPichDVo = new ApOngdPichDVo();
		storedApOngdPichDVo.setApvNo(apvNo);
		storedApOngdPichDVo.setPichTypCd("censr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
		storedApOngdPichDVo = (ApOngdPichDVo)commonDao.queryVo(storedApOngdPichDVo);
		
		if(storedApOngdPichDVo==null || (
				!userVo.getUserUid().equals(storedApOngdPichDVo.getPichUid())
				&& !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, storedApOngdPichDVo.getPichUid())
				)){
			LOGGER.error("Fail trans - not censr pich - apvNo:"+apvNo+"  userUid:"+userVo.getUserUid());
			// ap.msg.notActUser={0} 할 수 있는 사용자가 아닙니다. -  - ap.cfg.censr=심사 
			throw new CmException("ap.msg.notActUse", new String[]{"#ap.cfg.censr"}, locale);
		}
		
		if("rejt".equals(censrStatCd)){// 반려면
			// 본문 - 심사반려
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setEnfcStatCd("censrRejt");//censrRejt:심사반려
			queryQueue.update(apOngdBVo);
			
			// 심사자 - 심사반려
			ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
			apOngdPichDVo.setApvNo(apvNo);
			apOngdPichDVo.setPichTypCd("censr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
			apOngdPichDVo.setHdlStatCd("censrRejt");//censrRejt:심사반려
			apOngdPichDVo.setHdlDt("sysdate");
			apOngdPichDVo.setPichOpinCont(pichOpinCont);
			queryQueue.update(apOngdPichDVo);
			
		} else {// 승인이면
			
			// 심사자 - 심사승인
			ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
			apOngdPichDVo.setApvNo(apvNo);
			apOngdPichDVo.setPichTypCd("censr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
			apOngdPichDVo.setHdlStatCd("censrApvd");//censrApvd:심사승인
			apOngdPichDVo.setHdlDt("sysdate");
			apOngdPichDVo.setPichOpinCont(pichOpinCont);
			queryQueue.update(apOngdPichDVo);
			
			toToSendBx(queryQueue, apvNo, null, storedApOngdBVo, userVo, locale, model);
		}
	}
	
	/** 발송함으로 보내기 */
	private void toToSendBx(QueryQueue queryQueue, String apvNo, String trxApvNo, ApOngdBVo storedApOngdBVo,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, CmException{
		
		// 본문 - 발송대기로
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setTrxApvNo(trxApvNo);
		apOngdBVo.setEnfcStatCd("befoSend");// befoSend:발송대기
		queryQueue.update(apOngdBVo);
		
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// [옵션]sendFrom=발송 - toSendBx:발송함에서 발송
		if("toSendBx".equals(optConfigMap.get("sendFrom"))){
			// 발송자 - 기안자 부서
			ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
			apOngdPichDVo.setApvNo(apvNo);
			apOngdPichDVo.setPichTypCd("send");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
			apOngdPichDVo.setHdlStatCd("befoSend");//befoSend:발송대기
			apOngdPichDVo.setPichDeptId(storedApOngdBVo.getMakDeptId());
			OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(storedApOngdBVo.getMakDeptId(), storedApOngdBVo.getDocLangTypCd(), null);
			if(orOrgBVo != null){
				apOngdPichDVo.setPichDeptNm(orOrgBVo.getRescNm());
			}
			apOngdPichDVo.setHdlStatCd("befoSend");//befoSend:발송대기
			queryQueue.store(apOngdPichDVo);
		}
	}
	
	/** 심사의뢰(심사자 지정) */
	private void reqCensr(QueryQueue queryQueue, List<Map<String, String>> messengerQueue, ApOngdBVo storedApOngdBVo,
			String apvNo, String trxApvNo, String pichUid, String pichOpinCont, String userAuth, 
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException{
		
		// 기안자, 관리자 체크
		checkMakr(storedApOngdBVo, "ap.btn.reqCensr", userAuth,
				userVo, locale, model);//ap.btn.reqCensr=심사요청
		
		if(!"befoEnfc".equals(storedApOngdBVo.getEnfcStatCd())){
			LOGGER.error("Fail trans - not reqCensr stat - apvNo:"+apvNo+"  enfcStatCd:"+storedApOngdBVo.getEnfcStatCd()+"  userUid:"+userVo.getUserUid());
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - ap.btn.reqCensr=심사요청
			throw new CmException("ap.msg.notStat", new String[]{"#ap.btn.reqCensr"}, locale);
		}
		
		// 문서 - 시행상태코드 변경
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setTrxApvNo(trxApvNo);
		apOngdBVo.setEnfcStatCd("inCensr");//시행상태코드 - inCensr:심사
		queryQueue.update(apOngdBVo);
		
		// 심사의뢰자 세팅
		ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd("reqCensr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
		queryQueue.delete(apOngdPichDVo);
		
		apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd("reqCensr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
		OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), storedApOngdBVo.getDocLangTypCd(), null);
		apOngdPichDVo.setPichUid(orUserBVo.getUserUid());
		apOngdPichDVo.setPichNm(orUserBVo.getRescNm());
		apOngdPichDVo.setPichDeptId(userVo.getDeptId());
		apOngdPichDVo.setPichDeptNm(orUserBVo.getDeptRescNm());
		apOngdPichDVo.setPichOpinCont(pichOpinCont);
		apOngdPichDVo.setHdlStatCd("reqCensr");//처리상태코드 - reqCensr:심사요청
		apOngdPichDVo.setHdlDt("sysdate");
		queryQueue.insert(apOngdPichDVo);
		
		// 심사자 세팅
		apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd("censr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
		queryQueue.delete(apOngdPichDVo);
		
		apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd("censr");//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
		orUserBVo = apRescSvc.getOrUserBVo(pichUid, storedApOngdBVo.getDocLangTypCd(), null);
		apOngdPichDVo.setPichUid(orUserBVo.getUserUid());
		apOngdPichDVo.setPichNm(orUserBVo.getRescNm());
		apOngdPichDVo.setPichDeptId(userVo.getDeptId());
		apOngdPichDVo.setPichDeptNm(orUserBVo.getDeptRescNm());
		apOngdPichDVo.setHdlStatCd("befoCensr");//처리상태코드 - befoCensr:심사전
		apOngdPichDVo.setPrevApvrApvDt("sysdate");
		queryQueue.insert(apOngdPichDVo);
		
		// 메신저용 데이터 만들기
		apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setEnfcStatCd("inCensr");//시행상태코드 - inCensr:심사
		apOngdBVo.setDocSubj(storedApOngdBVo.getDocSubj());
		queryQueue.update(apOngdBVo);
		// 메신저용 데이터 만들기
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvrUid(pichUid);
		// 메신저 보내기
		addMesseger(apOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
	}
	
	/** 기안회수, 승인취소, 합의취소 */
	public void processCancel(QueryQueue queryQueue,
			String apvNo, String apvLnPno, String apvLnNo, String process, 
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException {
		
		// 결재라인번호 체크
		if("cancelMak".equals(process)){
			apvLnNo = "1";
			apvLnPno = "0";
		} else {
			if(apvLnPno==null || apvLnPno.isEmpty() || apvLnNo==null || apvLnNo.isEmpty()){
				LOGGER.error("Fail trans - "+process+" - missing parameter ! - userUid:"+userVo.getUserUid());
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException("cm.msg.notValidCall", locale);
			}
		}
		
		// 문서정보 조회
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		if(storedApOngdBVo==null){
			LOGGER.error("Fail trans - "+process+" - no doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
		}
		
		// 진행중 문서 체크
		if(!"ongo".equals(storedApOngdBVo.getDocProsStatCd())){
			LOGGER.error("Fail trans - "+process+" - not ongo doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid()+" docProsStatCd:"+storedApOngdBVo.getDocProsStatCd());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 기안회수, 승인취소, 합의취소
			throw new CmException("ap.msg.noApvr", new String[]{"#ap.btn."+process}, locale);
		}
		
		// 최상위 결재라인 조회
		List<ApOngdApvLnDVo> rootApOngdApvLnDVoList = queryApvLn(apvNo, "0", null);
		List<ApOngdApvLnDVo> currApOngdApvLnDVoList = "0".equals(apvLnPno) ? rootApOngdApvLnDVoList : queryApvLn(apvNo, apvLnPno, null);
		
		// 해당 취소자의 결재라인 조회
		ApOngdApvLnDVo myApOngdApvLnDVo = ApDocTransUtil.findApvLnByApvLnNo(currApOngdApvLnDVoList, apvLnNo);
		if(myApOngdApvLnDVo==null || (
				!userVo.getUserUid().equals(myApOngdApvLnDVo.getApvrUid())
				&& !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, myApOngdApvLnDVo.getApvrUid())
				)){
			if(myApOngdApvLnDVo==null){
				LOGGER.error("Fail trans - "+process+" - no user at apvLnNo - apvNo:"+apvNo+"  apvLnNo:"+apvLnNo+"  userUid:"+userVo.getUserUid());
			} else {
				LOGGER.error("Fail trans - "+process+" - not matched apvrUid - apvNo:"+apvNo+"  apvLnNo:"+apvLnNo+"  userUid:"+userVo.getUserUid()+"  apvrUid:"+myApOngdApvLnDVo.getApvrUid());
			}
			if("cancelMak".equals(process)){
				//ap.msg.noMakr=기안자 정보를 확인 할 수 없습니다.
				throw new CmException("ap.msg.noMakr", locale);
			} else {
				//ap.msg.notActUser={0} 할 수 있는 사용자가 아닙니다.
				throw new CmException("ap.msg.notActUser", new String[]{"#ap.btn."+process}, locale);
			}
		}
		
		if("cancelMak".equals(process) && !"mak".equals(myApOngdApvLnDVo.getApvrRoleCd())){//mak:기안
			LOGGER.error("Fail trans - "+process+" : not mak - apvNo:"+apvNo+"  apvLnNo:"+apvLnNo+" userUid:"+userVo.getUserUid()+"  apvrRoleCd:"+myApOngdApvLnDVo.getApvrRoleCd());
			//ap.msg.notActUser={0} 할 수 있는 사용자가 아닙니다.
			throw new CmException("ap.msg.notActUser", new String[]{"#ap.btn."+process}, locale);
		}
		
		// 다음 결재자를 부모 결재라인에서 찾았는지 여부
		boolean findNextApvrAtParentLn = false;
		
		// 현재 라인의 다음 결재자 목록
		List<ApOngdApvLnDVo> currNextApvrList = null;
		// 부모 라인의 다음 결재자 목록
		List<ApOngdApvLnDVo> parentNextApvrList = null;
		currNextApvrList = ApDocTransUtil.getNextApvrList(currApOngdApvLnDVoList, apvLnNo, false);
		
		if(currNextApvrList == null || currNextApvrList.isEmpty() || "abs".equals(currNextApvrList.get(currNextApvrList.size()-1).getApvrRoleCd())){
			// 루트 라인이 아닌 경우 루트라인에서 다음 결재자를 찾음
			if(!"0".equals(apvLnPno)){
				parentNextApvrList = ApDocTransUtil.getNextApvrList(rootApOngdApvLnDVoList, apvLnPno, false);
				findNextApvrAtParentLn = true;
			}
			// 다음 결재라인이 없으면
			if(parentNextApvrList == null || parentNextApvrList.isEmpty() || "abs".equals(parentNextApvrList.get(parentNextApvrList.size()-1).getApvrRoleCd())){
				LOGGER.error("Fail trans - "+process+" : no next apvr - apvNo:"+apvNo+"  apvLnNo:"+apvLnNo+"  userUid:"+userVo.getUserUid());
				//ap.msg.noNextApvr=다음 결재자 정보를 확인 할 수 없습니다.
				throw new CmException("ap.msg.noNextApvr", locale);
			}
		}
		
		// 부모 결재라인 - 서브일 경우
		ApOngdApvLnDVo parentApOngdApvLnDVo = null;
		if(findNextApvrAtParentLn){// 다음 결재자를 부모 결재라인에서 찾았을 때
			parentApOngdApvLnDVo = ApDocTransUtil.findApvLnByApvLnNo(rootApOngdApvLnDVoList, apvLnPno);
			if(parentApOngdApvLnDVo == null){
				LOGGER.error("Fail trans - "+process+" : no parent apvr - apvNo:"+apvNo+"  apvLnNo:"+apvLnNo+"  userUid:"+userVo.getUserUid());
				//ap.msg.noParentApvr=부모 결재라인을 확인 할 수 없습니다.
				throw new CmException("ap.msg.noParentApvr", locale);
			}
		}
		
		// 다음 결재자 목록 조회
		List<ApOngdApvLnDVo> nextApOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
		if(currNextApvrList != null && !currNextApvrList.isEmpty()){
			nextApOngdApvLnDVoList.addAll(currNextApvrList);
		}
		if(parentNextApvrList != null && !parentNextApvrList.isEmpty()){
			nextApOngdApvLnDVoList.addAll(parentNextApvrList);
		}
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		// [옵션] 승인자 열람 후 취소 가능
		String cnclAftRead = optConfigMap.get("cnclAftRead");
		
		// 승인자 열람 후 취소 가능 - 이 아닐 경우에 다음 결재자 읽으면 오류 발생
		if(!"Y".equals(cnclAftRead)){
			// 읽은 상태 확인
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : nextApOngdApvLnDVoList){
				// 결재자가 읽었는지 확인
				if(ApDocTransUtil.wasRead(storedApOngdApvLnDVo)){
					LOGGER.error("Fail trans - "+process+" : read by next user - apvNo:"+apvNo
							+"  userUid:"+userVo.getUserUid()
							+"  apvLnPno:"+storedApOngdApvLnDVo.getApvLnPno()
							+"  apvLnNo:"+storedApOngdApvLnDVo.getApvLnNo()
							+"  apvrUid:"+storedApOngdApvLnDVo.getApvrUid()
							+"  vwDt:"+storedApOngdApvLnDVo.getVwDt());

					//ap.msg.readByNext=다음 결재자가 조회해서 {0} 할 수 없습니다. - 기안회수, 승인취소, 합의취소
					throw new CmException("ap.msg.readByNext", new String[]{"#ap.btn."+process}, locale);
				}
			}
		}
		
		////////////////////////////////////////////
		// SQL
		
		String holdBodyHstNo, holdApvLnHstNo, holdAttHstNo, holdRecvDeptHstNo, holdRefDocHstNo;
		ApOngdBodyLVo apOngdBodyLVo;
		
		ApOngdApvLnDVo apOngdApvLnDVo;
		ApOngdApvLnDVo holdApOngdApvLnDVo;
		ApOngdAttFileLVo apOngdAttFileLVo;
		ApOngdRecvDeptLVo apOngdRecvDeptLVo;
		ApOngdRefDocLVo apOngdRefDocLVo;
		
		// 다음 결재자 - 전 상태로
		for(ApOngdApvLnDVo nextApOngdApvLnDVo : nextApOngdApvLnDVoList){
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnNo(nextApOngdApvLnDVo.getApvLnNo());
			
			if(ApDocUtil.isAgrOfApvrRole(nextApOngdApvLnDVo.getApvrRoleCd())){
				apOngdApvLnDVo.setApvStatCd("befoAgr");// befoAgr:합의전
			} else {
				apOngdApvLnDVo.setApvStatCd("befoApv");// befoApv:결재전
			}
			apOngdApvLnDVo.setPrevApvrApvDt("");//이전결재자결재일시
			apOngdApvLnDVo.setVwDt("");//조회일시
			
			// 현 결재자의 본문 보류 제거
			holdBodyHstNo = nextApOngdApvLnDVo.getHoldBodyHstNo();
			if(holdBodyHstNo!=null && !holdBodyHstNo.isEmpty()){
				apOngdBodyLVo = new ApOngdBodyLVo();
				apOngdBodyLVo.setApvNo(apvNo);
				apOngdBodyLVo.setBodyHstNo(holdBodyHstNo);
				queryQueue.delete(apOngdBodyLVo);
				apOngdApvLnDVo.setHoldBodyHstNo("");
			}
			
			// 현 결재자의 결재선 보류 제거
			holdApvLnHstNo = nextApOngdApvLnDVo.getHoldApvLnHstNo();
			if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
				holdApOngdApvLnDVo = new ApOngdApvLnDVo();
				holdApOngdApvLnDVo.setApvNo(apvNo);
				holdApOngdApvLnDVo.setApvLnHstNo(holdApvLnHstNo);
				holdApOngdApvLnDVo.setHistory();
				queryQueue.delete(holdApOngdApvLnDVo);
				apOngdApvLnDVo.setHoldApvLnHstNo("");
			}
			
			// 현 결재자의 첨부 보류 제거
			holdAttHstNo = nextApOngdApvLnDVo.getHoldAttHstNo();
			if(holdAttHstNo!=null && !holdAttHstNo.isEmpty()){
				apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setAttHstNo(holdAttHstNo);
				queryQueue.delete(apOngdAttFileLVo);
				apOngdApvLnDVo.setHoldAttHstNo("");
			}
			
			// 현 결재자의 수신처 보류 제거
			holdRecvDeptHstNo = nextApOngdApvLnDVo.getHoldRecvDeptHstNo();
			if(holdRecvDeptHstNo!=null && !holdRecvDeptHstNo.isEmpty()){
				apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
				apOngdRecvDeptLVo.setApvNo(apvNo);
				apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);
				queryQueue.delete(apOngdRecvDeptLVo);
				apOngdApvLnDVo.setHoldRecvDeptHstNo("");
			}
			
			// 현 결재자의 참조문서 보류 제거
			holdRefDocHstNo = nextApOngdApvLnDVo.getHoldRefDocHstNo();
			if(holdRefDocHstNo!=null && !holdRefDocHstNo.isEmpty()){
				apOngdRefDocLVo = new ApOngdRefDocLVo();
				apOngdRefDocLVo.setApvNo(apvNo);
				apOngdRefDocLVo.setRefDocHstNo(holdRefDocHstNo);
				queryQueue.delete(apOngdRefDocLVo);
				apOngdApvLnDVo.setHoldRefDocHstNo("");
			}
			
			queryQueue.update(apOngdApvLnDVo);
		}
		
		// 자신 - 결재중(합의중) 으로
		apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setApvLnNo(apvLnNo);
		if("cancelMak".equals(process)){
			apOngdApvLnDVo.setApvStatCd("inApv");// inApv:결재중
		} else {
			apOngdApvLnDVo.setApvStatCd("cncl");// cncl:취소
		}
		
		apOngdApvLnDVo.setSignImgPath("");//서명이미지경로
		apOngdApvLnDVo.setSignDispVa("");//서명표시값
		String signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, myApOngdApvLnDVo, storedApOngdBVo.getDocLangTypCd(), false);
		apOngdApvLnDVo.setDtDispVa(signAreaDtDisp==null ? "" : signAreaDtDisp);//일시표시값
		apOngdApvLnDVo.setApvDt("");//결재일시
		apOngdApvLnDVo.setVwDt("");//조회일시
		apOngdApvLnDVo.setPrevApvrApvDt("sysdate");
		queryQueue.update(apOngdApvLnDVo);
		
		// 부모결재라인(부서합의의 경우) - 합의중으로
		if(parentApOngdApvLnDVo != null){
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(parentApOngdApvLnDVo.getApvNo());
			apOngdApvLnDVo.setApvLnPno(parentApOngdApvLnDVo.getApvLnPno());
			apOngdApvLnDVo.setApvLnNo(parentApOngdApvLnDVo.getApvLnNo());
			
			apOngdApvLnDVo.setApvStatCd("inAgr");// inAgr:합의중
			
			// 결재자 정보
			apOngdApvLnDVo.setApvrUid("");
			apOngdApvLnDVo.setApvrNm("");
			apOngdApvLnDVo.setApvrPositCd("");
			apOngdApvLnDVo.setApvrPositNm("");
			apOngdApvLnDVo.setApvrTitleCd("");
			apOngdApvLnDVo.setApvrTitleNm("");
			
			// 결재자 서명 정보
			apOngdApvLnDVo.setSignImgPath("");//서명이미지경로
			signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, parentApOngdApvLnDVo, storedApOngdBVo.getDocLangTypCd(), false);
			apOngdApvLnDVo.setDtDispVa(signAreaDtDisp==null ? "" : signAreaDtDisp);//일시표시값
			apOngdApvLnDVo.setApvDt("");//결재일시
			
			apOngdApvLnDVo.setApvOpinDispYn("");	//	결재의견표시여부
			apOngdApvLnDVo.setApvOpinCont("");		//	결재의견내용
			
			queryQueue.update(apOngdApvLnDVo);
		}
		
		// 문서상태
		
		// 기안회수
		if("cancelMak".equals(process)){
			
			// 문서상태
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setDocStatCd("retrvMak");//retrvMak:기안회수
			apOngdBVo.setDocProsStatCd("mak");//mak:기안
			apOngdBVo.setMakDt("sysdate");
			apOngdBVo.setCurApvrId(myApOngdApvLnDVo.getApvrUid());//현재결재자ID
			apOngdBVo.setCurApvrNm(myApOngdApvLnDVo.getApvrNm());//현재결재자명
			apOngdBVo.setCurApvrDeptYn("N");//현재결재자부서여부
			queryQueue.update(apOngdBVo);
			
		} else {
			
			// 자기 라인이 루트 라인일 경우, 또는 부모라인에서 회수한것 - 문서 상태를 바꿔야함
			if(ApDocUtil.isRootLine(myApOngdApvLnDVo.getApvLnPno()) || findNextApvrAtParentLn){
				
				// 내가(또는 내 부모가) 병렬 상태이면
				if(ApDocUtil.isParalAgrOfApvrRole((parentApOngdApvLnDVo==null ? myApOngdApvLnDVo : parentApOngdApvLnDVo).getApvrRoleCd())){
					
					// 병렬합의 중 완료되지 않은 첫번째 병렬합의 찾기
					ApOngdApvLnDVo moveApOngdApvLnDVo = null;
					if(parentApOngdApvLnDVo!=null){
						int myIndex = ApDocTransUtil.findMyIndex(rootApOngdApvLnDVoList, apvLnPno);
						moveApOngdApvLnDVo = ApDocTransUtil.findFirstNotDoneParalAgr(rootApOngdApvLnDVoList, myIndex, true);
					} else {
						int myIndex = ApDocTransUtil.findMyIndex(currApOngdApvLnDVoList, apvLnNo);
						moveApOngdApvLnDVo = ApDocTransUtil.findFirstNotDoneParalAgr(currApOngdApvLnDVoList, myIndex, true);
					}
					
					// 문서상태
					ApOngdBVo apOngdBVo = new ApOngdBVo();
					apOngdBVo.setApvNo(apvNo);
					setCurApvrAtApOngdBVo(apOngdBVo, moveApOngdApvLnDVo);
					queryQueue.update(apOngdBVo);
					
				// 내가(또는 내 부모가) 병렬이 아니면
				} else {
					// 문서상태
					ApOngdBVo apOngdBVo = new ApOngdBVo();
					apOngdBVo.setApvNo(apvNo);
					setCurApvrAtApOngdBVo(apOngdBVo, myApOngdApvLnDVo);
					queryQueue.update(apOngdBVo);
					
				}
			}
		}
		
		// formBodyXML - 보류 데이터 삭제
		ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
		apOngdErpFormDVo.setApvNo(apvNo);
		apOngdErpFormDVo.setErpVaTypCd("formBodyHoldHTML");
		queryQueue.delete(apOngdErpFormDVo);
		
		// 연차에 해당할 경우
		if(ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, storedApOngdBVo.getXmlTypId())
				&& "cancelMak".equals(process)){
			
			// formBodyXML 조회
			apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
			apOngdErpFormDVo = (ApOngdErpFormDVo)commonDao.queryVo(apOngdErpFormDVo);
			if(apOngdErpFormDVo != null){
				
				String formBodyXML = apOngdErpFormDVo.getErpVa();
				if(formBodyXML!=null && !formBodyXML.isEmpty()){
					Map<String, String> wdNotiMap = new HashMap<String, String>();
					wdNotiMap.put("apvNo", apvNo);
					wdNotiMap.put("docSubj", storedApOngdBVo.getDocSubj());
					wdNotiMap.put("statCd", "retrvMak");
					wdNotiMap.put("formBodyXML", formBodyXML);
					model.put("wdNotiMap", wdNotiMap);
				}
			}
		}
	}
	
	/** 최종 승인 취소 */
	public void processCancelCmpl(HttpServletRequest request, QueryQueue queryQueue,
			String apvNo, UserVo userVo, boolean withForce, Locale locale, ModelMap model) throws CmException, SQLException {
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		
		if(apOngdBVo == null) {
			// ap.msg.noDoc=해당 문서가 없습니다.
			String message = messageProperties.getMessage("ap.msg.noDoc", locale);
			model.put("message", message);
			return;
		}
		
		ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
		apOngdErpFormDVo.setApvNo(apvNo);
		apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
		apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
		
		if(!withForce) {
			boolean isNotiDoc = apErpNotiSvc.isErpNotiFromAp(apOngdBVo, apOngdErpFormDVo==null ? null : apOngdErpFormDVo.getErpVa());
			if(isNotiDoc) {
				// ap.msg.NotCancelCmpl=연계된 문서는 완결 취소 할 수 없습니다.
				String message = messageProperties.getMessage("ap.msg.NotCancelCmpl", locale);
				model.put("message", message);
				return;
			}
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setOrderBy("APV_LN_PNO, APV_LN_NO");
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apvLnList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
		
		ApOngdApvLnDVo apvLnVo, lastApvLnVo = null, parentApvLnVo = null;
		String apvrRoleCd;
		int i, size = apvLnList==null ? 0 : apvLnList.size();
		for(i=size-1; i>=0; i--) {
			
			apOngdApvLnDVo = apvLnList.get(i);
			apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
			
			// 부서합의 - 부서
			if(lastApvLnVo != null) {
				
				if("0".equals(apOngdApvLnDVo.getApvLnPno())
						&& apOngdApvLnDVo.getApvLnNo().equals(lastApvLnVo.getApvLnPno()) ) {
					
					parentApvLnVo = apOngdApvLnDVo;
					
					apvLnVo = new ApOngdApvLnDVo();
					apvLnVo.setApvNo(apvNo);
					apvLnVo.setApvLnPno(apOngdApvLnDVo.getApvLnPno());
					apvLnVo.setApvLnNo(apOngdApvLnDVo.getApvLnNo());
					
					apvLnVo.setApvStatCd("inAgr");
					apvLnVo.setAgntUid("");
					
					apvLnVo.setApvrUid("");
					apvLnVo.setApvrNm("");
					apvLnVo.setApvrPositCd("");
					apvLnVo.setApvrPositNm("");
					apvLnVo.setApvrTitleCd("");
					apvLnVo.setApvrTitleNm("");
					
					apvLnVo.setVwDt("");
					apvLnVo.setApvDt("");
					
					apvLnVo.setSignDispVa("");
					apvLnVo.setDtDispVa("");
					apvLnVo.setSignImgPath("");
					
					queryQueue.update(apvLnVo);
					
					break;
				}
				
				
			// 통보 류
			} else if("entu".equals(apvrRoleCd) || "postApvd".equals(apvrRoleCd)
					|| "psnInfm".equals(apvrRoleCd) || "deptInfm".equals(apvrRoleCd)
					|| "abs".equals(apvrRoleCd)) {
				
				apvLnVo = new ApOngdApvLnDVo();
				apvLnVo.setApvNo(apvNo);
				apvLnVo.setApvLnPno(apOngdApvLnDVo.getApvLnPno());
				apvLnVo.setApvLnNo(apOngdApvLnDVo.getApvLnNo());
				
				apvLnVo.setApvStatCd("befoApv");
				apvLnVo.setVwDt("");
				apvLnVo.setApvDt("");
				apvLnVo.setPrevApvrApvDt("");
				
				queryQueue.update(apvLnVo);
				
			// 결재 또는 합의 류
			} else if("byOne".equals(apvrRoleCd) || "mak".equals(apvrRoleCd)
					|| "revw".equals(apvrRoleCd) || "revw2".equals(apvrRoleCd) || "revw3".equals(apvrRoleCd)
					|| "apv".equals(apvrRoleCd) || "pred".equals(apvrRoleCd)
					|| "psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd)
					|| "byOneAgr".equals(apvrRoleCd) || "makAgr".equals(apvrRoleCd)) {
				
				lastApvLnVo = apOngdApvLnDVo;
				
				apvLnVo = new ApOngdApvLnDVo();
				apvLnVo.setApvNo(apvNo);
				apvLnVo.setApvLnPno(apOngdApvLnDVo.getApvLnPno());
				apvLnVo.setApvLnNo(apOngdApvLnDVo.getApvLnNo());
				
				if("psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd)
						|| !"0".equals(apOngdApvLnDVo.getApvLnPno())) {
					apvLnVo.setApvStatCd("inAgr");
				} else {
					apvLnVo.setApvStatCd("inApv");
				}
				apvLnVo.setAgntUid("");
				apvLnVo.setVwDt("");
				apvLnVo.setApvDt("");
				
				apvLnVo.setSignDispVa("");
				apvLnVo.setDtDispVa("");
				apvLnVo.setSignImgPath("");
				
				queryQueue.update(apvLnVo);
				
				if("0".equals(apOngdApvLnDVo.getApvLnPno())) {
					break;
				}
			}
		}
		
		if(lastApvLnVo != null) {
			
			ApOngdBVo apBVo = new ApOngdBVo();
			
			// 부서 합의가 마지막 일 경우
			if(parentApvLnVo != null) {
				
				apBVo.setApvNo(apvNo);
				apBVo.setDocStatCd(parentApvLnVo.getApvrRoleCd());
				apBVo.setDocProsStatCd("ongo");
				
				apBVo.setCurApvrId(parentApvLnVo.getApvDeptId());
				apBVo.setCurApvrNm(parentApvLnVo.getApvDeptNm());
				apBVo.setCurApvrDeptYn("Y");
				
				apBVo.setCmplDt("");
				apBVo.setCmplrUid("");
				apBVo.setCmplrNm("");
				
				apBVo.setEnfcStatCd("");
				apBVo.setRecLstTypCd("");
				apBVo.setRecLstDeptId("");
				
				queryQueue.update(apBVo);
				
			// 결재자가 마지막 일 경우
			} else {
				
				apBVo.setApvNo(apvNo);
				apBVo.setDocStatCd(lastApvLnVo.getApvrRoleCd());
				apBVo.setDocProsStatCd("ongo");
				
				apBVo.setCurApvrId(lastApvLnVo.getApvrUid());
				apBVo.setCurApvrNm(lastApvLnVo.getApvrNm());
				apBVo.setCurApvrDeptYn("N");
				
				apBVo.setCmplDt("");
				apBVo.setCmplrUid("");
				apBVo.setCmplrNm("");
				
				apBVo.setEnfcStatCd("");
				apBVo.setRecLstTypCd("");
				apBVo.setRecLstDeptId("");
				
				queryQueue.update(apBVo);
				
			}
			
		} else {
			
			queryQueue.removeAll();
			
			// ap.msg.NotCancelApvLn=완결 취소 할 수 없는 문서 입니다.
			String message = messageProperties.getMessage("ap.msg.NotCancelApvLn", locale);
			model.put("message", message);
			return;
		}
		
		// 문서관리 - 등록된 것 삭제
		ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
		apOngdExDVo.setApvNo(apvNo);
		apOngdExDVo.setExId("sendToDm");
		apOngdExDVo = (ApOngdExDVo)commonSvc.queryVo(apOngdExDVo);
		if(apOngdExDVo != null) {
			dmDocSvc.delDocByAp(request, queryQueue, userVo, apvNo);
		}
		
	}
	
	/** 합의기안자 설정, 처리부서 담당자 지정 */
	public void processPich(QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			String apvNo, String apvLnPno, String apvLnNo, String process, String apvrUid, 
			UserVo userVo, Locale locale, ModelMap model) throws CmException, SQLException {
		
		if(apvLnPno==null || apvLnPno.isEmpty() || apvrUid==null || apvrUid.isEmpty()){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			throw new CmException("cm.msg.notValidCall", locale);
		}
		
		// 문서 조회
		ApOngdBVo storedApOngdBVo = new ApOngdBVo();
		storedApOngdBVo.setApvNo(apvNo);
		storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
		
		if(storedApOngdBVo==null){
			LOGGER.error("Fail - makAgr : no doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid());
			// ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다. - ap.jsp.doc=문서
			throw new CmException("ap.trans.notFound", new String[]{"#ap.jsp.doc"}, locale);
		}
		
		// 진행중 문서 체크
		if(!"ongo".equals(storedApOngdBVo.getDocProsStatCd())){
			LOGGER.error("Fail - makAgr : not ongo doc - apvNo:"+apvNo+" userUid:"+userVo.getUserUid()+" docProsStatCd:"+storedApOngdBVo.getDocProsStatCd());
			//ap.msg.notStat={0} 할 수 있는 상태가 아닙니다. - 기안회수, 승인취소, 합의취소
			throw new CmException("ap.msg.noApvr", new String[]{ptSysSvc.getTerm("ap.term.makAgr", locale.getLanguage())}, locale);
		}
		
		// 옵션 조회(캐쉬)
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 합의 기안자 설정 - 이면
		if("setMakAgr".equals(process)){
			
			// 부모결재라인 조회
			ApOngdApvLnDVo parentApOngdApvLnDVo = new ApOngdApvLnDVo();
			parentApOngdApvLnDVo.setApvNo(apvNo);
			parentApOngdApvLnDVo.setApvLnPno("0");
			parentApOngdApvLnDVo.setApvLnNo(apvLnPno);
			parentApOngdApvLnDVo = (ApOngdApvLnDVo)commonDao.queryVo(parentApOngdApvLnDVo);
			
			if(parentApOngdApvLnDVo == null){
				LOGGER.error("Fail - makAgr : no next apvr - apvNo:"+apvNo+"  apvLnPno:0  apvLnNo:"+apvLnPno+"  userUid:"+userVo.getUserUid());
				//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
				throw new CmException("ap.msg.noApvr", locale);
			}
			
			// 부모 결재라인 - UPDATE 용 데이터
			ApOngdApvLnDVo parentToSave = new ApOngdApvLnDVo();
			parentToSave.setApvNo(apvNo);
			parentToSave.setApvLnPno("0");
			parentToSave.setApvLnNo(apvLnPno);
			
			// 조회일시
			if(parentApOngdApvLnDVo.getVwDt()==null || parentApOngdApvLnDVo.getVwDt().isEmpty()){
				parentToSave.setVwDt("sysdate");
			}
			
			// 재검토로 - 의견자의 사용자 정보가 세팅된 경우
			if(parentApOngdApvLnDVo.getApvrUid()!=null && !parentApOngdApvLnDVo.getApvrUid().isEmpty()){
				// 사용자 정보 삭제
				parentToSave.setApvrUid("");
				parentToSave.setApvrNm("");
				parentToSave.setApvrPositCd("");
				parentToSave.setApvrPositNm("");
				parentToSave.setApvrTitleCd("");
				parentToSave.setApvrTitleNm("");
			}
			
			// 의견 삭제 - 재검토 의견
			if(parentApOngdApvLnDVo.getApvOpinCont()!=null && !parentApOngdApvLnDVo.getApvOpinCont().isEmpty()){
				parentToSave.setApvOpinCont("");
				parentToSave.setApvOpinDispYn("");
			}
			
			// 담당자지정여부
			parentToSave.setPichApntYn("Y");
			
			queryQueue.update(parentToSave);
			
			// 부서합의 서브라인 조회 - 의견 유지목적
			List<ApOngdApvLnDVo> subList = queryApvLn(apvNo, apvLnPno, null);
			Map<Integer, ApOngdApvLnDVo> opinMap = toOpinMap(subList, null);
			
			// 합의부서 결재라인 저장용 - 서브라인
			ApOngdApvLnDVo subToSave;
			// 사용자 정보 캐쉬용
			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
			
			// 합의부서 결재라인 삭제 여부
			boolean delDeptAgrLn = false;
			
			// 저장된[합의기안자] 조회
			ApOngdApvLnDVo makAgrVo = new ApOngdApvLnDVo();
			makAgrVo.setApvNo(apvNo);
			makAgrVo.setApvLnPno(apvLnPno);
			makAgrVo.setApvLnNo("1");
			makAgrVo = (ApOngdApvLnDVo)commonDao.queryVo(makAgrVo);
			
			// 저장된[합의기안자]와 전송된 전송된[합의기안자]가 다르면
			if(makAgrVo != null && !makAgrVo.getApvrUid().equals(apvrUid)){
				
				// [옵션-히든] keepAgrLnWhenChgMakr=합의부서 기안자 변경시 합의부서 결재경로 유지 - 디폴트(N)
				
				// 기안자 변경시 합의부서 사용자 삭제
				if(!"Y".equals(optConfigMap.get("keepAgrLnWhenChgMakr"))){
					// 합의부서 결재라인 삭제함 - 서브라인 삭제
					delDeptAgrLn = true;
				// 기안자 변경시 합의부서 사용자 유지
				} else {
					
					// 전송된[합의기안자] 사용자 정보 조회
					OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(apvrUid, storedApOngdBVo.getDocLangTypCd(), userCacheMap);
					
					// 전송된[합의기안자] 사용자의 부서와 저장된[합의기안자]의 부서가 다르면 경로 삭제 
					if(orUserBVo!=null && !orUserBVo.getDeptId().equals(makAgrVo.getApvDeptId())){
						// 합의부서 결재라인 삭제함 - 서브라인 삭제
						delDeptAgrLn = true;
					}
				}
			}
			// 합의부서 결재라인 삭제 여부
			if(delDeptAgrLn){
				// 합의부서 사용자 삭제
				subToSave = new ApOngdApvLnDVo();
				subToSave.setApvNo(apvNo);
				subToSave.setApvLnPno(apvLnPno);
				queryQueue.delete(subToSave);
			} else {
				// 합의부서 사용자 - 합의전 으로, 결재일시 지워줌
				subToSave = new ApOngdApvLnDVo();
				subToSave.setApvNo(apvNo);
				subToSave.setApvLnPno(apvLnPno);
				subToSave.setApvDt("");
				subToSave.setApvStatCd("befoAgr");//befoAgr:합의전
				queryQueue.update(subToSave);
			}
			
			// 담당자 정보 - INSERT/UPDATE 용 데이터
			subToSave = new ApOngdApvLnDVo();
			subToSave.setApvNo(apvNo);
			subToSave.setApvLnPno(apvLnPno);
			subToSave.setApvLnNo("1");
			
			// 결재자UID - 파라미터
			subToSave.setApvrUid(apvrUid);
			subToSave.setApvrDeptYn("N");//결재자부서여부
			
			// 결재자역할코드
			subToSave.setApvrRoleCd("makAgr");//makAgr:합의기안
			// 결재상태코드
			subToSave.setApvStatCd("inAgr");//결재상태코드 - inAgr:합의중
			// 이전결재자결재일시
			subToSave.setPrevApvrApvDt("sysdate");
			// 결재라인이력번호
			subToSave.setApvLnHstNo("1");
			
			// 결재선 데이터에 문서언어에 따른 리스스 바인딩
			setApOngdApvLnDVoResc(subToSave, storedApOngdBVo.getDocLangTypCd(), 
					userCacheMap, null, optConfigMap, locale);
			
			// 저장되어 있는 결재선 정보 - 의견
			if(opinMap != null){
				ApOngdApvLnDVo storedApOngdApvLnDVo = getFromMap(opinMap, subToSave);
				if(storedApOngdApvLnDVo != null){
					// 결재의견내용, 결재의견표시여부
					if(storedApOngdApvLnDVo.getApvOpinCont()!=null && !storedApOngdApvLnDVo.getApvOpinCont().isEmpty()){
						subToSave.setApvOpinCont(storedApOngdApvLnDVo.getApvOpinCont());
						subToSave.setApvOpinDispYn(storedApOngdApvLnDVo.getApvOpinDispYn());
					}
				}
			}

			// 메신저 보내기
			addMesseger(storedApOngdBVo, subToSave, userVo, messengerQueue, locale);
			
			queryQueue.store(subToSave);
			
			
		// 처리부서 담당자 지정
		} else if("setPrcDeptAgr".equals(process)){
			
			if(apvLnNo==null || apvLnNo.isEmpty()){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException("cm.msg.notValidCall", locale);
			}
			
			// 저장된 결재라인 조회
			ApOngdApvLnDVo storedApOngdApvLnDVo = new ApOngdApvLnDVo();
			storedApOngdApvLnDVo.setApvNo(apvNo);
			storedApOngdApvLnDVo.setApvLnPno(apvLnPno);
			storedApOngdApvLnDVo.setApvLnNo(apvLnNo);
			storedApOngdApvLnDVo = (ApOngdApvLnDVo)commonDao.queryVo(storedApOngdApvLnDVo);
			
			if(storedApOngdApvLnDVo==null){
				storedApOngdApvLnDVo = new ApOngdApvLnDVo();
				storedApOngdApvLnDVo.setApvNo(apvNo);
				storedApOngdApvLnDVo.setApvLnPno(apvLnPno);
				storedApOngdApvLnDVo.setApvLnNo(apCmSvc.addNo(apvLnNo, -1));
				storedApOngdApvLnDVo = (ApOngdApvLnDVo)commonDao.queryVo(storedApOngdApvLnDVo);
				if(storedApOngdApvLnDVo==null){
					//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
					throw new CmException("ap.msg.noApvr", locale);
				}
			}
			
			// 변경하려는 사용자가 결재선에 있는지 조회
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvrUid(apvrUid);
			apOngdApvLnDVo.setDblApvTypCd("prcDept");//이중결재구분코드 - reqDept:신청부서, prcDept:처리부서
			// revw:검토, abs:공석, apv:결재, pred:전결
			apOngdApvLnDVo.setApvrRoleCdList(ApCmUtil.toList("revw", "revw2", "revw3", "abs", "apv", "pred"));
			
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
			
			// 결재선 내의 - 같은 사용자를 찾으면
			if(apOngdApvLnDVoList!=null && apOngdApvLnDVoList.size()>0){
				// 결재선 내의 담당자 허용
				if(!"Y".equals(optConfigMap.get("pichInApvLnEnab"))){
					//ap.apvLn.alrdyUser=이미 사용자가 결재라인에 지정되어 있습니다.
					throw new CmException("ap.apvLn.alrdyUser", locale);
				} else {
					ApOngdApvLnDVo dupApOngdApvLnDVo = apOngdApvLnDVoList.get(0);
					
					// 해당 사용자 삭제
					apOngdApvLnDVo = new ApOngdApvLnDVo();
					apOngdApvLnDVo.setApvNo(apvNo);
					apOngdApvLnDVo.setApvLnPno(dupApOngdApvLnDVo.getApvLnPno());
					apOngdApvLnDVo.setApvLnNo(dupApOngdApvLnDVo.getApvLnNo());
					queryQueue.delete(apOngdApvLnDVo);
					
					// 해당사용자 뒤 - 결재라인번호 줄이기
					apOngdApvLnDVo = new ApOngdApvLnDVo();
					apOngdApvLnDVo.setApvNo(apvNo);
					apOngdApvLnDVo.setApvLnPno(dupApOngdApvLnDVo.getApvLnPno());
					apOngdApvLnDVo.setApvLnNo(dupApOngdApvLnDVo.getApvLnNo());
					apOngdApvLnDVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.updateApvLnNo");
					queryQueue.add(apOngdApvLnDVo);
				}
			}
			
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnPno(apvLnPno);
			apOngdApvLnDVo.setApvLnNo(apvLnNo);
			
			// 결재자UID - 파라미터
			apOngdApvLnDVo.setApvrUid(apvrUid);
			apOngdApvLnDVo.setApvrDeptYn("N");//결재자부서여부
			
			// 결재자역할코드
			apOngdApvLnDVo.setApvrRoleCd("revw");//revw:검토
			// 결재상태코드
			apOngdApvLnDVo.setApvStatCd("inApv");//결재상태코드 - inApv:결재중
			// 이전결재자결재일시
			apOngdApvLnDVo.setPrevApvrApvDt("sysdate");
			// 결재라인이력번호
			apOngdApvLnDVo.setApvLnHstNo(storedApOngdApvLnDVo.getApvLnHstNo());
			// 조회일자 삭제 - 재검토로 되돌려 보낸뒤 다시 받았을때 - 볼드 표시
			apOngdApvLnDVo.setVwDt("");
			// 결재의견내용 - 삭제함
			apOngdApvLnDVo.setApvOpinCont("");
			apOngdApvLnDVo.setApvOpinDispYn("");
			
			// 결재선 데이터에 문서언어에 따른 리스스 바인딩
			setApOngdApvLnDVoResc(apOngdApvLnDVo, storedApOngdBVo.getDocLangTypCd(), 
					null, null, optConfigMap, locale);
			
			// 메신저 보내기
			addMesseger(storedApOngdBVo, apOngdApvLnDVo, userVo, messengerQueue, locale);
			
			// 문서상태 : 처리부서 > 검토
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setDocStatCd("revw");
			apOngdBVo.setCurApvrId(apOngdApvLnDVo.getApvrUid());
			apOngdBVo.setCurApvrNm(apOngdApvLnDVo.getApvrNm());
			queryQueue.update(apOngdBVo);
			
			// 저장
			queryQueue.store(apOngdApvLnDVo);
			
		}
	}
	
	/** 메신저 메세지큐에 메세지 더하기 */
	private void addMesseger(ApOngdBVo apOngdBVo, ApOngdApvLnDVo apOngdApvLnDVo, UserVo userVo, List<Map<String, String>> messengerQueue, Locale locale) throws SQLException{
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		boolean isApvrDept = "Y".equals(apOngdApvLnDVo.getApvrDeptYn());
		boolean msgDeptBxEnable = "Y".equals(optConfigMap.get("msgDeptBx"));// [옵션] 부서 대기함 알림 사용
		boolean msgRecvBxEnable = "Y".equals(optConfigMap.get("msgRecvBx"));// [옵션] 접수함 알림 사용
		
//		// 시스템 정책 조회
//		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 알림 메일로 보내기
		//boolean sendMailNoti = "Y".equals(sysPlocMap.get("mailEnable")) && "Y".equals(optConfigMap.get("sendMailNoti"));
		boolean sendMailNoti = "Y".equals(optConfigMap.get("sendMailNoti"));
		
		if(isApvrDept && !msgDeptBxEnable && !msgRecvBxEnable){
			return;
		}
		
		String enfcStatCd = apOngdBVo.getEnfcStatCd();
//		String docProsStatCd = apOngdBVo.getDocProsStatCd();
		String apvStatCd = apOngdApvLnDVo.getApvStatCd();
		
		String apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
		String apvLnPno = apOngdApvLnDVo.getApvLnPno();
		String apvLnNo = apOngdApvLnDVo.getApvLnNo();
		
		// 리눅스 심사함 제외
		if(ServerConfig.IS_LINUX && "inCensr".equals(enfcStatCd)){
			return;
		}
		
		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		String webDomain = svrEnvMap.get("webDomain");
		
		if(isApvrDept){
			// 부서대기함 알림
			if(		// 부서대기함 알림
					(msgDeptBxEnable && ("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd) || "prcDept".equals(apvrRoleCd)))
					||
					// 접수함 알림
					(msgRecvBxEnable && "inSending".equals(enfcStatCd))
					){
				String deptId = apOngdApvLnDVo.getApvDeptId();
				String roleCd = "R02";//문서담당자(역할코드)
				List<OrUserBVo> orUserBVoList = orCmSvc.getUserListByDeptIdRoleCd(deptId, roleCd);
				if(orUserBVoList != null){
					for(OrUserBVo deptOrUserBVo : orUserBVoList){
						addMessegerQueue(apOngdBVo, 
								apvrRoleCd, apvLnPno, apvLnNo,
								deptOrUserBVo.getUserUid(), deptOrUserBVo.getOdurUid(), apvStatCd, webDomain, apOngdApvLnDVo.getSendSeq(), 
								messengerQueue, sendMailNoti, userVo, locale);
					}
				}
			}
		}  else {
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			boolean pcNotiEnable = "Y".equals(sysPlocMap.get("pcNotiEnable")) && "Y".equals(sysPlocMap.get("pcNotiAP"));
			String apvrOdurUid = null;
			if(pcNotiEnable){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(apOngdApvLnDVo.getApvrUid());
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo != null){
					apvrOdurUid = orUserBVo.getOdurUid();
				}
			}
			
			addMessegerQueue(apOngdBVo, 
					apvrRoleCd, apvLnPno, apvLnNo,
					apOngdApvLnDVo.getApvrUid(), apvrOdurUid, apvStatCd, webDomain, null,
					messengerQueue, sendMailNoti, userVo, locale);
		}
	}
	
	/** 메세지 큐에 더하기 */
	private void addMessegerQueue(ApOngdBVo apOngdBVo, 
			String apvrRoleCd, String apvLnPno, String apvLnNo,
			String apvrUid, String apvrOdurUid, String apvStatCd, String webDomain, String sendSeq,
			List<Map<String, String>> messengerQueue, 
			boolean sendMailNoti, UserVo userVo, Locale locale) throws SQLException{
		
		Map<String, String> messengerData = new HashMap<String, String>();
		String bxId = null;
		
		String enfcStatCd = apOngdBVo.getEnfcStatCd();
		String docProsStatCd = apOngdBVo.getDocProsStatCd();
		
		String langTypCd = ptPsnSvc.getLastLginLangTypCd(apvrUid, false);
		if(langTypCd==null) langTypCd = locale.getLanguage();
		Locale ApvrLocale = SessionUtil.toLocale(langTypCd);
		
		// 상태에 따른 제목 세팅
		if("inCensr".equals(enfcStatCd)){
			bxId = "censrBx";
			messengerData.put("subj", "["+messageProperties.getMessage("ap.cfg.censr", ApvrLocale)+"] "+apOngdBVo.getDocSubj());
		} else if("inSending".equals(enfcStatCd)){
			bxId = "recvBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.recv", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("inVw".equals(apvStatCd)){
			bxId = "waitBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.pubVw", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("inInfm".equals(apvStatCd)){
			bxId = "postApvdBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.infm", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd) || "prcDept".equals(apvrRoleCd)){
			bxId = "deptBx";
			messengerData.put("subj", "["+messageProperties.getMessage("ap.option.dept", ApvrLocale)+"] "+apOngdBVo.getDocSubj());
		} else if("rejt".equals(docProsStatCd)){
			bxId = "rejtBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.rejt", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("apvd".equals(docProsStatCd)){
			bxId = "myBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.apvd", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("inApv".equals(apvStatCd)){
			bxId = "waitBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.apv", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("inAgr".equals(apvStatCd)){
			bxId = "waitBx";
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.agr", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("reRevw".equals(apvStatCd)){
			if("0".equals(apvLnPno) && "1".equals(apvLnNo)){
				bxId = "myBx";//기안함
			} else {
				bxId = "waitBx";//대기함
			}
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.reRevw", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else if("inRefVw".equals(apvStatCd)){
			bxId = "refVwBx";//참조열람
			messengerData.put("subj", "["+ptSysSvc.getTerm("ap.term.refVw", ApvrLocale.getLanguage())+"] "+apOngdBVo.getDocSubj());
		} else {
			return;
		}
		
		String pushMsgId = StringUtil.getNextHexa(24);
		String webListDo = ("regRecLst".equals(bxId) || "recvRecLst".equals(bxId)) ? "listApvRecBx.do" : "listApvBx.do";
		
		// 메뉴ID를 붙인 URL 리턴
		// - 문제점 : 모바일:모바일 menuId, PC:PC menuId를 리턴함
		//url = apBxSvc.getDocUrlByBxId(adminVo, bxId, null);
		
		messengerData.put("pushMsgId", pushMsgId);
		messengerData.put("compId", apOngdBVo.getCompId());
		
		// 시스템 정책 조회
//		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
//		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		
//		String url = (useSSL ? "https://" : "http://")+webDomain+"/index.do?msgId="+pushMsgId;
		String url = "http://"+webDomain+"/index.do?msgId="+pushMsgId;
		messengerData.put("url", url);
		
		if(sendMailNoti){
			String mailNotiHTML = getNotiMailHTML(apOngdBVo, docProsStatCd, bxId, userVo, url, ApvrLocale);
			messengerData.put("mailNotiHTML", mailNotiHTML);
		}
		
		// 심사함 - 모바일 심사함 없음
		if("censrBx".equals(bxId)){
			
			// PC용
			messengerData.put("webUrl", "/ap/box/setDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo());
			messengerData.put("webAuthUrl", "/ap/box/"+webListDo+"?bxId="+bxId);
			// 모바일 디바이스용 - 모바일은 심사함 없음
			
		// 접수함
		} else if("recvBx".equals(bxId)){
			
			// PC용
			messengerData.put("webUrl", "/ap/box/setDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo()+"&sendSeq="+sendSeq);
			messengerData.put("webAuthUrl", "/ap/box/"+webListDo+"?bxId="+bxId);
			// 모바일 디바이스용 - 모바일은 접수함 없음
			
		// 심사함을 제외한 함 + 대장
		} else {
			
			// 참조열람
			if("recvBx".equals(bxId)){
				// PC용
				messengerData.put("webUrl", "/ap/box/setDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo());
				messengerData.put("webAuthUrl", "/ap/box/"+webListDo+"?bxId="+bxId);
				
				// 모바일 디바이스용
				messengerData.put("mobUrl", "/ap/box/viewDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo());
				messengerData.put("mobAuthUrl", "/ap/box/listApvBx.do?bxId="+bxId);
			} else {
				// PC용
				messengerData.put("webUrl", "/ap/box/setDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo()+"&apvLnPno="+apvLnPno+"&apvLnNo="+apvLnNo);
				messengerData.put("webAuthUrl", "/ap/box/"+webListDo+"?bxId="+bxId);
				
				// 모바일 디바이스용
				messengerData.put("mobUrl", "/ap/box/viewDoc.do?bxId="+bxId+"&apvNo="+apOngdBVo.getApvNo()+"&apvLnPno="+apvLnPno+"&apvLnNo="+apvLnNo);
				messengerData.put("mobAuthUrl", "/ap/box/listApvBx.do?bxId="+bxId);
			}
			
			// 개인정보 - 휴대폰 조회
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			orUserPinfoDVo.setUserUid(apvrUid);
			orUserPinfoDVo = (OrUserPinfoDVo)commonDao.queryVo(orUserPinfoDVo);
			if(orUserPinfoDVo != null){
				// 휴대전화번호암호값
				String mbnoEnc = orUserPinfoDVo.getMbnoEnc();
				// 휴대전화가 있으면 - 복호화 후 세팅
				if(mbnoEnc!=null && !mbnoEnc.isEmpty()){
					try{
						String mbno = cryptoSvc.decryptPersanal(mbnoEnc);
						messengerData.put("mbno", mbno);
					} catch(Exception ignore){}
				}
			}
		}
		
		messengerData.put("apvNo", apOngdBVo.getApvNo());
		messengerData.put("bxId", bxId);
		messengerData.put("langTypCd", ApvrLocale.getLanguage());
		
		messengerData.put("recvUid", apvrUid);
		messengerData.put("odurUid", apvrOdurUid);
		// 관리자 일경우는 - 기안자로 변경
		if("U0000001".equals(userVo.getUserUid())){
			messengerData.put("sendUid", apOngdBVo.getMakrUid());
		} else {
			messengerData.put("sendUid", userVo.getUserUid());
		}
		// em.messanger.catNm.ap=결재
		messengerData.put("catNm", messageProperties.getMessage("em.messanger.catNm.ap", ApvrLocale));
		messengerData.put("msgKey", "AP_"+apOngdBVo.getApvNo());
		messengerData.put("contents", "");
		
		messengerQueue.add(messengerData);
	}
	
	/** 메일로 통보할 경우(옵션) - 메일 본문 만들기 */
	private String getNotiMailHTML(ApOngdBVo apOngdBVo, String docProsStatCd, String bxId,
			UserVo userVo, String url, Locale ApvrLocale) throws SQLException{
		
		boolean hasUrl = url!=null && !url.isEmpty();
		String langTypCd = ApvrLocale.getLanguage();
		
		if(apOngdBVo.getMakDeptNm()==null || apOngdBVo.getDocLangTypCd()==null || apOngdBVo.getMakDeptId()==null){
			ApOngdBVo storedApOngdBVo = new ApOngdBVo();
			storedApOngdBVo.setApvNo(apOngdBVo.getApvNo());
			storedApOngdBVo.setQueryLang(langTypCd);
			storedApOngdBVo = (ApOngdBVo)commonDao.queryVo(storedApOngdBVo);
			if(storedApOngdBVo != null) apOngdBVo = storedApOngdBVo;
		}
		
		String leftWidth = "120px";
		if("ko".equals(langTypCd) || "jp".equals(langTypCd) || "zh".equals(langTypCd)){
			leftWidth = "70px";
		}
		
		StringBuilder builder = new StringBuilder(1024*4);
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if("rejt".equals(docProsStatCd)){
			// ap.mailNoti.docRejected=결재문서가 반려 되었습니다.
			builder.append(messageProperties.getMessage("ap.mailNoti.docRejected", ApvrLocale));
		} else if("myBx".equals(bxId) && "apvd".equals(docProsStatCd)){
			// ap.mailNoti.docApproved=결재문서가 승인 되었습니다.
			builder.append(messageProperties.getMessage("ap.mailNoti.docApproved", ApvrLocale));
		} else {
			// ap.mailNoti.docArrived=결재문서가 도착했습니다.
			builder.append(messageProperties.getMessage("ap.mailNoti.docArrived", ApvrLocale));
			String bxNm = apCmSvc.getMenuRescNmByBxId(userVo.getCompId(), bxId, ApvrLocale.getLanguage());
			if(bxNm!=null){
				builder.append(" (").append(bxNm).append(")");
			}
		}
		builder.append("</p></br>\n");
		
		builder.append("<table border=\"0\">\n");
		
		builder.append("<tr><td width=\"").append(leftWidth).append("\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		// ap.mailNoti.docSubj=문서제목
		builder.append(messageProperties.getMessage("ap.mailNoti.docSubj", ApvrLocale)).append("</td>");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if(hasUrl){
			builder.append("<a href=\"").append(url).append("\" target=\"_blank\">");
		}
		builder.append(apOngdBVo.getDocSubj());
		if(hasUrl){
			builder.append("</a>");
		}
		builder.append("</td></tr>\n");
		
		if(apOngdBVo.getMakDeptNm()!=null){
			builder.append("<tr><td width=\"").append(leftWidth).append("\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			// ap.doc.makDeptNm=기안부서
			builder.append(messageProperties.getMessage("ap.doc.makDeptNm", ApvrLocale)).append("</td>");
			builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			
			if(langTypCd.equals(apOngdBVo.getDocLangTypCd())){
				builder.append(apOngdBVo.getMakDeptNm());
			} else {
				// 받는 사람 어권으로 전환
				OrOrgBVo orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(apOngdBVo.getMakDeptId());
				orOrgBVo.setQueryLang(langTypCd);
				orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
				if(orOrgBVo!=null && orOrgBVo.getRescNm()!=null){
					builder.append(orOrgBVo.getRescNm());
				} else {
					builder.append(apOngdBVo.getMakDeptNm());
				}
			}
			
			builder.append("</td></tr>\n");
		}
		if(apOngdBVo.getMakrNm()!=null){
			builder.append("<tr><td width=\"").append(leftWidth).append("\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			// ap.doc.makrNm=기안자
			builder.append(messageProperties.getMessage("ap.doc.makrNm", ApvrLocale)).append("</td>");
			builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			
			if(langTypCd.equals(apOngdBVo.getDocLangTypCd())){
				builder.append(apOngdBVo.getMakrNm());
			} else {
				// 받는 사람 어권으로 전환
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(apOngdBVo.getMakrUid());
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo!=null && orUserBVo.getRescNm()!=null){
					builder.append(orUserBVo.getRescNm());
				} else {
					builder.append(apOngdBVo.getMakrNm());
				}
			}
			builder.append("</td></tr>\n");
		}
		builder.append("<tr><td width=\"").append(leftWidth).append("\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");

		if("rejt".equals(docProsStatCd)){
			// ap.mailNoti.rejectTime=반려시간
			builder.append(messageProperties.getMessage("ap.mailNoti.rejectTime", ApvrLocale));
		} else if("myBx".equals(bxId) && "apvd".equals(docProsStatCd)){
			// ap.mailNoti.approvalTime=승인시간
			builder.append(messageProperties.getMessage("ap.mailNoti.approvalTime", ApvrLocale));
		} else {
			// ap.mailNoti.arrivalTime=도착시간
			builder.append(messageProperties.getMessage("ap.mailNoti.arrivalTime", ApvrLocale));
		}
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		builder.append(StringUtil.getCurrDateTime());
		builder.append("</td></tr>\n");
		
		builder.append("</table>");
		
		return builder.toString();
	}
	
	/** 메세지 일괄 메신저 발송 */
	public void sendMesseger(List<Map<String, String>> messengerQueue, Map<String, String> optConfigMap) throws SQLException {
		if(messengerQueue != null){
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			boolean messengerEnable = "Y".equals(sysPlocMap.get("messengerEnable"));
			boolean sendMailNoti = "Y".equals(optConfigMap.get("sendMailNoti"));
			boolean pcNotiEnable = "Y".equals(sysPlocMap.get("pcNotiEnable")) && "Y".equals(sysPlocMap.get("pcNotiAP"));
			
			if(!messengerEnable && !sendMailNoti && !pcNotiEnable) return;
			
			// 겸직 통합 표시
			boolean adurMerg = "Y".equals(optConfigMap.get("adurMergLst"));
			
			List<Map<String, String>> pcNotiList = pcNotiEnable ? new ArrayList<Map<String, String>>() : null;
			Map<String, String> pcNotiData;
			String uid, oid;
			
			for(Map<String, String> data : messengerQueue){
				// 메신저 보내기
				if(messengerEnable){
					try{
						messengerSvc.sendNotice(
								data.get("recvUid"),
								data.get("sendUid"),
								data.get("catNm"),
								data.get("subj"),
								data.get("contents"),
								data.get("url"),
								data.get("msgKey") );
					} catch(Exception ignore){
						ignore.printStackTrace();
					}
				}
				// 알림메일 보내기
				if(sendMailNoti){
					String sendUid = data.get("sendUid");
					String recvUid = data.get("recvUid");
					String subj = data.get("subj");
					String mailNotiHTML = data.get("mailNotiHTML");
					String langTypCd = data.get("langTypCd");
					if(mailNotiHTML!=null && !mailNotiHTML.isEmpty()){
						try {
							emailSvc.sendMailSvc(sendUid, new String[]{recvUid}, subj, mailNotiHTML, null, false, false, langTypCd);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				// PC 알림
				if(pcNotiEnable){
					pcNotiData = new HashMap<String, String>();
					if(adurMerg){
						oid = data.get("odurUid");
						if(oid!=null){
							pcNotiData.put("oid", oid);
						} else {
							uid = data.get("recvUid");
							OrUserBVo orUserBVo = new OrUserBVo();
							orUserBVo.setUserUid(uid);
							orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
							if(orUserBVo != null){
								oid = orUserBVo.getOdurUid();
								pcNotiData.put("oid", oid);
							}
						}
					} else {
						pcNotiData.put("uid", data.get("recvUid"));
					}
					pcNotiData.put("body", data.get("subj"));
					pcNotiData.put("url", "/index.do?msgId="+data.get("pushMsgId"));
					pcNotiData.put("title", data.get("catNm"));
					
					pcNotiList.add(pcNotiData);
				}
			}
			
			if(pcNotiList!=null && !pcNotiList.isEmpty()){
				ptWebPushMsgSvc.sendAsyncWebPush(pcNotiList);
			}
		}
	}
	
	/** 메세지 일괄 메신저 발송 */
	public void addPushMsg(List<Map<String, String>> messageQueue, QueryQueue queryQueue, List<PtPushMsgDVo> ptPushMsgDVoList, Map<String, String> optConfigMap) throws SQLException {
		
		if(messageQueue != null){
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			boolean pushEnabled = "Y".equals(sysPlocMap.get("mobileEnable")) && "Y".equals(sysPlocMap.get("mobilePushEnable"));
			boolean pcNotiEnable = "Y".equals(sysPlocMap.get("pcNotiEnable")) && "Y".equals(sysPlocMap.get("pcNotiAP"));
			boolean sendMailNoti = "Y".equals(optConfigMap.get("sendMailNoti"));
			if(!(pushEnabled || pcNotiEnable || sendMailNoti) && "Y".equals(sysPlocMap.get("pushMsgLog"))){
				LOGGER.info("Push disabled !");
			}
			
			PtPushMsgDVo ptPushMsgDVo;
			for(Map<String, String> data : messageQueue){
				
				ptPushMsgDVo = new PtPushMsgDVo();
				ptPushMsgDVo.setPushMsgId(data.get("pushMsgId"));
				ptPushMsgDVo.setLangTypCd(data.get("langTypCd"));
				ptPushMsgDVo.setMdRid("AP");
				ptPushMsgDVo.setMdId(data.get("apvNo"));
				ptPushMsgDVo.setPushSubj(data.get("subj"));
				ptPushMsgDVo.setMbno(data.get("mbno"));
				
				ptPushMsgDVo.setWebUrl(data.get("webUrl"));
				ptPushMsgDVo.setWebAuthUrl(data.get("webAuthUrl"));
				ptPushMsgDVo.setMobUrl(data.get("mobUrl"));
				ptPushMsgDVo.setMobAuthUrl(data.get("mobAuthUrl"));
				
				ptPushMsgDVo.setUserUid(data.get("recvUid"));
				ptPushMsgDVo.setCompId(data.get("compId"));
				
				ptPushMsgDVo.setIsuDt("sysdate");
				ptPushMsgDVo.setValdLginCnt("3");
				
				// 푸쉬메시지상세(PT_PUSH_MSG_D) 테이블 - INSERT
				queryQueue.insert(ptPushMsgDVo);
				
				// 푸쉬 메세지용 목록에 따로 담음 - 푸쉬앱 발송용
				if(pushEnabled && data.get("mbno") != null
						 && !"censrBx".equals(data.get("bxId"))
						 && !"recvBx".equals(data.get("bxId"))){
					ptPushMsgDVoList.add(ptPushMsgDVo);
				}
			}
		}
	}
	
	/** ERP 연계 - ERP 시작 연계 */
	private AsyncNotiData createErpNotiData(String intgTypCd, String intgNo, String apvNo, String intgStatCd, 
			String docNo, QueryQueue queryQueue, Locale locale) throws SQLException, CmException{
		
		if(intgNo==null || intgNo.isEmpty()) return null;
		
		ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
		apErpIntgBVo.setIntgNo(intgNo);
		ApErpIntgBVo storedApErpIntgBVo = (ApErpIntgBVo)commonDao.queryVo(apErpIntgBVo);
		
		if(storedApErpIntgBVo==null){
			return null;
		}
		
		// 연계상태코드가 동일하면 - 연계 통보 하지 않음
		if(intgStatCd.equals(storedApErpIntgBVo.getIntgStatCd())){
			return null;
		}
		
		apErpIntgBVo = new ApErpIntgBVo();
		apErpIntgBVo.setIntgNo(intgNo);
		if("cncl".equals(intgStatCd)){
			apErpIntgBVo.setApvNo("");
		} else {
			apErpIntgBVo.setApvNo(apvNo);
		}
		
		if(intgStatCd!=null && !intgStatCd.equals(storedApErpIntgBVo.getIntgStatCd())){
			apErpIntgBVo.setIntgStatCd(intgStatCd);
			queryQueue.update(apErpIntgBVo);
		}
		
		// 연계구분코드
		if("ERP_HANWHA".equals(intgTypCd)){
			// 한화제약 에서 ERP 처리 방식
			String refVa = storedApErpIntgBVo.getRefVa();
			if(refVa==null || refVa.isEmpty()) return null;
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(refVa);
			Map<String, String> exMap = new HashMap<String, String>();
			exMap.put("apvNo", apvNo);
			exMap.put("intgTypCd", intgTypCd);
			exMap.put("intgStatCd", intgStatCd);
			String va;
			for(String key : new String[]{"gubun","fid","oid","mid"}){
				va = (String)jsonObject.get(key);
				if(va!=null) exMap.put(key, va);
			}
			AsyncNotiData AsyncNotiData = new AsyncNotiData(exMap);
			return AsyncNotiData;
		} else if("ERP_ONECARD".equals(intgTypCd)){
			// 센텍 - 원카드
			String refVa = storedApErpIntgBVo.getRefVa();
			if(refVa==null || refVa.isEmpty()) return null;
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(refVa);
			Map<String, String> exMap = new HashMap<String, String>();
			exMap.put("apvNo", apvNo);
			exMap.put("intgTypCd", intgTypCd);
			exMap.put("intgStatCd", intgStatCd);
			String va;
			for(String key : new String[]{"onecNo","ver"}){
				va = (String)jsonObject.get(key);
				if(va!=null) exMap.put(key, va);
			}
			AsyncNotiData AsyncNotiData = new AsyncNotiData(exMap);
			return AsyncNotiData;
		} else {
			String url = storedApErpIntgBVo.getIntgUrl();
			if(url==null || url.isEmpty()) return null;
			String rid = storedApErpIntgBVo.getRid();
			String docNoParam = "";
			try {
				// UniERP 의 경우
				// ERP에서 한글 깨지는 문제 발생해서 'EUC-KR' 로 변경(2016-04-05)
				String encCharset = url.indexOf("UI/GW_IF/return_status.asp")>0 ? "EUC-KR" : "UTF-8";
				rid = (rid==null || rid.isEmpty()) ? "" : "&rid="+URLEncoder.encode(rid.trim(), encCharset);
				docNoParam = (!"apvd".equals(intgStatCd) || docNo==null || docNo.isEmpty()) ? "" : "&docNo="+URLEncoder.encode(docNo, encCharset);
			} catch (UnsupportedEncodingException e) {}
			
			url = url + (url.indexOf('?') > 0 ? "&" : "?") + "intgNo="+intgNo+rid+docNoParam+"&intgStatCd="+intgStatCd;
			AsyncNotiData AsyncNotiData = new AsyncNotiData(url, null, "UTF-8", true);
			return AsyncNotiData;
		}
	}
	
	/** 검색 색인 데이터를 더함 */
	public void addSrchIndex(ApOngdBVo apOngdBVo, UserVo userVo, QueryQueue queryQueue) throws SQLException{
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		//통합검색 사용여부
		if(!"Y".equals(sysPlocMap.get("integratedSearchEnable"))){
			return;
		}
		
		UserVo adminVo = ptSecuSvc.createEmptyAdmin(userVo.getCompId(), userVo.getLoutCatId(), userVo.getLangTypCd());
		
		// 검색엔진 인덱싱 처리
		CmSrchBVo cmSrchBVo = emSrchSvc.createVo();
		cmSrchBVo.setCompId(userVo.getCompId());
		cmSrchBVo.setMdRid("AP");
		cmSrchBVo.setMdBxId(apOngdBVo.getRecLstTypCd());
		cmSrchBVo.setMdId(apOngdBVo.getApvNo());
		// 리소스ID세팅
		ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "AP");
		if(mnuList!=null){
			for(PtMnuDVo ptMnuDVo : mnuList){
				if(ptMnuDVo.getMdId().equals(apOngdBVo.getRecLstTypCd())){
					cmSrchBVo.setMdBxRescId(ptMnuDVo.getRescId());
					break;
				}
			}
		}
		
		// I:INSERT, U:UPDATE, D:DELETE, C:CATEGORY UPDATE
		cmSrchBVo.setActId("I");
		cmSrchBVo.setUrl("/cm/ap/redirect.do?bxId="+apOngdBVo.getRecLstTypCd()+"&apvNo="+apOngdBVo.getApvNo());
		cmSrchBVo.setRegDt("sysdate");
		queryQueue.insert(cmSrchBVo);
	}
	
	public boolean reindexAp(UserVo userVo, String compId) throws SQLException {
		
		ApOngdBVo apOngdBVo;
		
		// 대장 리소스ID세팅
		String mxRescId = null;
		UserVo adminVo = ptSecuSvc.createEmptyAdmin(compId, userVo.getLoutCatId(), userVo.getLangTypCd());
		ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "AP");
		
		boolean hasReindexData = false;
		Integer pageNo=0, pageRowCnt = 1000;
		QueryQueue queryQueue;
		CmSrchBVo cmSrchBVo;
		
		// regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		for(String recLstTypCd : new String[]{"regRecLst","recvRecLst","distRecLst"}){
			
			pageNo=0;
			
			// 리소스ID세팅
			mxRescId = null;
			if(mnuList!=null){
				for(PtMnuDVo ptMnuDVo : mnuList){
					if(ptMnuDVo.getMdId().equals(recLstTypCd)){
						mxRescId = ptMnuDVo.getRescId();
						break;
					}
				}
			}
			if(mxRescId==null) continue;
			
			while(true){
				
				apOngdBVo = new ApOngdBVo();
				apOngdBVo.setCompId(compId);
				apOngdBVo.setRecLstTypCd(recLstTypCd);
				if("regRecLst".equals(recLstTypCd)){
					apOngdBVo.setRegRecLstRegYn("Y");
				}
				apOngdBVo.setOrderBy("APV_NO ASC");
				
				apOngdBVo.setPageNo(++pageNo);
				apOngdBVo.setPageRowCnt(pageRowCnt);
				
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
				if(apOngdBVoList != null && !apOngdBVoList.isEmpty()){
					
					queryQueue = new QueryQueue();
					for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
						
						// 검색 인덱싱 처리
						cmSrchBVo = emSrchSvc.createVo();
						cmSrchBVo.setCompId(userVo.getCompId());
						cmSrchBVo.setMdRid("AP");
						cmSrchBVo.setMdBxId(storedApOngdBVo.getRecLstTypCd());
						cmSrchBVo.setMdId(storedApOngdBVo.getApvNo());
						cmSrchBVo.setMdBxRescId(mxRescId);
						cmSrchBVo.setActId("I");
						cmSrchBVo.setUrl("/cm/ap/redirect.do?bxId="+storedApOngdBVo.getRecLstTypCd()+"&apvNo="+storedApOngdBVo.getApvNo());
						cmSrchBVo.setRegDt("sysdate");
						queryQueue.insert(cmSrchBVo);
					}
					
					if(!queryQueue.isEmpty()){
						hasReindexData = true;
						commonSvc.execute(queryQueue);
					}
					if(apOngdBVoList.size() < pageRowCnt){
						break;
					}
				} else {
					break;
				}
			}//while(true){
			
		}//for(String recLstTypCd : new String[]{"regRecLst","recvRecLst","distRecLst"}){
		return hasReindexData;
	}
	
	/** 바닥글 치환 */
	public String replaceFooterVa(String footerVa, String orgId) throws SQLException{
		if(footerVa==null || footerVa.isEmpty()) return "";
		
		String returnVa = footerVa, replaceVa;
		String[] findings = {"#phone","#fax","#email","#homepage","#address"};
		
		boolean dataSelected = false;
		OrOrgCntcDVo orOrgCntcDVo = null;
		for(String finding : findings){
			
			if(returnVa.indexOf(finding)>=0){
				if(!dataSelected){
					dataSelected = true;
					
					orOrgCntcDVo = new OrOrgCntcDVo();
					orOrgCntcDVo.setOrgId(orgId);
					orOrgCntcDVo = (OrOrgCntcDVo)commonDao.queryVo(orOrgCntcDVo);
				}
				
				if(orOrgCntcDVo != null){
					if("#phone".equals(finding)){
						replaceVa = orOrgCntcDVo.getPhon();
					} else if("#fax".equals(finding)){
						replaceVa = orOrgCntcDVo.getFno();
					} else if("#email".equals(finding)){
						replaceVa = orOrgCntcDVo.getRepEmail();
					} else if("#homepage".equals(finding)){
						replaceVa = orOrgCntcDVo.getRepHpageUrl();
					} else if("#address".equals(finding)){
						replaceVa = (orOrgCntcDVo.getAdr()==null ? "" : orOrgCntcDVo.getAdr().trim())
								+ " "
								+(orOrgCntcDVo.getDetlAdr()==null ? "" : orOrgCntcDVo.getDetlAdr().trim());
					} else {
						replaceVa = "";
					}
					replaceVa = (replaceVa==null) ? "" : replaceVa.trim();
				} else {
					replaceVa = "";
				}
				returnVa = returnVa.replace(finding, replaceVa);
			}
		}
		
		return returnVa;
	}
	
	/** 추가검토  */
	public void processAdiRevw(JSONObject jsonObject, QueryQueue queryQueue, List<Map<String, String>> messengerQueue,
			UserVo userVo, ModelMap model, Locale locale) throws SQLException, CmException {

		String apvNo = (String)jsonObject.get("apvNo");
		String apvLnPno = (String)jsonObject.get("apvLnPno");
		String apvLnNo = (String)jsonObject.get("apvLnNo");
		String apvrUid = (String)jsonObject.get("apvrUid");
		String apvrRoleCd = (String)jsonObject.get("apvrRoleCd");
		String dblApvTypCd = (String)jsonObject.get("dblApvTypCd");
		String apvOpinCont = (String)jsonObject.get("apvOpinCont");
		
		String message = null;
		if(apvNo==null || apvLnPno==null || apvLnNo==null || apvrUid==null || apvrRoleCd==null){
			// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
			message = messageProperties.getMessage("cm.msg.notValidPage", locale);
			model.put("message", message);
			LOGGER.error("processAdiRevw - missing param - apvNo:"+apvNo+"  apvLnPno:"+apvLnPno+"  apvLnNo:"+apvLnNo+"  apvrUid:"+apvrUid+"  apvrRoleCd:"+apvrRoleCd);
			return;
		}
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			// ap.msg.noDoc=해당 문서가 없습니다.
			message = messageProperties.getMessage("ap.msg.noDoc", locale);
			model.put("message", message);
			return;
		}
		if(!"ongo".equals(apOngdBVo.getDocProsStatCd())){
			// ap.msg.notStat={0} 할 수 있는 상태가 아닙니다.
			message = messageProperties.getMessage("ap.msg.noDoc", locale);
			model.put("message", message);
			return;
		}
		String docLangTypCd = apOngdBVo.getDocLangTypCd();
		
		//////////////////////////////////////////////////////
		//
		//		도장방 표시 칸을 넘치는지 체크 : S
		
		// 양식 조회
		ApOngoFormBVo apOngoFormBVo = new ApOngoFormBVo();
		apOngoFormBVo.setFormId(apOngdBVo.getFormId());
		apOngoFormBVo.setFormSeq(apOngdBVo.getFormSeq());
		apOngoFormBVo = (ApOngoFormBVo)commonDao.queryVo(apOngoFormBVo);
		if(apOngoFormBVo == null){
			// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
			message = messageProperties.getMessage("cm.msg.notValidPage", locale);
			model.put("message", message);
			LOGGER.error("processAdiRevw - NO ApOngoFormBVo - formId:"+apOngdBVo.getFormId()+"  formSeq:"+apOngdBVo.getFormSeq());
			return;
		}
		
		String formApvLnTypCd = apOngoFormBVo.getFormApvLnTypCd();
		boolean apvLnMixd = "apvLnMixd".equals(formApvLnTypCd);		// 결재(합의1칸)
		boolean apvLn = "apvLn".equals(formApvLnTypCd);				// 결재(합의표시안함)
		boolean apvLn1LnAgr = "apvLn1LnAgr".equals(formApvLnTypCd);	// 결재+합의(1줄)
		boolean apvLn2LnAgr = "apvLn2LnAgr".equals(formApvLnTypCd);	// 결재+합의(2줄)
		boolean apvLnDbl = "apvLnDbl".equals(formApvLnTypCd);		// 신청+처리(이중결재)
		
		// 양식별 최대 도장방 갯수 - 조회
		int apvCnt=0, agrCnt=0, reqCnt=0, prcCnt=0;
		if("apvLnMixd".equals(formApvLnTypCd) || "apvLn".equals(formApvLnTypCd) 
				|| "apvLn1LnAgr".equals(formApvLnTypCd) || "apvLn2LnAgr".equals(formApvLnTypCd)
				|| "apvLnDbl".equals(formApvLnTypCd)){
			
			ApOngoFormApvLnDVo apOngoFormApvLnDVo = new ApOngoFormApvLnDVo();
			apOngoFormApvLnDVo.setFormId(apOngdBVo.getFormId());
			apOngoFormApvLnDVo.setFormSeq(apOngdBVo.getFormSeq());
			@SuppressWarnings("unchecked")
			List<ApOngoFormApvLnDVo> apOngoFormApvLnDVoList = (List<ApOngoFormApvLnDVo>)commonDao.queryList(apOngoFormApvLnDVo);
			if(apOngoFormApvLnDVoList!=null){
				
				String apvLnTitlTypCd, maxCnt;
				for(ApOngoFormApvLnDVo storedApOngoFormApvLnDVo : apOngoFormApvLnDVoList){
					
					maxCnt = storedApOngoFormApvLnDVo.getMaxCnt();
					if(maxCnt != null && !maxCnt.isEmpty()){
						apvLnTitlTypCd = storedApOngoFormApvLnDVo.getApvLnTitlTypCd();
						if("apv".equals(apvLnTitlTypCd)){
							apvCnt = Integer.parseInt(maxCnt);
						} else if("agr".equals(apvLnTitlTypCd)){
							agrCnt = Integer.parseInt(maxCnt);
						} else if("req".equals(apvLnTitlTypCd)){
							reqCnt = Integer.parseInt(maxCnt);
						} else if("prc".equals(apvLnTitlTypCd)){
							prcCnt = Integer.parseInt(maxCnt);
						}
					}
				}
			}
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setOrderBy("APV_LN_PNO, APV_LN_NO");
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
		
		if(apOngdApvLnDVoList!=null){
			
			int apvCntCur=0, agrCntCur=0, reqCntCur=0, prcCntCur=0;
			String roleCd;

			// byOne:1인결재, mak:기안, revw:검토, 
			// psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
			// prcDept:처리부서, byOneAgr:합의1인결재, 
			// makAgr:합의기안, abs:공석, apv:결재, pred:전결, 
			// entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보
			String[] apvLnMixdRoleCds = { "byOne","mak","revw","revw2","revw3","psnOrdrdAgr","psnParalAgr","deptOrdrdAgr","deptParalAgr","abs","apv","pred" };
			String[] apvRoleCds = { "byOne","mak","revw","revw2","revw3","abs","apv","pred" };
			String[] agrRoleCds = { "psnOrdrdAgr","psnParalAgr","deptOrdrdAgr","deptParalAgr" };
			
			boolean stampOverflow = false;
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
				if("0".equals(storedApOngdApvLnDVo.getApvLnPno())){
					roleCd = storedApOngdApvLnDVo.getApvrRoleCd();
					
					if(apvLnMixd){//결재(합의1칸)
						if(ArrayUtil.isInArray(apvLnMixdRoleCds, roleCd)){
							if(ArrayUtil.isInArray(apvLnMixdRoleCds, apvrRoleCd)){
								apvCntCur++;
								if(apvCntCur>=apvCnt){
									stampOverflow = true;
									break;
								}
							}
						}
					} else if(apvLn){//결재(합의표시안함)
						if(ArrayUtil.isInArray(apvRoleCds, roleCd)){
							if(ArrayUtil.isInArray(apvRoleCds, apvrRoleCd)){
								apvCntCur++;
								if(apvCntCur>=apvCnt){
									stampOverflow = true;
									break;
								}
							}
						}
					} else if(apvLn1LnAgr || apvLn2LnAgr){//결재+합의(1줄), 결재+합의(2줄)
						if(ArrayUtil.isInArray(apvRoleCds, roleCd)){
							if(ArrayUtil.isInArray(apvRoleCds, apvrRoleCd)){
								apvCntCur++;
								if(apvCntCur>=apvCnt){
									stampOverflow = true;
									break;
								}
							}
						} else if(ArrayUtil.isInArray(agrRoleCds, roleCd)){
							if(ArrayUtil.isInArray(agrRoleCds, apvrRoleCd)){
								agrCntCur++;
								if(agrCntCur>=agrCnt){
									stampOverflow = true;
									break;
								}
							}
						}
					} else if(apvLnDbl){//결재(합의표시안함)
						if("reqDept".equals(dblApvTypCd) && "reqDept".equals(storedApOngdApvLnDVo.getDblApvTypCd())){
							if(ArrayUtil.isInArray(apvRoleCds, apvrRoleCd)){
								reqCntCur++;
								if(reqCntCur>=reqCnt){
									stampOverflow = true;
									break;
								}
							}
						} else if("prcDept".equals(dblApvTypCd) && "prcDept".equals(storedApOngdApvLnDVo.getDblApvTypCd())){
							prcCntCur++;
							if(prcCntCur>=prcCnt){
								stampOverflow = true;
								break;
							}
						}
					}
				}
			}
			
			if(stampOverflow){
				// ap.msg.noAddByForm=양식에 결재자 추가를 위한 공간이 없습니다.
				message = messageProperties.getMessage("ap.msg.noAddByForm", locale);
				model.put("message", message);
				return;
			}
			
			//		도장방 표시 칸을 넘치는지 체크 : E
			//
			//////////////////////////////////////////////////////
			
			ApOngdApvLnDVo imfmApOngdApvLnDVo = null;
			
			// 중복된 결재자 있는지 체크
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
				if(apvrUid.equals(storedApOngdApvLnDVo.getApvrUid())){
					if(ApDocUtil.isPsnInfmOfApvrRole(storedApOngdApvLnDVo.getApvrRoleCd())){
						imfmApOngdApvLnDVo = storedApOngdApvLnDVo;
					} else {
						// ap.apvLn.alrdyUser=이미 사용자가 결재라인에 지정되어 있습니다.
						message = messageProperties.getMessage("ap.apvLn.alrdyUser", locale);
						model.put("message", message);
						return;
					}
				}
			}
			
			boolean passed = false;
			String[] cmplCds = {"apvd", "rejt", "cons", "pros"};//apvd:승인, rejt:반려, cons:반대, pros:찬성
			Integer intPno=null, intNo=null;
			
			
			HashMap<Integer, Integer> pnoCvtMap = new HashMap<Integer, Integer>();
			
			QueryQueue reverseQueryQueue = new QueryQueue();
			
			// 기존 결재선 - 해당 사용자 뒤의 결재라인 - 결재라인번호,결재라인부모번호 변경
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
				
				if(!passed){
					if(apvLnPno.equals(storedApOngdApvLnDVo.getApvLnPno())
							&& apvLnNo.equals(storedApOngdApvLnDVo.getApvLnNo())){
						
						if(ArrayUtil.isInArray(cmplCds, storedApOngdApvLnDVo.getApvStatCd())){
							// ap.msg.alreadyDone=이미 처리한 문서 입니다.
							message = messageProperties.getMessage("ap.msg.alreadyDone", locale);
							model.put("message", message);
							return;
						} else {
							passed = true;
							
							if(imfmApOngdApvLnDVo!=null){
								apOngdApvLnDVo = new ApOngdApvLnDVo();
								apOngdApvLnDVo.setApvNo(apvNo);
								apOngdApvLnDVo.setApvLnPno(imfmApOngdApvLnDVo.getApvLnPno());
								apOngdApvLnDVo.setApvLnNo(imfmApOngdApvLnDVo.getApvLnNo());
								queryQueue.delete(apOngdApvLnDVo);
							}
						}
					}
				}
				
				
				if(passed){
					
					if("0".equals(apvLnPno)){
						
						if("0".equals(storedApOngdApvLnDVo.getApvLnPno())){
							
							apOngdApvLnDVo = new ApOngdApvLnDVo();
							apOngdApvLnDVo.setApvNo(apvNo);
							apOngdApvLnDVo.setWhereApvLnPno(storedApOngdApvLnDVo.getApvLnPno());
							apOngdApvLnDVo.setWhereApvLnNo(storedApOngdApvLnDVo.getApvLnNo());
							
							if(intNo==null){
								intNo  = Integer.valueOf(apvLnNo);
								apOngdApvLnDVo.setApvStatCd("befoApv");
								if(apvOpinCont!=null && !apvOpinCont.isEmpty()){
									// ap.cfg.adiRevw=추가검토
									String prefix = "["+messageProperties.getMessage("ap.cfg.adiRevw", SessionUtil.toLocale(docLangTypCd))+"]";
									if(!apvOpinCont.startsWith(prefix)) apvOpinCont = prefix+" "+apvOpinCont;
									apOngdApvLnDVo.setApvOpinCont(apvOpinCont);
								}
							}
							intNo++;
							apOngdApvLnDVo.setApvLnNo(intNo.toString());
							reverseQueryQueue.update(apOngdApvLnDVo);
							
							pnoCvtMap.put(Integer.valueOf(storedApOngdApvLnDVo.getApvLnNo()), intNo);
						} else {
							
							// 부서합의 라인의 경우 - 부모번호가 변경된 경우 변경
							intPno = pnoCvtMap.get(Integer.valueOf(storedApOngdApvLnDVo.getApvLnPno()));
							if(intPno!=null){
								
								apOngdApvLnDVo = new ApOngdApvLnDVo();
								apOngdApvLnDVo.setApvNo(apvNo);
								apOngdApvLnDVo.setWhereApvLnPno(storedApOngdApvLnDVo.getApvLnPno());
								apOngdApvLnDVo.setWhereApvLnNo(storedApOngdApvLnDVo.getApvLnNo());
								
								apOngdApvLnDVo.setApvLnPno(intPno.toString());
								reverseQueryQueue.update(apOngdApvLnDVo);
								
							}
						}
						
					} else {
						// 이 경우 구현 안함 - 부서합의 내에서 추가검토 없기로 함.
					}
					
				}
			}
			
			// reverse
			for(int i = reverseQueryQueue.size()-1; i>=0; i--){
				queryQueue.add(reverseQueryQueue.get(i));
			}
			
			// 추가 검토자 INSERT
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setApvLnPno(apvLnPno);
			apOngdApvLnDVo.setApvLnNo(apvLnNo);
			apOngdApvLnDVo.setApvLnHstNo(apOngdApvLnDVoList.get(0).getApvLnHstNo());
			
			apOngdApvLnDVo.setApvrRoleCd(apvrRoleCd);
			apOngdApvLnDVo.setApvrUid(apvrUid);
			
			apOngdApvLnDVo.setDblApvTypCd(dblApvTypCd);
			String apvStatCd = "psnOrdrdAgr".equals(apvrRoleCd) ? "inAgr" : "inApv";
			apOngdApvLnDVo.setApvStatCd(apvStatCd);
			
			apOngdApvLnDVo.setApvrDeptYn("N");
			apOngdApvLnDVo.setPrevApvrApvDt("sysdate");
			
			// 결재 옵션
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
			Map<Integer, OrUserBVo> userCacheMap = new HashMap<Integer, OrUserBVo>();
			setApOngdApvLnDVoResc(apOngdApvLnDVo, docLangTypCd, userCacheMap, null, optConfigMap, locale);
			
			queryQueue.insert(apOngdApvLnDVo);
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo updateApOngdBVo = new ApOngdBVo();
			updateApOngdBVo.setApvNo(apvNo);
			updateApOngdBVo.setCurApvrId(apOngdApvLnDVo.getApvrUid());
			updateApOngdBVo.setCurApvrNm(apOngdApvLnDVo.getApvrNm());
			updateApOngdBVo.setCurApvrDeptYn(apOngdApvLnDVo.getApvrDeptYn());
			updateApOngdBVo.setDocStatCd(apvrRoleCd);
			queryQueue.update(updateApOngdBVo);
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String webDomain = svrEnvMap.get("webDomain");
			String sendSeq = null;
			boolean sendMailNoti = "Y".equals(optConfigMap.get("sendMailNoti"));
			
			// 메신저, 메일 알림
			OrUserBVo orUserBVo = userCacheMap.get(Hash.hashUid(apvrUid));
			String apvrOdurUid = orUserBVo==null ? null : orUserBVo.getOdurUid();
			addMessegerQueue(apOngdBVo, apvrRoleCd, apvLnPno, apvLnNo, apvrUid, apvrOdurUid, apvStatCd,
					webDomain, sendSeq, messengerQueue, sendMailNoti, userVo, locale);
			
			// ap.trans.processOk={0} 처리 하였습니다.
			// ap.cfg.adiRevw=추가검토
			message = messageProperties.getMessage("ap.trans.processOk", new String[]{"#ap.cfg.adiRevw"}, locale);
			model.put("message", message);
			
		} else {
			// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
			message = messageProperties.getMessage("cm.msg.notValidPage", locale);
			model.put("message", message);
			LOGGER.error("processAdiRevw - NO apvLine data - apvNo:"+apvNo);
			return;
		}
	}

	/** 되돌리기 */
	public void turnBackDoc(JSONObject jsonObject, QueryQueue queryQueue,
			List<Map<String, String>> messengerQueue, UserVo userVo,
			ModelMap model, Locale locale) throws SQLException, CmException {
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		String apvNo = (String)jsonObject.get("apvNo");
		String apvLnNo = (String)jsonObject.get("apvLnNo");
		String message = null;
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			// ap.msg.noDoc=해당 문서가 없습니다.
			message = messageProperties.getMessage("cm.msg.notValidPage", locale);
			model.put("message", message);
			return;
		}
		if(!"ongo".equals(apOngdBVo.getDocProsStatCd())){
			// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
			// cm.btn.turnBack=되돌리기
			message = messageProperties.getMessage("ap.msg.not.stat", new String[]{"#cm.btn.turnBack"}, locale);
			model.put("message", message);
			return;
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
		
		if(apOngdApvLnDVoList==null){
			// ap.msg.not.stat={0} 할 수 있는 문서 상태가 아닙니다.
			// cm.btn.turnBack=되돌리기
			message = messageProperties.getMessage("ap.msg.not.stat", new String[]{"#cm.btn.turnBack"}, locale);
			model.put("message", message);
			return;
		}
		
		boolean isParal = false;
		boolean isDept = false;
		boolean isRoot = false;
		boolean isFirstChild = false;
		
		int intApvLnNo = 0;
		String apvStatCd, signAreaDtDisp, docLangTypCd = apOngdBVo.getDocLangTypCd();
		
		ApOngdBVo updateApOngdBVo = null;
		ApOngdApvLnDVo updateApOngdApvLnDVo, currApOngdApvLnDVo = null;
		
		for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
			
			// 파라미터 결재라인을 찾음
			if(currApOngdApvLnDVo==null){
				if("0".equals(storedApOngdApvLnDVo.getApvLnPno())
						&& storedApOngdApvLnDVo.getApvLnNo().equals(apvLnNo)){
					
					currApOngdApvLnDVo = storedApOngdApvLnDVo;
					
					// 진행문서기본(AP_ONGD_B) 테이블
					updateApOngdBVo = new ApOngdBVo();
					updateApOngdBVo.setApvNo(apvNo);
					updateApOngdBVo.setCurApvrId(storedApOngdApvLnDVo.getApvrUid());
					updateApOngdBVo.setCurApvrNm(storedApOngdApvLnDVo.getApvrNm());
					updateApOngdBVo.setCurApvrDeptYn(storedApOngdApvLnDVo.getApvrDeptYn());
//					updateApOngdBVo.setDocStatCd(apvStatCd);
					
					queryQueue.update(updateApOngdBVo);
					
					intApvLnNo = Integer.parseInt(storedApOngdApvLnDVo.getApvLnNo());
					
					isParal = ApDocUtil.isParalAgrOfApvrRole(storedApOngdApvLnDVo.getApvrRoleCd());
					isDept  = ApDocUtil.isDeptAgrRole(storedApOngdApvLnDVo.getApvrRoleCd());
					
					// 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블
					updateApOngdApvLnDVo = new ApOngdApvLnDVo();
					updateApOngdApvLnDVo.setApvNo(apvNo);
					updateApOngdApvLnDVo.setApvLnPno(storedApOngdApvLnDVo.getApvLnPno());
					updateApOngdApvLnDVo.setApvLnNo(storedApOngdApvLnDVo.getApvLnNo());
					
					apvStatCd = storedApOngdApvLnDVo.getApvStatCd();
					if(apvStatCd.equals("apvd")){
						updateApOngdApvLnDVo.setApvStatCd("inApv");
					} else if(apvStatCd.equals("pros") || apvStatCd.equals("cons")){
						updateApOngdApvLnDVo.setApvStatCd("inAgr");
					}
					
					if(storedApOngdApvLnDVo.getAgntUid()!=null && !storedApOngdApvLnDVo.getAgntUid().isEmpty()){
						updateApOngdApvLnDVo.setAgntUid("");
					}
					
					updateApOngdApvLnDVo.setSignDispVa("");
					updateApOngdApvLnDVo.setSignImgPath("");
					updateApOngdApvLnDVo.setVwDt("");
					updateApOngdApvLnDVo.setApvDt("");
					updateApOngdApvLnDVo.setPrevApvrApvDt("sysdate");
					updateApOngdApvLnDVo.setApvOpinCont("");
					updateApOngdApvLnDVo.setApvOpinDispYn("");
					
					if(isDept){
						updateApOngdApvLnDVo.setApvrUid("");
						updateApOngdApvLnDVo.setApvrNm("");
						updateApOngdApvLnDVo.setApvrPositCd("");
						updateApOngdApvLnDVo.setApvrPositNm("");
						updateApOngdApvLnDVo.setApvrTitleCd("");
						updateApOngdApvLnDVo.setApvrTitleNm("");
						updateApOngdApvLnDVo.setDtDispVa("");
					} else {
						signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, storedApOngdApvLnDVo, docLangTypCd, false);
						updateApOngdApvLnDVo.setDtDispVa(signAreaDtDisp);
					}
					
					queryQueue.update(updateApOngdApvLnDVo);
					
				}
			} else if(isParal){
				
				isParal = ApDocUtil.isParalAgrOfApvrRole(storedApOngdApvLnDVo.getApvrRoleCd());
				
			} else {
				
				isRoot = "0".equals(storedApOngdApvLnDVo.getApvLnPno());
				if(!isRoot && isDept
						&& apvLnNo.equals(storedApOngdApvLnDVo.getApvLnPno())
						&& "1".equals(storedApOngdApvLnDVo.getApvLnNo())){
					isFirstChild = true;
				} else {
					isFirstChild = false;
				}
				
				if(isRoot || intApvLnNo >= Integer.parseInt(storedApOngdApvLnDVo.getApvLnPno()) ){
					
					apvStatCd = storedApOngdApvLnDVo.getApvStatCd();
					if(isFirstChild || apvStatCd.equals("apvd") || apvStatCd.equals("pros") || apvStatCd.equals("cons") || apvStatCd.equals("inApv") || apvStatCd.equals("inAgr")){
						
						updateApOngdApvLnDVo = new ApOngdApvLnDVo();
						updateApOngdApvLnDVo.setApvNo(apvNo);
						updateApOngdApvLnDVo.setApvLnPno(storedApOngdApvLnDVo.getApvLnPno());
						updateApOngdApvLnDVo.setApvLnNo(storedApOngdApvLnDVo.getApvLnNo());
						
						if(isFirstChild){
							updateApOngdApvLnDVo.setApvStatCd("inAgr");
						} else if(apvStatCd.equals("apvd")){
							updateApOngdApvLnDVo.setApvStatCd("befoApv");
						} else if(apvStatCd.equals("pros") || apvStatCd.equals("cons")){
							updateApOngdApvLnDVo.setApvStatCd("befoAgr");
						}
						
						if(storedApOngdApvLnDVo.getAgntUid()!=null && !storedApOngdApvLnDVo.getAgntUid().isEmpty()){
							updateApOngdApvLnDVo.setAgntUid("");
						}
						
						updateApOngdApvLnDVo.setSignDispVa("");
						updateApOngdApvLnDVo.setSignImgPath("");
						updateApOngdApvLnDVo.setVwDt("");
						updateApOngdApvLnDVo.setApvDt("");
						if(isFirstChild){
							updateApOngdApvLnDVo.setPrevApvrApvDt("sysdate");
						} else {
							updateApOngdApvLnDVo.setPrevApvrApvDt("");
						}
						updateApOngdApvLnDVo.setApvOpinCont("");
						updateApOngdApvLnDVo.setApvOpinDispYn("");
						
						if(ApDocUtil.isDeptAgrRole(storedApOngdApvLnDVo.getApvrRoleCd())){
							updateApOngdApvLnDVo.setApvrUid("");
							updateApOngdApvLnDVo.setApvrNm("");
							updateApOngdApvLnDVo.setApvrPositCd("");
							updateApOngdApvLnDVo.setApvrPositNm("");
							updateApOngdApvLnDVo.setApvrTitleCd("");
							updateApOngdApvLnDVo.setApvrTitleNm("");
							updateApOngdApvLnDVo.setDtDispVa("");
						} else {
							signAreaDtDisp = getSignAreaDtDispVa(optConfigMap, storedApOngdApvLnDVo, docLangTypCd, false);
							updateApOngdApvLnDVo.setDtDispVa(signAreaDtDisp);
						}
						
						queryQueue.update(updateApOngdApvLnDVo);
						
					}
				}
				
			}
		}
		
	}

	/** 의견 저장 - 의견 저장하고 옵션에 따라 알림 메일 발송함 */
	public void processSaveOpin(JSONObject jsonObject, QueryQueue queryQueue,
			List<Map<String, String>> messengerQueue, UserVo userVo,
			ModelMap model, Locale locale) throws SQLException, CmException, IOException {
		
		String apvNo = (String)jsonObject.get("apvNo");
		String apvLnPno = (String)jsonObject.get("apvLnPno");
		String apvLnNo = (String)jsonObject.get("apvLnNo");
		String apvOpinCont = (String)jsonObject.get("apvOpinCont");
		String apvOpinDispYn = (String)jsonObject.get("apvOpinDispYn");
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		if(apOngdBVo==null){
			LOGGER.error("processSaveOpin - no AP_ONGD_B : apvNo:"+apvNo);
			//cm.msg.noData=해당하는 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.noData", locale));
			return;
		}
		ApOngdApvLnDVo myTurnApOngdApvLnDVo = new ApOngdApvLnDVo();
		myTurnApOngdApvLnDVo.setApvNo(apvNo);
		myTurnApOngdApvLnDVo.setApvLnPno(apvLnPno);
		myTurnApOngdApvLnDVo.setApvLnNo(apvLnNo);
		myTurnApOngdApvLnDVo = (ApOngdApvLnDVo)commonDao.queryVo(myTurnApOngdApvLnDVo);
		if(myTurnApOngdApvLnDVo==null){
			LOGGER.error("processSaveOpin - no AP_ONGD_APV_LN_D : apvNo:"+apvNo + " apvLnPno:"+apvLnPno + " apvLnNo:"+apvLnNo);
			//cm.msg.noData=해당하는 데이터가 없습니다.
			model.put("message", messageProperties.getMessage("cm.msg.noData", locale));
			return;
		}
		
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setApvLnNo(apvLnNo);
		apOngdApvLnDVo.setApvOpinCont(apvOpinCont);
		apOngdApvLnDVo.setApvOpinDispYn(apvOpinDispYn);
		
		commonSvc.update(apOngdApvLnDVo);
		model.put("result", "ok");
		//cm.msg.save.success=저장되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// [옵션] 의견 저장 메일 알림
		if("Y".equals(optConfigMap.get("saveOpinMail"))
				&& (apvOpinCont!=null && !apvOpinCont.isEmpty())){
			
			// 결재선 정보 관리용 유틸 - 전체 경로선을 가지고 루트경로, 현재경로, 부서별 경로 등을 리턴함
			ApvLines apvLines = new ApvLines(queryApvLn(apvNo), apvLnPno);
			
			List<ApOngdApvLnDVo> receiverList = ApDocTransUtil.getOpinMailApvrList(apvLines.getAllApvLn(), apvLnPno, apvLnNo);
			String subject = apOngdBVo.getDocSubj();
			String senderUid = myTurnApOngdApvLnDVo.getApvrUid();
			String apvrNm = myTurnApOngdApvLnDVo.getApvrNm();
			
			sendOpinMail(receiverList, 
					subject, apvOpinCont, senderUid, apvrNm, 
					apOngdBVo.getDocLangTypCd(), false, false, false, false, true, model);
			
		}
		
	}
	
	/** 읽지않음 처리(읽음처리) */
	public void processUnread(JSONObject jsonObject, QueryQueue queryQueue, UserVo userVo,
			ModelMap model, Locale locale) throws SQLException, CmException, IOException {
		
		List<?> list = (List<?>)jsonObject.get("list");
		String vwDt = "unread".equals(jsonObject.get("process")) ? "" : "sysdate";
		
		Map<?, ?> map;
		ApOngdApvLnDVo apOngdApvLnDVo;
		String apvNo, apvLnPno, apvLnNo;
		
		int i, size = list==null ? 0 : list.size();
		for(i=0; i<size; i++){
			map = (Map<?, ?>)list.get(i);
			
			apvNo = (String)map.get("apvNo");
			apvLnPno = (String)map.get("apvLnPno");
			apvLnNo = (String)map.get("apvLnNo");
			
			if(apvNo!=null && !apvNo.isEmpty()
					&& apvLnPno!=null && !apvLnPno.isEmpty()
					&& apvLnNo!=null && !apvLnNo.isEmpty()){
				
				apOngdApvLnDVo = new ApOngdApvLnDVo();
				apOngdApvLnDVo.setApvNo(apvNo);
				apOngdApvLnDVo.setApvLnPno(apvLnPno);
				apOngdApvLnDVo.setApvLnNo(apvLnNo);
				apOngdApvLnDVo.setVwDt(vwDt);
				
				queryQueue.update(apOngdApvLnDVo);
			}
		}
	}
	
	
}
