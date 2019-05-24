package com.innobiz.orange.web.wb.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wb.vo.WbBcAgntAdmBVo;
import com.innobiz.orange.web.wb.vo.WbBcAgntSetupBVo;

@Service
public class WbCmSvc {
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "bcfiles";
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcFileSvc wbBcFileSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcMetngFileSvc wbBcMetngFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("WB_BC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'B', 8);
		} else if ("WB_BC_FLD_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'F', 8);
		} else if ("WB_BC_CNTC_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'C', 8);
		} else if ("WB_BC_CLNS_C".equals(tableName)) {
			return commonSvc.createId(tableName, 'L', 8);
		} else if ("WB_BC_METNG_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'M',  8);
		} else if ("WB_BC_METNG_ATND_R".equals(tableName)) {
			return commonSvc.createId(tableName, 'A',  8);
		} else if ("WM_RESC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'R',  8);
		} else if ("WB_PUB_FLD_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'O', 8);
		} else if ("WB_PUB_BC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'P', 8);
		} 
		
		return null;
	}
	
	/** 첨부파일 ID 생성 */
	public Integer createFileId(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 대리관리자 권한 체크 */
	public Map<String,Object> getAgntInfoMap(HttpServletRequest request,String schBcRegrUid , String userUid , String authCd )throws SQLException, CmException {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		
		/** 사용자의 명함 대리 관리자 정보 */
		WbBcAgntAdmBVo wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		wbBcAgntAdmBVo.setRegrUid(schBcRegrUid);
		wbBcAgntAdmBVo.setAgntAdmUid(userUid);
		/** 사용자의 명함 대리 관리자 정보 */
		//wbBcAgntAdmBVo.setRegrUid(schBcRegrUid);
		//wbBcAgntAdmBVo.setAgntAdmUid(userVo.getUserUid());
		wbBcAgntAdmBVo = (WbBcAgntAdmBVo)commonSvc.queryVo(wbBcAgntAdmBVo);
		
		// 대리관리자가 아닌경우 관리자의 권한을 회수 했거나 파라미터 변경을 통한 접근이므로 오류 메세지를 출력한다.[추후 메세지 변경 필요: ex)대리관리자가 아닙니다. ]
		if(wbBcAgntAdmBVo == null || ( authCd != null && !authCd.equals(wbBcAgntAdmBVo.getAuthCd()) ) ){
			//rsltMap.put("message", "cm.msg.notValidCall");
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			throw new CmException(msg);
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
		}
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(wbBcAgntAdmBVo.getRegrUid());
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		
		rsltMap.put("compId", orUserBVo.getCompId());
		
		rsltMap.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
		
		return rsltMap;
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	/** 첨부파일 목록 조회 */
	public ModelAndView getFileList(HttpServletRequest request,String fileIds , String actionParam) throws SQLException, IOException {
		List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
		// fileId
		String[] fileIdArray = fileIds.split(",");
		
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		
		CommonFileVo fileVo = null; 
		for (String fileId : fileIdArray) {
			// 첨부파일
			if(actionParam != null && "metng".equals(actionParam)){
				fileVo = wbBcMetngFileSvc.getFileVo(Integer.valueOf(fileId));
			}else{
				fileVo = wbBcFileSvc.getFileVo(Integer.valueOf(fileId));
			}
			
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
					
	}
	
	/** 코드 목록 조회 */
	public List<PtCdBVo> getCdList(String clsCd, String langTypCd, String useYn) throws SQLException {
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setClsCd(clsCd);
		ptCdBVo.setQueryLang(langTypCd);
		ptCdBVo.setUseYn(useYn);
		ptCdBVo.setOrderBy("(SELECT RESC_VA FROM PT_RESC_B WHERE RESC_ID = T.RESC_ID AND LANG_TYP_CD = '"+langTypCd+"')");
		@SuppressWarnings("unchecked")
		List<PtCdBVo> ptCdBList = (List<PtCdBVo>)commonDao.queryList(ptCdBVo);
		return ptCdBList;
	}
	
	/** 요청 경로에 따른 list view set page model에 세팅 */
	public void setPageName(ModelMap model , String path , String prefix , String suffix , String bizNm ){
		String pageName = prefix;
		if(path.indexOf("Open") > -1 ) pageName += "Open";
		else if(path.indexOf("Agnt") > -1 ) pageName += "Agnt";
		else if(path.indexOf("Pub") > -1 ) pageName += "Pub";
		else if(path.indexOf("All") > -1 ) pageName += "All";
		pageName+= bizNm == null ? "Bc" : bizNm;
		if(suffix != null){
			pageName+=suffix;
		}
		//System.out.println(prefix+(suffix != null ? suffix : "")+"Page = "+pageName);
		model.put(prefix+(suffix != null ? suffix : "")+"Page", pageName);
	}
	
	/** 요청 경로 */
	public String getRequestPath( HttpServletRequest request , ModelMap model , String bizNm){
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		String[] pages = {"list","view","set","trans","find"};
		for(String prefix : pages){
			setPageName(model, path, prefix , null , bizNm );
		}
		setPageName(model, path, "trans" , "Del" , bizNm);
		return path;
	}
	
	/** 대리명함 사용자 ID 조회 */
	public String getSchBcRegrUid(String schBcRegrUid , UserVo userVo , String langTypCd , ModelMap model) throws SQLException{
		String checkUserUid = null;
		WbBcAgntAdmBVo wbBcAgntAdmBVo = null;
		if(schBcRegrUid == null ){//조회UID가 null 인경우 
			// 대리명함기본 설정 조회
			WbBcAgntSetupBVo wbBcAgntSetupBVo = new WbBcAgntSetupBVo();
			wbBcAgntSetupBVo.setRegrUid(userVo.getUserUid());
			wbBcAgntSetupBVo = (WbBcAgntSetupBVo)commonSvc.queryVo(wbBcAgntSetupBVo);
			
			if(wbBcAgntSetupBVo != null ) { //기본설정이 있을경우 해당 Uid를 조회UID에 넣어준다.
				checkUserUid = wbBcAgntSetupBVo.getBcRegrUid();
			}
		}
		
		wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		if(schBcRegrUid != null && !schBcRegrUid.isEmpty()){//조회 UID가 있을경우 해당 대리정보가 있는지 조회하기 위해 해당 UID를 세팅한다.
			checkUserUid = schBcRegrUid;
		}
	
		if(checkUserUid != null){//체크할 조회UID가 있을경우
			wbBcAgntAdmBVo.setRegrUid(checkUserUid);
			wbBcAgntAdmBVo.setAgntAdmUid(userVo.getUserUid());
			wbBcAgntAdmBVo = (WbBcAgntAdmBVo)commonDao.queryVo(wbBcAgntAdmBVo);//조회UID에 대한 권한 정보를 가져온다.
			
			if(wbBcAgntAdmBVo != null ) {// 해당 UID가 대리관리자로 지정되어 있는경우 
				schBcRegrUid = wbBcAgntAdmBVo.getRegrUid();
				model.put("wbBcAgntAdmBVo", wbBcAgntAdmBVo);
			}
			else schBcRegrUid = null;
		}
		
		// 대리인 지정 대상 목록[조회조건]
		wbBcAgntAdmBVo = new WbBcAgntAdmBVo();
		wbBcAgntAdmBVo.setAgntAdmUid(userVo.getUserUid());
		wbBcAgntAdmBVo.setQueryLang(langTypCd);
		
		// 목록 조회
		@SuppressWarnings("unchecked")
		List<WbBcAgntAdmBVo> agntSetupList = (List<WbBcAgntAdmBVo>)commonDao.queryList(wbBcAgntAdmBVo);
		model.put("agntSetupList", agntSetupList);
		
		return schBcRegrUid;
	}
	
}
