package com.innobiz.orange.web.ap.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.cust.ApErpNotiSvc;
import com.innobiz.orange.web.ap.cust.ApWdSvc;
import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocSecuSvc;
import com.innobiz.orange.web.ap.svc.ApDocTransSvc;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApStorBVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtMobilePushMsgSvc;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;

/** 문서 저장 컨트롤러 - 결재 */
@Controller
public class ApDocTransCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocTransCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 문서 보안 서비스 */
	@Autowired
	private ApDocSecuSvc apDocSecuSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 문서 저장 서비스 */
	@Autowired
	private ApDocTransSvc apDocTransSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 포털 모바일 메세지 서비스 */
	@Autowired
	private PtMobilePushMsgSvc ptMobilePushMsgSvc;
	
	/** ERP 연계 알림 서비스 */
	@Autowired
	private ApErpNotiSvc apErpNotiSvc;
	
	/** 결재 - 연차관리 서비스 */
	@Autowired
	private ApWdSvc apWdSvc;
	
	/** [프레임] 결재저장 (상신, 임시저장, 결재, 반려, 찬성, 반대) */
	@RequestMapping(value = "/ap/box/transDoc")
	public String transDoc(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		try {
			apDocTransSvc.processTrans(request, queryQueue, locale, model);
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			
			StackTraceElement[] traces = e.getStackTrace();
			StringBuilder builder = new StringBuilder(2048);
			if(message!=null && !message.isEmpty()) builder.append(message).append('\n');
			builder.append(message).append('\n');
			builder.append("statCd : ").append(model.get("statCd")).append('\n');
			int traceCnt = 0;
			String trace;
			for(int i=1;i<traces.length;i++){
				trace = traces[i].toString();
				builder.append(trace).append('\n');
				if(++traceCnt > 12) break;
			}
			LOGGER.error(builder);
			
		}
		
		model.remove("storedApOngdBVo");
		return LayoutUtil.getResultJsp();
	}
	
	
	/** [프레임] 관리자 결재 저장 */
	@RequestMapping(value = "/ap/adm/box/transAdmDoc")
	public String transAdmDoc(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		try {
			apDocTransSvc.processAdmTrans(request, queryQueue, locale, model);
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			
			StackTraceElement[] traces = e.getStackTrace();
			StringBuilder builder = new StringBuilder(2048);
			if(message!=null && !message.isEmpty()) builder.append(message).append('\n');
			builder.append(message).append('\n');
			int traceCnt = 0;
			String trace;
			for(int i=1;i<traces.length;i++){
				trace = traces[i].toString();
				builder.append(trace).append('\n');
				if(++traceCnt > 12) break;
			}
			LOGGER.error(builder);
			
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJX] 결재 프로세스 처리 (기안취소, 승인취소, 합의취소) */
	@RequestMapping(value = {"/ap/box/transDocProcessAjx", "/ap/adm/box/transDocProcessAjx"})
	public String transDocProcessAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="menuId", required=false) String menuId,
