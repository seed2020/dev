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
%><%@ attribute name="spanStyle" required="false"
%><%@ attribute name="icon" required="false"
%><%@ attribute name="img" required="false"
%><%@ attribute name="imgW" required="false"
%><%@ attribute name="imgH" required="false"
%><%@ attribute name="popYn" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="dataList"  required="false"
%><%
/**
HTML	: <a href=""><span>버튼텍스트</span></a>
설명		: 테이블 내에 들어가는 작은 버튼, button.tag 에서 <li>를 제거한 형태

title	: 실제로 운영환경에 가면 titleId 가 사용되며(프로퍼티 파일에서 읽어서 다국어 치환) 삭제 예정, 우선 html 작업용
alt		: 소스의 가독성을 높이기위해 사용되지는 않지만 한글명 넣을것

auth	: S:슈퍼 > A:관리 > M:수정 > W:쓰기 > R:읽기

termId	: 용어ID - 용어설정을 한 경우 설정된 용어에서 가져옴

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

	if(img==null){
		img = "ico_search_s.png";
	} else {
		icon = "true";
	}
	if(imgW==null){
		imgW="14";
	}
	if(imgH==null){
		imgH="10";
	}
	style = (style==null) ? "" : " style=\""+style+"\"";
	
	spanStyle = (spanStyle==null || spanStyle.isEmpty()) ? "" : " style=\""+spanStyle+"\"";
	
	if(dataList!=null && !dataList.isEmpty()) dataList=" "+dataList;
	else dataList="";
	
	%><a<%= id%> href="<%=href%>"<%=onclick%> class="sbutton button_small" title="<%= desc%>"<%=style%><%= dataList%>><span<%=spanStyle%>><%if("true".equals(icon)){
	%><img src="${_cxPth}/images/${_skin}/<%=img%>" width="<%=imgW%>" height="<%=imgH%>" /><%}%><%= title%></span></a><%
}

%>