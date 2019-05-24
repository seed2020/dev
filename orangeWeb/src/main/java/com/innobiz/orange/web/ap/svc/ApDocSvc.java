package com.innobiz.orange.web.ap.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.xml.sax.SAXException;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.utils.ApDocTransUtil;
import com.innobiz.orange.web.ap.utils.ApDocUtil;
import com.innobiz.orange.web.ap.utils.ApvLines;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApApvLnGrpDVo;
import com.innobiz.orange.web.ap.vo.ApClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApErpIntgChitDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.ap.vo.ApOngdErpFormDVo;
import com.innobiz.orange.web.ap.vo.ApOngdExDVo;
import com.innobiz.orange.web.ap.vo.ApOngdHoldOpinDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPichDVo;
import com.innobiz.orange.web.ap.vo.ApOngdPubBxCnfmLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRecvDeptLVo;
import com.innobiz.orange.web.ap.vo.ApOngdRefVwDVo;
import com.innobiz.orange.web.ap.vo.ApOngdSendDVo;
import com.innobiz.orange.web.ap.vo.ApOngdTrxDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormCombDVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 결재 문서 서비스 */
@Service
public class ApDocSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;

	/** 결재 리소스 처리 서비스 */
	@Autowired
	private ApRescSvc apRescSvc;
	
	/** 결재 양식 서비스 */
	@Autowired
	private ApFormSvc apFormSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 참조기안 - 설정 */
	public void setRefDocMak(String refDocApvNo, Map<String, Object> apvData, ModelMap model) throws SQLException{
		
		String apvNo = refDocApvNo;
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		// 진행문서기본(AP_ONGD_B) 테이블 - 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setStorage(storage);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo!=null){
			
			apvData.put("docSubj", apOngdBVo.getDocSubj());// 문서제목
			
			//////////////////////////////////////////
			// 본문 조회
			ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
			apOngdBodyLVo.setApvNo(apOngdBVo.getApvNo());
			apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
			apOngdBodyLVo.setStorage(storage);
			apOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(apOngdBodyLVo);
			if(apOngdBodyLVo!=null && apOngdBodyLVo.getBodyHtml()!=null){
				apvData.put("bodyHtml", apOngdBodyLVo.getBodyHtml()); //본문HTML 세팅
			}
			
			//////////////////////////////////////////
			// 첨부 조회
			ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
			apOngdAttFileLVo.setApvNo(apOngdBVo.getApvNo());
			apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
			apOngdAttFileLVo.setStorage(storage);
			@SuppressWarnings("unchecked")
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonDao.queryList(apOngdAttFileLVo);
			if(apOngdAttFileLVoList != null && !apOngdAttFileLVoList.isEmpty()){
				model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
				
				apvData.put("apvNo", apvNo);
				apvData.put("attHstNo", apOngdBVo.getAttHstNo());
			}
		}
	}
	
	/** 진행중인 문서 조회 */
	public ApOngdBVo getOngoDoc(String apvNo, String apvLnPno, String apvLnNo, 
			String sendSeq, String bxId, String pubBxDeptId,
			boolean forDetlPop, String refdBy,
			UserVo userVo, Locale locale, ModelMap model) throws SQLException, IOException, CmException{
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		if(storage != null) model.put("storage", storage);
		
		//String langTypCd = locale.getLanguage();
		
		String sessionLangTypCd = locale.getLanguage();
		String docLangTypCd = sessionLangTypCd;
		
		// 진행문서기본(AP_ONGD_B) 테이블 - 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setStorage(storage);
		apOngdBVo.setQueryLang(sessionLangTypCd);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		
		if(apOngdBVo==null){
			return null;
		}
		
		// 언어셋이 세션과 다르면 다시 쿼리함
		if(apOngdBVo.getDocLangTypCd() != null && !apOngdBVo.getDocLangTypCd().isEmpty()
				&& !apOngdBVo.getDocLangTypCd().equals(sessionLangTypCd)){
			docLangTypCd = apOngdBVo.getDocLangTypCd();
			apOngdBVo = new ApOngdBVo();
			apOngdBVo.setApvNo(apvNo);
			apOngdBVo.setStorage(storage);
			apOngdBVo.setQueryLang(docLangTypCd);
			apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		}
		
		// 모바일에서 이력보기 여부
		boolean isHis = ServerConfig.IS_MOBILE && "Y".equals(model.get("isHis"));
		
		// 결재 비밀번호 복호화
		if(!ApDocUtil.isCmplOfDocProsStat(apOngdBVo.getDocProsStatCd())
				&& apOngdBVo.getDocPwEnc() != null && !apOngdBVo.getDocPwEnc().isEmpty()){
			apOngdBVo.setDocPw(cryptoSvc.decryptPersanal(apOngdBVo.getDocPwEnc()));
		}
		
		// 분류정보
		if(apOngdBVo.getClsInfoId() != null && !apOngdBVo.getClsInfoId().isEmpty()){
			ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
			apClsInfoDVo.setOrgId(apOngdBVo.getMakDeptId());
			apClsInfoDVo.setClsInfoId(apOngdBVo.getClsInfoId());
			apClsInfoDVo.setQueryLang(docLangTypCd);
			apClsInfoDVo = (ApClsInfoDVo)commonDao.queryVo(apClsInfoDVo);
			if(apClsInfoDVo!=null){
				apOngdBVo.setClsInfoNm(apClsInfoDVo.getRescNm());
			}
		}
		
		// 변환결재번호 - 시행변환된 정보
		String trxApvNo = apOngdBVo.getTrxApvNo();
		String vwMode = null;
		
		if(trxApvNo!=null && !trxApvNo.isEmpty()){
			
			vwMode = (String)model.get("vwMode");
			if("recvBx".equals(bxId) || "distBx".equals(bxId)){
				vwMode = "trx";
			} else if(vwMode==null && (
					"censrBx".equals(bxId) || "toSendBx".equals(bxId)
					|| ("myBx".equals(bxId) && "befoSend".equals(apOngdBVo.getEnfcStatCd()))// 기안함에서 - befoSend:발송대기
					)){
				vwMode = "trx";
			}
			
			// 변환문 보기 일때
			if("trx".equals(vwMode)){
				// 진행문서변환상세 - 조회
				ApOngdTrxDVo apOngdTrxDVo = new ApOngdTrxDVo();
				apOngdTrxDVo.setTrxApvNo(trxApvNo);
				apOngdTrxDVo.setStorage(storage);
				apOngdTrxDVo.setQueryLang(docLangTypCd);
				apOngdTrxDVo = (ApOngdTrxDVo)commonDao.queryVo(apOngdTrxDVo);
				if(apOngdTrxDVo!=null){
					// 양식 정보 복사
					apOngdBVo.fromMap(apOngdTrxDVo.toMap());
					model.put("vwMode", "trx");
				} else {
					trxApvNo = null;
				}
			} else {
				trxApvNo = null;
			}
		} else {
			trxApvNo = null;// empty 인 경우
		}
		
		Map<String, Object> apvData = VoUtil.toMap(apOngdBVo, null);
		model.put("apvData", apvData);
		
		// 임시저장함 일 경우 - 첫번째 사용자로 고정 - 파라미터 안 넘김
		if("drftBx".equals(bxId)){
			apvLnPno = "0";
			apvLnNo = "1";
		}
		
		// 보류본문이력번호(보류 체크용)
		String holdBodyHstNo = null;
		// 보류첨부이력번호(보류 체크용)
		String holdAttHstNo = null;
		// 보류결재라인이력번호(보류 체크용)
		String holdApvLnHstNo = null;
		// 보류수신처이력번호(보류 체크용)
		String holdRecvDeptHstNo = null;
		// 보류참조문서이력번호(보류 체크용)
		String holdRefDocHstNo = null;
		
		//사용자UID
		String userUid = userVo.getUserUid();
		
		ApOngdApvLnDVo apOngdApvLnDVo;
		
		// 최상위 라인인지 여부
		boolean isRootLn = "0".equals(apvLnPno);
		
		// 결재라인 조회
		apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setStorage(storage);
		apOngdApvLnDVo.setQueryLang(docLangTypCd);
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
		
		// 결재선 정보 관리용 유틸
		ApvLines apvLines = new ApvLines(apOngdApvLnDVoList, apvLnPno);
		// 루트 라인
		List<ApOngdApvLnDVo> rootApOngdApvLnDVoList = apvLines.getRootLn();
		model.put("rootApOngdApvLnDVoList", rootApOngdApvLnDVoList);
		
		// 결재선 출력 방법이 웹화면과 모바일 화면이 달라서 처리함
		if(ServerConfig.IS_MOBILE){
			
			model.put("mobileApOngdApvLnDVoList", apvLines.getRootLn());
			
			// 모바일에서 이력보기 - 의 경우
			if(isHis){
				ApOngdApvLnDVo hisApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, null, locale);
				
				if(hisApOngdApvLnDVo!=null){
					
					// 본문 이력
					String bodyHstNo = hisApOngdApvLnDVo.getPrevBodyHstNo();
					if(bodyHstNo!=null && !bodyHstNo.isEmpty()){
						ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
						apOngdBodyLVo.setApvNo(apvNo);
						apOngdBodyLVo.setBodyHstNo(bodyHstNo);
						apOngdBodyLVo.setStorage(storage);
						apOngdBodyLVo.setQueryLang(docLangTypCd);
						apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
						if(apOngdBodyLVo != null) model.put("hisApOngdBodyLVo", apOngdBodyLVo);
					}
					
					// 첨부 이력
					String attHstNo = hisApOngdApvLnDVo.getPrevAttHstNo();
					if(attHstNo!=null && !attHstNo.isEmpty()){
						ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
						apOngdAttFileLVo.setApvNo(apvNo);
						apOngdAttFileLVo.setAttHstNo(attHstNo);
						apOngdAttFileLVo.setStorage(storage);
						apOngdAttFileLVo.setQueryLang(docLangTypCd);
						@SuppressWarnings("unchecked")
						List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
						if(apOngdAttFileLVoList != null){
							model.put("hisApOngdAttFileLVoList", apOngdAttFileLVoList);
						} else {
							model.put("hisApOngdAttFileLVoList", new ArrayList<ApOngdAttFileLVo>());
						}
					}
					
					String apvLnHstNo = hisApOngdApvLnDVo.getPrevApvLnHstNo();
					if(apvLnHstNo!=null && !apvLnHstNo.isEmpty()){
						
						apOngdApvLnDVo = new ApOngdApvLnDVo();
						apOngdApvLnDVo.setApvNo(apvNo);
						//if(apvLnPno==null || apvLnPno.isEmpty()) apvLnPno = "0";
						//apOngdApvLnDVo.setApvLnPno(apvLnPno);
						apOngdApvLnDVo.setApvLnHstNo(apvLnHstNo);
						apOngdApvLnDVo.setStorage(storage);
						apOngdApvLnDVo.setQueryLang(docLangTypCd);
						apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
						@SuppressWarnings("unchecked")
						List<ApOngdApvLnDVo> hisApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
						//if(apOngdApvLnDVoList != null) model.put("hisApOngdApvLnDVoList", hisApOngdApvLnDVoList);
						
						apvLines = new ApvLines(hisApOngdApvLnDVoList, apvLnPno);
						// 루트 라인
						model.put("hisApOngdApvLnDVoList", apvLines.getCurrLn());
						
						String apvrRoleCd;
						List<ApOngdApvLnDVo> subLine;
						for(ApOngdApvLnDVo storedApOngdApvLnDVo : apvLines.getCurrLn()){
							apvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
							if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)){
								subLine = apvLines.getSubLnByApvLnPno(storedApOngdApvLnDVo.getApvLnNo());
								if(subLine != null && !subLine.isEmpty()){
									model.put("sub"+storedApOngdApvLnDVo.getApvLnNo()+"ApOngdApvLnDVoList", subLine);
								}
							}
						}
					}
					return apOngdBVo;
				}
			}
			
			String apvrRoleCd;
			List<ApOngdApvLnDVo> subLine;
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : apvLines.getRootLn()){
				apvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
				if("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)){
					subLine = apvLines.getSubLnByApvLnPno(storedApOngdApvLnDVo.getApvLnNo());
					if(subLine != null && !subLine.isEmpty()){
						model.put("sub"+storedApOngdApvLnDVo.getApvLnNo()+"ApOngdApvLnDVoList", subLine);
					}
				}
			}
		}
		
		// 현재 결재라인 : apvLnPno:0 이면 루트라인, 아니면 서브라인
		//  - 부서대기함 에서는 apvLnPno 가 서브라인을 구성할 부모의 apvLnNo 가 넘어오기 때문에 부모라인을 현재 라인으로 써야함
		List<ApOngdApvLnDVo> currApOngdApvLnDVoList = apvLines.getCurrLn();
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(model, userVo.getCompId());
		// 상세정보 버튼에 - 의견있음 표시
		if("Y".equals(optConfigMap.get("showOpinAtBtn")) && currApOngdApvLnDVoList!=null){
			if("waitBx".equals(bxId) || "ongoBx".equals(bxId) || "apvdBx".equals(bxId)
					|| "myBx".equals(bxId) || "rejtBx".equals(bxId) || "postApvdBx".equals(bxId)
					|| "admOngoBx".equals(bxId) || "admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId)
					|| "deptBx".equals(bxId)){
				boolean hasOpin = false;
				
				if(isRootLn){// 루트면
					for(ApOngdApvLnDVo currApOngdApvLnDVo : currApOngdApvLnDVoList){
						if(!hasOpin){
							// 의견이 있거나
							if(currApOngdApvLnDVo.getApvOpinCont()!=null && !currApOngdApvLnDVo.getApvOpinCont().isEmpty()){
								if(ArrayUtil.isInArray(ApConstant.CMPL_APV_STAT_CDS, currApOngdApvLnDVo.getApvStatCd())){
									hasOpin = true;
									break;
								}
							// 부서합의의 의견이 있을 경우
							} else if("Y".equals(currApOngdApvLnDVo.getSubOpinYn())){
								hasOpin = true;
								break;
							}
						}
					}
					
				} else {// 루트가 아니면
					// 자기 결재라인(부서합의)에 의견이 있거나
					for(ApOngdApvLnDVo currApOngdApvLnDVo : currApOngdApvLnDVoList){
						if(!hasOpin && currApOngdApvLnDVo.getApvOpinCont()!=null && !currApOngdApvLnDVo.getApvOpinCont().isEmpty()){
							if(ArrayUtil.isInArray(ApConstant.CMPL_APV_STAT_CDS, currApOngdApvLnDVo.getApvStatCd())){
								hasOpin = true;
								break;
							}
						}
					}
					if(!hasOpin){
						// 루트에 의견이 있는 경우
						for(ApOngdApvLnDVo rootApOngdApvLnDVo : rootApOngdApvLnDVoList){
							if(!hasOpin && rootApOngdApvLnDVo.getApvOpinCont()!=null && !rootApOngdApvLnDVo.getApvOpinCont().isEmpty()){
								if(ArrayUtil.isInArray(ApConstant.CMPL_APV_STAT_CDS, rootApOngdApvLnDVo.getApvStatCd())){
									hasOpin = true;
									break;
								}
							}
						}
					}
				}
				
				if(hasOpin){
					model.put("showOpinAtBtn", Boolean.TRUE);
				}
			}
		}
		
		
		// 해당 사용자의 결재라인 - 결재할 차례일때
		ApOngdApvLnDVo myTurnApOngdApvLnDVo = null;
		
		// 완료된 문서면
		if(ApDocUtil.isCmplOfDocProsStat(apOngdBVo.getDocProsStatCd())){
			
			// 접수대장 이면
			if("recvRecLst".equals(bxId) && "recv".equals(apOngdBVo.getDocStatCd())){
				// 담당자 생성(접수문서의 기안자)
				ApOngdApvLnDVo makVwVo = ApDocTransUtil.createMakVwVo(userVo, apvNo);
				List<ApOngdApvLnDVo> currList = new ArrayList<ApOngdApvLnDVo>();
				currList.add(makVwVo);
				model.put("currApOngdApvLnDVoList", currList);
				model.put("myTurnApOngdApvLnDVo", makVwVo);
				
			// 접수대장이 아니면
			} else {
				
				// 결재라인 표시용(상세정보 팝업, 결제라인 설정용 히든 데이터, 본문의 의견)
				model.put("currApOngdApvLnDVoList", apvLines.getRootLn());
				
				if(apvLnNo!=null && apvLnPno!=null && refdBy==null){
					// 해당 사용자의 결재라인
					ApOngdApvLnDVo myApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, locale);
					// 해당 사용자의 결재라인 - 결재할 차례일때
					myTurnApOngdApvLnDVo = null;
					if(myApOngdApvLnDVo!=null){
						model.put("myApOngdApvLnDVo", myApOngdApvLnDVo);
						// 공람할 차례인지 체크 - inVw:공람중
						if("inVw".equals(myApOngdApvLnDVo.getApvStatCd())){
							myTurnApOngdApvLnDVo = myApOngdApvLnDVo;
							model.put("myTurnApOngdApvLnDVo", myTurnApOngdApvLnDVo);
						}
						
						if(myApOngdApvLnDVo.getVwDt()==null || myApOngdApvLnDVo.getVwDt().isEmpty()){
							if(!userVo.isAdminSesn()) {
								updateApvLnVwDt(myApOngdApvLnDVo);
							}
						}
						// 승인된 개인통보 문서 - 통보자가 열때 - 결재일시에 날짜 입력
						if("psnInfm".equals(myApOngdApvLnDVo.getApvrRoleCd())
								&& "apvd".equals(apOngdBVo.getDocStatCd())
								&& (
								myApOngdApvLnDVo.getApvDt()==null || myApOngdApvLnDVo.getApvDt().isEmpty())){
							updateApvLnApvDt(myApOngdApvLnDVo);
						}
					}
					
					// 해당 사용자의 결재라인 - 결재할 차례일때 - 대기함에서 보류일 경우 보류정보용 - 공람은 보류 없음
					myTurnApOngdApvLnDVo = null;
					
				} else if("rejtBx".equals(bxId)){
					
					// 반려함의 경우 - 기안자와 세션의 사용자가 같으면 
					List<ApOngdApvLnDVo> rootLn = apvLines.getRootLn();
					ApOngdApvLnDVo firstApOngdApvLnDVo = rootLn==null || rootLn.isEmpty() ? null : rootLn.get(0);
					if(firstApOngdApvLnDVo!=null && userVo.getUserUid().equals(firstApOngdApvLnDVo.getApvrUid())){
						// 읽음 처리
						if(firstApOngdApvLnDVo.getVwDt()==null || firstApOngdApvLnDVo.getVwDt().isEmpty()){
							if(!userVo.isAdminSesn()) {
								updateApvLnVwDt(firstApOngdApvLnDVo);
							}
						}
					}
				}
			}
			
			// 승인된 문서중 - 등록대장에 등제된 문서
			if( forDetlPop &&"apvd".equals(apOngdBVo.getDocProsStatCd())
					&& "regRecLst".equals(apOngdBVo.getRecLstTypCd())){
				
				// 심사의견 조회
				ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
				apOngdPichDVo.setApvNo(apvNo);
				apOngdPichDVo.setStorage(storage);
				apOngdPichDVo.setQueryLang(docLangTypCd);
				@SuppressWarnings("unchecked")
				List<ApOngdPichDVo> apOngdPichDVoList = (List<ApOngdPichDVo>)commonDao.queryList(apOngdPichDVo);
				if(apOngdPichDVoList!=null){
					String pichTypCd;
					for(ApOngdPichDVo storedApOngdPichDVo : apOngdPichDVoList){
						pichTypCd = storedApOngdPichDVo.getPichTypCd();
						if("reqCensr".equals(pichTypCd) || "censr".equals(pichTypCd)){//reqCensr:심사요청, censr:심사
							model.put(pichTypCd+"ApOngdPichDVo", storedApOngdPichDVo);
						}
					}
				}
			}
			// 공람게시 이면
			if(!forDetlPop && "pubBx".equals(bxId) && pubBxDeptId!=null && !pubBxDeptId.isEmpty() && storage==null){
				// 진행문서공람게시확인내역(AP_ONGD_PUB_BX_CNFM_L) 테이블
				ApOngdPubBxCnfmLVo apOngdPubBxCnfmLVo = new ApOngdPubBxCnfmLVo();
				apOngdPubBxCnfmLVo.setPubBxDeptId(pubBxDeptId);
				apOngdPubBxCnfmLVo.setApvNo(apvNo);
				apOngdPubBxCnfmLVo.setUserUid(userVo.getUserUid());
				// 공람내역이 없으면
				if(commonSvc.count(apOngdPubBxCnfmLVo)==0){
					
					boolean useSessionLangCd = "session".equals(optConfigMap.get("distDocLangTypCd"));
					String distDocLangTypCd = useSessionLangCd ? locale.getLanguage() : apOngdBVo.getDocLangTypCd();
					
					// 사용자 조회
					OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(userVo.getUserUid(), distDocLangTypCd, null);
					if(orUserBVo!=null){
						apOngdPubBxCnfmLVo.setUserNm(orUserBVo.getRescNm());
						apOngdPubBxCnfmLVo.setPositCd(orUserBVo.getPositCd());
						apOngdPubBxCnfmLVo.setPositNm(orUserBVo.getPositNm());
					}
					
					apOngdPubBxCnfmLVo.setDeptId(userVo.getDeptId());
					// 부서 조회
					OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(userVo.getDeptId(), distDocLangTypCd, null);
					if(orOrgBVo!=null){
						apOngdPubBxCnfmLVo.setDeptNm(orOrgBVo.getRescNm());
					}
					
					apOngdPubBxCnfmLVo.setVwDt("sysdate");
					// 공람게시확인내역 입력
					commonSvc.insert(apOngdPubBxCnfmLVo);
				}
			}
		// 완료된 문서가 아니면
		} else {
			
			// 결재라인 표시용(상세정보 팝업, 결제라인 설정용 히든 데이터, 본문의 의견)
			model.put("currApOngdApvLnDVoList", currApOngdApvLnDVoList);
			
			// 관리자 수정용
			if("admOngoBx".equals(bxId) && "U0000001".equals(userVo.getUserUid())){
				ApOngdApvLnDVo viewApOngdApvLnDVo = apvLines.findFirstActiveApvr("0");
				if(viewApOngdApvLnDVo != null){
					model.put("myApOngdApvLnDVo", viewApOngdApvLnDVo);
					model.put("myTurnApOngdApvLnDVo", viewApOngdApvLnDVo);
				}
			}
			
			// 결재라인정보가 DB에 있을때 - 결재라인에서 해당 사용자를 찾음
			if(currApOngdApvLnDVoList!=null && apvLnNo!=null && apvLnPno!=null) {
				
				// 해당 사용자의 결재라인
				ApOngdApvLnDVo myApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, locale);
				
				// 해당 사용자의 결재라인 - 결재할 차례일때
				myTurnApOngdApvLnDVo = null;
				if(myApOngdApvLnDVo!=null){
					model.put("myApOngdApvLnDVo", myApOngdApvLnDVo);
					// 결재중 결재자 상태 인지 확인
					if(ApDocUtil.isInApvOfApvStat(myApOngdApvLnDVo.getApvStatCd())){
						myTurnApOngdApvLnDVo = myApOngdApvLnDVo;
						model.put("myTurnApOngdApvLnDVo", myTurnApOngdApvLnDVo);
					}
				}
				
				// 해당 결재자가 - 읽지 않은 것으로 되어 있을때
				if(myApOngdApvLnDVo != null &&
						(myApOngdApvLnDVo.getVwDt()==null || myApOngdApvLnDVo.getVwDt().isEmpty())){
					if(!userVo.isAdminSesn()) {
						updateApvLnVwDt(myApOngdApvLnDVo);
					}
				}
				
				// 합의기안 여부 - 부서 대기함에서 문서를 열때
				//  - 세팅이 필요한 이유
				//    부서 대기함에서 경로 지정해서 상신을 할 때
				//    현재 라인은 부모 라인이지만, 해당 라인의 서브라인 경로를 지정해야 하므로 - 구별을 위해 필요
				boolean atMakAgr = false;
				if(myTurnApOngdApvLnDVo != null){
					String apvrRoleCd = myTurnApOngdApvLnDVo.getApvrRoleCd();
					atMakAgr = "deptBx".equals(bxId)
							&& ("deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd));
					// 합의기안 이면 - 부서 대기함에서 문서를 열때
					if(atMakAgr){
						model.put("atMakAgr", "Y");// 부서대기함에서의 문서보기 상태임
						model.put("deptAgrFirstApvr", "Y");// 재검토 때 -  [본문에 표시] 여부가 나와야 함
						
						// 부서합의 부모 라인
						String deptAgrApvLnPno = apvLnNo;
						
						// 부서합의 라인
						List<ApOngdApvLnDVo> deptAgrApvLn = apvLines.getSubLnByApvLnPno(deptAgrApvLnPno);
						
						// 상세정보 팝업이 아니면
						//   - 상세정보는 루트라인의 정보를 보여줘야 하므로 경로작업 하지않음 - 합의 경로가 있을때 팝업 뜨도록 설정
						//   - 본문에서는 결재선 정보를 재검토가 있을 경우 해당 재검토 라인을 보여 주야 하므로 아래 로직 필요
						if(!forDetlPop){
							// 부서합의 라인 설정 여부
							boolean wasSetDeptAgrLn = false;
							ApOngdApvLnDVo deptAgrMakr = deptAgrApvLn==null || deptAgrApvLn.isEmpty() ? null : deptAgrApvLn.get(0);
							// 합의기안자 - 가 있으면
							if(deptAgrMakr != null){
								// [부서대기함]에서 - [기안자지정] 버튼 - 디폴트 선택 사용자
								model.put("deptAgrMakr", deptAgrMakr);
								
								// 사용자 세션과 [합의기안자]가 같으면 - 결재라인 세팅함
								if(userVo.getUserUid().equals(deptAgrMakr.getApvrUid())){
									// [부서대기함]에서 - [경로지정] 버튼 - 세팅된 결재 경로
									model.put("currApOngdApvLnDVoList", deptAgrApvLn);
									wasSetDeptAgrLn = true;
									
								// 사용자 세션과 [합의기안자]가 다르면
								} else {
									
									// [옵션-히든] keepAgrLnWhenChgMakr=합의부서 기안자 변경시 합의부서 결재경로 유지 - 디폴트(N)
									
									// 기안자 변경시 합의부서 사용자 유지면
									if("Y".equals(optConfigMap.get("keepAgrLnWhenChgMakr"))){
										// 합의기안자 생성
										ApOngdApvLnDVo agrMakr = ApDocTransUtil.createMakAgrVo(userVo, deptAgrApvLnPno);
										// 라인에 합의기안자 바꾸기
										deptAgrApvLn.remove(0);
										deptAgrApvLn.add(0, agrMakr);
										model.put("currApOngdApvLnDVoList", deptAgrApvLn);
										wasSetDeptAgrLn = true;
									}
								}
							}
							if(!wasSetDeptAgrLn){
								// 자신을 기안자로 하는 라인 생성 - 기안자만 있는
								ApOngdApvLnDVo agrMakr = ApDocTransUtil.createMakAgrVo(userVo, deptAgrApvLnPno);
								deptAgrApvLn = new ArrayList<ApOngdApvLnDVo>();
								deptAgrApvLn.add(agrMakr);
								model.put("currApOngdApvLnDVoList", deptAgrApvLn);
							}
							
						// 상세정보 팝업에서
						} else {
							// 하위라인 정보가 있으면 - 팝업 뜨도록 설정
							if(deptAgrApvLn!=null){
								myTurnApOngdApvLnDVo.setPichApntYn("Y");//부서명에 팝업링크 생성
							}
						}
						
					}
				}
				
				// 보류의견 조회
				if(myTurnApOngdApvLnDVo != null && "waitBx".equals(bxId)
						&& myTurnApOngdApvLnDVo.getApvrUid() != null && !myTurnApOngdApvLnDVo.getApvrUid().isEmpty()){
					// 진행문서보류의견상세 - 조회
					ApOngdHoldOpinDVo apOngdHoldOpinDVo = new ApOngdHoldOpinDVo();
					apOngdHoldOpinDVo.setApvNo(myTurnApOngdApvLnDVo.getApvNo());
					apOngdHoldOpinDVo.setApvrUid(myTurnApOngdApvLnDVo.getApvrUid());
					apOngdHoldOpinDVo = (ApOngdHoldOpinDVo)commonDao.queryVo(apOngdHoldOpinDVo);
					if(apOngdHoldOpinDVo != null){
						//model.put("apOngdHoldOpinDVo", apOngdHoldOpinDVo);
						// 결재의견내용
						myTurnApOngdApvLnDVo.setApvOpinCont(apOngdHoldOpinDVo.getApvOpinCont());
						// 결재의견표시여부
						myTurnApOngdApvLnDVo.setApvOpinDispYn(apOngdHoldOpinDVo.getApvOpinDispYn());
					}
				}
				
				// 의견 본문에 표시 제어
				// - 의견을 본문에 표시하도록 양식을 만들었어도, 합의부서의 결재에서는
				//   - 최종결재자가 승인/반대 할 때와
				//   - 최초결재자가 재검토 할 때만 [본문에 표시] 여부가 나와야 함
				// >> 서브라인의 최초 결재자 인지, 마지막 결재자인지 판단
				if(!isRootLn && myTurnApOngdApvLnDVo != null){// 루트 라인이 아닌 경우, 내 차례에 온 문서에 대해서
					// 첫번째 차례면 : deptAgrFirstApvr - 부서합의 첫번째 결재자 세팅
					if("1".equals(myTurnApOngdApvLnDVo.getApvLnNo())){
						model.put("deptAgrFirstApvr", "Y");
					}
					// 다음 결재자가 없으면 - 마지막 결재자임
					if(ApDocTransUtil.getNextApvrList(currApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnNo(), true)==null){
						model.put("deptAgrLastApvr", "Y");
					};
				}
				
				// [재검토] 버튼 보이게 할 지 여부 체크
				
				// 서브라인 1번 - 여부
				boolean isSubLineFirst = myTurnApOngdApvLnDVo!=null && "1".equals(myTurnApOngdApvLnDVo.getApvLnNo());
				
				// 재검토 체크용 내차례 : 자신이 병렬이면 재검토 제외
				//  - 서브라인 1번이면 - 해당 부모 라인
				//  - 그외 - 해당 파라미터의 결재라인
				ApOngdApvLnDVo myTurnForReReviewCheckVo = 
						myTurnApOngdApvLnDVo==null ? null :
							!isRootLn && isSubLineFirst ? ApDocTransUtil.findApvLnByApvLnNo(rootApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnPno()) :
								myTurnApOngdApvLnDVo;
				
				//  [재검토.1] 해당 사용자가 병렬합의가 아니면
				if(myTurnForReReviewCheckVo != null
						&& !ApDocUtil.isParalAgrOfApvrRole(myTurnForReReviewCheckVo.getApvrRoleCd())){
					
//					// 이전 결재자가 상위 결재라인에 있는지 여부
//					boolean prevAtRootLine = false;
					
					// 이전 결재자 정보 - 찾기
					ApOngdApvLnDVo prevApOngdApvLnDVo = null;
					if(isRootLn){
						prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(rootApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnNo());
					} else {
						prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(currApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnNo());
						if(prevApOngdApvLnDVo == null && !isRootLn){
							prevApOngdApvLnDVo = ApDocTransUtil.getPrevApOngdApvLnDVo(rootApOngdApvLnDVoList, myTurnApOngdApvLnDVo.getApvLnPno());
//							prevAtRootLine = true;
						}
					}
					
					// [재검토.2] 이전 결재자가 부서가 아니고, 병렬이 아니면
					// deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, psnParalAgr:개인병렬합의
					String prevApvrRoleCd = prevApOngdApvLnDVo==null ? null : prevApOngdApvLnDVo.getApvrRoleCd();
					if(prevApvrRoleCd!=null && !prevApvrRoleCd.isEmpty()
							&& !"deptOrdrdAgr".equals(prevApvrRoleCd)
							&& !"deptParalAgr".equals(prevApvrRoleCd)
							&& !"psnParalAgr".equals(prevApvrRoleCd)){
						
						String myApvrRoleCd = myTurnForReReviewCheckVo.getApvrRoleCd();
						
						// [옵션] reRevwToPrevAgr=이전 합의자로 재검토 사용
						boolean reRevwToPrevAgrOpt = "Y".equals(optConfigMap.get("reRevwToPrevAgr"));
						boolean reRevwToPrevAgr = ApDocUtil.isAgrOfApvrRole(prevApvrRoleCd);
						
						// [옵션] reRevwAtMyAgr=현재 합의자 재검토 사용
						boolean reRevwAtMyAgrOpt = "Y".equals(optConfigMap.get("reRevwAtMyAgr"));
						boolean reRevwAtMyAgr = ApDocUtil.isAgrOfApvrRole(myApvrRoleCd);
						
						// 이전이 합의가 아니거나, 이전 합의자에게 재검토 보낼 수 있고(옵션)
						// 내가 합의가 아니거나, 내가 합의여도 재검토 보낼 수 있고(옵션)
						if( (!reRevwToPrevAgr || reRevwToPrevAgrOpt)
								&& (!reRevwAtMyAgr || reRevwAtMyAgrOpt)){
							// 재검토 가능하도록 설정
							model.put("rejtPrevEnab", "Y");
						}
					}
					
					// 이중결재 - 첫번째 사용자면 - [담당자지정] 버튼 보이게, 경로지정 해야 넘어가도록 체크함
					if(("apvLnDbl".equals(apOngdBVo.getFormApvLnTypCd()) || "apvLnDblList".equals(apOngdBVo.getFormApvLnTypCd()))
								&& prevApOngdApvLnDVo!=null){
						// 첫번째 사용자면 - 현재:처리부서, 이전:신청부서 - 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서
						if("prcDept".equals(myTurnApOngdApvLnDVo.getDblApvTypCd())
								&& "reqDept".equals(prevApOngdApvLnDVo.getDblApvTypCd())){
							
							// 이중결재 첫째 결재자임 설정
							model.put("prcDeptFirstApvr", "Y");
							
							// 대기함 일때
							if("waitBx".equals(bxId)){
								
								// [옵션] 최종결재 필수 여부
								boolean needLastApvr = "Y".equals(optConfigMap.get("needLastApvr"));
								// 처리부서 결재자 수
								int prcApvrCnt = 0;
								// 처리부서 지정 여부
								//boolean hasPrcApvLn = false;
								
								if(rootApOngdApvLnDVoList != null){
									for(ApOngdApvLnDVo rootApOngdApvLnDVo : rootApOngdApvLnDVoList){
										if(needLastApvr){
											// 최종결재 필수 일때 -결재 전결 이 있으면 - apv:결재, pred:전결
											if("apv".equals(rootApOngdApvLnDVo.getApvrRoleCd())
													|| "pred".equals(rootApOngdApvLnDVo.getApvrRoleCd())){
												prcApvrCnt = 2;
												break;
											}
										} else {
											// 최종결재 필수 가 아닐때 - 처리부서의 사용자가 지정 되면
											if("prcDept".equals(rootApOngdApvLnDVo.getDblApvTypCd())// 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서
													&& !"Y".equals(rootApOngdApvLnDVo.getApvrDeptYn())){
												prcApvrCnt++;
												if(prcApvrCnt>1){
													break;
												}
											}
										}
									}
								}
								
								if(prcApvrCnt>1){
									// 이중결재 처리부서 결재자를 세팅했음 표시 
									model.put("dblApvLnDone", "Y");
								}
								
							}
							
						}
					}
					
				}// [재검토] 버튼 보이게 할 지 여부 체크
				
				
				// 다음 결재자가 읽었는지 체크 - 본 결재 라인에서 체크함(보류한 결재라인 사용안함)
				if(myApOngdApvLnDVo != null // 해당 사용자 결재 라인에 있음
						&& ("myBx".equals(bxId) || "ongoBx".equals(bxId))// 기안함:myBx, 진행함:ongoBx
						&& ApDocUtil.isCmplOfApvStat(myApOngdApvLnDVo.getApvStatCd())// 해당 사용자가 결재/합의 했음
						&& "ongo".equals(apOngdBVo.getDocProsStatCd())){//결재가 진행중
					
					// 현재 결재라인의 다음 결재자 목록
					List<ApOngdApvLnDVo> nextApvrList = ApDocTransUtil.getNextApvrList(currApOngdApvLnDVoList, myApOngdApvLnDVo.getApvLnNo(), true);
					// 현재 라인에 다음 결재자가 없고 루트라인이 아니면
					if(nextApvrList == null && !isRootLn){
						// 루트라인에서 다음 결재자 구해옴
						nextApvrList = ApDocTransUtil.getNextApvrList(rootApOngdApvLnDVoList, myApOngdApvLnDVo.getApvLnPno(), true);
					}
					
					// [옵션] 승인자 열람 후 취소 가능
					String cnclAftRead = optConfigMap.get("cnclAftRead");
					if("Y".equals(cnclAftRead)){
						// 다음 결재자가 결재나 합의를 했으면 - 기안취소 승인취소 등 버튼 안보임
						boolean nextApvrDone = false;
						if(nextApvrList != null){
							ApOngdApvLnDVo subFirst;
							for(ApOngdApvLnDVo nextApvr : nextApvrList){
								
								// 다음 결재자가 결재 했는지 체크
								if(ApDocUtil.isCmplOfApvStat(nextApvr.getApvStatCd())){
									nextApvrDone = true;
									break;
								// 다음 결재자가 보류 했는지 체크
								} else if(ApDocUtil.isHoldStat(nextApvr.getApvStatCd())){
									nextApvrDone = true;
									break;
								} else if(ApDocUtil.isDeptAgrRole(nextApvr.getApvrRoleCd())){
									subFirst = apvLines.getSubLnFirst(apvLnPno);
									if(subFirst!=null){
										if(ApDocUtil.isCmplOfApvStat(subFirst.getApvStatCd())){
											nextApvrDone = true;
											break;
										} else if(ApDocUtil.isHoldStat(subFirst.getApvStatCd())){
											nextApvrDone = true;
											break;
										}	
									}
								}
//								// 다음 결재자 차례인지 - 현 결재자가 병렬이면 다음 결재자에게 가지 않음
//								if(!ApDocUtil.isAtWait(nextApvr.getApvrRoleCd(), nextApvr.getApvStatCd())){
//									nextApvrDone = true;
//									break;
//								}
							}
						}
						model.put("readByNextApvr", nextApvrList==null || nextApvrDone ? "Y" : "N");
					} else {
						boolean readByNextApvr = false;
						if(nextApvrList != null){
							for(ApOngdApvLnDVo nextApvr : nextApvrList){
								if((nextApvr.getVwDt()!=null && !nextApvr.getVwDt().isEmpty())
										|| "reRevw".equals(nextApvr.getApvStatCd())){
									readByNextApvr = true;
									break;
								}
							}
						}
						// 다음 결재자가 읽지 않았음 표시 - 다음 결재자가 없으면 완결되어서 읽음 여부 체크가 필요 없어야 하지만 - 데이터가 조작시 고려
						model.put("readByNextApvr", nextApvrList==null || readByNextApvr ? "Y" : "N");
					}
					
				}// 다음 결재자가 읽었는지 체크
				
				
				// 보류 정보 체크 & 보류 결재라인 조회
				if(myTurnApOngdApvLnDVo!=null){
					
					model.put("myTurnApOngdApvLnDVo", myTurnApOngdApvLnDVo);
					
					// 보류, 재검토 면 - 보류이력번호 변수에 담아둠
					if(("hold".equals(myTurnApOngdApvLnDVo.getApvStatCd())
							|| "reRevw".equals(myTurnApOngdApvLnDVo.getApvStatCd())
							|| "cncl".equals(myTurnApOngdApvLnDVo.getApvStatCd()) )
						&& ("myBx".equals(bxId) || "waitBx".equals(bxId))// 기안함:myBx, 대기함:waitBx
							){
						// 보류본문이력번호(보류 체크용)
						holdBodyHstNo = myTurnApOngdApvLnDVo.getHoldBodyHstNo();
						// 보류첨부이력번호(보류 체크용)
						holdAttHstNo = myTurnApOngdApvLnDVo.getHoldAttHstNo();
						// 보류결재라인이력번호(보류 체크용)
						holdApvLnHstNo = myTurnApOngdApvLnDVo.getHoldApvLnHstNo();
						// 보류수신처이력번호(보류 체크용)
						holdRecvDeptHstNo = myTurnApOngdApvLnDVo.getHoldRecvDeptHstNo();
						// 보류참조문서이력번호(보류 체크용)
						holdRefDocHstNo = myTurnApOngdApvLnDVo.getHoldRefDocHstNo();
					}
					
					// 결재라인이 보류로 저장된 경우
					if(holdApvLnHstNo!=null && !holdApvLnHstNo.isEmpty()){
						
						// 보류된 결재라인 다시 조회
						apOngdApvLnDVo = new ApOngdApvLnDVo();
						apOngdApvLnDVo.setApvNo(apvNo);
//						apOngdApvLnDVo.setApvLnPno(apvLnPno);
						apOngdApvLnDVo.setApvLnHstNo(holdApvLnHstNo);//결재라인이력번호
						apOngdApvLnDVo.setStorage(storage);
						apOngdApvLnDVo.setQueryLang(docLangTypCd);
						apOngdApvLnDVo.setHistory();//히스토리테이블 세팅
						apOngdApvLnDVo.setOrderBy("APV_LN_NO");
						@SuppressWarnings("unchecked")
						List<ApOngdApvLnDVo> holdApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
						
						ApvLines holdApvLines = new ApvLines(holdApOngdApvLnDVoList, apvLnPno);
						List<ApOngdApvLnDVo> holdCurrApOngdApvLnDVoList = holdApvLines.getCurrLn();
						
						model.put("currApOngdApvLnDVoList", holdCurrApOngdApvLnDVoList);
						if(ServerConfig.IS_MOBILE){
							model.put("mobileApOngdApvLnDVoList", holdCurrApOngdApvLnDVoList);
						}
						
						currApOngdApvLnDVoList = holdCurrApOngdApvLnDVoList;
						
						// 보류한 결재라인의 해당 사용자의 결재라인 - 결재할 차례일때
						ApOngdApvLnDVo myTurnHoldApOngdApvLnDVo = apvLines.findMyApvrLn(apvLnPno, apvLnNo, userVo, holdCurrApOngdApvLnDVoList, locale);
						
						// 현재 사용자 정보의 롤만 변경함 - 보류된 이력정보 유지(결재선,첨부,본문)
						if(myTurnHoldApOngdApvLnDVo!=null){
							myTurnApOngdApvLnDVo.setApvrRoleCd(myTurnHoldApOngdApvLnDVo.getApvrRoleCd());
						} else {
							LOGGER.warn("FAIL to find current apvr at history line ! : apvNo:"+apvNo
									+" apvLnPno:"+apvLnPno+" holdApvLnHstNo:"+holdApvLnHstNo+" userUid:"+userUid);
						}
						
						// 보류된 라인이 최상위 라인이면 - 도장방용 변경용 세팅
						if(isRootLn){
							rootApOngdApvLnDVoList = holdCurrApOngdApvLnDVoList;
						}
					}
					
				}// 보류 정보 체크 & 보류 결재라인 조회
				
			}// 결재라인정보가 DB에 있을때 - 결재라인에서 해당 사용자를 찾음
			
		}// 완료된 문서가 아니면
		
		// 도장방용 결재선
		List<ApOngdApvLnDVo> stampApOngdApvLnDVoList = null;
		// 접수대장, 배부대장 에 등제된 문서 - 도장방용 결재라인 다시 조회
		if("recvRecLst".equals(apOngdBVo.getRecLstTypCd()) || "distRecLst".equals(apOngdBVo.getRecLstTypCd())){
			apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setStorage(storage);
			apOngdApvLnDVo.setQueryLang(docLangTypCd);
			apOngdApvLnDVo.setExecution();
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> stampAllApOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);
			
			// 결재선 정보 관리용 유틸
			ApvLines stampApvLines = new ApvLines(stampAllApOngdApvLnDVoList, "0");
			// 도장방용 결재선
			stampApOngdApvLnDVoList = stampApvLines.getRootLn();
		} else {
			stampApOngdApvLnDVoList = rootApOngdApvLnDVoList;
		}
		model.put("stampApOngdApvLnDVoList", stampApOngdApvLnDVoList);
		
		// 도장방 정보
		if(!forDetlPop && stampApOngdApvLnDVoList!=null){// 상세정보를 위한 팝업은 - 도장방 정보 생략

			///////////////////////////////////////////////////
			//
			// 결재라인 도장방 정보 구성
			
			// 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재, apvLnDblList:리스트(이중결재)
			String formApvLnTypCd = apOngdBVo.getFormApvLnTypCd();
			
			// 연계타입코드
			String intgTypCd = (String)model.get("intgTypCd");
			// ERP 전표보기의 경우 - 전표보기임 세팅
			if(ApConstant.ERP_CHIT.equals(intgTypCd) && apOngdBVo!=null && ApConstant.ERP_CHIT.equals(apOngdBVo.getIntgTypCd())){
				
				if(apOngdBVo.getIntgNo()!=null && !apOngdBVo.getIntgNo().isEmpty()){
					
					// ERP연계전표상세(AP_ERP_INTG_CHIT_D) 테이블 - 조회하여 전표양식번호 가져옴
					ApErpIntgChitDVo apErpIntgChitDVo = new ApErpIntgChitDVo();
					apErpIntgChitDVo.setIntgNo(apOngdBVo.getIntgNo());
					apErpIntgChitDVo = (ApErpIntgChitDVo)commonSvc.queryVo(apErpIntgChitDVo);
					
					if(apErpIntgChitDVo!=null){
						model.put("apErpIntgChitDVo", apErpIntgChitDVo);
						
						String formId = apErpIntgChitDVo.getChitFormId();
						
						// 양식기본(AP_FORM_B) 테이블 - 조회하여 양식결재라인구분코드 가져옴
						ApFormBVo apFormBVo = new ApFormBVo();
						apFormBVo.setFormId(formId);
						apFormBVo = (ApFormBVo)commonSvc.queryVo(apFormBVo);
						if(apFormBVo != null){
							formApvLnTypCd = apFormBVo.getFormApvLnTypCd();
						}
					}
				}
			}
			
			// 도장방 영역 설정
			setStampBxArea(formApvLnTypCd, stampApOngdApvLnDVoList, model);
			
		}// 상세정보를 위한 팝업은 - 도장방 정보 생략
		
		// 본문 / 첨부
		if(!forDetlPop){// 상세정보를 위한 팝업은 - 본문 / 첨부 생략

			//////////////////////////////////////////
			// 본문 조회
			
			// 진행문서본문내역(AP_ONGD_BODY_L) 테이블 - 조회
			ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
			if(trxApvNo != null){// 변환된 문서면
				apOngdBodyLVo.setApvNo(trxApvNo);
				apOngdBodyLVo.setBodyHstNo("1");//첨부파일이력번호
			} else {
				apOngdBodyLVo.setApvNo(apOngdBVo.getApvNo());
				if(holdBodyHstNo==null || holdBodyHstNo.isEmpty()){// 보류본문이력번호 - 가 없으면
					apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
				} else {
					apOngdBodyLVo.setBodyHstNo(holdBodyHstNo);
				}
			}
			apOngdBodyLVo.setStorage(storage);
			apOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(apOngdBodyLVo);
			if(apOngdBodyLVo!=null && apOngdBodyLVo.getBodyHtml()!=null){
				apvData.put("bodyHtml", apOngdBodyLVo.getBodyHtml()); //본문HTML 세팅
			}
			
			//////////////////////////////////////////
			// 첨부 조회
			
			ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
			
			if(trxApvNo != null){// 변환된 문서면
				apOngdAttFileLVo.setApvNo(trxApvNo);
				apOngdAttFileLVo.setAttHstNo("1");//첨부파일이력번호
			} else {
				apOngdAttFileLVo.setApvNo(apOngdBVo.getApvNo());
				if(holdAttHstNo==null || holdAttHstNo.isEmpty()){// 보류본문이력번호 - 가 없으면
					apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
				} else {
					apOngdAttFileLVo.setAttHstNo(holdAttHstNo);//첨부파일이력번호
				}
			}
			apOngdAttFileLVo.setStorage(storage);
			@SuppressWarnings("unchecked")
			List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonDao.queryList(apOngdAttFileLVo);
			if(apOngdAttFileLVoList != null && !apOngdAttFileLVoList.isEmpty()){
				model.put("apOngdAttFileLVoList", apOngdAttFileLVoList);
			}
			
		}// 상세정보를 위한 팝업은 - 본문 / 첨부 생략
		
		//////////////////////////////////////////
		// 수신처 조회
		ApOngdRecvDeptLVo apOngdRecvDeptLVo = new ApOngdRecvDeptLVo();
		apOngdRecvDeptLVo.setApvNo(apvNo);
		if(holdRecvDeptHstNo==null || holdRecvDeptHstNo.isEmpty()){
			apOngdRecvDeptLVo.setRecvDeptHstNo(apOngdBVo.getRecvDeptHstNo());//수신처이력번호
		} else {
			apOngdRecvDeptLVo.setRecvDeptHstNo(holdRecvDeptHstNo);//보류수신처이력번호
		}
		apOngdRecvDeptLVo.setStorage(storage);
		apOngdRecvDeptLVo.setQueryLang(docLangTypCd);
		@SuppressWarnings("unchecked")
		List<ApOngdRecvDeptLVo> apOngdRecvDeptLVoList = (List<ApOngdRecvDeptLVo>)commonDao.queryList(apOngdRecvDeptLVo);
		if(apOngdRecvDeptLVoList != null && !apOngdRecvDeptLVoList.isEmpty()){
			model.put("apOngdRecvDeptLVoList", apOngdRecvDeptLVoList);
			
			// 상세정보 팝업이 아닐 경우
			if(!forDetlPop){
				// 처음발송갯수 - 추가발송이 아닌것 - 1개의 경우 [수신처참조]에 수신처 참조 적어야 함
				int orgnSendCnt = 0;
				for(ApOngdRecvDeptLVo storedApOngdRecvDeptLVo : apOngdRecvDeptLVoList){
					if("N".equals(storedApOngdRecvDeptLVo.getAddSendYn())){//추가발송여부
						orgnSendCnt++;
					}
				}
				model.put("orgnSendCnt", Integer.valueOf(orgnSendCnt));
			}
			
		}

		//////////////////////////////////////////
		// 참조 문서 조회
		
		ApOngdBVo refApOngdBVo = new ApOngdBVo();
		refApOngdBVo.setRefApvNo(apvNo);
		if(holdRefDocHstNo==null || holdRefDocHstNo.isEmpty()){
			refApOngdBVo.setRefDocHstNo(apOngdBVo.getRefDocHstNo());//참조문서이력번호
		} else {
			refApOngdBVo.setRefDocHstNo(holdRefDocHstNo);//보류참조문서이력번호
		}
		refApOngdBVo.setStorage(storage);
		refApOngdBVo.setOrderBy("D.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApOngdBVo> refApOngdBVoList = (List<ApOngdBVo>)commonDao.queryList(refApOngdBVo);
		if(refApOngdBVoList != null && !refApOngdBVoList.isEmpty()){
			model.put("refApOngdBVoList", refApOngdBVoList);
		}
		
		
		if(!forDetlPop){
			if(!userVo.isAdminSesn()) {
				if("toSendBx".equals(bxId) || "censrBx".equals(bxId)){
					// 읽은 날짜 갱신 - toSendBx:발송함, censrBx:심사함
					updatePichVwDt(apvNo, "toSendBx".equals(bxId) ? "send" : "censr");//담당자구분코드 - censr:심사, send:발송
				} else if("recvBx".equals(bxId) || "distBx".equals(bxId)){
					// 읽은 날짜 갱신 - recvBx:접수함, distBx:배부함
					if(sendSeq != null){
						updateSendVwDt(apvNo, sendSeq);
					}
				} else if("refVwBx".equals(bxId)){
					updateRefVwDt(apvNo, storage, userVo, model);
				}
			}
		}
		
		// 배부대장 상세정보 면 - 배부정보[탭] 조회
		if(forDetlPop && ("distRecLst".equals(bxId) || "admDistRecLst".equals(bxId))){
			ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
			apOngdSendDVo.setApvNo(apvNo);
			apOngdSendDVo.setStorage(storage);
			apOngdSendDVo.setQueryLang(docLangTypCd);
			@SuppressWarnings("unchecked")
			List<ApOngdSendDVo> apOngdSendDVoList = (List<ApOngdSendDVo>)commonDao.queryList(apOngdSendDVo);
			if(apOngdSendDVoList != null) model.put("apOngdSendDVoList", apOngdSendDVoList);
		}
		
		// 문서관리 보내기 조회
		ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
		apOngdExDVo.setApvNo(apvNo);
		apOngdExDVo.setStorage(storage);
		@SuppressWarnings("unchecked")
		List<ApOngdExDVo> apOngdExDVoList = (List<ApOngdExDVo>)commonDao.queryList(apOngdExDVo);
		if(apOngdExDVoList != null){
			for(ApOngdExDVo storedApOngdExDVo : apOngdExDVoList){
				if("erpLinkedApvNo".equals(storedApOngdExDVo.getExId())){
					String linkedStorage = apCmSvc.queryStorage(storedApOngdExDVo.getExVa());
					ApOngdBVo linkedApOngdBVo = new ApOngdBVo();
					linkedApOngdBVo.setApvNo(storedApOngdExDVo.getExVa());
					linkedApOngdBVo.setStorage(linkedStorage);
					linkedApOngdBVo = (ApOngdBVo)commonDao.queryVo(linkedApOngdBVo);
					if(linkedApOngdBVo != null){
						model.put("linkedApOngdBVo", linkedApOngdBVo);
					}
				} else {
					model.put(storedApOngdExDVo.getExId(), storedApOngdExDVo.getExVa());
				}
			}
		}
		
		// 참조열람 조회
		ApOngdRefVwDVo apOngdRefVwDVo = new ApOngdRefVwDVo();
		apOngdRefVwDVo.setApvNo(apvNo);
		boolean isDistDoc = "recvRecLst".equals(apOngdBVo.getRecLstTypCd()) || "distRecLst".equals(apOngdBVo.getRecLstTypCd());
		if(isDistDoc){ apOngdRefVwDVo.setExecution(); }//이행문서 세팅
		apOngdRefVwDVo.setStorage(storage);
		
		@SuppressWarnings("unchecked")
		List<ApOngdRefVwDVo> apOngdRefVwDVoList = (List<ApOngdRefVwDVo>)commonDao.queryList(apOngdRefVwDVo);
		if(apOngdRefVwDVoList != null){
			model.put("apOngdRefVwDVoList", apOngdRefVwDVoList);
		}
		
		return apOngdBVo;
	}
	
	/** 양식 참조열람 세팅 */
	public void setFixedRefVw(String refVwGrpId, String refVwFixdApvrYn, UserVo userVo, ModelMap model) throws SQLException {
		
		if(refVwGrpId==null || refVwGrpId.isEmpty()) return;
		
		String langTypCd = userVo.getLangTypCd();
		
		ApApvLnGrpDVo apApvLnGrpDVo = new ApApvLnGrpDVo();
		apApvLnGrpDVo.setUserUid(userVo.getCompId());
		apApvLnGrpDVo.setApvLnGrpId(refVwGrpId);
		apApvLnGrpDVo.setOrderBy("APV_LN_GRP_SEQ");
		@SuppressWarnings("unchecked")
		List<ApApvLnGrpDVo> apApvLnGrpDVoList = (List<ApApvLnGrpDVo>)commonDao.queryList(apApvLnGrpDVo);
		
		if(apApvLnGrpDVoList!=null){
			
			String apvrUid;
			ApOngdRefVwDVo apOngdRefVwDVo;
			List<ApOngdRefVwDVo> apOngdRefVwDVoList = new ArrayList<ApOngdRefVwDVo>();
			
			OrUserBVo orUserBVo;
			OrOrgBVo orOrgBVo;
			
			String Y = "Y";
			for(ApApvLnGrpDVo storedApApvLnGrpDVo : apApvLnGrpDVoList){
				
				apvrUid = storedApApvLnGrpDVo.getApvrUid();
				
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(apvrUid);
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo==null || !"02".equals(orUserBVo.getUserStatCd())){
					continue;
				}
				
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(orUserBVo.getDeptId());
				orOrgBVo.setQueryLang(langTypCd);
				orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
				if(orOrgBVo==null || !"Y".equals(orOrgBVo.getUseYn())){
					continue;
				}
				
				apOngdRefVwDVo = new ApOngdRefVwDVo();
				apOngdRefVwDVo.setRefVwrUid(apvrUid);
				apOngdRefVwDVo.setRefVwrNm(orUserBVo.getRescNm());
				apOngdRefVwDVo.setRefVwStatCd("befoRefVw");
				apOngdRefVwDVo.setRefVwrPositNm(orUserBVo.getPositNm());
				apOngdRefVwDVo.setRefVwrTitleNm(orUserBVo.getTitleNm());
				
				apOngdRefVwDVo.setRefVwrDeptId(orOrgBVo.getOrgId());
				apOngdRefVwDVo.setRefVwrDeptNm(orOrgBVo.getRescNm());
				
				if(Y.equals(refVwFixdApvrYn)){
					apOngdRefVwDVo.setRefVwFixdApvrYn(Y);
				}
				
				apOngdRefVwDVoList.add(apOngdRefVwDVo);
			}
			
			model.put("apOngdRefVwDVoList", apOngdRefVwDVoList);
		}
	}
	
	/** 참조기안 결재선 세팅 */
	public void setRefApvLn(String apvNo, String formApvLnTypCd, UserVo userVo, ModelMap model) throws SQLException{
		
		String langTypCd = userVo.getLangTypCd();
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setStorage(storage);
		apOngdBVo.setQueryLang(langTypCd);
		apOngdBVo = (ApOngdBVo)commonDao.queryVo(apOngdBVo);
		if(apOngdBVo==null) return; 
		
		
		String refFormApvLnTypCd = apOngdBVo.getFormApvLnTypCd();
		boolean isRefApvLnDbl = "apvLnDbl".equals(refFormApvLnTypCd) || "apvLnDblList".equals(refFormApvLnTypCd);
		boolean isNewApvLnDbl = "apvLnDbl".equals(formApvLnTypCd)    || "apvLnDblList".equals(formApvLnTypCd);
		
		if(isRefApvLnDbl != isNewApvLnDbl){
			// ap.msg.apvLnNotSameApvLnTyp=결재선 타입이 같지 않아서 결재선을 불러올 수 없습니다.
			model.put("message", messageProperties.getMessage("ap.msg.apvLnNotSameApvLnTyp", SessionUtil.toLocale(langTypCd)));
			return;
		}
		
		// 결재라인 조회
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setStorage(storage);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setQueryLang(langTypCd);
		apOngdApvLnDVo.setOrderBy("APV_LN_NO");
		@SuppressWarnings("unchecked")
		List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonDao.queryList(apOngdApvLnDVo);

		if(apOngdApvLnDVoList==null || apOngdApvLnDVoList.isEmpty()) return;
		
		OrOrgBVo orOrgBVo;
		OrUserBVo orUserBVo;
		Integer apvLnNo=1;
		String apvStatCd;
		List<ApOngdApvLnDVo> apOngdApvLnDVoList2 = new ArrayList<ApOngdApvLnDVo>();
		boolean isDept;
		
		for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
			
			isDept  = "Y".equals(storedApOngdApvLnDVo.getApvrDeptYn());
			
			if(!isDept){
				orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(storedApOngdApvLnDVo.getApvrUid());
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				if(orUserBVo!=null && "02".equals(orUserBVo.getUserStatCd())){
					storedApOngdApvLnDVo.setApvLnNo(apvLnNo.toString());
					apOngdApvLnDVoList2.add(storedApOngdApvLnDVo);
					apvLnNo++;
				} else {
					continue;
				}
			} else {
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(storedApOngdApvLnDVo.getApvDeptId());
				orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
				if(orOrgBVo!=null && "Y".equals(orOrgBVo.getUseYn())){
					storedApOngdApvLnDVo.setApvLnNo(apvLnNo.toString());
					apOngdApvLnDVoList2.add(storedApOngdApvLnDVo);
					apvLnNo++;
				} else {
					continue;
				}
			}
			
			// 이전 서명 값 지우기
			storedApOngdApvLnDVo.setSignDispVa(null);
			storedApOngdApvLnDVo.setDtDispVa(null);
			storedApOngdApvLnDVo.setSignImgPath(null);
			storedApOngdApvLnDVo.setVwDt(null);
			storedApOngdApvLnDVo.setApvDt(null);
			storedApOngdApvLnDVo.setApvOpinCont(null);
			storedApOngdApvLnDVo.setApvOpinDispYn(null);
			storedApOngdApvLnDVo.setPredYn(null);
			
			// 부서 서명값 (부서장 명, 직위 등) 지우기
			if(isDept){
				storedApOngdApvLnDVo.setApvrUid(null);
				storedApOngdApvLnDVo.setApvrNm(null);
				storedApOngdApvLnDVo.setApvrPositCd(null);
				storedApOngdApvLnDVo.setApvrPositNm(null);
				storedApOngdApvLnDVo.setApvrTitleCd(null);
				storedApOngdApvLnDVo.setApvrTitleNm(null);
			}
			
			
			apvStatCd = storedApOngdApvLnDVo.getApvStatCd();
			if(apvLnNo.intValue()==1 || apvStatCd == null){
				storedApOngdApvLnDVo.setApvStatCd("");
			} else if(apvStatCd.equals("cons") || apvStatCd.equals("pros") || apvStatCd.endsWith("Agr")){
				storedApOngdApvLnDVo.setApvStatCd("befoAgr");
			} else if(apvStatCd.equals("apvd") || apvStatCd.equals("rejt") || apvStatCd.endsWith("Apv")){
				storedApOngdApvLnDVo.setApvStatCd("befoApv");
			} else if(apvStatCd.endsWith("Vw")){
				storedApOngdApvLnDVo.setApvStatCd("befoVw");
			} else if(apvStatCd.equals("inInfm")){
				storedApOngdApvLnDVo.setApvStatCd("");
			} else if(apvStatCd.equals("hold") || apvStatCd.equals("cncl") || apvStatCd.equals("reRevw")){
				storedApOngdApvLnDVo.setApvStatCd("befoAgr");
			} else {
				storedApOngdApvLnDVo.setApvStatCd("");
			}
			
		}
		
		if(apOngdApvLnDVoList2.isEmpty()) return;
		apOngdApvLnDVoList = apOngdApvLnDVoList2;
		
		apOngdApvLnDVo = apOngdApvLnDVoList.get(0);
		if(!apOngdApvLnDVo.getApvrUid().equals(userVo.getUserUid())){
			// ap.msg.apvLnNotMakr=기안자가 아니어서 결재선을 불러올 수 없습니다.
			model.put("message", messageProperties.getMessage("ap.msg.apvLnNotMakr", SessionUtil.toLocale(langTypCd)));
			return;
		}
		
		// 결재선 정보 관리용 유틸
		ApvLines apvLines = new ApvLines(apOngdApvLnDVoList, "0");

		// 루트 라인
		List<ApOngdApvLnDVo> rootApOngdApvLnDVoList = apvLines.getRootLn();
		model.put("rootApOngdApvLnDVoList", rootApOngdApvLnDVoList);

		// 현재 결재라인
		List<ApOngdApvLnDVo> currApOngdApvLnDVoList = apvLines.getCurrLn();
		model.put("currApOngdApvLnDVoList", currApOngdApvLnDVoList);
		
		setStampBxArea(formApvLnTypCd, rootApOngdApvLnDVoList, model);
		model.put("refMakApvLn", Boolean.TRUE);
	}
	
	/** 도장방 영역 설정 */
	private void setStampBxArea(String formApvLnTypCd, List<ApOngdApvLnDVo> stampApOngdApvLnDVoList, ModelMap model){
		
		// 결재자역할코드
		String apvrRoleCd;
		
		// 도장방에 표시 안할 목록 - 모든 [양식결재라인구분코드:formApvLnTypCd] 에 적용 되는 것
		// entu:위임, postApvd:후열, psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람, prcDept:처리부서
		String[] skips = {"entu","postApvd","psnInfm","deptInfm","makVw","fstVw","pubVw","paralPubVw"};
		
		// 이중결재 일 경우
		if("apvLnDbl".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd)){

			// [req:신청 도장방] 에 표시할 목록 - 정리
			List<ApOngdApvLnDVo> reqApvrList = new ArrayList<ApOngdApvLnDVo>();
			String[] roleInReq = {"mak","revw","revw2","revw3","abs"};//mak:기안, revw:검토, abs:공석,
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
				// 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서  -- 처리부서가 아닌것 제외
				if(!"reqDept".equals(storedApOngdApvLnDVo.getDblApvTypCd())) continue;
				if(!ArrayUtil.isInArray(roleInReq, storedApOngdApvLnDVo.getApvrRoleCd())) continue;
				reqApvrList.add(storedApOngdApvLnDVo);
			}
			// [req:신청 도장방]
			model.put("reqApvrList", reqApvrList);
			
			
			// [prc:처리 도장방] 에 표시할 목록 - 정리
			List<ApOngdApvLnDVo> prcApvrList = new ArrayList<ApOngdApvLnDVo>();
			String[] roleInPrc = {"prcDept","revw","revw2","revw3","abs","apv","pred"};//prcDept:처리부서, revw:검토, abs:공석, apv:결재, pred:전결,
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
				// 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서  -- 처리부서가 아닌것 제외
				if(!"prcDept".equals(storedApOngdApvLnDVo.getDblApvTypCd())) continue;
				if(!ArrayUtil.isInArray(roleInPrc, storedApOngdApvLnDVo.getApvrRoleCd())) continue;
				prcApvrList.add(storedApOngdApvLnDVo);
			}
			// [prc:처리 도장방]
			model.put("prcApvrList", prcApvrList);
			
		// 서면결재 일 경우
		} else if("apvLnWrtn".equals(formApvLnTypCd)){
			
			List<ApOngdApvLnDVo> apvApvrList = new ArrayList<ApOngdApvLnDVo>();
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
				apvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
				// 표시 안할 목록 제거
				if(ArrayUtil.isInArray(skips, apvrRoleCd)) continue;
				// 프로그램 적으로 [prcDept:처리부서]가 세팅되지 않지만 - 데이터 이상으로 있어도 제거함
				apvApvrList.add(storedApOngdApvLnDVo);
			}
			// [req:신청 도장방]
			model.put("reqApvrList", apvApvrList);
			
			// 참고 [prc:처리 도장방]
			// ApFormSvc : 의 아래 주석 부분에서 구현됨
			// apvLnWrtn=신청+처리(서면결재) - 처리(서면)영역 - 빈칸 출력용 빈 데이터 넣기
			
		// 이중결재, 서면결재가 아닌경우
		} else {

			// [apv:결재 도장방] 에 표시할 목록 - 정리
			
			// 합의 목록 - psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
			String[] agrs = new String[]{"psnOrdrdAgr", "psnParalAgr", "deptOrdrdAgr", "deptParalAgr"};
			
			// 합의가 필요한지 여부 - [apvLnMixd:결재(결재합의혼합), apvLnMultiTopList:서명+리스트] 의 경우 외에는 합의 제거
			boolean needAgr = "apvLnMixd".equals(formApvLnTypCd) || "apvLnMultiTopList".equals(formApvLnTypCd);
			
			List<ApOngdApvLnDVo> apvApvrList = new ArrayList<ApOngdApvLnDVo>();
			for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
				apvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
				// 표시 안할 목록 제거
				if(ArrayUtil.isInArray(skips, apvrRoleCd)) continue;
				// 합의가 필요 없으면 제거
				if(!needAgr && ArrayUtil.isInArray(agrs, apvrRoleCd)) continue;
				// 프로그램 적으로 [prcDept:처리부서]가 세팅되지 않지만 - 데이터 이상으로 있어도 제거함
				if("prcDept".equals(apvrRoleCd)) continue;
				apvApvrList.add(storedApOngdApvLnDVo);
			}
			// [apv:결재 도장방]
			model.put("apvApvrList", apvApvrList);
			
			// [agr:합의 도장방] 에 표시할 목록 - 정리
			// [합의] 도장방이 따로 구성되어 있는 곳 : apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄)
			if("apvLn1LnAgr".equals(formApvLnTypCd) || "apvLn2LnAgr".equals(formApvLnTypCd)){
				
				List<ApOngdApvLnDVo> agrApvrList = new ArrayList<ApOngdApvLnDVo>();
				for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
					if(!ArrayUtil.isInArray(agrs, storedApOngdApvLnDVo.getApvrRoleCd())) continue;
					agrApvrList.add(storedApOngdApvLnDVo);
				}
				// [apv:결재 도장방]
				model.put("agrApvrList", agrApvrList);
			}
		}
		
		// 통보목록
		List<ApOngdApvLnDVo> infmApOngdApvLnDVoList = new ArrayList<ApOngdApvLnDVo>();
		for(ApOngdApvLnDVo storedApOngdApvLnDVo : stampApOngdApvLnDVoList){
			if("psnInfm".equals(storedApOngdApvLnDVo.getApvrRoleCd()) || "deptInfm".equals(storedApOngdApvLnDVo.getApvrRoleCd())){
				infmApOngdApvLnDVoList.add(storedApOngdApvLnDVo);
			}
		}
		if(!infmApOngdApvLnDVoList.isEmpty()){
			model.put("infmApOngdApvLnDVoList", infmApOngdApvLnDVoList);
		}
	}
	
	/** 참조열람 조회일시 업데이트 */
	private void updateRefVwDt(String apvNo, String storage, UserVo userVo, ModelMap model) throws SQLException {
		
		//String refVwrUid = userVo.getUserUid();
		
		List<String> refVwrUidList = new ArrayList<String>();
		refVwrUidList.add(userVo.getUserUid());
		if(userVo.getAdurs()!=null && userVo.getAdurs().length!=0){
			String adurUid;
			for(String[] adur : userVo.getAdurs()){
				adurUid = adur[1];
				if(adurUid!=null && !adurUid.isEmpty() && !refVwrUidList.contains(adurUid)){
					refVwrUidList.add(adurUid);
				}
			}
		}
		
		ApOngdRefVwDVo apOngdRefVwDVo;
		
		for(String refVwrUid : refVwrUidList){
			
			// 진행문서참조열람상세 - 조회
			apOngdRefVwDVo = new ApOngdRefVwDVo();
			apOngdRefVwDVo.setApvNo(apvNo);
			apOngdRefVwDVo.setRefVwrUid(refVwrUid);
			apOngdRefVwDVo.setStorage(storage);
			apOngdRefVwDVo = (ApOngdRefVwDVo)commonDao.queryVo(apOngdRefVwDVo);
			
			if(apOngdRefVwDVo!=null){
				
				model.put("apOngdRefVwDVo", apOngdRefVwDVo);
				
				// 읽지 않음 이면 - 읽음 표시
				if(apOngdRefVwDVo.getVwDt()==null || apOngdRefVwDVo.getVwDt().isEmpty()){
					apOngdRefVwDVo = new ApOngdRefVwDVo();
					apOngdRefVwDVo.setApvNo(apvNo);
					apOngdRefVwDVo.setRefVwrUid(refVwrUid);
					apOngdRefVwDVo.setStorage(storage);
					apOngdRefVwDVo.setVwDt("sysdate");
					commonDao.update(apOngdRefVwDVo);
				}
				
				break;
			}
		}
		
	}

	/** 읽은 날짜 갱신 */
	private void updateSendVwDt(String apvNo, String sendSeq) throws SQLException{
		
		// 진행문서발송상세 - 조회
		ApOngdSendDVo apOngdSendDVo = new ApOngdSendDVo();
		apOngdSendDVo.setApvNo(apvNo);
		apOngdSendDVo.setSendSeq(sendSeq);
		apOngdSendDVo = (ApOngdSendDVo)commonDao.queryVo(apOngdSendDVo);
		
		if(apOngdSendDVo!=null && (
				apOngdSendDVo.getVwDt()==null || apOngdSendDVo.getVwDt().isEmpty()  )){
			// 읽은 날짜 update
			ApOngdSendDVo updateApOngdSendDVo = new ApOngdSendDVo();
			updateApOngdSendDVo.setApvNo(apvNo);
			updateApOngdSendDVo.setSendSeq(apOngdSendDVo.getSendSeq());
			updateApOngdSendDVo.setVwDt("sysdate");
			commonDao.update(updateApOngdSendDVo);
		}
	}
	
	/** 읽은 날짜 갱신 */
	private void updatePichVwDt(String apvNo, String pichTypCd) throws SQLException {
		
		// 진행문서담당자상세 - 조회
		ApOngdPichDVo apOngdPichDVo = new ApOngdPichDVo();
		apOngdPichDVo.setApvNo(apvNo);
		apOngdPichDVo.setPichTypCd(pichTypCd);
		apOngdPichDVo = (ApOngdPichDVo)commonDao.queryVo(apOngdPichDVo);
		
		if(apOngdPichDVo!=null && (
				apOngdPichDVo.getVwDt()==null || apOngdPichDVo.getVwDt().isEmpty()  )){
			// 읽은 날짜 update
			apOngdPichDVo = new ApOngdPichDVo();
			apOngdPichDVo.setApvNo(apvNo);
			apOngdPichDVo.setPichTypCd(pichTypCd);//담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송
			apOngdPichDVo.setVwDt("sysdate");
			commonDao.update(apOngdPichDVo);
		}
	}
	
	/** 읽은 날짜 갱신 - 통보의 경우 */
	private void updateApvLnApvDt(ApOngdApvLnDVo myApOngdApvLnDVo) throws SQLException{
		// 읽은 날짜 update
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(myApOngdApvLnDVo.getApvNo());
		apOngdApvLnDVo.setApvLnPno(myApOngdApvLnDVo.getApvLnPno());
		apOngdApvLnDVo.setApvLnNo(myApOngdApvLnDVo.getApvLnNo());
		apOngdApvLnDVo.setApvDt("sysdate");
		commonDao.update(apOngdApvLnDVo);
	}
	
	/** 읽은 날짜 갱신 */
	private void updateApvLnVwDt(ApOngdApvLnDVo myApOngdApvLnDVo) throws SQLException{
		// 읽은 날짜 update
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(myApOngdApvLnDVo.getApvNo());
		apOngdApvLnDVo.setApvLnPno(myApOngdApvLnDVo.getApvLnPno());
		apOngdApvLnDVo.setApvLnNo(myApOngdApvLnDVo.getApvLnNo());
		apOngdApvLnDVo.setVwDt("sysdate");
		commonDao.update(apOngdApvLnDVo);
	}
	
	
	/** 발신명의 표시용 [조직결재상세(OR_ORG_APV_D) 테이블] 조회하여 리턴 */
	public List<OrOrgApvDVo> getSenderList(UserVo userVo, String langTypCd) throws SQLException {
		if(userVo.getDeptPids() != null && userVo.getDeptPids().length>0){
			// 세션의 상위 부서 목록
			String[] pids = userVo.getDeptPids();
			
			// 상위 부서 목록의 [조직결재상세] 조회
			OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
			ArrayList<String> orgIdList = new ArrayList<String>();
			for(String orgId : pids) orgIdList.add(orgId);
			orOrgApvDVo.setOrgIdList(orgIdList);
			orOrgApvDVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<OrOrgApvDVo> list = (List<OrOrgApvDVo>)commonDao.queryList(orOrgApvDVo);
			
			Map<Integer, OrOrgApvDVo> map = new HashMap<Integer, OrOrgApvDVo>();
			List<OrOrgApvDVo> returnList = new ArrayList<OrOrgApvDVo>();
			if(list!=null){
				// 순서 맞추기 위해서 맵으로 전환
				for(OrOrgApvDVo storedOrOrgApvDVo : list){
					map.put(Hash.hashId(storedOrOrgApvDVo.getOrgId()), storedOrOrgApvDVo);
				}
				
				// 뒤에서부터 - 상위 조직부터
				for(int i=pids.length-1;i>=0;i--){
					orOrgApvDVo = map.get(Hash.hashId(pids[i]));
					if(orOrgApvDVo!=null){
						returnList.add(orOrgApvDVo);
					}
				}
			}
			return returnList.isEmpty() ? null : returnList;
		}
		return null;
	}
	
	/** 서명 이미지(orUserImgDVo) 서명방법(signMthdCd) 조회하여 모델에 세팅 */
	//public void setUserSignImg(String userUid, String odurUid, ModelMap model) throws SQLException{
	public void setUserSignImg(UserVo userVo, String odurUid, ModelMap model) throws SQLException{
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		// [옵션] signAreaSign=서명란 날인 방법 - psn:개인선택, 01:도장 이미지, 02:서명 이미지, 03:사용자명(문자)
		String signMthdCd = optConfigMap.get("signAreaSign");
		
		// [옵션] 서명방법 - 개인별 설정 이면
		if("psn".equals(signMthdCd)){
			// 개인설정 조회
			OrUserBVo storedOrUserBVo = new OrUserBVo();
			storedOrUserBVo.setUserUid(userVo.getUserUid());
			storedOrUserBVo = (OrUserBVo)commonDao.queryVo(storedOrUserBVo);
			if(storedOrUserBVo!=null){
				signMthdCd = storedOrUserBVo.getSignMthdCd();
				if(signMthdCd==null || signMthdCd.isEmpty()){
					signMthdCd = ApConstant.DFT_SIGN_MTHD_CD;// 03:사용자명(문자)
				}
			} else {
				signMthdCd = ApConstant.DFT_SIGN_MTHD_CD;// 03:사용자명(문자)
			}
		}
		// 서명 방법 세팅
		model.put("signMthdCd", signMthdCd);
		
		if(!"03".equals(signMthdCd)){
			// 사용자이미지상세(OR_USER_IMG_D) 테이블 - "겸직자" 도장/사인 조회
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			orUserImgDVo.setUserUid(userVo.getUserUid());
			orUserImgDVo.setUserImgTypCd(signMthdCd);
			orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
			if(orUserImgDVo!=null){
				// 겸직자의 서명 이미지가 세팅되면 - 겸직자 것 리턴
				model.put("orUserImgDVo", orUserImgDVo);
				return;
			} else {
				// 사용자이미지상세(OR_USER_IMG_D) 테이블 - "원직자" 도장/사인 조회
				orUserImgDVo = new OrUserImgDVo();
				orUserImgDVo.setUserUid(odurUid);
				orUserImgDVo.setUserImgTypCd(signMthdCd);
				orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
				if(orUserImgDVo!=null){
					// 원직자 이미지 리턴
					model.put("orUserImgDVo", orUserImgDVo);
					return;
				}
			}
		}
	}
	
	/** 첨부파일 조회 리턴 */
	public List<ApOngdAttFileLVo> getApOngdAttFileLVoList(String apvNo, String attHstNo, String[] attSeqs) throws SQLException {
		// 보류 첨부 이력번호
		if(attHstNo==null || attHstNo.isEmpty()) attHstNo = "1";
		
		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
		apOngdAttFileLVo.setApvNo(apvNo);
		apOngdAttFileLVo.setAttHstNo(attHstNo);
		if(attSeqs.length==1) apOngdAttFileLVo.setAttSeq(attSeqs[0]);
		else  apOngdAttFileLVo.setAttSeqList(ArrayUtil.toList(attSeqs, true));
		apOngdAttFileLVo.setOrderBy("APV_NO, ATT_SEQ");
		@SuppressWarnings("unchecked")
		List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonDao.queryList(apOngdAttFileLVo);
		return apOngdAttFileLVoList;
	}
	
	/** 참조문서의 본문 내용을 세팅함 */
	public void setRefDocBody(ApOngdBVo apOngdBVo, ModelMap model) throws SQLException{
		
		// 참조문서 목록
		@SuppressWarnings("unchecked")
		List<ApOngdBVo> refApOngdBVoList = (List<ApOngdBVo>)model.get("refApOngdBVoList");
		if(refApOngdBVoList==null) return;
		
		// 양식 구성 요소
		@SuppressWarnings("unchecked")
		List<ApOngoFormCombDVo> apFormCombDVoList = (List<ApOngoFormCombDVo>)model.get("apFormCombDVoList");
		if(apFormCombDVoList==null) return;
		
		// 참조문서 본문 삽입 - 을 가지고 있는지 여부
		boolean hasRefDocBody = false;
		for(ApOngoFormCombDVo storedApOngoFormCombDVo : apFormCombDVoList){
			if("refDocBdoy".equals(storedApOngoFormCombDVo.getFormCombId())){
				hasRefDocBody = true;
				break;
			}
		}
		if(!hasRefDocBody) return;
		
		// 본문 조회
		ApOngdBodyLVo apOngdBodyLVo;
		for(ApOngdBVo refApOngdBVo : refApOngdBVoList){
			
			apOngdBodyLVo = new ApOngdBodyLVo();
			apOngdBodyLVo.setApvNo(refApOngdBVo.getApvNo());
			apOngdBodyLVo.setBodyHstNo(refApOngdBVo.getBodyHstNo());
			apOngdBodyLVo = (ApOngdBodyLVo)commonDao.queryVo(apOngdBodyLVo);
			if(apOngdBodyLVo != null){
				// 참조문서에 본문 세팅
				refApOngdBVo.setBodyHtml(apOngdBodyLVo.getBodyHtml());
			}
		}
	}
	
	/** ERP 양식 결재 - XML 조회 */
	public void setFormBodyXML(String apvNo, String erpFormId, String erpFormTypCd, ModelMap model) throws SQLException, SAXException, IOException, ParserConfigurationException{
		if(erpFormId==null || erpFormId.isEmpty()) return;
		if(!"xmlFromAp".equals(erpFormTypCd)
				&& !"xmlEditFromAp".equals(erpFormTypCd)
				&& !"wfForm".equals(erpFormTypCd)) return;
		
		if(apvNo!=null && !apvNo.isEmpty()) {
		
			ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			@SuppressWarnings("unchecked")
			List<ApOngdErpFormDVo> apOngdErpFormDVoList = (List<ApOngdErpFormDVo>)commonDao.queryList(apOngdErpFormDVo);
			
			if(apOngdErpFormDVoList != null){
				model.remove("formBodyXML");
				String erpVaTypCd;
				
				for(ApOngdErpFormDVo storedApOngdErpFormDVo : apOngdErpFormDVoList){
					erpVaTypCd = storedApOngdErpFormDVo.getErpVaTypCd();
					if("formBodyXML".equals(erpVaTypCd)){
						if(model.get("formBodyXML") == null){
							model.put("formBodyXML", SAXHandler.parse(storedApOngdErpFormDVo.getErpVa()));
						}
					} else if("formBodyHoldXML".equals(erpVaTypCd)){
						model.put("formBodyXML", SAXHandler.parse(storedApOngdErpFormDVo.getErpVa()));
					} else {
						model.put(erpVaTypCd, storedApOngdErpFormDVo.getErpVa());
					}
				}
			}
		} else if("xmlEditFromAp".equals(erpFormTypCd)){
			ApFormBVo apFormBVo = (ApFormBVo)model.get("apFormBVo");
			apFormSvc.setFormEditBodyXml(apFormBVo.getFormId(), model);
		}
		if(model.get("formBodyXML") == null){
			model.put("formBodyXML", new XMLElement(null));
		}
	}
	
