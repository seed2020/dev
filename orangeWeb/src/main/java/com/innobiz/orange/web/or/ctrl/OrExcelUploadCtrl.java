package com.innobiz.orange.web.or.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.svc.OrExcelUploadSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;

@Controller
public class OrExcelUploadCtrl {

//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(OrExcelUploadCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Autowired
	private UploadManager uploadManager;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 조직 엑셀 업로드 서비스 */
	@Autowired
	OrExcelUploadSvc orExcelUploadSvc;
	
	/** [팝업] 조직도 - 엑셀 업로드 */
	@RequestMapping(value = "/pt/adm/org/setExcelPop")
	public String setExcelUpload(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(!"U0000001".equals(userVo.getUserUid())){
			request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).forward(request, response);
			return null;
		}
		
		return LayoutUtil.getJspPath("/pt/adm/org/setExcelPop");
	}
	
	/** [팝업] 조직도 - 엑셀 업로드 */
	@RequestMapping(value = "/pt/adm/org/transExcel")
	public String transExcelUpload(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		if(!"U0000001".equals(userVo.getUserUid())){
			request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).forward(request, response);
			return null;
		}
		

		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "or");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
			
			File file = fileMap.get("excel");
			if(file==null || !file.isFile()){
				// cm.msg.noFileSelected=선택한 파일이 없습니다.
				throw new CmException("cm.msg.noFileSelected", locale);
			}

			ArrayList<String> errorList = new ArrayList<String>();
			QueryQueue queryQueue = new QueryQueue();
			orExcelUploadSvc.processExcel(file, queryQueue, userVo, errorList);
			
			if(errorList.isEmpty()){
				// 코드 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP, CacheConfig.USER);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
				orCmSvc.setUsers();
				
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
		} catch(NullPointerException e){
			e.printStackTrace();
			model.put("message", "[Excel Format Error] - Download sample file and fill the data !");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
//			model.put("exception", e);
		} finally {
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 조직도 삭제 */
	@RequestMapping(value = "/pt/adm/org/transExcelDelAjx")
	public String transSecuAjx(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			
			if(!"U0000001".equals(userVo.getUserUid())){
				request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).forward(request, response);
				return null;
			}
			
			
			QueryQueue queryQueue = new QueryQueue();
			
			OrRescBVo orRescBVo = new OrRescBVo();
			orRescBVo.setWhereSqllet("RESC_ID NOT IN ('R0000001', 'R0000002')");
			queryQueue.delete(orRescBVo);
			
			OrOfseDVo orOfseDVo = new OrOfseDVo();
			queryQueue.delete(orOfseDVo);
			
			OrOrgCntcDVo orOrgCntcDVo = new OrOrgCntcDVo();
			queryQueue.delete(orOrgCntcDVo);
			
			OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
			queryQueue.delete(orOrgApvDVo);
			
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			queryQueue.delete(orOrgBVo);
			
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			queryQueue.delete(orUserImgDVo);
			
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			queryQueue.delete(orUserPinfoDVo);
			
			OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
			queryQueue.delete(orUserPwDVo);
			
			OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
			queryQueue.delete(orUserRoleRVo);
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setWhereSqllet("USER_UID != 'U0000001'");
			queryQueue.delete(orUserBVo);
			
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setWhereSqllet("ODUR_UID != 'U0000001'");
			queryQueue.delete(orOdurBVo);
			
			commonSvc.execute(queryQueue);
			
			//cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			model.put("message", message);
		}
		
		return JsonUtil.returnJson(model);
	}
}
