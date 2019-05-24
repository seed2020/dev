<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Map,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.pt.secu.SecuUtil,
			com.innobiz.orange.web.pt.utils.SysSetupUtil"
%><%@ attribute name="id" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="termId" required="false"
%><%@ attribute name="href" required="false"
%><%@ attribute name="onclick" required="false"
%><%@ attribute name="auth" required="false"
%><%@ attribute name="ownerUid" required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="popYn" required="false"
%><%@ attribute name="alt" required="false"
%><%
/**
HTML	: <li><a href=""><span>버튼텍스트</span></a></li>
설명		: 하단의 큰 버튼에 사용함

title	: 실제로 운영환경에 가면 titleId 가 사용되며(프로퍼티 파일에서 읽어서 다국어 치환) 삭제 예정, 우선 html 작업용
alt		: 소스의 가독성을 높이기위해 사용되지는 않지만 한글명 넣을것

auth	: S:슈퍼 > A:관리 > M:수정 > W:쓰기 > R:읽기

termId	: 용어ID - 용어설정을 한 경우 설정된 용어에서 가져옴

	Ctrl 에서 term 세팅 방법
	//조직도 용어설정 맵 가져옴 - 직무, 직급, 직위, 역할, 직책
	String setupClsId = "or.term";
	Map<String, String> orTermMap = ptSysSetupSvc.getTermMap(setupClsId, langTypCd);
	model.put(setupClsId, orTermMap);

*/
if(SecuUtil.hasAuth(request, auth, ownerUid)){
	
	// 용어ID
	if(termId!=null){
		int p = termId.lastIndexOf('.');
		if(p>0){
			String setupClsId = termId.substring(0, p);
			String setupId = termId.substring(p+1);
			Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, request);
			title = termMap==null ? null : termMap.get(setupId);
			if(title==null){
				title = MessageProperties.getInstance().getMessage(termId, request);
			}
		}
	} else if(titleId!=null){
		title = MessageProperties.getInstance().getMessage(titleId, request);
	}
	
	if(id==null || id.isEmpty()) id="";
	else {
		id = " id=\""+id+"\"";
	}
	if(href==null || href.isEmpty()) href="javascript:void(0);";
	if(onclick==null || onclick.isEmpty()) onclick = "";
	else {
		onclick = " onclick=\""+onclick+"\"";
	}
	
	String desc = title;
	if("Y".equals(popYn)){
		desc = title+" - "+MessageProperties.getInstance().getMessage("cm.pop", request);
	}
	
	style = (style==null) ? "" : " style=\""+style+"\"";
	
	%><li<%= id%> class="basic"<%=style%>><a href="<%=href%>"<%=onclick%> title="<%=desc%>"><span><%= title%></span></a></li><%
}

%>