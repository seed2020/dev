<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		javax.servlet.jsp.tagext.JspFragment,
		com.innobiz.orange.web.cm.utils.ArrayUtil,
		com.innobiz.orange.web.pt.secu.UserVo,
		com.innobiz.orange.web.pt.secu.LoginSession,
		com.innobiz.orange.web.pt.utils.PtConstant"
%><%

	UserVo userVo = LoginSession.getUser(request);
	String[] userAdminGrpIds = userVo.getAdminAuthGrpIds();
	
	if(ArrayUtil.isInArray(userAdminGrpIds, PtConstant.AUTH_SYS_ADMIN)
			|| ArrayUtil.isInArray(userAdminGrpIds, PtConstant.AUTH_ADMIN)){
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
	}
%>