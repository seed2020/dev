package com.innobiz.orange.web.wh.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.wh.svc.WhExcelUploadSvc;
import com.innobiz.orange.web.wh.utils.WhConstant;

@Controller
public class WhExcelUploadCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Autowired
	private UploadManager uploadManager;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 조직 엑셀 업로드 서비스 */
	@Autowired
	private WhExcelUploadSvc whExcelUploadSvc;
	
	/** [팝업] - 엑셀 업로드 */
	@RequestMapping(value = {"/wh/adm/md/setExcelUploadPop", "/wh/adm/help/setExcelUploadPop"})
	public String setExcelUploadPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wh/adm/md/setExcelUploadPop");
	}
	
	/** [팝업] - 엑셀 업로드 */
	@RequestMapping(value = {"/wh/adm/md/transExcelUpload", "/wh/adm/help/transExcelUpload"})
	public String transExcelUpload(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);

		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wh");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			
			File file = fileMap.get("excel");
			if(file==null || !file.isFile()){
				// cm.msg.noFileSelected=선택한 파일이 없습니다.
				throw new CmException("cm.msg.noFileSelected", locale);
			}
			
			// 데이터만 업로드
			boolean dataOnly = request.getRequestURI().startsWith("/wh/adm/help/");
			
			ArrayList<String> errorList = new ArrayList<String>();
			QueryQueue queryQueue = new QueryQueue();
			if(dataOnly)
				whExcelUploadSvc.processExcelData(file, queryQueue, userVo, errorList);
			else
				whExcelUploadSvc.processExcel(file, queryQueue, userVo, errorList);
			
			if(errorList.isEmpty()){
				if(dataOnly){
					commonSvc.execute(queryQueue);
				}else{
					// 캐쉬 삭제
					String dbTime = ptCacheExpireSvc.getDbTime();
					ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
					commonSvc.execute(queryQueue);
					ptCacheExpireSvc.checkNow(WhConstant.MD);
				}
				
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("todo", "parent.reload();");
			} else {
				StringBuilder builder = new StringBuilder(512);
				
				if("ko".equals(userVo.getLangTypCd())){
					builder.append("엑셀 데이터 오류 입니다.\n");
				} else {
					builder.append("Invalid Excel Data.\n");
				}
				
				for(String errorMsg : errorList){
					builder.append('\n').append(errorMsg);
				}
				model.put("message", builder.toString());
			}
			
		} catch(CmException e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("todo", "parent.errPopClose();");
		} catch(NullPointerException e){
			e.printStackTrace();
			model.put("todo", "parent.errPopClose();");
			model.put("message", "[Excel Format Error] - Download sample file and fill the data !");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("todo", "parent.errPopClose();");
//			model.put("exception", e);
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
}
