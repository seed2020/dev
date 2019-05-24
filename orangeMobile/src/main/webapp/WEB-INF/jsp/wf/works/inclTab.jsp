<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabList" test="${!empty param.tabList}" value="${param.tabList }" elseValue="${wfFormMobDVo.tabVa }"
/><c:if test="${!empty tabList }"><u:convertJson var="tabVa" value="${tabList }"
/><c:set var="tabList" value="${tabVa['tabList'] }"/><c:if test="${!empty tabList }"><div class="tabarea" id="tabBtnArea">
<div class="tabsize"><dl><c:forEach items="${tabList}" var="tab" varStatus="status"
><u:set var="tabTitle" test="${!empty tabLangListMap && !empty tabLangListMap[tab['loutId']] && !empty tabLangListMap[tab['loutId']]['dataJson'] && !empty tabLangListMap[tab['loutId']]['dataJson'][_lang]}" value="${tabLangListMap[tab['loutId']]['dataJson'][_lang] }" elseValue="${tab['title'] }"
/><dd class="tab${status.first ? '_on' : ''}" onclick="$layout.tab.on($(this).attr('id'));$layout.adjustBodyHtml('bodyHtmlArea');" id="${tab['loutId'] }">${tabTitle }</dd></c:forEach
></dl></div><div class="tab_icol" style="display:none" id="toLeft"></div><div class="tab_icor" style="display:none" id="toRight"></div>
</div></c:if>
</c:if>