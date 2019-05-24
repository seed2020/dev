<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		java.util.List,
		com.innobiz.orange.web.pt.secu.UserVo,
		com.innobiz.orange.web.cm.utils.EscapeUtil,
		com.innobiz.orange.web.pt.utils.LoutUtil,
		com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="vo"    	required="true" type="PtMnuLoutCombDVo"
%><%@ attribute name="tabCount"	required="true" type="java.lang.Integer"
%><%
/*
	좌측 메뉴의 트리 메뉴 구성용
*/
	UserVo userVo = (UserVo)session.getAttribute("userVo");

	StringBuilder tabBuilder = new StringBuilder();
	int i, size = tabCount;
	for(i=0;i<size;i++) tabBuilder.append('\t');
	String tab = tabBuilder.toString();
	
	request.setAttribute("leftVo", vo);
	PtMnuLoutCombDVo child;
	List<PtMnuLoutCombDVo> childList = vo.getChildList();
	
	boolean hasChild = (childList==null || childList.isEmpty());
	String treeIcon = hasChild ? "sidetree_open" : "sidetree_close";
	
	String menuFnc = vo.getMnuFnc()!= null ? "javascript:"+EscapeUtil.escapeValue(LoutUtil.converTypedParam(vo.getMnuFnc(), userVo))+";" :
			!hasChild ? "javascript:toggleLeftMenu('Left"+vo.getMnuLoutCombId()+"');" : LoutUtil.converTypedParam(vo.getMnuUrl(), userVo);
	
%>			<%=tab%><li id="Left${leftVo.mnuLoutCombId}">
			<%=tab%><a href="javascript:toggleLeftMenu('Left${leftVo.mnuLoutCombId}');" class="side_control"><img src="${_cxPth}/images/${_skin}/<%=treeIcon%>.gif"></a>
			<%=tab%><a href="<%= menuFnc%>" title="<%= EscapeUtil.escapeValue(vo.getRescNm())
				%>" class="menu"><%= EscapeUtil.escapeHTML(vo.getRescNm()) %></a><%
		
		if(childList!=null && !childList.isEmpty()){
%>
			<%=tab%><ul id="subLeft${leftVo.mnuLoutCombId}" style="display: none;"><%
			size = childList.size();
			for(i=0;i<size;i++){
				request.setAttribute("childMenu", childList.get(i));
				%>
<u:leftTree vo="${childMenu}" tabCount="${tabCount+1}" /><%
			}%>
			<%=tab%></ul><%
		}
%>
			<%=tab%></li>