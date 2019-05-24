<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 팝업 열기 %>
var gResultWin = null;<%
// 팝업으로 링크 열기 %>
function viewSrchDetlPop(url){
	var popTilte = "searchResultPop";
	if(window.name==popTilte) window.name = "";
	if(gResultWin!=null) gResultWin.close();
	gResultWin = window.open(url, popTilte);
}<%
// 메일 제목 클릭 %>
function shViewMailPop(url){
	viewSrchDetlPop("${mailUrl}&mailRd="+url.replace("?","&"));
}<%
// 메일 박스 클릭 %>
function shGoMail(url){
	location.href = "${mailUrl}&mailRd="+url.replace("?","&");
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	$("#searchForm #kwd").select().focus();
});
//-->
</script>
<style>
<!--
.searchViewArea { width:100%; padding:0; margin:0; }
.searchViewArea ul { clear:both; padding:0; margin:0; padding-top:10px; padding-bottom:10px; }
.searchViewArea ul li { list-style-type:none; }

.integ_search dl { width: 100%; overflow: hidden; }
.integ_search dt { float: left; font: 12px "dotum", "arial"; font-weight:bold; }
.integ_search dd { float: left; font: 12px "dotum", "arial"; padding-left:20px; }
.smry { padding:0; padding-top:6px; padding-bottom:6px; line-height: 18px; }

-->
</style>
<u:title title="${titleValue}" />

<%// 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./index.do"><c:if
		test="${not empty mdRid}">
	<input type="hidden" name="mdRid" value="${mdRid}" /></c:if><c:if
		test="${not empty mdBxId}">
	<input type="hidden" name="mdBxId" value="${mdBxId}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cm.schWord" alt="검색어" /></td>
		<td><u:input id="kwd" titleId="cm.schWord" style="width:400px;" value="${param.kwd}" maxByte="50"/></td>
		<td style="width:20px"></td>
		<u:checkbox value="Y" id="subjYn" name="subjYn" checked="${not empty subjYn or param.subjYn == 'Y'}" titleId="ap.doc.docSubj" />
		<u:checkbox value="Y" id="bodyYn" name="bodyYn" checked="${not empty bodyYn or param.bodyYn == 'Y'}" titleId="ap.btn.body" />
		<u:checkbox value="Y" id="attchYn" name="attchYn" checked="${not empty attchYn or param.attchYn == 'Y'}" titleId="ap.btn.att" />
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>


<%// 목록 %>
<div class="searchViewArea"><c:forEach
	items="${mdRids}" var="currMdRid" varStatus="mdRidStatus"><c:if
	
	test="${empty param.mdRid or param.mdRid == currMdRid}">
<div style="height:${mdRidStatus.first ? '10px' : '25px'};"></div><u:convert
	srcId="shSrchVoList${currMdRid}" var="shSrchVoList" /><u:convert
	srcId="navidXmlList${currMdRid}" var="navidXmlList" /><c:if
	
	test="${not mdRidStatus.first and empty param.mdRid}">
<div class="apBtnLine" style="filter:Alpha(Opacity=40);Opacity:0.4;"></div></c:if><c:if
	test="${empty param.mdRid}">
<u:title titleId="pt.sh.menu.${currMdRid}" type="small" /></c:if><c:if


	test="${not empty shSrchVoList}">
<c:forEach
	items="${shSrchVoList}" var="shSrchVo" varStatus="status">
