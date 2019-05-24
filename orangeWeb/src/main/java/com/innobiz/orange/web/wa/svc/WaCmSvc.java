package com.innobiz.orange.web.wa.svc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.pt.secu.SecuUtil;

@Service
public class WaCmSvc {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 요청 경로에 따른 list view set page model에 세팅 */
	public void setPageName(ModelMap model , String path , String prefix , String suffix , String bizNm ){
		String pageName = prefix;
		pageName+= bizNm == null ? "" : bizNm;
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
		model.put("path", path);
		String[] pages = {"list","view","set","trans"};
		for(String prefix : pages){
			setPageName(model, path, prefix , null , bizNm );
		}
		setPageName(model, path, "trans" , "Del" , bizNm);
		return path;
	}
	
	public String getPathSuffix(String path){
		String suffix = "";
		String[] pages = {"list","view","set","trans"};
		for(String page : pages){
			if(path.substring(0, page.length()).equals(page)){
				suffix = path.substring(page.length());
				break;
			}
		}
		if("".equals(suffix)) suffix = "Doc";
		return suffix;
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
}
