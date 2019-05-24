package com.innobiz.orange.web.ap.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOrgHstRVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;

/** 결재 함 서비스 */
@Service
public class ApBxSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DAO */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	public static ApBxSvc ins;
	public ApBxSvc(){ ins = this; }
	
	/** 함별 조회 조건 세팅 */
	public boolean setApvBx(UserVo userVo, String langTypCd, String bxId, ApOngdBVo apOngdBVo, boolean hasAdmin, ModelMap model) throws SQLException{
		
		// 결재 옵션
		Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
		
		// 피 대결자 UID 목록 - 자신의 UID 포함
		List<String> agntUidList = null;
		
		// [옵션][부재 설정 시 문서 열람 범위]
		if("Y".equals(optConfigMap.get(bxId+"Abs"))){
			agntUidList = apCmSvc.getApAgntUidList(userVo);
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = null;
		
		// 결재자 별 함(결재라인 테이블 기준) - 대기함, 진행함, 완료함, 기안함, 반려함, 후열함
		if(ArrayUtil.isInArray(ApConstant.APV_LN_BXES, bxId)){
			
			List<String> userUidList = agntUidList;
			
			// 겸직 통합 표시
			if("Y".equals(optConfigMap.get("adurMergLst")) && ArrayUtil.isInArray(ApConstant.ADUR_BXES, bxId)){
				// 겸직이 있으면
				String[][] adurs = userVo.getAdurs();
				if(adurs!=null && adurs.length>0){
					if(userUidList==null) userUidList = new ArrayList<String>();
					
					if(!userUidList.contains(userVo.getUserUid())) {
						userUidList.add(userVo.getUserUid());
					}
					for(int i=0;i<adurs.length;i++){
						if(!userUidList.contains(adurs[i][1])) userUidList.add(adurs[i][1]);
					}
				}
			}
			
			if(userUidList!=null && userUidList.size()>1){
				apOngdBVo.setApvrUidList(userUidList);
			} else {
				apOngdBVo.setApvrUid(userVo.getUserUid());
			}
			
			// 후열함(통보함)
			if("postApvdBx".equals(bxId)){
				// 부서이력
				List<String> apvDeptIdList = getDeptHistoryList(userVo.getDeptId());
				if(apvDeptIdList==null){
					apOngdBVo.setApvDeptId(userVo.getDeptId());
				} else {
					apOngdBVo.setApvDeptIdList(apvDeptIdList);
				}
				
				setSeculCdList(apOngdBVo, userVo, langTypCd);
			}
			
		// 결재 부서 별 함(결재라인 테이블 기준) - 부서대기함
		} else if("deptBx".equals(bxId)){
			apOngdBVo.setApvDeptId(userVo.getDeptId());
			
		// 담당 부서 별 함(담당자 테이블 기준) - 발송함
		} else if("toSendBx".equals(bxId)){
			apOngdBVo.setPichDeptId(userVo.getDeptId());

		// 담당자 별 함(담당자 테이블 기준) - 심사
		} else if("censrBx".equals(bxId)){
			if(agntUidList!=null){
				apOngdBVo.setPichUidList(agntUidList);
			} else {
				apOngdBVo.setPichUid(userVo.getUserUid());
			}
			
		// 접수 부서 별 함(발송 테이블 기준) - 접수함
		} else if("recvBx".equals(bxId)){
			String recvDeptId = apOngdBVo.getRecvDeptId();
			if(recvDeptId==null || recvDeptId.isEmpty()){
				recvDeptId = userVo.getDeptId();
			}
			apOngdBVo.setRecvDeptId(recvDeptId);//수신처ID
		// 접수 부서 별 함(발송 테이블 기준) - 배부함
		} else if("distBx".equals(bxId)){
			// 문서과 또는 대리문서과 조회
			String crdOrgId = apCmSvc.getCrdOrgId(userVo.getDeptId());
			if(crdOrgId==null) crdOrgId = userVo.getDeptId();
			apOngdBVo.setRecvDeptId(crdOrgId);//수신처ID - 배부처 ID
			
		// 대장 부서별 함(문서 테이블 기준) - 등록대장
		} else if("regRecLst".equals(bxId)){
			String recLstDeptId = apOngdBVo.getRecLstDeptId();
			if(recLstDeptId==null || recLstDeptId.isEmpty()){
				// 부서이력
				List<String> recLstDeptIdList = getDeptHistoryList(userVo.getDeptId());
				if(recLstDeptIdList==null){
					apOngdBVo.setRecLstDeptId(userVo.getDeptId());
				} else {
					apOngdBVo.setRecLstDeptIdList(recLstDeptIdList);
				}
			} else {
				// 자기 부서가 아닌 경우
				apOngdBVo.setAllReadYn("Y");//전체열람여부
			}
			apOngdBVo.setRegRecLstRegYn("Y");//등록대장등록여부
			if(!hasAdmin && "Y".equals(optConfigMap.get(bxId+"SecuLvl"))){
				setSeculCdList(apOngdBVo, userVo, langTypCd);
			}
		// 대장 부서별 함(문서 테이블 기준) - 접수대장
		} else if("recvRecLst".equals(bxId)){
			String recLstDeptId = apOngdBVo.getRecLstDeptId();
			if(recLstDeptId==null || recLstDeptId.isEmpty()){
				recLstDeptId = userVo.getDeptId();
			}
			// 부서이력
			List<String> recLstDeptIdList = getDeptHistoryList(recLstDeptId);
			if(recLstDeptIdList==null){
				apOngdBVo.setRecLstDeptId(recLstDeptId);
			} else {
				apOngdBVo.setRecLstDeptIdList(recLstDeptIdList);
			}
			
			if(!hasAdmin && "Y".equals(optConfigMap.get(bxId+"SecuLvl"))){
				setSeculCdList(apOngdBVo, userVo, langTypCd);
			}
		// 대장 부서별 함(문서 테이블 기준) - 배부대장
		} else if("distRecLst".equals(bxId)){
			// 문서과 또는 대리문서과 조회
			String crdOrgId = apCmSvc.getCrdOrgId(userVo.getDeptId());
			if(crdOrgId==null) crdOrgId = userVo.getDeptId();
			// 부서이력
			List<String> recLstDeptIdList = getDeptHistoryList(crdOrgId);
			if(recLstDeptIdList==null){
				apOngdBVo.setRecLstDeptId(crdOrgId);
			} else {
				apOngdBVo.setRecLstDeptIdList(recLstDeptIdList);
			}
			
		// 공람게시
		} else if("pubBx".equals(bxId)){
			String pubBxDeptId = apOngdBVo.getPubBxDeptId();
			if(pubBxDeptId==null || pubBxDeptId.isEmpty()){
				apOngdBVo.setPubBxDeptId(userVo.getDeptId());
			}
			if(!hasAdmin && "Y".equals(optConfigMap.get(bxId+"SecuLvl"))){
				setSeculCdList(apOngdBVo, userVo, langTypCd);
			}
		// 참조열람
		} else if("refVwBx".equals(bxId)){
			
			List<String> userUidList = null;
			
			// 겸직 통합 표시
			if("Y".equals(optConfigMap.get("adurMergLst")) && ArrayUtil.isInArray(ApConstant.ADUR_BXES, bxId)){
				// 겸직이 있으면
				String[][] adurs = userVo.getAdurs();
				if(adurs!=null && adurs.length>0){
					userUidList = new ArrayList<String>();
					userUidList.add(userVo.getUserUid());
					
					for(int i=0;i<adurs.length;i++){
						if(!userUidList.contains(adurs[i][1])) userUidList.add(adurs[i][1]);
					}
				}
			}
			
			if(userUidList!=null){
				apOngdBVo.setRefVwrUidList(userUidList);
			} else {
				apOngdBVo.setRefVwrUid(userVo.getUserUid());
			}
		}
		
		// 대기함
		if("waitBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"ongo"//ongo:결재중
					,"pubVw"//pubVw:공람
			));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					"makAgr"//makAgr:합의기안 - 부서합의
					,"byOneAgr"//byOneAgr:합의1인결재
					,"revw"//revw:검토
					,"revw2","revw3"//검토2,검토3 - 확장용
					,"psnOrdrdAgr"//psnOrdrdAgr:개인순차합의
					,"psnParalAgr"//psnParalAgr:개인병렬합의
					,"apv"//apv:결재
					,"pred"//pred:전결
					,"makVw"//makVw:담당
					,"fstVw"//fstVw:선람
					,"pubVw"//pubVw:공람
					,"paralPubVw"//paralPubVw:동시공람
			));
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"inApv"//inApv:결재중
					,"inAgr"//inAgr:합의중
					,"inVw"//inVw:공람중
					,"hold"//hold:보류
					,"cncl"//cncl:취소
					,"reRevw"//reRevw:재검토
			));
			
		// 진행함
		} else if("ongoBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCd("ongo");//ongo:결재중
			
			// [옵션][리스트 추가 조건][진행함] - ongoBxMak:기안
			boolean ongoBxMak = "Y".equals(optConfigMap.get("ongoBxMak"));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					
					// [옵션][리스트 추가 조건][진행함] - ongoBxMak:기안
					ongoBxMak ? "mak" : null // mak:기안
					
					,"makAgr"//makAgr:합의기안 - 부서합의
					,"byOneAgr"//byOneAgr:합의1인결재
					,"revw"//revw:검토
					,"revw2","revw3"//검토2,검토3 - 확장용
					,"psnOrdrdAgr"//psnOrdrdAgr:개인순차합의
					,"psnParalAgr"//psnParalAgr:개인병렬합의
					,"makAgr"//makAgr:합의기안
					,"abs"//abs:공석
					,"apv"//apv:결재
					,"pred"//pred:전결
			));
			
			//[옵션][리스트 추가 조건][진행함] - ongoBxBefoMyTurn:결재 이전
			boolean befoMyTurn = "Y".equals(optConfigMap.get("ongoBxBefoMyTurn"));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					
					// [옵션][리스트 추가 조건][진행함] - ongoBxBefoMyTurn:결재 이전
					befoMyTurn ? "befoApv" : null //befoApv:결재전
					
					// [옵션][리스트 추가 조건][진행함] - ongoBxBefoMyTurn:결재 이전
					,befoMyTurn ? "befoAgr" : null //befoAgr:합의전
					
					,"inApv"//inApv:결재중
					,"inAgr"//inAgr:합의중
					,"apvd"//apvd:승인
					,"pros"//pros:찬성
					,"cons"//cons:반대
					,"agrRejt"//agrRejt:합의반려
					,"hold"//hold:보류
					,"cncl"//cncl:취소
					,"reRevw"//reRevw:재검토
			));
			
			
		// 완료함
		} else if("apvdBx".equals(bxId)){
			
			// [옵션][리스트 추가 조건][완료함] - apvdBxFstVw:선람
			boolean apvdBxFstVw = "Y".equals(optConfigMap.get("apvdBxFstVw"));
			// [옵션][리스트 추가 조건][완료함] - apvdBxPubVw:공람
			boolean apvdBxPubVw = "Y".equals(optConfigMap.get("apvdBxPubVw"));
			// [옵션][리스트 추가 조건][완료함] - apvdBxParalPubVw:동시공람
			boolean apvdBxParalPubVw = "Y".equals(optConfigMap.get("apvdBxParalPubVw"));
			// [옵션][리스트 추가 조건][완료함] - apvdBxEntu:결재안함(위임)
			boolean apvdBxEntu = "Y".equals(optConfigMap.get("apvdBxEntu"));
			// [옵션][리스트 추가 조건][완료함] - apvdBxMak:기안
			boolean apvdBxMak = "Y".equals(optConfigMap.get("apvdBxMak"));
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			if(apvdBxFstVw || apvdBxPubVw || apvdBxParalPubVw){
				apOngdBVo.setDocProsStatCdList(toList(
						"apvd"//apvd:승인
						,"pubVw"//pubVw:공람
				));
			} else {
				apOngdBVo.setDocProsStatCd("apvd");//apvd:승인
			}
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxMak:기안
					apvdBxMak ? "mak" : null//mak:기안
					
					,"makAgr"//makAgr:합의기안
					,"byOneAgr"//byOneAgr:합의1인결재
					,"revw"//revw:검토
					,"revw2","revw3"//검토2,검토3 - 확장용
					,"psnOrdrdAgr"//psnOrdrdAgr:개인순차합의
					,"psnParalAgr"//psnParalAgr:개인병렬합의
					,"abs"//abs:공석
					,"apv"//apv:결재
					,"pred"//pred:전결
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxEntu:결재안함(위임)
					,apvdBxEntu ? "entu" : null//entu:위임
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxFstVw:선람
					,apvdBxFstVw ? "fstVw" : null//fstVw:선람
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxPubVw:공람
					,apvdBxPubVw ? "pubVw" : null//pubVw:공람
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxParalPubVw:동시공람
					,apvdBxParalPubVw ? "paralPubVw" : null//paralPubVw:동시공람
			));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"apvd"//apvd:승인
					,"pros"//pros:찬성
					,"cons"//cons:반대
					
					//[옵션][리스트 추가 조건][완료함] - apvdBxEntu:결재안함(위임)
					,apvdBxEntu ? "inInfm" : null//inInfm:통보중
					
					// [옵션][리스트 추가 조건][완료함] - apvdBxFstVw:선람
					// [옵션][리스트 추가 조건][완료함] - apvdBxPubVw:공람
					// [옵션][리스트 추가 조건][완료함] - apvdBxParalPubVw:동시공람
					, (apvdBxFstVw || apvdBxPubVw || apvdBxParalPubVw) ? "cmplVw" : null//cmplVw:공람완료
			));
			
			
		// 기안함
		} else if("myBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"mak"//mak:기안, 
					,"ongo"//ongo:결재중
					,"apvd"//apvd:승인
			));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					"byOne"//byOne:1인결재
					,"mak"//mak:기안
			));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"inApv"//inApv:결재중
					,"apvd"//apvd:승인
					,"cncl"//cncl:취소
					,"reRevw"//reRevw:재검토
			));
			
			
		// 반려함
		} else if("rejtBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"rejt"//rejt:반려
			));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					"mak"//mak:기안
					,"revw"//revw:검토
					,"revw2","revw3"//검토2,검토3 - 확장용
					,"psnOrdrdAgr"//psnOrdrdAgr:개인순차합의
					,"psnParalAgr"//psnParalAgr:개인병렬합의
					,"makAgr"//makAgr:합의기안
					,"byOneAgr"//byOneAgr:합의1인결재
					,"apv"//apv:결재
					,"pred"//pred:전결
			));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"apvd"//apvd:승인
					,"rejt"//rejt:반려
					,"pros"//pros:찬성
					,"cons"//cons:반대
					,"agrRejt"//agrRejt:합의반려
			));
			
			
		// 후열함(통보함)
		} else if("postApvdBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"apvd"//apvd:승인
			));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(

					// 위임의 경우 - 완료함에 표시하면 후열함에서 제거함
					// [옵션][리스트 추가 조건][완료함] - apvdBxEntu:결재안함(위임)
					"Y".equals(optConfigMap.get("apvdBxEntu")) ? null : "entu"//entu:위임
					
					,"postApvd"//postApvd:후열
					,"psnInfm"//psnInfm:개인통보
					,"deptInfm"//deptInfm:부서통보
			));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"inInfm"//inInfm:통보중
			));
			
		// 개인함
		} else if("drftBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"temp"//temp:임시저장
			));
			
			// 기안자UID - 세팅
			apOngdBVo.setMakrUid(userVo.getUserUid());
			
		// 부서 대기함
		} else if("deptBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"ongo"//ongo:결재중
			));
			
			// 결재자역할코드
			//   byOne:1인결재, mak:기안, revw:검토, 
			//   psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
			//   deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, 
			//   byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재,
			//   pred:전결, entu:결재안함(위임), postApvd:사후보고(후열),  
			//   psnInfm:개인통보, deptInfm:부서통보, 
			//   makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
			apOngdBVo.setApvrRoleCdList(toList(
					"deptOrdrdAgr"//deptOrdrdAgr:부서순차합의
					,"deptParalAgr"//deptParalAgr:부서병렬합의
					,"prcDept"//prcDept:처리부서
			));
			
			// 결재상태코드
			//   befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
			//   befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, agrRejt:합의반려, 
			//   hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, 
			//   befoVw:공람전, inVw:공람중, cmplVw:공람완료
			apOngdBVo.setApvStatCdList(toList(
					"inApv"//inApv:결재중
					,"inAgr"//inAgr:합의중
			));
			
			// 담당자지정여부
			apOngdBVo.setPichApntYn("N");
			
		// 발송함
		} else if("toSendBx".equals(bxId)){
			
			// 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소
			apOngdBVo.setEnfcStatCd("befoSend");// befoSend:발송대기
			
			// 담당자 테이블 조인 정보
			// 담당자구분코드 - reqCensr:심사요청, censr:심사, send:발송
			apOngdBVo.setPichTypCd("send");
			// 처리상태코드 - befoSend:발송대기, sendCmpl:발송완료
			apOngdBVo.setHdlStatCd("befoSend");//befoSend:발송대기
			
		// 심사함
		} else if("censrBx".equals(bxId)){

			// 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소
			apOngdBVo.setEnfcStatCd("inCensr");// inCensr:심사

			// 담당자 테이블 조인 정보
			// 담당자구분코드 - reqCensr:심사요청, censr:심사, send:발송
			apOngdBVo.setPichTypCd("censr");
			// 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려
			apOngdBVo.setHdlStatCd("befoCensr");//befoCensr:심사전
			
		// 접수함
		} else if("recvBx".equals(bxId)){
			
			// 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소
			apOngdBVo.setEnfcStatCd("sent");// sent:발송
			
			// 발송 테이블 조인 정보
			// 처리상태코드 - befoRecv:접수대기, recdCmpl:접수완료, befoDist:배부대기, distCmpl:배부완료
			apOngdBVo.setHdlStatCd("befoRecv");//befoRecv:접수대기
			
		// 배부함
		} else if("distBx".equals(bxId)){
			
			// 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소
			apOngdBVo.setEnfcStatCd("sent");// sent:발송
			
			// 발송 테이블 조인 정보
			// 처리상태코드 - befoRecv:접수대기, recdCmpl:접수완료, befoDist:배부대기, distCmpl:배부완료
			apOngdBVo.setHdlStatCd("befoDist");//befoDist:배부대기
			
		// 등록 대장, 접수 대장, 배부 대장
		} else if("regRecLst".equals(bxId) || "recvRecLst".equals(bxId) || "distRecLst".equals(bxId)){
			apOngdBVo.setRecLstTypCd(bxId);
		// 공람게시
		} else if("pubBx".equals(bxId)){
			apOngdBVo.setPubBxEndYmd(StringUtil.getDiffYmd(-1));
		// 참조열람
		} else if("refVwBx".equals(bxId)){
			
			model.put("refVwStatCdDefault", "");
//			if("BE6E4F".equals(CustConfig.CUST_CODE)){//BE6E4F : (주)고산  -  || CustConfig.IS_LOC
//				model.put("refVwStatCdDefault", "");
//			} else {
//				model.put("refVwStatCdDefault", "inRefVw");
//				if(apOngdBVo.getRefVwStatCd()==null){
//					apOngdBVo.setRefVwStatCd("inRefVw");//inRefVw:참조열람중
//				}
//			}
			
			// temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList("mak","ongo","apvd", "pubVw"));
			
			// 기안회수가 아닌것
			apOngdBVo.setWhereSqllet("AND T.DOC_STAT_CD != 'retrvMak'");
			
		// 관리자 - 진행문서
		} else if("admOngoBx".equals(bxId)){
			
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
				"ongo"//ongo:결재중
			));
			
		// 관리자 - 완료문서(등록대장 기준)
		} else if("admRegRecLst".equals(bxId)){
			apOngdBVo.setRecLstTypCd("regRecLst");
			
			// 시스템 정책 조회
			if(sysPlocMap==null) sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 완료문서 - 시스템옵션 : 결재 미등록 문서 보기(관리자 완료문서)
			if(!"Y".equals(sysPlocMap.get("apUnRegAdmView"))){
				apOngdBVo.setRegRecLstRegYn("Y");//등록대장등록여부
			}
		// 관리자 - 완료문서(승인 기준)
		} else if("admApvdBx".equals(bxId)){
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"apvd"//apvd:승인
			));
		// 관리자 - 양식별 진행문서
		} else if("admOngoFormBx".equals(bxId)){
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
				"ongo"//ongo:결재중
			));
		// 관리자 - 양식별 완료문서
		} else if("admApvdFormBx".equals(bxId)){
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"apvd"//apvd:승인
			));
		// 관리자 - 양식별 전체문서
		} else if("admAllFormBx".equals(bxId)){
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					//"mak","ongo","apvd","pubVw"
					"ongo","apvd"
			));
		// 관리자 - 접수문서
		} else if("admRecvRecLst".equals(bxId)){
			apOngdBVo.setRecLstTypCd("recvRecLst");
		// 관리자 - 배부문서
		} else if("admDistRecLst".equals(bxId)){
			apOngdBVo.setRecLstTypCd("distRecLst");
		// 관리자 - 반려문서
		} else if("admRejtBx".equals(bxId)){
			//문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람
			apOngdBVo.setDocProsStatCdList(toList(
					"rejt"//rejt:반려
			));
		// 관리자 - 의견목록
		} else if("admOpinBx".equals(bxId)){
			apOngdBVo.setHasOpinYn("Y");
		} else {
			return false;
		}
		
		// 관리자용 조회 프로그램일 경우
		if("admOngoBx".equals(bxId)
				|| "admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId)
				|| "admOngoFormBx".equals(bxId) || "admApvdFormBx".equals(bxId) || "admAllFormBx".equals(bxId)
				|| "admRecvRecLst".equals(bxId) || "admDistRecLst".equals(bxId)
				|| "admRejtBx".equals(bxId) || "admOpinBx".equals(bxId)){
			
			// 시스템 정책 조회
			if(sysPlocMap==null) sysPlocMap = ptSysSvc.getSysPlocMap();
			
			boolean isSysAdmin = ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_SYS_ADMIN);
			if(isSysAdmin){
				// 전사 결재 문서 보기(시스템 관리자)
				if(!"Y".equals(sysPlocMap.get("apSysAdmViewEnable"))){
					apOngdBVo.setCompId(userVo.getCompId());
				}
			} else {
				apOngdBVo.setCompId(userVo.getCompId());
			}
		}
		
		return true;
	}
	
	/** 함별 조회 조건 세팅 */
	public void setNextPrevDoc(UserVo userVo, String langTypCd, String bxId, String apvNo,
			boolean hasAdmin, String queryString, HttpSession session, ModelMap model) throws SQLException, IOException{
		
		if(apvNo==null) return;

		// 진행문서기본(AP_ONGD_B) 테이블
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		Map<String, String> paramMap = null;
		if(queryString!=null && !queryString.isEmpty()){
			paramMap = ParamUtil.getMapFromQueryString(queryString);
			apOngdBVo.fromMap(paramMap);
		}
		apOngdBVo.setBxId(bxId);
		apOngdBVo.setQueryLang(langTypCd);
		
		String schWord = apOngdBVo.getSchWord();
		String schCat = apOngdBVo.getSchCat();
		
		if(schWord != null && !schWord.isEmpty()){
			if("srchDocNo".equals(schCat)){
				apOngdBVo.setSrchDocNo(schWord);
			} else if("srchRecvDocNo".equals(schCat)){
				apOngdBVo.setSrchRecvDocNo(schWord);
			} else if("docSubj".equals(schCat)){
				apOngdBVo.setDocSubj(schWord);
			} else if("makrNm".equals(schCat)){
				apOngdBVo.setMakrNm(schWord);
			} else if("bodyHtml".equals(schCat)){
				apOngdBVo.setBodyHtml(schWord);
			}
		} else {
			apOngdBVo.setSchCat(null);
			apOngdBVo.setSchWord(null);
		}
		// 파라미터로 세팅된 날짜 - 데이터 정비
		setDurDt(apOngdBVo);
		
		// 리스트 옵션 조회 - 리스트에 보일 항목 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = apCmSvc.setListQueryOptions(apOngdBVo, bxId);
		model.put("ptLstSetupDVoList", ptLstSetupDVoList);
		
		//boolean hasAdmin = SecuUtil.hasAuth(request, "A", null);
		// 함별 조회 조건 세팅
		boolean valid = setApvBx(userVo, langTypCd, bxId, apOngdBVo, hasAdmin, model);
		
		if(valid){
			if(apOngdBVo.getPageRowCnt()==null){
				apOngdBVo.setPageNo(1);
				apOngdBVo.setPageRowCnt(PersonalUtil.getPageRowCnt("ap", session));
			}
			
			@SuppressWarnings("unchecked")
			List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonDao.queryList(apOngdBVo);
			
			Integer orginPageNo = apOngdBVo.getPageNo();
			if(orginPageNo==null) orginPageNo = 1;
			
			boolean completed = false, passed = false;
			ApOngdBVo storedApOngdBVo;
			int i, size = apOngdBVoList==null ? 0 : apOngdBVoList.size();
			
			// next 세팅
			for(i=0;i<size;i++){
				storedApOngdBVo = apOngdBVoList.get(i);
				if(passed){
					model.put("nextApOngdBVo", storedApOngdBVo);
					completed = true;
					break;
				} else if(apvNo.equals(storedApOngdBVo.getApvNo())){
					passed = true;
				}
			}
			
			// 다음페이지에 [next문서]가 있으면 - 다음 페이지 조회 후 첫번째 거
			if(!completed && passed){
				apOngdBVo.setPageNo(orginPageNo+1);
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> nextApOngdBVoList = (List<ApOngdBVo>)commonDao.queryList(apOngdBVo);
				if(nextApOngdBVoList!=null && !nextApOngdBVoList.isEmpty()){
					model.put("nextApOngdBVo", nextApOngdBVoList.get(0));
					model.put("nextDocAtNextPage", "1");
				}
			}
			
			// prev 세팅
			completed = false;
			passed = false;
			for(i=size-1;i>=0;i--){
				storedApOngdBVo = apOngdBVoList.get(i);
				if(passed){
					model.put("prevApOngdBVo", storedApOngdBVo);
					completed = true;
					break;
				} else if(apvNo.equals(storedApOngdBVo.getApvNo())){
					passed = true;
				}
			}
			
			// 이전페이지에 [prev문서]가 있으면 - 이전 페이지 조회 후 마지막 거
			if(!completed && passed && orginPageNo.intValue()!=1){
				apOngdBVo.setPageNo(orginPageNo-1);
				@SuppressWarnings("unchecked")
				List<ApOngdBVo> prevApOngdBVoList = (List<ApOngdBVo>)commonDao.queryList(apOngdBVo);
				if(prevApOngdBVoList!=null && !prevApOngdBVoList.isEmpty()){
					model.put("prevApOngdBVo", prevApOngdBVoList.get(prevApOngdBVoList.size()-1));
					model.put("prevDocAtPrevPage", "-1");
				}
			}
		}
	}
	
	/** 부서이력 조직ID 목록 리턴 */
	public List<String> getDeptHistoryList(String orgId) throws SQLException{
		OrOrgHstRVo orOrgHstRVo = new OrOrgHstRVo();
		orOrgHstRVo.setOrgId(orgId);
		@SuppressWarnings("unchecked")
		List<OrOrgHstRVo> orOrgHstRVoList = (List<OrOrgHstRVo>)commonDao.queryList(orOrgHstRVo);
		if(orOrgHstRVoList!=null){
			List<String> returnList = new ArrayList<String>();
			returnList.add(orgId);
			for(OrOrgHstRVo storedOrOrgHstRVo : orOrgHstRVoList){
				if(!returnList.contains(storedOrOrgHstRVo.getHstOrgId())){
					returnList.add(storedOrOrgHstRVo.getHstOrgId());
				}
			}
			if(returnList.size()>1) return returnList;
		}
		return null;
	}
	
	/** 배열을 List 로 전환 */
	private List<String> toList(String ... array){
		if(array==null || array.length==0) return null;
		List<String> list = new ArrayList<String>();
		for(String str : array){
			if(str!=null) list.add(str);
		}
		return list;
	}
	
	/** 보안등급을 설정함 */
	private void setSeculCdList(ApOngdBVo apOngdBVo, UserVo userVo, String langTypCd) throws SQLException{
		String seculCd = userVo.getSeculCd();
		
		List<String> seculCdList = new ArrayList<String>();
		seculCdList.add("none");
		
		if(seculCd!=null && !seculCd.isEmpty()){
			
			boolean afterMyLevel = userVo.isAdmin();
			List<PtCdBVo> cdList = ptCmSvc.getAllCdList("SECUL_CD", langTypCd);
			if(cdList != null){
				for(PtCdBVo ptCdBVo : cdList){
					if(afterMyLevel){
						seculCdList.add(ptCdBVo.getCd());
					} else {
						if(seculCd.equals(ptCdBVo.getCd())){
							seculCdList.add(ptCdBVo.getCd());
							afterMyLevel = true;
						}
					}
				}
			}
		}
		apOngdBVo.setSeculCdList(seculCdList);
	}
	
	/** bxId별 함 URL 리턴 */
	public String getBxUrlByBxId(UserVo userVo, String bxId, String menuId) throws SQLException{
		if(bxId==null || bxId.isEmpty()) bxId = "waitBx";
		String url = getBxUrlByBxId(bxId);
		if(menuId==null || menuId.isEmpty()) menuId = ptSecuSvc.getSecuMenuId(userVo, url);
		return menuId==null ? url : url+"&menuId="+menuId;
	}
	
	/** bxId별 View URL 리턴 */
	public String getDocUrlByBxId(UserVo userVo, String bxId, String menuId) throws SQLException{
		if(bxId==null || bxId.isEmpty()) bxId = "waitBx";
		String url = getBxUrlByBxId(bxId);
		if(menuId==null || menuId.isEmpty()) menuId = ptSecuSvc.getSecuMenuId(userVo, url);
		if(menuId==null){
			return "/ap/box/setDoc.do?bxId="+bxId;
		} else {
			return "/ap/box/setDoc.do?menuId="+menuId+"&bxId="+bxId;
		}
	}
	
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("regRecLst") || bxId.equals("recvRecLst") || bxId.equals("pubBx")){
			return "/ap/box/listApvRecBx.do?bxId="+bxId;
		} else {
			return "/ap/box/listApvBx.do?bxId="+bxId;
		}
	}
	
	/** bxId별 메뉴ID 리턴 */
	public String getMenuIdByBxId(UserVo userVo, String bxId) throws SQLException{
		if(bxId==null || bxId.isEmpty()) bxId = "waitBx";
		String url = getBxUrlByBxId(bxId);
		return ptSecuSvc.getSecuMenuId(userVo, url);
	}
	
	/** bxId별 함 URL 리턴 */
	public String getAdmBxUrlByBxId(UserVo userVo, String bxId, String strMnuParam) throws SQLException{
		if(bxId==null || bxId.isEmpty()) bxId = "admOngoBx";
		String url = "/ap/adm/box/listApvBx.do?bxId="+bxId;
		String menuId = ptSecuSvc.getSecuMenuId(userVo, url+(strMnuParam==null ? "" : strMnuParam));
		return menuId==null ? url : url+"&menuId="+menuId;
	}
	
	/** 검색용 날짜 세팅 - 종료일은 하루를 더함 */
	public void setDurDt(ApOngdBVo apOngdBVo){
		if(apOngdBVo.getDurStrtDt()!=null && apOngdBVo.getDurStrtDt().length()==10){
			apOngdBVo.setDurStrtDt(apOngdBVo.getDurStrtDt()+" 00:00:00");
		} else {
			apOngdBVo.setDurStrtDt(null);
		}
		if(apOngdBVo.getDurEndDt()!=null && apOngdBVo.getDurEndDt().length()==10){
			apOngdBVo.setDurEndDt(StringUtil.addDate(apOngdBVo.getDurEndDt(), 1)+" 00:00:00");
		} else {
			apOngdBVo.setDurEndDt(null);
		}
	}
	
	/** 의견 결재자 목록 리턴 */
	public List<String> getOpinApvrList(UserVo userVo, String bxId) throws SQLException{
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userVo.getUserUid());
		ptUserSetupDVo.setSetupClsId("ap.userSetup");
		ptUserSetupDVo.setSetupId(bxId);
		ptUserSetupDVo = (PtUserSetupDVo)commonDao.queryVo(ptUserSetupDVo);
		if(ptUserSetupDVo == null) return null;
		String setupVa = ptUserSetupDVo.getSetupVa();
		if(setupVa==null || setupVa.isEmpty()) return null;
		
		List<String> returnList = new ArrayList<String>();
		for(String apvrUid : setupVa.split(",")){
			returnList.add(apvrUid);
		}
		
		return returnList;
	}
	/** 의견 결재자 목록 저장 */
	public void storeOpinApvrList(UserVo userVo, String bxId, String apvrUids) throws SQLException{
		
		QueryQueue queryQueue = new QueryQueue();
		
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userVo.getUserUid());
		ptUserSetupDVo.setSetupClsId("ap.userSetup");
		ptUserSetupDVo.setSetupId(bxId);
		queryQueue.delete(ptUserSetupDVo);
		
		if(apvrUids!=null && !apvrUids.isEmpty()){
			ptUserSetupDVo = new PtUserSetupDVo();
			ptUserSetupDVo.setUserUid(userVo.getUserUid());
			ptUserSetupDVo.setSetupClsId("ap.userSetup");
			ptUserSetupDVo.setSetupId(bxId);
			ptUserSetupDVo.setSetupVa(apvrUids);
			ptUserSetupDVo.setCacheYn("N");
			queryQueue.insert(ptUserSetupDVo);
		}
		
		commonSvc.execute(queryQueue);
	}
}
