<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// [추가] - 참조문서 %>
function openRefDocList(){
	//titleId="ap.doc.refDocNm" alt="참조문서"      titleId="ap.cfg.regRecLst" alt="등록대장"
	dialog.open('listRefDocLstDialog','<u:msg titleId="ap.doc.refDocNm" alt="참조문서" />','./listRefDocLstPop.do?menuId=${param.menuId}&bxId=${param.bxId}${strMnuParam}');
	dialog.onClose("setDocAttchDialog", function(){ dialog.close('listRefDocLstDialog'); });
}<%
// 등록대장 팝업에서 [확인]버튼 클릭 - 첨부 팝업에 데이터 세팅 함 - 참조문서 %>
function setRefApvsToAttchPop(refApvs){
	var $area = $("#docAttchRefDocListArea");<%
	// 기존 참조문서 데이터 제거 %>
	$area.find("tr").not("#titleTr").not("#hiddenTr").remove();
	var $lastTr = $area.find("tr:last"), $newTr, $check;
	var attrs = ["apvNo","docSubj","makrUid","makrNm","cmplDt","secuYn"];
	refApvs.each(function(idx, refApv){
		$newTr = $lastTr.clone();<%// 숨겨진 TR 복사 %>
		$newTr.attr("id","");
		<%// checkbox 의 속성에 데이터 할당, 유니폼 적용 %>
		$check = $newTr.find("input[type='checkbox']");
		attrs.each(function(index, attr){
			$check.attr("data-"+attr, refApv[attr]);
		});
		$check.uniform();
		<%// 각 TD에 해당하는 데이터 입력 %>
		$newTr.find("td:eq(1)").html("<a href=\"javascript:openDocView('"+refApv["apvNo"]+"','"+refApv["secuYn"]+"')\">"+refApv["docSubj"]+"</a>");
		$newTr.find("td:eq(2)").html("<a href=\"javascript:viewUserPop('"+refApv["makrUid"]+"')\">"+refApv["makrNm"]+"</a>");
		$newTr.find("td:eq(3)").text(refApv["cmplDt"]==null ? '' : refApv["cmplDt"]);
		$newTr.show();
		<%// TR 삽입 %>
		$newTr.insertBefore($lastTr);
	});
	dialog.close("listRefDocLstDialog");
}<%
// [삭제] 버튼 클릭 - 참조문서 %>
function deleteRefDoc(){
	var trArr = [], tr, trId;
	$("#docAttchRefDocListArea input:checked").each(function(){
		tr = getParentTag(this, 'tr');
		trId = $(tr).attr('id');
		if(trId!='hiddenTr' && trId!='titleTr'){
			trArr.push(tr);
		}
	});
	trArr.each(function(index, tr){
		$(tr).remove();
	});
}<%
// 참조문서 데이터 모으기 %>
function collectRefApvToDoc(){
	var attrs = ["apvNo","docSubj","makrUid","makrNm","cmplDt","secuYn"], obj, $check, returnArr=[];
	$("#docAttchRefDocListArea input[id!='checkHeader']").not(":last").each(function(){
		obj = {};
		$check = $(this);
		attrs.each(function(index, attr){
			obj[attr] = $check.attr("data-"+attr);
		});
		returnArr.push(obj);
	});
	return returnArr;
}
<%
// [확인] 버튼 클릭 %>
function setAttchData(){
	setFilesToDoc();<%// 팝업에 설정된 파일을 문서에 세팅 - apFiles.tag %>
	var arr = collectRefApvToDoc();
	setRefApvToDoc(arr);<%// setDoc.jsp %>
	dialog.close('setDocAttchDialog');
}
<%
// 파일업로드 Html 로드 %>
function getMultiFileHtml(){
	return $("#docArea #multiFileList").html();
}<%
// 참조문서 - 위로이동, 아래로이동 %>
function moveRefDoc(direction){
	var chks = $("#docAttchRefDocListArea input[type='checkbox'][id!='checkHeader']:checked:visible");
	if(chks.length>0){
		var tr, prevTr;
		if(direction=='up'){
			chks.each(function(){
				tr = $(getParentTag(this,'tr'));
				prevTr = tr.prev();
				if(prevTr.attr('id')!='titleTr' && prevTr.find("input[type='checkbox']:checked").length==0){
					tr.insertBefore(prevTr);
				}
			});
		} else if(direction=='down'){
			var tr, nextTr;
			$(chks.get().reverse()).each(function(){
				tr = $(getParentTag(this,'tr'));
				nextTr = tr.next();
				if(nextTr.is(":visible") && nextTr.find("input[type='checkbox']:checked").length==0){
					tr.insertAfter(nextTr);
				}
			});
		}
	}
}
<%
// onload %>
$(document).ready(function() {<%
	// 참조문서 초기화 - 첨부파일 초기화는 apFiles.tag 에서 함 %>
	var arr = [], refd = "${not empty param.refdBy ? 'Y' : ''}";
	if(refd!='Y'){
		$("#docDataArea #docRefDocArea input").each(function(){
			if(this.name=='refApv'){
				arr.push(JSON.parse(this.value));
			}
		});
		if(arr.length>0) setRefApvsToAttchPop(arr);
	}
});
//-->
</script>

