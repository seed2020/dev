<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:if test="${(param.tabPage eq 'user' || param.tabPage eq 'preview') && !empty wfFormRegDVo.tabVa }"><u:convertJson var="tabList" value="${wfFormRegDVo.tabVa }"
/><u:tabGroup id="${param.loutPrefix }${idPrefix }tabArea" noBottomBlank="false"><c:forEach
	items="${tabList}" var="tab" varStatus="status"><u:set var="onclick" test="${param.tabPage eq 'user' }" value="tabHandler${loutPrefix }('${param.loutPrefix }${idPrefix }${tab['loutId'] }');" elseValue=""
	/><u:set var="tabTitle" test="${!empty tab['dataJson'] && !empty tab['dataJson'][_lang]}" value="${tab['dataJson'][_lang] }" elseValue="${tab['title'] }"/>
	<u:tab id="${param.loutPrefix }${idPrefix }tabArea" areaId="${param.loutPrefix }${idPrefix }${tab['loutId'] }" title="${tabTitle }" on="${status.first}" onclick="${onclick }" mobAStyle="border-bottom: none;"
		/></c:forEach>
</u:tabGroup></c:if>
<c:if test="${param.tabPage eq 'system' }">
<u:set var="tabDisplay" test="${empty wfFormRegDVo.tabVa }" value=" style=\"display:none;\"" elseValue=""/>
<div id="topArea">
<div style="width:99%; margin:0 0 0 2px; padding:3px 0 0 0;">
<div id="tabArea" class="utab"${tabDisplay }>
<div class="tab_left"><ul id="tabGroupArea"><c:set var="tabLastIndex" value="1"/>
<c:if test="${!empty wfFormRegDVo.tabVa }"
><u:convertJson var="tabList" value="${wfFormRegDVo.tabVa }"
/><c:forEach var="tab" items="${tabList }" varStatus="tabStatus"
><c:set var="tabIndex" value="${tabStatus.index }"
/><u:convertJsonString var="dataJson" value="${tab['dataJson'] }"
/><u:out var="dataJson" value="${dataJson }" type="value"
/><li class="basic${tabStatus.first ? '_open' : ''}" id="tab_${tabIndex }" data-areaid="${tab['loutId'] }" style="display:inline-block !important;" data-drag="Y"
<c:if test="${!empty dataJson }">data-json='${dataJson }'</c:if>
><div class="tab1" onclick="createTab('tabArea','${tabIndex}');"
><div class="tab2"><dl><dd class="title"
><span><u:set var="tabTitle" test="${!empty tab['dataJson'] && !empty tab['dataJson'][_lang]}" value="${tab['dataJson'][_lang] }" elseValue="${tab['title'] }"
/><u:out value="${tabTitle }"/></span></dd>
<dd class="btn"><a class="delete" onclick="deleteTab(event, 'tabArea', '${tabIndex}');" href="javascript:void(0);"><span>delete</span></a></dd>
</dl></div></div></li><c:if test="${tabStatus.last }"><c:set var="tabLastIndex" value="${tabIndex+1 }"/></c:if></c:forEach></c:if><c:if test="${empty wfFormRegDVo.tabVa }"
><li class="basic_open" id="tab_0" data-areaid="loutFormArea0" style="display:inline-block;"
><div class="tab1" onclick="createTab('tabArea',0);"><div class="tab2"><dl><dd class="title"><span><u:msg titleId="wf.jsp.form.tab.title" alt="새 탭"/></span></dd>
<dd class="btn"><a class="delete" onclick="deleteTab(event, 'tabArea', 0);" href="javascript:void(0);"><span>delete</span></a></dd>
</dl></div></div></li>
</c:if><li class="basic" id="tab_${tabLastIndex }" style="display:inline-block;"
><div class="tab1" onclick="createTab('tabArea','${tabLastIndex}');"><div class="tab2"><dl><dd class="title"><span>&nbsp;</span></dd>
<dd class="btn" style="display:none;"><a class="delete" onclick="deleteTab(event, 'tabArea', '${tabLastIndex}');" href="javascript:void(0);"><span>delete</span></a></dd>
</dl></div></div></li>
</ul></div></div></div></div></c:if><c:if test="${param.tabPage eq 'mob' && !empty wfFormRegDVo.tabVa}"
><c:if test="${!empty wfFormMobDVo }"><u:convertJson var="tabVa" value="${wfFormMobDVo.tabVa }" /></c:if>
<div style="width:99%; margin:0 0 0 2px; padding:3px 0 0 0;">
<div id="${param.loutPrefix }${idPrefix }tabArea" class="utab">
<div class="tab_left"><ul id="tabGroupArea">
<u:convertJson var="tabList" value="${wfFormRegDVo.tabVa }"
/><c:forEach var="tab" items="${tabList }" varStatus="tabStatus"
><c:set var="tabIndex" value="${tabStatus.index }"
/><c:set var="tabAreaId" value="${param.loutPrefix }${idPrefix }${tab['loutId'] }"
/><u:set var="tabUseYn" test="${!empty tabVa && !empty tabVa['tabAttr'] && !empty tabVa['tabAttr'][tabAreaId]}" value="Y" elseValue="N"
/><li class="basic${tabStatus.first ? '_open' : ''}" id="tab_${tabIndex }" data-areaid="${tabAreaId }" style="display:inline-block !important;" <c:if test="${tabUseYn eq 'Y' }">data-useyn="Y"</c:if>
><div class="tab1" onclick="changeTab('${param.loutPrefix }tabArea','${tabIndex}');"><div class="tab2"><dl><dd class="title"><span><u:out value="${tab['title'] }"/></span></dd>
<dd class="btn"><a class="check${tabUseYn eq 'Y' ? '_on' : ''}" onclick="checkTab(event, this);" href="javascript:void(0);" title="<u:msg titleId="cols.useYn" alt="사용여부"/>"><span><u:msg titleId="cols.useYn" alt="사용여부"/></span></a></dd>
</dl></div></div></li></c:forEach>
</ul></div></div></div></c:if>