<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext(), 
		FrameworkServlet.SERVLET_CONTEXT_PREFIX+"appServlet"); // "appServlet"는 web.xml의 org.springframework.web.servlet.DispatcherServlet의 servlet-name을 지정.
UserVo userVo = LoginSession.getUser(request);
OrCmSvc orCmSvc = (OrCmSvc)wac.getBean("orCmSvc");
Map<String, Object> map = orCmSvc.getUserMap(userVo.getUserUid(), "ko");
request.setAttribute("userMap", map);

if(!"view".equals(request.getAttribute("formBodyMode"))){
	com.innobiz.orange.web.ap.utils.XMLElement formBodyXML = (com.innobiz.orange.web.ap.utils.XMLElement)request.getAttribute("formBodyXML");
	if(formBodyXML != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(formBodyXML.getAttr("body/cont.erpCont"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", formBodyXML.getAttr("body/cont.erpCont"));
		}
	}
}
%><%
/*
	교육수강 신청서 : training
*/
// 구분 목록
String[][] erpGubuns = {
		{"10", "한화제약(그룹)"},
		{"40", "(주) 네츄럴 라이프"},
		{"50", "(주) 네츄럴라이프아시아"},
		{"60", "양지화학(주)"},
		{"80", "웨이더아시아(주)"}
	};
request.setAttribute("erpGubuns", erpGubuns);

//교통보험 환급금
String[][] erpRefundsYn = {
		{"Y", "대상"},
		{"N", "비 대상"}
	};
request.setAttribute("erpRefundsYn", erpRefundsYn);

//형태
String[][] erpShapeYn = {
		{"Y", "합숙"},
		{"N", "비 합숙"}
	};
request.setAttribute("erpShapeYn", erpShapeYn);

// 교육시간
request.setAttribute("strtSi", "5");
request.setAttribute("endSi", "23");
%>
<style type="text/css">
.titlearea {
    width: 100%;
    height: 16px;
    margin: 0 0 9px 0;
}
.titlearea .tit_left .title_s {
    float: left;
    height: 13px;
    font-weight: bold;
    color: #454545;
}
.listarea {
    float: left;
    width: 100%;
    padding: 0;
    margin: 0;
    color: #454545;
}
.listtable {
    width: 100%;
    background: #bfc8d2;
    color: #454545;
}
.listtable tr {
    background: #ffffff;
}
.listtable .head_ct {
	height: 22px;
    text-align: center;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 2px 0 2px;
}
.listtable .head_lt {
    height: 22px;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 0 0 4px;
}
.body_ct {
    height: 22px;
    color: #454545;
    text-align: center;
    line-height: 17px;
    padding: 2px 3px 0 3px;
}
.body_lt {
    height: 22px;
    color: #454545;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}
</style>
<c:set var="useEditor" value="N"/><!-- 에디터 사용여부 -->
<c:if
test="${formBodyMode ne 'view'}">	
<script type="text/javascript">
<!--
var erpGubuns=null;
<%//현재 등록된 id 목록 리턴 %>
function getErpGubuns(refVa){
	if(erpGubuns==null) {
		erpGubuns=new ParamMap();
		<c:forEach items="${erpGubuns}" var="erpGubun">
		erpGubuns.put('${erpGubun[0]}', '${erpGubun[1]}');
		</c:forEach>
	}
	var returnGubun = erpGubuns.get(refVa);
	return returnGubun;
}

<%//현재 등록된 id 목록 리턴 %>
function getChkIds(){
	var arr=[];
	$('#xml-users\\/user input[type="checkbox"]').each(function(){
		if($(this).attr("data-userUid")!=undefined && $(this).attr("data-userUid")!=''){
			arr.push($(this).attr("data-userUid"));
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

<%//사용자 추가 %>
function setUserRows(arr, vas, totalCnt){
	if(arr==null) return;
	if(totalCnt==null) totalCnt=0;
	var $tr, $hiddenTr = $("#xml-users\\/user tbody:first #hiddenTr");
	var html = $hiddenTr[0].outerHTML, erpGubuns;
	arr.each(function(index, vo){
		if(vo.refVa1==null || vo.ein==null) return true; // 구분(회사) or 사번 없으면 추가하지 않는다.
		if(vas==null || !vas.contains(vo.userUid)){
			totalCnt++;
			if(totalCnt>50){
				alert('최대 50명까지 신청 가능합니다.');
				return false;
			}
			erpGubuns=getErpGubuns(vo.refVa1);
			if(erpGubuns==null) return true; // 구분정보가 없으면 continue
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			$tr.attr('id','user');
			$tr.find("input[type='checkbox']").attr("data-userUid", vo.userUid);				
			$tr.find("input[name='erpGubun']").val(vo.refVa1);				
			$tr.find("input[name='erpOrgNm']").val(vo.deptRescNm);
			$tr.find("td#erpOrgNm").text(vo.deptRescNm);
			$tr.find("input[name='erpOrgId']").val(vo.orgId);
			$tr.find("input[name='erpUserUid']").val(vo.userUid);
			$tr.find("input[name='erpEin']").val(vo.ein);
			$tr.find("td#erpEin").text(vo.ein);
			$tr.find("input[name='erpPosit']").val(vo.positNm);
			$tr.find("td#erpPosit").text(vo.positNm);
			$tr.find("input[name='erpName']").val(vo.rescNm);
			$tr.find("td#erpName").text(vo.rescNm);
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
}

<%//사용자 추가 %>
function addUsers(){
	var data = [];
	var vas = getChkIds();
	if(vas!=null){
		$.each(vas, function(index, userUid){
			data.push({userUid:userUid});
		});	
	}
	var totalCnt = data.length;
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			setUserRows(arr, vas, totalCnt);
			<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
		}
	});
};
<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
		<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	}
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(conId, noSelectMsg){
	var arr=[], id, obj;
	conId=conId.replace('/','\\/');
	$("#"+conId+" tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	var validId='xml-users\\/user';
	
	// 사용자 추가여부
	var validCnt=$("#"+validId+" tbody:first tr").not('#headerTr, #hiddenTr').length;
	if(validCnt==0){
		alert("참가자를 추가해 주세요.");
		return false;
	}
	if(validator.validate('xml\\-body')){
		<c:if test="${useEditor eq 'Y'}">editor('erpCont').prepare();</c:if>		
		return true;
	}
	return false;
}
function clearFormEditor(){
	editor('erpCont').clean(); unloadEvent.removeEditor('erpCont');
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

$(document).ready(function() {
	$("#xml-body tbody:first").children().each(function(){
		<%// 행추가 영역 제외하고 uniform 적용%>
		if($(this).attr('id')!='hiddenTr'){
			setJsUniform(this);
		}
	});
	// 신청자 정보 등록
	<c:if test="${empty formBodyXML.getChildList('body/users') }">
		var arr=[];
		var data = {userUid:'${userMap.userUid}', refVa1:'${userMap.refVa1}', deptRescNm:'${userMap.deptRescNm}', orgId:'${userMap.orgId}', ein:'${userMap.ein}', positNm:'${userMap.positNm}', rescNm:'${userMap.rescNm}'};
		arr.push(data);
		setUserRows(arr, null, null);
	</c:if>
});
//-->
</script></c:if>
<c:if
	test="${formBodyMode ne 'view'}">

<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<u:title title="교육 참가자" type="small" alt="교육 참가자">
<u:titleButton title="참가자 추가" onclick="addUsers();" alt="참가자 추가"/><u:titleButton title="선택삭제" onclick="delSelRow('xml-users/user');" alt="선택삭제"/>
</u:title>
<u:set var="first" test="${empty formBodyXML.getAttr('head.typId') }" value="Y" elseValue="N"/>
<div id="xml-head">
	<input type="hidden" name="typId" value="${first eq 'Y' ? erpObject.erpTypId : formBodyXML.getAttr('head.typId')}"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-users/user" colgroup="3%,36%,10%,27%,24%">
	<tr id="headerTr">
		<th class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('xml-users\\/user', this.checked);" value=""/></th>
		<th class="head_ct">소속</th>
		<th class="head_ct">사번</th>
		<th class="head_ct">직위</th>
		<th class="head_ct">성명</th>
	</tr>
	<!-- 등록된 목록 -->
	<c:forEach
		begin="0" end="${fn:length(formBodyXML.getChildList('body/users'))}" var="index" varStatus="status"
		><c:set var="user" value="${status.last ? '' : formBodyXML.getChildList('body/users')[index]}"
		/><u:set test="${status.last}" var="trDisp" value="display:none"
		/><u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="user"
		/>
	<tr id="${trId}" style="${trDisp}">
		<td class="bodybg_ct">	<input type="checkbox" value="" data-userUid="${empty user ? '' :  user.getAttr('erpUserUid')}"
			/><input type="hidden" name="erpGubun" value="${empty user ? '' :  user.getAttr('erpGubun') }"
			/><input type="hidden" name="erpOrgNm" value="${empty user ? '' :  user.getAttr('erpOrgNm')}"
			/><input type="hidden" name="erpOrgId" value="${empty user ? '' :  user.getAttr('erpOrgId')}" data-validation="Y" 
			/><input type="hidden" name="erpUserUid" value="${empty user ? '' :  user.getAttr('erpUserUid')}" 
			/><input type="hidden" name="erpEin" value="${empty user ? '' :  user.getAttr('erpEin')}" 
			/><input type="hidden" name="erpPosit" value="${empty user ? '' :  user.getAttr('erpPosit')}" 
			/><input type="hidden" name="erpName" value="${empty user ? '' :  user.getAttr('erpName')}"
			/>
		</td>
		<td class="bodybg_ct" id="erpOrgNm">${empty user ? '' :  user.getAttr('erpOrgNm')}
		</td>
		<td class="bodybg_ct" id="erpEin">${empty user ? '' :  user.getAttr('erpEin')}</td>
		<td class="bodybg_ct" id="erpPosit">	${empty user ? '' :  user.getAttr('erpPosit')}</td>
		<td class="bodybg_ct" id="erpName">	${empty user ? '' :  user.getAttr('erpName')}</td>
	</tr></c:forEach>
</u:listArea>

<!-- ERP parameter Area -->
<div id="xml-detail" style="display:none;"
><input type="hidden" name="erpStart" value="${first eq 'Y' ? erpObject.erpStart : formBodyXML.getAttr('body/detail.erpStart')}"
/><input type="hidden" name="erpEnd" value="${first eq 'Y' ? erpObject.erpEnd : formBodyXML.getAttr('body/detail.erpEnd')}"
/><input type="hidden" name="erpPurpose" value="${first eq 'Y' ? erpObject.erpPurpose : formBodyXML.getAttr('body/detail.erpPurpose')}"
/></div>

<u:title title="교육내용" type="small" alt="교육내용"/>
<u:set var="bodyHtml" test="${first eq 'Y'}" value="${erpObject.bodyHtml }" elseValue="${_bodyHtml }"
/><c:if test="${useEditor eq 'Y' }">
<u:listArea>
<tr><td class="body_lt"><c:if
	test="${not (formBodyMode eq 'pop' or formBodyCall eq 'ajax')}">
	<div id="xml-cont"><u:editor id="erpCont" width="100%" height="300px" module="ap" padding="2"
		value="${bodyHtml}" noFocus="${not empty param.apvLnGrpId}"
	/></div><div id="xml-contView" class="editor" style="display:none"></div></c:if><c:if
	test="${formBodyMode eq 'pop' or formBodyCall eq 'ajax'}">
	<div id="xml-cont" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 184 : 306}px; padding-top:2px"><u:editor
		id="erpCont" width="100%" height="${empty namoEditorEnable ? 180 : 300}px" module="ap" areaId="erpEditArea" 
		value="${bodyHtml}" namoToolbar="${formBodyMode eq 'pop' ? 'wcPop' : ''}"
	/><div id="erpEditArea"></div></div>
	</c:if>
</td></tr>
</u:listArea>
</c:if><c:if test="${useEditor ne 'Y' }"><u:listArea>
<tr><td class="body_lt editor"><div id="xml-cont" ><input type="hidden" name="erpCont" value="<u:out value="${bodyHtml }" type="value"/>"
/></div>${bodyHtml}</td></tr>
</u:listArea></c:if>
</div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<u:title title="교육참가자" type="small" alt="교육참가자" />	
<u:listArea colgroup="39%,10%,27%,24%">
<tr id="headerTr">
	<th class="head_ct">소속</th>
	<th class="head_ct">사번</th>
	<th class="head_ct">직위</th>
	<th class="head_ct">성명</th>
</tr>
<c:forEach items="${formBodyXML.getChildList('body/users')}" var="user" ><c:if
		test="${not empty user.getAttr('erpOrgId') and not empty user.getAttr('erpName')}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">${user.getAttr('erpOrgNm')}</td>
		<td class="body_ct">${user.getAttr('erpEin')}</td>
		<td class="body_ct">${user.getAttr('erpPosit')}</td>
		<td class="body_ct"><a href="javascript:;" onclick="viewUserPop('${user.getAttr('erpUserUid')}');">${user.getAttr('erpName')}</a></td>
	</tr>	</c:if>
</c:forEach>
</u:listArea>

<u:title title="교육내용" type="small" alt="교육내용"/>

<u:listArea>
<tr><td class="body_lt editor">${formBodyXML.getAttr('body/cont.erpCont')}</td></tr>
</u:listArea>

</c:if>