<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n"); %>
<script type="text/javascript">
//<![CDATA[
<%
// Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#searchForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}
var durCatArrs = [];
function setDurCatPop(){
	if(durCatArrs.length==0) return;
	var selected=$('#searchForm #durCat').val();
	if(selected=='') selected=durCatArrs[0].cd;
	$m.dialog.openSelect({id:'durCat', cdList:durCatArrs, selected:selected}, function(selObj){
		if(selObj!=null){
			$('#searchForm #durCat').val(selObj.cd);
		}
	});
}
<% // 날짜옵션 초기화 %>
function durCatListInit(){
	<c:if test="${!empty durCatList }">
	<c:forEach var="durCat" items="${durCatList }" varStatus="status"	>
	durCatArrs.push({cd:'${durCat[0] }', nm:'${durCat[1] }'});
	</c:forEach>
	</c:if>
};
<%// 달력 클릭 %>
function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}
<%// 검색 클릭 %>
function searchList(event){
	$m.nav.curr(event, '/wf/works/${listPage}.do?'+$('#searchForm').serialize());
}
$(document).ready(function() {
	durCatListInit();
});
//]]>
</script>

<form id="searchForm" name="searchForm" action="${_uri}" onsubmit="searchList(event);" >
<input type="hidden" name="menuId" value="${menuId}" />
<m:input type="hidden" id="formNo" value="${param.formNo}" /><c:if 
test="${not empty param.pageRowCnt}">
<m:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>

<c:if test="${!empty schCatList }">	
<div class="listsearch" id="listsearch">
	<div class="listselect" id="schCatContainer">
		<c:set var="schCatNm"/>
		<div class="open1" id="schCatOpen" style="display:none">
			<div class="open_in1">
				<div class="open_div">
				<dl><c:forEach var="schCat" items="${schCatList }" varStatus="status"
					><c:if test="${!status.first }"><dd class="line"></dd></c:if
					><dd class="txt" onclick="setSelOptions('schCat',$(this).attr('data-code'),$(this).text());" data-code="${schCat[0] }">${schCat[1] }</dd>
					<c:if test="${empty param.schCat && status.first || param.schCat eq schCat[0]}"><c:set var="schCatNm" value="${schCat[1] }"
					/><input type="hidden" name="schCat" value="${schCat[0]}" /></c:if>
					</c:forEach>
				</dl>
				</div>
			</div>
		</div>
		
		<div class="select1">
			<div class="select_in1" onclick="$('#schCatContainer #schCatOpen').toggle();">
			<dl>
				<dd class="select_txt1" id="selectView"><span>${schCatNm }</span></dd>
				<dd class="select_btn"></dd>
			</dl>
			</div>
		</div>
		
	</div>
	<div class="listinput">
		<div class="input1">
		<dl>
			<dd class="input_left"></dd>
			<dd class="input_input"><input type="text" class="input_ip" name="schWord" maxlength="30" value="<u:out value="${param.schWord}" type="value" />" /></dd>
			<dd class="input_btn" onclick="searchList(event);"><div class="search"></div></dd>
		</dl>
		</div>
	</div>
</div>
</c:if><c:if test="${!empty durCatList}"
><div class="entryzone" id="durCatArea">
      <div class="entryarea">
      <dl>
		  <dd class="etr_blank"></dd>
		  <dd class="unfoldbtn"></dd>
	      <dd class="etr_select">
	      	<u:set var="paramDurCat" test="${empty param.durCat }" value="${durCatList[0][0] }" elseValue="${param.durCat }"/>
      		<input type="hidden" id="durCat" name="durCat" value="${paramDurCat }" class="notChkCls"/>
      		  <div class="unfoldbtn" style="right:inherit;" onclick="setDurCatPop();"></div>
		  	  <div class="etr_calendar_lt" style="right:46%;left:30px;">
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
		
	        <div class="etr_calendar_rt" style="left:53%;">
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
    	<dd class="etr_blank1"></dd>
 		</dl>
    </div> 
</div></c:if>
</form>