<ul>
	<li class="integ_search"><dl>
		<dt class="search_title"><a href="javascript:void(0)" onclick="javascript:viewSrchDetlPop('${shSrchVo.url}');" title="<u:out value="${shSrchVo.subj}" type="value"/> - <u:msg titleId="cm.pop"/>"><u:out value="${shSrchVo.subj}" /></a></dt>
		<dd class="search_title"><c:if
			test="${currMdRid=='AP'}"><c:if
				test="${shSrchVo.mdBxId=='distRecLst'}"><a href="<u:authUrl
					url="/ap/box/listApvBx.do?bxId=distRecLst" />"><u:out value="${shSrchVo.mdBxNm}" /></a></c:if><c:if
				test="${shSrchVo.mdBxId!='distRecLst'}"><a href="<u:authUrl
					url="/ap/box/listApvRecBx.do?bxId=${shSrchVo.mdBxId}" />"><u:out value="${shSrchVo.mdBxNm}" /></a></c:if></c:if><c:if
			test="${currMdRid=='BB'}"><a href="<u:authUrl
					url="/bb/listBull.do?brdId=${shSrchVo.mdBxId}" />"><u:out value="${shSrchVo.mdBxNm}" /></a></c:if><c:if
			test="${currMdRid=='DM'}"><u:out value="${shSrchVo.mdBxNm}" /></c:if></dd>
		<dd class="search_title"><c:if
			test="${not (shSrchVo.mdBxId == 'recvRecLst' or shSrchVo.mdBxId == 'distRecLst')}"><a href="javascript:viewUserPop('${shSrchVo.regrUid}')"><u:out value="${shSrchVo.regrNm}" /></a></c:if><c:if
			test="${shSrchVo.mdBxId == 'recvRecLst' or shSrchVo.mdBxId == 'distRecLst'}"><u:out value="${shSrchVo.regrNm}" /></c:if></dd>
		<dd><u:out value="${shSrchVo.regDt}" /></dd></dl></li>
	<li class="smry"><u:out value="${shSrchVo.smry}" /></li>
</ul>
	</c:forEach><u:convert
		srcId="more${currMdRid}" var="moreCount" /><c:if
		test="${not empty moreCount}">
<div style="text-align: center"><a href="/sh/index.do?mdRid=${currMdRid}&kwd=${kwdEnc}"><u:msg titleId="em.btn.more" alt="더보기" /></a>(<span class="strong">${moreCount}</span>)</div>
		</c:if><u:convert
		srcId="recodeCount${currMdRid}" var="recodeCount" /><c:if
		test="${not empty pageRowCnt and not empty recodeCount}">
<u:pagination pageRowCnt="${pageRowCnt}" recodeCount="${recodeCount}" noLeftSelect="true" />
		</c:if>
	
	</c:if><c:if
	
	
	
	test="${not empty navidXmlList}">
<c:forEach
	items="${navidXmlList}" var="navidXml" varStatus="status">
<ul>
	<li class="integ_search"><dl>
		<dt class="search_title"><a href="javascript:void(0)" onclick="javascript:shViewMailPop('${navidXml.getValue('subject_link')}');" title="<u:out value="${navidXml.getValue('subject')}" type="value"/> - <u:msg titleId="cm.pop"/>"><u:out value="${navidXml.getValue('subject')}" /></a></dt>
		<dd class="search_title"><a href="javascript:void(0)" onclick="javascript:shGoMail('${navidXml.getValue('mailbox_name_link')}');"><u:out value="${navidXml.getValue('mailbox_name')}" /></a></dd>
		<dd class="search_title"><a href="javascript:void(0)" onclick="javascript:mailToPop('${navidXml.getValue('from_mail')}');"><u:out value="${navidXml.getValue('from_name')}" /></a></dd>
		<dd><u:out value="${navidXml.getValue('receive_time', 'maildate')}" /></dd></dl></li>
	<li class="smry"><u:out value="${navidXml.getValue('attach_file_name')}" /></li>
</ul>
	</c:forEach><u:convert
		srcId="more${currMdRid}" var="moreCount" /><c:if
		test="${not empty moreCount}">
<div style="text-align: center"><a href="/sh/index.do?mdRid=${currMdRid}&kwd=${kwdEnc}"><u:msg titleId="em.btn.more" alt="더보기" /></a>(<span class="strong">${moreCount}</span>)</div>
		</c:if><u:convert
		srcId="recodeCount${currMdRid}" var="recodeCount" /><c:if
		test="${not empty pageRowCnt and not empty recodeCount}">
<u:pagination pageRowCnt="${pageRowCnt}" recodeCount="${recodeCount}" noLeftSelect="true" />
		</c:if>
	
	</c:if><c:if
	test="${empty shSrchVoList and empty navidXmlList}">
<div style="padding-top:10px; padding-bottom:10px;"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></div>	
	</c:if>
</c:if></c:forEach>
</div>

