package com.innobiz.orange.web.pt.svc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.pt.secu.UserVo;

/** 메뉴 파라미터 세팅 서비스 */
@Service
public class PtMnuParamSvc {

	/** 모듈별 파라미터를  strMnuParam, arrMnuParam 에 세팅함 */
	public void processParam(HttpServletRequest request, String uri, UserVo userVo){
		
		if(uri.startsWith("/ap")){
			if(uri.startsWith("/ap/box") || uri.startsWith("/ap/adm/box")){
				processByNames(request, new String[]{"formId", "makDeptId"});
			}
//			String bxId = request.getParameter("bxId");
//			if("admOngoFormBx".equals(bxId) || "admApvdFormBx".equals(bxId)){
//				processByNames(request, new String[]{"formId"});
//			} else if(uri.startsWith("/ap/box")){
//				processByNames(request, new String[]{"formId"});
//			}
		}
	}
	
	/** paramNames 파라미터를  strMnuParam, arrMnuParam 에 세팅함 */
	private void processByNames(HttpServletRequest request, String[] paramNames){
		
		StringBuilder builder = new StringBuilder(40);
		String na, va;
		List<String[]> arrParam = new ArrayList<String[]>();
		
		for(int i=0;i<paramNames.length;i++){
			na = paramNames[i];
			va = request.getParameter(na);
			if(va==null || va.isEmpty()) continue;
			
			builder.append('&').append(na).append('=').append(va);
			arrParam.add(new String[]{na, va});
		}
		
		if(!arrParam.isEmpty()){
			request.setAttribute("strMnuParam", builder.toString());
			request.setAttribute("arrMnuParam", arrParam);
		}
		
	}
}
