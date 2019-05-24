<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		com.innobiz.orange.web.pt.secu.SecuUtil,
		com.innobiz.orange.web.pt.secu.LoginSession,
		com.innobiz.orange.web.cm.utils.EscapeUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="id" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="href" required="false"
%><%@ attribute name="onclick" required="false"
%><%@ attribute name="auth" required="false"
%><%@ attribute name="ownerUid" required="false"
%><%@ attribute name="image" required="false"
%><%@ attribute name="popYn" required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="extData" required="false"
%><%
/**
HTML	: <a href=""><span>버튼텍스트</span></a>
설명		: 테이블 내에 들어가는 작은 버튼, button.tag 에서 <li>를 제거한 형태

title	: 실제로 운영환경에 가면 titleId 가 사용되며(프로퍼티 파일에서 읽어서 다국어 치환) 삭제 예정, 우선 html 작업용
alt		: 소스의 가독성을 높이기위해 사용되지는 않지만 한글명 넣을것

auth	: S:슈퍼 > A:관리 > M:수정 > W:쓰기 > R:읽기

*/
if(SecuUtil.hasAuth(request, auth, ownerUid)){
	
	if(titleId!=null){
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
	
	if((image==null || image.isEmpty()) && titleId!=null){
		if(titleId.equals("cm.btn.up")) image = "ico_wup.png";
		else if(titleId.equals("cm.btn.down")) image = "ico_wdown.png";
		else if(titleId.equals("cm.btn.left")) image = "ico_left.png";
		else if(titleId.equals("cm.btn.right")) image = "ico_right.png";
		else if(titleId.equals("cm.btn.del")) image = "ico_wdelete.png";
		else if(titleId.equals("cm.btn.plus")) image = "ico_wadd.png";
		else if(titleId.equals("cm.btn.minus")) image = "ico_wminus.png";
	}
	
	style = style==null ? "" : " style=\""+style+"\"";
	
	if(extData!=null && !extData.isEmpty()){
		StringBuilder builder = new StringBuilder();
		int p;
		String key, value;
		boolean hasData = false;
		for(String item : extData.split(",")){
			p = item.indexOf('=');
			if(p>0){
				key = item.substring(0,p);
				value = item.substring(p+1);
				if(!key.isEmpty() && !value.isEmpty()){
					hasData = true;
					builder.append(" data-").append(key).append("=\"").append(EscapeUtil.escapeValue(value)).append("\"");
				}
			}
		}
		if(hasData) extData = builder.toString();
		else extData = "";
	} else {
		extData = "";
	}
	
	%><a<%= id%> href="<%=href%>"<%=onclick%> title="<%= desc%>"<%= style%><%= extData%>><img src="${_cxPth}/images/${_skin}/<%=image%>" width="20" height="20" alt="<%=title%>" /></a><%
}

%>