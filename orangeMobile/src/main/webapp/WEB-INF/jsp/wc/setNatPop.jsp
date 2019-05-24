<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--
<%// 달력 클릭 %>
function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m&popId=viewDocReqCfrmDialog',
	});
}<%// [요청] 저장 %>
function dtlViewRequest(){
	if(validator.validate('setDocViewReqForm')){
		var param = new ParamMap().getData("setDocViewReqForm");
		var win = $m.nav.getWin();
		if(win==null) return;
		win.saveRequest(param);
	}
}
$(document).ready(function() {
});
//-->
</script>
<form id="setDocViewReqForm">
<m:input type="hidden" id="docGrpId" value="${param.docGrpId}" />
<!--entryzone S-->
<div class="entryzone">
    <div class="entryarea" id="cfrmArea">
    <dl>
    	<dd class="etr_bodytit"><u:msg titleId="dm.cols.dtlView.request.tgt" alt="열람요청대상" /></dd>
    	<dd class="etr_input">
			<div class="etr_ipmany" id="reqTgtTypArea">
                <dl>
					<m:check type="radio" id="reqTgtTyp" name="reqTgtTyp" inputId="reqTgtTyp" value="D" areaId="reqTgtTypArea" titleId="cols.dept" />
					<m:check type="radio" id="reqTgtTyp" name="reqTgtTyp" inputId="reqTgtTyp" value="U" areaId="reqTgtTypArea" checked="true" titleId="cols.user" />
                </dl>
            </div>
        </dd>
        <c:if test="${dtlViewPrdAllow eq 'Y' }">
        <dd class="etr_bodytit"><u:msg titleId="dm.cols.dtlView.request.prd" alt="열람요청기간" /></dd>
        <dd class="etr_select">
            <div class="etr_calendar_lt">
                <div class="etr_calendar">
                	<m:input id="readStrtDt" value="${readStrtDt}" type="hidden" />
                    <div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('readStrtDt','{end:\'readEndDt\'}');"><span id="readStrtDt">${readStrtDt }</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'readStrtDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('readStrtDt','{end:\'readEndDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
            <div class="etr_calendar_rt">
                <div class="etr_calendar">
                	<m:input id="readEndDt" value="${readEndDt}" type="hidden" mandatory="Y"/>
                    <div class="etr_calendarin">
                    <dl>
                    <dd class="ctxt" onclick="fnCalendar('readEndDt','{start:\'readStrtDt\'}');"><span id="readEndDt">${readEndDt }</span></dd>
                    <dd class="cdelete" onclick="fnTxtDelete(this,'readEndDt');"></dd>
                    <dd class="cbtn" onclick="fnCalendar('readEndDt','{start:\'readStrtDt\'}');"></dd>
                    </dl>
                    </div>
                </div>
            </div>
        </dd>
        </c:if>
    </dl>
    </div>
</div>
<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl>
		<u:secu auth="W"><dd class="btn" onclick="dtlViewRequest();"><u:msg titleId="dm.btn.dtlView.request" alt="열람요청" /></dd></u:secu>
		<dd class="btn" onclick="$m.dialog.close('viewDocReqCfrmDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>
