package com.innobiz.orange.web.em.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.svc.ApDocSvc;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ConvertUtil;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.preview.PreviewAuthSvc;
import com.innobiz.orange.web.em.utils.EmCmUtil;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.wb.svc.WbBcFileSvc;
import com.innobiz.orange.web.wb.svc.WbBcMetngFileSvc;
import com.innobiz.orange.web.wc.svc.WcFileSvc;
import com.innobiz.orange.web.wv.svc.WvFileSvc;

@Service
public class EmAttachViewSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmAttachViewSvc.class);
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 문서 파일 서비스 */
	@Resource(name = "convertUtil")
	private ConvertUtil convertUtil;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 문서 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 명함 파일 서비스 */
	@Autowired
	private WbBcFileSvc wbBcFileSvc;
	
	/** 미팅 파일 서비스 */
	@Autowired
	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 일정 파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	/** 설문 파일 서비스 */
	@Autowired
	private WvFileSvc wvFileSvc;
	
	/** 커뮤니티 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 결재 문서 서비스 */
	@Autowired
	private ApDocSvc apDocSvc;
	
	/** 문서뷰어 권한 서비스 */
	@Resource(name = "previewAuthSvc")
	private PreviewAuthSvc previewAuthSvc;
	
	/** 미리보기 */
	public void convertPreview(HttpServletRequest request, ModelMap model, UserVo userVo, String module) throws Exception{
		// 회사ID
		String compId =  userVo.getCompId();
		// 문서뷰어 사용여부
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 모바일 여부
		boolean isMob = ServerConfig.IS_MOBILE;
		String enable = isMob ? "docViewerMobEnable" : "docViewerWebEnable";
		boolean viewerEnable = sysPlocMap != null && sysPlocMap.containsKey(enable) && "Y".equals(sysPlocMap.get(enable));
		if(viewerEnable){
			// 시스템 설정 조회 - 첨부설정
			Map<String, String> attcSetupMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ATTC_SETUP+compId, true);
			String viewYn = (isMob ? "mob" : "web")+"ViewYn";
			if(attcSetupMap==null || !attcSetupMap.containsKey(viewYn) || "N".equals(attcSetupMap.get(viewYn))){
				// em.msg.not.viewer.service=미리보기 서비스를 제공하지 않습니다.
				throw new CmException("em.msg.not.viewer.service", request);
			}
		}else{
			// em.msg.not.viewer.service=미리보기 서비스를 제공하지 않습니다.
			throw new CmException("em.msg.not.viewer.service", request);
		}
		
		// 첨부파일
		CommonFileVo fileVo = null;
		
		String fileId = ParamUtil.getRequestParam(request, "fileIds", false);
		
		// 저장경로
		String savePath = null;
		String dispNm = null;
		// 등록일자
		String regDt = null;
		if("ap".equals(module)){
			String apvNo = ParamUtil.getRequestParam(request, "apvNo", true);
			String attHstNo = ParamUtil.getRequestParam(request, "attHstNo", false);
			String attSeq = ParamUtil.getRequestParam(request, "attSeq", true);
			String intgNo = ParamUtil.getRequestParam(request, "intgNo", false);
			// 기본 로직 - 연계 아닌 것
			if(intgNo==null || intgNo.isEmpty()){
				// 첨부일련번호
				String[] attSeqs = attSeq.split(",");
				List<ApOngdAttFileLVo> apOngdAttFileLVoList = apDocSvc.getApOngdAttFileLVoList(apvNo, attHstNo, attSeqs);
				
				int size = apOngdAttFileLVoList==null ? 0 : apOngdAttFileLVoList.size();
				if(size==0){
					// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
					String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
					LOGGER.error(message+ " : NO ATTCH DATA (apvNo:"+apvNo
							+(attHstNo==null || attHstNo.isEmpty() ? "" : "  attHstNo:"+attHstNo)
							+"  attSeqs:"+ArrayUtil.toString(attSeqs)+")");
					throw new CmException(message);
				}
				ApOngdAttFileLVo apOngdAttFileLVo = apOngdAttFileLVoList.get(0);
				savePath = apOngdAttFileLVo.getFilePath();
				dispNm = apOngdAttFileLVo.getAttDispNm();
				regDt = apOngdAttFileLVo.getModDt();
			}
		}else if("bb".equals(module)){
			fileVo = bbBullFileSvc.getFileVo(Integer.valueOf(fileId));
		}else if("ct".equals(module)){
			fileVo = ctFileSvc.getFileVo(Integer.valueOf(fileId));
		}else if("wc".equals(module)){
			fileVo = wcFileSvc.getFileVo(Integer.valueOf(fileId));
		}else if("wb".equals(module)){
			String actionParam = ParamUtil.getRequestParam(request, "actionParam", false);
			if(actionParam != null && "metng".equals(actionParam)){
				fileVo = wbBcMetngFileSvc.getFileVo(Integer.valueOf(fileId));
			}else{
				fileVo = wbBcFileSvc.getFileVo(Integer.valueOf(fileId));
			}
		}else if("wv".equals(module)){
			fileVo = wvFileSvc.getFileVo(Integer.valueOf(fileId));
		}else if("dm".equals(module)){
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, null, null, null, null, null);
			String tableName = dmStorBVo.getTblNm();
			// 첨부파일
			fileVo = dmFileSvc.getFileVo(Integer.valueOf(fileId), tableName);
		}else{
			LOGGER.error("[ERROR] module is Null!!");
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(!"ap".equals(module)){
			savePath = fileVo.getSavePath();
			dispNm = fileVo.getDispNm();
			regDt = fileVo.getRegDt();
		}
		if (savePath != null) {
			// 표시할 파일명
			String fileName = EmCmUtil.getFileExtension(savePath, false);
			if(fileName==null){
				LOGGER.error("[ERROR] fileName is Null!!");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 확장자
			String ext = EmCmUtil.getFileExtension(savePath, true);
			// 문서뷰어 가능한 파일 확장자
			String viewExts = ArrayUtil.toString(EmConstant.EXTENSIONS);
			if (ext!=null && !ext.isEmpty()){
				if(!ArrayUtil.isInArray(EmConstant.EXTENSIONS, ext.toLowerCase())){
					// cm.msg.attach.not.support.ext=첨부파일 확장자가 맞지 않습니다.(필요 확장자 \: {0})
					throw new CmException("cm.msg.attach.not.support.ext", new String[]{viewExts}, request);
				}
			}else{
				// cm.msg.attach.not.support.ext=첨부파일 확장자가 맞지 않습니다.(필요 확장자 \: {0})
				throw new CmException("cm.msg.attach.not.support.ext", new String[]{viewExts}, request);
			}
			// 미리보기 HTML 소스 업로드 기본 경로
			//String webCopyBaseDir = distManager.getWebCopyBaseDir();
			String webCopyBaseDir = request.getServletContext().getRealPath("");
			// 첨부파일 기본경로
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				//webCopyBaseDir = distManager.getWebCopyBaseDir();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			// 경로변경[웹,모바일 각각 컨버팅] - properties 로 관리X
			/*if(ServerConfig.IS_MOBILE){
				if(webCopyBaseDir.indexOf("gwOrange")>-1) webCopyBaseDir = webCopyBaseDir.replace("gwOrange", "gwOrangeMobile");
				// 모바일 개발PC에서 해당 경로로 변경 - 로컬 개발용 하드코딩
				if(ServerConfig.IS_LOC) webCopyBaseDir = webCopyBaseDir.replace("orangeWeb", "orangeMobile");
			}*/
			
			String filePath = wasCopyBaseDir+savePath;
			
			File file = new File(filePath);
			if(!file.isFile()){
				// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
				String message = messageProperties.getMessage("cm.msg.file.fileNotFound", request);
				throw new CmException(message + " : "+dispNm);
			}
			if(regDt==null || regDt.isEmpty()) regDt = StringUtil.getCurrYmd();
			// 변환결과 폴더 생성
			String rs = convertUtil.makeDir(EmConstant.VIEWER+File.separator+EmConstant.PREVIEW, webCopyBaseDir, regDt);
			String outputPath = webCopyBaseDir+File.separator+rs;
			
			// DRM 체크(한화제약)
			filePath = chkDrmFile(request, filePath, webCopyBaseDir, fileName);
			if(filePath==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			//System.out.println("filePath : "+filePath);
			int returnValue = convertUtil.convertToHtml(filePath, outputPath, fileName, null, null);
			// 파일변환 여부 상관없이 DRM 복호화 파일 삭제
			// 한화제약 - E78CEB
			if(CustConfig.CUST_HANWHA){
				removeFileDir(webCopyBaseDir, fileName); // 복호화된 파일 삭제
			}
			//int returnValue = 0;
			if(returnValue==0){
				// 표시할문서명
				model.put("dispNm", dispNm);
				// 원본문서명
				model.put("fn", fileName);
				// 변환결과폴더명
				model.put("rs", rs.replace('\\', '/'));
				// 확장자
				model.put("ext", ext);
				// 파일ID 세팅
				int p = fileName.lastIndexOf('.');
				String authFileId = p>=0 ? fileName.substring(0, p) : fileName;
				previewAuthSvc.setAuth(userVo.getUserUid(), authFileId);
			}else{
				LOGGER.error("[ERROR] dispNm : "+dispNm+"\treturnValue : "+returnValue);
				// cm.msg.errors.500=처리중 오류가 발생 하였습니다.
				throw new CmException("cm.msg.errors.500", request);
			}
		}
	}
	
	/** 다운로드 체크 */
	public void chkAttachDown(HttpServletRequest request, String compId) throws SQLException, CmException{
		// 시스템 설정 조회 - 첨부설정
		Map<String, String> attcSetupMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ATTC_SETUP+compId, true);
		// 다운로드 설정
		boolean isMob = ServerConfig.IS_MOBILE;
		String enablePrefix = isMob ? "mob" : "web";
		String downYn = enablePrefix+"DownYn";
		if(attcSetupMap==null || (attcSetupMap!=null && attcSetupMap.containsKey(downYn) && "N".equals(attcSetupMap.get(downYn)) )){
			// cm.msg.not.download.service=다운로드 서비스를 제공하지 않습니다.
			throw new CmException("cm.msg.not.download.service", request);
		}
	}
	
	/** 첨부 설정 체크 */
	public void chkAttachSetup(ModelMap model, String compId) throws SQLException{
		// 시스템 설정 조회 - 첨부설정
		Map<String, String> attcSetupMap = ptSysSvc.getSysSetupMap(PtConstant.PT_ATTC_SETUP+compId, true);
		boolean isMob = ServerConfig.IS_MOBILE;
		String enablePrefix = isMob ? "mob" : "web";
		String downYn = enablePrefix+"DownYn";
		if(attcSetupMap!=null && attcSetupMap.containsKey(downYn)) model.put("downYn", attcSetupMap.get(downYn));
		// 문서뷰어 사용여부
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		String enable = isMob ? "docViewerMobEnable" : "docViewerWebEnable";
		boolean viewerEnable = sysPlocMap != null && sysPlocMap.containsKey(enable) && "Y".equals(sysPlocMap.get(enable));
		String viewYn = enablePrefix+"ViewYn";
		if(viewerEnable && attcSetupMap!=null && attcSetupMap.containsKey(viewYn)) model.put("viewYn", attcSetupMap.get(viewYn));
	}
	
	/** DRM 파일 체크 
	 * - 호출 URL : http://192.168.10.238:8080/orange.jsp
	 * - 파라미터 : encPath : AP 서버(그룹웨어 서버-192.168.10.220)의 암호화된 파일의 절대 경로
	 * 				   decPath : AP 서버(그룹웨어 서버-192.168.10.220)의 복호화될 파일의 절대 경로
	 * - 호출결과
	 * SUCCESS : 성공
	 * NOT ENCRYPTED : 암호화 안된 파일(한화제약 이외 동일처리)
	 * ERROR : 에러메세지 
	 * */
	public String chkDrmFile(HttpServletRequest request, String encPath, String wasCopyBaseDir, String fileName) throws IOException, CmException{
		// 한화제약 - E78CEB
		if(!CustConfig.CUST_HANWHA){ // 한화제약이 아닌경우
			return encPath;
		}
		// 복호화될 파일의 절대 경로
		String decPath = wasCopyBaseDir+File.separator+EmConstant.VIEWER+File.separator+EmConstant.DECRYPT_PATH+File.separator+fileName;
		decPath = decPath.replace('\\', '/');
		HttpClient http = new HttpClient();
		String url = "http://192.168.10.238:8080/orange.jsp";
		url+="?encPath="+encPath;
		url+="&decPath="+decPath;
		// url 호출 결과
		String returnString = http.sendGet(url, "UTF-8");
		if(returnString==null || returnString.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(returnString.indexOf("ERROR")>-1){
			String[] message = returnString.split(":");
			// ERROR : 에러메세지 
			throw new CmException(message.length>1 ? message[1] : returnString);
		}
		if("SUCCESS".equals(returnString)){
			return decPath; // 복호화된 파일 경로
		}else if("NOT ENCRYPTED".equals(returnString)){
			return encPath; // 원본 파일 경로
		}
		
		return null;
		
	}
	
	/** 파일 저장 디렉토리 삭제 */
	public void removeFileDir(String wasCopyBaseDir, String fileName){
		try{
			// 복호화 디렉토리 기본 경로
			String decPath = wasCopyBaseDir+File.separator+EmConstant.VIEWER+File.separator+EmConstant.DECRYPT_PATH;
			if(fileName!=null && !fileName.isEmpty()){
				decPath+=File.separator+fileName;
			}
			decPath = decPath.replace('\\', '/');
			File dir = new File(decPath);
			if(dir.isDirectory()){
				File[] files = dir.listFiles();
				for(int i=0;files!=null && i<files.length;i++){
					files[i].delete();
				}
				//dir.delete();
			}else{
				if(dir.isFile()) dir.delete();
			}
			
		}catch(Exception e){
			LOGGER.error("removeFileDir Fail!! - "+e.getMessage());
		}
	}
	
	/** 문서뷰어 오류 메세지 조회*/
	public String getViewMsg(String msgKey){
		String[][] msgs = new String[][]{{"4","DRM이 걸려 있는 파일일 경우"},
				{"52","mod_path 에 설정한 디렉토리가 잘못됐을 경우"},
				{"53","Template 디렉토리가 없을 경우"},
				{"6","결과 HTML 파일을 출력할 디렉토리가 없을 경우"},
				{"7","결과 HTML 파일을 출력할 디렉토리에 쓰기 권한이 없을 경우"},
				{"54","기타 오류로, 상세 오류 코드를 확인해야 합니다"},
				{"2","변환을 지원하지 않는 파일일 경우"},
				{"253","사용 가능한 CPU core 개수를 초과하였을 경우"},
				{"254","실행 가능 제한 날짜를 초과하였을 경우"},
				{"3","암호화된 파일일 경우"}
				};
		for(String[] msg : msgs){
			if(msg[0].equals(msgKey)){
				return msg[1];
			}
		}
		return null;
	}
}
