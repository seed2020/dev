<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			java.util.Map,
			com.innobiz.orange.web.pt.utils.SysSetupUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil"
%><%@ attribute name="termId" required="true"
%><%@ attribute name="var" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="langTypCd" required="false"
%><%@ attribute name="charSeperator" required="false"
%><%
/*
	용어설정상세(PT_TERM_SETUP_D) 테이블에 설정된 용어값을 구하고
	해당 값이 null 이면 Message 파일에서 가져다가 해당 값을 출력함

	termId	: 용어ID - 용어설정을 한 경우 설정된 용어에서 가져옴

	Ctrl 에서 term 세팅 방법
	//조직도 용어설정 맵 가져옴 - 직무, 직급, 직위, 역할, 직책
	String setupClsId = "or.term";
	Map<String, String> orTermMap = ptSysSetupSvc.getTermMap(setupClsId, langTypCd);
	model.put(setupClsId, orTermMap);
	
*/

	int p = termId.lastIndexOf('.');
	String termVa = null;
	if(p>0){
		if(termId.indexOf(".term")>0){
			String setupClsId = termId.substring(0, p);
			String setupId = termId.substring(p+1);
			if(langTypCd==null || langTypCd.isEmpty()) langTypCd = SessionUtil.getLangTypCd(request);
			Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, langTypCd);
			termVa = termMap==null ? null : termMap.get(setupId);
		}
		
		if(termVa==null){
			Locale locale = (langTypCd==null || langTypCd.isEmpty())
					? SessionUtil.getLocale(request) : SessionUtil.toLocale(langTypCd);
			termVa = MessageProperties.getInstance().getMessage(termId, locale);
		}
	}
	
	if(termVa!=null && !termVa.isEmpty() && charSeperator!=null && !charSeperator.isEmpty()){
		StringBuilder builder = new StringBuilder(64);
		boolean first = true;
		for(char c : termVa.toCharArray()){
			if(first) first=false;
			else builder.append(charSeperator);
			builder.append(c);
		}
		termVa = builder.toString();
	}
	
	if(var!=null && !var.isEmpty()){
		if(termVa==null) request.removeAttribute(var);
		else request.setAttribute(var, termVa);
	} else {
		%><%= termVa%><%
	}
	
%>