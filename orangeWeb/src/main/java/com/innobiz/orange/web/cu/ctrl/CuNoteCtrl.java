package com.innobiz.orange.web.cu.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.svc.CuNoteFileSvc;
import com.innobiz.orange.web.cu.svc.CuNoteSvc;
import com.innobiz.orange.web.cu.vo.CuNoteRecvBVo;
import com.innobiz.orange.web.cu.vo.CuNoteRecvLVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 업무현황 */
@Controller
public class CuNoteCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuNoteCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNoteSvc")
	private CuNoteSvc cuNoteSvc;
	
	/** 파일 서비스 */
	@Resource(name = "cuNoteFileSvc")
	private CuNoteFileSvc cuNoteFileSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	
	/** 목록 */
	@RequestMapping(value = {"/cu/send/listNote", "/cu/adm/send/listNote", // 보낸쪽지
			"/cu/recv/listNote", "/cu/adm/recv/listNote",	// 받은쪽지
			"/cu/send/listNotePltFrm", "/cu/recv/listNotePltFrm"}) // 포틀릿
	public String listNote(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/cu/adm/");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 받은쪽지 여부
		boolean isRecvNote=request.getRequestURI().startsWith("/cu/recv/") || request.getRequestURI().startsWith("/cu/adm/recv/");
		
		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		CuNoteRecvBVo cuNoteRecvBVo = new CuNoteRecvBVo();		
		VoUtil.bind(request, cuNoteRecvBVo);
		cuNoteRecvBVo.setQueryLang(langTypCd);
		if(!isRecvNote){ // 보낸쪽지
			if(!isAdmin) cuNoteRecvBVo.setRegrUid(userVo.getUserUid());
			cuNoteRecvBVo.setRecvNote(isRecvNote);
		}else{
			String schRegrUid = ParamUtil.getRequestParam(request, "schRegrUid", false);
			if(schRegrUid==null || schRegrUid.isEmpty())
				cuNoteRecvBVo.setSchRegrUid(null);
			cuNoteRecvBVo.setRegrUid(null);
			if(!isAdmin) cuNoteRecvBVo.setRecvrUid(userVo.getUserUid());
			if(isPlt){
				String bxId = ParamUtil.getRequestParam(request, "bxId", false);
				if(bxId!=null && !bxId.isEmpty()){
					if("notRead".equals(bxId)){
						cuNoteRecvBVo.setReadYn("N");
					}
				}
			}
		}
		
		Integer recodeCount = commonSvc.count(cuNoteRecvBVo);
		
		if(isPlt){
			String pageRowCnt = ParamUtil.getRequestParam(request, "pageRowCnt", false);
			int rowCnt = 0;
			if(pageRowCnt!=null && !pageRowCnt.isEmpty()){
				try{
					rowCnt = Integer.parseInt(pageRowCnt);
					model.addAttribute("pageRowCnt", pageRowCnt);
				} catch(Exception ignore){}
			}
			if(rowCnt==0){
				String hghtPx = ParamUtil.getRequestParam(request, "hghtPx", true);
				String colYn = ParamUtil.getRequestParam(request, "colYn", true);
				// 한 페이지 레코드수 - 높이에 의한 계산
				int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
				int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
				rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
				model.addAttribute("pageRowCnt", Integer.valueOf(rowCnt));
			}
			PersonalUtil.setFixedPaging(request, cuNoteRecvBVo, rowCnt, recodeCount);
		}else {
			PersonalUtil.setPaging(request, cuNoteRecvBVo, recodeCount);
		}
		
		@SuppressWarnings("unchecked")
		List<CuNoteRecvBVo> cuNoteList = (List<CuNoteRecvBVo>) commonSvc.queryList(cuNoteRecvBVo);
		
		if(isRecvNote){
			for(CuNoteRecvBVo storedCuNoteRecvBVo : cuNoteList){
				storedCuNoteRecvBVo.setFileCnt(cuNoteFileSvc.getFileVoListCnt(storedCuNoteRecvBVo.getSendNo()));
			}
		}else{
			for(CuNoteRecvBVo storedCuNoteRecvBVo : cuNoteList){
				cuNoteSvc.setCuNoteRecvLVoList(storedCuNoteRecvBVo, langTypCd, storedCuNoteRecvBVo.getSendNo());
			}
		}
		
		if(isPlt && cuNoteList!=null && cuNoteList.size()>0){
			Map<String, Object> listMap;
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			for(CuNoteRecvBVo storedCuNoteRecvBVo : cuNoteList){
				listMap = VoUtil.toMap(storedCuNoteRecvBVo, null);
				rsltMapList.add(listMap);
			}
			model.put("rsltMapList", rsltMapList);
		}
		
		model.put("recodeCount", recodeCount);
		model.put("cuNoteList", cuNoteList);
		
		model.put("isRecvNote", isRecvNote);
		model.put("isAdmin", isAdmin);
		
		if(isPlt){//포틀릿
			return LayoutUtil.getJspPath("/cu/note/plt/listNotePltFrm");
		}
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
					
		return LayoutUtil.getJspPath("/cu/note/listNote");
	}
	
	/** [팝업] - 쪽지 보내기 */
	@RequestMapping(value = {"/cu/send/setNotePop", "/cu/send/setNotePltPop", "/cu/recv/setNotePop", "/cu/recv/setNotePltPop", 
			"/cu/adm/send/setNotePop", "/cu/send/viewNotePop", "/cu/adm/send/viewNotePop", 
		"/cu/recv/viewNotePop", "/cu/recv/viewNotePltPop", "/cu/adm/recv/viewNotePop"})
	public String viewReqPop(HttpServletRequest request,
			@RequestParam(value = "recvNo", required = false) String recvNo,
			ModelMap model) throws Exception {
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
				
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/cu/adm/");
					
		// 작성 여부
		boolean isSetMode=request.getRequestURI().startsWith("/cu/send/set") || request.getRequestURI().startsWith("/cu/recv/set") || request.getRequestURI().startsWith("/cu/adm/send/set");
		
		// 받은쪽지 여부
		boolean isRecvNote=request.getRequestURI().startsWith("/cu/recv/") || request.getRequestURI().startsWith("/cu/adm/recv/");
				
		if(isSetMode){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 최대 본문 사이즈 model에 추가
			cuNoteSvc.putBodySizeToModel(request, model);
			
			// 첨부파일 리스트 model에 추가
			cuNoteFileSvc.putFileListToModel(null, model, userVo.getCompId());
			
			model.put("viewMode", "set");
			
			if(recvNo!=null && !recvNo.isEmpty()){// 답장
				CuNoteRecvBVo cuNoteRecvBVo = new CuNoteRecvBVo();
				cuNoteRecvBVo.setRecvNo(recvNo);
				cuNoteRecvBVo=(CuNoteRecvBVo)commonSvc.queryVo(cuNoteRecvBVo);
				if(cuNoteRecvBVo!=null){
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					// 사용자 정보맵
					Map<String, Object> userMap = orCmSvc.getUserMap(cuNoteRecvBVo.getRegrUid(), langTypCd);
					model.put("userInfos", new String[]{(String)userMap.get("userUid"), (String)userMap.get("rescNm")});
				}
			}
						
		}else{
			cuNoteSvc.viewNote(request, model, isRecvNote, isAdmin); // 상세조회
			model.put("viewMode", "view");
		}
		model.put("isRecvNote", isRecvNote);
		model.put("isAdmin", isAdmin);
		model.put("isPlt", isPlt);
		
		return LayoutUtil.getJspPath("/cu/note/setNotePop");
	}
	
	/** [팝업] - 수신확인 */
	@RequestMapping(value = {"/cu/send/listRecvPop", "/cu/adm/send/listRecvPop"})
	public String listRecvPop(HttpServletRequest request,
			@RequestParam(value = "sendNo", required = false) String sendNo,
			ModelMap model) throws Exception {
		
		if(sendNo==null || sendNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 수신확인 조회
		CuNoteRecvLVo cuNoteRecvLVo = new CuNoteRecvLVo();
		cuNoteRecvLVo.setSendNo(sendNo);
		cuNoteRecvLVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<CuNoteRecvLVo> cuNoteRecvLVoList = (List<CuNoteRecvLVo>) commonSvc.queryList(cuNoteRecvLVo);
		model.put("cuNoteRecvLVoList", cuNoteRecvLVoList);
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/cu/adm/");
		
		model.put("isAdmin", isAdmin);
		
		return LayoutUtil.getJspPath("/cu/note/listRecvPop");
	}
	
	/** 저장 */
	@RequestMapping(value = {"/cu/send/transNote", "/cu/send/transNotePlt", "/cu/recv/transNote", "/cu/recv/transNotePlt", "/cu/adm/send/transNote"}, method = RequestMethod.POST)
	public String transNote(HttpServletRequest request,
			ModelMap model) {
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
				
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = cuNoteFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			QueryQueue queryQueue = new QueryQueue();
			// 저장
			CuNoteRecvBVo cuNoteRecvBVo=cuNoteSvc.saveNote(request, queryQueue);
			
			// 쪽지 보내기
			Integer recvLen = cuNoteSvc.sendNote(request, queryQueue, cuNoteRecvBVo);
			
			if(recvLen>0){
				// 파일 저장
				List<CommonFileVo> deletedFileList = cuNoteFileSvc.saveFile(request, cuNoteRecvBVo.getSendNo(), queryQueue);
				
				commonSvc.execute(queryQueue);
				
				// 파일 삭제
				cuNoteFileSvc.deleteDiskFiles(deletedFileList);
			}
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(isPlt) model.put("todo", "top.notePopClose();");
			else model.put("todo", "parent.location.replace('" + listPage + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 삭제 */
	@RequestMapping(value = {"/cu/send/transNoteDelAjx", "/cu/adm/send/transNoteDelAjx", 
			"/cu/recv/transNoteDelAjx", "/cu/adm/recv/transNoteDelAjx"})
	public String transNoteDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 받은쪽지 여부
			boolean isRecvNote=request.getRequestURI().startsWith("/cu/recv/") || request.getRequestURI().startsWith("/cu/adm/recv/");
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String seqNo = (String) object.get("seqNo");
			JSONArray seqNos = (JSONArray) object.get("seqNos");
			if (seqNo == null && seqNos == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 관리자 페이지 여부
			boolean isAdmin = request.getRequestURI().startsWith("/cu/adm/");
						
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			// 파일 삭제
			cuNoteSvc.deleteFileList(queryQueue, seqNo, seqNos, isRecvNote, deletedFileList);
			
			// 단건 삭제
			if(seqNo!=null && !seqNo.isEmpty()){
				cuNoteSvc.deleteNote(isRecvNote, queryQueue, userVo, seqNo, isAdmin);
			}
			
			// 복수 삭제
			if(seqNos!=null && !seqNos.isEmpty()){
				for(int i=0;i<seqNos.size();i++){
					seqNo = (String)seqNos.get(i);
					cuNoteSvc.deleteNote(isRecvNote, queryQueue, userVo, seqNo, isAdmin);
				}
			}
			
			// 파일 삭제
			//if(!isRecvNote) // 보낸편지함 삭제
			//	deleteFile(queryQueue, deletedFileList, seqNo);
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 파일 삭제
			cuNoteFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/cu/send/downFile","/cu/adm/send/downFile", 
			"/cu/recv/downFile","/cu/adm/recv/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
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
			
			// fileId
			String[] fileIdArray = fileIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String fileId : fileIdArray) {
				// 게시물첨부파일
				CommonFileVo fileVo = cuNoteFileSvc.getFileVo(Integer.valueOf(fileId));
				if (fileVo != null) {
					fileVo.setSavePath(wasCopyBaseDir+fileVo.getSavePath());
					File file = new File(fileVo.getSavePath());
					if (file.isFile()) {
						fileVoList.add(fileVo);
					}
				}
			}
			// 파일이 몇개인가
			if (fileVoList.size() == 0) {
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				Locale locale = SessionUtil.getLocale(request);
				// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
				mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
				return mv;
				
			} else if (fileVoList.size() == 1) {
				CommonFileVo fileVoVo = fileVoList.get(0);
				String savePath = fileVoVo.getSavePath();
				File file = new File(savePath);
				ModelAndView mv = new ModelAndView("fileDownloadView");
				mv.addObject("downloadFile", file);
				mv.addObject("realFileName", fileVoVo.getDispNm());
				return mv;
				
			} else {
				File zipFile = zipUtil.makeZipFile(fileVoList, "files.zip");
				ModelAndView mv = new ModelAndView("fileDownloadView");
				mv.addObject("downloadFile", zipFile);
				mv.addObject("realFileName", zipFile.getName());
				return mv;
			}
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
}
