package com.innobiz.orange.web.em.ctrl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.utils.EmCmUtil;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 파일 미리보기(문서뷰어) */
@Controller
public class EmFileUploadCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmFileUploadCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** [AJAX] 모듈별 첨부 용량 조회 */
	@RequestMapping(value = {"/cm/getAttachSizeAjx","/ap/box/getAttachSizeAjx","/bb/getAttachSizeAjx","/bb/adm/getAttachSizeAjx","/ct/getAttachSizeAjx","/ct/adm/getAttachSizeAjx",
			"/dm/doc/getAttachSizeAjx","/dm/adm/doc/getAttachSizeAjx","/wb/getAttachSizeAjx","/wb/adm/getAttachSizeAjx",
			"/wc/getAttachSizeAjx","/wc/adm/getAttachSizeAjx","/wv/getAttachSizeAjx","/wv/adm/getAttachSizeAjx",
			"/cm/doc/getAttachSizeAjx.do"})
	public String getDiscYnAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String module = (String) object.get("module");
			if ( module == null || module.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			Map<String, Integer> maxSizeMap = ptSysSvc.getAttachSizeMap(LoginSession.getLangTypCd(request), userVo.getCompId());
			Integer maxMegaBytes = maxSizeMap==null ? 0 : maxSizeMap.containsKey(module) ? maxSizeMap.get(module) : 0;
			model.put("maxMegaBytes", maxMegaBytes);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 임시 파일 저장 */
	@RequestMapping(value = {"/cm/transFileUploadAjx","/ap/box/transFileUploadAjx","/bb/transFileUploadAjx","/bb/adm/transFileUploadAjx","/ct/transFileUploadAjx","/ct/adm/transFileUploadAjx",
			"/dm/doc/transFileUploadAjx","/dm/adm/doc/transFileUploadAjx","/wb/transFileUploadAjx","/wb/adm/transFileUploadAjx",
			"/wc/transFileUploadAjx","/wc/adm/transFileUploadAjx","/wv/transFileUploadAjx","/wv/adm/transFileUploadAjx",
			"/wp/transFileUploadAjx",
			"/cm/doc/transFileUploadAjx.do"})
	public String transFileUploadAjx(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		String module = ParamUtil.getRequestParam(request, "module", true);
		String uploadId = ParamUtil.getRequestParam(request, "uploadId", true);
		
		UploadHandler uploadHandler = null;
		try {
			uploadHandler = uploadManager.createHandler(request, "temp", module);
			uploadHandler.upload();
			request = uploadHandler.getMultipartRequest();
			
			String fileId = ParamUtil.getRequestParam(request, "fileId", false);
			if(fileId!=null && !fileId.isEmpty()){
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("refId", fileId);
				paramMap.put("module", module);
				emFileUploadSvc.copyToTmpFile(request, model, paramMap);
			}else{
				MultipartFile file = ((MultipartHttpServletRequest) request).getFile(uploadId);
				if(file!=null){
					String filePath = uploadHandler.getAbsolutePath(uploadId).replace('\\', '/');
					EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
					emTmpFileTVo.setTmpFileId(commonSvc.nextVal("EM_TMP_FILE_T").intValue());
					emTmpFileTVo.setDispNm(file.getOriginalFilename());
					String ext = EmCmUtil.getFileExtension(filePath, true);
					emTmpFileTVo.setFileExt(ext);
					emTmpFileTVo.setFileSize(Long.valueOf(file.getSize()));
					emTmpFileTVo.setSavePath(filePath);
					commonSvc.insert(emTmpFileTVo);
					model.put("tmpFileId", emTmpFileTVo.getTmpFileId());
					
					if(ext!=null){
						// 이미지일 경우 가로 세로 길이 리턴
						String[] exts = new String[]{"gif","jpg","jpeg","png","tif"};
						if(ArrayUtil.isInArray(exts, ext.toLowerCase())){
							BufferedImage bimg = ImageIO.read(new File(filePath));
							model.put("imgWdth", Integer.toString(bimg.getWidth()));
							model.put("imgHght", Integer.toString(bimg.getHeight()));
						}
					}
				}else{
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					model.put("message", messageProperties.getMessage("pt.msg.nodata.passed", request));
				}
			}
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} 

		return JsonUtil.returnJson(model);
	}
	
	/** 파일 업로드 진행상태 팝업 */
	@RequestMapping(value = {"/cm/setUploadProgressPop","/ap/box/setUploadProgressPop","/bb/setUploadProgressPop","/bb/adm/setUploadProgressPop","/ct/setUploadProgressPop","/ct/adm/setUploadProgressPop",
			"/dm/doc/setUploadProgressPop","/dm/adm/doc/setUploadProgressPop","/wb/setUploadProgressPop","/wb/adm/setUploadProgressPop",
			"/wc/setUploadProgressPop","/wc/adm/setUploadProgressPop","/wv/setUploadProgressPop","/wv/adm/setUploadProgressPop",
			"/cm/doc/setUploadProgressPop"})
	public String setUploadProgressPop(HttpServletRequest request,
			ModelMap model) throws SQLException {

		return LayoutUtil.getJspPath("/em/setUploadProgressPop");
	}
	
	/** [FORM] 이미지 업로드 */
	@RequestMapping(value = "/cm/transImgUpload", method = RequestMethod.POST)
	public String transImgUpload(HttpServletRequest request,
			@RequestParam(value = "module", required = false) String module,
			@RequestParam(value = "uploadId", required = false) String uploadId,
			@RequestParam(value = "callback", required = false) String callback,
			ModelMap model) throws Exception {
		
		if ( module == null || module.isEmpty() || uploadId == null || uploadId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", module);
			uploadHandler.upload();
			request = uploadHandler.getMultipartRequest();
			
			MultipartFile file = ((MultipartHttpServletRequest) request).getFile(uploadId);
			if(file!=null){
				String filePath = uploadHandler.getAbsolutePath(uploadId).replace('\\', '/');
				EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
				emTmpFileTVo.setTmpFileId(commonSvc.nextVal("EM_TMP_FILE_T").intValue());
				emTmpFileTVo.setDispNm(file.getOriginalFilename());
				String ext = EmCmUtil.getFileExtension(filePath, true);
				emTmpFileTVo.setFileExt(ext);
				emTmpFileTVo.setFileSize(Long.valueOf(file.getSize()));
				emTmpFileTVo.setSavePath(filePath);
				commonSvc.insert(emTmpFileTVo);
				
				BufferedImage bimg = ImageIO.read(new File(filePath));
				if(callback==null || callback.isEmpty()) callback = "parent.setImage";
				model.put("todo", "parent.setImage('"+emTmpFileTVo.getTmpFileId()+"', "+Integer.toString(bimg.getWidth())+", "+Integer.toString(bimg.getHeight())+", "+emTmpFileTVo.getFileSize()+");");
				
			}else{
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				model.put("message", messageProperties.getMessage("pt.msg.nodata.passed", request));
			}
			
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		} 
	
		return LayoutUtil.getResultJsp();
	}
	
	/** 이미지에 대한 미리보기 */
	@RequestMapping(value = "/cm/viewTmpImage")
    public void viewTmpImage(HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
		
		String tmpFileId = ParamUtil.getRequestParam(request, "tmpFileId", true);
		EmTmpFileTVo emTmpFileTVo = new EmTmpFileTVo();
		emTmpFileTVo.setTmpFileId(Integer.parseInt(tmpFileId));
		emTmpFileTVo = (EmTmpFileTVo)commonSvc.queryVo(emTmpFileTVo);
		
		if(emTmpFileTVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String filePath = emTmpFileTVo.getSavePath();
		File file = new File(filePath);
    	if(file.isFile()){ 
			FileInputStream fis = new FileInputStream(file);
		
			BufferedInputStream in = new BufferedInputStream(fis);
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		
			try{
				int imgByte;
				while ((imgByte = in.read()) != -1) {
				    bStream.write(imgByte);
				}
				in.close();
			}catch(IllegalArgumentException iae){
				System.out.println("+ IllegalArgumentException : " + iae.toString());
			}finally{
				in.close();
			}
			
			String type = "image/jpeg"; 
		
			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());
			
			bStream.writeTo(response.getOutputStream());
			
			response.getOutputStream().flush();
			response.getOutputStream().close();
    	}else{
    		// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
    		throw new CmException("pt.msg.nodata.passed", request);
    	}

    }
	
	
	/** [팝업] 첨부파일 목록 조회 */
	@RequestMapping(value = {"/bb/viewFileListPop", "/bb/adm/viewFileListPop", "/bb/viewFileListFrm", "/dm/doc/viewFileListPop", "/dm/adm/doc/viewFileListPop", 
			"/ct/board/viewFileListPop", "/ct/adm/viewFileListPop", "/wl/task/viewFileListPop", "/wl/adm/task/viewFileListPop",
			"/wh/help/viewFileListPop", "/wh/adm/help/viewFileListPop", "/wf/works/viewFileListPop", "/wf/adm/works/viewFileListPop", 
			"/wp/viewFileListPop", "/wj/work/viewFileListPop", "/wj/adm/work/viewFileListPop"})
	public String viewFileListPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 프레임 여부
		boolean isFrm=request.getRequestURI().indexOf("Frm")>0;
				
		Map<String,String> paramMap = new HashMap<String,String>();
		String urlParam=null, urlSuffix=null;
		if(request.getRequestURI().startsWith("/bb")){ // 게시판
			String brdId=ParamUtil.getRequestParam(request, "brdId", false);
			String bullId=ParamUtil.getRequestParam(request, "bullId", true);
			paramMap.put("refTyp", "bb");
			paramMap.put("refId", bullId);			
			if(brdId!=null && !brdId.isEmpty()) urlParam="brdId="+brdId;
			model.put("module", "bb");
		}else if(request.getRequestURI().startsWith("/dm")){ // 문서관리
			String docId=ParamUtil.getRequestParam(request, "docId", true);			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			paramMap.put("refTyp", "dm");
			paramMap.put("tableName", dmStorBVo.getTblNm());
			paramMap.put("refId", docId);
			String docGrpId=ParamUtil.getRequestParam(request, "docGrpId", true);
			urlParam="docGrpId="+docGrpId;
			String paramStorId=ParamUtil.getRequestParam(request, "paramStorId", false);
			if(paramStorId!=null && !paramStorId.isEmpty()) urlParam+="&paramStorId="+paramStorId;
			model.put("module", "dm");
		}else if(request.getRequestURI().startsWith("/ct")){ // 커뮤니티
			String bullId=ParamUtil.getRequestParam(request, "bullId", true);
			paramMap.put("refTyp", "ct");
			paramMap.put("refId", bullId);
			model.put("module", "ct");
		}else if(request.getRequestURI().startsWith("/wl")){ // 업무일지
			String logNo=ParamUtil.getRequestParam(request, "logNo", false);
			paramMap.put("refTyp", "wl");
			paramMap.put("refId", logNo);			
			model.put("module", "wl");
		}else if(request.getRequestURI().startsWith("/wh")){ // 헬프데스크
			String reqNo=ParamUtil.getRequestParam(request, "reqNo", false);
			String statCd=ParamUtil.getRequestParam(request, "statCd", false);
			paramMap.put("refTyp", "wh");
			paramMap.put("refId", reqNo);
			if(statCd!=null)
				paramMap.put("statCd", statCd);
			model.put("module", "wh");
		}else if(request.getRequestURI().startsWith("/wf")){ // 업무관리
			String workNo=ParamUtil.getRequestParam(request, "workNo", false);
			String formNo=ParamUtil.getRequestParam(request, "formNo", false);
			paramMap.put("refTyp", "wf");
			paramMap.put("refId", workNo);
			paramMap.put("formNo", formNo);
			model.put("module", "wf");
			urlParam="formNo="+formNo;
			urlSuffix="/works";
		}else if(request.getRequestURI().startsWith("/wp")){ // 프로젝트 관리
			
		}else if(request.getRequestURI().startsWith("/wj")){ // 업무보고(다인)
			String reprtNo=ParamUtil.getRequestParam(request, "reprtNo", false);
			paramMap.put("refTyp", "wj");
			paramMap.put("refId", reprtNo);			
			model.put("module", "wj");
		}else{
			return LayoutUtil.getJspPath("/em/viewFileListPop");
		}
		paramMap.put("compId", userVo.getCompId());
		emFileUploadSvc.setFileList(model, paramMap);
		
		model.put("urlParam", urlParam);
		
		if(urlSuffix!=null) model.put("urlSuffix", urlSuffix);
		
		if(isFrm){// 프레임
			model.put("isFrm", isFrm);
			return LayoutUtil.getJspPath("/em/viewFileListPop","Frm");
		}
		
		return LayoutUtil.getJspPath("/em/viewFileListPop");
	}
	
	/** 이미지 다운로드 */
	@RequestMapping(value = "/cm/htmlToImg")
    public ModelAndView imgDown(HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
		
		try {
			String data = ParamUtil.getRequestParam(request, "data", true);
			String fileName = ParamUtil.getRequestParam(request, "fileName", false);
			if(fileName==null || fileName.isEmpty()) fileName=String.valueOf(System.currentTimeMillis());
			fileName+=".png";
			data = data.replaceAll("data:image/png;base64,", "");
			
            byte[] file = Base64.decodeBase64(data);
            ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadBytes", file);
			mv.addObject("realFileName", fileName);
			return mv;

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
