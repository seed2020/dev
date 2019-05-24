package com.innobiz.orange.web.ap.ctrl;

import java.io.File;
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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdExtnDocDVo;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 종이문서 관련 컨트롤러 */
@Controller
public class ApPaperDocCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApPaperDocCtrl.class);

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** [팝업] 종이문서 등록/수정 */
	@RequestMapping(value = {"/ap/box/setPaperDocPop", "/ap/box/viewPaperDocPop", "/ap/adm/box/viewPaperDocPop"})
	public String setPubBxPop(HttpServletRequest request,
			@Parameter(name="apvNo", required=false) String apvNo,
			@Parameter(name="mode", required=false) String mode,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		boolean viewMode = request.getRequestURI().indexOf("viewPaperDocPop")>0;
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(apvNo!=null && !apvNo.isEmpty()){
			
			// 저장소 조회
			String storage = apCmSvc.queryStorage(apvNo);
			
			// 진행문서외부문서상세(AP_ONGD_EXTN_DOC_D) 테이블 - 조회
			ApOngdExtnDocDVo apOngdExtnDocDVo = new ApOngdExtnDocDVo();
			apOngdExtnDocDVo.setApvNo(apvNo);
			apOngdExtnDocDVo.setStorage(storage);
			apOngdExtnDocDVo.setQueryLang(langTypCd);
			apOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(apOngdExtnDocDVo);
			if(apOngdExtnDocDVo != null){
				
				// 분리 등록의 경우
				if("spReg".equals(mode)){
					viewMode = true;
					model.put("spReg", Boolean.TRUE);
					// 원본결재번호
					String orgnApvNo = apOngdExtnDocDVo.getOrgnApvNo();
					if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
						model.put("orgnApvNo", orgnApvNo);
						apOngdExtnDocDVo = new ApOngdExtnDocDVo();
						apOngdExtnDocDVo.setApvNo(orgnApvNo);
						apOngdExtnDocDVo.setStorage(storage);
						apOngdExtnDocDVo.setQueryLang(langTypCd);
						apOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(apOngdExtnDocDVo);
						if(apOngdExtnDocDVo != null){
							model.put("apOngdExtnDocDVo", apOngdExtnDocDVo);
						} else {
							//ap.msg.notFoundSpDoc=분리등록 원본 문서를 찾을 수 없습니다.
							throw new CmException("ap.msg.notFoundSpDoc", locale);
						}
					} else {
						model.put("orgnApvNo", apvNo);
						model.put("apOngdExtnDocDVo", apOngdExtnDocDVo);
					}
					apOngdExtnDocDVo.setPageCnt(null);
					apOngdExtnDocDVo.setExtnDocContTypCd(null);
				} else {
					model.put("apOngdExtnDocDVo", apOngdExtnDocDVo);
				}
				
				if(apOngdExtnDocDVo.getOrgnApvNo()!=null && !apOngdExtnDocDVo.getOrgnApvNo().isEmpty()){
					if("set".equals(mode)){
						model.put("spReg", Boolean.TRUE);
					}
					viewMode = true;
				}
				
			} else {
				//ap.msg.notFoundSpDoc=분리등록 원본 문서를 찾을 수 없습니다.
				throw new CmException("ap.msg.notFoundSpDoc", locale);
			}
			
			// 진행문서기본(AP_ONGD_B) 테이블 - 조회
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setStorage(storage);
			apOngdBVo.setQueryLang(langTypCd);
			
			apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
			if(apOngdBVo != null){
				
				// 분리 등록의 경우
				if("spReg".equals(mode)){
					String nextSeq = apCmSvc.addNo(apOngdExtnDocDVo.getRegSeq(), 1);
					apOngdBVo.setDocNo(apOngdBVo.getDocNo()+"-"+nextSeq);
					apOngdBVo.setDocSubj(apOngdBVo.getDocSubj()+"-"+nextSeq);
				}
				model.put("apOngdBVo", apOngdBVo);
				
				// 분류정보
				if(apOngdBVo.getClsInfoId() != null && !apOngdBVo.getClsInfoId().isEmpty()){
					ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
					apClsInfoDVo.setOrgId(apOngdBVo.getMakDeptId());
					apClsInfoDVo.setClsInfoId(apOngdBVo.getClsInfoId());
					apClsInfoDVo.setQueryLang(langTypCd);
					apClsInfoDVo = (ApClsInfoDVo)commonSvc.queryVo(apClsInfoDVo);
					if(apClsInfoDVo!=null){
						apOngdBVo.setClsInfoNm(apClsInfoDVo.getRescNm());
					}
				}
			} else {
				//ap.msg.notFoundSpDoc=분리등록 원본 문서를 찾을 수 없습니다.
				throw new CmException("ap.msg.notFoundSpDoc", locale);
			}
			
			//////////////////////////////////////////
			// 첨부 조회
			
			if(!"spReg".equals(mode)){// 분리등록이 아닌 경우
				ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
				
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setAttHstNo("1");//첨부파일이력번호
				apOngdAttFileLVo.setStorage(storage);
				@SuppressWarnings("unchecked")
				List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
				if(apOngdAttFileLVoList != null && !apOngdAttFileLVoList.isEmpty()){
					model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
					
					// 다운로드|문서뷰어 사용여부
					emAttachViewSvc.chkAttachSetup(model, userVo.getCompId());
				}
			}
		}
		
		// 외부문서구분코드
		List<PtCdBVo> extnDocTypCdList = ptCmSvc.getCdList("EXTN_DOC_TYP_CD", langTypCd, "Y");
		model.put("extnDocTypCdList", extnDocTypCdList);
		// 외부문서컨텐츠구분코드
		List<PtCdBVo> extnDocContTypCdList = ptCmSvc.getCdList("EXTN_DOC_CONT_TYP_CD", langTypCd, "Y");
		model.put("extnDocContTypCdList", extnDocContTypCdList);

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
//		// 보안등급코드
//		List<PtCdBVo> seculCdList = ptCmSvc.getCdList("SECUL_CD", langTypCd, "Y");
//		model.put("seculCdList", seculCdList);
		
		// 문서보존기간코드
		List<PtCdBVo> docKeepPrdCdList = ptCmSvc.getCdList("DOC_KEEP_PRD_CD", langTypCd, "Y");
		model.put("docKeepPrdCdList", docKeepPrdCdList);
		
		// 결재 옵션 세팅
		apCmSvc.getOptConfigMap(model, userVo.getCompId());
		// 파일 - 설정
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
		
		if(viewMode){
			return LayoutUtil.getJspPath("/ap/box/viewPaperDocPop");
		} else {
			// 확장자 허용제한 
			ptSysSvc.setAttachExtMap(model, userVo.getCompId(), "ap");
			return LayoutUtil.getJspPath("/ap/box/setPaperDocPop");
		}
	}
	
	/** [프레임] 종이문서 등록/수정 */
	@RequestMapping(value = "/ap/box/transPaperDoc")
	public String transPaperDoc(HttpServletRequest request,
			@Parameter(name="bxId", required=false) String bxId,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		try {
			
			UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "ap");
			uploadHandler.upload();//업로드 파일 정보
			
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			Map<String, List<String>> paramListMap = uploadHandler.getParamListMap();//중복된 파라미터의 경우
			Map<String, List<File>> fileListMap = uploadHandler.getFileListMap();//파일 리스트 Map
			
			String apvNo = paramMap.get("apvNo");// 결재번호
			String docNo = paramMap.get("docNo");// 문서번호
			String orgnApvNo = paramMap.get("orgnApvNo");// 원본 결재번호
			boolean isNewDoc = false;
			
			// 진행문서기본(AP_ONGD_B) 테이블
			ApOngdBVo apOngdBVo = null, storedApOngdBVo = null;
			
			// 저장소
			String storage = null;
			
			// 결재번호 - KEY 가 있으면 저장된 데이터 조회
			if(apvNo!=null && !apvNo.isEmpty()){
				
				// 저장소 조회
				storage = apCmSvc.queryStorage(apvNo);
				
				storedApOngdBVo = new ApOngdBVo();
				storedApOngdBVo.setApvNo(apvNo);
				storedApOngdBVo.setStorage(storage);
				storedApOngdBVo = (ApOngdBVo)commonSvc.queryVo(storedApOngdBVo);
			}
			
			if(apvNo==null || apvNo.isEmpty() || storedApOngdBVo==null){
				Long no = apCmSvc.createNo("AP_ONGD_B");
				isNewDoc = true;
				apvNo = no.toString();
			}
			
			// 문서번호 중복체크
			if(docNo!=null){
				if(storedApOngdBVo==null || !docNo.equals(storedApOngdBVo.getDocNo())){
					
					// 디폴트 저장소 - 문서번호 중복체크
					ApOngdBVo docNoApOngdBVo = new ApOngdBVo();
					docNoApOngdBVo.setDocNo(docNo);
					if(commonSvc.count(docNoApOngdBVo) > 0){
						//ap.msg.dubDocNo=중복된 문서 번호 입니다.
						throw new CmException("ap.msg.dubDocNo", locale);
					}
					
					// 설정된 저장소 - 문서번호 중복체크
					if(storage != null){
						docNoApOngdBVo = new ApOngdBVo();
						docNoApOngdBVo.setDocNo(docNo);
						docNoApOngdBVo.setStorage(storage);
						if(commonSvc.count(docNoApOngdBVo) > 0){
							//ap.msg.dubDocNo=중복된 문서 번호 입니다.
							throw new CmException("ap.msg.dubDocNo", locale);
						}
					}
				}
			}
			
			// 원본결재번호 가 있으면 - 분리등록의 경우 - 분리등록으로 신규 등록 할 경우
			if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
				
				if(isNewDoc){
					
					// 진행문서기본(AP_ONGD_B) 테이블 - 원본 데이터 조회
					ApOngdBVo orgnApOngdBVo = new ApOngdBVo();
					orgnApOngdBVo.setApvNo(orgnApvNo);
					orgnApOngdBVo.setStorage(storage);
					orgnApOngdBVo = (ApOngdBVo)commonSvc.queryVo(orgnApOngdBVo);
					
					ApOngdExtnDocDVo orgnApOngdExtnDocDVo = new ApOngdExtnDocDVo();
					orgnApOngdExtnDocDVo.setApvNo(orgnApvNo);
					orgnApOngdExtnDocDVo.setStorage(storage);
					orgnApOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(orgnApOngdExtnDocDVo);
					
					if(orgnApOngdBVo==null || orgnApOngdExtnDocDVo==null){
						//ap.msg.notFoundSpDoc=분리등록 원본 문서를 찾을 수 없습니다.
						throw new CmException("ap.msg.notFoundSpDoc", locale);
					}
					// 다음 일련번호
					String regSeq = orgnApOngdExtnDocDVo.getRegSeq();
					String nextRegSeq = apCmSvc.addNo(regSeq, 1);
					
					// 결재번호, 문서 제목 설정 후 저장 - 진행문서기본(AP_ONGD_B) 테이블
					orgnApOngdBVo.setApvNo(apvNo);// 결재번호
					orgnApOngdBVo.setStorage(storage);
					orgnApOngdBVo.setDocSubj(orgnApOngdBVo.getDocSubj()+"-"+nextRegSeq);//문서 제목
					orgnApOngdBVo.setDocNo(orgnApOngdBVo.getDocNo()+"-"+nextRegSeq);
					queryQueue.insert(orgnApOngdBVo);
					// 첨부파일 존재여부 변경용
					apOngdBVo = orgnApOngdBVo;
					
					// 결재번호 세팅, 원본결재본호 세팅, 일련번호 삭제 후 저장 - 진행문서외부문서상세(AP_ONGD_EXTN_DOC_D) 테이블
					orgnApOngdExtnDocDVo.setApvNo(apvNo);// 결재번호
					orgnApOngdExtnDocDVo.setStorage(storage);
					orgnApOngdExtnDocDVo.setOrgnApvNo(orgnApvNo);// 원본결재번호
					orgnApOngdExtnDocDVo.setRegSeq(null);
					orgnApOngdExtnDocDVo.setPageCnt(paramMap.get("pageCnt"));//페이지수
					orgnApOngdExtnDocDVo.setExtnDocContTypCd(paramMap.get("extnDocContTypCd"));//외부문서컨텐츠구분코드 - doc:문서, photo:사진, blueprint:도면
					queryQueue.insert(orgnApOngdExtnDocDVo);
					
					// 원본문서 - 일련번호 올리기
					orgnApOngdExtnDocDVo = new ApOngdExtnDocDVo();
					orgnApOngdExtnDocDVo.setApvNo(orgnApvNo);
					orgnApOngdExtnDocDVo.setStorage(storage);
					orgnApOngdExtnDocDVo.setRegSeq(nextRegSeq);
					queryQueue.update(orgnApOngdExtnDocDVo);
					
				}
				
			// 분리등록이 아닐 경우
			} else {
				
				// 분리 등록된 부모 종이문서 여부 - 체크를 위한 조회
				ApOngdExtnDocDVo orgnChkApOngdExtnDocDVo = new ApOngdExtnDocDVo();
				orgnChkApOngdExtnDocDVo.setApvNo(apvNo);
				orgnChkApOngdExtnDocDVo.setStorage(storage);
				orgnChkApOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(orgnChkApOngdExtnDocDVo);
				
				// 분리 등록된 부모 종이문서 여부
				boolean spPaperParent = false;
				// 분리 등록된 자식 종이문서 여부
				boolean spPaperChild = false;
				// 분리 등록된 부모 문서 번호
				String parentApvNo = null;
				if(orgnChkApOngdExtnDocDVo!=null){
					if(orgnChkApOngdExtnDocDVo.getRegSeq()!=null && !orgnChkApOngdExtnDocDVo.getRegSeq().isEmpty()){
						spPaperParent = true;
					}
					parentApvNo = orgnChkApOngdExtnDocDVo.getOrgnApvNo();
					if(parentApvNo!=null && !parentApvNo.isEmpty()){
						spPaperChild = true;
					}
				}
				
				// 진행문서기본(AP_ONGD_B) 테이블
				apOngdBVo = new ApOngdBVo();
				VoUtil.fromMap(apOngdBVo, paramMap);
				apOngdBVo.setStorage(storage);
				
				if(!spPaperChild){
					if(apOngdBVo.getRecvDt()!=null && apOngdBVo.getRecvDt().length()==10){
						apOngdBVo.setRecvDt(apOngdBVo.getRecvDt()+" 00:00:00");
					}
					if(apOngdBVo.getEnfcDt()!=null && apOngdBVo.getEnfcDt().length()==10){
						apOngdBVo.setEnfcDt(apOngdBVo.getEnfcDt()+" 00:00:00");
					}
					
					if(apOngdBVo.getDocPw()!=null && !apOngdBVo.getDocPw().isEmpty()){
						apOngdBVo.setDocPwEnc(cryptoSvc.encryptPersanal(apOngdBVo.getDocPw()));
					} else if(paramMap.get("secuDocYn")==null){
						apOngdBVo.setDocPwEnc("");//비빌번호 삭제
					}
				}
				
				if(isNewDoc){
					// 결재번호
					apOngdBVo.setApvNo(apvNo);
					
					// 기본값 설정
					apOngdBVo.setDocLangTypCd(locale.getLanguage());// 문서언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문
					apOngdBVo.setDocStatCd("regRecLst".equals(bxId) ? "apvd" : "recv");// 문서상태코드 - 등록대장이면=apvd:승인, recv:접수
					apOngdBVo.setDocProsStatCd("apvd");// 문서승인상태코드 - apvd:승인
					apOngdBVo.setDocTypCd("paper");// 문서구분코드 - paper:종이문서
					
					// 양식결재용 NOT NULL 필드들 세팅 - 외부문서와 관련 없는 값들
					apOngdBVo.setCompId(userVo.getCompId());
					apOngdBVo.setAttFileYn("N");
					apOngdBVo.setFormSeq("0");
					apOngdBVo.setFormId("NONE");
					apOngdBVo.setFormApvLnTypCd("NONE");
					apOngdBVo.setApvLnTypCd("NONE");
					apOngdBVo.setBodyHstNo("1");
					apOngdBVo.setAttHstNo("1");
					apOngdBVo.setRecvDeptHstNo("1");
					apOngdBVo.setRefDocHstNo("1");
					apOngdBVo.setRegRecLstRegYn("Y");
					
					apOngdBVo.setRecLstDeptId(userVo.getDeptId());
					apOngdBVo.setRecLstTypCd(bxId);
					
					queryQueue.insert(apOngdBVo);
				} else {
					
					if(spPaperParent){
						queryQueue.update(apOngdBVo);
						
						// 분리 등록된 자식등 정보도 함께 수정해야함
						ApOngdBVo parentApOngdBVo = new ApOngdBVo();
						VoUtil.fromMap(parentApOngdBVo, paramMap);
						parentApOngdBVo.setStorage(storage);
						
						parentApOngdBVo.setRecvDt(apOngdBVo.getRecvDt());
						parentApOngdBVo.setEnfcDt(apOngdBVo.getEnfcDt());
						parentApOngdBVo.setDocPwEnc(apOngdBVo.getDocPwEnc());
						
						parentApOngdBVo.setApvNo(null);
						parentApOngdBVo.setPaperApvNo(apvNo);
						parentApOngdBVo.setDocNo(null);
						parentApOngdBVo.setAttFileYn(null);
						queryQueue.update(parentApOngdBVo);
					} else {
						queryQueue.update(apOngdBVo);
					}
				}
				
				// 진행문서외부문서상세(AP_ONGD_EXTN_DOC_D) 테이블
				ApOngdExtnDocDVo apOngdExtnDocDVo = new ApOngdExtnDocDVo();
				VoUtil.fromMap(apOngdExtnDocDVo, paramMap);
				apOngdExtnDocDVo.setStorage(storage);
				
				if(isNewDoc){
					apOngdExtnDocDVo.setApvNo(apvNo);
					queryQueue.insert(apOngdExtnDocDVo);
				} else {
					if(spPaperParent){
						queryQueue.update(apOngdExtnDocDVo);
						
						// 분리 등록된 자식등 정보도 함께 수정해야함
						apOngdExtnDocDVo = new ApOngdExtnDocDVo();
						VoUtil.fromMap(apOngdExtnDocDVo, paramMap);
						apOngdExtnDocDVo.setStorage(storage);
						
						apOngdExtnDocDVo.setApvNo(null);
						apOngdExtnDocDVo.setOrgnApvNo(apvNo);
						// 분리문서마다 달라지는 것들 제외
						apOngdExtnDocDVo.setPageCnt(null);
						apOngdExtnDocDVo.setExtnDocContTypCd(null);
						queryQueue.update(apOngdExtnDocDVo);
					} else {
						queryQueue.update(apOngdExtnDocDVo);
					}
				}
			}
			
			//////////////////
			// 첨부 파일
			
			Integer attSeqNo = 0;
			boolean hasFile = false;
			boolean newAdded = false;
			
			List<String> attNameList = paramListMap.get("docAttchFile");// 업로드된 File 명 목록
			List<File> attFileList = fileListMap.get("docAttchFile");// 업로드된 File 목록(물리적 파일)
			
			List<String> attSeqList = paramListMap.get("attSeq");//첨부파일 일련번호
			List<String> attUseYnList = paramListMap.get("attUseYn");//첨부파일 사용여부 - 삭제된것 파악용
			
			int i, p, fileKb;
			
			if(!isNewDoc){
				
				// 첨부 데이터 조회 - 이미 저장되어 있는 것
				ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setStorage(storage);
				apOngdAttFileLVo.setAttHstNo("1");
				@SuppressWarnings("unchecked")
				List<ApOngdAttFileLVo> storedApOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
				// 조회된 첨부 데이터를 맵으로 전환
				Map<Integer, ApOngdAttFileLVo> storedAttMap = new HashMap<Integer, ApOngdAttFileLVo>();
				if(storedApOngdAttFileLVoList != null){
					for(ApOngdAttFileLVo storedApOngdAttFileLVo : storedApOngdAttFileLVoList){
						storedAttMap.put(Integer.valueOf(storedApOngdAttFileLVo.getAttSeq()), storedApOngdAttFileLVo);
					}
				}
				
				int attSeqSize = Math.min(
						attUseYnList==null ? 0 : attUseYnList.size(),
						attSeqList==null ? 0: attSeqList.size() );
				
				// 마지막 파일 정보가 있도록함
				ApOngdAttFileLVo storedApOngdAttFileLVo = null;
				String attSeq;
				boolean deleted;
				for(i=0;i<attSeqSize; i++){
					
					deleted = false;
					attSeq = attSeqList.get(i);
					// 사용하는 데이터만 모집
					if("Y".equals(attUseYnList.get(i))){
						// 저장된 데이터 꺼내기
						storedApOngdAttFileLVo = storedAttMap.get(Integer.valueOf(attSeq));
						// 저장된 데이터가 있을 경우만
						if(storedApOngdAttFileLVo != null){
							hasFile = true;
						} else {
							deleted = true;
						}
					} else {
						deleted = true;
					}
					
					if(deleted){
						apOngdAttFileLVo = new ApOngdAttFileLVo();
						apOngdAttFileLVo.setApvNo(apvNo);
						apOngdAttFileLVo.setStorage(storage);
						apOngdAttFileLVo.setAttHstNo("1");
						apOngdAttFileLVo.setAttSeq(attSeq);
						queryQueue.delete(apOngdAttFileLVo);
					}
				}
				
				// 첨부 시퀀스 - 마지막 VO 첨부번호
				if(storedApOngdAttFileLVo!=null){
					attSeqNo = Integer.valueOf(storedApOngdAttFileLVo.getAttSeq());
				}
			}
			
			ApOngdAttFileLVo apOngdAttFileLVo;
			
			DistHandler distHandler = distManager.createHandler("ap", locale);
			
			// multi-part 로 전송된 파일 처리
			File file;
			String fileName, filePath;
			
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
					apOngdAttFileLVo.setStorage(storage);
					apOngdAttFileLVo.setAttHstNo("1");
					
					attSeqNo++;
					apOngdAttFileLVo.setAttSeq(attSeqNo.toString());
					
					fileName = emTmpFileTVo.getDispNm();
					apOngdAttFileLVo.setAttDispNm(fileName);
					
					apOngdAttFileLVo.setFileExt(emTmpFileTVo.getFileExt());
					fileKb = (int)(emTmpFileTVo.getFileSize()/1024L);
					if(fileKb==0) fileKb = 1;
					apOngdAttFileLVo.setFileKb(String.valueOf(fileKb));
					filePath = distHandler.addWasList(emTmpFileTVo.getSavePath());
					apOngdAttFileLVo.setFilePath(filePath);
					apOngdAttFileLVo.setModrUid(userVo.getUserUid());
					apOngdAttFileLVo.setModDt("sysdate");
					
					queryQueue.insert(apOngdAttFileLVo);
					newAdded = true;
					hasFile = true;
				}
			}
						
			int fileCount = attFileList==null ? 0 : attFileList.size();
			for(i=0;i<fileCount;i++){
				
				file = attFileList.get(i);
				apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setStorage(storage);
				apOngdAttFileLVo.setAttHstNo("1");
				
				attSeqNo++;
				apOngdAttFileLVo.setAttSeq(attSeqNo.toString());
				
				fileName = attNameList.get(i);
				apOngdAttFileLVo.setAttDispNm(fileName);
				
				p = fileName.lastIndexOf('.');
				if(p>0) apOngdAttFileLVo.setFileExt(fileName.substring(p+1));
				fileKb = (int)(file.length()/1024L);
				if(fileKb==0) fileKb = 1;
				apOngdAttFileLVo.setFileKb(String.valueOf(fileKb));
				filePath = distHandler.addWasList(file.getAbsolutePath());
				apOngdAttFileLVo.setFilePath(filePath);
				apOngdAttFileLVo.setModrUid(userVo.getUserUid());
				apOngdAttFileLVo.setModDt("sysdate");
				
				queryQueue.insert(apOngdAttFileLVo);
				newAdded = true;
				hasFile = true;
			}
			
			if(apOngdBVo!=null){
				apOngdBVo.setAttFileYn(hasFile ? "Y" : "N");
			} else {
				
				boolean storedHasFile = storedApOngdBVo != null
						&& "Y".equals(storedApOngdBVo.getAttFileYn());

				if(storedHasFile != hasFile){
					apOngdBVo = new ApOngdBVo();
					apOngdBVo.setApvNo(apvNo);
					apOngdBVo.setStorage(storage);
					apOngdBVo.setAttFileYn(hasFile ? "Y" : "N");
					queryQueue.update(apOngdBVo);
				}
			}
			
			if(newAdded){
				distHandler.distribute();
			}
			commonSvc.execute(queryQueue);
			uploadHandler.removeTempDir();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			model.put("todo", "parent.$(\"#searchForm1:visible, #searchForm2:visible\").submit();parent.dialog.close(\"setPaperDocDialog\");");
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+e.getStackTrace()[0].toString()
					+(message==null || message.isEmpty() ? "" : "\n"+message)
					+"\nstatCd - "+model.get("statCd")
					+"\n\n####### APV PAPER DOC - LOG S #######"
					+"\n"+queryQueue.getDebugString()
					+"####### APV PAPER DOC - LOG E #######\n");
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJX] 자식문서가 있는지 체크하고 메세지 리턴 */
	@RequestMapping(value = "/ap/box/checkPaperChildAjx")
	public String checkPaperChildAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		// 자식 문서가 있는지(분리등록한 문서가 있는지) 체크
		ApOngdExtnDocDVo apOngdExtnDocDVo = new ApOngdExtnDocDVo(), storedApOngdExtnDocDVo;
		String apvNos = (String)jsonObject.get("apvNos");
		boolean hasChild = false;
		String storage;
		
		if(apvNos != null){
			for(String apvNo : apvNos.split(",")){
				
				// 저장소 조회
				storage = apCmSvc.queryStorage(apvNo);
				
				apOngdExtnDocDVo.setApvNo(apvNo);
				apOngdExtnDocDVo.setStorage(storage);
				storedApOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(apOngdExtnDocDVo);
				if(storedApOngdExtnDocDVo != null
						&& storedApOngdExtnDocDVo.getRegSeq()!=null
						&& !storedApOngdExtnDocDVo.getRegSeq().isEmpty()){
					hasChild = true;
					break;
				}
			}
		}
		
		if(hasChild){
			// ap.msg.delWithSpSub=분리등록한 문서를 삭제하면 하위에 등록한 문서는 같이 삭제 됩니다.
			// cm.cfrm.del=삭제하시겠습니까 ?
			model.put("cfrm", 
					messageProperties.getMessage("ap.msg.delWithSpSub", locale) + "\n" +
					messageProperties.getMessage("cm.cfrm.del", locale));
		} else {
			// cm.cfrm.del=삭제하시겠습니까 ?
			model.put("cfrm", messageProperties.getMessage("cm.cfrm.del", locale));
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [AJX] 자식문서가 있는지 체크하고  */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ap/box/delPaperDocAjx")
	public String delPaperDocAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		QueryQueue queryQueue = new QueryQueue();
		
		ApOngdExtnDocDVo storedApOngdExtnDocDVo, apOngdExtnDocDVo;
		String apvNos = (String)jsonObject.get("apvNos");
		
		List<ApOngdExtnDocDVo> apOngdExtnDocDVoList;
		List<String> delApvNoList = new ArrayList<String>();
		Map<String, Integer> delCntMap = new HashMap<String, Integer>();
		Integer cnt;
		String orgnApvNo, storage;
		
		try {

			if(apvNos != null){
				for(String apvNo : apvNos.split(",")){
					
					storage = apCmSvc.queryStorage(apvNo);
					
					apOngdExtnDocDVo = new ApOngdExtnDocDVo();
					apOngdExtnDocDVo.setApvNo(apvNo);
					apOngdExtnDocDVo.setStorage(storage);
					storedApOngdExtnDocDVo = (ApOngdExtnDocDVo)commonSvc.queryVo(apOngdExtnDocDVo);
					if(storedApOngdExtnDocDVo != null){
						
						delPaperDoc(queryQueue, apvNo, delApvNoList);
						
						// 분리등록한 서브 문서가 있으면 - 분리등록 원본이면
						if(storedApOngdExtnDocDVo.getRegSeq()!=null
								&& !storedApOngdExtnDocDVo.getRegSeq().isEmpty()){
							
							// 서브 조회해서 삭제
							apOngdExtnDocDVo = new ApOngdExtnDocDVo();
							apOngdExtnDocDVo.setOrgnApvNo(storedApOngdExtnDocDVo.getOrgnApvNo());
							apOngdExtnDocDVoList = (List<ApOngdExtnDocDVo>)commonSvc.queryList(apOngdExtnDocDVo);
							if(apOngdExtnDocDVoList != null){
								for(ApOngdExtnDocDVo child : apOngdExtnDocDVoList){
									delPaperDoc(queryQueue, child.getApvNo(), delApvNoList);
								}
							}
						}
						
						// 분리등록한 서브 문서면 - 서브문서의 삭제 갯수 체크
						orgnApvNo = storedApOngdExtnDocDVo.getOrgnApvNo();
						if(orgnApvNo!=null && !orgnApvNo.isEmpty()){
							cnt = delCntMap.get(orgnApvNo);
							if(cnt==null){
								delCntMap.put(orgnApvNo, 1);
							} else {
								cnt++;
								delCntMap.put(orgnApvNo, cnt);
							}
						}
					}
				}
				
				if(!delCntMap.isEmpty()){
					Iterator<Entry<String, Integer>> iterator = delCntMap.entrySet().iterator();
					Entry<String, Integer> entry;
					while(iterator.hasNext()){
						entry = iterator.next();
						orgnApvNo = entry.getKey();
						cnt = entry.getValue();
						
						// 분리등록한 서브 갯수와 삭제된 갯수가 같으면
						apOngdExtnDocDVo = new ApOngdExtnDocDVo();
						apOngdExtnDocDVo.setOrgnApvNo(orgnApvNo);
						if(commonSvc.count(apOngdExtnDocDVo).intValue() == cnt.intValue()){
							apOngdExtnDocDVo = new ApOngdExtnDocDVo();
							apOngdExtnDocDVo.setApvNo(orgnApvNo);
							apOngdExtnDocDVo.setRegSeq("");
							queryQueue.update(apOngdExtnDocDVo);
						}
					}
				}
			}
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			
//		} catch(CmException e){
//			String message = e.getMessage()==null || e.getMessage().isEmpty() ? e.getClass().getCanonicalName() : e.getMessage();
//			model.put("message", message);
		} catch(Exception e){
			String message = e.getMessage()==null || e.getMessage().isEmpty() ? e.getClass().getCanonicalName() : e.getMessage();
			model.put("message", message);
			e.printStackTrace();
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 종이문서 삭제 */
	private void delPaperDoc(QueryQueue queryQueue, String apvNo, List<String> delApvNoList){
		if(!delApvNoList.contains(apvNo)){
			delApvNoList.add(apvNo);
			
			// 진행문서기본(AP_ONGD_B) 테이블 - 삭제
			ApOngdBVo apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			queryQueue.delete(apOngdBVo);
			
			// 진행문서외부문서상세(AP_ONGD_EXTN_DOC_D) 테이블 - 삭제
			ApOngdExtnDocDVo apOngdExtnDocDVo = new ApOngdExtnDocDVo();
			apOngdExtnDocDVo.setApvNo(apvNo);
			queryQueue.delete(apOngdExtnDocDVo);
			
			// 진행문서첨부파일내역(AP_ONGD_ATT_FILE_L) 테이블 - 삭제
			ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apvNo);
			queryQueue.delete(apOngdAttFileLVo);
			
		}
	}
	
	//delPaperDocAjx
}
