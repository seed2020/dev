<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 다운로드 검색 %>
function searchForDownload(){
	var param = new ParamMap().getData("setDownSearchForm").toObject();
	callAjax("./getDownTgtAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
		if(data.message != null) alert(data.message);
		else {
			var area = $("#setDownProcForm");
			if(data.tgtCnt!=null){
				area.show();
				area.find("#tgtCntView").text(data.tgtCnt);
				
				area.find("input[name='tgtCnt']").val(data.tgtCnt);
				area.find("input[name='durStrtDt']").val(param.durStrtDt);
				area.find("input[name='durEndDt']").val(param.durEndDt);
				
				if(data.tgtCnt!='0'){
					$("#startDownloadBtn").show();
				} else {
					$("#startDownloadBtn").hide();
				}
			}
		}
	});
}<%
// 다운로드 시작 %>
function startDownload(){
	var param = new ParamMap().getData("setDownProcForm").toObject();
	callAjax("./processDownAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
		if(data.message != null) alert(data.message);
		else {
			$("#downSearchBtn").hide();
			$("#startDownloadBtn").hide();
			window.setTimeout("prepareProgress()", 400);
		}
	});
}
function prepareProgress(){
	$("#divProgP").show();
	$("#stopDownloadBtn").show();
	showProgress();
}<%
// 진행율 보이기 타임아웃 %>
var progressTimeout = null;<%
//진행율 보이기 %>
function showProgress(initFlag){
	if(progressTimeout!=null){
		window.clearTimeout(progressTimeout);
		progressTimeout = null;
	}
	callAjax("./getDownProcCntAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", null, function(data){
		if(data.message != null) alert(data.message);
		else {
			var tgtCnt  = parseInt(data.tgtCnt);
			var cmplCnt = parseInt(data.cmplCnt);
			var errCnt  = parseInt(data.errCnt);
			
			var cDiv = $("#divProgC");
			if(tgtCnt==0){
				cDiv.css("width","0%");
				if(data.stop == null){
					window.setTimeout("showProgress()", 200);
				}
			} else if(tgtCnt == -1){
				
			} else {
				prcCnt = cmplCnt + errCnt;
				if(prcCnt == tgtCnt){
					cDiv.css("width","100%");
					if(initFlag==null){
						$("#stopDownloadBtn").hide();
						$("#initDownloadBtn").show();
						alert("success:"+cmplCnt+"   error:"+errCnt);
					}
				} else {
					var w100 = parseInt(100 * prcCnt / tgtCnt);
					if(w100==100) w100 = 99;
					cDiv.css("width",w100+"%");
					
					if(data.stop == null){
						progressTimeout = window.setTimeout("showProgress()", 200);
					}
				}
			}
		}
	},null,null,true);
}<%
// 초기화 버튼 %>
function initDownload(){
	if(progressTimeout!=null) {
		window.clearTimeout(progressTimeout);
		progressTimeout = null;
	}
	callAjax("./processDownDropAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", null, function(data){
		openDownloadPop();
	});
}<%
// 중지 버튼 %>
function stopDownload(){
	if(progressTimeout!=null) {
		window.clearTimeout(progressTimeout);
		progressTimeout = null;
	}
	callAjax("./processDownStopAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", null, function(data){
		$("#stopDownloadBtn").hide();
		$("#initDownloadBtn").show();
	});
}
$(document).ready(function() {
	var initFnc = "${not empty prop.domain ? 'showProgress(true)' : ''}";
	if(initFnc!='') eval(initFnc);
});
// -->
</script>
<div style="width:600px">


<% // 검색영역 %>
<u:searchArea>
<form name="setDownSearchForm" id="setDownSearchForm" >
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="search_tit" style="width:60px;"><u:msg titleId="ap.doc.makDd" alt="기안일자" /></td>
			<td><u:calendar id="durStrtDt2" name="durStrtDt" option="{end:'durEndDt2'}" titleId="cols.strtYmd" value="${prop.durStrtDt}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt2" name="durEndDt" option="{start:'durStrtDt2'}" titleId="cols.endYmd" value="${prop.durEndDt}" mandatory="Y" /></td>
			</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" id="downSearchBtn" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:searchForDownload();" style="${not empty prop.domain ? 'display:none' : ''}"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</form>
</u:searchArea>

<u:blank />

<div style="height:130px;">
<form id="setDownProcForm" style="${empty prop.domain ? 'display:none' : ''}">
<input name="durStrtDt" type="hidden">
<input name="durEndDt" type="hidden">
<input name="tgtCnt" type="hidden">
<u:listArea colgroup="20%,80%">
	<tr>
	<td class="head_ct"><u:msg titleId="ap.cols.tgtCnt" alt="대상 건수" /></td>
	<td class="body_lt" id="tgtCntView">${prop.tgtCnt}</td>
	</tr>
	<tr>
	<td class="head_ct"><u:msg titleId="dm.jsp.stor.title" alt="저장소" /></td>
	<td class="body_lt">${prop.baseDir}<u:input titleId="dm.jsp.stor.title" id="storeDir" value="${prop.storeDir}"/></td>
	</tr>
	<tr>
	<td class="head_ct"><u:msg titleId="cols.ext" alt="확장자" /></td>
	<td class="body_lt"><table cellspacing="0" cellpadding="0" border="0">
		<tr><u:radio name="fileType" value="html" title="HTML" checked="${prop.fileType ne 'mhtml'}"
			/><u:radio name="fileType" value="mhtml" title="MHTML" checked="${prop.fileType eq 'mhtml'}"
			/><u:radio name="fileType" value="htmlImage" title="HTML + IMAGE" checked="${prop.fileType eq 'htmlImage'}"
			/><u:radio name="fileType" value="htmlImageFile" title="HTML + IMAGE + FILE" checked="${prop.fileType eq 'htmlImageFile'}"
			/></tr>
		</table></td>
	</tr>
</u:listArea>

<u:listArea colgroup="20%,80%">
	<tr>
	<td class="head_ct"><u:msg titleId="ap.cols.prcStat" alt="진행 상태" /></td>
	<td style="padding:5px;"><div id="divProgP" style="width: 100%; background-color: lightgrey; height:16px; ${empty prop.domain ? 'display:none' : ''}"><div id="divProgC" style="width:0%; background-color: green; height:16px;"></div></div></td>
	</tr>
</u:listArea>
</form>

</div>

<u:buttonArea>
	<u:button titleId="cm.btn.init" onclick="initDownload();" alt="초기화" auth="A" id="initDownloadBtn" style="${empty prop.domain ? 'display:none' : empty prop.stop ? 'display:none' : ''}" />
	<u:button titleId="cm.btn.stop" onclick="stopDownload();" alt="중지" auth="A" id="stopDownloadBtn" style="${empty prop.domain ? 'display:none' : not empty prop.stop ? 'display:none' : ''}" />
	<u:button titleId="cm.btn.save" onclick="startDownload();" alt="저장" auth="A" id="startDownloadBtn" style="display:none" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>


</div>