//	/** 결재선 정보에 리소스 바인딩 */
//	private void setUserResc(ApOngdApvLnDVo apOngdApvLnDVo, UserVo userVo, String langTypCd, Locale locale) throws SQLException, CmException{
//		
//		String apvrUid = userVo.getUserUid();
//		String apvDeptId = userVo.getDeptId();
//		
//		// 사용자 정보 조회
//		OrUserBVo orUserBVo = apRescSvc.getOrUserBVo(apvrUid, langTypCd, null);
//		if(orUserBVo == null){
//			LOGGER.error("Fail trans - user not found ! - apvrUid:"+apvrUid);
//			//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
//			String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.user"}, locale)
//					+ apvrUid+ " : " + apOngdApvLnDVo.getApvrNm();
//			throw new CmException(message);
//		}
//		
//		apOngdApvLnDVo.setApvrUid(apvrUid);
//		apOngdApvLnDVo.setApvrNm(orUserBVo.getUserNm());
//		apOngdApvLnDVo.setApvrPositCd(orUserBVo.getPositCd());
//		apOngdApvLnDVo.setApvrPositNm(orUserBVo.getPositNm());
//		apOngdApvLnDVo.setApvrTitleCd(orUserBVo.getTitleCd());
//		apOngdApvLnDVo.setApvrTitleNm(orUserBVo.getTitleNm());
//		
//		// 부서정보
//		OrOrgBVo orOrgBVo = apRescSvc.getOrOrgBVo(apvDeptId, langTypCd, null);
//		if(orOrgBVo==null){
//			LOGGER.error("Fail trans - dept not found ! - apvDeptId:"+apvDeptId);
//			//ap.trans.notFound=해당 {0} 정보를 찾을 수 없습니다.
//			String message = messageProperties.getMessage("ap.trans.notFound", new String[]{"#cols.dept"}, locale)
//					+ apvDeptId+ " : " + apOngdApvLnDVo.getApvDeptNm();
//			throw new CmException(message);
//		}
//		
//		apOngdApvLnDVo.setApvDeptId(orOrgBVo.getOrgId());
//		apOngdApvLnDVo.setApvDeptNm(orOrgBVo.getRescNm());
//		String rescVa = orOrgBVo.getOrgAbbrRescNm();
//		if(rescVa==null || rescVa.isEmpty()){
//			apOngdApvLnDVo.setApvDeptAbbrNm(orOrgBVo.getRescNm());
//		} else {
//			apOngdApvLnDVo.setApvDeptAbbrNm(rescVa);
//		}
//	}
}
