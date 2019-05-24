package com.innobiz.orange.web.dm.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocNoSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmTaskSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmBumkBVo;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmClsRVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFileDVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Controller
public class DmDocSendCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmDocSendCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 게시물사진 서비스 */
	@Resource(name = "bbBullPhotoSvc")
	private BbBullPhotoSvc bbBullPhotoSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "dmDocNoSvc")
	private DmDocNoSvc dmDocNoSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 작업이력 서비스 */
	@Resource(name = "dmTaskSvc")
	private DmTaskSvc dmTaskSvc;
	
	/** [팝업] 보내기 */
	@RequestMapping(value = {"/dm/doc/sendPop","/dm/adm/doc/sendPop","/dm/doc/sendPsnPop","/dm/adm/doc/sendPsnPop"})
	public String sendPop(HttpServletRequest request,
			@RequestParam(value = "mode", required = false) String mode,
			@RequestParam(value = "multi", required = false) String multi,
			ModelMap model) throws Exception {
		
		// 저장소ID(이관문서에서 저장소ID가 있을경우)
		String paramStorId = ParamUtil.getRequestParam(request, "paramStorId", false);
		
		if(paramStorId != null && !paramStorId.isEmpty()) model.put("paramStorId", paramStorId);
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		// 개인문서여부
		boolean isPsn = "sendPsnPop".equals(path);
					
		if(mode != null && !mode.isEmpty() && "move".equals(mode)){
			if(isPsn)	model.put("tabList", new String[]{"psn"});
			else model.put("tabList", new String[]{"doc"});
		}else{
			if(isPsn){// 개인문서 [폴더,게시판]
				if(multi != null && "Y".equals(multi)) model.put("tabList", new String[]{"doc"});
				else model.put("tabList", new String[]{"doc","brd"});
				model.put("isPsn",Boolean.TRUE);
				model.put("isAdm",Boolean.FALSE);
			}else{
				// 관리자여부
				boolean isAdm = request.getRequestURI().startsWith("/dm/adm/");
				// 관리자 [폴더,게시판]
				if(isAdm){
					if(multi != null && "Y".equals(multi)) model.put("tabList", new String[]{"doc"});
					else model.put("tabList", new String[]{"doc","brd"});
					model.put("isAdm",Boolean.TRUE);
					model.put("isPsn",Boolean.FALSE);
				}else{// 공용문서 [폴더,개인폴더,게시판]
					if(multi != null && "Y".equals(multi)) model.put("tabList", new String[]{"doc"});
					else {
						String refTyp = ParamUtil.getRequestParam(request, "refTyp", false);
						if(refTyp != null && "ap".equals(refTyp)) model.put("tabList", new String[]{"doc"});
						else model.put("tabList", new String[]{"doc","psn","brd"});
					}
					model.put("isPsn",Boolean.FALSE);
					model.put("isAdm",Boolean.FALSE);
				}
			}
			
		}
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId","paramStorId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/dm/doc/sendPop");
	}
	
	/** [팝업] 보내기 옵션 - 문서 */
	@RequestMapping(value = {"/dm/doc/setSendOptPop","/dm/adm/doc/setSendOptPop","/cm/doc/setSendOptPop"})
	public String setSendOptPop(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "selId", required = false) String selId,
			@RequestParam(value = "ownrUid", required = false) String ownrUid,
			ModelMap model) throws Exception {
		
		 // 시스템 정책 조회
		 Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		  
		 // 문서관리 - 사용여부
		 if(sysPlocMap == null || !sysPlocMap.containsKey("dmEnable") || "N".equals(sysPlocMap.get("dmEnable"))){
		     //문서보내기 서비스를 제공하지 않습니다.
		     String message = messageProperties.getMessage("dm.msg.not.send.service", request);
			 LOGGER.error(message+" : setSendOptPop()");
			 //model.put("message", message);
			 throw new CmException(message);
			 //return LayoutUtil.getResultJsp();
			 //model.put("dmEnable", Boolean.TRUE);
		 }
		  
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String tableName = dmStorBVo.getTblNm();
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 항목 정보 조회
		List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, null, null, userVo.getCompId(), null, false);
		model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
		
		// 폴더ID(결재 등 에서 넘어오는 경우
		String fldId = ParamUtil.getRequestParam(request, "fldId", false);
		if(fldId != null && !fldId.isEmpty()) selId = fldId;			
		// 폴더 정보 맵핑
		if(selId != null && !selId.isEmpty()){
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setFldId(selId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			if(dmFldBVo != null){
				model.put("fldId", dmFldBVo.getFldId());
				model.put("fldNm", dmFldBVo.getFldNm());
			}
		}
		
		// 소유자UID가 있을 경우 정보를 세팅한다.
		if(ownrUid != null && !ownrUid.isEmpty()){
			OrUserBVo orUserBVo = dmAdmSvc.getOrUserBVo(ownrUid, langTypCd);
			if(orUserBVo != null) {
				model.put("ownrUid", orUserBVo.getUserUid());
				model.put("ownrNm", orUserBVo.getUserNm());
			}
			
		}
		// 분류체계 
		dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, null, null, tableName, langTypCd);
		
		//if(model.containsKey(key))
		// get 파라미터를 post 파라미터로 전달하기 위해
		//model.put("paramEntryList", ParamUtil.getEntryMapList(request, "data"));
					
		return LayoutUtil.getJspPath("/dm/doc/setSendOptPop");
	}
	
	/** 문서 보내기 , 이동 - 빠른, 옵션 */
	@RequestMapping(value = {"/dm/doc/transSendDoc","/dm/adm/doc/transSendDoc"})
	public String transSendDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "docTyp", required = false) String docTyp,
			@RequestParam(value = "selId", required = false) String selId,
			@RequestParam(value = "tabId", required = false) String tabId,
			@RequestParam(value = "mode", required = false) String mode,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {

		try {
			if (docId == null || docId.isEmpty() || mode == null || mode.isEmpty() || tabId == null || tabId.isEmpty()) {
				LOGGER.error("[ERROR] docId == null || docId.isEmpty() || mode == null || mode.isEmpty() || tabId == null || tabId.isEmpty()");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			
			String storId = dmStorBVo.getStorId();
			// 테이블명
			String tableName = dmStorBVo.getTblNm();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 파일 복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
			
			boolean isPsn = "psn".equals(docTyp);
			
			// 하위문서그룹ID + '|' + 폴더ID 조합키
			//String comKey = null;
			boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
			
			// 복사 또는 이동할 문서ID 배열
			String[] docIds = docId.split(",");
			
			if("doc".equals(tabId)){
				//미분류로의 복사 이동은 안됨
				if(selId != null && !selId.isEmpty() && DmConstant.EMPTY_CLS.equals(selId)){
					// dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.
					throw new CmException("dm.msg.not.save.emptyCls", request);
				}
			}
			
			// 이메일을 발송할 문서 목록
			List<DmDocLVo> emailList = null;
			// 원본, 복사대상 모두 공용문서일 경우
			if(!isPsn && "doc".equals(tabId)){
				// 저장소ID(이관문서에서 저장소ID가 있을경우)
				String paramStorId = ParamUtil.getRequestParam(request, "paramStorId", false);
				
				DmDocLVo tmpOriginVo = null;
				// 정렬할 문서그룹ID 목록
				List<String> docGrpIdList = new ArrayList<String>();
				// 복사할 원본 목록맵
				Map<String,DmDocLVo> originVoListMap = new HashMap<String,DmDocLVo>();
				for(String id : docIds){
					tmpOriginVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, id, null, true);
					if(tmpOriginVo == null) continue;
					//originVoList.add(tmpOriginVo);
					docGrpIdList.add(tmpOriginVo.getDocGrpId());
					originVoListMap.put(tmpOriginVo.getDocGrpId(), tmpOriginVo);
				}
				if(docGrpIdList.size()>0 && originVoListMap.size()>0 && docGrpIdList.size() == originVoListMap.size()){
					dmDocSvc.copyDoc(request, queryQueue, langTypCd, dmStorBVo, userVo, storId, tableName, mode, selId, copyFileList, originVoListMap, docGrpIdList, paramStorId);
					
					emailList = new ArrayList<DmDocLVo>();
					Entry<String, DmDocLVo> entry;
					Iterator<Entry<String, DmDocLVo>> iterator = originVoListMap.entrySet().iterator();
					while(iterator.hasNext()){
						entry = iterator.next();
						emailList.add(entry.getValue());
					}
				}
			}else{
				DmDocLVo originVo = null;
				Integer sortOrder = null;
				emailList = new ArrayList<DmDocLVo>();
				// 작업이력 목록
				//List<DmTaskHVo> dmTaskHVoList = new ArrayList<DmTaskHVo>();
				for(String id : docIds){
					originVo = dmDocSvc.getDmDocLVo(langTypCd, isPsn ? null : dmStorBVo, id, null, true);
					if(originVo == null) continue;
					// 복사
					dmDocSvc.copyDoc(request, queryQueue, langTypCd, dmStorBVo, userVo, storId, tableName, originVo, mode, tabId, docTyp, selId, isPsn, copyFileList, sortOrder);
					if("doc".equals(tabId)){
						if(sortOrder == null && originVo.getSortOrdr() != null && Integer.parseInt(originVo.getSortOrdr()) > 0) sortOrder = Integer.parseInt(originVo.getSortOrdr())+1;
						// 작업 이력저장[복사본] - 복사일 경우에만 저장
						if(!admUriChk && "copy".equals(mode) && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), mode)){
							dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, originVo.getDocGrpId(), userVo.getUserUid(), mode, null);
						}
						emailList.add(originVo);
					}
				}
			}
			
			commonSvc.execute(queryQueue);
			
			// 이메일 발송
			if("doc".equals(tabId) && emailList != null && emailList.size()>0){
				for(DmDocLVo storedDmDocLVo : emailList){
					if(storedDmDocLVo.getStatCd() == null || !"S".equals(storedDmDocLVo.getStatCd())) continue;
					dmDocSvc.sendDiscDocEmail(request, langTypCd, storedDmDocLVo.getSubj(), "S", storedDmDocLVo.getDiscrUid(), userVo, null, null, "disc");
				}
			}
			// 전체 하위문서그룹ID에서 중복을 제거 
			/*if(addDocVoMap.size()>0){
				// 문서상세 초기화
				DmDocDVo dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				
				// 최상위 문서그룹ID 맵
				Map<String,String> topDocMap = new HashMap<String,String>();
				// 관련문서 전체 목록
				List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
				// 관련 문서맵
				Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
				dmDocSvc.setSubDocGrpSortOrdrs(dmDocDVo, addDocVoMap, topDocMap, subGrpListMap, subGrpList);
				if(subGrpListMap.size()>0)
					dmDocSvc.updateSortOrdrs(queryQueue, topDocMap, subGrpList, subGrpListMap, "C", tableName);
				
			}*/
			
			// 파일 복사
			if(copyFileList.size()>0){
				dmFileSvc.copyFileList(request, tabId, copyFileList);
			}
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			if(dialog == null || dialog.isEmpty()) dialog = "";
			
			String multi = ParamUtil.getRequestParam(request, "multi", false);
			if(multi != null && !multi.isEmpty() && "Y".equals(multi)){
				model.put("todo", "parent.reloadDocFrm(null,'all');");
			}else{
				if("move".equals(mode)){
					if("doc".equals(tabId)){
						String listPage = ParamUtil.getRequestParam(request, "listPage", true);
						model.put("todo", "parent.reloadDocFrm('" + listPage + "','all');");
					}else{
						String menuId = ParamUtil.getRequestParam(request, "menuId", true);
						model.put("todo", "parent.reloadDocFrm('./viewDoc.do?menuId="+menuId+"&docId="+docId+"','"+dialog+"');");
					}
				}else{
					if(dialog != null && !dialog.isEmpty()) model.put("todo", "parent.dialog.close('" + dialog + "');");
				}
			}
			//	model.put("todo", "parent.dialog.close('" + dialog + "');");
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 문서 보내기 - 공용,개인문서 */
	@RequestMapping(value = {"/dm/doc/transSendDoc2","/dm/adm/doc/transSendDoc2","/cm/doc/transSendDoc2"},
			method = RequestMethod.POST)
	public String transSendDoc2(HttpServletRequest request,
			ModelMap model) {
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = dmFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = dmCmSvc.getRequestPath(request, model , "Doc");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			
			// 문서를 보낼 대상구분ID[공용,개인,게시판]
			String tabId = ParamUtil.getRequestParam(request, "tabId", true);
			
			// 문서구분[공용,개인,게시판]
			String docTyp = ParamUtil.getRequestParam(request, "docTyp", true);
			
			// 팝업ID
			String dialog = ParamUtil.getRequestParam(request, "dialog", true);
			
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 원본 문서Vo
			DmDocLVo originDmDocLVo = null;
			
			if(!"brd".equals(docTyp)){
				// 문서ID
				String docRefId = ParamUtil.getRequestParam(request, "docRefId", true);
				
				// 원본문서유형구분[공용문서,개인문서,게시판]
				boolean isPsn = "psn".equals(docTyp);
				
				// 상세정보 조회
				originDmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, isPsn ? null : dmStorBVo, docRefId, null);
				if(originDmDocLVo == null){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				// 공용문서일 경우 권한체크
				if(!isPsn){
					if("doc".equals(tabId)){
						if(docPid != null && !docPid.isEmpty()){
							// 하위문서로 보내기[보낼 문서중에 상위문서가 있는지 체크]
							if(dmDocSvc.eqChkId(docPid, originDmDocLVo.getDocGrpId())){
								// dm.msg.not.save.duplParent=동일한 상위문서로 저장할 수 없습니다.
								throw new CmException("dm.msg.not.save.duplParent", request);
							}
							// 보낼 문서와 상위문서가 같은지 비교
							if(originDmDocLVo.getDocPid() != null && "Y".equals(originDmDocLVo.getSubYn()) && dmDocSvc.eqChkId(originDmDocLVo.getDocPid(), docPid)){
								// dm.msg.not.save.duplParent=동일한 상위문서로 저장할 수 없습니다.
								throw new CmException("dm.msg.not.save.duplParent", request);
							}
						}
						// 부모문서ID
						String fldId = ParamUtil.getRequestParam(request, "fldId", true);
						if(!DmConstant.EMPTY_CLS.equals(originDmDocLVo.getFldId()) && dmDocSvc.eqChkId(originDmDocLVo.getFldId(), fldId)){
							// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.
							throw new CmException("dm.msg.not.save.duplFld", request);
						}
					}
					//상태코드 체크
					dmDocSvc.chkDocStatCd(request, "update", originDmDocLVo.getStatCd());
					/** [권한] Start */
					boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
					Map<String,String> authMap = dmDocSvc.getAuthMap(null, admUriChk, originDmDocLVo, userVo, storId, langTypCd, path, false);
					// 권한체크 제외 URL
					boolean isUrlChk = dmDocSvc.chkNotAuth(path);
					if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, originDmDocLVo, dmDocSvc.chkDocAuth(authMap, "send", path))){
						LOGGER.error("[ERROR] User Auth Check Fail - request path : ./transDoc.do \tuserUid : "+userVo.getUserUid()+"\tauth : update");
						//cm.msg.noAuth=권한이 없습니다.
						String message = messageProperties.getMessage("cm.msg.noAuth", request);
						model.put("message", message);
						return LayoutUtil.getResultJsp();
					}
					/** [권한] End */
					// 작업 이력저장[사용자]
					if(!admUriChk && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "copy")){
						dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, originDmDocLVo.getDocGrpId(), userVo.getUserUid(), "copy", null);
					}
				}
			}
			
			// 문서VO
			DmDocLVo dmDocLVo = null;
			
			if("doc".equals(tabId)){
				// 하위문서여부
				boolean isSub = docPid != null && !docPid.isEmpty() ? true : false;
				
				// 파일복사 목록
				List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
				// 저장[기본정보]
				dmDocLVo = dmDocSvc.saveDoc(request, queryQueue, dmStorBVo, null, userVo, null, isSub, copyFileList);
				
				if(dmDocLVo.getFldId() != null && !dmDocLVo.getFldId().isEmpty() && ( dmDocLVo.getStatCd() == null || dmDocLVo.getStatCd().isEmpty())){
					// 폴더정보 조회
					DmFldBVo dmFldBVo = dmDocSvc.getDmFldBVo(storId, dmDocLVo.getFldId(), langTypCd);
					//폴더ID가 없으면 'NONE' 미분류로 변경
					if(dmFldBVo == null){
						dmDocLVo.setFldId(DmConstant.EMPTY_CLS);
						dmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", SessionUtil.toLocale(langTypCd)));// 미분류로 세팅
					}else{
						// 해당 언어의 폴더명이 없으면 세션의 언어로 폴더를 조회한다.
						if(dmFldBVo.getFldNm() == null || dmFldBVo.getFldNm().isEmpty()) dmFldBVo = dmDocSvc.getDmFldBVo(storId, dmDocLVo.getFldId(), LoginSession.getLangTypCd(request));
					}
					
					// 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.
					dmDocSvc.setDmStatCd(dmDocLVo, storId, dmDocLVo.getFldId());
				}
				
				// 완료상태 문서일 경우 검색 색인 데이터 추가
				if("C".equals(dmDocLVo.getStatCd())){
					/** 검색 색인 데이터를 더함 */
					dmDocSvc.addSrchIndex(dmDocLVo.getDocGrpId(), userVo, queryQueue, "I");
				}
				
				// 상신목록 저장
				if("S".equals(dmDocLVo.getStatCd()))	dmDocSvc.saveDisc(queryQueue, userVo.getCompId(), dmDocLVo.getDocGrpId(), "S", null, dmDocLVo.getDiscrUid(), userVo.getUserUid(), "insert");
							
				// 문서가 '완료' 상태이면서 문서번호가 없으면 문서번호를 부여한다.
				if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()) && ( dmDocLVo.getDocNo() == null || dmDocLVo.getDocNo().isEmpty())){
					
					//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
					dmDocNoSvc.setDocNo(dmDocLVo, storId, langTypCd, DmConstant.EMPTY_CLS.equals(dmDocLVo.getFldId()) ? null : "C", dmStorBVo.getCompId(), dmDocLVo.getFldId(), dmDocLVo.getRegrUid());
					
					// 문서버전
					if(originDmDocLVo == null || originDmDocLVo.getVerVa() == null || originDmDocLVo.getVerVa().isEmpty()){
						// 관리자 환경설정 조회
						Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
						// 기본버전 저장
						dmDocLVo.setVerVa(envConfigMap.get("verDft"));
					}else{
						dmDocLVo.setVerVa(originDmDocLVo.getVerVa());
					}
					// 버전 목록 저장
					dmDocSvc.saveDocVer(queryQueue, dmDocLVo.getDocGrpId(), dmDocLVo.getDocGrpId(), tableName, dmDocLVo.getVerVa(), "store");
					
				}
				
				// 문서상세 저장[보존기한등]
				if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()))
					dmDocLVo.setCmplDt("sysdate");
				dmDocSvc.saveDmDocD(queryQueue, dmDocLVo, isSub ? dmDocLVo.getSubDocGrpId() : null);
				
				// 기본 테이블 저장 후 Url에 따른 추가 저장 옵션 실행[임시저장등]
				dmDocSvc.transUrlOptions(queryQueue, dmDocLVo, path, userVo);
				
				/** 부가정보 저장[분류체계,키워드,즐겨찾기] */
				// 분류체계 조회
				dmDocSvc.setDmClsBVoList(request, null, storId, null, dmDocLVo, isSub ? dmDocLVo.getSubDocGrpId() : null, null, dmDocLVo.getTableName(), langTypCd);
				List<DmClsRVo> dmClsRVoList = dmDocLVo.getDmClsRVoList();
				// 키워드 조회
				String[] kwdList = dmDocSvc.getDmKwdLVoList(request, dmDocLVo, isSub ? dmDocLVo.getSubDocGrpId() : null, tableName);
				if(dmClsRVoList != null && dmClsRVoList.size()>0) dmDocSvc.saveDmClsRVoList(queryQueue, dmClsRVoList, dmDocLVo.getDocGrpId(), tableName, false);
				if(kwdList != null && kwdList.length>0) dmDocSvc.saveDmKwdLVoList(queryQueue, kwdList, dmDocLVo.getDocGrpId(), tableName, false);
				// 즐겨찾기관계 저장
				String bumkId = ParamUtil.getRequestParam(request, "bumkId", false);
				if(bumkId != null && !bumkId.isEmpty())
					dmDocSvc.saveBumkDoc(request, queryQueue, storId, bumkId, "D", dmDocLVo.getDocGrpId(), false);
				
				// 관리자여부
				boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
				
				// 작업 이력저장[사용자]
				if(!admUriChk && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "insert")){
					dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, dmDocLVo.getDocGrpId(), userVo.getUserUid(), "insert", null);
				}
				
			}else{
				// 문서VO 생성
				dmDocLVo = dmDocSvc.newDmDocLVo();
				VoUtil.bind(request, dmDocLVo);
				dmDocLVo.setQueryLang(langTypCd);
				// 회사ID
				dmDocLVo.setCompId(userVo.getCompId());
				
				dmDocLVo.setDocId(dmCmSvc.createNo("DM_PSN_DOC_L").toString());
				// 등록자, 등록일시
				dmDocLVo.setRegrUid(userVo.getUserUid());
				dmDocLVo.setRegDt("sysdate");
				queryQueue.insert(dmDocLVo);
			}
			
			// 파일복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
						
			// 파일 저장
			dmFileSvc.saveDmSendFile(request, dmDocLVo.getDocId(), queryQueue, docTyp, tabId, tableName, copyFileList);
			//List<CommonFileVo> deletedFileList = dmFileSvc.saveDmFile(request, dmDocLVo.getDocId(), queryQueue, isPsn ? null : tableName);
						
			commonSvc.execute(queryQueue);
			
			// 파일 복사
			if(copyFileList.size()>0){
				dmFileSvc.copyFileList(request, tabId, copyFileList);
			}
			
			//이메일 발송
			if("doc".equals(tabId) && "S".equals(dmDocLVo.getStatCd())) 
				dmDocSvc.sendDiscDocEmail(request, langTypCd, dmDocLVo.getSubj(), "S", dmDocLVo.getDiscrUid(), userVo, null, null, "disc");
				
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			// 팝업 닫기
			model.put("todo", "parent.dialog.close('" + dialog + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 문서 보내기 - 게시판 */
	@RequestMapping(value = {"/dm/doc/transSendBrd","/dm/adm/doc/transSendBrd"},
			method = RequestMethod.POST)
	public String transSendBrd(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = bbBullFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = dmCmSvc.getRequestPath(request, model , "Doc");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			
			// 문서를 보낼 대상구분ID[공용,개인,게시판]
			String tabId = ParamUtil.getRequestParam(request, "tabId", true);
			
			// 문서구분[공용,개인]
			String docTyp = ParamUtil.getRequestParam(request, "docTyp", true);
			
			// 팝업ID
			String dialog = ParamUtil.getRequestParam(request, "dialog", true);
						
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 문서ID
			String docRefId = ParamUtil.getRequestParam(request, "docRefId", true);
			
			boolean isPsn = "psn".equals(docTyp);
			
			// 상세정보 조회
			DmDocLVo newDmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, isPsn ? null : dmStorBVo, docRefId, null);
			if(newDmDocLVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 개인문서가 아닐경우에만 권한체크
			if(!isPsn){
				//상태코드 체크
				dmDocSvc.chkDocStatCd(request, "update", newDmDocLVo.getStatCd());
				/** [권한] Start */
				boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
				Map<String,String> authMap = dmDocSvc.getAuthMap(null, admUriChk, newDmDocLVo, userVo, storId, langTypCd, path, false);
				// 권한체크 제외 URL
				boolean isUrlChk = dmDocSvc.chkNotAuth(path);
				if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, newDmDocLVo, dmDocSvc.chkDocAuth(authMap, "send", path))){
					LOGGER.error("[ERROR] User Auth Check Fail - request path : ./transDoc.do \tuserUid : "+userVo.getUserUid()+"\tauth : update");
					//cm.msg.noAuth=권한이 없습니다.
					String message = messageProperties.getMessage("cm.msg.noAuth", request);
					model.put("message", message);
					return LayoutUtil.getResultJsp();
				}
				/** [권한] End */
			}
			
			String brdId = ParamUtil.getRequestParam(request, "brdId", true);
			String bullId = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			if ("B".equals(bullStatCd) && bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			// 게시물 저장
			Integer storedBullId = bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);

			// 포토게시판이면
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 저장
				bbBullPhotoSvc.savePhoto(request, storedBullId, "photo", queryQueue);
			}
			
			if ("T".equals(bullStatCd)) {
				// 게시물 임시저장 저장
				bbBullSvc.saveBaTmpSaveLVo(request, storedBullId, queryQueue);
				
			} else if ("S".equals(bullStatCd)) {
				// 게시상신함 저장
				bbBullSvc.saveBaBullSubmLVo(request, storedBullId, baBrdBVo.getDiscrUid(), queryQueue);
				
			} else if ("R".equals(bullStatCd)) {
				// 게시물 예약저장 저장
				bbBullSvc.saveBaRezvSaveLVo(request, storedBullId, queryQueue);
			}
			
			// 파일복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
						
			// 파일 저장
			dmFileSvc.saveDmSendFile(request, String.valueOf(storedBullId), queryQueue, docTyp, tabId, tableName, copyFileList);
						
			commonSvc.execute(queryQueue);
			
			// 파일 복사
			if(copyFileList.size()>0){
				dmFileSvc.copyFileList(request, tabId, copyFileList);
			}
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			// 팝업 닫기
			model.put("todo", "parent.dialog.close('" + dialog + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	
	/** [팝업] 보내기 작성 - 문서 */
	@RequestMapping(value = {"/dm/doc/setSendWritePop","/dm/adm/doc/setSendWritePop","/cm/doc/setSendWritePop"})
	public String setSendWritePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "data", "menuId"));
		
		return LayoutUtil.getJspPath("/dm/doc/setSendWritePop");
	}
	
	/** [FRAME] 보내기 작성 - 문서 */
	@RequestMapping(value = {"/dm/doc/setSendWriteFrm","/dm/adm/doc/setSendWriteFrm"})
	public String setSendWriteFrm(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "selId", required = false) String selId,
			@RequestParam(value = "tabId", required = false) String tabId,			
			@RequestParam(value = "docTyp", required = false) String docTyp,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {
		
		if (docId == null || docId.isEmpty() || tabId == null || tabId.isEmpty() || selId == null || selId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(docTyp == null) docTyp = "psn";
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		String storId = dmStorBVo.getStorId();
		String tableName = dmStorBVo.getTblNm();
		// 공통문서여부[복사원본문서가 공통문서인지 여부]
		boolean isCmDoc = docTyp == null || docTyp.isEmpty() || !"psn".equals(docTyp);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 문서 상세 조회
		DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, isCmDoc ? dmStorBVo : null, docId, null, true);
		if(dmDocLVo == null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 공통문서일 경우 권한을 체크한다.
		if(isCmDoc){
			// 요청경로 세팅
			String path = dmCmSvc.getRequestPath(request, model , "Doc");
					
			/** [권한] Start */
			boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
			Map<String,String> authMap = dmDocSvc.getAuthMap(model, admUriChk, dmDocLVo, userVo, storId, langTypCd, "/viewSendOpt", false);
			// 권한체크 제외 URL
			boolean isUrlChk = dmDocSvc.chkNotAuth(path);
			if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "send", path))){
				LOGGER.error("[ERROR] User Auth Check Fail - request path : "+path+"\tuserUid : "+userVo.getUserUid()+"\tauth : send");
				//cm.msg.noAuth=권한이 없습니다.
				model.put("messageCode", "cm.msg.noAuth");
				model.put("todo", "parent.dialog.close('" + dialog + "');");
				return LayoutUtil.getResultJsp();
			}
			/** [권한] End */
		}

		// 시스템 설정
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// 첫 줄 삽입여부
		boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));						
		// 첫 줄 
		String cont=isFirstRow ? "<p><br/></p>" : "";
		
		// 탭[공용문서,개인문서,게시판] 별 상세정보 세팅
		if("doc".equals(tabId)){
			// 분류체계 조회
			dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, dmDocLVo.getDocGrpId(), null, tableName, langTypCd);
						
			// 항목 정보 조회
			List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, selId, null, userVo.getCompId(), null, false);
			model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
			
			// 즐겨찾기[개인] 조회
			List<DmBumkBVo> dmBumkBVoList = dmDocSvc.getDmBumkBVoList(langTypCd, "P", userVo, null);
			model.put("dmBumkBVoList", dmBumkBVoList);
			
			// 폴더 조회
			List<DmFldBVo> dmFldBVoList = dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null);
			model.put("dmFldBVoList", dmFldBVoList);
			
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			// 부모문서 조회
			if(docPid != null && !docPid.isEmpty()){
				DmDocLVo parentVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, docPid);
				if(parentVo.getFldId() != null && !parentVo.getFldId().isEmpty()){
					// 폴더 조회
					DmFldBVo dmFldBVo = new DmFldBVo(storId);
					dmFldBVo.setQueryLang(langTypCd);
					dmFldBVo.setFldId(parentVo.getFldId());
					dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
					if(dmFldBVo != null){
						dmDocLVo.setFldId(dmFldBVo.getFldId());
						dmDocLVo.setFldNm(dmFldBVo.getFldNm());
					}
				}
			}else{
				if(selId != null && !selId.isEmpty()){
					// 폴더 조회
					DmFldBVo dmFldBVo = new DmFldBVo(storId);
					dmFldBVo.setQueryLang(langTypCd);
					dmFldBVo.setFldId(selId);
					dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
					if(dmFldBVo != null){
						dmDocLVo.setFldId(dmFldBVo.getFldId());
						dmDocLVo.setFldNm(dmFldBVo.getFldNm());
					}
				}
			}
			
			model.put("transPage", "transSendDoc2");
			
			if(dmDocLVo.getOwnrUid() == null || dmDocLVo.getOwnrUid().isEmpty()){
				// 초기 소유자 세팅
				dmDocLVo.setOwnrNm(userVo.getUserNm());
				dmDocLVo.setOwnrUid(userVo.getUserUid());
			}
			
		}else if("psn".equals(tabId)){
			List<DmItemDispDVo> itemDispList = dmDocSvc.getPsnItemDispList(request, langTypCd, null, false, storId, selId, null);
			model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
			
			// 최대 본문 사이즈 model에 추가
			dmDocSvc.putBodySizeToModel(request, model);
			
			DmFldBVo dmFldBVo = new DmFldBVo(null);
			dmFldBVo.setQueryLang(langTypCd);
			dmFldBVo.setFldId(selId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			if(dmFldBVo != null){
				dmDocLVo.setFldId(dmFldBVo.getFldId());
				dmDocLVo.setFldNm(dmFldBVo.getFldNm());
			}
			
			model.put("transPage", "transSendDoc2");
		}else if("brd".equals(tabId)){
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, selId);
			model.put("baBrdBVo", baBrdBVo);
			
			if ("Y".equals(baBrdBVo.getCatYn())) {
				// 카테고리 목록 얻기
				model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
			}
			
			// 컬럼표시여부 리스트
			List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, selId, true, null, null, null, false);
			model.put("baColmDispDVoList", baColmDispDVoList);

			// 확장컬럼 코드 리스트 model에 추가
			bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");
			
			BbBullLVo bbBullLVo = new BbBullLVo();
			// 날짜 초기화
			bbBullSvc.initBullRezvDt(bbBullLVo);
			bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
			
			model.put("bbBullLVo", bbBullLVo);
			
			model.addAttribute("bullRezvDtYn", true);   // 게시예약일 활성화여부
			model.addAttribute("bullRezvChecked", false);                               // 게시예약일 체크박스 체크여부
			model.addAttribute("bbChoiYn", true);       // 게시판선택 표시여부
			model.addAttribute("tmpSaveYn", true);      // 임시저장 버튼 표시여부
			boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn()) && "N".equals(baBrdBVo.getDeptBrdYn());
			model.addAttribute("bbTgtDispYn", bbTgtDispYn);                             // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)
			
			// 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});

			// 최대 본문 사이즈 model에 추가
			bbBullSvc.putBodySizeToModel(request, model);
			model.put("transPage", "transSendBrd");
		}
		
		// 내용
		if(dmDocLVo.getCont()!=null)
			cont+=dmDocLVo.getCont();
		if(!cont.isEmpty()) dmDocLVo.setCont(cont);
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
					
		// 첨부파일 리스트 model에 추가
		dmFileSvc.putFileListToModel(docId, model, tableName, userVo.getCompId());
					
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		model.put("module", "dm");
		model.put("dmDocLVo", dmDocLVo);
		return LayoutUtil.getJspPath("/dm/doc/setSendWriteFrm");
	}
	
	/** [FRAME] 보내기 작성 - 게시판 */
	@RequestMapping(value = {"/cm/doc/setSendBrdWriteFrm"})
	public String setSendBrdWriteFrm(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			@RequestParam(value = "apvId", required = false) String apvId,
			@RequestParam(value = "selId", required = false) String selId,
			@RequestParam(value = "tabId", required = false) String tabId,			
			@RequestParam(value = "docTyp", required = false) String docTyp,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {
		
		if (tabId == null || tabId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		String storId = dmStorBVo.getStorId();
		String tableName = dmStorBVo.getTblNm();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 문서 상세 조회
		DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
		
		// 시스템 설정
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
					
		// 첫 줄 삽입여부
		boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));
		
		// 첫 줄 
		String cont=isFirstRow ? "<p><br/></p>" : "";
					
		if("brd".equals(docTyp)){
			if (brdId == null || brdId.isEmpty() || bullId == null || bullId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시판관리(BA_BRD_B) - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, true);
			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}
			dmDocLVo.setSubj(bbBullLVo.getSubj());// 제목
			
			if(bbBullLVo.getCont()!=null)
				cont+=bbBullLVo.getCont();
			if(!cont.isEmpty()) dmDocLVo.setCont(cont);// 내용
			
			//dmDocLVo.setCont(bbBullLVo.getCont());// 내용
			// 첨부파일 리스트 model에 추가
			List<CommonFileVo> fileVoList = bbBullFileSvc.getFileVoList(bullId);
			model.put("fileVoList", fileVoList);
			model.put("filesId", "dmfiles");
			
		}else if("apv".equals(docTyp)){
			
		}else{
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		//model.put("dmDocLVo", dmDocLVo);
		
		// 탭[공용문서,개인문서,게시판] 별 상세정보 세팅
		if("doc".equals(tabId)){
			// 분류체계 조회
			dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, dmDocLVo.getDocGrpId(), null, tableName, langTypCd);
						
			// 항목 정보 조회
			List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, selId, null, userVo.getCompId(), null, false);
			model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
			
			// 즐겨찾기[개인] 조회
			List<DmBumkBVo> dmBumkBVoList = dmDocSvc.getDmBumkBVoList(langTypCd, "P", userVo, null);
			model.put("dmBumkBVoList", dmBumkBVoList);
			
			// 개인폴더 조회
			List<DmFldBVo> dmFldBVoList = dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null);
			model.put("dmFldBVoList", dmFldBVoList);
			
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			// 부모문서 조회
			if(docPid != null && !docPid.isEmpty()){
				DmDocLVo parentVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, docPid);
				if(parentVo.getFldId() != null && !parentVo.getFldId().isEmpty()){
					// 폴더 조회
					DmFldBVo dmFldBVo = new DmFldBVo(storId);
					dmFldBVo.setFldId(parentVo.getFldId());
					dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
					if(dmFldBVo != null){
						dmDocLVo.setFldId(dmFldBVo.getFldId());
						dmDocLVo.setFldNm(dmFldBVo.getFldNm());
					}
				}
			}else{
				if(selId != null && !selId.isEmpty()){
					// 폴더 조회
					DmFldBVo dmFldBVo = new DmFldBVo(storId);
					dmFldBVo.setFldId(selId);
					dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
					if(dmFldBVo != null){
						dmDocLVo.setFldId(dmFldBVo.getFldId());
						dmDocLVo.setFldNm(dmFldBVo.getFldNm());
					}
				}
			}
			model.put("transPage", "transSendDoc2");
		}else if("psn".equals(tabId)){
			List<DmItemDispDVo> itemDispList = dmDocSvc.getPsnItemDispList(request, langTypCd, null, false, storId, selId, null);
			model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
			
			// 최대 본문 사이즈 model에 추가
			dmDocSvc.putBodySizeToModel(request, model);
			
			DmFldBVo dmFldBVo = new DmFldBVo(null);
			dmFldBVo.setFldId(selId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			if(dmFldBVo != null){
				dmDocLVo.setFldId(dmFldBVo.getFldId());
				dmDocLVo.setFldNm(dmFldBVo.getFldNm());
			}
			
			model.put("transPage", "transSendDoc2");
		}
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
					
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		model.put("urlPrefix", "/cm/doc");
		model.put("module", "bb");
		
		return LayoutUtil.getJspPath("/dm/doc/setSendWriteFrm");
	}
	
	/** [팝업] 보내기 옵션 - 게시판 */
	/*@RequestMapping(value = {"/dm/doc/setSendOptBrdPop","/dm/adm/doc/setSendOptBrdPop"})
	public String setSendOptBrdPop(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "brdId", required = false) String brdId,
			ModelMap model) throws Exception {
		
		if (brdId == null || brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		if ("Y".equals(baBrdBVo.getCatYn())) {
			// 카테고리 목록 얻기
			model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
		}
			
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null);
		model.put("baColmDispDVoList", baColmDispDVoList);

		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model);
		
		BbBullLVo bbBullLVo = new BbBullLVo();
		// 날짜 초기화
		bbBullSvc.initBullRezvDt(bbBullLVo);
		bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
		
		model.put("bbBullLVo", bbBullLVo);
		
		model.addAttribute("bullRezvDtYn", true);   // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", false);                               // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", true);       // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", true);      // 임시저장 버튼 표시여부
		boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn()) && "N".equals(baBrdBVo.getDeptBrdYn());
		model.addAttribute("bbTgtDispYn", bbTgtDispYn);                             // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)
		
		
					
		return LayoutUtil.getJspPath("/dm/doc/setSendOptBrdPop");
	}*/
	
	/** [AJAX] 이메일 초기정보 저장 (사용자) */
	@RequestMapping(value = {"/dm/doc/transEmailAjx", "/dm/adm/doc/transEmailAjx"})
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String docId = (String)object.get("docId");// 문서ID
			String docTyp = (String)object.get("docTyp"); // 문서구분[문서,개인]
			JSONArray recvIds = (JSONArray) object.get("recvIds");
			if (docId == null || docId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			DmStorBVo dmStorBVo = null;
			if(docTyp == null || docTyp.isEmpty()){
				String paramStorId = (String)object.get("paramStorId"); // 저장소ID
				// 기본저장소 조회
				dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, paramStorId, null, null);
				
				if(dmStorBVo == null){
					return LayoutUtil.getResultJsp();
					//throw new CmException("dm.msg.nodata.stor", request);
				}
			}
			String tableName = dmStorBVo == null ? null : dmStorBVo.getTblNm();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
					
			DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, null, true);
			if(dmDocLVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 시스템 설정
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 첫 줄 삽입여부
			boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));						
			// 첫 줄 
			String cont=isFirstRow ? "<p><br/></p>" : "";
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			cmEmailBVo.setSubj(dmDocLVo.getSubj());
			
			if(dmDocLVo.getCont()!=null)
				cont+=dmDocLVo.getCont();
			if(!cont.isEmpty()) cmEmailBVo.setCont(cont);
			
			//cmEmailBVo.setCont(dmDocLVo.getCont());
			
			// 첨부파일(DM_FILE_D) 테이블 - SELECT
			DmFileDVo dmFileDVo = new DmFileDVo();
			if(tableName != null) dmFileDVo.setTableName(tableName);
			dmFileDVo.setRefId(docId);
			@SuppressWarnings("unchecked")
			List<CommonFileVo> fileList = (List<CommonFileVo>) commonSvc.queryList(dmFileDVo);
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo, recvIds, fileList, queryQueue, userVo);
			//Integer emailId = bbBullSvc.saveBullLEmail(bbBullLVo,queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
						
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
