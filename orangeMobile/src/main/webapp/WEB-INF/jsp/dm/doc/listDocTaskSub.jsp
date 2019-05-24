<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="pageNo,noCache"/>
<c:set var="exKeys" value="owner,docHst,version,disuse,saveDisc,keepDdln,seculCd,fld,cls,docNoMod,setSubDoc,bumk,verDel"/><!-- 제외 key -->
<script type="text/javascript">
//<![CDATA[
<% // [팝업] More %>
function subListDetlPop(title,val){
	var html = [];
	html.push('<div class="listarea"><article><div class="listdiv"><div class="list"><dl>');
	html.push('<dd class="tit"><strong>');
	html.push(title);
	html.push('</strong></dd>');
	var body = val.split(',');
	for(var i=0;i<body.length;i++){
		html.push('<dd class="body">');
		html.push(body[i]);
		html.push('</dd>');
	}
	
	html.push('</dl></div></div></article></div>');
	$m.dialog.open({
		id:'subListDetlDialog',
		url:null,
		title:'<u:msg titleId="dm.cols.dtlView" alt="상세보기" />',
		html:html.join(''),
	});
	//container.show();
}<%// 검색 클릭 %>
function searchList(event){
	$m.nav.curr(event, '/dm/doc/listDocTaskSub.do?'+$('#listDocTaskForm').serialize());
}<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#listDocTaskForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}
<%// 달력 클릭 %>
function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}
function fnUnfold(){
	if($('#unfold').attr("class") == "unfoldbtn")
		$('#unfold').attr("class", "unfoldbtn_on");
	else
		$('#unfold').attr("class", "unfoldbtn");
	$('.unfoldArea').toggle(); 
}
var holdHide = false;
$(document).ready(function() {
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("#listsearch #searchCat").hide();
	});
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
	
	var unfoldUsed = false;
	$('#dtlSrchArea').find('input').each(function(){
		if($(this).val() != '') {
			unfoldUsed = true;
			return true;
		}
	});
	if(unfoldUsed) fnUnfold();
});

//]]>
</script>
<!-- 검색조건 -->
<form id="listDocTaskForm" name="listDocTaskForm" action="/dm/doc/listDocTaskFrm.do" >
<c:if test="${!empty paramEntryList}">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<m:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
</c:if>
<input type="hidden" name="srchCd" value="${param.srchCd}" />
<input type="hidden" id="durCat" name="durCat" value="taskDt"/>
<c:if test="${empty param.single || param.single eq 'N'}">
<div class="listsearch" id="listsearch">
	<div class="listselect" id="srchCdContainer">
		<c:set var="srchNm" value=""/>
		<div class="open4" id="srchCdOpen" style="display:none">
        <div class="open_in2">
			<div class="open_div">
			<dl>
			<dd class="txt" onclick="setSelOptions('srchCd',$(this).attr('data-code'),$(this).text());" data-code=""><u:msg var="noSelect" titleId="cm.option.all" alt="전체" />${noSelect }</dd>
			<c:if test="${empty param.srchCd }"><c:set var="srchNm" value="${noSelect }"/></c:if>
			<c:forEach var="colmVo" items="${taskList }" varStatus="status">
				<c:if test="${!fn:contains(exKeys,colmVo.va) }">
					<dd class="line"></dd>
					<c:if test="${colmVo.va eq param.srchCd }"><c:set var="srchNm" value="${colmVo.msg }"/></c:if>
					<dd class="txt" onclick="setSelOptions('srchCd',$(this).attr('data-code'),$(this).text());" data-code="${colmVo.va }">${colmVo.msg }</dd>
				</c:if>
			</c:forEach>
			</dl>
			</div>
			</div>
		</div>
                    
		<div class="select4">
            <div class="select_in2" onclick="$('#srchCdContainer #srchCdOpen').toggle();">
            <dl>
            <dd class="select_txt2" id="selectView"><span>${srchNm }</span></dd>
            <dd class="select_btn"></dd>
            </dl>
            </div>
        </div>
        <div class="searchbtn3" onclick="searchList(event);"><span></span></div>
	</div>
	<div class="unfoldbtn" onclick="fnUnfold();" id="unfold"></div>
</div>

<div class="entryzone unfoldArea" id="dtlSrchArea" style="display:none;">
      <div class="entryarea">
      <dl>
      	<dd class="etr_blank"></dd>
      	<dd class="etr_select">
		  	  <div class="etr_calendar_lt">
			      <div class="etr_calendar">
				  <input id="durStrtDt" name="durStrtDt" value="${param.durStrtDt}" type="hidden" />
				  <div class="etr_calendarin">
					   <dl>
					   <dd class="ctxt" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"><span id="durStrtDt">${param.durStrtDt}</span></dd>
					   <dd class="cdelete" onclick="fnTxtDelete(this,'durStrtDt');"></dd>
					   <dd class="cbtn" onclick="fnCalendar('durStrtDt','{end:\'durEndDt\'}');"></dd>
					   </dl>
				   </div>
				</div>
			</div>
		
	        <div class="etr_calendar_rt">
	            <div class="etr_calendar">
	            	<input id="durEndDt" name="durEndDt" value="${param.durEndDt}" type="hidden" />
	            	<div class="etr_calendarin">
	                    <dl>
	                    <dd class="ctxt" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"><span id="durEndDt">${param.durEndDt}</span></dd>
	                    <dd class="cdelete" onclick="fnTxtDelete(this,'durEndDt');"></dd>
	                    <dd class="cbtn" onclick="fnCalendar('durEndDt','{start:\'durStrtDt\'}');"></dd>
                    </dl>
                    </div>
	            </div>
	        </div>
	    </dd>
 		</dl>
    </div> 
</div>
</c:if>
</form>

<section>
<div class="listarea" id="listArea">
<article><c:forEach var="list" items="${dmTaskHVoList}" varStatus="status">
	<div class="listdiv" >
		<div class="list">
		<dl>
			<dd class="tit"><a href="javascript:$m.user.viewUserPop('${list.userUid}');"><u:out value="${list.userNm }" /></a>
			</dd><dd class="name"><u:msg titleId="dm.cols.task.${list.taskCd }"/>ㅣ <u:out value="${list.taskDt }" type="longdate"
			/><u:convertMap var="detlInfoMap" srcId="${list.docGrpId}_${list.taskDt }" attId="taskDetl" type="html" 
			/><c:forEach var="subList" items="${detlInfoMap }" varStatus="detlStatus"
			><div class="ellipsis" title="${subList.value }"><u:msg var="msgTitle" titleId="${subList.msgId }"
			/>${msgTitle }<c:set var="subListCnt" value="${fn:length(fn:split(subList.value,',')) }"/><c:if test="${subListCnt>1 }">(${subListCnt }) : <a href="javascript:;" onclick="subListDetlPop('${msgTitle }','${subList.value }');">${subList.value }</a></c:if
			><c:if test="${subListCnt==1 }"> : ${subList.value }</c:if></div></c:forEach
			></dd>
		</dl>
		</div>
	</div></c:forEach><c:if
	
		test="${recodeCount == 0}">
	<div class="listdiv_nodata" >
		<dl>
		<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
		</dl>
	</div></c:if>
</article>
</div>

<m:pagination />

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>