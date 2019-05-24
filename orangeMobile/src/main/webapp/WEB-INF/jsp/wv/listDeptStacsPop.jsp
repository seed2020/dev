<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<% //일자별 빈시간 확인 %>
function viewTable(code, value){
	$('div.replyrDeptArea').hide();
	$('div #replyrDeptArea'+code).show();
};
           
<% //콤보박스 클릭 %>
function setOptionVal(id , code, value){
	$("input[name='"+id+"']").val(code);
	$("#"+id+"View span").text(value);
	$("#"+id+"Container").hide();
	viewTable(code,value);
};

$(document).ready(function() {
});           
//]]>
</script>
<section>
<!--listsearch S-->
<div class="listsearch">
    <div class="listselect">
    
        <div class="open1" id="replyrDeptContainer" style="display:none">
            <div class="open_in1">
            <div class="open_div">
            <dl>
            	<c:forEach  var="wvsDept" items="${wvSurvReplyDeptList}"  varStatus="status">
            		<c:if test="${wvsDept.replyrDeptId == param.replyrDeptId }"><c:set var="replyrDeptNm" value="${wvsDept.replyrDeptNm}"/></c:if>
            		<c:if test="${status.index > 0 }"><dd class="line"></dd></c:if>
            		<dd class="txt" onclick="setOptionVal('replyrDept',$(this).attr('data-code'),$(this).text());" data-code="${wvsDept.replyrDeptId }">${wvsDept.replyrDeptNm}</dd>
            	</c:forEach>
         	</dl>
            </div>
            </div>
        </div>
        
        <div class="select1">
            <div class="select_in1" onclick="$('#replyrDeptContainer').toggle();">
            <dl>
            <dd class="select_txt1" id="replyrDeptView"><span><c:choose><c:when test="${empty replyrDeptNm && fn:length(wvSurvReplyDeptList) > 0 }">${wvSurvReplyDeptList[0].replyrDeptNm }</c:when><c:otherwise>${replyrDeptNm }</c:otherwise></c:choose></span></dd>
            <dd class="select_btn"></dd>
            </dl>
            </div>
        </div>
    </div>
</div>
<!--//listsearch E-->

<div class="blank5"></div>
<!--s_tablearea S-->
<c:forEach  var="wvsDept" items="${wvSurvReplyDeptList}"  varStatus="status1">
	<div class="s_tablearea replyrDeptArea" id="replyrDeptArea${wvsDept.replyrDeptId }" <c:if test="${wvsDept.replyrDeptId != param.replyrDeptId }">style="display:none;"</c:if>>
	<table class="s_table">
	    <colgroup>
	        <col width=""/>
	        <col width="20%"/>
	    </colgorup>
	    <tbody>
	       	<c:forEach  var="wvsQue" items="${wvsDept.quesExam}"  varStatus="status2">
		        <tr>
		            <td class="sbody_lt">${wvsQue.examOrdr}. ${wvsQue.examDispNm}</td>
		            <td class="sbody_ct">${wvsQue.selectCount}</td>
		        </tr>
	        </c:forEach>
	    </tbody>
	</table>
	</div>
</c:forEach>
<!--//s_tablearea E-->
<div class="blank20"></div>
<div class="btnarea">
	<div class="size">
		<dl>
		<dd class="btn" onclick="$m.dialog.close('listDeptStacsPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
		</dl>
	</div>
</div>
</section>            
  

