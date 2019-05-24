package com.innobiz.orange.mobile.ap.ctrl;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.ap.cust.ApErpNotiSvc;
import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocSecuSvc;
import com.innobiz.orange.web.ap.svc.ApDocTransSvc;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 문서 저장 컨트롤러 - 결재 */
@Controller
public class MoApDocTransCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoApDocTransCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 문서 보안 서비스 */
	@Autowired
	private ApDocSecuSvc apDocSecuSvc;
	
	/** 결재 함 서비스 */
	@Autowired
	private ApBxSvc apBxSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 문서 저장 서비스 */
	@Autowired
	private ApDocTransSvc apDocTransSvc;
	
	/** ERP 연계 알림 서비스 */
	@Autowired
	private ApErpNotiSvc apErpNotiSvc;
	
	/** [프레임] 결재저장 (상신, 임시저장, 결재, 반려, 찬성, 반대) */
	@RequestMapping(value = "/ap/box/transDocPost")
	public String transDoc(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		try {
			apDocTransSvc.processTrans(request, queryQueue, locale, model);
			model.put("todo", "$m.nav.prev(null, true);");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			StackTraceElement[] traces = e.getStackTrace();
			LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+traces[0].toString()
					+(traces.length>2 ? "\n"+traces[1].toString() : "")
					+(traces.length>3 ? "\n"+traces[2].toString() : "")
					+(traces.length>4 ? "\n"+traces[3].toString() : "")
					+(message==null || message.isEmpty() ? "" : "\n"+message)
					+"\nstatCd - "+model.get("statCd")
					+"\n\n####### APV TRANS - LOG S #######"
					+"\n"+queryQueue.getDebugString()
					+"####### APV TRANS - LOG E #######\n");
		}
		return MoLayoutUtil.getResultJsp();
	}
	
	/** [AJX] 결재 프로세스 처리 (기안취소, 승인취소, 합의취소) */
	@RequestMapping(value = "/ap/box/transDocProcessAjx")
	public String transDocProcessAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			@Parameter(name="menuId", required=false) String menuId,
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
					&& !"delDocs".equals(process)){
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
				// 대장문서 생성
				ApOngdBVo apOngdBVo = apDocTransSvc.createRecLstDoc(queryQueue, apvNo,
						sendSeq, "recvRecLst", clsInfoId, //regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
						userVo, locale, model);
				
				// 접수대장의 조회로 전환
				String paramMenuId = apBxSvc.getMenuIdByBxId(userVo, "recvRecLst");
				if(menuId != null){// 권한이 있는 경우 - menuId 가 넘어옴
					model.put("url", "/ap/box/setDoc.do?menuId="+paramMenuId+"&apvNo="+apOngdBVo.getApvNo()+"&bxId=recvRecLst");
				}
				
			// 일괄접수
			} else if("processBulkRecv".equals(process)){
				String clsInfoId = (String)jsonObject.get("clsInfoId");//분류정보ID
				@SuppressWarnings("unchecked")
				List<JSONObject> recvDocList = (List<JSONObject>)jsonObject.get("recvDocList");
				
				if(recvDocList!=null){
					for(JSONObject recvDoc : recvDocList){
						// 대장문서 생성
						apDocTransSvc.createRecLstDoc(queryQueue, (String)recvDoc.get("apvNo"),
								(String)recvDoc.get("sendSeq"), "recvRecLst", clsInfoId, //regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
								userVo, locale, model);
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
				
			// 참조열람 - 열람확인
			} else if("processRefVw".equals(process)){
				
				String refVwOpinCont = (String)jsonObject.get("refVwOpinCont");
				
				apDocTransSvc.processRefVw(apvNo, refVwOpinCont, queryQueue, userVo, model, locale);
				
			} else {
				//LOGGER.error("Fail trans - no apvNo ! - userUid:"+userVo.getUserUid());
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				throw new CmException("cm.msg.notValidCall", locale);
			}
			
			commonSvc.execute(queryQueue);
			model.put("result", "ok");
			
			// 결재 옵션
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
			
			// 메신저 메세지 일괄 발송
			apDocTransSvc.sendMesseger(messengerQueue, optConfigMap);
			
			// 결재 알림 처리 - [결재에서 시작 - ERP 통보]
			if(formBodyXMLBuilder.length()>0){
				apErpNotiSvc.sendErpNotiFromAp(userVo, apvNo, formBodyXMLBuilder.toString());
			}
			
			// 자동 발송
			if(request.getAttribute("needAutoSend") != null){
				apDocTransSvc.processAutoSendDoc(apvNo, userVo, locale, model);
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
			} else if("delDocPw".equals(process)){
				msgActionId = "#ap.btn.delDocPw";
			// 참조열람 - 열람확인
			} else if("processRefVw".equals(process)){
				msgActionId = "#ap.term.cfrmRefVw";
//				msgActionId = TermUtil.getTerm("ap.term.cfrmRefVw", locale);
			}
			if(msgActionId != null){
				// ap.trans.submitOk={0} 하였습니다.
				model.put("message", messageProperties.getMessage("ap.trans.submitOk", new String[]{msgActionId},  locale));
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
			if("processBulkRecv".equals(process)) model.put("statNm", messageProperties.getMessage("cm.msg.error", locale));
		} catch(Exception e){
			String message = e.getMessage();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
			LOGGER.error(e.getClass().getCanonicalName()
					+"\n"+e.getStackTrace()[0].toString()
					+(message==null || message.isEmpty() ? "" : "\n"+message)
					+"\n"+queryQueue.getDebugString());
			e.printStackTrace();
			if("processBulkRecv".equals(process)) model.put("statNm", messageProperties.getMessage("cm.msg.error", locale));
		}
		
		return LayoutUtil.returnJson(model);
	}
	
}