<div style="width:500px">
<form id="setFileAttForm">

<u:tabGroup id="docAttchTab" noBottomBlank="true">
	<u:tab id="docAttchTab" areaId="docAttchFileArea" titleId="ap.doc.attFile" alt="첨부파일" on="true" /><c:if
		test="${ param.sendWithRefDocYn eq 'Y' or
			(	not (param.bxId=='recvBx' or param.bxId=='distBx' or param.bxId=='distRecLst' or param.bxId=='admRecvRecLst' or param.bxId=='admDistRecLst')
			and not (param.bxId=='recvRecLst' and param.showRef!='Y')) }">
	<u:tab id="docAttchTab" areaId="docAttchRefDocArea" titleId="ap.doc.refDocNm" alt="참조문서" /></c:if>
</u:tabGroup>
<u:tabArea
	outerStyle="height:350px; overflow-x:hidden; overflow-y:auto;"
	innerStyle = "NO_INNER_IDV">
<u:cmt

	cmt="파일 첨부 영역" />
<div id="docAttchFileArea" class="resetFont" style="padding:5px 10px 0px 10px;">
<u:apFiles mode="${mode}" id="docAttch" apvNo="${param.apvNo}" attHstNo="${param.attHstNo}"
	module="${apFileModule}" fileTarget="${apFileTarget}" exts="${exts}" extsTyp="${extsTyp}" height="290"/>
</div>
<u:cmt

	cmt="참조 문서 영역" />
<div id="docAttchRefDocArea" class="resetFont" style="padding:5px 10px 0px 10px; display:none;"><c:if
	test="${mode=='set' and empty param.inTrx}">
<div class="front">
<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0">
	<tr><td class="frontico"><u:buttonIcon titleId="cm.btn.up" href="javascript:moveRefDoc('up')" /></td>
		<td class="frontico"><u:buttonIcon titleId="cm.btn.down" href="javascript:moveRefDoc('down')" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.add" alt="추가" onclick="openRefDocList();" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="deleteRefDoc();" /></td>
	</tr>
	</table>
</div>
</div></c:if>
<u:listArea id="docAttchRefDocListArea">
	<tr id="titleTr">
		<td width="5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
			/>" onclick="checkAllCheckbox('docAttchRefDocListArea', this.checked);" value=""/></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></td>
		<td width="30%" class="head_ct"><u:msg titleId="ap.list.cmplDt" alt="완결일시" /></td>
	</tr>
<c:if test="${fn:length(refApOngdBVoList)==1}">
	<tr id="refDocNoDataTr">
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${refApOngdBVoList}" var="refApOngdBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"<c:if
		test="${status.last}"> style="display:none;" id="hiddenTr"</c:if>>
		<td class="bodybg_ct"><input type="checkbox" name="apvNo"<c:if
				test="${status.last}"> class="skipThese"</c:if>
			value="${refApOngdBVo.apvNo
			}" data-apvNo="<u:out value="${refApOngdBVo.apvNo}" type="value"
			/>" data-docSubj="<u:out value="${refApOngdBVo.docSubj}" type="value"
			/>" data-makrUid="<u:out value="${refApOngdBVo.makrUid}" type="value"
			/>" data-makrNm="<u:out value="${refApOngdBVo.makrNm}" type="value"
			/>" data-cmplDt="<u:out value="${refApOngdBVo.cmplDt}" type="value"
			/>" data-secuYn="${
					not empty refApOngdBVo.docPwEnc
					and refApOngdBVo.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}" /></td>
		<td class="body_lt"><c:if
			test="${empty param.refdBy and empty dmUriBase and empty param.winPop}"><a href="javascript:parent.openDocView('${refApOngdBVo.apvNo}','${
					not empty refApOngdBVo.docPwEnc
					and refApOngdBVo.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"><u:out
				value="${refApOngdBVo.docSubj}" /></a></c:if><c:if
			test="${not (empty param.refdBy and empty dmUriBase and empty param.winPop)}"><u:out
				value="${refApOngdBVo.docSubj}" /></c:if></td>
		<td class="body_ct"><c:if
			test="${not empty refApOngdBVo.makrUid}"
			><a href="javascript:viewUserPop('${refApOngdBVo.makrUid}')"><u:out
				value="${refApOngdBVo.makrNm}" type="html" /></a></c:if><c:if
			test="${empty refApOngdBVo.makrUid}"
			><u:out value="${refApOngdBVo.makrNm}" type="html" /></c:if></td>
		<td class="body_ct"><u:out value="${refApOngdBVo.cmplDt}" type="date" /></td>
	</tr>
</c:forEach>
</u:listArea>
</div>

</u:tabArea>

<u:buttonArea>
	<c:if test="${mode == 'set'}">
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="setAttchData();" auth="W" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>