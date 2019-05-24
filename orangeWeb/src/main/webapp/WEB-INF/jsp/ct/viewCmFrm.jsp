<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">

function view(url) {
	
}

$(document).ready(function() {
   
	$(".wbox").attr("class", "");
	setUniformCSS();
	
	
});


function getTotalElem(selector, root, col)
{
    // root가 없으면 top.document
    if (!root) root = $(top.document);
    if (!col) col = $();

    // element select
    col = col.add(root.find(selector));   

    // frame 전부 뒤지기
    root.find('iframe, frame').each(function(){
        // TODO same origin 체크를 해야함.
        // $(this)[0].src 에서 hostname을 추출해서 하는 방법 원츄
  
        var tag_name = $(this)[0].tagName.toLowerCase();
        var contents = null;


        // frame
        if (tag_name === 'frame') contents = $($(this)[0].contentDocument).contents();
        // iframe
        else contents = $(this).contents();

        col = col.add(getTotalElem(selector, contents, col));
    });

    return col;
}





</script>

<table width="100%">
	<tr>
		<td style="padding:15px">
<c:set var="title" value="${ctEstbBVo.ctNm}" />

<u:title title="${title}" alt="커뮤니티명" menuNameFirst="true" />

<style>
.itrobox { float: left; width: 99%; background: #ebf1f6; border: 1px solid #bfc8d2; padding: 5px 7px 3px 10px; }
.itrobox_tit { float: left; width: 48%; background:url("/images/blue/dot_search.png") no-repeat 0 6px; padding:3px 2px 5px 9px; }
.itrobox_body { width: 99%; line-height:18px; padding:5px 0 3px 0; }
</style>

<u:blank />

	<div class="titlearea">
		<div class="tit_left">
		<dl>
			<dd class="title_s"><u:msg titleId="ct.jsp.main.title01" alt="커뮤니티 소개" /></dd>
		</dl>
		</div>
	</div>
	
	<c:if test="${tpl['1'] != null}">
	<div class="itrobox" style="float: left; width: <c:if test="${tpl['2'] != null}">69%</c:if><c:if test="${tpl['2'] == null}">99%</c:if>; height: 198px;overflow:auto;">
		<dl>
			<c:if test="${ctFncMngPtList[0].ctItro != null}">
				<dd class="itrobox_body">${ctFncMngPtList[0].ctItro}</dd>
			</c:if>
			<c:if test="${ctFncMngPtList[0].ctItro == null}">
				<dd class="itrobox_body"><span id="ctItroSpan"></span></dd>
			</c:if>
		</dl>
	</div>
	</c:if>
	
	<c:if test="${tpl['2'] != null}">
	<div  style="float: right; width: <c:if test="${tpl['1'] != null}">29%</c:if><c:if test="${tpl['1'] == null}">99%</c:if>; height: 198px;">
		
			<c:if test="${imgFilePath != null}">
				<img src="${imgFilePath}"   style="height: 100%;  width:100%"/>
			</c:if>
	
	</div>
	</c:if>
	<u:blank />
	
	<c:set var="idx"	value= "0" />
	<c:if test="${tpl['34'] != null}">
	<% // 영역1 %>
	<div style="float: left; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt1" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
			<c:set var="idx"	value="${idx+1}" />
	</div>
	
	<% // 영역2 %>
	<div style="float: right; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
	
		<u:titleArea frameId="baordPt2" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
				<c:set var="idx"	value="${idx+1}" />
	</div>
	
	</c:if>
	
	<c:if test="${tpl['5'] != null}">
	<% // 영역3 %>
	<div style="float: left; width: 100%; height: 198px;">
		
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt3" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
		<c:set var="idx"	value="${idx+1}" />
	</div>
	</c:if>
	
	<c:if test="${tpl['67'] != null}">
	<% // 영역4 %>
	<div style="float: left; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
		<c:set var="idx"	value="${idx+1}" />
	
	</div>
	
	<% // 영역5 %>
	<div style="float: right; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
		<c:set var="idx"	value="${idx+1}" />
	</div>
	
	</c:if>
	
	<c:if test="${tpl['8'] != null}">
	<% // 영역6 %>
	<div style="float: left; width: 100%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
	
		<c:set var="idx"	value="${idx+1}" />
	</div>
	</c:if>
	
	<c:if test="${tpl['910'] != null}">
	
	<% // 영역7 %>
	<div style="float: left; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
	
		<c:set var="idx"	value="${idx+1}" />
	</div>
	
	<% // 영역8 %>
	<div style="float: right; width: 49%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
	
		<c:set var="idx"	value="${idx+1}" />
	</div>
	</c:if>
	
	<c:if test="${tpl['11'] != null}">
	
	<% // 영역7 %>
	<div style="float: left; width: 100%; height: 198px;">
	
		<div class="titlearea">
			<div class="tit_left">
			<dl>
			<dd class="title_s">${ctFncMngPtList[idx].ctFncNm}</dd>
			</dl>
			</div>
		</div>
		
		<u:titleArea frameId="baordPt4" frameSrc="${ctFncMngPtList[idx].ptUrl}${menuId}${ctFncMngPtList[idx].ctFncUid}&ctId=${ctId}"
			 	outerStyle="height: 170px;  overflow:hidden;" 
			 	innerStyle="padding:0 0px 0 0px;"
				frameStyle="width:100%; height:200px; overflow:hidden;" />
	
		<c:set var="idx"	value="${idx+1}" />
	</div>
	</c:if>


	<u:buttonArea>
	
	<u:button titleId="ct.btn.close" alt="닫기" href="javascript:window.close();" />
	</u:buttonArea>

	</td>
	</tr></table>
	