//			@Parameter(name="bxId", required=false) String bxId,
			Locale locale, ModelMap model) throws Exception{
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		String process = (String)jsonObject.get("process");
		String apvNo = (String)jsonObject.get("apvNo");
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		StringBuilder formBodyXMLBuilder = new StringBuilder();
		
		// 메세지 큐 - 메신저 메세지 발송용
		List<Map<String, String>> messengerQueue = new ArrayList<Map<String, String>>();
		
		try {
			// 일괄배부, 일괄접수, 공람게시, 일괄 담당자지정(접수문서)
			if( !"processBulkDist".equals(process) && !"processBulkRecv".equals(process)
					&& !"setPubBx".equals(process) && !"setMakVwList".equals(process)
					&& !"delDocs".equals(process) && !"delDocPwByAdm".equals(process)
					&& !"changeSeculCdByAdm".equals(process) && !"sendToDm".equals(process)
					&& !"chngCls".equals(process) && !"chngPsnCls".equals(process)
					&& !"unread".equals(process) && !"read".equals(process)
					){
				// 결재번호 체크
				if(apvNo==null || apvNo.isEmpty()){
					LOGGER.error("Fail trans - "+process+" - no apvNo ! - userUid:"+userVo.getUserUid());
					//ap.msg.noApvNo=결재번호가 정확하지 않습니다.
					throw new CmException("ap.msg.noApvNo", locale);
				}
			}
			
			// 기안회수, 승인취소, 합의취소
			if("cancelMak".equals(process) || "cancelApv".equals(process) || "cancelAgr".equals(process)){
				String apvLnPno = (String)jsonObject.get("apvLnPno");
				String apvLnNo = (String)jsonObject.get("apvLnNo");
				apDocTransSvc.processCancel(queryQueue, apvNo, apvLnPno, apvLnNo, process, 
						userVo, locale, model);
				
			// 완결 취소
			} else if("cancelCmpl".equals(process)){
				String force = (String)jsonObject.get("force");
				apDocTransSvc.processCancelCmpl(request, queryQueue, apvNo, userVo,
						"Y".equals(force), locale, model);
				
			// 합의기안자 설정, 처리부서 담당자 지정
			} else if("setMakAgr".equals(process) || "setPrcDeptAgr".equals(process)){
				String apvLnPno = (String)jsonObject.get("apvLnPno");
				String apvLnNo = (String)jsonObject.get("apvLnNo");
				String apvrUid = (String)jsonObject.get("apvrUid");
				// 합의기안자 설정, 처리부서 담당자 지정
				apDocTransSvc.processPich(queryQueue, messengerQueue, apvNo, 
						apvLnPno, apvLnNo, process, apvrUid, 
						userVo, locale, model);
				
			// 심사
			} else if("processCensr".equals(process)){
				String censrStatCd = (String)jsonObject.get("censrStatCd");
				String pichOpinCont = (String)jsonObject.get("pichOpinCont");
				// 심사 처리
				apDocTransSvc.processCensr(queryQueue, apvNo, 
						censrStatCd, pichOpinCont, request, 
						userVo, locale, model);
				
			// 시행취소
			} else if("cnclEnfc".equals(process)){
				// 시행취소 처리
				apDocTransSvc.processCnclEnfc(queryQueue, apvNo, 
						userVo, locale, model);
				
			// 추가 발송 - [추가발송 팝업]
			} else if("sendMoreRecvDept".equals(process)){
				@SuppressWarnings("unchecked")
				List<String> recvDeptList = (List<String>)jsonObject.get("recvDeptList");
				// 추가발송 처리
				apDocTransSvc.sendMoreRecvDept(queryQueue, messengerQueue, apvNo, 
						recvDeptList, 
						userVo, locale, model);
				
			// 회수, 재발송, 수동완료처리 [접수확인 팝업]
			} else if("retrvDoc".equals(process) || "reSendDoc".equals(process) || "manlSendCmpl".equals(process)){
				@SuppressWarnings("unchecked")
				List<String> sendSeqs = (List<String>)jsonObject.get("sendSeqs");
				String hdlrNm = (String)jsonObject.get("hdlrNm");
				String hdlDt = (String)jsonObject.get("hdlDt");
				// 회수, 재발송, 수동완료처리 - [접수확인 팝업]
				apDocTransSvc.processCfrmRecv(queryQueue, messengerQueue, apvNo, 
						process, sendSeqs, hdlrNm, hdlDt,
						userVo, locale, model);
				
			// 반송
			} else if("retnDoc".equals(process)){
				String sendSeq = (String)jsonObject.get("sendSeq");
				String retnOpin = (String)jsonObject.get("retnOpin");
				// 반송 처리
				apDocTransSvc.processRetn(queryQueue, apvNo,
						sendSeq, retnOpin,
						userVo, locale, model);
				
			// 배부
			} else if("processDist".equals(process)){
				String sendSeq = (String)jsonObject.get("sendSeq");
				@SuppressWarnings("unchecked")
				List<JSONObject> distList = (List<JSONObject>)jsonObject.get("distList");
				// 배부 처리
				apDocTransSvc.processDist(queryQueue, apvNo,
						sendSeq, distList,
						userVo, locale, model);
				
			// 일괄배부
			} else if("processBulkDist".equals(process)){
				
				@SuppressWarnings("unchecked")
				List<JSONObject> distList = (List<JSONObject>)jsonObject.get("distList");
				@SuppressWarnings("unchecked")
				List<JSONObject> distDocList = (List<JSONObject>)jsonObject.get("distDocList");
				
				if(distDocList!=null){
					for(JSONObject distDoc : distDocList){
						// 배부 처리
						apDocTransSvc.processDist(queryQueue, (String)distDoc.get("apvNo"),
								(String)distDoc.get("sendSeq"), distList,
								userVo, locale, model);
					}
				}
				
			// 접수
			} else if("processRecv".equals(process)){
				String sendSeq = (String)jsonObject.get("sendSeq");//발송일련번호
				String clsInfoId = (String)jsonObject.get("clsInfoId");//분류정보ID
				String seculCd = (String)jsonObject.get("seculCd");//보안등급코드
//				String recvDeptId = (String)jsonObject.get("recvDeptId");//수신처ID
				
				// 대장문서 생성
				ApOngdBVo apOngdBVo = apDocTransSvc.createRecLstDoc(queryQueue, apvNo,
						sendSeq, "recvRecLst", clsInfoId, //regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
						userVo, locale, model);
				
				// 보안등급 반영
				apOngdBVo.setSeculCd(seculCd);
//				// 대장부서ID
//				if(recvDeptId!=null && !recvDeptId.isEmpty()){
//					apOngdBVo.setRecLstDeptId(recvDeptId);
//				}
				
				// 접수대장 - 검색 인덱싱
				apDocTransSvc.addSrchIndex(apOngdBVo, userVo, queryQueue);
				
				// 접수대장의 조회로 전환
				String paramMenuId = apBxSvc.getMenuIdByBxId(userVo, "recvRecLst");
				if(menuId != null){// 권한이 있는 경우 - menuId 가 넘어옴
					model.put("url", "/ap/box/setDoc.do?menuId="+paramMenuId+"&apvNo="+apOngdBVo.getApvNo()+"&bxId=recvRecLst");
				}
				
			// 일괄접수
			} else if("processBulkRecv".equals(process)){
				String clsInfoId = (String)jsonObject.get("clsInfoId");//분류정보ID
				String seculCd = (String)jsonObject.get("seculCd");//보안등급코드
//				String recvDeptId = (String)jsonObject.get("recvDeptId");//수신처ID
				@SuppressWarnings("unchecked")
				List<JSONObject> recvDocList = (List<JSONObject>)jsonObject.get("recvDocList");
				
				if(recvDocList!=null){
					ApOngdBVo apOngdBVo;
					for(JSONObject recvDoc : recvDocList){
						// 대장문서 생성
						apOngdBVo = apDocTransSvc.createRecLstDoc(queryQueue, (String)recvDoc.get("apvNo"),
								(String)recvDoc.get("sendSeq"), "recvRecLst", clsInfoId, //regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
								userVo, locale, model);
						
						// 보안등급 반영
						apOngdBVo.setSeculCd(seculCd);
						
						// 접수대장 - 검색 인덱싱
						apDocTransSvc.addSrchIndex(apOngdBVo, userVo, queryQueue);
					}
				}
				
			// 접수문서 담당 지정
			} else if("setMakVw".equals(process)){
				String apvrUid = (String)jsonObject.get("apvrUid");
				// 접수문서 담당 지정
				apDocTransSvc.processSetMakVw(queryQueue, messengerQueue, apvNo, apvrUid, 
						userVo, locale, model);
				
			// 접수문서 담당 지정 - 일괄
			} else if("setMakVwList".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String apvrUid = (String)jsonObject.get("apvrUid");
				if(apvNos != null && !apvNos.isEmpty()){
					for(String makVwApvNo : apvNos.split(",")){
						// 접수문서 담당 지정
						apDocTransSvc.processSetMakVw(queryQueue, messengerQueue, makVwApvNo, apvrUid, 
								userVo, locale, model);
					}
				}
				
			// 공람게시
			} else if("setPubBx".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String pubBxDeptId = (String)jsonObject.get("pubBxDeptId");
				String pubBxEndYmd = (String)jsonObject.get("pubBxEndYmd");
				String regDeptId = (String)jsonObject.get("regDeptId");
				String langTypCd = LoginSession.getLangTypCd(request);
				// 공람게시
				apDocTransSvc.processSetPubBx(queryQueue, apvNos,
						pubBxDeptId, pubBxEndYmd, regDeptId, langTypCd,
						userVo, locale, model);
				
			// 공람완료
			} else if("setCmplVw".equals(process)){
				// 공람완료
				apDocTransSvc.setCmplVw(queryQueue, apvNo,
						userVo, locale, model);
				
			// 일괄결재 - 브라우저에서 한껀씩 나누어 submit 함
			} else if("processBulkApv".equals(process)){
				
				String apvLnPno = (String)jsonObject.get("apvLnPno");
				String apvLnNo = (String)jsonObject.get("apvLnNo");
				String secuId = (String)jsonObject.get("secuId");
				// 보안ID(결재비밀번호) 체크 
				apDocTransSvc.checkSecuId(request, secuId, apvNo, userVo);
				// 일괄결재 처리
				apDocTransSvc.processBulkApv(queryQueue, messengerQueue, apvNo, apvLnPno, apvLnNo,
						menuId, formBodyXMLBuilder,
						userVo, locale, model, request);
				// 보안ID 생성
				model.put("secuId", apDocSecuSvc.createSecuId(request.getSession()));
				
			// 삭제
			} else if("delDocs".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				// 삭제
				apDocTransSvc.processDelDocs(queryQueue, apvNos, userVo, locale, model);
				
			// 대장등록, 비밀번호 해제(삭제)
			} else if("regRegRecLst".equals(process) || "delDocPw".equals(process)){
				// 대장등록, 비밀번호 해제(삭제) 
				apDocTransSvc.processDocByOwner(process, queryQueue, apvNo, userVo, locale);
				
			// [관리자] 비밀번호 해제(삭제), 보안등급 변경
			} else if("delDocPwByAdm".equals(process) || "changeSeculCdByAdm".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String seculCd = (String)jsonObject.get("seculCd");
				// 비밀번호 해제(삭제), 보안등급 변경
				apDocTransSvc.processDocByAdmin(process, queryQueue, apvNos, seculCd, userVo, locale);
				
			// 문서관리 보내기
			} else if("sendToDm".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String bxId = (String)jsonObject.get("bxId");
				String dmData = (String)jsonObject.get("dmData");
				
				apDocTransSvc.processSendToDm(queryQueue, apvNos, bxId, dmData, userVo, locale, model, request);
				
			// 분류변경
			} else if("chngCls".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String clsInfoId = (String)jsonObject.get("clsInfoId");
				apDocTransSvc.processChngCls(queryQueue, apvNos, clsInfoId, model, request);
				
			// 개인분류변경 - 기안함 - 옵션으로 보임
			} else if("chngPsnCls".equals(process)){
				String apvNos = (String)jsonObject.get("apvNos");
				String psnClsInfoId = (String)jsonObject.get("psnClsInfoId");
				apDocTransSvc.processPsnChngCls(queryQueue, apvNos, psnClsInfoId, model, request);
				
			// 통보추가
			} else if("addInfm".equals(process)){
				
				// 저장소 조회
				String storage = apCmSvc.queryStorage(apvNo);
				if(storage!=null && !storage.isEmpty() && !"ONGO".equals(storage)){
					//ap.msg.notSupportTran=이관된 문서에 대해서는 지원하지 않습니다.
					model.put("message", messageProperties.getMessage("ap.msg.notSupportTran", locale));
				} else {
					String apvrUids = (String)jsonObject.get("apvrUids");
					apDocTransSvc.processAddInfm(queryQueue, messengerQueue, apvNo, apvrUids, userVo, model, locale);
				}
			// 시스템 반려
			} else if("forceRejt".equals(process)){
				apDocTransSvc.forceRejt(queryQueue, apvNo, model, locale);
				
			// 참조열람 - 열람자 저장
			} else if("setRefVw".equals(process)){
				
				@SuppressWarnings("unchecked")
				List<String> refVwList = (List<String>)jsonObject.get("refVwList");
				
				// 참조열람 - 열람자 저장
				apDocTransSvc.transRefVw(refVwList, apvNo, queryQueue, messengerQueue, userVo, model, locale);
				
			// 참조열람 - 열람확인
			} else if("processRefVw".equals(process)){
				
				String refVwOpinCont = (String)jsonObject.get("refVwOpinCont");
				
				apDocTransSvc.processRefVw(apvNo, refVwOpinCont, queryQueue, userVo, model, locale);
				
			// 추가검토 - 자신앞에 결재자 삽입
			} else if("setAdiRevwr".equals(process)){
				
				apDocTransSvc.processAdiRevw(jsonObject, queryQueue, messengerQueue, userVo, model, locale);
				
			// 되돌리기 - 관리자용
			} else if("turnBackDoc".equals(process)){
				
				apDocTransSvc.turnBackDoc(jsonObject, queryQueue, messengerQueue, userVo, model, locale);
				
			// 의견 저장
			} else if("processSaveOpin".equals(process)){
				
				apDocTransSvc.processSaveOpin(jsonObject, queryQueue, messengerQueue, userVo, model, locale);
				
			// 읽지않음, 읽음 - 처리
			} else if("unread".equals(process) || "read".equals(process)){
				
				// 읽지않음 처리(읽음처리)
				apDocTransSvc.processUnread(jsonObject, queryQueue, userVo, model, locale);
				
			} else {
				//LOGGER.error("Fail trans - no apvNo ! - userUid:"+userVo.getUserUid());
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException("cm.msg.notValidCall", locale);
			}
			
			// 푸쉬용 메세지 데이터 저장
			List<PtPushMsgDVo> ptPushMsgDVoList = new ArrayList<PtPushMsgDVo>();
			if(!messengerQueue.isEmpty()){
				// 결재 옵션
				Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
				apDocTransSvc.addPushMsg(messengerQueue, queryQueue, ptPushMsgDVoList, optConfigMap);
			}
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				model.put("result", "ok");
			}
			
			// 메신저 전송
			if(!messengerQueue.isEmpty()){
				// 결재 옵션
				Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
				apDocTransSvc.sendMesseger(messengerQueue, optConfigMap);
			}
			
			// 푸쉬 전송
			if(!ptPushMsgDVoList.isEmpty()){
				// 모바일 푸쉬 메세지 - 보내기
				ptMobilePushMsgSvc.sendMobilePush(ptPushMsgDVoList);
			}
			
			// 결재 알림 처리 - [결재에서 시작 - ERP 통보]
			if(formBodyXMLBuilder.length()>0){
				apErpNotiSvc.sendErpNotiFromAp(userVo, apvNo, formBodyXMLBuilder.toString());
			}
			
			// 자동 발송
			if(request.getAttribute("needAutoSend") != null){
				apDocTransSvc.processAutoSendDoc(apvNo, userVo, locale, model);
			}
			
			
			// 연차
			@SuppressWarnings("unchecked")
			Map<String, String> wdNotiMap = (Map<String, String>)model.get("wdNotiMap");
			if(wdNotiMap != null){
				apWdSvc.sendWdNotiFromAp(
						wdNotiMap.get("apvNo"),
						wdNotiMap.get("docSubj"),
						wdNotiMap.get("statCd"),
						userVo,
						wdNotiMap.get("formBodyXML"));
			}
			
			String msgActionId = null;
			// 심사
			if("processCensr".equals(process)){
				msgActionId = "apvd".equals(jsonObject.get("censrStatCd")) ? "#ap.btn.censrApvd" : "#ap.btn.censrRejt";
			// 발송 - 추가발송
			} else if("sendMoreRecvDept".equals(process)){
				msgActionId = "#ap.btn.sendDoc";
			// 반송
			} else if("retnDoc".equals(process)){
				msgActionId = "#ap.btn.retn";
			// 접수
			} else if("processRecv".equals(process)){
				msgActionId = "#ap.btn.recv";
			// 배부
			} else if("processDist".equals(process)){
				msgActionId = "#ap.btn.dist";
			// 공람게시
			} else if("setPubBx".equals(process)){
				msgActionId = "#ap.btn.regPubBx";
			// 공람완료
			} else if("setCmplVw".equals(process)){
				msgActionId = "#ap.btn.cmplVw";
			// 담당자지정 - 일괄
			} else if("setMakVwList".equals(process)){
				msgActionId = "#ap.btn.setPich";
			// 대장등록
			} else if("regRegRecLst".equals(process)){
				msgActionId = "#ap.btn.regRegRecLst";
			// 비밀번호 해제
			} else if("delDocPw".equals(process) || "delDocPwByAdm".equals(process)){
				msgActionId = "#ap.btn.delDocPw";
			// 보안등급 변경
			} else if("changeSeculCdByAdm".equals(process)){
				//cm.msg.modify.success=변경 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.modify.success", locale));
			// 문서관리 보내기
			} else if("sendToDm".equals(process)){
				if(!queryQueue.isEmpty()){
					msgActionId = "#ap.btn.saveToDm";
				} else {
					//ap.msg.noDataToSave=저장할 데이터가 없습니다.
					model.put("message", messageProperties.getMessage("ap.msg.noDataToSave", locale));
				}
			// 분류변경
			} else if("sendToDm".equals(process)){
				msgActionId = "#ap.btn.chngCls";
			// 시스템 반려
			} else if("forceRejt".equals(process)){
				msgActionId = "#ap.btn.forceRejt";
			// 참조열람 - 열람자 저장
			} else if("setRefVw".equals(process)){
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", locale));
			// 참조열람 - 열람확인
			} else if("processRefVw".equals(process)){
				msgActionId = "#ap.term.cfrmRefVw";
				//msgActionId = TermUtil.getTerm("ap.term.cfrmRefVw", locale);
			} else if("turnBackDoc".equals(process)){
				msgActionId = "#cm.btn.turnBack";
			} else if("unread".equals(process)){
				msgActionId = "#ap.cfg.unreadMark";
			} else if("read".equals(process)){
				msgActionId = "#ap.cfg.readMark";
			} else if("cancelCmpl".equals(process)){
				if(!queryQueue.isEmpty()){
					msgActionId = "#ap.btn.cancelCmpl";
				} else {
					msgActionId = null;
				}
			}
			
			//ap.trans.processOk={0} 처리 하였습니다.
			
			if(msgActionId != null){
				// ap.trans.submitOk={0} 하였습니다.
				model.put("message", messageProperties.getMessage("ap.trans.submitOk", new String[]{msgActionId}, locale));
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
			if("processBulkRecv".equals(process)) model.put("statNm", messageProperties.getMessage("cm.msg.error", locale));
		} catch(Exception e){
			e.printStackTrace();
			
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			StackTraceElement[] traces = e.getStackTrace();
			LOGGER.error(e.getClass().getCanonicalName()
					+(traces!=null && traces[0]!=null ? "\n"+traces[0].toString() : "")
					+(message!=null && !message.isEmpty() ? "\n"+message : "")
					+"\n"+queryQueue.getDebugString());
			if("processBulkRecv".equals(process)) model.put("statNm", messageProperties.getMessage("cm.msg.error", locale));
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 등록대장에 예약 등록된 문서를 등록함 - 0초 3분 0시 매일 매월 년/주 */
	@Scheduled(cron="0 3 0 * * ?")
	public void regScheduledDoc() throws SQLException{
		
		// 저장소 목록
		ApStorBVo apStorBVo = new ApStorBVo();
//		apStorBVo.setQueryLang(langTypCd);
		apStorBVo.setOrderBy("STOR_ID DESC");
		@SuppressWarnings("unchecked")
		List<ApStorBVo> apStorBVoList = (List<ApStorBVo>)commonSvc.queryList(apStorBVo);
		
		int i, size = apStorBVoList==null ? 0 : apStorBVoList.size();
		String[] storIds = new String[size+1];
		storIds[0] = null;
		for(i=0;i<size;i++){
			storIds[i+1] = apStorBVoList.get(i).getStorId();
		}
		
		
		UserVo userVo;
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setRegRecLstRegYn("N");
		apOngdBVo.setRegRecLstRegSkedYmd(StringUtil.getCurrYmd());//WAS의 시간 세팅함 - WAS 기준으로 스케쥴링이 되기 때문
		
		QueryQueue queryQueue = new QueryQueue();
		
		for(String storage : storIds){
			@SuppressWarnings("unchecked")
			List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
			
			if(apOngdBVoList != null){
				for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
					apOngdBVo = new ApOngdBVo();
					apOngdBVo.setApvNo(storedApOngdBVo.getApvNo());
					apOngdBVo.setStorage(storage);
					apOngdBVo.setRegRecLstRegYn("Y");
					apOngdBVo.setRegRecLstRegSkedYmd("");
					queryQueue.update(apOngdBVo);
					
					// 검색 인덱싱 추가
					userVo = new UserVo();
					userVo.setCompId(storedApOngdBVo.getCompId());
					userVo.setLoutCatId("B");//하드코딩 - 달라지는것 없음
					userVo.setLangTypCd(storedApOngdBVo.getDocLangTypCd());
					apDocTransSvc.addSrchIndex(storedApOngdBVo, userVo, queryQueue);
				}
			}
		}
		
		if(!queryQueue.isEmpty()){
			commonSvc.execute(queryQueue);
		}
	}
	
}
